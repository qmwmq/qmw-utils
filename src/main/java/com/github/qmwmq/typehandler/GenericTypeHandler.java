package com.github.qmwmq.typehandler;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.databind.JavaType;
import com.github.qmwmq.utils.JacksonUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GenericTypeHandler extends BaseTypeHandler<Object> {

    private static final Map<String, Map<String, JavaType>> genericTypeMap = new ConcurrentHashMap<>();

    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) {
        ps.setString(i, parameter == null ? "{}" : JacksonUtils.toJSONString(parameter));
    }

    @Override
    @SneakyThrows
    public Object getNullableResult(ResultSet rs, String columnName) {
        if (rs.getString(columnName) == null)
            return null;
        return JacksonUtils.getObjectMapper().readValue(rs.getString(columnName), getGenericType(rs.getMetaData(), rs.findColumn(columnName)));
    }

    @Override
    @SneakyThrows
    public Object getNullableResult(ResultSet rs, int columnIndex) {
        if (rs.getString(columnIndex) == null)
            return null;
        return JacksonUtils.getObjectMapper().readValue(rs.getString(columnIndex), getGenericType(rs.getMetaData(), columnIndex));
    }

    @Override
    @SneakyThrows
    public Object getNullableResult(CallableStatement cs, int columnIndex) {
        if (cs.getString(columnIndex) == null)
            return null;
        return JacksonUtils.getObjectMapper().readValue(cs.getString(columnIndex), getGenericType(cs.getMetaData(), columnIndex));
    }

    private static JavaType getGenericType(ResultSetMetaData metaData, int columnIndex) throws SQLException {
        String tableName = metaData.getTableName(columnIndex);
        String columnName = metaData.getColumnName(columnIndex);

        return genericTypeMap.computeIfAbsent(
                tableName,
                key -> TableInfoHelper.getTableInfo(key)
                        .getFieldList().stream()
                        .filter(e ->
                                Arrays.stream(e.getField().getDeclaredAnnotations()).anyMatch(a -> {
                                    if (a instanceof TableField)
                                        return GenericTypeHandler.class == ((TableField) a).typeHandler();
                                    return false;
                                })
                        )
                        .collect(Collectors.toMap(
                                TableFieldInfo::getColumn,
                                tableFieldInfo -> {
//                                    Field field = tableFieldInfo.getField();
//                                    Class<?> type = field.getType();
//                                    Type genericType = field.getGenericType();

//                                    TableField annotation = field.getDeclaredAnnotation(TableField.class);
//                                    if (annotation != null)
//                                        System.out.println(annotation.typeHandler().equals(GenericTypeHandler.class));
//                                    System.out.println("type=" + type);
//                                    System.out.println("genericType=" + genericType);
//                                    if (genericType instanceof ParameterizedType pType) {
//                                        Type[] actualTypeArguments = pType.getActualTypeArguments();
//                                        System.out.println("actualTypeArguments=" + Arrays.toString(actualTypeArguments));
//                                    }
//                                    System.out.println();

                                    return JacksonUtils.getTypeFactory().constructType(tableFieldInfo.getField().getGenericType());
                                },
                                (v1, v2) -> v1,
                                ConcurrentHashMap::new
                        ))
        ).getOrDefault(columnName, JacksonUtils.getTypeFactory().constructType(Object.class));
    }

}
