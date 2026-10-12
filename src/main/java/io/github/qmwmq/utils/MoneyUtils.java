package io.github.qmwmq.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtils {

    private static final String[] DIGITS = {
            "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"
    };

    private static final String[] UNITS = {
            "", "拾", "佰", "仟"
    };

    private static final String[] SECTION_UNITS = {
            "", "万", "亿", "兆"
    };

    private static final String[] DECIMAL_UNITS = {
            "角", "分"
    };

    /**
     * 数字金额转中文大写金额
     *
     * @param amount 数字金额
     * @return 中文大写金额
     */
    public static String toChineseAmount(BigDecimal amount) {
        if (amount == null) {
            return "";
        }

        // 统一保留两位小数，采用四舍五入
        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return "零元整";
        }

        boolean negative = amount.signum() < 0;
        amount = amount.abs();

        String[] parts = amount.toPlainString().split("\\.");
        long integerPart = Long.parseLong(parts[0]);
        String decimalPart = parts[1];

        StringBuilder result = new StringBuilder();

        if (negative) {
            result.append("负");
        }

        // 整数部分
        if (integerPart > 0) {
            result.append(convertInteger(integerPart)).append("元");
        }

        // 小数部分
        int jiao = decimalPart.charAt(0) - '0';
        int fen = decimalPart.charAt(1) - '0';

        if (jiao == 0 && fen == 0) {
            result.append("整");
        } else {
            if (jiao > 0) {
                result.append(DIGITS[jiao]).append("角");
            } else if (integerPart > 0 && fen > 0) {
                result.append("零");
            }

            if (fen > 0) {
                result.append(DIGITS[fen]).append("分");
            }
        }

        return result.toString();
    }

    /**
     * 转换整数部分
     */
    private static String convertInteger(long number) {
        if (number == 0) {
            return "零";
        }

        StringBuilder result = new StringBuilder();
        int sectionIndex = 0;
        boolean needZero = false;

        while (number > 0) {
            int section = (int) (number % 10000);

            if (section == 0) {
                if (!result.isEmpty()) {
                    needZero = true;
                }
            } else {
                String sectionText = convertSection(section);

                if (needZero || (section < 1000 && !result.isEmpty())) {
                    result.insert(0, "零");
                }

                result.insert(0, sectionText + SECTION_UNITS[sectionIndex]);
                needZero = false;
            }

            number /= 10000;
            sectionIndex++;
        }

        return result.toString();
    }

    /**
     * 转换四位以内的数字
     */
    private static String convertSection(int number) {
        StringBuilder result = new StringBuilder();
        int unitIndex = 0;
        boolean zero = false;

        while (number > 0) {
            int digit = number % 10;

            if (digit == 0) {
                if (!zero && !result.isEmpty()) {
                    result.insert(0, "零");
                }
                zero = true;
            } else {
                result.insert(0, DIGITS[digit] + UNITS[unitIndex]);
                zero = false;
            }

            number /= 10;
            unitIndex++;
        }

        return result.toString();
    }

}