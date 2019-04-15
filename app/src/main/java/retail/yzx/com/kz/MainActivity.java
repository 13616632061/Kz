package retail.yzx.com.kz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;
import com.recker.flybanner.FlyBanner;
import com.rey.material.widget.ProgressView;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Connector.Connectors;
import Entty.Cash_entty;
import Entty.Commodity;
import Entty.Integral_Entty;
import Entty.ListEntty;
import Entty.Maidan;
import Entty.Member_entty;
import Entty.Mobile_pay;
import Entty.ShuliangEntty;
import Entty.Specification_Entty;
import Fragments.Cook_fragment;
import Fragments.Order_fragment;
import Fragments.Quick_fragment;
import Utils.Bitmap_Utils;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.MyXMLReader;
import Utils.NetUtil;
import Utils.PrintUtil;
import Utils.PrintWired;
import Utils.QRCode;
import Utils.RandomUtils;
import Utils.ScanGunKeyEventHelper;
import Utils.ServiceUtils;
import Utils.SetEditTextInput;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import Utils.UsbPrinter;
import Utils.UsbPrinterUtil;
import adapters.Adapter_Fuzzy;
import adapters.Adapter_integral;
import adapters.Adapter_optimize;
import adapters.Adapter_specification;
import adapters.listadapterzhu;
import android_serialport_api.SerialParam;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortOperaion;
import base.BaseActivity;
import base.BaseFragment;
import butterknife.BindView;
import comon.error.Common;
import hdx.HdxUtil;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Response;
import printUtils.debug;
import shujudb.SqliteHelper;
import shujudb.Sqlite_Entity;
import widget.ShapeLoadingDialog;
import widget.Switch;

import static Utils.Bitmap_Utils.addBitmap;
import static retail.yzx.com.kz.NetWorkService.isNetBad;


public class MainActivity extends BaseActivity implements ScanGunKeyEventHelper.OnScanSuccessListener, listadapterzhu.Onremove, Adapter_Fuzzy.SetOnclick, Adapter_optimize.OnClickListener, View.OnClickListener {


    protected DemoApplication mApplication;
    protected SerialPort mSerialPort=null;
    protected OutputStream mOutputStream=null;
    private InputStream mInputStream=null;
    private Thread mReadThread;



    public List<Commodity> cancelled;
    public List<ShuliangEntty> cancelledenyyt;

    //移动支付的定时器
    Handler handlernew;
    Runnable runnablenew;


    //现金支付
    public boolean iscash=false;

//    快捷栏副屏的显示

    public int kuaijie = 0;

    public List<Map<String, String>> mapList;

    //加载动画
//    public RelativeLayout Rl_yidong;
    public ProgressView pressed;
    public double jindu = 0;

    //商品的实体类
    public List<Commodity> commodities;
    public List<Commodity> Datas;
    public Commodity commoditys;
    //选中itme选中判断
    public List<ListEntty> itmeChecked;

    public ScanGunKeyEventHelper scanGunKeyEventHelper;

    public LinearLayout ll_listitme;

    public TextView tv_jiage, ed_shuliang, tv_xiaoji, tv_lirun;


    //    删除itme的第几条
    public int k = -1;
    public ImageView im_add, im_reductionof;
    public List<ListEntty> Entty;

    //共计商品和合计
    public double j = 0;
    public TextView tv_Totalmerchandise, tv_Total;
    public TextView tv_Totalamount, tv_11, tv_Symbol;

    //    副屏显示的标志位
    public boolean fuxiang = false;
    //    计算的总和
    public EditText et_keyoard, et_inputscancode;
    public final static Double MIXMONEY = 99999.00;//付款最大金额

    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;
    public int number = 0;

    //数量的
    public List<ShuliangEntty> entty = new ArrayList<>();
    public ShuliangEntty shuliangEntty;

    //    买单的实体类
    public Maidan maidan;
    public List<Maidan> listmaidan;
    public RelativeLayout keyboard_tv_layout, keyboard_et_layout;

    public ImageView im_code;
    //判断网络
    public TextView tv_internet;

    //    快捷栏的实体类
    List<Maidan> Kmaidan;
    public AlertDialog.Builder builder;

    //    移动收银的数据
    public TextView tv_yidong;
    //    移动支付实体类
    public Mobile_pay mobile_pay;

    //    现金收银的数据
    public TextView tv_xianjin_netreceipt, tv_amount, tv_change;
    public TextView tv_payment, tv_danhao, tv_day, tv_modes;
    //    现金收银的实体类
    public Cash_entty cash_entty=new Cash_entty();

    //用来检测钱箱的打开关闭
    public boolean Cashbox_switch=true;



    public Button but_xianjin;


    //支付状态
//    public TextView tv_status;

    //输入框选中的标志位
    public boolean isSelected = true;
    public EditText tv_Surplus;//找零
    public TextView tv_netreceipts;//应收
    public int frequency = 0;
    public TextView tv_Time;
    //判断是否有网
//    public boolean isnetwork;
    //判断是批发还是零售
    public boolean iswholesale=true;
    //用来实时更新找零的数据
    public Handler handler;
    //    public Handler handler2;
    //更多
    public TextView but_More;
    //中间控制栏
    public RadioButton but_Quick, but_Cook, rb_1, rb_2;

    //无网收银确定按钮
    public Button but_internet;

//    登出按钮

    public Button but_exit, but_cut, but_fruit,but_share_tools; //控制现金支付的按钮
    public FButton but_Cashbox, but_mobilepayment;
    public Button but_goback;

    //取消按钮
    public FButton tv_cancel;


    //手动输入条码
    public Button but_CodeInput;


    public Boolean version = false;

    //listView的id
    public ListView lv;
    public listadapterzhu adapterzhu;
    public Button but_Guadan, but_Takeasingle, but_Check, but_Delete,  but_credit,but_member,but_change_money;
    public ImageView but_Print,but_cc_Cashbox;
    public ListView lv_member;

    @BindView(R.id.but_take_out_food)
    Button but_take_out_food;

    //兑奖
    public Button but_redeem;
    public ImageView but_Lockscreen;
    public List<String> string;


    public Fragment mcontext;

    //    快捷栏界面计算机界面
    public RelativeLayout rl_quick, rl_jishuang;

    //  挂单赊账的点击按钮的
    public RadioButton rb_order, rb_credit;

    //    取单的list view
    public FrameLayout fl_single;
    //    取单界面
    public LinearLayout ll_check;
    //    取单退出按钮
    public ImageView im_tuichu;
    //挂单个数
    public Button but_cc_quick, but_cc_credit;
    //作废
    public TextView tv_jiegua, tv_cancellation;

    public TextView tv_jinjia, tv_lirunli, tv_store;

    public TextView tv_name;

    //定时时间
    public Handler handler1;

    //    扫描枪的扫描的类别
    public int type = 0;

    public BaseFragment quick_fragment ;
    public BaseFragment cook_fragment ;
    private List<Fragment> fragmentList = new ArrayList<>();

    public FrameLayout Rl_quick;

    public FlyBanner im_picture;
    public List<String> imageurls;

    public ShapeLoadingDialog loadingdialog;


    public int operational = 0;

    public RelativeLayout Rl_layout;

    public LinearLayout ll_time;
    private String text;

    public Adapter_Fuzzy adapter_fuzzy = new Adapter_Fuzzy(MainActivity.this);
    public List<Commodity> list_fuzzy = new ArrayList<Commodity>();

    public Dialog dialogfuzzy;
    public Button but_refresh;


    public static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public String versions;
    public int versionCode;

    //获取商户的电话号码
    public String tel;

    public Map<String, String> stringMap = new HashMap<>();
    public String stringcontext = "";

    //判断支付是否成功
    public static boolean issucceed = false;


    public Member_entty member_entty;
    public List<Member_entty> member_entties;
    public Adapter_optimize adapter_optimize;

    //记录会员支付的密码
    public String password="";

    public android.support.v7.app.AlertDialog dialog_member;


    //设置支付是否为会员支付
    public String pay_type="0";

    //会员id
    private String pbmember_id="";
    //会员积分
    private String Score="";
    //会员消耗的积分
    private String pay_score="";
    //判断是否是抵扣
    private String is_score_pay="no";
    //判断是否免密
    private String is_require_pass="no";

    //会员积分兑换
    private List<Integral_Entty> integral_list;
    private ListView lv_exchange;
    private Adapter_integral adapter_integral;
    public TextView tv_integral,tv_balance;

    //会员充值
    private List<Specification_Entty> specification_list;
    private Adapter_specification adapter_specification;
    private ListView lv_recharge;
    //记录点击的是那个会员
    private int specification_unms;
    //记录充值的规格
    private String recharge_id;

    private TextView tv_Staff;

    private boolean isgetseek=false;

    //判断是取哪个会员价
    private String Member_type="0";

    /**
     * 这有改动
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, getResources().getString(R.string.bluetooth_open_successfully), Toast.LENGTH_LONG).show();
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
                }
                break;
        }
    }

    public RelativeLayout Rl_xianjin,Rl_time,Rl_yidong;
    public LinearLayout ll_jshuang;
    public RelativeLayout layout_go_pay;
    public TextView tv_weight;
    public TextView tv_danwei1;
    public Button but_reduce,but_Discount,but_Remove;
    public boolean reduce=false,discount=false;
    public float _Memberdiscount=10;

    public String _reduce="0";
    public String _discount="0";
    public String _Total="0";


    public String discount_type="";
    public String num="0";
    public String discount_goods_id="";

    private SerialPortOperaion mSerialPortOperaion=null;
    public List<Integer> listInt=new ArrayList<>();//
    public TextView tv_discount,tv_phone,tv_birthday,tv_time,tv_remark,tv_integral1,tv_balance1,tv_name1,tv_grade;
    public TextView ed_Discount;
    public Button but_close,but_self_discount,but_discount,but_balance_paid,but_recharge,but_dimdis,but_eductible;

//    public static MainActivity instance;


    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    //    tv_netreceipts
    //副屏显示总金额
    public void Showtotal(String str){
        listInt=SetAdats(str);
        if (listInt.size()==15){
            mSerialPortOperaion.WriteData(listInt.get(0),listInt.get(1),listInt.get(2),listInt.get(3),listInt.get(4),
                    listInt.get(5),listInt.get(6),listInt.get(7),listInt.get(8),listInt.get(9),listInt.get(10),
                    listInt.get(11),listInt.get(12),listInt.get(13),listInt.get(14));
        }
    }

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


    //上传无网数据
    private void upnotnet(){
        int number = 0, number2 = 0;
        Cursor c = null, c2 = null;
        if (c == null && c2 == null) {
            sqLiteDatabase = sqliteHelper.getReadableDatabase();
            c = sqLiteDatabase.rawQuery("select * from ProOut", null);
            c2 = sqLiteDatabase.rawQuery("select * from goodsSell", null);
        }
        number = c.getCount();
        number2 = c2.getCount();
        if (number != 0 && number2 != 0) {
            List<Map<String, Object>> List = new ArrayList<>();
            while (c.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                String cash_id = c.getString(c.getColumnIndex("cash_id"));
                map.put("cash_id", cash_id);
                c.getString(c.getColumnIndex("cash_time"));
                map.put("cash_time", c.getString(c.getColumnIndex("cash_time")));
                c.getString(c.getColumnIndex("seller_id"));
                map.put("seller_id", c.getString(c.getColumnIndex("seller_id")));
                c.getString(c.getColumnIndex("amount_receivable"));
                map.put("amount_receivable", c.getString(c.getColumnIndex("amount_receivable")));
                c.getString(c.getColumnIndex("receive_amount"));
                map.put("receive_amount", c.getString(c.getColumnIndex("receive_amount")));
                c.getString(c.getColumnIndex("add_change"));
                map.put("add_change", c.getString(c.getColumnIndex("add_change")));
                c.getString(c.getColumnIndex("payment"));
                map.put("payment", c.getString(c.getColumnIndex("payment")));
                Cursor cursor = sqLiteDatabase.rawQuery("select * from goodsSell where cash_id=?", new String[]{cash_id});
                List<Map<String, String>> listmap = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Map<String, String> map2 = new HashMap<>();
                    cursor.getString(cursor.getColumnIndex("goods_id"));
                    map2.put("goods_id", cursor.getString(cursor.getColumnIndex("goods_id")));
                    cursor.getString(cursor.getColumnIndex("name"));
                    map2.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    cursor.getString(cursor.getColumnIndex("number"));
                    map2.put("nums", cursor.getString(cursor.getColumnIndex("number")));
                    cursor.getString(cursor.getColumnIndex("py"));
                    map2.put("py", cursor.getString(cursor.getColumnIndex("py")));
                    cursor.getString(cursor.getColumnIndex("price"));
                    map2.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    cursor.getString(cursor.getColumnIndex("cost"));
                    map2.put("cost", cursor.getString(cursor.getColumnIndex("cost")));
                    cursor.getString(cursor.getColumnIndex("amount"));
                    map2.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
                    cursor.getString(cursor.getColumnIndex("product_id"));
                    map2.put("product_id", cursor.getString(cursor.getColumnIndex("product_id")));
                    cursor.getString(cursor.getColumnIndex("bncode"));
                    map2.put("bncode", cursor.getString(cursor.getColumnIndex("bncode")));
                    cursor.getString(cursor.getColumnIndex("store"));
                    map2.put("store", cursor.getString(cursor.getColumnIndex("store")));
                    cursor.getString(cursor.getColumnIndex("good_limit"));
//                                    map2.put("good_limit",cursor.getString(cursor.getColumnIndex("good_limit")));
                    cursor.getString(cursor.getColumnIndex("good_stock"));
//                                    map2.put("good_stock",cursor.getString(cursor.getColumnIndex("good_stock")));
                    cursor.getString(cursor.getColumnIndex("PD"));
//                                    map2.put("PD",cursor.getString(cursor.getColumnIndex("PD")));
                    cursor.getString(cursor.getColumnIndex("GD"));
//                                    map2.put("GD",cursor.getString(cursor.getColumnIndex("GD")));
                    cursor.getString(cursor.getColumnIndex("marketable"));
//                                    map2.put("marketable",cursor.getString(cursor.getColumnIndex("marketable")));
                    cursor.getString(cursor.getColumnIndex("tag_name"));
//                                    map2.put("tag_name",cursor.getString(cursor.getColumnIndex("tag_name")));
                    cursor.getString(cursor.getColumnIndex("tag_id"));
//                                    map2.put("tag_id",cursor.getString(cursor.getColumnIndex("tag_id")));
                    cursor.getString(cursor.getColumnIndex("unit"));
//                                    map2.put("unit",cursor.getString(cursor.getColumnIndex("unit")));
                    cursor.getString(cursor.getColumnIndex("unit_id"));
//                                    map2.put("unit_id",cursor.getString(cursor.getColumnIndex("unit_id")));
                    cursor.getString(cursor.getColumnIndex("cash_id"));
                    map2.put("bn", cursor.getString(cursor.getColumnIndex("bn")));
                    map2.put("cash_id", cursor.getString(cursor.getColumnIndex("cash_id")));
                    listmap.add(map2);
                }
                map.put("goods_info", listmap);
                List.add(map);
            }
            Gson gson = new Gson();
            String str = gson.toJson(List);
            upNoInternet(str);
        } else {
            c.close();
            c2.close();
        }
    }

    //网络变化的监听接口
    @Override
    public void onNetChange(int netMobile) {
        super.onNetChange(netMobile);
        //网络状态变化时的操作
        if (netMobile== NetUtil.NETWORK_NONE){
            tv_internet.setVisibility(View.VISIBLE);
            isnetworknew=false;
            Log.d("printprint","网络情况为网络连接错误"+this.isNetConnect());
        }else {
            tv_internet.setVisibility(View.GONE);
            isnetworknew=true;
            upnotnet();
            Log.d("printprint","网络情况为网络连接正常"+this.isNetConnect());
        }
    }

    //初始化
    @Override
    protected void init() {
        super.init();
//        instance=this;//在onCreate里面写
//        getDialog();
        tv_discount= (TextView) findViewById(R.id.tv_discount);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_birthday= (TextView) findViewById(R.id.tv_birthday);
        tv_time= (TextView) findViewById(R.id.tv_time);
        tv_remark= (TextView) findViewById(R.id.tv_remark);
        tv_integral1= (TextView) findViewById(R.id.tv_integral1);
        tv_balance1= (TextView) findViewById(R.id.tv_balance1);
        tv_name1= (TextView) findViewById(R.id.tv_name1);
        tv_grade= (TextView) findViewById(R.id.tv_grade);
        ed_Discount= (TextView) findViewById(R.id.ed_Discount);
        but_close= (Button) findViewById(R.id.but_close);
        but_close.setOnClickListener(this);
        but_self_discount= (Button) findViewById(R.id.but_self_discount);
        but_discount= (Button) findViewById(R.id.but_discount);
        but_balance_paid= (Button) findViewById(R.id.but_balance_paid);
        but_recharge= (Button) findViewById(R.id.but_recharge);
        but_dimdis= (Button) findViewById(R.id.but_dimdis);
        but_eductible= (Button) findViewById(R.id.but_eductible);

        //副屏显示金额
        mSerialPortOperaion = new SerialPortOperaion(null,
                new SerialParam(2400,"/dev/ttyS3",0));
        try {
            mSerialPortOperaion.StartSerial();
        }catch (Exception e) {
        }

        Log.e("print","打印的数据"+SharedUtil.getString("seller_id"));

        //活动立减
        but_reduce= (Button) findViewById(R.id.but_reduce);
        but_reduce.setOnClickListener(this);
        but_Discount= (Button) findViewById(R.id.but_Discount);
        but_Discount.setOnClickListener(this);

        //去除小数操作
        but_Remove= (Button) findViewById(R.id.but_Remove);
        but_Remove.setOnClickListener(this);

        if (SharedUtil.getString("is_kg1")!=null){
            if (!SharedUtil.getString("is_kg1").equals("")){
                is_kg=SharedUtil.getString("is_kg1");
            }else {
                is_kg="2";
            }
        }else {
            is_kg="2";
        }

        tv_weight= (TextView) findViewById(R.id.tv_weight);
        tv_danwei1= (TextView) findViewById(R.id.tv_danwei1);

        //获取电子秤的数据
        initport();

        DemoApplication.setMainActivity(this);

        //蓝牙打印
        mService = new BluetoothService(this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(this, getResources().getString(R.string.Bluetooth_not_available), Toast.LENGTH_LONG).show();
            finish();
        }
        //蓝牙打印的自动开启
//        Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);      //��������һ����Ļ
//        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

        //判断蓝牙是否连接小票打印机
        String address_Mac="";
        if (SharedUtil.getString("ReceiptPrint_BluetoothMac_address")!=null){
            address_Mac= SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
        }
        if(TextUtils.isEmpty(address_Mac)){
//            retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(MainActivity.this,"如需打印小票，请先点击打印按钮，连接小票打印机",20);
        }else {
            con_dev = mService.getDevByMac(address_Mac);
            mService.connect(con_dev);
        }

        SharedUtil.putString("operational", "0");

        //初始化
        init1();
//        if (SysUtils.isNetworkAvailable(MainActivity.this)) {
//            getAdats();
//        }
        if (SharedUtil.getString("synchronization") != null) {
            if (Boolean.parseBoolean(SharedUtil.getString("synchronization"))) {
                getAdats();
            } else {
            }
        }

//        but_Quick.performClick();
        keyboard_tv_layout.performClick();


        if (isnetworknew){
            tv_internet.setVisibility(View.GONE);
            upnotnet();
        }else {
            tv_internet.setVisibility(View.VISIBLE);
        }

        //加载数据
        LoadDates();

        //获得挂单数挂单的个数
        getordernum();
        //获得赊账数个数
        getcreditnum();
    }


    //获取电子秤的数据
    private void initport() {
        mApplication = (DemoApplication) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            if(mInputStream==null){
                Log.d("获取重量的长度","444444");
            }

			/* Create a receiving thread */

//            mReadThread = new Thread();
//            mReadThread.start();
            start();

        } catch (SecurityException e) {
//			DisplayError(R.string.error_security);
        } catch (IOException e) {
//			DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
//			DisplayError(R.string.error_configuration);
        }
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_close:
                rl_jishuang.setVisibility(View.VISIBLE);
                layout_go_pay.setVisibility(View.GONE);
                setType("0",SharedUtil.getfalseBoolean("sw_member_price"));
                getwholesale(Member_type,SharedUtil.getfalseBoolean("sw_member_price"),"0","0");
//                adapterzhu.setType(SharedUtil.getfalseBoolean("sw_member_price"));
                break;
            case R.id.but_reduce:
                if (commodities.size()>0){
                    showRecharge();
//                    showreduce();
                }else {
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.no_reduction_without_commodities),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_Discount:
                showDiscount();
                break;
            case R.id.but_Remove:
                if (SharedUtil.getInt("zero")!=-1){
                    String str=TlossUtils.round_down(tv_netreceipts.getText().toString(),SharedUtil.getInt("zero"));
                    tv_netreceipts.setText(str + "");
                    Showtotal(str+"");
                }else {
                    String str=TlossUtils.getRemove(tv_netreceipts.getText().toString());
                    tv_netreceipts.setText(str + "");
                    Showtotal(str+"");
                }
                break;
        }
    }

    //立减加密码
    public void showreduce(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View layout = inflater.inflate(R.layout.dialog_lockscreen, null);
        final EditText ed_password = (EditText) layout.findViewById(R.id.ed_password);
        ed_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        final TextView ed_text = (TextView) layout.findViewById(R.id.ed_user);
        ed_text.setText(SharedUtil.getString("seller_name"));
        Button but_cancel= (Button) layout.findViewById(R.id.but_cancel);

        final Button but_lock = (Button) layout.findViewById(R.id.but_lock);
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("锁屏");
        dialog.show();
        dialog.getWindow().setContentView(layout);

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        but_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_password.getText().toString().equals(SharedUtil.getString("local_password"))) {
                    dialog.dismiss();
                    showRecharge();
                }
            }
        });
    }


    //设置折扣dialog
    public void showDiscount(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle(getResources().getString(R.string.discount));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.discount_dialog);
        final EditText ed_discount = (EditText) window.findViewById(R.id.ed_discount);

        Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button but_submit= (Button) window.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDiscount(ed_discount.getText().toString());
//                double dis_Total=0;
//                double Total=0;
//                double Total1=0;
//                if (commodities.size()>0&&!ed_discount.getText().toString().equals("")){
//                    for (int i=0;i<commodities.size();i++){
//                        if (commodities.get(i).getIs_special_offer()!=null){
//                            if (commodities.get(i).getIs_special_offer().equals("no")){
//                                    dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
//                            }else {
//                                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//                            }
//                        }else {
//                                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//                        }
//                    }
//
//                    Total1=TlossUtils.add(Total,dis_Total);
//
//                    Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(ed_discount.getText().toString())/10)));
//
//                    discount=true;
//                    _discount=TlossUtils.sub(Total1,Total)+"";
//                    _Total = Total1+"";
//
//                    _Memberdiscount=(float) (Double.parseDouble(ed_discount.getText().toString())/10);
//
//                    tv_Total.setText(Total+"");
//                    tv_netreceipts.setText(Total+"");
//                    Showtotal(Total+"");
//                    Intent intent=new Intent();
//                    intent.setAction("com.yzx.changing");
//                    intent.putExtra("type","2");
//                    intent.putExtra("Total", Total);
//                    sendBroadcast(intent);

//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
//                }
                dialog.dismiss();
            }
        });


    }


    //设置折扣
    public void setDiscount(String strdiscount){
        double dis_Total=0;
        double Total=0;
        double Total1=0;
        if (commodities.size()>0&&!strdiscount.equals("")){
            for (int i=0;i<commodities.size();i++){
                if (commodities.get(i).getIs_special_offer()!=null){
                    if (commodities.get(i).getIs_special_offer().equals("no")){
                        dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                    }else {
                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                    }
                }else {
                    if (!commodities.get(i).getGoods_id().equals("null")&&commodities.get(i).getGoods_id()!=null){
                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                    }else {
                        dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                    }
//                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                }
            }

            Total1=TlossUtils.add(Total,dis_Total);

            Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(strdiscount)/10)));

            discount=true;
            _discount=TlossUtils.sub(Total1,Total)+"";
            _Total = Total1+"";

            _Memberdiscount=(float) (Double.parseDouble(strdiscount)/10);

            tv_Total.setText(Total+"");
            tv_netreceipts.setText(Total+"");
            Showtotal(Total+"");
            Intent intent=new Intent();
            intent.setAction("com.yzx.changing");
            intent.putExtra("type","2");
            intent.putExtra("Total", Total);
            sendBroadcast(intent);
        }
    }


    //弹出立减的弹窗
    public void showRecharge(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle(getResources().getString(R.string.activities));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.redeem_dialog);
        final EditText ed_money = (EditText) window.findViewById(R.id.ed_money);
        ed_money.setHint(getResources().getString(R.string.input_password_account));
        Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);

        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Commodity commodity = new Commodity();
                ShuliangEntty shuliang = new ShuliangEntty();
                commodity.setName(getResources().getString(R.string.activities_reduction));
                commodity.setPrice("-"+ed_money.getText().toString());
                commodity.setMember_price("-"+ed_money.getText().toString());
                shuliang.setNumber(1);
                _reduce=ed_money.getText().toString();
                commodity.setCost(0 + "");
                commodity.setStore(200 + "");
                commodity.setGoods_id("null");
                commodities.add(commodity);
                entty.add(shuliang);
                adapterzhu.getadats(commodities);
                adapterzhu.getEntty(entty);
                lv.setAdapter(adapterzhu);

                tv_Totalmerchandise.setText((Float.parseFloat(tv_Totalmerchandise.getText().toString()) + 1) + "");
                j = Double.parseDouble(tv_Total.getText().toString());
                j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
                tv_Total.setText(StringUtils.stringpointtwo(j + ""));
                tv_netreceipts.setText(j + "");
                Showtotal(j + "");
                et_keyoard.setText(j + "");
                dialog.dismiss();

                reduce=true;

//                        tv_Total.setText(ed_money.getText().toString());
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
//                        tv_netreceipts.setText(ed_money.getText().toString());
            }
        });

        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //退货立减密码的功能  local_password
    public void showpassword(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View layout = inflater.inflate(R.layout.dialog_lockscreen, null);
        final EditText ed_password = (EditText) layout.findViewById(R.id.ed_password);
        ed_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        final TextView ed_text = (TextView) layout.findViewById(R.id.ed_user);
        ed_text.setText(SharedUtil.getString("seller_name"));
        Button but_cancel= (Button) layout.findViewById(R.id.but_cancel);

        final Button but_lock = (Button) layout.findViewById(R.id.but_lock);
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("锁屏");
        dialog.show();
        dialog.getWindow().setContentView(layout);

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        but_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_password.getText().toString().equals(SharedUtil.getString("local_password"))) {
                    dialog.dismiss();
                    showDialog();
                }
            }
        });
    }


    /**
     * 获取重量
     */
    String finalStr="0";
    String strweigh="0";
    public void getport(byte[] buffer, int size){
        String result = new String(buffer,0,size);
        String str="";
        Log.d("获取重量的长度",result+"");
        String isport = StringUtils.isport(result);



        if (result.length()>15&&result.indexOf("-") == -1) {
            str = result.substring(result.indexOf("+"));
//			tv_kg.setText(str.substring(1, str.indexOf("kg")));
            if (result.indexOf("+") < result.indexOf("kg")) {
//                finalStr = isport;
                finalStr = str;
                tv_weight.post(new Runnable() {
                    @Override
                    public void run() {
//                        if (!finalStr.equals("")) {
//                        if (!is_kg) {
//                            tv_weight.setVisibility(View.VISIBLE);
//                            tv_weight.setText(finalStr);
//                            tv_danwei1.setText("Kg");
//                            if (diatv_weight != null) {
//                                diatv_weight.setVisibility(View.VISIBLE);
//                                diatv_weight.setText(finalStr);
//                                tv_danwei.setText("Kg");
//                            }
//                            strweigh = tv_weight.getText().toString();
//                        } else {
//                            tv_weight.setVisibility(View.VISIBLE);
//                            tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr), (double) 2) + "");
//                            tv_danwei1.setText("斤");
//                            if (diatv_weight != null) {
//                                diatv_weight.setVisibility(View.VISIBLE);
//                                diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr), (double) 2) + "");
//                                tv_danwei.setText("斤");
//                            }
//                            strweigh = tv_weight.getText().toString();
//                        }
//                    }else {
//                            tv_weight.setText("0");
//                            tv_weight.setVisibility(View.GONE);
//                            tv_danwei1.setText("数据错误");
//                            if (diatv_weight!=null){
//                                tv_danwei.setText("数据错误");
//                                diatv_weight.setVisibility(View.GONE);
//                                diatv_weight.setText("0");
//                            }
//                        }

                        //更新UI  (?<==").*(?="kg)
                        if (finalStr.indexOf("kg") > 4) {
                            if (is_kg.equals("1")) {
//                            Log.d("zhu22", "onDataReceived="+finalStr.substring(1, finalStr.indexOf("kg")));
                                tv_weight.setText(finalStr.substring(1, finalStr.indexOf("kg")));
                                tv_danwei1.setText("Kg");
                                if (diatv_weight != null) {
                                    diatv_weight.setText(finalStr.substring(1, finalStr.indexOf("kg")));
                                    tv_danwei.setText("Kg");
                                }
                                strweigh=tv_weight.getText().toString();
                            } else if (is_kg.equals("2")){
                                tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 2) + "");
                                tv_danwei1.setText(getResources().getString(R.string.jin));
                                if (diatv_weight != null) {
                                    diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 2) + "");
                                    tv_danwei.setText(getResources().getString(R.string.jin));
                                }
                                strweigh=tv_weight.getText().toString();
                            }else if (is_kg.equals("3")){
                                tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 20) + "");
                                tv_danwei1.setText(getResources().getString(R.string.liang));
                                if (diatv_weight != null) {
                                    diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 20) + "");
                                    tv_danwei.setText(getResources().getString(R.string.liang));
                                }
                                strweigh=tv_weight.getText().toString();
                            }else if (is_kg.equals("4")){
                                tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 1000) + "");
                                tv_danwei1.setText(getResources().getString(R.string.liang));
                                if (diatv_weight != null) {
                                    diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 1000) + "");
                                    tv_danwei.setText(getResources().getString(R.string.liang));
                                }
                                strweigh=tv_weight.getText().toString();
                            }
                        }
                    }
                });
//            runOnUiThread(new Runnable(){
//
//                @Override
//                public void run() {
//
//                }
//
//            });
            }
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(MainActivity.this);
////        retail.yzx.com.supper_self_service.Utils.StringUtils.initOKgo(MainActivity.this);//初始化网络请求
//        setContentView(R.layout.activity_main);
//
//    }

    //手动刷新
    public void refresh_onClick(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle(getResources().getString(R.string.data_wait_long_time));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.refresh_dialog);
        Button but_goto = (Button) window.findViewById(R.id.but_goto);
        Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        but_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                /**
                 * 备份数据库
                 */


                getAdats();
            }
        });

    }

    //手动输入条码
    public void Codeinputonclick(View view) {
        dialogfuzzy = new Dialog(MainActivity.this);
        dialogfuzzy.setTitle(getResources().getString(R.string.please_input_code));
        dialogfuzzy.setCanceledOnTouchOutside(false);
        dialogfuzzy.show();
        Window window = dialogfuzzy.getWindow();
        window.setContentView(R.layout.fuzzy);
        final EditText editText = (EditText) window.findViewById(R.id.ed_page);
        final EditText ed_name = (EditText) window.findViewById(R.id.ed_name);
        Button but_goto = (Button) window.findViewById(R.id.but_goto);
        Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
        final ListView lv_fuzzy = (ListView) window.findViewById(R.id.lv_fuzzy);
        but_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Cursor cursor=null;
//                editText.getText().toString();
//                if (ed_name.getText().toString().equals("")&&!editText.getText().toString().equals("")){
//                    cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode like " + "'%" + editText.getText().toString() + "'", null);
//                    if (cursor != null) {
//                        list_fuzzy.clear();
//                        while (cursor.moveToNext()) {
//                            Commodity fuzzy = new Commodity();
//                            fuzzy.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
//                            fuzzy.setName(cursor.getString(cursor.getColumnIndex("name")));
//                            fuzzy.setPy(cursor.getString(cursor.getColumnIndex("py")));
//                            fuzzy.setPrice(cursor.getString(cursor.getColumnIndex("price")));
//                            fuzzy.setCost(cursor.getString(cursor.getColumnIndex("cost")));
//                            fuzzy.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
//                            fuzzy.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
//                            fuzzy.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
//                            fuzzy.setType(Member_type);
//                            list_fuzzy.add(fuzzy);
//                        }
//                        adapter_fuzzy.setAdats(list_fuzzy);
//                        adapter_fuzzy.SetOnclick(MainActivity.this);
//                        lv_fuzzy.setAdapter(adapter_fuzzy);
//                        adapter_fuzzy.notifyDataSetChanged();
//                    }
//                }

                if (!ed_name.getText().toString().equals("")&&editText.getText().toString().equals("")){
//                    cursor = sqLiteDatabase.rawQuery("select * from commodity where name like " + "'%" + editText.getText().toString() + "%'", null);
                    if (isnetworknew){
//                        getseek(ed_name.getText().toString(),lv_fuzzy);
                        LocalSeek(ed_name.getText().toString(),lv_fuzzy);
                    }else {
                        LocalSeek(ed_name.getText().toString(),lv_fuzzy);
                    }

                }
                if (!ed_name.getText().toString().equals("")&&!editText.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.only_one_times),Toast.LENGTH_SHORT).show();
                }






            }
        });
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialogfuzzy.dismiss();
            }
        });
    }

    //外卖订单
    public void Onclick_but_take(View view){
        Intent intent=new Intent(MainActivity.this,Take_outfood_Activity.class);
        intent.putExtra("type","1");
        startActivity(intent);
    }

    //获得赊账的个数
    public void getcreditnum() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_notpay_order_totalnum"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("TAG", "赊账的数量" + s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            JSONObject jo2 = jo.getJSONObject("response");
                            String status = jo2.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja = jo2.getJSONArray("data");
                                JSONObject jo3 = ja.getJSONObject(0);
                                String num = jo3.getString("num");
                                if (Integer.valueOf(num) > 0) {
                                    but_cc_credit.setVisibility(View.VISIBLE);
                                    but_cc_credit.setText(num);
                                } else {
                                    but_cc_credit.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

    }

    //获得挂单数挂单的个数
    private void getordernum() {
        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        long hangup = sqlite_entity.findMaxId("hangup");
        if (hangup>0){
            but_cc_quick.setVisibility(View.VISIBLE);
            but_cc_quick.setText(hangup+"");
        }else {
            but_cc_quick.setVisibility(View.GONE);
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
//                                    but_cc_quick.setVisibility(View.VISIBLE);
//                                    but_cc_quick.setText(num);
//                                } else {
//                                    but_cc_quick.setVisibility(View.GONE);
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

    //    public void switchFrament(Fragment from, Fragment to) {
//        if (from != to) {
//            mcontext = to;
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            if (!to.isAdded()) {
//                if (from != null) {
//                    ft.hide(from);
//                }
//                if (to != null) {
//                    ft.add(R.id.Rl_quick, to).commit();
//                }
//
//            } else {
//                if (from != null) {
//                    ft.hide(from);
//                }
//                if (to != null) {
//                    ft.show( to).commitAllowingStateLoss();
//                }
//
//
//            }
//
//        }
//    }


    /**
     * 保存Fragment
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (quick_fragment != null) {
            getSupportFragmentManager().putFragment(outState, HOME_FRAGMENT_KEY, quick_fragment);
        }
        if (cook_fragment != null) {
            getSupportFragmentManager().putFragment(outState, DASHBOARD_FRAGMENT_KEY, cook_fragment);
        }
    }

    private static final String HOME_FRAGMENT_KEY = "Quick_fragment";
    private static final String DASHBOARD_FRAGMENT_KEY = "Cook_fragment";
    @Override
    public void storageFragment(Bundle savedInstanceState) {
        super.storageFragment(savedInstanceState);
        if (savedInstanceState != null) {
            /*获取保存的fragment  没有的话返回null*/
            quick_fragment = (Quick_fragment) getSupportFragmentManager().getFragment(savedInstanceState, HOME_FRAGMENT_KEY);
            cook_fragment = (Cook_fragment) getSupportFragmentManager().getFragment(savedInstanceState, DASHBOARD_FRAGMENT_KEY);
            addToList(quick_fragment);
            addToList(cook_fragment);

        }
    }

    private void addToList(Fragment fragment) {
        if (fragment != null) {
            fragmentList.add(fragment);
        }
    }

    /**
     * 显示fragment
     */
    private void showFragment1(Fragment fragment) {
        for (Fragment frag : fragmentList) {
            if (frag != fragment) {
                /*先隐藏其他fragment*/
                getSupportFragmentManager().beginTransaction().hide(frag).commitAllowingStateLoss();
            }
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();

    }

    /**
     * 添加fragment
     */
    private void addFragment(Fragment fragment) {

        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.Rl_quick, fragment).commitAllowingStateLoss();
        /*添加到 fragmentList*/
            fragmentList.add(fragment);
        }
    }
