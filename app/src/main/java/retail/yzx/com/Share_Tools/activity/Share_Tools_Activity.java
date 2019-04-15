package retail.yzx.com.Share_Tools.activity;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.liangmayong.text2speech.Text2Speech;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
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
import Utils.PrintUtil;
import Utils.ScanGunKeyEventHelper;
import Utils.SetEditTextInput;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.TlossUtils;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.Share_Tools.entry.Share_GoodsInfo;
import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.Share_Tools.fragment.Share_Goods_Fragment;
import retail.yzx.com.Share_Tools.fragment.Share_Orders_Fragment;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Adapter.Res_Nomal_PalyOrderAdapter;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.QRCodeUtil;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

public class Share_Tools_Activity extends AppCompatActivity implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener {
    public final static Double MIXMONEY = 99999.00;//付款最大金额


    private View layout_share_tools_order_view;
    private LinearLayout layout_back;
    private TextView tv_goodsnums,tv_totalmoney;
    private SwipeMenuListView list_ordergoods;
    private FlyBanner im_picture;//数字键盘上轮播图
    private EditText et_keyoard,tv_Surplus,et_inputscancode;
    private Button btn_lock_screen,btn_table,btn_dish,btn_order, btn_ordersnums, btn_moneybox, btn_printer,btn_search;
    private FButton tv_cancel,but_Cashbox,but_mobilepayment;
    private LinearLayout layout_more, layout_logout, ll_jshuang;
    private RelativeLayout layout_peoplenums, layout_tablenums, keyboard_et_layout, keyboard_tv_layout;
    private RelativeLayout Rl_xianjin, Rl_time, Rl_yidong, layout_process_bar;
    private TextView tv_xianjin_netreceipt, tv_amount, tv_change, tv_yidong;
    private TextView tv_payment, tv_danhao, tv_day, tv_dopackage,tv_gone;
    private ImageView im_code;
    private Button btn_do_package, btn_do_order, but_cash_sure, but_internet, btn_mobile_cell, but_time,btn_refresh;
    private TextView tv_netreceipts, tv_sort, tv_logn_name, tv_ordernums, tv_ordertime, tv_peoplenums, tv_tablenums, tv_goods_nums, tv_total_money;
    private Timer threeTime_timer;//3秒关闭
    private Timer processTime_timer;//支付中进度条
    private int n = 3;
    public int frequency = 0;
    private Map<String, String> stringMap = new HashMap<>();
    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private GpService mGpService;


    public List<String> imageurls;//轮播图URL集合
    public boolean isSelected = true;//商品是否可以被选择
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;

