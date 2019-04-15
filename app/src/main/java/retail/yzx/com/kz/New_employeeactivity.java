package retail.yzx.com.kz;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Entty.Staff_entty;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import widget.Switch;

/**
 * Created by admin on 2017/5/10.
 * 新增
 */
public class New_employeeactivity extends BaseActivity implements Switch.OnCheckedChangeListener, View.OnClickListener {

//    public boolean select1 = false, select2 = false, select3 = false, select4 = false, select5 = false, select6 = false, select7 = false, select8 = false;
    public String select2 = "false", select3 = "false", select4 = "false";
    public TextView tv_im1, tv_im2, tv_im3, tv_im4, tv_im5, tv_im6, tv_im7, tv_im8;
    public boolean permission = false;
    public Switch sw_start;
    public String isChecked = "true";
    public EditText ed_user, ed_name, ed_password, ed_phone, tv_commissions, ed_newpassword,ed_newpassword_confirm;
    public Button but_preserve;
    // 判断是否为手机号码
    public boolean isphone = false;
    public Staff_entty adtas;
    public Intent intent;
    public RelativeLayout rl_password, rl_newpassword,rl_confirm;
    //设置标题
    public TextView tv_title,tv_oldpassword,tv_newpassword;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.new_employee);
//        init1();
//
//        Loadats();
//    }

    @Override
    protected int getContentId() {
        return R.layout.new_employee;
    }

    @Override
    protected void init() {
        super.init();
        adtas = new Staff_entty();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_oldpassword= (TextView) findViewById(R.id.tv_oldpassword);
        tv_newpassword= (TextView) findViewById(R.id.tv_newpassword);
        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
        rl_newpassword = (RelativeLayout) findViewById(R.id.rl_newpassword);
        rl_confirm = (RelativeLayout) findViewById(R.id.rl_confirm);
        ed_user = (EditText) findViewById(R.id.ed_user);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_newpassword = (EditText) findViewById(R.id.ed_newpassword);
        ed_newpassword_confirm = (EditText) findViewById(R.id.ed_newpassword_confirm);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isphone = StringUtils.isPhone(ed_phone.getText().toString());
            }
        });
        tv_commissions = (EditText) findViewById(R.id.tv_commissions);
        but_preserve = (Button) findViewById(R.id.but_preserve);
        but_preserve.setOnClickListener(this);

        sw_start = (Switch) findViewById(R.id.sw_start);

        tv_im1 = (TextView) findViewById(R.id.tv_im1);
        tv_im2 = (TextView) findViewById(R.id.tv_im2);
        tv_im3 = (TextView) findViewById(R.id.tv_im3);
        tv_im4 = (TextView) findViewById(R.id.tv_im4);
        tv_im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!permission) {
                    select2 = "true";
                    select3 = "true";
                    select4 = "true";
                    getDrawable(R.drawable.zidingyi2, tv_im1);
                    getDrawable(R.drawable.zidingyi2, tv_im2);
                    getDrawable(R.drawable.zidingyi2, tv_im3);
                    getDrawable(R.drawable.zidingyi2, tv_im4);
                    permission = true;
                } else {
                    select2 = "false";
                    select3 = "false";
                    select4 = "false";
                    getDrawable(R.drawable.zidingyi1, tv_im1);
                    getDrawable(R.drawable.zidingyi1, tv_im2);
                    getDrawable(R.drawable.zidingyi1, tv_im3);
                    getDrawable(R.drawable.zidingyi1, tv_im4);
                    permission = false;
                }

            }
        });
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        Loadats();
    }

    //    private void init1() {
