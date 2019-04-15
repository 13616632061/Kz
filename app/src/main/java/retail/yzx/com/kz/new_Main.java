package retail.yzx.com.kz;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Connector.Connectors;
import Entty.Cash_entty;
import Entty.Commodity;
import Entty.Function_entty;
import Entty.Integral_Entty;
import Entty.Member_entty;
import Entty.Mobile_pay;
import Entty.New_NumberEntty;
import Entty.ShuliangEntty;
import Entty.Specification_Entty;
import Fragments.Cook_fragment;
import Fragments.Order_fragment;
import Fragments.Print_Fragment;
import Fragments.Quick_fragment;
import Utils.AnimationUtil;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.NetUtil;
import Utils.PrintUtil;
import Utils.PrintWired;
import Utils.QRCode;
import Utils.RandomUtils;
import Utils.ScanGunKeyEventHelper;
import Utils.SetEditTextInput;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import adapters.Adapter_Fuzzy;
import adapters.Adapter_integral;
import adapters.Adapter_optimize;
import adapters.Adapter_specification;
import adapters.Function_adapter;
import adapters.New_Adapter_Zhu;
import android_serialport_api.SerialParam;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortOperaion;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import hdx.HdxUtil;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import shujudb.SqliteHelper;
import shujudb.Sqlite_Entity;
import widget.ShapeLoadingDialog;
import widget.Switch;

import static Utils.PrintUtil.context;
import static retail.yzx.com.kz.NetWorkService.isNetBad;

/**
 * Created by admin on 2018/8/10.
 */

public class new_Main extends BaseActivity implements ScanGunKeyEventHelper.OnScanSuccessListener, View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener, Function_adapter.SetOnclickitme, New_Adapter_Zhu.Onremove, Adapter_Fuzzy.SetOnclick, Adapter_optimize.OnClickListener {

    @BindView(R.id.but_more)
    Button but_more;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_Total)
    TextView tv_Total;
    @BindView(R.id.tv_netreceipts)
    TextView tv_netreceipts;
    @BindView(R.id.et_keyoard)
    EditText et_keyoard;
    @BindView(R.id.et_inputscancode)
    EditText et_inputscancode;
    @BindView(R.id.tv_Totalmerchandise)
    TextView tv_Totalmerchandise;
    @BindView(R.id.keyboard_et_layout)
    RelativeLayout keyboard_et_layout;
    @BindView(R.id.keyboard_tv_layout)
    RelativeLayout keyboard_tv_layout;
    @BindView(R.id.but_Cashbox)
    FButton but_Cashbox;
    @BindView(R.id.but_mobilepayment)
    FButton but_mobilepayment;
    @BindView(R.id.tv_cancel)
    FButton tv_cancel;
    //找零
    @BindView(R.id.tv_Surplus)
    EditText tv_Surplus;
    @BindView(R.id.Rl_xianjin)
    RelativeLayout Rl_xianjin;
    @BindView(R.id.Rl_yidong)
    RelativeLayout Rl_yidong;
    @BindView(R.id.ll_jshuang)
    LinearLayout ll_jshuang;
    @BindView(R.id.Rl_time)
    RelativeLayout Rl_time;
    @BindView(R.id.tv_xianjin_netreceipt)
    TextView tv_xianjin_netreceipt;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_change)
    TextView tv_change;
    @BindView(R.id.but_xianjin)
    Button but_xianjin;
    @BindView(R.id.tv_payment)
    TextView tv_payment;
    @BindView(R.id.tv_danhao)
    TextView tv_danhao;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_modes)
    TextView tv_modes;
    @BindView(R.id.im_code)
    ImageView im_code;
    @BindView(R.id.but_internet)
    Button but_internet;
    @BindView(R.id.tv_yidong)
    TextView tv_yidong;
    @BindView(R.id.but_time)
    Button but_time;
    @BindView(R.id.but_Quick)
    RadioButton but_Quick;
    @BindView(R.id.but_Cook)
    RadioButton but_Cook;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_weight)
    TextView tv_weight;
    @BindView(R.id.tv_danwei1)
    TextView tv_danwei1;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.ll_function)
    RelativeLayout ll_function;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.btn_isshow_staff)
    Button btn_isshow_staff;
    @BindView(R.id.ll_function_main)
    LinearLayout ll_function_main;
    @BindView(R.id.ll_photographs)
    LinearLayout ll_photographs;
    @BindView(R.id.tv_huanghui)
    TextView tv_huanghui;
    @BindView(R.id.but_reduce)
    Button but_reduce;
    @BindView(R.id.but_Discount)
    Button but_Discount;
    @BindView(R.id.but_Remove)
    Button but_Remove;
    @BindView(R.id.tv_internet)
    TextView tv_internet;
    @BindView(R.id.Rl_more)
    RelativeLayout Rl_more;

    //挂单显示数量的控件
    TextView but_cc_quick;


    //判断功能按钮是否关闭
    boolean isfunction=false;

    //判断打开或者编辑功能按钮
    boolean isEdittext=false;
    //判断打开或者员工权限按钮
    boolean isusershow=false;

    Function_adapter function_adapter;


    //功能按钮的数据
    List<String> liststring;

    //切换的Fragment
    public Fragment mcontext;

    //快捷栏的fragment
    public Fragment quick_fragment ;
    //计重栏的fragment
    public Fragment cook_fragment ;
    private List<Fragment> fragmentList = new ArrayList<>();


    //搜索的判断
    private boolean isgetseek=false;

    //    移动支付实体类
    public Mobile_pay mobile_pay;

    //定时时间
    public Handler handler1;

    //显示时间的定时器
    public Handler handlertime;
    public Runnable runnabletime;

    //移动支付的定时器
    Handler handlernew;
    Runnable runnablenew;

    //判断支付是否成功
    public static boolean issucceed = false;

    //    现金收银的实体类
    public Cash_entty cash_entty=new Cash_entty();

    //用来检测钱箱的打开关闭
    public boolean Cashbox_switch=true;

    //操作判断
    public int operational = 0;

    //    扫描枪的扫描的类别
    public int type = 0;

    //现金支付
    public boolean iscash=false;

    //设置支付是否为会员支付
    public String pay_type="0";

    //会员id
    private String pbmember_id="";

    //会员充值
    private List<Specification_Entty> specification_list=new ArrayList<>();
    private Adapter_specification adapter_specification;
    private ListView lv_recharge;
    //记录点击的是那个会员
    private int specification_unms;

    //上传数组
    public List<Map<String,String>> mapList=new ArrayList<>();

    //判断是批发还是零售
    public boolean iswholesale=true;

    //商品列表的适配器
    public New_Adapter_Zhu adapterzhu;
    //设置副屏的金额
    private SerialPortOperaion mSerialPortOperaion=null;
    public List<Integer> listInt=new ArrayList<>();

    public boolean reduce=false,discount=false;
    public String _reduce="0";
    public String _discount="0";


    public String discount_type="";
    public String num="0";
    public String discount_goods_id="";
    public float _Memberdiscount=10;

    //列表的数字
    List<Commodity> Datas=new ArrayList<>();
    List<New_NumberEntty> numberEntties=new ArrayList<>();
    public New_NumberEntty numberEntty;
    public final static Double MIXMONEY = 99999.00;//付款最大金额
    //输入框选中的标志位
    public boolean isSelected = true;
    public int frequency = 0;

    //加载数据的弹窗
    public ShapeLoadingDialog loadingdialog;
    public double m;

    public SqliteHelper sqliteHelper ;
    public SQLiteDatabase sqLiteDatabase;
    public Sqlite_Entity sqlite_entity;
    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    public List<Commodity> commodities=new ArrayList<>();

    public List<Function_entty> function_entties=new ArrayList<>();
    public List<Function_entty> function=new ArrayList<>();
    //    删除itme的第几条
    public int kselect = -1;

    View view;
    InputMethodManager  imm;
    android.support.v7.app.AlertDialog mAlertDialog_AddNotes;

    public String _Total="0";

    @Override
    protected int getContentId() {
        return R.layout.new_activity_main;
    }


    /**
     * 保存Fragment
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (quick_fragment != null) {
            getSupportFragmentManager().putFragment(outState, HOME_FRAGMENT_KEY, quick_fragment);
        }
        if (cook_fragment != null) {
            getSupportFragmentManager().putFragment(outState, DASHBOARD_FRAGMENT_KEY, cook_fragment);
        }
        super.onSaveInstanceState(outState);
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
                getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();

    }

    /**
     * 添加fragment
     */
    private void addFragment(Fragment fragment) {

        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.Rl_quick, fragment).commit();
        /*添加到 fragmentList*/
            fragmentList.add(fragment);
        }
    }

    @Override
    protected void init() {
        super.init();
        //隐藏底部按钮
        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(new_Main.this);
        retail.yzx.com.supper_self_service.Utils.StringUtils.setupUI(this, findViewById(R.id.main_rl));//点击空白处隐藏软键盘
        //数据库操作初始化
        sqlite_entity=new Sqlite_Entity(context);
        //条形码工具类
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        //副屏显示金额
        mSerialPortOperaion = new SerialPortOperaion(null,
                new SerialParam(2400,"/dev/ttyS3",0));
        try {
            mSerialPortOperaion.StartSerial();
        }catch (Exception e) {
        }

        SharedUtil.putString("operational", "0");

        initView();

        getphone();

        /**
         * 判断是否锁屏
         */
        if (SharedUtil.getString("lock")!=null){
            if (SharedUtil.getString("lock").equals("lock")){
                setLock();
            }
        }

        //会员兑换商品列表
        integral_list=new ArrayList<>();
        specification_list=new ArrayList<>();

        //判断是否显示千克
        if (SharedUtil.getBoolean("is_kg")){
            is_kg=true;
        }else {
            is_kg=false;
        }

        if (isnetworknew){
            tv_internet.setVisibility(View.GONE);
            upnotnet();
        }else {
            tv_internet.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        //edittext不弹出虚拟键盘
        SetEditTextInput.setPricePoint(tv_Surplus);
        tv_Surplus.setInputType(InputType.TYPE_NULL);

        et_keyoard.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(et_keyoard);
        et_inputscancode.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(et_inputscancode);
        et_inputscancode.addTextChangedListener(new_Main.this);
        SetEditTextInput.judgeNumber(tv_netreceipts);
        SetEditTextInput.judgeNumber(tv_Total);

        //适配器初始化
        adapterzhu=new New_Adapter_Zhu(new_Main.this);
        adapterzhu.setOnremove(this);
        but_more.setOnClickListener(this);
        keyboard_tv_layout.performClick();

        if (SharedUtil.getString("name") != null) {
            tv_name.setText(SharedUtil.getString("name"));
        }
        setTime();
        but_Quick.setOnCheckedChangeListener(new_Main.this);
        but_Cook.setOnCheckedChangeListener(new_Main.this);
        but_Quick.performClick();
        initport();
    }

    /**
     * 获取挂单数量的接口
     */
    private void getordernum() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_totalnum"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("TAG", "挂单数是" + s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            JSONObject jo2 = jo.getJSONObject("response");
                            String status = jo2.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo3 = jo2.getJSONObject("data");
                                String num = jo3.getString("num");
                                if (but_cc_quick!=null){
                                    if (Integer.valueOf(num) > 0) {
                                        but_cc_quick.setVisibility(View.VISIBLE);
                                        but_cc_quick.setText(num);
                                    } else {
                                        but_cc_quick.setVisibility(View.GONE);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        liststring = new ArrayList<>();
        liststring.add("挂单");
        liststring.add("取单");
        liststring.add("赊账");
        liststring.add("退货");
        liststring.add("兑奖");
        liststring.add("会员");
        liststring.add("换钱");
        liststring.add("删行");
        liststring.add("搜索");
        liststring.add("钱箱");
        liststring.add("锁屏");
        liststring.add("登出");
        liststring.add("同步");
        liststring.add("会员价");
        liststring.add("报表统计");
        liststring.add("商品管理");
        liststring.add("新增商品");
        liststring.add("出入库");
        liststring.add("盘点");
        liststring.add("报损");
        liststring.add("员工");
        liststring.add("报货");
        liststring.add("会员管理");
        liststring.add("设置");
        liststring.add("标签");
        liststring.add("打印");
        liststring.add("消息");
        liststring.add("关于");
        liststring.add("备用金");
        liststring.add("改价");
        liststring.add("计算器");
        liststring.add("旧版");

        //判断是否自动同步
        if (SharedUtil.getString("synchronization") != null) {
            if (Boolean.parseBoolean(SharedUtil.getString("synchronization"))) {
                getAdats();
            } else {
            }
        }


        loadFunction();

        getordernum();



    }

    /**
     * 加载功能按钮的功能
     */
    public void loadFunction(){
        function.clear();
        String string="0";
        if (but_cc_quick!=null) {
            string = but_cc_quick.getText().toString();
        }
        if (ll_function_main!=null){
            ll_function_main.removeAllViews();
        }
        sqlite_entity=new Sqlite_Entity(new_Main.this);
        function=sqlite_entity.queryfunctionnmain();
        if (function.size()>0){
            for (int i=0;i<function.size();i++){
                RelativeLayout functionview;
                if(function.get(i).getName().equals("取单")){
                    functionview = (RelativeLayout) View.inflate(new_Main.this, R.layout.function_itme1_layout, null);
                    but_cc_quick= (TextView) functionview.findViewById(R.id.but_cc_quick);
                    if (!string.equals("0")){
                        but_cc_quick.setVisibility(View.VISIBLE);
                        but_cc_quick.setText(string);
                    }else {
                        but_cc_quick.setVisibility(View.GONE);
                    }
                }else {
                    functionview = (RelativeLayout) View.inflate(new_Main.this, R.layout.function_itme2_layout, null);
                }
                TextView tv_function_name= (TextView) functionview.findViewById(R.id.tv_function_name);
                settop(function.get(i).getName(),tv_function_name);
                final int finalI = i;
                functionview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Onclickfunction(function.get(finalI).getName());
                    }
                });
                ll_function_main.addView(functionview);
            }
        }
    }

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

    /**
     * 数据上传
     */
    private void upnotnet(){
        sqlite_entity=new Sqlite_Entity(new_Main.this);
        String str=sqlite_entity.QueryOrder();
            if (!str.equals("")){
                upNoInternet(str);
            }
    }

    /**
     * 无网数据上传的接口
     * @param str
     */
    private void upNoInternet(String str) {

        OkGo.post(SysUtils.getSellerServiceUrl("not_network_cash_pay"))
                .tag(this)
                .params("map", str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "上传成功的数据为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                sqlite_entity=new Sqlite_Entity(new_Main.this);
                                sqlite_entity.deleteorder();
//                                sqLiteDatabase.execSQL(("delete from  ProOut"));
//                                sqLiteDatabase.execSQL(("delete from  goodsSell"));
                                Toast.makeText(new_Main.this, "数据上传成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 设置动态添加的按钮
     * @param name
     * @param tv_function_name
     */
    public void settop(String name,TextView tv_function_name){
        Drawable top;
        switch (name) {
            case "挂单":
                top = getResources().getDrawable(R.drawable.register);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "取单":
                top = getResources().getDrawable(R.drawable.withdrawal);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "赊账":
                top = getResources().getDrawable(R.drawable.credit);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "退货":
                top = getResources().getDrawable(R.drawable.return_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "兑奖":
                top = getResources().getDrawable(R.drawable.cashaprize);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "会员":
                top = getResources().getDrawable(R.drawable.vip_member);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "换钱":
                top = getResources().getDrawable(R.drawable.change);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "删行":
                top = getResources().getDrawable(R.drawable.delete_one);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "搜索":
                top = getResources().getDrawable(R.drawable.aabbcc);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "钱箱":
                top = getResources().getDrawable(R.drawable.cashbox);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "锁屏":
                top = getResources().getDrawable(R.drawable.lockscreen);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "登出":
                top = getResources().getDrawable(R.drawable.goout);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "同步":
                top = getResources().getDrawable(R.drawable.synchronization);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "会员价":
                top = getResources().getDrawable(R.drawable.vip_price);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "报表统计":
                top = getResources().getDrawable(R.drawable.report);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "商品管理":
                top = getResources().getDrawable(R.drawable.goods_manage);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "新增商品":
                top = getResources().getDrawable(R.drawable.new_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "出入库":
                top = getResources().getDrawable(R.drawable.out_in);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "盘点":
                top = getResources().getDrawable(R.drawable.lnventory);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "报损":
                top = getResources().getDrawable(R.drawable.loss);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "员工":
                top = getResources().getDrawable(R.drawable.staff);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "异常操作":
                top = getResources().getDrawable(R.drawable.back01);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "报货":
                top = getResources().getDrawable(R.drawable.up_goods);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "会员管理":
                top = getResources().getDrawable(R.drawable.member_manage);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "设置":
                top = getResources().getDrawable(R.drawable.setup);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "标签":
                top = getResources().getDrawable(R.drawable.label_print);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "打印":
                top = getResources().getDrawable(R.drawable.print);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "消息":
                top = getResources().getDrawable(R.drawable.news);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "关于":
                top = getResources().getDrawable(R.drawable.about);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "备用金":
                top = getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "改价":
                top = getResources().getDrawable(R.drawable.modify);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "计算器":
                top = getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
            case "旧版":
                top = getResources().getDrawable(R.drawable.spare);// 获取res下的图片drawable
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
                tv_function_name.setCompoundDrawables(null, top, null, null);
                tv_function_name.setText(name);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.yzx.kuaijie");
        registerReceiver(broadcastReceiver, intentFilter3);

        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("com.yzx.order");
        registerReceiver(broadcastReceiver, intentFilter4);

//        com.yzx.chech
        IntentFilter intentFilter5 = new IntentFilter();
        intentFilter5.addAction("com.yzx.chech");
        registerReceiver(broadcastReceiver, intentFilter5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
        mSerialPortOperaion.StopSerial();
    }

    /**
     * stop thread running
     */
    public void stop() {
        if (mWorking) {
            mWorking = false;
        }
    }

    /**
     * 接收广播的方法
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.yzx.kuaijie")) {
                if (Rl_time.getVisibility() == View.VISIBLE) {
                } else {
                    Bundle bundle = intent.getExtras();
                    Commodity commodity = (Commodity) bundle.getSerializable("maidan");
                    if (bundle.getString("type").equals("cook")) {
                        if (!commodity.getCook_position().equals("0")){
                            getweight(commodity);
                        }else {
                            AddCommodity(commodity,1,commodity.getPrice());
                        }
                    } else{
                       AddCommodity(commodity,1,commodity.getPrice());
                    }
                }
            }

            if (action.equals("com.yzx.chech")) {
                Bundle bundle = intent.getExtras();
                final String order_id = (String) bundle.getSerializable("order");
                chankanshangping(order_id);
//                im_tuichu.setClickable(false);
            }

            if (action.equals("com.yzx.order")) {
                Bundle bundle = intent.getExtras();
                final String order_id = (String) bundle.getSerializable("order");
                OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                        .tag(this)
                        .cacheKey("cacheKey")
                        .cacheMode(CacheMode.DEFAULT)
                        .params("order_id", order_id)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.d("print挂单的数量",s);
                                reduce=false;
                                discount=false;
                                commodities.clear();
                                numberEntties.clear();
                                int unm = 0;
                                double t = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONObject jo1 = jsonObject.getJSONObject("response");
                                    String status = jo1.getString("status");
                                    if (status.equals("200")) {
                                    JSONArray ja1 = jo1.getJSONArray("data");
                                    for (int i = 0; i < ja1.length(); i++) {
                                        commoditys = new Commodity();
                                        deleteList(order_id);
                                        New_NumberEntty new_numberEntty = new New_NumberEntty();
                                        JSONObject jo2 = ja1.getJSONObject(i);
                                        commoditys.setName(jo2.getString("name"));
                                        commoditys.setPrice(jo2.getString("price"));
                                        commoditys.setMember_price(jo2.getString("member_price"));
                                        commoditys.setStore(jo2.getString("store"));
                                        new_numberEntty.setNumber((float) jo2.getDouble("nums"));
                                        new_numberEntty.setChecked(false);
                                        commoditys.setCost(jo2.getString("cost"));
                                        commoditys.setGoods_id(jo2.getString("goods_id"));
                                        commoditys.setBncode(jo2.getString("bncode"));
                                        commoditys.setIs_special_offer(jo2.getString("is_special"));
                                        commodities.add(commoditys);
                                        numberEntties.add(new_numberEntty);
                                    }
                                    for (int i = 0; i < commodities.size(); i++) {
                                        unm += numberEntties.get(i).getNumber();
                                        t = TlossUtils.add(t, (Double.parseDouble(StringUtils.stringpointtwo(commodities.get(i).getPrice())) * (float) numberEntties.get(i).getNumber()));
                                    }
                                    tv_netreceipts.setText(t + "");
                                    Showtotal(t + "");
                                    tv_Totalmerchandise.setText(unm + "");
                                    tv_Total.setText(StringUtils.stringpointtwo(t + ""));
                                    adapterzhu.getadats(commodities);
                                    adapterzhu.setType("0");
                                    adapterzhu.getnumber(numberEntties);
                                    lv.setAdapter(adapterzhu);
                                    adapterzhu.notifyDataSetChanged();
                                    Log.d("print", "onSuccess: " + "打印的数量为" + commodities.size());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (ll_photographs.getVisibility()==View.VISIBLE){
                                        ll_photographs.setVisibility(View.GONE);
                                        ll_photographs.setAnimation(AnimationUtil.moveToViewBottom());
                                    }else {
                                        ll_photographs.setVisibility(View.VISIBLE);
                                        ll_photographs.setAnimation(AnimationUtil.moveToViewLocation());
                                    }
                                    if (ll_function.getVisibility()==View.VISIBLE){
                                        ll_function.setVisibility(View.GONE);
                                        ll_function.setAnimation(AnimationUtil.moveToViewBottom());
                                    }
                                }
                            }
                        });
            }
        }
    };


    /**
     * 删除挂单的方法
     * @param order_id
     */
    public void deleteList(String order_id){
        OkGo.post(SysUtils.getSellerServiceUrl("delete_order"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id", order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        getordernum();
                    }
                });
    }



    New_NumberEntty newNumber;
    List<Commodity> cancelled=new ArrayList<>();
    List<New_NumberEntty> cancelledenyyt=new ArrayList<>();
    /**
     * 查看订单的方法
     * @param order_id
     */
    public void chankanshangping(String order_id) {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id", order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","取单的接口"+s);
                        cancelled.clear();
                        cancelledenyyt.clear();
                        int unm = 0;
                        double j = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            JSONArray ja1 = jo1.getJSONArray("data");
                            for (int i = 0; i < ja1.length(); i++) {
                                commoditys = new Commodity();
                                newNumber = new New_NumberEntty();
                                JSONObject jo2 = ja1.getJSONObject(i);
                                commoditys.setName(jo2.getString("name"));
                                commoditys.setPrice(jo2.getString("price"));
                                commoditys.setStore(jo2.getString("store"));
                                newNumber.setNumber(jo2.getInt("nums"));
                                newNumber.setChecked(false);
                                commoditys.setCost(jo2.getString("cost"));
                                commoditys.setMember_price(jo2.getString("member_price"));
                                cancelled.add(commoditys);
                                cancelledenyyt.add(newNumber);
                            }
                            for (int i = 0; i < cancelled.size(); i++) {
                                unm += cancelledenyyt.get(i).getNumber();
                                j = TlossUtils.add(j, (Double.parseDouble(cancelled.get(i).getPrice()) * cancelledenyyt.get(i).getNumber()));
                            }
                            tv_netreceipts.setText(j + "");
                            Showtotal(j + "");
                            tv_Totalmerchandise.setText(unm + "");
                            tv_Total.setText(StringUtils.stringpointtwo(j + ""));
                            adapterzhu.setType("0");
                            adapterzhu.getadats(cancelled);
                            adapterzhu.setType("0");
                            adapterzhu.getnumber(cancelledenyyt);
                            lv.setAdapter(adapterzhu);
                            adapterzhu.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 设置时间的方法
     */
    public void setTime(){
        handlertime = new Handler();
        runnabletime=new Runnable() {
            @Override
            public void run() {
                tv_date.setText(DateUtils.getCurDate());
                handlertime.postDelayed(this,1000);
            }
        };
        handlertime.postDelayed(runnabletime, 1000);
    }

    /**
     * 获取重量的窗口
     * @param commodity 计重的商品
     */
    //判断是否为千克单位
    boolean is_kg=true;
    TextView tv_danwei;
    TextView diatv_weight;
    public void getweight(final Commodity commodity){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(new_Main.this).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_getweight);
        final widget.Switch sw_kg= (widget.Switch) window.findViewById(R.id.sw_kg);
        tv_danwei= (TextView) window.findViewById(R.id.tv_danwei);
        sw_kg.setChecked(SharedUtil.getBoolean("is_kg"));
        sw_kg.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw_kg.setChecked(isChecked);
                SharedUtil.putBoolean("is_kg",isChecked);
                is_kg=isChecked;
            }
        });

        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        Button but_cancel_cancel = (Button) window.findViewById(R.id.but_cancel_cancel);
        TextView tv_weight_name = (TextView) window.findViewById(R.id.tv_weight_name);
        tv_weight_name.setText(commodity.getName());
        TextView tv_weight_price= (TextView) window.findViewById(R.id.tv_weight_price);

        if (iswholesale){
            tv_weight_price.setText(Double.parseDouble(commodity.getPrice())+"");
        }else {
            tv_weight_price.setText(Double.parseDouble(commodity.getMember_price())+"");
        }



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

                if (iswholesale){
                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(editable.toString())))+"");
                }else {
                    tv_weight_total_Price.setText((float)(TlossUtils.mul(Double.parseDouble(commodity.getMember_price()),Double.parseDouble(editable.toString())))+"");
                }


            }
        });

        if (Double.parseDouble(strweigh)!=0&&Double.parseDouble(diatv_weight.getText().toString())!=0){
            diatv_weight.setText(strweigh);
        }

        but_cancel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCommodity(commodity, 1,commodity.getPrice());
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

    String finalStr="0";
    String strweigh="0";

    /**
     * 获取商品重量的方法
     * @param buffer
     * @param size
     */
    public void getport(byte[] buffer, int size){
        String result = new String(buffer,0,size);
        String str="";
        Log.d("获取重量的长度",result.length()+"");
        String isport = StringUtils.isport(result);
        Log.d("获取电子秤的数据",""+isport);
//        if (result.length()>15&&result.indexOf("-") == -1&&result.indexOf("+")!=-1) {
//            str = result.substring(result.indexOf("+"));
//			tv_kg.setText(str.substring(1, str.indexOf("kg")));
//            if (result.indexOf("+") < result.indexOf("kg")) {
                finalStr = isport;
                tv_weight.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!finalStr.equals("")) {
                            if (!is_kg) {
                                tv_weight.setVisibility(View.VISIBLE);
                                tv_weight.setText(finalStr);
                                tv_danwei1.setText("Kg");
                                if (diatv_weight != null) {
                                    diatv_weight.setVisibility(View.VISIBLE);
                                    diatv_weight.setText(finalStr);
                                    tv_danwei.setText("Kg");
                                }
                                strweigh = tv_weight.getText().toString();
                            } else {
                                tv_weight.setVisibility(View.VISIBLE);
                                tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr), (double) 2) + "");
                                tv_danwei1.setText("斤");
                                if (diatv_weight != null) {
                                    diatv_weight.setVisibility(View.VISIBLE);
                                    diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr), (double) 2) + "");
                                    tv_danwei.setText("斤");
                                }
                                strweigh = tv_weight.getText().toString();
                            }
                        }else {
                            tv_weight.setText("0");
                            tv_weight.setVisibility(View.GONE);
                            tv_danwei1.setText("数据错误");
                            if (diatv_weight!=null){
                                tv_danwei.setText("数据错误");
                                diatv_weight.setVisibility(View.GONE);
                                diatv_weight.setText("0");
                            }
                        }
