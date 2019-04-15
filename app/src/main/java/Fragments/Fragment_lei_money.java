package Fragments;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Select_Tag;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.PrintUtil;
import Utils.PrintWired;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_Fuzzy;
import adapters.Adapter_Money;
import adapters.Adapter_tag_dialog;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;
import widget.MylistView;

/**
 * Created by admin on 2017/6/25.
 */
public class Fragment_lei_money extends Fragment implements Adapter_Money.Setonclick, View.OnClickListener, Adapter_tag_dialog.SetOnclick, Adapter_Money.Setnumsonclick, CompoundButton.OnCheckedChangeListener, Adapter_Fuzzy.SetOnclick {

    public View view;
    public ListView lv_money;
    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Commodity commoditys;
    public List<Commodity> adats;
    public List<Commodity> listcommoditys;
    public List<Commodity> listprint;
    public List<Integer> list_print_entty;
    public List<Integer> entty;
    public List<Integer> listentty;
    public List<Boolean> Checkeds;
    public List<Boolean> listCheckeds;
    public Adapter_Money adapter;
    public List<Commodity> Listadats;
    public List<Integer> listint;
    public List<Boolean> listboolean;
    public List<Map<String,String>> listmap;
    public List<Map<String,String>> report_map;
    public TextView tv_total,tv_cc_unms;
    public Dialog dialog;
    public CheckBox cc_box;
    public boolean cc_b=true;
    public Button but_money,but_tag;
    public int index=0;
    public List<Select_Tag> select_tags;
    public Adapter_tag_dialog adapter_tag_dialog;
    public RecyclerView Rl_dialog;
    public String Tag_id;
    public Button but_shopping,but_shuliang;
    public String str="";
    private CheckBox cc_select;


    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 1;



    //添加商品
    List<Commodity> list_fuzzy=new ArrayList<>();
    public Adapter_Fuzzy adapter_fuzzy;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

