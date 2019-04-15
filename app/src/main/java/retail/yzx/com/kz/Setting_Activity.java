package retail.yzx.com.kz;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import Utils.DbBackups;
import Utils.File_Utils;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SystemUtil;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import shujudb.Sqlite_Entity;
import widget.MyFileManager;
import widget.Switch;

/**
 * Created by admin on 2017/4/28.
 */
public class Setting_Activity extends BaseActivity implements View.OnClickListener {

    public ImageView im_huanghui, im_map;
    public RelativeLayout rl_password,layout_self_service_set;
    public RelativeLayout rl_label;
    public EditText tv_map,ed_remarks;
    private EditText edit_selfservice_nums,edit_unms;
    private Button btn_selfservice_sure,btn_print;
    private LocationManager locationManager;

    private double latitude = 0;

    public TextView tv_name;

    public Switch sw5,sw_order_lebal;

    public Switch sw3,sw6,sw7,sw4,sw_weight,sw_total,sw_price,sw8;

    public RelativeLayout rl_print_nums;
    public RelativeLayout rl_wechat,rl_alipay;

    public static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    public static final int REQUEST_CONNECT_DEVICE = 1;

    private double longitude = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("print", "点击了");
            double[] data = (double[]) msg.obj;
            Log.d("print", "点击了");
            tv_map.setText("经度：" + data[0] + "\t纬度:" + data[1]);
        }

    };

    public RelativeLayout Rl_video;
    public RelativeLayout Rl_image;
    public RelativeLayout rl_local;
    public EditText ed_local_password;
    public Button but_backup,but_restroe;
    public Sqlite_Entity sqlite_entity;
    public Button btn_resolving_power;
    public TextView edit_resolving_power;
    public EditText ed_discount;
    public RelativeLayout Rl_calculation;

    @BindView(R.id.ed_mstrIp)
    EditText ed_mstrIp;
    @BindView(R.id.edit_zero)
    EditText edit_zero;
    @BindView(R.id.btn_zero)
    Button btn_zero;


    @Override
    protected int getContentId() {
        return R.layout.setting_layout;
    }

    @Override
    protected void init() {
        super.init();

        Rl_calculation= (RelativeLayout) findViewById(R.id.Rl_calculation);
        Rl_calculation.setOnClickListener(this);

        mService = new BluetoothService(this, mHandler);
        if( mService.isAvailable() == false ){
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
        }
        /**
         * 全场打折设置
         */
        ed_discount= (EditText) findViewById(R.id.ed_discount);

        if (SharedUtil.getString("ed_discount")!=null){
            ed_discount.setText(SharedUtil.getString("ed_discount"));
        }
        ed_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedUtil.putString("ed_discount",editable.toString());
            }
        });

        /**
         * 备份数库数据
         */
        but_backup= (Button) findViewById(R.id.but_backup);
        but_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite_entity=new Sqlite_Entity(Setting_Activity.this);
                getpopup(sqlite_entity.findMaxId("commodity")+"");
            }
        });

        but_restroe= (Button) findViewById(R.id.but_restroe);
        but_restroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DbBackups(getApplication()).execute("restroeDatabase");
            }
        });

        btn_resolving_power= (Button) findViewById(R.id.btn_resolving_power);
        edit_resolving_power= (TextView) findViewById(R.id.edit_resolving_power);

        if (SharedUtil.getString("power")!=null){
            edit_resolving_power.setText(SharedUtil.getString("power"));
        }

        btn_resolving_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("power",edit_resolving_power.getText().toString());
            }
        });

        if (SharedUtil.getInt("zero")!=-1){
            edit_zero.setText(SharedUtil.getInt("zero")+"");
        }

        im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
        layout_self_service_set = (RelativeLayout) findViewById(R.id.layout_self_service_set);
        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
        im_map = (ImageView) findViewById(R.id.im_map);
        tv_map = (EditText) findViewById(R.id.tv_map);

        rl_local=(RelativeLayout)findViewById(R.id.rl_local);
        ed_local_password=(EditText) findViewById(R.id.ed_local_password);
        if (SharedUtil.getString("type").equals("4")){
            rl_local.setVisibility(View.GONE);
        }else {
            rl_local.setVisibility(View.VISIBLE);
        }

        if (!SharedUtil.getString("local_password").equals("")){
            ed_local_password.setText(SharedUtil.getString("local_password"));
        }

        ed_local_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedUtil.putString("local_password",editable.toString());
            }
        });


        ed_remarks= (EditText) findViewById(R.id.ed_remarks);

        if (SharedUtil.getString("address")!=null){
            tv_map.setText(SharedUtil.getString("address"));
        }

        tv_map.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                SharedUtil.putString("address",editable.toString());
            }
        });


        if (SharedUtil.getString("print_remarks")!=null){
            ed_remarks.setText(SharedUtil.getString("print_remarks"));
        }
            ed_remarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    SharedUtil.putString("print_remarks",editable.toString());
                }
            });


        edit_selfservice_nums = (EditText) findViewById(R.id.edit_selfservice_nums);
        edit_unms = (EditText) findViewById(R.id.edit_unms);
        btn_selfservice_sure= (Button) findViewById(R.id.btn_selfservice_sure);
        btn_print= (Button) findViewById(R.id.btn_print);

        rl_print_nums= (RelativeLayout) findViewById(R.id.rl_print_nums);

        //设置支付的二维码
        rl_wechat= (RelativeLayout) findViewById(R.id.rl_wechat);
        rl_wechat.setOnClickListener(this);
        rl_alipay= (RelativeLayout) findViewById(R.id.rl_alipay);
        rl_alipay.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Rl_video= (RelativeLayout) findViewById(R.id.Rl_video);
        Rl_video.setOnClickListener(this);

        Rl_image= (RelativeLayout) findViewById(R.id.Rl_image);
        Rl_image.setOnClickListener(this);

        im_map.setOnClickListener(this);
        rl_label.setOnClickListener(this);
        rl_password.setOnClickListener(this);
        im_huanghui.setOnClickListener(this);
        layout_self_service_set.setOnClickListener(this);
        btn_selfservice_sure.setOnClickListener(this);
        btn_print.setOnClickListener(this);


        if (SharedUtil.getString("selfservice_nums") != null) {
            edit_selfservice_nums.setText(SharedUtil.getString("selfservice_nums"));
        }else {
            edit_selfservice_nums.setText("0");
        }

        if (SharedUtil.getString("print_unms") != null) {
            edit_unms.setText(SharedUtil.getString("print_unms")+"");
        }else {
            edit_unms.setText("0");
        }



        sw3= (Switch) findViewById(R.id.sw3);

        if (SharedUtil.getString("synchronization") != null) {
            sw3.setChecked(Boolean.parseBoolean(SharedUtil.getString("synchronization")));
        }
        sw3.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw3.setChecked(isChecked);
                SharedUtil.putString("synchronization", isChecked + "");
            }
        });


        //是否开启二维码支付
        sw_weight=(Switch)findViewById(R.id.sw_weight);
        if (SharedUtil.getString("sw_weight") != null) {
            sw_weight.setChecked(Boolean.parseBoolean(SharedUtil.getString("sw_weight")));
        }else {
            sw_weight.setChecked(true);
            SharedUtil.putString("sw_weight", true + "");
        }
        sw_weight.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw_weight.setChecked(isChecked);
                SharedUtil.putString("sw_weight", isChecked + "");
            }
        });


        sw_price= (Switch) findViewById(R.id.sw_price);
        if (SharedUtil.getString("print_price")!=null){
            sw_price.setChecked(Boolean.parseBoolean(SharedUtil.getString("print_price")));
        }else {
            sw_total.setChecked(false);
            SharedUtil.putString("print_price",false+"");
        }

        sw_price.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw_total.setChecked(isChecked);
                SharedUtil.putString("print_price", isChecked + "");
            }
        });

        sw_total= (Switch) findViewById(R.id.sw_total);
        if (SharedUtil.getString("sw_total") != null) {
            sw_total.setChecked(Boolean.parseBoolean(SharedUtil.getString("sw_total")));
        }else {
            sw_total.setChecked(false);
            SharedUtil.putString("sw_total", false + "");
        }
        sw_total.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw_total.setChecked(isChecked);
                SharedUtil.putString("sw_total", isChecked + "");
            }
        });


        //是否开启二维码支付
        sw4= (Switch) findViewById(R.id.sw4);
        sw4.setChecked(!SharedUtil.getBoolean("code"));

        sw4.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw4.setChecked(isChecked);
                SharedUtil.putBoolean("code",!isChecked);
                if (!isChecked){
                    rl_wechat.setVisibility(View.VISIBLE);
                    rl_alipay.setVisibility(View.VISIBLE);
                }else {
                    rl_wechat.setVisibility(View.VISIBLE);
                    rl_alipay.setVisibility(View.VISIBLE);
                }

            }
        });

        //是否开启餐桌
        sw7= (Switch) findViewById(R.id.sw7);
        sw7.setChecked(SharedUtil.getBoolean("open_table"));//默认开启餐桌
        sw7.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw5.setChecked(isChecked);
                SharedUtil.putBoolean("open_table",isChecked);
            }
        });

        sw6= (Switch) findViewById(R.id.sw6);

        if (SharedUtil.getString("printlabel") != null) {
            sw6.setChecked(Boolean.parseBoolean(SharedUtil.getString("synchronization")));
        }
        sw6.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw6.setChecked(isChecked);
                SharedUtil.putString("printlabel", isChecked + "");
            }
        });


        sw5 = (Switch) findViewById(R.id.sw5);
        sw5.setChecked(SharedUtil.getBoolean("self_print"));//默认自动打印
        rl_print_nums.setVisibility(View.VISIBLE);
        //设置是否自动打印
        sw5.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw5.setChecked(isChecked);
                SharedUtil.putBoolean("self_print",isChecked);
            }
        });

        sw8=(Switch)findViewById(R.id.sw8);
        sw8.setChecked(SharedUtil.getfalseBoolean("pay_print"));
        sw8.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                SharedUtil.putfalseBoolean("pay_print",isChecked);
            }
        });
        //订单标签打印
        sw_order_lebal= (Switch) findViewById(R.id.sw_order_lebal);
        sw_order_lebal.setChecked(SharedUtil.getBoolean("self_print_order_label"));//默认自动打印
        sw_order_lebal.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                sw_order_lebal.setChecked(isChecked);
                SharedUtil.putBoolean("self_print_order_label",isChecked);
            }
        });
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(SharedUtil.getString("seller_name"));

        if (!SharedUtil.getString("ip").equals("")&&SharedUtil.getString("ip")!=null){
            ed_mstrIp.setText(SharedUtil.getString("ip"));
        }
        ed_mstrIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.IsIp(editable.toString())){
                    SharedUtil.putString("ip",editable.toString());
                }
            }
        });

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.setting_layout);
//        SharedUtil.init(this);
//        mService = new BluetoothService(this, mHandler);
//        if( mService.isAvailable() == false ){
//            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        init1();
//    }

