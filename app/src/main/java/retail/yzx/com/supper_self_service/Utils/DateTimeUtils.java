package retail.yzx.com.supper_self_service.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Utils.DateUtils;
import Utils.SysUtils;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DateTimeUtils {
    /**
     * 毫秒转固定时间格式，android是13位
     * 时间戳转化为日期
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
    /**
     * 显示时间
     * @param editText
     */
    public static void showCalendarTime(final Context context,final TextView editText) {
        StringUtils.hideSoftKeyboard((Activity) context);
        final TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                mCalendar.set(Calendar.MINUTE,minute);
                editText.setText(mTimeFormat.format(mCalendar.getTime()));
            }
        };
        TimePickerDialog mTimePickerDialog=new TimePickerDialog(context,time,
                mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),true);
        mTimePickerDialog.show();
    }

    /**
     * 显示日期
     * @param editText
     */
    static Calendar mCalendar=Calendar.getInstance(Locale.CHINA);//设置为中国时间
    static DateFormat mDateFormat=new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    static DateFormat mTimeFormat=new SimpleDateFormat("HH:mm");//设置时间格式
    Date mDate=new Date(System.currentTimeMillis());//获取当前系统时间
    public static void showCalendarDate(final Context context, final TextView editText, final String mesg) {
        StringUtils.hideSoftKeyboard((Activity) context);
        final String CurDate= DateUtils.getCurDate().substring(0,10);
        DatePickerDialog.OnDateSetListener data=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR,year);
                mCalendar.set(Calendar.MONTH,monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                if(DateUtils.getDateSpan(CurDate,mDateFormat.format(mCalendar.getTime()),1)<0){
                    editText.setText(CurDate);
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(context,mesg,20);
                }else {
                    editText.setText(mDateFormat.format(mCalendar.getTime()));
                }

            }
        };
        DatePickerDialog mDatePickerDialog=new DatePickerDialog(context,data,mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }
    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
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
    /**
     * 日期和时间一起的时间戳
     *
     */
    private static Calendar dateAndTime=Calendar.getInstance(Locale.CHINA);
    private static DateFormat fmtDate=new java.text.SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat fmtTime=new java.text.SimpleDateFormat("HH:mm:ss");
    public static void runTime(Context context,final TextView mEditText) {
        TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                dateAndTime.set(Calendar.MINUTE, minute);
                mEditText.setText(fmtDate.format(dateAndTime.getTime())+" "+fmtTime.format(dateAndTime.getTime()));
            }
        };

        DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR,year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            }
        };

        TimePickerDialog mTimePickerDialog=new TimePickerDialog(context, t, dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true);
        mTimePickerDialog.show();

        DatePickerDialog mDatePickerDialog=new DatePickerDialog(context, d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }
}