//    private FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case R.id.but_Quick:
//                    return new Quick_fragment();
//                case R.id.but_Cook:
//                    return new Cook_fragment();
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    };

    /**
     * 蓝牙打印连接蓝牙的handler
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
//                            Toast.makeText(getApplicationContext(), "连接成功",
//                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
//                    Toast.makeText(getApplicationContext(), "设备连接丢失",
//                            Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    if (getApplicationContext()!=null){
//                        Toast.makeText(getApplicationContext(), "关闭设备",
//                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };



    //判断字符是小数还是整数
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }



    @Override
    public void setnums(final int i) {
        if (!issucceed) {
            final Dialog dialog = new Dialog(this);
            dialog.setTitle(getResources().getString(R.string.modified_quantity));
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_supplement);
            Button but_add = (Button) window.findViewById(R.id.but_add);
            TextView tv_t1 = (TextView) window.findViewById(R.id.tv_t1);
            tv_t1.setText(getResources().getString(R.string.modified_quantity));
            final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
            ed_add.setHint(getResources().getString(R.string.please_modified_quantity));
            DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
            ed_add.setKeyListener(numericOnlyListener);

            ed_add.setText(entty.get(i).getNumber() + "");
            ed_add.selectAll();
            but_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ed_add.getText().toString().equals("") && Float.parseFloat(ed_add.getText().toString()) > 0) {
                        entty.get(i).setNumber(Float.parseFloat(ed_add.getText().toString()));
                        adapterzhu.getadats(commodities);
                        adapterzhu.getEntty(entty);
                        lv.setAdapter(adapterzhu);
                        double tolta = 0;
                        float Totalmerchandise = 0;
                        for (int p = 0; p < commodities.size(); p++) {
//                        Double.parseDouble(commodities.get(p).getPrice()) * entty.get(p).getNumber()

                            if (iswholesale){
                                tolta = TlossUtils.add(tolta,TlossUtils.mul(Double.parseDouble(commodities.get(p).getPrice()),Double.parseDouble(entty.get(p).getNumber()+"")) );
                            }else {
                                tolta = TlossUtils.add(tolta,TlossUtils.mul(Double.parseDouble(commodities.get(p).getMember_price()),Double.parseDouble(entty.get(p).getNumber()+"")) );
                            }

                            Totalmerchandise = Totalmerchandise + entty.get(p).getNumber();
                        }
                        tv_netreceipts.setText("" + tolta);
                        Showtotal("" + tolta);
                        tv_Total.setText("" + tolta);
                        if (isNumeric(ed_add.getText().toString())){
                            tv_Totalmerchandise.setText("" + Totalmerchandise);
                        }
//
                        Intent intent=new Intent();
                        intent.setAction("com.yzx.changing");
                        intent.putExtra("type","0");
                        intent.putExtra("item", i);
                        intent.putExtra("num",Float.parseFloat(ed_add.getText().toString()));
                        sendBroadcast(intent);

                        dialog.dismiss();
                    }
                }
            });
        }
    }


    /**
     * 设置价格
     * @param i
     */
    @Override
    public void setprice(final int i) {
        final Dialog dialog1 = new Dialog(this);
        dialog1.setTitle(getResources().getString(R.string.modified_price));
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setContentView(R.layout.dialog_supplement);
        TextView tv_t1= (TextView) window.findViewById(R.id.tv_t1);
        final EditText ed_add= (EditText) window.findViewById(R.id.ed_add);
        Button but_add= (Button) window.findViewById(R.id.but_add);
        LinearLayout ll_grade= (LinearLayout) window.findViewById(R.id.ll_grade);
        ll_grade.setVisibility(View.VISIBLE);
        tv_t1.setText(getResources().getString(R.string.modified_price));
        ed_add.setHint(getResources().getString(R.string.please_modified_price));
        DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
        ed_add.setKeyListener(numericOnlyListener);

        TextView tv_lv0= (TextView) window.findViewById(R.id.tv_lv0);
        TextView tv_lv1= (TextView) window.findViewById(R.id.tv_lv1);
        TextView tv_lv2= (TextView) window.findViewById(R.id.tv_lv2);
        TextView tv_lv3= (TextView) window.findViewById(R.id.tv_lv3);
        TextView tv_lv4= (TextView) window.findViewById(R.id.tv_lv4);
        TextView tv_lv5= (TextView) window.findViewById(R.id.tv_lv5);

        tv_lv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("0");
                getwholesale("0",true,"1",i+"");
                adapterzhu.getadats(commodities);
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });

        tv_lv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("1");
                getwholesale("1",true,"1",i+"");
                adapterzhu.getadats(commodities);
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });
        tv_lv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("2");
                getwholesale("2",true,"1",i+"");
                adapterzhu.getadats(commodities);
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });
        tv_lv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("3");
                getwholesale("3",true,"1",i+"");
                adapterzhu.getadats(commodities);
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });
        tv_lv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("4");
                getwholesale("4",true,"1",i+"");
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });
        tv_lv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodities.get(i).setType("5");
                getwholesale("5",true,"1",i+"");
                adapterzhu.getadats(commodities);
                adapterzhu.setType(true);
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });

        if(iswholesale){
            ed_add.setText(commodities.get(i).getPrice() + "");
        }else {
            ed_add.setText(commodities.get(i).getMember_price() + "");
        }

        if (SharedUtil.getfalseBoolean("sw_member_price")) {
            if (commodities.get(i).getIs_special_offer()!=null){
                if (commodities.get(i).getIs_special_offer().equals("no")) {
                    if (commodities.get(i).getType().equals("0")) {
                        ed_add.setText(commodities.get(i).getPrice() + "");
                    }else {
                        if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                            String[] strings = StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",");
//                            String price = "";
//                            strings[Integer.parseInt(commodities.get(i).getType()) - 1] = ed_add.getText().toString();
//                            for (int j = 0; j < strings.length; j++) {
//                                if (strings.length - 1 == j) {
//                                    price = price + strings[j];
//                                } else {
//                                    price = price + strings[j] + ",";
//                                }
//                            }
                            ed_add.setText(strings[Integer.parseInt(commodities.get(i).getType()) - 1] + "");
                        }else {
                            ed_add.setText(commodities.get(i).getMember_price() + "");
                        }
                    }
                }else {
                    ed_add.setText(commodities.get(i).getPrice() + "");
                }
            }else {
                ed_add.setText(commodities.get(i).getPrice() + "");
            }
        }else {
            ed_add.setText(commodities.get(i).getPrice() + "");
        }

        ed_add.selectAll();
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(iswholesale){
//                    commodities.get(i).setPrice(ed_add.getText().toString());
//                }else {
//                    commodities.get(i).setMember_price(ed_add.getText().toString());
//                }

                if (SharedUtil.getfalseBoolean("sw_member_price")) {
                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("no")) {
                            if (commodities.get(i).getType().equals("0")) {
                                commodities.get(i).setPrice(ed_add.getText().toString());
                            }else {
                                if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                    String[] strings = StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",");
                                    String price = "";
                                    strings[Integer.parseInt(commodities.get(i).getType()) - 1] = ed_add.getText().toString();
                                    for (int j = 0; j < strings.length; j++) {
                                        if (strings.length - 1 == j) {
                                            price = price + strings[j];
                                        } else {
                                            price = price + strings[j] + ",";
                                        }
                                    }
                                    commodities.get(i).setCustom_member_price(price);
                                }else {
                                    commodities.get(i).setPrice(ed_add.getText().toString());
                                }
                            }
                        }else {
                            commodities.get(i).setPrice(ed_add.getText().toString());
                        }
                    }else {
                        commodities.get(i).setPrice(ed_add.getText().toString());
                    }
                }else {
                    commodities.get(i).setPrice(ed_add.getText().toString());
                }

                Intent intent=new Intent();
                intent.setAction("com.yzx.changing");
                intent.putExtra("type","1");
                intent.putExtra("item", i);
                intent.putExtra("num",Float.parseFloat(ed_add.getText().toString()));
                sendBroadcast(intent);

                float nums=0;
                double totalMember=0;
                for (int k=0;k<commodities.size();k++){
                    nums=nums+entty.get(k).getNumber();
//                    totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                    if (commodities.get(k).getType()!=null&&!commodities.get(k).getType().equals("")&&!commodities.get(k).getType().equals("0")) {
                        if (commodities.get(k).getIs_special_offer()!=null){
                            if (commodities.get(k).getIs_special_offer().equals("no")) {
                                if (commodities.get(k).getCustom_member_price() != null && !commodities.get(k).getCustom_member_price().equals("")&& !commodities.get(k).getCustom_member_price().equals("null")) {
                                    if (!StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1]), Double.parseDouble(entty.get(k).getNumber() + "")));
                                    }
                                } else {
                                    if (!commodities.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                    }
                                }
                            } else {
                                if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                    totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                }
                            }
                        }else {
                            if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                            }
                        }
                    }else {
                        if (!commodities.get(k).getPrice().equals("")&&!(entty.get(k).getNumber()+"").equals("")){
                            totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                        }
                    }
                }

                tv_Totalmerchandise.setText(nums+"");
                tv_Total.setText(totalMember+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                tv_netreceipts.setText(totalMember+"");
                Showtotal(totalMember+"");
                adapterzhu.notifyDataSetChanged();
                dialog1.dismiss();
            }
        });
    }

    private void init1() {
        cancelled=new ArrayList<>();
        cancelledenyyt=new ArrayList<>();

        member_entties=new ArrayList<>();

        getphone();

//      版本更新
        update();

        //会员兑换商品列表
        integral_list=new ArrayList<>();
        specification_list=new ArrayList<>();

        but_xianjin= (Button) findViewById(R.id.but_xianjin);

        Rl_xianjin=(RelativeLayout) findViewById(R.id.Rl_xianjin);
        Rl_time=(RelativeLayout) findViewById(R.id.Rl_time);
        Rl_yidong=(RelativeLayout) findViewById(R.id.Rl_yidong);
        ll_jshuang=(LinearLayout) findViewById(R.id.ll_jshuang);
        layout_go_pay=(RelativeLayout) findViewById(R.id.layout_go_pay);

        but_refresh = (Button) findViewById(R.id.but_refresh);

        but_CodeInput = (Button) findViewById(R.id.but_CodeInput);
        but_share_tools = (Button) findViewById(R.id.but_share_tools);//共享
        but_share_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Dialog dialog = new Dialog(MainActivity.this);
//                dialog.setTitle("活动");
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setContentView(R.layout.redeem_dialog);
//                final EditText ed_money = (EditText) window.findViewById(R.id.ed_money);
//                ed_money.setHint("输入优惠金额");
//                Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
//                Button but_submit = (Button) window.findViewById(R.id.but_submit);
//
//                but_submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Commodity commodity = new Commodity();
//                        ShuliangEntty shuliang = new ShuliangEntty();
//                        commodity.setName("活动立减");
//                        commodity.setPrice("-"+ed_money.getText().toString());
//                        shuliang.setNumber(1);
//                        commodity.setCost(100 + "");
//                        commodity.setStore(200 + "");
//                        commodity.setGoods_id("null");
//                        commodities.add(commodity);
//                        entty.add(shuliang);
//                        adapterzhu.getadats(commodities);
//                        adapterzhu.getEntty(entty);
//                        lv.setAdapter(adapterzhu);
//
//                        tv_Totalmerchandise.setText((Integer.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
//                        j = Double.parseDouble(tv_Total.getText().toString());
//                        j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
//                        tv_Total.setText(StringUtils.stringpointtwo(j + ""));
//                        tv_netreceipts.setText(j + "");
//                        et_keyoard.setText(j + "");
//                        dialog.dismiss();
//
////                        tv_Total.setText(ed_money.getText().toString());
////                                            tv_netreceipts.setText(specification_list.get(i).getVal());
////                        tv_netreceipts.setText(ed_money.getText().toString());
//                    }
//                });
//
//                but_abolish.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
                /**
                 *会员价
                 */
                getmemprice(iswholesale);

//                if (SharedUtil.getfalseBoolean("sw_member_price")){
//                    getwholesale(Member_type);
//                }else {
//                    getretail();
//                }
                /**
                 * 去除共享
                 */
//                Intent intent=new Intent(MainActivity.this, Share_Tools_Activity.class);
//                startActivity(intent);
            }
        });
        tv_Staff= (TextView) findViewById(R.id.tv_Staff);
        tv_Staff.setText(SharedUtil.getString("name"));
        but_cut = (Button) findViewById(R.id.but_cut);
        but_fruit = (Button) findViewById(R.id.but_fruit);

        Rl_layout = (RelativeLayout) findViewById(R.id.Rl_layout);

        ll_time = (LinearLayout) findViewById(R.id.ll_time);

        tv_Totalamount = (TextView) findViewById(R.id.tv_Totalamount);
        tv_11 = (TextView) findViewById(R.id.tv_11);
        tv_Symbol = (TextView) findViewById(R.id.tv_Symbol);

        rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_2 = (RadioButton) findViewById(R.id.rb_2);

//        but_cut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!version) {
//                    but_cut.setText("便利店");
//
//                    tv_Symbol.setVisibility(View.GONE);
//                    but_cc_Cashbox.setVisibility(View.GONE);
//                    but_Print.setVisibility(View.GONE);
//                    but_credit.setVisibility(View.GONE);
//                    but_Lockscreen.setVisibility(View.GONE);
//                    tv_Total.setTextSize(20f);
//                    tv_Totalmerchandise.setTextSize(20f);
//                    tv_Totalamount.setTextSize(20f);
//                    tv_11.setTextSize(20f);
//                    tv_Symbol.setTextSize(20f);
//
//                    adapterzhu.setversion(version);
//                    adapterzhu.notifyDataSetChanged();
//                    tv_name.setVisibility(View.GONE);
//                    but_Quick.setText("川菜");
//                    but_Cook.setText("粤菜");
//                    rb_1.setVisibility(View.VISIBLE);
//                    rb_2.setVisibility(View.VISIBLE);
//                    rb_1.setText("鲁菜");
//                    rb_2.setText("湘菜");
//                    tv_name.setVisibility(View.GONE);
//                    Intent intent = new Intent();
//                    intent.setAction("com.yzx.cut");
//                    intent.putExtra("line", version);
//                    sendBroadcast(intent);
//                    but_cc_credit.setVisibility(View.GONE);
//
//                    ll_time.setVisibility(View.GONE);
//                    tv_store.setVisibility(View.GONE);
//                    tv_jinjia.setVisibility(View.GONE);
//                    tv_lirunli.setVisibility(View.GONE);
//                    ll_time.setVisibility(View.GONE);
//                    Rl_layout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f));
//                    rl_quick.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
//                    version = true;
//                } else {
//                    but_cut.setText("餐饮");
//                    tv_Symbol.setVisibility(View.VISIBLE);
//                    but_cc_Cashbox.setVisibility(View.VISIBLE);
//                    but_Print.setVisibility(View.VISIBLE);
//                    but_credit.setVisibility(View.VISIBLE);
//                    but_Lockscreen.setVisibility(View.VISIBLE);
//                    tv_Total.setTextSize(40f);
//                    tv_Totalmerchandise.setTextSize(40f);
//                    tv_Totalamount.setTextSize(30f);
//                    tv_11.setTextSize(30f);
//                    tv_Symbol.setTextSize(40f);
//                    adapterzhu.setversion(version);
//                    adapterzhu.notifyDataSetChanged();
//                    rb_1.setVisibility(View.GONE);
//                    rb_2.setVisibility(View.GONE);
//                    tv_name.setVisibility(View.VISIBLE);
//                    but_Quick.setText("快捷栏");
//                    but_Cook.setText("煮物栏");
//                    but_cc_credit.setVisibility(View.VISIBLE);
//                    Intent intent = new Intent();
//                    intent.setAction("com.yzx.cut");
//                    intent.putExtra("line", version);
//                    sendBroadcast(intent);
//
//                    tv_name.setVisibility(View.VISIBLE);
//
//                    ll_time.setVisibility(View.VISIBLE);
//                    tv_store.setVisibility(View.VISIBLE);
//                    tv_jinjia.setVisibility(View.VISIBLE);
//                    tv_lirunli.setVisibility(View.VISIBLE);
//                    Rl_layout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
//                    rl_quick.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
//                    version = false;
//                }
//
//            }
//        });
//        but_fruit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (version) {
//                    but_Quick.setVisibility(View.VISIBLE);
//                    but_Cook.setVisibility(View.VISIBLE);
//                    rb_2.setVisibility(View.VISIBLE);
//                    rb_1.setVisibility(View.VISIBLE);
//
//                    but_Quick.setText("热带水果");
//                    but_Cook.setText("苹果");
//                    rb_1.setText("香蕉");
//                    rb_2.setText("梨");
//
//                } else {
//                    but_Quick.setVisibility(View.VISIBLE);
//                    but_Cook.setVisibility(View.VISIBLE);
//                    rb_2.setVisibility(View.VISIBLE);
//                    rb_1.setVisibility(View.VISIBLE);
//
//                    tv_name.setVisibility(View.GONE);
//                    tv_Symbol.setVisibility(View.GONE);
//                    but_cc_Cashbox.setVisibility(View.GONE);
//                    but_Print.setVisibility(View.GONE);
//                    but_credit.setVisibility(View.GONE);
//                    but_Lockscreen.setVisibility(View.GONE);
//                    tv_Total.setTextSize(20f);
//                    tv_Totalmerchandise.setTextSize(20f);
//                    tv_Totalamount.setTextSize(20f);
//                    tv_11.setTextSize(20f);
//                    tv_Symbol.setTextSize(20f);
//
//                    but_Quick.setText("热带水果");
//                    but_Cook.setText("苹果");
//                    rb_1.setText("香蕉");
//                    rb_2.setText("梨");
//
//                    adapterzhu.setversion(version);
//                    adapterzhu.notifyDataSetChanged();
//                    Intent intent = new Intent();
//                    intent.setAction("com.yzx.cut");
//                    intent.putExtra("line", version);
//                    sendBroadcast(intent);
//                    but_cc_credit.setVisibility(View.GONE);
//
//                    ll_time.setVisibility(View.GONE);
//                    tv_store.setVisibility(View.GONE);
//                    tv_jinjia.setVisibility(View.GONE);
//                    tv_lirunli.setVisibility(View.GONE);
//                    ll_time.setVisibility(View.GONE);
//                    Rl_layout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f));
//                    rl_quick.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
//                    version = true;
//                }
//
//            }
//        });

        im_picture = (FlyBanner) findViewById(R.id.im_picture);
        imageurls = new ArrayList<>();
        getimager();

        Rl_quick = (FrameLayout) findViewById(R.id.Rl_quick);

        but_internet = (Button) findViewById(R.id.but_internet);

        tv_internet = (TextView) findViewById(R.id.tv_internet);

        im_code = (ImageView) findViewById(R.id.im_code);

        but_Cashbox = (FButton) findViewById(R.id.but_Cashbox);
        but_mobilepayment = (FButton) findViewById(R.id.but_mobilepayment);

        but_goback= (Button) findViewById(R.id.but_goback);

        Rl_yidong = (RelativeLayout) findViewById(R.id.Rl_yidong);
        pressed = (ProgressView) findViewById(R.id.pressed);

        if (ServiceUtils.isServiceRunning(this,"Myservice")){
        }else {
            Intent stopIntent = new Intent(MainActivity.this, Myservice.class);
            startService(stopIntent);
        }

//        MiPushClient.setAlias(getApplicationContext(), "seller_" + SharedUtil.getString("seller_id"), null);
//        MiPushClient.unsetUserAccount(getApplicationContext(), "seller_" + SharedUtil.getString("seller_id"), null);
        tv_name = (TextView) findViewById(R.id.tv_name);
        if (SharedUtil.getString("name") != null) {
            tv_name.setText(SharedUtil.getString("name"));
        }
        tv_jinjia = (TextView) findViewById(R.id.tv_jinjia);
        tv_lirunli = (TextView) findViewById(R.id.tv_lirunli);
        tv_store = (TextView) findViewById(R.id.tv_store);

        but_More = (TextView) findViewById(R.id.but_More);

//        if (SharedUtil.getString("type") != null) {
//            if (SharedUtil.getString("type").equals("4")) {
//                tv_lirunli.setVisibility(View.GONE);
//                tv_jinjia.setVisibility(View.GONE);
////            but_More.setVisibility(View.GONE);
//            }
//        }
        if (SharedUtil.getString("type") != null) {
            if (SharedUtil.getString("type").equals("4")) {
                if (SharedUtil.getString("cost_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("cost_disable"))) {
                        tv_jinjia.setVisibility(View.GONE);
                    }
                }
                if (SharedUtil.getString("store_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("store_disable"))) {
                        tv_store.setVisibility(View.GONE);
                    }
                }
                if (SharedUtil.getString("profit_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("profit_disable"))) {
                        tv_lirunli.setVisibility(View.GONE);
                    }
                }
            }
        }
        mapList = new ArrayList<>();

        commodities = new ArrayList<>();
        Datas = new ArrayList<>();
        //快捷栏的实体类
        Kmaidan = new ArrayList<>();

        itmeChecked = new ArrayList<>();

//        商品小计和合计
        tv_Totalmerchandise = (TextView) findViewById(R.id.tv_Totalmerchandise);
        tv_Total = (TextView) findViewById(R.id.tv_Total);
        SetEditTextInput.judgeNumber(tv_Total);
//        购买商品的数据
        listmaidan = new ArrayList<>();
        //数据库的操作
        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
//        getAdats();

        //条形码工具类
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);

        maidan = new Maidan();
        //登出
        but_exit = (Button) findViewById(R.id.but_exit);
        but_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commodities.size() > 0) {
                    getpopup();
                } else {
                    startActivity(new Intent(MainActivity.this, Handover_activity.class));
//                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
                    finish();
                }
            }
        });

        //换钱
        but_change_money= (Button) findViewById(R.id.but_change_money);
        but_change_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle(getResources().getString(R.string.change_money));
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_change);
                final EditText ed_password= (EditText) window.findViewById(R.id.ed_password);
                Button but_lock= (Button) window.findViewById(R.id.but_lock);
                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                but_lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ed_password.getText().toString()!=null&&!ed_password.getText().toString().equals("")){
                            Commodity commodity = new Commodity();
                            ShuliangEntty shuliang = new ShuliangEntty();
                            commodity.setName(getResources().getString(R.string.change_money));
                            commodity.setPrice(ed_password.getText().toString());
                            commodity.setMember_price(ed_password.getText().toString());
                            shuliang.setNumber(1);
                            commodity.setCost(100 + "");
                            commodity.setStore(200 + "");
                            commodity.setGoods_id("null");
                            commodity.setType(Member_type);
                            commodities.add(commodity);
                            entty.add(shuliang);
                            adapterzhu.getadats(commodities);
                            adapterzhu.getEntty(entty);
                            lv.setAdapter(adapterzhu);
                            dialog.dismiss();
                            tv_Totalmerchandise.setText(entty.size()+"");
                            tv_Total.setText(TlossUtils.add(Double.parseDouble(tv_Total.getText().toString()),Double.parseDouble(ed_password.getText().toString()))+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                            tv_netreceipts.setText(TlossUtils.add(Double.parseDouble(tv_netreceipts.getText().toString()),Double.parseDouble(ed_password.getText().toString()))+"");
                            Showtotal(TlossUtils.add(Double.parseDouble(tv_netreceipts.getText().toString()),Double.parseDouble(ed_password.getText().toString()))+"");
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        //会员
        but_member= (Button) findViewById(R.id.but_member);
        but_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay_type.equals("0")) {
                    dialog_member = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                    dialog_member.setCanceledOnTouchOutside(false);
                    dialog_member.setCancelable(false);
                    dialog_member.show();
                    Window window = dialog_member.getWindow();
                    window.setContentView(R.layout.dialog_memberpaw);
                    dialog_member.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    final EditText editText = (EditText) window.findViewById(R.id.ed_paw);
                    Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
                    Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                    Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                    LinearLayout ll_title= (LinearLayout) window.findViewById(R.id.ll_title);
                    ll_title.setVisibility(View.GONE);
                    final Switch sw_member_price= (Switch) window.findViewById(R.id.sw_member_price);
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
                    if (SharedUtil.getfalseBoolean("sw_member_price")){
                        sw_member_price.setChecked(true);
//                        getwholesale();
                    }else {
                        sw_member_price.setChecked(false);
//                        getretail();
                    }
                    sw_member_price.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(boolean isChecked) {
                            SharedUtil.putBoolean("sw_member_price",isChecked);
//                            if (isChecked){
//                                getwholesale();
//                            }else {
////                                getretail();
//                            }
                        }
                    });
                    lv_member= (ListView) window.findViewById(R.id.lv_member);
                    adapter_optimize=new Adapter_optimize(MainActivity.this);
                    adapter_optimize.OnClickListener(MainActivity.this);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            /**
                             *会员优化
                             */
                            if (editable.toString().length()==15){
                                getsearch_members(editable.toString());
                            }else if (editable.toString().length()==6){
                                if (StringUtils.isCard(editable.toString())){
                                    getsearch_members(editable.toString());
                                }
                            }
                        }
                    });


                    but_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getretail();
                            dialog_member.dismiss();
                        }
                    });

                    but_dimdis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**获取输入法打开的状态**/
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            boolean isOpen = imm.isActive();
                            //isOpen若返回true，则表示输入法打开，反之则关闭。
                            if (isOpen) {
                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            getsearch_members(editText.getText().toString());
                        }
                    });

                    but_qixiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**获取输入法打开的状态**/
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            boolean isOpen = imm.isActive();
//                            if (isOpen) {
//                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                            }
                            Intent intent=new Intent(MainActivity.this,Member_Activity.class);
                            startActivity(intent);
                            dialog_member.dismiss();
                        }
                    });

                }
            }
        });
        //圆形按钮的获取
        but_credit = (Button) findViewById(R.id.but_credit);
        but_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commodities.size() > 0) {


                    List<Map<String ,String>> listmap1=new ArrayList<>();
                    for (int j=0;j<commodities.size();j++){
                        Map<String,String> map=new HashMap<>();
                        map.put("goods_id",commodities.get(j).getGoods_id());
                        map.put("num",entty.get(j).getNumber()+"");
                        map.put("name",commodities.get(j).getPrice()+"");
                        listmap1.add(map);
                    }
                    Gson gson=new Gson();
                    String s = gson.toJson(listmap1);
                    order1 = "2"+ DateUtils.getTime().substring(1,10) + RandomUtils.gettowrandom();
                    int to=0;
                    for (int k=0;k<order1.length();k++){
                        if (k%2==0){
                            to=to+Integer.parseInt(order1.substring(k,k+1));
                        }else {
                            to=to+3*Integer.parseInt(order1.substring(k,k+1));
                        }
                    }
                    if (to%10==0){
                        order1=order1+"0";
                    }else {
                        order1=order1+(10-to%10);
                    }
                    Intent intent=new Intent(MainActivity.this,Addgoodgs_Activity.class);
                    Commodity commodity=new Commodity();
                    commodity.setGoods_id("null");
                    commodity.setBncode(order1);
                    commodity.setPrice(tv_netreceipts.getText().toString());
                    commodity.setIntro(s);
                    intent.putExtra("commodity",commodity);
                    startActivity(intent);

//                    if (SharedUtil.getString("type") != null) {
//                        if (SharedUtil.getString("type").equals("4")) {
//                            Toast.makeText(MainActivity.this, "你还没有这个权限", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (SharedUtil.getString("operational").equals("0")) {
//                                LayoutInflater inflater = getLayoutInflater();
//                                View layout = inflater.inflate(R.layout.dialog,
//                                        (ViewGroup) findViewById(R.id.dialog));
//                                final EditText editText = (EditText) layout.findViewById(R.id.ed_mark_text);
//                                new AlertDialog.Builder(MainActivity.this).setTitle("备注").setView(layout)
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
////                                                creditupadats(editText.getText().toString());
//                                                List<Map<String ,String>> listmap1=new ArrayList<>();
//                                                for (int j=0;j<commodities.size();j++){
//                                                    Map<String,String> map=new HashMap<>();
//                                                    map.put("goods_id",commodities.get(j).getGoods_id());
//                                                    map.put("num",entty.get(j).getNumber()+"");
//                                                    map.put("name",commodities.get(j).getPrice()+"");
//                                                    listmap1.add(map);
//                                                }
//                                                Gson gson=new Gson();
//                                                String s = gson.toJson(listmap1);
//                                                Addtemporary(editText.getText().toString(),s);
//                                            }
//                                        })
//                                        .setNegativeButton("取消", null).show();
//                            }
//                        }
//                    }
                }else {
                    startActivity(new Intent(MainActivity.this,Temporary_GoodsActivity.class));
                }
            }
        });

//        挂单
        but_Guadan = (Button) findViewById(R.id.but_Guadan);
        but_Guadan.performClick();
        but_Guadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedUtil.getString("operational").equals("0")) {
                    if(commodities.size()>0){
                        if(commodities.get(0).getName().equals(getResources().getString(R.string.cash_prize))){
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.can_not_put_order), Toast.LENGTH_SHORT).show();
                        }else {
                            if (isFastClick()) {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.too_frequent), Toast.LENGTH_SHORT).show();
                            } else {
//                                Pendingupadats();
                                Localguadan();
                            }
                        }
                    }
                }
//                getordernum();
            }
        });

        tv_jiegua = (TextView) findViewById(R.id.tv_jiegua);
        //作废按钮
        tv_cancellation = (TextView) findViewById(R.id.tv_cancellation);
        tv_cancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_jiegua.setText(getResources().getString(R.string.out_order));
                Intent intent = new Intent();
                intent.setAction("com.yzx.cancellation");
                sendBroadcast(intent);
            }
        });

        //取单
        fl_single = (FrameLayout) findViewById(R.id.fl_single);
        rb_order = (RadioButton) findViewById(R.id.rb_order);
        rb_credit = (RadioButton) findViewById(R.id.rb_credit);

        //取单
        rb_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_single, new Order_fragment(false))
                        .commit();
//                tv_cancellation.setVisibility(View.VISIBLE);
            }
        });
        //赊账
        rb_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_single, new Order_fragment(true))
                        .commit();
//                tv_cancellation.setVisibility(View.GONE);
            }
        });
        rl_quick = (RelativeLayout) findViewById(R.id.rl_quick);
        rl_jishuang = (RelativeLayout) findViewById(R.id.rl_jishuang);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        but_cc_quick = (Button) findViewById(R.id.but_cc_quick);
        but_cc_credit = (Button) findViewById(R.id.but_cc_credit);
        but_Takeasingle = (Button) findViewById(R.id.but_Takeasingle);
        but_Takeasingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getordernum();
                if (SharedUtil.getString("operational").equals("0")) {
                    if (commodities.size() > 0) {
                        getpopup();
                    } else {
                        tv_jiegua.setText(getResources().getString(R.string.put_order));
                        rb_order.performClick();
                        rl_quick.setVisibility(View.GONE);
                        rl_jishuang.setVisibility(View.GONE);
                        ll_check.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        im_tuichu = (ImageView) findViewById(R.id.im_tuichu);
        im_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_quick.setVisibility(View.VISIBLE);
                rl_jishuang.setVisibility(View.VISIBLE);
                ll_check.setVisibility(View.GONE);
                if(cancelled.size()>0){
                    cancelled.clear();
                    cancelledenyyt.clear();
                    tv_netreceipts.setText(0 + "");
                    Showtotal(0 + "");
                    tv_Totalmerchandise.setText(0+ "");
                    tv_Total.setText(StringUtils.stringpointtwo(0 + ""));
                    adapterzhu.getadats(cancelled);
                    adapterzhu.getEntty(cancelledenyyt);
                    lv.setAdapter(adapterzhu);
                    adapterzhu.notifyDataSetChanged();
                }
            }

        });


//        查单的点击事件
        but_Check = (Button) findViewById(R.id.but_Check);
        but_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (commodities.size() > 0) {
//                    getpopup();
//                } else {
                showDialog();

                /**
                 *
                 * 查单
                 *
                 */
//                    startActivity(new Intent(MainActivity.this, Check_Activity.class));
            }
//            }
        });

        //锁屏
        but_Lockscreen = (ImageView) findViewById(R.id.but_Lockscreen);
        //自定义Dialog
//        LayoutInflater inflater = getLayoutInflater();
        //获取自定义布局
        //锁屏的按钮
        but_Lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View layout = inflater.inflate(R.layout.dialog_lockscreen, null);
                final EditText ed_password = (EditText) layout.findViewById(R.id.ed_password);
                ed_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                final TextView ed_text = (TextView) layout.findViewById(R.id.ed_user);
                ed_text.setText(SharedUtil.getString("seller_name"));
                final Button but_lock = (Button) layout.findViewById(R.id.but_lock);
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle(getResources().getString(R.string.lock_screen));
                dialog.show();
                dialog.getWindow().setContentView(layout);

                but_lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ed_password.getText().toString().equals(SharedUtil.getString("password"))) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        //兑奖
        but_redeem = (Button) findViewById(R.id.but_redeem);
        but_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Setredeem();
            }
        });


//        打开钱箱
        but_cc_Cashbox = (ImageView) findViewById(R.id.but_cc_Cashbox);
        but_cc_Cashbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (SysUtils.getSystemModel().equals("rk3288")){
                        SysUtils.OpennewCashbox(MainActivity.this);
                    }else {
                        SysUtils.OpenCashbox();
                    }
                    PrintWired.operCash(MainActivity.this);
                    stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                    stringMap.put("work_name", SharedUtil.getString("name"));
                    stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                    stringMap.put("operate_type",getResources().getString(R.string.cash_box_operation));
                    stringcontext = getResources().getString(R.string.open_cash_box);
                    stringMap.put("content", stringcontext);
                    Gson gson = new Gson();
                    String ing = gson.toJson(stringMap);
                    getsensitivity(ing);
                } catch (UnsatisfiedLinkError e) {
                    e.printStackTrace();
                }
            }
        });

        but_Print = (ImageView) findViewById(R.id.but_Print);

        but_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);      //��������һ����Ļ
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        });

        but_Delete = (Button) findViewById(R.id.but_Delete);
        //消费单
        lv = (ListView) findViewById(R.id.lv);
        adapterzhu = new listadapterzhu(this);

        adapterzhu.setOnremove(this);

        //快捷栏，煮物栏
        but_Quick = (RadioButton) findViewById(R.id.but_Quick);
        but_Cook = (RadioButton) findViewById(R.id.but_Cook);
//        but_Quick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                but_Quick.setBackgroundResource(R.drawable.kuaijie2);
//                but_Cook.setBackgroundResource(R.drawable.chuwu);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.Rl_quick, new Quick_fragment())
//                        .commitAllowingStateLoss();
//            }
//        });
        but_Quick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    but_Quick.setBackgroundResource(R.drawable.kuaijie2);
                    but_Cook.setBackgroundResource(R.drawable.chuwu);
//                    Fragment fragment = (Fragment) fragmentPagerAdapter.instantiateItem(Rl_quick, compoundButton.getId());
//                    switchFrament(mcontext,quick_fragment);

                    if (quick_fragment==null){
                        quick_fragment = new Quick_fragment();
                    }
                    addFragment(quick_fragment);
                    showFragment1( quick_fragment);
//                    showFragment(R.id.Rl_quick,quick_fragment);

//                    fragmentPagerAdapter.setPrimaryItem(Rl_quick, 0, fragment);
//                    fragmentPagerAdapter.finishUpdate(Rl_quick);
                }
            }
        });

//        but_Cook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                but_Quick.setBackgroundResource(R.drawable.chuwu);
//                but_Cook.setBackgroundResource(R.drawable.kuaijie2);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.Rl_quick, quick_fragment)
//                        .commitAllowingStateLoss();
//            }
//        });
        but_Cook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {


//                    startActivity(new Intent(MainActivity.this,Weight_activity.class));

                    but_Quick.setBackgroundResource(R.drawable.chuwu);
                    but_Cook.setBackgroundResource(R.drawable.kuaijie2);
//                    switchFrament(mcontext,cook_fragment);

                    if (cook_fragment==null){
                        cook_fragment = new Cook_fragment();
                    }
                    addFragment(cook_fragment);
                    showFragment1( cook_fragment);

//                    showFragment(R.id.Rl_quick,cook_fragment);

//                    Fragment fragment= (Fragment) fragmentPagerAdapter.instantiateItem(Rl_quick,compoundButton.getId());
//                    fragmentPagerAdapter.setPrimaryItem(Rl_quick, 1, fragment);
//                    fragmentPagerAdapter.finishUpdate(Rl_quick);
                }
            }
        });


        //线程
        new Thread(new ThreadShow()).start();

        //跳转到商品管理

        but_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (commodities.size() > 0) {
                    getpopup();
                } else {
                    startActivity(new Intent(MainActivity.this, Commoditymanagement_Activity.class));
                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
//     finish();
                }

            }
        });

        //选中背景框颜色
        keyboard_et_layout = (RelativeLayout) findViewById(R.id.keyboard_et_layout);
        keyboard_tv_layout = (RelativeLayout) findViewById(R.id.keyboard_tv_layout);

        //时间
        tv_Time = (TextView) findViewById(R.id.tv_time);

        keyboard_et_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSelected) {
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
                }
            }
        });
        keyboard_tv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected) {
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
                }
            }
        });


        //移动
        tv_yidong = (TextView) findViewById(R.id.tv_yidong);
//        tv_status = (TextView) findViewById(R.id.tv_status);


//        现金支付的页面数据
        tv_xianjin_netreceipt = (TextView) findViewById(R.id.tv_xianjin_netreceipt);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_change = (TextView) findViewById(R.id.tv_change);

        tv_payment = (TextView) findViewById(R.id.tv_payment);
        tv_danhao = (TextView) findViewById(R.id.tv_danhao);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_modes = (TextView) findViewById(R.id.tv_modes);


        tv_Surplus = (EditText) findViewById(R.id.tv_Surplus);
        SetEditTextInput.setPricePoint(tv_Surplus);
        tv_Surplus.setInputType(InputType.TYPE_NULL);
        tv_netreceipts = (TextView) findViewById(R.id.tv_netreceipts);
        SetEditTextInput.judgeNumber(tv_netreceipts);
        //edittext不弹出虚拟键盘
        et_keyoard = (EditText) findViewById(R.id.et_keyoard);
        et_keyoard.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(et_keyoard);
        et_inputscancode = (EditText) findViewById(R.id.et_inputscancode);
        et_inputscancode.setInputType(InputType.TYPE_NULL);

        tv_netreceipts.setInputType(InputType.TYPE_NULL);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    for (int j = 0; j < 5; j++) {
                        if (jindu == 1) {
                            jindu = 0;
                        } else {
                            pressed.setProgress((float) jindu);
                            jindu = TlossUtils.add(jindu, 0.01);
                        }
                    }
                    tv_Time.setText(getTime());
//                    if (SysUtils.isWifiConnected(MainActivity.this)) {
////                    if (SysUtils.isNetworkOnline()) {
//                        isnetwork = true;
//                        tv_internet.setVisibility(View.GONE);
//                    } else {
//                        tv_internet.setVisibility(View.VISIBLE);
//                        isnetwork = false;
//                    }
                    if (!tv_netreceipts.getText().toString().equals("") && !et_inputscancode.getText().toString().equals("") && frequency > 1) {
                        float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
                        float f2 = Float.parseFloat(et_inputscancode.getText().toString());
                        float f3 = f2 - f1;
                        if (f3 >= 0) {
                            tv_Surplus.setText(f3 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    } else if (!tv_netreceipts.getText().toString().equals("") && et_inputscancode.getText().toString().equals("") && frequency > 1) {
//                        tv_Surplus.setText("-" + Float.parseFloat(tv_netreceipts.getText().toString()));
                    } else if (tv_netreceipts.getText().toString().equals("") && !et_inputscancode.getText().toString().equals("") && frequency > 1) {
                        tv_Surplus.setText(Float.parseFloat(et_inputscancode.getText().toString()) + "");
                    } else {
                        if (frequency == 1) {
                            tv_Surplus.setText("0.00");
                        }
                    }
                }

            }
        };

//        handler2 = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == 3) {
////                    if (!issucceed) {
////
////                    } else {
//////                        Log.e("wwwww", "order_id"+ SharedUtil.getString("order_id"));
////                        getpay();
////                    }
//                }
//            }
//        };


        //切换屏幕的监听
//        findViewById(R.id.but1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        //付款成功的点击事件
        findViewById(R.id.but_zhifusucc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handlernew!=null){
                    handlernew.removeCallbacks(runnablenew);
                }
                stopService(new Intent(MainActivity.this,NetWorkService.class));
                issucceed = false;
                Intent mIntent = new Intent();
                mIntent.setAction("poiu");
                mIntent.putExtra("yaner", "hhhhhh");
                //发送广播  
                sendBroadcast(mIntent);

                ll_jshuang.setVisibility(View.VISIBLE);
                Rl_time.setVisibility(View.GONE);
                Rl_xianjin.setVisibility(View.GONE);
                Rl_yidong.setVisibility(View.GONE);
                type = 0;
                operational = 0;
                SharedUtil.putString("operational", operational + "");
            }
        });



        //现金支付的取消按钮
        but_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cashbox_switch=true;
                Rl_xianjin.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.VISIBLE);
                operational = 0;

                iscash = false;

                but_xianjin.setText(getResources().getString(R.string.confirm));

                Intent Intent1 = new Intent();
                Intent1.setAction("com.yzx.fupingxianjing");
                sendBroadcast(Intent1);

            }
        });

