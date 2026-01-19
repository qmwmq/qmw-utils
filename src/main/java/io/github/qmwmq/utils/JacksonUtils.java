package io.github.qmwmq.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.type.TypeFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JacksonUtils {

    @Getter
    private static final JsonMapper jsonMapper = JsonMapper.builder()
            // 序列化配置
            .changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_NULL)) // 忽略 null 值的 key
            .addModule(new SimpleModule()
                    .addSerializer(BigDecimal.class, new ValueSerializer<>() {
                        @Override
                        public void serialize(BigDecimal value, JsonGenerator gen, SerializationContext provider) {
                            gen.writeString(value.toPlainString());
                        }
                    })
                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            )
            .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS) // 允许数字以0开头
            .build();
    @Getter
    private static final TypeFactory typeFactory = jsonMapper.getTypeFactory();

    public static String toJSONString(Object o, boolean prettyFormat) {
        if (prettyFormat)
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        else
            return jsonMapper.writer().writeValueAsString(o);
    }

    public static String toJSONString(Object o) {
        return toJSONString(o, false);
    }

    static void main() {
        User user = new User().setName("qmwmq").setAge(18).setTime(LocalDateTime.now()).setDate(LocalDate.now());
        System.out.println(JacksonUtils.toJSONString(user, true));
        System.out.println(JacksonUtils.toJSONString(new Object(), true));

        String u = "{" +
                "  \"age\" : \"0018\"," +
                "  \"name\" : \"qmwmq\"," +
                "  \"aaa\" : \"qmwmq\"," +
                "  \"time\" : \"2026-01-19 23:58:53\"" +
                "}";

        System.out.println(jsonMapper.readValue(u, User.class));

    }

    @Data
    @Accessors(chain = true)
    static class User {
        private String name;
        private int age;
        private LocalDate date;
        private LocalDateTime time;
        private BigDecimal q = new BigDecimal("111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333.111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333");
    }

}
