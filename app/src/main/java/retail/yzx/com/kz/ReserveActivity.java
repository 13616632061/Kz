package retail.yzx.com.kz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Utils.SharedUtil;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.restaurant_nomal.Restaurant_Nomal_MainAcitvity;

/**
 * Created by admin on 2017/3/20.
 */
public class ReserveActivity extends BaseActivity {
    public LinearLayout ll_reserve;
    public EditText ed_reserve;
    private int logntype;//1自助零售，2自助餐饮，3餐饮
    @Override
    protected int getContentId() {
        return R.layout.reserve;
    }

    @Override
    protected void init() {
        super.init();
        ed_reserve = (EditText) findViewById(R.id.ed_reserve);
        ll_reserve = (LinearLayout) findViewById(R.id.ll_reserve);
        Intent intent=getIntent();
        if(intent!=null){
            logntype=intent.getIntExtra("logntype",0);
        }
        findViewById(R.id.ed_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) ll_reserve.getLayoutParams();
                lp1.setMargins(0, 0, 0, 100);
                ll_reserve.setLayoutParams(lp1);
            }
        });
        if (!TextUtils.isEmpty(SharedUtil.getString("reserve"))) {
            if (!SharedUtil.getString("reserve").equals("")) {
                if(logntype==3){
                    Intent intentRestaurant_Nomal=new Intent(ReserveActivity.this,Restaurant_Nomal_MainAcitvity.class);
                    intentRestaurant_Nomal.putExtra("logntype",logntype);
                    startActivity(intentRestaurant_Nomal);
                    finish();
                }else {
                    startActivity(new Intent(ReserveActivity.this, MainActivity.class));
//                    startActivity(new Intent(ReserveActivity.this, new_Main.class));
                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
                    finish();
                }
            }
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.reserve);
//        StringUtils.HideBottomBar(ReserveActivity.this);//隐藏底部状态栏
//        ed_reserve = (EditText) findViewById(R.id.ed_reserve);
//        ll_reserve = (LinearLayout) findViewById(R.id.ll_reserve);
//
//        Intent intent=getIntent();
//        if(intent!=null){
//            logntype=intent.getIntExtra("logntype",0);
//        }
//
//        findViewById(R.id.ed_reserve).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ViewGroup.MarginLayoutParams lp1 = (ViewGroup.MarginLayoutParams) ll_reserve.getLayoutParams();
//                lp1.setMargins(0, 0, 0, 100);
//                ll_reserve.setLayoutParams(lp1);
//            }
//        });
//
//        if (!TextUtils.isEmpty(SharedUtil.getString("reserve"))) {
//            if (!SharedUtil.getString("reserve").equals("")) {
//                if(logntype==3){
//                    Intent intentRestaurant_Nomal=new Intent(ReserveActivity.this,Restaurant_Nomal_MainAcitvity.class);
//                    intentRestaurant_Nomal.putExtra("logntype",logntype);
//                    startActivity(intentRestaurant_Nomal);
//                    finish();
//                }else {
//                    startActivity(new Intent(ReserveActivity.this, MainActivity.class));
//                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
//                    finish();
//                }
//
//            }
//        }
//    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.but_next:
                String reserve=ed_reserve.getText().toString().trim();
                if(TextUtils.isEmpty(reserve)) {
                    reserve = "0";
                }
                SharedUtil.putString("reserve", reserve);
                SetReserve(ed_reserve.getText().toString());
                if(logntype==3){
                    Intent intent=new Intent(ReserveActivity.this,Restaurant_Nomal_MainAcitvity.class);
                    intent.putExtra("logntype",logntype);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(ReserveActivity.this, MainActivity.class));
//                    startActivity(new Intent(ReserveActivity.this, new_Main.class));
                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
                }
                    finish();
//                    Toast.makeText(ReserveActivity.this, "请输入正确格式的金额", Toast.LENGTH_SHORT).show();
                break;
//            点击空白处键盘消逝
            case R.id.ll:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.im_beiyong:
                finish();
                break;
        }
    }

    public void SetReserve(final String reserve) {
        String cashier_id;
        if (SharedUtil.getString("type").equals("4")) {
            cashier_id = SharedUtil.getString("operator_id");
        } else {
            cashier_id = "0";
        }
        OkGo.post(SysUtils.getTestServiceUrl("add_spare"))
                .tag(this)
                .params("map", reserve)
                .params("cashier_id", cashier_id)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "备用金上传" + request.getParams().toString());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                String work_id = jo2.getString("work_id");
                                SharedUtil.putString("work_id", work_id);
                                SharedUtil.putString("reserve", reserve);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
