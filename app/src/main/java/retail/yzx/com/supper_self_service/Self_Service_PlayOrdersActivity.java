package retail.yzx.com.supper_self_service;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Connector.Connectors;
import Utils.BluetoothPrintFormatUtil;
import Utils.PrintUtil;
import Utils.ScanGunKeyEventHelper;
import Utils.SharedUtil;
import Utils.SysUtils;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Adapter.Self_Service_PlayOrdersAdapter;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.QRCodeUtil;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import shujudb.SqliteHelper;


public class Self_Service_PlayOrdersActivity extends AppCompatActivity implements ScanGunKeyEventHelper.OnScanSuccessListener, View.OnClickListener {

    private ImageView iv_logo,iv_paycode;
    private TextView tv_sellername,tv_phone,tv_totalnums,tv_totalprice,tv_bottom,tv_money,tv_paymoney,tv_orderid,tv_ordertime;
    private ListView list_content;
    private Button btn_ok,btn_continue,btn_cell,btn_timecell;
    private RelativeLayout layout_left;
    private View activity_self__service__play_orders;
    private View layout_select_ok,layout_scancodepay,layout_paysuccess;
    private ScanGunKeyEventHelper mScanGunKeyEventHelper;
    private Self_Service_PlayOrdersAdapter mSelf_Service_PlayOrdersAdapter;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist;
    private boolean isScanCode=true;//是否可以扫描商品
    private int n=3;
    private int pre_totalnums=0;
    private int time=60;
    private  Timer GetPatStatus_timer;//获取订单状态的定时器
    private  Timer threeTime_timer;//3秒关闭
    private  Timer isHandle_timer;//页面是否操作定时器
    private int selfservice_nums;//自助收银商品限制0表示不限制
    public List<Map<String, String>> mapList;
    private int pay_type=0;//0表示正扫1表示反扫
    private String orderS_id;
    private String tel;
    private Dialog progressDialog = null;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                        if(n>0){
                            btn_timecell.setText(n+"秒后关闭");
                            n--;
                        }else {
                            mHandler.removeMessages(200);
                            finish();
                        }
                    break;
                case 201:
                    if(time>0){
                        time= time-5;
                    }else {
                        StringUtils.showToast(Self_Service_PlayOrdersActivity.this,"支付超时，请重新支付！",50);
                        mHandler.removeMessages(201);
                        mHandler.removeMessages(202);
                        finish();
                    }

