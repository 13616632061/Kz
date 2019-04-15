package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
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

import Entty.Cashier_entty;
import Entty.Check_tuihuo;
import Entty.Refund_entty;
import Entty.Stat_Zong_ENtty;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.PrintUtil;
import Utils.PrintWired;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import Utils.USBPrinters;
import adapters.Cashier_adapter;
import adapters.Leibu_adapter;
import adapters.Sp3adapter;
import adapters.Statement_adapter;
import base.BaseFragment;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import shujudb.Sqlite_Entity;
import widget.MylistView;

/**
 * Created by admin on 2017/5/2.
 */
public class Fragment_nei_statement extends BaseFragment implements View.OnClickListener, Statement_adapter.SetOnclick, Leibu_adapter.Ontuihuoonclick {

    public View view;
    public ListView lv_statem;
    public Statement_adapter adapter;
    public CustomPopWindow mCustomPopWindow;
    //筛选
    public TextView tv_seek;
    //    判断是否弹出弹窗
    public boolean isshow = false;
    //    显示数据时间
    public TextView tv_time;
    //    时间选择
    public RadioButton but_day, but_triduum, but_week, but_month, but_lastmonth,but_reset;

    public List<Boolean> daylist;

    public String day = "1";
    //    时间
    public TextView tv_timestart, tv_timeend;

    public boolean randomtime = false;
    //选择了第一个框
    public boolean isdiyi=false;
    //    时间选择器
    public TimeSelector timeSelector;

    public TextView tv_dingdan, tv_pay, tv_user;
    //单据的筛选receipts支付方式payment；
    public String receipts = "", payment = "";

    public View layout_tv_dingdan, layout_tv_pay, layout_tv_user;

    public ListView lv_pop;
    private PopupWindow popRight;
    private PopupWindow popMiddle;
    private PopupWindow popleft;

    public Sp3adapter spadapter;
    public List<String> sp1adats, sp2adats;

    public TextView tv_zong, tv_micro, tv_cash, tv_size, tv_lirun;

    public List<Stat_Zong_ENtty> stat_zong_eNttyList;

    //判断是否分页加载完成
    public boolean paging1 = false, paging2 = false;
    public TextView tv_page;
    public Button but_next, but_previous,but_updatas;
    public String count = "1";
    public int page = 1;
    //判断是否显示详情
    public boolean details = false;
    public LinearLayout ll_xiangcitem;
    public TextView tv_serial, tv_lei_time, tv_lei_mode, tv_lei_name, tv_lei_zong, tv_shiji, tv_zhaoling;
    public MylistView lv_details;
    public List<Check_tuihuo> listcheck_tuihuo;
    public List<Refund_entty> list_refund;
    public Leibu_adapter leibu_adapter;
    public Button but_refund;
    public String order_id;
    public String sp1 = "", sp2 = "", sp3 = "";
    public TextView tv_delete;
    public List<Cashier_entty> cashier_enttyList;
    public Cashier_adapter cashier_adapter;
    public String work_id = "";
    public int j = -1;
    //打印
    public Button but_print;
    public Button but_sale;
    private  ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo=new ArrayList<>();
    private int payType;//1移动支付，2现金支付
//    private BluetoothService mService = null;
//    private BluetoothDevice con_dev = null;
    private String tel;//商户电话

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case BluetoothService.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothService.STATE_CONNECTED:
//                            if (getActivity()!=null) {
//                                Toast.makeText(getActivity(), "连接成功",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            break;
//                        case BluetoothService.STATE_CONNECTING:
//
//                            break;
//                        case BluetoothService.STATE_LISTEN:
//                        case BluetoothService.STATE_NONE:
//                            break;
//                    }
//                    break;
//                case BluetoothService.MESSAGE_CONNECTION_LOST:
//                    if (getActivity()!=null) {
//                        Toast.makeText(getActivity(), "设备连接丢失",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case BluetoothService.MESSAGE_UNABLE_CONNECT:
//                    if (getActivity()!=null){
//                        Toast.makeText(getActivity(), "关闭设备",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//        }
//    };





//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        USBPrinters.initPrinter(getActivity());
//        view = inflater.inflate(R.layout.statement_nei_fragment, null);
//        init();
//        Loaddata();
//        setemployee();
//        return view;
//    }

    public TextView tv_discount;

    @Override
    protected int getContentId() {
        return R.layout.statement_nei_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
        Loaddata();
        setemployee();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        USBPrinters.destroyPrinter();
    }

    //初始化
    private void init1(View view) {
        //蓝牙打印
//        mService = new BluetoothService(getActivity(), mHandler);
//        if (mService.isAvailable() == false) {
//            Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_LONG).show();
//            getActivity().finish();
//        }
//        //判断蓝牙是否连接小票打印机
//        String address_Mac= SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
//        if(TextUtils.isEmpty(address_Mac)){
//            retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"如需打印小票，请先点击打印按钮，连接小票打印机",20);
//        }else {
//            con_dev = mService.getDevByMac(address_Mac);
//            mService.connect(con_dev);
//        }
        tel= SharedUtil.getString("seller_tel");
        cashier_enttyList = new ArrayList<>();
//        判断时间的选中
        daylist = new ArrayList<>();
        daylist.clear();
        for (int i = 0; i < 5; i++) {
            daylist.add(false);
        }
        but_print= (Button) view.findViewById(R.id.but_print);
        but_print.setOnClickListener(this);

        but_sale= (Button) view.findViewById(R.id.but_sale);
        but_sale.setOnClickListener(this);

        sp1adats = new ArrayList<>();
        sp2adats = new ArrayList<>();

        list_refund = new ArrayList<>();
        listcheck_tuihuo = new ArrayList<>();
        stat_zong_eNttyList = new ArrayList<>();

        sp1adats.add("所有单据");
        sp1adats.add("有效单据");
        sp1adats.add("赊账单据");
//        sp1adats.add("退货单据");

        sp2adats.add("全部付款方式");
        sp2adats.add("移动支付");
        sp2adats.add("现金");

        tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);

