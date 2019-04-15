package retail.yzx.com.kz;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Custom_Entty;
import Entty.ShuliangEntty;
import Utils.Bitmap_Utils;
import Utils.EscposUtil;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.UsbPrinter;
import Utils.UsbPrinterUtil;
import adapters.Print_Adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import widget.Switch;

import static Utils.Bitmap_Utils.addBitmap;

/**
 * Created by admin on 2017/6/1.
 * 标签打印机设置页面
 */
public class Printer_activity extends BaseActivity implements View.OnClickListener, Print_Adapter.OnClickListener {
    public ImageView im_huanghui;
    public ListView lv;
    public Print_Adapter adapter;
    public int total;
    public List<Commodity> commodities;
    Custom_Entty custom_entty;
//    public TwinklingRefreshLayout refreshLayout;
    //下拉加载的判断
    public boolean isLoad=false;
    public int page=1;
    public EditText tv_seek;

    public Button but_debug;
    public String size="35";
    Switch aSwitch;
    Switch sw_price;
    TextView tv_price;

    public TextView tv_page;
    public Button but_next,but_last,but_print;
    public boolean paging1 = false, paging2 = false;

    public Button but_setting;
    public List<Map<String,String>> mapList=new ArrayList<>();

    public String order="0";

    @Override
    protected int getContentId() {
        return R.layout.printer_activity;
    }

    @Override
    protected void init() {
        super.init();
        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(this);
        but_setting= (Button) findViewById(R.id.but_setting);
        but_next = (Button) findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        but_last = (Button) findViewById(R.id.but_last);
        but_last.setOnClickListener(this);
        but_print= (Button) findViewById(R.id.but_print);

        but_print.setOnClickListener(this);

        tv_page = (TextView)findViewById(R.id.tv_page);

        tv_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Printer_activity.this);
                dialog.setTitle("请输入页面");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.page_layout);
                final EditText editText= (EditText) window.findViewById(R.id.ed_page);
                Button but_goto= (Button) window.findViewById(R.id.but_goto);
                Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editText.getText().toString().equals("")&& StringUtils.isNumber(editText.getText().toString())){
                            page=Integer.parseInt(editText.getText().toString());
                            Loadats();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            dialog.dismiss();
                        }

                    }
                });
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    }
                });
            }
        });


        aSwitch = (Switch) findViewById(R.id.sw6);
        sw_price = (Switch) findViewById(R.id.sw_price);

        tv_price = (TextView) findViewById(R.id.tv_price);

        tv_seek= (EditText) findViewById(R.id.tv_seek);
        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    tv_seek.getText().toString();
                    getseek(tv_seek.getText().toString());
                }

                return true;
            }
        });

        but_debug= (Button) findViewById(R.id.but_debug);
        but_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_seek.setText("");

            }
        });

        lv= (ListView) findViewById(R.id.lv);
        adapter=new Print_Adapter(this);
        adapter.setOnClickListener(this);

        commodities=new ArrayList<>();

//        refreshLayout= (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
////                if (!isLoad){
////                    isLoad=true;
////                    page++;
////                    Loadats();
////                    isLoad=false;
////                    refreshLayout.finishLoadmore();
////                }
//                refreshLayout.finishLoadmore();
//            }
//
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//            }
//        });