                            Toast.makeText(getContext(), "连接成功",
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
                    Toast.makeText(getContext(), "设备连接丢失",
                            Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    if (getContext()!=null){
                        Toast.makeText(getContext(), "关闭设备",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.money_lei_layout,null);
        init();

        //获取保护数量
        getAdtas();

//        getAdats(true);
        cc_box.setChecked(true);
        return view;
    }

    private void init() {
        but_shopping= (Button) view.findViewById(R.id.but_shopping);
        but_shuliang= (Button) view.findViewById(R.id.but_shuliang);
        but_shopping.setOnClickListener(this);

        cc_select= (CheckBox) view.findViewById(R.id.cc_select);
        cc_select.setOnCheckedChangeListener(this);

        listprint=new ArrayList<>();
        list_print_entty=new ArrayList<>();

        select_tags=new ArrayList<>();

        listboolean=new ArrayList<>();
        Listadats=new ArrayList<>();
        listint=new ArrayList<>();

        adats=new ArrayList<>();
        listcommoditys=new ArrayList<>();
        entty=new ArrayList<>();
        listentty=new ArrayList<>();
        Checkeds=new ArrayList<>();
        listCheckeds=new ArrayList<>();
        listmap=new ArrayList<>();
        report_map=new ArrayList<>();
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        cc_box= (CheckBox) view.findViewById(R.id.cc_box);
        cc_box.setOnClickListener(this);
        cc_box.setChecked(true);

        but_money= (Button) view.findViewById(R.id.but_money);
        but_money.setOnClickListener(this);
        but_tag= (Button) view.findViewById(R.id.but_tag);
        but_tag.setOnClickListener(this);
        tv_cc_unms= (TextView) view.findViewById(R.id.tv_cc_unms);
        tv_total= (TextView) view.findViewById(R.id.tv_total);
        lv_money= (ListView) view.findViewById(R.id.lv_money);
        adapter=new Adapter_Money(getContext());
        adapter.Setitmeonclcik(this);
        adapter.Setitmenumsonclick(this);

        //蓝牙打印
        mService = new BluetoothService(getContext(), mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(getContext(), "蓝牙不可用", Toast.LENGTH_LONG).show();
        }

        //蓝牙标签打印机绑定服务
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(getContext(), GpPrintService.class);
        getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService

        //判断蓝牙是否连接小票打印机
        String address_Mac=SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
        if(TextUtils.isEmpty(address_Mac)){
            retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"如需打印小票，请先点击打印按钮，连接小票打印机",20);
        }else {
            con_dev = mService.getDevByMac(address_Mac);
            mService.connect(con_dev);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
    }

    //点击选中的商品
    @Override
    public void setClickListener(int i) {
        adats.add(list_fuzzy.get(i));
        entty.add(0);
        Checkeds.add(true);
        adapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        dialog1.dismiss();
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

//    public void getAdats(boolean b){
//        int number = 0;
//        Cursor cursor=null;
//        adats.clear();
//        if (cursor==null){
//            cursor = sqLiteDatabase.rawQuery("select * from commodity", null);
//        }
//        number=cursor.getCount();
//        if (number!=0){
//            while (cursor.moveToNext()){
//                commoditys = new Commodity();
//                commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
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
//                commoditys.setProvider_id(cursor.getString(cursor.getColumnIndex("provider_id")));
//                commoditys.setProvider_name(cursor.getString(cursor.getColumnIndex("provider_name")));
//                if (cursor.getString(cursor.getColumnIndex("good_limit"))!=null&&cursor.getString(cursor.getColumnIndex("good_stock"))!=null&&!cursor.getString(cursor.getColumnIndex("store")).equals("null")&&!cursor.getString(cursor.getColumnIndex("good_stock")).equals("null")&&
//                        !cursor.getString(cursor.getColumnIndex("good_limit")).equals("null")){
//                        if (Float.parseFloat(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))<=Float.parseFloat(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_stock"))))&&
//                                (TlossUtils.sub(Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_limit")))),Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))))>0){
//                            Log.d("print","报货打印的数据为");
//                            adats.add(commoditys);
//                            Checkeds.add(true);
//                            entty.add((int) TlossUtils.sub(Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_limit")))),Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))));
//                        }else {
//                            if (!b){
//                                adats.add(commoditys);
//                                Checkeds.add(true);
//                                entty.add(0);
//                            }
//                        }
//                }else {
//                    if (!b){
//                        adats.add(commoditys);
//                        Checkeds.add(true);
//                        entty.add(0);
//                    }
//                }
//            }
//        }
//        tv_total.setText(adats.size()+"");
//        tv_cc_unms.setText(adats.size()+"");
//        adapter.getAdats(adats);
//        adapter.getnums(entty);
//        adapter.getCheckeds(Checkeds);
//        lv_money.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }


    //获取报货数据
    public void getAdtas(){
        OkGo.post(SysUtils.getSellerServiceUrl("get_close_goods"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("报货的数据",s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            String message=jsonObject1.getString("message");
                            adats.clear();
                            entty.clear();
                            Checkeds.clear();
                            if (status.equals("200")){
                                JSONArray ja=jsonObject1.getJSONArray("data");
                                for (int i=0;i<ja.length();i++){
                                    JSONObject jo=ja.getJSONObject(i);
                                    commoditys = new Commodity();
                                    commoditys.setName(jo.getString("name"));
                                    commoditys.setGoods_id(jo.getString("goods_id"));
                                    commoditys.setPrice(jo.getString("price"));
                                    commoditys.setCost(jo.getString("cost"));
                                    commoditys.setBncode(jo.getString("bncode"));
                                    commoditys.setStore(jo.getString("store"));
                                    commoditys.setGood_limit(jo.getString("good_limit"));
                                    commoditys.setGood_stock(jo.getString("good_stock"));
                                    commoditys.setTag_id(jo.getString("tag_id"));
                                    commoditys.setProvider_id("1");
                                    adats.add(commoditys);
                                    Checkeds.add(true);
                                    entty.add((int) TlossUtils.sub(Double.parseDouble(jo.getString("good_limit")),Double.parseDouble(jo.getString("store"))));
                                }
                            }else {
                                if (getActivity()!=null){
                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.Setitmenumsonclick(Fragment_lei_money.this);
                            adapter.Setitmeonclcik(Fragment_lei_money.this);
                            tv_total.setText(adats.size()+"");
                            tv_cc_unms.setText(adats.size()+"");
                            adapter.getAdats(adats);
                            adapter.getnums(entty);
                            adapter.getCheckeds(Checkeds);
                            lv_money.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    Dialog dialog1;
    //添加商品
    public void AddAdtas(){
        dialog1= new Dialog(getContext());
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View rootView = View.inflate(getContext(), R.layout.fuzzy, null);
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        window.addContentView(rootView, params);
        final EditText ed_name= (EditText) rootView.findViewById(R.id.ed_name);
        Button but_goto= (Button) rootView.findViewById(R.id.but_goto);
        Button but_abolish= (Button) rootView.findViewById(R.id.but_abolish);

        final MylistView lv_fuzzy= (MylistView) rootView.findViewById(R.id.lv_fuzzy);


        but_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getseek(ed_name.getText().toString(),lv_fuzzy);
            }
        });

        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialog1.dismiss();
            }
        });


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
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setTag_id(jo2.getString("tag_id"));
                                    commodity.setProvider_id("2");
                                    list_fuzzy.add(commodity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter_fuzzy= new Adapter_Fuzzy(getContext());
                            adapter_fuzzy.setAdats(list_fuzzy);
                            adapter_fuzzy.SetOnclick(Fragment_lei_money.this);
                            lv_fuzzy.setAdapter(adapter_fuzzy);
                            adapter_fuzzy.notifyDataSetChanged();
                        }
                    }
                });
    }


