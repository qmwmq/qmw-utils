package io.github.qmwmq.utils;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.TypeFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JacksonUtils {

    @Getter
    private static final JsonMapper jsonMapper = JsonMapper.builder().build();
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
        System.out.println(JacksonUtils.toJSONString(user));
    }

    @Data
    @Accessors(chain = true)
    static class User {
        private String name;
        private int age;
        private LocalDate date;
        private LocalDateTime time;
    }

}
