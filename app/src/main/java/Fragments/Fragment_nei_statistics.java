package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entty.Fenlei_Entty;
import Entty.GridView_xuangzhong;
import Entty.Label_entty;
import Entty.Market_entty;
import Entty.Specification_Entty;
import Utils.DateUtils;
import Utils.SharedUtil;
import Utils.SortUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_specification;
import adapters.Sp2adapter;
import adapters.Sp3adapter;
import adapters.Spadapter;
import adapters.Statistics_adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;

/**
 * Created by admin on 2017/5/11.
 */
public class Fragment_nei_statistics extends Fragment implements View.OnClickListener {
    public View view;

    public ListView lv;
    private EditText ed_seek;

    public List<Fenlei_Entty> listFenlei;

    public TextView tv_volume, tv_lable, tv_classify;
    public List<Label_entty> listlabel;
    public Label_entty label_entty;

    public String tag_name="";
    public String sort="desc";

    //排序的判断
    public boolean isrank=false;

    //    popMiddle中间的popLeft左边
    public PopupWindow popMiddle, popLeft, popRight;
    public View layout_tv_volume, layout_tv_lable, layout_tv_classify;
    public ListView lv_pop;
    public Sp3adapter sp2adapter;
    public Sp2adapter sp3adapter;
    public Spadapter sp1adapter;
    public List<String> sp2adats;

    public ArrayList<Market_entty> adats;
    public Statistics_adapter adapter;

    public CustomPopWindow mCustomPopWindow;
    //    时间选择
    public RadioButton but_day, but_triduum, but_week, but_month, but_lastmonth;
    public TextView tv_timestart,tv_timeend;
    public List<Boolean> daylist;
    //    时间选择器
    public TimeSelector timeSelector;
    public boolean randomtime=false;
    //判断是否选择了开始时间
    public boolean isdiyi=false;

    public String beginTime="",endTime="";

