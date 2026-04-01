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

    // 包含边界
    public static boolean isBetween(Number a, Number b, Number c) {
        if (a == null || b == null || c == null)
            throw new IllegalArgumentException("numbers cannot be null");

        double aVal = a.doubleValue();
        double bVal = b.doubleValue();
        double cVal = c.doubleValue();

        return aVal >= Math.min(bVal, cVal) && aVal <= Math.max(bVal, cVal);
    }

    // 不包含边界
    public static boolean isInside(Number a, Number b, Number c) {
        if (a == null || b == null || c == null)
            throw new IllegalArgumentException("numbers cannot be null");

        double aVal = a.doubleValue();
        double bVal = b.doubleValue();
        double cVal = c.doubleValue();

        return aVal > Math.min(bVal, cVal) && aVal < Math.max(bVal, cVal);
    }

}