    private Fragment mCurrentFrgment;//显示当前Fragment
    private int currentIndex = 0;//控制当前需要显示第几个Fragment
    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;
    private ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;
    private ArrayList<Fragment> fragmentArrayList;
    private Res_Nomal_PalyOrderAdapter mRes_Nomal_PalyOrderAdapter;
    private ArrayList<Res_GoodsOrders> mShare_Tools_GoodsOrderslist;
    private String tel;
    private Share_Goods_Fragment mShare_Goods_Fragment;



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
        setContentView(R.layout.activity_share__tools_);
        StringUtils.HideBottomBar(Share_Tools_Activity.this);
        StringUtils.setupUI(this, findViewById(R.id.activity_share__tools_));//点击空白处隐藏软键盘
        initFragment();
        initView();
        initListener();
        initData();
    }
    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        mShare_Goods_Fragment=new Share_Goods_Fragment();
        fragmentArrayList.add(mShare_Goods_Fragment);
        fragmentArrayList.add(new Share_Orders_Fragment());

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
        ft.commit();
    }
    private void initView() {
        layout_share_tools_order_view=findViewById(R.id.layout_share_tools_order_view);
        layout_back= (LinearLayout) findViewById(R.id.layout_back);//返回收银页面按钮
        tv_goodsnums= (TextView) findViewById(R.id.tv_goodsnums);//选择商品的总数量
        tv_totalmoney= (TextView) findViewById(R.id.tv_totalmoney);//选择商品的总金额
        list_ordergoods= (SwipeMenuListView) findViewById(R.id.list_ordergoods);//选择商品的列表
        im_picture= (FlyBanner) findViewById(R.id.im_picture);//轮播图
        et_keyoard = (EditText) findViewById(R.id.et_keyoard);//运算显示栏
        et_inputscancode = (EditText) findViewById(R.id.et_inputscancode);//实收
        tv_Surplus = (EditText) findViewById(R.id.tv_Surplus);//找零
        tv_netreceipts = (TextView) findViewById(R.id.tv_netreceipts);//应收
        keyboard_tv_layout = (RelativeLayout) findViewById(R.id.keyboard_tv_layout);//应收栏
        btn_lock_screen = (Button) findViewById(R.id.btn_lock_screen);//锁屏按钮
        btn_table = (Button) findViewById(R.id.btn_table);//c餐桌按钮
        btn_order = (Button) findViewById(R.id.btn_order);//订单按钮
        btn_dish = (Button) findViewById(R.id.btn_dish);//商品按钮
        tv_cancel= (FButton) findViewById(R.id.tv_cancel);//键盘取消按钮
        but_Cashbox= (FButton) findViewById(R.id.but_Cashbox);//键盘现金支付按钮
        but_mobilepayment= (FButton) findViewById(R.id.but_mobilepayment);//键盘移动按钮
        btn_dish.setText("商品");
        btn_lock_screen.setVisibility(View.GONE);
        btn_table.setVisibility(View.INVISIBLE);

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
        keyboard_tv_layout = (RelativeLayout) findViewById(R.id.keyboard_tv_layout);//应收金额
        keyboard_et_layout = (RelativeLayout) findViewById(R.id.keyboard_et_layout);//实收金额
        btn_moneybox = (Button) findViewById(R.id.btn_moneybox);//钱箱按钮
        btn_printer = (Button) findViewById(R.id.btn_printer);//打印按钮
        btn_refresh = (Button) findViewById(R.id.btn_refresh);//同步按钮
        btn_ordersnums = (Button) findViewById(R.id.btn_ordersnums);//挂单数量
        btn_search = (Button) findViewById(R.id.btn_search);//搜索按钮



        //设置输入框 手动点击输入
        et_inputscancode.setInputType(InputType.TYPE_NULL);
        tv_netreceipts.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(tv_Surplus);
        tv_Surplus.setInputType(InputType.TYPE_NULL);
        et_keyoard.setInputType(InputType.TYPE_NULL);
        SetEditTextInput.setPricePoint(et_keyoard);
        setSwipeMenuListViewDelete(list_ordergoods);//左滑动删除
        keyboard_tv_layout.performClick();//自动触发应收栏被点击

        //条形码工具类
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);

        //蓝牙打印
        mService = new BluetoothService(this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
        }

        //判断蓝牙是否连接小票打印机
        String address_Mac=SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
        if(TextUtils.isEmpty(address_Mac)){
            StringUtils.showToast(Share_Tools_Activity.this,"如需打印小票，请先点击打印按钮，连接小票打印机",20);
        }else {
            con_dev = mService.getDevByMac(address_Mac);
            mService.connect(con_dev);
        }
       tel=SharedUtil.getString("phone");

        mShare_GoodsInfoList=new ArrayList<>();
        layout_back.setOnClickListener(this);
        but_mobilepayment.setOnClickListener(this);
        btn_mobile_cell.setOnClickListener(this);
        keyboard_et_layout.setOnClickListener(this);
        keyboard_tv_layout.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        but_Cashbox.setOnClickListener(this);
        but_mobilepayment.setOnClickListener(this);
        but_cash_sure.setOnClickListener(this);
        btn_mobile_cell.setOnClickListener(this);
        but_time.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_dish.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_printer.setOnClickListener(this);
        btn_moneybox.setOnClickListener(this);

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
                        mSelf_Service_GoodsInfo.remove(position);
                        mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                        setTotalPrice(mSelf_Service_GoodsInfo);
                        Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));

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
//                        AddGoodsNumsNoteDialog(position, mSelf_Service_GoodsInfo);
                    }
                }
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
                StringUtils.showToast(Share_Tools_Activity.this, goods_name + "商品规格异常,请重新添加规格或者拨打服务电话咨询！", 20);
            }
            total_nums += nums;
            total_price += price * nums;
        }
        tv_goodsnums.setText(total_nums + "");
        tv_totalmoney.setText("￥ " + Utils.StringUtils.stringpointtwo(total_price + ""));
        tv_netreceipts.setText(Utils.StringUtils.stringpointtwo(total_price + ""));
    }
    //初始化监听
    private void initListener() {
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
        mSelf_Service_GoodsInfo=new ArrayList<>();
        getImagesList();
        changeTab(0);
    }