//                        //更新UI  (?<==").*(?="kg)
//                        if (finalStr.indexOf("kg") > 4) {
//                            Log.d("zhu11", "onDataReceived=" + finalStr.substring(1, finalStr.indexOf("kg")));
//                            if (is_kg) {
////                            Log.d("zhu22", "onDataReceived="+finalStr.substring(1, finalStr.indexOf("kg")));
//                                tv_weight.setText(finalStr.substring(1, finalStr.indexOf("kg")));
//                                tv_danwei1.setText("Kg");
//                                if (diatv_weight != null) {
//                                    diatv_weight.setText(finalStr.substring(1, finalStr.indexOf("kg")));
//                                    tv_danwei.setText("Kg");
//                                }
//                                strweigh=tv_weight.getText().toString();
//                                Log.d("zhu22", "onDataReceived=" + tv_weight.getText().toString());
//                            } else {
//                                tv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 2) + "");
//                                tv_danwei1.setText("斤");
//                                if (diatv_weight != null) {
//                                    diatv_weight.setText(TlossUtils.mul(Double.parseDouble(finalStr.substring(1, finalStr.indexOf("kg"))), (double) 2) + "");
//                                    tv_danwei.setText("斤");
//                                }
//                                strweigh=tv_weight.getText().toString();
//                            }
//
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
//            }
//        }
    }
    protected DemoApplication mApplication;
    protected SerialPort mSerialPort=null;
    protected OutputStream mOutputStream=null;
    private InputStream mInputStream=null;
    public boolean mWorking=false;
    private Thread mReadThread;

    //初始化
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
                            byte[] buffer = new byte[512];
                            if (mInputStream == null) return;
                            size = mInputStream.read(buffer);
//                            if (size > 0) {
                                getport(buffer, size);