    @Override
    public void Onitmeclick(int i) {
        int k=0;
        if (Checkeds.get(i)){
            Checkeds.set(i,false);
        }else {
            Checkeds.set(i,true);
        }
        for (int j=0;j<Checkeds.size();j++){
            if (Checkeds.get(j)){
                k++;
            }
        }
        if (k==Checkeds.size()){
            cc_box.setText("全选");
            cc_box.setChecked(true);
            cc_b=true;
            tv_cc_unms.setText(k+"");
        }else {
            cc_box.setText("全选");
            cc_box.setChecked(false);
            cc_b=false;
            tv_cc_unms.setText(k+"");
        }
        adapter.getCheckeds(Checkeds);
        adapter.notifyDataSetChanged();
//        adats.remove(i);
//        entty.remove(i);
//        lv_money.setSelection(i-1);
//        adapter.notifyDataSetChanged();
    }


    //点击报货的改变
    @Override
    public void Onnumsitmeclick(final int i) {
        dialog= new Dialog(getActivity());
        dialog.setTitle("请输入报货数");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.money_dialog);
        final EditText editText= (EditText) window.findViewById(R.id.ed_nums);
        final TextView tv_name= (TextView) window.findViewById(R.id.tv_name);
        tv_name.setText(adats.get(i).getName());
        final TextView tv_oldnums= (TextView) window.findViewById(R.id.tv_oldnums);
        tv_oldnums.setText(entty.get(i)+"");
        Button but_goto= (Button) window.findViewById(R.id.but_goto);
        Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
        but_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString()!=null&& StringUtils.isNumber(editText.getText().toString())){
                    entty.set(i, Integer.valueOf(editText.getText().toString()));
                    lv_money.setSelection(i);
                    adapter.getnums(entty);
                    adapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialog.dismiss();
                }
            }
        });
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialog.dismiss();
            }
        });
    }


    //加入购物车
    @Override
    public void addShopping(int i) {
        adats.remove(i);
        Checkeds.remove(i);
        entty.remove(i);
        adapter.notifyDataSetChanged();
//        Listadats.add(adats.get(i));
//        listint.add(entty.get(i));
//        listboolean.add(Checkeds.get(i));
//        adats.remove(i);
//        Checkeds.remove(i);
//        entty.remove(i);
//        but_shuliang.setText(Listadats.size()+"");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_shopping:

                AddAdtas();
//                adapter.getAdats(Listadats);
//                adapter.getnums(listint);
//                adapter.getCheckeds(listboolean);
//                adapter.gettype(1);
//                lv_money.setAdapter(adapter);

//                for (int j=0;j<Checkeds.size();j++){
//                    if (Checkeds.get(j)){
//                        Listadats.add(adats.get(j));
//                        listint.add(entty.get(j));
//                        listboolean.add(Checkeds.get(j));
//                    }else {
//                        listcommoditys.add(adats.get(j));
//                        listentty.add(entty.get(j));
//                        listCheckeds.add(Checkeds.get(j));
//                    }
//                }
//                adats.clear();
//                entty.clear();
//                Checkeds.clear();
//                adats=listcommoditys;
//                entty=listentty;
//                Checkeds=listCheckeds;
//                adapter.getAdats(adats);
//                adapter.getCheckeds(Checkeds);
//                adapter.getnums(entty);
//                lv_money.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//                but_shuliang.setText(Listadats.size()+"");
//                Log.e("print","购物车"+Listadats.size());

                break;
            //全选的点击事件
            case R.id.cc_box:
                if (cc_b){
                        for (int i=0;i<Checkeds.size();i++){
                            cc_box.setText("全选");
                            Checkeds.set(i,false);
                            tv_cc_unms.setText(0+"");
                    }
                    cc_b=false;
                }else {
                        for (int i=0;i<Checkeds.size();i++){
                            cc_box.setText("全选");
                            Checkeds.set(i,true);
                            tv_cc_unms.setText(Checkeds.size()+"");
                    }
                    cc_b=true;
                }
                adapter.notifyDataSetChanged();
                break;
            //报货的点击事件
            case R.id.but_money:
//                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setContentView(R.layout.dialog_but_refund);
//                TextView tv_title= (TextView) window.findViewById(R.id.tv_title);
//                TextView tv_refund= (TextView) window.findViewById(R.id.tv_refund);
//                TextView tv_cash= (TextView) window.findViewById(R.id.tv_cash);
//                tv_refund.setVisibility(View.GONE);
//                tv_cash.setVisibility(View.GONE);
//                tv_title.setText("是否修改报货商品");
//                Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
//                Button but_confirm= (Button) window.findViewById(R.id.but_confirm);
//                but_confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });

//                but_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
                getacceptance();
                break;
            //分类的选择
            case R.id.but_tag:
//                final AlertDialog dialog1 = new AlertDialog.Builder(getContext()).create();
//                dialog1.show();
//                Window window1 = dialog1.getWindow();
//                window1.setContentView(R.layout.dialog_but_tag);
//
//                Rl_dialog= (RecyclerView) window1.findViewById(R.id.Rl_dialog);
//                Button but_tags= (Button) window1.findViewById(R.id.but_tags);
//                but_tags.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        adats.clear();
//                        for (int i=0;i<select_tags.size();i++){
//                            if (select_tags.get(i).isSelect()){
//                                Log.d("print","fenlei "+select_tags.get(i).getTag_id());
//                                str=str+select_tags.get(i).getTag_id()+",";
//
//                                dialog1.dismiss();
//                            }
//                        }
//                        if (str!=null){
//                            String id=str.substring(0,str.length()-1);
//                            getfiltrate(id);
//                        }
//
//                    }
//                });
//                adapter_tag_dialog=new Adapter_tag_dialog(getActivity());
//                adapter_tag_dialog.SetONclickCC(this);
//                Rl_dialog.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                getTags();
                break;


        }
    }


    public void getacceptance(){
        OkGo.post(SysUtils.getSellerServiceUrl("get_not_verify_goods"))
                .tag(getActivity())
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String g, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(g);
                            JSONObject jo=jsonObject.getJSONObject("response");
                            String status=jo.getString("status");
//                            if (status.equals("200")){
//                                Toast.makeText(getActivity(),"有订单未验收请验收完在报货",Toast.LENGTH_SHORT).show();
//                            }else {
                                int unms=0;
                                Double total= Double.valueOf(0);
                                Double total_amount= Double.valueOf(0);
                                listmap.clear();
                                String str1="";
                                for (int j=0;j<adats.size();j++){
                                    Map<String,String> map=new HashMap<>();
                                    map.put("goods_name",adats.get(j).getName());
                                    map.put("bncode",adats.get(j).getBncode());
                                    map.put("berfore_num",entty.get(j)+"");
                                    map.put("goods_id",adats.get(j).getGoods_id()+"");
                                    map.put("price",adats.get(j).getCost());
                                    map.put("nav_id",adats.get(j).getTag_id());
                                    map.put("provider_id",adats.get(j).getProvider_id());
                                    total= TlossUtils.mul(Double.parseDouble(adats.get(j).getPrice()+""),Double.parseDouble(entty.get(j)+""));
//                                Log.e("数量金额",""+listint.get(j)+"   "+Listadats.get(j).getPrice()+"      "+total);
                                    total_amount= TlossUtils.add(total_amount,TlossUtils.mul(Double.parseDouble(adats.get(j).getPrice()+""),Double.parseDouble(entty.get(j)+"")));
                                    listmap.add(map);
//                                unms=TlossUtils.add(unms,entty.get(j));
                                    unms=(int) TlossUtils.add(Double.parseDouble(unms+""),entty.get(j));
                                    str1+=adats.get(j).getProvider_id()+",";
                                }



                                String syy = BluetoothPrintFormatUtil.getMoneyPrinterMsg(SharedUtil.getString("name"), DateUtils.getNowtimeKeyStr(), SharedUtil.getString("phone"),adats,entty);
//                        USBPrinters.initPrinter(getContext().getApplicationContext());
//                        USBPrinters.getInstance().print(syy);
//                        USBPrinters.destroyPrinter();
                                PrintUtil printUtil1 = new PrintUtil(getActivity());
                                printUtil1.openButtonClicked();
                                printUtil1.printstring(syy);
                                Gson gson=new Gson();

                                if (getActivity()!=null){
                                    if (PrintWired.usbPrint(getActivity(),syy)){

                                    }else {
                                        printUtil1.printstring(syy);

                                        mService.sendMessage(syy, "GBK");
                                    }
                                }

//                        PrintUtil.doPrint(getActivity(),mService,syy);
                                //商品信息
                                String str=gson.toJson(listmap);
                                report_map.clear();
                                for (int i=0;i<1;i++){
                                    Map<String,String> map=new HashMap<>();
                                    map.put("nums",unms+"");
//                            map.put("cashier",SharedUtil.getString("name"));
                                    if (!SharedUtil.getString("type").equals("0")){
                                        map.put("cashier_id","0");
                                    }else {
                                        map.put("cashier_id", SharedUtil.getString("operator_id"));
                                    }
                                    map.put("total_amount",total_amount+"");
                                    map.put("status","1");
                                    map.put("seller_id", SharedUtil.getString("seller_id"));
                                    map.put("seller_name", SharedUtil.getString("seller_name"));
                                    report_map.add(map);
                                }


                                Gson gson2=new Gson();
                                String str2=gson2.toJson(report_map);
//                        for (int j=0;j<Checkeds.size();j++){
//                            if (Checkeds.get(j)){
//                                Log.e("print","选中"+j+Checkeds.get(j));
//                            }
//                        }
                                String aa_="";
                                String aaArray[] = str1.split(",");
                                HashSet<String> hs = new HashSet<String>();
                                for(String s : aaArray){
                                    hs.add(s);
                                }
                                Iterator<String> it = hs.iterator();
                                if(it.hasNext()){
                                    aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
                                }

                                upmoney(str,str2,aa_);
//                        dialog.dismiss();
//                    }
//                });

//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void upmoney(String str,String str2,String str3){
        OkGo.post(SysUtils.getSellerServiceUrl("report_insert"))
                .tag(getActivity())
                .params("goods_map",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .params("report_map",str2)
                .params("provider_id",str3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","报货成功"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                adats.clear();
                                entty.clear();
                                Checkeds.clear();
//                                for (int j=0;j<adats.size();j++){
//                                    if (Checkeds.get(j)){
//                                    }else {
//                                        listcommoditys.add(adats.get(j));
//                                        listentty.add(entty.get(j));
//                                        listCheckeds.add(Checkeds.get(j));
//                                    }
//                                }
                                but_shuliang.setText("0");

                                Toast.makeText(getActivity(),"报货成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"报货失败",Toast.LENGTH_SHORT).show();
                            }
                            tv_cc_unms.setText("0");
//                            adats=listcommoditys;
//                            entty=listentty;
//                            Checkeds=listCheckeds;
                            adapter.getAdats(adats);
                            adapter.getCheckeds(Checkeds);
                            adapter.getnums(entty);
                            lv_money.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            index++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取分类
//    public void getTags(){
//
//        OkGo.post(SysUtils.getGoodsServiceUrl("provider_list"))
//                .tag(getActivity())
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("print","数据为"+s);
//                        try {
//                            JSONObject jsonobject=new JSONObject(s);
//                            JSONObject jo1=jsonobject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                JSONArray ja1=jo1.getJSONArray("data");
//                                select_tags.clear();
//                                for (int i=0;i<ja1.length();i++){
//                                    Select_Tag fenlei = new Select_Tag();
//                                    JSONObject jo2=ja1.getJSONObject(i);
//                                    fenlei.setName(jo2.getString("provider_name"));
//                                    fenlei.setTag_id(Integer.parseInt(jo2.getString("provider_id")));
//                                    select_tags.add(fenlei);
//                                }
//                            }
//                            adapter_tag_dialog.setAdats(select_tags);
//                            Rl_dialog.setAdapter(adapter_tag_dialog);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
////        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
////                .tag(this)
////                .cacheKey("cacheKey")
////                .cacheMode(CacheMode.DEFAULT)
////                .params("seller_id", SharedUtil.getString("seller_id"))
////                .params("seller_token", SharedUtil.getString("seller_token"))
////                .execute(new StringCallback() {
////                    @Override
////                    public void onSuccess(String s, Call call, Response response) {
////                        Log.e("print","获取的数据为"+s);
////                        JSONObject jsonobject = null;
////                        try {
////                            jsonobject = new JSONObject(s);
////                            JSONObject j1 = jsonobject.getJSONObject("response");
////                            JSONObject j2 = j1.getJSONObject("data");
////                            JSONArray ja = j2.getJSONArray("nav_info");
////                            select_tags.clear();
////                            for (int i = 0; i < ja.length(); i++) {
////                                Select_Tag fenlei = new Select_Tag();
////                                JSONObject jo = ja.getJSONObject(i);
////                                fenlei.setName(jo.getString("tag_name"));
////                                fenlei.setTag_id(jo.getInt("tag_id"));
////                                fenlei.setSelect(false);
////                                select_tags.add(fenlei);
////                            }
////                            Log.e("print","获取的数据为"+select_tags);
////                            adapter_tag_dialog.setAdats(select_tags);
////                            Rl_dialog.setAdapter(adapter_tag_dialog);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                });
//    }

    @Override
    public void setOnclickCheck(int i) {
        for (int j=0;j<select_tags.size();j++){
            select_tags.get(j).setSelect(false);
        }
        select_tags.get(i).setSelect(true);
        adapter_tag_dialog.setAdats(select_tags);
//        if (select_tags.get(i).isSelect()){
//            select_tags.get(i).setSelect(false);
//        }else {
//            select_tags.get(i).setSelect(true);
//        }
    }

    //筛选的数据
//    public void getfiltrate(String i){
//        Log.d("print","查询的语句"+i);
//        sqliteHelper = new SqliteHelper(getActivity());
//        sqLiteDatabase = sqliteHelper.getReadableDatabase();
//        int number = 0;
//        Cursor cursor=null;
//        if (cursor==null){
////            cursor = sqLiteDatabase.rawQuery("select * from commodity where tag_id=?", new String[]{i});
//            cursor = sqLiteDatabase.rawQuery("SELECT * FROM commodity WHERE provider_id in(?)", new String[]{i});
//
//        }
//        number=cursor.getCount();
//        Log.d("print","fenlei "+number);
//        Checkeds.clear();
//            while (cursor.moveToNext()){
//                commoditys = new Commodity();
//                commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
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
//                commoditys.setProvider_id(cursor.getString(cursor.getColumnIndex("provider_id")));
//                commoditys.setGD(cursor.getString(cursor.getColumnIndex("GD")));
//                commoditys.setMarketable(cursor.getString(cursor.getColumnIndex("marketable")));
//                commoditys.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
//                if (cursor.getString(cursor.getColumnIndex("good_limit"))!=null&&cursor.getString(cursor.getColumnIndex("good_stock"))!=null&&!cursor.getString(cursor.getColumnIndex("store")).equals("null")&&!cursor.getString(cursor.getColumnIndex("good_stock")).equals("null")
//                        &&!cursor.getString(cursor.getColumnIndex("good_limit")).equals("null")){
//                        if (Float.parseFloat(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))<=Float.parseFloat(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_stock"))))&&
//                                (TlossUtils.sub(Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_limit")))),Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))))>0){
//                            adats.add(commoditys);
//                            Checkeds.add(true);
//                            entty.add((int) TlossUtils.sub(Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("good_limit")))),Double.parseDouble(StringUtils.stringpointtwo(cursor.getString(cursor.getColumnIndex("store"))))));
//                        }else {
//                            adats.add(commoditys);
//                            Checkeds.add(true);
//                            entty.add(0);
//                        }
//                }else {
//                    adats.add(commoditys);
//                    Checkeds.add(true);
//                    entty.add(0);
//                }
//            }
//
//        tv_total.setText(adats.size()+"");
//        tv_cc_unms.setText(Checkeds.size()+"");
//        adapter.getAdats(adats);
//        adapter.getnums(entty);
//        adapter.getCheckeds(Checkeds);
//        lv_money.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        str="";
//    }

    //是否选择自动报货
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            getAdats(b);
    }

}