//        mUtil = new UsbPrinterUtil(Printer_activity.this);
//        List<UsbDevice> devs = mUtil.getUsbPrinterList();
//        if (devs.size() > 0) {
//            mUtil.requestPermission(devs.get(0), null);
//        }
        im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);


        if (!SharedUtil.getBoolean("printlabel1") ) {
            tv_price.setVisibility(View.VISIBLE);
            sw_price.setVisibility(View.VISIBLE);
        }else {
            tv_price.setVisibility(View.GONE);
            sw_price.setVisibility(View.GONE);
        }
        aSwitch.setChecked(SharedUtil.getBoolean("printlabel1"));
        aSwitch.setOnCheckedChangeListener(new widget.Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                aSwitch.setChecked(isChecked);
                SharedUtil.putBoolean("printlabel1",isChecked);
                if (!isChecked ) {
                    tv_price.setVisibility(View.VISIBLE);
                    sw_price.setVisibility(View.VISIBLE);
                }else {
                    tv_price.setVisibility(View.GONE);
                    sw_price.setVisibility(View.GONE);
                    //弹出选择打印模板
                    ShowTemplatedialog();
                }
            }
        });
        sw_price.setChecked(SharedUtil.getfalseBoolean("old_version"));
        sw_price.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                SharedUtil.putfalseBoolean("old_version",isChecked);
            }
        });
        but_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
        if (SharedUtil.getString("sw_order")!=null){
            if (SharedUtil.getString("sw_order").equals("0")){
                order="0";
            }else {
                order="1";
            }
        }else {
            order="0";
        }


    }


    /**
     * 选择打印模板
     */
    private void ShowTemplatedialog(){
        final Dialog dialog = new Dialog(Printer_activity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Printer_activity.this, R.layout.printtemplae, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.addContentView(rootView, params);
        final CheckBox cc_box1= (CheckBox) rootView.findViewById(R.id.cc_box1);
        final CheckBox cc_box2= (CheckBox) rootView.findViewById(R.id.cc_box2);
        final CheckBox cc_box3= (CheckBox) rootView.findViewById(R.id.cc_box3);
        final CheckBox cc_box4= (CheckBox) rootView.findViewById(R.id.cc_box4);
        Switch sw_order= (Switch) rootView.findViewById(R.id.sw_order);

        if (SharedUtil.getString("sw_order")!=null){
            if (SharedUtil.getString("sw_order").equals("0")){
                sw_order.setChecked(false);
            }else {
                sw_order.setChecked(true);
            }
        }else {
            sw_order.setChecked(false);
        }

        sw_order.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    SharedUtil.putString("sw_order",1+"");
                }else {
                    SharedUtil.putString("sw_order",0+"");
                }
            }
        });

        if (SharedUtil.getString("is_price")!=null&& SharedUtil.getString("is_price").equals("")){
            cc_box1.setChecked(true);
        }else {
            switch(SharedUtil.getString("is_price")){
                case "1":
                    cc_box1.setChecked(true);
                    cc_box2.setChecked(false);
                    cc_box3.setChecked(false);
                    cc_box4.setChecked(false);
                    break;
                case "2":
                    cc_box1.setChecked(false);
                    cc_box2.setChecked(true);
                    cc_box3.setChecked(false);
                    cc_box4.setChecked(false);
                    break;
                case "3":
                    cc_box1.setChecked(false);
                    cc_box2.setChecked(false);
                    cc_box3.setChecked(true);
                    cc_box4.setChecked(false);
                    break;
                case "4":
                    cc_box1.setChecked(false);
                    cc_box2.setChecked(false);
                    cc_box3.setChecked(false);
                    cc_box4.setChecked(true);
                    break;
            }

        }

        cc_box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_price","1");
                cc_box2.setChecked(false);
                cc_box3.setChecked(false);
                cc_box4.setChecked(false);
                dialog.dismiss();
                Toast.makeText(Printer_activity.this,"选择打印模板成功",Toast.LENGTH_SHORT).show();
            }
        });
        cc_box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_price","2");
                cc_box1.setChecked(false);
                cc_box3.setChecked(false);
                cc_box4.setChecked(false);
                dialog.dismiss();
                Toast.makeText(Printer_activity.this,"选择打印模板成功",Toast.LENGTH_SHORT).show();
            }
        });
        cc_box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_price","3");
                cc_box1.setChecked(false);
                cc_box2.setChecked(false);
                cc_box4.setChecked(false);
                dialog.dismiss();
                Toast.makeText(Printer_activity.this,"选择打印模板成功",Toast.LENGTH_SHORT).show();
            }
        });
        cc_box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil.putString("is_price","4");
                cc_box1.setChecked(false);
                cc_box2.setChecked(false);
                cc_box3.setChecked(false);
                dialog.dismiss();
                Toast.makeText(Printer_activity.this,"选择打印模板成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 自定义打印模板
     */
    private void customDialog(){
        final Dialog dialog = new Dialog(Printer_activity.this);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(Printer_activity.this, R.layout.customprint, null);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.addContentView(rootView, params);

        final EditText ed_Size= (EditText) rootView.findViewById(R.id.ed_Size);
        final EditText ed_nameshopY= (EditText) rootView.findViewById(R.id.ed_nameshopY);
        final EditText ed_nameshopX= (EditText) rootView.findViewById(R.id.ed_nameshopX);
        final EditText ed_nameshopsize=(EditText) rootView.findViewById(R.id.ed_nameshopsize);
        final EditText ed_nameY= (EditText) rootView.findViewById(R.id.ed_nameY);
        final EditText ed_nameX= (EditText) rootView.findViewById(R.id.ed_nameX);
        final EditText ed_namesize=(EditText) rootView.findViewById(R.id.ed_namesize);
        final EditText ed_namepriceY= (EditText) rootView.findViewById(R.id.ed_namepriceY);
        final EditText ed_namepriceX= (EditText) rootView.findViewById(R.id.ed_namepriceX);
        final EditText ed_namepricesize=(EditText) rootView.findViewById(R.id.ed_namepricesize);
        final EditText ed_memberpriceY= (EditText) rootView.findViewById(R.id.ed_memberpriceY);
        final EditText ed_memberpriceX= (EditText) rootView.findViewById(R.id.ed_memberpriceX);
        final EditText ed_memberpricesize=(EditText) rootView.findViewById(R.id.ed_memberpricesize);
        final EditText ed_namecompanyY= (EditText) rootView.findViewById(R.id.ed_namecompanyY);
        final EditText ed_namecompanyX= (EditText) rootView.findViewById(R.id.ed_namecompanyX);
        final EditText ed_namecompanysize=(EditText) rootView.findViewById(R.id.ed_namecompanysize);
        final EditText ed_namespecificationsY= (EditText) rootView.findViewById(R.id.ed_namespecificationsY);
        final EditText ed_namespecificationsX= (EditText) rootView.findViewById(R.id.ed_namespecificationsX);
        final EditText ed_namespecificationssize= (EditText) rootView.findViewById(R.id.ed_namespecificationssize);
        final EditText ed_nameproduce_addrY= (EditText) rootView.findViewById(R.id.ed_nameproduce_addrY);
        final EditText ed_nameproduce_addrX= (EditText) rootView.findViewById(R.id.ed_nameproduce_addrX);
        final EditText ed_nameproduce_addrsize= (EditText) rootView.findViewById(R.id.ed_nameproduce_addrsize);

        final EditText ed_namecodeY= (EditText) rootView.findViewById(R.id.ed_namecodeY);
        final EditText ed_namecodeX= (EditText) rootView.findViewById(R.id.ed_namecodeX);

        setdistance();
        if (SharedUtil.getString("custom")!=null&&!SharedUtil.getString("custom").equals("")){

            ed_Size.setText(custom_entty.getSize());
            ed_nameshopY.setText(custom_entty.getShopY());
            ed_nameshopX.setText(custom_entty.getShopX());
            ed_nameshopsize.setText(custom_entty.getShopsize()+"");
            ed_nameY.setText(custom_entty.getNameY());
            ed_nameX.setText(custom_entty.getNameX());
            ed_namesize.setText(custom_entty.getNamesize()+"");
            ed_namepriceY.setText(custom_entty.getPriceY());
            ed_namepriceX.setText(custom_entty.getPriceX());
            ed_namepricesize.setText(custom_entty.getPricesize()+"");
            ed_memberpriceY.setText(custom_entty.getMemberpriceY());
            ed_memberpriceX.setText(custom_entty.getMemberpriceX());
            ed_memberpricesize.setText(custom_entty.getMemberpricesize()+"");
            ed_namecompanyY.setText(custom_entty.getCompanyY());
            ed_namecompanyX.setText(custom_entty.getCompanyX());
            ed_namecompanysize.setText(custom_entty.getCompanysize()+"");
            ed_namespecificationsY.setText(custom_entty.getSpecificationsY());
            ed_namespecificationsX.setText(custom_entty.getSpecificationsX());
            ed_namespecificationssize.setText(custom_entty.getSpecificationssize()+"");
            ed_namecodeY.setText(custom_entty.getCodeY());
            ed_namecodeX.setText(custom_entty.getCodeX());
            ed_nameproduce_addrY.setText(custom_entty.getNameproduce_addrY());
            ed_nameproduce_addrX.setText(custom_entty.getNameproduce_addrX());
            ed_nameproduce_addrsize.setText(custom_entty.getNameproduce_addrsize()+"");
        }
        Button but_baochun= (Button) rootView.findViewById(R.id.but_baochun);
        but_baochun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map=new HashMap<>();
                if (ed_Size.getText().toString().indexOf("*") == -1){
                    return;
                }else {
                    map.put("size",ed_Size.getText().toString());
                }
                if (ed_nameshopY.getText().toString().equals("")&&ed_nameshopY.getText().toString()==null){
                    map.put("shopY","");
                }else {
                    map.put("shopY",ed_nameshopY.getText().toString());
                }
                if (ed_nameshopX.getText().toString().equals("")&&ed_nameshopX.getText().toString()==null){
                    map.put("shopX","");
                }else {
                    map.put("shopX",ed_nameshopX.getText().toString());
                }
                if (ed_nameshopsize.getText().toString().equals("")&&ed_nameshopsize.getText().toString()==null){
                    map.put("shopsize",ed_nameshopsize.getHint().toString());
                }else {
                    map.put("shopsize",ed_nameshopsize.getText().toString());
                }
                if (ed_nameY.getText().toString().equals("")&&ed_nameY.getText().toString()==null){
                    map.put("nameY","");
                }else {
                    map.put("nameY",ed_nameY.getText().toString());
                }
                if (ed_nameX.getText().toString().equals("")&&ed_nameX.getText().toString()==null){
                    map.put("nameX","");
                }else {
                    map.put("nameX",ed_nameX.getText().toString());
                }
                if (ed_namesize.getText().toString().equals("")&&ed_namesize.getText().toString()==null){
                    map.put("namesize",ed_namesize.getHint().toString());
                }else {
                    map.put("namesize",ed_namesize.getText().toString());
                }
                if (ed_namepriceY.getText().toString().equals("")&&ed_namepriceY.getText().toString()==null){
                    map.put("priceY","");
                }else {
                    map.put("priceY",ed_namepriceY.getText().toString());
                }
                if (ed_namepriceX.getText().toString().equals("")&&ed_namepriceX.getText().toString()==null){
                    map.put("priceX","");
                }else {
                    map.put("priceX",ed_namepriceX.getText().toString());
                }
                if (ed_namepricesize.getText().toString().equals("")&&ed_namepricesize.getText().toString()==null){
                    map.put("pricesize",ed_namepricesize.getHint().toString());
                }else {
                    map.put("pricesize",ed_namepricesize.getText().toString());
                }
                if (ed_namecompanyY.getText().toString().equals("")&&ed_namecompanyY.getText().toString()==null){
                    map.put("companyY","");
                }else {
                    map.put("companyY",ed_namecompanyY.getText().toString());
                }
                if (ed_namecompanyX.getText().toString().equals("")&&ed_namecompanyX.getText().toString()==null){
                    map.put("companyX","");
                }else {
                    map.put("companyX",ed_namecompanyX.getText().toString());
                }

                if (ed_namecompanysize.getText().toString().equals("")&&ed_namecompanysize.getText().toString()==null){
                    map.put("companysize",ed_namecompanysize.getHint().toString());
                }else {
                    map.put("companysize",ed_namecompanysize.getText().toString());
                }

                if (ed_namespecificationsY.getText().toString().equals("")&&ed_namespecificationsY.getText().toString()==null){
                    map.put("specificationsY","");
                }else {
                    map.put("specificationsY",ed_namespecificationsY.getText().toString());
                }
                if (ed_namespecificationsX.getText().toString().equals("")&&ed_namespecificationsX.getText().toString()==null){
                    map.put("specificationsX","");
                }else {
                    map.put("specificationsX",ed_namespecificationsX.getText().toString());
                }
                if (ed_namespecificationssize.getText().toString().equals("")&&ed_namespecificationssize.getText().toString()==null){
                    map.put("specificationssize",ed_namespecificationssize.getHint().toString());
                }else {
                    map.put("specificationssize",ed_namespecificationssize.getText().toString());
                }
                if (ed_nameproduce_addrY.getText().toString().equals("")&&ed_nameproduce_addrY.getText().toString()==null){
                    map.put("nameproduce_addrY","");
                }else {
                    map.put("nameproduce_addrY",ed_nameproduce_addrY.getText().toString());
                }
                if (ed_nameproduce_addrX.getText().toString().equals("")&&ed_nameproduce_addrX.getText().toString()==null){
                    map.put("nameproduce_addrX","");
                }else {
                    map.put("nameproduce_addrX",ed_nameproduce_addrX.getText().toString());
                }
                if (ed_nameproduce_addrsize.getText().toString().equals("")&&ed_nameproduce_addrsize.getText().toString()==null){
                    map.put("nameproduce_addrsize",ed_nameproduce_addrsize.getHint().toString());
                }else {
                    map.put("nameproduce_addrsize",ed_nameproduce_addrsize.getText().toString());
                }
                if (ed_namecodeY.getText().toString().equals("")&&ed_namecodeY.getText().toString()==null){
                    map.put("codeY","");
                }else {
                    map.put("codeY",ed_namecodeY.getText().toString());
                }
                if (ed_namecodeX.getText().toString().equals("")&&ed_namecodeX.getText().toString()==null){
                    map.put("codeX","");
                }else {
                    map.put("codeX",ed_namecodeX.getText().toString());
                }
                if (ed_memberpriceX.getText().toString().equals("")&&ed_memberpriceX.getText().toString()==null){
                    map.put("memberpriceX","");
                }else {
                    map.put("memberpriceX",ed_memberpriceX.getText().toString());
                }

                if (ed_memberpriceY.getText().toString().equals("")&&ed_memberpriceY.getText().toString()==null){
                    map.put("memberpriceY","");
                }else {
                    map.put("memberpriceY",ed_memberpriceY.getText().toString());
                }
                if (ed_memberpricesize.getText().toString().equals("")&&ed_memberpricesize.getText().toString()==null){
                    map.put("memberpricesize",ed_memberpricesize.getHint().toString());
                }else {
                    map.put("memberpricesize",ed_memberpricesize.getText().toString());
                }


                Gson gson=new Gson();
                String string=gson.toJson(map);
                Log.d("print","记录的数据为"+string);
                SharedUtil.putString("custom",string);
                dialog.dismiss();
                Toast.makeText(Printer_activity.this,"保存打印模板成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        Loadats();
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.printer_activity);
////        init();
//        Loadats();
//
//    }

    //加载商品信息
    private void Loadats() {
        //        获得全部数据
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("page", page)
                .params("pagelimit",20)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = new JSONObject(s);
                        JSONObject j1 = jsonobject.getJSONObject("response");
                        JSONObject j2 = j1.getJSONObject("data");
                        total=j2.getInt("total");
                        JSONArray jsonArray = j2.getJSONArray("goods_info");
                        JSONArray ja2=j2.getJSONArray("sum");
                        JSONObject jo4=ja2.getJSONObject(0);
                            commodities.clear();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            Commodity commodity = new Commodity();
                            JSONObject jo1 = jsonArray.getJSONObject(j);
                            commodity.setGoods_id(jo1.getString("goods_id"));
                            commodity.setName(jo1.getString("name"));
                            commodity.setPy(jo1.getString("py"));
//                                commodity.setSeller_id(jsonObject.getString("seller_id"));
                            commodity.setPrice(jo1.getString("price"));
                            commodity.setCost(jo1.getString("cost"));
                            commodity.setBncode(jo1.getString("bncode"));
                            commodity.setTag_id(jo1.getString("tag_id"));
                            commodity.setTag_name(jo1.getString("tag_name"));
                            if (jo1.getString("unit").equals("null")){
                                commodity.setUnit("");
                            }else {
                                commodity.setUnit(jo1.getString("unit"));
                            }

                            commodity.setUnit_id(jo1.getInt("unit_id"));
                            commodity.setSpecification(jo1.getString("specification"));
                            commodity.setStore(jo1.getString("store"));
//                                jinjia += Float.valueOf(jo1.getString("cost"));
//                                zongshoujia += Float.valueOf(jo1.getString("price"));
//                                zong += Float.valueOf(jo1.getString("store"));
                            commodity.setGood_limit(jo1.getString("good_limit"));
                            commodity.setGood_stock(jo1.getString("good_stock"));
                            commodity.setPD(jo1.getString("PD"));
                            commodity.setGD(jo1.getString("GD"));
                            commodity.setCook_position(jo1.getString("cook_position"));
//                                    commodity.setGood_remark(jsonObject.getString("good_remark"));
                            commodity.setMarketable(jo1.getString("marketable"));
                            commodity.setMember_price(jo1.getString("member_price"));
                            commodity.setProduce_addr(jo1.getString("produce_addr"));

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
                            commodities.add(commodity);

                        }
                            if (total % 20 == 0) {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 20));
                            } else {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 20 + 1));
                            }
                            adapter.setAdats(commodities);
                            lv.setAdapter(adapter);
                            lv.setSelection((page-1)*20-10);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                        }

