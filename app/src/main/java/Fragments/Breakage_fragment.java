package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entty.Breakage_entty;
import Entty.Commodity;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_Fuzzy;
import adapters.Adapter_breakage;
import base.BaseFragment;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import shujudb.SqliteHelper;

/**
 * Created by admin on 2017/5/24.
 */
public class Breakage_fragment extends BaseFragment implements View.OnClickListener, Adapter_Fuzzy.SetOnclick {
    public View view;
    public ListView lv;
    public EditText ed_seek;
    public Button but_addbreakage;
    public int number = 0;

    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Commodity commodity;
    public Adapter_breakage adapter;

    public List<Breakage_entty> adats;
    public TextView tv_filtrate,tv_total;

    public CustomPopWindow mCustomPopWindow;
    public String day="-1";
    public String startTime="",endtime="";
    public List<Boolean> daylist;
    public Button but_day,but_triduum,but_week,but_month,but_lastmonth;
    public TextView tv_timestart,tv_timeend;
    public TimeSelector timeSelector;
    //判断开始时间是否选择了
    public Boolean isdiyi=false;

    public double total,excessive;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.breakage_fragment,null);
//        init();
//        Loadat();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.breakage_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
        Loadat();
    }

    private void init1(View view) {

        daylist=new ArrayList<>();
        daylist.clear();
        for (int i=0;i<6;i++){
            daylist.add(false);
        }

        adats=new ArrayList<>();
        adapter=new Adapter_breakage(getContext());

        ed_seek= (EditText) view.findViewById(R.id.ed_seek);
        lv= (ListView) view.findViewById(R.id.lv);
        but_addbreakage= (Button) view.findViewById(R.id.but_addbreakage);
        but_addbreakage.setOnClickListener(this);

        tv_total= (TextView) view.findViewById(R.id.tv_total);
        tv_filtrate= (TextView) view.findViewById(R.id.tv_filtrate);
        tv_filtrate.setOnClickListener(this);
        //数据库的操作
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

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
//                getreport_list(str1);
                Loadat();
                mCustomPopWindow.dissmiss();
            }
        });

        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(popwindow)
                .create();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_filtrate:
                mCustomPopWindow.showAsDropDown( tv_filtrate, 0, 20);
                break;
            case R.id.but_addbreakage:
                Seek_Dialog();

//                DialogUIUtils.showCustomAlert(getActivity(),dialog,Gravity.CENTER,true,false).show();
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
        }
    }


    //订单商品添加数量与备注
    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    InputMethodManager  imm;
    public Adapter_Fuzzy adapter_fuzzy = new Adapter_Fuzzy(getActivity());
    public List<Commodity> list_fuzzy = new ArrayList<Commodity>();

    /**
     * 添加报损的弹窗
     */

    public void settete(final Commodity commodity){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("报损商品");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_breakage);
        final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
        final EditText ed_number= (EditText) window.findViewById(R.id.ed_number);
        final EditText ed_content= (EditText) window.findViewById(R.id.ed_content);
        ed_number.setText(1+"");
        ed_bncode.setText(commodity.getName());
        number =Integer.parseInt(ed_number.getText().toString());
        ImageView im_reductionof= (ImageView) window.findViewById(R.id.im_reductionof);
        ImageView im_add= (ImageView) window.findViewById(R.id.im_add);
        Button but_submit= (Button) window.findViewById(R.id.but_submit);
