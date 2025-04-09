package io.github.qmwmq.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 无法strip的特殊空格符
     */
    public static final String[] specialSpaces = {
            "\u00A0", "\u2000", "\u2001", "\u2002", "\u2003",
            "\u2004", "\u2005", "\u2006", "\u2007", "\u2008",
            "\u2009", "\u200A", "\u200B", "\u200C", "\u200D",
            "\u202C", "\u202D", "\u202F", "\u205F", "\u2060",
            "\u3000", "\uFEFF"
    };

    /**
     * 判断是否空字符串
     *
     * @param str 入参
     * @return 是否空字符串
     */
    public static boolean isBlank(Object str) {
        return Optional.ofNullable(str).orElse("").toString().strip().isBlank();
    }

    /**
     * 如果是空字符串则转换为另一个字符串
     *
     * @param str     入参
     * @param replace 另一个字符串
     * @return 入参或另一个字符串
     */
    public static String ifBlank(Object str, String replace) {
        return isBlank(str) ? replace : str.toString();
    }

    /**
     * 去除所有空格，包括一些特殊空格
     *
     * @param string 入参
     * @return 出参
     */
    public static String stripAll(Object string) {
        return Arrays.stream(ifBlank(string, "").split(""))
                .map(String::strip)
                .filter(e -> Arrays.stream(specialSpaces).noneMatch(o -> o.equals(e)))
                .collect(Collectors.joining());
    }

    /**
     * 生成UUID，不带'-'
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        var a = """
                123%s
                    321+"123"
                """.formatted(123);
        System.out.println(a);
    }

}
