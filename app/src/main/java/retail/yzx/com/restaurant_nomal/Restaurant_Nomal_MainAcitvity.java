package retail.yzx.com.restaurant_nomal;

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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;
import com.recker.flybanner.FlyBanner;
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
import Entty.Goods_Common_Notes;
import Entty.Integral_Entty;
import Entty.Member_entty;
import Entty.ShuliangEntty;
import Entty.Specification_Entty;
import Utils.DateUtils;
import Utils.PrintUtil;
import Utils.PrintUtils;
import Utils.RandomUtils;
import Utils.ScanGunKeyEventHelper;
import Utils.SetEditTextInput;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_integral;
import adapters.Adapter_optimize;
import adapters.Adapter_specification;
import android_serialport_api.SerialParam;
import android_serialport_api.SerialPortOperaion;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.Commoditymanagement_Activity;
import retail.yzx.com.kz.DeviceListActivity;
import retail.yzx.com.kz.Handover_activity;
import retail.yzx.com.kz.Member_Activity;
import retail.yzx.com.kz.Printer_activity;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Adapter.Common_Notes_DialogAdapter;
import retail.yzx.com.restaurant_nomal.Adapter.Res_Nomal_PalyOrderAdapter;
import retail.yzx.com.restaurant_nomal.Entry.PreferencesService;
import retail.yzx.com.restaurant_nomal.Entry.ResDialog;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.restaurant_nomal.Fragment.Res_GoodsFragment;
import retail.yzx.com.restaurant_nomal.Fragment.Res_GoodsOrderFragment;
import retail.yzx.com.restaurant_nomal.Fragment.Res_Table_Fragment;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntGoodsAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntHideGoodsPictureAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntSortsAdapter;
import retail.yzx.com.restaurant_self_service.Entty.GoodsNotes;
import retail.yzx.com.restaurant_self_service.Entty.GoodsSortInfos;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandard;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandardInfos;
import retail.yzx.com.restaurant_self_service.Entty.Self_Service_RestanrauntGoodsInfo;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.QRCodeUtil;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import shujudb.Sqlite_Entity;
import widget.Switch;

import static base.BaseActivity.isnetworknew;

public class Restaurant_Nomal_MainAcitvity extends AppCompatActivity implements  View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener, Adapter_optimize.OnClickListener {
    public final static Double MIXMONEY = 99999.00;//付款最大金额
    private EditText et_keyoard, et_inputscancode, tv_Surplus;
    public boolean isSelected = true;
    private TextView tv_netreceipts, tv_sort, tv_logn_name, tv_ordernums, tv_ordertime, tv_peoplenums, tv_tablenums, tv_goods_nums, tv_total_money;
    private ListView list_goods, list_sort;
    private SwipeMenuListView list_ordergoods;
    private Button btn_do_package, btn_do_order, but_cash_sure, but_internet, btn_mobile_cell, but_time,btn_refresh;
    private LinearLayout layout_more, layout_logout, ll_jshuang;
    ;
    private RelativeLayout layout_peoplenums, layout_tablenums, keyboard_et_layout, keyboard_tv_layout;
    private RelativeLayout Rl_xianjin, Rl_time, Rl_yidong, layout_process_bar;
    private TextView tv_xianjin_netreceipt, tv_amount, tv_change, tv_yidong;
    private TextView tv_payment, tv_danhao, tv_day, tv_dopackage,tv_gone;
    private ImageView im_code;
    private Button btn_dish, btn_order, btn_table, btn_lock_screen, btn_ordersnums, btn_moneybox, btn_printer,btn_search,but_member;
    //控制现金支付的按钮
    public FButton but_Cashbox, but_mobilepayment, tv_cancel;
    public int frequency = 0;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 1;

    private FlyBanner im_picture;//数字键盘上轮播图
    public List<String> imageurls;//轮播图URL集合
    private Dialog progressDialog = null;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;
    private ArrayList<GoodsStandardInfos> mGoodsStandardInfosList;//商品规格信息
    private ArrayList<GoodsNotes> mGoodsNotesLists;//商品备注信息
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;
    private ArrayList<Self_Service_GoodsInfo> newmSelf_Service_GoodsInfo=new ArrayList<>();;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoorderlist;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_Goods;
    private ArrayList<GoodsStandard> mGoodsStandardList;//具体的规格
    private Self_Service_RestanrauntSortsAdapter mSelf_Service_RestanrauntSortsAdapter;
    private Self_Service_RestanrauntGoodsAdapter mSelf_Service_RestanrauntGoodsAdapter;
    private Self_Service_RestanrauntHideGoodsPictureAdapter mSelf_Service_RestanrauntHideGoodsPictureAdapter;
    private Res_Nomal_PalyOrderAdapter mRes_Nomal_PalyOrderAdapter;
    private Timer threeTime_timer;//3秒关闭
    private Timer processTime_timer;//支付中进度条
    private int n = 3;
    private double m = 1;
    private int showType = 2;
    private PreferencesService mPreferencesService;
    private ArrayList<Fragment> fragmentArrayList;
    private int currentIndex = 0;//控制当前需要显示第几个Fragment
    private Fragment mCurrentFrgment;//显示当前Fragment
    private boolean doPackage = false;
    private ArrayList<Res_GoodsOrders> mRes_GoodsOrderslist;//左侧商品列表
    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private Map<String, String> stringMap = new HashMap<>();
    private String stringcontext = "";
    private boolean isEditOrder = false;//是否挂单编辑
    private boolean isadd=false;
    private String pre_goodsordernums = "";//最初挂单的商品数量
    private  Res_GoodsFragment  mRes_GoodsFragment;
    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    private boolean isOrder=false;//是否为挂单
    private ArrayList<Goods_Common_Notes> mCommonNotesList;//常用备注
    private Common_Notes_DialogAdapter mCommon_Notes_DialogAdapter;
    private boolean open_table;//是否开启餐桌
    private String table_id="";//餐桌id
    private String table_name="";//餐桌名字

    public AlertDialog dialog_member;
    public ListView lv_member;
    public Adapter_optimize adapter_optimize;
    public Member_entty member_entty;
    public List<Member_entty> member_entties;
    public List<Map<String, String>> mapList;

    //会员id
    private String pbmember_id="";
    //会员积分
    private String Score="";
    //判断是否免密
    private String is_require_pass="no";

    //会员积分兑换
    private List<Integral_Entty> integral_list;
    private ListView lv_exchange;
    private Adapter_integral adapter_integral;
    public TextView tv_integral,tv_balance;

    //会员充值
    private List<Specification_Entty> specification_list=new ArrayList<>();
    private Adapter_specification adapter_specification;
    private ListView lv_recharge;
    //记录点击的是那个会员
    private int specification_unms;

    //设置副屏的金额
    private SerialPortOperaion mSerialPortOperaion=null;
    public List<Integer> listInt=new ArrayList<>();

    private Button but_reduce;//立减金额的弹窗
    private Button but_set_print;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    if (n > 0) {
                        but_time.setText("手动关闭" + "(" + n + "秒后关闭" + ")");
                        n--;
                    } else {
                        mHandler.removeMessages(200);
                        Rl_time.setVisibility(View.GONE);
                        ll_jshuang.setVisibility(View.VISIBLE);
                        if (mRes_Nomal_PalyOrderAdapter != null) {
                            mRes_Nomal_PalyOrderAdapter.setClick(true);
                        }
                        if (threeTime_timer != null) {
                            threeTime_timer.cancel();
                        }
                        setInitData();
                        n=3;
                    }
                    break;
                case 201:

                    break;
                case 202:
                    break;
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

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
//        retail.yzx.com.supper_self_service.Utils.StringUtils.initOKgo(Restaurant_Nomal_MainAcitvity.this);//初始化网络请求
        setContentView(R.layout.activity_restaurant__nomal__main_acitvity);
        StringUtils.HideBottomBar(Restaurant_Nomal_MainAcitvity.this);
//        StringUtils.setupUI(this, findViewById(R.id.activity_restaurant__nomal__main_acitvity));//点击空白处隐藏软键盘
        initFragment();
        initView();
        initData();
        initListener();
    }



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

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        mRes_GoodsFragment=new Res_GoodsFragment();
        fragmentArrayList.add(mRes_GoodsFragment);
        fragmentArrayList.add(new Res_GoodsOrderFragment());
        fragmentArrayList.add(new Res_Table_Fragment());
    }



    //订单作废
    private void delete_order(String order_id){
        OkGo.post(SysUtils.getSellerServiceAPPUrl("delete_order"))
                .tag(this)
                .params("order_id",order_id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
//                                if(!TextUtils.isEmpty(mRes_GoodsOrderslist.get(position).getTable_id())){
//                                    editTableStatus("0",mRes_GoodsOrderslist.get(position).getTable_id());
//                                }
//                                mRes_GoodsOrderslist.remove(position);
//                                notifyDataSetChanged();
                                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"订单作废成功！",20);
                                sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 6));

                            }else {
                                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"订单作废失败！",20);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //会员的点击事件
    //会员的监听
    @Override
    public void setonclick(final int i) {
        dialog_member.dismiss();
        final Dialog dialog = new Dialog(Restaurant_Nomal_MainAcitvity.this);
        dialog.setTitle("会员");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_member_information);
        TextView tv_card_number = (TextView) window.findViewById(R.id.tv_card_number);
        TextView tv_discount = (TextView) window.findViewById(R.id.tv_discount);
        TextView tv_name = (TextView) window.findViewById(R.id.tv_name);
        tv_balance = (TextView) window.findViewById(R.id.tv_balance);
        TextView tv_phone = (TextView) window.findViewById(R.id.tv_phone);
        tv_integral = (TextView) window.findViewById(R.id.tv_integral);
        TextView tv_birthday = (TextView) window.findViewById(R.id.tv_birthday);
        TextView tv_time = (TextView) window.findViewById(R.id.tv_time);
        TextView tv_remark = (TextView) window.findViewById(R.id.tv_remark);
        Button but_discount = (Button) window.findViewById(R.id.but_discount);
        Button but_recharge = (Button) window.findViewById(R.id.but_recharge);
        Button but_balance_paid = (Button) window.findViewById(R.id.but_balance_paid);
        Button but_self_discount= (Button) window.findViewById(R.id.but_self_discount);
        but_self_discount.setText("使用折扣");
        final EditText ed_Discount= (EditText) window.findViewById(R.id.ed_Discount);

        if (SharedUtil.getString("Discount")!=null){
            ed_Discount.setText(SharedUtil.getString("Discount"));
        }
        ed_Discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                SharedUtil.putString("Discount",editable.toString());
            }
        });


        pbmember_id = member_entties.get(i).getMember_id();
        Score=member_entties.get(i).getScore();
        is_require_pass=member_entties.get(i).getIs_require_pass();

        tv_card_number.setText(member_entties.get(i).getMobile());
        tv_discount.setText(member_entties.get(i).getDiscount_rate());
        tv_name.setText(member_entties.get(i).getMember_name());
        tv_balance.setText(member_entties.get(i).getSurplus());
        tv_phone.setText(member_entties.get(i).getMobile());
        tv_integral.setText(member_entties.get(i).getScore());
        if (member_entties.get(i).getBirthday()!=null&&!member_entties.get(i).getBirthday().equals("null")){
            tv_birthday.setText(DateUtils.getDateTimeFromMillisecond((long)Long.parseLong(member_entties.get(i).getBirthday())*1000));
        }
        tv_time.setText(DateUtils.getDateTimeFromMillisecond((long)Integer.parseInt(member_entties.get(i).getTime())*1000));
        tv_remark.setText(member_entties.get(i).getRemark());

        but_discount.setText("使用余额");

        but_self_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double dis_Total=0;
                double Total=0;
                dis_Total=Double.parseDouble(tv_netreceipts.getText().toString());
                if (!ed_Discount.getText().toString().equals("")&&ed_Discount.getText().toString()!=null){
                    dis_Total=TlossUtils.mul(dis_Total,Double.parseDouble(ed_Discount.getText().toString()));
                }else {
                    dis_Total=TlossUtils.mul(dis_Total,1);
                    ed_Discount.setText("1");
                }
                Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Double.parseDouble(member_entties.get(i).getDiscount_rate())));
                tv_netreceipts.setText(Total+"");
                dialog.dismiss();
            }
        });

        //余额
        but_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (commodities.size() > 0) {
                    if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
                        Double tolot1 = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                        tv_netreceipts.setText(tolot1 + "");
                        Showtotal(tolot1+"");
                        float total_amount = 0;
                        if (mSelf_Service_GoodsInfo.size() > 0) {
                            if (Double.parseDouble(member_entties.get(i).getSurplus()) >= Double.parseDouble(tv_netreceipts.getText().toString())) {
                                Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                                tv_netreceipts.setText(tv_netreceipts.getText().toString() + "");
                                Showtotal(tv_netreceipts.getText().toString()+"");
                                mapList.clear();
                                for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                                    Map<String, String> map1 = new HashMap<>();
                                    if (!mSelf_Service_GoodsInfo.get(i).getName().equals("会员充值")) {
                                        map1.put("goods_id", mSelf_Service_GoodsInfo.get(i).getGoods_id());
                                        map1.put("name", mSelf_Service_GoodsInfo.get(i).getName());
                                        map1.put("number", mSelf_Service_GoodsInfo.get(i).getNumber() + "");
                                        map1.put("nums", mSelf_Service_GoodsInfo.get(i).getNumber() + "");
                                        map1.put("price", mSelf_Service_GoodsInfo.get(i).getPrice());
                                        map1.put("cost", mSelf_Service_GoodsInfo.get(i).getCost());
                                        map1.put("amount", (Float.parseFloat(mSelf_Service_GoodsInfo.get(i).getPrice()) * Integer.parseInt(mSelf_Service_GoodsInfo.get(i).getNumber())) + "");
                                        map1.put("py", "");
                                        map1.put("PD", "");
                                        map1.put("GD", "");
                                        map1.put("bn", "");
                                        map1.put("product_id", mSelf_Service_GoodsInfo.get(i).getProduct_id());
                                        map1.put("good_limit", "");
                                        map1.put("good_stock", "");
                                        map1.put("marketable", "");
                                        map1.put("tag_name", "");
                                        map1.put("tag_id", "");
                                        map1.put("unit", "");
                                        map1.put("unit_id", "");
                                        map1.put("bncode", "");
                                        map1.put("orders_status", 1 + "");
                                        map1.put("pay_status", 0 + "");
                                        total_amount += (Float.parseFloat(mSelf_Service_GoodsInfo.get(i).getPrice()) * Double.parseDouble(mSelf_Service_GoodsInfo.get(i).getNumber()));
                                    } else {
                                        map1.put("name", mSelf_Service_GoodsInfo.get(i).getName());
                                        map1.put("number", "1");
                                        map1.put("nums", "1");
                                        map1.put("price", tv_netreceipts.getText().toString() + "");
                                        map1.put("amount", tv_netreceipts.getText().toString() + "");
                                    }
                                    mapList.add(map1);
                                }

                                Gson gson = new Gson();
                                final String str = gson.toJson(mapList);
                                Log.e("print", "余额支付传过去的数据为" + str);
                                //余额支付方法
                                if (is_require_pass.equals("no")){
                                    android.support.v7.app.AlertDialog dialog2 = null;
                                    getbalancepaid( dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), member_entties.get(i).getMobile(), true);
                                }else {
                                    if (!password.equals("") && password.length() == 15) {
                                        AlertDialog dialog2 = null;
                                        getbalancepaid(dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), password, false);
                                    } else {
                                        final AlertDialog dialog2 = new AlertDialog.Builder(Restaurant_Nomal_MainAcitvity.this).create();
                                        dialog2.show();
                                        Window window = dialog2.getWindow();
                                        window.setContentView(R.layout.dialog_memberpaw);
                                        dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                                        LinearLayout ll_title = (LinearLayout) window.findViewById(R.id.ll_title);
                                        Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                                        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);

                                        ll_title.setVerticalGravity(View.GONE);
                                        but_dimdis.setVisibility(View.GONE);
                                        but_qixiao.setVisibility(View.GONE);

                                        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
                                        final EditText ed_paw = (EditText) window.findViewById(R.id.ed_paw);
                                        tv_title.setText("请输入密码");
                                        ed_paw.setHint("请输入密码");
                                        ed_paw.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            }

                                            @Override
                                            public void afterTextChanged(Editable editable) {
                                                if (ed_paw.getText().toString().length() == 15)
                                                    if (Float.parseFloat(member_entties.get(i).getSurplus()) >= Float.parseFloat(tv_netreceipts.getText().toString())) {
                                                        getbalancepaid(dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString(), false);
                                                    } else {
                                                        Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                        });
                                    }
                                }
                                dialog.dismiss();
                            } else {
                                Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"没有商品请选择商品",Toast.LENGTH_SHORT).show();
                        }