//                减号得监听
                im_reductionof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.parseInt(ed_number.getText().toString())>1){
                            number--;
                            ed_number.setText(number+"");
                        }else {
                            Toast.makeText(getActivity(),"不能在减了",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                im_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            number++;
                            ed_number.setText(number+"");
                    }
                });

                but_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //查询数据库的报损商品详情
                        ed_number.getText().toString();
                        ed_content.getText().toString();
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("nums",ed_number.getText().toString());
                        if (commodity!=null) {
                            map.put("store", commodity.getStore());
                        }
                        if (SharedUtil.getString("type").equals("4")) {
                            map.put("work_id", "0");
                        } else {
                            map.put("work_id", SharedUtil.getString("seller_id"));
                        }
                        if (commodity!=null) {
                            map.put("goods_id", commodity.getGoods_id());
                        }
                        map.put("desc",ed_content.getText().toString());

                        Gson gson=new Gson();
                        String str=gson.toJson(map);
                        getsubmit(str);
                        dialog.dismiss();
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

    /**
     * 本地搜索的弹窗
     */
    public void Seek_Dialog(){
        cancelDailog();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        view_add_nums_notes = View.inflate(getActivity(), R.layout.new_fuzzy, null);
        Button btn_cell_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_cell_dialog);
        Button btn_sure_dialog= (Button) view_add_nums_notes.findViewById(R.id.btn_sure_dialog);
        final ListView lv_fuzzy= (ListView) view_add_nums_notes.findViewById(R.id.lv_fuzzy);
        btn_cell_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog_add_nums_notes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        final EditText ed_mark_text= (EditText) view_add_nums_notes.findViewById(R.id.ed_mark_text);
        btn_sure_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSeek(ed_mark_text.getText().toString(),lv_fuzzy);
            }
        });


        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
        }
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();

    }

    /**
     * 关闭弹窗
     */
    public void cancelDailog(){
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * 本地搜索的接口
     * @param str
     * @param lv_fuzzy
     */
    public void LocalSeek(String str,final ListView lv_fuzzy){
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor=null;
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        if (m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode like " + "'%" + str + "%'", null);
        }
        p=Pattern.compile("[a-zA-Z]*");
        m=p.matcher(str);
        if(m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where py like " + "'%" + str + "%'", null);
        }
        p=Pattern.compile("[\u4e00-\u9fa5]*");
        m=p.matcher(str);
        if(m.matches()){
            cursor = sqLiteDatabase.rawQuery("select * from commodity where name like " + "'%" + str + "%'", null);
        }

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                getseek(str, lv_fuzzy);
            } else {
                list_fuzzy.clear();
                while (cursor.moveToNext()) {
                    Commodity fuzzy = new Commodity();
                    fuzzy.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
                    fuzzy.setName(cursor.getString(cursor.getColumnIndex("name")));
                    fuzzy.setPy(cursor.getString(cursor.getColumnIndex("py")));
                    fuzzy.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    fuzzy.setCost(cursor.getString(cursor.getColumnIndex("cost")));
                    fuzzy.setStore(cursor.getString(cursor.getColumnIndex("store")));
                    fuzzy.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
                    fuzzy.setCook_position(cursor.getString(cursor.getColumnIndex("cook_position")));
                    fuzzy.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
                    fuzzy.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
                    list_fuzzy.add(fuzzy);
                }
                adapter_fuzzy = new Adapter_Fuzzy(getActivity());
                adapter_fuzzy.setAdats(list_fuzzy);
                adapter_fuzzy.SetOnclick(Breakage_fragment.this);
                lv_fuzzy.setAdapter(adapter_fuzzy);
                adapter_fuzzy.notifyDataSetChanged();
            }
        }
    }

    /**
     * 线上搜索商品的方法
     * @param str
     * @param lv_fuzzy
     */
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
                                    commodity.setIs_special_offer(jo2.getString("is_special"));
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    list_fuzzy.add(commodity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter_fuzzy = new Adapter_Fuzzy(getActivity());
                            adapter_fuzzy.setAdats(list_fuzzy);
                            adapter_fuzzy.SetOnclick(Breakage_fragment.this);
                            lv_fuzzy.setAdapter(adapter_fuzzy);
                            adapter_fuzzy.notifyDataSetChanged();
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

    public void getsubmit(String string){
        OkGo.post(SysUtils.getGoodsServiceUrl("bad_goods_add"))
                .tag(getActivity())
                .params("map",string)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","报损"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            Loadat();
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

    //获取报损数据
    private void Loadat() {
        total=0;
        OkGo.post(SysUtils.getGoodsServiceUrl("bad_goods_list"))
                .tag(getActivity())
                .params("startTime",startTime)
                .params("endTime",endtime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","报损的列表"+s);
                        try {
                            adats.clear();
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    Breakage_entty breakage=new Breakage_entty();
                                    breakage.setAddtime(jo2.getString("addtime"));
                                    breakage.setBncode(jo2.getString("bncode"));
                                    breakage.setCost(jo2.getString("cost"));
                                    breakage.setLogin_name(jo2.getString("login_name"));
                                    breakage.setName(jo2.getString("name"));
                                    breakage.setNums(jo2.getString("nums"));
                                    breakage.setDesc(jo2.getString("desc"));
                                    excessive= TlossUtils.mul(Double.parseDouble(jo2.getString("nums")),Double.parseDouble(jo2.getString("cost")));
                                    total= TlossUtils.add(total,excessive);
                                    adats.add(breakage);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_total.setText(total+"");
                            adapter.setAdats(adats);
                            lv.setAdapter(adapter);
                        }
                    }
                });
    }

    /**
     * 选择商品
     * @param i
     */
    @Override
    public void setClickListener(int i) {
        settete(list_fuzzy.get(i));
        cancelDailog();
    }
}
