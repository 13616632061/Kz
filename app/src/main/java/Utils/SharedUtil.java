package Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import Entty.Kitchen_IP;

/**
 *
 *
 */
public class SharedUtil {

	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor editor;

	/**
	 */
	public static void init(Context context){
		sharedPreferences = context.getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
		editor = sharedPreferences.edit();
	}

	public static void putString(String key, String value){
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(String key){
		return sharedPreferences.getString(key, "");
	}

	public static void putInt(String key, int value){
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(String key){
		return sharedPreferences.getInt(key, -1);
	}

	public static void putBoolean(String key, boolean value){
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(String key){
		return sharedPreferences.getBoolean(key, true);
	}


	public static void putfalseBoolean(String key, boolean value){
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getfalseBoolean(String key){
		return sharedPreferences.getBoolean(key, false);
	}

	public static void setMole(List<Kitchen_IP> namelist){
		Gson gson = new Gson();
		String jsonStr=gson.toJson(namelist); //将List转换成Json
		editor.putString("Kitchen_IP", jsonStr) ; //存入json串
		editor.commit() ;  //提交
	}

	public static List<Kitchen_IP> getMole(){
		String peopleListJson = sharedPreferences.getString("Kitchen_IP","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
		List<Kitchen_IP> namelist=null;
		if(peopleListJson!="")  //防空判断
		{
			Gson gson = new Gson();
			namelist = gson.fromJson(peopleListJson, new TypeToken<List<Kitchen_IP>>() {}.getType()); //将json字符串转换成List集合
		}
		return namelist;
	}

}
