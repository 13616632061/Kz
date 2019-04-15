package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Fenlei_Entty;
import Entty.Goods_details;
import Entty.Label_entty;
import Entty.Settlement_Entty;
import Utils.DateUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_Goods_details;
import adapters.Adapter_settlement;
import adapters.Sp2adapter;
import adapters.Sp3adapter;
import adapters.Spadapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import widget.MylistView;

/**
 * Created by admin on 2017/8/4.
 */
public class Fragment_settlement extends Fragment implements View.OnClickListener,  Adapter_settlement.Setonclick {

    public View view;
    public ListView lv_settlement;
    public List<Settlement_Entty> adats;
    public Adapter_settlement adapter;
    public Button but_reconciliation,but_settle;
    public String type="";
    public CheckBox cc_box;
    public boolean cc_b=true;
    public TextView tv_cc_unms;
    private List<Boolean> Checkeds;
    //对账
    private Button but_confirm;
    private List<Map<String,String>> listmap;
    public TextView tv_nums,tv_price;
    public MylistView mylv;
    //供应商商品详情实体类
    public List<Goods_details> goods_detailses;
    public int total;
    public double total_price,getTotal_price;
    public Adapter_Goods_details goods_Adapter;

    private TextView tv_sp1, tv_sp2, tv_sp3;
    private PopupWindow popLeft;
    private PopupWindow popRight;
    private PopupWindow popMiddle;
    private View layout_sp1;
    private ListView lv_pop;
    public Spadapter spadapter;
    public Sp2adapter sp2adapter;
    public List<Fenlei_Entty> fenlei_enttyList;
    public List<Label_entty> label_entties;
    public String sp1str;
    public String str1,sp2str,sp3str;
    public List<String> stringList;


