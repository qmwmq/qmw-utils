package io.github.qmwmq.utils;


import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtils<T> {

    // todo
    public static <T> T newInstance(Type type) {
        if (type instanceof ParameterizedType t) {
            Type rawType = t.getRawType();
            if (rawType instanceof Class<?> clazz) {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

}