//                            }
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

    //获取电子秤的数据
    private void initport() {
        mApplication = (DemoApplication) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

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
    /**
     * 将数据写入本地数据库
     */
    private void getAdats() {
        showdialog("正在初始化数据...");
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
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            SharedUtil.putString("acquisition_time", DateUtils.getTime());
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            JSONArray ja2 = j2.getJSONArray("sum");
                            JSONObject jo4 = ja2.getJSONObject(0);
                            Datas.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Commodity commodity = new Commodity();
                                JSONObject jo1 = jsonArray.getJSONObject(j);
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
                            sqlite_entity=new Sqlite_Entity(new_Main.this);
                            sqlite_entity.insertcommodity(Datas);
//                            if (isnetworknew) {
//                                sqLiteDatabase.execSQL(("delete from  commodity"));
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            quick_fragment = new Quick_fragment();
                            showFragment1( quick_fragment);
//                            switchFrament(mcontext,quick_fragment);
                            loadingdialog.dismiss();
                        }
                        Datas.clear();
                    }
                });

    }

    @OnClick({R.id.but_more,R.id.keyboard_et_layout,
            R.id.keyboard_tv_layout,R.id.but_Cashbox,
            R.id.but_mobilepayment,R.id.tv_cancel,
            R.id.but_goback,R.id.but_xianjin,
            R.id.but_time,R.id.but_zhifusucc,
            R.id.tv_huanghui,R.id.but_reduce,
            R.id.but_Discount,R.id.but_Remove,
            R.id.but_add_price,R.id.Rl_more})
    public void bntclick(View view){
        switch (view.getId()){
            case R.id.Rl_more:
                if (SharedUtil.getString("type").equals("4")){
                    getMore();
                }else {
                }
                if (!isfunction){
                    isfunction=true;
                    getMore();
                }else {
                    ll_function.setVisibility(View.GONE);
                    isfunction=false;
                }
                break;
                //加价
            case R.id.but_add_price:
                showAddprice();
                break;
            case R.id.but_Remove:
                String str=TlossUtils.getRemove(tv_netreceipts.getText().toString());
                tv_netreceipts.setText(str + "");
                Showtotal(str+"");
                break;
            case R.id.but_Discount:
                showDiscount();
                break;
            case R.id.but_reduce:
                if (commodities.size()>0){
                    showRecharge();
//                    showreduce();
                }else {
                    Toast.makeText(new_Main.this,"没有商品不能立减",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_more:
                break;
            case R.id.keyboard_et_layout:
                receivable_Layout();
                break;
            case R.id.keyboard_tv_layout:
                netreceipts_Layout();
                break;
                //现金支付
            case R.id.but_Cashbox:
                onclickCashbox();
                break;
                //移动支付
            case R.id.but_mobilepayment:
                upmobile();
                break;
                //支付成功的按钮
            case R.id.but_zhifusucc:
                onclicksucc();
                break;
                //点击一秒取消
            case R.id.but_time:
                Rl_time.setVisibility(View.GONE);
                ll_jshuang.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cancel:
                bnt_cancel();
                break;
                //现金支付的取消
            case R.id.but_goback:
                cashboxcancel();
                break;
                //现金支付的确定
            case R.id.but_xianjin:
                if(isnetworknew){
//                if(SysUtils.isNetworkAvailable(getApplication())){
//                if (SysUtils.isNetworkOnline()){
                    if (!iscash){
                        if (but_xianjin.getText().toString().equals("确定")&&!iscash){
                            iscash=true;
                            upcash();
                        }
                    }
                    Cashbox_switch=true;
                }else {
                    Writelocal();
                }
                break;
            case R.id.tv_huanghui:
                ll_photographs.setVisibility(View.GONE);
                ll_photographs.setAnimation(AnimationUtil.moveToViewBottom());
                if (ll_function.getVisibility()==View.VISIBLE){
                    ll_function.setVisibility(View.VISIBLE);
                    ll_function.setAnimation(AnimationUtil.moveToViewLocation());
                }
                break;
        }
    }

    /**
     * 设置折扣的方法
     */
    public void showAddprice(){
        cancelDailog();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.discount_dialog, null);
        if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        final EditText ed_discount = (EditText) view_add_nums_notes.findViewById(R.id.ed_discount);
        ed_discount.setHint("请输入增加的价格");
        Button but_abolish= (Button) view_add_nums_notes.findViewById(R.id.but_abolish);
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        Button but_submit= (Button) view_add_nums_notes.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double dis_Total=0;
                if (commodities.size()>0&&!ed_discount.getText().toString().equals("")){
                    for (int i=0;i<commodities.size();i++){
                        dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
                    }

                    discount=true;
                    dis_Total=TlossUtils.add(dis_Total,Double.parseDouble(ed_discount.getText().toString()));
                    _discount=TlossUtils.sub(dis_Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(ed_discount.getText().toString())/10)))+"";
                    tv_Total.setText(dis_Total+"");
                    tv_netreceipts.setText(dis_Total+"");
                    Showtotal(dis_Total+"");
                }
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    /**
     * 设置折扣的方法
     */
    public void showDiscount(){
        cancelDailog();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.discount_dialog, null);
        if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        final EditText ed_discount = (EditText) view_add_nums_notes.findViewById(R.id.ed_discount);
        Button but_abolish= (Button) view_add_nums_notes.findViewById(R.id.but_abolish);
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        Button but_submit= (Button) view_add_nums_notes.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                double dis_Total=0;
//                if (commodities.size()>0&&!ed_discount.getText().toString().equals("")){
//                    for (int i=0;i<commodities.size();i++){
//                        dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
//                    }
//
//                    discount=true;
//                    _discount=TlossUtils.sub(dis_Total,TlossUtils.mul(dis_Total,(double) (Double.parseDouble(ed_discount.getText().toString())/10)))+"";
//                    tv_Total.setText(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
//                    tv_netreceipts.setText(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
//                    Showtotal(TlossUtils.mul(dis_Total,(Double.parseDouble(ed_discount.getText().toString())/10))+"");
//                }
                setDiscount(ed_discount.getText().toString());
                mAlertDialog_add_nums_notes.dismiss();
//                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
//                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
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
                        dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(numberEntties.get(i).getNumber() + "")));
                    }else {
                        Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
                    }
                }else {
                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
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

    public double j = 0;
    /**
     * 立减的弹窗
     */
    public void showRecharge(){
        cancelDailog();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.redeem_dialog, null);
        if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        final EditText ed_money = (EditText) view_add_nums_notes.findViewById(R.id.ed_money);
        ed_money.setHint("输入优惠金额");
        Button but_abolish = (Button) view_add_nums_notes.findViewById(R.id.but_abolish);
        Button but_submit = (Button) view_add_nums_notes.findViewById(R.id.but_submit);

        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Commodity commodity = new Commodity();
                New_NumberEntty shuliang = new New_NumberEntty();
                commodity.setName("活动立减");
                commodity.setPrice("-"+ed_money.getText().toString());
                commodity.setMember_price("-"+ed_money.getText().toString());
                shuliang.setNumber(1);
                shuliang.setChecked(false);
                _reduce=ed_money.getText().toString();
                commodity.setCost(0 + "");
                commodity.setStore(200 + "");
                commodity.setGoods_id("null");
                commodities.add(commodity);
                numberEntties.add(shuliang);
                adapterzhu.getadats(commodities);
                adapterzhu.getnumber(numberEntties);
                lv.setAdapter(adapterzhu);

                tv_Totalmerchandise.setText((Float.parseFloat(tv_Totalmerchandise.getText().toString()) + 1) + "");
                j = Double.parseDouble(tv_Total.getText().toString());
                j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
                tv_Total.setText(StringUtils.stringpointtwo(j + ""));
                tv_netreceipts.setText(j + "");
                Showtotal(j + "");
                et_keyoard.setText(j + "");
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                reduce=true;
            }
        });

        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
        }

        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
//        final Dialog dialog = new Dialog(new_Main.this);
//        dialog.setTitle("活动");
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setContentView(R.layout.redeem_dialog);
//        final EditText ed_money = (EditText) window.findViewById(R.id.ed_money);
//        ed_money.setHint("输入优惠金额");
//        Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
//        Button but_submit = (Button) window.findViewById(R.id.but_submit);
//        but_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Commodity commodity = new Commodity();
//                New_NumberEntty shuliang = new New_NumberEntty();
//                commodity.setName("活动立减");
//                commodity.setPrice("-"+ed_money.getText().toString());
//                commodity.setMember_price("-"+ed_money.getText().toString());
//                shuliang.setNumber(1);
//                shuliang.setChecked(false);
//                _reduce=ed_money.getText().toString();
//                commodity.setCost(0 + "");
//                commodity.setStore(200 + "");
//                commodity.setGoods_id("null");
//                commodities.add(commodity);
//                numberEntties.add(shuliang);
//                adapterzhu.getadats(commodities);
//                adapterzhu.getnumber(numberEntties);
//                lv.setAdapter(adapterzhu);
//
//                tv_Totalmerchandise.setText((Float.parseFloat(tv_Totalmerchandise.getText().toString()) + 1) + "");
//                j = Double.parseDouble(tv_Total.getText().toString());
//                j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
//                tv_Total.setText(StringUtils.stringpointtwo(j + ""));
//                tv_netreceipts.setText(j + "");
//                Showtotal(j + "");
//                et_keyoard.setText(j + "");
//                dialog.dismiss();
//                reduce=true;
//            }
//        });
//
//        but_abolish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

    }

    /**
     * 支付成功的按钮
     */
    public void onclicksucc(){
        if (handlernew!=null){
            handlernew.removeCallbacks(runnablenew);
        }
        stopService(new Intent(new_Main.this,NetWorkService.class));

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

    /**
     * 移动支付的接口
     */
    public void upmobile(){
        netreceipts_Layout();
        startService(new Intent(new_Main.this,NetWorkService.class));
        im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.load));
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
                runnablenew=new Runnable() {
                    @Override
                    public void run() {
                        getpay();
                        if(isNetBad){
                            Toast.makeText(new_Main.this,"当前网络较差",Toast.LENGTH_SHORT).show();
                        }
                        handlernew.postDelayed(this,3000);
                    }
                };
                handlernew.postDelayed(runnablenew, 3000);

            }
        } else {
            Writelocalmobile();
        }
    }

    /**
     * 移动支付订单的生成
     */
    public void pendingupyidong() {
        float total_amount = 0;
        mapList.clear();
        if (commodities.size() > 0) {
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                if (!commodities.get(i).getName().equals("会员充值")&&!commodities.get(i).getName().equals("换钱")&&!commodities.get(i).getName().equals("兑奖")&&!commodities.get(i).getName().equals("活动立减")){
                    map1.put("goods_id", commodities.get(i).getGoods_id());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", numberEntties.get(i).getNumber() + "");

                    if (iswholesale){
                        map1.put("price", commodities.get(i).getPrice());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber()) + "");
                        total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber());
                    }else {
                        map1.put("price", commodities.get(i).getMember_price());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber()) + "");
                        total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber());
                    }

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
                    map1.put("number", numberEntties.get(i).getNumber()+"");
                    map1.put("price", tv_netreceipts.getText().toString() + "");
                    map1.put("amount", tv_netreceipts.getText().toString() + "");
                    map1.put("cost",tv_netreceipts.getText().toString()+"");
                }else if (commodities.get(i).getName().equals("活动立减")){
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", numberEntties.get(i).getNumber()+"");
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
        Log.e("print", "移动支付上传信息" + str);
        OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("discount_type",discount_type)
                .params("num",num)
                .params("discount_goods_id",discount_goods_id)
                .params("total_fee", (int) (Float.valueOf(tv_netreceipts.getText().toString()) * 100))
                .params("commodity", str)
                .params("operator_id", SharedUtil.getString("operator_id"))
                .params("auth_code", 111111111)
                .params("pay_type", "wxpayjsapi")//
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        SharedUtil.putString("order_id","");
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
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

    /**
     * 移动支付写入本地的方法
     */
    public void Writelocalmobile(){
        im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.load));
        tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
        tv_amount.setText(et_inputscancode.getText().toString());
        tv_change.setText(tv_Surplus.getText().toString());
        String order = DateUtils.getorder() + RandomUtils.getrandom();
        //商品写入数据库方法

