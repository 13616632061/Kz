package retail.yzx.com.kz;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import Connector.Connectors;
import Utils.SharedUtil;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Self_SerciceGuideAcitvity;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import widget.ShapeLoadingDialog;

/**
 * Created by admin on 2017/3/20.
 */
public class LoginActivity extends BaseActivity {

    public EditText ed_user, ed_password,ed_passwords;
    public TextView tv_change, tv_error;
    public RelativeLayout Rl_login;
    public LinearLayout Rl;
    public String singn;
    public RadioButton rb_merchant, rb_clerk;
    public String type = "1";
    public Button but_login;
    long[] mHits = new long[3];//点击3下
    private int logntype;//1自助零售，2自助餐饮，3餐饮


    public ShapeLoadingDialog loadingdialog;


    @Override
    protected int getContentId() {
        return R.layout.login_activity;
    }


    @Override
    protected void init() {
        super.init();
        Intent intent=getIntent();
        if(intent!=null){
            logntype=intent.getIntExtra("logntype",0);//1表示商超自助买单,2表示餐饮自助买单,3餐饮
        }


        //副屏的服务
//        Intent startIntent = new Intent(this,Myservice.class);
//        startService(startIntent);

//      保存数据的初始化
//        SharedUtil.init(this);
        but_login = (Button) findViewById(R.id.but_login);
        rb_merchant = (RadioButton) findViewById(R.id.rb_merchant);
        rb_merchant.performClick();
        rb_clerk = (RadioButton) findViewById(R.id.rb_clerk);
        rb_merchant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "1";
                }
            }
        });
        rb_clerk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    type = "4";
                }
            }
        });

        Rl = (LinearLayout) findViewById(R.id.Rl);
        ed_user = (EditText) findViewById(R.id.ed_user);
        Rl_login = (RelativeLayout) findViewById(R.id.Rl_login);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_passwords = (EditText) findViewById(R.id.ed_passwords);
        ed_passwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_clerk.performClick();
            }
        });
        tv_error = (TextView) findViewById(R.id.tv_error);
        final View view = Rl.getRootView();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开  
                if (isOpen) {

                } else {
                    ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_login.getLayoutParams();
                    lp1.setMargins(0, 0, 0, 0);
                    Rl_login.setLayoutParams(lp1);
                }
            }
        });

        ed_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_login.getLayoutParams();
                lp1.setMargins(0, 0, 0, 320);
                Rl_login.setLayoutParams(lp1);
            }
        });

        ed_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_login.getLayoutParams();
                lp1.setMargins(0, 0, 0, 320);
                Rl_login.setLayoutParams(lp1);
            }
        });

        String username = SharedUtil.getString("seller_name");
        String password = SharedUtil.getString("password");

        if (!TextUtils.isEmpty(username)) {
            ed_user.setText(username);
        }
        if (!TextUtils.isEmpty(password)) {
            ed_password.setText(password);
            type = SharedUtil.getString("type");
            if (type.equals("1")) {
                rb_merchant.setChecked(true);
            } else if (type.equals("4")) {
                rb_clerk.setChecked(true);
            }
        }
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !SharedUtil.getString("type").equals("0")) {
            LoginType();
        }
        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(LoginActivity.this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        StringUtils.initOKgo(LoginActivity.this);
//        setContentView(R.layout.login_activity);
//
//        //    隐藏底部按钮
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(LoginActivity.this);
//
////        ed_user.setText("1005346");
////        ed_password.setText("496289");
//    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(LoginActivity.this);
//    }

    //登录按钮
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.but_login:
//                if(SysUtils.isWifiConnected(LoginActivity.this)){
                if(isnetworknew){
//                if(SysUtils.isNetworkAvailable(LoginActivity.this)|| SysUtils.isNetworkOnline()){
                if (!type.equals("0")) {
                    if (!ed_user.getText().toString().isEmpty() || !ed_password.getText().toString().isEmpty()) {
                        if (ed_user.getText().toString().length() < 30) {
                            OkGo.post(SysUtils.getSellerServiceUrl("sign"))
                                    .tag(this)
                                    .cacheKey("cacheKey")
                                    .connTimeOut(1000)
                                    .cacheMode(CacheMode.DEFAULT)
                                    .params("key", Connectors.LOGIN_KEY)
                                    .execute(new StringCallback() {


                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.d("print", "Sing" + s);
                                            try {
                                                JSONObject jsonObject = new JSONObject(s);
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                singn = jsonObject1.getString("data");
                                                Log.d("print", "SSSSS" + singn);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } finally {
                                                if (ed_passwords.getText().toString().isEmpty()) {//商家登录
                                                    if (!ed_password.getText().toString().isEmpty()){
                                                        doLogin(ed_user.getText().toString().trim(), ed_password.getText().toString().trim(), singn);
                                                    }else {
                                                        tv_error.setVisibility(View.VISIBLE);
                                                        tv_error.setText(getResources().getString(R.string.input_password));
                                                    }
                                                }else {//店员登录
                                                    if (!ed_password.getText().toString().isEmpty()){
                                                        doLogin(ed_user.getText().toString().trim(), ed_password.getText().toString().trim(), singn, ed_passwords.getText().toString().trim());
                                                    }else {
                                                        tv_error.setVisibility(View.VISIBLE);
                                                        tv_error.setText(getResources().getString(R.string.input_password));
                                                    }
                                                }
                                            }
                                        }
                                    });
                        } else {
                            tv_error.setVisibility(View.VISIBLE);
                            tv_error.setText(getResources().getString(R.string.input_30_characters));
                            Toast.makeText(this,getResources().getString(R.string.input_30_characters), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        tv_error.setVisibility(View.VISIBLE);
                        tv_error.setText(getResources().getString(R.string.input_password_account));
                        Toast.makeText(this,getResources().getString(R.string.input_password_account), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.login_type), Toast.LENGTH_SHORT).show();
                }}else {//无网登录
                    String name=ed_user.getText().toString().trim();
                    String shpoper_name=ed_passwords.getText().toString().trim();
                    String pwd=ed_password.getText().toString().trim();
                    Log.e("print", "type: "+type );
                    if(type.equals("1")) {//1商家，4店员
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                            StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.cannot_be_empty), 20);
                        } else {
                            if (name.equals(SharedUtil.getString("seller_username").trim()) && pwd.equals(SharedUtil.getString("seller_password").trim())) {
                                Log.e("print", "开始登录");
                                LoginType();
                            }else {
                                StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.account_password_incorrect), 20);
                            }
                        }
                    }else if(type.equals("4")){
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)||TextUtils.isEmpty(shpoper_name)) {
                            StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.cannot_be_empty), 20);
                        } else {
                            if (name.equals(SharedUtil.getString("seller_name").trim()) && pwd.equals(SharedUtil.getString("shopper_pwd").trim())
                                    &&shpoper_name.equals(SharedUtil.getString("shopper_username").trim())) {
                                LoginType();
                            }else {
                                StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.account_password_incorrect), 20);
                            }
                        }
                    }else if(TextUtils.isEmpty(type)){
                        StringUtils.showToast(LoginActivity.this,getResources().getString(R.string.login_type),20);
                    }
                }
                break;
            //修改密码
            case R.id.tv_change:
                startActivity(new Intent(new Intent(LoginActivity.this, Changepasswordactivity.class)));
                break;
            case R.id.Rl:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    ed_password.setText("");
                    SharedUtil.putString("type", "0");
                    SharedUtil.putString("password", "");
