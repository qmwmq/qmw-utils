package io.github.qmwmq.utils;

public class NumberUtils {

    public Long parseLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return null;
        }
    }

    // 两个非负整数映射到唯一非负整数，Cantor 配对函数
    public static long uniqueNumber(long a, long b) {
        if (a < 0 || b < 0)
            throw new RuntimeException("both number must be non-negative");
        return (a + b) * (a + b + 1) / 2 + b;
    }

}