//                        pay_type = "1";
                        dialog.dismiss();
//                    }
                }
            }
        });

        //余额支付
        but_balance_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("print","点击余额支付");
                float total_amount = 0;
                if (mSelf_Service_GoodsInfo.size() > 0||mSelf_Service_GoodsInfoorderlist.size()>0) {
                    double dis_Total=0;
                    double Total=0;
                    dis_Total=Double.parseDouble(tv_netreceipts.getText().toString());
                    if (!ed_Discount.getText().toString().equals("")&&ed_Discount.getText().toString()!=null){
                        dis_Total=TlossUtils.mul(dis_Total,Double.parseDouble(ed_Discount.getText().toString()));
                    }else {
                        dis_Total=TlossUtils.mul(dis_Total,1);
                        ed_Discount.setText("1");
                    }
                    Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Double.parseDouble(member_entties.get(i).getDiscount_rate())));

                    if (Double.parseDouble(member_entties.get(i).getSurplus()) >= Total) {
//                        Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                        tv_netreceipts.setText(Total + "");
                        Showtotal(Total + "");
                        mapList.clear();
                        if (mSelf_Service_GoodsInfo.size() > 0) {
                            for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                                Map<String, String> map1 = new HashMap<>();
                                if (!mSelf_Service_GoodsInfo.get(i).getName().equals("会员充值")) {
                                    map1.put("goods_id", mSelf_Service_GoodsInfo.get(i).getGoods_id());
                                    map1.put("name", mSelf_Service_GoodsInfo.get(i).getName());
                                    map1.put("number", mSelf_Service_GoodsInfo.get(i).getNumber() + "");
                                    map1.put("nums", mSelf_Service_GoodsInfo.get(i).getNumber() + "");
                                    map1.put("price", mSelf_Service_GoodsInfo.get(i).getPrice());
                                    map1.put("cost", mSelf_Service_GoodsInfo.get(i).getCost());
                                    map1.put("amount", (Float.parseFloat(mSelf_Service_GoodsInfo.get(i).getPrice()) * Integer.parseInt(mSelf_Service_GoodsInfo.get(i).getNumber())) + "");
                                    map1.put("py", "");
                                    map1.put("PD", "");
                                    map1.put("GD", "");
                                    map1.put("bn", "");
                                    map1.put("product_id", mSelf_Service_GoodsInfo.get(i).getProduct_id());
                                    map1.put("good_limit", "");
                                    map1.put("good_stock", "");
                                    map1.put("marketable", "");
                                    map1.put("tag_name", "");
                                    map1.put("tag_id", "");
                                    map1.put("unit", "");
                                    map1.put("unit_id", "");
                                    map1.put("bncode", "");
                                    map1.put("orders_status", 1 + "");
                                    map1.put("pay_status", 0 + "");
                                    total_amount += (Float.parseFloat(mSelf_Service_GoodsInfo.get(i).getPrice()) * Double.parseDouble(mSelf_Service_GoodsInfo.get(i).getNumber()));

                                } else {
                                    map1.put("name", mSelf_Service_GoodsInfo.get(i).getName());
                                    map1.put("number", "1");
                                    map1.put("nums", "1");
                                    map1.put("price", tv_netreceipts.getText().toString() + "");
                                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                                }
                                mapList.add(map1);
                            }
                        } else if (mSelf_Service_GoodsInfoorderlist.size() > 0) {
                            for (int i = 0; i < mSelf_Service_GoodsInfoorderlist.size(); i++) {
                                Map<String, String> map1 = new HashMap<>();
                                if (!mSelf_Service_GoodsInfoorderlist.get(i).getName().equals("会员充值")) {
                                    map1.put("goods_id", mSelf_Service_GoodsInfoorderlist.get(i).getGoods_id());
                                    map1.put("name", mSelf_Service_GoodsInfoorderlist.get(i).getName());
                                    map1.put("number", mSelf_Service_GoodsInfoorderlist.get(i).getNumber() + "");
                                    map1.put("nums", mSelf_Service_GoodsInfoorderlist.get(i).getNumber() + "");
                                    map1.put("price", mSelf_Service_GoodsInfoorderlist.get(i).getPrice());
                                    map1.put("cost", mSelf_Service_GoodsInfoorderlist.get(i).getCost());
                                    map1.put("amount", (Double.parseDouble(mSelf_Service_GoodsInfoorderlist.get(i).getPrice()) * Double.parseDouble(mSelf_Service_GoodsInfoorderlist.get(i).getNumber())) + "");
                                    map1.put("py", "");
                                    map1.put("PD", "");
                                    map1.put("GD", "");
                                    map1.put("bn", "");
                                    map1.put("product_id", mSelf_Service_GoodsInfoorderlist.get(i).getProduct_id());
                                    map1.put("good_limit", "");
                                    map1.put("good_stock", "");
                                    map1.put("marketable", "");
                                    map1.put("tag_name", "");
                                    map1.put("tag_id", "");
                                    map1.put("unit", "");
                                    map1.put("unit_id", "");
                                    map1.put("bncode", "");
                                    map1.put("orders_status", 1 + "");
                                    map1.put("pay_status", 0 + "");
                                    total_amount += (Float.parseFloat(mSelf_Service_GoodsInfoorderlist.get(i).getPrice()) * Double.parseDouble(mSelf_Service_GoodsInfoorderlist.get(i).getNumber()));

                                } else {
                                    map1.put("name", mSelf_Service_GoodsInfoorderlist.get(i).getName());
                                    map1.put("number", "1");
                                    map1.put("nums", "1");
                                    map1.put("price", tv_netreceipts.getText().toString() + "");
                                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                                }
                                mapList.add(map1);
                            }
                        }
                        Gson gson = new Gson();
                        final String str = gson.toJson(mapList);
                        Log.e("print", "余额支付传过去的数据为" + str);
                        //余额支付方法

                        if (is_require_pass.equals("no")){
                            android.support.v7.app.AlertDialog dialog2 = null;
                            getbalancepaid( dialog2, str, Total+"", (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), member_entties.get(i).getMobile(), true);
                        }else {
                        if (!password.equals("") && password.length() == 15) {
                            AlertDialog dialog2 = null;
                            getbalancepaid(dialog2, str, Total+"", (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), password,false);
                        } else {
                            final AlertDialog dialog2 = new AlertDialog.Builder(Restaurant_Nomal_MainAcitvity.this).create();
                            dialog2.show();
                            Window window = dialog2.getWindow();
                            window.setContentView(R.layout.dialog_memberpaw);
                            dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                            LinearLayout ll_title = (LinearLayout) window.findViewById(R.id.ll_title);
                            Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                            Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);

                            ll_title.setVerticalGravity(View.GONE);
                            but_dimdis.setVisibility(View.GONE);
                            but_qixiao.setVisibility(View.GONE);

                            TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
                            final EditText ed_paw = (EditText) window.findViewById(R.id.ed_paw);
                            tv_title.setText("请输入密码");
                            ed_paw.setHint("请输入密码");
                            final double finalTotal = Total;
                            ed_paw.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if (ed_paw.getText().toString().length() == 15)
                                        if (Float.parseFloat(member_entties.get(i).getSurplus()) >= finalTotal) {
                                            getbalancepaid(dialog2, str, finalTotal+"", (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString(),false);
                                        } else {
                                            Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
                        }
                    }
                        dialog.dismiss();
                    }else {
                        Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"余额不足请充值",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"没有商品请选择商品",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //充值的按钮
        but_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final AlertDialog dialog1 = new AlertDialog.Builder(Restaurant_Nomal_MainAcitvity.this).create();
                dialog1.show();
                final Window window = dialog1.getWindow();
                window.setContentView(R.layout.dialog_recharge);
                lv_recharge = (ListView) window.findViewById(R.id.lv_recharge);
                adapter_specification = new Adapter_specification(Restaurant_Nomal_MainAcitvity.this);
                adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
                    @Override
                    public void onclickdialog(int i) {
                        specification_unms = i;
                        if (mSelf_Service_GoodsInfo.size() == 0) {
                        Log.d("print", "点击了" + specification_list.get(i).getVal());
                        Self_Service_GoodsInfo self_service_goodsInfo = new Self_Service_GoodsInfo();
                        self_service_goodsInfo.setName("会员充值");
                        self_service_goodsInfo.setPrice(specification_list.get(i).getVal()+".00");
//                        self_service_goodsInfo.setPrice("1000");
//                            self_service_goodsInfo.setSize(100+"");
                        self_service_goodsInfo.setNumber(1 + "");
                        self_service_goodsInfo.setCost(100 + "");
                        self_service_goodsInfo.setGoods_id("null");
                        mSelf_Service_GoodsInfo.add(self_service_goodsInfo);
                            if (mRes_Nomal_PalyOrderAdapter!=null){
                                mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                            }else {
                                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, true);
                                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                                list_ordergoods.setSelection(mSelf_Service_GoodsInfo.size());
                            }
                            tv_total_money.setText(specification_list.get(i).getVal()+"");
//                        commodities.add(commodity);
//                        entty.add(shuliang);
//                        adapterzhu.getadats(commodities);
//                        adapterzhu.getEntty(entty);
//                        lv.setAdapter(adapterzhu);
                        dialog1.dismiss();
//                        tv_Totalmerchandise.setText("1");
//                        tv_Total.setText(specification_list.get(i).getVal());
                        tv_netreceipts.setText(specification_list.get(i).getVal());
                        tv_netreceipts.setText(specification_list.get(i).getVal());
                        Showtotal(specification_list.get(i).getVal()+"");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("member_id", member_entty.getMember_id());
                        map.put("surplus", Integer.parseInt(specification_list.get(i).getGive()) +
                                Integer.parseInt(specification_list.get(i).getVal()) + "");
                        Gson gson = new Gson();
                        String s1 = gson.toJson(map);

                        /**
                         * 会员支付的充值
                         */
//                        UPmoney(s1);
                        Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                        but_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });
                    }else {
                         Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"还有订单为处理",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getData();
//                                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                                    final EditText ed_money= (EditText) window.findViewById(R.id.ed_money);
//                                    Button but_Recharge_amount= (Button) window.findViewById(R.id.but_Recharge_amount);
//                                    but_Recharge_amount.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Map<String ,String> map=new HashMap<String, String>();
//                                            map.put("member_id",member_entty.getMember_id());
//                                            map.put("surplus",ed_money.getText().toString());
//                                            Gson gson=new Gson();
//                                            String s1=gson.toJson(map);
//                                            UPmoney(s1,ed_money.getText().toString());
//                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                            boolean isOpen = imm.isActive();
//                                            //isOpen若返回true，则表示输入法打开，反之则关闭。
//                                            if (isOpen) {
//                                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                                            }
//                                            dialog.dismiss();
//                                        }
//                                    });
            }
        });

        //兑换商品的按钮
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getexchange();
                dialog.dismiss();
            }
        });
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
     * 设置副屏金额的方法
     * @param str 设置的金额
     */
    public void Showtotal(String str){
        listInt=SetAdats(str);
        mSerialPortOperaion.WriteData(listInt.get(0),listInt.get(1),listInt.get(2),listInt.get(3),listInt.get(4),
                listInt.get(5),listInt.get(6),listInt.get(7),listInt.get(8),listInt.get(9),listInt.get(10),
                listInt.get(11),listInt.get(12),listInt.get(13),listInt.get(14));
    }


    /**
     * 把金额拆分成数组
     * @param string 金额
     * @return 返回金额拆分的数组
     */
    public List<Integer> SetAdats(String string){
        int command;
        List<Integer> comm=new ArrayList<>();
        for (int i=0;i<string.length();i++){
            String str=string.substring(i,i+1);
            if (str.equals("0")){
                command=0x30;
                comm.add(command);
            }
            if (str.equals("1")){
                command=0x31;
                comm.add(command);
            }
            if (str.equals("2")){
                command=0x32;
                comm.add(command);
            }
            if (str.equals("3")){
                command=0x33;
                comm.add(command);
            }
            if (str.equals("4")){
                command=0x34;
                comm.add(command);
            }
            if (str.equals("5")){
                command=0x35;
                comm.add(command);
            }
            if (str.equals("6")){
                command=0x36;
                comm.add(command);
            }
            if (str.equals("7")){
                command=0x37;
                comm.add(command);
            }
            if (str.equals("8")){
                command=0x38;
                comm.add(command);
            }
            if (str.equals("9")){
                command=0x39;
                comm.add(command);
            }
            if (str.equals(".")){
                command=0x2E;
                comm.add(command);
            }
        }
        int j=comm.size();
        for (;j<8;j++){
            comm.add(0x20);
        }
        if (comm.size()==8){
            comm.add(0,27);
            comm.add(1,115);
            comm.add(2,50);
            comm.add(3,27);
            comm.add(4,81);
            comm.add(5,65);
            comm.add(13);
        }

        Log.e("print","数组为"+comm);
        return comm;

    }

    /**
     * 功能按钮切换
     *
     * @param index
     */
    private void changeTab(int index) {
        currentIndex = index;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //判断当前的Fragment是否为空，不为空则隐藏
        if (null != mCurrentFrgment) {
            if (index == 0) {//设置不刷新页面
                if (mCurrentFrgment != fragmentArrayList.get(0)) {
                    ft.remove(mCurrentFrgment);
                }
            } else {
                ft.hide(mCurrentFrgment);
            }

        }
        //先根据Tag从FragmentTransaction事物获取之前添加的Fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentArrayList.get(currentIndex).getClass().getName());

        if (null == fragment) {
            //如fragment为空，则之前未添加此Fragment。便从集合中取出
            fragment = fragmentArrayList.get(index);
        }
        mCurrentFrgment = fragment;

        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!fragment.isAdded()) {
            ft.add(R.id.fragment, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
    }

    private void initView() {
        et_keyoard = (EditText) findViewById(R.id.et_keyoard);
        et_inputscancode = (EditText) findViewById(R.id.et_inputscancode);
        tv_Surplus = (EditText) findViewById(R.id.tv_Surplus);
        tv_netreceipts = (TextView) findViewById(R.id.tv_netreceipts);
        im_picture = (FlyBanner) findViewById(R.id.im_picture);
        list_goods = (ListView) findViewById(R.id.list_goods);
        list_sort = (ListView) findViewById(R.id.list_sort);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        tv_logn_name = (TextView) findViewById(R.id.tv_logn_name);//登录的商户名
        tv_ordernums = (TextView) findViewById(R.id.tv_ordernums);
        tv_ordertime = (TextView) findViewById(R.id.tv_ordertime);
        tv_peoplenums = (TextView) findViewById(R.id.tv_peoplenums);
        tv_tablenums = (TextView) findViewById(R.id.tv_tablenums);
        tv_goods_nums = (TextView) findViewById(R.id.tv_goods_nums);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_gone = (TextView) findViewById(R.id.tv_gone);
        list_ordergoods = (SwipeMenuListView) findViewById(R.id.list_ordergoods);
        btn_do_package = (Button) findViewById(R.id.btn_do_package);
        btn_do_order = (Button) findViewById(R.id.btn_do_order);
        layout_more = (LinearLayout) findViewById(R.id.layout_more);//更多
        layout_logout = (LinearLayout) findViewById(R.id.layout_logout);//登出
        layout_peoplenums = (RelativeLayout) findViewById(R.id.layout_peoplenums);//人数
        layout_tablenums = (RelativeLayout) findViewById(R.id.layout_tablenums);//桌号
        keyboard_tv_layout = (RelativeLayout) findViewById(R.id.keyboard_tv_layout);//应收金额
        keyboard_et_layout = (RelativeLayout) findViewById(R.id.keyboard_et_layout);//实收金额
        but_Cashbox = (FButton) findViewById(R.id.but_Cashbox);//现金支付
        but_mobilepayment = (FButton) findViewById(R.id.but_mobilepayment);//移动支付
        tv_cancel = (FButton) findViewById(R.id.tv_cancel);//取消
        ll_jshuang = (LinearLayout) findViewById(R.id.ll_jshuang);//计算器页面
        Rl_xianjin = (RelativeLayout) findViewById(R.id.Rl_xianjin);//现金确定页面
        Rl_time = (RelativeLayout) findViewById(R.id.Rl_time);//收款成功页面
        Rl_yidong = (RelativeLayout) findViewById(R.id.Rl_yidong);//移动确定页面
        tv_xianjin_netreceipt = (TextView) findViewById(R.id.tv_xianjin_netreceipt);//现金确定页面：现金应收
        tv_amount = (TextView) findViewById(R.id.tv_amount);//现金确定页面：现金实收
        tv_change = (TextView) findViewById(R.id.tv_change);//现金确定页面：找零
        but_cash_sure = (Button) findViewById(R.id.but_cash_sure);//现金确定页面：现金确定
        tv_yidong = (TextView) findViewById(R.id.tv_yidong);//移动支付页面：应付金额
        im_code = (ImageView) findViewById(R.id.im_code);//移动支付页面：二维码显示
        layout_process_bar = (RelativeLayout) findViewById(R.id.layout_process_bar);//移动支付页面：请稍等
        but_internet = (Button) findViewById(R.id.but_internet);//移动支付页面：无网收银按钮
        btn_mobile_cell = (Button) findViewById(R.id.btn_mobile_cell);//移动支付页面：无网收银按钮
        tv_payment = (TextView) findViewById(R.id.tv_payment);//移动收款成功页面：收款金额
        tv_danhao = (TextView) findViewById(R.id.tv_danhao);//移动收款成功页面：订单号
        tv_day = (TextView) findViewById(R.id.tv_day);//移动收款成功页面：订单时间
        but_time = (Button) findViewById(R.id.but_time);//移动收款成功页面：关闭按钮
        btn_dish = (Button) findViewById(R.id.btn_dish);//点菜按钮
        btn_order = (Button) findViewById(R.id.btn_order);//订单按钮
        btn_table = (Button) findViewById(R.id.btn_table);//餐桌按钮
        btn_moneybox = (Button) findViewById(R.id.btn_moneybox);//钱箱按钮
        btn_printer = (Button) findViewById(R.id.btn_printer);//打印按钮
        btn_refresh = (Button) findViewById(R.id.btn_refresh);//同步按钮
        btn_lock_screen = (Button) findViewById(R.id.btn_lock_screen);//锁屏按钮
        btn_ordersnums = (Button) findViewById(R.id.btn_ordersnums);//挂单数量
        tv_dopackage = (TextView) findViewById(R.id.tv_dopackage);//打包显示
        btn_search = (Button) findViewById(R.id.btn_search);//搜索按钮

        but_member= (Button) findViewById(R.id.but_huiyuan);//会员的按钮
        but_reduce= (Button) findViewById(R.id.but_reduce);


        but_set_print= (Button) findViewById(R.id.but_set_print);
        but_set_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPrint();
            }
        });
        //设置输入框 手动点击输入
        et_inputscancode.setInputType(InputType.TYPE_NULL);
        tv_netreceipts.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(tv_Surplus);
        tv_Surplus.setInputType(InputType.TYPE_NULL);
        et_keyoard.setInputType(InputType.TYPE_NULL);

