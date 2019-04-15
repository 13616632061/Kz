package retail.yzx.com.kz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.DateUtils;
import Utils.PrintWired;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.TlossUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import widget.ShapeLoadingDialog;

import static Utils.BluetoothPrintFormatUtil.PrintTransfer;

/**
 * Created by admin on 2017/4/25.
 * 交接班的界面
 */
public class Handover_activity extends BaseActivity {

    public Button but_quit, but_Cashbox,but_goout;
    public TextView tv_starttime, tv_endtime;
    public ImageView im_huanhui;
    public TextView tv_name, tv_standby, tv_yingyoujiner, tv_total, tv_unms, tv_guadanjiner,tv_payment;

    //挂单数量
    public String guandan = "0";

    public String micro_money = "0", cash_money = "0", total_num = "0";

    public EditText ed_money, tv_other,ed_reard,ed_cash_hand;
    private int logntype=0;

    public ShapeLoadingDialog loadingdialog;


    @Override
    protected int getContentId() {
        return R.layout.handover_layout;
    }

    @Override
    protected void init() {
        super.init();

        Intent intent=getIntent();
        if(intent!=null){
            logntype=intent.getIntExtra("logntype",0);
        }
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(SharedUtil.getString("name"));

        tv_standby = (TextView) findViewById(R.id.tv_standby);
        tv_yingyoujiner = (TextView) findViewById(R.id.tv_yingyoujiner);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_unms = (TextView) findViewById(R.id.tv_unms);
        tv_guadanjiner = (TextView) findViewById(R.id.tv_guadanjiner);

        tv_payment = (TextView) findViewById(R.id.tv_payment);

        ed_money = (EditText) findViewById(R.id.ed_money);
        tv_other = (EditText) findViewById(R.id.tv_other);
        ed_reard = (EditText) findViewById(R.id.ed_reard);
        ed_cash_hand = (EditText) findViewById(R.id.ed_cash_hand);

        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_endtime);
        String time = SharedUtil.getString("time");
        if (!TextUtils.isEmpty(time)) {
            //获取时间
            tv_starttime.setText(time);

        }

        im_huanhui = (ImageView) findViewById(R.id.im_huanhui);
        im_huanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Handover_activity.this,MainActivity.class));
                finish();
            }
        });


        but_goout= (Button) findViewById(R.id.but_goout);
        but_goout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guandan!=null&&!guandan.equals("null")){
//                if (Integer.valueOf(guandan) > 0) {
//                    getpopup();
//                } else {
                    String syy=PrintTransfer(tv_name.getText().toString(),tv_payment.getText().toString(),tv_yingyoujiner.getText().toString(),
                            "",tv_total.getText().toString(),tv_unms.getText().toString(),ed_money.getText().toString(),tv_other.getText().toString(),
                            ed_reard.getText().toString(),tv_starttime.getText().toString(),tv_endtime.getText().toString());
                    PrintWired.usbPrint(Handover_activity.this,syy);
                    UpDatas();
//                }
                }else {
                    String syy=PrintTransfer(tv_name.getText().toString(),tv_payment.getText().toString(),tv_yingyoujiner.getText().toString(),
                            "",tv_total.getText().toString(),tv_unms.getText().toString(),ed_money.getText().toString(),tv_other.getText().toString(),
                            ed_reard.getText().toString(),tv_starttime.getText().toString(),tv_endtime.getText().toString());
                    PrintWired.usbPrint(Handover_activity.this,syy);
                    UpDatas();
                }
            }
        });

        but_quit = (Button) findViewById(R.id.but_quit);
        but_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guandan!=null&&!guandan.equals("null")) {
                    if (Integer.valueOf(guandan) > 0) {
                        getpopup();
                    } else {
                        String syy = PrintTransfer(tv_name.getText().toString(), tv_payment.getText().toString(), tv_yingyoujiner.getText().toString(),
                                "", tv_total.getText().toString(), tv_unms.getText().toString(), ed_money.getText().toString(), tv_other.getText().toString(),
                                ed_reard.getText().toString(), tv_starttime.getText().toString(), tv_endtime.getText().toString());
                        PrintWired.usbPrint(Handover_activity.this, syy);
                        UpDatas();
                    }
                }else {
                    String syy = PrintTransfer(tv_name.getText().toString(), tv_payment.getText().toString(), tv_yingyoujiner.getText().toString(),
                            "", tv_total.getText().toString(), tv_unms.getText().toString(), ed_money.getText().toString(), tv_other.getText().toString(),
                            ed_reard.getText().toString(), tv_starttime.getText().toString(), tv_endtime.getText().toString());
                    PrintWired.usbPrint(Handover_activity.this, syy);
                    UpDatas();
                }

            }
        });
        but_Cashbox = (Button) findViewById(R.id.but_Cashbox);
        but_Cashbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SysUtils.getSystemModel().equals("rk3288")){
                    SysUtils.OpennewCashbox(Handover_activity.this);
                }else {
                    SysUtils.OpenCashbox();
                }
            }
        });
        tv_standby.setText("备用金：" + SharedUtil.getString("reserve"));
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();

        getServerTime();

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.handover_layout);
//        init1();
//    }



