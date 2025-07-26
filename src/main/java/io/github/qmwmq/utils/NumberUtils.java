package io.github.qmwmq.utils;

import java.util.HashSet;
import java.util.Set;

public class NumberUtils {

    // 两个非负整数映射到唯一非负整数，Cantor 配对函数
    public static long uniqueNumber(long a, long b) {
        if (a < 0 || b < 0)
            throw new RuntimeException("both number must be non-negative");
        return (a + b) * (a + b + 1) / 2 + b;
    }

    public static void main(String[] args) {
        Set<Long> ids = new HashSet<>();

        long max = 0L;
        for (int a = 0; a < 100000; a++) {
            for (int b = 0; b < 100000; b++) {
                max = Math.max(max, uniqueNumber(a, b));
            }
        }

//        System.out.println(ids);
        System.out.println(max);
        System.out.println("end");

    }

}
