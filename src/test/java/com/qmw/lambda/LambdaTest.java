package com.qmw.lambda;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaTest {

    public static void main(String[] args) {

        IMathOperation add = Integer::sum;
        add.operate(1, 2);

        int max = getMax(() -> 1);

        Function<Integer, ?> a = Object::hashCode;

        System.out.println(a.apply(2));

        Comparator<String> comparator = (o1, o2) -> o2.length() - o1.length();

        String method = method(
                Integer::parseInt,
                num -> String.valueOf(num + 10)
        );

        System.out.println(method);

    }

    public static int getMax(Supplier<Integer> sup) {
        return sup.get();
    }

    public static String method(Function<String, Integer> a, Function<Integer, String> b) {
        return a.andThen(b).apply("10"); // a的运行结果作为入参丢给b
    }

}
