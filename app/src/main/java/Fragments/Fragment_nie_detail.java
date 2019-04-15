package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

import Entty.Detail_Entty;
import Utils.DateUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Adapter_detail;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/17.
 */
public class Fragment_nie_detail extends Fragment implements View.OnClickListener {
    private View view;
    private ListView lv_detail;
    private Adapter_detail adapter;
    public Button but_next, but_last;
    public  List<Detail_Entty> adats;

    public String phone="";
    public int page = 1;
    public int total;
    public TextView tv_page,tv_seek;
    public boolean paging1 = false, paging2 = false;
    private String startTime="",endTime="";

    public List<Boolean> daylist;
    public CustomPopWindow mCustomPopWindow;
    public Button but_day,but_triduum,but_week,but_month,but_lastmonth;
    public TextView tv_timestart,tv_timeend;
    public String day="-1";
    public TimeSelector timeSelector;
    //判断开始时间是否选择了
    public Boolean isdiyi=false;

    public String memberNums="0",surplusTotal="0",scoreTotal="0",surplusAdd="0";

    private TextView tv_zong,tv_micro,tv_cash,tv_deal,tv_total;

    private EditText ed_seek;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.detail_fragment,null);
        init();
        LoadAdat(startTime,endTime,"");
        return view;
    }

    //初始化
    private void init() {

        adats=new ArrayList<>();
        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<6;i++){
            daylist.add(false);
        }

        tv_page= (TextView) view.findViewById(R.id.tv_page);
        tv_seek= (TextView) view.findViewById(R.id.tv_seek);
        tv_seek.setOnClickListener(this);

        tv_zong= (TextView) view.findViewById(R.id.tv_zong);
        tv_micro= (TextView) view.findViewById(R.id.tv_micro);
        tv_cash= (TextView) view.findViewById(R.id.tv_cash);
        tv_deal= (TextView) view.findViewById(R.id.tv_deal);

        lv_detail= (ListView) view.findViewById(R.id.lv_detail);
        adapter=new Adapter_detail(getActivity());

        but_next= (Button) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        but_last= (Button) view.findViewById(R.id.but_last);
        but_last.setOnClickListener(this);

        tv_total= (TextView) view.findViewById(R.id.tv_total);


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
                            endTime= DateUtils.getTime();
                        }else if (i==1){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endTime= DateUtils.getTime();
                        }else if (i==2){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endTime= DateUtils.getTime();
                        }else if (i==3){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+" 00:00:00");
                            endTime= DateUtils.getTime();
                        }else if (i==4){
                            startTime= DateUtils.data(tv_timestart.getText().toString()+"-01 00:00:00");
                            endTime= DateUtils.data(tv_timeend.getText().toString()+"-01 00:00:00");
                        }else {
                            startTime= DateUtils.data(tv_timestart.getText().toString()+":00");
                            endTime= DateUtils.data(tv_timeend.getText().toString()+":00");
                        }
                    }
                }
                adats.clear();
                LoadAdat(startTime,endTime,"");
                mCustomPopWindow.dissmiss();
            }
        });
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();
        ed_seek= (EditText) view.findViewById(R.id.ed_seek);
        ed_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i== EditorInfo.IME_ACTION_SEARCH){
                    ed_seek.getText().toString();
                    //搜索的接口
//                    SeekMember(tv_seek.getText().toString());
//                    LoadAdat(tv_seek.getText().toString());
                    phone=tv_seek.getText().toString();
                    LoadAdat(startTime,endTime,ed_seek.getText().toString());
                }
                return true;
            }
        });


    }

    //加载数据
    private void LoadAdat(String startTime,String endTime,String phone) {
        OkGo.post(SysUtils.getSellerServiceUrl("surplus_score_list"))
                .tag(getActivity())
                .params("page",page)
                .params("startTime",startTime)
                .params("endTime",endTime)
                .params("mobile",phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","数据分页"+s);
                        try {
                            adats.clear();
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String message=jo1.getString("message");
                            tv_total.setText("会员余额:"+message);
                            if (status.equals("200")){
                                JSONObject jo3=jo1.getJSONObject("data");
                                total=jo3.getInt("pageAll");
                                memberNums=jo3.getString("memberNums");
                                surplusTotal=jo3.getString("surplusTotal");
                                scoreTotal=jo3.getString("scoreTotal");
                                surplusAdd=jo3.getString("surplusAdd");
                                JSONArray ja1=jo3.getJSONArray("info");
                                for (int i=0;i<ja1.length();i++){
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    Detail_Entty detail_entty=new Detail_Entty();
                                    detail_entty.setAddtime(jo2.getString("addtime"));
                                    detail_entty.setLogin_name(jo2.getString("login_name"));
                                    detail_entty.setMember_name(jo2.getString("member_name"));
                                    detail_entty.setMobile(jo2.getString("mobile"));
                                    detail_entty.setScore(jo2.getString("score"));
                                    detail_entty.setSurplus(jo2.getString("surplus"));
                                    adats.add(detail_entty);
                                }
                            }
                            adapter.setAdats(adats);
                            lv_detail.setAdapter(adapter);

                            if (memberNums.equals("null")){
                                tv_zong.setText("0");
                            }else {
                                tv_zong.setText(Float.parseFloat(memberNums.replace("-",""))+"");
                            }
                            if (surplusTotal.equals("null")){
                                tv_micro.setText("0");
                            }else {
                                tv_micro.setText(Float.parseFloat(surplusTotal.replace("-",""))+"");
                            }
                            if (scoreTotal.equals("null")){
                                tv_cash.setText("0");
                            }else {
                                tv_cash.setText(Float.parseFloat(scoreTotal.replace("-",""))+"");
                            }

                            if (surplusAdd==null||surplusAdd.equals("null")){
                                tv_deal.setText("0");
                            }else {
                                tv_deal.setText(Float.parseFloat(surplusAdd)+"");
                            }
                            if (total % 10 == 0) {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10));
                            } else {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10 + 1));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