//        list_sort.setOnItemClickListener(this);
        keyboard_et_layout.setOnClickListener(this);
        keyboard_tv_layout.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        but_Cashbox.setOnClickListener(this);
        but_mobilepayment.setOnClickListener(this);
        but_cash_sure.setOnClickListener(this);
        btn_mobile_cell.setOnClickListener(this);
        but_time.setOnClickListener(this);
        layout_peoplenums.setOnClickListener(this);
        layout_tablenums.setOnClickListener(this);
        btn_dish.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_table.setOnClickListener(this);
        btn_lock_screen.setOnClickListener(this);
        layout_more.setOnClickListener(this);
        btn_do_package.setOnClickListener(this);
        btn_do_order.setOnClickListener(this);
        tv_dopackage.setOnClickListener(this);
        layout_logout.setOnClickListener(this);
        btn_moneybox.setOnClickListener(this);
        btn_printer.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        but_member.setOnClickListener(this);


        SetEditTextInput.setPricePoint(et_keyoard);
        setSwipeMenuListViewDelete(list_ordergoods);//左滑动删除
        keyboard_tv_layout.performClick();//自动触发应收栏被点击
        changeTab(0);



        //蓝牙打印
        mService = new BluetoothService(this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
        }

        //蓝牙标签打印机绑定服务
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
        //登录人信息
        String username="";
        try {
            username = SharedUtil.getString("name");
        }catch (Exception e){}
        tv_logn_name.setText(username);

        //条形码工具类
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);


        //判断蓝牙是否连接小票打印机
        String address_Mac=SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
        if(TextUtils.isEmpty(address_Mac)){
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"如需打印小票，请先点击打印按钮，连接小票打印机",20);
        }else {
            con_dev = mService.getDevByMac(address_Mac);
            mService.connect(con_dev);
        }
    }

    //初始化监听
    private void initListener() {

        but_reduce.setOnClickListener(this);

        //监听实收输入框数值的变化
        et_inputscancode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tv_netreceipts_src = tv_netreceipts.getText().toString().trim();
                String et_inputscancode_src = et_inputscancode.getText().toString().trim();
                if (TextUtils.isEmpty(tv_netreceipts_src)) {
                    tv_netreceipts_src = "0.00";
                }
                if (TextUtils.isEmpty(et_inputscancode_src)) {
                    et_inputscancode_src = "0.00";
                }
                float f1 = Float.parseFloat(tv_netreceipts_src);
                float f2 = Float.parseFloat(et_inputscancode_src);
                float f3 = f2 - f1;
                if (f3 < 0) {
                    f3 = 0;
                }
                tv_Surplus.setText(Utils.StringUtils.stringpointtwo(f3 + ""));
            }
        });
        //监听应收输入框数值的变化
        tv_netreceipts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tv_netreceipts_src = tv_netreceipts.getText().toString().trim();
                String et_inputscancode_src = et_inputscancode.getText().toString().trim();
                if (TextUtils.isEmpty(tv_netreceipts_src)) {
                    tv_netreceipts_src = "0.00";
                }
                if (TextUtils.isEmpty(et_inputscancode_src)) {
                    et_inputscancode_src = "0.00";
                }
                float f1 = Float.parseFloat(tv_netreceipts_src);
                float f2 = Float.parseFloat(et_inputscancode_src);
                float f3 = f2 - f1;
                if (f3 < 0) {
                    f3 = 0;
                }
                tv_Surplus.setText(Utils.StringUtils.stringpointtwo(f3 + ""));
            }
        });
    }

    private void initData() {

        mapList=new ArrayList<>();
        integral_list=new ArrayList<>();
        member_entties=new ArrayList<>();
        mSelf_Service_GoodsInfo = new ArrayList<>();
        mSelf_Service_GoodsInfoorderlist=new ArrayList<>();
        mSelf_Service_Goods=new ArrayList<>();
        //副屏显示金额
        mSerialPortOperaion = new SerialPortOperaion(null,
                new SerialParam(2400,"/dev/ttyS3",0));
        try {
            mSerialPortOperaion.StartSerial();
        }catch (Exception e) {
        }


        GetSellerPhone();
        getOrderNums();
        getImagesList();

//        GetGoods_info();
    }

    //设置左滑删除
    private void setSwipeMenuListViewDelete(final SwipeMenuListView mListView) {

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(70);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if (mRes_Nomal_PalyOrderAdapter.getisClick()) {
                            if (mSelf_Service_GoodsInfo.size() > 0) {
                                mSelf_Service_GoodsInfo.remove(position);
                                setTotalPrice(mSelf_Service_GoodsInfo);
                                Log.d("print", "删除的数据1111："+mSelf_Service_GoodsInfo);
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));

                            }
                            if (mSelf_Service_GoodsInfoorderlist.size()>position) {
                                if (mSelf_Service_GoodsInfoorderlist != null && mSelf_Service_GoodsInfoorderlist.size() > 0) {
                                    mSelf_Service_GoodsInfoorderlist.remove(position);
                                    setTotalPrice(mSelf_Service_GoodsInfoorderlist);
                                    Log.d("print", "删除的数据2222：" + mSelf_Service_GoodsInfo);
                                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));
                                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));
                                }
                            }
                            mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                if (mRes_Nomal_PalyOrderAdapter.getisClick()) {
                    if (mSelf_Service_GoodsInfo.size() > 0) {
                        AddGoodsNumsNoteDialog(position, mSelf_Service_GoodsInfo);
                    } else if (mSelf_Service_GoodsInfoorderlist.size() > 0) {
                        AddGoodsNumsNoteDialog(position, mSelf_Service_GoodsInfoorderlist);
                    }
                }
            }
        });
    }
    //获取轮播图
    private void getImagesList() {
        imageurls = new ArrayList<>();
        OkGo.post(SysUtils.getSellerServiceUrl("get_pic"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.d("print", "轮播图" + jsonObject.toString());
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja = jo1.getJSONArray("data");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo2 = ja.getJSONObject(i);
                                    imageurls.add(jo2.getString("link"));
                                }
                                im_picture.setImagesUrl(imageurls);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //计算器总金额设置
    private double getSumstr(String str) {
        String s = new String(str);
        String a[] = s.split("[+]");
        double res = 0;
        String s11 = "";
        for (int i = 0; i < a.length; i++) {
            if (!(a[i] == "")) {
                double yy = Double.parseDouble(a[i]);
                CharSequence tt = (yy + "");
                if (tt.toString().contains(".")) {
                    if (tt.length() - 1 - tt.toString().indexOf(".") > 2) {
                        tt = tt.toString().subSequence(0,
                                tt.toString().indexOf(".") + 3);
                    }
                }
                res += yy;
            }
        }
        return res;
    }

    /**
     * 数字键盘0-9的点击事件
     *
     * @param nums
     */
    private void setOne_NineClickEns(String nums) {
        String myString = et_keyoard.getText().toString();
        myString += nums;
        if (getSumstr(myString) >= MIXMONEY) {
            Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSelected) {
            et_keyoard.setText(myString);
            et_inputscancode.setText(getSumstr(myString) + "");
        } else {
            if (mSelf_Service_GoodsInfo.size() < 1) {//有选择的商品时，应收金额不能被编辑，数字键盘只对应实收金额
                et_keyoard.setText(myString);
                tv_netreceipts.setText(getSumstr(myString) + "");
                Showtotal(getSumstr(myString) +"");
            }
        }
    }

    /**
     * 数字键盘10-100的点击事件
     *
     * @param nums
     */
    private void setTenty_OnehundradClick(int nums) {
        String et_inputscancode_src = et_inputscancode.getText().toString().trim();
        String tv_netreceipts_src = tv_netreceipts.getText().toString().trim();
        if (TextUtils.isEmpty(et_inputscancode_src)) {
            et_inputscancode_src = "0.0";
        }
        if (TextUtils.isEmpty(tv_netreceipts_src)) {
            tv_netreceipts_src = "0.0";
        }
        if (isSelected) {
            et_keyoard.setText(et_inputscancode_src + "+" + nums);
            float f1 = nums + Float.parseFloat(et_inputscancode_src);
            et_inputscancode.setText(Utils.StringUtils.stringpointtwo(f1 + ""));
            float i1 = f1 - Float.parseFloat(tv_netreceipts_src);
            if (i1 >= 0) {
                tv_Surplus.setText(Utils.StringUtils.stringpointtwo(i1 + ""));
            } else {
                tv_Surplus.setText("0");
            }
        } else {
            if (mSelf_Service_GoodsInfo.size() < 1) {
                et_keyoard.setText(tv_netreceipts_src + "+" + nums);
                float f1 = nums + Float.parseFloat(tv_netreceipts_src);
                tv_netreceipts.setText(Utils.StringUtils.stringpointtwo(f1 + ""));
                Showtotal(Utils.StringUtils.stringpointtwo(f1 + ""));
                float i1 = nums - Float.parseFloat(tv_netreceipts_src);
                if (i1 >= 0) {
                    tv_Surplus.setText(Utils.StringUtils.stringpointtwo(i1 + ""));
                } else {
                    tv_Surplus.setText("0");
                }
            }
        }
    }

    //数字键盘的监听
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_one:
                setOne_NineClickEns("1");
                break;
            case R.id.keyboard_two:
                setOne_NineClickEns("2");
                break;
            case R.id.keyboard_three:
                setOne_NineClickEns("3");
                break;
            case R.id.keyboard_four:
                setOne_NineClickEns("4");
                break;
            case R.id.keyboard_five:
                setOne_NineClickEns("5");
                break;
            case R.id.keyboard_six:
                setOne_NineClickEns("6");
                break;
            case R.id.keyboard_seven:
                setOne_NineClickEns("7");
                break;
            case R.id.keyboard_eight:
                setOne_NineClickEns("8");
                break;
            case R.id.keyboard_nine:
                setOne_NineClickEns("9");
                break;
            case R.id.keyboard_zro:
                setOne_NineClickEns("0");
                break;
            //点的监听
            case R.id.keyboard_point:
                String myStringpoint = et_keyoard.getText().toString().trim();
                if (myStringpoint.length() < 1) {
                    return;
                }
                if (myStringpoint.substring(myStringpoint.length() - 1).equals("+")) {
                    et_keyoard.setText(myStringpoint);
                } else {
                    myStringpoint += ".";
                    et_keyoard.setText(myStringpoint);
                }
                if (myStringpoint.length() > 0) {
                    if ((myStringpoint.substring(myStringpoint.length() - 1)).equals(".")) {
                        et_keyoard.setText(myStringpoint);
                    } else {
                        myStringpoint += ".";
                        et_keyoard.setText(myStringpoint);
                    }
                }
                if (myStringpoint.length() >= 2 && myStringpoint.length() < 3) {
                    if ((myStringpoint.substring(myStringpoint.length() - 1)).equals(".") || (myStringpoint.substring(myStringpoint.length() - 2)).equals(".")) {
                        et_keyoard.setText(myStringpoint);
                    } else {
                        myStringpoint += ".";
                        et_keyoard.setText(myStringpoint);
                    }
                }
                break;


            case R.id.keyboardcancell_layout:
                if (isSelected) {
                    String cancell_str = et_keyoard.getText().toString().trim();
                    if (cancell_str.length() <= 0) {
                        et_inputscancode.setText("");
                        return;
                    }
                    et_keyoard.setText(cancell_str.substring(0, cancell_str.length() - 1));
                    if ((getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "") == "") {
                        et_inputscancode.setText("");
                    } else {
                        et_inputscancode.setText(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
                    }
                } else {
                    if (mSelf_Service_GoodsInfo == null) {
                        mSelf_Service_GoodsInfo = new ArrayList<>();
                    }
                    if (mSelf_Service_GoodsInfo.size() < 1) {
                        String cancell_str = et_keyoard.getText().toString().trim();
                        if (cancell_str.length() <= 0) {
                            tv_netreceipts.setText("");
                            Showtotal("0");
                            return;
                        }
                        et_keyoard.setText(cancell_str.substring(0, cancell_str.length() - 1));
                        if ((getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "") == "") {
                            tv_netreceipts.setText("");
                            Showtotal("0");
                        } else {
                            tv_netreceipts.setText(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
                            Showtotal(getSumstr(cancell_str.substring(0, cancell_str.length() - 1))+"");
                        }
                    }
                }
                break;

            case R.id.keyboard_add_layout:
                if (isSelected) {
                    CharSequence myStringadd = et_keyoard.getText();
                    et_keyoard.setText(myStringadd);
                    String str = myStringadd.toString();
                    if (myStringadd.length() > 0) {
                        if ((str.substring(myStringadd.length() - 1)).equals("+") || (str.substring(myStringadd.length() - 1)).equals(".")) {
                            et_keyoard.setText(myStringadd);
                        } else {
                            str += "+";
                            et_keyoard.setText(str);
                            if (getSumstr(str) <= MIXMONEY) {
                                et_inputscancode.setText(getSumstr(str) + "");
                            } else {
                                Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    CharSequence myStringadd = et_keyoard.getText();
                    et_keyoard.setText(myStringadd);
                    String str = myStringadd.toString();
                    if (myStringadd.length() > 0) {
                        if ((str.substring(myStringadd.length() - 1)).equals("+") || (str.substring(myStringadd.length() - 1)).equals(".")) {
                            et_keyoard.setText(myStringadd);
                        } else {
                            if (mSelf_Service_GoodsInfo.size() < 1) {
                                str += "+";
                                et_keyoard.setText(str);
                                if (getSumstr(str) <= MIXMONEY) {
                                    tv_netreceipts.setText(getSumstr(str) + "");
                                    Showtotal(getSumstr(str) + "");
                                } else {
                                    Toast.makeText(Restaurant_Nomal_MainAcitvity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                }
                break;

            case R.id.but_Ten:
                setTenty_OnehundradClick(10);
                break;
            case R.id.but_Twenty:
                setTenty_OnehundradClick(20);
                break;
            case R.id.but_Fifty:
                setTenty_OnehundradClick(50);
                break;
            case R.id.but_Onehundred:
                setTenty_OnehundradClick(100);
                break;
        }

    }

    //记录会员支付的密码
    public String password="";
    //获得会员的信息
    public void getsearch_members(final String member1){
        String member="";
        if (member1.length()>11){
            member=member1.substring(0,11);
        }else {
            member=member1;
        }
        Log.e("会员的信息",""+ SysUtils.getSellerServiceUrl("search_members"));
        PostRequest search_members = OkGo.post(SysUtils.getSellerServiceUrl("search_members"))
                .tag(this);

        if (Utils.StringUtils.isCard(member)){
            search_members.params("member_name",member);
        }else {
            search_members.params("mobile",member);
        }
        search_members.execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("获得会员的信息",""+s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                password=member1;
                                JSONObject jo2 = jo1.getJSONObject("data");
                                JSONArray ja = jo2.getJSONArray("info");
                                member_entties.clear();
                                for(int l=0;l<ja.length();l++){
                                    member_entty=new Member_entty();
                                    JSONObject jo3 = ja.getJSONObject(l);
                                    member_entty.setMember_id(jo3.getString("member_id"));
                                    member_entty.setSurplus(jo3.getString("surplus"));
                                    member_entty.setMember_name(jo3.getString("member_name"));
                                    member_entty.setMobile(jo3.getString("mobile"));
                                    member_entty.setDiscount_rate(jo3.getString("discount_rate"));
                                    member_entty.setScore(jo3.getString("score"));
                                    member_entty.setTime(jo3.getString("addtime"));
                                    member_entty.setBirthday(jo3.getString("birthday"));
                                    member_entty.setRemark(jo3.getString("remark"));
                                    member_entty.setIs_require_pass(jo3.getString("is_require_pass"));
                                    member_entties.add(member_entty);
                                }
                                adapter_optimize.setAdats(member_entties);
                                lv_member.setAdapter(adapter_optimize);
                                /**
                                 * 优化会员搜索
                                 */
                            }else {
                                Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"没有该会员",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    //使用余额支付
    public void getbalancepaid(final AlertDialog dia, String map, String f, int i, final String str,String pay_code,boolean is_pass){
        PostRequest params = OkGo.post(SysUtils.getSellerServiceUrl("surplus_pay"))
                .tag(this)
                .params("type", "1")
                .params("commodity", map)
                .params("total_fee", f)
                .params("score", i)
                .params("pay_code", pay_code)
                .params("member_id", str);

                if (is_pass){
                    params.params("mobile",pay_code);
                }

//        if (tv_ordernums.getText().toString().trim() == null) {
//        if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim()) && tv_ordernums.getText().toString().trim().length() > 10) {
//            params.params("order_id",tv_ordernums.getText().toString().trim());
//        }
            params.execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print","余额支付的上传数据"+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","余额"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String message=jo1.getString("message");
                            if (status.equals("200")){
                                if (mSelf_Service_GoodsInfoorderlist.size()>0){

                                    if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim()) && tv_ordernums.getText().toString().trim().length() > 10) {
                                        delete_order(tv_ordernums.getText().toString().trim());
                                        }
                                    changeTab(0);
                                    setBtnBackgroud(btn_dish, btn_table, btn_order);
                                    StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "收款成功！", 20);
                                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));
                                    if (open_table) {
                                        if (!TextUtils.isEmpty(table_id)) {
                                            Log.d("print打印那个餐桌", "table_id: "+table_id);
                                            editTableStatus("0", table_id);
                                        }
                                    }
                                }
//                                getOrderNums();
//                                setInitData();
                                String data =jo1.getString("data");
                                if (SharedUtil.getBoolean("self_print")) {
                                    /**
                                    //判断是否自动打印小票
                                     */
                                    getordenums();
                                    if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
                                        PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,data, Long.parseLong(DateUtils.getTime()), "", 1, Double.parseDouble(tv_netreceipts.getText().toString())+"",Double.parseDouble(tv_netreceipts.getText().toString())+"","0.0",6,mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                    }else {
                                        PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,data, Long.parseLong(DateUtils.getTime()), "", 1, Double.parseDouble(tv_netreceipts.getText().toString())+"",Double.parseDouble(tv_netreceipts.getText().toString())+"","0.0",6,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                    }
                                }
                                if (mGpService!=null){
                                    PrintUtils.Setprint(Restaurant_Nomal_MainAcitvity.this, mGpService, data.substring(data.length() - 4, data.length()),
                                            DateUtils.getNowtimeKeyStr(), mSelf_Service_GoodsInfo);
                                }
                                Printer_activity.setPaintSelf_srevice(Restaurant_Nomal_MainAcitvity.this, data.substring(data.length() - 4, data.length()),
                                        DateUtils.getNowtimeKeyStr(), mSelf_Service_GoodsInfo);

                                setInitData();

                                if (dia!=null){
                                    dia.dismiss();
                                }
                                Toast.makeText(Restaurant_Nomal_MainAcitvity.this,message,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Restaurant_Nomal_MainAcitvity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //充值金额
    public void UPmoney(String map){
        String operator_id="0";
        if (SharedUtil.getString("type").equals("4")){
            operator_id=SharedUtil.getString("operator_id");
        }
        OkGo.post(SysUtils.getSellerServiceUrl("surplus_add"))
                .tag(this)
                .params("worker_id",operator_id)
                .params("map",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","充值的金额"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                            }
                            String data=jo1.getString("data");
                            Toast.makeText(Restaurant_Nomal_MainAcitvity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //兑换商品的列表
    public void getexchange(){

        final Dialog dialog= new Dialog(Restaurant_Nomal_MainAcitvity.this);
        dialog.setTitle("会员");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_exchange);
        lv_exchange= (ListView) window.findViewById(R.id.lv_exchange);
        adapter_integral=new Adapter_integral(this,1);
        LoadAdats();
        Button but_indeed= (Button) window.findViewById(R.id.but_indeed);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        but_indeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score=0;
                List<Map<String,String>> mapList1=new ArrayList<>();
                for (int y=0;y<integral_list.size();y++){
                    if (integral_list.get(y).isElect()){
                        Map<String,String> stringMap=new HashMap<String, String>();
                        stringMap.put("swap_goods_id",integral_list.get(y).getSwap_goods_id());
                        stringMap.put("score",integral_list.get(y).getScore());
                        stringMap.put("nums","1");
                        stringMap.put("goods_id",integral_list.get(y).getGoods_id());
                        score+=Integer.parseInt(integral_list.get(y).getScore());
                        mapList1.add(stringMap);
                    }
                }
                Gson gson=new Gson();
                String integral=gson.toJson(mapList1);
                Log.e("print","string会员数据为"+integral);
                if (score<Integer.parseInt(Score)){
                    Upcredits(pbmember_id,score,integral);
                }else {
                    Toast.makeText(Restaurant_Nomal_MainAcitvity.this,"积分不足",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

    }

    //充值金额的列表
    private void getData(){
        OkGo.post(SysUtils.getSellerServiceUrl("recharge_list"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","规格的"+s);
                        try {
                            specification_list.clear();
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    Specification_Entty specification=new Specification_Entty();
                                    specification.setRecharge_id(jo2.getString("recharge_id"));
                                    specification.setGive(jo2.getString("give"));
                                    specification.setVal(jo2.getString("val"));
                                    specification_list.add(specification);
                                }
                            }
                            adapter_specification.setAdats(specification_list,0);
                            lv_recharge.setAdapter(adapter_specification);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //加载积分兑换的商品
    private void LoadAdats() {
        OkGo.post(SysUtils.getGoodsServiceUrl("swap_goods_list"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","积分兑换的商品是"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            integral_list.clear();
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    Integral_Entty integral=new Integral_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    integral.setBncode(jo2.getString("bncode"));
                                    integral.setPrice(jo2.getString("price"));
                                    integral.setName(jo2.getString("name"));
                                    integral.setScore(jo2.getString("score"));
                                    integral.setNums(jo2.getString("nums"));
                                    integral.setSwap_goods_id(jo2.getString("swap_goods_id"));
                                    integral.setGoods_id(jo2.getString("goods_id"));
                                    integral.setElect(false);
                                    integral_list.add(integral);
                                }
                            }
                            adapter_integral.setAdats(integral_list);
                            lv_exchange.setAdapter(adapter_integral);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

//
    }

    //积分兑换的接口
    private void Upcredits(String member_id, final int score, final String str){
        String typeword="0";
        if (SharedUtil.getString("type").equals("4")){
            typeword=SharedUtil.getString("operator_id");
        }
        OkGo.post(SysUtils.getGoodsServiceUrl("swap_goods"))
                .tag(this)
                .params("member_id",member_id)
                .params("score",score)
                .params("work_id",typeword)
                .params("map",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("积分兑换返回的数据为",""+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                double talot=0;
                                talot=TlossUtils.mul(Double.parseDouble(tv_balance.getText().toString()),
                                        score);
//                                tv_integral.setText(talot+"");
                            }
                            String data=jo1.getString("data");
                            Toast.makeText(Restaurant_Nomal_MainAcitvity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * type 1 点击商品  2.计算 3，点击挂单（取单操作）4，订单标签打印，5订单小票打印
             */
            int type = intent.getIntExtra("type", 0);
            Log.d("print","点击的type"+type);
            if (mRes_Nomal_PalyOrderAdapter != null) {
                if (!mRes_Nomal_PalyOrderAdapter.getisClick()) {
                    Log.e("mSelf_Service_GoodsInfo", "mSelf_Service_GoodsInfo: " + mSelf_Service_GoodsInfo);
                    return;//支付状态不能编辑订单商品
                }
            }
            if (type == 1) {
                isEditOrder = true;
                et_inputscancode.setText("0.0");
                et_keyoard.setText("");
                keyboard_et_layout.performClick();//选择商品时，自动定位到实收选中栏

//                if(mSelf_Service_GoodsInfoorderlist!=null){
//                        mSelf_Service_GoodsInfoorderlist.clear();
//                }else {
//                    mSelf_Service_GoodsInfoorderlist.addAll(mSelf_Service_GoodsInfo;
//                }
//                mSelf_Service_GoodsInfo.clear();
                mSelf_Service_Goods = intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfoList");

                if (newmSelf_Service_GoodsInfo.size()>0){
                    boolean isHas1 = false;
                    for (int k = 0; k < newmSelf_Service_GoodsInfo.size(); k++) {
                            if (newmSelf_Service_GoodsInfo.get(k).getGoods_id().equals(mSelf_Service_Goods.get(0).getGoods_id())) {
                                isHas1=true;
                                k=0;
                                break;
                            } else {
                                isHas1=false;
                            }
                    }
                    if (!isHas1){
                        newmSelf_Service_GoodsInfo.add(mSelf_Service_Goods.get(0));
                    }
                }else {
                    newmSelf_Service_GoodsInfo.add(mSelf_Service_Goods.get(0));
                }

                boolean isHas = false;
                for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                    if (mSelf_Service_GoodsInfo.get(i).getGoods_id().equals(mSelf_Service_Goods.get(0).getGoods_id())) {
                        String nums_src = mSelf_Service_GoodsInfo.get(i).getNumber();
                        int nums = Integer.parseInt(nums_src);
                        nums++;
                        mSelf_Service_GoodsInfo.get(i).setNumber(nums + "");
                        isHas = true;
                        break;
                    } else {
                        isHas = false;
                    }
                }
                if (!isHas) {
                    for (int i=0;i<mSelf_Service_Goods.size();i++){
                        mSelf_Service_GoodsInfo.add(mSelf_Service_GoodsInfo.size(),mSelf_Service_Goods.get(i));
                    }
                }

//                if (mSelf_Service_Goods!=null&&mSelf_Service_Goods.size()>0){
//                    mSelf_Service_GoodsInfo.addAll(0,mSelf_Service_Goods);
//                }

//                Log.e("print", "打印商品大小2: "+mSelf_Service_GoodsInfoorderlist.size() );

//                    for (int k=0;k<mSelf_Service_GoodsInfoorderlist.size();k++){
////                        if (isEditOrder){
//                            mSelf_Service_GoodsInfo.add(mSelf_Service_GoodsInfoorderlist.get(k));
////                        }
//                    }


//                mSelf_Service_GoodsInfo.addAll(mSelf_Service_GoodsInfoorderlist);

                mSelf_Service_GoodsInfoorderlist.clear();
                for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
                    mSelf_Service_GoodsInfoorderlist.add(mSelf_Service_GoodsInfo.get(i));
                }

//                mSelf_Service_GoodsInfoorderlist.clear();
//                if (mSelf_Service_GoodsInfoorderlist!=null){
////                    for (int k=0;k<mSelf_Service_GoodsInfo.size();k++){
////                        mSelf_Service_GoodsInfoorderlist.add(mSelf_Service_GoodsInfo.get(k));
////                    }
//                    boolean isHas1 = false;
//                    for (int i = 0; i < mSelf_Service_GoodsInfoorderlist.size(); i++) {
//                        if (mSelf_Service_GoodsInfoorderlist.get(i).getGoods_id().equals(mSelf_Service_Goods.get(0).getGoods_id())) {
//                            String nums_src = mSelf_Service_GoodsInfoorderlist.get(i).getNumber();
//                            int nums = Integer.parseInt(nums_src);
//                            nums++;
//                            mSelf_Service_GoodsInfoorderlist.get(i).setNumber(nums + "");
//                            isHas1 = true;
//                            break;
//                        } else {
//                            isHas1 = false;
//                        }
//                    }
//                    if (!isHas1) {
//                        for (int i=0;i<mSelf_Service_Goods.size();i++){
//                            mSelf_Service_GoodsInfoorderlist.add(mSelf_Service_GoodsInfoorderlist.size(),mSelf_Service_Goods.get(i));
//                        }
//                    }
//                }


//                Log.e("print", "打印商品大小3: "+mSelf_Service_GoodsInfo.size() );
//                Log.e("print", "打印商品大小4: "+mSelf_Service_GoodsInfoorderlist.size() );

//                mSelf_Service_GoodsInfoorderlist.clear();
                Log.e("print", "mSelf_Service_GoodsInfo2: "+mSelf_Service_GoodsInfo );
                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, true);
                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                list_ordergoods.setSelection(mSelf_Service_GoodsInfo.size());
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 2));
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
            } else if (type == 2) {
                setTotalPrice(mSelf_Service_GoodsInfo);
            } else if (type == 3) {
                isEditOrder = false;
                keyboard_et_layout.performClick();//选择商品时，自动定位到实收选中栏
                if (mRes_GoodsOrderslist == null) {
                    mRes_GoodsOrderslist = new ArrayList<>();
                }
                if(mSelf_Service_GoodsInfo!=null){
                    mSelf_Service_GoodsInfo.clear();
                }
                mSelf_Service_GoodsInfoorderlist = new ArrayList<>();
                mRes_GoodsOrderslist = intent.getParcelableArrayListExtra("mRes_GoodsOrderslist");
                int position = intent.getIntExtra("position", 0);
                mSelf_Service_GoodsInfoorderlist = mRes_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo();
                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfoorderlist, true);
                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                list_ordergoods.setSelection(mSelf_Service_GoodsInfoorderlist.size());

                tv_goods_nums.setText(mRes_GoodsOrderslist.get(position).getOrder_total_nums());
                tv_total_money.setText("￥ " + Utils.StringUtils.stringpointtwo(mRes_GoodsOrderslist.get(position).getOrder_total_money()));
                tv_netreceipts.setText(Utils.StringUtils.stringpointtwo(mRes_GoodsOrderslist.get(position).getOrder_total_money()));
                tv_peoplenums.setText(mRes_GoodsOrderslist.get(position).getOrder_people_num());
                Showtotal(Utils.StringUtils.stringpointtwo(mRes_GoodsOrderslist.get(position).getOrder_total_money()));
                if ("true".equals(mRes_GoodsOrderslist.get(position).getOrder_dopackage())) {
                    doPackage = true;
                    btn_do_package.setBackgroundResource(R.drawable.btn_corner_orange);
                    tv_dopackage.setText("打包");
                } else {
                    doPackage = false;
                    btn_do_package.setBackgroundResource(R.drawable.btn_whilte_red);
                    tv_dopackage.setText("");
                }
                    table_id=mRes_GoodsOrderslist.get(position).getTable_id();
                    tv_peoplenums.setText(mRes_GoodsOrderslist.get(position).getOrder_people_num());
                    tv_tablenums.setText(mRes_GoodsOrderslist.get(position).getOrder_table_nums());
                    tv_ordernums.setText(mRes_GoodsOrderslist.get(position).getOrder_id());
                    tv_ordertime.setText(mRes_GoodsOrderslist.get(position).getOrder_time());

                    pre_goodsordernums = mRes_GoodsOrderslist.get(position).getOrder_id();
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));

                changeTab(0);
                setBtnBackgroud(btn_dish, btn_table, btn_order);

//                if (mSelf_Service_Goods!=null&&mSelf_Service_Goods.size()>0){
//                    mSelf_Service_GoodsInfo.addAll(0,mSelf_Service_Goods);
//                }

                mSelf_Service_GoodsInfo.clear();
                if (mSelf_Service_GoodsInfo!=null){
                    for (int k=0;k<mRes_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo().size();k++){
                        mSelf_Service_GoodsInfo.add(mRes_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo().get(k));
                    }
                }
            }else if (type==4){
                if (mRes_GoodsOrderslist == null) {
                    mRes_GoodsOrderslist = new ArrayList<>();
                }
                int position=intent.getIntExtra("position",0);
                mRes_GoodsOrderslist=intent.getParcelableArrayListExtra("mRes_GoodsOrderslist");
              //打印订单标签
                    PrintUtil.order_RabelPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mRes_GoodsOrderslist.get(position).getOrder_id(),0,mRes_GoodsOrderslist.get(position).getOrder_time(),0,
                            mRes_GoodsOrderslist.get(position).getOrder_total_money(),"0","0",3,
                            mRes_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo());

            }else if(type==5){
                if (mRes_GoodsOrderslist == null) {
                    mRes_GoodsOrderslist = new ArrayList<>();
                }
                mRes_GoodsOrderslist=intent.getParcelableArrayListExtra("mRes_GoodsOrderslist");
                int position=intent.getIntExtra("position",0);

                if (mRes_GoodsOrderslist.size()>0){
                    getordenums();
                    //打印小票
                    PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,mRes_GoodsOrderslist.get(position).getOrder_id(),0,mRes_GoodsOrderslist.get(position).getOrder_time(),0,
                            mRes_GoodsOrderslist.get(position).getOrder_total_money(),"0","0",3,
                            mRes_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo(),newmSelf_Service_GoodsInfo,mRes_GoodsOrderslist.get(position).getOrder_dopackage(),
                            mRes_GoodsOrderslist.get(position).getOrder_people_num(),mRes_GoodsOrderslist.get(position).getOrder_table_nums(),nums1,mRes_GoodsOrderslist.get(position).getOrder_notes());
                }
            }else if(type==6){
                getOrderNums();
            }else if(type==7) {

//                setInitData();

//                if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim())&&!tv_tablenums.getText().toString().equals("--")){
////                    setInitData();
//                    if("挂单".equals(btn_do_order.getText().toString().trim())) {
//                        if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim()) && tv_ordernums.getText().toString().trim().length() > 10) {
//                            if (isEditOrder) {
//                                edit_order(tv_ordernums.getText().toString().trim(), mSelf_Service_GoodsInfo);
//                            } else {
//                                edit_order(tv_ordernums.getText().toString().trim(), mSelf_Service_GoodsInfoorderlist);
//                            }
//                        } else {
//                            do_order();
//                        }
//                    }
//                }
                table_id = intent.getStringExtra("table_id");
                table_name = intent.getStringExtra("table_name");
                peoplenums = intent.getStringExtra("peoplenums");
                if (intent.getStringExtra("money") != null) {
                money = intent.getStringExtra("money");
                Log.d("print", "打印定金金额" + money);

                Self_Service_GoodsInfo self_service_goodsInfo = new Self_Service_GoodsInfo();
                self_service_goodsInfo.setName("定金");
                self_service_goodsInfo.setPrice("-" + money);
                self_service_goodsInfo.setNumber(1 + "");
                self_service_goodsInfo.setCost(100 + "");
                self_service_goodsInfo.setSize("");
                self_service_goodsInfo.setProduct_id("");
                self_service_goodsInfo.setGoods_id("null");
                boolean isHas = false;
                for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                    if (mSelf_Service_GoodsInfo.get(i).getGoods_id().equals(self_service_goodsInfo.getGoods_id())) {
                        isHas = true;
                        break;
                    } else {
                        isHas = false;
                    }
                }
                if (!isHas) {
                        mSelf_Service_GoodsInfo.add(self_service_goodsInfo);
                }
                Log.d("print","打印商品大小1"+mSelf_Service_GoodsInfo.size());
                    mSelf_Service_GoodsInfoorderlist.clear();
                for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
                    mSelf_Service_GoodsInfoorderlist.add(mSelf_Service_GoodsInfo.get(i));
                }
                Log.d("print","打印商品大小2"+mSelf_Service_GoodsInfoorderlist.size());

//                    if (mSelf_Service_GoodsInfoorderlist!=null){
//                        boolean isHas1 = false;
//                        for (int i = 0; i < mSelf_Service_GoodsInfoorderlist.size(); i++) {
//                            if (mSelf_Service_GoodsInfoorderlist.get(i).getGoods_id().equals(self_service_goodsInfo.getGoods_id())) {
//                                isHas1 = true;
//                                break;
//                            } else {
//                                isHas1 = false;
//                            }
//                        }
//                        if (!isHas1) {
//                            mSelf_Service_GoodsInfoorderlist.add(self_service_goodsInfo);
//                        }
//                    }
                    mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, true);
                    list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                    list_ordergoods.setSelection(mSelf_Service_GoodsInfo.size());
                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 2));
                    Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
            }
                tv_tablenums.setText(table_name);
                tv_peoplenums.setText(peoplenums);
            }else if (type==8){
                if (mRes_GoodsFragment!=null){
                    setInitData();
//                    if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim())&&!tv_tablenums.getText().toString().equals("--")){
////                    setInitData();
//                    if("挂单".equals(btn_do_order.getText().toString().trim())) {
//                        if (!TextUtils.isEmpty(tv_ordernums.getText().toString().trim()) && tv_ordernums.getText().toString().trim().length() > 10) {
//                            if (isEditOrder) {
//                                edit_order(tv_ordernums.getText().toString().trim(), mSelf_Service_GoodsInfo);
//                            } else {
//                                edit_order(tv_ordernums.getText().toString().trim(), mSelf_Service_GoodsInfoorderlist);
//                            }
//                        } else {
//                            do_order();
//                        }
//                    }
//                    }
                    changeTab(0);
                    setBtnBackgroud(btn_dish, btn_table, btn_order);
                    table_id=intent.getStringExtra("table_id");
                    table_name=intent.getStringExtra("table_name");
                    peoplenums=intent.getStringExtra("peoplenums");
                    tv_tablenums.setText(table_name);
                    tv_peoplenums.setText(peoplenums);
                }
            }
        }
    };

    private String money;


    //弹出立减的弹窗
    public void showRecharge(){
        final Dialog dialog = new Dialog(Restaurant_Nomal_MainAcitvity.this);
        dialog.setTitle("活动");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.redeem_dialog);
        final EditText ed_money = (EditText) window.findViewById(R.id.ed_money);
        ed_money.setHint("输入优惠金额");
        Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);

        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Self_Service_GoodsInfo self_service_goodsInfo = new Self_Service_GoodsInfo();
                ShuliangEntty shuliang = new ShuliangEntty();
                self_service_goodsInfo.setName("活动立减");
                self_service_goodsInfo.setPrice("-" + ed_money.getText().toString());
                self_service_goodsInfo.setNumber(1 + "");
                self_service_goodsInfo.setCost(100 + "");
                self_service_goodsInfo.setSize("");
                self_service_goodsInfo.setProduct_id("");
                self_service_goodsInfo.setGoods_id("null");
                mSelf_Service_GoodsInfo.add(self_service_goodsInfo);

                Log.d("print","打印商品大小1"+mSelf_Service_GoodsInfo.size());
                mSelf_Service_GoodsInfoorderlist.clear();
                for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
                    mSelf_Service_GoodsInfoorderlist.add(mSelf_Service_GoodsInfo.get(i));
                }
                Log.d("print","打印商品大小2"+mSelf_Service_GoodsInfoorderlist.size());

                if (mRes_Nomal_PalyOrderAdapter!=null){
                    mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                }else {
                    mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, true);
                    list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                    list_ordergoods.setSelection(mSelf_Service_GoodsInfo.size());
                }
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 2));
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                setTotalPrice(mSelf_Service_GoodsInfo);
                if (NoDoubleClickUtils.isSoftShowing(Restaurant_Nomal_MainAcitvity.this)) {
                    imm = (InputMethodManager) Restaurant_Nomal_MainAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                dialog.dismiss();
            }
        });

        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    private void ShowPrint(){
        final Dialog dialog = new Dialog(Restaurant_Nomal_MainAcitvity.this);
        dialog.setTitle("活动");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.list_print);
        CheckBox cc_print= (CheckBox) window.findViewById(R.id.cc_print);
        CheckBox cc_print_kitchen= (CheckBox) window.findViewById(R.id.cc_print_kitchen);
        if (SharedUtil.getfalseBoolean("list_print")){
            cc_print.setChecked(SharedUtil.getfalseBoolean("list_print"));
        }else {
            cc_print.setChecked(SharedUtil.getfalseBoolean("list_print"));
        }
        cc_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedUtil.putfalseBoolean("list_print",b);
                dialog.dismiss();
            }
        });
        if (SharedUtil.getBoolean("kitchen_print")){
            cc_print_kitchen.setChecked(SharedUtil.getBoolean("kitchen_print"));
        }else {
            cc_print_kitchen.setChecked(SharedUtil.getBoolean("kitchen_print"));
        }
        cc_print_kitchen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedUtil.putBoolean("kitchen_print",b);
                dialog.dismiss();
            }
        });

    }

    //计算商品总数据，总价格
    private void setTotalPrice(ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo) {
        int total_nums = 0;
        double total_price = 0.00;
        for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
            String goods_name = mSelf_Service_GoodsInfo.get(i).getName();
            String nums_str = mSelf_Service_GoodsInfo.get(i).getNumber();
            String price_str = mSelf_Service_GoodsInfo.get(i).getPrice();
            int nums = Integer.parseInt(nums_str);
            double price = 0.00;
            try {
                price = Double.parseDouble(price_str);
            } catch (Exception e) {
                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, goods_name + "商品规格异常,请重新添加规格或者拨打服务电话咨询！", 20);
            }
            total_nums += nums;
            total_price += price * nums;
        }
        tv_goods_nums.setText(total_nums + "");
        tv_total_money.setText("￥ " + Utils.StringUtils.stringpointtwo(total_price + ""));
        tv_netreceipts.setText(Utils.StringUtils.stringpointtwo(total_price + ""));
        Showtotal(Utils.StringUtils.stringpointtwo(total_price + ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommonNotesInfo();
        mPreferencesService = new PreferencesService(Restaurant_Nomal_MainAcitvity.this);
        mPreferencesService.setGoodsIsClick(true);
        Restaurant_Nomal_MainAcitvity.this.registerReceiver(broadcastReceiver, new IntentFilter("Restaurant_Nomal_MainAcitvity.Action"));

        open_table=SharedUtil.getBoolean("open_table");
        if(open_table){
            btn_table.setVisibility(View.VISIBLE);
        }else {
            btn_table.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Restaurant_Nomal_MainAcitvity.this.unregisterReceiver(broadcastReceiver);
        OkGo.getInstance().cancelAll();
    }

    //订单商品添加数量与备注
    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    private InputMethodManager imm;
    private  GridView gv_common_notes;

    /**
     * 左侧点击商品添加备注dialog
     * @param position
     * @param mSelf_Service_GoodsInfo_dialog
     */
    private void AddGoodsNumsNoteDialog(final int position, final ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo_dialog) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Restaurant_Nomal_MainAcitvity.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(Restaurant_Nomal_MainAcitvity.this, R.layout.dialog_add_nums_notes, null);
        TextView tv_goodsname = (TextView) view_add_nums_notes.findViewById(R.id.tv_goodsname);
        Button btn_cell = (Button) view_add_nums_notes.findViewById(R.id.btn_cell);
        Button btn_add = (Button) view_add_nums_notes.findViewById(R.id.btn_add);
        Button btn_cell_dialog = (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog = (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
         gv_common_notes = (GridView) view_add_nums_notes.findViewById(R.id.gv_common_notes);

         Button btn_delete= (Button) view_add_nums_notes.findViewById(R.id.btn_delete);


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRes_Nomal_PalyOrderAdapter.getisClick()) {
                    if (mSelf_Service_GoodsInfo.size() > 0) {
                        if (newmSelf_Service_GoodsInfo.size()>0){
                            for (int i=0;i<newmSelf_Service_GoodsInfo.size();i++){
                                if (newmSelf_Service_GoodsInfo.get(i).getGoods_id()==mSelf_Service_GoodsInfo.get(position).getGoods_id()){
                                    newmSelf_Service_GoodsInfo.remove(i);
                                }
                            }
                        }
                        mSelf_Service_GoodsInfo.remove(position);
                        setTotalPrice(mSelf_Service_GoodsInfo);
                        Log.d("print", "删除的数据1111："+mSelf_Service_GoodsInfo);
                        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                    }
                    if (mSelf_Service_GoodsInfoorderlist.size()>position) {
                        if (mSelf_Service_GoodsInfoorderlist != null && mSelf_Service_GoodsInfoorderlist.size() > 0) {
                            if (newmSelf_Service_GoodsInfo.size()>0){
                                for (int i=0;i<newmSelf_Service_GoodsInfo.size();i++){
                                    if (newmSelf_Service_GoodsInfo.get(i).getGoods_id()==mSelf_Service_GoodsInfoorderlist.get(position).getGoods_id()){
                                        newmSelf_Service_GoodsInfo.remove(i);
                                    }
                                }
                            }
                            mSelf_Service_GoodsInfoorderlist.remove(position);
                            setTotalPrice(mSelf_Service_GoodsInfoorderlist);
                            Log.d("print", "删除的数据2222：" + mSelf_Service_GoodsInfo);
                            Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));
                            Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));
                            Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfoorderlist));
                        }
                    }
                    mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                }
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(Restaurant_Nomal_MainAcitvity.this)) {
                    imm = (InputMethodManager) Restaurant_Nomal_MainAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        final EditText et_nums = (EditText) view_add_nums_notes.findViewById(R.id.et_nums);
        final EditText et_notes = (EditText) view_add_nums_notes.findViewById(R.id.et_notes);
//        if (!NoDoubleClickUtils.isSoftShowing(Restaurant_Nomal_MainAcitvity.this)) {
//            imm = (InputMethodManager) Restaurant_Nomal_MainAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        }
        et_nums.setSelectAllOnFocus(true);
        tv_goodsname.setText(mSelf_Service_GoodsInfo_dialog.get(position).getName());
        et_nums.setText(mSelf_Service_GoodsInfo_dialog.get(position).getNumber());
        et_notes.setText(mSelf_Service_GoodsInfo_dialog.get(position).getNotes());
        et_nums.setSelection(mSelf_Service_GoodsInfo_dialog.get(position).getNumber().length());
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nums_src = et_nums.getText().toString().trim();
                int nums = Integer.parseInt(nums_src);
                if (nums > 1) {
                    nums--;
                } else {
                    nums = 1;
                }
                et_nums.setText(nums + "");
                et_nums.setSelection(et_nums.getText().toString().trim().length());
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nums_src = et_nums.getText().toString().trim();
                int nums = Integer.parseInt(nums_src);
                nums++;
                et_nums.setText(nums + "");
                et_nums.setSelection(et_nums.getText().toString().trim().length());
            }
        });
        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(Restaurant_Nomal_MainAcitvity.this)) {
                    imm = (InputMethodManager) Restaurant_Nomal_MainAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelf_Service_GoodsInfo_dialog.get(position).setNumber(et_nums.getText().toString().trim());
                mSelf_Service_GoodsInfo_dialog.get(position).setNotes(et_notes.getText().toString().trim());
//                    if (mSelf_Service_GoodsInfo.size() > 0) {
//                        mSelf_Service_GoodsInfo=mSelf_Service_GoodsInfo_dialog;
//                    } else if (mSelf_Service_GoodsInfoorderlist.size() > 0) {
//                        mSelf_Service_GoodsInfoorderlist=mSelf_Service_GoodsInfo_dialog;
//                    }
                mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                setTotalPrice(mSelf_Service_GoodsInfo_dialog);
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo_dialog));
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo_dialog));
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo_dialog));
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(Restaurant_Nomal_MainAcitvity.this)) {
                    imm = (InputMethodManager) Restaurant_Nomal_MainAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mCommon_Notes_DialogAdapter=new Common_Notes_DialogAdapter(Restaurant_Nomal_MainAcitvity.this,mCommonNotesList);
        gv_common_notes.setAdapter(mCommon_Notes_DialogAdapter);
        mCommon_Notes_DialogAdapter.setonBtnClick(new Common_Notes_DialogAdapter.onBtnClick() {
            @Override
            public void onBtnClick(int position) {
                String et_notes_src=et_notes.getText().toString().trim();
                String notes_src=et_notes_src+mCommonNotesList.get(position).getNotes();
                et_notes.setText(notes_src);
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();

    }
    //获取常用备注信息
    private void getCommonNotesInfo(){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "常用备注：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                if(mCommonNotesList!=null){
                                    mCommonNotesList.clear();
                                }
                                JSONArray data=jo1.getJSONArray("data");
                                if(data!=null){
                                    mCommonNotesList=new ArrayList<Goods_Common_Notes>();
                                    for(int i=0;i<data.length();i++){
                                        JSONObject dataobj=data.getJSONObject(i);
                                        String notes=dataobj.getString("notes");
                                        String notes_id=dataobj.getString("id");
                                        Goods_Common_Notes goods_common_notes=new Goods_Common_Notes(notes_id,notes);
                                        mCommonNotesList.add(goods_common_notes);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"服务器数据异常",20);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"网络不给力",20);
                    }
                });

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        String order_id = tv_ordernums.getText().toString().trim();
        switch (v.getId()) {
            case R.id.keyboard_et_layout://实收
                but_mobilepayment.setButtonColor(Color.parseColor("#ffffff"));
                but_mobilepayment.setTextColor(Color.parseColor("#434343"));
                but_Cashbox.setButtonColor(Color.parseColor("#FF6501"));
                but_Cashbox.setTextColor(Color.parseColor("#ffffff"));
                keyboard_et_layout.setBackgroundResource(R.drawable.xuanzhong);
                keyboard_tv_layout.setBackgroundResource(R.drawable.banner);
                if (!et_inputscancode.getText().toString().equals("")) {
                    float f1 = Float.parseFloat(et_inputscancode.getText().toString());
                    if (f1 <= 0) {
                        et_keyoard.setText(0 + "");
                    } else {
                        et_keyoard.setText(f1 + "");
                    }
                } else {
                    et_keyoard.setText("");
                }
                isSelected = true;
                frequency++;
                break;
            case R.id.keyboard_tv_layout://应收
                but_mobilepayment.setButtonColor(Color.parseColor("#FF6501"));
                but_mobilepayment.setTextColor(Color.parseColor("#ffffff"));
                but_Cashbox.setButtonColor(Color.parseColor("#ffffff"));
                but_Cashbox.setTextColor(Color.parseColor("#434343"));
                keyboard_tv_layout.setBackgroundResource(R.drawable.xuanzhong);
                keyboard_et_layout.setBackgroundResource(R.drawable.banner);
                if (!tv_netreceipts.getText().toString().equals("")) {
                    float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
                    et_keyoard.setText((int) f1 + "");
                } else {
                    et_keyoard.setText("");
                }
                isSelected = false;
                frequency++;
                break;
            case R.id.tv_cancel://取消按钮
                setInitData();
                break;
            case R.id.but_Cashbox://点击现金支付按钮
                CashPay();
                break;
            case R.id.but_mobilepayment://点击移动支付按钮
                MobilePay();
                break;
            case R.id.but_cash_sure://现金支付确定按钮
                if(but_cash_sure.getText().toString().trim().equals("确定")){
                    /**
                     * 挂单本地化所以跟首次下单效果一样
                     */
//                    if (!TextUtils.isEmpty(order_id) && order_id.length() > 10) {
//                        GoodsOrderCommitCashPay();
//                    } else {
                        CommitCashPay();
//                    }
                }
                break;
            case R.id.btn_mobile_cell://移动支付取消按钮
                ll_jshuang.setVisibility(View.VISIBLE);
                Rl_yidong.setVisibility(View.GONE);
                if (mRes_Nomal_PalyOrderAdapter != null) {
                    mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                    mRes_Nomal_PalyOrderAdapter.setClick(true);
                }
                if(GetPatStatus_timer!=null){
                    GetPatStatus_timer.cancel();
                }
                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 7));
                break;
            case R.id.but_time://移动支付成功，关闭按钮
                mHandler.removeMessages(200);
                Rl_time.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.VISIBLE);
                if (mRes_Nomal_PalyOrderAdapter != null) {
                    mRes_Nomal_PalyOrderAdapter.setClick(true);
                }
                if (threeTime_timer != null) {
                    threeTime_timer.cancel();
                }
                setInitData();
                break;
            case R.id.layout_peoplenums://人数
                ResDialog.AddNotesDialog(Restaurant_Nomal_MainAcitvity.this, "输入人数", "请输入人数", tv_peoplenums);
                break;
            case R.id.layout_tablenums://桌号
                if(open_table){
                    changeTab(2);
                    setBtnBackgroud(btn_table, btn_dish, btn_order);
                }else {
                    ResDialog.AddNotesDialog(Restaurant_Nomal_MainAcitvity.this, "输入桌号", "请输入桌号", tv_tablenums);
                }
                break;
            case R.id.btn_dish://点菜
                changeTab(0);
                setBtnBackgroud(btn_dish, btn_table, btn_order);
                break;
            case R.id.btn_order://订单
                getOrderNums();
                changeTab(1);
                setBtnBackgroud(btn_order, btn_dish, btn_table);
                break;
            case R.id.btn_table://餐桌
                changeTab(2);
                setBtnBackgroud(btn_table, btn_dish, btn_order);
                break;
            case R.id.btn_lock_screen://锁屏
                ResDialog.LockDialog(Restaurant_Nomal_MainAcitvity.this);
                break;
            case R.id.layout_more://更多
                if (mSelf_Service_GoodsInfo.size() > 0) {
                    ResDialog.GotoDoOrderDialog(Restaurant_Nomal_MainAcitvity.this);
                } else {
                    startActivity(new Intent(Restaurant_Nomal_MainAcitvity.this, Commoditymanagement_Activity.class));
                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
                }
                break;
            case R.id.btn_do_package://打包
                if (!doPackage) {
                    doPackage = true;
                    btn_do_package.setBackgroundResource(R.drawable.btn_corner_orange);
                    tv_dopackage.setText("打包");
                } else {
                    doPackage = false;
                    btn_do_package.setBackgroundResource(R.drawable.btn_whilte_red);
                    tv_dopackage.setText("");
                }

                break;
            case R.id.btn_do_order://挂单
                if("挂单".equals(btn_do_order.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(order_id) && order_id.length() > 10) {
                        if (isEditOrder) {
                            edit_order(order_id, mSelf_Service_GoodsInfo);
                        } else {
                            edit_order(order_id, mSelf_Service_GoodsInfoorderlist);
                        }
                    } else {
                        do_order();
                    }
                }
                break;
            case R.id.layout_logout://登出
                Log.e("barcode", "Params：" + mSelf_Service_GoodsInfo.size());
                if (mSelf_Service_GoodsInfo.size() > 0) {
                    ResDialog.GotoDoOrderDialog(Restaurant_Nomal_MainAcitvity.this);
                } else {
                    Intent intent_out = new Intent(Restaurant_Nomal_MainAcitvity.this, Handover_activity.class);
                    intent_out.putExtra("logntype", 3);
                    startActivity(intent_out);
                }
                break;
            case R.id.btn_moneybox://钱箱
                OpenCashBox();
                break;
            case R.id.btn_printer://打印
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                break;
            case R.id.btn_refresh://同步
                mRes_GoodsFragment.GetGoods_info();
                changeTab(0);
                setBtnBackgroud(btn_dish, btn_table, btn_order);
                break;
            case R.id.btn_search://搜索
                tv_gone.setText("");
             ResDialog.AddNotesDialog(Restaurant_Nomal_MainAcitvity.this,"商品搜索","请输入商品名",tv_gone);
                ResDialog resDialog=new ResDialog();
                resDialog.setOnClickListener(new ResDialog.onClickSure() {
                    @Override
                    public void onClickSure() {
                        mRes_GoodsFragment.GoodsSearch(tv_gone.getText().toString().trim());
                    }
                });
                changeTab(0);
                setBtnBackgroud(btn_dish, btn_table, btn_order);
                break;
                //会员点击事件
            case R.id.but_huiyuan:
                Log.d("print","会员的点击事件");
                password="";
                dialog_member = new AlertDialog.Builder(Restaurant_Nomal_MainAcitvity.this).create();
                dialog_member.show();
                Window window = dialog_member.getWindow();
                dialog_member.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                View rootView = View.inflate(Restaurant_Nomal_MainAcitvity.this, R.layout.dialog_memberpaw, null);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                window.setContentView(rootView,params);
                dialog_member.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                final EditText editText = (EditText) window.findViewById(R.id.ed_paw);
                Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
                Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                final LinearLayout ll_title= (LinearLayout) window.findViewById(R.id.ll_title);
                ll_title.setVisibility(View.GONE);
                final Switch sw_memberpaw= (Switch) window.findViewById(R.id.sw_memberpaw);
                if (SharedUtil.getBoolean("showmemberpaw")){
                    sw_memberpaw.setChecked(true);
                    SharedUtil.putBoolean("showmemberpaw",true);
                }else {
                    sw_memberpaw.setChecked(SharedUtil.getBoolean("showmemberpaw"));
                }
                sw_memberpaw.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(boolean isChecked) {

                        Intent Intent1 = new Intent();
                        Intent1.setAction("com.yzx.image");
                        Intent1.putExtra("type","1");
                        Intent1.putExtra("memberpaw",isChecked);
                        sendBroadcast(Intent1);
                        sw_memberpaw.setChecked(isChecked);
                        SharedUtil.putBoolean("showmemberpaw",isChecked);
                    }
                });

                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);

                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_member.dismiss();
                    }
                });

                lv_member= (ListView) window.findViewById(R.id.lv_member);
                adapter_optimize=new Adapter_optimize(Restaurant_Nomal_MainAcitvity.this);
                adapter_optimize.OnClickListener(Restaurant_Nomal_MainAcitvity.this);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().length()==15){
                            getsearch_members(editable.toString());
                        }
                    }
                });


                but_dimdis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**获取输入法打开的状态**/
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        boolean isOpen = imm.isActive();
//                        //isOpen若返回true，则表示输入法打开，反之则关闭。
//                        if (isOpen) {
//                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        }
                        ll_title.setVisibility(View.VISIBLE);
                        getsearch_members(editText.getText().toString());

                    }


                });

                but_qixiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**获取输入法打开的状态**/
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        boolean isOpen = imm.isActive();
//                        if (isOpen) {
//                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        }
                        Intent intent=new Intent(Restaurant_Nomal_MainAcitvity.this,Member_Activity.class);
                        startActivity(intent);
                        dialog_member.dismiss();
                    }
                });
                break;
            case R.id.but_reduce:
                showRecharge();
                break;
        }
    }

    //打开钱箱
    private void OpenCashBox(){
        try {
            if (SysUtils.getSystemModel().equals("rk3288")){
                SysUtils.OpennewCashbox(Restaurant_Nomal_MainAcitvity.this);
            }else {
                SysUtils.OpenCashbox();
            }
        stringMap.put("seller_id", SharedUtil.getString("seller_id"));
        stringMap.put("work_name", SharedUtil.getString("name"));
        stringMap.put("seller_name", SharedUtil.getString("seller_name"));
        stringMap.put("operate_type", "钱箱操作");
        stringcontext = "打开了钱箱";
        stringMap.put("content", stringcontext);
        Gson gson = new Gson();
        String ing = gson.toJson(stringMap);
        getsensitivity(ing);
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }
    //异常操作的记录
    public void getsensitivity(String stringcontext) {
        OkGo.post(SysUtils.getSellerServiceUrl("log_insert"))
                .tag(this)
                .params("map", stringcontext)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "敏感操作的数据" + s);
                    }
                });
    }

    //获得挂单数挂单的个数
    private void getOrderNums() {

        Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
        long hangup = sqlite_entity.findMaxId("hangup");
        if (hangup > 0) {
            btn_ordersnums.setVisibility(View.VISIBLE);
            btn_ordersnums.setText(hangup+"");
        } else {
            btn_ordersnums.setVisibility(View.GONE);
        }

//        OkGo.post(SysUtils.getSellerServiceUrl("get_order_totalnum"))
//                .tag(this)
//                .cacheKey("cacheKey")
//                .cacheMode(CacheMode.DEFAULT)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("TAG", "挂单数是" + s);
//                        try {
//                            JSONObject jo = new JSONObject(s);
//                            JSONObject jo2 = jo.getJSONObject("response");
//                            String status = jo2.getString("status");
//                            if (status.equals("200")) {
//                                JSONObject jo3 = jo2.getJSONObject("data");
//                                String num = jo3.getString("num");
//                                if (Integer.valueOf(num) > 0) {
//                                    btn_ordersnums.setVisibility(View.VISIBLE);
//                                    btn_ordersnums.setText(num);
//                                } else {
//                                    btn_ordersnums.setVisibility(View.GONE);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
    }
    /**
     *餐桌状态
     * 0空闲，1开桌，2使用中，3预定，4禁桌
     */
    private void editTableStatus(String table_status, String id){
        OkGo.post(SysUtils.getTableServiceUrl("table_insert"))
                .tag(this)
                .params("id",id)
                .params("status",table_status)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     * 挂单接口
     */
    private String tablenums;
   private String peoplenums;
    private void do_order() {
        if (mSelf_Service_GoodsInfo != null) {
            if (mSelf_Service_GoodsInfo.size() < 1) {
                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "选择商品不能为空！", 20);
                return;
            }
            tablenums = tv_tablenums.getText().toString().trim();
            peoplenums = tv_peoplenums.getText().toString().trim();
            if (TextUtils.isEmpty(tablenums) || "- -".equals(tablenums)) {
                tablenums = "";
            }
            if (TextUtils.isEmpty(peoplenums) || "- -".equals(peoplenums)) {
                peoplenums = "0";
            }

            Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
            String order_id = DateUtils.getorder() + RandomUtils.getrandom();
            long time=Long.parseLong(DateUtils.getTime());
            sqlite_entity.addguadancanyin(mSelf_Service_GoodsInfo,order_id,doPackage+"",tablenums,table_id,time,peoplenums);

            if(mSelf_Service_GoodsInfo!=null&&mSelf_Service_GoodsInfo.size()>0){
                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this, mGpService, mService, tel, order_id, time, "", 1, tv_netreceipts.getText().toString().trim().replace("￥", "").trim(), "0.0", "0.0", 3, mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo, doPackage + "", peoplenums, tablenums, nums1);
            }else {
                PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this, mGpService, mService, tel, order_id, time, "", 1, tv_netreceipts.getText().toString().trim().replace("￥", "").trim(), "0.0", "0.0", 3, mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo, doPackage + "", peoplenums, tablenums, nums1);
            }
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "挂单成功！", 20);
            getOrderNums();

            setInitData();

            btn_do_order.setText("挂单");
            btn_do_order.setTextColor(Color.parseColor("#000000"));
            if(open_table){
                if(!TextUtils.isEmpty(table_id)){
                    editTableStatus("2",table_id);
                }
            }