        tv_time = (TextView) view.findViewById(R.id.tv_time);

        tv_discount= (TextView) view.findViewById(R.id.tv_discount);

        //分页
        tv_page = (TextView) view.findViewById(R.id.tv_page);
        but_next = (Button) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        but_previous = (Button) view.findViewById(R.id.but_previous);
        but_previous.setOnClickListener(this);
        but_updatas= (Button) view.findViewById(R.id.but_updatas);
        but_updatas.setOnClickListener(this);

        //详情
        ll_xiangcitem = (LinearLayout) view.findViewById(R.id.ll_xiangcitem);
        tv_serial = (TextView) view.findViewById(R.id.tv_serial);
        tv_lei_time = (TextView) view.findViewById(R.id.tv_lei_time);
        tv_lei_mode = (TextView) view.findViewById(R.id.tv_lei_mode);
        tv_lei_name = (TextView) view.findViewById(R.id.tv_lei_name);
        tv_lei_zong = (TextView) view.findViewById(R.id.tv_lei_zong);
        tv_shiji = (TextView) view.findViewById(R.id.tv_shiji);
        tv_zhaoling = (TextView) view.findViewById(R.id.tv_zhaoling);

        but_refund = (Button) view.findViewById(R.id.but_refund);
        but_refund.setOnClickListener(this);

        lv_details = (MylistView) view.findViewById(R.id.lv_details);
        leibu_adapter = new Leibu_adapter(getContext());

        tv_zong = (TextView) view.findViewById(R.id.tv_zong);
        tv_micro = (TextView) view.findViewById(R.id.tv_micro);
        tv_cash = (TextView) view.findViewById(R.id.tv_cash);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_lirun = (TextView) view.findViewById(R.id.tv_lirun);

        tv_dingdan = (TextView) view.findViewById(R.id.tv_dingdan);
        tv_dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_tv_dingdan = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_dingdan.findViewById(R.id.lv_pop);
                spadapter = new Sp3adapter(getContext());
                spadapter.setAdats(sp1adats);
                lv_pop.setAdapter(spadapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_dingdan.setText(sp1adats.get(i));
                        page = 1;
                        if (randomtime) {
                            setcredit(sp1, "", tv_timestart.getText().toString(), tv_timeend.getText().toString());
                        } else {
                            if (i == 0) {
                                sp1 = "first";
                                setcredit(sp1, "", day);
                            }
                            if (i == 2) {
                                sp1 = "shezhang";
                                setcredit(sp1, "", day);
                            }
                            if (i == 3) {
                                sp1 = "first";
                                setcredit(sp1, "yes", day);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        popRight.dismiss();
                    }
                });