//                  登出设置备用金为空
                    SharedUtil.putString("reserve", "");
                    String isTest;
                    if (SharedUtil.getString("isTest") != null) {
                        isTest = SharedUtil.getString("isTest");
                    } else {
                        isTest = "0";
                        SharedUtil.putString("isTest", "0");
                    }
                    final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this).create();
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.logout_layout);
                    final RadioButton but_Online = (RadioButton) window.findViewById(R.id.but_Online);
                    final RadioButton but_Test = (RadioButton) window.findViewById(R.id.but_Test);
                    but_Online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                but_Online.setChecked(true);
                                SharedUtil.putString("isTest", "0");
                            }
                        }
                    });
                    but_Test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                but_Test.setChecked(true);
                                SharedUtil.putString("isTest", "1");
                            }
                        }
                    });
                    Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                    but_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    Button but_confirm = (Button) window.findViewById(R.id.but_confirm);
                    but_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
        }
    }
    //商家登录的方法
    public void doLogin(final String username, final String password, String sign) {
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(LoginActivity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText(getResources().getString(R.string.logining));
        loadingdialog.show();
        OkGo.post(SysUtils.getServiceUrl("log"))
                .tag(this)
                .cacheKey("cacheKey")
                .connTimeOut(1000)
                .cacheMode(CacheMode.DEFAULT)
                .params(Connectors.USERNAME, username)
                .params(Connectors.PASSWORD, password)
                .params(Connectors.SiGNS, sign)
                .params(Connectors.TYPE, type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "username" + username + "password" + password);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            String message = jo1.getString("message");
                            if (status.equals("200")) {
                                SharedUtil.putString("password", password);
                                JSONObject jo2 = jo1.getJSONObject("data");
                                String token = jo2.getString("token");
                                String seller_name = jo2.getString("seller_name");
                                String id = jo2.getString("id");
                                String type = jo2.getString("type");
                                if (type.equals("4")) {
                                    String operator_id = jo2.getString("work_id");
                                    SharedUtil.putString("operator_id", operator_id);
                                    Log.d("print", "work_id=" + SharedUtil.getString("operator_id"));
                                } else {
                                    SharedUtil.putString("operator_id", id);
                                }
                                SharedUtil.putString("type", type);
                                SharedUtil.putString("seller_token", token);
                                SharedUtil.putString("name", seller_name);
                                SharedUtil.putString("seller_name", username);
                                SharedUtil.putString("seller_id", id);
                                Log.d("print", "商家id" + id);
                                SharedUtil.putString("seller_username",username);//有网时记录商家登录的帐号
                                SharedUtil.putString("seller_password",password);//有网时记录商家登录的帐号密码
                                MiPushClient.setUserAccount(getApplicationContext(), "seller_" + SharedUtil.getString("seller_id"), null);
                                if (SharedUtil.getString("time")==null||SharedUtil.getString("time").equals("")){
                                    getServerTime();
                                }
//                                SharedUtil.putString("time", "2017-07-09 00:00:00");
                                loadingdialog.dismiss();
                                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !SharedUtil.getString("type").equals("0")) {
                                    LoginType();
                                }
                                overridePendingTransition(R.anim.main_in, R.anim.main_out);

                            } else {
                                loadingdialog.dismiss();
                                tv_error.setVisibility(View.VISIBLE);
                                tv_error.setText(message);
                                if (message.equals("API方法不存在")){
                                        String name=ed_user.getText().toString().trim();
                                        String shpoper_name=ed_passwords.getText().toString().trim();
                                        String pwd=ed_password.getText().toString().trim();
                                        Log.e("print", "type: "+type );
                                        if(type.equals("1")) {//1商家，4店员
                                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                                                StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.cannot_be_empty), 20);
                                            } else {
                                                if (name.equals(SharedUtil.getString("seller_username").trim()) && pwd.equals(SharedUtil.getString("seller_password").trim())) {
                                                    Log.e("print", "开始登录");
                                                    LoginType();
                                                }else {
                                                    StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.account_password_incorrect), 20);
                                                }
                                            }
                                        }else if(type.equals("4")){
                                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)||TextUtils.isEmpty(shpoper_name)) {
                                                StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.cannot_be_empty), 20);
                                            } else {
                                                if (name.equals(SharedUtil.getString("seller_name").trim()) && pwd.equals(SharedUtil.getString("shopper_pwd").trim())
                                                        &&shpoper_name.equals(SharedUtil.getString("shopper_username").trim())) {
                                                    LoginType();
                                                }else {
                                                    StringUtils.showToast(LoginActivity.this, getResources().getString(R.string.account_password_incorrect), 20);
                                                }
                                            }
                                        }else if(TextUtils.isEmpty(type)){
                                            StringUtils.showToast(LoginActivity.this,getResources().getString(R.string.login_type),20);
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            getcode();
                        }
                    }
                });
    }
    //登录类型
