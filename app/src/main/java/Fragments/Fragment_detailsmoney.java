package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import Entty.Group_Entty;
import Entty.Label_entty;
import Entty.Money_Entty;
import Entty.Provider_Entty;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_Goods_details;
import adapters.Adapter_details;
import adapters.Adapter_providers;
import adapters.Expandab_Adapter;
import adapters.Sp2adapter;
import adapters.Sp3adapter;
import adapters.Spadapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import widget.MylistView;

/**
 * Created by admin on 2017/6/26.
 *  报货详情
 */
public class Fragment_detailsmoney extends Fragment implements View.OnClickListener, Adapter_details.StrOnoclick, Adapter_providers.Setonclickitme, Adapter_Goods_details.SetOnclickbut {

    public View view;
    public ListView exlv;
    public List<Money_Entty> adats;
    public Adapter_details adapter;
    public Button but_last,but_next;
    public int page=1;
    public TextView tv_page;
    public boolean paging1=false,paging2=false;
    public int nums=0;
    public ExpandableListView lv_ex;

    public List<Group_Entty> datas;
    public Expandab_Adapter ex_adapter;

    public List<Provider_Entty> provider_enttyList;
    public Adapter_providers adapters;
    public Dialog dialog;
    public Adapter_Goods_details goods_Adapter;
    public MylistView mylv;

    public TextView tv_nums,tv_price;

    public int total;
    public double total_price,getTotal_price;
    public List<Map<String,String>> listmap=new ArrayList<>();


    //供应商商品详情实体类
    public List<Goods_details> goods_detailses;

    //记录点击单号的位置
    public int amount=0;
    //记录点击供应商的位置
    public int distributor_id=0;
    //减少数量得计算
    public double money;

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
    public Button but_details;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.moneydetails_layout,null);
        init();
        getmember();
        getprovider();
        LoadAdat();
        return view;
    }

    private void init() {

        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<6;i++){
            daylist.add(false);
        }

        stringList=new ArrayList<>();
        stringList.add("全部订单");
        stringList.add("未验收");
        stringList.add("与验收");


        fenlei_enttyList=new ArrayList<>();
        label_entties=new ArrayList<>();

        tv_sp1 = (TextView) view.findViewById(R.id.tv_sp1);
        tv_sp2 = (TextView) view.findViewById(R.id.tv_sp2);
        tv_sp3 = (TextView) view.findViewById(R.id.tv_sp3);
        tv_sp1.setOnClickListener(this);
        tv_sp2.setOnClickListener(this);
        tv_sp3.setOnClickListener(this);

        but_details= (Button) view.findViewById(R.id.but_details);
        but_details.setOnClickListener(this);

        goods_detailses=new ArrayList<>();
        goods_Adapter=new Adapter_Goods_details(getActivity(),0);
        goods_Adapter.SetOnclickbut(this);


        provider_enttyList=new ArrayList<>();
        adats=new ArrayList<>();
        but_last= (Button) view.findViewById(R.id.but_last);
        but_next= (Button) view.findViewById(R.id.but_next);
        but_last.setOnClickListener(this);
        but_next.setOnClickListener(this);
        tv_page= (TextView) view.findViewById(R.id.tv_page);
        exlv= (ListView) view.findViewById(R.id.exlv);
        adapter=new Adapter_details(getActivity(),0);
        adapter.StrOnoclick(this);

        datas=new ArrayList<>();

        adapters=new Adapter_providers(getActivity());
        adapters.Setonclickitme(this);

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
                adats.clear();
                getreport_list(str1);
                mCustomPopWindow.dissmiss();
            }
        });

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();