//    private void init1() {
//        //隐藏底部按钮
////        toggleHideyBar();
//
//        tv_seek= (EditText) findViewById(R.id.tv_seek);
//        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i== EditorInfo.IME_ACTION_SEARCH){
//                    tv_seek.getText().toString();
//                    getseek(tv_seek.getText().toString());
//                }
//
//                return true;
//            }
//        });
//
//        but_debug= (Button) findViewById(R.id.but_debug);
//        but_debug.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tv_seek.setText("");
//
//            }
//        });
//
//        lv= (ListView) findViewById(R.id.lv);
//        adapter=new Print_Adapter(this);
//        adapter.setOnClickListener(this);
//
//        commodities=new ArrayList<>();
//
//        refreshLayout= (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                if (!isLoad){
//                    isLoad=true;
//                    page++;
//                    Loadats();
//                    isLoad=false;
//                    refreshLayout.finishLoadmore();
//                }
//
//
//            }
//
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//            }
//        });
//
//
//        mUtil = new UsbPrinterUtil(this);
//        List<UsbDevice> devs = mUtil.getUsbPrinterList();
//        if (devs.size() > 0) {
//            mUtil.requestPermission(devs.get(0), null);
//        }
//            im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
//            im_huanghui.setOnClickListener(this);
//        }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.but_next:
                if (Integer.valueOf(total) % 20 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                    if (page < (Integer.valueOf(total) / 20)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            Loadats();
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 20 + 1));
                    if (page < (Integer.valueOf(total) / 20 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            Loadats();
                            paging1 = false;
                        }
                    }
                }
                break;
            //上一页
            case R.id.but_last:
                if (page > 1) {
                    if (Integer.valueOf(total) % 20 == 0) {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                        if (!paging2) {
                            paging2 = true;
                            Loadats();
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 20 + 1));
                        if (!paging2) {
                            paging2 = true;
                            Loadats();
                            paging2 = false;
                        }
                    }
                }
                break;
            case R.id.but_print:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setPrint();
                    }
                });
                thread.start();
                break;
        }
    }




    //批量打印

    private void setPrint(){
        UsbPrinterUtil mUtil=null;
        if (mUtil==null){
            mUtil = new UsbPrinterUtil(Printer_activity.this);
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
//                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.dd);
//                Bitmap_Utils.FormatBMP(Bitmap_Utils.getBitmap("250ml"));
//                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "文字图片.bmp");
//                LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml"));
//                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "文字图片.bmp");
//
//                LabelCommand label = new LabelCommand();
//                LabelCommand labelCommand = new LabelCommand(1, 1, 1);
//
//                label.addSize(70,38);
//                label.addGap(3);
//                label.addPrint(12);
//                label.addText(80, 60, LabelCommand.FONTTYPE.FONT_1, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "名字");
////                label.addBitmap(20, 20, LabelCommand.BITMAP_MODE.OR, 20, LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));
//
//                label.addPrint(1,1);
//                LabelUtils.bitmapToBWPix(LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));


                prt = new UsbPrinter(this, dev);

//                prt.write(new byte[label.getCommand().size()]);

//                prt.printBitmap(Bitmap_Utils.getBitmap("250ml"),Bitmap_Utils.getBitmap("250ml").getWidth(),80,60);
//                prt.downloadbmp(LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));

//                String sendString = "SIZE 71mm,50mm\\nGAP 0,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS" +
//                        "\\nTEXT 440,40,\"TSS24.BF2\",0,2,2,\"" + StringUtils.stringpointtwo(commodities.get(i).getPrice()) +
//                        "\\nTEXT 80,100,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getName() + "\"" +
//                        "\\nTEXT 446,148,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getUnit() + "\"" +
//                        "\\nTEXT 160,180,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getSpecification() + "\"" +
//                        "\\nBARCODE 130,250,\"128\",50,0,0,2,4,\"" + commodities.get(i).getBncode() + "\"" +
//                        "\\nPRINT 1,1";
//                byte[] f= EscposUtil.convertEscposToBinary(sendString);
//                prt.printString(sendString, font, bold, underlined, doubleHeight,
//                        doubleWidth);

                /**
                 * 打印条码
                 */
//                String str1="SIZE 71mm,35mm\\nGAP 0,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS";
//                String str6="\\nDENSITY 15\\nTEXT 426,30,\"TSS24.BF2\",0,2,2,\""+ StringUtils.stringpointtwo(commodities.get(i).getPrice())+"\"";
//                String str2="\\nDENSITY 7\\nTEXT 108,65,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getName()+"\"";
//                String str3="\\nDENSITY 7\\nTEXT 446,130,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getUnit()+"\"";
//                String str4="\\nDENSITY 7\\nTEXT 160,130,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getSpecification()+"\"";
//                String str5="\\nBARCODE 130,190,\"128\",50,0,0,2,4,\""+commodities.get(i).getBncode()+"\""+ "\\nPRINT 1,1";
//                prt.printString(str1, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str6, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str2, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str3, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str4, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str5, font, bold, underlined, doubleHeight,
//                        doubleWidth);
                for (int n=0;n<commodities.size();n++) {
                    if (!SharedUtil.getBoolean("printlabel1")) {
                        String str1 = "SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                        String str2 = "\\nDENSITY 3\\nBARCODE 20,30,\"128\",80,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"";
//                    String str3="\\nDENSITY 7\\nTEXT 40,55,\"TSS24.BF2\",0,1,2,\""+mSelf_Service_GoodsInfo.get(i).getName()+"\"";
//                    String str4="\\nDENSITY 7\\nTEXT 5,120,\"TSS24.BF2\",0,1,1,\""+"备注:"+mSelf_Service_GoodsInfo.get(i).getSize()+mSelf_Service_GoodsInfo.get(i).getNotes()+"\"";
//                    String str5="\\nDENSITY 7\\nTEXT 5,165,\"TSS24.BF2\",0,1,1,\""+"金额:"+mSelf_Service_GoodsInfo.get(i).getPrice()+"\"";

                        String str6 = "";
//                    if (sw_price.getVisibility()==View.GONE) {
                        String str = " ";
                        int j = 40 - (commodities.get(n).getName().length() * 3);
                        if (j <= 0) {
                        } else {
                            for (int k = 0; k < (j / 3); k++) {
                                str += " ";
                            }
                        }
//                    String str6="\\nDENSITY 15\\nTEXT 0,170,\"TSS24.BF2\",0,1,2,\""+str+commodities.get(i).getName()+"\""+"\\nPRINT 1,1";
                        str6 = "\\nDENSITY 15\\nTEXT 0,170,\"TSS24.BF2\",0,1,1,\"" + str + commodities.get(n).getName() + "\"" + "\\nPRINT 1,1";
//                    }else {
////                        if (){
////
////                        }
//                    }
//                String str7="\\nBARCODE 130,170,\"128\",50,0,0,2,4,\""+commodities.get(i).getBncode()+"\""+ "\\nPRINT 1,1";
                        try {
                            List<String> strings=new ArrayList<>();
//                            prt.printString(str1, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                            prt.printString(str2, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
////                        prt.printString(str3, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str4, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str5, font, bold, underlined, doubleHeight,
////                                doubleWidth);
//                            prt.printString(str6, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
                            strings.add(str1);
                            strings.add(str2);
                            strings.add(str6);
                            setPrint(prt,strings);
                        } catch (Exception e) {
                            Log.e("barcode", "标签打印异常：" + e.toString());
                        }
                    } else {
//                        String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION 0\\nCLS";
//                        String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
//                        String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"" + "\\r\\n";
//                        String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getPrice()) + "\"" + "\\r\\n";
//                        String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"" + "\\r\\n";
//                        String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"" + "\\r\\n";
//
////                String str6="\\nDENSITY 7\\nTEXT 20,210,\"TSS24.BF2\",0,1,1,\""+"时间："+"2017:07:22 11:48"+"\""+"\\nPRINT 1,1";
//                        String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";
                        if (SharedUtil.getString("is_price")!=null&& SharedUtil.getString("is_price").equals("")){
                            String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                            String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                            String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"" + "\\r\\n";
                            String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getPrice()) + "\"" + "\\r\\n";
                            String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"" + "\\r\\n";
                            String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"" + "\\r\\n";
                            String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";
                            List<String> strings=new ArrayList<>();
//                            prt.printString(str1, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                            prt.printString(str2, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                            prt.printString(str3, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                        prt.printString(str8, font, bold, underlined, doubleHeight,
//                                doubleWidth);
//                            prt.printString(str4, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                            prt.printString(str5, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
//                            prt.printString(str7, font, bold, underlined, doubleHeight,
//                                    doubleWidth);
                            strings.add(str1);
                            strings.add(str2);
                            strings.add(str3);
                            strings.add(str8);
                            strings.add(str4);
                            strings.add(str5);
                            strings.add(str7);
                            setPrint(prt,strings);
                        }else {
                            switch(SharedUtil.getString("is_price")){
                                case "1":
                                    Bitmap bitmap2=null;
                                    Bitmap bitmap3=null;
                                    Bitmap bitmap4=null;
                                    Bitmap bitmap5=null;
                                    Bitmap bitmap8=null;
                                    String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                                    String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                    bitmap2= Bitmap_Utils.fromText(SharedUtil.getString("name"),40);
                                    String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"" + "\\r\\n";
                                    bitmap3= Bitmap_Utils.fromText(commodities.get(n).getName(),40);
                                    String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getPrice()) + "\"" + "\\r\\n";
                                    bitmap8= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(n).getPrice())+"",80);
                                    String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"" + "\\r\\n";
                                    bitmap4= Bitmap_Utils.fromText(commodities.get(n).getSpecification()+"",40);
                                    String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"" + "\\r\\n";
                                    bitmap5= Bitmap_Utils.fromText(commodities.get(n).getUnit(),20);
                                    String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";
                                    prt.printString(str1, font, bold, underlined, doubleHeight,
                                            doubleWidth);

                                    if (SharedUtil.getfalseBoolean("old_version")){
                                        prt.printString(str2, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str3, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str8, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str4, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str5, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                    }else {

                                        prt.write(addBitmap(280, 15, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                                        prt.write(addBitmap(95, 65, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                                        prt.write(addBitmap(400, 180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap8.getWidth(), bitmap8));
                                        prt.write(addBitmap(85, 155, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                                        prt.write(addBitmap(220, 125, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));

                                    }
                                    prt.printString(str7, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    break;
                                case "2":
                                    Bitmap bit20=null;
                                    Bitmap bit30=null;
                                    Bitmap bit40=null;
                                    Bitmap bit50=null;
                                    String str10 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                    String str20 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodities.get(n).getPrice()) + "\"";
                                    bit20= Bitmap_Utils.fromText(StringUtils.setPointone(commodities.get(n).getPrice()) ,100);
                                    String str30 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
                                    bit30= Bitmap_Utils.fromText(commodities.get(n).getName(),40);
                                    String str40 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"";
                                    bit40= Bitmap_Utils.fromText(commodities.get(n).getSpecification(),20);
                                    String str50 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"";
                                    bit50= Bitmap_Utils.fromText(commodities.get(n).getUnit() ,20);
                                    String str70 = "\\nDENSITY 3\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";

                                    prt.printString(str10, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    if (SharedUtil.getfalseBoolean("old_version")){
                                        prt.printString(str20, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str30, font, bold, underlined, doubleHeight,
                                                doubleWidth);
//                        prt.printString(str8, font, bold, underlined, doubleHeight,
//                                doubleWidth);
                                        prt.printString(str40, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str50, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                    }else {
                                        prt.write(addBitmap(320, 12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit20.getWidth(), bit20));
                                        prt.write(addBitmap(100, 70, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit30.getWidth(), bit30));
                                        prt.write(addBitmap(340, 150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit40.getWidth(), bit40));
                                        prt.write(addBitmap(140, 150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit50.getWidth(), bit50));
                                    }
                                    prt.printString(str70, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    break;
                                case "3":
                                    Bitmap bit12=null;
                                    Bitmap bit13=null;
                                    Bitmap bit14=null;
                                    Bitmap bit16=null;
                                    Bitmap bit18=null;
                                    Bitmap bit19=null;
                                    String str11 = "SIZE 50mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                    String str21 = "\\nDENSITY 7\\nTEXT 100,12,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName()+ "\"";
                                    bit12= Bitmap_Utils.fromText(commodities.get(n).getName() ,40);
//                                    String str31 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
                                    String str41 = "\\nDENSITY 15\\nTEXT 250,110,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getPrice()) + "\"";
                                    bit14= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(n).getPrice())+"" ,100);
                                    String str31 = "\\nDENSITY 15\\nTEXT 20,240,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"";
                                    bit13= Bitmap_Utils.fromText(SharedUtil.getString("name") ,40);
                                    String str61;
                                    String str81;
                                    String str91;
                                    String str100="\\nPRINT 1,1";
                                    if (commodities.get(n).getCook_position().equals("0")){
                                        str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\"" +"单位:"+ commodities.get(n).getUnit() + "\"";
                                        bit16= Bitmap_Utils.fromText("单位:"+ commodities.get(n).getUnit() ,20);
                                        str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:"+commodities.get(n).getSpecification() + "\"";
                                        bit18= Bitmap_Utils.fromText("规格:"+commodities.get(n).getSpecification() ,20);
                                        str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装" + "\""+"\\nPRINT 1,1";
                                        bit19= Bitmap_Utils.fromText("产地:见包装" ,20);
                                    }else {
                                        str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\""  +"单位:散称" + "\"";
                                        bit16= Bitmap_Utils.fromText("单位:散称" ,20);
                                        str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:见包装" + "\"";
                                        bit18= Bitmap_Utils.fromText("规格:见包装" ,20);
                                        str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装"+ "\""+"\\nPRINT 1,1";
                                        bit19= Bitmap_Utils.fromText("产地:见包装" ,20);
                                    }

                                    prt.printString(str11, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    if (SharedUtil.getfalseBoolean("old_version")){
                                        prt.printString(str21, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str31, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str41, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str61, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str81, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        prt.printString(str91, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                    }else {
                                        prt.write(addBitmap(100, 12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit12.getWidth(), bit12));
                                        prt.write(addBitmap(250, 110, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit14.getWidth(), bit14));
                                        prt.write(addBitmap(20, 240, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit13.getWidth(), bit13));
                                        prt.write(addBitmap(40, 80, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit16.getWidth(), bit16));
                                        prt.write(addBitmap(40, 120, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit18.getWidth(), bit18));
                                        prt.write(addBitmap(40, 160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit19.getWidth(), bit19));
                                    }
                                    prt.printString(str100,font,bold,underlined,doubleHeight,doubleWidth);
                                    break;
                                case "4":
                                    setdistance();
                                    if (custom_entty!=null) {
                                        Bitmap bit=null;
                                        Bitmap bit2=null;
                                        Bitmap bit3=null;
                                        Bitmap bit4=null;
                                        Bitmap bitm4=null;
                                        Bitmap bit5=null;
                                        Bitmap bit6=null;
                                        Bitmap bit7=null;
                                        String strin1 = "SIZE " + custom_entty.getSize().substring(0, custom_entty.getSize().indexOf("*")) + "mm," + custom_entty.getSize().substring(custom_entty.getSize().indexOf("*") + 1) + "mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION " + order + "\\nCLS";
                                        String strin2 = "\\nDENSITY 15\\nTEXT " + custom_entty.getShopY() + "," + custom_entty.getShopX() + ",\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                        bit2= Bitmap_Utils.fromText(SharedUtil.getString("name") ,custom_entty.getShopsize());
                                        String strin3 = "\\nDENSITY 15\\nTEXT " + custom_entty.getNameY() + "," + custom_entty.getNameX() + ",\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"" + "\\r\\n";
                                        bit3= Bitmap_Utils.fromText(commodities.get(n).getName() ,custom_entty.getNamesize());
                                        String strin4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getPriceY() + "," + custom_entty.getPriceX() + ",\"TSS24.BF2\",0,4,4,\"" + commodities.get(n).getPrice() + "\"" + "\\r\\n";
                                        bit4= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(n).getPrice())+"" ,custom_entty.getPricesize());
                                        String strinm4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getMemberpriceY() + "," + custom_entty.getMemberpriceX() + ",\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getMember_price()) + "\"" + "\\r\\n";
                                        bitm4= Bitmap_Utils.fromText(commodities.get(n).getMember_price() ,custom_entty.getMemberpricesize());
                                        String strin5 = "\\nDENSITY 15\\nTEXT " + custom_entty.getSpecificationsY() + "," + custom_entty.getSpecificationsX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"" + "\\r\\n";
                                        bit5= Bitmap_Utils.fromText(commodities.get(n).getSpecification() ,custom_entty.getSpecificationssize());
                                        String strin6 = "\\nDENSITY 15\\nTEXT " + custom_entty.getCompanyY() + "," + custom_entty.getCompanyX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"" + "\\r\\n";
                                        bit6= Bitmap_Utils.fromText(commodities.get(n).getUnit() ,custom_entty.getCompanysize());
                                        String strin9="\\nDENSITY 15\\nTEXT " + custom_entty.getNameproduce_addrY() + "," + custom_entty.getNameproduce_addrX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getProduce_addr() + "\"" + "\\r\\n";
                                        bit7= Bitmap_Utils.fromText(commodities.get(n).getProduce_addr() ,custom_entty.getNameproduce_addrsize());
                                        String strin7 = "\\nDENSITY 3\\nBARCODE " + custom_entty.getCodeY() + "," + custom_entty.getCodeX() + ",\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" +"\\nPRINT 1,1";
                                        prt.printString(strin1, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                        if (custom_entty.getShopY() != null) {
                                            if (!custom_entty.getShopY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")){
                                                    prt.printString(strin2, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                }else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getShopY()) - 20, Integer.parseInt(custom_entty.getShopX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit2.getWidth(), bit2));
                                                }
                                            }
                                        }
                                        if (custom_entty.getNameY() != null) {
                                            if (!custom_entty.getNameY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")){
                                                    prt.printString(strin3, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                }else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getNameY()) - 20, Integer.parseInt(custom_entty.getNameX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit3.getWidth(), bit3));
                                                }
                                            }
                                        }
                                        if (custom_entty.getPriceY() != null) {
                                            if (!custom_entty.getPriceY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")){
                                                    prt.printString(strin4, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                }else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getPriceY()) - 50, Integer.parseInt(custom_entty.getPriceX()) - 20, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit4.getWidth(), bit4));
                                                }
                                            }
                                        }
                                        if (custom_entty.getMemberpriceY() != null) {
                                            if (!custom_entty.getMemberpriceY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")){
                                                    prt.printString(strinm4, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                }else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getMemberpriceY()) - 20, Integer.parseInt(custom_entty.getMemberpriceX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitm4.getWidth(), bitm4));
                                                }
                                            }
                                        }
                                        if (custom_entty.getSpecificationsY() != null) {
                                            if (!custom_entty.getSpecificationsY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")){
                                                    prt.printString(strin5, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                }else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getSpecificationsY()) - 20, Integer.parseInt(custom_entty.getSpecificationsX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit5.getWidth(), bit5));
                                                }
                                            }
                                        }

                                        if (custom_entty.getNameproduce_addrY() != null) {
                                            if (!custom_entty.getNameproduce_addrY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")) {
                                                    prt.printString(strin9, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                } else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getNameproduce_addrY()) - 20, Integer.parseInt(custom_entty.getNameproduce_addrX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit7.getWidth(), bit7));
                                                }
                                            }
                                        }

                                        if (custom_entty.getCompanyY() != null) {
                                            if (!custom_entty.getCompanyY().equals("")) {
                                                if (SharedUtil.getfalseBoolean("old_version")) {
                                                    prt.printString(strin6, font, bold, underlined, doubleHeight,
                                                            doubleWidth);
                                                } else {
                                                    prt.write(addBitmap(Integer.parseInt(custom_entty.getCompanyY()) - 20, Integer.parseInt(custom_entty.getCompanyX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit6.getWidth(), bit6));
                                                }
                                            }
                                        }
                                        prt.printString(strin7, font, bold, underlined, doubleHeight,
                                                doubleWidth);
                                    }
                                    break;
                            }

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
//                if (prt!=null){
//                    prt.close();
//                }
            }
        }else {
            init();
        }
    }



    private void globalprint(UsbPrinter prt, UsbPrinter.FONT font, Boolean bold, Boolean underlined, Boolean doubleHeight,
                             Boolean doubleWidth, int n){
                                String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION 0\\nCLS";
                        String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                        String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"" + "\\r\\n";
                        String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(n).getPrice()) + "\"" + "\\r\\n";
                        String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"" + "\\r\\n";
                        String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"" + "\\r\\n";