//        现金支付
        but_Cashbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboard_et_layout.performClick();
                if (!tv_netreceipts.getText().toString().equals("") && !et_inputscancode.getText().toString().equals("") && Float.parseFloat(tv_Surplus.getText().toString()) >= 0) {
                    if (TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) >= 0 && Double.parseDouble(et_inputscancode.getText().toString()) > 0 && Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
                        if (SharedUtil.getString("ed_discount")!=null&&!SharedUtil.getString("ed_discount").equals("")){
                            setDiscount(SharedUtil.getString("ed_discount"));
                        }
                        Cashbox_switch=false;

                        PrintWired.operCash(MainActivity.this);

                        if (SysUtils.getSystemModel().equals("rk3288")){
                            SysUtils.OpennewCashbox(MainActivity.this);
                        }else {
                            SysUtils.OpenCashbox();
                        }
                        ll_jshuang.setVisibility(View.GONE);
                        Rl_time.setVisibility(View.GONE);
                        Rl_xianjin.setVisibility(View.VISIBLE);
//                        Log.e("print","显示的金额"+tv_netreceipts.getText().toString());
//                        Log.e("print","显示的金额"+et_inputscancode.getText().toString());
//                        Log.e("print","显示的金额"+tv_Surplus.getText().toString());
                        tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
                        tv_amount.setText(et_inputscancode.getText().toString());
                        tv_change.setText(TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) + "");

                        cash_entty.setAmount(et_inputscancode.getText().toString());
                        cash_entty.setChange(tv_Surplus.getText().toString());
                        cash_entty.setNetreceipt(tv_netreceipts.getText().toString());

                        Intent Intent1 = new Intent();
                        Intent1.setAction("com.yzx.fupingxianjing");
                        Intent1.putExtra("cash_entty",cash_entty);
                        sendBroadcast(Intent1);

                        operational = 1;

                        //挂单数量的获取

                        getordernum();
//                        tv_xianjin_netreceipt.setText(cash_entty.getNetreceipt());
//                        tv_amount.setText(cash_entty.getAmount());
//                        tv_change.setText(cash_entty.getChange());
                    }
                }
//                if (number != 0) {
//                    //广播的发送
//                    Intent mIntent = new Intent();
//                    mIntent.setAction("qwer");
//                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                    //发送广播  
//                    sendBroadcast(mIntent);
//                    fuxiang = false;
////                    findViewById(R.id.Rl_xianjin).setVisibility(View.VISIBLE);
//                    number = 0;
//                }
            }
        });
        //现金支付确定
        but_xianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isnetworknew){
//                if(SysUtils.isNetworkAvailable(getApplication())){
//                if (SysUtils.isNetworkOnline()){
                    if (!iscash){
                        if (but_xianjin.getText().toString().equals(getResources().getString(R.string.confirm))&&!iscash){
                            iscash=true;
                            upcash();
                        }
                    }else {
                        iscash=false;
                    }
                    Cashbox_switch=true;
                }else {
                    Localorder();
                }
            }
        });
        //一秒后关闭
        findViewById(R.id.but_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent Intent1 = new Intent();
//                Intent1.setAction("com.yzx.clear");
//                sendBroadcast(Intent1);
                Rl_time.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.VISIBLE);
            }
        });

        //移动支付
        findViewById(R.id.but_mobilepayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isnetworknew) {
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.use_other_payment_methods),Toast.LENGTH_SHORT).show();
                }else {
                    startService(new Intent(MainActivity.this, NetWorkService.class));
//                Log.e("print", "operational3" + operational);
//                Log.d("print", "点击了移动支付" + commodities.size());
                    im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.load));
                    issucceed = true;
                    keyboard_tv_layout.performClick();
//                if (SysUtils.isWifiConnected(getApplication())) {
                    if (isnetworknew) {
//                if (SysUtils.isNetworkAvailable(getApplication())) {
//                if (SysUtils.isNetworkOnline()) {
                        //挂单数量的获取
                        getordernum();
                        but_internet.setVisibility(View.INVISIBLE);
                        if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
                            //全场折扣
                            if (SharedUtil.getString("ed_discount")!=null&&!SharedUtil.getString("ed_discount").equals("")){
                                setDiscount(SharedUtil.getString("ed_discount"));
                            }

                            type = 1;
                            if (!tv_netreceipts.getText().toString().isEmpty() && Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
                                tv_yidong.setText(tv_netreceipts.getText().toString());
                            }

                            ll_jshuang.setVisibility(View.GONE);
                            Rl_yidong.setVisibility(View.VISIBLE);

                            pendingupyidong();
                            //线程
//                        new Thread(new ThreadTimes()).start();
                            handlernew = new Handler();
                            runnablenew = new Runnable() {
                                @Override
                                public void run() {
                                    getpay();
                                    if (isNetBad) {
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.current_network_is_poor), Toast.LENGTH_SHORT).show();
                                    }
                                    handlernew.postDelayed(this, 3000);
                                }
                            };
                            handlernew.postDelayed(runnablenew, 3000);

                            /**
                             * 先不要这个广播
                             * 这是消费列表栏的广播
                             */
                            /** //广播的发送
                             Intent mIntent = new Intent();
                             mIntent.setAction("qwer");
                             mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                             //发送广播  
                             sendBroadcast(mIntent);
                             */
//                Intent mIntent1 = new Intent();
//                mIntent1.setAction("poiu");
//                mIntent1.putExtra("yaner", "http://www.czxshop.net//public//images//e8//ad//98//e4dc6c89b6d17861d157e9ee38b1e3539e11bf83.png");
//                mIntent1.putExtra("jinger", tv_netreceipts.getText().toString());
//                sendBroadcast(mIntent1);
                        }
                    } else {
                        im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.load));
                        tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
                        tv_amount.setText(et_inputscancode.getText().toString());
                        tv_change.setText(tv_Surplus.getText().toString());
                        String order = DateUtils.getorder() + RandomUtils.getrandom();
                        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
                        sqlite_entity.insertOrder1(order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), "0", "micro",commodities,entty,true);
                        //商品写入数据库方法
//                        sqLiteDatabase.execSQL("insert into ProOut (cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)values(?,?,?,?,?,?,?)", new Object[]{order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), "0", "micro"});
//                        for (int i = 0; i < commodities.size(); i++) {
//                            String price = "";
//                            String amount = "";
//                            if (iswholesale) {
//                                price = commodities.get(i).getPrice();
//                                amount = Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber() + "";
//                            } else {
//                                price = commodities.get(i).getMember_price();
//                                amount = Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber() + "";
//                            }
//                            sqLiteDatabase.execSQL("insert into goodsSell (goods_id,name,number,py,price,cost,amount,product_id,bncode,store," +
//                                    "good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
//                                    commodities.get(i).getGoods_id(), commodities.get(i).getName(),
//                                    entty.get(i).getNumber(), commodities.get(i).getPy(), price,
//                                    commodities.get(i).getCost(), amount,
//                                    commodities.get(i).getProduct_id(), commodities.get(i).getBncode(), commodities.get(i).getStore(),
//                                    commodities.get(i).getGood_limit(), commodities.get(i).getGood_stock(),
//                                    commodities.get(i).getPD(), commodities.get(i).getGD(), commodities.get(i).getMarketable(),
//                                    commodities.get(i).getTag_name(), commodities.get(i).getTag_id(), commodities.get(i).getUnit(),
//                                    commodities.get(i).getUnit_id(), order, commodities.get(i).getBn()});
//                        }
                        but_internet.setVisibility(View.VISIBLE);
                        ll_jshuang.setVisibility(View.GONE);
                        Rl_yidong.setVisibility(View.VISIBLE);
                        tv_yidong.setText(tv_netreceipts.getText().toString());
                        Intent mIntent1 = new Intent();
                        mIntent1.setAction("poiu");
                        mIntent1.putExtra("yaner", " ");
                        //设置二维码
                        type = 0;
                        operational = 0;
                        SharedUtil.putString("operational", operational + "");
//                        createChineseQRCode("");
                        SharedUtil.getString("url");
                        if (SharedUtil.getString("url") != null && !SharedUtil.getString("url").equals("")) {
                            im_code.setImageBitmap(QRCode.createQRCode(SharedUtil.getString("url")));
                        }

                        mIntent1.putExtra("jinger", tv_netreceipts.getText().toString());
                        sendBroadcast(mIntent1);
                        but_internet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("mobile_pay", mobile_pay);
                                sendBroadcast(mIntent1);
                                tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                                tv_modes.setText(getResources().getString(R.string.mobile_type));

                                SharedUtil.putString("order_id", "");
                                Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
                                sqlite_entity.insertStock1(commodities,entty);
//                                for (int r = 0; r < commodities.size(); r++) {
//                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
//                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
//                                    while (cursor.moveToNext()) {
//                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
//                                        String newnums = (Double.parseDouble(nums) - entty.get(r).getNumber()) + "";
//                                        ContentValues values = new ContentValues();
//                                        values.put("store", newnums);
//                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
//                                    }
//                                }
                                mapList.clear();
                                commodities.clear();
                                entty.clear();

                                _Total="0";
                                Member_type="0";
//                            for (int j = 0; j < entty.size(); j++) {
//                                entty.get(j).setNumber(1);
//                            }
                                adapterzhu.notifyDataSetChanged();
                                tv_Totalmerchandise.setText(0 + "");
                                tv_Total.setText(0.0 + "");
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_inputscancode.setText(0 + "");
                                et_keyoard.setText(0 + "");
                                keyboard_tv_layout.performClick();
                                Rl_yidong.setVisibility(View.GONE);
                                ll_jshuang.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);
                                new Thread(new ThreadTime()).start();
                                handler1 = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == 2) {
//                                tv_status.setText("正在获取客户支付状态...");
                                            ll_jshuang.setVisibility(View.VISIBLE);
                                            Rl_time.setVisibility(View.GONE);
                                            Rl_xianjin.setVisibility(View.GONE);
                                            Rl_yidong.setVisibility(View.GONE);
                                        }
                                        //小圆点消失
                                        Intent Intent1 = new Intent();
                                        Intent1.setAction("com.yzx.clear");
                                        sendBroadcast(Intent1);
                                    }
                                };
                            }
                        });
                    }
                }
            }

        });

//        //订单详情
//        findViewById(R.id.but2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //广播的发送
//                Intent mIntent = new Intent();
//                mIntent.setAction("qwer");
//                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                //发送广播  
//                sendBroadcast(mIntent);
//            }
//        });
        //二维码
//        findViewById(R.id.but3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //弹出二维码的广播
//                Intent mIntent = new Intent();
//                mIntent.setAction("poiu");
//                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                //发送广播                  sendBroadcast(mIntent);
//            }
//        });
        //取消
        tv_cancel = (FButton) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_netreceipts.setText("0.0");
                Showtotal("0.0");
                et_inputscancode.setText("0.0");
                et_keyoard.setText("");
                tv_Total.setText("0.0");
                tv_Totalmerchandise.setText("0");
                keyboard_tv_layout.performClick();

                if (handlernew!=null){
                    handlernew.removeCallbacks(runnablenew);
                }

                reduce=false;
                discount=false;

                discount_type="reduce";
                num="0";
                discount_goods_id="";

                stringcontext=getResources().getString(R.string.removed);
                for (int l=0;l<commodities.size();l++){
                    if (l!=commodities.size()-1){
                        stringcontext+=commodities.get(l).getName()+",";
                    }else {
                        stringcontext+=commodities.get(l).getName();
                    }
                }
                stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                stringMap.put("work_name", SharedUtil.getString("name"));
                stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                stringMap.put("operate_type", getResources().getString(R.string.remove));
                stringMap.put("content", stringcontext);
                Gson gson = new Gson();
                String ing = gson.toJson(stringMap);
                getsensitivity(ing);

                commodities.clear();
                entty.clear();
                if (Entty!=null){
                    for (int l=0;l<Entty.size();l++){
                        Entty.get(l).setChecked(false);
                    }
                }

                _Total="0";
                pay_type="0";
                adapterzhu.setType(false);

                Member_type="0";

                pay_score="";
                is_score_pay="no";

                reduce=false;
                discount=false;

                discount_type="reduce";
                num="0";
                discount_goods_id="";

                operational = 0;
                SharedUtil.putString("operational", operational + "");
                Intent Intent1 = new Intent();
                Intent1.setAction("com.yzx.clear");
                sendBroadcast(Intent1);
                adapterzhu.notifyDataSetChanged();
            }
        });
    }

    /**
     * 会员价
     */
    public void getmemprice(boolean isw){
//        if(!isw){
//            iswholesale=true;
//            but_share_tools.setText("零售");
//            getretail();
//        }else {
//            iswholesale=false;
//            but_share_tools.setText("批发");
//            getwholesale("");
//        }
//        Intent intent2=new Intent();
//        intent2.setAction("com.yzx.iswholesale");
//        intent2.putExtra("iswholesale", iswholesale);
//        sendBroadcast(intent2);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.member_lv);
        EditText tv_lv0= (EditText) window.findViewById(R.id.tv_lv0);
        EditText tv_lv1= (EditText) window.findViewById(R.id.tv_lv1);
        EditText tv_lv2= (EditText) window.findViewById(R.id.tv_lv2);
        EditText tv_lv3= (EditText) window.findViewById(R.id.tv_lv3);
        EditText tv_lv4= (EditText) window.findViewById(R.id.tv_lv4);
        EditText tv_lv5= (EditText) window.findViewById(R.id.tv_lv5);

        tv_lv0.setFocusable(false);
        tv_lv0.setFocusableInTouchMode(false);
        tv_lv1.setFocusable(false);
        tv_lv1.setFocusableInTouchMode(false);
        tv_lv2.setFocusable(false);
        tv_lv2.setFocusableInTouchMode(false);
        tv_lv3.setFocusable(false);
        tv_lv3.setFocusableInTouchMode(false);
        tv_lv4.setFocusable(false);
        tv_lv4.setFocusableInTouchMode(false);
        tv_lv5.setFocusable(false);
        tv_lv5.setFocusableInTouchMode(false);

        tv_lv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="0";
                setType(Member_type,true);
                getwholesale("0",true,"0","0");
                dialog.dismiss();
            }
        });

        tv_lv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="1";
                setType(Member_type,true);
                getwholesale("1",true,"0","0");
                dialog.dismiss();
            }
        });
        tv_lv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="2";
                setType(Member_type,true);
                getwholesale("2",true,"0","0");
                dialog.dismiss();
            }
        });
        tv_lv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="3";
                setType(Member_type,true);
                getwholesale("3",true,"0","0");
                dialog.dismiss();
            }
        });
        tv_lv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="4";
                setType(Member_type,true);
                getwholesale("4",true,"0","0");
                dialog.dismiss();
            }
        });
        tv_lv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member_type="5";
                setType(Member_type,true);
                getwholesale("5",true,"0","0");
                dialog.dismiss();
            }
        });
    }



    /**
     * 零售价格的切换
     */
    public void getretail(){
        if (commodities.size()>0){
            adapterzhu.setType(false);
            adapterzhu.notifyDataSetChanged();

            float nums=0;
            double totalMember=0;
            for (int k=0;k<commodities.size();k++){
                nums=nums+entty.get(k).getNumber();
                totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
            }
            tv_Totalmerchandise.setText(nums+"");
            tv_Total.setText(totalMember+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
            tv_netreceipts.setText(totalMember+"");
            Showtotal(totalMember+"");
        }else {
            adapterzhu.setType(false);
        }
    }

    /**
     * 会员价的切换
     */
    public void getwholesale(String type,boolean ismember,String str,String subscript){
        if (commodities.size()>0){
            adapterzhu.notifyDataSetChanged();
            float nums=0;
            double totalMember=0;
            for (int k=0;k<commodities.size();k++){
                nums=nums+entty.get(k).getNumber();
                if (commodities.get(k).getType()!=null&&!commodities.get(k).getType().equals("")&&!commodities.get(k).getType().equals("0")) {
                    if (commodities.get(k).getIs_special_offer()!=null){
                    if (commodities.get(k).getIs_special_offer().equals("no")) {
                        if (ismember) {
                        if (commodities.get(k).getCustom_member_price() != null && !commodities.get(k).getCustom_member_price().equals("")&& !commodities.get(k).getCustom_member_price().equals("null")) {
                            Log.d("print","打印商品的等级会员价"+commodities.get(k).getCustom_member_price());
                            if (!StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1]), Double.parseDouble(entty.get(k).getNumber() + "")));
                            }
                        } else {
                            if (!commodities.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
                            }
                        }
                    }else {
                            if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                            }
                        }
                    } else {
                        if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                        }
                    }
                }else {
                        if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                        }
                    }
                }else {
                    if (!commodities.get(k).getPrice().equals("")&&!(entty.get(k).getNumber()+"").equals("")){
                        totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                    }
                }
            }
            tv_Totalmerchandise.setText(nums+"");
            tv_Total.setText(totalMember+"");
            tv_netreceipts.setText(totalMember+"");
            Showtotal(totalMember+"");
            if (ismember){
                Intent intent2=new Intent();
                intent2.setAction("com.yzx.iswholesale");
                intent2.putExtra("type",str);
                intent2.putExtra("iswholesale", type);
                intent2.putExtra("subscript",subscript);
                sendBroadcast(intent2);
            }
        }else {
            adapterzhu.setType(false);
        }
    }



    public void setType(String type,boolean istype){
        for (int i1=0;i1<commodities.size();i1++){
            commodities.get(i1).setType(type);
        }
        adapterzhu.setType(istype);
        adapterzhu.notifyDataSetChanged();
    }

    /**
     * 本地订单的接口
     */
    public void Localorder(){
//无网得现金支付
        tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
        tv_amount.setText(et_inputscancode.getText().toString());
        tv_change.setText(tv_Surplus.getText().toString());
        String order = DateUtils.getorder() + RandomUtils.getrandom();


        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        sqlite_entity.insertOrder1(order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash",commodities,entty,true);

//        //商品写入数据库方法
//        sqLiteDatabase.execSQL("insert into ProOut (cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)values(?,?,?,?,?,?,?)", new Object[]{order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash"});
//        for (int i = 0; i < commodities.size(); i++) {
////            String price="";
////            String amount="";
////            if (iswholesale){
////                price= commodities.get(i).getPrice();
////                amount=Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()+"";
////            }else {
////                price=commodities.get(i).getMember_price();
////                amount=Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()+"";
////            }
//            String price=commodities.get(i).getPrice();
//            double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
//            if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
//                if (commodities.get(i).getIs_special_offer()!=null){
//                    if (commodities.get(i).getIs_special_offer().equals("no")) {
//                        if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")) {
//                            if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
//                                price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
//                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
//                            }
//                        } else {
//                            if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
//                                price=commodities.get(i).getMember_price();
//                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
//                            }
//                        }
//                    }
//                } else {
//                    if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
//                        price=commodities.get(i).getPrice();
//                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
//                    }
//                }
//            }else {
//                if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
//                    price=commodities.get(i).getPrice();
//                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
//                }
//            }
//            sqLiteDatabase.execSQL("insert into goodsSell (goods_id,name,number,py,price,cost,amount,product_id,bncode,store," +
//                    "good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
//                    commodities.get(i).getGoods_id(), commodities.get(i).getName(),
//                    entty.get(i).getNumber(), commodities.get(i).getPy(), price,
//                    commodities.get(i).getCost(), amount,
//                    commodities.get(i).getProduct_id(), commodities.get(i).getBncode(), commodities.get(i).getStore(),
//                    commodities.get(i).getGood_limit(), commodities.get(i).getGood_stock(),
//                    commodities.get(i).getPD(), commodities.get(i).getGD(), commodities.get(i).getMarketable(),
//                    commodities.get(i).getTag_name(), commodities.get(i).getTag_id(), commodities.get(i).getUnit(),
//                    commodities.get(i).getUnit_id(), order, commodities.get(i).getBn()});
//        }

        cash_entty.setOrder_id(order);
        cash_entty.setNetreceipt(tv_netreceipts.getText().toString());
        cash_entty.setAmount(et_inputscancode.getText().toString());
        cash_entty.setChange(tv_Surplus.getText().toString());
        cash_entty.setPayed_time(DateUtils.getCurDate());
        cash_entty.setSellername(SharedUtil.getString("seller_name"));

        ll_jshuang.setVisibility(View.GONE);
        Rl_time.setVisibility(View.GONE);
        Rl_xianjin.setVisibility(View.VISIBLE);

        if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
            PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
            printUtil1.openButtonClicked();

            String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, entty,
                    2, Double.parseDouble(cash_entty.getNetreceipt()),"", cash_entty.getAmount(), cash_entty.getNetreceipt(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);
            if (PrintWired.usbPrint(MainActivity.this,syy)){
            }else {
                printUtil1.printstring(syy);
                mService.sendMessage(syy, "GBK");
            }
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("cash_entty",cash_entty);
        mIntent.setAction("com.yzx.fupingxianjing");

        //发送广播  
        sendBroadcast(mIntent);
        Cashbox_switch=true;

        type = 0;
        operational = 0;
        SharedUtil.putString("operational", operational + "");
//                            if (commodities.size() > 0) {
//                                Intent Intent1 = new Intent();
//                                Intent1.setAction("com.yzx.clear");
//                                sendBroadcast(Intent1);
//                            }
        adapterzhu.notifyDataSetChanged();
        tv_Totalmerchandise.setText(0 + "");
        tv_Total.setText(0.0 + "");
        tv_netreceipts.setText(0 + "");
        Showtotal(0 + "");
        et_inputscancode.setText(0 + "");
        et_keyoard.setText(0 + "");
        keyboard_tv_layout.performClick();
        tv_payment.setText(cash_entty.getNetreceipt());
        tv_danhao.setText(cash_entty.getOrder_id());
        tv_day.setText(cash_entty.getPayed_time());

        Rl_xianjin.setVisibility(View.GONE);
        ll_jshuang.setVisibility(View.GONE);
        Rl_time.setVisibility(View.VISIBLE);
        keyboard_tv_layout.performClick();
        mIntent.setAction("com.yzx.determination");
        mIntent.putExtra("cash_entty", cash_entty);
        //发送广播  
        sendBroadcast(mIntent);

//                    for (int j = 0; j < entty.size(); j++) {
//                        entty.get(j).setNumber(1);
//                    }
        entty.clear();
        commodities.clear();

        _Total="0";

        reduce=false;
        discount=false;

        Member_type="0";

        discount_type="reduce";
        num="0";
        discount_goods_id="";

        if (Entty!=null){
            for (int l=0;l<Entty.size();l++){
                Entty.get(l).setChecked(false);
            }
        }

        Rl_time.setVisibility(View.GONE);
        Rl_xianjin.setVisibility(View.GONE);
        if (Rl_yidong.getVisibility()==View.VISIBLE){

        }else {
            ll_jshuang.setVisibility(View.VISIBLE);
            Rl_yidong.setVisibility(View.GONE);
        }
//                            findViewById(R.id.Rl_xianjin).setVisibility(View.GONE);
//                            findViewById(R.id.ll_jshuang).setVisibility(View.GONE);
//                            findViewById(R.id.Rl_time).setVisibility(View.VISIBLE);
//                            new Thread(new ThreadTime()).start();
//                            handler1 = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    super.handleMessage(msg);
//                                    if (msg.what == 2) {
//                                        findViewById(R.id.ll_jshuang).setVisibility(View.VISIBLE);
//                                        findViewById(R.id.Rl_time).setVisibility(View.GONE);
//                                        findViewById(R.id.Rl_xianjin).setVisibility(View.GONE);
//                                        findViewById(R.id.Rl_yidong).setVisibility(View.GONE);
//                                    }
//                                }
//                            };
        PrintWired.operCash(MainActivity.this);
        if (SysUtils.getSystemModel().equals("rk3288")){
            SysUtils.OpennewCashbox(MainActivity.this);
        }else {
            SysUtils.OpenCashbox();
        }

    }


    //本地的搜索接口
    public void LocalSeek(String str,final ListView lv_fuzzy){
        list_fuzzy.clear();
        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        if (sqlite_entity.LocalSeek(str)!=null) {
            list_fuzzy = sqlite_entity.LocalSeek(str);
            adapter_fuzzy.setAdats(list_fuzzy);
            adapter_fuzzy.SetOnclick(MainActivity.this);
            lv_fuzzy.setAdapter(adapter_fuzzy);
            adapter_fuzzy.notifyDataSetChanged();
        }else {
            getseek(str, lv_fuzzy);
        }
//        Cursor cursor=null;
//        Pattern p = Pattern.compile("[0-9]*");
//        Matcher m = p.matcher(str);
//        if (m.matches()){
//            cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode like " + "'%" + str + "%'", null);
//        }
//        p=Pattern.compile("[a-zA-Z]*");
//        m=p.matcher(str);
//        if(m.matches()){
//            cursor = sqLiteDatabase.rawQuery("select * from commodity where py like " + "'%" + str + "%'", null);
//        }
//        p=Pattern.compile("[\u4e00-\u9fa5]*");
//        m=p.matcher(str);
//        if(m.matches()){
//            cursor = sqLiteDatabase.rawQuery("select * from commodity where name like " + "'%" + str + "%'", null);
//        }
//
//        if (cursor != null) {
//            if (cursor.getCount() == 0) {
//                getseek(str, lv_fuzzy);
//            } else {
//                list_fuzzy.clear();
//                while (cursor.moveToNext()) {
//                    Commodity fuzzy = new Commodity();
//                    fuzzy.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
//                    fuzzy.setName(cursor.getString(cursor.getColumnIndex("name")));
//                    fuzzy.setPy(cursor.getString(cursor.getColumnIndex("py")));
//                    fuzzy.setPrice(cursor.getString(cursor.getColumnIndex("price")));
//                    fuzzy.setCost(cursor.getString(cursor.getColumnIndex("cost")));
//                    fuzzy.setStore(cursor.getString(cursor.getColumnIndex("store")));
//                    fuzzy.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
//                    fuzzy.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
//                    fuzzy.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
//                    fuzzy.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
//                    fuzzy.setCustom_member_price(cursor.getString(cursor.getColumnIndex("custom_member_price")));
//                    fuzzy.setType(Member_type);
//                    list_fuzzy.add(fuzzy);
//                }
//                adapter_fuzzy.setAdats(list_fuzzy);
//                adapter_fuzzy.SetOnclick(MainActivity.this);
//                lv_fuzzy.setAdapter(adapter_fuzzy);
//                adapter_fuzzy.notifyDataSetChanged();
//            }
//        }
    }

    //搜索的接口
    private void getseek(String str, final ListView lv_fuzzy) {
        String name="search";
        if (StringUtils.isNumber1(str)){
            name="bncode";
        }else {
            name="search";
        }
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params(name,str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                list_fuzzy.clear();
                                for (int j = 0; j < ja1.length(); j++) {
                                    Commodity commodity = new Commodity();
                                    JSONObject jo2 = ja1.getJSONObject(j);
                                    commodity.setCustom_member_price(jo2.getString("custom_member_price"));
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setIs_special_offer(jo2.getString("is_special"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    commodity.setType(Member_type);
                                    list_fuzzy.add(commodity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter_fuzzy.setAdats(list_fuzzy);
                            adapter_fuzzy.SetOnclick(MainActivity.this);
                            lv_fuzzy.setAdapter(adapter_fuzzy);
                            adapter_fuzzy.notifyDataSetChanged();
                        }
                    }
                });
    }

    public  void getlocal(String barcode){
        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        List<Commodity> bncode = sqlite_entity.query("bncode", barcode);
        if (bncode!=null){
        for (int g=0;g<bncode.size();g++){
            if (!bncode.get(g).getCook_position().equals("0")){
                getweight(bncode.get(g));
            }else {
                Log.d("print","打印数据的名字"+bncode.get(g).getName());
//                if (iswholesale){
//                    AddCommodity(bncode.get(g),1,bncode.get(g).getPrice());
//                }else {
//                    AddCommodity(bncode.get(g),1,bncode.get(g).getMember_price());
//                }
                if (bncode.get(g).getType()!=null&&!bncode.get(g).getType().equals("")&&!bncode.get(g).getType().equals("0")) {
                    if (bncode.get(g).getIs_special_offer()!=null) {
                        if (bncode.get(g).getIs_special_offer().equals("no")) {
                            if (bncode.get(g).getCustom_member_price() != null && !bncode.get(g).getCustom_member_price().equals("")&& !bncode.get(g).getCustom_member_price().equals("null")) {
                                AddCommodity(bncode.get(g), 1, StringUtils.getStrings(bncode.get(g).getCustom_member_price(), ",")[Integer.parseInt(bncode.get(g).getType()) - 1]);
                            } else {
                                AddCommodity(bncode.get(g), 1, bncode.get(g).getMember_price());
                            }
                        }else {
                            AddCommodity(bncode.get(g), 1, bncode.get(g).getPrice());
                        }
                    }else {
                        AddCommodity(bncode.get(g), 1, bncode.get(g).getPrice());
                    }
                }else {
                    AddCommodity(bncode.get(g),1,bncode.get(g).getPrice());
                }
            }
        }
        }else {
            getSeek(barcode);
        }
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode=?", new String[]{barcode});
//        if (cursor.getCount() != 0) {
//            while (cursor.moveToNext()) {
//                commoditys = new Commodity();
//                commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
//                commoditys.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
//                commoditys.setName(cursor.getString(cursor.getColumnIndex("name")));
//                commoditys.setPy(cursor.getString(cursor.getColumnIndex("py")));
//                commoditys.setPrice(cursor.getString(cursor.getColumnIndex("price")));
//                commoditys.setCost(cursor.getString(cursor.getColumnIndex("cost")));
//                commoditys.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
//                commoditys.setTag_id(cursor.getString(cursor.getColumnIndex("tag_id")));
//                commoditys.setProduct_id(cursor.getString(cursor.getColumnIndex("product_id")));
//                commoditys.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
//                commoditys.setStore(cursor.getString(cursor.getColumnIndex("store")));
//                commoditys.setGood_limit(cursor.getString(cursor.getColumnIndex("good_limit")));
//                commoditys.setGood_stock(cursor.getString(cursor.getColumnIndex("good_stock")));
//                commoditys.setPD(cursor.getString(cursor.getColumnIndex("PD")));
//                commoditys.setGD(cursor.getString(cursor.getColumnIndex("GD")));
//                commoditys.setMarketable(cursor.getString(cursor.getColumnIndex("marketable")));
//                commoditys.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
//                commoditys.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
//                commoditys.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
//                commoditys.setCustom_member_price(cursor.getString(cursor.getColumnIndex("custom_member_price")));
//                commoditys.setType(Member_type);
//            }
//            if (!commoditys.getCook_position().equals("0")) {
//                getweight(commoditys);
//            } else {
//                if (commodities.size() > 0) {
//                    int in = 0;
//                    aa:
//                    for (int k = 0; k < commodities.size(); k++) {
//                        if (commodities.get(k).getGoods_id().equals(commoditys.getGoods_id())) {
//                            in = in + (k + 1);
//                            break aa;
//                        }
//                    }
//                    if (in == 0) {
//                        shuliangEntty = new ShuliangEntty();
//                        shuliangEntty.setNumber(1);
//                        entty.add(shuliangEntty);
//                        commodities.add(commoditys);
//                    } else {
//                        float i = entty.get(in - 1).getNumber();
//                        i++;
//                        entty.get(in - 1).setNumber(i);
////                        listmaidan.get(in - 1).setNumber(i);
//                        adapterzhu.getEntty(entty);
//                        adapterzhu.getadats(commodities);
//
//                        adapterzhu.notifyDataSetChanged();
//                        lv.setAdapter(adapterzhu);
//                        lv.setSelection(adapterzhu.getCount() - 1);
//                    }
//                } else {
//                    if (commoditys != null) {
//                        if (commodities.size() > 0) {
//                            shuliangEntty = new ShuliangEntty();
//                            shuliangEntty.setNumber(1);
//                            entty.add(commodities.size() - 1, shuliangEntty);
//                            commodities.add(commodities.size() - 1, commoditys);
//                        } else {
//                            shuliangEntty = new ShuliangEntty();
//                            shuliangEntty.setNumber(1);
//                            entty.add(0, shuliangEntty);
//                            commodities.add(0, commoditys);
//                        }
//                    }
//
//                }
//                if (commoditys != null) {
//                    tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
//                    j = Double.parseDouble(tv_Total.getText().toString());
//
////                    if (layout_go_pay.getVisibility()==View.GONE) {
////                        j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
////                    } else {
//                        if (commoditys.getType()!=null&&!commoditys.getType().equals("")&&!commoditys.getType().equals("0")) {
//                            if (SharedUtil.getfalseBoolean("sw_member_price")) {
//                                if (commoditys.getCustom_member_price() != null && !commoditys.getCustom_member_price().equals("")&& !commoditys.getCustom_member_price().equals("null")) {
//                                    j = TlossUtils.add(j, Double.parseDouble(StringUtils.getStrings(commoditys.getCustom_member_price(), ",")[Integer.parseInt(commoditys.getType())-1]));
//                                } else {
//                                    j = TlossUtils.add(j, Double.parseDouble(commoditys.getMember_price()));
//                                }
//                            }else {
//                                j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
//                            }
//                        }else {
//                            j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
//                        }
////                        j = TlossUtils.add(j, Double.parseDouble(commoditys.getMember_price()));
////                    }
//
//
//                    tv_Total.setText(StringUtils.stringpointtwo(j + ""));
//                    int a = 0;
//                    for (int i = 0; i < commodities.size(); i++) {
////                a += entty.get(i).getNumber();
//                    }
//                    tv_netreceipts.setText(j + "");
//                    Showtotal(j + "");
//                    et_keyoard.setText(j + "");
//
//                    adapterzhu.getEntty(entty);
//                    adapterzhu.getadats(commodities);
//                    lv.setAdapter(adapterzhu);
//
//                    lv.setSelection(adapterzhu.getCount() - 1);
//                    if (number >= 0) {
//                        //广播的发送
//                        Intent mIntent = new Intent();
//                        mIntent.setAction("qwer");
//                        mIntent.putExtra("yaner", commoditys);
//                        //发送广播  
//                        sendBroadcast(mIntent);
//                        fuxiang = true;
//                        number++;
//                    }
//                }
//            }
////        commodities.add(commoditys);
//        } else {
//            getSeek(barcode);
//        }
    };

    public double m;
    //搜索的接口
    public void getSeek(final String str) {
        m=(double) 0;
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("bncode",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        isgetseek=false;
                        getlocal(str);
                        LogUtils.d("print","出现的错误111");
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("print","搜索的结果"+s);
                        isgetseek=false;
//                        getweight
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                list_fuzzy.clear();
                                if (ja1.length() == 0) {
                                    final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    window.setContentView(R.layout.newcommodity_dialog);
                                    Button but_new_comm = (Button) window.findViewById(R.id.but_new_comm);
                                    Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                                    but_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    but_new_comm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Commodity commodity=new Commodity();
                                            commodity.setGoods_id("null");
                                            commodity.setBncode(str);
                                            Intent intent =new Intent(MainActivity.this,Addgoodgs_Activity.class);
                                            intent.putExtra("commodity",commodity);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    for (int j = 0; j < ja1.length(); j++) {
                                        Commodity commodity = new Commodity();
                                        JSONObject jo2 = ja1.getJSONObject(j);
                                        commodity.setGoods_id(jo2.getString("goods_id"));
                                        commodity.setIs_special_offer(jo2.getString("is_special"));
                                        commodity.setName(jo2.getString("name"));
                                        commodity.setPy(jo2.getString("py"));

                                        commodity.setPrice(jo2.getString("price"));
                                        commodity.setMember_price(jo2.getString("member_price"));
                                        commodity.setCost(jo2.getString("cost"));
                                        commodity.setBncode(jo2.getString("bncode"));
                                        commodity.setStore(jo2.getString("store"));
                                        commodity.setCook_position(jo2.getString("cook_position"));
                                        commodity.setCustom_member_price(jo2.getString("custom_member_price"));
                                        commodity.setType(Member_type);
                                        if (!commodity.getCook_position().equals("0")) {
                                            getweight(commodity);
                                        }else {
                                            if (commodities.size() > 0) {
                                                int in = 0;
                                                aa:
                                                for (int k = 0; k < commodities.size(); k++) {
                                                    if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                                        in = in + (k + 1);
                                                        break aa;
                                                    }
                                                }
                                                if (in == 0) {
                                                    shuliangEntty = new ShuliangEntty();
                                                    shuliangEntty.setNumber(1);
                                                    entty.add(shuliangEntty);
                                                    commodities.add(commodity);
                                                } else {
                                                    float i = entty.get(in - 1).getNumber();
                                                    i++;
                                                    entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);
                                                    Log.d("print", "iiii" + entty);
                                                    adapterzhu.getEntty(entty);
                                                    adapterzhu.getadats(commodities);

                                                    adapterzhu.notifyDataSetChanged();
                                                    lv.setAdapter(adapterzhu);
                                                    lv.setSelection(adapterzhu.getCount() - 1);
                                                }
                                            } else {
                                                if (commodity != null) {
                                                    if (commodities.size() > 0) {
                                                        shuliangEntty = new ShuliangEntty();
                                                        shuliangEntty.setNumber(1);
                                                        entty.add(commodities.size() - 1, shuliangEntty);
                                                        commodities.add(commodities.size() - 1, commodity);
                                                    } else {
                                                        shuliangEntty = new ShuliangEntty();
                                                        shuliangEntty.setNumber(1);
                                                        entty.add(0, shuliangEntty);
                                                        commodities.add(0, commodity);
                                                    }
                                                }
                                            }
                                            if (commodity != null) {
                                                tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
                                                m = Double.parseDouble(tv_Total.getText().toString());

//                                                if (layout_go_pay.getVisibility()==View.GONE) {
//                                                    m = TlossUtils.add(m, Double.parseDouble(commodity.getPrice()));
//                                                } else {
                                                    if (commodity.getType()!=null&&!commodity.getType().equals("")&&!commodity.getType().equals("0")) {
                                                        if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                                            if (commodity.getIs_special_offer() != null) {
                                                                if (commodity.getIs_special_offer().equals("no")) {
                                                                    if (commodity.getCustom_member_price() != null && !commodity.getCustom_member_price().equals("")&& !commodity.getCustom_member_price().equals("null")) {
                                                                        m = TlossUtils.add(m, Double.parseDouble(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]));
                                                                    } else {
                                                                        m = TlossUtils.add(m, Double.parseDouble(commodity.getMember_price()));
                                                                    }
                                                                }else {
                                                                    m = TlossUtils.add(m, Double.parseDouble(commodity.getPrice()));
                                                                }
                                                        }else {
                                                                m = TlossUtils.add(m, Double.parseDouble(commodity.getPrice()));
                                                            }
                                                        }else {
                                                            m = TlossUtils.add(m, Double.parseDouble(commodity.getPrice()));
                                                        }
                                                    }else {
                                                        m = TlossUtils.add(m, Double.parseDouble(commodity.getPrice()));
                                                    }
//                                                    m = TlossUtils.add(m, Double.parseDouble(commodity.getMember_price()));
//                                                }


//                                        f = new BigDecimal(m).setScale(2, BigDecimal.ROUND_HALF_UP)
//                                                .floatValue();
                                                tv_Total.setText(StringUtils.stringpointtwo(m + ""));
                                                int a = 0;
                                                for (int i = 0; i < commodities.size(); i++) {
//                a += entty.get(i).getNumber();
                                                }
                                                tv_netreceipts.setText(m + "");
                                                Showtotal(m + "");
                                                et_keyoard.setText(m + "");

                                                adapterzhu.getEntty(entty);
                                                adapterzhu.getadats(commodities);
                                                lv.setAdapter(adapterzhu);

                                                lv.setSelection(adapterzhu.getCount() - 1);
                                                if (number >= 0) {
                                                    //广播的发送
                                                    Intent mIntent = new Intent();
                                                    mIntent.setAction("qwer");
                                                    mIntent.putExtra("yaner", commodity);
                                                    //发送广播  
                                                    sendBroadcast(mIntent);
                                                    fuxiang = true;
                                                    number++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.toast_layout, null);
                                TextView tv = (TextView) view.findViewById(R.id.tv);
                                tv.setText(getResources().getString(R.string.no_such_commodity));
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

    //移动支付的支付成功的接口
    public void getpay() {
        OkGo.getInstance().cancelTag("order_status");
        OkGo.post(SysUtils.getSellerServiceUrl("order_status"))
                .tag("order_status")
                .params("is_score_pay",is_score_pay)
                .params("pay_score",pay_score)
                .params("type",pay_type)
                .params("pbmember_id",pbmember_id)
//                .params("score",(int)Float.parseFloat(tv_netreceipts.getText().toString())/1.0)
                .params("score",100)
                .params("order_id", SharedUtil.getString("order_id"))
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                handlernew.removeCallbacks(runnablenew);
                                stopService(new Intent(MainActivity.this,NetWorkService.class));

                                im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.load));
                                if (commodities.size()>0){
//                                    if(commodities.get(0).getName().equals("会员充值")){
//                                        Map<String, String> map = new HashMap<String, String>();
//                                        map.put("member_id", pbmember_id);
////                                        map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
////                                                Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");
//
//                                        map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
//                                                Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(entty.get(0).getNumber()+"")) + "");
//                                        Gson gson = new Gson();
//                                        String s1 = gson.toJson(map);
//                                        UPmoney(s1);
//                                    }
                                    if(commodities.get(0).getName().equals(getResources().getString(R.string.change_money))){
                                        /**
                                         * 换钱成功
                                         */
                                        change_money(commodities.get(0).getPrice());
                                    }

                                }
                                SharedUtil.putString("order_id", "");
                                pay_type="0";
                                adapterzhu.setType(false);
                                issucceed = false;

                                pay_score="";
                                is_score_pay="no";

                                JSONObject jo2 = jo1.getJSONObject("data");
                                Mobile_pay mobile_pay1 = new Mobile_pay();
                                String order_id = jo2.getString("order_id");

                                mobile_pay1.setOrder_id(order_id);
                                String time = jo2.getString("time");

                                String time1 = TimeZoneUtil.getTime1((Long.parseLong(time) * 1000));
                                mobile_pay1.setPayed_time(time1);
                                tv_danhao.setText(order_id);
                                tv_day.setText(time1);

                                SharedUtil.putString("order_id","");
                                operational = 0;
                                SharedUtil.putString("operational", operational + "");

                                type = 0;

                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("mobile_pay", mobile_pay1);
                                sendBroadcast(mIntent1);
                                if (Double.parseDouble(tv_netreceipts.getText().toString())>0){
                                    tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                                }else {
                                }
                                tv_modes.setText(getResources().getString(R.string.mobile_type));

                                //判断支付为0元的解决方法
                                if (Double.parseDouble(tv_netreceipts.getText().toString())>0){
                                    Text2Speech.isSpeeching();
                                    Text2Speech.speech(MainActivity.this,getResources().getString(R.string.pay_success)+tv_netreceipts.getText().toString()+"元",4,false);

                                    if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
                                        PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                                        printUtil1.openButtonClicked();

                                        //112233445566
                                        String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, order_id, time1, commodities, entty,
                                                1, Double.parseDouble(tv_netreceipts.getText().toString()), "", tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), tv_Surplus.getText().toString(), "0", "", "",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                        if (PrintWired.usbPrint(MainActivity.this, syy)) {

                                        } else {
                                            printUtil1.printstring(syy);

                                            mService.sendMessage(syy, "GBK");
                                        }
                                    }
//                                        USBPrinters.initPrinter(getApplicationContext());
//                                        USBPrinters.getInstance().print(syy);
//                                        USBPrinters.destroyPrinter();
                                }

                                Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
                                sqlite_entity.insertStock1(commodities,entty);

//                                for (int r = 0; r < commodities.size(); r++) {
//                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
//                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
//                                    while (cursor.moveToNext()) {
//                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
//                                        String newnums = (Double.parseDouble(nums) - entty.get(r).getNumber()) + "";
//                                        ContentValues values = new ContentValues();
//                                        values.put("store", newnums);
//                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
//                                    }
//                                }

                                reduce=false;
                                discount=false;

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                if (Entty!=null){
                                    for (int l=0;l<Entty.size();l++){
                                        Entty.get(l).setChecked(false);
                                    }
                                }
                                SharedUtil.putString("order_id", "");
                                mapList.clear();
                                commodities.clear();
                                entty.clear();
                                _Total="0";

                                Member_type="0";
//                                for (int j = 0; j < entty.size(); j++) {
//                                    entty.get(j).setNumber(1);
//                                }
                                adapterzhu.notifyDataSetChanged();
                                tv_Totalmerchandise.setText(0 + "");
                                tv_Total.setText(0.0 + "");
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_inputscancode.setText(0 + "");
                                et_keyoard.setText(0 + "");
                                keyboard_tv_layout.performClick();
                                Rl_yidong.setVisibility(View.GONE);
                                ll_jshuang.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);
                                //小圆点消失
                                Intent Intent1 = new Intent();
                                Intent1.setAction("com.yzx.clear");
                                sendBroadcast(Intent1);

