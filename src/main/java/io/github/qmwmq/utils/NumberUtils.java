package io.github.qmwmq.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class NumberUtils {

    private static final Pattern VALID_NUMBER_PATTERN = Pattern.compile("^-?((\\d{1,3})(,\\d{3})*|\\d+)(\\.\\d+)?$");

    public static BigDecimal parse(Object n) {
        if (StringUtils.isBlank(n))
            return null;
        String str = StringUtils.strip(n);
        if (!VALID_NUMBER_PATTERN.matcher(str).matches())
            return null;
        try {
            // 1. 创建适配千位分隔符的NumberFormat（Locale.US 适配英文千位分隔符规则）
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            // 2. 解析字符串为Number类型
            Number number = numberFormat.parse(str);
            // 3. 转换为BigDecimal（推荐用BigDecimal.valueOf，避免精度丢失）
            return BigDecimal.valueOf(number.doubleValue());
        } catch (Exception e) {
            return null;
        }
    }

    static void main() {
        System.out.println(parse("1234567.89"));
        System.out.println(parse("1,234,567.89"));
        System.out.println(parse("1234567"));
        System.out.println(parse("1,234,567"));
        System.out.println(parse("0.89"));
        System.out.println(parse("1a234567.89"));
        System.out.println(parse("12,34567.89"));
        System.out.println(parse("1234567.89a"));
        System.out.println(parse("1,234.56.78"));
        System.out.println(parse(",1234567.89"));
    }

    // 两个非负整数映射到唯一非负整数，Cantor 配对函数
//    public static long uniqueNumber(long a, long b) {
//        if (a < 0 || b < 0)
//            throw new RuntimeException("both number must be non-negative");
//        return (a + b) * (a + b + 1) / 2 + b;
//    }

}
