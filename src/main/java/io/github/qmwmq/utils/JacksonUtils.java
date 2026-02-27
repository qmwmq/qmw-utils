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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JacksonUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Getter
    private static final JsonMapper jsonMapper = JsonMapper.builder()
            // 序列化配置
            .changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_NULL)) // 忽略 null 值的 key
            .addModule(new SimpleModule()
                    .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
                    .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER))
                    .addSerializer(BigDecimal.class, new ValueSerializer<>() {
                        @Override
                        public void serialize(BigDecimal value, JsonGenerator gen, SerializationContext provider) {
                            gen.writeString(value.toPlainString());
                        }
                    })
                    .addSerializer(Date.class, new ValueSerializer<>() {
                        @Override
                        public void serialize(Date value, JsonGenerator gen, SerializationContext provider) {
                            gen.writeString(value.toString());
                        }
                    })
                    .addSerializer(Timestamp.class, new ValueSerializer<>() {
                        @Override
                        public void serialize(Timestamp value, JsonGenerator gen, SerializationContext provider) {
                            gen.writeString(DATE_TIME_FORMATTER.format(value.toLocalDateTime()));
                        }
                    })
                    .addSerializer(OffsetDateTime.class, new ValueSerializer<>() {
                        @Override
                        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializationContext provider) {
                            gen.writeString(DATE_TIME_FORMATTER.withZone(ZONE_ID).format(value));
                        }
                    })
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
        User user = new User()
                .setName("qmwmq")
                .setAge(18)
                .setD(new Date(System.currentTimeMillis()))
                .setT(new Timestamp(System.currentTimeMillis()))
                .setTime(LocalDateTime.now())
                .setDate(LocalDate.now());
        System.out.println(JacksonUtils.toJSONString(user, true));

        String u = "{" +
                "  \"age\" : \"0018\"," +
                "  \"name\" : \"qmwmq\"," +
                "  \"aaa\" : \"qmwmq\"," +
                "  \"d\" : \"2026-01-19\"," +
                "  \"t\" : \"2026-01-19\"," +
                "  \"q\" : \"111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333\"," +
                "  \"time\" : \"2026-01-19 23:58:53\"" +
                "}";

        System.out.println(jsonMapper.readValue(u, User.class));

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        System.out.println(offsetDateTime);

        System.out.println(ZoneId.systemDefault());

    }

    @Data
    @Accessors(chain = true)
    static class User {
        private String name;
        private int age;
        private LocalDate date;
        private Date d;
        private Timestamp t;
        private LocalDateTime time;
        private BigDecimal q = new BigDecimal("111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333.111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333111111111111111122222222222222222233333333");
    }

}