//                                new Thread(new ThreadTimes());

                                new Thread(new ThreadTime()).start();
                                handler1 = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == 2) {
//                                tv_status.setText("正在获取客户支付状态...");
                                            ll_jshuang.setVisibility(View.VISIBLE);
                                            Rl_time.setVisibility(View.GONE);
                                            Rl_xianjin.setVisibility(View.GONE);
                                            Rl_yidong.setVisibility(View.GONE);
                                        }
                                    }
                                };
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    //    加载数据
    private void getAdats() {
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(MainActivity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在初始化数据...");
        loadingdialog.show();
//        goods_pb DateUtils.getTime()

        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("type", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.d("print","同步的方法是"+"接口"+ SysUtils.getGoodsServiceUrl("goods_pb"));
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            Log.d("print","同步好的数据"+s);
                            SharedUtil.putString("acquisition_time", DateUtils.getTime());
                            JSONObject jsonobject = new JSONObject(s);

                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
//                            total=j2.getInt("total");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            JSONArray ja2 = j2.getJSONArray("sum");
                            JSONObject jo4 = ja2.getJSONObject(0);
//                            jinjia=jo4.getInt("sum_store");
//                            zongshoujia=jo4.getInt("sum_price");
//                            zong=jo4.getInt("sum_cost");
                            commodities.clear();
                            entty.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Commodity commodity = new Commodity();
                                JSONObject jo1 = jsonArray.getJSONObject(j);
                                commodity.setCustom_member_price(jo1.getString("custom_member_price"));
                                commodity.setIs_special_offer(jo1.getString("is_special"));
                                commodity.setGoods_id(jo1.getString("goods_id"));
                                commodity.setMember_price(jo1.getString("member_price"));
                                commodity.setName(jo1.getString("name"));
                                commodity.setPy(jo1.getString("py"));
//                                commodity.setSeller_id(jsonObject.getString("seller_id"));
                                commodity.setPrice(jo1.getString("price"));
                                commodity.setCost(jo1.getString("cost"));
                                commodity.setBncode(jo1.getString("bncode"));
                                commodity.setBn(jo1.getString("bn"));
                                commodity.setTag_id(jo1.getString("tag_id"));
                                commodity.setTag_name(jo1.getString("tag_name"));
                                commodity.setUnit(jo1.getString("unit"));
                                commodity.setUnit_id(jo1.getInt("unit_id"));
                                commodity.setStore(jo1.getString("store"));
//                                jinjia += Float.valueOf(jo1.getString("cost"));
//                                zongshoujia += Float.valueOf(jo1.getString("price"));
//                                zong += Float.valueOf(jo1.getString("store"));
                                commodity.setGood_limit(jo1.getString("good_limit"));
                                commodity.setGood_stock(jo1.getString("good_stock"));
                                commodity.setPD(jo1.getString("PD"));
                                commodity.setGD(jo1.getString("GD"));
//                                    commodity.setGood_remark(jsonObject.getString("good_remark"));
                                commodity.setMarketable(jo1.getString("marketable"));

                                commodity.setProvider_id(jo1.getString("provider_id"));
                                commodity.setProvider_name(jo1.getString("provider_name"));
                                commodity.setCook_position(jo1.getString("cook_position"));
                                commodity.setAltc(jo1.getString("ALTC"));
                                JSONArray ja3 = jo1.getJSONArray("label");
                                List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                for (int k = 0; k < ja3.length(); k++) {
                                    Commodity.Labels labels = new Commodity.Labels();
                                    JSONObject jo3 = ja3.getJSONObject(k);
                                    String label_id = jo3.getString("label_id");
                                    String label_name = jo3.getString("label_name");
                                    labels.setLabel_id(label_id);
                                    labels.setLabel_name(label_name);
                                    List_labels.add(labels);
                                }
                                commodity.setAdats(List_labels);
                                Datas.add(commodity);

                            }
//                                if (SysUtils.isWifiConnected(getApplication())) {
                            if (isnetworknew) {
//                                if (SysUtils.isNetworkAvailable(getApplication())) {
//                                if (SysUtils.isNetworkOnline()) {
                                sqLiteDatabase.execSQL(("delete from  commodity"));
                            }
                            String sql="insert into commodity (goods_id,name," +
                                    "py,price" +
                                    ",cost,bncode," +
                                    "tag_id,unit," +
                                    "store,good_limit," +
                                    "good_stock,PD,GD," +
                                    "marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer,custom_member_price)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            SQLiteStatement stat= sqLiteDatabase.compileStatement(sql);

                            sqLiteDatabase.beginTransaction();

                            for (int i = 0; i < Datas.size(); i++) {
                                stat.bindString(1,Datas.get(i).getGoods_id());
                                stat.bindString(2,Datas.get(i).getName());
                                stat.bindString(3,Datas.get(i).getPy());
                                stat.bindString(4,Datas.get(i).getPrice());
                                stat.bindString(5,Datas.get(i).getCost());
                                stat.bindString(6, Datas.get(i).getBncode().replaceAll(" ",""));
                                stat.bindString(7,Datas.get(i).getTag_id());
                                stat.bindString(8,Datas.get(i).getUnit());
                                stat.bindString(9,Datas.get(i).getStore());
                                stat.bindString(10,Datas.get(i).getGood_limit());
                                stat.bindString(11,Datas.get(i).getGood_stock());
                                stat.bindString(12,Datas.get(i).getPD());
                                stat.bindString(13,Datas.get(i).getGD());
                                stat.bindString(14,Datas.get(i).getMarketable());
                                stat.bindString(15,Datas.get(i).getTag_name());
                                stat.bindString(16,Datas.get(i).getAltc());
                                stat.bindString(17,Datas.get(i).getProduct_id()+"");
                                stat.bindString(18,Datas.get(i).getBn());
                                stat.bindString(19,Datas.get(i).getProvider_id());
                                stat.bindString(20,Datas.get(i).getProvider_name());
                                stat.bindString(21,Datas.get(i).getCook_position());
                                stat.bindString(22,Datas.get(i).getMember_price());
                                stat.bindString(23,Datas.get(i).getIs_special_offer());
                                stat.bindString(24,Datas.get(i).getCustom_member_price());
                                Log.d(TAG, "Is_special_offer: "+Datas.get(i).getIs_special_offer());
                                stat.executeInsert();
                            }
                            sqLiteDatabase.setTransactionSuccessful();
                            sqLiteDatabase.endTransaction();
//                            sqLiteDatabase.close();
//                            for (int i = 0; i < Datas.size(); i++) {
//                                sqLiteDatabase.execSQL("insert into commodity (goods_id,name," +
//                                        "py,price" +
//                                        ",cost,bncode," +
//                                        "tag_id,unit," +
//                                        "store,good_limit," +
//                                        "good_stock,PD,GD," +
//                                        "marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
//                                        Datas.get(i).getGoods_id(), Datas.get(i).getName()
//                                        , Datas.get(i).getPy(), Datas.get(i).getPrice(),
//                                        Datas.get(i).getCost(), Datas.get(i).getBncode().replaceAll(" ","")
//                                        , Datas.get(i).getTag_id(), Datas.get(i).getUnit(),
//                                        Datas.get(i).getStore(), Datas.get(i).getGood_limit()
//                                        , Datas.get(i).getGood_stock(), Datas.get(i).getPD(),
//                                        Datas.get(i).getGD(), Datas.get(i).getMarketable(), Datas.get(i).getTag_name()
//                                        , Datas.get(i).getAltc(), Datas.get(i).getProduct_id(), Datas.get(i).getBn()
//                                        ,Datas.get(i).getProvider_id(),Datas.get(i).getProvider_name(),Datas.get(i).getCook_position(),Datas.get(i).getMember_price()});
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
//                            quick_fragment = new Quick_fragment();
////                            switchFrament(mcontext,quick_fragment);
//                            showFragment1( quick_fragment);
                            loadingdialog.dismiss();
                        }
                        Datas.clear();
//                        but_Quick.performClick();
//                        adapterzhu.getadats(commodities);
//                        lv.setAdapter(adapterzhu);
//
//                        lv.setSelection(adapterzhu.getCount() - 1)
                    }
                });

    }

    //实时更新数据
    public void getData_Feed(){
        OkGo.post(SysUtils.getGoodsServiceUrl("get_modify_goods"))
                .tag(MainActivity.this)
                .params("time", SharedUtil.getString("acquisition_time"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","实时更新数据"+s);
                        try {
                            SharedUtil.putString("acquisition_time", DateUtils.getTime());
                            JSONObject jsonobject = new JSONObject(s);
                            Log.d("print", "同步" + jsonobject);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
//                            total=j2.getInt("total");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
//                            JSONArray ja2 = j2.getJSONArray("sum");
//                            JSONObject jo4 = ja2.getJSONObject(0);
//                            jinjia=jo4.getInt("sum_store");
//                            zongshoujia=jo4.getInt("sum_price");
//                            zong=jo4.getInt("sum_cost");
                            commodities.clear();
                            entty.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Commodity commodity = new Commodity();
                                JSONObject jo1 = jsonArray.getJSONObject(j);
                                commodity.setCustom_member_price(jo1.getString("custom_member_price"));
                                commodity.setIs_special_offer(jo1.getString("is_special"));
                                commodity.setGoods_id(jo1.getString("goods_id"));
                                commodity.setName(jo1.getString("name"));
                                commodity.setPy(jo1.getString("py"));
//                                commodity.setSeller_id(jsonObject.getString("seller_id"));
                                commodity.setPrice(jo1.getString("price"));
                                commodity.setMember_price(jo1.getString("member_price"));
                                commodity.setCost(jo1.getString("cost"));
                                commodity.setBncode(jo1.getString("bncode"));
                                commodity.setBn(jo1.getString("bn"));
                                commodity.setTag_id(jo1.getString("tag_id"));
                                commodity.setTag_name(jo1.getString("tag_name"));
                                commodity.setUnit(jo1.getString("unit"));
                                commodity.setUnit_id(jo1.getInt("unit_id"));
                                commodity.setStore(jo1.getString("store"));
                                commodity.setProvider_id(jo1.getString("provider_id"));
                                commodity.setProvider_name(jo1.getString("provider_name"));
                                commodity.setCook_position(jo1.getString("cook_position"));



//                                  jinjia += Float.valueOf(jo1.getString("cost"));
//                                zongshoujia += Float.valueOf(jo1.getString("price"));
//                                zong += Float.valueOf(jo1.getString("store"));
                                commodity.setGood_limit(jo1.getString("good_limit"));
                                commodity.setGood_stock(jo1.getString("good_stock"));
                                commodity.setPD(jo1.getString("PD"));
                                commodity.setGD(jo1.getString("GD"));
//                                    commodity.setGood_remark(jsonObject.getString("good_remark"));
                                commodity.setMarketable(jo1.getString("marketable"));
                                commodity.setAltc(jo1.getString("ALTC"));
                                JSONArray ja3 = jo1.getJSONArray("label");
                                List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                for (int k = 0; k < ja3.length(); k++) {
                                    Commodity.Labels labels = new Commodity.Labels();
                                    JSONObject jo3 = ja3.getJSONObject(k);
                                    String label_id = jo3.getString("label_id");
                                    String label_name = jo3.getString("label_name");
                                    labels.setLabel_id(label_id);
                                    labels.setLabel_name(label_name);
                                    List_labels.add(labels);
                                }
                                commodity.setAdats(List_labels);
                                Datas.add(commodity);
                            }
                            Log.e("print","shuju"+Datas);
                            for (int i = 0; i < Datas.size(); i++) {
                                sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{Datas.get(i).getGoods_id()});
                                if (cursor.moveToFirst()) {
                                    ContentValues values = new ContentValues();
                                    String name = Datas.get(i).getName();
                                    String price = Datas.get(i).getPrice();
                                    String cost = Datas.get(i).getCost();
                                    String store = Datas.get(i).getStore();
                                    String bncode = Datas.get(i).getBncode();
                                    String provider_id = Datas.get(i).getProvider_id();
                                    String cook_position = Datas.get(i).getCook_position();
                                    String member_price = Datas.get(i).getMember_price();
                                    String is_special_offer = Datas.get(i).getIs_special_offer();
                                    values.put("name", name);
                                    values.put("price", price);
                                    values.put("cost", cost);
                                    values.put("store", store);
                                    values.put("bncode", bncode);
                                    values.put("provider_id", provider_id);
                                    values.put("cook_position",cook_position);
                                    values.put("member_price",member_price);
                                    values.put("is_special_offer",is_special_offer);
                                    sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{Datas.get(i).getGoods_id()});
                                }else {
                                    sqLiteDatabase.execSQL("insert into commodity (goods_id,name," +
                                            "py,price" +
                                            ",cost,bncode," +
                                            "tag_id,unit," +
                                            "store,good_limit," +
                                            "good_stock,PD,GD," +
                                            "marketable,tag_name,ALTC,product_id,bn,provider_id,provider_name,cook_position,member_price,is_special_offer)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                                            Datas.get(i).getGoods_id(), Datas.get(i).getName()
                                            , Datas.get(i).getPy(), Datas.get(i).getPrice(),
                                            Datas.get(i).getCost(), Datas.get(i).getBncode().replaceAll(" ","")
                                            , Datas.get(i).getTag_id(), Datas.get(i).getUnit(),
                                            Datas.get(i).getStore(), Datas.get(i).getGood_limit()
                                            , Datas.get(i).getGood_stock(), Datas.get(i).getPD(),
                                            Datas.get(i).getGD(), Datas.get(i).getMarketable(), Datas.get(i).getTag_name()
                                            , Datas.get(i).getAltc(), Datas.get(i).getProduct_id(), Datas.get(i).getBn(),
                                            Datas.get(i).getProvider_id(),Datas.get(i).getProvider_name(),Datas.get(i).getCook_position(),Datas.get(i).getMember_price(),Datas.get(i).getIs_special_offer() });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        loadingdialog.dismiss();
                        Datas.clear();
                    }
                });
    }

    //获取轮播图
    private void getimager() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_pic"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "图片是" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja = jo1.getJSONArray("data");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo2 = ja.getJSONObject(i);
                                    imageurls.add(jo2.getString("link"));
                                }
                                im_picture.setImagesUrl(imageurls);
//                                Glide.with(MainActivity.this).load("").into(im_picture);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    //获取商户电话
    public void getphone() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_seller_tel"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "电话号码为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                JSONObject jo2 = ja1.getJSONObject(0);
                                tel = jo2.getString("tel");
                                SharedUtil.putString("phone",tel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //软件的检查更新
    private void update() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            Log.d("print", "版本号是" + versionName);
            Log.d("print", "版本号是" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        OkGo.get("http://www.yzx6868.com/apk/pingban.xml")
                .tag(this)
                .execute(new FileCallback("text.xml") {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        versions = MyXMLReader.pagetee();
                        if (versionCode != Integer.parseInt(versions)) {
                            Log.d("print", "版本号是" + versions);
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.update_dialog,
                                    (ViewGroup) findViewById(R.id.update_dialog));
                            new AlertDialog.Builder(MainActivity.this).setTitle("有新版本是否更新")
                                    .setView(layout)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            AlertDialog downloadDialog;    //下载弹出框
                                            startService(new Intent(MainActivity.this, UPdate_MyServices.class));
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }
                    }
                });
    }

    //加载数据
    private void LoadDates() {
        SetEditTextInput.setEtPoint(et_inputscancode);
        SetEditTextInput.setEtPoint(et_inputscancode);
        SetEditTextInput.setEtPoint(tv_Surplus);
//        sqLiteDatabase.execSQL("insert into users (name,unitprice,number,subtotal,Price,profit,QRCode)values(?,?,?,?,?,?,?)",new Object[]{"火鸡","3","5","5","1","1","1234567890004"});
        //删除按钮的
        but_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("print", "print" + SharedUtil.getString("operational"));
                if (SharedUtil.getString("operational").equals("0")) {
                    if (commodities.size() > 0) {
                        if (k == -1) {
                            if (commodities.size() == 1) {
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_keyoard.setText(0 + "");
                                tv_Total.setText(0 + "");
                                tv_Totalmerchandise.setText(0 + "");
                                pay_type="0";
                                adapterzhu.setType(false);
                                pay_score="";
                                is_score_pay="no";

                                reduce=false;
                                discount=false;

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                if (commodities.size() > 0) {
                                    entty.remove(entty.size() - 1);
                                }
                                stringcontext = "移除" + commodities.get(commodities.size() - 1).getName();
//                                but_time
                                Intent intent = new Intent("com.yzx.fupingdelete");
                                intent.putExtra("delete", commodities.size() - 1);
                                intent.putExtra("commoditys", commodities.get(commodities.size() - 1));
                                sendBroadcast(intent);
                                commodities.remove(commodities.size() - 1);
                                adapterzhu.notifyDataSetChanged();
                            } else {
                                double f = Double.parseDouble(tv_Total.getText().toString());
                                if (commodities.get(commodities.size()-1).getType()!=null&&!commodities.get(commodities.size()-1).getType().equals("")&&!commodities.get(commodities.size()-1).getType().equals("0")) {
                                    if (commodities.get(commodities.size()-1).getIs_special_offer()!=null){
                                        if (commodities.get(commodities.size()-1).getIs_special_offer().equals("no")) {
                                            if (commodities.get(commodities.size()-1).getCustom_member_price() != null && !commodities.get(commodities.size()-1).getCustom_member_price().equals("")&& !commodities.get(commodities.size()-1).getCustom_member_price().equals("null")) {
                                                if (!StringUtils.getStrings(commodities.get(commodities.size()-1).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(commodities.size()-1).getType()) - 1].equals("") && !(entty.get(entty.size()-1).getNumber() + "").equals("")) {
                                                    f = TlossUtils.sub(f, (Double.parseDouble(StringUtils.getStrings(commodities.get(commodities.size()-1).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(commodities.size()-1).getType()) - 1]) * entty.get(entty.size() - 1).getNumber()));
                                                }
                                            } else {
                                                if (!commodities.get(commodities.size() - 1).getMember_price().equals("") && !(entty.get(entty.size() - 1).getNumber() + "").equals("")) {
                                                    f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getMember_price()) * entty.get(entty.size() - 1).getNumber()));
                                                }
                                            }
                                        } else {
                                            if (!commodities.get(commodities.size() - 1).getPrice().equals("") && !(entty.get(entty.size() - 1).getNumber() + "").equals("")) {
                                                f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getPrice()) * entty.get(entty.size() - 1).getNumber()));
                                            }
                                        }
                                    }else {
                                        if (!commodities.get(commodities.size() - 1).getPrice().equals("") && !(entty.get(entty.size() - 1).getNumber() + "").equals("")) {
                                            f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getPrice()) * entty.get(entty.size() - 1).getNumber()));
                                        }
                                    }
                                }else {
                                    if (!commodities.get(commodities.size() - 1).getPrice().equals("")&&!(entty.get(entty.size() - 1).getNumber()+"").equals("")){
                                        f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getPrice()) * entty.get(entty.size() - 1).getNumber()));
                                    }
                                }
//                                if (iswholesale){
//                                    f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getPrice()) * entty.get(entty.size() - 1).getNumber()));
//                                }else {
//                                    f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getMember_price()) * entty.get(entty.size() - 1).getNumber()));
//                                }
                                tv_netreceipts.setText(f + "");
                                Showtotal(f + "");
                                if (f == 0) {
                                    et_keyoard.setText((int) f + "");
                                } else {
                                    et_keyoard.setText(f + "");
                                }

                                tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                                tv_Totalmerchandise.setText("" + (Float.valueOf(tv_Totalmerchandise.getText().toString()) - entty.get(entty.size() - 1).getNumber()));
                                Log.d("print", "删除的数量" + entty.get(commodities.size() - 1).getNumber());

                                if (commodities.size() > 0) {
                                    entty.remove(entty.size() - 1);
                                }

                                if (commodities.get(commodities.size()-1).equals("活动立减")){
                                    reduce=false;
                                    discount=false;

                                    discount_type="reduce";
                                    num="0";
                                    discount_goods_id="";

                                }

                                Intent intent = new Intent("com.yzx.fupingdelete");
                                intent.putExtra("delete", commodities.size() - 1);
                                intent.putExtra("commoditys", commodities.get(commodities.size() - 1));
                                sendBroadcast(intent);
                                Log.d("print", "合计" + f);
                                commodities.remove(commodities.size() - 1);
                                adapterzhu.notifyDataSetChanged();
                            }
                        } else if (k >= 0) {
                            double f = Double.parseDouble(tv_Total.getText().toString());

                            if (commodities.get(k).getType()!=null&&!commodities.get(k).getType().equals("")&&!commodities.get(k).getType().equals("0")) {
                                if (commodities.get(k).getIs_special_offer()!=null){
                                    if (commodities.get(k).getIs_special_offer().equals("no")) {
                                        if (commodities.get(k).getCustom_member_price() != null && !commodities.get(k).getCustom_member_price().equals("") && !commodities.get(k).getCustom_member_price().equals("null")) {
                                            if (!StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                                f = TlossUtils.sub(f, (Double.parseDouble(StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1]) * entty.get(k).getNumber()));
                                            }
                                        } else {
                                            if (!commodities.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                                f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(k).getMember_price()) * entty.get(k).getNumber()));
                                            }
                                        }
                                    } else {
                                        if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                            f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(k).getPrice()) * entty.get(k).getNumber()));
                                        }
                                    }
                                }else {
                                    if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(k).getPrice()) * entty.get(k).getNumber()));
                                    }
                                }
                            }else {
                                if (!commodities.get(k).getPrice().equals("")&&!(entty.get(k).getNumber()+"").equals("")){
                                    f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(k).getPrice()) * entty.get(k).getNumber()));
                                }
                            }

//                            if (iswholesale){
//                                f = TlossUtils.sub(f, Double.valueOf(Double.parseDouble(commodities.get(k).getPrice()) * entty.get(k).getNumber()));
//                            }else {
//                                f = TlossUtils.sub(f, Double.valueOf(Double.parseDouble(commodities.get(k).getMember_price()) * entty.get(k).getNumber()));
//                            }


                            tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                            tv_netreceipts.setText(f + "");
                            Showtotal(f + "");
                            if (f == 0) {
                                et_keyoard.setText((int) f + "");
                            } else {
                                et_keyoard.setText(f + "");
                            }
                            tv_Totalmerchandise.setText("" + (Float.valueOf(tv_Totalmerchandise.getText().toString()) - entty.get(k).getNumber()));
                            Log.d("print", "合计" + f);
                            Log.d("print", "删除的数量" + entty.get(k).getNumber());

