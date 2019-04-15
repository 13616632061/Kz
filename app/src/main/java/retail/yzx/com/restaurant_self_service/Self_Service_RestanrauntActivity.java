package retail.yzx.com.restaurant_self_service;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.service.GpPrintService;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zj.btsdk.BluetoothService;

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
import Utils.PrintUtil;
import Utils.PrintUtils;
import Utils.ScanGunKeyEventHelper;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.USBPrinters;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.DeviceListActivity;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntGoodsAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntGoodsLinkAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntHideGoodsPictureAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntHideGoodsPictureLinkAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntSortsAdapter;
import retail.yzx.com.restaurant_self_service.Entty.GoodsNotes;
import retail.yzx.com.restaurant_self_service.Entty.GoodsSortInfos;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandard;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandardInfos;
import retail.yzx.com.restaurant_self_service.Entty.Self_Service_RestanrauntGoodsInfo;
import retail.yzx.com.supper_self_service.Adapter.Self_Service_PlayOrdersAdapter;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.QRCodeUtil;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * 自助餐饮点单页面
 */

public class Self_Service_RestanrauntActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener, AbsListView.OnScrollListener {

    private PrinterServiceConnection conn = null;
    private GpService mGpService;

    //获取商品信息
    private ImageView iv_logo,iv_paycode;
    private TextView tv_sellername,tv_phone,tv_totalnums,tv_totalprice,tv_sort,tv_money,tv_paymoney,tv_orderid,tv_ordertime;
    private ListView list_content,list_goods,list_sort;
    private View self_service_restanraunt_select_ok,layout_scancodepay,layout_paysuccess;
    private Button btn_ok,btn_continue,btn_cell,btn_timecell,btn_search;
    private RelativeLayout layout1;
    private String tel;
    private Self_Service_RestanrauntGoodsAdapter mSelf_Service_RestanrauntGoodsAdapter;
    private Self_Service_RestanrauntHideGoodsPictureAdapter mSelf_Service_RestanrauntHideGoodsPictureAdapter;
    private Self_Service_RestanrauntSortsAdapter mSelf_Service_RestanrauntSortsAdapter;
    private Self_Service_PlayOrdersAdapter mSelf_Service_PlayOrdersAdapter;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Search_Restanraunt_GoodsInfoList;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList_Search;
    private ArrayList<GoodsStandardInfos> mGoodsStandardInfosList;//商品规格信息
    private ArrayList<GoodsStandardInfos>  mGoodsStandardInfosList_Search;//商品规格信息
    private ArrayList<GoodsNotes> mGoodsNotesLists;//商品备注信息
    private ArrayList<GoodsNotes> mGoodsNotesLists_Search;//商品备注信息
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;
    private ArrayList<GoodsStandard> mGoodsStandardList;//具体的规格
    private Dialog progressDialog = null;
    public List<Map<String, String>> mapList;
    private int pay_type=0;//0表示正扫1表示反扫
    private String orderS_id;
    private int n=3;
    private int time=120;
    private  Timer GetPatStatus_timer;//获取订单状态的定时器
    private  Timer threeTime_timer;//3秒关闭
    private  Timer isHandle_timer;//页面是否操作定时器
    private ScanGunKeyEventHelper mScanGunKeyEventHelper;
    long[] mHits = new long[3];//点击3下
    private int showType=1;
    private Self_Service_RestanrauntGoodsLinkAdapter mSelf_Service_RestanrauntGoodsLinkAdapter;
    private Self_Service_RestanrauntHideGoodsPictureLinkAdapter mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private boolean isScroll = true;
    private BluetoothAdapter bluetoothAdapter;
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
                        self_service_restanraunt_select_ok.setVisibility(View.VISIBLE);
                        layout_scancodepay.setVisibility(View.GONE);
                        layout_paysuccess.setVisibility(View.GONE);
                        if(threeTime_timer!=null){
                            threeTime_timer.cancel();
                        }
                        mSelf_Service_GoodsInfo.clear();
                        mSelf_Service_PlayOrdersAdapter.notifyDataSetChanged();
                        setTotalPrice();
                        setInitAdapterdata();//初始化商品数据
                    }
                    break;
                case 201:
                    if(time>0){
                        time= time-5;
                    }else {
                        StringUtils.showToast(Self_Service_RestanrauntActivity.this,"支付超时，请重新支付！",50);
                        mHandler.removeMessages(201);
                        mHandler.removeMessages(202);
                        self_service_restanraunt_select_ok.setVisibility(View.VISIBLE);
                        layout_scancodepay.setVisibility(View.GONE);
                        layout_paysuccess.setVisibility(View.GONE);
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

                    break;
                case 202:
                    time=120;
                    break;
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            if (popupWindow != null) {
                                popupWindow.dismiss();

                                popupWindow = null;
                            }
                            Toast.makeText(getApplicationContext(), "连接成功",
                                    Toast.LENGTH_SHORT).show();

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "设备连接丢失",
                            Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    if (getApplicationContext()!=null){
                        Toast.makeText(getApplicationContext(), "关闭设备",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringUtils.initOKgo(Self_Service_RestanrauntActivity.this);//初始化网络请求
        USBPrinters.initPrinter(getApplicationContext());
        setContentView(R.layout.activity_self__service__restanraunt);
        StringUtils.HideBottomBar(Self_Service_RestanrauntActivity.this);
        StringUtils.setupUI(this,findViewById(R.id.activity_self__service__restanraunt));
        SharedUtil.init(this);


        //蓝牙标签打印机绑定服务
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService

        initView();
        initData();
    }

    private void initView() {
        iv_logo= (ImageView) findViewById(R.id.iv_logo);
        tv_sellername= (TextView) findViewById(R.id.tv_sellername);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_totalnums= (TextView) findViewById(R.id.tv_totalnums);
        tv_totalprice= (TextView) findViewById(R.id.tv_totalprice);
        tv_sort= (TextView) findViewById(R.id.tv_sort);
        list_content= (ListView) findViewById(R.id.list_content);
        list_goods= (ListView) findViewById(R.id.list_goods);
        list_sort= (ListView) findViewById(R.id.list_sort);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_search= (Button) findViewById(R.id.btn_search);
        layout1= (RelativeLayout) findViewById(R.id.layout1);

        layout_scancodepay=findViewById(R.id.layout_scancodepay);
        layout_paysuccess=findViewById(R.id.layout_paysuccess);
        self_service_restanraunt_select_ok=findViewById(R.id.self_service_restanraunt_select_ok);
        iv_paycode= (ImageView) layout_scancodepay.findViewById(R.id.iv_paycode);
        tv_money= (TextView) layout_scancodepay.findViewById(R.id.tv_money);
        btn_continue= (Button) layout_scancodepay.findViewById(R.id.btn_continue);
        btn_cell= (Button) layout_scancodepay.findViewById(R.id.btn_cell);
        tv_paymoney= (TextView) layout_paysuccess.findViewById(R.id.tv_paymoney);
        btn_timecell= (Button) layout_paysuccess.findViewById(R.id.btn_timecell);
        tv_orderid= (TextView) layout_paysuccess.findViewById(R.id.tv_orderid);
        tv_ordertime= (TextView) layout_paysuccess.findViewById(R.id.tv_ordertime);

        tv_sellername.setText(SharedUtil.getString("name"));
        mScanGunKeyEventHelper= new ScanGunKeyEventHelper(this);
        showType=SharedUtil.getInt("showType");
        if(showType==-1){
            showType=1;
        }

        //蓝牙打印
        mService = new BluetoothService(this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
        }
        //判断蓝牙是否连接小票打印机
        String address_Mac=SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
        if(TextUtils.isEmpty(address_Mac)){
            StringUtils.showToast(Self_Service_RestanrauntActivity.this,"如需打印小票，请先连接小票打印机",20);
        }else {
            con_dev = mService.getDevByMac(address_Mac);
            mService.connect(con_dev);
        }
        tel=SharedUtil.getString("seller_tel");

        list_sort.setOnItemClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cell.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_timecell.setOnClickListener(this);
        layout1.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        list_goods.setOnScrollListener(this);
    }
    private void initData() {
        mSelf_Service_GoodsInfo=new ArrayList<>();
        GetSellerPhone();
        GetGoods_info();

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
                                SharedUtil.putString("seller_tel",tel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //获取商品信息
    String spec_name;
    public void GetGoods_info() {

        OkGo.post(SysUtils.getGoodsServiceUrl("goods_info"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "Params：" + request.getParams().toString());
                        if(progressDialog!=null){
                            progressDialog=null;
                        }
                        progressDialog = StringUtils.createLoadingDialog(Self_Service_RestanrauntActivity.this, "请稍等...", true);
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
                            Log.e("barcode", "goods_info" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data=jo1.getJSONObject("data");
                                JSONArray catagory_infos=data.getJSONArray("catagory_infos");
                                mGoodsSortInfosList=new ArrayList<>();
                                if(catagory_infos!=null&&catagory_infos.length()>0){
                                    for(int i=0;i<catagory_infos.length();i++){
                                        JSONObject catagory_infos_object=catagory_infos.getJSONObject(i);
                                        String tag_id=catagory_infos_object.getString("tag_id");
                                        String tag_name=catagory_infos_object.getString("tag_name");
                                        JSONArray pro_info=catagory_infos_object.getJSONArray("pro_info");
                                        mSelf_Service_Restanraunt_GoodsInfoList =new ArrayList<>();
                                        if(pro_info!=null&&pro_info.length()>0){
                                            for(int j=0;j<pro_info.length();j++){
                                                JSONObject pro_info_object=pro_info.getJSONObject(j);
                                                String goods_id=pro_info_object.getString("goods_id");//id
                                                String name=pro_info_object.getString("name");//名字
                                                String price=pro_info_object.getString("price");//显示的价格
                                                String image_default_id=pro_info_object.getString("image_default_id");//商品图片
                                                JSONArray spec_desc=null;
                                                try{
                                                    spec_desc=pro_info_object.getJSONArray("spec_desc");
                                                }catch (Exception e){
                                                    Log.e("barcode", " spec_desc：" + e.toString());
                                                }
                                                mGoodsStandardInfosList = new ArrayList<>();
                                                if(spec_desc!=null&&spec_desc.length()>0){
                                                    for(int x=0;x<spec_desc.length();x++) {
                                                        JSONObject spec_desc_object = spec_desc.getJSONObject(x);
                                                        spec_name = spec_desc_object.getString("spec_name");//规格型号
                                                        JSONArray spec_info = null;
                                                        try {
                                                            spec_info = spec_desc_object.getJSONArray("spec_info");
                                                        } catch (Exception e) {
                                                            Log.e("barcode", " spec_info：" + e.toString());
                                                        }
                                                        mGoodsStandardList = new ArrayList<GoodsStandard>();
                                                        if (spec_info != null && spec_info.length() > 0) {
                                                            for (int a = 0; a < spec_info.length(); a++) {
                                                                JSONObject spec_info_object = spec_info.getJSONObject(a);
                                                                String spec_value = spec_info_object.getString("spec_value");//规格
                                                                GoodsStandard goodsStandard = new GoodsStandard(spec_value,false);
                                                                mGoodsStandardList.add(goodsStandard);
                                                            }
                                                        }
                                                        GoodsStandardInfos goodsStandardInfos=new GoodsStandardInfos(spec_name,mGoodsStandardList);
                                                        mGoodsStandardInfosList.add(goodsStandardInfos);
                                                    }
                                                        JSONArray spec_infos=null;
                                                        try {
                                                           spec_infos=pro_info_object.getJSONArray("spec_infos");
                                                        }catch (Exception e){
                                                            Log.e("barcode", " spec_infos：" + e.toString());
                                                        }

                                                        mGoodsNotesLists=new ArrayList<GoodsNotes>();
                                                            for(int a=0;a<spec_infos.length();a++){
                                                                JSONObject spec_infos_object=spec_infos.getJSONObject(a);
                                                                String price_standard=spec_infos_object.getString("price");//不同规格商品的价格
                                                                String spec_info_standard=spec_infos_object.getString("spec_info");//含有的规格信息
                                                                String is_default=spec_infos_object.getString("is_default");//是否默认的规格
                                                                String product_id=spec_infos_object.getString("product_id");//产品id
                                                                GoodsNotes goodsNotes=new GoodsNotes(price_standard,spec_info_standard,is_default,product_id);
                                                                mGoodsNotesLists.add(goodsNotes);
                                                            }
                                                        }

                                                Self_Service_RestanrauntGoodsInfo self_service_restanrauntGoodsInfo=new Self_Service_RestanrauntGoodsInfo(goods_id,name,price,image_default_id,"0",mGoodsStandardInfosList,mGoodsNotesLists,tag_id);
                                                mSelf_Service_Restanraunt_GoodsInfoList.add(self_service_restanrauntGoodsInfo);
                                                }
                                            }
                                        GoodsSortInfos goodsSortInfos=new GoodsSortInfos(tag_id,tag_name,mSelf_Service_Restanraunt_GoodsInfoList);
                                        mGoodsSortInfosList.add(goodsSortInfos);
                                        }

                                    }
                                    mSelf_Service_RestanrauntSortsAdapter=new Self_Service_RestanrauntSortsAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList);
                                    list_sort.setAdapter(mSelf_Service_RestanrauntSortsAdapter);

                                    if(mGoodsSortInfosList.size()>0){
                                        if(showType==1){
                                        mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
//                                        mSelf_Service_RestanrauntGoodsAdapter=new Self_Service_RestanrauntGoodsAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList.get(0).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
//                                        list_goods.setAdapter(mSelf_Service_RestanrauntGoodsAdapter);
                                            mSelf_Service_RestanrauntGoodsLinkAdapter=new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
                                            list_goods.setAdapter( mSelf_Service_RestanrauntGoodsLinkAdapter);
                                        tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                                        }else if(showType==2){
                                                mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
//                                                mSelf_Service_RestanrauntHideGoodsPictureAdapter=new Self_Service_RestanrauntHideGoodsPictureAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList.get(0).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
//                                                list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureAdapter);
                                            mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter=new Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
                                                list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter);
                                                tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                                        }
                                    }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Self_Service_RestanrauntActivity.this,"服务器数据异常！",Toast.LENGTH_SHORT).show();
                            Log.e("barcode", " spec_infos：" + e.toString());

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showType=SharedUtil.getInt("showType");
                mSelf_Service_RestanrauntSortsAdapter.setDefSelect(position);
        if(showType==-1){
            showType=1;
        }
        if(showType==1){
            mSelf_Service_RestanrauntGoodsLinkAdapter=new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
            list_goods.setAdapter( mSelf_Service_RestanrauntGoodsLinkAdapter);
            int rightSection = 0;
            for (int i = 0; i < position; i++) {
                rightSection +=(int)(Math.ceil((float)mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().size() / 4)) + 1;
            }
            list_goods.setSelection(rightSection);
        }else if(showType==2){
            mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter=new Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
            list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter);
            int rightSection = 0;
            for (int i = 0; i < position; i++) {
                rightSection +=(int)(Math.ceil((float)mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().size() / 6)) + 1;
            }
            list_goods.setSelection(rightSection);
        }
                tv_sort.setText(mGoodsSortInfosList.get(position).getSort_name());

    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        isScroll = true;
        switch (scrollState) {
            case SCROLL_STATE_TOUCH_SCROLL:
                new PauseOnScrollListener(ImageLoader.getInstance(), true, true);//滑动时，不加载图片
//                        isScroll = true;
                break;
            case SCROLL_STATE_FLING:
//                        isScroll= false;
                break;
            case SCROLL_STATE_IDLE:
//                        isScroll = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isScroll) {
            if (mGoodsSortInfosList != null) {
                if(showType==1&& mSelf_Service_RestanrauntGoodsLinkAdapter!=null) {

                    for (int i = 0; i < mGoodsSortInfosList.size(); i++) {
                        if (i ==  mSelf_Service_RestanrauntGoodsLinkAdapter.getSectionForPosition(firstVisibleItem)) {
                            if (i > visibleItemCount) {
                                list_sort.smoothScrollToPosition(i);
                            } else {
                                list_sort.smoothScrollToPosition(0);
                            }
                            mSelf_Service_RestanrauntSortsAdapter.setDefSelect(i);
                        } else {

                        }
                    }
                }
                else if(showType==2&&mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter!=null){
                    for (int i = 0; i < mGoodsSortInfosList.size(); i++) {
                        if (i == mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter.getSectionForPosition(firstVisibleItem)) {
                            if (i > visibleItemCount) {
                                list_sort.smoothScrollToPosition(i);
                            } else {
                                list_sort.smoothScrollToPosition(0);
                            }
                            mSelf_Service_RestanrauntSortsAdapter.setDefSelect(i);
                        } else {

                        }
                    }
                }
            } else {
                isScroll = true;
            }
        }
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            String action=intent.getAction();
            if(type==1){
                mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_RestanrauntActivity.this, mSelf_Service_GoodsInfo,true,2);
                list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
                list_content.setSelection(mSelf_Service_GoodsInfo.size());
                Self_Service_RestanrauntActivity.this.sendBroadcast(new Intent("Self_Service_RestanrauntActivity.Action").putExtra("type",2));
            }else if(type==2){
                setTotalPrice();
            }
            if(!TextUtils.isEmpty(action)){
                PrintUtils.setBlueLabel(Self_Service_RestanrauntActivity.this,action,intent);
            }

        }
    };
    //设置商品总数据，总价格
    private void setTotalPrice(){
        int total_nums=0;
        double total_price=0.00;
        for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
            String nums_str= mSelf_Service_GoodsInfo.get(i).getNumber();
            String price_str= mSelf_Service_GoodsInfo.get(i).getPrice();
            int nums=Integer.parseInt(nums_str);
            double price=Double.parseDouble(price_str);
            total_nums+=nums;
            total_price+=price*nums;
        }
        tv_totalnums.setText( total_nums+"");
        tv_totalprice.setText("￥ "+ Utils.StringUtils.stringpointtwo(total_price+""));
    }
    @Override
    protected void onResume() {
        super.onResume();
        Self_Service_RestanrauntActivity.this.registerReceiver(broadcastReceiver,new IntentFilter("Self_Service_RestanrauntActivity.Action"));
        Self_Service_RestanrauntActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        USBPrinters.destroyPrinter();
        if (popupWindow != null) {
            popupWindow.dismiss();

            popupWindow = null;
        }
        Self_Service_RestanrauntActivity.this.unregisterReceiver(broadcastReceiver);
        if(GetPatStatus_timer!=null){
            GetPatStatus_timer.cancel();
        }
        if(threeTime_timer!=null){
            threeTime_timer.cancel();
        }
        if(isHandle_timer!=null){
            isHandle_timer.cancel();
        }
        ImageLoader.getInstance().clearMemoryCache();//清除内存
        ImageLoader.getInstance().clearDiskCache();//清除sd卡
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id. btn_ok:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                if(mSelf_Service_GoodsInfo.size()>0){
                    GoToPay();
                }else {
                    StringUtils.showToast(Self_Service_RestanrauntActivity.this,"请先选择购买的商品！",40);
//                    Toast.makeText(Self_Service_RestanrauntActivity.this,"购买的商品不能为空！",Toast.LENGTH_LONG).show();
                }
                pay_type=1;
                break;
            case R.id.btn_continue:
                GetPatStatus_timer.cancel();
                self_service_restanraunt_select_ok.setVisibility(View.VISIBLE);
                layout_scancodepay.setVisibility(View.GONE);
                mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_RestanrauntActivity.this,mSelf_Service_GoodsInfo,true,2);
                list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
                mHandler.sendEmptyMessage(202);
                isHandle_timer.cancel();
                GetPatStatus_timer.cancel();
                break;
            case R.id.btn_cell:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                self_service_restanraunt_select_ok.setVisibility(View.VISIBLE);
                layout_scancodepay.setVisibility(View.GONE);
                layout_paysuccess.setVisibility(View.GONE);
                isHandle_timer.cancel();
                GetPatStatus_timer.cancel();
                mSelf_Service_GoodsInfo.clear();
                mSelf_Service_PlayOrdersAdapter.notifyDataSetChanged();
                setInitAdapterdata();//初始化商品数据
                setTotalPrice();
                break;
            case R.id.btn_timecell:
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                self_service_restanraunt_select_ok.setVisibility(View.VISIBLE);
                layout_scancodepay.setVisibility(View.GONE);
                layout_paysuccess.setVisibility(View.GONE);
                if(threeTime_timer!=null){
                    threeTime_timer.cancel();
                }
                mSelf_Service_GoodsInfo.clear();
                mSelf_Service_PlayOrdersAdapter.notifyDataSetChanged();
                setInitAdapterdata();//初始化商品数据
                setTotalPrice();
                break;
            case R.id.layout1:
                setDialog();
                break;
            case R.id.btn_search:
                setSearchDialog();
                break;
        }
    }

    /**
     * 初始化商品数据
     */
    private void setInitAdapterdata(){
        showType=SharedUtil.getInt("showType");
        mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
        if(showType==-1){
            showType=1;
        }
        if(showType==1){
            mSelf_Service_RestanrauntGoodsLinkAdapter=new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
            list_goods.setAdapter( mSelf_Service_RestanrauntGoodsLinkAdapter);
        }else if(showType==2){
            mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter=new Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
            list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter);
        }
        tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
    }
    /**
     * 生成支付二维码
     */
    private void GoToPay(){
        ArrayList<Map<String,String>> mapList=new ArrayList<>();
        for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
            String goods_id= mSelf_Service_GoodsInfo.get(i).getGoods_id();
            String name = mSelf_Service_GoodsInfo.get(i).getName();
            String price= mSelf_Service_GoodsInfo.get(i).getPrice();
            String cost= mSelf_Service_GoodsInfo.get(i).getCost();
            String nums= mSelf_Service_GoodsInfo.get(i).getNumber();
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
                        progressDialog = StringUtils.createLoadingDialog(Self_Service_RestanrauntActivity.this, "请稍等...", true);
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
                                self_service_restanraunt_select_ok.setVisibility(View.GONE);
                                layout_scancodepay.setVisibility(View.VISIBLE);
                                layout_paysuccess.setVisibility(View.GONE);
                                mSelf_Service_PlayOrdersAdapter=new Self_Service_PlayOrdersAdapter(Self_Service_RestanrauntActivity.this,mSelf_Service_GoodsInfo,false,2);
                                list_content.setAdapter(mSelf_Service_PlayOrdersAdapter);
//                                isScanCode=false;
                                mHandler.sendEmptyMessage(202);
                                //定时获取支付状态
                                GetPatStatus_timer=new Timer();
                                TimerTask GetPatStatus_TimerTask=new TimerTask() {
                                    @Override
                                    public void run() {
                                        GetPayStatus(orderS_id);
                                    }
                                };
                                GetPatStatus_timer.schedule(GetPatStatus_TimerTask,1000,3000);
                                //支付1分钟超时
                                isHandle_timer=new Timer();
                                isHandle_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                            mHandler.sendEmptyMessage(201);
                                    }
                                }, 1000,5000);

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
    /**
     * 生成二维码
     * @param code_url
     */
    private void  createPatCode(final String code_url){
        final String filePath = QRCodeUtil.getFileRoot(Self_Service_RestanrauntActivity.this) + File.separator
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
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
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
                                Text2Speech.speech(Self_Service_RestanrauntActivity.this,"支付成功"+paymoney+"元",false);
                                pay_type=0;
                                tv_orderid.setText("订单号："+order_id);
                                tv_ordertime.setText("日期："+ DateTimeUtils.getDateTimeFromMillisecond(time_long*1000));
                                tv_paymoney.setText(tv_money.getText().toString().trim());
                                self_service_restanraunt_select_ok.setVisibility(View.GONE);
                                layout_scancodepay.setVisibility(View.GONE);
                                layout_paysuccess.setVisibility(View.VISIBLE);

                                //3秒关闭
                                threeTime_timer=new Timer();
                                threeTime_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(200);
                                    }
                                }, 1000,1000);
                                GetPatStatus_timer.cancel();
                                isHandle_timer.cancel();
                                //打印
                                    PrintUtil.doPrint(Self_Service_RestanrauntActivity.this,mGpService,mService,tel,orderS_id, 0,tv_ordertime.getText().toString().trim(), 0,tv_money.getText().toString().trim().replace("￥","").trim(),tv_money.getText().toString().trim().replace("￥","").trim(),
                                            "",2,mSelf_Service_GoodsInfo,mSelf_Service_GoodsInfo,"","","","");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("barcode","e+"+e.toString());
                        }
                    }
                });
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
        if(pay_type==1) {
            uppay(barcode);

        }
    }
    //扫码枪移动支付
    public void uppay(final String auth_code) {

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
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "shitilei:" + s);
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
    /**
     * 进入商品管理页面弹窗
     */
    private View view;
    private PopupWindow popupWindow;
    private Button btn_bluetooth_printer;
    private void setDialog() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
            if(popupWindow==null) {
                view = View.inflate(Self_Service_RestanrauntActivity.this, R.layout.self_service_res_three_clickdialog, null);
                LinearLayout layout_dialog = (LinearLayout) view.findViewById(R.id.layout_dialog);
                Button btn_show_picture = (Button) view.findViewById(R.id.btn_show_picture);
                Button btn_hide_picture = (Button) view.findViewById(R.id.btn_hide_picture);
                Button btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
                btn_bluetooth_printer = (Button) view.findViewById(R.id.btn_bluetooth_printer);
                Button btn_back = (Button) view.findViewById(R.id.btn_back);
                popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                //实例化一个ColorDrawable颜色为半透明
                ColorDrawable mColorDrawable = new ColorDrawable(0x20000000);
                //设置弹框的背景
                popupWindow.setBackgroundDrawable(mColorDrawable);
                popupWindow.showAtLocation(layout_dialog, Gravity.CENTER, 0, 0);
                btn_show_picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedUtil.putInt("showType", 1);//1商品有图，2商品无图
                        mSelf_Service_RestanrauntSortsAdapter = new Self_Service_RestanrauntSortsAdapter(Self_Service_RestanrauntActivity.this, mGoodsSortInfosList);
                        list_sort.setAdapter(mSelf_Service_RestanrauntSortsAdapter);
                        if (mGoodsSortInfosList.size() > 0) {
                            mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
                            mSelf_Service_RestanrauntGoodsLinkAdapter = new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this, mGoodsSortInfosList, mSelf_Service_GoodsInfo);
                            list_goods.setAdapter(mSelf_Service_RestanrauntGoodsLinkAdapter);
                            tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                        }
                        if (popupWindow != null) {
                            popupWindow.dismiss();

                            popupWindow = null;
                        }

                    }
                });
                btn_hide_picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedUtil.putInt("showType", 2);
                        mSelf_Service_RestanrauntSortsAdapter = new Self_Service_RestanrauntSortsAdapter(Self_Service_RestanrauntActivity.this, mGoodsSortInfosList);
                        list_sort.setAdapter(mSelf_Service_RestanrauntSortsAdapter);
                        if (mGoodsSortInfosList.size() > 0) {
                            mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
                            mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter = new Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Self_Service_RestanrauntActivity.this, mGoodsSortInfosList, mSelf_Service_GoodsInfo);
                            list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter);
                            tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                        }
                        if (popupWindow != null) {
                            popupWindow.dismiss();

                            popupWindow = null;
                        }

                    }
                });
                btn_refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetGoods_info();
                        if (popupWindow != null) {
                            popupWindow.dismiss();

                            popupWindow = null;
                        }
                    }
                });
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        if (popupWindow != null) {
                            popupWindow.dismiss();

                            popupWindow = null;
                        }
                    }
                });
                if (mService.isBTopen() == false) {
                    btn_bluetooth_printer.setText("开启蓝牙打印");
                } else {
                    btn_bluetooth_printer.setText("关闭蓝牙打印");
                }
                btn_bluetooth_printer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("开启蓝牙打印".equals(btn_bluetooth_printer.getText().toString().trim())) {
                            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                        } else {
                            if (popupWindow != null) {
                                popupWindow.dismiss();

                                popupWindow = null;
                            }
                            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            bluetoothAdapter.disable();
                            btn_bluetooth_printer.setText("开启蓝牙打印");

                        }
                    }
                });
                //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int height = view.findViewById(R.id.layout_dialog).getBottom();
                        int height_top = view.findViewById(R.id.layout_dialog).getTop();
                        int weight = view.findViewById(R.id.layout_dialog).getLeft();
                        int weight_right = view.findViewById(R.id.layout_dialog).getRight();
                        int y = (int) event.getY();
                        int x = (int) event.getX();
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (y > height || y < height_top) {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();

                                    popupWindow = null;
                                }
                            }
                            if (x < weight || x > weight_right) {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();

                                    popupWindow = null;
                                }
                            }
                        }
                        return true;
                    }
                });

            }
            }
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "蓝牙打开成功", Toast.LENGTH_LONG).show();

                    Intent serverIntent = new Intent(Self_Service_RestanrauntActivity.this, DeviceListActivity.class);      //��������һ����Ļ
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    if (SharedUtil.getBoolean("self_print")) {
                        con_dev = mService.getDevByMac(address);
                        SharedUtil.putString("ReceiptPrint_BluetoothMac_address",address);
                        mService.connect(con_dev);
                    }
                    if(SharedUtil.getBoolean("self_print_order_label")) {
                        try {
                            if (mGpService != null && address != null) {
                                mGpService.openPort(0, 4, address, 0);
                            } else {
//                                con_dev = mService.getDevByMac(address);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "蓝牙1=" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
        }
    }
//蓝牙标签打印机服务连接
    class PrinterServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mGpService = GpService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mGpService = null;
        }
    }

    /**
     * 商品搜索弹框
     */
    private View viewSearchDialog;