                popRight = new PopupWindow(layout_tv_dingdan, tv_dingdan.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popRight.setTouchable(true);// 设置popupwindow可点击  
                popRight.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popRight.setFocusable(true);// 获取焦点  

                popRight.showAsDropDown(tv_dingdan);
                popRight.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popRight.setFocusable(false);//失去焦点  
                        popRight.dismiss();//消除pw 
                        return true;
                    }
                });

            }
        });
        tv_pay = (TextView) view.findViewById(R.id.tv_pay);
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_tv_pay = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_pay.findViewById(R.id.lv_pop);
                spadapter = new Sp3adapter(getContext());
                spadapter.setAdats(sp2adats);
                lv_pop.setAdapter(spadapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_pay.setText(sp2adats.get(i));
                        Log.d("print", "点击了" + i);
                        page = 1;
                        if (i == 0) {
                            sp2 = "";
                            setfiltrate(day, sp1, sp2, sp1);
                        }
                        if (i == 1) {
                            sp2 = "micro";
                            setfiltrate(day, sp1, sp2, sp1);
                        }
                        if (i == 2) {
                            sp2 = "cash";
                            setfiltrate(day, sp1, sp2, sp1);
                        }
                        adapter.notifyDataSetChanged();
                        popMiddle.dismiss();
                    }
                });

                popMiddle = new PopupWindow(layout_tv_pay, tv_pay.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popMiddle.setTouchable(true);// 设置popupwindow可点击  
                popMiddle.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popMiddle.setFocusable(true);// 获取焦点  

                popMiddle.showAsDropDown(tv_pay);
                popMiddle.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popMiddle.setFocusable(false);//失去焦点  
                        popMiddle.dismiss();//消除pw 
                        return true;
                    }
                });
            }
        });
        tv_user = (TextView) view.findViewById(R.id.tv_user);
        tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_tv_user = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_user.findViewById(R.id.lv_pop);
                cashier_adapter = new Cashier_adapter(getContext());
                cashier_adapter.setAdats(cashier_enttyList);
                lv_pop.setAdapter(cashier_adapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_user.setText(cashier_enttyList.get(i).getLogin_name());
                        work_id = cashier_enttyList.get(i).getWork_id();
                        setfiltrate(day, sp1, sp2, sp1);
                        popleft.dismiss();
                    }
                });
                popleft = new PopupWindow(layout_tv_user, tv_user.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                popleft.setTouchable(true);// 设置popupwindow可点击  
                popleft.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                popleft.setFocusable(true);// 获取焦点  

                popleft.showAsDropDown(tv_user);
                popleft.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        popleft.setFocusable(false);//失去焦点  
                        popleft.dismiss();//消除pw 
                        return true;
                    }
                });

            }
        });

        lv_statem = (ListView) view.findViewById(R.id.lv_statem);
        adapter = new Statement_adapter(getContext());
        adapter.setbutonclick(this);

        tv_seek = (TextView) view.findViewById(R.id.tv_seek);
        tv_seek.setOnClickListener(this);

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
                but_day.setBackgroundColor(Color.parseColor("#ff757575"));
                but_triduum.setBackgroundColor(Color.parseColor("#ff757575"));
                but_week.setBackgroundColor(Color.parseColor("#ff757575"));
                but_month.setBackgroundColor(Color.parseColor("#ff757575"));
                but_lastmonth.setBackgroundColor(Color.parseColor("#ff757575"));
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
                    setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
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
                setfiltrate(day, sp1, sp2, sp3);
                page = 1;
                mCustomPopWindow.dissmiss();
            }
        });
    }

    //加载数据
    private void Loaddata() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_total_price"))
                .tag(getActivity())
                .params("day", 1)
                .params("first", "first")
                .params("page", page)
                .params("work_id", work_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "报表数据" + s);
                        try {
                            stat_zong_eNttyList.clear();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                count = jo2.getString("count");
                                String sum_cash = jo2.getString("sum_cash");
                                String sum_orders = jo2.getString("sum_orders");
                                String sum = jo2.getString("sum");
                                String sum_lirun = jo2.getString("sum_lirun");
                                JSONArray ja1 = jo2.getJSONArray("detail");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Stat_Zong_ENtty stat_zong_eNtty = new Stat_Zong_ENtty();
                                    JSONObject jo3 = ja1.getJSONObject(i);
                                    stat_zong_eNtty.setPayment(jo3.getString("pay"));
                                    if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                        stat_zong_eNtty.setReceive_amount(jo3.getString("money"));
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(0 + "");
                                    } else {
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(jo3.getString("pay"));
                                        if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                            stat_zong_eNtty.setReceive_amount(TlossUtils.add(Double.parseDouble(jo3.getString("money")), Double.parseDouble(jo3.getString("pay"))) + "");
                                        }
                                    }
//                                    stat_zong_eNtty.setRemark(jo3.getString("remark"));
                                    stat_zong_eNtty.setWork_name(jo3.getString("worker_name"));
                                    stat_zong_eNtty.setId(jo3.getString("id"));
                                    stat_zong_eNtty.setTime(jo3.getString("time"));
                                    stat_zong_eNtty.setMoney(jo3.getString("money"));
                                    stat_zong_eNtty.setMember_id(jo3.getString("member_id"));
                                    stat_zong_eNtty.setLirun(jo3.getString("lirun"));
                                    stat_zong_eNttyList.add(stat_zong_eNtty);
                                }
                                tv_zong.setText(StringUtils.stringpointtwo(Float.parseFloat(sum)+""));
                                tv_size.setText(count + "");
                                if (!sum_orders.equals("null")) {
                                    tv_micro.setText(Double.parseDouble(sum_orders)+"");
                                } else {
                                    tv_micro.setText(0.0 + "");
                                }
                                if (sum_cash.equals("null")) {
                                    tv_cash.setText("0");
                                } else {
                                    tv_cash.setText(sum_cash);
                                }
                                if (SharedUtil.getString("type").equals("4")){
                                    tv_lirun.setText("--");
                                }else {
                                    tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                }
//                                tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                if (Integer.valueOf(count) % 8 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                                }
                            }
                            adapter.getAdats(stat_zong_eNttyList);
                            lv_statem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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
        //DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(reserve_time)*1000)
        tv_timestart = (TextView) popwindow.findViewById(R.id.tv_timestart);
        tv_timestart.setOnClickListener(this);
        tv_timeend = (TextView) popwindow.findViewById(R.id.tv_timeend);
        tv_timeend.setOnClickListener(this);
    }

    public void setPrint(){

        String syy = BluetoothPrintFormatUtil.Printsale(SharedUtil.getString("name"),tv_time.getText().toString(),tv_user.getText().toString(),tv_pay.getText().toString(),tv_zong.getText().toString(),tv_cash.getText().toString(),tv_micro.getText().toString());
        PrintWired.usbPrint(getActivity(),syy);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_sale:
                setPrint();
                break;
            case R.id.tv_seek:
                mCustomPopWindow.showAsDropDown(tv_seek, 0, 20);
                break;
            case R.id.but_day:
                if (!daylist.get(0)) {
//                    but_day.setBackgroundColor(Color.parseColor("#FF6501"));
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
//                timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_timeend.setText(time);
//                        randomtime = true;
//                        isdiyi=false;
//                    }
//                }, tv_timestart.getText().toString(), getTime());
//                timeSelector.show();
                }
                break;
            case R.id.but_next:
                Log.d("print", "分页得数据为" + count);
                if (Integer.valueOf(count) % 8 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                    if (page < (Integer.valueOf(count) / 8)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
                            if (day.equals("6")){
                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
                            }else {
                                setfiltrate(day, sp1, sp2, sp3);
                            }
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                    if (page < (Integer.valueOf(count) / 8 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
                            if (day.equals("6")){
                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
                            }else {
                                setfiltrate(day, sp1, sp2, sp3);
                            }
                            paging1 = false;
                        }
                    }
                }
                break;
            case R.id.but_previous:
                if (page > 1) {
                    if (Integer.valueOf(count) % 8 == 0) {
                        tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                        page--;
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
                            if (day.equals("6")){
                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
                            }else {
                                setfiltrate(day, sp1, sp2, sp3);
                            }
                            paging2 = false;
                        }
                    } else {
                        tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                        page--;
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
                            if (day.equals("6")){
                                setfiltrate(day, tv_timestart.getText().toString(), tv_timeend.getText().toString());
                            }else {
                                setfiltrate(day, sp1, sp2, sp3);
                            }
                            paging2 = false;
                        }
                    }
                }
                break;
            case R.id.but_refund:
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_but_refund);
                Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
