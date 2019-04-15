package Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import Entty.Shifting_Entty;
import Utils.DateUtils;
import Utils.PrintWired;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import adapters.Shifting_adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;

import static Utils.BluetoothPrintFormatUtil.PrintTransfer;

/**
 * Created by admin on 2017/5/17.
 * 交接日结
 */
public class Shifting_nei_Fragment extends Fragment implements View.OnClickListener, Shifting_adapter.SetLinearLayoutclick {

    public View view;
    public ListView lv_list;
    public List<Shifting_Entty> adats;
    public Shifting_adapter adapter;
    public TextView tv_filtrate;
    public CustomPopWindow mCustomPopWindow;
    public Button but_day,but_triduum,but_week,but_month,but_lastmonth;
    public TextView tv_timestart,tv_timeend;
    //判断开始时间是否选择了
    public Boolean isdiyi=false;

    public TimeSelector timeSelector;

    public List<Boolean> daylist;

    public String day="-1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shifting_layout, null);
        init();
        Loadats();


        return view;
    }
    //初始化
    private void init() {
        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<6;i++){
            daylist.add(false);
        }
        adats = new ArrayList<>();
        lv_list = (ListView) view.findViewById(R.id.lv_list);
        adapter = new Shifting_adapter(getContext());
        adapter.SetLinearLayoutclick(this);
        tv_filtrate= (TextView) view.findViewById(R.id.tv_filtrate);
        tv_filtrate.setOnClickListener(this);

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
                            day="1";
                        }else if (i==1){
                            day="2";
                        }else if (i==2){
                            day="3";
                        }else if (i==3){
                            day="4";
                        }else if (i==4){
                            day="5";
                        }else {
                            day="6";

                        }
                    }
                }
                mCustomPopWindow.dissmiss();
                Timefiltrate(day);
            }
        });

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();
    }

    private void Loadats() {
        OkGo.post(SysUtils.getTestServiceUrl("show_list"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "交接日结" + s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject jo1 = jsonobject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Shifting_Entty shifting = new Shifting_Entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    String begin_time = jo2.getString("begin_time");
                                    if (!begin_time.equals("null")) {
                                        shifting.setBegin_time(TimeZoneUtil.getTime1((long) (Float.parseFloat(begin_time) * 1000)));
                                    }
                                    String end_time = jo2.getString("end_time");
                                    if (!end_time.equals("null")) {
                                        shifting.setEnd_time(TimeZoneUtil.getTime1((long) (Float.parseFloat(end_time) * 1000)));
                                    }
                                    String total_money = jo2.getString("total_money");
                                    shifting.setTotal_money(total_money);
                                    String cash_money = jo2.getString("cash_money");
                                    shifting.setCash_money(cash_money);
                                    String store_money = jo2.getString("store_money");
                                    shifting.setStore_money(store_money);
                                    shifting.setRemark(jo2.getString("remark"));
                                    String micro_money = jo2.getString("micro_money");
                                    shifting.setMicro_money(micro_money);
                                    String other_money=jo2.getString("other_money");
                                    shifting.setOther_money(other_money);
                                    String spare_money = jo2.getString("spare_money");
                                    shifting.setSpare_money(spare_money);
                                    shifting.setTurn_in_money(jo2.getString("turn_in_money"));
                                    shifting.setWorker_name(jo2.getString("worker_name"));

                                    adats.add(shifting);
                                }
                            }
                            adapter.setAdats(adats);
                            lv_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_filtrate:
                mCustomPopWindow.showAsDropDown(tv_filtrate, 0, 20);
                break;
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
                tv_timestart.setText(getTime1() + " 00:00");
                tv_timeend.setText(getTime());
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
                tv_timestart.setText(DateUtils.gettime(-3));
                tv_timeend.setText(getTime());
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
                tv_timestart.setText(DateUtils.gettime(-7));
                tv_timeend.setText(getTime());
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
                tv_timestart.setText(StringUtils.gettimeDataTime()+"-01");
                tv_timeend.setText(getTime());
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

                tv_timestart.setText(DateUtils.getMonthDate(-1));
                tv_timeend.setText(DateUtils.getMonthDate(0));
                break;
            case R.id.tv_timestart:
                day="6";
                DateTimeUtils.runTime(getActivity(),tv_timestart);
                isdiyi=true;
//                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_timestart.setText(time);
//                        isdiyi=true;
//                    }
//                }, "2015-11-22 17:34", getTime());
//                timeSelector.show();
                break;
            case R.id.tv_timeend:
                if (isdiyi){
                    day="6";
                    if (tv_timestart.getText().toString()!=null){
                        DateTimeUtils.runTime(getActivity(),tv_timeend);
                        isdiyi=false;
//                        timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                            @Override
//                            public void handle(String time) {
//                                tv_timeend.setText(time);
//                                isdiyi=false;
//                            }
//                        }, tv_timestart.getText().toString(), getTime());
//                        timeSelector.show();
                }
                }
                break;
        }
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
    //时间筛选
    public void Timefiltrate(String day){
        Log.d("print","筛选的结果是"+day);
        OkGo.post(SysUtils.getTestServiceUrl("show_list"))
                .tag(getActivity())
                .params("day",day)
                .params("begin_time",tv_timestart.getText().toString())
                .params("end_time",tv_timeend.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                            Log.d("print","筛选的结果是"+s);
                            adats.clear();
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject jo1 = jsonobject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Shifting_Entty shifting = new Shifting_Entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    String begin_time = jo2.getString("begin_time");
                                    if (!begin_time.equals("null")) {
                                        shifting.setBegin_time(TimeZoneUtil.getTime1((long) (Float.parseFloat(begin_time) * 1000)));
                                    }
                                    String end_time = jo2.getString("end_time");
                                    if (!end_time.equals("null")) {
                                        shifting.setEnd_time(TimeZoneUtil.getTime1((long) (Float.parseFloat(end_time) * 1000)));
                                    }
                                    String total_money = jo2.getString("total_money");
                                    shifting.setTotal_money(total_money);
                                    shifting.setRemark(jo2.getString("remark"));
                                    String cash_money = jo2.getString("cash_money");
                                    shifting.setCash_money(cash_money);
                                    String micro_money = jo2.getString("micro_money");
                                    shifting.setMicro_money(micro_money);
                                    String spare_money = jo2.getString("spare_money");
                                    shifting.setSpare_money(spare_money);
                                    String other_money=jo2.getString("other_money");
                                    shifting.setOther_money(other_money);
                                    shifting.setWorker_name(jo2.getString("worker_name"));
                                    shifting.setTurn_in_money(jo2.getString("turn_in_money"));
                                    adats.add(shifting);
                                }
                            }
                            adapter.setAdats(adats);
                            lv_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void setOnClickListener(final int i) {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.setTitle("备注：");
        dialog1.show();
        Window window1 = dialog1.getWindow();
        window1.setContentView(R.layout.shifting_dialog);
        TextView tv_remark= (TextView) window1.findViewById(R.id.tv_remark);
        Button but_print= (Button) window1.findViewById(R.id.but_print);
        tv_remark.setText(adats.get(i).getRemark());
        but_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String syy=PrintTransfer(adats.get(i).getWorker_name(),adats.get(i).getMicro_money(),adats.get(i).getCash_money(),
                        "",adats.get(i).getTotal_money(),adats.get(i).getTotal_num(),adats.get(i).getDeserve_money(),adats.get(i).getOther_money(),
                        adats.get(i).getRemark(),adats.get(i).getBegin_time(),adats.get(i).getEnd_time());
                PrintWired.usbPrint(getContext(),syy);
            }
        });
    }
}