////        toggleHideyBar();
//
//        adtas = new Staff_entty();
//
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_oldpassword= (TextView) findViewById(R.id.tv_oldpassword);
//        tv_newpassword= (TextView) findViewById(R.id.tv_newpassword);
//        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
//        rl_newpassword = (RelativeLayout) findViewById(R.id.rl_newpassword);
//        rl_confirm = (RelativeLayout) findViewById(R.id.rl_confirm);
//        ed_user = (EditText) findViewById(R.id.ed_user);
//        ed_name = (EditText) findViewById(R.id.ed_name);
//        ed_password = (EditText) findViewById(R.id.ed_password);
//        ed_newpassword = (EditText) findViewById(R.id.ed_newpassword);
//        ed_newpassword_confirm = (EditText) findViewById(R.id.ed_newpassword_confirm);
//        ed_phone = (EditText) findViewById(R.id.ed_phone);
//        ed_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                isphone = StringUtils.isPhone(ed_phone.getText().toString());
//            }
//        });
//        tv_commissions = (EditText) findViewById(R.id.tv_commissions);
//        but_preserve = (Button) findViewById(R.id.but_preserve);
//        but_preserve.setOnClickListener(this);
//
//        sw_start = (Switch) findViewById(R.id.sw_start);
//
//        tv_im1 = (TextView) findViewById(R.id.tv_im1);
//        tv_im2 = (TextView) findViewById(R.id.tv_im2);
//        tv_im3 = (TextView) findViewById(R.id.tv_im3);
//        tv_im4 = (TextView) findViewById(R.id.tv_im4);
//        tv_im1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!permission) {
//                    select2 = "true";
//                    select3 = "true";
//                    select4 = "true";
//                    getDrawable(R.drawable.zidingyi2, tv_im1);
//                    getDrawable(R.drawable.zidingyi2, tv_im2);
//                    getDrawable(R.drawable.zidingyi2, tv_im3);
//                    getDrawable(R.drawable.zidingyi2, tv_im4);
//                    permission = true;
//                } else {
//                    select2 = "false";
//                    select3 = "false";
//                    select4 = "false";
//                    getDrawable(R.drawable.zidingyi1, tv_im1);
//                    getDrawable(R.drawable.zidingyi1, tv_im2);
//                    getDrawable(R.drawable.zidingyi1, tv_im3);
//                    getDrawable(R.drawable.zidingyi1, tv_im4);
//                    permission = false;
//                }
//
//            }
//        });
//    }

    private void Loadats() {
        sw_start.setChecked(true);
        sw_start.setOnCheckedChangeListener(this);
        intent = getIntent();
        if (intent.getSerializableExtra("staff") != null) {
            tv_oldpassword.setText("旧密码");
            tv_newpassword.setText("新密码");
            ed_password.setHint("请输入旧密码");
            rl_confirm.setVisibility(View.VISIBLE);
            tv_title.setText("编辑员工");
            adtas = (Staff_entty) intent.getSerializableExtra("staff");
            if (!adtas.getDisable().equals("") && adtas.getDisable().equals("null")) {
                sw_start.setChecked(Boolean.parseBoolean(adtas.getDisable()));
            }
//            rl_password.setVisibility(View.GONE);
//            rl_newpassword.setVisibility(View.GONE);
            ed_user.setText(adtas.getBn());
            ed_name.setText(adtas.getLogin_name());
            ed_phone.setText(adtas.getPhone());
            tv_commissions.setText(adtas.getRate());
            Log.d("print",""+adtas.getProfit_disable()+adtas.getCost_disable()+adtas.getStore_disable());
            if (Boolean.parseBoolean(adtas.getProfit_disable())){
                select3=adtas.getProfit_disable();
                getDrawable(R.drawable.zidingyi2, tv_im3);
            }
            if (Boolean.parseBoolean(adtas.getCost_disable())){
                select2=adtas.getCost_disable();
                getDrawable(R.drawable.zidingyi2, tv_im2);
            }
            if (Boolean.parseBoolean(adtas.getStore_disable())){
                select4=adtas.getStore_disable();
                getDrawable(R.drawable.zidingyi2, tv_im4);
            }
            if (Boolean.parseBoolean(adtas.getProfit_disable())
                    &&Boolean.parseBoolean(adtas.getCost_disable())
                    &&Boolean.parseBoolean(adtas.getStore_disable())){
                getDrawable(R.drawable.zidingyi2, tv_im1);
            } else {
                getDrawable(R.drawable.zidingyi1, tv_im1);
            }
        }else {
            rl_confirm.setVisibility(View.GONE);
        }
    }

    //点击事件
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_huanghui:
                finish();
                break;
