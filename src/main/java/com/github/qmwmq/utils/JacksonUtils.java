package com.github.qmwmq.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson相关工具类
 */
public class JacksonUtils {

    // https://blog.csdn.net/xueyijin/article/details/128892398

    private JacksonUtils() {
    }

    // 基础ObjectMapper
    @Getter
    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true) // 允许\r\n等
            .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true) // 允许带反斜杠\
            .configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS, true) // 允许数字以0开头
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) // 序列化无成员变量的对象会报错，这样配置直接序列化成{}
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 反序列化时忽略不存在的属性
            .serializationInclusion(JsonInclude.Include.NON_NULL) // 忽略null值的key
            .build()
            .registerModule(
                    new SimpleModule()
                            .addSerializer(BigDecimal.class, new JsonSerializer<>() {
                                @Override
                                public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider provider) throws IOException {
                                    generator.writeString(value.toPlainString());
                                }
                            })
                            .addSerializer(LocalDateTime.class, new JsonSerializer<>() {
                                @Override
                                public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
                                    generator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(value));
                                }
                            })
                            .addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
                                @Override
                                public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                                    String text = jsonParser.getText();
                                    return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                }
                            })
                            .addSerializer(LocalDate.class, new JsonSerializer<>() {
                                @Override
                                public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
                                    generator.writeString(value.toString());
                                }
                            })
                            .addDeserializer(LocalDate.class, new JsonDeserializer<>() {
                                @Override
                                public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                                    String text = jsonParser.getText();
                                    return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                }
                            })
            );

    @Getter
    private static final TypeFactory typeFactory = objectMapper.getTypeFactory();

    /**
     * 转换为string
     *
     * @param o            入参
     * @param prettyFormat 是否格式化
     * @return 出参
     */
    @SneakyThrows
    public static String toJSONString(Object o, boolean prettyFormat) {
        ObjectWriter writer;
        if (prettyFormat)
            writer = objectMapper.writerWithDefaultPrettyPrinter();
        else
            writer = objectMapper.writer();
        return writer.writeValueAsString(o);
    }

    /**
     * 转换为string
     *
     * @param o 入参
     * @return 出参
     */
    public static String toJSONString(Object o) {
        return toJSONString(o, false);
    }

    /**
     * 解析为map
     *
     * @param o       入参
     * @param mapType map的类型
     * @return 出参
     */
    @SneakyThrows
    public static <K, V> Map<K, V> toMap(Object o, MapType mapType) {
        return objectMapper.readValue(o.toString(), mapType);
    }

    /**
     * 解析为map
     *
     * @param o 入参
     * @return 出参
     */
    public static Map<String, Object> toMap(Object o) {
        return toMap(o, typeFactory.constructMapType(HashMap.class, String.class, Object.class));
    }

    /**
     * 解析为对象
     *
     * @param o     入参
     * @param clazz 对象类
     * @param <T>   对象类泛型
     * @return 出参
     */
    @SneakyThrows
    public static <T> T toJavaObject(Object o, Class<T> clazz) {
        if (StringUtils.isBlank(o))
            o = "{}";
        return objectMapper.readValue(o.toString(), clazz);
    }

    /**
     * 解析为list
     *
     * @param o 入参
     * @return 出参
     */
    @SneakyThrows
    public static <K, V> List<Map<K, V>> toList(Object o, MapType mapType) {
        if (StringUtils.isBlank(o))
            o = "[]";
        // 构建list的泛型
        CollectionType listType = typeFactory.constructCollectionType(List.class, mapType);
        return objectMapper.readValue(o.toString(), listType);
    }

    /**
     * 解析为list
     *
     * @param o   入参
     * @param <K> Map的key类型
     * @param <V> Map的value类型
     * @return 出参
     */
    public static <K, V> List<Map<K, V>> toList(Object o) {
        // 构建map类型和key、value的泛型
        return toList(o, typeFactory.constructMapType(HashMap.class, String.class, Object.class));
    }

    /**
     * 解析为list
     *
     * @param o     入参
     * @param clazz list元素类
     * @param <T>   list元素类泛型
     * @return 出参
     */
    @SneakyThrows
    public static <T> List<T> toList(Object o, Class<T> clazz) {
        if (StringUtils.isBlank(o))
            o = "[]";
        CollectionType listType = typeFactory.constructCollectionType(List.class, clazz);
        return objectMapper.readValue(o.toString(), listType);
    }

}
