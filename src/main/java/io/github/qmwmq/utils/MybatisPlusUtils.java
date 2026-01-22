package io.github.qmwmq.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;
import java.util.Objects;

/**
 * MybatisPlus 相关工具
 */
public class MybatisPlusUtils {

    /**
     * 屏蔽默认构造器
     */
    private MybatisPlusUtils() {
    }

    /**
     * 将 wrapper 转换为 sql
     *
     * @param wrapper wrapper
     * @return sql
     */
    public static String getSql(LambdaQueryWrapper<?> wrapper) {
        String columns = wrapper.getSqlSelect();
        if (StringUtils.isBlank(columns))
            columns = "*";
        String sql = wrapper.getCustomSqlSegment();

        TableInfo tableInfo = TableInfoHelper.getTableInfo(wrapper.getEntityClass());
        String tableName = tableInfo.getTableName();

        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        for (Map.Entry<String, Object> entry : paramNameValuePairs.entrySet()) {
            Object value = entry.getValue();
            sql = switch (value) {
                case null -> replaceValue(sql, entry.getKey(), "null");
                case String v -> replaceValue(sql, entry.getKey(), StringEscape.escapeString(v));
                case Enum<?> v -> replaceValue(sql, entry.getKey(), StringEscape.escapeString(v.name()));
                default -> replaceValue(sql, entry.getKey(), value.toString());
            };
        }
        return "select " + columns + " from " + tableName + " " + sql;
    }

    private static String replaceValue(String sql, String key, String value) {
        return sql.replace("#{ew.paramNameValuePairs." + key + "}", value);
    }

    public static <T> String getColumn(SFunction<T, ?> fn) {
        LambdaMeta meta = LambdaUtils.extract(fn);
        Class<?> clazz = meta.getInstantiatedClass();
        String property = PropertyNamer.methodToProperty(meta.getImplMethodName());
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (Objects.equals(property, tableInfo.getKeyProperty()))
            return tableInfo.getKeyColumn();
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList())
            if (Objects.equals(property, fieldInfo.getProperty()))
                return fieldInfo.getColumn();
        throw new RuntimeException("未找到字段：" + property);
    }

}