//功能按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_dish://商品按钮
                changeTab(0);
                setBtnBackgroud(btn_dish, btn_order, btn_table);
                break;
            case R.id.but_mobilepayment://移动支付
                MobilePay();
                break;
            case R.id.btn_mobile_cell://移动支付取消按钮
                ll_jshuang.setVisibility(View.VISIBLE);
                Rl_yidong.setVisibility(View.GONE);
                if (mRes_Nomal_PalyOrderAdapter != null) {
                    mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                    mRes_Nomal_PalyOrderAdapter.setClick(true);
                }
                GetPatStatus_timer.cancel();
                Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 7));
                break;
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
            case R.id.but_cash_sure://现金支付确定按钮
                if(but_cash_sure.getText().toString().trim().equals("确定")){
                        CommitCashPay();
                }
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
            case R.id.btn_order://订单按钮
//                getOrderNums();
                changeTab(1);
                setBtnBackgroud(btn_order, btn_dish, btn_table);
                get_ShareOrder_totalnum();
                break;
            case R.id.btn_refresh://同步按钮
                mShare_Goods_Fragment.getShareGoodsSortInfo();
                changeTab(0);
                setBtnBackgroud(btn_dish, btn_order, btn_table);
                break;
            case R.id. btn_search://搜索按钮

                break;
            case R.id.btn_printer://打印按钮

                break;
            case R.id.btn_moneybox://钱箱按钮

                break;
        }
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
                map.put("goods_id", goods_id);
                map.put("name", name);
                map.put("number", nums);
                map.put("cost", cost);
                map.put("price", price);
                mapList.add(map);
            }
        }
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cashPay"))
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
                                but_cash_sure.setText("确定");
                                but_cash_sure.setOnClickListener(Share_Tools_Activity.this);
                                ll_jshuang.setVisibility(View.VISIBLE);
                                Rl_xianjin.setVisibility(View.GONE);

                                JSONObject data = jo1.getJSONObject("data");
                                String order_id = data.getString("order_id");
                                String payed_time = data.getString("payed_time");
                                StringUtils.showToast(Share_Tools_Activity.this, "收款成功！", 20);
                                get_ShareOrder_totalnum();

                                String pay_money=tv_xianjin_netreceipt.getText().toString().trim();
                                String really_money=tv_amount.getText().toString().trim();
                                String change_money=tv_change.getText().toString().trim();

                                PrintUtil.receiptPrint(Share_Tools_Activity.this,mService,tel,order_id, 0, payed_time, 2, pay_money,really_money,change_money,1,mSelf_Service_GoodsInfo,mSelf_Service_GoodsInfo,"","","","","");

                                setInitData();
                                Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));
