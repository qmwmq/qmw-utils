package com.qmw.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
            list.add(String.valueOf(i));

        long t1 = System.currentTimeMillis();
        list.forEach(System.out::println);
        long t2 = System.currentTimeMillis();
        list.parallelStream().forEach(System.out::println);
        long t3 = System.currentTimeMillis();

        System.out.println();
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);

        // 跳过
        list.stream().skip(900).forEach(System.out::println);

        // 连接多个流
        Stream.concat(list.stream(), list.parallelStream()).forEach(System.out::println);

    }

}