//            ArrayList<Map<String, String>> mapArrayList = new ArrayList<>();
//            for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
//                String goods_id = mSelf_Service_GoodsInfo.get(i).getGoods_id();
//                String price = mSelf_Service_GoodsInfo.get(i).getPrice();
//                String nums = mSelf_Service_GoodsInfo.get(i).getNumber();
//                String name = mSelf_Service_GoodsInfo.get(i).getName();
//                String good_size = mSelf_Service_GoodsInfo.get(i).getSize();
//                String goods_notes = mSelf_Service_GoodsInfo.get(i).getNotes();
//                String product_id = mSelf_Service_GoodsInfo.get(i).getProduct_id();
//                Map<String, String> map = new HashMap<>();
//                if (!goods_id.equals("null")){
//                    map.put("goods_id", goods_id);
//                }
//                map.put("price", price);
//                map.put("nums", nums);
//                map.put("name", name);
//                map.put("goods_size", good_size);
//                if (!goods_id.equals("null")){
//                    map.put("goods_notes", goods_notes);
//                }else {
//                    map.put("goods_notes", "");
//                }
//                map.put("product_id", product_id);
//                mapArrayList.add(map);
//            }
//            Gson gson = new Gson();
//            String str = gson.toJson(mapArrayList);
//            final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
//             tablenums = tv_tablenums.getText().toString().trim();
//             peoplenums = tv_peoplenums.getText().toString().trim();
//            if (TextUtils.isEmpty(tablenums) || "- -".equals(tablenums)) {
//                tablenums = "";
//            }
//            if (TextUtils.isEmpty(peoplenums) || "- -".equals(peoplenums)) {
//                peoplenums = "0";
//            }
//
//            OkGo.post(SysUtils.getSellerServiceUrl("catering_order_add"))
//                    .tag(this)
//                    .cacheKey("cacheKey")
//                    .cacheMode(CacheMode.NO_CACHE)
//                    .params("total_amount", total_price_str)
////                    .params("total_fee", 1)
//                    .params("map", str)
//                    .params("desk_num", tablenums+"@"+table_id)
//                    .params("customer_num", peoplenums)
//                    .params("package", doPackage)//
//                    .params("mark_text", "")//
//                    .params("seller_token", SharedUtil.getString("seller_token"))
//                    .execute(new StringCallback() {
//
//                        @Override
//                        public void onBefore(BaseRequest request) {
//                            super.onBefore(request);
//                            Log.e("barcode", "Params：" + request.getUrl());
//                            Log.e("barcode", "Params：" + request.getParams());
//                            btn_do_order.setText("挂单中...");
//                            btn_do_order.setTextColor(Color.parseColor("#ff0000"));
//                        }
//
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                Log.e("barcode", "返回数据：" + jsonObject);
//                                JSONObject jo1 = jsonObject.getJSONObject("response");
//                                String status = jo1.getString("status");
//                                if (status.equals("200")) {
//
//                                    setInitData();
//
//                                    JSONObject data=jo1.getJSONObject("data");
//                                    String order_id=data.getString("order_id");
//                                    String time=data.getString("time");
//                                    long time_long=0;
//                                    if(!TextUtils.isEmpty(time)){
//                                        time_long=Long.parseLong(time);
//                                    }
//                                    getordenums();
////                                    if(mSelf_Service_GoodsInfo!=null&&mSelf_Service_GoodsInfo.size()>0){
////                                    if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
//
////                                    Log.e("打印的数据为", "onSuccess: "+mRes_GoodsOrderslist.size()+"       " +mSelf_Service_GoodsInfoorderlist.size());
//                                    if(mSelf_Service_GoodsInfo!=null&&mSelf_Service_GoodsInfo.size()>0){
//                                        if (SharedUtil.getfalseBoolean("list_print")) {
//                                            PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this, mGpService, mService, tel, nums1, time_long, "", 1, total_price_str, "0.0", "0.0", 3, mSelf_Service_GoodsInfo, doPackage + "", peoplenums, tablenums, nums1);
//                                        }
//                                    }else {
//                                        if (SharedUtil.getfalseBoolean("list_print")) {
//                                            PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this, mGpService, mService, tel, order_id, time_long, "", 1, total_price_str, "0.0", "0.0", 3, mSelf_Service_GoodsInfoorderlist, doPackage + "", peoplenums, tablenums, nums1);
//                                        }
//                                    }
//                                    StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "挂单成功！", 20);
//
//                                    getOrderNums();
//
//                                    btn_do_order.setText("挂单");
//                                    btn_do_order.setTextColor(Color.parseColor("#000000"));
//                                    if(open_table){
//                                        if(!TextUtils.isEmpty(table_id)){
//                                            editTableStatus("2",table_id);
//                                        }
//                                    }
//
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Log.d("barcode", "返回错误数据：" + e.toString());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Call call, Response response, Exception e) {
//                            super.onError(call, response, e);
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                                progressDialog = null;
//                            }
//                        }
//                    });
        }

    }

    String nums1 = "0";
    //获取订单数量
    public void getordenums(){
        OkGo.post(SysUtils.getSellerServiceUrl("getOrdersNum"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject res=jsonObject.getJSONObject("response");
                            String status=res.getString("status");
                            if (status.equals("200")){
                                JSONObject data=res.getJSONObject("data");
                                nums1 =data.getString("num");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //挂单编辑
    private void edit_order(final String order_id, final ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo) {
        if (mSelf_Service_GoodsInfo != null && mSelf_Service_GoodsInfoorderlist != null) {
            if (mSelf_Service_GoodsInfoorderlist.size() < 1 && mSelf_Service_GoodsInfo.size() < 1) {
                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "商品数据为空！", 20);
                return;
            }

            tablenums = tv_tablenums.getText().toString().trim();
            peoplenums = tv_peoplenums.getText().toString().trim();
            if (TextUtils.isEmpty(tablenums) || "- -".equals(tablenums)) {
                tablenums = "";
            }
            if (TextUtils.isEmpty(peoplenums) || "- -".equals(peoplenums)) {
                peoplenums = "0";
            }
            Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
            sqlite_entity.deleteguadan(order_id);
            long time=Long.parseLong(DateUtils.getTime());

            sqlite_entity.addguadancanyin(mSelf_Service_GoodsInfo,order_id,doPackage+"",tablenums,table_id,time,peoplenums);

//            long time_long=0;
//            if(!TextUtils.isEmpty("")){
//                time_long=Long.parseLong("");
//            }

            if(mSelf_Service_GoodsInfo!=null&&mSelf_Service_GoodsInfo.size()>0){
                getordenums();
                if (SharedUtil.getfalseBoolean("list_print")){
                    /**
                     * 时间的打印
                     */
                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, time, "", 1, tv_netreceipts.getText().toString().trim().replace("￥", "").trim(),"0.0","0.0",3,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                }
            }
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "挂单成功！", 20);
            if(open_table){
                if(!TextUtils.isEmpty(table_id)){
                    editTableStatus("2",table_id);
                }
            }
            btn_do_order.setText("挂单");
            btn_do_order.setTextColor(Color.parseColor("#000000"));
            setInitData();
            getOrderNums();
            changeTab(0);
            setBtnBackgroud(btn_dish, btn_table, btn_order);

//            String cur_goodsordernums=tv_tablenums.getText().toString().trim();
//            if (cur_goodsordernums== pre_goodsordernums) {
//                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "数据未发生改变，不能再次挂单！", 20);
//                return;
//            }
//            ArrayList<Map<String, String>> mapArrayList = new ArrayList<>();
//            for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
//                String goods_id = mSelf_Service_GoodsInfo.get(i).getGoods_id();
//                String price = mSelf_Service_GoodsInfo.get(i).getPrice();
//                String nums = mSelf_Service_GoodsInfo.get(i).getNumber();
//                String name = mSelf_Service_GoodsInfo.get(i).getName();
//                String good_size = mSelf_Service_GoodsInfo.get(i).getSize();
//                String goods_notes = mSelf_Service_GoodsInfo.get(i).getNotes();
//                String product_id = mSelf_Service_GoodsInfo.get(i).getProduct_id();
//                Map<String, String> map = new HashMap<>();
//                if (!goods_id.equals("null")){
//                    map.put("goods_id", goods_id);
//                }
//                map.put("price", price);
//                map.put("nums", nums);
//                map.put("name", name);
//                map.put("goods_size", good_size);
//                if (!goods_id.equals("null")){
//                    map.put("goods_notes", goods_notes);
//                }else {
//                    map.put("goods_notes", "");
//                }
//                map.put("product_id", product_id);
//                mapArrayList.add(map);
//            }
//            Gson gson = new Gson();
//            String str = gson.toJson(mapArrayList);
//            final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
//            tablenums = tv_tablenums.getText().toString().trim();
//            peoplenums = tv_peoplenums.getText().toString().trim();
//            if (TextUtils.isEmpty(tablenums) || "- -".equals(tablenums)) {
//                tablenums = "";
//            }
//            if (TextUtils.isEmpty(peoplenums) || "- -".equals(peoplenums)) {
//                peoplenums = "0";
//            }
//            OkGo.post(SysUtils.getSellerServiceUrl("catering_order_add"))
//                    .tag(this)
//                    .cacheKey("cacheKey")
//                    .cacheMode(CacheMode.NO_CACHE)
//                    .params("total_amount", total_price_str)
////                    .params("total_fee", 1)
//                    .params("map", str)
//                    .params("desk_num", tablenums+"@"+table_id)
//                    .params("order_id", order_id)
//                    .params("customer_num", peoplenums)
//                    .params("package", doPackage)//
//                    .params("mark_text", "")//
//                    .params("seller_token", SharedUtil.getString("seller_token"))
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onBefore(BaseRequest request) {
//                            super.onBefore(request);
//                            Log.e("barcode", "再次挂单：" + request.getUrl());
//                            btn_do_order.setText("挂单中...");
//                            btn_do_order.setTextColor(Color.parseColor("#ff0000"));
//                        }
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                Log.e("barcode", "返回数据：" + jsonObject);
//                                JSONObject jo1 = jsonObject.getJSONObject("response");
//                                String status = jo1.getString("status");
//                                if (status.equals("200")) {
//                                    JSONObject data=jo1.getJSONObject("data");
//                                    String order_id=data.getString("order_id");
//                                    String time=data.getString("time");
//                                    long time_long=0;
//                                    if(!TextUtils.isEmpty(time)){
//                                        time_long=Long.parseLong(time);
//                                    }
//                                    if(mSelf_Service_GoodsInfo!=null&&mSelf_Service_GoodsInfo.size()>0){
//                                        getordenums();
//                                        if (SharedUtil.getfalseBoolean("list_print")){
//                                            PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, time_long, "", 1, total_price_str,"0.0","0.0",3,mSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
//                                        }
//                                    }
//                                    StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "挂单成功！", 20);
//                                    if(open_table){
//                                        if(!TextUtils.isEmpty(table_id)){
//                                            editTableStatus("2",table_id);
//                                        }
//                                    }
//                                    btn_do_order.setText("挂单");
//                                    btn_do_order.setTextColor(Color.parseColor("#000000"));
//                                    setInitData();
//                                    getOrderNums();
//                                    changeTab(0);
//                                    setBtnBackgroud(btn_dish, btn_table, btn_order);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Log.d("barcode", "返回错误数据：" + e.toString());
//
//
//                            }
//                        }
//
//                        @Override
//                        public void onError(Call call, Response response, Exception e) {
//                            super.onError(call, response, e);
//                            if (progressDialog != null) {
//                                progressDialog.dismiss();
//                                progressDialog = null;
//                            }
//                        }
//                    });
        }

    }

    /**
     * 设置功能按钮的颜色
     *
     * @param btn_select
     * @param btn_isselect1
     * @param btn_isselect2
     */
    private void setBtnBackgroud(Button btn_select, Button btn_isselect1, Button btn_isselect2) {
        btn_select.setBackgroundResource(R.drawable.btn_whilte_orange_circle);
        btn_select.setTextColor(Color.parseColor("#ffffff"));
        btn_isselect1.setBackgroundResource(R.drawable.cc_di);
        btn_isselect1.setTextColor(Color.parseColor("#000000"));
        btn_isselect2.setBackgroundResource(R.drawable.cc_di);
        btn_isselect2.setTextColor(Color.parseColor("#000000"));
    }

    //初始化订单数据及计算数据
    private void setInitData() {
        keyboard_tv_layout.performClick();//自动触发应收栏被点击
        tv_netreceipts.setText("0.0");
        Showtotal("0.0");
        et_inputscancode.setText("0.0");
        et_keyoard.setText("");
        doPackage = false;
        btn_do_package.setBackgroundResource(R.drawable.btn_whilte_red);
        tv_dopackage.setText("");
        tv_peoplenums.setText("- -");
        tv_tablenums.setText("- -");
        tv_ordernums.setText("- -");
        tv_ordertime.setText("- - - -");
        if (mSelf_Service_GoodsInfo != null) {
            mSelf_Service_GoodsInfo.clear();
            Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Res_GoodsFragment.Action").putExtra("type",2));
            if (mRes_Nomal_PalyOrderAdapter != null) {
                mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                mRes_Nomal_PalyOrderAdapter.setClick(true);
            }
            setTotalPrice(mSelf_Service_GoodsInfo);
        }

        if (mSelf_Service_GoodsInfoorderlist != null) {
            mSelf_Service_GoodsInfoorderlist.clear();
            setTotalPrice(mSelf_Service_GoodsInfoorderlist);
        }

        if (newmSelf_Service_GoodsInfo!=null){
            newmSelf_Service_GoodsInfo.clear();
        }

        if (mSelf_Service_GoodsInfo!=null){
            mSelf_Service_GoodsInfo.clear();
        }

        if (mSelf_Service_Goods!=null){
            mSelf_Service_Goods.clear();
        }
        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 3));
        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action").putExtra("type",2));
    }

    //直接现金支付确认
    private void CommitCashPay() {
        ArrayList<Map<String, String>> mapList = new ArrayList<>();
        if (mapList.size() > 0) {
            mapList.clear();
        }
        if (mSelf_Service_GoodsInfo == null) {
            mSelf_Service_GoodsInfo = new ArrayList<>();
        }
        if (mSelf_Service_GoodsInfo.size() < 1) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "无商品收银");
            map.put("number", "1");
            map.put("price", tv_netreceipts.getText().toString() + "");
            map.put("amount", tv_netreceipts.getText().toString() + "");
            mapList.add(map);
        } else {
            for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                String goods_id = mSelf_Service_GoodsInfo.get(i).getGoods_id();
                String name = mSelf_Service_GoodsInfo.get(i).getName();
                String price = mSelf_Service_GoodsInfo.get(i).getPrice();
                String cost = mSelf_Service_GoodsInfo.get(i).getCost();
                String nums = mSelf_Service_GoodsInfo.get(i).getNumber();
                Map<String, String> map = new HashMap<>();
                if (!goods_id.equals("null")){
                    map.put("goods_id", goods_id);
                }
                map.put("name", name);
                map.put("number", nums);
                map.put("cost", cost);
                map.put("price", price);
                mapList.add(map);
            }
        }
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        OkGo.post(SysUtils.getSellerServiceUrl("cashPay"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("member_id", "90577")
                .params("commodity", str)
                .params("operator_id", SharedUtil.getString("operator_id"))
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("amount_receivable", tv_netreceipts.getText().toString())
                .params("receive_amount", et_inputscancode.getText().toString())
                .params("add_change", tv_Surplus.getText().toString())
                .params("mark_text", "")
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("pay", "现金支付URL: " + request.getUrl());
                        Log.e("pay", "现金支付Params: " + request.getParams());
                        but_cash_sure.setText("支付中...");
                        but_cash_sure.setOnClickListener(null);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            Log.e("pay", "现金支付: " + jo);
                            JSONObject jo1 = jo.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
                                sqlite_entity.deleteguadan(tv_ordernums.getText().toString().trim());
                                /**
                                 * 会员充值
                                 */
                                if (mSelf_Service_GoodsInfo.size()>0){
                                if(mSelf_Service_GoodsInfo.get(0).getName().equals("会员充值")){
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("member_id", pbmember_id);
                                    map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                            Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");
                                    Gson gson = new Gson();
                                    String s1 = gson.toJson(map);
                                    UPmoney(s1);
                                }
                                }
                                but_cash_sure.setText("确定");
                                but_cash_sure.setOnClickListener(Restaurant_Nomal_MainAcitvity.this);
                                ll_jshuang.setVisibility(View.VISIBLE);
                                Rl_xianjin.setVisibility(View.GONE);

                                JSONObject data = jo1.getJSONObject("data");
                                String order_id = data.getString("order_id");
                                String payed_time = data.getString("payed_time");
                                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "收款成功！", 20);
                                if(open_table){
                                    if(!TextUtils.isEmpty(table_id)){
                                        editTableStatus("0",table_id);
                                    }
                                }
                                String pay_money=tv_xianjin_netreceipt.getText().toString().trim();
                                String really_money=tv_amount.getText().toString().trim();
                                String change_money=tv_change.getText().toString().trim();
                                String peoplenums=tv_peoplenums.getText().toString().trim();
                                String tablenums=tv_tablenums.getText().toString().trim();
                                getordenums();
                                if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
                                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, 0, payed_time, 0, pay_money,really_money,change_money,2,mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                }else {
                                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, 0, payed_time, 0, pay_money,really_money,change_money,2,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                }

                                setInitData();
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));
                                getOrderNums();
                                changeTab(0);
                                setBtnBackgroud(btn_dish, btn_table, btn_order);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
    //挂单现金支付确认
    private void GoodsOrderCommitCashPay() {
        ArrayList<Map<String, String>> mapList = new ArrayList<>();
        if (mapList.size() > 0) {
            mapList.clear();
        }
        Map<String, String> map = new HashMap<>();
        map.put("order_id", tv_ordernums.getText().toString().trim());
        map.put("price", tv_netreceipts.getText().toString().trim());
        mapList.add(map);
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        OkGo.post(SysUtils.getSellerServiceAPPUrl("cashPay"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("map", str)
                .params("amount_receivable", tv_netreceipts.getText().toString())
                .params("receive_amount", et_inputscancode.getText().toString())
                .params("add_change", tv_Surplus.getText().toString())
                .params("mark_text", "")
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        but_cash_sure.setText("支付中...");
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jo = new JSONObject(s);
                            Log.e("pay", "现金支付: " + jo);
                            JSONObject jo1 = jo.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
                                sqlite_entity.deleteguadan(tv_ordernums.getText().toString().trim());
                                but_cash_sure.setText("确定");
                                ll_jshuang.setVisibility(View.VISIBLE);
                                Rl_xianjin.setVisibility(View.GONE);
                                String peoplenums=tv_peoplenums.getText().toString().trim();
                                String tablenums=tv_tablenums.getText().toString().trim();
                                getordenums();
                                if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
//                                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,tv_ordernums.getText().toString().trim(), 0,tv_ordertime.getText().toString().trim(), 0, tv_netreceipts.getText().toString(),et_inputscancode.getText().toString(),
//                                            tv_Surplus.getText().toString(),2,mSelf_Service_GoodsInfoorderlist,doPackage+"",peoplenums,tablenums);
                                    PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,tv_ordernums.getText().toString().trim(),0,tv_ordertime.getText().toString().trim(),0,tv_netreceipts.getText().toString(),et_inputscancode.getText().toString(),
                                            tv_Surplus.getText().toString(),2,mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1,"");
                                }else {
//                                    PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,tv_ordernums.getText().toString().trim(), 0, tv_ordertime.getText().toString().trim(), 0, tv_netreceipts.getText().toString(),
//                                            et_inputscancode.getText().toString(),tv_Surplus.getText().toString(),2,mSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums);
                                    PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,tv_ordernums.getText().toString().trim(),0,tv_ordertime.getText().toString().trim(),0,tv_netreceipts.getText().toString(),et_inputscancode.getText().toString(),
                                            tv_Surplus.getText().toString(),2,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1,"");
                                }
                                setInitData();
                                changeTab(0);
                                setBtnBackgroud(btn_dish, btn_table, btn_order);
                                StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "收款成功！", 20);
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));
                                if(open_table){
                                    if(!TextUtils.isEmpty(table_id)){
                                        editTableStatus("0",table_id);
                                    }
                                }
                                getOrderNums();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            //重新初始化
                            setInitData();
                        }
                    }
                });

    }

    //现金支付页面
    private void CashPay() {
        String tv_netreceipts_str = tv_netreceipts.getText().toString().trim();//应收
        String et_inputscancode_str = et_inputscancode.getText().toString().trim();//实收
        if (TextUtils.isEmpty(et_inputscancode_str)) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "实收金额不能为空！", 20);
            return;
        }
        if (TextUtils.isEmpty(tv_netreceipts_str)) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "应收金额不能为空！", 20);
            return;
        }
        double tv_netreceipts_double = Double.parseDouble(tv_netreceipts_str);
        double et_inputscancode_double = Double.parseDouble(et_inputscancode_str);
        if (tv_netreceipts_double == 0) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "应收金额必须大于0元！", 20);
            return;
        }
        if (tv_netreceipts_double > et_inputscancode_double) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "实收金额小于应收金额，请重新检查之后再确认支付！", 20);
            return;
        }
        ll_jshuang.setVisibility(View.GONE);
        Rl_xianjin.setVisibility(View.VISIBLE);
        tv_xianjin_netreceipt.setText(tv_netreceipts_str);
        tv_amount.setText(et_inputscancode_str);
        tv_change.setText(TlossUtils.sub(et_inputscancode_double, tv_netreceipts_double) + "");

        if (mRes_Nomal_PalyOrderAdapter != null) {
            mRes_Nomal_PalyOrderAdapter.setClick(false);
        }
        OpenCashBox();
        Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 4).putExtra("RealedPrice", et_inputscancode_str).putExtra("ChangeCash", tv_change.getText().toString().trim()).putExtra("paymoney", tv_netreceipts_str));
    }

    //移动支付页面
    private void MobilePay() {
        keyboard_tv_layout.performClick();
        String tv_netreceipts_str = tv_netreceipts.getText().toString().trim();//应收
        if (TextUtils.isEmpty(tv_netreceipts_str)) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "应收金额不能为空！", 20);
            return;
        }
        double tv_netreceipts_double = Double.parseDouble(tv_netreceipts_str);
        if (tv_netreceipts_double == 0) {
            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this, "应收金额不能为0元！", 20);
            return;
        }
