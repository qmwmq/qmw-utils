package io.github.qmwmq.utils;

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

}