                    break;
                case 202:
                    time=60;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringUtils.initOKgo(Self_Service_PlayOrdersActivity.this);//初始化网络请求
        setContentView(R.layout.activity_self__service__play_orders);
        StringUtils.HideBottomBar(Self_Service_PlayOrdersActivity.this);
        SharedUtil.init(this);
        initView();
    }

    private void initView() {
        activity_self__service__play_orders=super.findViewById(R.id.activity_self__service__play_orders);
        iv_logo= (ImageView) findViewById(R.id.iv_logo);
        layout_left= (RelativeLayout) findViewById(R.id.layout_left);
        tv_sellername= (TextView) findViewById(R.id.tv_sellername);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_totalnums= (TextView) findViewById(R.id.tv_totalnums);
        tv_totalnums= (TextView) findViewById(R.id.tv_totalnums);
        tv_totalprice= (TextView) findViewById(R.id.tv_totalprice);
        tv_bottom= (TextView) findViewById(R.id.tv_bottom);
        list_content= (ListView) findViewById(R.id.list_content);
        layout_select_ok=findViewById(R.id.layout_select_ok);
        layout_scancodepay=findViewById(R.id.layout_scancodepay);
        layout_paysuccess=findViewById(R.id.layout_paysuccess);
        btn_ok= (Button)layout_select_ok.findViewById(R.id.btn_ok);
        iv_paycode= (ImageView) layout_scancodepay.findViewById(R.id.iv_paycode);
        tv_money= (TextView) layout_scancodepay.findViewById(R.id.tv_money);
        btn_continue= (Button) layout_scancodepay.findViewById(R.id.btn_continue);
        btn_cell= (Button) layout_scancodepay.findViewById(R.id.btn_cell);
        tv_paymoney= (TextView) layout_paysuccess.findViewById(R.id.tv_paymoney);
        btn_timecell= (Button) layout_paysuccess.findViewById(R.id.btn_timecell);
        tv_orderid= (TextView) layout_paysuccess.findViewById(R.id.tv_orderid);
        tv_ordertime= (TextView) layout_paysuccess.findViewById(R.id.tv_ordertime);


        mScanGunKeyEventHelper= new ScanGunKeyEventHelper(this);
        mSelf_Service_GoodsInfolist=new ArrayList<>();

        tv_sellername.setText(SharedUtil.getString("name"));
        GetSellerPhone();
        String selfservice_nums_str=SharedUtil.getString("selfservice_nums");
        if(TextUtils.isEmpty(selfservice_nums_str)){
            selfservice_nums=0;
        }else {
            selfservice_nums=Integer.parseInt(selfservice_nums_str);
        }
        btn_ok.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_cell.setOnClickListener(this);
        btn_timecell.setOnClickListener(this);

        //定时判断界面是否操作
        isHandle_timer=new Timer();
        isHandle_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int total_nums=0;
               for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
                   String nums_str=mSelf_Service_GoodsInfolist.get(i).getNumber();
                   int nums=Integer.parseInt(nums_str);
                   total_nums+=nums;
               }
                if(pre_totalnums==total_nums){
                    mHandler.sendEmptyMessage(201);
                }else {
                    mHandler.sendEmptyMessage(202);
                }
                pre_totalnums=total_nums;
            }
        }, 1000,5000);
    }

    /**
     * Activity截获按键事件.发给ScanGunKeyEventHelper
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//            if(event.getKeyCode()!=KeyEvent.KEYCODE_BACK){
        mScanGunKeyEventHelper.analysisKeyEvent(event);
//            }
        return true;
    }
    @Override
    public void onScanSuccess(String barcode) {
        Log.e("barcode",barcode);
        Log.d("print","执行到这里0"+barcode);
        if(isScanCode){
            int total_nums=0;
            for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
                    String num_str= mSelf_Service_GoodsInfolist.get(i).getNumber();
                    int num=Integer.parseInt(num_str);
                total_nums+=num;
            }
                if(selfservice_nums>total_nums||selfservice_nums==0){
                    getScanCodeInfo(barcode);
                }else {
                    StringUtils.showToast(Self_Service_PlayOrdersActivity.this,"自助买单限"+selfservice_nums+"件商品,请前往人工收银台收银，谢谢配合！",50);
                }
        }
        if(pay_type==1) {
            uppay(barcode);

        }
    }
    //获取扫码的商品信息，并显示
    private void getScanCodeInfo(String barcode){
        SqliteHelper dbHelper = SqliteHelper.getInstance(Self_Service_PlayOrdersActivity.this);
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "select * from commodity where bncode=?";
        Cursor cursor = sqlite.rawQuery(sql, new String[]{barcode});
        if(cursor.moveToFirst()){
            do {
                String goods_id= cursor.getString(cursor.getColumnIndex("goods_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String price= cursor.getString(cursor.getColumnIndex("price"));
                String cost= cursor.getString(cursor.getColumnIndex("cost"));
                String bncode= cursor.getString(cursor.getColumnIndex("bncode"));
                String store= cursor.getString(cursor.getColumnIndex("store"));
                String tag_id= cursor.getString(cursor.getColumnIndex("tag_id"));
                boolean isexit=false;
                for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
                    if(goods_id.equals(mSelf_Service_GoodsInfolist.get(i).getGoods_id())){
                        String num_str= mSelf_Service_GoodsInfolist.get(i).getNumber();
                        int num=Integer.parseInt(num_str);
                        mSelf_Service_GoodsInfolist.get(i).setNumber((num+1)+"");
                        isexit=true;
                    }
                }
                if(!isexit){
                    Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,"1",cost,price,"","","",tag_id);
                    mSelf_Service_GoodsInfolist.add(mSelf_Service_GoodsInfo);
                }
            } while (cursor.moveToNext());

            mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_PlayOrdersActivity.this,mSelf_Service_GoodsInfolist,true,1);
            list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
            setTotalPrice();
        }else {
            StringUtils.showToast(Self_Service_PlayOrdersActivity.this,"商品不存在或条码有误，请重新扫描或咨询营业员！",25);

        }
    }

    /**
     * 生成支付二维码
     */
    private void GoToPay(){
        ArrayList<Map<String,String>> mapList=new ArrayList<>();
        for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
            String goods_id= mSelf_Service_GoodsInfolist.get(i).getGoods_id();
            String name = mSelf_Service_GoodsInfolist.get(i).getName();
            String price= mSelf_Service_GoodsInfolist.get(i).getPrice();
            String cost= mSelf_Service_GoodsInfolist.get(i).getCost();
            String nums= mSelf_Service_GoodsInfolist.get(i).getNumber();
            Map<String,String> map=new HashMap<>();
            map.put("goods_id",goods_id);
            map.put("name",name);
            map.put("number",nums);
            map.put("cost",cost);
            map.put("price",price);
            map.put("orders_status","1");
            map.put("pay_status","0");
            mapList.add(map);
        }
        Gson gson = new Gson();
         String str = gson.toJson(mapList);
        final String total_price_str=tv_totalprice.getText().toString().trim().replace("￥","").trim();
        double total_price=Double.parseDouble(total_price_str);

        OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("total_fee", (int)(total_price * 100))