//    private PopupWindow popupWindowSearchDialog;
    private AlertDialog mAlertDialog;
    private InputMethodManager imm;
    private EditText et_search;
    private void setSearchDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Self_Service_RestanrauntActivity.this,R.style.AlertDialog);
        viewSearchDialog = View.inflate(Self_Service_RestanrauntActivity.this, R.layout.dialog_self_res_goodssearch, null);
         et_search= (EditText) viewSearchDialog.findViewById(R.id.et_search);
        Button btn_search_cell= (Button) viewSearchDialog.findViewById(R.id.btn_search_cell);
        Button btn_search_sure= (Button) viewSearchDialog.findViewById(R.id.btn_search_sure);
        //没有键盘自动弹出键盘
        if (!NoDoubleClickUtils.isSoftShowing(Self_Service_RestanrauntActivity.this)) {
            imm = (InputMethodManager) Self_Service_RestanrauntActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        btn_search_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(Self_Service_RestanrauntActivity.this)) {
                    imm = (InputMethodManager) Self_Service_RestanrauntActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_search_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_search_src=et_search.getText().toString().trim();
                if (TextUtils.isEmpty(et_search_src)){
                    StringUtils.showToast(Self_Service_RestanrauntActivity.this,"搜索的内容不能为空！",25);
                    return;
                }
                if( mGoodsSortInfosList_Search!=null){
                    mGoodsSortInfosList_Search.clear();
                }
                mGoodsSortInfosList_Search=new ArrayList<GoodsSortInfos>();
                for(int i=0;i<mGoodsSortInfosList.size();i++){
                    boolean isexist=false;
                    String tag_id=mGoodsSortInfosList.get(i).getSort_id();
                    String tag_name=mGoodsSortInfosList.get(i).getSort_name();
                    mSelf_Service_Search_Restanraunt_GoodsInfoList=new ArrayList<Self_Service_RestanrauntGoodsInfo>();
                    for(int j=0;j<mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().size();j++){
                        if(mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getName().indexOf(et_search_src)!=-1){
                            String goods_id=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getGoods_id();
                            String name=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getName();
                            String price=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getPrice();
                            String image_default_id=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getImage_default_id();
                            mGoodsStandardInfosList_Search=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getmGoodsStandardInfosList();
                            mGoodsNotesLists_Search=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getmGoodsNotesLists();
                            Self_Service_RestanrauntGoodsInfo self_service_restanrauntGoodsInfo=new Self_Service_RestanrauntGoodsInfo(goods_id,name,price,image_default_id,"0",mGoodsStandardInfosList_Search,mGoodsNotesLists_Search,tag_id);
                            mSelf_Service_Search_Restanraunt_GoodsInfoList.add(self_service_restanrauntGoodsInfo);
                            isexist=true;
                        }
                    }
                    if(isexist){
                        GoodsSortInfos goodsSortInfos=new GoodsSortInfos(tag_id,tag_name,mSelf_Service_Search_Restanraunt_GoodsInfoList);
                        mGoodsSortInfosList_Search.add(goodsSortInfos);
                    }
                }
                if( mGoodsSortInfosList_Search.size()>0){
                    mSelf_Service_RestanrauntGoodsLinkAdapter=new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this, mGoodsSortInfosList_Search,mSelf_Service_GoodsInfo);
                    list_goods.setAdapter( mSelf_Service_RestanrauntGoodsLinkAdapter);
                }else {
                    StringUtils.showToast(Self_Service_RestanrauntActivity.this,"此商品不存在，请重新输入或咨询工作人员！",25);
                }
                mAlertDialog.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(Self_Service_RestanrauntActivity.this)) {
                    imm = (InputMethodManager) Self_Service_RestanrauntActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog = dialog.setView( viewSearchDialog).show();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }
}