    public TextView tv_rank,tv_total,tv_receipts,tv_return,tv_filtrate;
    public boolean sort1=false,sort2=false,sort3=false;
    Button but_classification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_nei_fragment, null);
        init();
        LOadadats();
        return view;
    }

    private void init() {

        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<5;i++){
            daylist.add(false);
        }

        View popwindow = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_statement, null);
        Button but_seek= (Button) popwindow.findViewById(R.id.but_seek);
        Button but_reset= (Button) popwindow.findViewById(R.id.but_reset);
        LinearLayout ll_fuhe= (LinearLayout) popwindow.findViewById(R.id.ll_fuhe);
        ll_fuhe.setVisibility(View.GONE);
        handleLogic(popwindow);      //创建并显示popWindow


        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();
        but_seek.setOnClickListener(this);
        but_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < daylist.size(); i++) {
                    daylist.set(i, false);
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
            }
        });

        adats=new ArrayList<>();
        adapter=new Statistics_adapter(getContext());

        listFenlei = new ArrayList<>();
        listlabel = new ArrayList<>();
        sp2adats = new ArrayList<>();

        sp2adats.add("倒序");
        sp2adats.add("顺序");

        lv= (ListView) view.findViewById(R.id.lv);
        lv.setEmptyView(view.findViewById(R.id.tv_tishi));
        ed_seek= (EditText) view.findViewById(R.id.ed_seek);

        tv_volume = (TextView) view.findViewById(R.id.tv_volume);
        tv_lable = (TextView) view.findViewById(R.id.tv_lable);
        tv_classify = (TextView) view.findViewById(R.id.tv_classify);
        tv_volume.setOnClickListener(this);
        tv_lable.setOnClickListener(this);
        tv_classify.setOnClickListener(this);

        ed_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    ed_seek.getText().toString();
                    getseek(ed_seek.getText().toString());
                }
                return true;
            }
        });
        //点击数量排序
        tv_rank= (TextView) view.findViewById(R.id.tv_rank);
        tv_rank.setOnClickListener(this);
        tv_total= (TextView) view.findViewById(R.id.tv_total);
        tv_total.setOnClickListener(this);
        tv_receipts= (TextView) view.findViewById(R.id.tv_receipts);
        tv_receipts.setOnClickListener(this);
        tv_return= (TextView) view.findViewById(R.id.tv_return);
        tv_return.setOnClickListener(this);
        tv_filtrate= (TextView) view.findViewById(R.id.tv_filtrate);
        tv_filtrate.setOnClickListener(this);
        but_classification= (Button) view.findViewById(R.id.but_classification);
        but_classification.setOnClickListener(this);
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

    private void LOadadats() {
        getLabel();
        getclassify();
        getadats(sort,tag_name,beginTime,endTime);
//        getsalesVolumeByNav(beginTime,endTime);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_day:
                if (!daylist.get(0)){
                    but_day.setTextColor(Color.parseColor("#FF6501"));
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
                    but_day.setTextColor(Color.parseColor("#ff757575"));
                }
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(getTime1() + " 00:00:00");
                tv_timeend.setText(getTime()+":00");
                break;
            case R.id.but_triduum:
                if (!daylist.get(1)){
                    but_triduum.setTextColor(Color.parseColor("#FF6501"));
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
                    but_triduum.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timeend.setText(DateUtils.gettime(-3)+" 00:00:00");
                tv_timestart.setText(getTime()+":00");
                break;
            case R.id.but_week:
                if (!daylist.get(2)){
                    but_week.setTextColor(Color.parseColor("#FF6501"));
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
                    but_week.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timeend.setText(DateUtils.gettime(-7)+" 00:00:00");
                tv_timestart.setText(getTime()+":00");
                break;
            case R.id.but_month:
                if (!daylist.get(3)){
                    but_month.setTextColor(Color.parseColor("#FF6501"));
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
                    but_month.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(StringUtils.gettimeDataTime()+"-01"+" 00:00:00");
                tv_timeend.setText(getTime()+":00");
                break;
            case R.id.but_lastmonth:
                if (!daylist.get(4)){
                    but_lastmonth.setTextColor(Color.parseColor("#FF6501"));
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
                    but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));

                tv_timestart.setText(DateUtils.getMonthDate(-1)+"-01 00:00:00");
                tv_timeend.setText(DateUtils.getMonthDate(0)+"-01 00:00:00");
                break;
            case R.id.tv_timestart:
                DateTimeUtils.runTime(getActivity(),tv_timestart);
                randomtime=true;
                isdiyi=true;
//                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_timestart.setText(time);
//                        randomtime=true;
//                        isdiyi=true;
//                    }
//                }, "2015-11-22 17:34", getTime());
//                timeSelector.show();
                break;
            case R.id.tv_timeend:
                if (isdiyi){
                    DateTimeUtils.runTime(getActivity(),tv_timeend);
                    isdiyi=false;
                    randomtime=true;
//                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_timeend.setText(time);
//                        isdiyi=false;
//                        randomtime=true;
//                    }
//                }, tv_timestart.getText().toString(), getTime());
//                timeSelector.show();
                }
                break;
            case R.id.tv_volume:
                layout_tv_volume = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_volume.findViewById(R.id.lv_pop);
                sp2adapter = new Sp3adapter(getContext());
                sp2adapter.setAdats(sp2adats);
                lv_pop.setAdapter(sp2adapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_volume.setText(sp2adats.get(i));
                        if (i==0){
                            sort="desc";
                        }else if (i==1){
                            sort="";
                        }
                        getadats(sort,tag_name,beginTime,endTime);
//                        getsalesVolumeByNav(beginTime,endTime);
                        popMiddle.dismiss();
                    }
                });
                popMiddle = new PopupWindow(layout_tv_volume, tv_volume.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popMiddle.setTouchable(true);// 设置popupwindow可点击  
                popMiddle.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popMiddle.setFocusable(true);// 获取焦点  

                popMiddle.showAsDropDown(tv_volume);
                popMiddle.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popMiddle.setFocusable(false);//失去焦点  
                        popMiddle.dismiss();//消除pw 
                        return true;
                    }
                });
                break;
            case R.id.tv_lable:
                layout_tv_lable = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_lable.findViewById(R.id.lv_pop);
                sp3adapter = new Sp2adapter(getContext());
                sp3adapter.setAdats(listlabel);
                lv_pop.setAdapter(sp3adapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_lable.setText(listlabel.get(i).getLabel_name());
                        popLeft.dismiss();
                    }
                });
                popLeft = new PopupWindow(layout_tv_lable, tv_lable.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popLeft.setTouchable(true);// 设置popupwindow可点击  
                popLeft.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popLeft.setFocusable(true);// 获取焦点  

                popLeft.showAsDropDown(tv_lable);
                popLeft.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popLeft.setFocusable(false);//失去焦点  
                        popLeft.dismiss();//消除pw 
                        return true;
                    }
                });
                break;
            case R.id.tv_classify:
                layout_tv_classify = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_classify.findViewById(R.id.lv_pop);
                sp1adapter = new Spadapter(getContext());
                sp1adapter.setAdats(listFenlei);
                lv_pop.setAdapter(sp1adapter);
                sp1adapter.setOneidtextonclick(new Spadapter.Oneidtextonclick() {
                    @Override
                    public void itmeeidtonclick(int i, String type) {
                        if (i==0){
                            tag_name="";
                        }else {
                            tag_name=String.valueOf(listFenlei.get(i).getTag_id());
                        }
                        tv_classify.setText(listFenlei.get(i).getName());
                        Log.d("print","chuangkkkkkkk"+String.valueOf(listFenlei.get(i).getTag_id()));
                        getadats(sort,tag_name,beginTime,endTime);
//                        getsalesVolumeByNav(beginTime,endTime);
                        popRight.dismiss();
                    }
                });
