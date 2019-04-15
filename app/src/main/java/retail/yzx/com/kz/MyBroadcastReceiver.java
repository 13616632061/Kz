package retail.yzx.com.kz;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by admin on 2018/11/9.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        // 获得广播发送的数据
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.activity_dialog_view, null);
//        dialogBuilder.setTitle("提示");
//        dialogBuilder.setMessage("这是在BroadcastReceiver弹出的对话框。");
//        dialogBuilder.setCancelable(false);
//        dialogBuilder.setPositiveButton("确定", null);
        Button but_qixiao= (Button) view.findViewById(R.id.but_qixiao);
        Button but_dimdis= (Button) view.findViewById(R.id.but_dimdis);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        but_qixiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(context.getApplicationContext(),Take_outfood_Activity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("type","0");
                context.startActivity(intent1);
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
}
