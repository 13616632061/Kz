package retail.yzx.com.supper_self_service.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/7/13.
 */

public class StringUtils {
    //    隐藏底部按钮
    public static  void HideBottomBar(Activity mContext) {
//        // BEGIN_INCLUDE (get_current_ui_flags) 
//        //  The UI options currently enabled are represented by a bitfield.
//        //  getSystemUiVisibility() gives us that bitfield.  
//        int uiOptions = mContext.getWindow().getDecorView().getSystemUiVisibility();
//        int newUiOptions = uiOptions;
//        // END_INCLUDE (get_current_ui_flags)
//        //  BEGIN_INCLUDE (toggle_ui_flags)  
//        boolean isImmersiveModeEnabled =
//                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
//        if (isImmersiveModeEnabled) {
//            Log.i("123", "Turning immersive mode mode off. ");
//        } else {
//            Log.i("123", "Turning immersive mode mode on.");
//        }
//        // Navigation bar hiding:  Backwards compatible to ICS.  
//        if (Build.VERSION.SDK_INT >= 14) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        }
//        // Status bar hiding: Backwards compatible to Jellybean  
//        if (Build.VERSION.SDK_INT >= 16) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
//        }
//        // Immersive mode: Backward compatible to KitKat.  
//        // Note that this flag doesn't do anything by itself, it only augments the behavior  
//        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample  
//        // all three flags are being toggled together.  
//        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".  
//        // Sticky immersive mode differs in that it makes the navigation and status bars  
//        // semi-transparent, and the UI flag does not get cleared when the user interacts with  
//        // the screen.  
//        if (Build.VERSION.SDK_INT >= 18) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        }
////         getWindow().getDecorView().setSystemUiVisibility(newUiOptions);//上边状态栏和底部状态栏滑动都可以调出状态栏  
//        mContext.getWindow().getDecorView().setSystemUiVisibility(4108);//这里的4108可防止从底部滑动调出底部导航栏  
//        //END_INCLUDE (set_ui_flags)  
//
////        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
////            View v = mContext.getWindow().getDecorView();
////            v.setSystemUiVisibility(View.GONE);
////        } else if (Build.VERSION.SDK_INT >= 19) {
////            //for new api versions.
////            View decorView = mContext.getWindow().getDecorView();
////            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
////            decorView.setSystemUiVisibility(uiOptions);
////        }
        if (Build.VERSION.SDK_INT<=22){
            // BEGIN_INCLUDE (get_current_ui_flags) 
            //  The UI options currently enabled are represented by a bitfield.
            //  getSystemUiVisibility() gives us that bitfield.  
            int uiOptions = mContext.getWindow().getDecorView().getSystemUiVisibility();
            int newUiOptions = uiOptions;
            // END_INCLUDE (get_current_ui_flags)
            //  BEGIN_INCLUDE (toggle_ui_flags)  
            boolean isImmersiveModeEnabled =
                    ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
            if (isImmersiveModeEnabled) {
                Log.i("123", "Turning immersive mode mode off. ");
            } else {
                Log.i("123", "Turning immersive mode mode on.");
            }
            // Navigation bar hiding:  Backwards compatible to ICS.  
            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            // Status bar hiding: Backwards compatible to Jellybean  
            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            // Immersive mode: Backward compatible to KitKat.  
            // Note that this flag doesn't do anything by itself, it only augments the behavior  
            // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample  
            // all three flags are being toggled together.  
            // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".  
            // Sticky immersive mode differs in that it makes the navigation and status bars  
            // semi-transparent, and the UI flag does not get cleared when the user interacts with  
            // the screen.  
            if (Build.VERSION.SDK_INT >= 18) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
//         getWindow().getDecorView().setSystemUiVisibility(newUiOptions);//上边状态栏和底部状态栏滑动都可以调出状态栏  
            mContext.getWindow().getDecorView().setSystemUiVisibility(4108);//这里的4108可防止从底部滑动调出底部导航栏  
            //END_INCLUDE (set_ui_flags)
        }


        if (Build.VERSION.SDK_INT>22){
        final View decorView = mContext.getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(flags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                 if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                     decorView.setSystemUiVisibility(flags);
                     }
                 }
            });

        }
    }
    /**
     * 初始化OKGo网络请求
     * @param mContext
     */
    public static void initOKgo(Activity mContext){
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");  //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");  //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-----------------------------------------------------------------------------------//

        //必须调用初始化
        OkGo.init(mContext.getApplication());

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)//全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)  //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)  //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
//             .setCookieStore(new PersistentCookieStore()) //cookie持久化存储，如果cookie不过期，则一直有效
                    //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//                    .setCertificates()                                  //方法一：信任所有证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密
                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })
                    //这两行同上,不需要就不要传
                    .addCommonHeaders(headers) //设置全局公共头
                    .addCommonParams(params);  //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请稍等dialog
     * @param context
     * @param text
     * @param canCancel
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String text, boolean canCancel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_bar, null);

        final Dialog loadingDialog = new Dialog(context, R.style.MyDialog);
        loadingDialog.setContentView(v);
        loadingDialog.setCancelable(false);
        ImageButton imgbtn_guanbi = (ImageButton)v.findViewById(R.id.imgbtn_guanbi);
        if(canCancel) {
            //可以被取消
            imgbtn_guanbi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog.dismiss();
                }
            });
        }

        TextView top_process_promot = (TextView)v.findViewById(R.id.top_process_promot);
        top_process_promot.setText(text);

        return loadingDialog;
    }

    /**
     * 设置TOast的自提大小，及居中
     * @param context
     * @param info
     */
    public static void showToast(Context context,String info,int size){
        Toast toast=null;
        if (toast==null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
            LinearLayout layout = (LinearLayout) toast.getView();
            layout.setBackgroundResource(R.drawable.toast_backgtound_gray);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setPadding(25,0,25,0);
            v.setTextColor(Color.WHITE);
            v.setTextSize(size);
        }else {
            toast.setText(info);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 点击空白处隐藏软键盘
     * @param act
     * @param view
     */
    public static void setupUI(final Activity act, View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    StringUtils.hideSoftKeyboard(act);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                StringUtils.setupUI(act, innerView);
            }
        }
    }
    //隐藏键盘
    public static void hideSoftKeyboard(Context ctx, EditText commentText) {
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
