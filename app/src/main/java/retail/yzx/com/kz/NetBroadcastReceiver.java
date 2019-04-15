package retail.yzx.com.kz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import Utils.NetUtil;
import base.BaseActivity;

/**
 * Created by admin on 2018/4/28.
 * 网络变化的监听
 */

public class NetBroadcastReceiver extends BroadcastReceiver{
    public NetEvevt evevt = BaseActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            if (netWorkState==NetUtil.NETWORK_NONE){

            }else {

            }
            // 接口回调传过去状态的类型
            if(evevt!=null){
                evevt.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);
    }
}