//    private void init1() {
//        Intent intent=getIntent();
//        if(intent!=null){
//            logntype=intent.getIntExtra("logntype",0);
//        }
//        tv_name = (TextView) findViewById(R.id.tv_name);
//        tv_name.setText(SharedUtil.getString("name"));
//
//        tv_standby = (TextView) findViewById(R.id.tv_standby);
//        tv_yingyoujiner = (TextView) findViewById(R.id.tv_yingyoujiner);
//        tv_total = (TextView) findViewById(R.id.tv_total);
//        tv_unms = (TextView) findViewById(R.id.tv_unms);
//        tv_guadanjiner = (TextView) findViewById(R.id.tv_guadanjiner);
//
//        tv_payment = (TextView) findViewById(R.id.tv_payment);
//
//        ed_money = (EditText) findViewById(R.id.ed_money);
//        tv_other = (EditText) findViewById(R.id.tv_other);
//        ed_reard = (EditText) findViewById(R.id.ed_reard);
//        ed_cash_hand = (EditText) findViewById(R.id.ed_cash_hand);
//
//        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
//        tv_endtime = (TextView) findViewById(R.id.tv_endtime);
//        String time = SharedUtil.getString("time");
//        if (!TextUtils.isEmpty(time)) {
//            //获取时间
//            tv_starttime.setText(SharedUtil.getString("time"));
//            tv_endtime.setText(DateUtils.getCurDate());
//        }
//
//        im_huanhui = (ImageView) findViewById(R.id.im_huanhui);
//        im_huanhui.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        but_quit = (Button) findViewById(R.id.but_quit);
//        but_quit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Integer.valueOf(guandan) > 0) {
//                    getpopup();
//                } else {
//
//                    String syy=PrintTransfer(tv_name.getText().toString(),tv_payment.getText().toString(),tv_yingyoujiner.getText().toString(),
//                            "",tv_total.getText().toString(),tv_unms.getText().toString(),ed_money.getText().toString(),tv_other.getText().toString(),
//                            ed_reard.getText().toString(),tv_starttime.getText().toString(),tv_endtime.getText().toString());
//                    PrintWired.usbPrint(Handover_activity.this,syy);
//                    UpDatas();
//                }
//
//            }
//        });
//        but_Cashbox = (Button) findViewById(R.id.but_Cashbox);
//        but_Cashbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    HdxUtil.SetV12Power(1);
//                } catch (UnsatisfiedLinkError e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        tv_standby.setText("备用金：" + SharedUtil.getString("reserve"));
//        //获取数据
//        getDatas();
//
//    }



    //获取服务器时间
    public void getServerTime(){

        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(Handover_activity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在登出...");
        loadingdialog.show();

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
                                tv_endtime.setText(time);
                                //获取数据
                                getDatas();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    //  获取数据
    public void getDatas() {
        Log.d("print","开始的时间"+tv_starttime.getText().toString()+"     "+tv_endtime.getText().toString());
        if (SharedUtil.getString("work_id") != null) {
            Log.d("print", SharedUtil.getString("operator_id"));
        }
        OkGo.post(SysUtils.getTestServiceUrl("list"))
                .tag(this)
                .params("work_id", SharedUtil.getString("work_id"))
                .params("begin_time", tv_starttime.getText().toString())
                .params("end_time", tv_endtime.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "交接班数据" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                String count = jo2.getString("count");
                                tv_unms.setText(count);
                                total_num = count;
                                String sum_cash = jo2.getString("sum_cash");
                                cash_money = sum_cash;
                                String sum_spare = jo2.getString("sum_spare");
                                if (sum_spare.equals("null")) {
                                    sum_spare="0";
                                }
                                double yingyou = 0;
                                if (!TextUtils.isEmpty(sum_spare)&&!TextUtils.isEmpty(sum_cash)&& !"null".equals(sum_cash)&& !"null".equals(sum_spare) ) {
                                    yingyou = TlossUtils.add(Double.parseDouble(sum_cash) , Double.parseDouble(sum_spare));
                                }

                                tv_yingyoujiner.setText(yingyou + "");

                                String sum_micro = jo2.getString("sum_micro");
                                tv_payment.setText(sum_micro );
                                micro_money = sum_micro;
                                yingyou=0;
                                yingyou =TlossUtils.add(Double.valueOf(sum_cash),Double.valueOf(sum_micro));
                                tv_total.setText(yingyou + "");
                                guandan = jo2.getString("guadan_num");
                                tv_guadanjiner.setText(guandan);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            loadingdialog.dismiss();
                        }
                    }
                });
    }

    //    提示
    public void getpopup() {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_untreated);
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
    }

    //交接班上传数据
    public void UpDatas() {
        Map<String, String> map = new HashMap<>();
        map.put("total_money", tv_total.getText().toString());
        map.put("micro_money", micro_money);
        map.put("cash_money", cash_money);
        map.put("guadan_num", guandan);
        map.put("remark",ed_reard.getText().toString());
        map.put("total_num", total_num);
        map.put("begin_time", DateUtils.data(tv_starttime.getText().toString()));
        map.put("store_money",ed_cash_hand.getText().toString());
        if (tv_other.getText().toString().equals("")) {
            map.put("other_money", "0");
        } else {
            map.put("other_money", tv_other.getText().toString());
        }
        map.put("work_id", SharedUtil.getString("work_id"));
        map.put("seller_id", SharedUtil.getString("seller_id"));
        if (SharedUtil.getString("type").equals("4")) {
            map.put("cashier_id", "0");
        } else {
            map.put("cashier_id", SharedUtil.getString("seller_id"));
        }
        map.put("end_time", DateUtils.data(tv_endtime.getText().toString()));
        if (SharedUtil.getString("type").equals("4")) {
            map.put("cashier_id", SharedUtil.getString("operator_id"));
            Log.e("print", "上传的数据是" + SharedUtil.getString("operator_id"));
        } else {
            map.put("cashier_id", "0");
        }
        if (ed_money.getText().toString().equals("")) {
            map.put("turn_in_money", "0");
        } else {
            map.put("turn_in_money", ed_money.getText().toString());
        }
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.e("print", "上传的数据是" + json);

        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(Handover_activity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在登出...");
        loadingdialog.show();

        OkGo.post(SysUtils.getTestServiceUrl("logout"))
                .tag(this)
                .params("map", json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                SharedUtil.putString("type", "0");
                                SharedUtil.putString("password", "");
//                              登出设置备用金为空
                                SharedUtil.putString("reserve", "");
                                SharedUtil.putString("time","");
                                MiPushClient.unsetUserAccount(getApplicationContext(), "seller_" + SharedUtil.getString("seller_id"), null);
                                Intent intent_restanraunt=new Intent(Handover_activity.this, LoginActivity.class);
                                intent_restanraunt.putExtra("logntype",logntype);
                                startActivity(intent_restanraunt);
                                loadingdialog.dismiss();
                                Handover_activity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
