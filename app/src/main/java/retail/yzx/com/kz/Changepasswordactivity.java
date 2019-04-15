package retail.yzx.com.kz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import Utils.SharedUtil;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/3/20.
 * 修改密码页面
 */
public class Changepasswordactivity extends BaseActivity {
    public RelativeLayout Rl_chang;
    public EditText ed_old,ed_new;
    public TextView tv_error,ed_user;
//    public List<Map<String, String>> mapList=new ArrayList<>();


    @Override
    protected int getContentId() {
        return R.layout.changepassword;
    }


    @Override
    protected void init() {
        super.init();
        Rl_chang= (RelativeLayout) findViewById(R.id.Rl_chang);
        ed_user= (TextView) findViewById(R.id.ed_user);
        ed_old= (EditText) findViewById(R.id.ed_old);
        ed_new= (EditText) findViewById(R.id.ed_new);
        tv_error= (TextView) findViewById(R.id.tv_error);
        findViewById(R.id.ed_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Changepasswordactivity.this,"点击了1111111",Toast.LENGTH_SHORT).show();
                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_chang.getLayoutParams();
                lp1.setMargins(0,0,0,310);
                Rl_chang.setLayoutParams(lp1);
            }
        });
        findViewById(R.id.ed_old).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Changepasswordactivity.this,"点击了1111111",Toast.LENGTH_SHORT).show();
                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_chang.getLayoutParams();
                lp1.setMargins(0,0,0,200);
                Rl_chang.setLayoutParams(lp1);
            }
        });
        findViewById(R.id.ed_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) Rl_chang.getLayoutParams();
                lp1.setMargins(0,0,0,310);
                Rl_chang.setLayoutParams(lp1);
            }
        });

        String username= SharedUtil.getString("seller_name");
        ed_user.setText(username);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.changepassword);
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(Changepasswordactivity.this);
//
//
//    }



    public void onclick(View v){
        switch (v.getId()){
            case R.id.but_change:
                if(ed_user.getText().toString().isEmpty()||ed_old.getText().toString().isEmpty()){
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("账号和密码不能为空");
                }else if(ed_new.getText().toString().isEmpty()){
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("请设置新密码");
                } else if(ed_new.getText().toString().equals(ed_old.getText().toString())){
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("密码不能相同");
                } else if(!checkAccountMark(ed_new.getText().toString())){
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("输入格式不正确");
                }

                Changepaw();
                break;
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.rl_password:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                break;
        }

    }

    //判断输入的密码是什么
    public static boolean checkAccountMark(String account){
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all,account);
    }

    public void Changepaw(){
        OkGo.post(SysUtils.getSellerServiceUrl("updatepwd"))
                .tag(this)
                .params("name", SharedUtil.getString("seller_name"))
                .params("pwd", SharedUtil.getString("password"))
                .params("passwd",ed_new.getText().toString())
                .params("passwdcfm",ed_new.getText().toString())
                .params("type","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","数据是"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(Changepasswordactivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                                SharedUtil.putString("type","0");
                                SharedUtil.putString("password","");
                                startActivity(new Intent(Changepasswordactivity.this, LoginActivity.class));
                            }else {
                                String message=jo1.getString("message");
                                tv_error.setVisibility(View.VISIBLE);
                                tv_error.setText(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }



}
