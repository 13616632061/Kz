package retail.yzx.com.kz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.xw.repo.XEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Check_tuihuo;
import Utils.SysUtils;
import adapters.Check_Adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/4/21.
 * 查单的界面
 */
public class Check_Activity extends BaseActivity implements View.OnClickListener {

    public Button but_seek;
    public XEditText ed_seek;
    public ImageView im_huanghui;
    public ListView lv_check;
    public Check_Adapter adapter;

    public List<Check_tuihuo> adats;



    @Override
    protected int getContentId() {
        return R.layout.check_activity;
    }


    @Override
    protected void init() {
        super.init();
        adats=new ArrayList<>();
        but_seek= (Button) findViewById(R.id.but_seek);
        but_seek.setOnClickListener(this);
        ed_seek= (XEditText) findViewById(R.id.ed_seek);
        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);

        adapter=new Check_Adapter(this);

        lv_check= (ListView) findViewById(R.id.lv_check);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.check_activity);
//
////        init1();
//    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction("com.yzx.refund");
        registerReceiver(broadcastReceiver, intentFilter);

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.yzx.refund")){
                finish();
            }
        }
    };

//    private void init1() {
//
//
//    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_seek:
                String str=ed_seek.getText().toString();
                getSeekadats(str);
                break;
            case R.id.im_huanghui:
                finish();
                break;

        }
    }

    //搜索返回的数据
    public void getSeekadats(String str){
        OkGo.post(SysUtils.getSellerServiceUrl("search_order"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","String"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                adats.clear();

                                JSONObject jo2=jo1.getJSONObject("data");
                                JSONArray micro=jo2.getJSONArray("micro");
                                JSONArray cash=jo2.getJSONArray("cash");
                                    if (cash.length()>0){
                                        for (int i=0;i<cash.length();i++){
                                            Check_tuihuo check_tuihuo=new Check_tuihuo();
                                            JSONObject jo3=cash.getJSONObject(i);
                                        check_tuihuo.setPayment("cash");
                                        check_tuihuo.setCash_id(jo3.getString("cash_id"));
                                        check_tuihuo.setCash_time(jo3.getString("cash_time"));
                                        check_tuihuo.setAmount_receivable(jo3.getString("amount_receivable"));
                                        check_tuihuo.setReceive_amount(jo3.getString("receive_amount"));
                                        check_tuihuo.setAdd_change(jo3.getString("add_change"));
                                        check_tuihuo.setSeller_cash_name(jo3.getString("seller_name"));
                                       adats.add(check_tuihuo);
                                        }
                                    }
                                 if (micro.length()>0){
                                        for (int i=0;i<micro.length();i++) {
                                            Check_tuihuo check_tuihuo=new Check_tuihuo();
                                            JSONObject jo3 = micro.getJSONObject(i);
                                            check_tuihuo.setPayment("micro");
                                            check_tuihuo.setOrder_id(jo3.getString("order_id"));
                                            check_tuihuo.setCreatetime(jo3.getString("createtime"));
                                            check_tuihuo.setMark_text(jo3.getString("mark_text"));
                                            check_tuihuo.setFinal_amount(jo3.getString("final_amount"));
                                            check_tuihuo.setSeller_name(jo3.getString("seller_name"));
                                            adats.add(check_tuihuo);
                                        }
                                        }
//                                    String payment = jo2.getString("payment");
//                                    if (payment.equals("micro")) {
//                                        check_tuihuo.setPayment(payment);
//                                        check_tuihuo.setOrder_id(jo2.getString("order_id"));
//                                        check_tuihuo.setCreatetime(jo2.getString("createtime"));
//                                        check_tuihuo.setMark_text(jo2.getString("mark_text"));
//                                        check_tuihuo.setFinal_amount(jo2.getString("final_amount"));
//                                        check_tuihuo.setSeller_name(jo2.getString("seller_name"));
//                                    } else if (payment.equals("cash")) {
//
//                                    }

                                Log.d("print","shuliang"+adats.size());
                                adapter.getAdats(adats);
                                lv_check.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                String message=jo1.getString("message");
                                Toast.makeText(Check_Activity.this,message,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

                        }
                    }
                });


    }


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
            int[] leftTop = { 0, 0 };
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