//                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    }
//                });
                popRight = new PopupWindow(layout_tv_classify, tv_classify.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popRight.setTouchable(true);// 设置popupwindow可点击  
                popRight.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popRight.setFocusable(true);// 获取焦点  

                popRight.showAsDropDown(tv_classify);
                popRight.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popRight.setFocusable(false);//失去焦点  
                        popRight.dismiss();//消除pw 
                        return true;
                    }
                });
                break;
            case R.id.tv_rank:
                if (!isrank){
//                    sort="desc";
                    SortUtils.sort8(adats,isrank);
//                    getadats(sort,tag_name,beginTime,endTime);
                    isrank=true;
                }else{
//                    sort="";
                    SortUtils.sort8(adats,isrank);
//                    getadats(sort,tag_name,beginTime,endTime);
                    isrank=false;
                }
                adapter.notifyDataSetChanged();
                break;
            // 总价得排序
            case R.id.tv_total:
                if (!sort1){
                    SortUtils.sort4(adats,sort1);
                    sort1=true;
                }else {
                    SortUtils.sort4(adats,sort1);
                    sort1=false;
                }
                adapter.notifyDataSetChanged();
                break;
//                实收金额得排序
            case R.id.tv_receipts:
                if (!sort2){
                    SortUtils.sort4(adats,sort2);
                    sort2=true;
                }else {
                    SortUtils.sort4(adats,sort2);
                    sort2=false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_return:
                if (!sort3){
                    SortUtils.sort6(adats,sort3);
                    sort3=true;
                }else {
                    SortUtils.sort6(adats,sort3);
                    sort3=false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_filtrate:
                if (mCustomPopWindow!=null){
                    mCustomPopWindow.showAsDropDown(tv_filtrate, 0, 20);
                }
                break;
            case R.id.but_seek:
                beginTime=tv_timestart.getText().toString();
                endTime=tv_timeend.getText().toString();
                getadats(sort,tag_name,beginTime,endTime);
//                getsalesVolumeByNav(beginTime,endTime);
                mCustomPopWindow.dissmiss();
                break;
            case R.id.but_classification:
                Show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCustomPopWindow!=null){
            mCustomPopWindow.dissmiss();
            mCustomPopWindow=null;
        }
    }

    //获取标签
    public void getLabel() {
        OkGo.post(SysUtils.getGoodsServiceUrl("label_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("Tag", s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            JSONObject jo1 = jo.getJSONObject("response");
                            JSONObject jo2 = jo1.getJSONObject("data");
                            JSONArray ja = jo2.getJSONArray("units_info");
                            for (int i = 0; i < ja.length(); i++) {
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                label_entty = new Label_entty();
                                JSONObject jo3 = ja.getJSONObject(i);
                                label_entty.setLabel_name(jo3.getString("label_name"));
                                label_entty.setLabel_id(jo3.getString("label_id"));
                                xuangzhong.setCategory(jo3.getString("label_name"));
                                xuangzhong.setId(Integer.valueOf(jo3.getString("label_id")));
                                xuangzhong.setChecked(false);
                                listlabel.add(label_entty);
                            }

//                            gv_adapter.notifyDataSetChanged(false, strings);
//                            gv_2.setAdapter(gv_adapter);
//                            gv_adapter.notifyDataSetChanged();
                            if (label_entty != null) {
                                label_entty.setLabel_name("全部标签");
                                label_entty.setLabel_id("");
                                listlabel.add(0, label_entty);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
//                            sp2adapter=new Sp2adapter(getContext());
//                            sp2adapter.setAdats(listlabel);
//                            sp2.setAdapter(sp2adapter);
//                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void getclassify() {
        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray ja = j2.getJSONArray("nav_info");
                            for (int i = 0; i < ja.length(); i++) {
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                xuangzhong.setCategory(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                xuangzhong.setId(jo.getInt("tag_id"));
                                xuangzhong.setChecked(false);
                                listFenlei.add(fenlei);
                            }
                            Fenlei_Entty fenlei_entty = new Fenlei_Entty();
                            fenlei_entty.setName("全部分类");
                            fenlei_entty.setVisibility(false);
                            fenlei_entty.setTag_id(1);
                            listFenlei.add(0, fenlei_entty);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
//                            spadapter = new Spadapter(getContext());
//                            spadapter.setAdats(listFenlei);
//                            sp1.setAdapter(spadapter);
                        }
                    }
                });
    }

    //获取数据
    public void getadats(String order,String tag_name,String beginTime,String endTime){
        Log.d("print","打印开始时间"+beginTime);
        Log.d("print","打印开始时间"+endTime);
        OkGo.post(SysUtils.getGoodsServiceUrl("count_goods"))
                .tag(getActivity())
                .params("order",order)
                .params("tag_id",tag_name)
                .params("beginTime",beginTime)
                .params("endTime",endTime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","销售统计的数量"+s);
                        double k=0;
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            adats.clear();
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    Market_entty market=new Market_entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    market.setName(jo2.getString("goods_name"));
                                    market.setBncode(jo2.getString("bncode"));
                                    market.setCat_name(jo2.getString("menu"));
                                    market.setCost(jo2.getString("cost"));
                                    market.setPrice(jo2.getString("sell_price"));
                                    market.setStore(jo2.getString("store"));
                                    market.setNums(jo2.getString("nums"));
                                    market.setTotal(jo2.getString("total"));
                                    market.setCost(jo2.getString("cost"));
                                    adats.add(market);
                                    k= TlossUtils.add(Double.parseDouble(jo2.getString("nums")),k);
                                }
                            }
                            Log.e("print","销售的总额是"+k);
                            adapter.setAdats(adats);
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    //搜索
    public void getseek(String str){
        OkGo.post(SysUtils.getGoodsServiceUrl("count_goods"))
                .tag(getActivity())
                .params("name",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","搜索的内容是"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                adats.clear();
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    Market_entty market=new Market_entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    market.setName(jo2.getString("goods_name"));
                                    market.setBncode(jo2.getString("bncode"));
                                    market.setCat_name(jo2.getString("menu"));
                                    market.setCost(jo2.getString("cost"));
                                    market.setStore(jo2.getString("store"));
                                    market.setNums(jo2.getString("nums"));
                                    market.setTotal(jo2.getString("total"));
                                    market.setPrice(jo2.getString("price"));
                                    adats.add(market);
                                }
                            }
                            adapter.setAdats(adats);
                            lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            ed_seek.setText("");