//    private void LoadAdat(String phone) {
//        OkGo.post(SysUtils.getSellerServiceUrl("surplus_score_list"))
//                .tag(getActivity())
//                .params("page",page)
//                .params("phone",phone)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("print","数据分页"+s);
//                        try {
//                            adats.clear();
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jo1=jsonObject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                JSONObject jo3=jo1.getJSONObject("data");
//                                total=jo3.getInt("pageAll");
//                                memberNums=jo3.getString("memberNums");
//                                surplusTotal=jo3.getString("surplusTotal");
//                                scoreTotal=jo3.getString("scoreTotal");
//                                surplusAdd=jo3.getString("surplusAdd");
//                                JSONArray ja1=jo3.getJSONArray("info");
//                                for (int i=0;i<ja1.length();i++){
//                                    JSONObject jo2=ja1.getJSONObject(i);
//                                    Detail_Entty detail_entty=new Detail_Entty();
//                                    detail_entty.setAddtime(jo2.getString("addtime"));
//                                    detail_entty.setLogin_name(jo2.getString("login_name"));
//                                    detail_entty.setMember_name(jo2.getString("member_name"));
//                                    detail_entty.setMobile(jo2.getString("mobile"));
//                                    detail_entty.setScore(jo2.getString("score"));
//                                    detail_entty.setSurplus(jo2.getString("surplus"));
//                                    adats.add(detail_entty);
//                                }
//                            }
//                            adapter.setAdats(adats);
//                            lv_detail.setAdapter(adapter);
//
//                            if (memberNums.equals("null")){
//                                tv_zong.setText("0");
//                            }else {
//                                tv_zong.setText(Float.parseFloat(memberNums.replace("-",""))+"");
//                            }
//                            if (surplusTotal.equals("null")){
//                                tv_micro.setText("0");
//                            }else {
//                                tv_micro.setText(Float.parseFloat(surplusTotal.replace("-",""))+"");
//                            }
//                            if (scoreTotal.equals("null")){
//                                tv_cash.setText("0");
//                            }else {
//                                tv_cash.setText(Float.parseFloat(scoreTotal.replace("-",""))+"");
//                            }
//
//                            if (surplusAdd==null||surplusAdd.equals("null")){
//                                tv_deal.setText("0");
//                            }else {
//                                tv_deal.setText(Float.parseFloat(surplusAdd)+"");
//                            }
//                            if (total % 10 == 0) {
//                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10));
//                            } else {
//                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10 + 1));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }


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

            case R.id.tv_seek:
                mCustomPopWindow.showAsDropDown(tv_seek, 0, 20);
                break;
            case R.id.but_next:
                if (Integer.valueOf(total) % 10 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                    if (page < (Integer.valueOf(total) / 10)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            LoadAdat(startTime,endTime,"");
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                    if (page < (Integer.valueOf(total) / 10 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            LoadAdat(startTime,endTime,"");
                            paging1 = false;
                        }
                    }
                }
                break;
            //上一页
            case R.id.but_last:
                if (page > 1) {
                    if (Integer.valueOf(total) % 10 == 0) {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 10));
                        if (!paging2) {
                            paging2 = true;
                            LoadAdat(startTime,endTime,"");
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                        if (!paging2) {
                            paging2 = true;
                            LoadAdat(startTime,endTime,"");
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
