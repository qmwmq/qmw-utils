package io.github.qmwmq.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;

import java.util.Map;

public class MybatisPlusUtils {

    public static String getCustomSqlSegment(LambdaQueryWrapper<?> wrapper) {
        String sql = wrapper.getCustomSqlSegment();

        Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();

        for (Map.Entry<String, Object> entry : paramNameValuePairs.entrySet()) {
            Object value = entry.getValue();
            if (value == null)
                sql = sql.replace("#{ew.paramNameValuePairs." + entry.getKey() + "}", "null");
            else if (value instanceof String)
                sql = sql.replace("#{ew.paramNameValuePairs." + entry.getKey() + "}", StringEscape.escapeString(value.toString()));
            else
                sql = sql.replace("#{ew.paramNameValuePairs." + entry.getKey() + "}", value.toString());
        }
        return sql;
    }

}