//        lv_ex= (ExpandableListView) view.findViewById(R.id.lv_ex);
//        ex_adapter=new Expandab_Adapter(getContext());
//        ex_adapter.SetListenerOnclick(this);
//        Loadats();

    }
    private void LoadAdat() {
        OkGo.post(SysUtils.getSellerServiceUrl("report_list"))
                .tag(getActivity())
                .params("page",page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","详情"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONObject jo3=jo1.getJSONObject("data");
                                nums=jo3.getInt("nums");
                                JSONArray ja1=jo3.getJSONArray("detail");
                                adats.clear();
                                for (int k=0;k<ja1.length();k++){
                                    Money_Entty money=new Money_Entty();
                                    JSONObject jo2=ja1.getJSONObject(k);
                                    money.setReport_id(jo2.getString("report_id"));
                                    money.setSeller_name(jo2.getString("seller_name"));
                                    money.setAddtime(jo2.getString("addtime"));
//                                    money.setCashier(jo2.getString("cashier"));

                                    money.setNums(jo2.getString("nums"));
                                    money.setTotal_amount(jo2.getString("total_amount"));
                                    money.setStatus(jo2.getString("status"));
                                    adats.add(money);
                                }
                                adapter.setAdats(adats);
                                exlv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                if (nums % 10 == 0) {
                                    tv_page.setText((page ) + "/" + (nums / 10));
                                } else {
                                    tv_page.setText((page ) + "/" + (nums / 10 + 1));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            case R.id.but_next:
                if (nums % 10 == 0) {
                    tv_page.setText(page + "/" + (nums / 10));
                    if (page < (nums / 10)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
//                            Loadats();
                            LoadAdat();
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (nums / 10 + 1));
                    if (page < (nums / 10 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
//                            Loadats();
                            LoadAdat();
                            paging1 = false;
                        }
                    }
                }
                break;
            case R.id.but_last:
                if (page > 1) {
                    if (nums % 10 == 0) {
                        page--;
                        tv_page.setText(page + "/" + (nums/ 10));
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
//                            Loadats();
                            LoadAdat();
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (nums / 10 + 1));
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
//                            Loadats();
                            LoadAdat();
                            paging2 = false;
                        }
                    }
                }
                break;
            case R.id.tv_sp1:
                if (popRight != null && popRight.isShowing()) {
                    popRight.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    spadapter = new Spadapter(getContext());
                    spadapter.setAdats(fenlei_enttyList);
                    lv_pop.setAdapter(spadapter);
                    spadapter.setOneidtextonclick(new Spadapter.Oneidtextonclick() {
                        @Override
                        public void itmeeidtonclick(int i, String type) {
                            tv_sp1.setText(fenlei_enttyList.get(i).getName());
                            page=1;
                            if (i == 0) {
                                sp1str = "";
                            } else {
                                sp1str = fenlei_enttyList.get(i).getTag_id() + "";
                            }
//                            commodities.clear();
//                            getTag_name(sp1str, sp2str, sp3str);

                            Map<String,String> map=new HashMap<String, String>();
                            map.put("cashier_id",sp1str);
                            Gson gson=new Gson();
                            str1=gson.toJson(map);
                            getreport_list(str1);
                            popRight.dismiss();
                        }
                    });
//                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        }
//                    });
                    popRight = new PopupWindow(layout_sp1, tv_sp1.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popRight.setTouchable(true);// 设置popupwindow可点击  
                    popRight.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popRight.setFocusable(true);// 获取焦点  

                    popRight.showAsDropDown(tv_sp1);
                    popRight.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popRight.setFocusable(false);//失去焦点  
                            popRight.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popRight.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });
                }


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
                            page=1;
                            tv_sp2.setText(stringList.get(i));
                            if (i == 0) {
                                sp2str = "";
                            } else if (i == 1){
                                sp2str = "0";
                            } else if (i == 2){
                                sp2str = "1";
                            }
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("status",sp2str);
                            Gson gson=new Gson();
                            str1=gson.toJson(map);
                            adats.clear();
                            getreport_list(str1);
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
                            page=1;
                            tv_sp3.setText(label_entties.get(i).getLabel_name());
                            if (i == 0) {
                                sp3str = "";
                            }else {
                                sp3str=label_entties.get(i).getLabel_id();
                            }
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("provider_id",sp3str);
                            Gson gson=new Gson();
                            str1=gson.toJson(map);
                            adats.clear();
                            getreport_list(str1);
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
            case R.id.but_details:
                mCustomPopWindow.showAsDropDown(but_details, 0, 20);
                break;
        }
    }


    //筛选
    public void getreport_list(String str){
        Log.e("print","数据为"+str);
        OkGo.post(SysUtils.getSellerServiceUrl("report_list"))
                .tag(getActivity())
                .params("filter",str)
                .params("startTime",startTime)
                .params("endTime",endtime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONObject jo3=jo1.getJSONObject("data");
                                nums=jo3.getInt("nums");
                                JSONArray ja1=jo3.getJSONArray("detail");
                                adats.clear();
                                for (int k=0;k<ja1.length();k++){
                                    Money_Entty money=new Money_Entty();
                                    JSONObject jo2=ja1.getJSONObject(k);
                                    money.setReport_id(jo2.getString("report_id"));
                                    money.setSeller_name(jo2.getString("seller_name"));
                                    money.setAddtime(jo2.getString("addtime"));
//                                    money.setCashier(jo2.getString("cashier"));
                                    money.setNums(jo2.getString("nums"));
                                    money.setTotal_amount(jo2.getString("total_amount"));
                                    money.setStatus(jo2.getString("status"));
                                    adats.add(money);
                                }
                                adapter.setAdats(adats);
                                exlv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                if (nums % 10 == 0) {
                                    tv_page.setText((page ) + "/" + (nums / 10));
                                } else {
                                    tv_page.setText((page ) + "/" + (nums / 10 + 1));
                                }
                            }else {
                                adats.clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //操作员
    public void getmember(){
        OkGo.post(SysUtils.getSellerServiceUrl("employee_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","接口名字"+s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject jo1 = jsonobject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Fenlei_Entty staff = new Fenlei_Entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    staff.setName(jo2.getString("login_name"));
                                    staff.setTag_id(Integer.parseInt(jo2.getString("work_id")));
                                    fenlei_enttyList.add(staff);
                                }

                                Fenlei_Entty staff = new Fenlei_Entty();
                                staff.setName("全部操作员");
                                staff.setTag_id(1);
                                fenlei_enttyList.set(0,staff);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;//0.0-1.0  
        getActivity().getWindow().setAttributes(lp);
    }

    //点击详情
    @Override
    public void setbutonclick(final int i) {
        amount=i;
        if (adats.size()>0){
//            adats.get(i).getReport_id();
//            getreport_provider_list(adats.get(i).getReport_id());
            final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getContext()).create();
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.goods_details);
            Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
            Button but_acceptance= (Button) window.findViewById(R.id.but_acceptance);
            but_acceptance.setText("全部验收");
            tv_nums= (TextView) window.findViewById(R.id.tv_nums);
            tv_price= (TextView) window.findViewById(R.id.tv_price);
            mylv= (MylistView) window.findViewById(R.id.lv);
            getprovider_goods(adats.get(i).getReport_id());
            but_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoadAdat();
                    dialog.dismiss();
                }
            });
            but_acceptance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id="";
                    listmap.clear();
                    for (int j=0;j<goods_detailses.size();j++){
                        if (!goods_detailses.get(j).getGoods_status().equals("1")){
                            id+=goods_detailses.get(j).getId()+",";
                            Map<String,String> map=new HashMap<>();
                            map.put("nums",goods_detailses.get(j).getNums());
                            map.put("id",goods_detailses.get(j).getId());
                            map.put("goods_id",goods_detailses.get(j).getGoods_id());
                            listmap.add(map);
                        }
                    }
                    String ids;
                    if (id.length()>0){
                        ids=id.substring(0,id.length()-1);
                    }else {
                        ids="";
                    }
                    Gson gson=new Gson();
                    String before=gson.toJson(listmap);

                    Log.d("print验收的数据",before);
                    getcheck_goods(ids,"status",adats.get(amount).getReport_id(),"","",amount,before);
                    LoadAdat();
                }
            });


        }else {
            Toast.makeText(getActivity(),"请退出该界面在进入",Toast.LENGTH_SHORT).show();
        }



    }


    public void getreport_provider_list(String report_id){
        OkGo.post(SysUtils.getSellerServiceUrl("report_provider_list"))
                .tag(getActivity())
                .params("report_id",report_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","供应商为"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                provider_enttyList.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Provider_Entty provider_entty=new Provider_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    String provider_name=jo2.getString("provider_name");
                                    String provider_id=jo2.getString("provider_id");
                                    provider_entty.setProvider_name(provider_name);
                                    provider_entty.setProvider_id(provider_id);
                                    provider_enttyList.add(provider_entty);
                                }
                                dialog= new Dialog(getActivity());
                                dialog.setTitle("供应商");
                                dialog.show();
                                Window window = dialog.getWindow();
                                window.setContentView(R.layout.provider_listview);
                                ListView lv= (ListView) window.findViewById(R.id.lv);
                                adapters.setAdats(provider_enttyList);
                                lv.setAdapter(adapters);
                                adapters.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //点击供应商
    @Override
    public void setOnclick(final int i) {
//        dialog.dismiss();
//        Dialog dialog1= new Dialog(getActivity());
//        dialog1.setTitle("商品详情");
//        dialog1.show();
//        Window window = dialog.getWindow();
//        window.setContentView(R.layout.goods_details);
        distributor_id=i;
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
//        getprovider_goods(provider_enttyList.get(i).getProvider_id());
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        but_acceptance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id="";
                listmap.clear();
                for (int j=0;j<goods_detailses.size();j++){
                    if (!goods_detailses.get(j).getGoods_status().equals("1")){
                        id+=goods_detailses.get(j).getId()+",";
                        Map<String,String> map=new HashMap<>();
                        map.put("nums",goods_detailses.get(j).getNums());
                        map.put("id",goods_detailses.get(j).getId());
                        map.put("goods_id",goods_detailses.get(j).getGoods_id());
                        listmap.add(map);
                    }
                }
                String ids=id.substring(0,id.length()-1);
                Gson gson=new Gson();
                String before=gson.toJson(listmap);
                Log.d("print验收的数据","id="+ids  +"type="+"status" +before);
                getcheck_goods(ids,"status",adats.get(amount).getReport_id(),"","",amount,before);
            }
        });


    }


    public void getprovider_goods(String id){
        total=0;
        total_price=0;
        Log.e("print","商品详情是444"+id);
        OkGo.post(SysUtils.getSellerServiceUrl("provider_goods"))
                .tag(getActivity())
//                .params("provider_id",id)
                .params("report_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.e("print商品详情是",s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            goods_detailses.clear();
                            getTotal_price=0;
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    Goods_details goods_details=new Goods_details();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    goods_details.setBncode(jo2.getString("bncode"));
                                    goods_details.setGoods_name(jo2.getString("goods_name"));
                                    goods_details.setGoods_status(jo2.getString("goods_status"));
                                    goods_details.setMenu(jo2.getString("menu"));
                                    goods_details.setPrice(jo2.getString("price"));
                                    goods_details.setBefore_nums(jo2.getString("berfore_num"));
                                    goods_details.setGoods_id(jo2.getString("goods_id"));
                                    if (!jo2.getString("goods_status").equals("1")){
                                        goods_details.setNums(jo2.getString("berfore_num"));
                                    }else {
                                        goods_details.setNums(jo2.getString("nums"));
                                    }
                                    goods_details.setId(jo2.getString("id"));
                                    total+=Integer.parseInt(jo2.getString("nums"));
                                    getTotal_price= TlossUtils.add(getTotal_price,TlossUtils.mul(Double.parseDouble(jo2.getString("nums")),Double.parseDouble(jo2.getString("price"))));
                                    total_price= TlossUtils.add(total_price,getTotal_price);
                                    goods_detailses.add(goods_details);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_nums.setText(total+"");
                            tv_price.setText(getTotal_price+"");
                            goods_Adapter.setAdats(goods_detailses);
                            mylv.setAdapter(goods_Adapter);
                            goods_Adapter.notifyDataSetChanged();
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

    //验收
    @Override
    public void Setonclick(final int i) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("补充库存");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_supplement);
        TextView tv_t1= (TextView) window.findViewById(R.id.tv_t1);
        final EditText ed_add= (EditText) window.findViewById(R.id.ed_add);
        Button but_add= (Button) window.findViewById(R.id.but_add);
        tv_t1.setText("到货数量");
        ed_add.setHint("请输入到货数量");
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_add.getText().toString().isEmpty()){
                    if (Integer.parseInt(ed_add.getText().toString())<Integer.parseInt(goods_detailses.get(i).getNums())){
                        double nums=Integer.parseInt(goods_detailses.get(i).getNums())-Integer.parseInt(ed_add.getText().toString());
                        money= TlossUtils.sub(Double.parseDouble(adats.get(amount).getTotal_amount()), TlossUtils.mul(nums,Double.parseDouble(goods_detailses.get(i).getPrice())));

                        goods_detailses.get(i).setNums(ed_add.getText().toString());

                        listmap.clear();
                        Map<String,String> map=new HashMap<>();
                        map.put("nums",goods_detailses.get(i).getNums());
                        map.put("id",goods_detailses.get(i).getId());
                        map.put("goods_id",goods_detailses.get(i).getGoods_id());
                        listmap.add(map);
                        Gson gson=new Gson();
                        String before=gson.toJson(listmap);
                        Log.d("print验收的数据",before);
                        getcheck_goods(goods_detailses.get(i).getId(),"status",adats.get(amount).getReport_id(),ed_add.getText().toString(),money+"",amount,before);
                        dialog.dismiss();
                    }else {
                        double nums=Integer.parseInt(goods_detailses.get(i).getNums())-Integer.parseInt(ed_add.getText().toString());
                        money= TlossUtils.sub(Double.parseDouble(adats.get(amount).getTotal_amount()), TlossUtils.mul(Double.parseDouble(goods_detailses.get(i).getPrice()),nums));

                        goods_detailses.get(i).setBefore_nums(ed_add.getText().toString());

                        listmap.clear();
                        Map<String,String> map=new HashMap<>();
                        map.put("nums",goods_detailses.get(i).getNums());
                        map.put("id",goods_detailses.get(i).getId());
                        map.put("goods_id",goods_detailses.get(i).getGoods_id());
                        listmap.add(map);
                        Gson gson=new Gson();
                        String before=gson.toJson(listmap);
                        Log.d("print验收的数据",before);
                        getcheck_goods(goods_detailses.get(i).getId(),"status",adats.get(amount).getReport_id(),ed_add.getText().toString(),money+"",amount,before);
                        dialog.dismiss();
                    }
                }else {
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

    //验收商品
    public void getcheck_goods(String id, final String str, String s, String nums, String amount, final int i,String goods_detail){
        OkGo.post(SysUtils.getSellerServiceUrl("check_goods"))
                .tag(getActivity())
                .params("id",id)
                .params("type",str)
                .params("nums",nums)
                .params("goods_detail",goods_detail)
                .params("total_amount",amount)
                .params("report_id",s)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","验收的结果是"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(getActivity(),"验收成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"验收失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            getprovider_goods(adats.get(i).getReport_id());
//                            getprovider_goods(provider_enttyList.get(distributor_id).getProvider_id());
                        }
                    }
                });

    }