//                            Intent Intent1 = new Intent();
//                            Intent1.putExtra("index", k);
//                            Intent1.setAction("com.yzx.checked");
//                            sendBroadcast(Intent1);

                            entty.remove(k);
                            im_reductionof.setVisibility(View.GONE);
                            im_add.setVisibility(View.GONE);
                            Entty.get(k).setChecked(false);

                            adapterzhu.notifyDataSetChanged();
                            Intent intent = new Intent("com.yzx.fupingdelete");
                            intent.putExtra("delete", k);
                            intent.putExtra("commoditys", commodities.get(k));
                            sendBroadcast(intent);
                            Log.d("print", "删除的是第" + k + "个");
                            stringcontext = "移除" + commodities.get(k).getName();
                            commodities.remove(k);
                            adapterzhu.notifyDataSetChanged();
                            k = -1;
                        } else {
                            Toast.makeText(MainActivity.this, "不能再删除了", Toast.LENGTH_SHORT).show();
                        }
                        stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                        stringMap.put("work_name", SharedUtil.getString("name"));
                        stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                        stringMap.put("operate_type", "移除");
                        stringMap.put("content", stringcontext);
                        Gson gson = new Gson();
                        String ing = gson.toJson(stringMap);
                        getsensitivity(ing);

                    }
                }
            }
        });

        /**
         *
         *
         */
        //listview的点击事件
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("print", "选中了" + i);
//                lv.setItemChecked(i, true);
//                k = i;
//                im_reductionof= (ImageView) view.findViewById(R.id.im_reductionof);
//                im_add= (ImageView) view.findViewById(R.id.im_add);
//                ll_listitme= (LinearLayout) view.findViewById(R.id.ll_listitme);
//                for (int j=0;j<listmaidan.size();j++){
//                    ListEntty listEntty=new ListEntty();
//                    listEntty.setChecked(false);
//                    itmeChecked.add(listEntty);
//                }
//
//                    if(itmeChecked.get(i).getChecked()){
//                        int buk=i%2==0?0:1;
//
//                        if(buk==0){
//                            Log.d("print","#ffffff");
//                            im_add.setVisibility(View.GONE);
//                            im_reductionof.setVisibility(View.GONE);
//                            ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
//                            itmeChecked.get(i).setChecked(false);
//                        }else {
//                            Log.d("print","#f1f1f1");
//                            im_add.setVisibility(View.GONE);
//                            im_reductionof.setVisibility(View.GONE);
//                            ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                            itmeChecked.get(i).setChecked(false);
//                        }
//                    }else {
//                        Log.d("print","#ff0000");
//                        im_add.setVisibility(View.VISIBLE);
//                        im_reductionof.setVisibility(View.VISIBLE);
//                        ll_listitme.setBackgroundColor(Color.parseColor("#FF736F6F"));
//                        itmeChecked.get(i).setChecked(true);
//                    }
//
//
//
//
////                if(k==i){
////                    itmeChecked.get(i).setChecked(true);
////                }else {
////                    itmeChecked.get(i).setChecked(false);
////                }
////                if(itmeChecked.get(i).getChecked()){
////
////
////                }else if(i%2==0){
////                    ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
////                }else {
////                    ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
////                }
////                adapterzhu.notifyDataSetChanged();
//
////                listmaidan.remove(i);
////                adapterzhu.notifyDataSetChanged();
////                if(listmaidan.size()==0) {
////                    tv_Total.setText("");
////                    tv_Totalmerchandise.setText("");
////                    tv_netreceipts.setText("");
////                    j=0;
////                }else {
////                    j = j - listmaidan.get(i - 1).getSubtotal();
////                    tv_Total.setText(j + "");
////                    tv_Totalmerchandise.setText(listmaidan.size() + "");
////                    tv_netreceipts.setText(j + "");
////                }
////            }im_reductionof= (ImageView) view.findViewById(R.id.im_reductionof);
//                tv_jiage= (TextView) view.findViewById(R.id.tv_jiage);
//                ed_shuliang= (TextView) view.findViewById(R.id.ed_shuliang);
//                tv_xiaoji= (TextView) view.findViewById(R.id.tv_xiaoji);
//                tv_lirun= (TextView) view.findViewById(R.id.tv_lirun);
//
//                //点击加号的监听
//                im_add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        float shuliang=Float.parseFloat(ed_shuliang.getText().toString());
//                        float jiage=Float.parseFloat(tv_jiage.getText().toString());
//                        float xiaoji=Float.parseFloat(tv_xiaoji.getText().toString());
//                        float heji=Float.parseFloat(tv_Total.getText().toString());
//                        if(shuliang>0){
//                            shuliang++;
//                            itmeChecked.get(k).setNumber((int)shuliang);
//                            ed_shuliang.setText((int)shuliang+"");
//                            xiaoji+=jiage;
//                            tv_xiaoji.setText(xiaoji+"");
//                            heji+=jiage;
//                            tv_Total.setText(heji+"");
//                            Log.d("print","k="+k);
//                            listmaidan.get(k).setPrice((int)shuliang);
//                            Log.d("print","ddddd"+listmaidan);
//                        }
//
//                    }
//                });
//
//                //点击减号的监听
//                im_reductionof.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        float shuliang=Float.parseFloat(ed_shuliang.getText().toString());
//                        float jiage=Float.parseFloat(tv_jiage.getText().toString());
//                        float xiaoji=Float.parseFloat(tv_xiaoji.getText().toString());
//                        float heji=Float.parseFloat(tv_Total.getText().toString());
//                        if(shuliang>1){
//                            shuliang--;
//                            itmeChecked.get(k).setNumber((int)shuliang);
//                            ed_shuliang.setText((int)shuliang+"");
//                            xiaoji-=jiage;
//                            tv_xiaoji.setText(xiaoji+"");
//                            heji-=jiage;
//                            tv_Total.setText(heji+"");
//                            listmaidan.get(k).setPrice((int)shuliang);
//                            Log.d("print","ddddd"+listmaidan);
//                        }else {
//                            Toast.makeText(MainActivity.this,"不能再减了",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }

    }

    /**
     * 退货的方法
     * @param
     */
    public void showDialog() {
        if (commodities.size() > 0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setTitle("退货详情");
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.refund_layout);
            RelativeLayout Rl_number = (RelativeLayout) window.findViewById(R.id.Rl_number);
            Rl_number.setVisibility(View.GONE);
            final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
            Button but_submit = (Button) window.findViewById(R.id.but_submit);
            but_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!ed_describe.getText().toString().equals("")) {
                        Double sums = Double.valueOf(0);
                        Double profit = Double.valueOf(0);
                        Double profits = Double.valueOf(0);
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        for (int k = 0; k < commodities.size(); k++) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", commodities.get(k).getName());
                            map.put("nums", entty.get(k).getNumber() + "");

//                            if (iswholesale){
//                                map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getPrice()) + ""));
//                                profit = TlossUtils.mul(TlossUtils.sub(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(commodities.get(k).getCost())), Double.parseDouble(entty.get(k).getNumber() + ""));
//                                profits = TlossUtils.add(profits, profit);
//                                map.put("sum", TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")) + "");
//                                sums = TlossUtils.add(sums, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
//                            }else {
//                                map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getMember_price()) + ""));
//                                profit = TlossUtils.mul(TlossUtils.sub(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(commodities.get(k).getCost())), Double.parseDouble(entty.get(k).getNumber() + ""));
//                                profits = TlossUtils.add(profits, profit);
//                                map.put("sum", TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")) + "");
//                                sums = TlossUtils.add(sums, TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
//                            }

                            String price=commodities.get(k).getPrice();
                            double amount=TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + ""));
                            if (commodities.get(k).getType()!=null&&!commodities.get(k).getType().equals("")&&!commodities.get(k).getType().equals("0")) {
                                if (commodities.get(k).getIs_special_offer()!=null){
                                    if (commodities.get(k).getIs_special_offer().equals("no")) {
                                        if (commodities.get(k).getCustom_member_price() != null && !commodities.get(k).getCustom_member_price().equals("")&& !commodities.get(k).getCustom_member_price().equals("null")) {
                                            if (!StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(k).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(k).getType()) - 1]), Double.parseDouble(entty.get(k).getNumber() + "")));
                                            }
                                        } else {
                                            if (!commodities.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                            }
                                        }
                                    }
                                } else {
                                    if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                    }
                                }
                            }else {
                                if (!commodities.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                }
                            }

                            map.put("price", StringUtils.stringpointtwo(price) + "");
                            profit = TlossUtils.mul(TlossUtils.sub(Double.parseDouble(price), Double.parseDouble(commodities.get(k).getCost())), Double.parseDouble(entty.get(k).getNumber() + ""));
                            profits = TlossUtils.add(profits, profit);
                            map.put("sum", amount + "");
                            sums = TlossUtils.add(sums, amount);


                            map.put("goods_id", commodities.get(k).getGoods_id());
                            list.add(map);
                        }
                        Gson gson = new Gson();
                        String str = gson.toJson(list);
                        Log.d("print", "退货的数据" + str);

                        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(MainActivity.this);
                        builder.delay(1);
                        loadingdialog = new ShapeLoadingDialog(builder);
                        loadingdialog.getBuilder().loadText("正在退货...");
                        loadingdialog.show();

                        OkGo.post(SysUtils.getSellerServiceUrl("back_goods"))
                                .tag(this)
                                .cacheKey("cacheKey")
                                .cacheMode(CacheMode.DEFAULT)
                                .params("operator", SharedUtil.getString("operator_id"))
                                .params("memo", ed_describe.getText().toString())
                                .params("items", str)
                                .params("sums", sums)
                                .params("type", "customer_back")
                                .params("work_id", SharedUtil.getString("work_id"))
                                .params("cost_sums", profits)
                                .params("worker_name", SharedUtil.getString("name"))
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print", "退货的" + s);
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                            String status = jo1.getString("status");
                                            String message = jo1.getString("message");
                                            if (status.equals("200")) {
                                                mapList.clear();
                                                for (int r = 0; r < commodities.size(); r++) {
                                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                                    while (cursor.moveToNext()) {
                                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                                                        String newnums = (Double.parseDouble(nums) + entty.get(r).getNumber()) + "";
                                                        ContentValues values = new ContentValues();
                                                        values.put("store", newnums);
                                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                                    }
                                                }
                                                commodities.clear();
                                                entty.clear();
                                                _Total="0";
                                                Member_type="0";
//                                                for (int j = 0; j < entty.size(); j++) {
//                                                    entty.get(j).setNumber(1);
//                                                }
                                                adapterzhu.notifyDataSetChanged();
                                                tv_Totalmerchandise.setText(0 + "");
                                                tv_Total.setText(0.0 + "");
                                                tv_netreceipts.setText(0 + "");
                                                Showtotal(0 + "");
                                                et_inputscancode.setText(0 + "");
                                                et_keyoard.setText(0 + "");

                                                Intent Intent1 = new Intent();
                                                Intent1.setAction("com.yzx.clear");
                                                sendBroadcast(Intent1);
                                                dialog.dismiss();
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                boolean isOpen = imm.isActive();
                                                //isOpen若返回true，则表示输入法打开，反之则关闭。
                                                if (isOpen) {
                                                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                }
                                                Toast.makeText(MainActivity.this, "退货成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }finally {
                                            loadingdialog.dismiss();
                                        }

                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "请输入备注", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            /**
             * 退货
             */
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
//                    Inputmethod_Utils.getshow(getApplication());
                }
            });
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            Toast.makeText(MainActivity.this, "请扫码商品退货", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 兑奖的方法
     * @param
     */
    public void Setredeem() {
        if (commodities.size() == 1) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setTitle("兑奖");
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.redeem_dialog);
            final EditText ed_money = (EditText) window.findViewById(R.id.ed_money);
            Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
            Button but_submit = (Button) window.findViewById(R.id.but_submit);
            but_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ed_money.getText().toString().equals("")) {
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        for (int k = 0; k < commodities.size(); k++) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", commodities.get(k).getName());
                            map.put("nums", entty.get(k).getNumber() + "");
                            map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getPrice()) + ""));
                            map.put("status", "0");
                            map.put("seller_id", SharedUtil.getString("seller_id"));
                            map.put("goods_id", commodities.get(k).getGoods_id());
                            map.put("total_amount", ed_money.getText().toString());
                            if (SharedUtil.getString("type").equals("4")) {
                                map.put("oprator", SharedUtil.getString("name"));
                                map.put("work_id", SharedUtil.getString("operator_id"));
                            } else {
                                map.put("oprator", SharedUtil.getString("seller_name"));
                                map.put("work_id", "0");
                            }

                            list.add(map);
                        }
                        Gson gson = new Gson();
                        String str = gson.toJson(list);
                        Log.d("print", "兑奖" + str);
                        OkGo.post(SysUtils.getSellerServiceUrl("redeem_insert"))
                                .tag(this)
                                .cacheKey("cacheKey")
                                .cacheMode(CacheMode.DEFAULT)
                                .params("map", str)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print", "兑奖的" + s);
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                            String status = jo1.getString("status");
                                            String message = jo1.getString("message");
                                            if (status.equals("200")) {
                                                mapList.clear();
                                                for (int r = 0; r < commodities.size(); r++) {
                                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                                    while (cursor.moveToNext()) {
                                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                                                        String newnums = (Double.parseDouble(nums) - entty.get(r).getNumber()) + "";
                                                        ContentValues values = new ContentValues();
                                                        values.put("store", newnums);
                                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                                    }
                                                }
                                                commodities.clear();
                                                entty.clear();
                                                _Total="0";
                                                Member_type="0";
//                                            for (int j = 0; j < entty.size(); j++) {
//                                                entty.get(j).setNumber(1);
//                                            }
                                                adapterzhu.notifyDataSetChanged();
                                                tv_Totalmerchandise.setText(0 + "");
                                                tv_Total.setText(0.0 + "");
                                                tv_netreceipts.setText(0 + "");
                                                Showtotal(0 + "");
                                                et_inputscancode.setText(0 + "");
                                                et_keyoard.setText(0 + "");

                                                Intent Intent1 = new Intent();
                                                Intent1.setAction("com.yzx.clear");
                                                sendBroadcast(Intent1);
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                                dialog.dismiss();
                                                Toast.makeText(MainActivity.this, "兑奖成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                                dialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } finally {
                                            if (Double.parseDouble(ed_money.getText().toString()) == 0) {

                                            } else {
                                                Commodity commodity = new Commodity();
                                                ShuliangEntty shuliang = new ShuliangEntty();
                                                commodity.setName("兑奖");
                                                commodity.setPrice(ed_money.getText().toString());
                                                commodity.setMember_price(ed_money.getText().toString());
                                                shuliang.setNumber(1);
                                                commodity.setCost(100 + "");
                                                commodity.setStore(200 + "");
                                                commodity.setGoods_id("null");
                                                commodity.setType(Member_type);
                                                commodities.add(commodity);
                                                entty.add(shuliang);
                                                adapterzhu.getadats(commodities);
                                                adapterzhu.getEntty(entty);
                                                lv.setAdapter(adapterzhu);
                                                tv_Totalmerchandise.setText("1");
                                                tv_Total.setText(ed_money.getText().toString());
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                                                tv_netreceipts.setText(ed_money.getText().toString());
                                                Showtotal(ed_money.getText().toString());
                                            }
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(MainActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
                    }
                }
            });


            but_abolish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "请扫码商品兑奖", Toast.LENGTH_SHORT).show();
        }
    }

    //把无网环境下的订单上传到服务器
    private void upNoInternet(String str) {
//        sqLiteDatabase.execSQL(("delete from  ProOut"));
//        sqLiteDatabase.execSQL(("delete from  goodsSell"));
        OkGo.post(SysUtils.getSellerServiceUrl("not_network_cash_pay"))
                .tag(this)
                .params("map", str)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.d("print","上传的数据为"+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "上传成功的数据为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                sqLiteDatabase.execSQL(("delete from  ProOut"));
                                sqLiteDatabase.execSQL(("delete from  goodsSell"));
                                Toast.makeText(MainActivity.this, "数据上传成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @param barcode
     */
    //拦截扫描的信息
    @Override
    public void onScanSuccess(String barcode) {
//        sqLiteDatabase.execSQL("insert into users (name,unitprice,number,subtotal,Price,profit,QRCode)values(?,?,?,?,?,?,?)",new Object[]{"火鸡","3","5","5","1","1","1234567890004"});
        if (type == 0&&Cashbox_switch) {
            if (Rl_time.getVisibility() == View.VISIBLE) {

            } else {
                if (barcode.length() > 0) {
                    if (barcode.substring(0, 1).equals("2")) {
                           getseek(barcode);
                    } else {
                        if (barcode.length() > 14) {
                            barcode = barcode.substring(0, 13);
                        }
                    if (isnetworknew) {
//                    if (!isgetseek){
//                        isgetseek=true;
//                        getSeek(barcode);
//                    }else {
//
//                    }
                        getlocal(barcode);
                    } else {
                        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode=?", new String[]{barcode});
                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                commoditys = new Commodity();
                                commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                                commoditys.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
                                commoditys.setName(cursor.getString(cursor.getColumnIndex("name")));
                                commoditys.setPy(cursor.getString(cursor.getColumnIndex("py")));
                                commoditys.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                                commoditys.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                                commoditys.setCost(cursor.getString(cursor.getColumnIndex("cost")));
                                commoditys.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
                                commoditys.setTag_id(cursor.getString(cursor.getColumnIndex("tag_id")));
                                commoditys.setProduct_id(cursor.getString(cursor.getColumnIndex("product_id")));
                                commoditys.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
                                commoditys.setStore(cursor.getString(cursor.getColumnIndex("store")));
                                commoditys.setGood_limit(cursor.getString(cursor.getColumnIndex("good_limit")));
                                commoditys.setGood_stock(cursor.getString(cursor.getColumnIndex("good_stock")));
                                commoditys.setPD(cursor.getString(cursor.getColumnIndex("PD")));
                                commoditys.setGD(cursor.getString(cursor.getColumnIndex("GD")));
                                commoditys.setMarketable(cursor.getString(cursor.getColumnIndex("marketable")));
                                commoditys.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
                                commoditys.setCustom_member_price(cursor.getString(cursor.getColumnIndex("custom_member_price")));
                                commoditys.setType(Member_type);
                            }
                            if (commodities.size() > 0) {
                                int in = 0;
                                aa:
                                for (int k = 0; k < commodities.size(); k++) {
                                    if (commodities.get(k).getGoods_id().equals(commoditys.getGoods_id())) {
                                        in = in + (k + 1);
                                        break aa;
                                    }
                                }
                                if (in == 0) {
                                    shuliangEntty = new ShuliangEntty();
                                    shuliangEntty.setNumber(1);
                                    entty.add(shuliangEntty);
                                    commodities.add(commoditys);
                                } else {
                                    float i = entty.get(in - 1).getNumber();
                                    i++;
                                    entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);
                                    adapterzhu.getEntty(entty);
                                    adapterzhu.getadats(commodities);
                                    adapterzhu.notifyDataSetChanged();
                                    lv.setAdapter(adapterzhu);
                                    lv.setSelection(adapterzhu.getCount() - 1);
                                }
                            } else {
                                if (commoditys != null) {
                                    if (commodities.size() > 0) {
                                        shuliangEntty = new ShuliangEntty();
                                        shuliangEntty.setNumber(1);
                                        entty.add(commodities.size() - 1, shuliangEntty);
                                        commodities.add(commodities.size() - 1, commoditys);
                                    } else {
                                        shuliangEntty = new ShuliangEntty();
                                        shuliangEntty.setNumber(1);
                                        entty.add(0, shuliangEntty);
                                        commodities.add(0, commoditys);
                                    }
                                }
                            }
                            if (commoditys != null) {
                                tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
                                j = Double.parseDouble(tv_Total.getText().toString());
//                            if (layout_go_pay.getVisibility()==View.GONE) {
//                                j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
//                            } else {
                                if (commoditys.getType() != null && !commoditys.getType().equals("") && !commoditys.getType().equals("0")) {
                                    if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                        if (commoditys.getCustom_member_price() != null && !commoditys.getCustom_member_price().equals("")&& !commoditys.getCustom_member_price().equals("null")) {
                                            j = TlossUtils.add(j, Double.parseDouble(StringUtils.getStrings(commoditys.getCustom_member_price(), ",")[Integer.parseInt(commoditys.getType()) - 1]));
                                        } else {
                                            j = TlossUtils.add(j, Double.parseDouble(commoditys.getMember_price()));
                                        }
                                    } else {
                                        j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
                                    }
                                } else {
                                    j = TlossUtils.add(j, Double.parseDouble(commoditys.getPrice()));
                                }
//                                j = TlossUtils.add(j, Double.parseDouble(commoditys.getMember_price()));
//                            }
                                tv_Total.setText(StringUtils.stringpointtwo(j + ""));
                                int a = 0;
                                for (int i = 0; i < commodities.size(); i++) {
//                a += entty.get(i).getNumber();
                                }
                                tv_netreceipts.setText(j + "");
                                Showtotal(j + "");
                                et_keyoard.setText(j + "");

                                adapterzhu.getEntty(entty);
                                adapterzhu.getadats(commodities);
                                lv.setAdapter(adapterzhu);

                                lv.setSelection(adapterzhu.getCount() - 1);
                                if (number >= 0) {
                                    //广播的发送
                                    Intent mIntent = new Intent();
                                    mIntent.setAction("qwer");
                                    mIntent.putExtra("yaner", commoditys);
                                    //发送广播  
                                    sendBroadcast(mIntent);
                                    fuxiang = true;
                                    number++;
                                }
                            }
//        commodities.add(commoditys);
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.toast_layout, null);
                            TextView tv = (TextView) view.findViewById(R.id.tv);
                            tv.setText("没有该商品");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(view);
                            toast.show();
                        }
                    }
                }
            }else {
                    Toast.makeText(MainActivity.this,"商品条码为空请检查usb接口或者商品条码",Toast.LENGTH_SHORT).show();
                }
        }
        } else if (type == 1) {
            uppay(barcode);
        }
    }

    //搜索临时商品的
    public void getseek(String bncode){
        OkGo.post(SysUtils.getSellerorderUrl("getTempGoodsByBncode"))
                .tag("temporary")
                .cacheKey("cacheKey")
                .params("bncode",bncode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印搜索临时的商品数据"+s);
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(s);
                        JSONObject jsonObject1=jsonObject.getJSONObject("response");
                        String status=jsonObject1.getString("status");
                        if (status.equals("200")) {
                            JSONObject data=jsonObject1.getJSONObject("data");
                            data.getString("createtime");
                            data.getString("id");
                            Commodity commodity = new Commodity();
                            ShuliangEntty shuliang = new ShuliangEntty();
                            commodity.setName(data.getString("goods_name"));
                            commodity.setPrice(data.getString("total"));
                            shuliang.setNumber(1);
                            commodity.setMember_price(data.getString("total"));
                            commodity.setCost(data.getString("total")+"");
                            commodity.setStore(200 + "");
                            commodity.setGoods_id("null");
                            commodity.setType(Member_type);
                            commodities.add(commodity);
                            entty.add(shuliang);
                            adapterzhu.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //自定义键盘的监听
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_one:
                String myString1 = et_keyoard.getText().toString();
                myString1 += "1";
                if (getSumstr(myString1) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString1);
                    et_inputscancode.setText(getSumstr(myString1) + "");
                } else {
                    if (commodities.size() > 0) {
                    } else {
                        et_keyoard.setText(myString1);
                        tv_netreceipts.setText(getSumstr(myString1) + "");
                        Showtotal(getSumstr(myString1) + "");
                    }

//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString1) + "");
//                    } else {
//                        et_keyoard.setText(myString1);
//                        tv_netreceipts.setText(getSumstr(myString1) + "");
//                    }
                }
                break;
            case R.id.keyboard_two:
                String myString2 = et_keyoard.getText().toString();
                myString2 += "2";
                if (getSumstr(myString2) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString2);
                    et_inputscancode.setText(getSumstr(myString2) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString2) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString2);
                        tv_netreceipts.setText(getSumstr(myString2) + "");
                        Showtotal(getSumstr(myString2) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_three:
                String myString3 = et_keyoard.getText().toString();
                myString3 += "3";
                if (getSumstr(myString3) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString3);
                    et_inputscancode.setText(getSumstr(myString3) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString3) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString3);
                        tv_netreceipts.setText(getSumstr(myString3) + "");
                        Showtotal(getSumstr(myString3) + "");
                    }

//                    }
                }
                break;
            case R.id.keyboard_four:
                String myString4 = et_keyoard.getText().toString();
                myString4 += "4";
                if (getSumstr(myString4) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString4);
                    et_inputscancode.setText(getSumstr(myString4) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString4) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString4);
                        tv_netreceipts.setText(getSumstr(myString4) + "");
                        Showtotal(getSumstr(myString4) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_five:
                String myString5 = et_keyoard.getText().toString();
                myString5 += "5";
                if (getSumstr(myString5) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString5);
                    et_inputscancode.setText(getSumstr(myString5) + "");
                } else {
//                    if (!tv_netreceipts.getText().toString().equals("") && Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText((f1 + Float.parseFloat(myString5)) + "");
//                        tv_netreceipts.setText(getSumstr(myString5 + "+") + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString5);
                        tv_netreceipts.setText(getSumstr(myString5) + "");
                        Showtotal(getSumstr(myString5) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_six:
                String myString6 = et_keyoard.getText().toString();
                myString6 += "6";
                if (getSumstr(myString6) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString6);
                    et_inputscancode.setText(getSumstr(myString6) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString6) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString6);
                        tv_netreceipts.setText(getSumstr(myString6) + "");
                        Showtotal(getSumstr(myString6) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_seven:
                String myString7 = et_keyoard.getText().toString();
                myString7 += "7";
                if (getSumstr(myString7) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString7);
                    et_inputscancode.setText(getSumstr(myString7) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString7) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString7);
                        tv_netreceipts.setText(getSumstr(myString7) + "");
                        Showtotal(getSumstr(myString7) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_eight:
                String myString8 = et_keyoard.getText().toString();
                myString8 += "8";
                if (getSumstr(myString8) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString8);
                    et_inputscancode.setText(getSumstr(myString8) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString8) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString8);
                        tv_netreceipts.setText(getSumstr(myString8) + "");
                        Showtotal(getSumstr(myString8) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_nine:
                String myString9 = et_keyoard.getText().toString();
                myString9 += "9";
                if (getSumstr(myString9) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString9);
                    et_inputscancode.setText(getSumstr(myString9) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString9) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString9);
                        tv_netreceipts.setText(getSumstr(myString9) + "");
                        Showtotal(getSumstr(myString9) + "");
                    }
//                    }
                }
                break;
            case R.id.keyboard_zro:
                String myString0 = et_keyoard.getText().toString();
                myString0 += "0";
                if (getSumstr(myString0) >= MIXMONEY) {
                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isSelected) {
                    et_keyoard.setText(myString0);
                    et_inputscancode.setText(getSumstr(myString0) + "");
                } else {
//                    if (Float.parseFloat(tv_netreceipts.getText().toString()) > 0) {
//                        Float f1 = Float.parseFloat(tv_netreceipts.getText().toString());
//                        et_keyoard.setText(f1 + "+");
//                        tv_netreceipts.setText(getSumstr(myString0) + "");
//                    } else {
                    if (commodities.size() > 0) {

                    } else {
                        et_keyoard.setText(myString0);
                        tv_netreceipts.setText(getSumstr(myString0) + "");
                        Showtotal(getSumstr(myString0) + "");
                    }
//                    }
                }
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

//                String str_et_realitygetmoney=et_keyoard.getText().toString().trim();
//                boolean istrue=true;
//                for(int i=1;i<=str_et_realitygetmoney.length();i++){
//                    if((str_et_realitygetmoney.substring(str_et_realitygetmoney.length()-i,str_et_realitygetmoney.length()-i+1)).equals(".")){
//                        istrue=false;
//                    }
//                }
//                if(istrue){
//                    str_et_realitygetmoney += ".";
//                    et_keyoard.setText(str_et_realitygetmoney);
//                }else {
//                    et_keyoard.setText(str_et_realitygetmoney);
//                }

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
                    String cancell_str = et_keyoard.getText().toString().trim();
                    if (cancell_str.length() <= 0) {
                        tv_netreceipts.setText("");
                        return;
                    }
                    et_keyoard.setText(cancell_str.substring(0, cancell_str.length() - 1));
                    if ((getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "") == "") {
                        tv_netreceipts.setText("");
                    } else {
                        tv_netreceipts.setText(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
                        Showtotal(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
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
                                Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {

                    CharSequence myStringadd = et_keyoard.getText();
//                    CharSequence myStringadd=tv_netreceipts.getText().toString() + "+";
                    et_keyoard.setText(myStringadd);
//                        et_keyoard.setText(tv_netreceipts.getText().toString() + "+");
                    String str = myStringadd.toString();
//                    String str=tv_netreceipts.getText().toString() + "+";
                    if (myStringadd.length() > 0) {
                        if ((str.substring(myStringadd.length() - 1)).equals("+") || (str.substring(myStringadd.length() - 1)).equals(".")) {
                            et_keyoard.setText(myStringadd);
                        } else {
                            if (commodities.size() > 0) {

                            } else {
                                str += "+";
                                et_keyoard.setText(str);
                                if (getSumstr(str) <= MIXMONEY) {
                                    tv_netreceipts.setText(getSumstr(str) + "");
                                    Showtotal(getSumstr(str) + "");
                                } else {
                                    Toast.makeText(MainActivity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                }
                break;

            case R.id.but_Ten:
                if (isSelected) {
                    if (!et_inputscancode.getText().toString().equals("")) {
                        et_keyoard.setText(et_inputscancode.getText().toString() + "+" + "10");
                        float f1 = 10 + Float.parseFloat(et_inputscancode.getText().toString());
                        et_inputscancode.setText(f1 + "");
                        String string1 = tv_netreceipts.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 10 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }

                    } else {
                        et_keyoard.setText(10 + "");
                        et_inputscancode.setText(10 + "");
                        String string1 = et_inputscancode.getText().toString();
                        float i1 = 10 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    }
                } else {
                    if (!tv_netreceipts.getText().toString().equals("")) {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(tv_netreceipts.getText().toString() + "+" + "10");
                            float f1 = 10 + Float.parseFloat(tv_netreceipts.getText().toString());
                            tv_netreceipts.setText(f1 + "");
                            Showtotal(f1 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 10 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    } else {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(10 + "");
                            tv_netreceipts.setText(10 + "");
                            Showtotal(10 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 10 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    }
                }
                break;
            case R.id.but_Twenty:
                if (isSelected) {
                    if (!et_inputscancode.getText().toString().equals("")) {
                        et_keyoard.setText(et_inputscancode.getText().toString() + "+" + "20");
                        float f1 = 20 + Float.parseFloat(et_inputscancode.getText().toString());
                        et_inputscancode.setText(f1 + "");
                        String string1 = tv_netreceipts.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 20 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    } else {
                        et_keyoard.setText(20 + "");
                        et_inputscancode.setText(20 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 20 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    }
                } else {
                    if (!tv_netreceipts.getText().toString().equals("")) {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(tv_netreceipts.getText().toString() + "+" + "20");
                            float f1 = 20 + Float.parseFloat(tv_netreceipts.getText().toString());
                            tv_netreceipts.setText(f1 + "");
                            Showtotal(f1 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 20 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    } else {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(20 + "");
                            tv_netreceipts.setText(20 + "");
                            Showtotal(20 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 20 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    }
                }
                break;
            case R.id.but_Fifty:
                if (isSelected) {
                    if (!et_inputscancode.getText().toString().equals("")) {
                        et_keyoard.setText(et_inputscancode.getText().toString() + "+" + "50");
                        float f1 = 50 + Float.parseFloat(et_inputscancode.getText().toString());
                        et_inputscancode.setText(f1 + "");
                        String string1 = tv_netreceipts.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 50 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    } else {
                        et_keyoard.setText(50 + "");
                        et_inputscancode.setText(50 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 50 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    }
                } else {
                    if (!tv_netreceipts.getText().toString().equals("")) {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(tv_netreceipts.getText().toString() + "+" + "50");
                            float f1 = 50 + Float.parseFloat(tv_netreceipts.getText().toString());
                            tv_netreceipts.setText(f1 + "");
                            Showtotal(f1 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 50 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    } else {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(50 + "");
                            tv_netreceipts.setText(50 + "");
                            Showtotal(50 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 50 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    }
                }
                break;
            case R.id.but_Onehundred:
                if (isSelected) {
                    if (!et_inputscancode.getText().toString().equals("")) {
                        et_keyoard.setText(et_inputscancode.getText().toString() + "+" + "100");
                        float f1 = 100 + Float.parseFloat(et_inputscancode.getText().toString());
                        et_inputscancode.setText(f1 + "");
                        String string1 = tv_netreceipts.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 100 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    } else {
                        et_keyoard.setText(100 + "");
                        et_inputscancode.setText(100 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
                        }
                        float i1 = 100 - Float.parseFloat(string1);
                        if (i1 >= 0) {
                            tv_Surplus.setText(i1 + "");
                        } else {
                            tv_Surplus.setText("0");
                        }
                    }
                } else {
                    if (!tv_netreceipts.getText().toString().equals("")) {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(tv_netreceipts.getText().toString() + "+" + "100");
                            float f1 = 100 + Float.parseFloat(tv_netreceipts.getText().toString());
                            tv_netreceipts.setText(f1 + "");
                            Showtotal(f1 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 100 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    } else {
                        if (commodities.size() > 0) {

                        } else {
                            et_keyoard.setText(100 + "");
                            tv_netreceipts.setText(100 + "");
                            Showtotal(100 + "");
                            String string1 = tv_netreceipts.getText().toString();
                            if(TextUtils.isEmpty(string1)){
                                string1="0.0";
                            }
                            float i1 = 100 - Float.parseFloat(string1);
                            if (i1 >= 0) {
                                tv_Surplus.setText(i1 + "");
                            } else {
                                tv_Surplus.setText("0");
                            }
                        }
                    }
                }
                break;
        }

    }

    //总金额
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

    //获得选中itme的接口回掉
    @Override
    public void setOnClickListener(List<ListEntty> listEntty, int i, ImageView im_add, ImageView im_reductionof) {
        this.Entty = listEntty;
        this.k = i;
        this.im_add = im_add;
        this.im_reductionof = im_reductionof;
    }

    //    获得选中的itme的保存实体类
    @Override
    public void getEntty(List<ShuliangEntty> entty) {
//        this.entty = entty;
    }

    //模糊筛选的结果
    @Override
    public void setClickListener(int i) {
//        list_fuzzy.get(i).getBncode();
//        onScanSuccess(list_fuzzy.get(i).getBncode());
        if (!list_fuzzy.get(i).getCook_position().equals("0")) {
            getweight(list_fuzzy.get(i));
        }else {
//            if (layout_go_pay.getVisibility()==View.GONE){
//                AddCommodity(list_fuzzy.get(i),1,list_fuzzy.get(i).getPrice());
//            }else {

                if (list_fuzzy.get(i).getType()!=null&&!list_fuzzy.get(i).getType().equals("")&&!list_fuzzy.get(i).getType().equals("0")) {
                    if (list_fuzzy.get(i).getIs_special_offer()!=null) {
                        if (list_fuzzy.get(i).getIs_special_offer().equals("no")) {
                            if (list_fuzzy.get(i).getCustom_member_price() != null && !list_fuzzy.get(i).getCustom_member_price().equals("")&& !list_fuzzy.get(i).getCustom_member_price().equals("null")) {
                                AddCommodity(list_fuzzy.get(i), 1, StringUtils.getStrings(list_fuzzy.get(i).getCustom_member_price(), ",")[Integer.parseInt(list_fuzzy.get(i).getType()) - 1]);
                            } else {
                                AddCommodity(list_fuzzy.get(i), 1, list_fuzzy.get(i).getMember_price());
                            }
                        }else {
                            AddCommodity(list_fuzzy.get(i), 1, list_fuzzy.get(i).getPrice());
                        }
                    }else {
                        AddCommodity(list_fuzzy.get(i), 1, list_fuzzy.get(i).getPrice());
                    }
                }else {
                    AddCommodity(list_fuzzy.get(i),1,list_fuzzy.get(i).getPrice());
                }
//                AddCommodity(list_fuzzy.get(i),1,list_fuzzy.get(i).getMember_price());
//            }

        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        dialogfuzzy.dismiss();
    }

    //会员的监听
    @Override
    public void setonclick(final int i) {

        rl_jishuang.setVisibility(View.GONE);
        layout_go_pay.setVisibility(View.VISIBLE);

        if (member_entties.get(i).getMember_lv_custom_key().equals("0")){
            Member_type="0";
        }else {
            Log.e("print","打印的数据获取会员价的数据"+member_entties.get(i).getMember_lv_custom_key());
            Member_type=member_entties.get(i).getMember_lv_custom_key();
            setType(Member_type,SharedUtil.getfalseBoolean("sw_member_price"));
            getwholesale(Member_type,SharedUtil.getfalseBoolean("sw_member_price"),"0","0");
//            adapterzhu.setType(SharedUtil.getfalseBoolean("sw_member_price"));
        }

        if (SharedUtil.getString("Discount")!=null){
            ed_Discount.setText(SharedUtil.getString("Discount"));
        }

        ed_Discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("设置会员折扣");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_supplement);
                Button but_add = (Button) window.findViewById(R.id.but_add);
                TextView tv_t1 = (TextView) window.findViewById(R.id.tv_t1);
                final EditText ed_add= (EditText) window.findViewById(R.id.ed_add);
                tv_t1.setText("设置会员折扣");
                ed_add.setHint("请设置会员的通用折扣");
                but_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ed_add.getText().toString().equals("")) {
                            if (Double.parseDouble(ed_add.getText().toString()) > 0 && Double.parseDouble(ed_add.getText().toString()) <= 1) {
                                SharedUtil.putString("Discount", ed_add.getText().toString());
                                ed_Discount.setText(ed_add.getText().toString());
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "输入的数据为0-1", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            SharedUtil.putString("Discount", ed_add.getText().toString());
                            ed_Discount.setText(ed_add.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        pbmember_id = member_entties.get(i).getMember_id();
        Score=member_entties.get(i).getScore();
        is_require_pass=member_entties.get(i).getIs_require_pass();

        tv_discount.setText(member_entties.get(i).getDiscount_rate());
        tv_name1.setText(member_entties.get(i).getMember_name());
        if (member_entties.get(i).getMember_lv_custom_key().equals("0")){
            tv_grade.setText("普通会员");
        }else {
            tv_grade.setText(member_entties.get(i).getMember_lv_custom_name());
        }
        tv_balance1.setText(member_entties.get(i).getSurplus());
        tv_phone.setText(member_entties.get(i).getMobile());
        tv_integral1.setText(member_entties.get(i).getScore());

        if (member_entties.get(i).getBirthday()!=null&&!member_entties.get(i).getBirthday().equals("null")){
            tv_birthday.setText(DateUtils.getDateTimeFromMillisecond((long)Long.parseLong(member_entties.get(i).getBirthday())*1000));
        }
        tv_time.setText(DateUtils.getDateTimeFromMillisecond((long)Integer.parseInt(member_entties.get(i).getTime())*1000));
        tv_remark.setText(member_entties.get(i).getRemark());

        but_self_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commodities.size() > 0) {
                    if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
                        Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                        tv_netreceipts.setText(tolot + "");
                        pay_type = "1";
                        Member_type="0";
                        layout_go_pay.setVisibility(View.GONE);
                        rl_jishuang.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(MainActivity.this,"付款金额为负数",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"请选择商品",Toast.LENGTH_SHORT).show();
                }
            }
        });
        but_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Yuezhifu(1.0f,i);
            }
        });

        but_balance_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float total_amount = 0;
                if (commodities.size() > 0) {
                    double dis_Total=0;
                    double Total=0;
                    double Total1=0;
                    for (int i=0;i<commodities.size();i++){
                        if (commodities.get(i).getIs_special_offer()!=null){
                            if (commodities.get(i).getIs_special_offer().equals("no")){
                                if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                                    if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                        if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                            dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType())-1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        } else {
                                            dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    }else {
                                        dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                                    }
                                }else {
                                    dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                                }
                            }else {
                                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                            }
                        }else {
                            if (!commodities.get(i).getGoods_id().equals("null")&&commodities.get(i).getGoods_id()!=null){
                                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                            }else {
                                dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                            }
//                            Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                        }
                    }
                    if (!ed_Discount.getText().toString().equals("")&&ed_Discount.getText().toString()!=null){
                        dis_Total=TlossUtils.mul(dis_Total,Double.parseDouble(ed_Discount.getText().toString()));
                    }else {
                        dis_Total=TlossUtils.mul(dis_Total,1);
                        ed_Discount.setText("1");
                    }
                    Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Double.parseDouble(member_entties.get(i).getDiscount_rate())));
                    Total1=TlossUtils.add(Total,dis_Total);
//                        discount=true;
//                        _discount=TlossUtils.sub(Total1,Total)+"";

//                        _Memberdiscount=(float) Double.parseDouble(ed_discount.getText().toString())/10;
//
//                        tv_Total.setText(Total+"");
//                        tv_netreceipts.setText(Total+"");
//                        Showtotal(Total+"");


//                    Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                    Log.d("print","打印总价为余额支付"+Total);
                    Double tolot = Total;
                    if (Double.parseDouble(member_entties.get(i).getSurplus()) >= tolot) {
                        Log.e("print", "余额支付传过去的数据为" + tv_netreceipts.getText().toString());
                        _Total=tv_netreceipts.getText().toString();
                        //优惠金额
                        final Double Discount = TlossUtils.sub(Double.parseDouble(tv_netreceipts.getText().toString()), tolot);
                        //会员折扣
                        final float Memberdiscount = (float) TlossUtils.mul(Double.parseDouble(member_entties.get(i).getDiscount_rate()), Double.parseDouble(ed_Discount.getText().toString()));
                        //余额
                        final Double balance = TlossUtils.sub(Double.parseDouble(member_entties.get(i).getSurplus()), tolot);

                        tv_netreceipts.setText(tolot + "");
                        Showtotal(tolot + "");
                        mapList.clear();
                        for (int i = 0; i < commodities.size(); i++) {
                            Map<String, String> map1 = new HashMap<>();
                            if (!commodities.get(i).getName().equals("会员充值")) {
                                map1.put("goods_id", commodities.get(i).getGoods_id());
                                map1.put("name", commodities.get(i).getName());
                                map1.put("number", entty.get(i).getNumber() + "");
                                map1.put("nums", entty.get(i).getNumber() + "");
//                                if (iswholesale) {
//                                    map1.put("price", commodities.get(i).getPrice());
//                                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
//                                    map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
//                                    map1.put("cost", commodities.get(i).getCost() + "");
////                                    map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getPrice())) + "");
//                                } else {
//                                    map1.put("price", commodities.get(i).getMember_price());
//                                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
//                                    map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                                    map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getMember_price())) + "");
//                                }

                                String price=commodities.get(i).getPrice();
                                double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                                if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                                    if (commodities.get(i).getIs_special_offer()!=null){
                                        if (commodities.get(i).getIs_special_offer().equals("no")) {
                                            if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                                if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                    price=StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                                }
                                            } else {
                                                if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                    price=commodities.get(i).getMember_price();
                                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                                }
                                            }
                                        }
                                    } else {
                                        if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                            price=commodities.get(i).getPrice();
                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    }
                                }else {
                                    if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                        price=commodities.get(i).getPrice();
                                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                    }
                                }

                                map1.put("price", price);
                                total_amount += amount;
                                map1.put("amount", amount + "");
                                map1.put("cost", commodities.get(i).getCost() + "");

                                map1.put("py", commodities.get(i).getPy());
                                map1.put("PD", commodities.get(i).getPD());
                                map1.put("GD", commodities.get(i).getGD());
                                map1.put("bn", commodities.get(i).getBn());
                                map1.put("product_id", commodities.get(i).getProduct_id());
                                map1.put("good_limit", commodities.get(i).getGood_limit());
                                map1.put("good_stock", commodities.get(i).getGood_stock());
                                map1.put("marketable", commodities.get(i).getMarketable());
                                map1.put("tag_name", commodities.get(i).getTag_name());
                                map1.put("tag_id", commodities.get(i).getTag_id());
                                map1.put("unit", commodities.get(i).getUnit());
                                map1.put("unit_id", commodities.get(i).getUnit_id() + "");
                                map1.put("bncode", commodities.get(i).getBncode());
//                map1.put("goods_id", commodities.get(i).getGoods_id());
//                map1.put("name", commodities.get(i).getName());
//                map1.put("number", entty.get(i).getNumber() + "");
//                map1.put("cost", commodities.get(i).getCost());
//                map1.put("price", commodities.get(i).getPrice());
                                map1.put("orders_status", 1 + "");
                                map1.put("pay_status", 0 + "");
//            map1.put("py", commodities.get(i).getPy());
//            map1.put("PD", commodities.get(i).getPD());
//            map1.put("GD", commodities.get(i).getGD());
//            map1.put("product_id", commodities.get(i).getProduct_id());
//            map1.put("good_limit", commodities.get(i).getGood_limit());
//            map1.put("good_stock", commodities.get(i).getGood_stock());
//            map1.put("marketable", commodities.get(i).getMarketable());
//            map1.put("tag_name", commodities.get(i).getTag_name());
//            map1.put("tag_id", commodities.get(i).getTag_id());
//            map1.put("unit", commodities.get(i).getUnit());
//            map1.put("unit_id", commodities.get(i).getUnit_id() + "");
//            map1.put("bncode", commodities.get(i).getBncode());
                            } else {
                                map1.put("name", commodities.get(i).getName());
                                map1.put("number", "1");
                                map1.put("nums", "1");
                                map1.put("cost", commodities.get(i).getCost());
                                map1.put("price", tv_netreceipts.getText().toString() + "");
                                map1.put("amount", tv_netreceipts.getText().toString() + "");
                            }
                            mapList.add(map1);
                        }
                        Gson gson = new Gson();
                        final String str = gson.toJson(mapList);
                        Log.d("print","打印是否免密"+is_require_pass);
                        if (is_require_pass.equals("no")){
                            android.support.v7.app.AlertDialog dialog2 = null;
                            getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), member_entties.get(i).getMobile(), true);
                        }else {
                            //余额支付方法
                            if (!password.equals("") && password.length() == 15) {
                                android.support.v7.app.AlertDialog dialog2 = null;
                                getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), password,false);
                            } else {
                                final android.support.v7.app.AlertDialog dialog2 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                                dialog2.show();
                                Window window = dialog2.getWindow();
                                window.setContentView(R.layout.dialog_memberpaw);
                                dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                                Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                                Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
                                TextView tv_memberprice = (TextView) window.findViewById(R.id.tv_memberprice);

                                Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                                but_cancel.setVisibility(View.GONE);
                                tv_memberprice.setVisibility(View.GONE);

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
                                                getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString(),false);
                                            } else {
                                                Toast.makeText(MainActivity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                });
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        but_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                dialog.show();
                final Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_recharge);
                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                lv_recharge = (ListView) window.findViewById(R.id.lv_recharge);
                adapter_specification = new Adapter_specification(MainActivity.this);
                adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
                    @Override
                    public void onclickdialog(int i) {
                        if (commodities.size() == 0) {
                            specification_unms = i;
                            recharge_id=specification_list.get(i).getRecharge_id();
                            Commodity commodity = new Commodity();
                            ShuliangEntty shuliang = new ShuliangEntty();
                            commodity.setName("会员充值");
                            commodity.setPrice(specification_list.get(i).getVal());
                            shuliang.setNumber(1);
                            commodity.setMember_price(specification_list.get(i).getVal());
                            commodity.setCost(TlossUtils.add(Double.parseDouble(specification_list.get(i).getVal()),Double.parseDouble(specification_list.get(i).getGive()))+"");
                            commodity.setStore(200 + "");
                            commodity.setGoods_id("null");
                            commodity.setType(Member_type);
                            commodities.add(commodity);
                            entty.add(shuliang);
                            adapterzhu.getadats(commodities);
                            adapterzhu.getEntty(entty);
                            lv.setAdapter(adapterzhu);
                            dialog.dismiss();

                            tv_Totalmerchandise.setText("1");
                            tv_Total.setText(specification_list.get(i).getVal());
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                            tv_netreceipts.setText(specification_list.get(i).getVal());
                            Showtotal(specification_list.get(i).getVal());
//                                            Map<String, String> map = new HashMap<String, String>();
//                                            map.put("member_id", member_entty.getMember_id());
//                                            map.put("surplus", Integer.parseInt(specification_list.get(i).getGive()) +
//                                                    Integer.parseInt(specification_list.get(i).getVal()) + "");
//                                            Gson gson = new Gson();
//                                            String s1 = gson.toJson(map);
//                                            UPmoney(s1);
                            rl_jishuang.setVisibility(View.VISIBLE);
                            layout_go_pay.setVisibility(View.GONE);
                            Intent mIntent = new Intent();
                            mIntent.setAction("qwer");
                            mIntent.putExtra("yaner", commodity);
                            //发送广播  
                            sendBroadcast(mIntent);

                            Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                            but_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this,"请清除列表商品",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getData("1");
            }
        });

        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getexchange();
            }
        });

        but_eductible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                dialog.show();
                final Window window = dialog.getWindow();
                dialog.setCanceledOnTouchOutside(false);
                window.setContentView(R.layout.dialog_recharge);
                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                lv_recharge = (ListView) window.findViewById(R.id.lv_recharge);
                adapter_specification = new Adapter_specification(MainActivity.this);
                adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
                    @Override
                    public void onclickdialog(int i) {
                        if (Integer.parseInt(specification_list.get(i).getVal())<Integer.parseInt(tv_integral1.getText().toString())){
                            specification_unms = i;
                            is_score_pay="yes";
                            pay_score=specification_list.get(i).getVal();
                            tv_integral1.setText(Integer.parseInt(tv_integral1.getText().toString())-Integer.parseInt(pay_score)+"");
                            Commodity commodity = new Commodity();
                            ShuliangEntty shuliang = new ShuliangEntty();
                            commodity.setName("活动立减");
                            commodity.setPrice("-"+specification_list.get(i).getGive());
                            shuliang.setNumber(1);
                            commodity.setCost("0");
                            commodity.setStore(200 + "");
                            commodity.setGoods_id("null");
                            commodity.setType(Member_type);
                            commodities.add(commodity);
                            entty.add(shuliang);
                            adapterzhu.getadats(commodities);
                            adapterzhu.getEntty(entty);
                            lv.setAdapter(adapterzhu);
                            dialog.dismiss();

                            Toast.makeText(MainActivity.this,"抵扣"+specification_list.get(i).getGive()+"成功",Toast.LENGTH_SHORT).show();

                            double dis_Total=0;
                            double Total=0;
                            double Total1=0;
                            for (int j=0;j<commodities.size();j++){
                                if (commodities.get(j).getIs_special_offer()!=null){
//                                    if (commodities.get(j).getIs_special_offer().equals("no")){
//                                        dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
//                                    }else {
//                                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
//                                    }
                                    if (commodities.get(i).getIs_special_offer().equals("no")){
                                        if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                                            if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                            if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                                dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType())-1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                            } else {
                                                dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                            }
                                        }else {
                                                dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                            }
                                        }else {
                                            dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                                        }

                                    }else {
                                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                                    }
                                }else {
                                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
                                }
                            }
                            Total=TlossUtils.add(Total,dis_Total);
                            tv_Totalmerchandise.setText(commodities.size()+"");
                            tv_Total.setText(Total+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                            tv_netreceipts.setText(Total+"");
                            Showtotal(Total+"");
//                                            Map<String, String> map = new HashMap<String, String>();
//                                            map.put("member_id", member_entty.getMember_id());
//                                            map.put("surplus", Integer.parseInt(specification_list.get(i).getGive()) +
//                                                    Integer.parseInt(specification_list.get(i).getVal()) + "");
//                                            Gson gson = new Gson();
//                                            String s1 = gson.toJson(map);
//                                            UPmoney(s1)
                            Intent mIntent = new Intent();
                            mIntent.setAction("qwer");
                            mIntent.putExtra("yaner", commodity);
                            //发送广播  
                            sendBroadcast(mIntent);

                            Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                            but_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this,"积分不足",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getData("2");
            }
        });
        dialog_member.dismiss();