//                            ed_seek.performClick();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 分类的销售额
     * @param beginTime 开始时间
     * @param endTime 结束时间
     */
    private void getsalesVolumeByNav(String beginTime,String endTime){
        Log.d("print","打印开始时间"+DateUtils.data(beginTime));
        Log.d("print","打印开始时间"+DateUtils.data(endTime));
        OkGo.post(SysUtils.getSellerServiceUrl("salesVolumeByNav"))
                .tag(getActivity())
                .params("begintime",DateUtils.data(beginTime))
                .params("endtime",DateUtils.data(endTime))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject object=jsonObject.getJSONObject("response");
                            String status=object.getString("status");
                            if (status.equals("200")){
                                specification_list.clear();
                                JSONObject object1=object.getJSONObject("data");
                                JSONArray jsonArray=object1.getJSONArray("detail");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jo2=jsonArray.getJSONObject(i);
                                    Specification_Entty specification=new Specification_Entty();
                                    if (jo2.getString("nav_id").equals("null")||jo2.getString("nav_id").equals("")){
                                        specification.setVal("其他");
                                    }else {
                                        specification.setVal(jo2.getString("menu"));
                                    }
                                    specification.setGive(StringUtils.stringpointtwo(jo2.getString("total")));
                                    specification_list.add(specification);
                                    adapter_specification.setAdats(specification_list,1);
                                    lv_recharge.setAdapter(adapter_specification);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 显示分类销售额
     */
    ListView lv_recharge;
    Adapter_specification adapter_specification=new Adapter_specification(getActivity());
    List<Specification_Entty> specification_list=new ArrayList<>();
    private void Show(){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
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
        adapter_specification=new Adapter_specification(getActivity());
        getsalesVolumeByNav(beginTime,endTime);
    }
}