//    private void Loadats(){
//        OkGo.post(SysUtils.getSellerServiceUrl("report_list"))
//                .tag(getActivity())
//                .params("page",page)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("print","报货的数据为"+s);
//                        try {
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jo1=jsonObject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                JSONObject jo2=jo1.getJSONObject("data");
//                                nums=Integer.parseInt(jo2.getString("number"));
//                                JSONArray ja1=jo2.getJSONArray("info");
//                                datas.clear();
//                                for (int i=0;i<ja1.length();i++){
//                                    Group_Entty group_entty=new Group_Entty();
//                                    JSONObject jo3=ja1.getJSONObject(i);
//                                    group_entty.setSeller_name(jo3.getString("seller_name"));
//                                    group_entty.setAddtime(jo3.getString("addtime"));
//                                    group_entty.setCashier(jo3.getString("cashier"));
//                                    group_entty.setTotal_amount(jo3.getString("total_amount"));
//                                    group_entty.setReport_id(jo3.getString("report_id"));
//                                    JSONArray ja2=jo3.getJSONArray("child");
//                                    List<Group_Entty.ChildBean> childBeen=new ArrayList<Group_Entty.ChildBean>();
//                                    for (int j=0;j<ja2.length();j++){
//                                        Group_Entty.ChildBean childBean=new Group_Entty.ChildBean();
//                                        JSONObject jo4=ja2.getJSONObject(j);
//                                        childBean.setGoods_name(jo4.getString("goods_name"));
//                                        childBean.setNums(Double.parseDouble(jo4.getString("price"))+"");
//                                        childBean.setPrice(jo4.getString("nums"));
//                                        childBean.setBncode(jo4.getString("bncode"));
//                                        childBean.setGoods_id(jo4.getString("goods_id"));
//                                        childBean.setGoods_status(jo4.getString("goods_status"));
//                                        childBean.setId(jo4.getString("id"));
//                                        childBeen.add(childBean);
//                                    }
//                                    group_entty.setChild(childBeen);
//                                    datas.add(group_entty);
//                                }
//                                Log.e("print","数据为多少"+datas);
//                                ex_adapter.setDatas(datas);
//                                lv_ex.setAdapter(ex_adapter);
//                                if (nums % 10 == 0) {
//                                    tv_page.setText((page ) + "/" + (nums / 10));
//                                } else {
//                                    tv_page.setText((page ) + "/" + (nums / 10 + 1));
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//    }