//        if (SysUtils.isWifiConnected(Restaurant_Nomal_MainAcitvity.this)) {
        if (isnetworknew) {
//        if (SysUtils.isNetworkAvailable(Restaurant_Nomal_MainAcitvity.this)) {
//        if (SysUtils.isNetworkOnline()) {
            ll_jshuang.setVisibility(View.GONE);
            Rl_yidong.setVisibility(View.VISIBLE);
            tv_yidong.setText(tv_netreceipts.getText().toString());
            if (mRes_Nomal_PalyOrderAdapter != null) {
                mRes_Nomal_PalyOrderAdapter.setClick(false);
            }
            String order_id = tv_ordernums.getText().toString().trim();
            /**
             * 挂单本地化所以跟首次下单效果一样
             */
//            if (!TextUtils.isEmpty(order_id) && order_id.length() > 10) {
//                GoodsOrderGoToPay();
//            } else {
                Log.e("print","打印移动支付的数据");
                GoToPay();
//            }
        }
    }

    /**
     * 直接买单生成支付二维码
     */
    private String orders_id;
    private Timer GetPatStatus_timer;//获取订单状态的定时器

    private void GoToPay() {
        isOrder=false;
        orders_id="";
        ArrayList<Map<String, String>> mapList = new ArrayList<>();
        for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
            String goods_id = mSelf_Service_GoodsInfo.get(i).getGoods_id();
            String name = mSelf_Service_GoodsInfo.get(i).getName();
            String price = mSelf_Service_GoodsInfo.get(i).getPrice();
            String cost = mSelf_Service_GoodsInfo.get(i).getCost();
            String nums = mSelf_Service_GoodsInfo.get(i).getNumber();
            Map<String, String> map = new HashMap<>();
            if (name.equals("会员充值")) {
                map.put("name", name);
                map.put("number", "1");
                map.put("price", price + "");
                map.put("amount", price + "");
            } else {

                if (!goods_id.equals("null")){
                    map.put("goods_id", goods_id);
                }
            map.put("name", name);
            map.put("number", nums);
            map.put("cost", cost);
            map.put("price", price);
            map.put("amount", Float.parseFloat(price) * Float.parseFloat(nums) + "");
            map.put("orders_status", "1");
            map.put("pay_status", "0");
            }
            mapList.add(map);
        }
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        Log.e("print","打印移动支付的上传数据"+str);
        final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
        double total_price = Double.parseDouble(total_price_str);

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
//                        Log.e("barcode", "Params：" + request.getParams().toString());
//                        Log.e("barcode", "二维码url：" + request.getUrl());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data = jo1.getJSONObject("data");
                                orders_id = data.getString("order_id");
                                String url = data.getString("url");
                                createPatCode(url);
                                tv_yidong.setText(total_price_str);
                                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, false);
                                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 6).putExtra("url", url));
                                //定时获取支付状态
                                GetPatStatus_timer = new Timer();
                                TimerTask GetPatStatus_TimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        GetPayStatus(orders_id);
                                    }
                                };
                                GetPatStatus_timer.schedule(GetPatStatus_TimerTask, 1000, 3000);
                                //支付进度条
                                processTime_timer = new Timer();
                                processTime_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(201);
                                    }
                                }, 1000, 1000);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("barcode", "返回错误数据：" + e.toString());

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                });
    }

    //挂单买单生成支付二维码
    private void GoodsOrderGoToPay() {
        isOrder=true;
        ArrayList<Map<String, String>> mapList = new ArrayList<>();
        if (mapList.size() > 0) {
            mapList.clear();
        }
        Map<String, String> map = new HashMap<>();
        map.put("order_id", tv_ordernums.getText().toString().trim());
        map.put("price", tv_netreceipts.getText().toString().trim());
        mapList.add(map);
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
        double total_price = Double.parseDouble(total_price_str);

        OkGo.post(SysUtils.getSellerServiceAPPUrl("common_pay"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("total_fee", (int)(total_price * 100))
//                .params("total_fee", 1)
                .params("map", str)
                .params("auth_code", "code")
                .params("pay_type", "wxpayjsapi")
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "Params：" + request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data = jo1.getJSONObject("data");
                                orders_id = data.getString("order_id");
                                String url = data.getString("url");
                                createPatCode(url);
                                tv_yidong.setText(total_price_str);

                                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Restaurant_Nomal_MainAcitvity.this, mSelf_Service_GoodsInfo, false);
                                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 6).putExtra("url", url));
                                //定时获取支付状态
                                GetPatStatus_timer = new Timer();
                                TimerTask GetPatStatus_TimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        GetPayStatus(orders_id);
                                    }
                                };
                                GetPatStatus_timer.schedule(GetPatStatus_TimerTask, 1000, 3000);
                                //支付进度条
                                processTime_timer = new Timer();
                                processTime_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(201);
                                    }
                                }, 1000, 1000);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("barcode", "返回错误数据：" + e.toString());

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                });
    }

    //获取支付状态
    public void GetPayStatus(String order_id) {
        OkGo.getInstance().cancelTag("payment");
        OkGo.post(SysUtils.getSellerServiceUrl("order_status"))
                .tag("payment")
                .params("order_id", order_id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("获取支付状态URL：", "= " + request.getUrl());
                        Log.e("获取支付状态Params：", "= " + request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            Log.e("获取到数据：", "= " + jsonObject);
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                Sqlite_Entity sqlite_entity=new Sqlite_Entity(Restaurant_Nomal_MainAcitvity.this);
                                sqlite_entity.deleteguadan(tv_ordernums.getText().toString().trim());
                                if (mSelf_Service_GoodsInfo.size()>0){
                                if(mSelf_Service_GoodsInfo.get(0).getName().equals("会员充值")){
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("member_id", pbmember_id);
                                    map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                            Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");
                                    Gson gson = new Gson();
                                    String s1 = gson.toJson(map);
                                    UPmoney(s1);
                                    }
                                }

                                GetPatStatus_timer.cancel();
                                processTime_timer.cancel();

                                JSONObject data = jo1.getJSONObject("data");
                                String order_id = data.getString("order_id");
                                String time = data.getString("time");
                                long time_long = Long.parseLong(time);
                                String paymoney = tv_yidong.getText().toString().trim();
                                String peoplenums=tv_peoplenums.getText().toString().trim();
                                String tablenums=tv_tablenums.getText().toString().trim();

                                Text2Speech.speech(Restaurant_Nomal_MainAcitvity.this, "收款成功" + paymoney + "元", false);
                                if(open_table){
                                    Log.e("餐桌table_id：", "= " + table_id);
                                    if(!TextUtils.isEmpty(table_id)){
                                        editTableStatus("0",table_id);
                                    }
                                }
                                getordenums();
                                //挂单买单只打印小票，直接买单的打印小票和标签
                                if(isOrder){
                                    if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
                                        PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1,"");
                                    }else {
                                        PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1,"");
                                    }
                                }else {
//                                    if (mSelf_Service_GoodsInfoorderlist.size()>0){
//                                        PrintUtil.receiptPrint(Restaurant_Nomal_MainAcitvity.this,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfoorderlist,doPackage+"",peoplenums,tablenums,nums1,"");
//                                    }
                                    if(mSelf_Service_GoodsInfoorderlist!=null&&mSelf_Service_GoodsInfoorderlist.size()>0){
                                        PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfoorderlist,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                    }else {
                                        PrintUtil.doPrint(Restaurant_Nomal_MainAcitvity.this,mGpService,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfo,newmSelf_Service_GoodsInfo,doPackage+"",peoplenums,tablenums,nums1);
                                    }
                                }
                                Restaurant_Nomal_MainAcitvity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));
                                tv_danhao.setText(order_id);
                                tv_day.setText(DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000));
                                String pay_money = tv_netreceipts.getText().toString().trim().replace("￥", "");
                                tv_payment.setText(pay_money);
                                Rl_yidong.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);//收款成功页面
                                getOrderNums();
                                changeTab(0);
                                setBtnBackgroud(btn_dish, btn_table, btn_order);
                                if (mRes_Nomal_PalyOrderAdapter != null) {
                                    mRes_Nomal_PalyOrderAdapter.setClick(true);
                                }
