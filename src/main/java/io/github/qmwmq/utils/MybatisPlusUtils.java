package io.github.qmwmq.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;

import java.util.Map;

/**
 * MybatisPlus相关工具
 */
public class MybatisPlusUtils {

    /**
     * 屏蔽默认构造器
     */
    private MybatisPlusUtils() {
    }

    /**
     * 将wrapper转换为sql
     *
     * @param wrapper wrapper
     * @return sql
     */
    public static String getCustomSqlSegment(LambdaQueryWrapper<?> wrapper) {
        String sql = wrapper.getCustomSqlSegment();

        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();

        for (Map.Entry<String, Object> entry : paramNameValuePairs.entrySet()) {
            Object value = entry.getValue();
            sql = switch (value) {
                case null -> replaceValue(sql, entry.getKey(), "null");
                case String v -> replaceValue(sql, entry.getKey(), StringEscape.escapeString(v));
                case Enum<?> v -> replaceValue(sql, entry.getKey(), StringEscape.escapeString(v.name()));
                default -> replaceValue(sql, entry.getKey(), StringEscape.escapeString(value.toString()));
            };
        }
        return sql;
    }

    private static String replaceValue(String sql, String key, String value) {
        return sql.replace("#{ew.paramNameValuePairs." + key + "}", value);
    }

}