//        final Dialog dialog = new Dialog(MainActivity.this);
//        dialog.setTitle("会员");
//        dialog.show();
//        Window window = dialog.getWindow();
//        dialog.setCanceledOnTouchOutside(false);
//        window.setContentView(R.layout.dialog_member_information);
//        TextView tv_card_number = (TextView) window.findViewById(R.id.tv_card_number);
//        TextView tv_discount = (TextView) window.findViewById(R.id.tv_discount);
//        TextView tv_name = (TextView) window.findViewById(R.id.tv_name);
//        tv_balance = (TextView) window.findViewById(R.id.tv_balance);
//        TextView tv_phone = (TextView) window.findViewById(R.id.tv_phone);
//        tv_integral = (TextView) window.findViewById(R.id.tv_integral);
//        TextView tv_birthday = (TextView) window.findViewById(R.id.tv_birthday);
//        TextView tv_time = (TextView) window.findViewById(R.id.tv_time);
//        TextView tv_remark = (TextView) window.findViewById(R.id.tv_remark);
//        Button but_discount = (Button) window.findViewById(R.id.but_discount);
//        Button but_recharge = (Button) window.findViewById(R.id.but_recharge);
//        Button but_balance_paid = (Button) window.findViewById(R.id.but_balance_paid);
//        Button but_self_discount = (Button) window.findViewById(R.id.but_self_discount);
//
//        Button but_eductible= (Button) window.findViewById(R.id.but_eductible);
//
//        final EditText ed_Discount= (EditText) window.findViewById(R.id.ed_Discount);
//
//        if (SharedUtil.getString("Discount")!=null){
//            ed_Discount.setText(SharedUtil.getString("Discount"));
//        }
//
//        ed_Discount.addTextChangedListener(new TextWatcher() {
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
//                SharedUtil.putString("Discount",editable.toString());
//            }
//        });
//
//        pbmember_id = member_entties.get(i).getMember_id();
//        Score=member_entties.get(i).getScore();
//        is_require_pass=member_entties.get(i).getIs_require_pass();
//
//        tv_card_number.setText(member_entties.get(i).getMobile());
//        tv_discount.setText(member_entties.get(i).getDiscount_rate());
//        tv_name.setText(member_entties.get(i).getMember_name());
//        tv_balance.setText(member_entties.get(i).getSurplus());
//        tv_phone.setText(member_entties.get(i).getMobile());
//        tv_integral.setText(member_entties.get(i).getScore());
//        if (member_entties.get(i).getBirthday()!=null&&!member_entties.get(i).getBirthday().equals("null")){
//            tv_birthday.setText(DateUtils.getDateTimeFromMillisecond((long)Long.parseLong(member_entties.get(i).getBirthday())*1000));
//        }
//        tv_time.setText(DateUtils.getDateTimeFromMillisecond((long)Integer.parseInt(member_entties.get(i).getTime())*1000));
//        tv_remark.setText(member_entties.get(i).getRemark());
//
//        but_self_discount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (commodities.size() > 0) {
//                    if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
//                        Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
//                        tv_netreceipts.setText(tolot + "");
//                        pay_type = "1";
//                        dialog.dismiss();
//                    }else {
//                        Toast.makeText(MainActivity.this,"付款金额为负数",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(MainActivity.this,"请选择商品",Toast.LENGTH_SHORT).show();
//                }
////                float total_amount = 0;
////                if (commodities.size() > 0) {
////                    if (Double.parseDouble(member_entties.get(i).getSurplus()) >= Double.parseDouble(tv_netreceipts.getText().toString())) {
////                        final Dialog dialog1 = new Dialog(MainActivity.this);
////                        dialog1.setTitle("折扣");
////                        dialog1.show();
////                        Window window = dialog1.getWindow();
////                        window.setContentView(R.layout.discount_dialog);
////                        final EditText ed_discount = (EditText) window.findViewById(R.id.ed_discount);
////
////                        Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
////                        but_abolish.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                dialog1.dismiss();
////                            }
////                        });
////
////                        Button but_submit= (Button) window.findViewById(R.id.but_submit);
////                        but_submit.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                double dis_Total=0;
////                                double Total=0;
////                                double Total1=0;
////                                if (commodities.size()>0&&!ed_discount.getText().toString().equals("")){
//////                                    for (int i=0;i<commodities.size();i++){
//////                                        dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//////                                    }
//////
//////                                    discount=true;
//////                                    _discount=TlossUtils.sub(dis_Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(ed_discount.getText().toString())/10)))+"";
//////                                    tv_Total.setText(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
//////                                    tv_netreceipts.setText(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
//////                                    Showtotal(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
////                                    for (int i=0;i<commodities.size();i++){
////                                        if (commodities.get(i).getIs_special_offer()!=null){
////                                        if (commodities.get(i).getIs_special_offer().equals("no")){
////                                            dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
////                                        }else {
////                                            Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
////                                        }
////                                        }else {
////                                            Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
////                                        }
////                                    }
////                                    Total1=TlossUtils.add(Total,dis_Total);
////                                    Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(ed_discount.getText().toString())/10)));
////                                    discount=true;
////                                    _discount=TlossUtils.sub(Total1,Total)+"";
////
////                                    _Memberdiscount=(float) Double.parseDouble(ed_discount.getText().toString())/10;
////
////                                    tv_Total.setText(Total+"");
////                                    tv_netreceipts.setText(Total+"");
////                                    Showtotal(Total+"");
////
////                                    Yuezhifu((float) (Double.parseDouble(ed_discount.getText().toString())/10),i,dialog);
//////                                            tv_netreceipts.setText(specification_list.get(i).getVal());
////                                }
////                                dialog1.dismiss();
////                            }
////                        });
////
////                    }
////                }
//            }
//        });
//
//        //折扣
//        but_discount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Yuezhifu(1.0f,i,dialog);
//            }
//        });
//
//        //抵扣的数据
//        but_eductible.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
//                dialog.show();
//                final Window window = dialog.getWindow();
//                dialog.setCanceledOnTouchOutside(false);
//                window.setContentView(R.layout.dialog_recharge);
//                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
//                but_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                lv_recharge = (ListView) window.findViewById(R.id.lv_recharge);
//                adapter_specification = new Adapter_specification(MainActivity.this);
//                adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
//                    @Override
//                    public void onclickdialog(int i) {
//                        if (Integer.parseInt(specification_list.get(i).getVal())<Integer.parseInt(tv_integral.getText().toString())){
//                            specification_unms = i;
//                            is_score_pay="yes";
//                            pay_score=specification_list.get(i).getVal();
//                            tv_integral.setText(Integer.parseInt(tv_integral.getText().toString())-Integer.parseInt(pay_score)+"");
//                            Commodity commodity = new Commodity();
//                            ShuliangEntty shuliang = new ShuliangEntty();
//                            commodity.setName("活动立减");
//                            commodity.setPrice("-"+specification_list.get(i).getGive());
//                            shuliang.setNumber(1);
//                            commodity.setCost("0");
//                            commodity.setStore(200 + "");
//                            commodity.setGoods_id("null");
//                            commodities.add(commodity);
//                            entty.add(shuliang);
//                            adapterzhu.getadats(commodities);
//                            adapterzhu.getEntty(entty);
//                            lv.setAdapter(adapterzhu);
//                            dialog.dismiss();
//
//                            Toast.makeText(MainActivity.this,"抵扣"+specification_list.get(i).getGive()+"成功",Toast.LENGTH_SHORT).show();
//
//                            double dis_Total=0;
//                            double Total=0;
//                            double Total1=0;
//                            for (int j=0;j<commodities.size();j++){
//                                if (commodities.get(j).getIs_special_offer()!=null){
//                                    if (commodities.get(j).getIs_special_offer().equals("no")){
//                                            dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
//                                    }else {
//                                            Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
//                                    }
//                                }else {
//                                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(entty.get(j).getNumber()+"")));
//                                }
//                            }
//                            Total=TlossUtils.add(Total,dis_Total);
//                            tv_Totalmerchandise.setText(commodities.size()+"");
//                            tv_Total.setText(Total+"");
////                                            tv_netreceipts.setText(specification_list.get(i).getVal());
//                            tv_netreceipts.setText(Total+"");
//                            Showtotal(Total+"");
////                                            Map<String, String> map = new HashMap<String, String>();
////                                            map.put("member_id", member_entty.getMember_id());
////                                            map.put("surplus", Integer.parseInt(specification_list.get(i).getGive()) +
////                                                    Integer.parseInt(specification_list.get(i).getVal()) + "");
////                                            Gson gson = new Gson();
////                                            String s1 = gson.toJson(map);
////                                            UPmoney(s1)
//                            Intent mIntent = new Intent();
//                            mIntent.setAction("qwer");
//                            mIntent.putExtra("yaner", commodity);
//                            //发送广播  
//                            sendBroadcast(mIntent);
//
//                            Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
//                            but_cancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog.dismiss();
//                                }
//                            });
//                        }else {
//                            Toast.makeText(MainActivity.this,"积分不足",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                getData("2");
//            }
//        });
//
//        //余额支付
//        but_balance_paid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                float total_amount = 0;
//                if (commodities.size() > 0) {
//                    double dis_Total=0;
//                    double Total=0;
//                    double Total1=0;
//                    for (int i=0;i<commodities.size();i++){
//                        if (commodities.get(i).getIs_special_offer()!=null){
//                            if (commodities.get(i).getIs_special_offer().equals("no")){
//                                    dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//                            }else {
//                                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//                            }
//                        }else {
//                                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
//                        }
//                    }
//                    if (!ed_Discount.getText().toString().equals("")&&ed_Discount.getText().toString()!=null){
//                        dis_Total=TlossUtils.mul(dis_Total,Double.parseDouble(ed_Discount.getText().toString()));
//                    }else {
//                        dis_Total=TlossUtils.mul(dis_Total,1);
//                        ed_Discount.setText("1");
//                    }
//                    Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Double.parseDouble(member_entties.get(i).getDiscount_rate())));
//                    Total1=TlossUtils.add(Total,dis_Total);
////                        discount=true;
////                        _discount=TlossUtils.sub(Total1,Total)+"";
//
////                        _Memberdiscount=(float) Double.parseDouble(ed_discount.getText().toString())/10;
////
////                        tv_Total.setText(Total+"");
////                        tv_netreceipts.setText(Total+"");
////                        Showtotal(Total+"");
//
//
////                    Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
//                    Double tolot = Total;
//                    if (Double.parseDouble(member_entties.get(i).getSurplus()) >= tolot) {
//                        Log.e("print", "余额支付传过去的数据为" + tv_netreceipts.getText().toString());
//                        _Total=tv_netreceipts.getText().toString();
//                        //优惠金额
//                        final Double Discount = TlossUtils.sub(Double.parseDouble(tv_netreceipts.getText().toString()), tolot);
//                        //会员折扣
//                        final float Memberdiscount = (float) TlossUtils.mul(Double.parseDouble(member_entties.get(i).getDiscount_rate()), Double.parseDouble(ed_Discount.getText().toString()));
//                        //余额
//                        final Double balance = TlossUtils.sub(Double.parseDouble(member_entties.get(i).getSurplus()), tolot);
//
//                        tv_netreceipts.setText(tolot + "");
//                        Showtotal(tolot + "");
//                        mapList.clear();
//                        for (int i = 0; i < commodities.size(); i++) {
//                            Map<String, String> map1 = new HashMap<>();
//                            if (!commodities.get(i).getName().equals("会员充值")) {
//                                map1.put("goods_id", commodities.get(i).getGoods_id());
//                                map1.put("name", commodities.get(i).getName());
//                                map1.put("number", entty.get(i).getNumber() + "");
//                                map1.put("nums", entty.get(i).getNumber() + "");
//                                if (iswholesale) {
//                                    map1.put("price", commodities.get(i).getPrice());
//                                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
//                                    map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
//                                    map1.put("cost", commodities.get(i).getCost() + "");
////                                    map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getPrice())) + "");
//                                } else {
//                                    map1.put("price", commodities.get(i).getMember_price());
//                                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
//                                    map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                                    map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getMember_price())) + "");
//                                }
//                                map1.put("py", commodities.get(i).getPy());
//                                map1.put("PD", commodities.get(i).getPD());
//                                map1.put("GD", commodities.get(i).getGD());
//                                map1.put("bn", commodities.get(i).getBn());
//                                map1.put("product_id", commodities.get(i).getProduct_id());
//                                map1.put("good_limit", commodities.get(i).getGood_limit());
//                                map1.put("good_stock", commodities.get(i).getGood_stock());
//                                map1.put("marketable", commodities.get(i).getMarketable());
//                                map1.put("tag_name", commodities.get(i).getTag_name());
//                                map1.put("tag_id", commodities.get(i).getTag_id());
//                                map1.put("unit", commodities.get(i).getUnit());
//                                map1.put("unit_id", commodities.get(i).getUnit_id() + "");
//                                map1.put("bncode", commodities.get(i).getBncode());
////                map1.put("goods_id", commodities.get(i).getGoods_id());
////                map1.put("name", commodities.get(i).getName());
////                map1.put("number", entty.get(i).getNumber() + "");
////                map1.put("cost", commodities.get(i).getCost());
////                map1.put("price", commodities.get(i).getPrice());
//                                map1.put("orders_status", 1 + "");
//                                map1.put("pay_status", 0 + "");
////            map1.put("py", commodities.get(i).getPy());
////            map1.put("PD", commodities.get(i).getPD());
////            map1.put("GD", commodities.get(i).getGD());
////            map1.put("product_id", commodities.get(i).getProduct_id());
////            map1.put("good_limit", commodities.get(i).getGood_limit());
////            map1.put("good_stock", commodities.get(i).getGood_stock());
////            map1.put("marketable", commodities.get(i).getMarketable());
////            map1.put("tag_name", commodities.get(i).getTag_name());
////            map1.put("tag_id", commodities.get(i).getTag_id());
////            map1.put("unit", commodities.get(i).getUnit());
////            map1.put("unit_id", commodities.get(i).getUnit_id() + "");
////            map1.put("bncode", commodities.get(i).getBncode());
//                            } else {
//                                map1.put("name", commodities.get(i).getName());
//                                map1.put("number", "1");
//                                map1.put("nums", "1");
//                                map1.put("cost", commodities.get(i).getCost());
//                                map1.put("price", tv_netreceipts.getText().toString() + "");
//                                map1.put("amount", tv_netreceipts.getText().toString() + "");
//                            }
//                            mapList.add(map1);
//                        }
//                        Gson gson = new Gson();
//                        final String str = gson.toJson(mapList);
//                        Log.d("print","打印是否免密"+is_require_pass);
//                        if (is_require_pass.equals("no")){
//                            android.support.v7.app.AlertDialog dialog2 = null;
//                            getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), member_entties.get(i).getMobile(), true);
//                        }else {
//                        //余额支付方法
//                        if (!password.equals("") && password.length() == 15) {
//                            android.support.v7.app.AlertDialog dialog2 = null;
//                            getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), password,false);
//                        } else {
//                            final android.support.v7.app.AlertDialog dialog2 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
//                            dialog2.show();
//                            Window window = dialog2.getWindow();
//                            window.setContentView(R.layout.dialog_memberpaw);
//                            dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//
//                            Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
//                            Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
//                            TextView tv_memberprice = (TextView) window.findViewById(R.id.tv_memberprice);
//
//                            Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
//                            but_cancel.setVisibility(View.GONE);
//                            tv_memberprice.setVisibility(View.GONE);
//
//                            but_dimdis.setVisibility(View.GONE);
//                            but_qixiao.setVisibility(View.GONE);
//
//                            TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
//                            final EditText ed_paw = (EditText) window.findViewById(R.id.ed_paw);
//                            tv_title.setText("请输入密码");
//                            ed_paw.setHint("请输入密码");
//                            ed_paw.addTextChangedListener(new TextWatcher() {
//                                @Override
//                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable editable) {
//                                    if (ed_paw.getText().toString().length() == 15)
//                                        if (Float.parseFloat(member_entties.get(i).getSurplus()) >= Float.parseFloat(tv_netreceipts.getText().toString())) {
//                                            getbalancepaid(Memberdiscount, Discount, balance, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString(),false);
//                                        } else {
//                                            Toast.makeText(MainActivity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
//                                        }
//                                }
//                            });
//                        }
//                    }
//                        dialog.dismiss();
//                    } else {
//                        Toast.makeText(MainActivity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        //充值的按钮
//        but_recharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
//                dialog.show();
//                final Window window = dialog.getWindow();
//                window.setContentView(R.layout.dialog_recharge);
//                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
//                but_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                lv_recharge = (ListView) window.findViewById(R.id.lv_recharge);
//                adapter_specification = new Adapter_specification(MainActivity.this);
//                adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
//                    @Override
//                    public void onclickdialog(int i) {
//                        if (commodities.size() == 0) {
//                            specification_unms = i;
//                            Commodity commodity = new Commodity();
//                            ShuliangEntty shuliang = new ShuliangEntty();
//                            commodity.setName("会员充值");
//                            commodity.setPrice(specification_list.get(i).getVal());
//                            shuliang.setNumber(1);
//                            commodity.setMember_price(specification_list.get(i).getVal());
//                            commodity.setCost(TlossUtils.add(Double.parseDouble(specification_list.get(i).getVal()),Double.parseDouble(specification_list.get(i).getGive()))+"");
//                            commodity.setStore(200 + "");
//                            commodity.setGoods_id("null");
//                            commodities.add(commodity);
//                            entty.add(shuliang);
//                            adapterzhu.getadats(commodities);
//                            adapterzhu.getEntty(entty);
//                            lv.setAdapter(adapterzhu);
//                            dialog.dismiss();
//
//                            tv_Totalmerchandise.setText("1");
//                            tv_Total.setText(specification_list.get(i).getVal());
////                                            tv_netreceipts.setText(specification_list.get(i).getVal());
//                            tv_netreceipts.setText(specification_list.get(i).getVal());
//                            Showtotal(specification_list.get(i).getVal());
////                                            Map<String, String> map = new HashMap<String, String>();
////                                            map.put("member_id", member_entty.getMember_id());
////                                            map.put("surplus", Integer.parseInt(specification_list.get(i).getGive()) +
////                                                    Integer.parseInt(specification_list.get(i).getVal()) + "");
////                                            Gson gson = new Gson();
////                                            String s1 = gson.toJson(map);
////                                            UPmoney(s1);
//
//                            Intent mIntent = new Intent();
//                            mIntent.setAction("qwer");
//                            mIntent.putExtra("yaner", commodity);
//                            //发送广播  
//                            sendBroadcast(mIntent);
//
//                            Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
//                            but_cancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog.dismiss();
//                                }
//                            });
//                        }else {
//                            Toast.makeText(MainActivity.this,"请清除列表商品",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                getData("1");
////                                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
////                                    final EditText ed_money= (EditText) window.findViewById(R.id.ed_money);
////                                    Button but_Recharge_amount= (Button) window.findViewById(R.id.but_Recharge_amount);
////                                    but_Recharge_amount.setOnClickListener(new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View view) {
////                                            Map<String ,String> map=new HashMap<String, String>();
////                                            map.put("member_id",member_entty.getMember_id());
////                                            map.put("surplus",ed_money.getText().toString());
////                                            Gson gson=new Gson();
////                                            String s1=gson.toJson(map);
////                                            UPmoney(s1,ed_money.getText().toString());
////                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                                            boolean isOpen = imm.isActive();
////                                            //isOpen若返回true，则表示输入法打开，反之则关闭。
////                                            if (isOpen) {
////                                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
////                                            }
////                                            dialog.dismiss();
////                                        }
////                                    });
//            }
//        });
//
//        //兑换商品的按钮
//        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
//        but_dimdis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getexchange();
////                                    dialog.dismiss();
//            }
//        });
    }
    //定时器3秒
//    public class ThreadTimes implements Runnable {
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    Message mag = new Message();
//                    mag.what = 3;
//                    Thread.sleep(3000);
//                    handler2.sendMessage(mag);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void Yuezhifu(final float Memberdiscount,final int j){
        float total_amount = 0;
        if (commodities.size() > 0) {
            double dis_Total=0;
            double Total=0;
            double Total1=0;
            for (int i=0;i<commodities.size();i++){
                if (commodities.get(i).getIs_special_offer()!=null) {
                    if (commodities.get(i).getIs_special_offer().equals("no")){
                        if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                            if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                    dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType())-1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                } else {
                                    dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                }
                            }else {
                                dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                            }
                        }else {
                            dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                        }
                    }else {
                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                    }
                }else {
                    if (!commodities.get(i).getGoods_id().equals("null")&&commodities.get(i).getGoods_id()!=null){
                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                    }else {
                        dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                    }
//                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(entty.get(i).getNumber()+"")));
                }
            }

            Total1=TlossUtils.add(Total,dis_Total);
            Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Memberdiscount));
            discount=true;
            _discount=TlossUtils.sub(Total1,Total)+"";
            _Total = Total1+"";

            if (Double.parseDouble(member_entties.get(j).getSurplus()) >= Total) {
//                Double tolot1 = Double.parseDouble(tv_netreceipts.getText().toString());
                Double tolot1 = Total;
                _Total=tv_netreceipts.getText().toString();
                //优惠金额
                final Double Discount1=TlossUtils.sub(tolot1,Double.parseDouble(tv_netreceipts.getText().toString()));
                //余额
                final Double balance1=TlossUtils.sub(Double.parseDouble(member_entties.get(j).getSurplus()),Double.parseDouble(tv_netreceipts.getText().toString()));

                mapList.clear();
                for (int i = 0; i < commodities.size(); i++) {
                    Map<String, String> map1 = new HashMap<>();
                    if (!commodities.get(i).getName().equals("会员充值")) {
                        map1.put("goods_id", commodities.get(i).getGoods_id());
                        map1.put("name", commodities.get(i).getName());
                        map1.put("number", entty.get(i).getNumber() + "");
                        map1.put("nums", entty.get(i).getNumber() + "");
//                        if (iswholesale){
//                            map1.put("price", commodities.get(i).getPrice());
//                            total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
//                            map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
////                            map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()),Double.parseDouble(commodities.get(i).getPrice()))+"");
//                            map1.put("cost", commodities.get(i).getCost()+"");
//                        }else {
//                            map1.put("price", commodities.get(i).getMember_price());
//                            total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
//                            map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                            map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()),Double.parseDouble(commodities.get(i).getMember_price()))+"");
//                        }

                        String price=commodities.get(i).getPrice();
                        double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                        if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                            if (commodities.get(i).getIs_special_offer()!=null){
                                if (commodities.get(i).getIs_special_offer().equals("no")) {
                                    if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                        if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                            price=StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    } else {
                                        if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                            price=commodities.get(i).getMember_price();
                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    }
                                }
                            } else {
                                if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                    price=commodities.get(i).getPrice();
                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                }
                            }
                        }else {
                            if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                price=commodities.get(i).getPrice();
                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                            }
                        }

                        map1.put("price", price);
                        total_amount += amount;
                        map1.put("amount", amount + "");
                        map1.put("cost", commodities.get(i).getCost()+"");


                        map1.put("py", commodities.get(i).getPy());
                        map1.put("PD", commodities.get(i).getPD());
                        map1.put("GD", commodities.get(i).getGD());
                        map1.put("bn", commodities.get(i).getBn());
                        map1.put("product_id", commodities.get(i).getProduct_id());
                        map1.put("good_limit", commodities.get(i).getGood_limit());
                        map1.put("good_stock", commodities.get(i).getGood_stock());
                        map1.put("marketable", commodities.get(i).getMarketable());
                        map1.put("tag_name", commodities.get(i).getTag_name());
                        map1.put("tag_id", commodities.get(i).getTag_id());
                        map1.put("unit", commodities.get(i).getUnit());
                        map1.put("unit_id", commodities.get(i).getUnit_id() + "");
                        map1.put("bncode", commodities.get(i).getBncode());
//                map1.put("goods_id", commodities.get(i).getGoods_id());
//                map1.put("name", commodities.get(i).getName());
//                map1.put("number", entty.get(i).getNumber() + "");
//                map1.put("cost", commodities.get(i).getCost());
//                map1.put("price", commodities.get(i).getPrice());
                        map1.put("orders_status", 1 + "");
                        map1.put("pay_status", 0 + "");

//            map1.put("py", commodities.get(i).getPy());
//            map1.put("PD", commodities.get(i).getPD());
//            map1.put("GD", commodities.get(i).getGD());
//            map1.put("product_id", commodities.get(i).getProduct_id());
//            map1.put("good_limit", commodities.get(i).getGood_limit());
//            map1.put("good_stock", commodities.get(i).getGood_stock());
//            map1.put("marketable", commodities.get(i).getMarketable());
//            map1.put("tag_name", commodities.get(i).getTag_name());
//            map1.put("tag_id", commodities.get(i).getTag_id());
//            map1.put("unit", commodities.get(i).getUnit());
//            map1.put("unit_id", commodities.get(i).getUnit_id() + "");
//            map1.put("bncode", commodities.get(i).getBncode());
                    } else {
                        map1.put("name", commodities.get(i).getName());
                        map1.put("number", "1");
                        map1.put("nums", "1");
                        map1.put("cost", commodities.get(i).getCost());
                        map1.put("price", tv_netreceipts.getText().toString() + "");
                        map1.put("amount", tv_netreceipts.getText().toString() + "");
                    }
                    mapList.add(map1);
                }
                Gson gson = new Gson();
                final String str = gson.toJson(mapList);

                if (is_require_pass.equals("no")){
                    android.support.v7.app.AlertDialog dialog2 = null;
                    getbalancepaid(Memberdiscount,Discount1,balance1,dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(j).getMember_id(), member_entties.get(j).getMobile(),true);
                }else {
                    //余额支付方法
                    if (!password.equals("") && password.length() == 15) {
                        android.support.v7.app.AlertDialog dialog2 = null;
                        getbalancepaid(Memberdiscount, Discount1, balance1, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(j).getMember_id(), password,false);
                    } else {
                        final android.support.v7.app.AlertDialog dialog2 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                        dialog2.show();
                        Window window = dialog2.getWindow();
                        window.setContentView(R.layout.dialog_memberpaw);
                        dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                        Button but_qixiao = (Button) window.findViewById(R.id.but_qixiao);
                        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);

                        TextView tv_memberprice = (TextView) window.findViewById(R.id.tv_memberprice);

                        Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                        but_cancel.setVisibility(View.GONE);
                        tv_memberprice.setVisibility(View.GONE);
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
                                    if (Float.parseFloat(member_entties.get(j).getSurplus()) >= Float.parseFloat(tv_netreceipts.getText().toString())) {
                                        getbalancepaid(Memberdiscount, Discount1, balance1, dialog2, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(j).getMember_id(), ed_paw.getText().toString(),false);
                                    } else {
                                        Toast.makeText(MainActivity.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });
                    }
                }
//                        if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
//                            Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
//                            tv_netreceipts.setText(tolot + "");
//                            Showtotal(tolot + "");
//                            pay_type = "1";
//                            dialog.dismiss();
//                        }
            }
        }
    }


    //    定时器1秒
    public class ThreadShow implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message mag = new Message();
                    mag.what = 1;
                    handler.sendMessage(mag);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //获取时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    //条形码的拦截
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//            if(event.getKeyCode()!=KeyEvent.KEYCODE_DEL){
        scanGunKeyEventHelper.analysisKeyEvent(event);
//            }else {
//                return false;
//            }
        return true;
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
////  
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
//
//    }

    /**
     * 注册广播的问题
     */

    @Override
    protected void onStop() {
        super.onStop();
//        loadingdialog.dismiss();
        unregisterReceiver(broadcastReceiver);
    }





    //    onSaveInstanceState


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    //弹窗显示重量
    TextView diatv_weight;
    TextView tv_danwei;
    TextView tv_company;

    String is_kg="2";

    //获取重量的弹窗
    public void getweight(final Commodity commodity){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_getweight);
        final widget.Switch sw_kg= (widget.Switch) window.findViewById(R.id.sw_kg);
        tv_danwei= (TextView) window.findViewById(R.id.tv_danwei);
        tv_company= (TextView) window.findViewById(R.id.tv_company);
        if (SharedUtil.getString("is_kg1")!=null) {
            if (!SharedUtil.getString("is_kg1").equals("")) {
                if (SharedUtil.getString("is_kg1").equals("1")) {
                    tv_company.setText("千克");
                } else if (SharedUtil.getString("is_kg1").equals("2")) {
                    tv_company.setText("斤");
                } else if (SharedUtil.getString("is_kg1").equals("3")) {
                    tv_company.setText("两");
                } else if (SharedUtil.getString("is_kg1").equals("4")) {
                    tv_company.setText("克");
                }
            }else {
                tv_company.setText("斤");
            }
        }else {
            tv_company.setText("斤");
        }
        tv_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedUtil.getString("is_kg1")!=null){
                    if (!SharedUtil.getString("is_kg1").equals("")){
                        changemetering(SharedUtil.getString("is_kg1"));
                    }else {
                        changemetering("2");
                    }
                }else {
                    changemetering("2");
                }

            }
        });
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        Button but_cancel_cancel = (Button) window.findViewById(R.id.but_cancel_cancel);
        TextView tv_weight_name = (TextView) window.findViewById(R.id.tv_weight_name);
        tv_weight_name.setText(commodity.getName());
        TextView tv_weight_price= (TextView) window.findViewById(R.id.tv_weight_price);

        if (commodity.getType()!=null&&!commodity.getType().equals("")&&!commodity.getType().equals("0")) {
            if (commodity.getIs_special_offer()!=null){
                if (commodity.getIs_special_offer().equals("no")) {
                    if (commodity.getCustom_member_price() != null && !commodity.getCustom_member_price().equals("")&& !commodity.getCustom_member_price().equals("null")) {
                        if (!StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1].equals("")) {
                            tv_weight_price.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]+"");
                        }
                    } else {
                        tv_weight_price.setText(Double.parseDouble(commodity.getMember_price())+"");
                    }
                } else {
                    tv_weight_price.setText(Double.parseDouble(commodity.getPrice())+"");
                }
            }else {
                tv_weight_price.setText(Double.parseDouble(commodity.getPrice())+"");
            }
        }else {
                tv_weight_price.setText(Double.parseDouble(commodity.getPrice())+"");
        }


//        if (iswholesale){
//
//            tv_weight_price.setText(Double.parseDouble(commodity.getPrice())+"");
//        }else {
//            tv_weight_price.setText(Double.parseDouble(commodity.getMember_price())+"");
//        }

        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final TextView tv_weight_total_Price= (TextView) window.findViewById(R.id.tv_weight_total_Price);
        diatv_weight= (TextView) window.findViewById(R.id.tv_weight);

        diatv_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (commodity.getType()!=null&&!commodity.getType().equals("")&&!commodity.getType().equals("0")) {
                    if (commodity.getIs_special_offer()!=null){
                        if (commodity.getIs_special_offer().equals("no")) {
                            if (commodity.getCustom_member_price() != null && !commodity.getCustom_member_price().equals("")&& !commodity.getCustom_member_price().equals("null")) {
                                if (!StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1].equals("")) {
                                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]),Double.parseDouble(editable.toString())))+"");
                                }
                            } else {
                                tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getMember_price()),Double.parseDouble(editable.toString())))+"");                            }
                        } else {
                            tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(editable.toString())))+"");                        }
                    }else {
                        tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(editable.toString())))+"");                    }
                }else {
                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(editable.toString())))+"");
                }

//                if (iswholesale){
//                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(editable.toString())))+"");
//                }else {
//                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getMember_price()),Double.parseDouble(editable.toString())))+"");
//                }


            }
        });

        if (Double.parseDouble(strweigh)!=0&&Double.parseDouble(diatv_weight.getText().toString())!=0){
            diatv_weight.setText(strweigh);
        }

        but_cancel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (commodity.getType()!=null&&!commodity.getType().equals("")&&!commodity.getType().equals("0")) {
                    if (commodity.getIs_special_offer()!=null){
                        if (commodity.getIs_special_offer().equals("no")) {
                            if (commodity.getCustom_member_price() != null && !commodity.getCustom_member_price().equals("")&& !commodity.getCustom_member_price().equals("null")) {
                                if (!StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1].equals("")) {
//                                    tv_weight_price.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]+"");
                                    AddCommodity(commodity, 1,StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]);
                                }
                            } else {
                                AddCommodity(commodity, 1,commodity.getMember_price());
                            }
                        } else {
                            AddCommodity(commodity, 1,commodity.getPrice());
                        }
                    }else {
                        AddCommodity(commodity, 1,commodity.getPrice());
                    }
                }else {
                    AddCommodity(commodity, 1,commodity.getPrice());
                }