//    @Override
//    public void SetOnclickChild(int i, int i1) {
//        getaddstore((int)Double.parseDouble(StringUtils.stringpointtwo(datas.get(i).getChild().get(i1).getNums())),datas.get(i).getChild().get(i1).getGoods_id(),datas.get(i).getChild().get(i1).getId());
//    }

//    public void getaddstore(final int i, final String goods_id, final String id){
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.setTitle("补充库存");
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setContentView(R.layout.dialog_supplement);
//        Button but_add = (Button) window.findViewById(R.id.but_add);
//        final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
//        ed_add.setHint(i+"");
//        but_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!ed_add.getText().toString().equals("")){
//                    setaddStor(ed_add.getText().toString(),goods_id,id);
//                    if (Integer.parseInt(ed_add.getText().toString())<i||Integer.parseInt(ed_add.getText().toString())>i){
//                        dialog.dismiss();
//                        ex_adapter.notifyDataSetChanged();
//                    }else {
//                        setrecord(ed_add.getText().toString(),""+(i-Integer.parseInt(ed_add.getText().toString())),goods_id,id);
//                        dialog.dismiss();
//                        ex_adapter.notifyDataSetChanged();
//                    }
//                }else {
//                    setaddStor(ed_add.getHint().toString(),goods_id,id);
//                    dialog.dismiss();
//                }
//            }
//        });
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });
//
//
//
//    }

//    setrecord
//    public void setaddStor(String i,String goods_id,String id){
//        OkGo.post(SysUtils.getGoodsServiceUrl("edit_store"))
//                .tag(getActivity())
//                .params("store",i)
//                .params("id",id)
//                .params("type","report")
//                .params("seller_token", SharedUtil.getString("seller_token"))
//                .params("goods_id",goods_id)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("print","修改库存成功"+s);
//                        try {
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jo1=jsonObject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                Toast.makeText(getActivity(),"补充库存成功",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }

//    public void setrecord(String i,String owe_nums,String goods_id,String id){
//        OkGo.post(SysUtils.getGoodsServiceUrl("edit_store"))
//                .tag(getActivity())
//                .params("store",i)
//                .params("id",id)
//                .params("owe_nums",owe_nums)
//                .params("type","report")
//                .params("seller_token", SharedUtil.getString("seller_token"))
//                .params("goods_id",goods_id)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("print","修改库存成功"+s);
//                        try {
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jo1=jsonObject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                Toast.makeText(getActivity(),"补充库存成功",Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }


}