//                                getOrderNums();
                                changeTab(0);
                                setBtnBackgroud(btn_dish, btn_table, btn_order);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
        et_inputscancode.setText("0.0");
        et_keyoard.setText("");
        if (mSelf_Service_GoodsInfo != null) {
            mSelf_Service_GoodsInfo.clear();
            if (mRes_Nomal_PalyOrderAdapter != null) {
                mRes_Nomal_PalyOrderAdapter.notifyDataSetChanged();
                mRes_Nomal_PalyOrderAdapter.setClick(true);
            }
            Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 3));
            setTotalPrice(mSelf_Service_GoodsInfo);
        }

    }
    //现金支付页面
    private void CashPay() {
        String tv_netreceipts_str = tv_netreceipts.getText().toString().trim();//应收
        String et_inputscancode_str = et_inputscancode.getText().toString().trim();//实收
        if (TextUtils.isEmpty(et_inputscancode_str)) {
            StringUtils.showToast(Share_Tools_Activity.this, "实收金额不能为空！", 20);
            return;
        }
        if (TextUtils.isEmpty(tv_netreceipts_str)) {
            StringUtils.showToast(Share_Tools_Activity.this, "应收金额不能为空！", 20);
            return;
        }
        double tv_netreceipts_double = Double.parseDouble(tv_netreceipts_str);
        double et_inputscancode_double = Double.parseDouble(et_inputscancode_str);
        if (tv_netreceipts_double == 0) {
            StringUtils.showToast(Share_Tools_Activity.this, "应收金额必须大于0元！", 20);
            return;
        }
        if (tv_netreceipts_double > et_inputscancode_double) {
            StringUtils.showToast(Share_Tools_Activity.this, "实收金额小于应收金额，请重新检查之后再确认支付！", 20);
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
        Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 4).putExtra("RealedPrice", et_inputscancode_str).putExtra("ChangeCash", tv_change.getText().toString().trim()).putExtra("paymoney", tv_netreceipts_str));
    }
    //打开钱箱
    private String stringcontext = "";
    private void OpenCashBox(){
        if (SysUtils.getSystemModel().equals("rk3288")){
            SysUtils.OpennewCashbox(Share_Tools_Activity.this);
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
    //移动支付页面
    private void MobilePay() {
        keyboard_tv_layout.performClick();
        String tv_netreceipts_str = tv_netreceipts.getText().toString().trim();//应收
        if (TextUtils.isEmpty(tv_netreceipts_str)) {
            StringUtils.showToast(Share_Tools_Activity.this, "应收金额不能为空！", 20);
            return;
        }
        double tv_netreceipts_double = Double.parseDouble(tv_netreceipts_str);
        if (tv_netreceipts_double == 0) {
            StringUtils.showToast(Share_Tools_Activity.this, "应收金额不能为0元！", 20);
            return;
        }
        if (SysUtils.isWifiConnected(Share_Tools_Activity.this)) {
            ll_jshuang.setVisibility(View.GONE);
            Rl_yidong.setVisibility(View.VISIBLE);
            tv_yidong.setText(tv_netreceipts.getText().toString());
            if (mRes_Nomal_PalyOrderAdapter != null) {
                mRes_Nomal_PalyOrderAdapter.setClick(false);
            }
            do_ShareOrder();
        }
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
            Toast.makeText(Share_Tools_Activity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSelected) {
            et_keyoard.setText(myString);
            et_inputscancode.setText(getSumstr(myString) + "");
        } else {
            if (mSelf_Service_GoodsInfo.size() < 1) {//有选择的商品时，应收金额不能被编辑，数字键盘只对应实收金额
                et_keyoard.setText(myString);
                tv_netreceipts.setText(getSumstr(myString) + "");
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
                            return;
                        }
                        et_keyoard.setText(cancell_str.substring(0, cancell_str.length() - 1));
                        if ((getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "") == "") {
                            tv_netreceipts.setText("");
                        } else {
                            tv_netreceipts.setText(getSumstr(cancell_str.substring(0, cancell_str.length() - 1)) + "");
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
                                Toast.makeText(Share_Tools_Activity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
                                } else {
                                    Toast.makeText(Share_Tools_Activity.this, "付款金额不能超过最大金额！", Toast.LENGTH_SHORT).show();
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
    /**
     * 生成移动支付订单
     */
    private String orders_id;
    private Timer GetPatStatus_timer;//获取订单状态的定时器
    private void do_ShareOrder() {
        if (mSelf_Service_GoodsInfo != null) {
            if (mSelf_Service_GoodsInfo.size() < 1) {
                StringUtils.showToast(Share_Tools_Activity.this, "选择商品不能为空！", 20);
                return;
            }
            ArrayList<Map<String, String>> mapArrayList = new ArrayList<>();
            for (int i = 0; i < mSelf_Service_GoodsInfo.size(); i++) {
                String goods_id = mSelf_Service_GoodsInfo.get(i).getGoods_id();
                String price = mSelf_Service_GoodsInfo.get(i).getPrice();
                String nums = mSelf_Service_GoodsInfo.get(i).getNumber();
                String name = mSelf_Service_GoodsInfo.get(i).getName();
                String goods_notes = mSelf_Service_GoodsInfo.get(i).getNotes();
                Map<String, String> map = new HashMap<>();
                map.put("goods_id", goods_id);
                map.put("price", price);
                map.put("nums", nums);
                map.put("name", name);
                map.put("goods_notes", goods_notes);
                mapArrayList.add(map);
            }
            Gson gson = new Gson();
            String str = gson.toJson(mapArrayList);
            final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
            final String total_nums_str = tv_goodsnums.getText().toString().trim().trim();

            OkGo.post(SysUtils.getShareGoodsServiceUrl("catering_order_add"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("total_amount", (new Double(Double.parseDouble(total_price_str)*100)).intValue()+"")
                    .params("total_nums", total_nums_str)
                    .params("map",str)
                    .params("seller_token", SharedUtil.getString("seller_token"))
                    .execute(new StringCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            Log.e("barcode", "Params：" + request.getUrl());
                            Log.e("barcode", "Params：" + request.getParams());
                        }
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                Log.e("barcode", "返回数据：" + jsonObject);
                                JSONObject jo1 = jsonObject.getJSONObject("response");
                                String status = jo1.getString("status");
                                if (status.equals("200")) {
                                    JSONObject data=jo1.getJSONObject("data");
                                    orders_id = data.getString("order_id");
                                    String url = data.getString("url");
                                    createPatCode(url);
                                    tv_yidong.setText(total_price_str);
                                    mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Share_Tools_Activity.this, mSelf_Service_GoodsInfo, false);
                                    list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                                    Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 6).putExtra("url", url));
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
                        }
                    });
        } }
    //获取支付状态
    public void GetPayStatus(String order_id) {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("order_status"))
                .tag(this)
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
                                GetPatStatus_timer.cancel();
                                processTime_timer.cancel();

                                JSONObject data = jo1.getJSONObject("data");
                                String order_id = data.getString("order_id");
                                String time = data.getString("time");
                                long time_long = Long.parseLong(time);
                                String paymoney = tv_yidong.getText().toString().trim();
                                Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 5));

                                Text2Speech.speech(Share_Tools_Activity.this, "收款成功" + paymoney + "元", false);
                                get_ShareOrder_totalnum();
                                //打印小票
                                        PrintUtil.receiptPrint(Share_Tools_Activity.this,mService,tel,order_id, time_long, "", 1, paymoney,paymoney,"0",1,mSelf_Service_GoodsInfo,mSelf_Service_GoodsInfo,"","","","","");

                                tv_danhao.setText(order_id);
                                tv_day.setText(DateTimeUtils.getDateTimeFromMillisecond(time_long * 1000));
                                String pay_money = tv_netreceipts.getText().toString().trim().replace("￥", "");
                                tv_payment.setText(pay_money);
                                Rl_yidong.setVisibility(View.GONE);
                                Rl_time.setVisibility(View.VISIBLE);//收款成功页面
//                                getOrderNums();
                                changeTab(0);
//                                setBtnBackgroud(btn_dish, btn_table, btn_order);
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
                            Log.e("barcode", "e+" + e.toString());
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

        final String filePath = QRCodeUtil.getFileRoot(Share_Tools_Activity.this) + File.separator
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
                            Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(filePath));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                im_code.setBackground(drawable);
                            } else {
                                im_code.setImageBitmap(BitmapFactory.decodeFile(filePath));
                            }
                        }
                    });
                }
            }
        }).start();
    }

    //获取共享订单 总数
    public void get_ShareOrder_totalnum() {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("get_order_totalnum"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("共享订单总数URL：", "= " + request.getUrl());
                        Log.e("共享订单总数Params：", "= " + request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            Log.e("共享订单总数数据：", "= " + jsonObject);
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data=jo1.getJSONObject("data");
                                int num=JsonUtil.getJsonInt(data,"num");
                                if(num>0){
                                    btn_ordersnums.setVisibility(View.VISIBLE);
                                }else {
                                    btn_ordersnums.setVisibility(View.GONE);
                                }
                                btn_ordersnums.setText(""+num);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("barcode", "e+" + e.toString());
                        }
                    }
                });
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
            StringUtils.showToast(Share_Tools_Activity.this,"请先点击移动支付按钮！",20);
        }else {
            MobilePayment(barcode);

        }


    }
    //扫码枪移动支付
    public void MobilePayment(final String auth_code) {
        final String total_price_str = tv_netreceipts.getText().toString().trim().replace("￥", "").trim();
        double total_price = Double.parseDouble(total_price_str);
        OkGo.post(SysUtils.getShareGoodsServiceUrl("common_pays"))
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



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            if(type==1){
                boolean ishas=false;
                String goods_id=intent.getStringExtra("goods_id");
                String goods_name=intent.getStringExtra("goods_name");
                String goods_price=intent.getStringExtra("goods_price");


                for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
                    if(goods_id.equals(mSelf_Service_GoodsInfo.get(i).getGoods_id())){
                        int n=Integer.parseInt(mSelf_Service_GoodsInfo.get(i).getNumber());
                        mSelf_Service_GoodsInfo.get(i).setNumber((n+1)+"");
                        ishas=true;
                        break;
                    }
                }
                if(!ishas){
                    Self_Service_GoodsInfo self_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,goods_name,
                            "1","",goods_price,"","","","");
                    mSelf_Service_GoodsInfo.add(self_Service_GoodsInfo);
                }
                mRes_Nomal_PalyOrderAdapter = new Res_Nomal_PalyOrderAdapter(Share_Tools_Activity.this, mSelf_Service_GoodsInfo, true);
                list_ordergoods.setAdapter(mRes_Nomal_PalyOrderAdapter);
                list_ordergoods.setSelection(mSelf_Service_GoodsInfo.size());
                setTotalPrice(mSelf_Service_GoodsInfo);

                et_inputscancode.setText("0.0");
                et_keyoard.setText("");
                keyboard_et_layout.performClick();//选择商品时，自动定位到实收选中栏
               Share_Tools_Activity.this.sendBroadcast(new Intent("DifferentDislay.Action").putExtra("type", 2).putParcelableArrayListExtra("mSelf_Service_GoodsInfo", mSelf_Service_GoodsInfo));

