package Utils;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/5/16.
 * 精度丢失
 */
public class TlossUtils {
    //减
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    //加
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    //乘
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    //除
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale).doubleValue();
    }


    public static int parseNumber(double number){
        String[] aa = (number+"").split("\\.");
        return aa[1].length();
    }

    /**
     * 四舍五入保留确定位数小数
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 返回值 String类型
     */

    //直接删除多余的小数位，如2.35会变成2.3
    public static String round_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_DOWN).toString();
    }

    //进位处理，2.35变成2.4
    public static String round_up(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_UP).toString();
    }
    //四舍五入，2.35变成2.4
    public static String round_half_up(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString();
    }
    //四舍五入，2.35变成2.3，如果是5则向下舍
    public static String round_half_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_DOWN).toString();
    }


    //设置金额去除小数0.4直接去除大于0.5就是按照0.5计算
    public static String getRemove(String number){
        int i=(int) Double.parseDouble(number);
        if ((i+0.5)>Double.parseDouble(number)){
            return i+"";
        }else {
           return (i+0.5)+"";
        }
    }


}