    public CustomPopWindow mCustomPopWindow;
    public String day="-1";
    public String startTime="",endtime="";
    public List<Boolean> daylist;
    public Button but_day,but_triduum,but_week,but_month,but_lastmonth;
    public TextView tv_timestart,tv_timeend;
    public TimeSelector timeSelector;
    //判断开始时间是否选择了
    public Boolean isdiyi=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_settlement,null);
        init();
        LoAdats();
        getprovider();
        cc_box.setChecked(true);
        return view;
    }

    private void init() {

        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<6;i++){
            daylist.add(false);
        }

        label_entties=new ArrayList<>();

        stringList=new ArrayList<>();

        stringList.add("全部订单状态");
        stringList.add("待对账");
        stringList.add("待结算");
        stringList.add("已结算");


        tv_sp1 = (TextView) view.findViewById(R.id.tv_sp1);
        tv_sp2 = (TextView) view.findViewById(R.id.tv_sp2);
        tv_sp3 = (TextView) view.findViewById(R.id.tv_sp3);
        tv_sp1.setOnClickListener(this);
        tv_sp2.setOnClickListener(this);
        tv_sp3.setOnClickListener(this);

        goods_detailses=new ArrayList<>();

        goods_Adapter=new Adapter_Goods_details(getActivity(),0);

        but_confirm= (Button) view.findViewById(R.id.but_confirm);
        but_confirm.setOnClickListener(this);

        tv_cc_unms= (TextView) view.findViewById(R.id.tv_cc_unms);

        cc_box= (CheckBox) view.findViewById(R.id.cc_box);
        cc_box.setOnClickListener(this);
        but_reconciliation= (Button) view.findViewById(R.id.but_reconciliation);
        but_reconciliation.setOnClickListener(this);
        but_settle= (Button) view.findViewById(R.id.but_settle);
        but_settle.setOnClickListener(this);

        Checkeds=new ArrayList<>();
        adats=new ArrayList<>();

        listmap=new ArrayList<>();

        lv_settlement= (ListView) view.findViewById(R.id.lv_settlement);
        adapter=new Adapter_settlement(getActivity());
        adapter.Setonclick(this);

        View popwindow = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_statement, null);
        Button but_seek= (Button) popwindow.findViewById(R.id.but_seek);
        LinearLayout ll_fuhe= (LinearLayout) popwindow.findViewById(R.id.ll_fuhe);
        ll_fuhe.setVisibility(View.GONE);
        handleLogic(popwindow);

        but_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<daylist.size();i++){
                    if (daylist.get(i)){
                        if (i==0){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+":00");
                            endtime= DateUtils.getTime();
                        }else if (i==1){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endtime= DateUtils.getTime();
                        }else if (i==2){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endtime= DateUtils.getTime();
                        }else if (i==3){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endtime= DateUtils.getTime();
                        }else if (i==4){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+"-01 00:00:00");
                            endtime= DateUtils.data(tv_timeend.getText().toString()+"-01 00:00:00");
                        }else {
                            startTime= DateUtils.data(tv_timestart.getText().toString()+":00");
                            endtime= DateUtils.data(tv_timeend.getText().toString()+":00");

                        }
                    }
                }

                getreport_list("");
                mCustomPopWindow.dissmiss();
            }
        });

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();


    }
    private void LoAdats() {
        OkGo.post(SysUtils.getSellerServiceUrl("provider_account"))
                .tag(getActivity())
                .params("type",type)
                .params("group_type","account")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","结算"+s);
                        adats.clear();
                        Checkeds.clear();
                        try {
                            JSONObject jsonbject=new JSONObject(s);
                            JSONObject jo1=jsonbject.getJSONObject("response");
                            String status=jo1.getString("status");
                            JSONObject jo2=jo1.getJSONObject("data");
                            if (status.equals("200")){
                                JSONArray ja1=jo2.getJSONArray("detail");
                                for (int i=0;i<ja1.length();i++){
                                    Settlement_Entty settlement=new Settlement_Entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    settlement.setAddtime(jo3.getString("addtime"));
                                    settlement.setCashier_id(jo3.getString("cashier_id"));
                                    settlement.setContact(jo3.getString("contact"));
                                    settlement.setGoods_num(jo3.getString("goods_num"));
                                    settlement.setOrder_status(jo3.getString("order_status"));
                                    settlement.setPhone(jo3.getString("phone"));
                                    settlement.setProvider_id(jo3.getString("provider_id"));
                                    settlement.setProvider_name(jo3.getString("provider_name"));
                                    settlement.setReport_id(jo3.getString("report_id"));
                                    settlement.setReport_order(jo3.getString("report_order"));
                                    settlement.setSeller_name(jo3.getString("seller_name"));
                                    settlement.setTotal_amount(jo3.getString("total_amount"));
                                    settlement.setReconciliation(false);
                                    adats.add(settlement);
                                    Checkeds.add(true);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.setAdats(adats);
                            adapter.setCheckeds(Checkeds);
                            lv_settlement.setAdapter(adapter);
                        }
                    }
                });
    }

    //popwindow的视图
    private void handleLogic(View popwindow) {
        but_day = (RadioButton) popwindow.findViewById(R.id.but_day);
        but_day.setOnClickListener(this);
        but_triduum = (RadioButton) popwindow.findViewById(R.id.but_triduum);
        but_triduum.setOnClickListener(this);
        but_week = (RadioButton) popwindow.findViewById(R.id.but_week);
        but_week.setOnClickListener(this);
        but_month = (RadioButton) popwindow.findViewById(R.id.but_month);
        but_month.setOnClickListener(this);
        but_lastmonth = (RadioButton) popwindow.findViewById(R.id.but_lastmonth);
        but_lastmonth.setOnClickListener(this);
        tv_timestart = (TextView) popwindow.findViewById(R.id.tv_timestart);
        tv_timestart.setOnClickListener(this);
        tv_timeend = (TextView) popwindow.findViewById(R.id.tv_timeend);
        tv_timeend.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_day:
                if (!daylist.get(0)){
                    but_day.setBackgroundColor(Color.parseColor("#FF6501"));
                    for (int i=0;i<daylist.size();i++){
                        if (i==0){
                            daylist.set(i,true);
                        }else {
                            daylist.set(i,false);
                        }
                    }
                }else {
                    for (int i=0;i<daylist.size();i++){
                        daylist.set(i,false);
                    }
                    but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
                but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
                tv_timestart.setText(getTime1() + " 00:00");
                tv_timeend.setText(getTime());
                break;
            case R.id.but_triduum:
                if (!daylist.get(1)){
                    but_triduum.setBackgroundColor(Color.parseColor("#FF6501"));
                    for (int i=0;i<daylist.size();i++){
                        if (i==1){
                            daylist.set(i,true);
                        }else {
                            daylist.set(i,false);
                        }
                    }
                }else {
                    for (int i=0;i<daylist.size();i++){
                        daylist.set(i,false);
                    }
                    but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
                but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
                tv_timestart.setText(DateUtils.gettime(3));
                tv_timeend.setText(getTime());
                break;
            case R.id.but_week:
                if (!daylist.get(2)){
                    but_week.setBackgroundColor(Color.parseColor("#FF6501"));
                    for (int i=0;i<daylist.size();i++){
                        if (i==2){
                            daylist.set(i,true);
                        }else {
                            daylist.set(i,false);
                        }
                    }
                }else {
                    for (int i=0;i<daylist.size();i++){
                        daylist.set(i,false);
                    }
                    but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
                but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
                tv_timestart.setText(DateUtils.gettime(7));
                tv_timeend.setText(getTime());
                break;
            case R.id.but_month:
                if (!daylist.get(3)){
                    but_month.setBackgroundColor(Color.parseColor("#FF6501"));
                    for (int i=0;i<daylist.size();i++){
                        if (i==3){
                            daylist.set(i,true);
                        }else {
                            daylist.set(i,false);
                        }
                    }
                }else {
                    for (int i=0;i<daylist.size();i++){
                        daylist.set(i,false);
                    }
                    but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
                but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
                tv_timestart.setText(StringUtils.gettimeDataTime()+"-01");
                tv_timeend.setText(getTime());
                break;
            case R.id.but_lastmonth:
                if (!daylist.get(4)){
                    but_lastmonth.setBackgroundColor(Color.parseColor("#FF6501"));
                    for (int i=0;i<daylist.size();i++){
                        if (i==4){
                            daylist.set(i,true);
                        }else {
                            daylist.set(i,false);
                        }
                    }
                }else {
                    for (int i=0;i<daylist.size();i++){
                        daylist.set(i,false);
                    }
                    but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
                but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));

                tv_timestart.setText(DateUtils.getMonthDate(-1));
                tv_timeend.setText(DateUtils.getMonthDate(0));
                break;
            case R.id.tv_timestart:
                day="6";
                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        tv_timestart.setText(time);
                        isdiyi=true;
                    }
                }, "2015-11-22 17:34", getTime());
                timeSelector.show();
                break;
            case R.id.tv_timeend:
                if (isdiyi){
                    day="6";
                    if (tv_timestart.getText().toString()!=null){
                        timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
                            @Override
                            public void handle(String time) {
                                tv_timeend.setText(time);
                                isdiyi=false;
                            }
                        }, tv_timestart.getText().toString(), getTime());
                        timeSelector.show();
                    }
                }
                break;
            case R.id.tv_sp1:
                mCustomPopWindow.showAsDropDown(tv_sp1, 0, 20);