//                AddCommodity(commodity, 1,commodity.getPrice());

                dialog.dismiss();
            }
        });

        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Float.parseFloat(diatv_weight.getText().toString())>0){
                    AddCommodity(commodity, Float.parseFloat(diatv_weight.getText().toString()),tv_weight_total_Price.getText().toString());
                    dialog.dismiss();
                }
            }
        });

    }

    //更改称重的计量单位
    public void changemetering(String sw){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.weight_company);
        TextView tv_kg= (TextView) window.findViewById(R.id.tv_kg);
        TextView tv_jin= (TextView) window.findViewById(R.id.tv_jin);
        TextView tv_two= (TextView) window.findViewById(R.id.tv_two);
        TextView tv_g= (TextView) window.findViewById(R.id.tv_g);
        if (sw.equals("1")){
            tv_kg.setTextColor(Color.parseColor("#FF6501"));
            tv_jin.setTextColor(Color.parseColor("#ff757575"));
            tv_two.setTextColor(Color.parseColor("#ff757575"));
            tv_g.setTextColor(Color.parseColor("#ff757575"));
        }else if (sw.equals("2")){
            tv_kg.setTextColor(Color.parseColor("#ff757575"));
            tv_jin.setTextColor(Color.parseColor("#FF6501"));
            tv_two.setTextColor(Color.parseColor("#ff757575"));
            tv_g.setTextColor(Color.parseColor("#ff757575"));
        }else if (sw.equals("3")){
            tv_kg.setTextColor(Color.parseColor("#ff757575"));
            tv_jin.setTextColor(Color.parseColor("#ff757575"));
            tv_two.setTextColor(Color.parseColor("#FF6501"));
            tv_g.setTextColor(Color.parseColor("#ff757575"));
        }else if (sw.equals("4")){
            tv_kg.setTextColor(Color.parseColor("#ff757575"));
            tv_jin.setTextColor(Color.parseColor("#ff757575"));
            tv_two.setTextColor(Color.parseColor("#ff757575"));
            tv_g.setTextColor(Color.parseColor("#FF6501"));
        }

        tv_kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_kg1","1");
                if (tv_company!=null){
                    tv_company.setText("千克");
                }
                tv_danwei1.setText("千克");
                is_kg="1";
                dialog.dismiss();
            }
        });
        tv_jin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_kg1","2");
                if (tv_company!=null){
                    tv_company.setText("斤");
                }
                tv_danwei1.setText("斤");
                is_kg="2";
                dialog.dismiss();
            }
        });
        tv_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_kg1","3");
                if (tv_company!=null){
                    tv_company.setText("两");
                }
                tv_danwei1.setText("两");
                is_kg="3";
                dialog.dismiss();
            }
        });
        tv_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_kg1","4");
                if (tv_company!=null){
                    tv_company.setText("克");
                }
                tv_danwei1.setText("克");
                is_kg="4";
                dialog.dismiss();
            }
        });

    }


    //添加到商品列表的方法
    public void AddCommodity(Commodity commodity,float weigt,String total){
        Intent mIntent = new Intent();
        mIntent.setAction("qwer");
        mIntent.putExtra("weigt",weigt+"");
        mIntent.putExtra("yaner", commodity);
        //发送广播  
        sendBroadcast(mIntent);

        int in = 0;
        if (commodities.size() > 0) {
            aa:
            for (int k = 0; k < commodities.size(); k++) {
                if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                    in = in + (k + 1);
                    break aa;
                }
            }
            if (in == 0) {
                shuliangEntty = new ShuliangEntty();
                shuliangEntty.setNumber(weigt);
                entty.add(shuliangEntty);
                commodities.add(commodity);
            } else {
                float i = entty.get(in - 1).getNumber();
                i = i + weigt;
                //快捷栏增加的广播
                Intent kuaijieadd = new Intent();
                kuaijieadd.setAction("com.yzx.kuaijieadd");
                kuaijieadd.putExtra("index", in);
                kuaijieadd.putExtra("number", i);
                sendBroadcast(kuaijieadd);

                entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);

                adapterzhu.getEntty(entty);
                adapterzhu.getadats(commodities);
                adapterzhu.notifyDataSetChanged();
                lv.setAdapter(adapterzhu);
                lv.setSelection(adapterzhu.getCount() - 1);
            }

        } else {

            if (commodities.size() > 0) {
                shuliangEntty = new ShuliangEntty();
                shuliangEntty.setNumber(weigt);
                entty.add(commodities.size() - 1, shuliangEntty);
                commodities.add(commodities.size() - 1, commodity);
            } else {
                shuliangEntty = new ShuliangEntty();
                shuliangEntty.setNumber(weigt);
                entty.add(0, shuliangEntty);
                commodities.add(0, commodity);
            }

        }

        double f = Double.parseDouble(tv_Total.getText().toString());
        f = TlossUtils.add(f, Double.parseDouble(total));
        tv_Total.setText(StringUtils.stringpointtwo(f + ""));
        tv_netreceipts.setText(f + "");
        Showtotal(f + "");
        et_keyoard.setText(f + "");
        tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) +1) + "");
        if (adapterzhu != null) {
            adapterzhu.getEntty(entty);
            adapterzhu.getadats(commodities);
            lv.setAdapter(adapterzhu);
            adapterzhu.notifyDataSetChanged();
            lv.setSelection(adapterzhu.getCount() - 1);
        } else {
            adapterzhu = new listadapterzhu(MainActivity.this);
            adapterzhu.getEntty(entty);
            adapterzhu.getadats(commodities);
            lv.setAdapter(adapterzhu);
            adapterzhu.notifyDataSetChanged();
            lv.setSelection(adapterzhu.getCount() - 1);
        }
    }



    //    加号减号的广播接收注册
    @Override
    protected void onStart() {


//        initport();

        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        iscash=false;
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        if (SharedUtil.getString("acquisition_time")!=null) {
            getData_Feed();
        }else {
            getAdats();
        }

        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(MainActivity.this);

//        init();
//
        but_Quick.performClick();
        super.onStart();
//        减号的广播
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.yzx.reductionof");
        registerReceiver(broadcastReceiver, intentFilter1);

        //加号的广播
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.yzx.add");
        registerReceiver(broadcastReceiver, intentFilter2);

//        快捷的广播
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.yzx.kuaijie");
        registerReceiver(broadcastReceiver, intentFilter3);

        //        挂单的广播
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("com.yzx.order");
        registerReceiver(broadcastReceiver, intentFilter4);
//查看的广播
        IntentFilter intentFilter5 = new IntentFilter();
        intentFilter5.addAction("com.yzx.chech");
        registerReceiver(broadcastReceiver, intentFilter5);



    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.yzx.reductionof")) {
                if (!issucceed) {
                    Bundle bundle = intent.getExtras();
                    double i = Double.parseDouble((String) bundle.get("reductionof"));
                    double f = Double.parseDouble(tv_Total.getText().toString());
                    float tota = Float.valueOf(tv_Totalmerchandise.getText().toString());
//                    if (iswholesale){
//                        f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getPrice()));
//                    }else {
//                        f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getMember_price()));
//                    }
                    if (commodities.get((int)i).getIs_special_offer()!=null){
                        if (commodities.get((int)i).getIs_special_offer().equals("no")){
                            if (commodities.get((int)i).getType()!=null&&!commodities.get((int)i).getType().equals("")&&!commodities.get((int) i).getType().equals("0")) {
                                if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                    if (commodities.get((int)i).getCustom_member_price() != null && !commodities.get((int)i).getCustom_member_price().equals("")&& !commodities.get((int)i).getCustom_member_price().equals("null")) {
                                        f = TlossUtils.sub(f, Double.parseDouble(StringUtils.getStrings(commodities.get((int)i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get((int)i).getType())-1]));
                                    } else {
                                        f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getMember_price()));
                                    }
                                }else {
                                    f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                                }
                            }else {
                                f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                            }

                        }else {
                            f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                        }
                    }else {
                        f = TlossUtils.sub(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                    }

                    tota--;
                    tv_Totalmerchandise.setText(tota + "");
                    tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                    tv_netreceipts.setText(f + "");
                    Showtotal(f + "");
                    et_keyoard.setText(f + "");
                    adapterzhu.notifyDataSetChanged();
                }
            }
            if (action.equals("com.yzx.add")) {
                if (!issucceed) {
                    Bundle bundle = intent.getExtras();
                    double i = Double.parseDouble((String) bundle.get("add"));
                    double f = Double.parseDouble(tv_Total.getText().toString());
                    float tota = Float.valueOf(tv_Totalmerchandise.getText().toString());
//                    if (iswholesale){
//                        f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getPrice()));
//                    }else {
//                        f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getMember_price()));
//                    }
                    if (commodities.get((int)i).getIs_special_offer()!=null){
                        if (commodities.get((int)i).getIs_special_offer().equals("no")){
                            if (commodities.get((int)i).getType()!=null&&!commodities.get((int)i).getType().equals("")&&!commodities.get((int)i).getType().equals("0")) {
                                if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                    if (commodities.get((int)i).getCustom_member_price() != null && !commodities.get((int)i).getCustom_member_price().equals("")&& !commodities.get((int)i).getCustom_member_price().equals("null")) {
                                        f = TlossUtils.add(f, Double.parseDouble(StringUtils.getStrings(commodities.get((int)i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get((int)i).getType())-1]));
                                    } else {
                                        f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getMember_price()));
                                    }
                                }else {
                                    f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                                }
                            }else {
                                f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                            }

                        }else {
                            f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                        }
                    }else {
                        f = TlossUtils.add(f, Double.parseDouble(commodities.get((int) i).getPrice()));
                    }

                    tota++;
                    tv_Totalmerchandise.setText(tota + "");
                    tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                    tv_netreceipts.setText(f + "");
                    Showtotal(f + "");
                    et_keyoard.setText(f + "");
                    adapterzhu.notifyDataSetChanged();
                }
            }
            if (action.equals("com.yzx.kuaijie")) {
                if (Rl_time.getVisibility() == View.VISIBLE) {

                } else {
                    Bundle bundle = intent.getExtras();
                    Commodity commodity = (Commodity) bundle.getSerializable("maidan");
                    commodity.setType(Member_type);
                    if (bundle.getString("type").equals("cook")) {
                        if (!commodity.getCook_position().equals("0")){
                            getweight(commodity);
                        }else {
                            int in = 0;
                            int position = (int) bundle.get("position");
                            if (commodities.size() > 0) {
                                aa:
                                for (int k = 0; k < commodities.size(); k++) {
                                    if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                        in = in + (k + 1);
                                        break aa;
                                    }
                                }
                                if (in == 0) {
                                    shuliangEntty = new ShuliangEntty();
                                    shuliangEntty.setNumber(1);
                                    entty.add(shuliangEntty);
                                    commodities.add(commodity);
                                } else {
                                    float i = entty.get(in - 1).getNumber();
                                    i = i + 1;
                                    //快捷栏增加的广播
                                    Intent kuaijieadd = new Intent();
                                    kuaijieadd.setAction("com.yzx.kuaijieadd");
                                    kuaijieadd.putExtra("index", in);
                                    kuaijieadd.putExtra("number", i);
                                    sendBroadcast(kuaijieadd);

                                    entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);

                                    adapterzhu.getEntty(entty);
                                    adapterzhu.getadats(commodities);
                                    adapterzhu.notifyDataSetChanged();
                                    lv.setAdapter(adapterzhu);
                                    lv.setSelection(adapterzhu.getCount() - 1);
                                }

                            } else {

                                if (commodities.size() > 0) {
                                    shuliangEntty = new ShuliangEntty();
                                    shuliangEntty.setNumber(1);
                                    entty.add(commodities.size() - 1, shuliangEntty);
                                    commodities.add(commodities.size() - 1, commodity);
                                } else {
                                    shuliangEntty = new ShuliangEntty();
                                    shuliangEntty.setNumber(1);
                                    entty.add(0, shuliangEntty);
                                    commodities.add(0, commodity);
                                }

                            }

                            double f = Double.parseDouble(tv_Total.getText().toString());
//                            if (layout_go_pay.getVisibility()==View.GONE){
//                                f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
//                            }else {
                                if (commoditys.getType()!=null&&!commoditys.getType().equals("")&&!commoditys.getType().equals("0")) {
                                    if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                        if (commoditys.getIs_special_offer() != null) {
                                            if (commoditys.getIs_special_offer().equals("no")) {
                                                if (commoditys.getCustom_member_price() != null && !commoditys.getCustom_member_price().equals("")&& !commoditys.getCustom_member_price().equals("null")) {
                                                    f = TlossUtils.add(f, Double.parseDouble(StringUtils.getStrings(commoditys.getCustom_member_price(), ",")[Integer.parseInt(commoditys.getType()) - 1]));
                                                } else {
                                                    f = TlossUtils.add(f, Double.parseDouble(commoditys.getMember_price()));
                                                }
                                            } else {
                                                f = TlossUtils.add(f, Double.parseDouble(commoditys.getPrice()));
                                            }
                                        }else {
                                            f = TlossUtils.add(f, Double.parseDouble(commoditys.getPrice()));
                                        }
                                    }else {
                                        f = TlossUtils.add(f, Double.parseDouble(commoditys.getPrice()));
                                    }
                                }else {
                                    f = TlossUtils.add(f, Double.parseDouble(commoditys.getPrice()));
                                }
//                                f = TlossUtils.add(f, Double.parseDouble(commodity.getMember_price()));
//                            }
                            tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                            tv_netreceipts.setText(f + "");
                            Showtotal(f + "");
                            et_keyoard.setText(f + "");
                            tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
                            if (adapterzhu != null) {
                                adapterzhu.getEntty(entty);
                                adapterzhu.getadats(commodities);
                                lv.setAdapter(adapterzhu);
                                adapterzhu.notifyDataSetChanged();
                                lv.setSelection(adapterzhu.getCount() - 1);
                            } else {
                                adapterzhu = new listadapterzhu(MainActivity.this);
                                adapterzhu.getEntty(entty);
                                adapterzhu.getadats(commodities);
                                lv.setAdapter(adapterzhu);
                                adapterzhu.notifyDataSetChanged();
                                lv.setSelection(adapterzhu.getCount() - 1);
                            }
                            Intent mIntent = new Intent();
                            mIntent.setAction("qwer");
                            mIntent.putExtra("yaner", commodity);
                            //发送广播  
                            sendBroadcast(mIntent);
                        }
                    } else{
                        int in = 0;
                        int position = (int) bundle.get("position");
                        if (commodities.size() > 0) {
                            aa:
                            for (int k = 0; k < commodities.size(); k++) {
                                if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                    in = in + (k + 1);
                                    break aa;
                                }
                            }
                            if (in == 0) {
                                shuliangEntty = new ShuliangEntty();
                                shuliangEntty.setNumber(1);
                                entty.add(shuliangEntty);
                                commodities.add(commodity);
                            } else {
                                float i = entty.get(in - 1).getNumber();
                                i = i + 1;
                                //快捷栏增加的广播
                                Intent kuaijieadd = new Intent();
                                kuaijieadd.setAction("com.yzx.kuaijieadd");
                                kuaijieadd.putExtra("index", in);
                                kuaijieadd.putExtra("number", i);
                                sendBroadcast(kuaijieadd);

                                entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);

                                adapterzhu.getEntty(entty);
                                adapterzhu.getadats(commodities);
                                adapterzhu.notifyDataSetChanged();
                                lv.setAdapter(adapterzhu);
                                lv.setSelection(adapterzhu.getCount() - 1);
                            }

                        } else {

                            if (commodities.size() > 0) {
                                shuliangEntty = new ShuliangEntty();
                                shuliangEntty.setNumber(1);
                                entty.add(commodities.size() - 1, shuliangEntty);
                                commodities.add(commodities.size() - 1, commodity);
                            } else {
                                shuliangEntty = new ShuliangEntty();
                                shuliangEntty.setNumber(1);
                                entty.add(0, shuliangEntty);
                                commodities.add(0, commodity);
                            }

                        }

                        double f = Double.parseDouble(tv_Total.getText().toString());
//                        if (layout_go_pay.getVisibility()==View.GONE){
//                            f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
//                        }else {
                            if (commodity.getType()!=null&&!commodity.getType().equals("")&&!commodity.getType().equals("0")) {
                                if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                    if (commodity.getIs_special_offer() != null) {
                                        if (commodity.getIs_special_offer().equals("no")) {
                                            if (commodity.getCustom_member_price() != null && !commodity.getCustom_member_price().equals("")&& !commodity.getCustom_member_price().equals("null") && !commodity.getType().equals("0")) {
                                                f = TlossUtils.add(f, Double.parseDouble(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[Integer.parseInt(commodity.getType()) - 1]));
                                            } else {
                                                f = TlossUtils.add(f, Double.parseDouble(commodity.getMember_price()));
                                            }
                                        }else {
                                            f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
                                        }
                                    }else {
                                        f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
                                    }
                                }else {
                                    f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
                                }
                            }else {
                                f = TlossUtils.add(f, Double.parseDouble(commodity.getPrice()));
                            }
//                            f = TlossUtils.add(f, Double.parseDouble(commodity.getMember_price()));
//                        }
                        tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                        tv_netreceipts.setText(f + "");
                        Showtotal(f + "");
                        et_keyoard.setText(f + "");
                        tv_Totalmerchandise.setText((Float.valueOf(tv_Totalmerchandise.getText().toString()) + 1) + "");
                        if (adapterzhu != null) {
                            adapterzhu.getEntty(entty);
                            adapterzhu.getadats(commodities);
                            lv.setAdapter(adapterzhu);
                            adapterzhu.notifyDataSetChanged();
                            lv.setSelection(adapterzhu.getCount() - 1);
                        } else {
                            adapterzhu = new listadapterzhu(MainActivity.this);
                            adapterzhu.getEntty(entty);
                            adapterzhu.getadats(commodities);
                            lv.setAdapter(adapterzhu);
                            adapterzhu.notifyDataSetChanged();
                            lv.setSelection(adapterzhu.getCount() - 1);
                        }
                    }
                }
//                adapterzhu.getadats(Kmaidan);
//                adapterzhu.notifyDataSetChanged();
            }
            if (action.equals("com.yzx.order")) {
                Bundle bundle = intent.getExtras();
                final String order_id = (String) bundle.getSerializable("order");

                Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
                String s=sqlite_entity.queryguadangoods(order_id);
                if (!s.equals("")){
                    reduce=false;
                    discount=false;
                    discount_type="reduce";
                    num="0";
                    discount_goods_id="";
                    commodities.clear();
                    entty.clear();
                    _Total="0";
                    if (Entty!=null){
                        for (int l=0;l<Entty.size();l++){
                            Entty.get(l).setChecked(false);
                        }
                    }
                    int unm = 0;
                    double t = 0;
                    try {
                        JSONArray jsonArray=new JSONArray(s);
                        for (int i=0;i<jsonArray.length();i++){
                            commoditys = new Commodity();
                            shuliangEntty = new ShuliangEntty();
                            JSONObject jo2 = jsonArray.getJSONObject(i);
                            commoditys.setName(jo2.getString("name"));
                            commoditys.setPrice(jo2.getString("price"));
                            commoditys.setMember_price(jo2.getString("member_price"));
                            commoditys.setStore(jo2.getString("store"));
                            shuliangEntty.setNumber((float) jo2.getDouble("number"));
                            commoditys.setCost(jo2.getString("cost"));
                            commoditys.setGoods_id(jo2.getString("goods_id"));
                            commoditys.setIs_special_offer(jo2.getString("is_special_offer"));
                            commoditys.setBncode(jo2.getString("bncode"));
                            commodities.add(commoditys);
                            entty.add(shuliangEntty);
                        }
                        for (int i = 0; i < commodities.size(); i++) {
                            unm += entty.get(i).getNumber();
                            t = TlossUtils.add(t, (Double.parseDouble(StringUtils.stringpointtwo(commodities.get(i).getPrice())) * (float) entty.get(i).getNumber()));
                        }
                        tv_netreceipts.setText(t + "");
                        Showtotal(t + "");
                        tv_Totalmerchandise.setText(unm + "");
                        tv_Total.setText(StringUtils.stringpointtwo(t + ""));
                        adapterzhu.getadats(commodities);
                        adapterzhu.setType(false);
                        adapterzhu.getEntty(entty);
                        lv.setAdapter(adapterzhu);
                        adapterzhu.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        but_Delete.setClickable(true);
                        im_tuichu.setClickable(true);
                        rl_quick.setVisibility(View.VISIBLE);
                        rl_jishuang.setVisibility(View.VISIBLE);
                        ll_check.setVisibility(View.GONE);
                        sqlite_entity.deleteguadan(order_id);
                    }
                }

//                OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
//                        .tag(this)
//                        .cacheKey("cacheKey")
//                        .cacheMode(CacheMode.DEFAULT)
//                        .params("order_id", order_id)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(String s, Call call, Response response) {
//                                Log.d("print挂单的数量",s);
//                                reduce=false;
//                                discount=false;
//                                discount_type="reduce";
//                                num="0";
//                                discount_goods_id="";
//                                commodities.clear();
//                                entty.clear();
//                                _Total="0";
//                                Member_type="0";
//                                if (Entty!=null){
//                                    for (int l=0;l<Entty.size();l++){
//                                        Entty.get(l).setChecked(false);
//                                    }
//                                }
//                                int unm = 0;
//                                double t = 0;
//                                try {
//                                    JSONObject jsonObject = new JSONObject(s);
//                                    JSONObject jo1 = jsonObject.getJSONObject("response");
//                                    JSONArray ja1 = jo1.getJSONArray("data");
//                                    for (int i = 0; i < ja1.length(); i++) {
//                                        commoditys = new Commodity();
//                                        shuliangEntty = new ShuliangEntty();
//                                        JSONObject jo2 = ja1.getJSONObject(i);
//                                        commoditys.setName(jo2.getString("name"));
//                                        commoditys.setPrice(jo2.getString("price"));
//                                        commoditys.setMember_price(jo2.getString("member_price"));
//                                        commoditys.setStore(jo2.getString("store"));
//                                        shuliangEntty.setNumber((float) jo2.getDouble("nums"));
//                                        commoditys.setCost(jo2.getString("cost"));
//                                        commoditys.setGoods_id(jo2.getString("goods_id"));
//                                        commoditys.setIs_special_offer(jo2.getString("is_special"));
//                                        commoditys.setBncode(jo2.getString("bncode"));
//                                        commodities.add(commoditys);
//                                        entty.add(shuliangEntty);
//                                    }
//                                    for (int i = 0; i < commodities.size(); i++) {
//                                        unm += entty.get(i).getNumber();
//                                        t = TlossUtils.add(t, (Double.parseDouble(StringUtils.stringpointtwo(commodities.get(i).getPrice())) * (float) entty.get(i).getNumber()));
//                                    }
//                                    tv_netreceipts.setText(t + "");
//                                    Showtotal(t + "");
//                                    tv_Totalmerchandise.setText(unm + "");
//                                    tv_Total.setText(StringUtils.stringpointtwo(t + ""));
//                                    adapterzhu.getadats(commodities);
//                                    adapterzhu.setType("");
//                                    adapterzhu.getEntty(entty);
//                                    lv.setAdapter(adapterzhu);
//                                    adapterzhu.notifyDataSetChanged();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    but_Delete.setClickable(true);
//                                    im_tuichu.setClickable(true);
//                                    rl_quick.setVisibility(View.VISIBLE);
//                                    rl_jishuang.setVisibility(View.VISIBLE);
//                                    ll_check.setVisibility(View.GONE);
//                                    OkGo.post(SysUtils.getSellerServiceUrl("delete_order"))
//                                            .tag(this)
//                                            .cacheKey("cacheKey")
//                                            .cacheMode(CacheMode.DEFAULT)
//                                            .params("order_id", order_id)
//                                            .execute(new StringCallback() {
//                                                @Override
//                                                public void onSuccess(String s, Call call, Response response) {
//                                                    getordernum();
//                                                }
//                                            });
//                                }
//                            }
//                        });

            }
            if (action.equals("com.yzx.chech")) {
                Bundle bundle = intent.getExtras();
                final String order_id = (String) bundle.getSerializable("order");
                chankanshangping(order_id);
                but_Delete.setClickable(false);
//                im_tuichu.setClickable(false);
            }
            if (action.equals("com.yzx.pay")) {
//                msg.what
                Mobile_pay mobile_pay1 = new Mobile_pay();
                Bundle bundle = intent.getExtras();
                String payorder_id = bundle.getString("order_id");
                mobile_pay1.setOrder_id(payorder_id);
                String time = bundle.getString("time");
                String time1 = TimeZoneUtil.getTime1((long) (Long.parseLong(time) * 1000));
                mobile_pay1.setPayed_time(time1);
                Log.e("print", "支付成功");

                operational = 0;
                SharedUtil.putString("operational", operational + "");
                Log.e("print", "operational6" + operational);

                type = 0;

                Log.e("print", "支付成功");
                Intent mIntent1 = new Intent();
                mIntent1.setAction("poiu");
                mIntent1.putExtra("mobile_pay", mobile_pay1);
                sendBroadcast(mIntent1);
                tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                Log.e("print","设置收款金额6666666666");
                tv_modes.setText("移动支付");


                if (SharedUtil.getString("print") != null) {
                    if (Boolean.parseBoolean(SharedUtil.getString("print"))) {
                        PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                        printUtil1.openButtonClicked();
//                        String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, payorder_id, time1, commodities, entty,
//                                true, Double.parseDouble(tv_netreceipts.getText().toString()), tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), "0");

                        //112233445566
                        String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, payorder_id, time1, commodities, entty,
                                1, Double.parseDouble(tv_netreceipts.getText().toString()),"", tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                        if (PrintWired.usbPrint(MainActivity.this,syy)){

                        }else {
                            printUtil1.printstring(syy);

                            mService.sendMessage(syy, "GBK");
                        }
//                        printUtil1.printstring(syy);
//
//                        printUtil1.printstring(syy);
//                        mService.sendMessage(syy, "GBK");
//                        USBPrinters.initPrinter(getApplicationContext());
//                        USBPrinters.getInstance().print(syy);
//                        USBPrinters.destroyPrinter();
                    } else {
                    }
                } else {
                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                    printUtil1.openButtonClicked();
//                    String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, payorder_id, time1, commodities, entty,
//                            true, Double.parseDouble(tv_netreceipts.getText().toString()), tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), "0");

                    //112233445566
                    String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, payorder_id, time1, commodities, entty,
                            1, Double.parseDouble(tv_netreceipts.getText().toString()),"", tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                    if (PrintWired.usbPrint(MainActivity.this,syy)){

                    }else {
                        printUtil1.printstring(syy);

                        mService.sendMessage(syy, "GBK");
                    }
//                    printUtil1.printstring(syy);

//                    printUtil1.printstring(syy);
//                    mService.sendMessage(syy, "GBK");
//                    USBPrinters.initPrinter(getApplicationContext());
//                    USBPrinters.getInstance().print(syy);
//                    USBPrinters.destroyPrinter();
                }

                for (int r = 0; r < commodities.size(); r++) {
                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    while (cursor.moveToNext()) {
                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                        String newnums = (Double.parseDouble(nums) - entty.get(r).getNumber()) + "";
                        ContentValues values = new ContentValues();
                        values.put("store", newnums);
                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                    }
                }

                SharedUtil.putString("order_id", "");
                mapList.clear();
                commodities.clear();
                entty.clear();

                _Total="0";
                reduce=false;
                discount=false;

                Member_type="0";

                discount_type="reduce";
                num="0";
                discount_goods_id="";

                if (Entty!=null){
                    for (int l=0;l<Entty.size();l++){
                        Entty.get(l).setChecked(false);
                    }
                }
//                for (int j = 0; j < entty.size(); j++) {
//                    entty.get(j).setNumber(1);
//                }
                adapterzhu.notifyDataSetChanged();
                tv_Totalmerchandise.setText(0 + "");
                tv_Total.setText(0.0 + "");
                tv_netreceipts.setText(0 + "");
                Showtotal(0 + "");
                et_inputscancode.setText(0 + "");
                et_keyoard.setText(0 + "");
                keyboard_tv_layout.performClick();
                Rl_yidong.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.GONE);
                Rl_time.setVisibility(View.VISIBLE);

                handlernew.removeCallbacks(runnablenew);
                stopService(new Intent(MainActivity.this,NetWorkService.class));

                //小圆点消失
                Intent Intent1 = new Intent();
                Intent1.setAction("com.yzx.clear");
                sendBroadcast(Intent1);

                new Thread(new ThreadTime()).start();
                handler1 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 2) {
//                                tv_status.setText("正在获取客户支付状态...");
                            ll_jshuang.setVisibility(View.VISIBLE);
                            Rl_time.setVisibility(View.GONE);
                            Rl_xianjin.setVisibility(View.GONE);
                            Rl_yidong.setVisibility(View.GONE);
                        }
                    }
                };
            }
        }

    };

//    public static String ToHexString(byte[] bytes) // 0xae00cf => "AE00CF "
//    {
//        StringBuilder stringBuilder = new StringBuilder("");
//        if (bytes == null || bytes.length <= 0) {
//            return null;
//        }
//        for (int i = 0; i < bytes.length; i++) {
//            int v = bytes[i] & 0xFF;
//            String hv = Integer.toHexString(v);
//            if (hv.length() < 2) {
//                stringBuilder.append(0);
//            }
//            stringBuilder.append(hv);
//        }
//        return stringBuilder.toString();
//
//    }
    public void  change_money(String str){
        OkGo.post(SysUtils.getSellerServiceUrl("cashPay"))
                .tag(this)
                .params("score",(int)Float.parseFloat(tv_netreceipts.getText().toString())/1.0)
                .params("member_id", "90577")
                .params("operator_id", SharedUtil.getString("operator_id"))
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("amount_receivable", "-"+str)
                .params("receive_amount", "-"+str)
                .params("add_change", "0.01")
                .params("mark_text", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("换钱的数据为",""+s);
                    }
                });


    }

    //    现金支付
    public void upcash() {
        if (commodities.size() > 0) {
            mapList.clear();
            discount_goods_id="";
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                if (!commodities.get(i).getName().equals("会员充值")) {
                    map1.put("goods_id", commodities.get(i).getGoods_id());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", entty.get(i).getNumber() + "");
                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("yes")){
                            if (i==commodities.size()-1){
                                discount_goods_id=discount_goods_id+commodities.get(i).getGoods_id();
                            }else {
                                discount_goods_id=discount_goods_id+commodities.get(i).getGoods_id()+",";
                            }
                        }else {

                        }
                    }else {
                        if (i==commodities.size()-1) {
                            discount_goods_id = discount_goods_id + commodities.get(i).getGoods_id() ;
                        }else {
                            discount_goods_id = discount_goods_id + commodities.get(i).getGoods_id() + ",";
                        }
                    }

//                    if (iswholesale){
//                        map1.put("price", commodities.get(i).getPrice());
//                        map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
//                    }else {
//                        map1.put("price", commodities.get(i).getMember_price());
//                        map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                    }
                    String price=commodities.get(i).getPrice();
                    double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                    if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                        if (commodities.get(i).getIs_special_offer()!=null){
                            if (commodities.get(i).getIs_special_offer().equals("no")) {
                                    if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                        if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                            price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    } else {
                                        if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                            price=commodities.get(i).getMember_price();
                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                        }
                                    }
                                }
                            } else {
                                if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                    price=commodities.get(i).getPrice();
                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                }
                            }
                        }else {
                            if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                price=commodities.get(i).getPrice();
                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                            }
                        }

                    map1.put("price", price);
                    map1.put("amount", amount + "");
                    map1.put("cost", commodities.get(i).getCost());

                    map1.put("py", commodities.get(i).getPy());
                    map1.put("PD", commodities.get(i).getPD());
                    map1.put("GD", commodities.get(i).getGD());
                    map1.put("bn", commodities.get(i).getBn());
                    map1.put("product_id", commodities.get(i).getProduct_id());
                    map1.put("good_limit", commodities.get(i).getGood_limit());
                    map1.put("good_stock", commodities.get(i).getGood_stock());
                    map1.put("marketable", commodities.get(i).getMarketable());
                    map1.put("tag_name", commodities.get(i).getTag_name());
                    map1.put("tag_id", commodities.get(i).getTag_id());
                    map1.put("unit", commodities.get(i).getUnit());
                    map1.put("unit_id", commodities.get(i).getUnit_id() + "");
                    map1.put("bncode", commodities.get(i).getBncode());
                }else {
                    map1.put("name",commodities.get(i).getName());
                    map1.put("number", "1");
                    map1.put("cost", commodities.get(i).getCost());
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                }

                mapList.add(map1);
            }
        } else {
            mapList.clear();
            Map<String, String> map1 = new HashMap<>();
            map1.put("name", "无商品收银");
            map1.put("number", "1");
            map1.put("price", tv_netreceipts.getText().toString() + "");
            map1.put("amount", tv_netreceipts.getText().toString() + "");
            mapList.add(map1);
        }
        Gson gson = new Gson();
        final String str = gson.toJson(mapList);
        if (TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) >= 0) {
            ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(MainActivity.this);
            builder.delay(1);
            loadingdialog = new ShapeLoadingDialog(builder);
            loadingdialog.getBuilder().loadText("正在上传数据...");
            loadingdialog.show();

            if (discount){
                discount_type="discount";
                num=_Memberdiscount+"";
            }
            if (reduce){
                discount_type="reduce";
                num=_reduce;
            }

            OkGo.post(SysUtils.getSellerServiceUrl("cashPay"))
                    .tag(this)
                    .params("discount_type",discount_type)
                    .params("num",num)
                    .params("discount_goods_id",discount_goods_id)
                    .params("is_score_pay",is_score_pay)
                    .params("pay_score",pay_score)
                    .params("pay_type",pay_type)
                    .params("pbmember_id",pbmember_id)
                    .params("score",(int)Float.parseFloat(tv_netreceipts.getText().toString())/1.0)
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
                        public void onCacheError(Call call, Exception e) {
                            super.onCacheError(call, e);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Localorder();
                            loadingdialog.dismiss();
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            Log.e("print","上传的参数为"+request.getParams().toString());
                            but_xianjin.setText("支付中");
                        }
                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            but_xianjin.setText("确定");
                            iscash=false;
                            pay_type="0";
                            pay_score="";
                            is_score_pay="no";
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("print","上传的参数为"+s);
                            try {
                                JSONObject jo = new JSONObject(s);
                                JSONObject jo1 = jo.getJSONObject("response");
                                String status = jo1.getString("status");
                                String message=jo1.getString("message");
                                if (status.equals("200")) {
                                    cash_entty = new Cash_entty();
                                    JSONObject jo2 = jo1.getJSONObject("data");
                                    String msg = jo2.getString("msg");
                                    String order_id = jo2.getString("order_id");
                                    String sellername = jo2.getString("sellername");
                                    String payed_time = jo2.getString("payed_time");
                                    cash_entty.setAmount(et_inputscancode.getText().toString());
                                    cash_entty.setChange(tv_Surplus.getText().toString());
                                    cash_entty.setNetreceipt(tv_netreceipts.getText().toString());
                                    cash_entty.setOrder_id(order_id);
                                    cash_entty.setSellername(sellername);
                                    cash_entty.setPayed_time(payed_time);
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    if (commodities.size() > 0) {
                                        if (commodities.get(0).getName().equals("会员充值")) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("member_id", pbmember_id);
                                            map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                                    Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(entty.get(0).getNumber()+"")) + "");
                                            Gson gson = new Gson();
                                            String s1 = gson.toJson(map);
                                            UPmoney(s1);
                                        }
                                    }
                                }else {
                                    tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
                                    tv_amount.setText(et_inputscancode.getText().toString());
                                    tv_change.setText(tv_Surplus.getText().toString());
                                    String order = DateUtils.getorder() + RandomUtils.getrandom();


                                    //商品写入数据库方法
                                    sqLiteDatabase.execSQL("insert into ProOut (cash_id,cash_time,seller_id,amount_receivable,receive_amount,add_change,payment)values(?,?,?,?,?,?,?)", new Object[]{order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash"});
                                    for (int i = 0; i < commodities.size(); i++) {
//                                        String price="0";
//                                        if (iswholesale){
//                                            price=commodities.get(i).getPrice();
//                                        }else {
//                                            price=commodities.get(i).getMember_price();
//
                                        String price=commodities.get(i).getPrice();
                                        double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                                        if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                                            if (commodities.get(i).getIs_special_offer()!=null){
                                                if (commodities.get(i).getIs_special_offer().equals("no")) {
                                                    if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                                        if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                            price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                                        }
                                                    } else {
                                                        if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                            price=commodities.get(i).getMember_price();
                                                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                    price=commodities.get(i).getPrice();
                                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                                }
                                            }
                                        }else {
                                            if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                                price=commodities.get(i).getPrice();
                                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                            }
                                        }

                                        sqLiteDatabase.execSQL("insert into goodsSell (goods_id,name,number,py,price,cost,amount,product_id,bncode,store," +
                                                "good_limit,good_stock,PD,GD,marketable,tag_name,tag_id,unit,unit_id,cash_id,bn) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                                                commodities.get(i).getGoods_id(), commodities.get(i).getName(),
                                                entty.get(i).getNumber(), commodities.get(i).getPy(), price,
                                                commodities.get(i).getCost(), (Float.parseFloat(price) * entty.get(i).getNumber()),
                                                commodities.get(i).getProduct_id(), commodities.get(i).getBncode(), commodities.get(i).getStore(),
                                                commodities.get(i).getGood_limit(), commodities.get(i).getGood_stock(),
                                                commodities.get(i).getPD(), commodities.get(i).getGD(), commodities.get(i).getMarketable(),
                                                commodities.get(i).getTag_name(), commodities.get(i).getTag_id(), commodities.get(i).getUnit(),
                                                commodities.get(i).getUnit_id(), order, commodities.get(i).getBn()});
                                    }
                                    cash_entty.setOrder_id(order);
                                    cash_entty.setNetreceipt(tv_netreceipts.getText().toString());
                                    cash_entty.setAmount(et_inputscancode.getText().toString());
                                    cash_entty.setChange(tv_Surplus.getText().toString());
                                    cash_entty.setPayed_time(DateUtils.getCurDate());
                                    cash_entty.setSellername(SharedUtil.getString("seller_name"));

                                    ll_jshuang.setVisibility(View.GONE);
                                    Rl_time.setVisibility(View.GONE);
                                    Rl_xianjin.setVisibility(View.VISIBLE);
                                    Intent mIntent = new Intent();
                                    mIntent.putExtra("cash_entty",cash_entty);
                                    mIntent.setAction("com.yzx.fupingxianjing");

                                    //发送广播  
                                    sendBroadcast(mIntent);
                                    Cashbox_switch=true;

                                    type = 0;
                                    operational = 0;
                                    SharedUtil.putString("operational", operational + "");
                                    Log.e("print", "operational2" + operational);

                                    adapterzhu.notifyDataSetChanged();
                                    tv_Totalmerchandise.setText(0 + "");
                                    tv_Total.setText(0.0 + "");
                                    tv_netreceipts.setText(0 + "");
                                    Showtotal(0 + "");
                                    et_inputscancode.setText(0 + "");
                                    et_keyoard.setText(0 + "");
                                    keyboard_tv_layout.performClick();
                                    tv_payment.setText(cash_entty.getNetreceipt());

                                    tv_danhao.setText(cash_entty.getOrder_id());
                                    tv_day.setText(cash_entty.getPayed_time());

                                    Rl_xianjin.setVisibility(View.GONE);
                                    ll_jshuang.setVisibility(View.GONE);
                                    Rl_time.setVisibility(View.VISIBLE);
                                    keyboard_tv_layout.performClick();
                                    mIntent.setAction("com.yzx.determination");
                                    mIntent.putExtra("cash_entty", cash_entty);
                                    //发送广播  
                                    sendBroadcast(mIntent);

//                                        for (int j = 0; j < entty.size(); j++) {
//                                            entty.get(j).setNumber(1);
//                                        }
//                                    commodities.clear();
//                                    entty.clear()
                                    Rl_time.setVisibility(View.GONE);
                                    Rl_xianjin.setVisibility(View.GONE);
                                    if (Rl_yidong.getVisibility()==View.VISIBLE){

                                    }else {
                                        ll_jshuang.setVisibility(View.VISIBLE);
                                        Rl_yidong.setVisibility(View.GONE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                loadingdialog.dismiss();
                                if (Entty!=null){
                                    for (int l=0;l<Entty.size();l++){
                                        Entty.get(l).setChecked(false);
                                    }
                                }
                                iscash=false;
                                tv_payment.setText(cash_entty.getNetreceipt());
                                tv_danhao.setText(cash_entty.getOrder_id());
                                tv_day.setText(cash_entty.getPayed_time());

                                SharedUtil.putString("operational", operational + "");
                                Log.e("print", "operational1" + operational);
                                SharedUtil.putString("order_id","");
                                Intent mIntent = new Intent();
                                mIntent.setAction("com.yzx.determination");
                                mIntent.putExtra("cash_entty", cash_entty);
                                //发送广播  
                                sendBroadcast(mIntent);

                                operational = 0;
                                SharedUtil.putString("operational", operational + "");
                                Log.e("print", "operational5" + operational);

                                //现金支付的确定按钮
//                if (commodities.size() > 0) {

//                }

//                if (commodities.size() > 0) {
//                    Intent Intent1 = new Intent();
//                    Intent1.setAction("com.yzx.clear");
//                    sendBroadcast(Intent1);
//                }
                                tv_modes.setText("现金支付");
                                mapList.clear();

                                for (int r = 0; r < commodities.size(); r++) {
                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                    while (cursor.moveToNext()) {
                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                                        String newnums = (Float.parseFloat(nums) - entty.get(r).getNumber()) + "";
                                        ContentValues values = new ContentValues();
                                        values.put("store", newnums);
                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                    }
                                }

                                adapterzhu.notifyDataSetChanged();
                                tv_Totalmerchandise.setText(0 + "");
                                tv_Total.setText(0.0 + "");
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_inputscancode.setText(0 + "");
                                et_keyoard.setText(0 + "");
                                keyboard_tv_layout.performClick();
                                if (cash_entty!=null){

                                }

                                Rl_xianjin.setVisibility(View.GONE);
                                ll_jshuang.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);

                                if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
                                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                                    printUtil1.openButtonClicked();
//                                    String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, entty,
//                                            false, Double.parseDouble(cash_entty.getNetreceipt()), cash_entty.getAmount(), cash_entty.getNetreceipt(), cash_entty.getChange());

                                    Log.d("打印报错", "Order_id=  "+cash_entty.getOrder_id()+"      "+cash_entty.getNetreceipt()+"     "+cash_entty.getAmount()+"      "+cash_entty.getNetreceipt());
                                    String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, entty,
                                            2, Double.parseDouble(cash_entty.getNetreceipt()),"", cash_entty.getAmount(), cash_entty.getNetreceipt(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                    if (PrintWired.usbPrint(MainActivity.this,syy)){

                                    }else {
                                        printUtil1.printstring(syy);

                                        mService.sendMessage(syy, "GBK");
                                    }
//                                    printUtil1.printstring(syy);
//                                    mService.sendMessage(syy, "GBK");
//                                        USBPrinters.initPrinter(getApplicationContext());
//                                        USBPrinters.getInstance().print(syy);
//                                        USBPrinters.destroyPrinter();
                                }
//                                if (SharedUtil.getString("print") != null) {
//                                    if (Boolean.parseBoolean(SharedUtil.getString("print"))) {
//
//                                        USBPrinters.initPrinter(getApplicationContext());
//                                        USBPrinters.getInstance().print(syy);
//                                        USBPrinters.destroyPrinter();
//                                    } else {
//
//                                    }
//                                } else {
//                                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
//                                    printUtil1.openButtonClicked();
//                                    String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, entty,
//                                            false, Double.parseDouble(cash_entty.getNetreceipt()), cash_entty.getAmount(), cash_entty.getNetreceipt(), cash_entty.getChange());
//                                    printUtil1.printstring(syy);
//                                    mService.sendMessage(syy, "GBK");
//                                    USBPrinters.initPrinter(getApplicationContext());
//                                    USBPrinters.getInstance().print(syy);
//                                    USBPrinters.destroyPrinter();
//                                }
//                                for (int j = 0; j < entty.size(); j++) {
//                                    entty.get(j).setNumber(1);
//
                                commodities.clear();
                                entty.clear();

                                _Total="0";
                                reduce=false;
                                discount=false;

                                Member_type="0";

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                Rl_time.setVisibility(View.GONE);
                                Rl_xianjin.setVisibility(View.GONE);
                                if (Rl_yidong.getVisibility()==View.VISIBLE){
                                }else {
                                    ll_jshuang.setVisibility(View.VISIBLE);
                                    Rl_yidong.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }


    //扫码枪移动支付
    public void uppay(final String auth_code) {
        OkGo.getInstance().cancelTag("payment");
        if (commodities.size() > 0) {
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("number", entty.get(i).getNumber() + "");

//                if (iswholesale){
//                    map1.put("price", commodities.get(i).getPrice());
//                    map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
//                }else {
//                    map1.put("price", commodities.get(i).getMember_price());
//                    map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                }

                String price=commodities.get(i).getPrice();
                double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("no")) {
                            if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                    price =StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                }
                            } else {
                                if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                    price=commodities.get(i).getMember_price();
                                    amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                }
                            }
                        }
                    } else {
                        if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                            commodities.get(i).getPrice();
                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                        }
                    }
                }else {
                    if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                        commodities.get(i).getPrice();
                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                    }
                }

                map1.put("price",price);
                map1.put("amount",amount+"");
                map1.put("py", commodities.get(i).getPy());
                map1.put("PD", commodities.get(i).getPD());
                map1.put("bn", commodities.get(i).getBn());
                map1.put("GD", commodities.get(i).getGD());
                map1.put("product_id", commodities.get(i).getProduct_id());
                map1.put("good_limit", commodities.get(i).getGood_limit());
                map1.put("good_stock", commodities.get(i).getGood_stock());
                map1.put("marketable", commodities.get(i).getMarketable());
                map1.put("tag_name", commodities.get(i).getTag_name());
                map1.put("tag_id", commodities.get(i).getTag_id());
                map1.put("unit", commodities.get(i).getUnit());
                map1.put("unit_id", commodities.get(i).getUnit_id() + "");
                map1.put("bncode", commodities.get(i).getBncode());
                mapList.add(map1);
            }
        }
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        final int total_fee = (int) TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()) , 100);
        if (discount){
            discount_type="discount";
            num=_Memberdiscount+"";
        }
        if (reduce){
            discount_type="reduce";
            num=_reduce;
        }

        PostRequest postRequest = OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag("payment")
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE);
        if (commodities.size()>0){
            if (commodities.get(0).getName().equals("会员充值")){
                postRequest.params("member_id",pbmember_id);
                postRequest.params("recharge_id",recharge_id);
            }
        }
        postRequest.params("is_score_pay",is_score_pay)
                .params("pay_score",pay_score)
                .params("type",pay_type)
                .params("pbmember_id",pbmember_id)
                .params("score",(int)Float.parseFloat(tv_netreceipts.getText().toString())/1.0)