//                           全部退货
                Button but_confirm = (Button) window.findViewById(R.id.but_confirm);
                but_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int T=0;
                        for (int k=0;k<listcheck_tuihuo.size();k++){
                            if (listcheck_tuihuo.get(k).getShip_nums()!=null&&!listcheck_tuihuo.get(k).getShip_nums().equals("null")){
                                T+=Integer.parseInt(listcheck_tuihuo.get(k).getShip_nums());
                            }
                        }
                        if (T==0){

                        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
                        for (int i = 0; i < listcheck_tuihuo.size(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", listcheck_tuihuo.get(i).getSeller_name());
                            map.put("nums", listcheck_tuihuo.get(i).getUnms());
                            map.put("item_id",listcheck_tuihuo.get(i).getItem_id());
                            map.put("price", listcheck_tuihuo.get(i).getPrice());
                            map.put("sum", TlossUtils.mul(Double.parseDouble(listcheck_tuihuo.get(i).getPrice()) , Double.parseDouble(listcheck_tuihuo.get(i).getUnms().toString()))+"");
                            map.put("operator", SharedUtil.getString("operator_id"));
                            map.put("goods_id", listcheck_tuihuo.get(i).getItem_id());
                            mapList.add(map);
                        }
                        Gson gson = new Gson();
                        String str = gson.toJson(mapList);
                        Log.d("print", "反结账数据" + str);
                        OkGo.post(SysUtils.getSellerServiceUrl("refund"))
                                .tag(getContext())
                                .cacheKey("cacheKey")
                                .cacheMode(CacheMode.DEFAULT)
                                .params("type", 1)
                                .params("order_id", order_id)
                                .params("worker_name", SharedUtil.getString("name"))
                                .params("items", str)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print", "反结账" + s);
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                            JSONObject jo2 = jo1.getJSONObject("data");
                                            String usersname = jo2.getString("usersname");
                                            String memo = jo2.getString("memo");
                                            String order_id1 = jo2.getString("order_id");
                                            String time = jo2.getString("time");
                                            list_refund.clear();
                                            JSONArray ja1 = jo2.getJSONArray("name_price_nums");
                                            Float heji = 0f;
                                            for (int j = 0; j < ja1.length(); j++) {
                                                Refund_entty refund_entty = new Refund_entty();
                                                JSONObject jo3 = ja1.getJSONObject(j);
                                                refund_entty.setName(jo3.getString("name"));
                                                String nums = jo3.getString("nums");
                                                refund_entty.setNums(nums);
                                                String price = jo3.getString("price");
                                                refund_entty.setPrice(price);
                                                heji += (Float.parseFloat(nums) * Float.parseFloat(price));
                                                list_refund.add(refund_entty);
                                                Toast.makeText(getActivity(), "退货成功", Toast.LENGTH_SHORT).show();
                                                lv_statem.setVisibility(View.VISIBLE);
                                            }
                                            PrintUtil printUtil1 = new PrintUtil(getActivity());

                                            printUtil1.openButtonClicked();
                                            printUtil1.PrintString(StringUtils.stringpointtwo(heji + ""), "-" + StringUtils.stringpointtwo(heji + ""), "0", order_id1, time, 0, list_refund);
                                            dialog.dismiss();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }else {
                            Toast.makeText(getContext(),"有退货不能全部退款",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_delete:
                if (ll_xiangcitem.getVisibility() == View.VISIBLE) {
                    ll_xiangcitem.setVisibility(View.GONE);
                    lv_statem.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.but_updatas:
                upnotnet();
                break;
        }
    }
    public Sqlite_Entity sqlite_entity;
    //上传无网的数据
    private void upnotnet(){
        sqlite_entity=new Sqlite_Entity(getActivity());
        String str=sqlite_entity.QueryOrder();
        if (!str.equals("")){
            upNoInternet(str);
        }else {
            Toast.makeText(getActivity(),"没有数据上传",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 无网数据上传的接口
     * @param str
     */
    private void upNoInternet(String str) {

        OkGo.post(SysUtils.getSellerServiceUrl("not_network_cash_pay"))
                .tag(this)
                .params("map", str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "上传成功的数据为" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                sqlite_entity=new Sqlite_Entity(getActivity());
                                sqlite_entity.deleteorder();
//                                sqLiteDatabase.execSQL(("delete from  ProOut"));
//                                sqLiteDatabase.execSQL(("delete from  goodsSell"));
                                Toast.makeText(getActivity(), "数据上传成功", Toast.LENGTH_SHORT).show();
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



    @Override
    public void Setbutonclick(final int i) {

//        Log.d("","点击了"+i+stat_zong_eNttyList.get(i).getWork_name());
        lv_statem.setVisibility(View.GONE);
        ll_xiangcitem.setVisibility(View.VISIBLE);
        order_id = stat_zong_eNttyList.get(i).getId();

        getdetails(stat_zong_eNttyList.get(i).getId());

        leibu_adapter.setOntuihuoonclick(this);

        tv_discount.setText(stat_zong_eNttyList.get(i).getRemark());
        tv_serial.setText(stat_zong_eNttyList.get(i).getId());
        tv_lei_time.setText(TimeZoneUtil.getTime1((Long.valueOf(stat_zong_eNttyList.get(i).getTime()) * 1000)));

        if (stat_zong_eNttyList.size()>0){
//        if (stat_zong_eNttyList.get(i).getWork_name()==null&&stat_zong_eNttyList.get(i).getWork_name().equals("null")&&stat_zong_eNttyList.get(i).getWork_name().equals("")){
//            tv_lei_name.setText(SharedUtil.getString("seller_name"));
//        }else {
            tv_lei_name.setText(stat_zong_eNttyList.get(i).getWork_name());
//        }
        }


        if (stat_zong_eNttyList.get(i).getPayment().equals("null")) {
            tv_lei_mode.setText("移动支付");
            j = 0;
            payType=1;
        } else {
            if (!StringUtils.isNumber1(stat_zong_eNttyList.get(i).getPayment())) {
                tv_lei_mode.setText("移动支付");
                j = 0;
                payType=1;
            } else {
                tv_lei_mode.setText("现金支付");
                j = 1;
                payType=2;
            }
        }


//        if (!StringUtils.isNumber1(stat_zong_eNttyList.get(i).getPayment())) {
//            tv_lei_mode.setText("移动支付");
//            j = 0;
//            payType=1;
//        } else {
//            tv_lei_mode.setText("现金支付");
//            j = 1;
//            payType=2;
//        }
        tv_shiji.setText(TlossUtils.add(Double.parseDouble(stat_zong_eNttyList.get(i).getAmount_receivable()), Double.parseDouble(stat_zong_eNttyList.get(i).getAdd_change())) + "");
        tv_lei_zong.setText(stat_zong_eNttyList.get(i).getAmount_receivable());
        tv_zhaoling.setText(stat_zong_eNttyList.get(i).getAdd_change());
        but_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Refund_entty> listadats=new ArrayList<Refund_entty>();
                for (int k=0;k<listcheck_tuihuo.size();k++){
                    Refund_entty refund=new Refund_entty();
                    refund.setName(listcheck_tuihuo.get(k).getSeller_name());
                    refund.setNums(listcheck_tuihuo.get(k).getUnms());
                    refund.setPrice(listcheck_tuihuo.get(k).getPrice());
                    listadats.add(refund);
                }
                //小票打印
                PrintUtil.receiptPrint(getActivity(),tel,order_id,0,tv_lei_time.getText().toString(),0,tv_lei_zong.getText().toString(),tv_shiji.getText().toString(),tv_zhaoling.getText().toString(),
                        payType,mSelf_Service_GoodsInfo,"","","","");
            }
        });

        details = true;
    }

    public void getdetails(final String order_id1) {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                .tag(getActivity())
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id", order_id1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "详细信息" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            listcheck_tuihuo.clear();
                            if (status.equals("200")) {
                                if(mSelf_Service_GoodsInfo!=null){
                                    mSelf_Service_GoodsInfo.clear();
                                }
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Check_tuihuo check_tuihuo = new Check_tuihuo();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    check_tuihuo.setItem_id(jo2.getString("item_id"));
                                    check_tuihuo.setUnms(jo2.getString("nums"));
                                    check_tuihuo.setShip_nums(jo2.getString("ship_nums"));
                                    check_tuihuo.setPrice(jo2.getString("price"));
                                    check_tuihuo.setGoods_id(jo2.getString("goods_id"));
                                    check_tuihuo.setOrder_id(order_id1 + "");
                                    check_tuihuo.setSeller_name(jo2.getString("name"));
                                    Self_Service_GoodsInfo self_service_goodsInfo=new Self_Service_GoodsInfo(jo2.getString("goods_id"),jo2.getString("name"),
                                            jo2.getString("nums"),"",jo2.getString("price"),"","","","");
                                    mSelf_Service_GoodsInfo.add(self_service_goodsInfo);
                                    int in = 0;
                                    if (listcheck_tuihuo.size() > 0) {
                                        aa:
                                        for (int k = 0; k < listcheck_tuihuo.size(); k++) {
                                            if (listcheck_tuihuo.get(k).getGoods_id().equals(check_tuihuo.getGoods_id())) {
                                                in = in + (k + 1);
                                                listcheck_tuihuo.get(k).setUnms((Integer.parseInt(check_tuihuo.getUnms()) + 1) + "");
                                                break aa;
                                            }
                                        }
                                        if (in == 0) {
                                            listcheck_tuihuo.add(check_tuihuo);
                                        }
                                    } else {

                                        if (listcheck_tuihuo.size() > 0) {
                                            listcheck_tuihuo.add(listcheck_tuihuo.size() - 1, check_tuihuo);
                                        } else {
                                            listcheck_tuihuo.add(0, check_tuihuo);
                                        }
                                    }
                                }
                                leibu_adapter.getAdats(listcheck_tuihuo);
                                lv_details.setAdapter(leibu_adapter);
                                leibu_adapter.notifyDataSetChanged();
                            } else {
                                leibu_adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void itmeonclick(int i) {
        showDialog(i);
    }

    public void showDialog(final int i) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("退货详情");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.refund_layout);
        final TextView tv_nums = (TextView) window.findViewById(R.id.tv_nums);
        ImageView im_reductionof = (ImageView) window.findViewById(R.id.im_reductionof);
        ImageView im_add = (ImageView) window.findViewById(R.id.im_add);
        final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);
        tv_nums.setText(listcheck_tuihuo.get(i).getUnms());
        im_reductionof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tv_nums.getText().toString()) > 1) {
                    int j = Integer.valueOf(tv_nums.getText().toString()) - 1;
                    tv_nums.setText(j + "");
                }
            }
        });
        im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tv_nums.getText().toString()) < Integer.valueOf(listcheck_tuihuo.get(i).getUnms())) {
                    int j = Integer.valueOf(tv_nums.getText().toString()) + 1;
                    tv_nums.setText(j + "");
                }
            }
        });
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_describe.getText().toString().equals("")) {
                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                    for (int k = 0; k < 1; k++) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", listcheck_tuihuo.get(i).getSeller_name());
                        map.put("nums", tv_nums.getText().toString());
                        map.put("price", StringUtils.stringpointtwo(Float.valueOf(listcheck_tuihuo.get(i).getPrice()) + ""));
                        map.put("sum", TlossUtils.mul(Double.parseDouble(listcheck_tuihuo.get(i).getPrice()) , Double.parseDouble(tv_nums.getText().toString()))+"");
                        map.put("item_id", listcheck_tuihuo.get(i).getItem_id());
                        map.put("goods_id", listcheck_tuihuo.get(i).getGoods_id());
                        list.add(map);
                        if (j == 0) {
                            payment = "micro";
                        }
                        if (j == 1) {
                            payment = "cash";
                        }
                    }
                    Gson gson = new Gson();
                    String str = gson.toJson(list);
                    Log.d("print", "退货的数据" + str);
                    OkGo.post(SysUtils.getSellerServiceUrl("refund"))
                            .tag(getActivity())
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.DEFAULT)
                            .params("operator", SharedUtil.getString("operator_id"))
                            .params("order_id", listcheck_tuihuo.get(i).getOrder_id())
                            .params("nums", tv_nums.getText().toString())
                            .params("memo", ed_describe.getText().toString())
                            .params("type", 0)
                            .params("worker_name", SharedUtil.getString("name"))
                            .params("payment", payment)
                            .params("items", str)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d("print", "退货的" + s);
                                    try {
                                        list_refund.clear();
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONObject jo1 = jsonObject.getJSONObject("response");
                                        String status = jo1.getString("status");
                                        if (status.equals("200")) {
                                            Refund_entty refund_entty = new Refund_entty();
                                            JSONObject jo2 = jo1.getJSONObject("data");
                                            String usersname = jo2.getString("usersname");
                                            refund_entty.setUsersname(usersname);
                                            String name = jo2.getString("name");
                                            refund_entty.setName(name);
                                            String price = jo2.getString("price");
                                            refund_entty.setPrice(price);
                                            String nums = jo2.getString("nums");
                                            refund_entty.setNums(nums);
                                            String order_id1 = jo2.getString("order_id");
                                            refund_entty.setOrder_id(order_id1);
                                            String time = jo2.getString("time");
                                            refund_entty.setTime(time);
                                            Log.d("print", "usersname" + usersname + "name" + name + "price" + price
                                                    + "nums" + nums + "order_id" + order_id1 + "time" + time);
                                            list_refund.add(refund_entty);
                                            PrintUtil printUtil1 = new PrintUtil(getActivity());
//
                                            Float heji = (Float.parseFloat(nums) * Float.parseFloat(price));
                                            printUtil1.openButtonClicked();
                                            printUtil1.PrintString(StringUtils.stringpointtwo(heji + ""), "-" + StringUtils.stringpointtwo(heji + ""), "0", order_id1, time, 0, list_refund);
                                            //判断软键盘是否显示
//                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                                            Toast.makeText(getContext(), "退货成功", Toast.LENGTH_SHORT).show();
                                            lv_statem.setVisibility(View.VISIBLE);
                                            ll_xiangcitem.setVisibility(View.GONE);
                                            details = false;
                                            dialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                }else {
                    Toast.makeText(getActivity(),"请输入备注",Toast.LENGTH_SHORT).show();
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

    //筛选得方法
    public void setfiltrate(String day, String sp1, String sp2, String sp3) {
        Log.d("print", "筛选得方法" + work_id);
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_total_price"))
                .tag(getActivity())
                .params("payment", sp2)
                .params("page", page)
                .params("first", "first")
                .params("day", day)
                .params("work_id", work_id)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

                        Log.d("print", "上传的数据是" + request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "报表数据" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            stat_zong_eNttyList.clear();
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                count = jo2.getString("count");
                                String sum_cash = jo2.getString("sum_cash");
                                String sum_orders = jo2.getString("sum_orders");
                                String sum = jo2.getString("sum");
                                String sum_lirun = jo2.getString("sum_lirun");
                                JSONArray ja1 = jo2.getJSONArray("detail");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Stat_Zong_ENtty stat_zong_eNtty = new Stat_Zong_ENtty();
                                    JSONObject jo3 = ja1.getJSONObject(i);
                                    stat_zong_eNtty.setPayment(jo3.getString("pay"));
                                    if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                        stat_zong_eNtty.setReceive_amount(jo3.getString("money"));
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(0 + "");
                                    } else {
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(jo3.getString("pay"));
                                        if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                            stat_zong_eNtty.setReceive_amount(TlossUtils.add(Double.parseDouble(jo3.getString("money")), Double.parseDouble(jo3.getString("pay"))) + "");
                                        }
                                    }
                                    stat_zong_eNtty.setWork_name(jo3.getString("worker_name"));
                                    stat_zong_eNtty.setId(jo3.getString("id"));
                                    stat_zong_eNtty.setTime(jo3.getString("time"));
                                    stat_zong_eNtty.setMoney(jo3.getString("money"));
                                    stat_zong_eNtty.setMember_id(jo3.getString("member_id"));
                                    stat_zong_eNtty.setLirun(jo3.getString("lirun"));
                                    stat_zong_eNttyList.add(stat_zong_eNtty);
                                }
                                tv_zong.setText(StringUtils.stringpointtwo(Float.parseFloat(sum)+""));
                                tv_size.setText(count);
                                if (!sum_orders.equals("null")) {
                                    tv_micro.setText(Double.parseDouble(sum_orders)+"");
                                } else {
                                    tv_micro.setText(0.0 + "");
                                }
                                tv_cash.setText(sum_cash);
                                if (SharedUtil.getString("type").equals("4")){
                                    tv_lirun.setText("--");
                                }else {
                                    tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                }
//                                tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                if (Integer.valueOf(count) % 8 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                                }
                            }
                            Log.d("print", "" + stat_zong_eNttyList);
                            adapter.getAdats(stat_zong_eNttyList);
                            lv_statem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //任意时间筛选筛选
    public void setfiltrate(String day, String day1, String day2) {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_total_price"))
                .tag(getActivity())
                .params("day", day)
                .params("page", page)
                .params("first", "first")
                .params("begin_time", day1)
                .params("end_time", day2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            stat_zong_eNttyList.clear();
                            tv_zong.setText("0");
                            tv_micro.setText("0");
                            tv_cash.setText("0");
                            tv_size.setText("0");
                            tv_lirun.setText("0");
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                count = jo2.getString("count");
                                String sum_cash = jo2.getString("sum_cash");
                                String sum_orders = jo2.getString("sum_orders");
                                String sum = jo2.getString("sum");
                                String sum_lirun = jo2.getString("sum_lirun");
                                JSONArray ja1 = jo2.getJSONArray("detail");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Stat_Zong_ENtty stat_zong_eNtty = new Stat_Zong_ENtty();
                                    JSONObject jo3 = ja1.getJSONObject(i);
                                    stat_zong_eNtty.setPayment(jo3.getString("pay"));
                                    if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                        stat_zong_eNtty.setReceive_amount(jo3.getString("money"));
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(0 + "");
                                    } else {
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(jo3.getString("pay"));
                                        if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                            stat_zong_eNtty.setReceive_amount(TlossUtils.add(Double.parseDouble(jo3.getString("money")), Double.parseDouble(jo3.getString("pay"))) + "");
                                        }
                                    }
                                    stat_zong_eNtty.setId(jo3.getString("id"));
                                    stat_zong_eNtty.setTime(jo3.getString("time"));
                                    stat_zong_eNtty.setMoney(jo3.getString("money"));
                                    stat_zong_eNtty.setMember_id(jo3.getString("member_id"));
                                    stat_zong_eNtty.setLirun(jo3.getString("lirun"));
                                    stat_zong_eNttyList.add(stat_zong_eNtty);
                                }
                                tv_zong.setText(StringUtils.stringpointtwo(Float.parseFloat(sum)+""));
                                tv_size.setText(count);
                                if (!sum_orders.equals("null")) {
                                    tv_micro.setText(Double.parseDouble(sum_orders)+"");
                                } else {
                                    tv_micro.setText(0.0 + "");
                                }
                                tv_cash.setText(sum_cash);
                                if (SharedUtil.getString("type").equals("4")){
                                    tv_lirun.setText("--");
                                }else {
                                    tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                }
                                if (Integer.valueOf(count) % 8 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                                }
                            }
                            Log.d("print", "" + stat_zong_eNttyList);
                            adapter.getAdats(stat_zong_eNttyList);
                            lv_statem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //有效单据
    public void setcredit(final String str, String string, String day) {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_total_price"))
                .tag(getActivity())
                .params("tuihuo_flag", string)
                .params("order_status", str)
                .params("page", page)
                .params("day", day)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "报表得数据赊账" + str + s);
                        try {
                            stat_zong_eNttyList.clear();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                count = jo2.getString("count");
                                String sum_cash = jo2.getString("sum_cash");
                                String sum_orders = jo2.getString("sum_orders");
                                String sum = jo2.getString("sum");
                                String sum_lirun = jo2.getString("sum_lirun");
                                JSONArray ja1 = jo2.getJSONArray("detail");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Stat_Zong_ENtty stat_zong_eNtty = new Stat_Zong_ENtty();
                                    JSONObject jo3 = ja1.getJSONObject(i);
                                    stat_zong_eNtty.setPayment(jo3.getString("pay"));
                                    if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                        stat_zong_eNtty.setReceive_amount(jo3.getString("money"));
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(0 + "");
                                    } else {
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(jo3.getString("pay"));
                                        if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                            stat_zong_eNtty.setReceive_amount(TlossUtils.add(Double.parseDouble(jo3.getString("money")), Double.parseDouble(jo3.getString("pay"))) + "");
                                        }
                                    }
                                    stat_zong_eNtty.setId(jo3.getString("id"));
                                    stat_zong_eNtty.setTime(jo3.getString("time"));
                                    stat_zong_eNtty.setMoney(jo3.getString("money"));
                                    stat_zong_eNtty.setMember_id(jo3.getString("member_id"));
                                    stat_zong_eNtty.setLirun(jo3.getString("lirun"));
                                    stat_zong_eNttyList.add(stat_zong_eNtty);
                                }
                                tv_zong.setText(StringUtils.stringpointtwo(Float.parseFloat(sum)+""));
                                tv_size.setText(count);
                                if (!sum_orders.equals("null")) {
                                    tv_micro.setText(Double.parseDouble(sum_orders)+"");
                                } else {
                                    tv_micro.setText(0.0 + "");
                                }
                                tv_cash.setText(sum_cash);
                                if (SharedUtil.getString("type").equals("4")){
                                    tv_lirun.setText("--");
                                }else {
                                    tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                }
