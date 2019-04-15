package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|170|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    private final static Pattern Card=Pattern
            .compile("(00[0])\\d{3}");
    private final static Pattern isIp=Pattern
            .compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                            +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    private final static Pattern port1=Pattern
            .compile("(\\+).*?.(.)(.)(.)(\\.)(.)(.)(.)(.)(k)(g)");
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    public static boolean IsIp(String ip){
        if (ip == null || ip.trim().length() == 0)
            return false;
        return isIp.matcher(ip).matches();
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * 返回当前系统时间
     */
    public static String gettimeDataTime() {
        return getDataTime("yyyy-MM");
    }

    /**
     * 毫秒值转换为mm:ss
     *
     * @author kymjs
     * @param ms
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        return phone.matcher(phoneNum).matches();
    }

    public static String isport(String port){
        String str="";
        String re1="(\\+)";	// Any Single Character 1
        String re2="(.)";	// Any Single Character 2
        String re3="(.)";	// Any Single Character 3
        String re4="(.)";	// Any Single Character 4
        String re5="(\\.)";	// Any Single Character 5
        String re6="(.)";	// Any Single Character 6
        String re7="(.)";	// Any Single Character 7
        String re8="(.)";	// Any Single Character 8
        String re9="(.)";	// Any Single Character 9
        String re10="(k)";	// Any Single Character 10
        String re11="(g)";	// Any Single Character 11

        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(port);
        if (m.find())
        {
            String c1=m.group(1);
            String c2=m.group(2);
            String c3=m.group(3);
            String c4=m.group(4);
            String c5=m.group(5);
            String c6=m.group(6);
            String c7=m.group(7);
            String c8=m.group(8);
            String c9=m.group(9);
            String c10=m.group(10);
            String c11=m.group(11);
            str= c2.toString()+c3.toString()+c4.toString()+c5.toString()+c6.toString()+c7.toString()+c8.toString()+c9.toString()+"\n";
        }
        return str;
    }

    /**
     * 判断是不是一个会员卡号
     */
    public static boolean isCard(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        return Card.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNumber1(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * KJ加密
     */
    public static String KJencrypt(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c + 5));
        }
        return hex.toString();
    }

    /**
     * KJ解密
     */
    public static String KJdecipher(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c - 5));
        }
        return hex.toString();
    }

    public static String beautyMobile(String mobile) {
        String ret = "";

        if(!StringUtils.isEmpty(mobile)) {
            ret = mobile.substring(0, mobile.length()-(mobile.substring(3)).length())+"****"+mobile.substring(7);
        }

        return ret;
    }

    /**
     * 字符串小数点后两位
     * @param str
     */
    public static String stringpointtwo(String str){
        int posDot=str.lastIndexOf(".");
        if (str.length() - posDot - 1 > 2&&str.length() - posDot < str.length())//如果包含小数点
        {
            str= str.subSequence(0,posDot+3).toString();//删除小数点后的第三位
        }
        return str;
    }

    /**
     * 字符串小数点后1位
     * @param str
     */
    public static String setPointone(String str){
        int posDot=str.lastIndexOf(".");
        if (str.length() - posDot - 1 > 1)//如果包含小数点
        {
            str= str.subSequence(0,posDot+2).toString();//删除小数点后的第三位
        }
        return str;
    }


    public static String[] getStrings(String str,String Separate){
        String[] split = str.split(Separate);
        return split;
    }


}