package retail.yzx.com.kz;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import base.BaseActivity;

/**
 * Created by admin on 2017/5/20.
 * 新增角色
 */
public class New_role extends BaseActivity {

    //判断是否全选
    public boolean permission=false;

    public RelativeLayout rl_permissions;
    public LinearLayout ll_permissions,ll_more;

    //判断权限是否展开
    public boolean unfold=false,more=false;


    @Override
    protected int getContentId() {
        return R.layout.new_role;
    }

    @Override
    protected void init() {
        super.init();
        rl_permissions= (RelativeLayout) findViewById(R.id.rl_permissions);
        ll_permissions= (LinearLayout) findViewById(R.id.ll_permissions);
        ll_more= (LinearLayout) findViewById(R.id.ll_more);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.new_role);
//        //初始化
////        init1();
//        //加载数据
//        Loadats();
//
//    }
//    private void init1() {
//
//        rl_permissions= (RelativeLayout) findViewById(R.id.rl_permissions);
//        ll_permissions= (LinearLayout) findViewById(R.id.ll_permissions);
//        ll_more= (LinearLayout) findViewById(R.id.ll_more);
////        toggleHideyBar();
//    }




    //点击事件
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.im_finish:
                finish();
                break;
            case R.id.rl_permissions:
                if (!unfold){
                    ll_permissions.setVisibility(View.VISIBLE);
                    unfold=true;
                }else {
                    ll_permissions.setVisibility(View.GONE);
                    unfold=false;
                }
                break;
            case R.id.rl_more:
                if (!more){
                    ll_more.setVisibility(View.VISIBLE);
                    more=true;
                }else {
                    ll_more.setVisibility(View.GONE);
                    more=false;
                }
                break;
        }
    }
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
////        //END_INCLUDE (set_ui_flags)  
//
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
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(New_role.this);
//    }

    //点击编辑框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
