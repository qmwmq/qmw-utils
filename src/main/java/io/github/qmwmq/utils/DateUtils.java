package io.github.qmwmq.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 屏蔽默认构造器
     */
    private DateUtils() {
    }

    private static final String[] patterns = {
            "yyyy-M-d HH:mm",
            "yyyy-M-d HH:mm:ss",
            "yyyy-M-d HH:mm:ss.S",
            "yyyy-M-d HH:mm:ss.SS",
            "yyyy-M-d HH:mm:ss.SSS",

            "yyyy/M/d HH:mm",
            "yyyy/M/d HH:mm:ss",
            "yyyy/M/d HH:mm:ss.S",
            "yyyy/M/d HH:mm:ss.SS",
            "yyyy/M/d HH:mm:ss.SSS",

            "yyyy-M-d",
            "yyyy/M/d",
            "yyyy年M月d日",
            "yyyyMMdd",
    };

    public static LocalDateTime parseDateTime(Object o) {
        String str = StringUtils.ifBlank(o, "").strip();
        // 日期时间
        for (String pattern : patterns) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception _) {
            }
        }
        // 日期
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
            } catch (Exception _) {
            }
        }
        // 13位时间戳
        try {
            return new Timestamp(Long.parseLong(str)).toLocalDateTime();
        } catch (Exception _) {
        }
        return null;
    }

    public static LocalDate parseDate(Object o) {
        LocalDateTime localDateTime = parseDateTime(o);
        return localDateTime == null ? null : localDateTime.toLocalDate();
    }

}
