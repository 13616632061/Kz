package retail.yzx.com.kz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Utils.ServiceUtils;

/**
 * Created by admin on 2017/3/1.
 * 开机自启
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            if (ServiceUtils.isServiceRunning(context,"Myservice")){
            }else {
                context.startService(new Intent(context, Myservice.class));
            }
        }
    }
}
