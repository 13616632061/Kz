package retail.yzx.com.kz;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import Utils.StringUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/6/21.
 */
public class UPdate_MyServices extends Service{
    public static final int NO_3 =0x3;
//    public String url="http://zjzc.oss-cn-beijing.aliyuncs.com/yzx-pingban.apk";
    public String url="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        url=intent.getStringExtra("url");
        Log.d("print","获取下载的数据"+url);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("下载");
        builder.setContentText("正在下载");
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NO_3, builder.build());
        builder.setProgress(100, 0, false);
        if (!url.equals("")) {
            OkGo.get(url)
                    .tag(this)
                    .execute(new FileCallback("易之星.apk") {
                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                            Log.d("schedule", "当前大小" + currentSize);
                            Log.d("schedule", "总大小" + totalSize);
                            Log.d("schedule", "进度" + progress);
//                        Log.d("schedule","网络"+(new Float(currentSize).intValue()/new Float(progress).intValue())*100);
//                        builder.setProgress(new Long(totalSize).intValue(),new Float(progress).intValue(),false);
                            manager.notify(NO_3, builder.build());
                            //下载进度提示
                            if (progress > 0 && progress < 1) {
                                builder.setContentText("下载" + (Float.parseFloat(StringUtils.stringpointtwo(progress + ""))) * 100 + "%");
                            } else {
                                builder.setContentText("下载" + "100%");
                            }

                            builder.setProgress(100, 50, false);
                        }

                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            installApk();
                        }
                    });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();




    }
    private void installApk(){
        File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/Download/"+"易之星.apk");
        if(!file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