private void LoginType(){
    if(logntype==1){
        Intent intent_restanraunt=new Intent(LoginActivity.this,Self_SerciceGuideAcitvity.class);
        intent_restanraunt.putExtra("logntype",1);
        startActivity(intent_restanraunt);
        finish();
    }else if(logntype==2){
        Intent intent_restanraunt=new Intent(LoginActivity.this,Self_SerciceGuideAcitvity.class);
        intent_restanraunt.putExtra("logntype",2);
        startActivity(intent_restanraunt);
        finish();
    }else if (logntype==3){
        Intent intent_restanraunt=new Intent(LoginActivity.this,ReserveActivity.class);
        intent_restanraunt.putExtra("logntype",3);
        startActivity(intent_restanraunt);
        finish();
    }else {
        startActivity(new Intent(LoginActivity.this, ReserveActivity.class));
        finish();
    }
}

    //店员登录的方法
    public void doLogin(final String username, final String password, String sign, final String passwords) {
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(LoginActivity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在登录...");
        loadingdialog.show();
        OkGo.post(SysUtils.getServiceUrl("log"))
                .tag(this)
                .cacheKey("cacheKey")
                .connTimeOut(1000)
                .cacheMode(CacheMode.DEFAULT)
                .params(Connectors.USERNAME, username)
                .params(Connectors.PASSWORD, password)
                .params("bncode", SharedUtil.getString("seller_name")+passwords)
                .params(Connectors.SiGNS, sign)
                .params(Connectors.TYPE, type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print",""+s);
                        Log.d("print", "username" + username + "password" + password);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            String message = jo1.getString("message");
                            if (status.equals("200")) {
                                SharedUtil.putString("password", password);
                                JSONObject jo2 = jo1.getJSONObject("data");
                                String token = jo2.getString("token");
                                String seller_name = jo2.getString("seller_name");
                                String id = jo2.getString("id");
                                String type = jo2.getString("type");
                                if (type.equals("4")) {
                                    String operator_id = jo2.getString("work_id");
                                    Log.d("print", "work_id=" + operator_id);
                                    SharedUtil.putString("operator_id", operator_id);
                                    Log.d("print", "work_id=" + SharedUtil.getString("operator_id"));
                                } else {
                                    SharedUtil.putString("operator_id", id);
                                }
                                SharedUtil.putString("type", type);
                                SharedUtil.putString("seller_token", token);
//                                SharedUtil.putString("name", seller_name);
                                SharedUtil.putString("seller_id", id);
                                getServerTime();
//                                SharedUtil.putString("time", DateUtils.getCurDate());
                                SharedUtil.putString("seller_name", username);
                                SharedUtil.putString("shopper_username",passwords);
                                SharedUtil.putString("shopper_pwd",password);
                                loadingdialog.dismiss();
                                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !SharedUtil.getString("type").equals("0")) {
                                   LoginType();
                                }
                                overridePendingTransition(R.anim.main_in, R.anim.main_out);

                            } else {
                                loadingdialog.dismiss();
                                tv_error.setVisibility(View.VISIBLE);
                                tv_error.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            getcode();
                            get_power();
                        }
                    }
                });
    }

    //无网收银获取固定二维码
    public void getcode() {
        OkGo.post(SysUtils.getSellerServiceUrl("url_turn_qrcode"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "生成固定二维码为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data = jo1.getString("data");
                                if (SharedUtil.getString("seller_id") != null) {
                                    if (!SharedUtil.getString("seller_id").equals("null")) {
                                        if (!SharedUtil.getString("seller_id").equals("")){
                                            String url = String.format(data, Integer.parseInt(SharedUtil.getString("seller_id")));
                                            SharedUtil.putString("url", url);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //权限的获取
    public void get_power() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_power"))
                .tag(this)
                .params("work_id", SharedUtil.getString("operator_id"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "获取的权限是" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                SharedUtil.putString("profit_disable", jo2.getString("profit_disable"));
                                SharedUtil.putString("cost_disable", jo2.getString("cost_disable"));
                                SharedUtil.putString("store_disable", jo2.getString("store_disable"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }


    //获取服务器时间
    public void getServerTime(){
        OkGo.get(SysUtils.getSellerServiceUrl("getServerTime"))
                .tag(this)
                .cacheKey("cacheKey")
                .connTimeOut(1000)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print获取的时间是",""+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")){
                                JSONObject data=jsonObject1.getJSONObject("data");
                                String time=data.getString("time");
                                SharedUtil.putString("time", time);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}