//                .params("total_fee", 1)
                .params("commodity", str)
                .params("operator_id", SharedUtil.getString("operator_id"))
                .params("auth_code", 111111111)
                .params("pay_type", "wxpayjsapi")//
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "Params：" + request.getParams().toString());
                        progressDialog = StringUtils.createLoadingDialog(Self_Service_PlayOrdersActivity.this, "请稍等...", true);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.d("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject  data=jo1.getJSONObject("data");
                                orderS_id=data.getString("order_id");
                                String url=data.getString("url");
                                createPatCode(url);
                                tv_money.setText("￥"+total_price_str);
                                layout_select_ok.setVisibility(View.GONE);
                                layout_scancodepay.setVisibility(View.VISIBLE);
                                layout_paysuccess.setVisibility(View.GONE);
                                mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_PlayOrdersActivity.this,mSelf_Service_GoodsInfolist,false,1);
                                list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
                                isScanCode=false;
                                mHandler.sendEmptyMessage(202);
                                //定时获取支付状态
                                GetPatStatus_timer=new Timer();
                                TimerTask  GetPatStatus_TimerTask=new TimerTask() {
                                    @Override
                                    public void run() {
                                        GetPayStatus(orderS_id);
                                    }
                                };
                                GetPatStatus_timer.schedule(GetPatStatus_TimerTask,1000,3000);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("barcode", "返回错误数据：" + e.toString());

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                });
    }
    //获取支付状态
    public void GetPayStatus(String order_id) {
        OkGo.post(SysUtils.getSellerServiceUrl("order_status"))
                .tag(this)
                .params("order_id",order_id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "Params：" + request.getParams().toString());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("barcode", "" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data=jo1.getJSONObject("data");
                                String order_id=data.getString("order_id");
                                String time=data.getString("time");
                                long time_long=Long.parseLong(time);
                                String paymoney=tv_money.getText().toString().trim().replace("￥","").trim();
                                Text2Speech.speech(Self_Service_PlayOrdersActivity.this,"支付成功"+paymoney+"元",false);
                                pay_type=0;
                                tv_orderid.setText("订单号："+order_id);
                                tv_ordertime.setText("日期："+ DateTimeUtils.getDateTimeFromMillisecond(time_long*1000));
                                tv_paymoney.setText(tv_money.getText().toString().trim());
                                layout_select_ok.setVisibility(View.GONE);
                                layout_scancodepay.setVisibility(View.GONE);
                                layout_paysuccess.setVisibility(View.VISIBLE);
                                GetPatStatus_timer.cancel();
                                //支付成功打印小票
                                PrintUtil printUtil1 = new PrintUtil(Self_Service_PlayOrdersActivity.this);
                                printUtil1.openButtonClicked();
                                String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsg(SharedUtil.getString("name"), tel, orderS_id, DateTimeUtils.getDateTimeFromMillisecond(time_long*1000), mSelf_Service_GoodsInfolist, mSelf_Service_GoodsInfolist,
                                        1, Double.parseDouble(tv_money.getText().toString().trim().replace("￥","").trim()), "",  tv_money.getText().toString().trim().replace("￥","").trim(), "0","","","","","");
                                printUtil1.printstring(syy);
                                //3秒关闭
                                threeTime_timer=new Timer();
                                threeTime_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                       mHandler.sendEmptyMessage(200);
                                    }
                                }, 1000,1000);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //获取商户电话
    public void GetSellerPhone() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_seller_tel"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("barcode", "电话号码为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                JSONObject jo2 = ja1.getJSONObject(0);
                                tel = jo2.getString("tel");
                                tv_phone.setText("服务热线："+tel);
                                Log.e("barcode", "服务热线："+tel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     * 广播处理
     */
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            if(type==1){
                setTotalPrice();
            }

        }
    };

    /**
     * 生成二维码
     * @param code_url
     */
    private void  createPatCode(final String code_url){
        final String filePath = QRCodeUtil.getFileRoot(Self_Service_PlayOrdersActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(code_url, 800, 800, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Drawable drawable =new BitmapDrawable(BitmapFactory.decodeFile(filePath));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                iv_paycode.setBackground(drawable);
                            }else {
                                iv_paycode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                            }
                        }
                    });
                }
            }
        }).start();
    }
    //设置商品总数据，总价格
    private void setTotalPrice(){
        int total_nums=0;
        double total_price=0.00;
        for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
            String nums_str= mSelf_Service_GoodsInfolist.get(i).getNumber();
            String price_str= mSelf_Service_GoodsInfolist.get(i).getPrice();
            int nums=Integer.parseInt(nums_str);
            double price=Double.parseDouble(price_str);
            total_nums+=nums;
            total_price+=price*nums;
        }
        tv_totalnums.setText( total_nums+"");
        tv_totalprice.setText("￥ "+total_price);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Self_Service_PlayOrdersActivity.this.registerReceiver(mBroadcastReceiver,new IntentFilter("Self_Service_PlayOrdersActivity"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Self_Service_PlayOrdersActivity.this.unregisterReceiver(mBroadcastReceiver);
        if(GetPatStatus_timer!=null){
            GetPatStatus_timer.cancel();
        }
       if(threeTime_timer!=null){
           threeTime_timer.cancel();
       }
       if(isHandle_timer!=null){
           isHandle_timer.cancel();
       }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                if(mSelf_Service_GoodsInfolist.size()>0){
                    GoToPay();
                }else {
                    StringUtils.showToast(Self_Service_PlayOrdersActivity.this,"购买的商品为空，请扫描购买商品的条形码！",50);

                }
                pay_type=1;
                break;
            case R.id.btn_continue:
                GetPatStatus_timer.cancel();
                layout_select_ok.setVisibility(View.VISIBLE);
                layout_scancodepay.setVisibility(View.GONE);
                mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_PlayOrdersActivity.this,mSelf_Service_GoodsInfolist,true,1);
                list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
                isScanCode=true;
                mHandler.sendEmptyMessage(202);
                break;
            case R.id.btn_cell:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                finish();
                break;
            case R.id.btn_timecell:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                finish();
                break;
        }
    }
    //扫码枪移动支付
    public void uppay(final String auth_code) {
        Log.d("print","执行到这里");

        final int total_fee = (int) (Float.parseFloat(tv_money.getText().toString().trim().replace("￥","").trim()) * 100);

        OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("total_fee", total_fee)
                .params("order_id", orderS_id)
                .params("pay_type", Connectors.Pay_TYPE)//
                .params("auth_code", auth_code)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.d("print","扫码的"+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "shitilei" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jO2 = jo1.getJSONObject("data");
                                String msg = jO2.getString("msg");
                                JSONObject jo3 = jO2.getJSONObject("data");

                                    pay_type=0;
                            } else {
                                String message = jo1.getString("message");
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.toast_layout, null);
                                TextView tv = (TextView) view.findViewById(R.id.tv);
                                tv.setText(message);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(view);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
