/* vim: set expandtab sw=4 ts=4 sts=4: */
/**
 * @author:          eddie
 * @last modified:   2012-07-07 10:45:27
 * @filename:        MyJsonUtils.java
 * @description:     
 */
package Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	public static String getDateTimeFromMillisecond(Long millisecond){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millisecond);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
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

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		return toDate(sdate, dateFormater.get());
	}

	public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateString(Date date) {
		return dateFormater.get().format(date);
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = null;

		if (TimeZoneUtil.isInEasternEightZones())
			time = toDate(sdate);
		else
			time = TimeZoneUtil.transformTime(toDate(sdate),
					TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天 ";
		} else if (days > 2 && days < 31) {
			ftime = days + "天前";
		} else if (days >= 31 && days <= 2 * 31) {
			ftime = "一个月前";
		} else if (days > 2 * 31 && days <= 3 * 31) {
			ftime = "2个月前";
		} else if (days > 3 * 31 && days <= 4 * 31) {
			ftime = "3个月前";
		} else {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	public static String friendly_time2(String sdate) {
		String res = "";
		if (StringUtils.isEmpty(sdate))
			return "";

		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String currentData = StringUtils.getDataTime("MM-dd");
		int currentDay = StringUtils.toInt(currentData.substring(3));
		int currentMoth = StringUtils.toInt(currentData.substring(0, 2));

		int sMoth = StringUtils.toInt(sdate.substring(5, 7));
		int sDay = StringUtils.toInt(sdate.substring(8, 10));
		int sYear = StringUtils.toInt(sdate.substring(0, 4));
		Date dt = new Date(sYear, sMoth - 1, sDay - 1);

		if (sDay == currentDay && sMoth == currentMoth) {
//			res = "今天 / " + weekDays[getWeekOfDate(new Date())];
			res = weekDays[getWeekOfDate(new Date())];
		} else if (sDay == currentDay + 1 && sMoth == currentMoth) {
//			res = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
			res =  weekDays[(getWeekOfDate(new Date()) + 6) % 7];
		} else {
			if (sMoth < 10) {
				res = "0";
			}
			res += sMoth + "/";
			if (sDay < 10) {
				res += "0";
			}
//			res += sDay + " / " + weekDays[getWeekOfDate(dt)];
			res= weekDays[getWeekOfDate(dt)];
		}

		return res;
	}


	/**
	 * 智能格式化
	 */
	public static String friendly_time3(String sdate) {
		String res = "";
		if (StringUtils.isEmpty(sdate))
			return "";

		Date date = StringUtils.toDate(sdate);
		if (date == null)
			return sdate;

		SimpleDateFormat format = dateFormater2.get();

		if (isToday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
			res = format.format(date);
		} else if (isYesterday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
			res = format.format(date);
		} else if (isCurrentYear(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
			res = format.format(date);
		} else {
			format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
			res = format.format(date);
		}
		return res;
	}

	/**
	 * @return 判断一个时间是不是上午
	 */
	public static boolean isMorning(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int hour = time.hour;
		return (hour >= 0) && (hour < 12);
	}

	/**
	 * @return 判断一个时间是不是今天
	 */
	public static boolean isToday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (thenMonthDay == time.monthDay);
	}

	/**
	 * @return 判断一个时间是不是昨天
	 */
	public static boolean isYesterday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (time.monthDay - thenMonthDay == 1);
	}

	/**
	 * @return 判断一个时间是不是今年
	 */
	public static boolean isCurrentYear(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year);
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
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
	 * 返回long类型的今天的日期
	 *
	 * @return
	 */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater2.get().format(cal.getTime());
		curDate = curDate.replace("-", "");
		return Long.parseLong(curDate);
	}

	public static String getCurTimeStr() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater.get().format(cal.getTime());
		return curDate;
	}

	/**
	 * 获取当前系统时间
	 * @return
     */
	public  static String getCurDate(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new Date());
		return date;
	}

	/**
	 * 获取当前系统时间格式MM dd
	 * @return
	 */
	public  static String getCurDate1(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM dd");
		String date = sDateFormat.format(new Date());
		return date;
	}


	public  static String getDate(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new Date());
		return date;
	}

	/**
	 * 日期间隔
	 * @param date_begin
	 * @param date_end
     * @return
     */
	public  static long getDateSpan(String date_begin,String date_end,int type){
		DateFormat df = null;
		if(type==0){
			 df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}else if(type==1){
			 df = new SimpleDateFormat("yyyy-MM-dd");
		}
		long days = 0;
		try {
			Date d1 = df.parse(date_begin);
			Date d2 = df.parse(date_end);
			long diff = d2.getTime() - d1.getTime();
			 days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	/**
	 * 时间间隔
	 * @param date_begin
	 * @param date_end
	 * @return
	 */
	public  static long getTimeSpan(String date_begin,String date_end){
		DateFormat df = new SimpleDateFormat("HH:mm");
		System.out.println("df="+df);
		long times = 0;
		try {
			Date d1 = df.parse(date_begin);
			Date d2 = df.parse(date_end);
			System.out.println("d1="+d1);
			System.out.println("d2="+d2);
			long diff = d2.getTime() - d1.getTime();
			times = diff / (1000 * 60 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("times="+times);
		return times;
	}
	/***
	 * 计算两个时间差，返回的是的秒s
	 *
	 * @author 火蚁 2015-2-9 下午4:50:06
	 *
	 * @return long
	 * @param dete1
	 * @param date2
	 * @return
	 */
	public static long calDateDifferent(String dete1, String date2) {

		long diff = 0;

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = dateFormater.get().parse(dete1);
			d2 = dateFormater.get().parse(date2);

			// 毫秒ms
			diff = d2.getTime() - d1.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return diff / 1000;
	}

	/**
	 * 获取昨天日期
	 * @return
     */
	public static String getYesterdayDate(){
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;

	}

	/**
	 * 获取月份时间
	 * 本月：i=0;
	 * 上月：i=-31
	 * @return
     */
	public static String getMonthDate(int i){
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MONTH,i);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(date);
		System.out.println("dateString="+dateString);
		return dateString;

	}
	/**
	 * 获取当前时间标识码精确到毫秒
	 * */
	public static String getNowtimeKeyStr(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String nowdate = df.format(new Date());// new Date()为获取当前系统时间
		return nowdate;
	}


	public static String gettime(int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, n);//往上推一天  30推三十天  365推一年
		dBefore= calendar.getTime();
		return sdf.format(dBefore);
	}



	public static String gettime(String str,int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dBefore = StringToDate(str);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dBefore);
		calendar.add(Calendar.DAY_OF_MONTH, n);//往上推一天  30推三十天  365推一年
		dBefore= calendar.getTime();
		return sdf.format(dBefore);
	}



	/**
	 * String   转 Date;
	 * @param str
	 * @return
	 */

	public static Date StringToDate(String str){

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
		Date date= null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}



	//获得订单id得时间格式
	public static String getorder(){
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");// 设置日期格式
		String nowdate = df.format(new Date());// new Date()为获取当前系统时间
		return nowdate;
	}

	//获取时间戳
	public static String getTime(){
		long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
		String str=String.valueOf(time);
		return str;
	}

	public static long lastClickTime = 0;//上次点击的时间

	public static int spaceTime = 1000;//时间间隔
	public static boolean isFastClick() {

		long currentTime = System.currentTimeMillis();//当前系统时间

		boolean isAllowClick;//是否允许点击

		if (currentTime - lastClickTime > spaceTime) {

			isAllowClick = false;

		} else {

			isAllowClick = true;

		}

		lastClickTime = currentTime;

		return isAllowClick;

	}


	//掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳 
	public static String data(String time) {
		SimpleDateFormat sdr= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date date;
		String times = null;
		 try {
			 date = sdr.parse(time);
			 long l = date.getTime();
			 String stf = String.valueOf(l);
			 times = stf.substring(0, 10);
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
		 return times;
		 }



	public static long getFormatedDateTime(String pattern, String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		long millionSeconds=0;
		try {
			millionSeconds = sdf.parse(dateTime).getTime()/1000;//毫秒

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millionSeconds;
	}




}