//            case R.id.tv_im1:
//                break;
            case R.id.tv_im2:
                if (!Boolean.parseBoolean(select2)) {
                    getDrawable(R.drawable.zidingyi2, tv_im2);
                    select2 = "true";
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im2);
                    select2 = "false";
                }
                if (Boolean.parseBoolean(select2)&&Boolean.parseBoolean(select3)&&Boolean.parseBoolean(select4)){
                    getDrawable(R.drawable.zidingyi2, tv_im1);
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im1);
                }

                break;
            case R.id.tv_im3:
                if (!Boolean.parseBoolean(select3)) {
                    getDrawable(R.drawable.zidingyi2, tv_im3);
                    select3 = "true";
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im3);
                    select3 = "false";
                }
                if (Boolean.parseBoolean(select2)&&Boolean.parseBoolean(select3)&&Boolean.parseBoolean(select4)){
                    getDrawable(R.drawable.zidingyi2, tv_im1);
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im1);
                }
                break;
            case R.id.tv_im4:
                if (!Boolean.parseBoolean(select4)) {
                    getDrawable(R.drawable.zidingyi2, tv_im4);
                    select4 = "true";
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im4);
                    select4 = "false";
                }
                if (Boolean.parseBoolean(select2)&&Boolean.parseBoolean(select3)&&Boolean.parseBoolean(select4)){
                    getDrawable(R.drawable.zidingyi2, tv_im1);
                } else {
                    getDrawable(R.drawable.zidingyi1, tv_im1);
                }
                break;
            case R.id.tv_im21:
                break;
            case R.id.tv_im22:
                break;
            case R.id.tv_im23:
                break;
            case R.id.tv_im24:
                break;
            case R.id.tv_im25:
                break;
            case R.id.tv_im26:
                break;

        }

    }

    public void add_employee() {
        if (ed_newpassword.getText().toString().equals(ed_password.getText().toString())) {
            Map<String, Object> map = new HashMap<>();
            map.put("disable", isChecked);
            map.put("login_name", ed_name.getText().toString());
            map.put("bn", SharedUtil.getString("seller_name")+ed_user.getText().toString());
            map.put("phone", ed_phone.getText().toString());
            map.put("rate", tv_commissions.getText().toString());
            map.put("login_pass", ed_password.getText().toString());
            map.put("seller_id", SharedUtil.getString("seller_id"));
            if (permission){
                map.put("profit_disable","true");
                map.put("cost_disable","true");
                map.put("store_disable","true");
            }else {
                map.put("profit_disable",select3);
                map.put("cost_disable",select2);
                map.put("store_disable",select4);
            }
            Gson gson = new Gson();
            String string = gson.toJson(map);
            Log.d("print", "传过去的数据是" + string);
            OkGo.post(SysUtils.getSellerServiceUrl("add_employee"))
                    .tag(this)
                    .params("map", string)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d("print", "新增的员工" + s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject jo1 = jsonObject.getJSONObject("response");
                                String status = jo1.getString("status");
                                String message = jo1.getString("message");
                                if (status.equals("200")) {
                                    Toast.makeText(New_employeeactivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(New_employeeactivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(New_employeeactivity.this);
//    }

    //  更换textView右边的图片
    public void getDrawable(int i, TextView view) {
        Drawable drawable = getResources().getDrawable(i);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

    //    开关的点击事件
    @Override
    public void onCheckedChanged(boolean Checked) {
        if (Checked) {
            sw_start.setChecked(true);
            isChecked = "true";
        } else {
            sw_start.setChecked(false);
            isChecked = "false";
        }
    }

    //保存
    @Override
    public void onClick(View view) {
        if (intent.getSerializableExtra("staff") != null) {
            tv_title.setText("编辑员工");
            setEdit_staff();
        } else {
            tv_title.setText("新增员工");
            if (isphone) {
                if (!ed_name.getText().toString().equals("") && !ed_user.getText().toString().equals("")
                        && !ed_password.getText().toString().equals("") && !ed_phone.getText().toString().equals("")) {
                    add_employee();
                }
            } else {
                Toast.makeText(New_employeeactivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void setEdit_staff() {
        if (ed_newpassword.getText().toString().equals(ed_newpassword_confirm.getText().toString())){
        Map<String, Object> map = new HashMap<>();
        map.put("disable", isChecked);
        map.put("login_name", ed_name.getText().toString());
        map.put("bn", ed_user.getText().toString());
        map.put("phone", ed_phone.getText().toString());
        map.put("new_pass",ed_newpassword_confirm.getText().toString());
        map.put("rate", tv_commissions.getText().toString());
        map.put("login_pass", ed_password.getText().toString());
        map.put("seller_id", SharedUtil.getString("seller_id"));
        map.put("work_id", adtas.getWork_id());
        if (permission){
            map.put("profit_disable","true");
            map.put("cost_disable","true");
            map.put("store_disable","true");
        }else {
            map.put("profit_disable",select3);
            map.put("cost_disable",select2);
            map.put("store_disable",select4);
        }
        Gson gson = new Gson();
        String string = gson.toJson(map);
        Log.d("print", "保存消息为" + string);
        OkGo.post(SysUtils.getSellerServiceUrl("edit_employee"))
                .tag(this)
                .params("map", string)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "保存消息为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            String message = jo1.getString("message");
                            if (status.equals("200")) {
                                Toast.makeText(New_employeeactivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(New_employeeactivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        }else {
            Toast.makeText(New_employeeactivity.this,"新密码两次输入不相同",Toast.LENGTH_SHORT).show();
        }
    }
}