//                .params("score",100)
                .params("total_fee", total_fee)
                .params("order_id", SharedUtil.getString("order_id"))
                .params("order_id", SharedUtil.getString("order_id"))
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
                        try {
                            mobile_pay = new Mobile_pay();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                handlernew.removeCallbacks(runnablenew);
                                stopService(new Intent(MainActivity.this,NetWorkService.class));
                                if (commodities.size()>0){
                                    if(commodities.get(0).getName().equals("会员充值")){
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("member_id", pbmember_id);
//                                        map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
//                                                Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");
                                        map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                                Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(entty.get(0).getNumber()+"")) + "");

                                        Gson gson = new Gson();
                                        String s1 = gson.toJson(map);
                                        UPmoney(s1);
                                    }

                                    if(commodities.get(0).getName().equals("换钱")){
                                        /**
                                         *换钱成功
                                         */
                                        change_money(commodities.get(0).getPrice());
                                    }
                                }

                                JSONObject jO2 = jo1.getJSONObject("data");
                                String msg = jO2.getString("msg");
                                JSONObject jo3 = jO2.getJSONObject("data");
                                mobile_pay.setOrder_id(jo3.getString("order_id"));
                                mobile_pay.setName(jo3.getString("sellername"));
                                mobile_pay.setMoney(jo3.getString("money"));
                                mobile_pay.setPayed_time(jo3.getString("payed_time"));
                                tv_danhao.setText(jo3.getString("order_id"));
                                tv_day.setText(jo3.getString("payed_time"));

                                pay_type="0";
                                adapterzhu.setType(false);

                                pay_score="";
                                is_score_pay="no";


                                Text2Speech.isSpeeching();
                                Text2Speech.speech(MainActivity.this,"支付成功"+tv_netreceipts.getText().toString()+"元",4,false);

                                if (SharedUtil.getBoolean("self_print")) {
                                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                                    printUtil1.openButtonClicked();
//                                        String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, mobile_pay.getOrder_id(), mobile_pay.getPayed_time(), commodities, entty,
//                                                true, Double.parseDouble(mobile_pay.getMoney()), mobile_pay.getMoney(), mobile_pay.getMoney(), "0");

                                    String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), tel, mobile_pay.getOrder_id(), mobile_pay.getPayed_time(), commodities, entty,
                                            1, Double.parseDouble(mobile_pay.getMoney()),"", mobile_pay.getMoney(), mobile_pay.getMoney(),tv_Surplus.getText().toString(), "0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                    if (PrintWired.usbPrint(MainActivity.this,syy)){
                                    }else {
                                        printUtil1.printstring(syy);

                                        mService.sendMessage(syy, "GBK");
                                    }
//                                        printUtil1.printstring(syy);
//                                        mService.sendMessage(syy, "GBK");
//                                        USBPrinters.initPrinter(getApplicationContext());
//                                        USBPrinters.getInstance().print(syy);
//                                        USBPrinters.destroyPrinter();
                                }
//                                else {
//                                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
//                                    printUtil1.openButtonClicked();
//                                    String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, mobile_pay.getOrder_id(), mobile_pay.getPayed_time(), commodities, entty,
//                                            true, Double.parseDouble(mobile_pay.getMoney()), mobile_pay.getMoney(), mobile_pay.getMoney(), "0");
//                                    printUtil1.printstring(syy);
//                                    mService.sendMessage(syy, "GBK");
////                                    USBPrinters.initPrinter(getApplicationContext());
////                                    USBPrinters.getInstance().print(syy);
////                                    USBPrinters.destroyPrinter();
//
//                                }

                                type = 0;
                                issucceed=false;
                                operational = 0;
                                SharedUtil.putString("operational", operational + "");
                                Log.e("print", "operational6" + operational);

                                SharedUtil.putString("order_id","");
//                               扫码成功客显二维码消失
                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("mobile_pay", mobile_pay);
                                sendBroadcast(mIntent1);
                                tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                                tv_modes.setText("移动支付");

                                for (int r = 0; r < commodities.size(); r++) {
                                    sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                    Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                    while (cursor.moveToNext()) {
                                        String nums = cursor.getString(cursor.getColumnIndex("store"));
                                        String newnums = (Float.parseFloat(nums) - entty.get(r).getNumber()) + "";
                                        ContentValues values = new ContentValues();
                                        values.put("store", newnums);
                                        sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodities.get(r).getGoods_id()});
                                    }
                                }

                                reduce=false;
                                discount=false;

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                if (Entty!=null){
                                    for (int l=0;l<Entty.size();l++){
                                        Entty.get(l).setChecked(false);
                                    }
                                }

                                mapList.clear();
                                commodities.clear();
                                entty.clear();

                                _Total="0";

                                Member_type="0";

//                                for (int j = 0; j < entty.size(); j++) {
//                                    entty.get(j).setNumber(1);
//                                }
                                adapterzhu.notifyDataSetChanged();
                                tv_Totalmerchandise.setText(0 + "");
                                tv_Total.setText(0.0 + "");
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_inputscancode.setText(0 + "");
                                et_keyoard.setText(0 + "");
                                keyboard_tv_layout.performClick();
//                                tv_status.setText(msg);
                                Rl_yidong.setVisibility(View.GONE);
                                ll_jshuang.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);

                                new Thread(new ThreadTime()).start();
                                handler1 = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (msg.what == 2) {
//                                            tv_status.setText("正在获取客户支付状态...");
                                            if (commodities.size()==0){
                                                ll_jshuang.setVisibility(View.VISIBLE);
                                                Rl_time.setVisibility(View.GONE);
                                                Rl_xianjin.setVisibility(View.GONE);
                                                Rl_yidong.setVisibility(View.GONE);
                                            }else {
                                            }
                                        }
                                        //小圆点消失
                                        Intent Intent1 = new Intent();
                                        Intent1.setAction("com.yzx.clear");
                                        sendBroadcast(Intent1);
                                    }
                                };
                            } else {
                                String message = jo1.getString("message");
//                                tv_status.setText(message);
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.toast_layout, null);
                                TextView tv = (TextView) view.findViewById(R.id.tv);
                                tv.setText(message);
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(view);
                                toast.show();
//                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //定时器5秒
    public class ThreadTime implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                Message mag = new Message();
                mag.what = 2;
                handler1.sendMessage(mag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    String order1;
    public void Addtemporary(final String goods_name,String map){
        order1 = "2"+ DateUtils.getTime().substring(1,10) + RandomUtils.gettowrandom();
        int to=0;
        for (int k=0;k<order1.length();k++){
            if (k%2==0){
                to=to+Integer.parseInt(order1.substring(k,k+1));
            }else {
                to=to+3*Integer.parseInt(order1.substring(k,k+1));
            }
        }
        if (to%10==0){
            order1=order1+"0";
        }else {
            order1=order1+(10-to%10);
        }
        Intent intent=new Intent(MainActivity.this,Addgoodgs_Activity.class);
        Commodity commodity=new Commodity();
        commodity.setGoods_id("null");
        commodity.setBncode(order1);
        commodity.setName(goods_name);
        commodity.setPrice(tv_netreceipts.getText().toString());
        commodity.setIntro(map);
        intent.putExtra("commodity",commodity);
        startActivity(intent);

//        OkGo.post(SysUtils.getSellerorderUrl("createGoodsTempCode"))
//                .tag("temporary")
//                .cacheKey("cacheKey")
//                .cacheMode(CacheMode.NO_CACHE)
//                .params("goods_name",goods_name)
//                .params("goods_info",map)
//                .params("total",tv_netreceipts.getText().toString())
//                .params("bncode",order1)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("print","打印数据为"+s);
//                        try {
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject object=jsonObject.getJSONObject("response");
//                            String status=object.getString("status");
//                            if (status.equals("200")){
//                                setPrint(goods_name,order1,tv_netreceipts.getText().toString());
//                                commodities.clear();
//                                entty.clear();
//                                adapterzhu.notifyDataSetChanged();
//                                Intent Intent1 = new Intent();
//                                Intent1.setAction("com.yzx.clear");
//                                sendBroadcast(Intent1);
//                            }else {
//                                Toast.makeText(MainActivity.this,"生成组合商品失败",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }

    public void setPrint(String name,String code,String price){
        Log.d("print","打印随机数为"+code);
        UsbPrinterUtil mUtil=null;
        if (mUtil==null){
            mUtil = new UsbPrinterUtil(MainActivity.this);
            List<UsbDevice> devs = mUtil.getUsbPrinterList();
            if (devs.size() > 0) {
                mUtil.requestPermission(devs.get(0), null);
            }
        }
        if (mUtil.getUsbPrinterList().size()>0){
            UsbDevice dev = mUtil.getUsbPrinterList().get(0);
            UsbPrinter prt = null;
            UsbPrinter.FONT font = UsbPrinter.FONT.FONT_B;
            Boolean bold = false;
            Boolean underlined = false;
            Boolean doubleHeight = false;
            Boolean doubleWidth = false;
            try {
                prt = new UsbPrinter(this, dev);
                Bitmap bitmap=null;
                Bitmap bitmap1=null;
                Bitmap bitmap2=null;
                Bitmap bitmap3=null;
                Bitmap bitmap4=null;
                Bitmap bitmap5=null;
                Bitmap bitmap6=null;
                String str1 = "SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+SharedUtil.getString("sw_order")+"\\nCLS";
                bitmap= Bitmap_Utils.fromText(name,40);
                bitmap1= Bitmap_Utils.fromText("单价："+price,20);
                bitmap2= Bitmap_Utils.fromText("净含量："+1,20);
                bitmap3= Bitmap_Utils.fromText("日期："+DateUtils.getCurDate1(),20);
                bitmap4= Bitmap_Utils.fromText(SharedUtil.getString("name"),20);
                bitmap5= Bitmap_Utils.fromText("金额/元",20);
                bitmap6= Bitmap_Utils.fromText(price,50);
                String str7 = "\\nDENSITY 3\\nBARCODE 20,55,\"EAN13\",60,1,0,2,4,\"" +code + "\"" + "\\nPRINT 1,1";

                prt.printString(str1, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.write(addBitmap(20,5, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap.getWidth(), bitmap));
                prt.write(addBitmap(20,140, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap1.getWidth(), bitmap1));
                prt.write(addBitmap(20,160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                prt.write(addBitmap(20,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                prt.write(addBitmap(20,200, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                prt.write(addBitmap(160,140, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
                prt.write(addBitmap(220,160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap6.getWidth(), bitmap6));
                prt.printString(str7, font, bold, underlined, doubleHeight,
                        doubleWidth);
                Log.d("print","打印随机数为"+code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //赊账的接口
    public void creditupadats(String st) {
        float total_amount = 0;
        if (commodities.size() > 0) {
            mapList.clear();
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("nums", entty.get(i).getNumber() + "");
                map1.put("cost", commodities.get(i).getCost());
                map1.put("price", commodities.get(i).getPrice());
                map1.put("member_price", commodities.get(i).getMember_price());
                map1.put("orders_status", 2 + "");
                map1.put("pay_status", 0 + "");
                map1.put("mark_text", st);
                if (iswholesale){
                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
                }else {
                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
                }
//            map1.put("py", commodities.get(i).getPy());
//            map1.put("PD", commodities.get(i).getPD());
//            map1.put("GD", commodities.get(i).getGD());
//            map1.put("product_id", commodities.get(i).getProduct_id());
//            map1.put("good_limit", commodities.get(i).getGood_limit());
//            map1.put("good_stock", commodities.get(i).getGood_stock());
//            map1.put("marketable", commodities.get(i).getMarketable());
//            map1.put("tag_name", commodities.get(i).getTag_name());
//            map1.put("tag_id", commodities.get(i).getTag_id());
//            map1.put("unit", commodities.get(i).getUnit());
//            map1.put("unit_id", commodities.get(i).getUnit_id() + "");
//            map1.put("bncode", commodities.get(i).getBncode());
                mapList.add(map1);
            }

            Gson gson = new Gson();
            String str = gson.toJson(mapList);
            OkGo.post(SysUtils.getSellerServiceUrl("shengcheng_order"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("map", str)
                    .params("total_amount", total_amount + "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                JSONObject jo2 = jo.getJSONObject("response");
                                String status = jo2.getString("status");
                                if (status.equals("200")) {
                                    JSONArray ja1 = jo2.getJSONArray("data");
//                                    JSONObject jo3 = ja.getJSONObject(0);
//                                    JSONArray ja1 = jo3.getJSONArray("num");
                                    JSONObject jo4 = ja1.getJSONObject(0);
                                    String num = jo4.getString("num");
                                    if (Integer.valueOf(num) > 0) {
                                        but_cc_credit.setVisibility(View.VISIBLE);
                                        but_cc_credit.setText(num);
                                    } else {
                                        but_cc_credit.setVisibility(View.GONE);
                                    }
                                    commodities.clear();
                                    entty.clear();
                                    _Total="0";
                                    reduce=false;
                                    discount=false;

                                    Member_type="0";

                                    num="0";
                                    discount_goods_id="";

                                    if (Entty!=null){
                                        for (int l=0;l<Entty.size();l++){
                                            Entty.get(l).setChecked(false);
                                        }
                                    }

                                    adapterzhu.notifyDataSetChanged();
                                    Intent Intent1 = new Intent();
                                    Intent1.setAction("com.yzx.clear");
                                    sendBroadcast(Intent1);
                                    tv_Totalmerchandise.setText(0 + "");
                                    tv_Total.setText(0 + "");
                                    tv_netreceipts.setText(0 + "");
                                    Showtotal(0 + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public static long lastClickTime = 0;//上次点击的时间

    public static int spaceTime = 1000;//时间间隔

    public static boolean isFastClick() {

        long currentTime = System.currentTimeMillis();//当前系统时间

        boolean isAllowClick;//是否允许点击

        if (currentTime - lastClickTime > spaceTime) {

            isAllowClick = false;

        } else {

            isAllowClick = true;

        }

        lastClickTime = currentTime;

        return isAllowClick;

    }

    //挂单的本地方法
    public void Localguadan(){
        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        String order = DateUtils.getorder() + RandomUtils.getrandom();
        sqlite_entity.addguadan(commodities,order,entty,"","");
        commodities.clear();
        entty.clear();
        adapterzhu.notifyDataSetChanged();
        Intent Intent1 = new Intent();
        Intent1.setAction("com.yzx.clear");
        sendBroadcast(Intent1);
        Log.e("打印的挂单数据",sqlite_entity.queryguadanorder());
        Log.e("打印的挂单数据",""+sqlite_entity.checkColumnExist1("hangup","table_id"));
        getordernum();
    }

    //挂单的接口
    public void Pendingupadats() {
        float total_amount = 0;
        if (commodities.size() > 0 && ll_check.getVisibility() == View.GONE) {
            Intent intent = new Intent();
            intent.setAction("com.yzx.clear");
            sendBroadcast(intent);
            mapList.clear();
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("nums", entty.get(i).getNumber() + "");
                map1.put("cost", commodities.get(i).getCost());
                map1.put("price", commodities.get(i).getPrice());
                map1.put("member_price", commodities.get(i).getMember_price());

                if (iswholesale){
                    map1.put("is_member","no");
                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
                }else {
                    map1.put("is_member","yes");
                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
                }

                map1.put("orders_status", 1 + "");
                map1.put("pay_status", 0 + "");
//            map1.put("py", commodities.get(i).getPy());
//            map1.put("PD", commodities.get(i).getPD());
//            map1.put("GD", commodities.get(i).getGD());
//            map1.put("product_id", commodities.get(i).getProduct_id());
//            map1.put("good_limit", commodities.get(i).getGood_limit());
//            map1.put("good_stock", commodities.get(i).getGood_stock());
//            map1.put("marketable", commodities.get(i).getMarketable());
//            map1.put("tag_name", commodities.get(i).getTag_name());
//            map1.put("tag_id", commodities.get(i).getTag_id());
//            map1.put("unit", commodities.get(i).getUnit());
//            map1.put("unit_id", commodities.get(i).getUnit_id() + "");
//            map1.put("bncode", commodities.get(i).getBncode());
                mapList.add(map1);
            }

            Gson gson = new Gson();
            String str = gson.toJson(mapList);

            ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(MainActivity.this);
            builder.delay(1);
            loadingdialog = new ShapeLoadingDialog(builder);
            loadingdialog.getBuilder().loadText("正在挂单...");
            loadingdialog.show();

            OkGo.post(SysUtils.getSellerServiceUrl("shengcheng_order"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("map", str)
                    .params("total_amount", total_amount + "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                JSONObject jo2 = jo.getJSONObject("response");
                                String status = jo2.getString("status");
                                if (status.equals("200")) {
                                    JSONObject jo3 = jo2.getJSONObject("data");
//                                    JSONObject jo3 = ja.getJSONObject(0);
                                    JSONArray ja1 = jo3.getJSONArray("num");
                                    JSONObject jo4 = ja1.getJSONObject(0);
                                    String num = jo4.getString("num");
                                    if (Integer.valueOf(num) > 0) {
                                        but_cc_quick.setVisibility(View.VISIBLE);
                                        but_cc_quick.setText(num);
                                    } else {
                                        but_cc_quick.setVisibility(View.GONE);
                                    }

                                    reduce=false;
                                    discount=false;

                                    num="0";
                                    discount_goods_id="";

                                    if (Entty!=null){
                                        for (int l=0;l<Entty.size();l++){
                                            Entty.get(l).setChecked(false);
                                        }
                                    }

                                    commodities.clear();
                                    entty.clear();
                                    _Total="0";
                                    Member_type="0";
                                    adapterzhu.notifyDataSetChanged();
                                    Intent Intent1 = new Intent();
                                    Intent1.setAction("com.yzx.clear");
                                    sendBroadcast(Intent1);
                                    tv_Totalmerchandise.setText(0 + "");
                                    tv_Total.setText(0 + "");
                                    tv_netreceipts.setText(0 + "");
                                    Showtotal(0 + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                getordernum();
                                loadingdialog.dismiss();
                            }
                        }
                    });
        }
    }

    //移动支付获取订单数据
    public void pendingupyidong() {
        float total_amount = 0;
        mapList.clear();
        discount_goods_id="";
        if (commodities.size() > 0) {
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                if (!commodities.get(i).getName().equals("会员充值")&&!commodities.get(i).getName().equals("换钱")&&!commodities.get(i).getName().equals("兑奖")&&!commodities.get(i).getName().equals("活动立减")){
                    map1.put("goods_id", commodities.get(i).getGoods_id());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", entty.get(i).getNumber() + "");

//                    if (iswholesale){
//                        map1.put("price", commodities.get(i).getPrice());
//                        map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber()) + "");
//                        total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
//                    }else {
//                        map1.put("price", commodities.get(i).getMember_price());
//                        map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber()) + "");
//                        total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * entty.get(i).getNumber());
//                    }

                    String price=commodities.get(i).getPrice();
                    double amount=TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + ""));
                    if (commodities.get(i).getType()!=null&&!commodities.get(i).getType().equals("")&&!commodities.get(i).getType().equals("0")) {
                        if (commodities.get(i).getIs_special_offer()!=null){
                            if (commodities.get(i).getIs_special_offer().equals("no")) {
                                if (commodities.get(i).getCustom_member_price() != null && !commodities.get(i).getCustom_member_price().equals("")&& !commodities.get(i).getCustom_member_price().equals("null")) {
                                    if (!StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1].equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                        price=StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1];
                                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(commodities.get(i).getCustom_member_price(), ",")[Integer.parseInt(commodities.get(i).getType()) - 1]), Double.parseDouble(entty.get(i).getNumber() + "")));
                                    }
                                } else {
                                    if (!commodities.get(i).getMember_price().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                        price=commodities.get(i).getMember_price();
                                        amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getMember_price()), Double.parseDouble(entty.get(i).getNumber() + "")));
                                    }
                                }
                            }
                        } else {
                            if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                                price=commodities.get(i).getPrice();
                                amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                            }
                        }
                    }else {
                        if (!commodities.get(i).getPrice().equals("") && !(entty.get(i).getNumber() + "").equals("")) {
                            price=commodities.get(i).getPrice();
                            amount = TlossUtils.add(amount, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(entty.get(i).getNumber() + "")));
                        }
                    }

                        map1.put("price", price);
                        map1.put("amount", amount + "");
                        total_amount += (Float.parseFloat(amount+""));

                    if (commodities.get(i).getIs_special_offer()!=null){
                        if (commodities.get(i).getIs_special_offer().equals("yes")){
                            if (i==commodities.size()-1){
                                discount_goods_id=discount_goods_id+commodities.get(i).getGoods_id();
                            }else {
                                discount_goods_id=discount_goods_id+commodities.get(i).getGoods_id()+",";
                            }
                        }else {

                        }
                    }else {
                        if (i==commodities.size()-1) {
                            discount_goods_id = discount_goods_id + commodities.get(i).getGoods_id() ;
                        }else {
                            discount_goods_id = discount_goods_id + commodities.get(i).getGoods_id() + ",";
                        }
                    }

                    map1.put("cost", commodities.get(i).getCost());
                    map1.put("py", commodities.get(i).getPy());
                    map1.put("PD", commodities.get(i).getPD());
                    map1.put("GD", commodities.get(i).getGD());
                    map1.put("bn", commodities.get(i).getBn());
                    map1.put("product_id", commodities.get(i).getProduct_id());
                    map1.put("good_limit", commodities.get(i).getGood_limit());
                    map1.put("good_stock", commodities.get(i).getGood_stock());
                    map1.put("marketable", commodities.get(i).getMarketable());
                    map1.put("tag_name", commodities.get(i).getTag_name());
                    map1.put("tag_id", commodities.get(i).getTag_id());
                    map1.put("unit", commodities.get(i).getUnit());
                    map1.put("unit_id", commodities.get(i).getUnit_id() + "");
                    map1.put("bncode", commodities.get(i).getBncode());

                    map1.put("orders_status", 1 + "");
                    map1.put("pay_status", 0 + "");


                }else if (commodities.get(i).getName().equals("会员充值")){
                    map1.put("cost", commodities.get(i).getCost());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", "1");
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                }else if(commodities.get(i).getName().equals("换钱")) {
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", "1");
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                    map1.put("cost",tv_netreceipts.getText().toString()+"");
                }else if (commodities.get(i).getName().equals("兑奖")){
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", entty.get(i).getNumber()+"");
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                    map1.put("cost",tv_netreceipts.getText().toString()+"");
                }else if (commodities.get(i).getName().equals("活动立减")){
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", entty.get(i).getNumber()+"");
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                    map1.put("cost",tv_netreceipts.getText().toString()+"");
                }
                mapList.add(map1);
            }
        } else {
            Map<String, String> map1 = new HashMap<>();
            map1.put("name", "无商品收银");
            map1.put("number", "1");
            map1.put("price", tv_netreceipts.getText().toString() + "");
            map1.put("amount", tv_netreceipts.getText().toString() + "");
            mapList.add(map1);
        }
        Gson gson = new Gson();
        final String str = gson.toJson(mapList);

        if (discount){
            discount_type="discount";
            num=_Memberdiscount+"";
        }
        if (reduce){
            discount_type="reduce";
            num=_reduce;
        }
        PostRequest postRequest = OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE);
        if (commodities.size()>0){
            if (commodities.get(0).getName().equals("会员充值")){
                postRequest.params("member_id",pbmember_id);
                postRequest.params("recharge_id",recharge_id);
            }
        }
        postRequest.params("discount_type",discount_type)
                .params("num",num)
                .params("discount_goods_id",discount_goods_id)
                .params("total_fee", (int) TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()) , 100))
                .params("commodity", str)
                .params("operator_id", SharedUtil.getString("operator_id"))
                .params("auth_code", 111111111)
                .params("pay_type", "wxpayjsapi")//
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.d("移动支付上传的接口",request.getParams().toString());
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        SharedUtil.putString("order_id","");
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            Log.e("print", "移动支付的记重" + s);
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                SharedUtil.putString("order_id", jo2.getString("order_id"));
                                String url = jo2.getString("url");
                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("yaner", url);
                                //设置二维码
//                                    createChineseQRCode(url);
                                operational = 1;
                                SharedUtil.putString("operational", operational + "");

                                im_code.setImageBitmap(QRCode.createQRCode(url));
                                mIntent1.putExtra("jinger", tv_netreceipts.getText().toString());
                                sendBroadcast(mIntent1);
                            }else {
                                SharedUtil.putString("order_id","");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //单据未处理提示
    public void getpopup() {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_untreated);
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //    //    赊账
//    public void Accountupadats() {
//        float total_amount = 0;
//        Log.d("print", "挂单" + commodities.size());
//        if (commodities.size() > 0) {
//            mapList.clear();
//            for (int i = 0; i < commodities.size(); i++) {
//                Map<String, String> map1 = new HashMap<>();
//                map1.put("goods_id", commodities.get(i).getGoods_id());
//                map1.put("name", commodities.get(i).getName());
//                map1.put("nums", entty.get(i).getNumber() + "");
//                map1.put("cost", commodities.get(i).getCost());
//                map1.put("price", commodities.get(i).getPrice());
//                map1.put("orders_status", 1 + "");
//                map1.put("pay_status", 0 + "");
//                total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * entty.get(i).getNumber());
//                mapList.add(map1);
//            }
//
//            Gson gson = new Gson();
//            String str = gson.toJson(mapList);
//            OkGo.post(SysUtils.getSellerServiceUrl("shengcheng_order"))
//                    .tag(this)
//                    .cacheKey("cacheKey")
//                    .cacheMode(CacheMode.NO_CACHE)
//                    .params("map", str)
//                    .params("total_amount", total_amount + "")
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//
//
//                        }
//                    });
//
//
//        }
//    }

    //    点击item实现查看商品挂单详情
    public void chankanshangping(String order_id) {

        Sqlite_Entity sqlite_entity=new Sqlite_Entity(MainActivity.this);
        String s=sqlite_entity.queryguadangoods(order_id);
        if (!s.equals("")){
            cancelled.clear();
            cancelledenyyt.clear();
            int unm = 0;
            double j = 0;
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    commoditys = new Commodity();
                    shuliangEntty = new ShuliangEntty();
                    JSONObject jo2 = jsonArray.getJSONObject(i);
                    commoditys.setName(jo2.getString("name"));
                    commoditys.setPrice(jo2.getString("price"));
                    commoditys.setMember_price(jo2.getString("member_price"));
                    commoditys.setStore(jo2.getString("store"));
                    shuliangEntty.setNumber((float) jo2.getDouble("number"));
                    commoditys.setCost(jo2.getString("cost"));
                    commoditys.setGoods_id(jo2.getString("goods_id"));
                    commoditys.setIs_special_offer(jo2.getString("is_special_offer"));
                    commoditys.setBncode(jo2.getString("bncode"));
                    cancelled.add(commoditys);
                    cancelledenyyt.add(shuliangEntty);
                }
                for (int i = 0; i < cancelled.size(); i++) {
                    unm += cancelledenyyt.get(i).getNumber();
                    j = TlossUtils.add(j, (Double.parseDouble(cancelled.get(i).getPrice()) * cancelledenyyt.get(i).getNumber()));
                }
                tv_netreceipts.setText(j + "");
                Showtotal(j + "");
                tv_Totalmerchandise.setText(unm + "");
                tv_Total.setText(StringUtils.stringpointtwo(j + ""));
                adapterzhu.setType(false);
                adapterzhu.getadats(cancelled);
                adapterzhu.getEntty(cancelledenyyt);
                lv.setAdapter(adapterzhu);
                adapterzhu.notifyDataSetChanged();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
//        OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
//                .tag(this)
//                .cacheKey("cacheKey")
//                .cacheMode(CacheMode.DEFAULT)
//                .params("order_id", order_id)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("print","取单的接口"+s);
//                        cancelled.clear();
//                        cancelledenyyt.clear();
//                        int unm = 0;
//                        double j = 0;
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            JSONObject jo1 = jsonObject.getJSONObject("response");
//                            JSONArray ja1 = jo1.getJSONArray("data");
//                            for (int i = 0; i < ja1.length(); i++) {
//                                commoditys = new Commodity();
//                                shuliangEntty = new ShuliangEntty();
//                                JSONObject jo2 = ja1.getJSONObject(i);
//                                commoditys.setName(jo2.getString("name"));
//                                commoditys.setPrice(jo2.getString("price"));
//                                commoditys.setStore(jo2.getString("store"));
//                                shuliangEntty.setNumber(Float.parseFloat(jo2.getString("nums")));
//                                commoditys.setCost(jo2.getString("cost"));
//                                cancelled.add(commoditys);
//                                cancelledenyyt.add(shuliangEntty);
//                            }
//                            for (int i = 0; i < cancelled.size(); i++) {
//                                unm += cancelledenyyt.get(i).getNumber();
//                                j = TlossUtils.add(j, (Double.parseDouble(cancelled.get(i).getPrice()) * cancelledenyyt.get(i).getNumber()));
//                            }
//                            tv_netreceipts.setText(j + "");
//                            Showtotal(j + "");
//                            tv_Totalmerchandise.setText(unm + "");
//                            tv_Total.setText(StringUtils.stringpointtwo(j + ""));
//                            adapterzhu.setType("");
//                            adapterzhu.getadats(cancelled);
//                            adapterzhu.getEntty(cancelledenyyt);
//                            lv.setAdapter(adapterzhu);
//                            adapterzhu.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }

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
        if (StringUtils.isCard(member)){
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
                                    member_entty.setMember_lv_custom_id(jo3.getString("member_lv_custom_id"));
                                    if (!jo3.getString("member_lv_custom_id").equals("0")){
                                        member_entty.setMember_lv_custom_name(jo3.getString("member_lv_custom_name"));
                                    }
                                    member_entty.setMember_lv_custom_key(jo3.getString("member_lv_custom_key"));
                                    member_entties.add(member_entty);
                                }
                                adapter_optimize.setAdats(member_entties);
                                lv_member.setAdapter(adapter_optimize);

                                /**
                                 * 优化会员搜索
                                 */
                            }else {
                                Toast.makeText(MainActivity.this,"没有该会员",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    //使用余额支付
    public void getbalancepaid(final float Memberdiscount,final Double Discount,final Double balance,final android.support.v7.app.AlertDialog dia, String map, String f, int i, final String str,String pay_code,boolean is_pass){
        PostRequest params = OkGo.post(SysUtils.getSellerServiceUrl("surplus_pay"))
                .tag(this)
                .params("type", "1")
                .params("commodity", map)
                .params("total_fee", f)
                .params("score", i)
                .params("pay_code",pay_code)
                .params("member_id",str);
                if (is_pass){
                    params.params("mobile",pay_code);
                }
        params.execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String message=jo1.getString("message");
                            if (status.equals("200")){
                                String data =jo1.getString("data");
                                if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
                                    PrintUtil printUtil1 = new PrintUtil(MainActivity.this);
                                    printUtil1.openButtonClicked();
                                    String syy = BluetoothPrintFormatUtil.getPrinterYUer(SharedUtil.getString("name"), tel, data, DateUtils.getCurDate(), commodities, entty,
                                            true, Double.parseDouble(tv_netreceipts.getText().toString()), tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), "0",Discount+"",balance+"",reduce,_reduce,discount,_discount,Memberdiscount,_Total);
                                    if (PrintWired.usbPrint(MainActivity.this,syy)){
                                    }else {
                                        printUtil1.printstring(syy);
                                        mService.sendMessage(syy, "GBK");
                                    }
//                                    printUtil1.printstring(syy);

//                                    mService.sendMessage(syy, "GBK");
//                                        USBPrinters.initPrinter(getApplicationContext());
//                                        USBPrinters.getInstance().print(syy);
//                                        USBPrinters.destroyPrinter();
                                }


                                reduce=false;
                                discount=false;

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";
                                _Total="0";

                                Member_type="0";

                                rl_jishuang.setVisibility(View.VISIBLE);
                                layout_go_pay.setVisibility(View.GONE);
                                Member_type="0";

                                adapterzhu.setType(false);
                                commodities.clear();
                                entty.clear();

                                if (Entty!=null){
                                    for (int l=0;l<Entty.size();l++){
                                        Entty.get(l).setChecked(false);
                                    }
                                }

                                reduce=false;
                                discount=false;

                                _Total="0";
                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                Member_type="0";

                                adapterzhu.notifyDataSetChanged();

                                tv_Totalmerchandise.setText(0 + "");
                                tv_Total.setText(0.0 + "");
                                tv_netreceipts.setText(0 + "");
                                Showtotal(0 + "");
                                et_inputscancode.setText(0 + "");
                                et_keyoard.setText(0 + "");
                                keyboard_tv_layout.performClick();
//                                tv_status.setText(msg);
                                ll_jshuang.setVisibility(View.VISIBLE);
                                Rl_time.setVisibility(View.GONE);
                                Rl_xianjin.setVisibility(View.GONE);
                                Rl_yidong.setVisibility(View.GONE);

                                Intent Intent1 = new Intent();
                                Intent1.setAction("com.yzx.clear");
                                sendBroadcast(Intent1);
                                if (dia!=null){
                                    dia.dismiss();
                                }
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
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
            operator_id= SharedUtil.getString("operator_id");
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
                            Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //兑换商品的列表
    public void getexchange(){
        final Dialog dialog= new Dialog(MainActivity.this);
        dialog.setTitle("会员");
        dialog.show();
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(false);
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
                    Toast.makeText(MainActivity.this,"积分不足",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

    }

    //充值金额的列表
    private void getData(final String type){
        OkGo.post(SysUtils.getSellerServiceUrl("recharge_list"))
                .tag(this)
                .params("type",type)
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
                            adapter_specification.setType(type);
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
    }

    //积分兑换的接口
    private void Upcredits(String member_id, final int score, final String str){
        String typeword="0";
        if (SharedUtil.getString("type").equals("4")){
            typeword= SharedUtil.getString("operator_id");
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
                                talot= TlossUtils.mul(Double.parseDouble(tv_balance.getText().toString()),
                                        score);
//                                tv_integral.setText(talot+"");
                            }
                            String data=jo1.getString("data");
                            Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static final String TAG = "SampleCodeActivity";
    String debug_str = "";
    private int lcd_width;

    public boolean mWorking=false;

    public void start() {
        mWorking = true;
        if (mReadThread != null && mReadThread.isAlive()) {
        } else {
            mReadThread= new Thread(new Runnable() {
                @Override
                public void run() {
                    while(mWorking) {
                        int size;
                        try {
                            Log.d("获取重量的长度","555555");
                            byte[] buffer = new byte[512];
                            if (mInputStream == null) return;
                            size = mInputStream.read(buffer);
                            Log.d("获取重量的长度","");
                            if (size > 0) {
                                getport(buffer, size);
                            }
                            try
                            {
                                Thread.sleep(50);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            });
            mReadThread.start();
        }
    }

    /**
     * stop thread running
     */
    public void stop() {
        if (mWorking) {
            mWorking = false;
        }
    }

    public void stopThread() {
        if (mReadThread != null) {
            mReadThread.interrupt();
            mReadThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService=null;
        mSerialPortOperaion.StopSerial();
        stop();
        stopThread();
//        mReadThread.destroy();
//        loadingdialog.dismiss();
        //数据库的close
        sqliteHelper.close();
        sqLiteDatabase.close();

        if (ServiceUtils.isServiceRunning(this,"Myservice")){
        }else {
            Intent stopIntent = new Intent(MainActivity.this, Myservice.class);
            stopService(stopIntent);
        }

        DemoApplication.setMainActivity(null);

//        if (m_Device.isDeviceOpen())
//            m_Device.closeDevice();
    }


    final static int Max_Dot = 576;
    public void printStringmoban(View view) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }
    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
    }
    public static List<String> logList = new CopyOnWriteArrayList<String>();
    public AsyncTask<Void, Void, Bitmap> execute;
}