//                                3秒关闭
                                threeTime_timer = new Timer();
                                threeTime_timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(200);
                                    }
                                }, 1000, 1000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //获取商户电话
    private String tel;

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
                                SharedUtil.putString("seller_tel",tel);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 生成二维码
     *
     * @param code_url
     */
    private void createPatCode(final String code_url) {
        layout_process_bar.setVisibility(View.GONE);
        im_code.setVisibility(View.VISIBLE);

        final String filePath = QRCodeUtil.getFileRoot(Restaurant_Nomal_MainAcitvity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();


        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(code_url, 800, 800, null, filePath);

                if (success) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                            Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(filePath));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                im_code.setBackground(drawable);
                            } else {
                                im_code.setImageBitmap(BitmapFactory.decodeFile(filePath));
                            }
//                        }
//                    });
                }
            }
        };
        Restaurant_Nomal_MainAcitvity.this.runOnUiThread(runnable);


    }

    //选择蓝牙打印机回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Intent serverIntent = new Intent(Restaurant_Nomal_MainAcitvity.this, DeviceListActivity.class);      //��������һ����Ļ
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

    //扫码抢扫码事件处理
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//            if(event.getKeyCode()!=KeyEvent.KEYCODE_BACK){
        scanGunKeyEventHelper.analysisKeyEvent(event);
//            }
        return true;
    }
    //扫码枪数据返回
    @Override
    public void onScanSuccess(String barcode) {
        if (Rl_yidong.getVisibility()!= View.VISIBLE) {

            StringUtils.showToast(Restaurant_Nomal_MainAcitvity.this,"请先点击移动支付按钮！",20);
        }else {
            MobilePayment(barcode);
        }


    }
    //扫码枪移动支付
    public void MobilePayment(final String auth_code) {
        final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
        double total_price = Double.parseDouble(total_price_str);
        OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("total_fee",(new Double(total_price*100)).intValue()+"")
                .params("order_id", orders_id)
                .params("pay_type", Connectors.Pay_TYPE)//
                .params("auth_code", auth_code)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("扫码URL：", "= " + request.getUrl());
                        Log.e("扫码Params：", "= " + request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.d("print", "扫码枪支付：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                            /**
                              * 会员充值
                              */
                            if (mSelf_Service_GoodsInfo.size()>0){
                                if(mSelf_Service_GoodsInfo.get(0).getName().equals("会员充值")){
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("member_id", pbmember_id);
                                    map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                            Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");
                                    Gson gson = new Gson();
                                    String s1 = gson.toJson(map);
                                    UPmoney(s1);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}