//                                tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                if (Integer.valueOf(count) % 8 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                                }
                            }
                            Log.d("print", "" + stat_zong_eNttyList);
                            adapter.getAdats(stat_zong_eNttyList);
                            lv_statem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void setcredit(final String str, String string, String day1, String day2) {
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_total_price"))
                .tag(getActivity())
                .params("page", page)
                .params("tuihuo_flag", string)
                .params("order_status", str)
                .params("begin_time", day1)
                .params("end_time", day2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "报表得数据赊账" + str + s);
                        try {
                            stat_zong_eNttyList.clear();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo2 = jo1.getJSONObject("data");
                                count = jo2.getString("count");
                                String sum_cash = jo2.getString("sum_cash");
                                String sum_orders = jo2.getString("sum_orders");
                                String sum = jo2.getString("sum");
                                String sum_lirun = jo2.getString("sum_lirun");
                                JSONArray ja1 = jo2.getJSONArray("detail");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Stat_Zong_ENtty stat_zong_eNtty = new Stat_Zong_ENtty();
                                    JSONObject jo3 = ja1.getJSONObject(i);
                                    stat_zong_eNtty.setPayment(jo3.getString("pay"));
                                    if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                        stat_zong_eNtty.setReceive_amount(jo3.getString("money"));
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(0 + "");
                                    } else {
                                        stat_zong_eNtty.setAmount_receivable(jo3.getString("money"));
                                        stat_zong_eNtty.setAdd_change(jo3.getString("pay"));
                                        if (!StringUtils.isNumber1(jo3.getString("pay"))) {
                                            stat_zong_eNtty.setReceive_amount(TlossUtils.add(Double.parseDouble(jo3.getString("money")), Double.parseDouble(jo3.getString("pay"))) + "");
                                        }
                                    }
                                    stat_zong_eNtty.setId(jo3.getString("id"));
                                    stat_zong_eNtty.setTime(jo3.getString("time"));
                                    stat_zong_eNtty.setMoney(jo3.getString("money"));
                                    stat_zong_eNtty.setMember_id(jo3.getString("member_id"));
                                    stat_zong_eNtty.setLirun(jo3.getString("lirun"));
                                    stat_zong_eNttyList.add(stat_zong_eNtty);
                                }
                                tv_zong.setText(StringUtils.stringpointtwo(Float.parseFloat(sum)+""));
                                tv_size.setText(count);
                                if (!sum_orders.equals("null")) {
                                    tv_micro.setText(Double.parseDouble(sum_orders)+"");
                                } else {
                                    tv_micro.setText(0.0 + "");
                                }
                                tv_cash.setText(sum_cash);
                                if (SharedUtil.getString("type").equals("4")){
                                    tv_lirun.setText("--");
                                }else {
                                    tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                }
//                                tv_lirun.setText(StringUtils.stringpointtwo(Float.parseFloat(sum_lirun)+""));
                                if (Integer.valueOf(count) % 8 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(count) / 8 + 1));
                                }
                            }
                            Log.d("print", "" + stat_zong_eNttyList);
                            adapter.getAdats(stat_zong_eNttyList);
                            lv_statem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //    收银员
    public void setemployee() {
        OkGo.post(SysUtils.getSellerServiceUrl("get_all_employee"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "收银员的数据" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                cashier_enttyList.clear();
                                for (int i = 0; i < ja1.length(); i++) {
                                    Cashier_entty cashier_entty = new Cashier_entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    cashier_entty.setLogin_name(jo2.getString("login_name"));
                                    cashier_entty.setWork_id(jo2.getString("work_id"));
                                    cashier_enttyList.add(cashier_entty);
                                }
                                Cashier_entty cashier_entty = new Cashier_entty();
                                cashier_entty.setLogin_name("全部收银员");
                                cashier_entty.setWork_id("");
                                cashier_enttyList.add(0, cashier_entty);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
