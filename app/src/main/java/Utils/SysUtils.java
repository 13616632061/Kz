package Utils;

//import com.xuebao.exam.AdActivity;
//import com.xuebao.exam.GoodsActivity;
//import com.xuebao.exam.MainActivity;
//import com.xuebao.exam.NewsDetailActivity;
//import com.xuebao.exam.SchoolDetailActivity;
//import com.xuebao.exam.ZhuantiActivity;
//import com.xuebao.global.Global;
//import com.xuebao.exam.ExamApplication;

//import com.oho.rugao.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import hdx.HdxUtil;

public class SysUtils {
    public static String getWebUri() {
        String isTest;
        if (SharedUtil.getString("isTest")!=null){
            isTest= SharedUtil.getString("isTest");
        }else {
            isTest= "1";
            SharedUtil.putString("isTest","1");
        }

        if (isTest.equals("0")) {
//            return "http://www.czxshop.net/";
            return "http://test.czxshop.com/";
//            return "http://114.215.184.119/";
        } else {
//            return "http://www.yzx6868.com/";
//            return "http://new.czxshop.com/";
//            return "http://fx.yzx6868.com";
//            return "http://118.178.174.183";
            return "http://114.55.73.122/";
        }
    }

    public static String getServiceUrl(String method) {
        return SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_seller." + method + "&vsn=1.0&format=json";
    }

    public static String getSms(String method){
        return SysUtils.getWebUri()+"wap/"+method+".html?seller_token="+SharedUtil.getString("seller_token");
    }


    public static String getUpDatas(){
        //    http://test.czxshop.com/wap/appUpdate.html
        return SysUtils.getWebUri() + "wap/appUpdate.html";
    }

    public static String getCoed(String id){
        String isTest;
        if (SharedUtil.getString("isTest")!=null){
            isTest= SharedUtil.getString("isTest");
        }else {
            isTest= "1";
            SharedUtil.putString("isTest","1");
        }

        if (isTest.equals("0")) {
//            return "http://www.czxshop.net/";
            return "http://test.czxshop.com/wap/cash-register-"+id+".html";
//            return "http://114.215.184.119/";
        } else {
//            return "http://www.yzx6868.com/";
            return "http://new.czxshop.com/wap/cash-register-"+id+".html";
//            return "http://fx.yzx6868.com";
//            return "http://118.178.174.183";
        }


    }


//    http://www.yzx6868.com/rpc/service/?method=ks.pingban_seller.login&vsn=1.0&format=json
//    public static String getMemberServiceUrl(String method) {
//        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.member." + method + "&vsn=1.0&format=json";
//        ret += "&member_token=" + KsApplication.getString("token", "");
//        return ret;
//    }


    public static String getSellerorderUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_order." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }

    public static String getSellerServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }
    public static String getTestServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }
    public static String getTableServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_catering." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    } public static String getShareGoodsServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_share." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }

    public static String getShareTextServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_goods." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }

    public static String getSellerServiceAPPUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.seller." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }
    /**
     * 商品添加
     *
     * @param method
     * @return
     */
    public static String getGoodsServiceUrl(String method) {
        String ret = SysUtils.getWebUri() + "rpc/service/?method=ks.pingban_goods." + method + "&vsn=1.0&format=json";
        ret += "&seller_token=" + SharedUtil.getString("seller_token");
        return ret;
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的  
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用  
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断有连接没网
     */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断wifi是否有连接
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }



    /**
     * @author sky
     * Email vipa1888@163.com
     * QQ:840950105
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
     * @param context
     * @return
     */
    public static int getAPNType(Context context){
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType =3;
            }
            else{
                netType = 2;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = 1;
        }
        return netType;
    }


    public static String priceFormat(double d, boolean hasSuffix) {
        String ret = "";
        if(d == (long) d) {
            ret = String.format("%d",(long)d);
        } else {
            ret = String.format("%.2f", d);
            ret = String.format("%s", ret);

//            Log.v("huigu", "price: " + ret);
            if (ret.endsWith(".0")) {
                if (ret.equals(".0")) {
                    ret = "0";
                } else {
                    ret = ret.substring(0, ret.length() - 2);
                }
            }
        }

        if(hasSuffix) {
            ret = String.format("￥%s元", ret);
        }

        return ret;
    }



    /**

     * 获取手机型号

     *

     * @return  手机型号

     */

    public static String getSystemModel() {

        return android.os.Build.MODEL;

    }

    public static void OpenCashbox(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HdxUtil.SetV12Power(1);
                } catch (UnsatisfiedLinkError e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void OpennewCashbox(Context context){
        Intent intent = new Intent("android.intent.action.CASHBOX");
        intent.putExtra("cashbox_open", true);
        context.sendBroadcast(intent);
    }

}

