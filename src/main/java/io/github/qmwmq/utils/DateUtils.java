package io.github.qmwmq.utils;

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
        for (String pattern : patterns) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception e) {
                continue;
            }
        }
        // 时间戳11位或13位
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public static LocalDate parseDate(Object o) {
        LocalDateTime localDateTime = parseDateTime(o);
        return localDateTime == null ? null : localDateTime.toLocalDate();
    }

}
