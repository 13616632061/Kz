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
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entty.In_Out_Details;
import Entty.In_out_boundEntty;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.PrintWired;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import adapters.Details_inout_adapter;
import adapters.Statement_inout_adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import widget.MylistView;

/**
 * Created by admin on 2017/10/26.
 */
public class Fragment_out_statement extends Fragment implements Statement_inout_adapter.SetOnclick, View.OnClickListener {

    private View view;
    private ListView listview;
    private Statement_inout_adapter adapter;
    private LinearLayout ll_xiangcitem;
    private boolean isdisplay=false;
    //记录选择的item
    private int item;
    private MylistView lv_details;
    public In_out_boundEntty in_out_boundEntty;
    public int page = 1;
    //判断是否分页加载完成
    public boolean paging1 = false, paging2 = false;
    public TextView but_next,tv_page;
    Button but_previous;
    public TextView tv_seek,tv_delete;
    public CustomPopWindow mCustomPopWindow;
    public boolean randomtime = false;
    //选择了第一个框
    public boolean isdiyi=false;
    //    时间选择器
    public TimeSelector timeSelector;
    //    时间选择
    public RadioButton but_day, but_triduum, but_week, but_month, but_lastmonth,but_reset;
    //    时间
    public TextView tv_timestart, tv_timeend;
//    public boolean randomtime = false;
    public List<Boolean> daylist;

    public TextView tv_serial,tv_lei_time,tv_lei_mode,tv_lei_name,tv_beizhu;


    public In_Out_Details in_out_details;
    public Details_inout_adapter details_inout_adapter;
    public Button but_print;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_out_statement,null);
        init();
        getDatas();
        return view;
    }

    private void init() {


        tv_serial= (TextView) view.findViewById(R.id.tv_serial);
        tv_lei_time= (TextView) view.findViewById(R.id.tv_lei_time);
        tv_lei_mode= (TextView) view.findViewById(R.id.tv_lei_mode);
        tv_lei_name= (TextView) view.findViewById(R.id.tv_lei_name);
        tv_beizhu= (TextView) view.findViewById(R.id.tv_beizhu);

        but_print= (Button) view.findViewById(R.id.but_print);
        but_print.setOnClickListener(this);


        daylist = new ArrayList<>();
        daylist.clear();
        for (int i = 0; i < 5; i++) {
            daylist.add(false);
        }

        listview= (ListView) view.findViewById(R.id.listview);
        lv_details= (MylistView) view.findViewById(R.id.lv_details);
        details_inout_adapter=new Details_inout_adapter(getContext());

        ll_xiangcitem= (LinearLayout) view.findViewById(R.id.ll_xiangcitem);


        adapter=new Statement_inout_adapter(getContext());
        adapter.setbutonclick(this);

        but_previous= (Button) view.findViewById(R.id.but_previous);
        but_previous.setOnClickListener(this);
        but_next= (TextView) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        tv_page= (TextView) view.findViewById(R.id.tv_page);

        tv_seek = (TextView) view.findViewById(R.id.tv_seek);
        tv_seek.setOnClickListener(this);

        tv_delete= (TextView) view.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);

        View popwindow = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_statement, null);
        Button but_seek = (Button) popwindow.findViewById(R.id.but_seek);
        Button but_reset = (Button) popwindow.findViewById(R.id.but_reset);
        LinearLayout ll_day= (LinearLayout) popwindow.findViewById(R.id.ll_day);