//                do_ShareOrder();
            }else if(type==2){//小票打印
                if (mShare_Tools_GoodsOrderslist == null) {
                    mShare_Tools_GoodsOrderslist = new ArrayList<>();
                }
                mShare_Tools_GoodsOrderslist=intent.getParcelableArrayListExtra("mShare_Tools_GoodsOrderslist");
                int position=intent.getIntExtra("position",0);
                //打印小票
                PrintUtil.receiptPrint(Share_Tools_Activity.this,mService,tel,mShare_Tools_GoodsOrderslist.get(position).getOrder_id(),0,mShare_Tools_GoodsOrderslist.get(position).getOrder_time(),0,
                        mShare_Tools_GoodsOrderslist.get(position).getOrder_total_money(),"0","0",3,
                        mShare_Tools_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo(), mShare_Tools_GoodsOrderslist.get(position).getmSelf_Service_GoodsInfo(),mShare_Tools_GoodsOrderslist.get(position).getOrder_dopackage(),
                        mShare_Tools_GoodsOrderslist.get(position).getOrder_people_num(),mShare_Tools_GoodsOrderslist.get(position).getOrder_table_nums(),"",mShare_Tools_GoodsOrderslist.get(position).getOrder_notes());
            }else if(type==3){
                get_ShareOrder_totalnum();
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Share_Tools_Activity.this.registerReceiver(broadcastReceiver,new IntentFilter("Share_Tools_Activity_Action"));
        get_ShareOrder_totalnum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Share_Tools_Activity.this.unregisterReceiver(broadcastReceiver);
    }
}