//                getreport_list("");
                break;
            case R.id.tv_sp2:
                if (popMiddle != null && popMiddle.isShowing()) {
                    popMiddle.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    Sp3adapter sp3adapter = new Sp3adapter(getContext());
                    sp3adapter.setAdats(stringList);
                    lv_pop.setAdapter(sp3adapter);
                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            page=1;
                            tv_sp2.setText(stringList.get(i));
                            if (i == 0) {
//                                全部
                                sp2str = "";
                                getreport_list(sp2str,"");
                            } else if (i == 1){
//                                待对账
                                sp2str = "0";
                                getreport_list(sp2str,"reconciliation");
                            } else if (i == 2){
//                                待结算
                                sp2str = "1";
                                getreport_list(sp2str,"checkout");
                            }else if (i == 3){
//                                已结算
                                sp2str = "2";
                                getreport_list(sp2str,"handle");
                            }
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("order_status",sp2str);
                            Gson gson=new Gson();
                            str1=gson.toJson(map);
//                            adats.clear();
//                            getreport_list(str1,"");
                            popMiddle.dismiss();
                        }
                    });
                    popMiddle = new PopupWindow(layout_sp1, tv_sp2.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popMiddle.setTouchable(true);// 设置popupwindow可点击  
                    popMiddle.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popMiddle.setFocusable(true);// 获取焦点  

                    popMiddle.showAsDropDown(tv_sp2);
                    popMiddle.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popMiddle.setFocusable(false);//失去焦点  
                            popMiddle.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popMiddle.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });

                }

                break;
            case R.id.tv_sp3:
                if (popLeft != null && popLeft.isShowing()) {
                    popLeft.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    sp2adapter= new Sp2adapter(getContext());
                    sp2adapter.setAdats(label_entties);
                    lv_pop.setAdapter(sp2adapter);
                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            tv_sp3.setText(label_entties.get(i).getLabel_name());
                            if (i == 0) {
                                sp3str = "";
                            }else {
                                sp3str=label_entties.get(i).getLabel_id();
                            }
//                            adats.clear();
                            getreport_list(sp3str);
//                            Map<String,String> map=new HashMap<String, String>();
//                            map.put("provider_id",sp3str);
//                            Gson gson=new Gson();
//                            str1=gson.toJson(map);
//                            adats.clear();

//                            getmarketable(sp3str);
                            popLeft.dismiss();
                        }
                    });
                    popLeft = new PopupWindow(layout_sp1, tv_sp3.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popLeft.setTouchable(true);// 设置popupwindow可点击  
                    popLeft.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popLeft.setFocusable(true);// 获取焦点  

                    popLeft.showAsDropDown(tv_sp3);
                    popLeft.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popLeft.setFocusable(false);//失去焦点  
                            popLeft.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popLeft.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });


                }
                break;

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
                adapter.setCheckeds(Checkeds);
                adapter.notifyDataSetChanged();
                break;
            case R.id.but_reconciliation:
                type="reconciliation";
                but_confirm.setText("确定对账");
                adats.clear();
                LoAdats();
                if (type.equals("reconciliation")){
                    but_reconciliation.setBackgroundColor(Color.parseColor("#f3f3f3"));
                    but_settle.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                break;
            case R.id.but_settle:
                type="checkout";
                but_confirm.setText("确定结算");
                if (type.equals("checkout")){
                    but_reconciliation.setBackgroundColor(Color.parseColor("#ffffff"));
                    but_settle.setBackgroundColor(Color.parseColor("#f3f3f3"));
                }
                adats.clear();
                LoAdats();
                break;
            case R.id.but_confirm:
                if (type.equals("checkout")){
                    listmap.clear();
                    for (int i=0;i<Checkeds.size();i++){
                        if (Checkeds.get(i)){
                            Map<String,String> map=new HashMap<>();
                            map.put("report_id",adats.get(i).getReport_id());
                            map.put("provider_id",adats.get(i).getProvider_id());
                            map.put("order_status","2");
                            listmap.add(map);
                        }
                    }
                    Gson gson=new Gson();
                    String str=gson.toJson(listmap);
                    Log.e("print","数据"+str);
                    type="checkout";
                    uporder(str);
                } else if (type.equals("reconciliation")){
                    listmap.clear();
                    for (int i=0;i<Checkeds.size();i++){
                        if (Checkeds.get(i)){
                            Map<String,String> map=new HashMap<>();
                            map.put("report_id",adats.get(i).getReport_id());
                            map.put("provider_id",adats.get(i).getProvider_id());
                            map.put("order_status","1");
                            listmap.add(map);
                        }
                    }
                    Gson gson=new Gson();
                    String str=gson.toJson(listmap);
                    Log.e("print","数据"+str);
                    type="reconciliation";
                    uporder(str);
                }else if (type.equals("")){
                    Toast.makeText(getActivity(),"请选择对账还是结账",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;//0.0-1.0  
        getActivity().getWindow().setAttributes(lp);
    }

    //筛选
    public void getreport_list(String str,String type){
        Log.e("print","数据为"+str);
        OkGo.post(SysUtils.getSellerServiceUrl("provider_account"))
                .tag(getActivity())
                .params("group_type","account")
                .params("type",type)
                .params("startTime",startTime)
                .params("endTime",endtime)
                .params("provider_id",str)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print","传过去的数是"+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                       Log.e("print","结算的筛选结果"+s);
                        try {
                            JSONObject jsonbject=new JSONObject(s);
                            JSONObject jo1=jsonbject.getJSONObject("response");
                            String status=jo1.getString("status");
                            JSONObject jo2=jo1.getJSONObject("data");
                            adats.clear();
                            Checkeds.clear();
                            if (status.equals("200")){
                                JSONArray ja1=jo2.getJSONArray("detail");
                                for (int i=0;i<ja1.length();i++){
                                    Settlement_Entty settlement=new Settlement_Entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    settlement.setAddtime(jo3.getString("addtime"));
                                    settlement.setCashier_id(jo3.getString("cashier_id"));
                                    settlement.setContact(jo3.getString("contact"));
                                    settlement.setGoods_num(jo3.getString("goods_num"));
                                    settlement.setOrder_status(jo3.getString("order_status"));
                                    settlement.setPhone(jo3.getString("phone"));
                                    settlement.setProvider_id(jo3.getString("provider_id"));
                                    settlement.setProvider_name(jo3.getString("provider_name"));
                                    settlement.setReport_id(jo3.getString("report_id"));
                                    settlement.setReport_order(jo3.getString("report_order"));
                                    settlement.setSeller_name(jo3.getString("seller_name"));
                                    settlement.setTotal_amount(jo3.getString("total_amount"));
                                    settlement.setReconciliation(false);
                                    adats.add(settlement);
                                    Checkeds.add(true);
                                }
                            }else {
                                adats.clear();
                            }
                            adapter.setAdats(adats);
                            adapter.setCheckeds(Checkeds);
                            lv_settlement.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //筛选
    public void getreport_list(String str){
        adats.clear();
        Log.e("print","数据为"+tv_timestart.getText().toString()+"   "+startTime+"   "+endtime);
        OkGo.post(SysUtils.getSellerServiceUrl("provider_account"))
                .tag(getActivity())
                .params("group_type","account")
                .params("type","reconciliation")
                .params("startTime",startTime)
                .params("endTime",endtime)
                .params("provider_id",str)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print","传过去的数是"+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","结算的筛选结果"+s);
                        try {
                            JSONObject jsonbject=new JSONObject(s);
                            JSONObject jo1=jsonbject.getJSONObject("response");
                            String status=jo1.getString("status");
                            JSONObject jo2=jo1.getJSONObject("data");
                            adats.clear();
                            Checkeds.clear();
                            if (status.equals("200")){
                                JSONArray ja1=jo2.getJSONArray("detail");
                                for (int i=0;i<ja1.length();i++){
                                    Settlement_Entty settlement=new Settlement_Entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    settlement.setAddtime(jo3.getString("addtime"));
                                    settlement.setCashier_id(jo3.getString("cashier_id"));
                                    settlement.setContact(jo3.getString("contact"));
                                    settlement.setGoods_num(jo3.getString("goods_num"));
                                    settlement.setOrder_status(jo3.getString("order_status"));
                                    settlement.setPhone(jo3.getString("phone"));
                                    settlement.setProvider_id(jo3.getString("provider_id"));
                                    settlement.setProvider_name(jo3.getString("provider_name"));
                                    settlement.setReport_id(jo3.getString("report_id"));
                                    settlement.setReport_order(jo3.getString("report_order"));
                                    settlement.setSeller_name(jo3.getString("seller_name"));
                                    settlement.setTotal_amount(jo3.getString("total_amount"));
                                    settlement.setReconciliation(false);
                                    adats.add(settlement);
                                    Checkeds.add(true);
                                }
                            }else {
                                adats.clear();
                            }
                            adapter.setAdats(adats);
                            adapter.setCheckeds(Checkeds);
                            lv_settlement.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }


    //获取全部供应商
    public void getprovider(){
        OkGo.post(SysUtils.getGoodsServiceUrl("provider_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","数据为"+s);
                        try {
                            label_entties.clear();
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Label_entty label_entty=new Label_entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    label_entty.setLabel_name(jo2.getString("provider_name"));
                                    label_entty.setLabel_id(jo2.getString("provider_id"));
                                    label_entties.add(label_entty);
                                }
                                Label_entty label_entty=new Label_entty();
                                label_entty.setLabel_name("全部供应商");
                                label_entty.setLabel_id("");
                                label_entties.add(0,label_entty);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void SetonChecked(int i) {
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
        adapter.setCheckeds(Checkeds);
        adapter.notifyDataSetChanged();
    }

    //详情
    @Override
    public void Setonclick(int i) {
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getContext()).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.goods_details);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        Button but_acceptance= (Button) window.findViewById(R.id.but_acceptance);
        but_acceptance.setText("全部审核");
        tv_nums= (TextView) window.findViewById(R.id.tv_nums);
        tv_price= (TextView) window.findViewById(R.id.tv_price);
        mylv= (MylistView) window.findViewById(R.id.lv);

        if (adats.size()>0){
            getprovider_goods(adats.get(i).getProvider_id(),adats.get(i).getReport_id());
            Log.e("点击的itme",""+adats.get(i).getProvider_id()+"   "+adats.get(i).getReport_id());
        }

    }


    public void uporder(String str){
        OkGo.post(SysUtils.getSellerServiceUrl("change_statsu"))
                .tag(getActivity())
                .params("map",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print",""+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                LoAdats();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取时间 yyyy-MM-dd  HH:mm
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public String getTime1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }


    public void getprovider_goods(String id,String str){
        OkGo.post(SysUtils.getSellerServiceUrl("provider_goods"))
                .tag(getActivity())
                .params("provider_id",id)
                .params("report_id",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","商品详情是"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                goods_detailses.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Goods_details goods_details=new Goods_details();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    goods_details.setBncode(jo2.getString("bncode"));
                                    goods_details.setGoods_name(jo2.getString("goods_name"));
                                    goods_details.setGoods_status(jo2.getString("goods_status"));
                                    goods_details.setMenu(jo2.getString("menu"));
                                    goods_details.setPrice(jo2.getString("price"));
                                    goods_details.setNums(jo2.getString("nums"));
                                    goods_details.setId(jo2.getString("id"));
                                    total+=Integer.parseInt(jo2.getString("nums"));
                                    getTotal_price= TlossUtils.mul(Double.parseDouble(total+""),Double.parseDouble(jo2.getString("price")));
                                    total_price= TlossUtils.add(total_price,getTotal_price);
                                    goods_detailses.add(goods_details);
                                }
                            }else {
                                goods_detailses.clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_nums.setText(total+"");
//                            tv_price.setText(adats.get(i).getTotal_amount());
                            goods_Adapter.setAdats(goods_detailses);
                            mylv.setAdapter(goods_Adapter);
                            goods_Adapter.notifyDataSetChanged();
                        }

                    }
                });



    }

}
