package Fragments;

import android.content.Context;
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

import Entty.Return_Entty;
import Utils.DateUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Return_Adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import widget.MylistView;

/**
 * Created by admin on 2017/6/8.
 */
public class Fragment_return extends Fragment implements View.OnClickListener {

    public View view;
    public Context context;
    public MylistView lv;
    public Return_Adapter adapter;
    public List<Return_Entty> adats;
    public double Merchants=0;
    public double customer=0;
    public TextView tv_zong,tv_lirun,tv_time,tvtime;
    public CustomPopWindow mCustomPopWindow;
    //    时间选择
    public RadioButton but_day, but_triduum, but_week, but_month, but_lastmonth,but_reset;
    //    时间
    public TextView tv_timestart, tv_timeend;
    //    时间选择器
    public TimeSelector timeSelector;
    public List<Boolean> daylist;
    //选择了第一个框
    public boolean isdiyi=false;
    public boolean randomtime = false;
    public String day = "1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_return,null);
        init();
        Loadats();
        return view;
    }

    //加载数据
    private void Loadats() {
        OkGo.post(SysUtils.getSellerServiceUrl("ship_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","退货数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Return_Entty return_entty=new Return_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    return_entty.setOrder_id(jo2.getString("order_id"));
                                    return_entty.setSeller_id(jo2.getString("seller_id"));
                                    return_entty.setWorker_name(jo2.getString("worker_name"));
                                    return_entty.setAddtime(jo2.getString("addtime"));
                                    return_entty.setShip_id(jo2.getString("ship_id"));
                                    String type=jo2.getString("type");
                                    if (type.equals("seller_back")){
                                        Merchants= TlossUtils.add(Merchants,Double.parseDouble(StringUtils.stringpointtwo(jo2.getString("cost_sums"))));
                                    }else {
                                        customer= TlossUtils.add(customer,Double.parseDouble(StringUtils.stringpointtwo(jo2.getString("cost_sums"))));
                                    }
                                    return_entty.setDlytype(jo2.getString("dlytype"));
                                    return_entty.setDly_id(jo2.getString("dly_id"));
                                    List<Return_Entty.ItemsBean> listitemsBean=new ArrayList<Return_Entty.ItemsBean>();
                                    JSONArray ja2=jo2.getJSONArray("items");
                                    for (int j=0;j<ja2.length();j++){
                                        Return_Entty.ItemsBean itemsBean=new Return_Entty.ItemsBean();
                                        JSONObject jo3=ja2.getJSONObject(j);
                                        itemsBean.setGoods_id(jo3.getString("goods_id"));
                                        itemsBean.setName(jo3.getString("name"));
                                        itemsBean.setNums(jo3.getString("nums"));
//                                        itemsBean.setItem_id(jo3.getString("item_id"));
                                        itemsBean.setPrice(jo3.getString("price"));
                                        itemsBean.setSum(jo3.getString("sum"));
                                        listitemsBean.add(itemsBean);
                                    }
                                    return_entty.setItems(listitemsBean);
                                    adats.add(return_entty);
                                }
                                tv_zong.setText(customer+"");
                                tv_lirun.setText(Merchants+"");
                                adapter.setAdats(adats);
                                lv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//     初始化
    private void init() {
        adats=new ArrayList<>();
        daylist = new ArrayList<>();
        daylist.clear();
        for (int i = 0; i < 5; i++) {
            daylist.add(false);
        }
        lv= (MylistView) view.findViewById(R.id.lv);
        tv_zong= (TextView) view.findViewById(R.id.tv_zong);
        tv_lirun= (TextView) view.findViewById(R.id.tv_lirun);
        tv_time= (TextView) view.findViewById(R.id.tv_time);
        tvtime= (TextView) view.findViewById(R.id.tvtime);
        tvtime.setOnClickListener(this);
        adapter=new Return_Adapter(getContext());

        View popwindow = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_statement, null);
        Button but_seek = (Button) popwindow.findViewById(R.id.but_seek);
        Button but_reset = (Button) popwindow.findViewById(R.id.but_reset);
        LinearLayout ll_fuhe = (LinearLayout) popwindow.findViewById(R.id.ll_fuhe);
        ll_fuhe.setVisibility(View.GONE);
        handleLogic(popwindow);      //创建并显示popWindow

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();
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

        but_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (randomtime) {
                    tv_timestart.getText().toString();
                    tv_timeend.getText().toString();
                    randomtime = false;
                    day="6";
//                    setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
                }
                for (int i = 0; i < daylist.size(); i++) {
                    if (daylist.get(i)) {
                        if (i == 0) {
                            day = "1";
                            daylist.set(0, true);
                        } else if (i == 1) {
                            day = "2";
                            daylist.set(1, true);
                        } else if (i == 2) {
                            day = "3";
                            daylist.set(2, true);
                        } else if (i == 3) {
                            day = "4";
                            daylist.set(3, true);
                        } else if (i == 4) {
                            day = "5";
                            daylist.set(4, true);
                        }
                    }
                }
                if (day.equals("1")) {
                    tv_time.setText("今日数据");
                } else if (day.equals("2")) {
                    tv_time.setText("近三日数据");
                } else if (day.equals("3")) {
                    tv_time.setText("本周数据");
                } else if (day.equals("4")) {
                    tv_time.setText("本月数据");
                } else if (day.equals("5")) {
                    tv_time.setText("上月数据");
                }
//                setfiltrate(day, sp1, sp2, sp3);
//                page = 1;
                mCustomPopWindow.dissmiss();
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
            case R.id.tvtime:
                mCustomPopWindow.showAsDropDown(tvtime, 0, 20);
                break;
            case R.id.but_day:
                if (!daylist.get(0)) {
                    but_day.setTextColor(Color.parseColor("#FF6501"));
                    for (int i = 0; i < daylist.size(); i++) {
                        if (i == 0) {
                            daylist.set(i, true);
                        } else {
                            daylist.set(i, false);
                        }
                    }
                } else {
                    for (int i = 0; i < daylist.size(); i++) {
                        daylist.set(i, false);
                    }
                    but_day.setTextColor(Color.parseColor("#ff757575"));
                }
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(getTime1() + " 00:00");
                tv_timeend.setText(getTime());
                break;
            case R.id.but_triduum:
                if (!daylist.get(1)) {
                    but_triduum.setTextColor(Color.parseColor("#FF6501"));
                    for (int i = 0; i < daylist.size(); i++) {
                        if (i == 1) {
                            daylist.set(i, true);
                        } else {
                            daylist.set(i, false);
                        }
                    }
                } else {
                    for (int i = 0; i < daylist.size(); i++) {
                        daylist.set(i, false);
                    }
                    but_triduum.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(DateUtils.gettime(-3));
                tv_timeend.setText(getTime());
                break;
            case R.id.but_week:
                if (!daylist.get(2)) {
                    but_week.setTextColor(Color.parseColor("#FF6501"));
                    for (int i = 0; i < daylist.size(); i++) {
                        if (i == 2) {
                            daylist.set(i, true);
                        } else {
                            daylist.set(i, false);
                        }
                    }
                } else {
                    for (int i = 0; i < daylist.size(); i++) {
                        daylist.set(i, false);
                    }
                    but_week.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(DateUtils.gettime(-7));
                tv_timeend.setText(getTime());
                break;
            case R.id.but_month:
                if (!daylist.get(3)) {
                    but_month.setTextColor(Color.parseColor("#FF6501"));
                    for (int i = 0; i < daylist.size(); i++) {
                        if (i == 3) {
                            daylist.set(i, true);
                        } else {
                            daylist.set(i, false);
                        }
                    }
                } else {
                    for (int i = 0; i < daylist.size(); i++) {
                        daylist.set(i, false);
                    }
                    but_month.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                tv_timestart.setText(StringUtils.gettimeDataTime()+"-01");
                tv_timeend.setText(getTime());
                break;
            case R.id.but_lastmonth:
                if (!daylist.get(4)) {
                    but_lastmonth.setTextColor(Color.parseColor("#FF6501"));
                    for (int i = 0; i < daylist.size(); i++) {
                        if (i == 4) {
                            daylist.set(i, true);
                        } else {
                            daylist.set(i, false);
                        }
                    }
                } else {
                    for (int i = 0; i < daylist.size(); i++) {
                        daylist.set(i, false);
                    }
                    but_lastmonth.setTextColor(Color.parseColor("#ff757575"));
                }
                but_day.setTextColor(Color.parseColor("#ff757575"));
                but_triduum.setTextColor(Color.parseColor("#ff757575"));
                but_week.setTextColor(Color.parseColor("#ff757575"));
                but_month.setTextColor(Color.parseColor("#ff757575"));

                tv_timestart.setText(DateUtils.getMonthDate(-1));
                tv_timeend.setText(DateUtils.getMonthDate(0));
                break;
            case R.id.tv_timestart:
                DateTimeUtils.runTime(getActivity(),tv_timestart);
                isdiyi=true;
                randomtime = true;
//                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_timestart.setText(time);
//                        isdiyi=true;
//                        randomtime = true;
//                    }
//                }, "2015-11-22 17:34", getTime());
//                timeSelector.show();
                break;
            case R.id.tv_timeend:
                if (isdiyi){
                    DateTimeUtils.runTime(getActivity(),tv_timeend);
                    randomtime = true;
                    isdiyi=false;
//                    timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                        @Override
//                        public void handle(String time) {
//                            tv_timeend.setText(time);
//                            randomtime = true;
//                            isdiyi=false;
//                        }
//                    }, tv_timestart.getText().toString(), getTime());
//                    timeSelector.show();
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
