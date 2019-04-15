package retail.yzx.com.kz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Utils.PermissionUtils;
import Utils.ServiceUtils;
import base.BaseActivity;

/**
 * Created by admin on 2017/6/29.
 */
public class Welcome_activity extends BaseActivity implements View.OnClickListener {

    public RelativeLayout Rl_retail,im_Cashier;
    public TextView im_shake;
    private LinearLayout self_service_retail,self_service_restaurant;


    @Override
    protected int getContentId() {
        return R.layout.welcome_layout;
    }

    @Override
    protected void init() {
        super.init();


        verifyStoragePermissions(this);

        getPermission();

        im_shake = (TextView) findViewById(R.id.im_shake);
        Rl_retail = (RelativeLayout) findViewById(R.id.Rl_retail);
        im_Cashier = (RelativeLayout) findViewById(R.id.im_Cashier);
        self_service_retail = (LinearLayout) findViewById(R.id.self_service_retail);
        self_service_restaurant = (LinearLayout) findViewById(R.id.self_service_restaurant);
        Rl_retail.setOnClickListener(this);
        self_service_retail.setOnClickListener(this);
        self_service_restaurant.setOnClickListener(this);
        im_Cashier.setOnClickListener(this);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(4000);
//        im_shake.startAnimation(animation);
        im_shake.setOnClickListener(this);

        //启动副屏服务
        if (ServiceUtils.isServiceRunning(this,"Myservice")){

        }else {
            Intent stopIntent = new Intent(Welcome_activity.this, Myservice.class);
            startService(stopIntent);
        }
        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(Welcome_activity.this);
    }

    private void getPermission(){
        PermissionUtils.PermissionGrant mPermissionGrant =new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                switch (requestCode) {
                    case PermissionUtils.CODE_RECORD_AUDIO:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_GET_ACCOUNTS:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_READ_PHONE_STATE:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_CALL_PHONE:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_CAMERA:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                        break;
                    case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        //获取权限的方法
        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.welcome_layout);
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(Welcome_activity.this);
//
//       PermissionUtils.PermissionGrant mPermissionGrant =new PermissionUtils.PermissionGrant() {
//            @Override
//            public void onPermissionGranted(int requestCode) {
//                switch (requestCode) {
//                    case PermissionUtils.CODE_RECORD_AUDIO:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_GET_ACCOUNTS:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_READ_PHONE_STATE:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_CALL_PHONE:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_CAMERA:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
//                        Toast.makeText(Welcome_activity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//
//
//        //获取权限的方法
//        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
//
//        init1();
//        //    隐藏底部按钮
//        StringUtils.HideBottomBar(Welcome_activity.this);
//    }

    //    隐藏底部按钮
//    public void toggleHideyBar() {
////        // BEGIN_INCLUDE (get_current_ui_flags) 
////        //  The UI options currently enabled are represented by a bitfield.
////        //  getSystemUiVisibility() gives us that bitfield.  
////        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
////        int newUiOptions = uiOptions;
////        // END_INCLUDE (get_current_ui_flags)
////        //  BEGIN_INCLUDE (toggle_ui_flags)  
////        boolean isImmersiveModeEnabled =
////                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
////        if (isImmersiveModeEnabled) {
////            Log.i("123", "Turning immersive mode mode off. ");
////        } else {
////            Log.i("123", "Turning immersive mode mode on.");
////        }
////        // Navigation bar hiding:  Backwards compatible to ICS.  
////        if (Build.VERSION.SDK_INT >= 14) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
////        }
////        // Status bar hiding: Backwards compatible to Jellybean  
////        if (Build.VERSION.SDK_INT >= 16) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
////        }
////        // Immersive mode: Backward compatible to KitKat.  
////        // Note that this flag doesn't do anything by itself, it only augments the behavior  
////        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample  
////        // all three flags are being toggled together.  
////        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".  
////        // Sticky immersive mode differs in that it makes the navigation and status bars  
////        // semi-transparent, and the UI flag does not get cleared when the user interacts with  
////        // the screen.  
////        if (Build.VERSION.SDK_INT >= 18) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
////        }
//////         getWindow().getDecorView().setSystemUiVisibility(newUiOptions);//上边状态栏和底部状态栏滑动都可以调出状态栏  
////        getWindow().getDecorView().setSystemUiVisibility(4108);//这里的4108可防止从底部滑动调出底部导航栏  
//        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//
//
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Welcome_activity.this);
//    }

//    private void init1() {
//        im_shake = (TextView) findViewById(R.id.im_shake);
//        Rl_retail = (RelativeLayout) findViewById(R.id.Rl_retail);
//        im_Cashier = (RelativeLayout) findViewById(R.id.im_Cashier);
//        self_service_retail = (LinearLayout) findViewById(R.id.self_service_retail);
//        self_service_restaurant = (LinearLayout) findViewById(R.id.self_service_restaurant);
//        Rl_retail.setOnClickListener(this);
//        self_service_retail.setOnClickListener(this);
//        self_service_restaurant.setOnClickListener(this);
//        im_Cashier.setOnClickListener(this);
//
//
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setDuration(4000);
//        im_shake.startAnimation(animation);
//        im_shake.setOnClickListener(this);
//
//        //启动副屏服务
//        Intent stopIntent = new Intent(Welcome_activity.this, Myservice.class);
//        startService(stopIntent);
//    }




    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

//        try {
//            //检测是否有写的权限
//            int permission = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // 没有写的权限，去申请写的权限，会弹出对话框
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){

//            int permission = ActivityCompat.checkSelfPermission(activity,
//                    "android.permission.WRITE_EXTERNAL_STORAGE");

            permissionList.add("android.permission.WRITE_EXTERNAL_STORAGE");

        }



        if (ContextCompat.checkSelfPermission(activity,"com.android.example.USB_PERMISSION") != PackageManager.PERMISSION_GRANTED){

            permissionList.add("com.android.example.USB_PERMISSION");

        }



        if (!permissionList.isEmpty()){  //申请的集合不为空时，表示有需要申请的权限

            ActivityCompat.requestPermissions(activity,permissionList.toArray(new String[permissionList.size()]),1);

        }else { //所有的权限都已经授权过了



        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Rl_retail:
                startActivity(new Intent(Welcome_activity.this, LoginActivity.class));
                break;
            case R.id.self_service_retail:
                Intent intent=new Intent(Welcome_activity.this,LoginActivity.class);
                intent.putExtra("logntype",1);
                startActivity(intent);
                break;
            case R.id.self_service_restaurant:
                Intent intent_restaurant=new Intent(Welcome_activity.this,LoginActivity.class);
                intent_restaurant.putExtra("logntype",2);
                startActivity(intent_restaurant);
                break;
            case R.id.im_Cashier:
                Intent intent_restaurant_nomal=new Intent(Welcome_activity.this,LoginActivity.class);
                intent_restaurant_nomal.putExtra("logntype",3);
                startActivity(intent_restaurant_nomal);
                break;
            case R.id.im_shake:
                final Dialog dialog = new Dialog(this);
                dialog.setTitle(getResources().getString(R.string.mobile_app_cashier_system));
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.welcome_app);
                break;
        }
    }
}
