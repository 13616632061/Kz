package Utils;

import android.util.Log;

/**
 * Created by admin on 2017/8/22.
 */
public class LogUtils {

    //可以全局控制是否打印log日志  
    public static boolean isPrintLog = true;
    public static int LOG_MAXLENGTH = 2000;


 public static void v(String msg) {
         v("LogUtil", msg);
          }

 public static void v(String tagName, String msg) {
         if (isPrintLog) {
             int strLength = msg.length();
             int start = 0;
             int end = LOG_MAXLENGTH;
             for (int i = 0; i < 100; i++) {
                 if (strLength > end) {
                     Log.v(tagName  + i, msg.substring(start, end));
                     start = end;
                     end = end + LOG_MAXLENGTH;
                     } else {
                     Log.v(tagName + i, msg.substring(start, strLength));
                    break;
                     }
                 }
             }
         }

 public static void d(String msg) {
         d("LogUtil", msg);
         }
 public static void d(String tagName, String msg) {
         if (isPrintLog) {
             int strLength = msg.length();
             int start = 0;
             int end = LOG_MAXLENGTH;
             for (int i = 0; i < 100; i++) {
                 if (strLength > end) {
                     Log.d(tagName + i, msg.substring(start, end));
                     start = end;
                     end = end + LOG_MAXLENGTH;
                     } else {
                     Log.d(tagName + i, msg.substring(start, strLength));
                     break;
                     }
                 }
             }
         }
 public static void i(String msg) {
         i("LogUtil", msg);
         }
 public static void i(String tagName, String msg) {
         if (isPrintLog) {
             int strLength = msg.length();
             int start = 0;
             int end = LOG_MAXLENGTH;
             for (int i = 0; i < 100; i++) {
                 if (strLength > end) {
                     Log.i(tagName + i, msg.substring(start, end));
                     start = end;
                     end = end + LOG_MAXLENGTH;
                     } else {
                     Log.i(tagName + i, msg.substring(start, strLength));
                     break;
                     }
                 }
             }
         }
 public static void w(String msg) {
         w("LogUtil", msg);
         }
 public static void w(String tagName, String msg) {
         if (isPrintLog) {
             int strLength = msg.length();
             int start = 0;
             int end = LOG_MAXLENGTH;
             for (int i=0;i<100;i++){
                if(strLength>end){
                    Log.w(tagName+i,msg.substring(start,end));
                    start=end;
                    end=end+LOG_MAXLENGTH;
                    }else{
                    Log.w(tagName+i,msg.substring(start,strLength));
                    break;
                    }
                }
            }
        }

 public static void e(String msg) {
         e("LogUtil", msg);
         }
 public static void e(String tagName,String msg){
        if(isPrintLog){
            int strLength=msg.length();
            int start=0;
            int end=LOG_MAXLENGTH;
            for(int i=0;i<100;i++){
                if(strLength>end){
                    Log.e(tagName+i,msg.substring(start,end));
                    start=end;
                    end=end+LOG_MAXLENGTH;
                    }else{
                    Log.e(tagName+i,msg.substring(start,strLength));
                    break;
                    }
                }
            }
        }


    public static void i(String tag, String msg,String str) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

}