//    private void init1() {
//        //    隐藏底部按钮
//        StringUtils.HideBottomBar(Setting_Activity.this);
//        im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
//        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
//        layout_self_service_set = (RelativeLayout) findViewById(R.id.layout_self_service_set);
//        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
//        im_map = (ImageView) findViewById(R.id.im_map);
//        tv_map = (TextView) findViewById(R.id.tv_map);
//        edit_selfservice_nums = (EditText) findViewById(R.id.edit_selfservice_nums);
//        edit_unms = (EditText) findViewById(R.id.edit_unms);
//        btn_selfservice_sure= (Button) findViewById(R.id.btn_selfservice_sure);
//        btn_print= (Button) findViewById(R.id.btn_print);
//
//        rl_print_nums= (RelativeLayout) findViewById(R.id.rl_print_nums);
//
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        Rl_video= (RelativeLayout) findViewById(R.id.Rl_video);
//        Rl_video.setOnClickListener(this);
//        im_map.setOnClickListener(this);
//        rl_label.setOnClickListener(this);
//        rl_password.setOnClickListener(this);
//        im_huanghui.setOnClickListener(this);
//        layout_self_service_set.setOnClickListener(this);
//        btn_selfservice_sure.setOnClickListener(this);
//        btn_print.setOnClickListener(this);
//
//
//        if (SharedUtil.getString("selfservice_nums") != null) {
//            edit_selfservice_nums.setText(SharedUtil.getString("selfservice_nums"));
//        }else {
//            edit_selfservice_nums.setText("0");
//        }
//
//        if (SharedUtil.getString("print_unms") != null) {
//            edit_unms.setText(SharedUtil.getString("print_unms")+"");
//        }else {
//            edit_unms.setText("0");
//        }
//
//
//
//        sw3= (Switch) findViewById(R.id.sw3);
//
//        if (SharedUtil.getString("synchronization") != null) {
//            sw3.setChecked(Boolean.parseBoolean(SharedUtil.getString("synchronization")));
//        }
//        sw3.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                sw3.setChecked(isChecked);
//                SharedUtil.putString("synchronization", isChecked + "");
//            }
//        });
//        //是否开启餐桌
//        sw7= (Switch) findViewById(R.id.sw7);
//        sw7.setChecked(SharedUtil.getBoolean("open_table"));//默认开启餐桌
//        sw7.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                sw5.setChecked(isChecked);
//                SharedUtil.putBoolean("open_table",isChecked);
//            }
//        });
//
//        sw6= (Switch) findViewById(R.id.sw6);
//
//        if (SharedUtil.getString("printlabel") != null) {
//            sw6.setChecked(Boolean.parseBoolean(SharedUtil.getString("synchronization")));
//        }
//        sw6.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                sw6.setChecked(isChecked);
//                SharedUtil.putString("printlabel", isChecked + "");
//            }
//        });
//
//
//        sw5 = (Switch) findViewById(R.id.sw5);
//        sw5.setChecked(SharedUtil.getBoolean("self_print"));//默认自动打印
//            rl_print_nums.setVisibility(View.VISIBLE);
//        //设置是否自动打印
//        sw5.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                sw5.setChecked(isChecked);
//                SharedUtil.putBoolean("self_print",isChecked);
//            }
//        });
//        //订单标签打印
//        sw_order_lebal= (Switch) findViewById(R.id.sw_order_lebal);
//        sw_order_lebal.setChecked(SharedUtil.getBoolean("self_print_order_label"));//默认自动打印
//        sw_order_lebal.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                sw_order_lebal.setChecked(isChecked);
//                SharedUtil.putBoolean("self_print_order_label",isChecked);
//            }
//        });
//        tv_name = (TextView) findViewById(R.id.tv_name);
//        tv_name.setText(SharedUtil.getString("seller_name"));
//    }

    @OnClick(R.id.btn_zero)
    public void BntOnclick(View view){
        switch (view.getId()){
            case R.id.btn_zero:
                if (!edit_zero.getText().toString().equals("")
                        &&!edit_zero.getText().toString().equals("null")
                        &&edit_zero.getText().toString()!=null){
                    SharedUtil.putInt("zero",Integer.parseInt(edit_zero.getText().toString()));
                }else {
                    SharedUtil.putInt("zero",-1);
                }
                break;
        }
    }

    public void getpopup(String nums) {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(Setting_Activity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_untreated);
        TextView tv_context= (TextView) window.findViewById(R.id.tv_context);
        tv_context.setText("备份数据库数量"+nums);
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        but_dimdis.setText("确定");
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DbBackups(getApplication()).execute("backupDatabase");
                dialog.dismiss();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alipay:
                Intent intent1 = new Intent(Setting_Activity.this, MyFileManager.class);
                startActivityForResult(intent1, 2);
                break;
            case R.id.rl_wechat:
                Intent intent2 = new Intent(Setting_Activity.this, MyFileManager.class);
                startActivityForResult(intent2, 3);
                break;
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.btn_selfservice_sure:
                SharedUtil.putString("selfservice_nums",edit_selfservice_nums.getText().toString().trim());
                Toast.makeText(Setting_Activity.this,"设置保存成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_password:
                startActivity(new Intent(Setting_Activity.this, Changepasswordactivity.class));
                break;
            case R.id.im_map:
                new Thread() {
                    @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(Setting_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Setting_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude(); //经度
                            longitude = location.getLongitude(); //纬度
                            double[] data = {latitude, longitude};
                            Message msg = handler.obtainMessage();
                            msg.obj = data;
                            handler.sendMessage(msg);
                        }
                    }
                }.start();
                break;
            case R.id.rl_label:
                startActivity(new Intent(Setting_Activity.this, Printer_activity.class));
                break;
            case R.id.Rl_video:
                Intent intent = new Intent(Setting_Activity.this, MyFileManager.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.Rl_image:
                Intent intent3 = new Intent(Setting_Activity.this, MyFileManager.class);
                startActivityForResult(intent3, 6);
                break;
            case R.id.btn_print:
                if (Utils.StringUtils.isNumber(edit_unms.getText().toString())){
                 SharedUtil.putString("print_unms",edit_unms.getText().toString());
                }
                break;
            case R.id.Rl_calculation:
                Log.d("print","设置计算器");
                if (SystemUtil.getAppOps(Setting_Activity.this)) {
                    Intent intent4 = new Intent(Setting_Activity.this, CalculatorService.class);
                    startService(intent4);
                } else {
                    Intent intent4 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent4, 10);
                }
                break;
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1){
            Bundle bundle=null;
            if (data!=null){
            if ((bundle = data.getExtras()) != null){
                String path=bundle.getString("file");
                Log.e("print","视屏路径为"+path);
                Intent intent = new Intent("com.yzx.video");
                if (path!=null) {
                    if (path.endsWith(".mp4")) {
                        intent.putExtra("path", path);
                        File_Utils.copyFile(path,Environment.getExternalStorageDirectory().getPath() + "/Download/"+"格式工厂厨之星宣传总成片~1.mp4");
                    } else {
                        path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4";
                        intent.putExtra("path", path);
                    }
                }
                sendBroadcast(intent);
            }
            }
        }
        if (requestCode==2){
            Bundle bundle=null;
            if (data!=null) {
                if ((bundle = data.getExtras()) != null) {
                    String path=bundle.getString("file");
                    SharedUtil.putString("alipay",path);
                }
            }
        }
        if (requestCode==3){
            Bundle bundle=null;
            if (data!=null) {
                if ((bundle = data.getExtras()) != null) {
                    String path=bundle.getString("file");
                    SharedUtil.putString("wechat",path);
                }
            }
        }
        if (requestCode==6){
            Bundle bundle=null;
            if (data!=null) {
                if ((bundle = data.getExtras()) != null) {
                    Intent intent = new Intent("com.yzx.image");
                    String path=bundle.getString("file");
                    intent.putExtra("type","0");
                    intent.putExtra("path",path);
                    sendBroadcast(intent);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mService.isBTopen() == false)
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
//        StringUtils.HideBottomBar(Setting_Activity.this);
    }
}
