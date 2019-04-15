package base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;

import java.util.Locale;

import Utils.NetUtil;
import Utils.ScreenUtil;
import butterknife.ButterKnife;
import retail.yzx.com.kz.DemoApplication;
import retail.yzx.com.kz.MainActivity;
import retail.yzx.com.kz.NetBroadcastReceiver;
import retail.yzx.com.kz.R;


/**
 * Created by YGD on 2016/9/26.9:47
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {

    private FragmentManager fragmentManager;
    //当前正在展示的Fragment
    private BaseFragment showFragment;
    private BaseActivity oContext;
    private DemoApplication application;

    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;
    public static boolean isnetworknew;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        en();
        setContentView(getContentId());

        storageFragment(savedInstanceState);
        evevt = (NetBroadcastReceiver.NetEvevt) this;
        isnetworknew = inspectNet();

        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(this);

        //注册activity
        ButterKnife.bind(this);

        //获得FragmentManager对象
        fragmentManager = getSupportFragmentManager();

        /**
         * 沉浸式状态栏
         */
        if (isOpenStatus()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            //获得状态栏的高度
            int height = ScreenUtil.getStatusHeight(this);
            if (height != -1) {
                //设置Padding
                View view = findViewById(R.id.actionbar);
                if (view != null) {
                    view.setPadding(0, height, 0, 0);

                    if (view instanceof Toolbar) {
                        setSupportActionBar((Toolbar) view);

                        //隐藏标题
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
            }
        }

        if (application == null) {
            // 得到Application对象
            application = (DemoApplication) getApplication();
        }

        oContext = this;

        init();
        bindListener();
        loadDatas();

        addActivity();

    }


    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect();

        // if (netMobile == 1) {
        // System.out.println("inspectNet：连接wifi");
        // } else if (netMobile == 0) {
        // System.out.println("inspectNet:连接移动数据");
        // } else if (netMobile == -1) {
        // System.out.println("inspectNet:当前没有网络");
        //
        // }
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        isnetworknew = isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == 2) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }


    public void storageFragment(Bundle savedInstanceState) {

    }

    /**
     * 初始化
     */
    protected void init() {

    }

    /**
     * 绑定监听
     */
    protected void bindListener() {

    }

    /**
     * 加载数据
     */
    protected void loadDatas() {

    }

    /**
     * 以动画的方式启动activity
     *
     * @param intent
     * @param animinid
     * @param animoutid
     */
    public void startActivityForAnimation(Intent intent, int animinid, int animoutid) {
        startActivity(intent);
        overridePendingTransition(animinid, animoutid);
    }

    /**
     * 展示Fragment
     */
    protected void showFragment(int resid, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //隐藏正在暂时的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }

        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }
        fragmentTransaction.commit();
    }

    /**
     * 获得activity显示的布局ID
     *
     * @return
     */
    protected abstract int getContentId();

    /**
     * 是否打开沉浸式状态栏
     *
     * @return
     */
    public boolean isOpenStatus() {
        return true;
    }


    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();
    }


    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    /**
     * 切换英文
     */
    public void en() {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = Locale.ENGLISH; // 英文
        resources.updateConfiguration(config, dm);
//        finish();//如果不重启当前界面，是不会立马修改的
//        startActivity(new Intent(this,MainActivity.class));
    }

    /**
     * 切换中文
     */
    public void cn() {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = Locale.CHINA; // 简体中文
        resources.updateConfiguration(config, dm);
//        finish();////如果不重启当前界面，是不会立马修改的
//        startActivity(new Intent(this,MainActivity.class));
    }

}
