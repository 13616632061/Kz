package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author HuangWenwei
 * 
 * @date 2014年10月9日
 */
public class TimeZoneUtil {

	/**
	 * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
			defaultVaule = true;
		else
			defaultVaule = false;
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间 2014年7月31日
	 * @param date
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;
	}

	//获得固定格式时间
	public static String getTime(Long adta) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(adta);
		return format.format(date);
	}

	//获得固定格式时间
	public static String getTime1(Long adta) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(adta);
		return format.format(date);
	}


//	获得两个日期间隔的天数
	public static String getDay(String beginTime,String endTime){
		String str1 = beginTime; // "yyyyMMdd"格式 如 20131022
		System.out.println("\n结束时间:");
		String str2 = endTime; // "yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式
		Date date1 = null;

		try {
			date1 = simpleDateFormat.parse(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		return dayCount+"";
	}
}
