package com.space.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 计算工具类
 *
 * @author zhuzhe
 * @date 2018/3/26 17:25
 * @email zhe.zhu1@outlook.com
 */
public class CalculateUtils {

    /**
     * 求两个数字的最大公约数
     *
     * @param num1
     * @param num2
     * @return
     */
    public static long commonDivisor(long num1, long num2) {
        if (num1 == num2) {
            return num1;
        }
        long max = num1 > num2 ? num1 : num2;
        long min = num1 < num2 ? num1 : num2;
        if (max % min == 0) {
            return min;
        }
        for (long i = min; i > 0; i--) {
            if (min % i == 0 && max % i == 0) {
                return i;
            }
        }
        return 1;
    }

    /**
     * 求两个数字的最小公倍数
     */
    public static long diploid(long num1, long num2) {
        if (num1 == num2) {
            return num1;
        }
        long max = num1 > num2 ? num1 : num2;
        long min = num1 < num2 ? num1 : num2;
        if (max % min == 0) {
            return max;
        }
        for (long i = 1; i <= min; i++) {
            if (max * i % min == 0) {
                return max * i;
            }
        }
        return num1 * num2;
    }

    /**
     * 计算两个数字的最简整数比
     *
     * @param num1
     * @param num2
     * @return num1:num2
     */
    public static String scale(Double num1, Double num2) {
        while (num1 != num1.intValue() || num2 != num2.intValue()) {
            num1 = mul(num1, 10);
            num2 = mul(num2, 10);
        }
        for (int i = num1.intValue(); i > 0; i--) {
            if (num1.intValue() % i == 0 && num2.intValue() % i == 0) {
                num1 = num1 / i;
                num2 = num2 / i;
                return num1.intValue() + ":" + num2.intValue();
            }
        }
        return num1.intValue() + ":" + num2.intValue();
    }

    /**
     * 精准乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，
     * 由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }
        if (v2 == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        double result = b1.add(b2).doubleValue();
        return result;
    }

    /**
     * 提供精确加法计算的add方法，确认精确度
     *
     * @param value1
     * @param value2
     * @param scale  小数点后保留几位
     * @return 两个参数求和之后，按精度四舍五入的结果
     */
    public static double add(double value1, double value2, int scale) {
        return round(add(value1, value2), scale);
    }

    /**
     * 提供精确减法运算的sub方法，确认精确度
     *
     * @param value1 被减数
     * @param value2 减数
     * @param scale  小数点后保留几位
     * @return 两个参数的求差之后，按精度四舍五入的结果
     */
    public static double sub(double value1, double value2, int scale) {
        return round(sub(value1, value2), scale);
    }

    /**
     * 提供精确乘法运算的mul方法，确认精确度
     *
     * @param value1
     * @param value2
     * @param scale  小数点后保留几位
     * @return 两个参数的乘积之后，按精度四舍五入的结果
     */
    public static double mul(double value1, double value2, int scale) {
        return round(mul(value1, value2), scale);
    }

    /**
     * 四舍五入格式化
     *
     * @param number       被格式化对象
     * @param scale        保留小数点
     * @param groupingSize 每组几个，0-代表不分组
     * @return 格式化数字字符串
     */
    public static String format(BigDecimal number, int scale, int groupingSize) {
        DecimalFormat df = new DecimalFormat();
        if (groupingSize == 0) {
            df.setGroupingUsed(false);
        } else {
            df.setGroupingSize(groupingSize);
        }
        df.setMaximumFractionDigits(scale);
        df.setMinimumFractionDigits(scale);
        return df.format(number.setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
