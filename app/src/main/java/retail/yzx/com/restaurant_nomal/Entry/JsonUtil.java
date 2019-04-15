package retail.yzx.com.restaurant_nomal.Entry;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/22.
 */

public class JsonUtil {

    /**
     * 获取json字符串
     * @param object
     * @param str
     * @return
     */
    public static String getJsonString(JSONObject object,String str){
        String obj_str="";
        try {
            obj_str = object.getString(str);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("print", str + ";"+e.toString());
        }
        return obj_str;
    }
    /**
     * 获取json整型数据
     * @param object
     * @param str
     * @return
     */
    public static int getJsonInt(JSONObject object,String str){
        int obj_str=0;
        try {
            obj_str = object.getInt(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_str;
    }
    /**
     * 获取json  double数据
     * @param object
     * @param str
     * @return
     */
    public static double getJsonDouble(JSONObject object,String str){
        double obj_str=0.00;
        try {
            obj_str = object.getInt(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_str;
    }
    /**
     * 获取json  float数据
     * @param object
     * @param str
     * @return
     */
    public static float getJsonFloat(JSONObject object,String str){
        float obj_str=0;
        try {
            obj_str = object.getInt(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_str;
    }
    /**
     * 获取json  数组数据
     * @param object
     * @param str
     * @return
     */
    public static JSONArray getJsonArray(JSONObject object, String str){
        JSONArray obj_str=null;
        try {
            obj_str = object.getJSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error;"+e.toString());
        }
        return obj_str;
    }
}
