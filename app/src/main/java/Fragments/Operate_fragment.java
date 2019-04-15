package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entty.Operate_Entty;
import Utils.DateUtils;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Adapter_operate;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/1.
 * 敏感操作界面
 */
public class Operate_fragment extends Fragment implements View.OnClickListener {

    public View view;
    public ListView lv_operate;
    public List<Operate_Entty> adats;
    public Adapter_operate adapter;
    public Button but_next,but_last;
    public TextView tv_page;
    public int page=1;
    public boolean paging1=false,paging2=false;
    public int nums=0;
    public TextView tv_filtrate;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.operate_layout,null);
        init();
        LoadAdats();
        return view;
    }

    private void init() {
        adats=new ArrayList<>();
        adapter=new Adapter_operate(getActivity());
        lv_operate= (ListView) view.findViewById(R.id.lv_operate);
        but_next= (Button) view.findViewById(R.id.but_next);
        but_last= (Button) view.findViewById(R.id.but_last);
        tv_page= (TextView) view.findViewById(R.id.tv_page);
        but_last.setOnClickListener(this);
        but_next.setOnClickListener(this);
        tv_filtrate= (TextView) view.findViewById(R.id.tv_filtrate);
        tv_filtrate.setOnClickListener(this);

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
                but_day.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_triduum.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_week.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_month.setBackgroundColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#f1f1f1"));
            }
        });
    }
    private void LoadAdats() {
        OkGo.post(SysUtils.getSellerServiceUrl("log_list"))
                .tag(getActivity())
                .params("seller_token", SharedUtil.getString("seller_token"))
                .params("page", page)
                .params("endtime",endTime)
                .params("starttime",beginTime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","敏感操作"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            adats.clear();
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONObject jo2=jo1.getJSONObject("data");
                                nums= jo2.getInt("nums");
                                JSONArray ja1=jo2.getJSONArray("info");
                                for (int i=0;i<ja1.length();i++){
                                    Operate_Entty entty=new Operate_Entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    entty.setSeller_name(jo3.getString("seller_name"));
                                    entty.setWork_name(jo3.getString("work_name"));
                                    entty.setOperate_type(jo3.getString("operate_type"));
                                    entty.setAddtime(jo3.getString("addtime"));
                                    entty.setContent(jo3.getString("content"));
                                    adats.add(entty);
                                }
                                Log.d("print","敏感操作数据"+adats);

                            }
                            adapter.getAdats(adats);
                            lv_operate.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (nums % 10 == 0) {
                                tv_page.setText((page ) + "/" + (nums / 10));
                            } else {
                                tv_page.setText((page ) + "/" + (nums / 10 + 1));
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
            case R.id.but_seek:
                beginTime=tv_timestart.getText().toString();
                endTime=tv_timeend.getText().toString();
//                getadats(sort,tag_name,beginTime,endTime);
                LoadAdats();
                mCustomPopWindow.dissmiss();
                break;
            case R.id.but_next:
                if (nums % 10 == 0) {
                    tv_page.setText(page + "/" + (nums / 10));
                    if (page < (nums / 10)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
                            LoadAdats();
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
                            LoadAdats();
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
                            LoadAdats();
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (nums / 10 + 1));
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
                            LoadAdats();
                            paging2 = false;
                        }
                    }
                }
                break;
            case R.id.tv_filtrate:
                mCustomPopWindow.showAsDropDown(tv_filtrate, 0, 20);
                break;
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
                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        tv_timestart.setText(time);
                        randomtime=true;
                        isdiyi=true;
                    }
                }, "2015-11-22 17:34", getTime());
                timeSelector.show();
                break;
            case R.id.tv_timeend:
                if (isdiyi){
                    timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {
                            tv_timeend.setText(time);
                            isdiyi=false;
                            randomtime=true;
                        }
                    }, tv_timestart.getText().toString(), getTime());
                    timeSelector.show();
                }
                break;
        }

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
}