//        ll_day.setVisibility(View.GONE);
        LinearLayout ll_fuhe = (LinearLayout) popwindow.findViewById(R.id.ll_fuhe);
        ll_fuhe.setVisibility(View.GONE);
        handleLogic(popwindow);      //创建并显示popWindow

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();


        but_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page=1;
                getDatas();
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
        tv_timestart.setText(getTime1() + " 00:00");
        tv_timeend.setText(getTime());
    }
    //获取数据
    private void getDatas() {
        OkGo.post(SysUtils.getSellerServiceUrl("detail_list"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("page", page)
                .params("starttime", tv_timestart.getText().toString()+":00")
                .params("endtime", tv_timeend.getText().toString()+":00")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","出入库的数据"+s);

                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo=jsonObject.getJSONObject("response");
                            String status=jo.getString("status");
                            if (in_out_boundEntty!=null){
                                in_out_boundEntty.getResponse().getData().getList().clear();
                            }
                            if (status.equals("200")){
                            Gson gson=new Gson();
                            in_out_boundEntty=gson.fromJson(s,In_out_boundEntty.class);
                        if (in_out_boundEntty.getResponse().getStatus().equals("200")){
                            adapter.getAdats(in_out_boundEntty);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) % 8 == 0) {
                                tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8));
                            } else {
                                tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8 + 1));
                            }
                        }else {
                        }
                            }
                            adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
    }


    //点击详情
    @Override
    public void Setbutonclick(int i) {
            isdisplay=true;
            item=i;
            getDetails(in_out_boundEntty.getResponse().getData().getList().get(i).getId());
            ll_xiangcitem.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);

        tv_serial.setText(in_out_boundEntty.getResponse().getData().getList().get(i).getOrder_id());
        tv_lei_time.setText(TimeZoneUtil.getTime1((Long.valueOf(in_out_boundEntty.getResponse().getData().getList().get(i).getCreatetime()) * 1000)));
        if (in_out_boundEntty.getResponse().getData().getList().get(i).getType().equals("0")) {
            tv_lei_mode.setText("入库");
        } else {
            tv_lei_mode.setText("出库");
        }
        tv_lei_name.setText(in_out_boundEntty.getResponse().getData().getList().get(i).getOparator());
        tv_beizhu.setText(in_out_boundEntty.getResponse().getData().getList().get(i).getRemark());
    }


    public void getDetails(String id){
        Log.d("print","打印数据出入库的详情为"+id);
        OkGo.post(SysUtils.getSellerServiceUrl("store_info"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("id",id)
//                .params("oparator",operator)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印数据出入库的详情为"+s);
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(s);
                            JSONObject jo=jsonObject.getJSONObject("response");
                            String status=jo.getString("status");
                            if (status.equals("200")){
                                Gson gson=new Gson();
                                in_out_details=gson.fromJson(s,In_Out_Details.class);
                                details_inout_adapter.getAdats(in_out_details);
                                lv_details.setAdapter(details_inout_adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_print:

//                String syy = BluetoothPrintFormatUtil.getInOutPrinterMsg(SharedUtil.getString("name"), "入库单","3655789",in_out_details,"0",tv_beizhu.getText().toString());
                String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), SharedUtil.getString("phone"),DateUtils.getCurDate(),in_out_details,tv_beizhu.getText().toString(),tv_beizhu.getText().toString(),"",in_out_boundEntty.getResponse().getData().getList().get(item).getMoney());
                PrintWired.usbPrint(getActivity(),syy);
                break;
            case R.id.tv_delete:
                ll_xiangcitem.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
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
                tv_timestart.setText(DateUtils.gettime(-3)+" 00:00");
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
                tv_timestart.setText(DateUtils.gettime(-7)+" 00:00");
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
                but_day.setTextColor(Color.parseColor("#f1f1f1"));
                but_triduum.setTextColor(Color.parseColor("#f1f1f1"));
                but_week.setTextColor(Color.parseColor("#f1f1f1"));
                but_lastmonth.setTextColor(Color.parseColor("#f1f1f1"));
                tv_timestart.setText(StringUtils.gettimeDataTime()+"-01 00:00");
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

                tv_timestart.setText(DateUtils.getMonthDate(-1)+"-01 00:00");
                tv_timeend.setText(DateUtils.getMonthDate(0)+"-01 00:00");
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
            case R.id.tv_seek:
                mCustomPopWindow.showAsDropDown(tv_seek, 0, 20);
                break;
            case R.id.but_next:
                if (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) % 8 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8));
                    if (page < (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
//                            if (day.equals("6")){
//                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
//                            }else {
//                                setfiltrate(day, sp1, sp2, sp3);
//                            }
                            getDatas();
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8 + 1));
                    if (page < (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
//                            if (day.equals("6")){
//                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
//                            }else {
//                                setfiltrate(day, sp1, sp2, sp3);
//                            }
                            getDatas();
                            paging1 = false;
                        }
                    }
                }
                break;
            case R.id.but_previous:
                if (page > 1) {
                    if (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) % 8 == 0) {
                        tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8));
                        page--;
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
//                            if (day.equals("6")){
//                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
//                            }else {
//                                setfiltrate(day, sp1, sp2, sp3);
//                            }
                            getDatas();
                            paging2 = false;
                        }
                    } else {
                        tv_page.setText(page + "/" + (Integer.valueOf(in_out_boundEntty.getResponse().getData().getTotal().get(0).getNum()) / 8 + 1));
                        page--;
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
//                            if (day.equals("6")){
//                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
//                            }else {
//                                setfiltrate(day, sp1, sp2, sp3);
//                            }
                            getDatas();
                            paging2 = false;
                        }
                    }
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