//                String str6="\\nDENSITY 7\\nTEXT 20,210,\"TSS24.BF2\",0,1,1,\""+"时间："+"2017:07:22 11:48"+"\""+"\\nPRINT 1,1";
                        String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";
        try {
            prt.printString(str1, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str2, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str3, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str8, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str4, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str5, font, bold, underlined, doubleHeight,
                    doubleWidth);
            prt.printString(str7, font, bold, underlined, doubleHeight,
                    doubleWidth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void yixingprint(UsbPrinter prt, UsbPrinter.FONT font, Boolean bold, Boolean underlined, Boolean doubleHeight,
                             Boolean doubleWidth, int n){
        String str1 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS";
        String str2 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodities.get(n).getPrice()) + "\"";
        String str3 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
        String str4 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getSpecification() + "\"";
        String str5 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(n).getUnit() + "\"";
//                String str6="\\nDENSITY 7\\nTEXT 20,210,\"TSS24.BF2\",0,1,1,\""+"时间："+"2017:07:22 11:48"+"\""+"\\nPRINT 1,1";
        String str7 = "\\nDENSITY 3\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodities.get(n).getBncode() + "\"" + "\\nPRINT 1,1";
        try {
            prt.printString(str1, font, bold, underlined, doubleHeight,
                    doubleWidth);
        prt.printString(str2, font, bold, underlined, doubleHeight,
                doubleWidth);
        prt.printString(str3, font, bold, underlined, doubleHeight,
                doubleWidth);
//                        prt.printString(str8, font, bold, underlined, doubleHeight,
//                                doubleWidth);
        prt.printString(str4, font, bold, underlined, doubleHeight,
                doubleWidth);
        prt.printString(str5, font, bold, underlined, doubleHeight,
                doubleWidth);
        prt.printString(str7, font, bold, underlined, doubleHeight,
                doubleWidth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getseek(String str) {
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
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","搜索的结果"+s);
                        commodities.clear();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            String message=jsonObject1.getString("message");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                for (int j = 0; j < ja1.length(); j++) {
                                    Commodity commodity = new Commodity();
                                    JSONObject jo2 = ja1.getJSONObject(j);
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
//                                commodity.setSeller_id(jsonObject.getString("seller_id"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setTag_id(jo2.getString("tag_id"));
                                    commodity.setTag_name(jo2.getString("tag_name"));
                                    commodity.setUnit(jo2.getString("unit"));
                                    commodity.setUnit_id(jo2.getInt("unit_id"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setSpecification(jo2.getString("specification"));
//                                jinjia += Float.valueOf(jo1.getString("cost"));
//                                zongshoujia += Float.valueOf(jo1.getString("price"));
//                                zong += Float.valueOf(jo1.getString("store"));
                                    commodity.setGood_limit(jo2.getString("good_limit"));
                                    commodity.setGood_stock(jo2.getString("good_stock"));
                                    commodity.setPD(jo2.getString("PD"));
                                    commodity.setGD(jo2.getString("GD"));
//                                    commodity.setGood_remark(jsonObject.getString("good_remark"));
                                    commodity.setMarketable(jo2.getString("marketable"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    commodity.setAltc(jo2.getString("ALTC"));
                                    commodity.setProduce_addr(jo2.getString("produce_addr"));
                                    JSONArray ja3 = jo2.getJSONArray("label");
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
                                    commodities.add(commodity);
                                }
                            }
                            tv_seek.setText("");
                            adapter.setAdats(commodities);
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
//
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(Printer_activity.this);
//    }

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

    //订单标签USB
    public  static void setPaintSelf_srevice(Context mContext,String nubmer,String time,ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo){
        UsbPrinterUtil UsbPrinterUtil = new UsbPrinterUtil(mContext);
        UsbDevice dev=null;
        if (UsbPrinterUtil.getUsbPrinterList().size()>=1){
            dev = UsbPrinterUtil.getUsbPrinterList().get(0);
        }
        UsbPrinter prt = null;
        UsbPrinter.FONT font = UsbPrinter.FONT.FONT_B;
        Boolean bold = false;
        Boolean underlined = false;
        Boolean doubleHeight = false;
        Boolean doubleWidth = false;
        try {
            if (dev!=null){
                prt = new UsbPrinter(mContext, dev);
            }
        }catch (Exception e){
            Log.e("barcode","e11+"+e.toString());
        }
        for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
            for(int j=0;j<Integer.parseInt(mSelf_Service_GoodsInfo.get(i).getNumber());j++){
            String str1="SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS";
            String str2="\\nDENSITY 7\\nTEXT 130,13,\"TSS24.BF2\",0,1,1,\""+nubmer+"\"";
            String str3="\\nDENSITY 7\\nTEXT 40,55,\"TSS24.BF2\",0,1,2,\""+mSelf_Service_GoodsInfo.get(i).getName()+"\"";
            String str4="\\nDENSITY 7\\nTEXT 5,120,\"TSS24.BF2\",0,1,1,\""+"备注:"+mSelf_Service_GoodsInfo.get(i).getSize()+mSelf_Service_GoodsInfo.get(i).getNotes()+"\"";
            String str5="\\nDENSITY 7\\nTEXT 5,165,\"TSS24.BF2\",0,1,1,\""+"金额:"+mSelf_Service_GoodsInfo.get(i).getPrice()+"\"";
            String str6="\\nDENSITY 7\\nTEXT 5,210,\"TSS24.BF2\",0,1,1,\""+"时间:"+time+"\""+"\\nPRINT 1,1";
//                String str7="\\nBARCODE 130,170,\"128\",50,0,0,2,4,\""+commodities.get(i).getBncode()+"\""+ "\\nPRINT 1,1";
            try{

                prt.printString(str1, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.printString(str2, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.printString(str3, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.printString(str4, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.printString(str5, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.printString(str6, font, bold, underlined, doubleHeight,
                        doubleWidth);
            }catch (Exception e){
                Log.e("barcode", "标签打印异常："+e.toString());
            }
            }
        }
    }

    //数量的
    public List<ShuliangEntty> entty = new ArrayList<>();

    @Override
    public void setonclick(int i) {
        UsbPrinterUtil mUtil=null;
        if (mUtil==null){
            mUtil = new UsbPrinterUtil(Printer_activity.this);
            List<UsbDevice> devs = mUtil.getUsbPrinterList();
            if (devs.size() > 0) {
                mUtil.requestPermission(devs.get(0), null);
            }
        }
        ShuliangEntty shuliangEntty=new ShuliangEntty();
        shuliangEntty.setNumber(1);
        entty.add(shuliangEntty);
//        String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), "1122", "1111", "11", commodities, entty,
//                1, 11,"", "11", "11","11", "0","","");
//
//        PrintWired.usbPrint(Printer_activity.this,syy);
        if (mUtil.getUsbPrinterList().size()>0){
            UsbDevice dev = mUtil.getUsbPrinterList().get(0);
            UsbPrinter prt = null;
            UsbPrinter.FONT font = UsbPrinter.FONT.FONT_B;
            Boolean bold = false;
            Boolean underlined = false;
            Boolean doubleHeight = false;
            Boolean doubleWidth = false;
            try {
//                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.dd);
//                Bitmap_Utils.FormatBMP(Bitmap_Utils.getBitmap("250ml"));
//                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "文字图片.bmp");
//                LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml"));
//                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "文字图片.bmp");
//                LabelCommand label = new LabelCommand();
//                LabelCommand labelCommand = new LabelCommand(1, 1, 1);
//                label.addPrint(12);
//                label.addText(80, 60, LabelCommand.FONTTYPE.FONT_1, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "名字");
//                label.addBitmap(20, 20, LabelCommand.BITMAP_MODE.OR, 20, LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));
//                LabelUtils.bitmapToBWPix(LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));
                prt = new UsbPrinter(this, dev);
//                prt.printBitmap(Bitmap_Utils.getBitmap("250ml"),Bitmap_Utils.getBitmap("250ml").getWidth(),80,60);
//                prt.downloadbmp(LabelUtils.toGrayscale(Bitmap_Utils.getBitmap("250ml")));

//                String sendString = "SIZE 71mm,50mm\\nGAP 0,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS" +
//                        "\\nTEXT 440,40,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getPrice()) +
//                        "\\nTEXT 80,100,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getName() + "\"" +
//                        "\\nTEXT 446,148,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getUnit() + "\"" +
//                        "\\nTEXT 160,180,\"TSS24.BF2\",0,2,2,\"" + commodities.get(i).getSpecification() + "\"" +
//                        "\\nBARCODE 130,250,\"128\",50,0,0,2,4,\"" + commodities.get(i).getBncode() + "\"" +
//                        "\\nPRINT 1,1";
//                byte[] f= EscposUtil.convertEscposToBinary(sendString);
//                prt.printString(sendString, font, bold, underlined, doubleHeight,
//                        doubleWidth);
                /**
                 * 打印条码
                 */
//                String str1="SIZE 71mm,35mm\\nGAP 0,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS";
//                String str6="\\nDENSITY 15\\nTEXT 426,30,\"TSS24.BF2\",0,2,2,\""+ StringUtils.stringpointtwo(commodities.get(i).getPrice())+"\"";
//                String str2="\\nDENSITY 7\\nTEXT 108,65,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getName()+"\"";
//                String str3="\\nDENSITY 7\\nTEXT 446,130,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getUnit()+"\"";
//                String str4="\\nDENSITY 7\\nTEXT 160,130,\"TSS24.BF2\",0,2,2,\""+commodities.get(i).getSpecification()+"\"";
//                String str5="\\nBARCODE 130,190,\"128\",50,0,0,2,4,\""+commodities.get(i).getBncode()+"\""+ "\\nPRINT 1,1";
//                prt.printString(str1, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str6, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str2, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str3, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str4, font, bold, underlined, doubleHeight,
//                        doubleWidth);
//                prt.printString(str5, font, bold, underlined, doubleHeight,
//                        doubleWidth);
                if (!SharedUtil.getBoolean("printlabel1")) {       //order
                    Bitmap bitmap2=null;
                    String str1 = "SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                    String str2 = "\\nDENSITY 3\\nBARCODE 20,30,\"128\",80,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"";
//                    String str3="\\nDENSITY 7\\nTEXT 40,55,\"TSS24.BF2\",0,1,2,\""+mSelf_Service_GoodsInfo.get(i).getName()+"\"";
//                    String str4="\\nDENSITY 7\\nTEXT 5,120,\"TSS24.BF2\",0,1,1,\""+"备注:"+mSelf_Service_GoodsInfo.get(i).getSize()+mSelf_Service_GoodsInfo.get(i).getNotes()+"\"";
//                    String str5="\\nDENSITY 7\\nTEXT 5,165,\"TSS24.BF2\",0,1,1,\""+"金额:"+mSelf_Service_GoodsInfo.get(i).getPrice()+"\"";
                    String str6 = "";
//                    if (sw_price.getVisibility()==View.GONE) {
                    String str = " ";
                    int j = 40 - (commodities.get(i).getName().length() * 3);
                    if (j <= 0) {
                    } else {
                        for (int k = 0; k < (j / 3); k++) {
                            str += " ";
                        }
                    }
//                    String str6="\\nDENSITY 15\\nTEXT 0,170,\"TSS24.BF2\",0,1,2,\""+str+commodities.get(i).getName()+"\""+"\\nPRINT 1,1";
                    str6 = "\\nDENSITY 15\\nTEXT 0,170,\"TSS24.BF2\",0,1,1,\"" + str + commodities.get(i).getName() + "\"" + "\\nPRINT 1,1";
//                    }else {
////                        if (){
////
////                        }
//                    }

//                String str7="\\nBARCODE 130,170,\"128\",50,0,0,2,4,\""+commodities.get(i).getBncode()+"\""+ "\\nPRINT 1,1";
                    try {
                        prt.printString(str1, font, bold, underlined, doubleHeight,
                                doubleWidth);
                        prt.printString(str2, font, bold, underlined, doubleHeight,
                                doubleWidth);
//                        prt.printString(str3, font, bold, underlined, doubleHeight,
//                                doubleWidth);
//                        prt.printString(str4, font, bold, underlined, doubleHeight,
//                                doubleWidth);
//                        prt.printString(str5, font, bold, underlined, doubleHeight,
//                                doubleWidth);
                        prt.printString(str6, font, bold, underlined, doubleHeight,
                                doubleWidth);
                    } catch (Exception e) {
                        Log.e("barcode", "标签打印异常：" + e.toString());
                    }
                } else {
                    if (SharedUtil.getString("is_price")!=null&& SharedUtil.getString("is_price").equals("")){
                        Bitmap bitmap2=null;
                        Bitmap bitmap3=null;
                        Bitmap bitmap4=null;
                        Bitmap bitmap5=null;
                        Bitmap bitmap8=null;
                        String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                        String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                        bitmap2= Bitmap_Utils.fromText(SharedUtil.getString("name"),40);
                        String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"" + "\\r\\n";
                        bitmap3= Bitmap_Utils.fromText(commodities.get(i).getName(),40);
                        String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getPrice()) + "\"" + "\\r\\n";
                        bitmap8= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(i).getPrice())+"",80);
                        String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"" + "\\r\\n";
                        bitmap4= Bitmap_Utils.fromText(commodities.get(i).getSpecification()+"",40);
                        String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"" + "\\r\\n";
                        bitmap5= Bitmap_Utils.fromText(commodities.get(i).getUnit(),20);
                        String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" + "\\nPRINT 1,1";
                        prt.printString(str1, font, bold, underlined, doubleHeight,
                                doubleWidth);
                        if (SharedUtil.getfalseBoolean("old_version")){
                                prt.printString(str2, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str3, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str8, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str4, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str5, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                        }else {
                            prt.write(addBitmap(280,15, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                            prt.write(addBitmap(95,65, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                            prt.write(addBitmap(400,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap8.getWidth(), bitmap8));
                            prt.write(addBitmap(85,155, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                            prt.write(addBitmap(220,125, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
                        }
                        prt.printString(str7, font, bold, underlined, doubleHeight,
                                doubleWidth);
                    }else {
                        switch (SharedUtil.getString("is_price")) {
                            case "1":
                                Bitmap bitmap2=null;
                                Bitmap bitmap3=null;
                                Bitmap bitmap4=null;
                                Bitmap bitmap5=null;
                                Bitmap bitmap8=null;
                                String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                                String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                bitmap2= Bitmap_Utils.fromText(SharedUtil.getString("name"),40);
                                String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"" + "\\r\\n";
                                bitmap3= Bitmap_Utils.fromText(commodities.get(i).getName(),40);
                                String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getPrice()) + "\"" + "\\r\\n";
                                bitmap8= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(i).getPrice())+"",80);
                                String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"" + "\\r\\n";
                                bitmap4= Bitmap_Utils.fromText(commodities.get(i).getSpecification()+"",40);
                                String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"" + "\\r\\n";
                                bitmap5= Bitmap_Utils.fromText(commodities.get(i).getUnit(),20);
                                String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" + "\\nPRINT 1,1";
                                prt.printString(str1, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                if (SharedUtil.getfalseBoolean("old_version")){
                                    prt.printString(str2, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str3, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str8, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str4, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str5, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                }else {
                                    prt.write(addBitmap(280,15, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                                    prt.write(addBitmap(95,65, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                                    prt.write(addBitmap(400,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap8.getWidth(), bitmap8));
                                    prt.write(addBitmap(85,155, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                                    prt.write(addBitmap(220,125, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
                                }
                                prt.printString(str7, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                break;
                            case "2":
                                Bitmap bit20=null;
                                Bitmap bit30=null;
                                Bitmap bit40=null;
                                Bitmap bit50=null;
                                String str10 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                String str20 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodities.get(i).getPrice()) + "\"";
                                bit20= Bitmap_Utils.fromText(StringUtils.setPointone(commodities.get(i).getPrice()) ,100);
                                String str30 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"";
                                bit30= Bitmap_Utils.fromText(commodities.get(i).getName(),40);
                                String str40 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"";
                                bit40= Bitmap_Utils.fromText(commodities.get(i).getSpecification(),20);
                                String str50 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"";
                                bit50= Bitmap_Utils.fromText(commodities.get(i).getUnit() ,20);
                                String str70 = "\\nDENSITY 3\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" + "\\nPRINT 1,1";

                                prt.printString(str10, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                if (SharedUtil.getfalseBoolean("old_version")){
                                    prt.printString(str20, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str30, font, bold, underlined, doubleHeight,
                                            doubleWidth);
//                        prt.printString(str8, font, bold, underlined, doubleHeight,
//                                doubleWidth);
                                    prt.printString(str40, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    prt.printString(str50, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                }else {
                                    prt.write(addBitmap(320,12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit20.getWidth(), bit20));
                                    prt.write(addBitmap(100,70, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit30.getWidth(), bit30));
                                    prt.write(addBitmap(340,150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit40.getWidth(), bit40));
                                    prt.write(addBitmap(140,150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit50.getWidth(), bit50));
                                }

                                prt.printString(str70, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                break;
                            case "3":
                                Bitmap bit12=null;
                                Bitmap bit13=null;
                                Bitmap bit14=null;
                                Bitmap bit16=null;
                                Bitmap bit18=null;
                                Bitmap bit19=null;
                                String str11 = "SIZE 50mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                String str21 = "\\nDENSITY 7\\nTEXT 100,12,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName()+ "\"";
                                bit12= Bitmap_Utils.fromText(commodities.get(i).getName() ,40);
//                                    String str31 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
                                String str41 = "\\nDENSITY 15\\nTEXT 250,110,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getPrice()) + "\"";
                                bit14= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(i).getPrice())+"" ,100);
                                String str31 = "\\nDENSITY 15\\nTEXT 20,240,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"";
                                bit13= Bitmap_Utils.fromText(SharedUtil.getString("name") ,40);
                                String str61;
                                String str81;
                                String str91;
                                String str100="\\nPRINT 1,1";
                                if (commodities.get(i).getCook_position().equals("0")){
                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\"" +"单位:"+ commodities.get(i).getUnit() + "\"";
                                    bit16= Bitmap_Utils.fromText("单位:"+ commodities.get(i).getUnit() ,20);
                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:"+commodities.get(i).getSpecification() + "\"";
                                    bit18= Bitmap_Utils.fromText("规格:"+commodities.get(i).getSpecification() ,20);
                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装" + "\""+"\\nPRINT 1,1";
                                    bit19= Bitmap_Utils.fromText("产地:见包装" ,20);
                                }else {
                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\""  +"单位:散称" + "\"";
                                    bit16= Bitmap_Utils.fromText("单位:散称" ,20);
                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:见包装" + "\"";
                                    bit18= Bitmap_Utils.fromText("规格:见包装" ,20);
                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装"+ "\""+"\\nPRINT 1,1";
                                    bit19= Bitmap_Utils.fromText("产地:见包装" ,20);
                                }
                                prt.printString(str11, font, bold, underlined, doubleHeight,
                                        doubleWidth);

                                if (SharedUtil.getfalseBoolean("old_version")){
                                    prt.printString(str21, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                    prt.printString(str31, font, bold, underlined, doubleHeight,
                                            doubleWidth);
//                        prt.printString(str8, font, bold, underlined, doubleHeight,
//                                doubleWidth);
                                prt.printString(str41, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str61, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str81, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.printString(str91, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                }else {
                                    prt.write(addBitmap(100, 12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit12.getWidth(), bit12));
                                    prt.write(addBitmap(250, 110, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit14.getWidth(), bit14));
                                    prt.write(addBitmap(20, 240, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit13.getWidth(), bit13));
                                    prt.write(addBitmap(40, 80, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit16.getWidth(), bit16));
                                    prt.write(addBitmap(40, 120, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit18.getWidth(), bit18));
                                    prt.write(addBitmap(40, 160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit19.getWidth(), bit19));
                                }
                                prt.printString(str100,font,bold,underlined,doubleHeight,doubleWidth);
                                break;
                            case "4":
                                setdistance();
                                if (custom_entty!=null) {
                                    Bitmap bit=null;
                                    Bitmap bit2=null;
                                    Bitmap bit3=null;
                                    Bitmap bit4=null;
                                    Bitmap bitm4=null;
                                    Bitmap bit5=null;
                                    Bitmap bit6=null;
                                    Bitmap bit7=null;
                                    String strin1 = "SIZE " + custom_entty.getSize().substring(0, custom_entty.getSize().indexOf("*")) + "mm," + custom_entty.getSize().substring(custom_entty.getSize().indexOf("*") + 1) + "mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION " + order + "\\nCLS";
                                    String strin2 = "\\nDENSITY 15\\nTEXT " + custom_entty.getShopY() + "," + custom_entty.getShopX() + ",\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                    bit2= Bitmap_Utils.fromText(SharedUtil.getString("name") ,custom_entty.getShopsize());
                                    String strin3 = "\\nDENSITY 15\\nTEXT " + custom_entty.getNameY() + "," + custom_entty.getNameX() + ",\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"" + "\\r\\n";
                                    bit3= Bitmap_Utils.fromText(commodities.get(i).getName() ,custom_entty.getNamesize());
                                    String strin4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getPriceY() + "," + custom_entty.getPriceX() + ",\"TSS24.BF2\",0,4,4,\"" + commodities.get(i).getPrice() + "\"" + "\\r\\n";
                                    bit4= Bitmap_Utils.fromText(Double.parseDouble(commodities.get(i).getPrice())+"" ,custom_entty.getPricesize());
                                    String strinm4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getMemberpriceY() + "," + custom_entty.getMemberpriceX() + ",\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getMember_price()) + "\"" + "\\r\\n";
                                    bitm4= Bitmap_Utils.fromText(commodities.get(i).getMember_price() ,custom_entty.getMemberpricesize());
                                    String strin5 = "\\nDENSITY 15\\nTEXT " + custom_entty.getSpecificationsY() + "," + custom_entty.getSpecificationsX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"" + "\\r\\n";
                                    bit5= Bitmap_Utils.fromText(commodities.get(i).getSpecification() ,custom_entty.getSpecificationssize());
                                    String strin6 = "\\nDENSITY 15\\nTEXT " + custom_entty.getCompanyY() + "," + custom_entty.getCompanyX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"" + "\\r\\n";
                                    bit6= Bitmap_Utils.fromText(commodities.get(i).getUnit() ,custom_entty.getCompanysize());
                                    String strin9="\\nDENSITY 15\\nTEXT " + custom_entty.getNameproduce_addrY() + "," + custom_entty.getNameproduce_addrX() + ",\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getProduce_addr() + "\"" + "\\r\\n";
                                    bit7= Bitmap_Utils.fromText(commodities.get(i).getProduce_addr() ,custom_entty.getNameproduce_addrsize());
                                    String strin7 = "\\nDENSITY 3\\nBARCODE " + custom_entty.getCodeY() + "," + custom_entty.getCodeX() + ",\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" +"\\nPRINT 1,1";
//                                    String strin9=setBitmap(20, 50, 0, bit.getWidth(), bit);
                                    prt.printString(strin1, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    if (custom_entty.getShopY() != null) {
                                        if (!custom_entty.getShopY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin2, font, bold, underlined, doubleHeight,
                                                    doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getShopY()) - 20, Integer.parseInt(custom_entty.getShopX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit2.getWidth(), bit2));

                                            }
                                        }
                                    }
                                    if (custom_entty.getNameY() != null) {
                                        if (!custom_entty.getNameY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin3, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getNameY()) - 20, Integer.parseInt(custom_entty.getNameX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit3.getWidth(), bit3));
                                            }
                                        }
                                    }
                                    if (custom_entty.getPriceY() != null) {
                                        if (!custom_entty.getPriceY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin4, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getPriceY()) - 50, Integer.parseInt(custom_entty.getPriceX()) - 20, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit4.getWidth(), bit4));
                                            }
                                        }
                                    }
                                    if (custom_entty.getMemberpriceY() != null) {
                                        if (!custom_entty.getMemberpriceY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strinm4, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getMemberpriceY()) - 20, Integer.parseInt(custom_entty.getMemberpriceX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitm4.getWidth(), bitm4));
                                            }
                                        }
                                    }
                                    if (custom_entty.getSpecificationsY() != null) {
                                        if (!custom_entty.getSpecificationsY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin5, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getSpecificationsY()) - 20, Integer.parseInt(custom_entty.getSpecificationsX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit5.getWidth(), bit5));
                                            }
                                        }
                                    }

                                    if (custom_entty.getNameproduce_addrY() != null) {
                                        if (!custom_entty.getNameproduce_addrY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin9, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getNameproduce_addrY()) - 20, Integer.parseInt(custom_entty.getNameproduce_addrX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit7.getWidth(), bit7));
                                            }
                                        }
                                    }

                                    if (custom_entty.getCompanyY() != null) {
                                        if (!custom_entty.getCompanyY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin6, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getCompanyY()) - 20, Integer.parseInt(custom_entty.getCompanyX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit6.getWidth(), bit6));
                                            }
                                        }
                                    }
//                                    Bitmap bitmap1=Bitmap_Utils.getBitmap(commodities.get(i),custom_entty,"2",30);

//                                    Bitmap bitmap1=Bitmap_Utils.getBitmap("100",30);
//                                    prt.write(addBitmap(0, 0, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap1.getWidth(), bitmap1));
                                    prt.printString(strin7, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                }
                                break;
                        }
                    }
//                    String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION 0\\nCLS";
//                    String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
//                    String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"" + "\\r\\n";
//                    String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodities.get(i).getPrice()) + "\"" + "\\r\\n";
//                    String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"" + "\\r\\n";
//                    String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"" + "\\r\\n";
//
////                String str6="\\nDENSITY 7\\nTEXT 20,210,\"TSS24.BF2\",0,1,1,\""+"时间："+"2017:07:22 11:48"+"\""+"\\nPRINT 1,1";
//                    String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" + "\\nPRINT 1,1";

//                String str1 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION 1\\nCLS";
//                String str2 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodities.get(i).getPrice()) + "\"";
//                String str3 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(i).getName() + "\"";
//                String str4 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getSpecification() + "\"";
//                String str5 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodities.get(i).getUnit() + "\"";
//
////                String str6="\\nDENSITY 7\\nTEXT 20,210,\"TSS24.BF2\",0,1,1,\""+"时间："+"2017:07:22 11:48"+"\""+"\\nPRINT 1,1";
//                String str7 = "\\nDENSITY 3\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodities.get(i).getBncode() + "\"" + "\\nPRINT 1,1";
//
//                    prt.printString(str1, font, bold, underlined, doubleHeight,
//                            doubleWidth);
//                    prt.printString(str2, font, bold, underlined, doubleHeight,
//                            doubleWidth);
//                    prt.printString(str3, font, bold, underlined, doubleHeight,
//                            doubleWidth);
////                    prt.printString(str8, font, bold, underlined, doubleHeight,
////                            doubleWidth);
//                    prt.printString(str4, font, bold, underlined, doubleHeight,
//                            doubleWidth);
//                    prt.printString(str5, font, bold, underlined, doubleHeight,
//                            doubleWidth);
//                    prt.printString(str7, font, bold, underlined, doubleHeight,
//                            doubleWidth);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
//                if (prt!=null){
//                    prt.close();
//                }
            }
        }else {
            init();
        }
    }

    //设置打印标签距离
    public void setdistance(){
        if (SharedUtil.getString("custom")!=null){
            if (!SharedUtil.getString("custom").equals("")) {
                try {
                    custom_entty = new Custom_Entty();
                    JSONObject jsonObject = new JSONObject(SharedUtil.getString("custom"));
                    custom_entty.setCodeX(jsonObject.getString("codeX"));
                    custom_entty.setCodeY(jsonObject.getString("codeY"));
                    custom_entty.setCompanyX(jsonObject.getString("companyX"));
                    custom_entty.setCompanyY(jsonObject.getString("companyY"));
                    custom_entty.setCompanysize(jsonObject.getInt("companysize"));
                    custom_entty.setNameX(jsonObject.getString("nameX"));
                    custom_entty.setNameY(jsonObject.getString("nameY"));
                    custom_entty.setNamesize(jsonObject.getInt("namesize"));
                    custom_entty.setPriceX(jsonObject.getString("priceX"));
                    custom_entty.setPriceY(jsonObject.getString("priceY"));
                    custom_entty.setPricesize(jsonObject.getInt("pricesize"));
                    custom_entty.setShopX(jsonObject.getString("shopX"));
                    custom_entty.setShopY(jsonObject.getString("shopY"));
                    custom_entty.setShopsize(jsonObject.getInt("shopsize"));
                    custom_entty.setSize(jsonObject.getString("size"));
                    custom_entty.setMemberpriceX(jsonObject.getString("memberpriceX"));
                    custom_entty.setMemberpriceY(jsonObject.getString("memberpriceY"));
                    custom_entty.setMemberpricesize(jsonObject.getInt("memberpricesize"));
                    custom_entty.setSpecificationsX(jsonObject.getString("specificationsX"));
                    custom_entty.setSpecificationsY(jsonObject.getString("specificationsY"));
                    custom_entty.setSpecificationssize(jsonObject.getInt("specificationssize"));
                    custom_entty.setNameproduce_addrX(jsonObject.getString("nameproduce_addrX"));
                    custom_entty.setNameproduce_addrY(jsonObject.getString("nameproduce_addrY"));
                    custom_entty.setNameproduce_addrsize(jsonObject.getInt("nameproduce_addrsize"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public void write(byte[] data) throws IOException {
//        UsbEndpoint mEndpoint=null;
//        UsbDevice usbDevice = mUtil.getUsbPrinterList().get(0);
//        UsbInterface iface=null;
//        for (int i=0;i<usbDevice.getInterfaceCount();i++) {
//            iface = usbDevice.getInterface(i);
//            if (iface == null)
//                throw new IOException("failed to get interface " + i);
//            int epcount = iface.getEndpointCount();
//            for (int j = 0; j < epcount; j++) {
//                UsbEndpoint ep = iface.getEndpoint(j);
//                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
//                    mEndpoint = ep;
//                    break;
//                }
//            }
//        }
//        UsbManager usbman= (UsbManager) getSystemService(USB_SERVICE);
//        UsbDeviceConnection mConnection=usbman.openDevice(usbDevice);
//        if (mConnection.bulkTransfer(mEndpoint, data, data.length,
//                200) != data.length)
//            throw new IOException("failed to write usb endpoint.");
//    }

    /**
     * 设置打印
     * @param print
     * @param str
     */
    public void setPrint(UsbPrinter print, List<String> str){
        if (print!=null){
            String string="";
            String command = "";
            for (int i=0;i<str.size();i++){
                string=string+str.get(i);
            }
            try {
                byte[] b = EscposUtil.convertEscposToBinary(string);
                if(b != null) print.write(b);

                command = String.format("'%s' LF", string );
                b = EscposUtil.convertEscposToBinary(command);
                if(b != null)
                    print.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
