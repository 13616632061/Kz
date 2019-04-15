package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YGD on 2017/11/10.
 */

public class DateTimeUtil {

    /**
     * 获取当前时间
     */
    public static long getTime(){
        Date time=new Date(System.currentTimeMillis());
        long date=time.getTime();
        return date;
    }


    /**
     * @param date1
     * @param date2
     * @return
     * 判断两个时间的间隔天数
     */
    public static long differentDaysByMillisecond(long date1,long date2)
    {
        long days = ((date2 - date1) / (1000*3600*24));
        return days;
    }


    public static String getformatTime(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }

    public static String getformatTime1(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }

    public static String getformatDay(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }




    public static String getjisTime(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }


    public static long getLongtime(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    public static String getTime(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd - HH : mm : ss");//初始化Formatter的转换格式。
        String hms = formatter.format(time);
        return hms;
    }


    public static String getTimemsg(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//初始化Formatter的转换格式。
        String hms = formatter.format(time);
        return hms;
    }

    /**
     * 判断给定字符串时间是否为今日
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate){
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if(time != null){
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if(nowDate.equals(timeDate)){
                b = true;
            }
        }
        return b;
    }
    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
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

}