//        order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), "0", "micro"

        sqlite_entity = new Sqlite_Entity(new_Main.this);
        sqlite_entity.insertOrder(order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), "0", "micro",commodities,numberEntties,iswholesale);

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
        if (SharedUtil.getString("url")!=null&&!SharedUtil.getString("url").equals("")){
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
                tv_modes.setText("移动支付");
                SharedUtil.putString("order_id", "");

                    sqlite_entity = new Sqlite_Entity(new_Main.this);

                    sqlite_entity.insertStock(commodities,numberEntties);


                mapList.clear();
                //重置的方法
                bnt_cancel();
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
                    }
                };
            }
        });
    }


    /**
     * 快捷栏计重栏的点击事件
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            //点击快捷栏
            case R.id.but_Quick:
                if (b) {
                    but_Quick.setTextColor(Color.parseColor("#b5b5b5"));
                    but_Cook.setTextColor(Color.parseColor("#ffffff"));
                    if (quick_fragment==null){
                        quick_fragment = new Quick_fragment();
                    }
                    addFragment(quick_fragment);
                    showFragment1( quick_fragment);
                }
                break;
            //点击计重栏
            case R.id.but_Cook:
                if (b) {
                    but_Quick.setTextColor(Color.parseColor("#ffffff"));
                    but_Cook.setTextColor(Color.parseColor("#b5b5b5"));
                    if (cook_fragment==null){
                        cook_fragment = new Cook_fragment();
                    }
                    addFragment(cook_fragment);
                    showFragment1( cook_fragment);
//                    switchFrament(mcontext, cook_fragment);
                }
                break;
        }
    }

    /**
     * frament的切换方法
     * @param from 从这个frament
     * @param to 到这个frament
     */
    public void switchFrament(Fragment from, Fragment to) {
        if (from != to) {
            mcontext = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.add(R.id.Rl_quick, to).commit();
                }

            } else {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.show( to).commitAllowingStateLoss();
                }


            }

        }
    }

    /**
     * 支付完成之后延长5秒
     */
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

    /**
     * 判断支付成功状态
     */
    public void getpay() {
        OkGo.post(SysUtils.getSellerServiceUrl("order_status"))
                .tag(this)
                .params("is_score_pay",is_score_pay)
                .params("pay_score",pay_score)
                .params("type",pay_type)
                .params("pbmember_id",pbmember_id)
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
                                Log.d("print","测试支付密码不减库存");
                                handlernew.removeCallbacks(runnablenew);
                                stopService(new Intent(new_Main.this,NetWorkService.class));

                                im_code.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.load));
                                if (commodities.size()>0){
                                    if(commodities.get(0).getName().equals("会员充值")){
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("member_id", pbmember_id);
                                        map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                                Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(numberEntties.get(0).getNumber()+"")) + "");
                                        Gson gson = new Gson();
                                        String s1 = gson.toJson(map);

                                        UPmoney(s1);
                                    }
                                    if(commodities.get(0).getName().equals("换钱")){

                                        change_money(commodities.get(0).getPrice());
                                    }

                                }
                                SharedUtil.putString("order_id", "");
                                pay_type="0";
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
                                Log.e("print", "支付成功");

                                SharedUtil.putString("order_id","");
                                operational = 0;
                                SharedUtil.putString("operational", operational + "");
                                Log.e("print", "operational6" + operational);

                                type = 0;

                                Log.e("print", "支付成功");
                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("mobile_pay", mobile_pay1);
                                sendBroadcast(mIntent1);
                                if (Double.parseDouble(tv_netreceipts.getText().toString())>0){
                                    tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                                }else {
                                }
                                tv_modes.setText("移动支付");

                                //判断支付为0元的解决方法
                                if (Double.parseDouble(tv_netreceipts.getText().toString())>0){
                                    Text2Speech.isSpeeching();
                                    Text2Speech.speech(new_Main.this,"支付成功"+tv_netreceipts.getText().toString()+"元",4,false);

                                    if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
                                        PrintUtil printUtil1 = new PrintUtil(new_Main.this);
                                        printUtil1.openButtonClicked();

                                        //112233445566
                                        String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgnew(SharedUtil.getString("name"), tel, order_id, time1, commodities, numberEntties,
                                                1, Double.parseDouble(tv_netreceipts.getText().toString()), "", tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), tv_Surplus.getText().toString(), "0", "", "",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                        if (PrintWired.usbPrint(new_Main.this, syy)) {

                                        } else {
                                            printUtil1.printstring(syy);

                                        }
                                    }
                                }


                                    sqlite_entity = new Sqlite_Entity(new_Main.this);

                                    sqlite_entity.insertStock(commodities,numberEntties);

                                mapList.clear();
                                //重置的方法
                                bnt_cancel();

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
                                    }
                                };
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 换钱的方法
     * @param str
     */
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

    /**
     * 现金支付上传数据
     */
    public void upcash() {
        if (commodities.size() > 0) {
            mapList.clear();
            discount_goods_id="";
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                if (!commodities.get(i).getName().equals("会员充值")) {
                    map1.put("goods_id", commodities.get(i).getGoods_id());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", numberEntties.get(i).getNumber() + "");
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
                    if (iswholesale){
                        map1.put("price", commodities.get(i).getPrice());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber()) + "");
                    }else {
                        map1.put("price", commodities.get(i).getMember_price());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber()) + "");
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

        Log.e("print","打印现金支付的数据"+str);

        if (TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) >= 0) {

            String remark="";
            double Discount=0;
            if (reduce){
                Discount=TlossUtils.add(Double.parseDouble(_reduce),Discount);
            }
            if (discount){
                Discount=TlossUtils.add(Double.parseDouble(_discount),Discount);
            }
            remark="折扣优惠"+Discount;
            if (discount){
                discount_type="discount";
                num=_Memberdiscount+"";
            }
            if (reduce){
                discount_type="reduce";
                num=_reduce;
            }
            OkGo.getInstance().cancelTag("cashpay");
            showdialog("正在上传数据");
            OkGo.post(SysUtils.getSellerServiceUrl("cashPay"))
                    .tag("cashpay")
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
//                    .params("remark",remark)
                    .params("mark_text", "")
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.d("打印错误数据","onError"+e.toString());
                            Writelocal();
                            loadingdialog.dismiss();
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            but_xianjin.setText("支付中");
                        }
                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            but_xianjin.setText("确定");
                            iscash=false;
                            pay_type="0";
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
                                    Toast.makeText(new_Main.this, msg, Toast.LENGTH_SHORT).show();
                                    if (commodities.size() > 0) {
                                        if (commodities.get(0).getName().equals("会员充值")) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("member_id", pbmember_id);
                                            map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                                    Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(numberEntties.get(0).getNumber()+"")) + "");
                                            Gson gson = new Gson();
                                            String s1 = gson.toJson(map);
                                            UPmoney(s1);
                                        }
                                    }
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                loadingdialog.dismiss();
                                iscash=false;
                                tv_payment.setText(cash_entty.getNetreceipt());
                                tv_danhao.setText(cash_entty.getOrder_id());
                                tv_day.setText(cash_entty.getPayed_time());

                                SharedUtil.putString("operational", operational + "");
                                SharedUtil.putString("order_id","");
                                Intent mIntent = new Intent();
                                mIntent.setAction("com.yzx.determination");
                                mIntent.putExtra("cash_entty", cash_entty);
                                //发送广播  
                                sendBroadcast(mIntent);

                                operational = 0;
                                SharedUtil.putString("operational", operational + "");
                                Log.e("print", "operational5" + operational);


                                tv_modes.setText("现金支付");
                                mapList.clear();


                                    sqlite_entity=new Sqlite_Entity(new_Main.this);

                                    sqlite_entity.insertStock(commodities,numberEntties);

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
                                    PrintUtil printUtil1 = new PrintUtil(new_Main.this);
                                    printUtil1.openButtonClicked();

                                    Log.d("打印报错", "Order_id=  "+cash_entty.getOrder_id()+"      "+cash_entty.getNetreceipt()+"     "+cash_entty.getAmount()+"      "+cash_entty.getNetreceipt());
                                    String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgnew(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, numberEntties,
                                            2, Double.parseDouble(cash_entty.getNetreceipt()),"", cash_entty.getAmount(), cash_entty.getNetreceipt(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                    if (PrintWired.usbPrint(new_Main.this,syy)){

                                    }else {
                                        printUtil1.printstring(syy);
                                    }
                                }
                                commodities.clear();
                                numberEntties.clear();

                                discount_type="reduce";
                                num="0";
                                discount_goods_id="";

                                reduce=false;
                                discount=false;

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

    /**
     * 写入本地数据
     */
    public void Writelocal(){
        tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
        tv_amount.setText(et_inputscancode.getText().toString());
        tv_change.setText(tv_Surplus.getText().toString());
        String order = DateUtils.getorder() + RandomUtils.getrandom();

        sqlite_entity=new Sqlite_Entity(new_Main.this);
        sqlite_entity.insertOrder(order, DateUtils.getTime(), SharedUtil.getString("seller_id"), tv_netreceipts.getText().toString(), et_inputscancode.getText().toString(), tv_Surplus.getText().toString(), "cash",commodities,numberEntties,iswholesale);

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
            PrintUtil printUtil1 = new PrintUtil(new_Main.this);
            printUtil1.openButtonClicked();

            String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgnew(SharedUtil.getString("name"), tel, cash_entty.getOrder_id(), cash_entty.getPayed_time(), commodities, numberEntties,
                    2, Double.parseDouble(cash_entty.getNetreceipt()),"", cash_entty.getAmount(), cash_entty.getNetreceipt(), tv_Surplus.getText().toString(),"0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);
            if (PrintWired.usbPrint(new_Main.this,syy)){
            }else {
                printUtil1.printstring(syy);
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

        commodities.clear();
        numberEntties.clear();
        Rl_time.setVisibility(View.GONE);
        Rl_xianjin.setVisibility(View.GONE);
        if (Rl_yidong.getVisibility()==View.VISIBLE){

        }else {
            ll_jshuang.setVisibility(View.VISIBLE);
            Rl_yidong.setVisibility(View.GONE);
        }

    }

    /**
     * 现金支付的取消按钮
     */
    public void cashboxcancel(){
        Cashbox_switch=true;
        Rl_xianjin.setVisibility(View.GONE);
        ll_jshuang.setVisibility(View.GONE);
        ll_jshuang.setVisibility(View.VISIBLE);
        operational = 0;

        iscash=false;

        OkGo.getInstance().cancelTag("cashpay");

        but_xianjin.setText("确定");


        Intent Intent1 = new Intent();
        Intent1.setAction("com.yzx.fupingxianjing");
        sendBroadcast(Intent1);
    }

    /**
     * 点击现金支付
     */
    public void onclickCashbox(){
        receivable_Layout();
        if (!tv_netreceipts.getText().toString().equals("") && !et_inputscancode.getText().toString().equals("") && Float.parseFloat(tv_Surplus.getText().toString()) >= 0) {
            if (TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) >= 0 && Double.parseDouble(et_inputscancode.getText().toString()) > 0 && Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
                Cashbox_switch = false;
                if (SysUtils.getSystemModel().equals("rk3288")){
                    SysUtils.OpennewCashbox(new_Main.this);
                }else {
                    SysUtils.OpenCashbox();
                }
                ll_jshuang.setVisibility(View.GONE);
                Rl_time.setVisibility(View.GONE);
                Rl_xianjin.setVisibility(View.VISIBLE);

                tv_xianjin_netreceipt.setText(tv_netreceipts.getText().toString());
                tv_amount.setText(et_inputscancode.getText().toString());
                tv_change.setText(TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()), Double.parseDouble(tv_netreceipts.getText().toString())) + "");

                cash_entty.setAmount(et_inputscancode.getText().toString());
                cash_entty.setChange(tv_Surplus.getText().toString());
                cash_entty.setNetreceipt(tv_netreceipts.getText().toString());

                Intent Intent1 = new Intent();
                Intent1.setAction("com.yzx.fupingxianjing");
                Intent1.putExtra("cash_entty", cash_entty);
                sendBroadcast(Intent1);

                operational = 1;
                SharedUtil.putString("operational", operational + "");
                //挂单数量的获取
                getordernum();
            }
        }
    }

    /**
     * 重置方法
     */
    public void bnt_cancel(){
        tv_netreceipts.setText("0.0");
        Showtotal("0.0");
        et_inputscancode.setText("0.0");
        et_keyoard.setText("");
        tv_Total.setText("0.0");
        tv_Totalmerchandise.setText("0");
        keyboard_et_layout.performClick();

        reduce=false;
        discount=false;

        pay_score="";
        is_score_pay="no";

        discount_type="reduce";
        num="0";
        discount_goods_id="";

        commodities.clear();
        numberEntties.clear();
        pay_type="0";

        operational = 0;
        SharedUtil.putString("operational", operational + "");
        Intent Intent1 = new Intent();
        Intent1.setAction("com.yzx.clear");
        sendBroadcast(Intent1);
        adapterzhu.notifyDataSetChanged();
    }
    /**
     * 选中应收的栏目
     */
    public void receivable_Layout(){
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

    /**
     * 选中实收的栏目
     */
    public void  netreceipts_Layout(){
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

    WindowManager.LayoutParams params;
    /**
     * 点击更多的方法
     */
    public void getMore(){
//        android.support.v7.app.AlertDialog.Builder notes_dialog= new android.support.v7.app.AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
//        view=View.inflate(this,R.layout.dialog_res_add_notes,null);
////        et_notes= (EditText) view.findViewById(R.id.et_notes);
////        String tv_content=textview.getText().toString().trim();
////        if(title.indexOf("编辑")!=-1){
////            et_notes.setText(tv_content);
////            et_notes.setSelection(tv_content.length());
////        }else {
////            et_notes.setHint(hint);
////        }
//
//        TextView tv_search= (TextView) view.findViewById(R.id.tv_search);
////        tv_search.setText(title);
//        Button btn_addnotes_cell= (Button) view.findViewById(R.id.btn_addnotes_cell);
//        Button btn_addnotes_sure= (Button) view.findViewById(R.id.btn_addnotes_sure);
//        //没有键盘自动弹出键盘
//        if (!NoDoubleClickUtils.isSoftShowing(this)) {
//            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        }
//        btn_addnotes_cell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mAlertDialog_AddNotes.dismiss();
//                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
//                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                }
//            }
//        });
////        btn_addnotes_sure.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String et_search_src=et_notes.getText().toString().trim();
////                if (TextUtils.isEmpty(et_search_src)){
////                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(mContext,"输入内容不能为空！",25);
////                    return;
////                }
////                textview.setText(et_search_src);
////                if(monClickSure!=null){
////                    monClickSure.onClickSure();
////                }
////                mAlertDialog_AddNotes.dismiss();
////                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
////                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
////                }
////            }
////        });
//        mAlertDialog_AddNotes = notes_dialog.setView(view).show();
//        mAlertDialog_AddNotes.setCancelable(false);
//        mAlertDialog_AddNotes.show();

//        final Home_Popupwindow home_Popuwindow=new Home_Popupwindow(new_Main.this,R.layout.function_pop_layout,R.id.ll_photographs,1000);
//        home_Popuwindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        home_Popuwindow.showAtLocation(rg, 0, 0, Gravity.TOP);
//        params = getWindow().getAttributes();
//        params.alpha=0.7f;
//        getWindow().setAttributes(params);
//        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        home_Popuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params = getWindow().getAttributes();
//                params.alpha=1f;
//                getWindow().setAttributes(params);
//            }
//        });
        ll_function.setVisibility(View.VISIBLE);
        sqlite_entity=new Sqlite_Entity(new_Main.this);
        sqlite_entity.insertfunction(liststring);
        function_adapter=new Function_adapter(new_Main.this);
        if (SharedUtil.getString("type").equals("4")){
            function_entties = sqlite_entity.queryuser();
        }else {
            function_entties = sqlite_entity.queryfunction();
        }
        function_adapter.setAdats(function_entties,"0");
        function_adapter.setOnclickitme(new_Main.this);
        gridview.setAdapter(function_adapter);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdittext){
                    isEdittext=true;
                    function_adapter.setAdats(function_entties,"1");
                    function_adapter.notifyDataSetChanged();
                }else {
                    isEdittext=false;
                    function_adapter.setAdats(function_entties,"0");
                    function_adapter.notifyDataSetChanged();
                }
            }
        });
        btn_isshow_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isusershow){
                    isusershow=true;
                    function_adapter.setAdats(function_entties,"2");
                    function_adapter.notifyDataSetChanged();
                }else {
                    isusershow=false;
                    function_adapter.setAdats(function_entties,"0");
                    function_adapter.notifyDataSetChanged();
                }
            }
        });


    }

    /**
     * 点击功能的itme
     * @param i 点击的是第几个itme
     * @param type 判断是点击还是编辑
     */
    @Override
    public void Onclickitme(int i, String type) {
        if (type.equals("0")){
            Onclickfunction(function_entties.get(i).getName());
        }else if (type.equals("1")){
            if (Boolean.parseBoolean(function_entties.get(i).getType())){
                sqlite_entity=new Sqlite_Entity(new_Main.this);
                sqlite_entity.insertTYpe(function_entties.get(i).getName(),"false",DateUtils.getTime());
                function_entties.get(i).setType("false");
                function_adapter.notifyDataSetChanged();
            }else {
                if (sqlite_entity.queryfunctionnmain().size()<11){
                sqlite_entity=new Sqlite_Entity(new_Main.this);
                sqlite_entity.insertTYpe(function_entties.get(i).getName(),"true",DateUtils.getTime());
                function_entties.get(i).setType("true");
                function_adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(new_Main.this,"功能栏按钮不能在添加了",Toast.LENGTH_SHORT).show();
                }
            }
        }else if (type.equals("2")){
            if (function_entties.get(i).getSecondary_sequence().equals("0")){
                sqlite_entity=new Sqlite_Entity(new_Main.this);
                sqlite_entity.insertuser(function_entties.get(i).getName(),"1",DateUtils.getTime());
                function_entties.get(i).setSecondary_sequence("1");
                function_adapter.notifyDataSetChanged();
            }else {
                sqlite_entity=new Sqlite_Entity(new_Main.this);
                sqlite_entity.insertuser(function_entties.get(i).getName(),"0",DateUtils.getTime());
                function_entties.get(i).setSecondary_sequence("0");
                function_adapter.notifyDataSetChanged();
            }
        }
        loadFunction();
    }

    /**
     * 判断点击按钮的是哪个
     */
    public void Onclickfunction(String name){
        Log.d("print","点击的是哪个功能按钮"+name);
        switch (name) {
            case "挂单":
                if (commodities.size()>0){
                    upregister();
                }else {
                    Toast.makeText(new_Main.this,"没有商品请选择商品",Toast.LENGTH_SHORT).show();
                }
                break;
            case "取单":
//                if (commodities.size()>0){
//                    Toast.makeText(new_Main.this,"有商品不能取单",Toast.LENGTH_SHORT).show();
//                }else {
//                    Intent intent=new Intent(new_Main.this,Report_Activity.class);
//                    startActivity(intent);
//                }
//                ShowPopupWindow();
//                initPopupWindow();
                if (ll_photographs.getVisibility()==View.VISIBLE){
                    ll_photographs.setVisibility(View.GONE);
                    ll_photographs.setAnimation(AnimationUtil.moveToViewBottom());
                }else {
                    ll_photographs.setVisibility(View.VISIBLE);
                    ll_photographs.setAnimation(AnimationUtil.moveToViewLocation());
                }
                if (ll_function.getVisibility()==View.VISIBLE){
                    ll_function.setVisibility(View.GONE);
                    ll_function.setAnimation(AnimationUtil.moveToViewBottom());
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_report, new Order_fragment(false))
                        .commit();
//                showFragment(R.id.fl_report,new Order_fragment(false));
                break;
            case "赊账":
                if (commodities.size()>0){
                    AddCredit();
                }else {
                    Toast.makeText(new_Main.this,"没有商品请选择商品",Toast.LENGTH_SHORT).show();
                }
                break;
            case "退货":
                if (commodities.size()>0){
                    Returngoods();
                }else {
                    Toast.makeText(new_Main.this,"请扫码商品",Toast.LENGTH_SHORT).show();
                }
                break;
            case "兑奖":
                Setredeem();
                break;
            case "会员":
                Member();
                break;
            case "换钱":
                exchangemoney();
                break;
            case "删行":
                OnclickDelete();
                break;
            case "搜索":
                Seek_Dialog();
                break;
            case "钱箱":
                if (SysUtils.getSystemModel().equals("rk3288")){
                    SysUtils.OpennewCashbox(new_Main.this);
                }else {
                    SysUtils.OpenCashbox();
                }
                break;
            case "锁屏":
                cancelDailog();
                AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
                view_add_nums_notes = View.inflate(new_Main.this, R.layout.refresh_new_dialog, null);
                TextView tv_title= (TextView) view_add_nums_notes.findViewById(R.id.tv_title);
                tv_title.setText("锁屏要输入密码");
                Button but_abolish= (Button) view_add_nums_notes.findViewById(R.id.but_abolish);
                Button but_goto= (Button) view_add_nums_notes.findViewById(R.id.but_goto);
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog_add_nums_notes.dismiss();
                    }
                });
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLock();
                        mAlertDialog_add_nums_notes.dismiss();
                    }
                });
                mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
                mAlertDialog_add_nums_notes.setCancelable(false);
                mAlertDialog_add_nums_notes.show();
                break;
            case "登出":
                if (commodities.size() > 0) {
                    getpopup();
                } else {
                    startActivity(new Intent(new_Main.this, Handover_activity.class));
                    overridePendingTransition(R.anim.main_in, R.anim.main_out);
                }
                break;
            case "同步":
                cancelDailog();
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
                view_add_nums_notes = View.inflate(new_Main.this, R.layout.refresh_new_dialog, null);
                Button but_abolish1= (Button) view_add_nums_notes.findViewById(R.id.but_abolish);
                Button but_goto1= (Button) view_add_nums_notes.findViewById(R.id.but_goto);
                but_abolish1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog_add_nums_notes.dismiss();
                    }
                });
                but_goto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAdats();
                        mAlertDialog_add_nums_notes.dismiss();
                    }
                });
                mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
                mAlertDialog_add_nums_notes.setCancelable(false);
                mAlertDialog_add_nums_notes.show();
                break;
            case "会员价":
                setmember();
                break;
            case "报表统计":
                startActivity("1");
                break;
            case "商品管理":
                startActivity("2");
                break;
            case "新增商品":
                startActivity(new Intent(new_Main.this,Addgoodgs_Activity.class));
                overridePendingTransition(R.anim.main_in, R.anim.main_out);
                break;
            case "出入库":
                startActivity("3");
                break;
            case "盘点":
                startActivity("4");
                break;
            case "报损":
                startActivity("5");
                break;
            case "员工":
                startActivity("6");
                break;
            case "异常操作":
                break;
            case "报货":
                startActivity("7");
                break;
            case "会员管理":
                startActivity("8");
                break;
            case "设置":
                Intent intent=new Intent(new_Main.this,Setting_Activity.class);
                startActivity(intent);
                break;
            case "标签":
                Intent intent1=new Intent(new_Main.this,Printer_activity.class);
                startActivity(intent1);
                break;
            case "打印":
                Intent intent3=new Intent(new_Main.this, Print_Fragment.class);
                startActivity(intent3);
                break;
            case "消息":
                Intent intent2=new Intent(new_Main.this,Member_Activity.class);
                startActivity(intent2);
                break;
            case "关于":
                startActivity(new Intent(new_Main.this,Reard_activity.class));
                break;
            case "备用金":
                break;
            case "改价":
                gaijia();
                break;
            case "计算器":

                break;
            case "旧版":

                break;
        }
    }

    public void gaijia(){
        if (SharedUtil.getString("operational").equals("0")) {
            if (commodities.size() > 0) {
                if (kselect == -1) {
                }else {
                    if (commodities.size() == 1) {
                    } else if (kselect >= 0) {
                        setprice(kselect);
                        kselect = -1;
                    } else {
                        Toast.makeText(new_Main.this, "不能再删除了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 跳转页面
     * @param type
     */
    public void startActivity(String type){
        Intent intent=new Intent(new_Main.this,Report_form_Activity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        overridePendingTransition(R.anim.main_in, R.anim.main_out);
    }

    //订单商品添加数量与备注
    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    //赊账的弹窗
    private void AddCredit() {
        cancelDailog();
        AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.credit_dialog, null);
        Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
        final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
        if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditupadats(ed_mark_text.getText().toString());
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();

    }

    public ListView lv_member;
    public Adapter_optimize adapter_optimize;

    /**
     * 会员
     */
    public void Member(){
        if (pay_type.equals("0")) {
            cancelDailog();
            AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
            view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_memberpaw, null);
            final EditText ed_paw= (EditText) view_add_nums_notes.findViewById(R.id.ed_paw);
            Button but_dimdis = (Button) view_add_nums_notes.findViewById(R.id.but_dimdis);
            Button but_qixiao = (Button) view_add_nums_notes.findViewById(R.id.but_qixiao);
            Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
            lv_member= (ListView) view_add_nums_notes.findViewById(R.id.lv_member);
            adapter_optimize=new Adapter_optimize(new_Main.this);
            adapter_optimize.OnClickListener(new_Main.this);

            final Switch sw_memberpaw= (Switch) view_add_nums_notes.findViewById(R.id.sw_memberpaw);
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


            ed_paw.addTextChangedListener(new TextWatcher() {
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
                    }else if (editable.toString().length()==6){
                        if (StringUtils.isCard(editable.toString())){
                            getsearch_members(editable.toString());
                        }
                    }
                }
            });

            but_dimdis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                    getsearch_members(ed_paw.getText().toString());
                }
            });

            but_qixiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(new_Main.this,Member_Activity.class);
                    startActivity(intent);
                    mAlertDialog_add_nums_notes.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
            but_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                    mAlertDialog_add_nums_notes.dismiss();
                }
            });
            mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
            mAlertDialog_add_nums_notes.setCancelable(true);
            mAlertDialog_add_nums_notes.show();
        }


    }

    /**
     * 关闭弹窗
     */
    public void cancelDailog(){
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    //记录密码
    public String password="";
    //会员的列表
    public List<Member_entty> member_entties=new ArrayList<>();
    /**
     * 获取会员的信息
     * @param member1
     */
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
                                    Member_entty member_entty=new Member_entty();
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
                            }else {
                                Toast.makeText(new_Main.this,"没有该会员",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    TextView tv_balance;
    TextView tv_integral;
    String Score="";
    //判断是否免密
    private String is_require_pass="no";
    //会员消耗的积分
    private String pay_score="";
    //判断是否是抵扣
    private String is_score_pay="no";
    /**
     * 点击会员的列表
     * @param i
     */
    @Override
    public void setonclick(final int i) {
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_member_information, null);

        TextView tv_card_number = (TextView) view_add_nums_notes.findViewById(R.id.tv_card_number);
        TextView tv_discount = (TextView) view_add_nums_notes.findViewById(R.id.tv_discount);
        TextView tv_name = (TextView) view_add_nums_notes.findViewById(R.id.tv_name);
        tv_balance = (TextView) view_add_nums_notes.findViewById(R.id.tv_balance);
        TextView tv_phone = (TextView) view_add_nums_notes.findViewById(R.id.tv_phone);
        tv_integral = (TextView) view_add_nums_notes.findViewById(R.id.tv_integral);
        TextView tv_birthday = (TextView) view_add_nums_notes.findViewById(R.id.tv_birthday);
        TextView tv_time = (TextView) view_add_nums_notes.findViewById(R.id.tv_time);
        TextView tv_remark = (TextView) view_add_nums_notes.findViewById(R.id.tv_remark);
        Button but_discount = (Button) view_add_nums_notes.findViewById(R.id.but_discount);
        Button but_recharge = (Button) view_add_nums_notes.findViewById(R.id.but_recharge);
        Button but_balance_paid = (Button) view_add_nums_notes.findViewById(R.id.but_balance_paid);
        Button but_self_discount = (Button) view_add_nums_notes.findViewById(R.id.but_self_discount);

        but_self_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commodities.size() > 0) {
                    if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
                        Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
                        tv_netreceipts.setText(tolot + "");
                        pay_type = "1";
                        mAlertDialog_add_nums_notes.dismiss();
                    }else {
                        Toast.makeText(new_Main.this,"付款金额为负数",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(new_Main.this,"请选择商品",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //抵扣
        Button but_eductible= (Button) view_add_nums_notes.findViewById(R.id.but_eductible);

        but_eductible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getexchange();
            }
        });

        final EditText ed_Discount= (EditText) view_add_nums_notes.findViewById(R.id.ed_Discount);

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

        /**
         * 折扣
         */
        but_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (commodities.size() > 0) {
//                    if (Double.parseDouble(tv_netreceipts.getText().toString()) > 0) {
//                        Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
//                        tv_netreceipts.setText(tolot + "");
//                        Showtotal(tolot + "");
//                        pay_type = "1";
//                    }
//                }
                Yuezhifu(1.0f,i,mAlertDialog_add_nums_notes);
            }
        });

        //使用余额
        but_balance_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commodities.size() > 0) {
                        Usebalance(ed_Discount,i,mAlertDialog_add_nums_notes);
                }else {
                    Toast.makeText(new_Main.this,"请加入商品",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //充值的按钮
        but_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAlertDialog_add_nums_notes.dismiss();
                Recharge();
                getData("1");
            }
        });

        //兑换商品的按钮
        Button but_dimdis = (Button) view_add_nums_notes.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getexchanges();
            }
        });


        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(true);
        mAlertDialog_add_nums_notes.show();


    }

    /**
     * 余额支付
     * @param Memberdiscount
     * @param j
     * @param dialog
     */
    public void Yuezhifu(final float Memberdiscount,final int j, Dialog dialog){
        float total_amount = 0;
        if (commodities.size() > 0) {
            double dis_Total=0;
            double Total=0;
            double Total1=0;
            for (int i=0;i<commodities.size();i++){
                if (commodities.get(i).getIs_special_offer()!=null) {
                    if (commodities.get(i).getIs_special_offer().equals("no")) {
                        dis_Total = TlossUtils.add(dis_Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(numberEntties.get(i).getNumber() + "")));
                    } else {
                        Total = TlossUtils.add(Total, TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()), Double.parseDouble(numberEntties.get(i).getNumber() + "")));
                    }
                }else {
                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
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
                        map1.put("number", numberEntties.get(i).getNumber() + "");
                        map1.put("nums", numberEntties.get(i).getNumber() + "");
                        if (iswholesale){
                            map1.put("price", commodities.get(i).getPrice());
                            total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber());
                            map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber()) + "");
