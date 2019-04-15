package retail.yzx.com;

import android.util.Log;

public class debug {

	private static String AppName = "CardServer";

	private static Boolean isTesting = true;
	private static Boolean isApdu= true;
	
	public static void i(String string) {
		if (isTesting) {
			Log.i(AppName, string);
		}
	}

	public static void w(String string) {
		if (isTesting) {
			Log.e(AppName, string);
		}
	}

	public static void e(String string) {
		if (isTesting) {
			Log.e(AppName, string);
		}
	}

	public static void d(String string) {
		if (isTesting) {
			Log.d(AppName, string);
		}
	}

	public static void a(int num) {
		if (isTesting) {
			Log.d(AppName, Integer.toString(num));
		}
	}
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	
//-------------
	public static void i(String str,String string) {
		if (isTesting) {
			Log.i(str, string);
		}
	}

	public static void w(String str,String string) {
		if (isTesting) {
			Log.e(str, string);
		}
	}

	public static void e(String str,String string) {
		if (isTesting) {
			Log.e(str, string);
		}
	}

	public static void d(String str,String string) {
		if (isTesting) {
			Log.d(str, string);
		}
	}

	public static void a(String str,int num) {
		if (isTesting) {
			Log.d(str, Integer.toString(num));
		}
	}
	//------------------------------------
	public static void ii(String str,String string) {
		if (isApdu) {
			Log.i(str, string);
		}
	}	
	public static void ee(String str,String string) {
		if (isApdu) {
			Log.e(str, string);
		}
	}
	
	
}