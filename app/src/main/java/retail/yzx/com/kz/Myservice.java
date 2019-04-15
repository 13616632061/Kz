package retail.yzx.com.kz;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import Utils.ServiceUtils;

/**
 * Created by admin on 2017/3/1.
 */
public class Myservice extends Service {

    private DisplayManager displayManager;
    private Display[] displays;
    private DifferentDislay differentDislay;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("print","OnCREATE");
            displayManager = (DisplayManager) this.getSystemService(Activity.DISPLAY_SERVICE);
            displays = displayManager.getDisplays();
        if(displays.length>1){
            differentDislay = new DifferentDislay(this, displays[1]);
            differentDislay.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            super.onCreate();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(displays.length>1){
            differentDislay.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
            differentDislay.show();
        }
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ServiceUtils.isServiceRunning(this,"Myservice")){

        }else {
            startService(new Intent(this, Myservice.class));
        }
    }
}