//                            map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()),Double.parseDouble(commodities.get(i).getPrice()))+"");
                            map1.put("cost", commodities.get(i).getCost()+"");
                        }else {
                            map1.put("price", commodities.get(i).getMember_price());
                            total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber());
                            map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber()) + "");
                            map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()),Double.parseDouble(commodities.get(i).getMember_price()))+"");
                        }
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
                        final android.support.v7.app.AlertDialog dialog2 = new android.support.v7.app.AlertDialog.Builder(new_Main.this).create();
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
                                        Toast.makeText(new_Main.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });
                    }
                }
                dialog.dismiss();
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
    //会员积分兑换
    private List<Integral_Entty> integral_list;
    private ListView lv_exchange;
    private Adapter_integral adapter_integral;

    /**
     * 加载积分兑换商品
     */
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

    /**
     * 抵扣商品
     */
    public void getexchanges(){
        final Dialog dialog= new Dialog(new_Main.this);
        dialog.setTitle("会员");
        dialog.show();
        Window window = dialog.getWindow();
//        dialog.setCanceledOnTouchOutside(false);
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
                    Toast.makeText(new_Main.this,"积分不足",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

    }

    /**
     * 积分抵扣商品
     * @param member_id
     * @param score
     * @param str
     */
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
                            Toast.makeText(new_Main.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 抵扣积分的方法
     */
    public void getexchange(){
        cancelDailog();
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_recharge, null);
        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
            }
        });
        lv_recharge = (ListView) view_add_nums_notes.findViewById(R.id.lv_recharge);
        adapter_specification = new Adapter_specification(new_Main.this);
        adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
            @Override
            public void onclickdialog(int i) {
                if (Integer.parseInt(specification_list.get(i).getVal())<Integer.parseInt(tv_integral.getText().toString())){
                    specification_unms = i;
                    is_score_pay="yes";
                    pay_score=specification_list.get(i).getVal();
                    tv_integral.setText(Integer.parseInt(tv_integral.getText().toString())-Integer.parseInt(pay_score)+"");
                    Commodity commodity = new Commodity();
                    New_NumberEntty shuliang = new New_NumberEntty();
                    commodity.setName("活动立减");
                    commodity.setPrice("-"+specification_list.get(i).getGive());
                    shuliang.setNumber(1);
                    commodity.setCost("0");
                    commodity.setStore(200 + "");
                    commodity.setGoods_id("null");
                    commodities.add(commodity);
                    numberEntties.add(shuliang);
                    adapterzhu.getadats(commodities);
                    adapterzhu.getnumber(numberEntties);
                    lv.setAdapter(adapterzhu);
                    mAlertDialog_add_nums_notes.dismiss();
                    Toast.makeText(new_Main.this,"抵扣"+specification_list.get(i).getGive()+"成功",Toast.LENGTH_SHORT).show();
                    double dis_Total=0;
                    double Total=0;
                    double Total1=0;
                    for (int j=0;j<commodities.size();j++){
                        if (commodities.get(j).getIs_special_offer()!=null){
                            if (commodities.get(j).getIs_special_offer().equals("no")){
                                dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(numberEntties.get(j).getNumber()+"")));
                            }else {
                                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(numberEntties.get(j).getNumber()+"")));
                            }
                        }else {
                            Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(j).getPrice()),Double.parseDouble(numberEntties.get(j).getNumber()+"")));
                        }
                    }
                    Total=TlossUtils.add(Total,dis_Total);
                    tv_Totalmerchandise.setText(commodities.size()+"");
                    tv_Total.setText(Total+"");
                    tv_netreceipts.setText(Total+"");
                    Showtotal(Total+"");
                    Intent mIntent = new Intent();
                    mIntent.setAction("qwer");
                    mIntent.putExtra("yaner", commodity);
                    //发送广播  
                    sendBroadcast(mIntent);

                    Button but_cancel = (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
                    but_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAlertDialog_add_nums_notes.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(new_Main.this,"积分不足",Toast.LENGTH_SHORT).show();
                }
            }
        });
        getData("2");
        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    /**
     * 获取充值规格的数据
     */
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

    /**
     * 充值的方法
     */
    public void Recharge(){

        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_recharge, null);
        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
            }
        });
        lv_recharge = (ListView) view_add_nums_notes.findViewById(R.id.lv_recharge);
        adapter_specification = new Adapter_specification(new_Main.this);
        adapter_specification.SetOnclick(new Adapter_specification.SetOnclick() {
            @Override
            public void onclickdialog(int i) {
                if (commodities.size() == 0) {
                    mAlertDialog_add_nums_notes.dismiss();
                    specification_unms = i;
                    Commodity commodity = new Commodity();
                    New_NumberEntty shuliang = new New_NumberEntty();
                    commodity.setName("会员充值");
                    commodity.setPrice(specification_list.get(i).getVal());
                    shuliang.setNumber(1);
                    shuliang.setChecked(false);
                    commodity.setCost("0");
                    commodity.setStore(200 + "");
                    commodity.setGoods_id("null");
                    commodities.add(commodity);
                    numberEntties.add(shuliang);
                    adapterzhu.getadats(commodities);
                    adapterzhu.getnumber(numberEntties);
                    lv.setAdapter(adapterzhu);


                    tv_Totalmerchandise.setText("1");
                    tv_Total.setText(specification_list.get(i).getVal());
                    tv_netreceipts.setText(specification_list.get(i).getVal());

                    Showtotal(specification_list.get(i).getVal());
                    Intent mIntent = new Intent();
                    mIntent.setAction("qwer");
                    mIntent.putExtra("yaner", commodity);
                    //发送广播  
                    sendBroadcast(mIntent);

                }else {
                    Toast.makeText(new_Main.this,"请清除列表商品",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    /**
     * 使用余额
     */
    public void Usebalance(EditText ed_Discount,int subscript,AlertDialog mAlertDialog_add_nums_notes) {
        float total_amount = 0;
        double dis_Total=0;
        double Total=0;
        double Total1=0;
        for (int i=0;i<commodities.size();i++){
            if (commodities.get(i).getIs_special_offer()!=null){
                if (commodities.get(i).getIs_special_offer().equals("no")){
                    dis_Total=TlossUtils.add(dis_Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
                }else {
                    Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
                }
            }else {
                Total=TlossUtils.add(Total,TlossUtils.mul(Double.parseDouble(commodities.get(i).getPrice()),Double.parseDouble(numberEntties.get(i).getNumber()+"")));
            }
        }
        if (!ed_Discount.getText().toString().equals("")&&ed_Discount.getText().toString()!=null){
            dis_Total=TlossUtils.mul(dis_Total,Double.parseDouble(ed_Discount.getText().toString()));
        }else {
            dis_Total=TlossUtils.mul(dis_Total,1);
            ed_Discount.setText("1");
        }
        Total=TlossUtils.add(Total,TlossUtils.mul(dis_Total,Double.parseDouble(member_entties.get(subscript).getDiscount_rate())));
        Total1=TlossUtils.add(Total,dis_Total);
//                        discount=true;
//                        _discount=TlossUtils.sub(Total1,Total)+"";

//                        _Memberdiscount=(float) Double.parseDouble(ed_discount.getText().toString())/10;
//
//                        tv_Total.setText(Total+"");
//                        tv_netreceipts.setText(Total+"");
//                        Showtotal(Total+"");


//                    Double tolot = TlossUtils.mul(Double.parseDouble(tv_netreceipts.getText().toString()), Double.parseDouble(member_entties.get(i).getDiscount_rate()));
        Double tolot = Total;
        if (Double.parseDouble(member_entties.get(subscript).getSurplus()) >= tolot) {
            Log.e("print", "余额支付传过去的数据为" + tv_netreceipts.getText().toString());
            _Total=tv_netreceipts.getText().toString();
            //优惠金额
            final Double Discount = TlossUtils.sub(Double.parseDouble(tv_netreceipts.getText().toString()), tolot);
            //会员折扣
            final float Memberdiscount = (float) TlossUtils.mul(Double.parseDouble(member_entties.get(subscript).getDiscount_rate()), Double.parseDouble(ed_Discount.getText().toString()));
            //余额
            final Double balance = TlossUtils.sub(Double.parseDouble(member_entties.get(subscript).getSurplus()), tolot);
            tv_netreceipts.setText(tolot + "");
            Showtotal(tolot + "");
            mapList.clear();
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                if (!commodities.get(i).getName().equals("会员充值")) {
                    map1.put("goods_id", commodities.get(i).getGoods_id());
                    map1.put("name", commodities.get(i).getName());
                    map1.put("number", numberEntties.get(i).getNumber() + "");
                    map1.put("nums", numberEntties.get(i).getNumber() + "");
                    if (iswholesale) {
                        map1.put("price", commodities.get(i).getPrice());
                        total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber()) + "");
                        map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getPrice())) + "");
                    } else {
                        map1.put("price", commodities.get(i).getMember_price());
                        total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber());
                        map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber()) + "");
                        map1.put("cost", TlossUtils.add(Double.parseDouble(commodities.get(i).getCost()), Double.parseDouble(commodities.get(i).getMember_price())) + "");
                    }
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
                getbalancepaid(Memberdiscount, Discount, balance, mAlertDialog_add_nums_notes, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(subscript).getMember_id(), member_entties.get(subscript).getMobile(), true);
            }else {
            //余额支付方法
            if (!password.equals("") && password.length() == 15) {
//                getbalancepaid(mAlertDialog_add_nums_notes, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(subscript).getMember_id(), password);
                getbalancepaid(Memberdiscount, Discount, balance, mAlertDialog_add_nums_notes, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(subscript).getMember_id(), member_entties.get(subscript).getMobile(), true);
            } else {
                //输入密码的方法
                mAlertDialog_add_nums_notes.dismiss();
                Usebalansepaw(Memberdiscount, Discount, balance, mAlertDialog_add_nums_notes,subscript,str);
            }
            }
        }else {
            Toast.makeText(new_Main.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 余额移动支付的接口
     * @param Memberdiscount 会员折扣
     * @param Discount 直接折扣
     * @param balance
     * @param dia
     * @param map
     * @param f
     * @param i
     * @param str
     * @param pay_code
     * @param is_pass
     */
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
                        Log.e("print","余额支付的上传数据"+request.getParams().toString());
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.e("print","余额"+e.toString());
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
                                Log.e("print","余额的返回数据"+s);
                                String data =jo1.getString("data");
                                if (SharedUtil.getBoolean("self_print")) {//判断是否自动打印小票
                                    PrintUtil printUtil1 = new PrintUtil(new_Main.this);
                                    printUtil1.openButtonClicked();

                                    String syy = BluetoothPrintFormatUtil.getPrinterYUernew(SharedUtil.getString("name"), tel, data, DateUtils.getCurDate(), commodities, numberEntties,
                                            true, Double.parseDouble(tv_netreceipts.getText().toString()), tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), "0",Discount+"",balance+"",reduce,_reduce,discount,_discount,Memberdiscount,_Total);
                                    if (PrintWired.usbPrint(new_Main.this,syy)){
                                    }else {
                                        printUtil1.printstring(syy);
                                    }
                                }
                                if (mAlertDialog_add_nums_notes!=null){
                                    mAlertDialog_add_nums_notes.dismiss();
                                }
                                bnt_cancel();
                                if (dia!=null){
                                    dia.dismiss();
                                }
                                keyboard_tv_layout.performClick();

                                ll_jshuang.setVisibility(View.VISIBLE);
                                Rl_time.setVisibility(View.GONE);
                                Rl_xianjin.setVisibility(View.GONE);
                                Rl_yidong.setVisibility(View.GONE);
                                Toast.makeText(new_Main.this,message,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(new_Main.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 使用余额输入密码的方法
     */
    public void Usebalansepaw(final float Memberdiscount,final Double Discount,final Double balance,final android.support.v7.app.AlertDialog dia,final int i, final String str){
        cancelDailog();
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_memberpaw, null);
        LinearLayout ll_title= (LinearLayout) view_add_nums_notes.findViewById(R.id.ll_title);
        ll_title.setVisibility(View.GONE);
        Button but_qixiao = (Button) view_add_nums_notes.findViewById(R.id.but_qixiao);
        Button but_dimdis = (Button) view_add_nums_notes.findViewById(R.id.but_dimdis);

        but_dimdis.setVisibility(View.GONE);
        but_qixiao.setVisibility(View.GONE);

        final EditText ed_paw= (EditText) view_add_nums_notes.findViewById(R.id.ed_paw);

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
//                        getbalancepaid(mAlertDialog_add_nums_notes, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString());
                        getbalancepaid(Memberdiscount, Discount, balance, dia, str, tv_netreceipts.getText().toString(), (int) Float.parseFloat(tv_netreceipts.getText().toString()), member_entties.get(i).getMember_id(), ed_paw.getText().toString(),false);
                    } else {
                        Toast.makeText(new_Main.this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(true);
        mAlertDialog_add_nums_notes.show();

    }

    /**
     * 锁屏的方法
     */
    public void setLock(){
        cancelDailog();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        View view_add_nums_notesdialog = View.inflate(new_Main.this, R.layout.credit_dialog, null);
        TextView tv_credit= (TextView) view_add_nums_notesdialog.findViewById(R.id.tv_credit);
        tv_credit.setText("锁屏");
        final EditText ed_mark_text= (EditText) view_add_nums_notesdialog.findViewById(R.id.ed_mark_text);
        ed_mark_text.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button btn_cell_dialog= (Button) view_add_nums_notesdialog.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notesdialog.findViewById(R.id.btn_sure_dialog);
        btn_cell_dialog.setVisibility(View.GONE);
        final AlertDialog mAlertDialog_add_nums_notes1= dialog.setView(view_add_nums_notesdialog).show();
        mAlertDialog_add_nums_notes1.setCancelable(false);
        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_mark_text.getText().toString().equals(SharedUtil.getString("password"))) {
                    SharedUtil.putString("lock","");
                    mAlertDialog_add_nums_notes1.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });
        SharedUtil.putString("lock","lock");
        mAlertDialog_add_nums_notes1.show();
    }

    /**
     * 会员价
     */
    public void setmember(){
        if(!iswholesale){
            Log.d("print","会员价11"+iswholesale);
            iswholesale=true;
//            but_share_tools.setText("零售");
            if (commodities.size()>0){
                adapterzhu.setType("0");
                adapterzhu.notifyDataSetChanged();
                float nums=0;
                double totalMember=0;
                for (int k=0;k<commodities.size();k++){
                    nums=nums+numberEntties.get(k).getNumber();
                    totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(numberEntties.get(k).getNumber()+"")));
                }
                tv_Totalmerchandise.setText(nums+"");
                tv_Total.setText(totalMember+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                tv_netreceipts.setText(totalMember+"");
                Showtotal(totalMember+"");
            }else {
                adapterzhu.setType("0");
            }
        }else {

            iswholesale=false;
//            but_share_tools.setText("批发");
            if (commodities.size()>0){
                if (commodities.get(0).getMember_price()==null){
                    adapterzhu.setType("0");
                }else {
                    adapterzhu.setType("1");
                }
                adapterzhu.notifyDataSetChanged();
                float nums=0;
                double totalMember=0;
                for (int k=0;k<commodities.size();k++){
                    nums=nums+numberEntties.get(k).getNumber();
                    Log.d("print","会员价"+commodities.get(k).getMember_price()+"      "+numberEntties.get(k).getNumber());
                    if (!commodities.get(k).getMember_price().equals("")&&!(numberEntties.get(k).getNumber()+"").equals("")){
                        totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()),Double.parseDouble(numberEntties.get(k).getNumber()+"")));
                    }
                }
                tv_Totalmerchandise.setText(nums+"");
                tv_Total.setText(totalMember+"");
                tv_netreceipts.setText(totalMember+"");
                Showtotal(totalMember+"");
            }else {
                if (adapterzhu!=null){
                    adapterzhu.setType("1");
                }
            }
        }
        Intent intent2=new Intent();
        intent2.setAction("com.yzx.iswholesale");
        intent2.putExtra("iswholesale", iswholesale);
        sendBroadcast(intent2);
    }

    /**
     * 换钱的接口
     */
    public void exchangemoney(){
        cancelDailog();
        AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.credit_dialog, null);
        TextView tv_credit= (TextView) view_add_nums_notes.findViewById(R.id.tv_credit);
        tv_credit.setText("换钱");
        final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
        ed_mark_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        ed_mark_text.setHint("请输入换钱金额");
        Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_mark_text.getText().toString()!=null&&!ed_mark_text.getText().toString().equals("")){
                    Commodity commodity = new Commodity();
                    New_NumberEntty numberEntty = new New_NumberEntty();
                    commodity.setName("换钱");
                    commodity.setPrice(ed_mark_text.getText().toString());
                    commodity.setMember_price(ed_mark_text.getText().toString());
                    numberEntty.setNumber(1);
                    numberEntty.setChecked(false);
                    commodity.setCost(100 + "");
                    commodity.setStore(200 + "");
                    commodity.setGoods_id("null");
                    commodities.add(commodity);
                    numberEntties.add(numberEntty);
                    adapterzhu.getadats(commodities);
                    adapterzhu.getnumber(numberEntties);
                    lv.setAdapter(adapterzhu);
                    tv_Totalmerchandise.setText(numberEntties.size()+"");
                    tv_Total.setText(TlossUtils.add(Double.parseDouble(tv_Total.getText().toString()),Double.parseDouble(ed_mark_text.getText().toString()))+"");
//                                            tv_netreceipts.setText(specification_list.get(i).getVal());
                    tv_netreceipts.setText(TlossUtils.add(Double.parseDouble(tv_netreceipts.getText().toString()),Double.parseDouble(ed_mark_text.getText().toString()))+"");
                    Showtotal(TlossUtils.add(Double.parseDouble(tv_netreceipts.getText().toString()),Double.parseDouble(ed_mark_text.getText().toString()))+"");
                }
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    public Adapter_Fuzzy adapter_fuzzy = new Adapter_Fuzzy(new_Main.this);
    public List<Commodity> list_fuzzy = new ArrayList<Commodity>();
    /**
     * 本地搜索的弹窗
     */
    public void Seek_Dialog(){
        cancelDailog();
        AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.new_fuzzy, null);
        Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
        final ListView lv_fuzzy= (ListView) view_add_nums_notes.findViewById(R.id.lv_fuzzy);
        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSeek(ed_mark_text.getText().toString(),lv_fuzzy);
            }
        });


        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
        }
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();

    }

    /**
     * 本地搜索的接口
     * @param str
     * @param lv_fuzzy
     */
    public void LocalSeek(String str,final ListView lv_fuzzy){
        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor=null;
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        if (m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode like " + "'%" + str + "%'", null);
        }
        p=Pattern.compile("[a-zA-Z]*");
        m=p.matcher(str);
        if(m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where py like " + "'%" + str + "%'", null);
        }
        p=Pattern.compile("[\u4e00-\u9fa5]*");
        m=p.matcher(str);
        if(m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where name like " + "'%" + str + "%'", null);
        }

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                getseek(str, lv_fuzzy);
            } else {
                list_fuzzy.clear();
                while (cursor.moveToNext()) {
                    Commodity fuzzy = new Commodity();
                    fuzzy.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                    fuzzy.setName(cursor.getString(cursor.getColumnIndex("name")));
                    fuzzy.setPy(cursor.getString(cursor.getColumnIndex("py")));
                    fuzzy.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    fuzzy.setCost(cursor.getString(cursor.getColumnIndex("cost")));
                    fuzzy.setStore(cursor.getString(cursor.getColumnIndex("store")));
                    fuzzy.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
                    fuzzy.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
                    fuzzy.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                    fuzzy.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
                    list_fuzzy.add(fuzzy);
                }
                adapter_fuzzy.setAdats(list_fuzzy);
                adapter_fuzzy.SetOnclick(new_Main.this);
                lv_fuzzy.setAdapter(adapter_fuzzy);
                adapter_fuzzy.notifyDataSetChanged();
            }
        }
    }

    /**
     * 线上搜索商品的方法
     * @param str
     * @param lv_fuzzy
     */
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
                        LogUtils.i("搜索的结果",s,"");
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
                                    commodity.setIs_special_offer(jo2.getString("is_special"));
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    list_fuzzy.add(commodity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter_fuzzy.setAdats(list_fuzzy);
                            adapter_fuzzy.SetOnclick(new_Main.this);
                            lv_fuzzy.setAdapter(adapter_fuzzy);
                            adapter_fuzzy.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 点击搜索的商品itme
     * @param i
     */
    @Override
    public void setClickListener(int i) {
        if (!list_fuzzy.get(i).getCook_position().equals("0")){
            getweight(list_fuzzy.get(i));
        }else {
            AddCommodity(list_fuzzy.get(i),1,list_fuzzy.get(i).getPrice());
        }
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
    }

    /**
     * 搜索本地条码的方法
     * @param barcode
     */
    Commodity commoditys;
    public  void getlocal(String barcode){
        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode=?", new String[]{barcode});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                commoditys = new Commodity();
                commoditys.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
                commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                commoditys.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                commoditys.setName(cursor.getString(cursor.getColumnIndex("name")));
                commoditys.setPy(cursor.getString(cursor.getColumnIndex("py")));
                commoditys.setPrice(cursor.getString(cursor.getColumnIndex("price")));
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
                commoditys.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
            }
            if (!commoditys.getCook_position().equals("0")) {
                getweight(commoditys);
            } else {
                AddCommodity(commoditys,1,commoditys.getPrice());
            }
//        commodities.add(commoditys);
        } else {
            getSeek(barcode);
        }
    }

    /**
     * 兑奖的方法
     * @param
     */
    public void Setredeem() {
        if (commodities.size() == 1) {
            cancelDailog();
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
            view_add_nums_notes = View.inflate(new_Main.this, R.layout.credit_dialog, null);
            TextView tv_credit= (TextView) view_add_nums_notes.findViewById(R.id.tv_credit);
            tv_credit.setText("兑奖");
            final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
            ed_mark_text.setHint("请输入兑奖金额");
            ed_mark_text.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
            btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog_add_nums_notes.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
            Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
            btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Redeem(ed_mark_text.getText().toString());
                    mAlertDialog_add_nums_notes.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
            mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
            mAlertDialog_add_nums_notes.setCancelable(false);
            mAlertDialog_add_nums_notes.show();
        } else {
            Toast.makeText(new_Main.this, "请扫码商品兑奖", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 退货的方法
     * @param money
     */
    public void Redeem(final String money){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int k = 0; k < commodities.size(); k++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", commodities.get(k).getName());
            map.put("nums", numberEntties.get(k).getNumber() + "");

            map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getPrice()) + ""));
            map.put("status", "0");

            map.put("seller_id", SharedUtil.getString("seller_id"));
            map.put("goods_id", commodities.get(k).getGoods_id());
            map.put("total_amount", money);
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
                                sqlite_entity=new Sqlite_Entity(new_Main.this);
                                sqlite_entity.insertStock(commodities,numberEntties);

                                bnt_cancel();

                                Toast.makeText(new_Main.this, "兑奖成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(new_Main.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            if (Double.parseDouble(money)==0){

                            }else {
                                Commodity commodity = new Commodity();
                                New_NumberEntty shuliang = new New_NumberEntty();
                                commodity.setName("兑奖");
                                commodity.setPrice(money);
                                commodity.setMember_price(money);
                                shuliang.setNumber(1);
                                shuliang.setChecked(false);
                                commodity.setCost(100 + "");
                                commodity.setStore(200 + "");
                                commodity.setGoods_id("null");
                                commodities.add(commodity);
                                numberEntties.add(shuliang);
                                adapterzhu.getadats(commodities);
                                adapterzhu.getnumber(numberEntties);
                                lv.setAdapter(adapterzhu);
                                tv_Totalmerchandise.setText("1");
                                tv_Total.setText(money);
                                tv_netreceipts.setText(money);
                                Showtotal(money);
                            }
                        }
                    }
                });
    }

    /**
     * 单据未处理
     */
    public void getpopup() {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(new_Main.this).create();
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

    /**
     * 删除的按钮
     */
    public void OnclickDelete(){
        Log.d("print", "print" + SharedUtil.getString("operational"));
        if (SharedUtil.getString("operational").equals("0")) {
            if (commodities.size() > 0) {
                if (kselect == -1) {
                    if (commodities.size() == 1) {
                        if (commodities.size() > 0) {
                            numberEntties.remove(numberEntties.size() - 1);
                        }
//                        stringcontext = "移除" + commodities.get(commodities.size() - 1).getName();
//                                but_time
                        Intent intent = new Intent("com.yzx.fupingdelete");
                        intent.putExtra("delete", commodities.size() - 1);
                        intent.putExtra("commoditys", commodities.get(commodities.size() - 1));
                        sendBroadcast(intent);
                        Showtotal(0 + "");
                        bnt_cancel();

                        adapterzhu.notifyDataSetChanged();
                    } else {
                        double f = Double.parseDouble(tv_Total.getText().toString());

                        if (iswholesale){
                            f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getPrice()) * numberEntties.get(numberEntties.size() - 1).getNumber()));
                        }else {
                            f = TlossUtils.sub(f, (Double.parseDouble(commodities.get(commodities.size() - 1).getMember_price()) * numberEntties.get(numberEntties.size() - 1).getNumber()));
                        }
                        tv_netreceipts.setText(f + "");
                        Showtotal(f + "");
                        if (f == 0) {
                            et_keyoard.setText((int) f + "");
                        } else {
                            et_keyoard.setText(f + "");
                        }

                        tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                        tv_Totalmerchandise.setText("" + (Float.valueOf(tv_Totalmerchandise.getText().toString()) - numberEntties.get(numberEntties.size() - 1).getNumber()));
                        if (commodities.size() > 0) {
                            numberEntties.remove(numberEntties.size() - 1);
                        }
                        if (commodities.get(commodities.size()-1).equals("活动立减")){
                            reduce=false;
                            discount=false;
                        }
                        Intent intent = new Intent("com.yzx.fupingdelete");
                        intent.putExtra("delete", commodities.size() - 1);
                        intent.putExtra("commoditys", commodities.get(commodities.size() - 1));
                        sendBroadcast(intent);
                        Log.d("print", "合计" + f);
                        commodities.remove(commodities.size() - 1);
                        adapterzhu.notifyDataSetChanged();
                    }
                } else if (kselect >= 0) {
                    Log.d("print", "OnclickDelete: 删除的是"+kselect);
                    double f = Double.parseDouble(tv_Total.getText().toString());

                    if (iswholesale){
                        f = TlossUtils.sub(f, Double.valueOf(Double.parseDouble(commodities.get(kselect).getPrice()) * numberEntties.get(kselect).getNumber()));
                    }else {
                        f = TlossUtils.sub(f, Double.valueOf(Double.parseDouble(commodities.get(kselect).getMember_price()) * numberEntties.get(kselect).getNumber()));
                    }


                    tv_Total.setText(StringUtils.stringpointtwo(f + ""));
                    tv_netreceipts.setText(f + "");
                    Showtotal(f + "");
                    if (f == 0) {
                        et_keyoard.setText((int) f + "");
                    } else {
                        et_keyoard.setText(f + "");
                    }
                    tv_Totalmerchandise.setText("" + (Float.valueOf(tv_Totalmerchandise.getText().toString()) - numberEntties.get(kselect).getNumber()));


                    numberEntties.remove(kselect);
//                    im_reductionof.setVisibility(View.GONE);
//                    im_add.setVisibility(View.GONE);
//                    numberEntties.get(kselect).setChecked(false);

                    adapterzhu.notifyDataSetChanged();
                    Intent intent = new Intent("com.yzx.fupingdelete");
                    intent.putExtra("delete", kselect);
                    intent.putExtra("commoditys", commodities.get(kselect));
                    sendBroadcast(intent);
//                    stringcontext = "移除" + commodities.get(k).getName();
                    commodities.remove(kselect);
                    adapterzhu.notifyDataSetChanged();
                    kselect = -1;
                } else {
                    Toast.makeText(new_Main.this, "不能再删除了", Toast.LENGTH_SHORT).show();
                }
                /**
                 * 注释的敏感操作记录
                 */
//                stringMap.put("seller_id", SharedUtil.getString("seller_id"));
//                stringMap.put("work_name", SharedUtil.getString("name"));
//                stringMap.put("seller_name", SharedUtil.getString("seller_name"));
//                stringMap.put("operate_type", "移除");
//                stringMap.put("content", stringcontext);
//                Gson gson = new Gson();
//                String ing = gson.toJson(stringMap);
//                getsensitivity(ing);
            }
        }
    }

    /**
     * 设置选中的是那个itme
     * @param i
     */
    @Override
    public void setOnClickListener(int i) {
        this.kselect=i;
    }

    //设置数量的
    @Override
    public void getEntty(List<ShuliangEntty> entty) {

    }

    /**
     * 改数量
     * @param i
     */
    @Override
    public void setnums(final int i) {
        if (!issucceed) {
            cancelDailog();
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
            view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_add_goods, null);
            Button btn_cell = (Button) view_add_nums_notes.findViewById(R.id.btn_cell);
            Button btn_add = (Button) view_add_nums_notes.findViewById(R.id.btn_add);
            Button btn_cell_dialog = (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
            Button btn_sure_dialog = (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
            final EditText et_nums = (EditText) view_add_nums_notes.findViewById(R.id.et_nums);
            et_nums.setText(numberEntties.get(i).getNumber()+"");
            et_nums.selectAll();

            DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
            et_nums.setKeyListener(numericOnlyListener);

            btn_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nums_src = et_nums.getText().toString().trim();
                    Double nums = Double.parseDouble(nums_src);
                    if (nums > 1) {
                        nums--;
                    } else {
                        nums = 1.0;
                    }
                    et_nums.setText(nums + "");
                    et_nums.setSelection(et_nums.getText().toString().trim().length());
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nums_src = et_nums.getText().toString().trim();
                    Double nums = Double.parseDouble(nums_src);
                    nums++;
                    et_nums.setText(nums + "");
                    et_nums.setSelection(et_nums.getText().toString().trim().length());
                }
            });

            btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog_add_nums_notes.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                        imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });

            btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!et_nums.getText().toString().equals("") && Float.parseFloat(et_nums.getText().toString()) > 0) {
                        numberEntties.get(i).setNumber(Float.parseFloat(et_nums.getText().toString()));
                        adapterzhu.getadats(commodities);
                        adapterzhu.getnumber(numberEntties);
                        lv.setAdapter(adapterzhu);
                        double tolta = 0;
                        float Totalmerchandise = 0;
                        for (int p = 0; p < commodities.size(); p++) {
//                        Double.parseDouble(commodities.get(p).getPrice()) * entty.get(p).getNumber()

                            if (iswholesale) {
                                tolta = TlossUtils.add(tolta, TlossUtils.mul(Double.parseDouble(commodities.get(p).getPrice()), Double.parseDouble(numberEntties.get(p).getNumber() + "")));
                            } else {
                                tolta = TlossUtils.add(tolta, TlossUtils.mul(Double.parseDouble(commodities.get(p).getMember_price()), Double.parseDouble(numberEntties.get(p).getNumber() + "")));
                            }

                            Totalmerchandise = Totalmerchandise + numberEntties.get(p).getNumber();
                        }
                        tv_netreceipts.setText("" + tolta);
                        Showtotal("" + tolta);
                        tv_Total.setText("" + tolta);
                        if (isNumeric(et_nums.getText().toString())) {
                            tv_Totalmerchandise.setText("" + Totalmerchandise);
                        }
//
                        Intent intent = new Intent();
                        intent.setAction("com.yzx.changing");
                        intent.putExtra("type","0");
                        intent.putExtra("item", i);
                        intent.putExtra("num", Float.parseFloat(et_nums.getText().toString()));
                        sendBroadcast(intent);

                        mAlertDialog_add_nums_notes.dismiss();
                        if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }else {
                        Toast.makeText(new_Main.this,"请输入数量",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
            mAlertDialog_add_nums_notes.setCancelable(false);
            mAlertDialog_add_nums_notes.show();
//            final Dialog dialog = new Dialog(this);
//            dialog.setTitle("修改数量");
//            dialog.show();
//            Window window = dialog.getWindow();
//            window.setContentView(R.layout.dialog_supplement);
//            Button but_add = (Button) window.findViewById(R.id.but_add);
//            TextView tv_t1 = (TextView) window.findViewById(R.id.tv_t1);
//            tv_t1.setText("修改数量");
//            final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
//            ed_add.setHint("请输入数量");
//            final DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
//            ed_add.setKeyListener(numericOnlyListener);
//
//            ed_add.setText(numberEntties.get(i).getNumber() + "");
//            ed_add.selectAll();
//            but_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!ed_add.getText().toString().equals("") && Float.parseFloat(ed_add.getText().toString()) > 0) {
//                        numberEntties.get(i).setNumber(Float.parseFloat(ed_add.getText().toString()));
//                        adapterzhu.getadats(commodities);
//                        adapterzhu.getnumber(numberEntties);
//                        lv.setAdapter(adapterzhu);
//                        double tolta = 0;
//                        float Totalmerchandise = 0;
//                        for (int p = 0; p < commodities.size(); p++) {
////                        Double.parseDouble(commodities.get(p).getPrice()) * entty.get(p).getNumber()
//
//                            if (iswholesale){
//                                tolta = TlossUtils.add(tolta,TlossUtils.mul(Double.parseDouble(commodities.get(p).getPrice()),Double.parseDouble(numberEntties.get(p).getNumber()+"")) );
//                            }else {
//                                tolta = TlossUtils.add(tolta,TlossUtils.mul(Double.parseDouble(commodities.get(p).getMember_price()),Double.parseDouble(numberEntties.get(p).getNumber()+"")) );
//                            }
//
//                            Totalmerchandise = Totalmerchandise + numberEntties.get(p).getNumber();
//                        }
//                        tv_netreceipts.setText("" + tolta);
//                        Showtotal("" + tolta);
//                        tv_Total.setText("" + tolta);
//                        if (isNumeric(ed_add.getText().toString())){
//                            tv_Totalmerchandise.setText("" + Totalmerchandise);
//                        }
////
//                        Intent intent=new Intent();
//                        intent.setAction("com.yzx.changing");
//                        intent.putExtra("item", i);
//                        intent.putExtra("num",Float.parseFloat(ed_add.getText().toString()));
//                        sendBroadcast(intent);
//
//                        dialog.dismiss();
//                    }
//                }
//            });
        }
    }

    /**
     * 设置价格
     * @param i
     */
    @Override
    public void setprice(final int i) {
        cancelDailog();
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.dialog_supplement, null);
        TextView tv_t1= (TextView) view_add_nums_notes.findViewById(R.id.tv_t1);
        final EditText ed_add= (EditText) view_add_nums_notes.findViewById(R.id.ed_add);
        Button but_add= (Button) view_add_nums_notes.findViewById(R.id.but_add);
        tv_t1.setText("修改价格");
        ed_add.setHint("请输入修改价格");
        DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
        ed_add.setKeyListener(numericOnlyListener);
        if(iswholesale){
            ed_add.setText(commodities.get(i).getPrice() + "");
        }else {
            ed_add.setText(commodities.get(i).getMember_price() + "");
        }
        ed_add.selectAll();
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_add.getText().toString().equals("")){
                    Toast.makeText(new_Main.this,"请输入修改的价格",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(iswholesale){
                        commodities.get(i).setPrice(ed_add.getText().toString());
                    }else {
                        commodities.get(i).setMember_price(ed_add.getText().toString());
                    }
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
                    nums=nums+numberEntties.get(k).getNumber();
                    totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()),Double.parseDouble(numberEntties.get(k).getNumber()+"")));
                }
                tv_Totalmerchandise.setText(nums+"");
                tv_Total.setText(totalMember+"");
                tv_netreceipts.setText(totalMember+"");
                Showtotal(totalMember+"");
                adapterzhu.notifyDataSetChanged();
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    //判断字符是小数还是整数
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 退货处理
     */
    public void Returngoods(){
        cancelDailog();
        AlertDialog.Builder dialog = new AlertDialog.Builder(new_Main.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(new_Main.this, R.layout.credit_dialog, null);
        TextView tv_credit= (TextView) view_add_nums_notes.findViewById(R.id.tv_credit);
        tv_credit.setText("退货说明");
        final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
        ed_mark_text.setHint("请输入退货详情");
        Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
        if (!NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
            imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upreturndatas(ed_mark_text.getText().toString());
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(new_Main.this)) {
                    imm = (InputMethodManager) new_Main.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();


    }

    /**
     * 上传退货的数据
     */
    public void upreturndatas(String strg){
        if (!strg.equals("")) {
            Double sums = Double.valueOf(0);
            Double profit = Double.valueOf(0);
            Double profits = Double.valueOf(0);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (int k = 0; k < commodities.size(); k++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", commodities.get(k).getName());
                map.put("nums", numberEntties.get(k).getNumber() + "");



                if (iswholesale){
                    map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getPrice()) + ""));
                    profit = TlossUtils.mul(TlossUtils.sub(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(commodities.get(k).getCost())), Double.parseDouble(numberEntties.get(k).getNumber() + ""));
                    profits = TlossUtils.add(profits, profit);
                    map.put("sum", TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(numberEntties.get(k).getNumber() + "")) + "");
                    sums = TlossUtils.add(sums, TlossUtils.mul(Double.parseDouble(commodities.get(k).getPrice()), Double.parseDouble(numberEntties.get(k).getNumber() + "")));
                }else {
                    map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodities.get(k).getMember_price()) + ""));
                    profit = TlossUtils.mul(TlossUtils.sub(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(commodities.get(k).getCost())), Double.parseDouble(numberEntties.get(k).getNumber() + ""));
                    profits = TlossUtils.add(profits, profit);
                    map.put("sum", TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(numberEntties.get(k).getNumber() + "")) + "");
                    sums = TlossUtils.add(sums, TlossUtils.mul(Double.parseDouble(commodities.get(k).getMember_price()), Double.parseDouble(numberEntties.get(k).getNumber() + "")));
                }
                map.put("goods_id", commodities.get(k).getGoods_id());
                list.add(map);
            }
            Gson gson = new Gson();
            String str = gson.toJson(list);
            Log.d("print", "退货的数据" + str);
            OkGo.post(SysUtils.getSellerServiceUrl("back_goods"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.DEFAULT)
                    .params("operator", SharedUtil.getString("operator_id"))
                    .params("memo", strg)
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
                                    sqlite_entity = new Sqlite_Entity(new_Main.this);
                                    sqlite_entity.insertStock(commodities,numberEntties);
                                    commodities.clear();
                                    numberEntties.clear();
                                    bnt_cancel();
                                    Toast.makeText(new_Main.this, "退货成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(new_Main.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else {
            Toast.makeText(new_Main.this, "请输入备注", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 赊账的接口
     * @param st 赊账的备注
     */
    public void creditupadats(String st) {
        float total_amount = 0;
        if (commodities.size() > 0) {
            mapList.clear();
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("nums", numberEntties.get(i).getNumber() + "");
                map1.put("cost", commodities.get(i).getCost());
                map1.put("price", commodities.get(i).getPrice());
                map1.put("member_price", commodities.get(i).getMember_price());
                map1.put("orders_status", 2 + "");
                map1.put("pay_status", 0 + "");
                map1.put("mark_text", st);
                if (iswholesale){
                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber());
                }else {
                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber());
                }
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
                                    JSONObject jo4 = ja1.getJSONObject(0);
                                    String num = jo4.getString("num");
                                    /**
                                     * 注释的
                                     */
//                                    if (Integer.valueOf(num) > 0) {
//                                        but_cc_credit.setVisibility(View.VISIBLE);
//                                        but_cc_credit.setText(num);
//                                    } else {
//                                        but_cc_credit.setVisibility(View.GONE);
//                                    }

                                    reduce=false;
                                    discount=false;

                                    bnt_cancel();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            Toast.makeText(new_Main.this,"没有商品不能挂单",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 挂单的方法
     */
    public void upregister(){
        if (SharedUtil.getString("operational").equals("0")) {
            if(commodities.size()>0){
                if(commodities.get(0).getName().equals("兑奖")){
                    Toast.makeText(new_Main.this, "兑奖不能挂单", Toast.LENGTH_SHORT).show();
                }else {
                    if (DateUtils.isFastClick()) {
                        Toast.makeText(new_Main.this, "您的操作太频繁了", Toast.LENGTH_SHORT).show();
                    } else {
                        Pendingupadats();
                    }
                }
            }
        }
    }

    /**
     * 弹出加载的框
     */
    public void showdialog(String text){
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(new_Main.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText(text);
        loadingdialog.show();
    }
    /**
     * 挂单数据上传
     */
    public void Pendingupadats() {
        float total_amount = 0;
        if (commodities.size() > 0 ) {
            mapList.clear();
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("nums", numberEntties.get(i).getNumber() + "");
                map1.put("cost", commodities.get(i).getCost());
                map1.put("price", commodities.get(i).getPrice());
                map1.put("member_price", commodities.get(i).getMember_price());
                if (iswholesale){
                    map1.put("is_member","no");
                    total_amount += (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber());
                }else {
                    map1.put("is_member","yes");
                    total_amount += (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber());
                }
                map1.put("orders_status", 1 + "");
                map1.put("pay_status", 0 + "");
                mapList.add(map1);
            }
            Gson gson = new Gson();
            String str = gson.toJson(mapList);
            showdialog("正在挂单...");
            OkGo.post(SysUtils.getSellerServiceUrl("shengcheng_order"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("map", str)
                    .params("total_amount", total_amount + "")
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            loadingdialog.dismiss();
                        }
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                JSONObject jo2 = jo.getJSONObject("response");
                                String status = jo2.getString("status");
                                if (status.equals("200")) {
                                    JSONObject jo3 = jo2.getJSONObject("data");
                                    JSONArray ja1 = jo3.getJSONArray("num");
                                    JSONObject jo4 = ja1.getJSONObject(0);
                                    String num = jo4.getString("num");
                                    if (but_cc_quick!=null){
                                        if (Integer.valueOf(num) > 0) {
                                            but_cc_quick.setVisibility(View.VISIBLE);
                                            but_cc_quick.setText(num);
                                        } else {
                                            but_cc_quick.setVisibility(View.GONE);
                                        }
                                    }
                                    reduce=false;
                                    discount=false;
                                    Toast.makeText(new_Main.this,"挂单成功",Toast.LENGTH_SHORT).show();
                                    bnt_cancel();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                loadingdialog.dismiss();
                                getordernum();
                            }
                        }
                    });
        }
    }

    /**
     * 条形码拦截的方法
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        scanGunKeyEventHelper.analysisKeyEvent(event);
        return true;
    }

    /**
     * 扫码枪扫码成功的方法
     * @param barcode 扫码枪获取的数据
     */
    @Override
    public void onScanSuccess(String barcode) {

        if (type == 0&&Cashbox_switch) {

            if (Rl_time.getVisibility() == View.VISIBLE) {

            } else {
                /**
                 * 判断
                 */
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
                    isgetseek=true;
                    getlocal(barcode);
                    isgetseek=false;
                } else {
                    getlocal(barcode);
                }
            }

        }else if (type == 1) {
                uppay(barcode);
            }


    }

    /**
     * 扫码枪的支付
     * @param auth_code
     */
    public void uppay(final String auth_code) {
        OkGo.getInstance().cancelTag("payment");
        if (commodities.size() > 0) {
            for (int i = 0; i < commodities.size(); i++) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("goods_id", commodities.get(i).getGoods_id());
                map1.put("name", commodities.get(i).getName());
                map1.put("number", numberEntties.get(i).getNumber() + "");
                if (iswholesale){
                    map1.put("price", commodities.get(i).getPrice());
                    map1.put("amount", (Float.parseFloat(commodities.get(i).getPrice()) * numberEntties.get(i).getNumber()) + "");
                }else {
                    map1.put("price", commodities.get(i).getMember_price());
                    map1.put("amount", (Float.parseFloat(commodities.get(i).getMember_price()) * numberEntties.get(i).getNumber()) + "");
                }
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
        final int total_fee = (int) (Float.parseFloat(tv_netreceipts.getText().toString()) * 100);
        if (discount){
            discount_type="discount";
            num=_Memberdiscount+"";
        }
        if (reduce){
            discount_type="reduce";
            num=_reduce;
        }

        OkGo.post(SysUtils.getSellerServiceUrl("common_pays"))
                .tag("payment")
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .params("is_score_pay",is_score_pay)
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
                                Log.e("print","测试支付输入密码不减库存"+s);
                                handlernew.removeCallbacks(runnablenew);
                                stopService(new Intent(new_Main.this,NetWorkService.class));
                                if (commodities.size()>0){
                                    if(commodities.get(0).getName().equals("会员充值")){
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("member_id", pbmember_id);
//                                        map.put("surplus", Integer.parseInt(specification_list.get(specification_unms).getGive()) +
//                                                Integer.parseInt(specification_list.get(specification_unms).getVal()) + "");

                                        map.put("surplus", TlossUtils.mul((Integer.parseInt(specification_list.get(specification_unms).getGive()) +
                                                Integer.parseInt(specification_list.get(specification_unms).getVal())),Double.parseDouble(numberEntties.get(0).getNumber()+"")) + "");

                                        Gson gson = new Gson();
                                        String s1 = gson.toJson(map);
                                        UPmoney(s1);
                                    }

                                    if(commodities.get(0).getName().equals("换钱")){
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

                                Text2Speech.isSpeeching();
                                Text2Speech.speech(new_Main.this,"支付成功"+tv_netreceipts.getText().toString()+"元",4,false);

                                if (SharedUtil.getBoolean("self_print")) {
                                    PrintUtil printUtil1 = new PrintUtil(new_Main.this);
                                    printUtil1.openButtonClicked();
//                                        String syy = BluetoothPrintFormatUtil.getPrinterMsg(SharedUtil.getString("name"), tel, mobile_pay.getOrder_id(), mobile_pay.getPayed_time(), commodities, entty,
//                                                true, Double.parseDouble(mobile_pay.getMoney()), mobile_pay.getMoney(), mobile_pay.getMoney(), "0");

                                    String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgnew(SharedUtil.getString("name"), tel, mobile_pay.getOrder_id(), mobile_pay.getPayed_time(), commodities, numberEntties,
                                            1, Double.parseDouble(mobile_pay.getMoney()),"", mobile_pay.getMoney(), mobile_pay.getMoney(),tv_Surplus.getText().toString(), "0","","",reduce,_reduce,discount,_discount,_Memberdiscount,_Total);

                                    if (PrintWired.usbPrint(new_Main.this,syy)){
                                    }else {
                                        printUtil1.printstring(syy);

                                    }
                                }
                                type = 0;
                                issucceed=false;
                                operational = 0;
                                SharedUtil.putString("operational", operational + "");


                                SharedUtil.putString("order_id","");
//                               扫码成功客显二维码消失
                                Intent mIntent1 = new Intent();
                                mIntent1.setAction("poiu");
                                mIntent1.putExtra("mobile_pay", mobile_pay);
                                sendBroadcast(mIntent1);
                                tv_payment.setText(Float.parseFloat(tv_netreceipts.getText().toString()) + "");
                                tv_modes.setText("移动支付");

                                sqlite_entity=new Sqlite_Entity(new_Main.this);
                                sqlite_entity.insertStock(commodities,numberEntties);


                                mapList.clear();

                                bnt_cancel();

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

    /**
     * 充值金额
     * @param map
     */
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
                            Toast.makeText(new_Main.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 搜索商品的接口
     * @param str 搜索的值
     */
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
//                        getlocal(str);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("print","搜索的结果"+s);
                        isgetseek=false;
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
//                                list_fuzzy.clear();
                                if (ja1.length() == 0) {
                                    final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(new_Main.this).create();
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
                                            startActivity(new Intent(new_Main.this,ItemAdd_Activity.class));
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    for (int j = 0; j < ja1.length(); j++) {
                                        Commodity commodity = new Commodity();
                                        JSONObject jo2 = ja1.getJSONObject(j);
                                        commodity.setIs_special_offer(jo2.getString("is_special"));
                                        commodity.setGoods_id(jo2.getString("goods_id"));
                                        commodity.setName(jo2.getString("name"));
                                        commodity.setPy(jo2.getString("py"));

                                        commodity.setPrice(jo2.getString("price"));
                                        commodity.setMember_price(jo2.getString("member_price"));
                                        commodity.setCost(jo2.getString("cost"));
                                        commodity.setBncode(jo2.getString("bncode"));
                                        commodity.setStore(jo2.getString("store"));
                                        commodity.setCook_position(jo2.getString("cook_position"));
                                        if (!commodity.getCook_position().equals("0")) {
                                            getweight(commodity);
                                        }else {
                                            AddCommodity(commodity,1,commodity.getPrice());
                                        }
                                    }
                                }
                            }else {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 添加到商品列表的方法
     * @param commodity 需要添加的商品
     * @param weigt 数量
     * @param total 该商品重量的小计
     */
    public void AddCommodity(Commodity commodity,float weigt,String total){
        Log.d("print","打印传过来的数据");
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
                numberEntty = new New_NumberEntty();
                numberEntty.setNumber(weigt);
                numberEntty.setChecked(false);
                numberEntties.add(numberEntty);
                commodities.add(commodity);
            } else {
                float i = numberEntties.get(in - 1).getNumber();
                i = i + weigt;
                //快捷栏增加的广播
                Intent kuaijieadd = new Intent();
                kuaijieadd.setAction("com.yzx.kuaijieadd");
                kuaijieadd.putExtra("index", in);
                kuaijieadd.putExtra("number", i);
                sendBroadcast(kuaijieadd);

                numberEntties.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);

//                adapterzhu.getEntty(entty);
//                adapterzhu.getadats(commodities);
//                adapterzhu.notifyDataSetChanged();
//                lv.setAdapter(adapterzhu);
//                lv.setSelection(adapterzhu.getCount() - 1);
            }

        } else {

            if (commodities.size() > 0) {
                numberEntty = new New_NumberEntty();
                numberEntty.setNumber(weigt);
                numberEntty.setChecked(false);
                numberEntties.add(commodities.size() - 1, numberEntty);
                commodities.add(commodities.size() - 1, commodity);
            } else {
                numberEntty = new New_NumberEntty();
                numberEntty.setNumber(weigt);
                numberEntty.setChecked(false);
                numberEntties.add(0, numberEntty);
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
            adapterzhu.getnumber(numberEntties);
            adapterzhu.getadats(commodities);
            lv.setAdapter(adapterzhu);
            adapterzhu.notifyDataSetChanged();
            lv.setSelection(adapterzhu.getCount() - 1);
        } else {
            adapterzhu = new New_Adapter_Zhu(new_Main.this);
            adapterzhu.getnumber(numberEntties);
            adapterzhu.getadats(commodities);
            lv.setAdapter(adapterzhu);
            adapterzhu.notifyDataSetChanged();
            lv.setSelection(adapterzhu.getCount() - 1);
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

    //自定义键盘的监听
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_one:
                String myString1 = et_keyoard.getText().toString();
                myString1 += "1";
                if (getSumstr(myString1) >= MIXMONEY) {
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(new_Main.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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

                    } else {
                        et_keyoard.setText(10 + "");
                        et_inputscancode.setText(10 + "");

                        String string1 = et_inputscancode.getText().toString();
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
                    } else {
                        et_keyoard.setText(20 + "");
                        et_inputscancode.setText(20 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
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
                    } else {
                        et_keyoard.setText(50 + "");
                        et_inputscancode.setText(50 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
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
                    } else {
                        et_keyoard.setText(100 + "");
                        et_inputscancode.setText(100 + "");
                        String string1 = et_inputscancode.getText().toString();
                        if(TextUtils.isEmpty(string1)){
                            string1="0.0";
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

                        }
                    }
                }
                break;

        }

    }

    /**
     * 总金额
     * @param str
     * @return
     */
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

    //按钮的点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
    /**
     * edittext变化的方法
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void afterTextChanged(Editable editable) {
        setchange();
    }
    /**
     * 设置找零的钱
     */
    public void setchange(){
        Log.d("print", "setchange: "+et_inputscancode.getText().toString());
        if (!et_inputscancode.getText().toString().equals("")&&!tv_netreceipts.getText().toString().equals("")){
            if (TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()),Double.parseDouble(tv_netreceipts.getText().toString()))>=0){
                tv_Surplus.setText(TlossUtils.sub(Double.parseDouble(et_inputscancode.getText().toString()),Double.parseDouble(tv_netreceipts.getText().toString()))+"");
            }else {
                tv_Surplus.setText("0");
            }
        }else {
            tv_Surplus.setText("0");
        }
    }

    //获取商户的电话号码
    public String tel;
    /**
     * 获取商户电话
     */
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
}
