package retail.yzx.com.kz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import Entty.Member_entty;
import Entty.Pay_success;
import Entty.Recharge_smsEntty;
import Entty.Specification_Entty;
import Entty.Template_Entty;
import Utils.DateUtils;
import Utils.QRCode;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Adapter_sms_Template;
import adapters.Gridview_member_adapter;
import adapters.Spadapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import widget.MyGirdView;
import widget.ShapeLoadingDialog;
import widget.Switch;

import static retail.yzx.com.kz.NetWorkService.isNetBad;

/**
 * Created by admin on 2017/8/10.
 * 会员
 */
public class Member_Activity extends BaseActivity implements View.OnClickListener, Gridview_member_adapter.SetOnclickitme, Adapter_sms_Template.SetOnclick, Spadapter.Oneidtextonclick {

    public EditText ed_member_name,ed_phone,ed_money
            ,ed_score,tv_integral,ed_discount_rate,ed_password,ed_remark;

    public Button but_new_members,but_delete_members,but_sms;
    public TextView tv_huanghui;
    public Member_entty member_entty;
    public TextView ed_member_bncoder,tv_day,ed_birthday;

    //时间选择器
    public TimeSelector timeSelector;
    public Switch sw_password;
    public boolean is_require_pass=false;
    boolean isSend=false;
    //加载数据的弹窗
    public ShapeLoadingDialog loadingdialog;
    //订单商品添加数量与备注
    private View view_add_nums_notes;
    private android.support.v7.app.AlertDialog mAlertDialog_add_nums_notes;
    InputMethodManager  imm;

    //充值的定时器
    Handler handlernew;
    Runnable runnablenew;

    //充值规格的按钮
    private MyGirdView gl_recharge;
    private Gridview_member_adapter adapter;
    private List<Specification_Entty> specification_entties;
    private Recharge_smsEntty recharge_smsEntty;

    //会员等级&享受会员价
    private String member_lv_custom_id,member_lv_custom_key;
    private TextView tv_grade,tv_price;
    private RelativeLayout rl_lv_member,rl_grade;

    @Override
    protected int getContentId() {
        return R.layout.dialog_member;
    }

    @Override
    protected void init() {
        super.init();

//        getLock();

        specification_entties=new ArrayList<>();
        for (int i=0;i<6;i++){
            Specification_Entty specification_entty=new Specification_Entty();
            specification_entty.setGive("1"+i);
            specification_entty.setVal("1"+i);
            specification_entties.add(specification_entty);
        }
        sw_password= (Switch) findViewById(R.id.sw_password);

        ed_member_bncoder= (TextView) findViewById(R.id.ed_member_bncoder);
        tv_day= (TextView) findViewById(R.id.tv_day);
        tv_day.setOnClickListener(this);
        tv_day.setText(DateUtils.getNowtimeKeyStr());
        ed_member_name= (EditText) findViewById(R.id.ed_member_name);
        ed_member_name.requestFocus();

        rl_lv_member= (RelativeLayout) findViewById(R.id.rl_lv_member);
        rl_grade= (RelativeLayout) findViewById(R.id.rl_grade);
        rl_lv_member.setOnClickListener(this);
        rl_grade.setOnClickListener(this);

        ed_phone= (EditText) findViewById(R.id.ed_phone);
        ed_money= (EditText) findViewById(R.id.ed_money);
        DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false,true);
        ed_money.setKeyListener(numericOnlyListener);
        ed_score= (EditText) findViewById(R.id.ed_score);
        DigitsKeyListener numericOnlyListener1 = new DigitsKeyListener(false,true);
        ed_score.setKeyListener(numericOnlyListener1);
//        tv_integral= (EditText) findViewById(R.id.tv_integral);
        ed_discount_rate= (EditText) findViewById(R.id.ed_discount_rate);
        ed_password= (EditText) findViewById(R.id.ed_password);
        ed_remark= (EditText) findViewById(R.id.ed_remark);
        ed_birthday= (TextView) findViewById(R.id.ed_birthday);
        ed_birthday.setOnClickListener(this);

        but_new_members= (Button) findViewById(R.id.but_new_members);
        but_delete_members= (Button) findViewById(R.id.but_delete_members);
        but_new_members.setOnClickListener(this);

        but_sms= (Button) findViewById(R.id.but_sms);
        but_sms.setOnClickListener(this);

        tv_huanghui= (TextView) findViewById(R.id.tv_huanghui);
        tv_huanghui.setOnClickListener(this);
        sw_password.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (!isChecked){
                    is_require_pass=true;
                }else {
                    is_require_pass=false;
                }
            }
        });

        tv_grade= (TextView) findViewById(R.id.tv_grade);
        tv_price= (TextView) findViewById(R.id.tv_price);


        if (SharedUtil.getString("type").equals("4")){

            Log.d("printp","员工登陆的");
            ed_money.setFocusable(false);
            ed_money.setFocusableInTouchMode(false);

            ed_score.setFocusable(false);
            ed_score.setFocusableInTouchMode(false);

            ed_discount_rate.setFocusable(false);
            ed_discount_rate.setFocusableInTouchMode(false);
        }else {
            Log.d("printp","老板登陆的");


            ed_money.setFocusableInTouchMode(true);
            ed_money.setFocusable(true);
            ed_money.requestFocus();

            ed_score.setFocusableInTouchMode(true);
            ed_score.setFocusable(true);
            ed_score.requestFocus();

            ed_discount_rate.setFocusableInTouchMode(true);
            ed_discount_rate.setFocusable(true);
            ed_discount_rate.requestFocus();


            ed_member_name.setFocusableInTouchMode(true);
            ed_member_name.setFocusable(true);
            ed_member_name.requestFocus();
        }
    }

    public void getLock(){
        LayoutInflater inflater = LayoutInflater.from(Member_Activity.this);
        final View layout = inflater.inflate(R.layout.dialog_lockscreen, null);
        final EditText ed_password = (EditText) layout.findViewById(R.id.ed_password);
        ed_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        final TextView ed_text = (TextView) layout.findViewById(R.id.ed_user);
        ed_text.setText(SharedUtil.getString("seller_name"));
        final Button but_lock = (Button) layout.findViewById(R.id.but_lock);
        final Dialog dialog = new Dialog(Member_Activity.this);
        dialog.setCancelable(false);
        dialog.setTitle("锁屏");
        dialog.show();
        dialog.getWindow().setContentView(layout);

        but_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_password.getText().toString().equals(SharedUtil.getString("password"))) {
                    dialog.dismiss();
                }
            }
        });
    }
    @Override
    protected void loadDatas() {
        super.loadDatas();

        getMemberGrade();





    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_member);
//        StringUtils.HideBottomBar(Member_Activity.this);
////        init();
//        Intent intent=getIntent();
//        if(intent.getSerializableExtra("member") != null){
//            member_entty= (Member_entty) intent.getSerializableExtra("member");
//            Loaddatas();
//        }
//    }

    private void Loaddatas() {
        if (member_entty.getIs_require_pass().equals("no")){
            sw_password.setChecked(true);
            is_require_pass=false;
        }else {
            sw_password.setChecked(false);
            is_require_pass=true;
        }
        ed_phone.setText(member_entty.getMobile());
        ed_score.setText(member_entty.getScore());
        ed_money.setText(member_entty.getSurplus());
        ed_member_bncoder.setText(member_entty.getMember_id());
        ed_member_name.setText(member_entty.getMember_name());
        ed_discount_rate.setText(member_entty.getDiscount_rate());
        ed_password.setText(member_entty.getPwd());
        ed_remark.setText(member_entty.getRemark());
        if (!member_entty.getBirthday().equals("null")&&member_entty.getBirthday()!=null){
        ed_birthday.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(member_entty.getBirthday())*1000));
        }
        tv_day.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(member_entty.getTime())*1000));
        but_delete_members.setVisibility(View.VISIBLE);
        but_new_members.setText("保存");
        but_delete_members.setOnClickListener(this);
        but_sms.setVisibility(View.VISIBLE);


        tv_grade.setText(member_entty.getMember_lv_custom_name());
        if (adats.size()>Integer.parseInt(member_entty.getMember_lv_custom_key())){
            if ((Integer.parseInt(member_entty.getMember_lv_custom_key()))>0){
                tv_price.setText(adats.get(Integer.parseInt(member_entty.getMember_lv_custom_key())).getName());
            }else {
                tv_price.setText(adats.get(Integer.parseInt(member_entty.getMember_lv_custom_key())).getName());
            }
        }
        member_lv_custom_id=member_entty.getMember_lv_custom_id();
        member_lv_custom_key=member_entty.getMember_lv_custom_key();
    }

//    private void init1() {
//        ed_member_bncoder= (TextView) findViewById(R.id.ed_member_bncoder);
//        tv_day= (TextView) findViewById(R.id.tv_day);
//        tv_day.setOnClickListener(this);
//        tv_day.setText(DateUtils.getNowtimeKeyStr());
//        ed_member_name= (EditText) findViewById(R.id.ed_member_name);
//        ed_phone= (EditText) findViewById(R.id.ed_phone);
//        ed_money= (EditText) findViewById(R.id.ed_money);
//        ed_score= (EditText) findViewById(R.id.ed_score);
////        tv_integral= (EditText) findViewById(R.id.tv_integral);
//        ed_discount_rate= (EditText) findViewById(R.id.ed_discount_rate);
//        ed_password= (EditText) findViewById(R.id.ed_password);
//        ed_remark= (EditText) findViewById(R.id.ed_remark);
//        ed_birthday= (TextView) findViewById(R.id.ed_birthday);
//        ed_birthday.setOnClickListener(this);
//
//        but_new_members= (Button) findViewById(R.id.but_new_members);
//        but_delete_members= (Button) findViewById(R.id.but_delete_members);
//
//        but_new_members.setOnClickListener(this);
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//
//
//        if (SharedUtil.getString("type").equals("4")){
//            Log.d("printp","员工登陆的");
//            ed_money.setFocusable(false);
//            ed_money.setFocusableInTouchMode(false);
//
//            ed_score.setFocusable(false);
//            ed_score.setFocusableInTouchMode(false);
//
//            ed_discount_rate.setFocusable(false);
//            ed_discount_rate.setFocusableInTouchMode(false);
//        }else {
//            Log.d("printp","老板登陆的");
//            ed_money.setFocusableInTouchMode(true);
//            ed_money.setFocusable(true);
//            ed_money.requestFocus();
//
//            ed_score.setFocusableInTouchMode(true);
//            ed_score.setFocusable(true);
//            ed_score.requestFocus();
//
//            ed_discount_rate.setFocusableInTouchMode(true);
//            ed_discount_rate.setFocusable(true);
//            ed_discount_rate.requestFocus();
//        }
//    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                //享受的会员价
                case R.id.rl_lv_member:
                    ShowPop(adats,rl_lv_member,"1");
                    break;
                //会员等级
                case R.id.rl_grade:
                    ShowPop(listFenlei,rl_grade,"2");
                    break;
                case R.id.ed_birthday:
                    timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {
                            ed_birthday.setText(time);
                        }
                    }, "2015-11-22 17:34", getTime());
                    timeSelector.show();
                    break;
                case R.id.tv_day:
                    DateTimeUtils.runTime(Member_Activity.this,tv_day);

//                    timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
//                        @Override
//                        public void handle(String time) {
//                            tv_day.setText(time);
//                        }
//                    }, "2015-11-22 17:34", getTime());
//                    timeSelector.show();
                    break;
                case R.id.but_new_members:
                    List<Map<String,String>> listmap=new ArrayList<Map<String, String>>();
                            Map<String,String> map=new HashMap<String, String>();
                    if (ed_phone.getText().toString().length()==11){
                        map.put("mobile",ed_phone.getText().toString());
                    }else {
                        Toast.makeText(Member_Activity.this,"请输入11位手机号码",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ed_score.getText().toString()!=null){
                        map.put("score",ed_score.getText().toString());
                    }else {
                        map.put("score",ed_score.getHint().toString());
                    }
                    if (ed_money.getText().toString()!=null&&!ed_money.getText().toString().equals("")){
                        map.put("surplus",ed_money.getText().toString());
                    }else {
                        map.put("surplus",ed_money.getHint().toString());
                    }

                    if (ed_discount_rate.getText().toString()!=null&&!ed_discount_rate.getText().toString().equals("")){
                        map.put("discount_rate",ed_discount_rate.getText().toString());
                    }else {
                        map.put("discount_rate",ed_discount_rate.getHint().toString());
                    }
                            map.put("member_lv_custom_id",member_lv_custom_id);
                            map.put("member_lv_custom_key",member_lv_custom_key);

                            map.put("member_name",ed_member_name.getText().toString());
//                            map.put("pwd","1111");
                            map.put("birthday", DateUtils.data(ed_birthday.getText().toString()+":00"));
                    if (ed_remark.getText().toString()!=null&&!ed_discount_rate.getText().toString().equals("")){
                        map.put("remark", ed_remark.getText().toString());
                    }
                    if (is_require_pass){
                            map.put("is_require_pass","yes");
                        }else {
                            map.put("is_require_pass","no");
                        }
                            listmap.add(map);
                            Gson gson=new Gson();
                            String str=gson.toJson(listmap);
                            Log.e("print","插入的数据为"+str);
                    if (member_entty!=null){
                        Map<String,String> map1=new HashMap<String, String>();
                        map1.put("mobile",ed_phone.getText().toString());
                        if (ed_score.getText().toString()!=null){
                            map1.put("score",ed_score.getText().toString());
                        }else {
                            map1.put("score",ed_score.getHint().toString());
                        }
                        if (ed_money.getText().toString()!=null){
                            map1.put("surplus",ed_money.getText().toString());
                        }else {
                            map1.put("surplus",ed_money.getHint().toString());
                        }

                        if (ed_discount_rate.getText().toString()!=null){
                            map1.put("discount_rate",ed_discount_rate.getText().toString());
                        }else {
                            map1.put("discount_rate",ed_discount_rate.getHint().toString());
                        }
                        map1.put("member_lv_custom_id",member_lv_custom_id);
                        map1.put("member_lv_custom_key",member_lv_custom_key);
                        map1.put("member_name",ed_member_name.getText().toString());
//                            map.put("pwd","1111");
                        map1.put("birthday", DateUtils.data(ed_birthday.getText().toString()+":00"));
                        map1.put("remark", ed_remark.getText().toString());
                        map1.put("member_id",member_entty.getMember_id());
                        if (is_require_pass){
                            map1.put("is_require_pass","yes");
                        }else {
                            map1.put("is_require_pass","no");
                        }
                        Gson gson1=new Gson();
                        String str1=gson1.toJson(map1);
                        //编辑会员
                        Edit_members(str1);
                    }else {
                        //新增商品
                        setBut_Newmembers(str);
                    }
                    break;
                case R.id.tv_huanghui:
                    finish();
                    break;
                //删除
                case R.id.but_delete_members:
                    new AlertDialog.Builder(this).setTitle("")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Delete_members(member_entty.getMember_id());
                                }
                            }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                    break;
                    //发送短信的按钮
                case R.id.but_sms:
                    getSMSTemplate();
//                    setSMS();
//                    if (!isSend){
//                        if (StringUtils.isPhone(ed_phone.getText().toString())){
//                            isSend=true;
//                            SendSMS();
//                        }
//                    }else {
//                        Toast.makeText(Member_Activity.this,"正在发送短信请稍后",Toast.LENGTH_SHORT).show();
//                    }
                    break;
                    //点击充值按钮
                case R.id.but_Recharge:
                    getRecharge();
                    break;
                    //点击使用详情
                case R.id.tv_record:
//                    getusedetails();
                    startActivity(new Intent(Member_Activity.this,UseSms_Activity.class));
                    break;
            }
    }



    //获取会员等级
    public void getMemberGrade(){
        OkGo.post(SysUtils.getSellerServiceUrl("member_lv_custom_list"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "打印会员等级的数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject respons=jsonObject.getJSONObject("response");
                            String status=respons.getString("status");
                            if (status.equals("200")){
                                listFenlei.clear();
                                adats.clear();
                                JSONArray ja=respons.getJSONArray("data");
                                for (int i=0;i<ja.length();i++){
                                    JSONObject jo=ja.getJSONObject(i);
                                    Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                    Fenlei_Entty fenlei_entty1=new Fenlei_Entty();
                                    fenlei_entty.setName(jo.getString("name"));
                                    fenlei_entty.setTag_id(jo.getInt("member_lv_custom_id"));
                                    fenlei_entty1.setName(jo.getString("name")+"价");
                                    fenlei_entty1.setTag_id(i+1);
                                    listFenlei.add(fenlei_entty);
                                    adats.add(fenlei_entty1);
                                }
                                Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                Fenlei_Entty fenlei_entty1=new Fenlei_Entty();
                                fenlei_entty.setName("普通会员");
                                fenlei_entty.setTag_id(0);
                                fenlei_entty1.setName("普通价");
                                fenlei_entty1.setTag_id(0);
                                listFenlei.add(0,fenlei_entty);
                                adats.add(0,fenlei_entty1);
                                Intent intent=getIntent();
                                if(intent.getSerializableExtra("member") != null){
                                    member_entty= (Member_entty) intent.getSerializableExtra("member");
                                    Loaddatas();
                                }else {
                                    tv_grade.setText(listFenlei.get(0).getName());
                                    member_lv_custom_id=listFenlei.get(0).getTag_id()+"";
                                    tv_price.setText(adats.get(0).getName());
                                    member_lv_custom_key=adats.get(0).getTag_id()+"";
                                }
                                Log.e("print", "打印数据大小"+listFenlei.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //选择会员等级的弹窗
    PopupWindow popupWindow;
    ListView popListView;
    Spadapter spadapter=new Spadapter(Member_Activity.this);
    List<Fenlei_Entty> listFenlei=new ArrayList<>();
    List<Fenlei_Entty> adats=new ArrayList<>();
    public void ShowPop(List<Fenlei_Entty> adats, View view, String type){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
        View popView=View.inflate(this,R.layout.pop_listview,null);

        /**

         * 第一个参数：View contenView（布局）

         * 第二个参数：int width（宽度）

         * 第三个参数：int height（高度）

         *      宽高参数：-2 和 ViewGroup.LayoutParams.WRAP_CONTENT 一样

         *                -1 和 ViewGroup.LayoutParams.MATCH_PARENT 一样

         *

         * 三个参数缺少任意一个都不可能弹出来PopWindow；

         *

         */

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //popupWindow是否响应touch事件

        popupWindow.setTouchable(true);

        //popupWindow是否具有获取焦点的能力

        popupWindow.setFocusable(true);

        //这个方法是重中之重，不仅仅是设置背景,不设置背景上面两行代码无效

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));



        popListView= (ListView) popView.findViewById(R.id.listview);



        //这里有个小坑，代码设置分割线，必须先设置颜色，再设置高度，不然不生效

        popListView.setDivider(new ColorDrawable(Color.WHITE));

        popListView.setDividerHeight(1);


        spadapter.setAdats(adats);
        spadapter.setType(type);
        spadapter.setOneidtextonclick(Member_Activity.this);
        popListView.setAdapter(spadapter);



        /**

         * 第一个参数：显示在ｉｖ布局下面

         * 第二个参数：xoff表示x轴的偏移，正值表示向左，负值表示向右；

         * 第三个参数：yoff表示相对y轴的偏移，正值是向下，负值是向上；

         */

        popupWindow.showAsDropDown(view,80,-33);

    }

    /**
     * 获取充值和使用详情
     */
    public void getusedetails(){

    }
    /**
     * 获得短信充值的规格
     */
    public void getRecharge(){
        OkGo.post(SysUtils.getSms("sms-buyList"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        recharge_smsEntty = gson.fromJson(s, Recharge_smsEntty.class);
                        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(Member_Activity.this, R.style.AlertDialog);
                        view_add_nums_notes = View.inflate(Member_Activity.this, R.layout.member_recharge, null);
                        gl_recharge= (MyGirdView) view_add_nums_notes.findViewById(R.id.gl_recharge);
                        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
                        adapter=new Gridview_member_adapter(Member_Activity.this);
                        adapter.setAdats(recharge_smsEntty);
                        adapter.setOnclickitme(Member_Activity.this);
                        gl_recharge.setAdapter(adapter);
                        but_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (handlernew!=null){
                                    if (handlernew!=null){
                                        handlernew.removeCallbacks(runnablenew);
                                    }
                                }
                                mAlertDialog_add_nums_notes.dismiss();
                            }
                        });
                        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
                        mAlertDialog_add_nums_notes.setCancelable(false);
                        mAlertDialog_add_nums_notes.show();
                    }
                });
    }
    TextView tv_nums;
    /**
     *设置短信填充内容
     */
    public void setSMS(final Template_Entty.ResponseBean.DataBean bean){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Member_Activity.this, R.style.AlertDialog);
        View view_notes= View.inflate(Member_Activity.this, R.layout.smstemplate, null);
        final EditText ed_context= (EditText) view_notes.findViewById(R.id.ed_context);
        ed_context.clearFocus();
        ed_context.setHint(bean.getContent());
        if (SharedUtil.getString("smscontest")!=null){
            ed_context.setText(SharedUtil.getString("smscontext"));
        }
        tv_nums= (TextView) view_notes.findViewById(R.id.tv_nums);
        tv_nums.setText(num+"");
        Button but_Recharge= (Button) view_notes.findViewById(R.id.but_Recharge);
        but_Recharge.setOnClickListener(this);
        TextView tv_record= (TextView) view_notes.findViewById(R.id.tv_record);
        tv_record.setOnClickListener(this);
        Button but_submit= (Button) view_notes.findViewById(R.id.but_submit);
        final android.support.v7.app.AlertDialog mAlertDialog_notes = dialog.setView(view_notes).show();
        mAlertDialog_notes.setCancelable(true);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=ed_context.getText().toString();
                if (!str.equals("")) {
                    SharedUtil.putString("smscontext", str);
                    if (str.indexOf(",") != -1) {
                        str = str.replaceAll(",","，");
                    }
                    if (Integer.parseInt(tv_nums.getText().toString()) >= 1) {
                        if (!isSend) {
                            if (StringUtils.isPhone(ed_phone.getText().toString())) {
                                isSend = true;
                                SendSMS(str,bean.getTemplate_id());
                            }
                        } else {
                            Toast.makeText(Member_Activity.this, "正在发送短信请稍后", Toast.LENGTH_SHORT).show();
                        }
                        mAlertDialog_notes.dismiss();
                        if (NoDoubleClickUtils.isSoftShowing(Member_Activity.this)) {
                            imm = (InputMethodManager) Member_Activity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }else {
                            Toast.makeText(Member_Activity.this,"短信不足请充值",Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
        mAlertDialog_notes.show();
    }

    /**
     * 获取短信剩余数量
     */
    int num=0;
    int send_num=0;
    int history_num=0;
    public void getsmsnums(final Template_Entty.ResponseBean.DataBean bean){
        OkGo.post(SysUtils.getSms("sms-num"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")){
                                JSONObject data=jsonObject1.getJSONObject("data");
                                num =data.getInt("num");
                                send_num =data.getInt("send_num");
                                history_num =data.getInt("history_num");
                                setSMS(bean);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    Template_Entty template_entty;
    ListView lv_recharge;
    Adapter_sms_Template adapter_sms_template;
    /**
     * 获取短信模板
     */
    public void getSMSTemplate(){
        OkGo.post(SysUtils.getSms("sms-templates"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        template_entty=gson.fromJson(s,Template_Entty.class);
                        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(Member_Activity.this, R.style.AlertDialog);
                        view_add_nums_notes = View.inflate(Member_Activity.this, R.layout.dialog_recharge, null);
                        lv_recharge = (ListView) view_add_nums_notes.findViewById(R.id.lv_recharge);
                        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
                        but_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mAlertDialog_add_nums_notes!=null){
                                    mAlertDialog_add_nums_notes.dismiss();
                                }
                            }
                        });
                        adapter_sms_template=new Adapter_sms_Template(Member_Activity.this);
                        adapter_sms_template.setAdats(template_entty);
                        lv_recharge.setAdapter(adapter_sms_template);
                        adapter_sms_template.SetOnclick(Member_Activity.this);
                        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
                        mAlertDialog_add_nums_notes.setCancelable(false);
                        mAlertDialog_add_nums_notes.show();
                    }
                });
    }
    /**
     * 发送短信的方法
     */
    public void SendSMS(String str,String tid){
        showdialog("正在发送短信...");
        OkGo.post(SysUtils.getSms("sms-send"))
                .tag(this)
                .params("tels",ed_phone.getText().toString())
                .params("params","【"+SharedUtil.getString("name")+"】"+","+str)
                .params("tid",tid)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        loadingdialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("printsms", "发送短信"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jso=jsonObject.getJSONObject("response");
                            String status=jso.getString("status");
                            if (status.equals("200")){
                                isSend=false;
                            Toast.makeText(Member_Activity.this,"发送成功",Toast.LENGTH_SHORT).show();
                            }else {
                                isSend=false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            loadingdialog.dismiss();
                        }
                    }
                });
    }
    /**
     * 弹出加载的框
     */
    public void showdialog(String text){
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(Member_Activity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText(text);
        loadingdialog.show();
    }
    //新增会员
    public void setBut_Newmembers(String map){
        OkGo.post(SysUtils.getSellerServiceUrl("add_members"))
                .tag(this)
                .params("map",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","新增会员的结果"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String message=jo1.getString("message");
                            if (status.equals("200")){
                                Toast.makeText(Member_Activity.this,message,Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(Member_Activity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    //编辑会员
    public void Edit_members(String map){
        Log.d("print",""+map);
        OkGo.post(SysUtils.getSellerServiceUrl("edit_members"))
                .tag(this)
                .params("map",map)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","得到的数据为"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(Member_Activity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(Member_Activity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //删除会员
    public void Delete_members(String id){
        OkGo.post(SysUtils.getSellerServiceUrl("del_members"))
                .tag(this)
                .params("member_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","删除返回"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(Member_Activity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(Member_Activity.this,"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //点击编辑框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    //获取时间 yyyy-MM-dd  HH:mm
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
    /**
     * 定时器查询短信充值是否成功
     */
    public void getpay(String order_id, final String num){
        OkGo.post(SysUtils.getSms("sms-query"))
                .tag(this)
                .params("order_id", order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("打印的订单的id", "onSuccess: "+s);
                        Gson gson=new Gson();
                        Pay_success pay_success = gson.fromJson(s, Pay_success.class);
                        if (pay_success!=null){
                            if (pay_success.getResponse().getStatus().equals("200")){
                                if (tv_nums!=null){
                                    String str=tv_nums.getText().toString();
                                    tv_nums.setText(Integer.parseInt(str)+Integer.parseInt(num)+"");
                                }
                                if (handlernew!=null){
                                    if (handlernew!=null){
                                        handlernew.removeCallbacks(runnablenew);
                                    }
                                }
                                mAlertDialog_add_nums_notes.dismiss();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handlernew!=null){
            if (handlernew!=null){
                handlernew.removeCallbacks(runnablenew);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handlernew!=null){
            if (handlernew!=null){
                handlernew.removeCallbacks(runnablenew);
            }
        }
    }
    /**
     * 点击充值规格
     * @param i
     */
    @Override
    public void Onclickitme(int i) {
        if (handlernew!=null){
            if (handlernew!=null){
                handlernew.removeCallbacks(runnablenew);
            }
        }
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        if (recharge_smsEntty!=null){
            getorder(recharge_smsEntty.getResponse().getData().getList().get(i));
        }

    }
    /**
     * 获取订单
     */
    public void getorder(final Recharge_smsEntty.ResponseBean.DataBean.ListBean bean){
        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(Member_Activity.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(Member_Activity.this, R.layout.code_pay, null);
        final ImageView im_code1= (ImageView) view_add_nums_notes.findViewById(R.id.im_code);
        TextView tv_price= (TextView) view_add_nums_notes.findViewById(R.id.tv_price);
        tv_price.setText("￥"+bean.getPrice());
        TextView tv_alipay=(TextView)view_add_nums_notes.findViewById(R.id.tv_alipay);
        TextView tv_wechat=(TextView)view_add_nums_notes.findViewById(R.id.tv_wechat);

        tv_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode("ali",bean.getId(),im_code1,bean.getNum());
            }
        });
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode("wx",bean.getId(),im_code1,bean.getNum());
            }
        });

        TextView tv_huanghui= (TextView) view_add_nums_notes.findViewById(R.id.tv_huanghui);
        tv_huanghui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handlernew!=null){
                    if (handlernew!=null){
                        handlernew.removeCallbacks(runnablenew);
                    }
                }
                mAlertDialog_add_nums_notes.dismiss();
            }
        });
        mAlertDialog_add_nums_notes = dialog1.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
//        OkGo.post("")
//                .tag(this)
//                .params("","")
//                .params("","")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        try {
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jo1=jsonObject.getJSONObject("response");
//                            String status=jo1.getString("status");
//                            if (status.equals("200")){
//                                handlernew = new Handler();
//                                runnablenew=new Runnable() {
//                                    @Override
//                                    public void run() {
////                                        getpay("");
//                                        if(isNetBad){
//                                            Toast.makeText(Member_Activity.this,"当前网络较差",Toast.LENGTH_SHORT).show();
//                                        }
//                                        handlernew.postDelayed(this,3000);
//                                    }
//                                };
//                                handlernew.postDelayed(runnablenew, 3000);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }
    /**
     * 获取支付二维码
     */
    public void getCode(String pay_type, String id, final ImageView im_code1,final String num){
     OkGo.post(SysUtils.getSms("sms-buy"))
             .tag(this)
             .params("pay_type",pay_type)
             .params("id",id)
             .execute(new StringCallback() {
                 @Override
                 public void onSuccess(String s, Call call, Response response) {
                     Log.e("print","打印的订单的id"+s);
                     try {
                         JSONObject jsonObject=new JSONObject(s);
                         JSONObject jsonObject1=jsonObject.getJSONObject("response");
                         String status=jsonObject1.getString("status");
                         if (status.equals("200")){
                             JSONObject jo=jsonObject1.getJSONObject("data");
                             String url=jo.getString("url");
                             im_code1.setImageBitmap(QRCode.createQRCode(url));
                             final String order_id=jo.getString("order_id");
                             if (handlernew!=null){
                                 if (handlernew!=null){
                                     handlernew.removeCallbacks(runnablenew);
                                 }
                             }
                             handlernew = new Handler();
                                runnablenew=new Runnable() {
                                    @Override
                                    public void run() {
                                        getpay(order_id,num);
                                        if(isNetBad){
                                            Toast.makeText(Member_Activity.this,"当前网络较差",Toast.LENGTH_SHORT).show();
                                        }
                                        handlernew.postDelayed(this,3000);
                                    }
                                };
                                handlernew.postDelayed(runnablenew, 3000);
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             });
    }
    /**
     * 选择模板
     * @param i
     */
    @Override
    public void onclickdialog(int i) {
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        if(template_entty!=null){
            template_entty.getResponse().getData().get(i);
            getsmsnums(template_entty.getResponse().getData().get(i));
        }
    }

    /**
     * 选择会员等级和享受的会员价
     * @param i
     * @param type
     */
    @Override
    public void itmeeidtonclick(int i, String type) {
        if(type.equals("2")){
            tv_grade.setText(listFenlei.get(i).getName());
            member_lv_custom_id=listFenlei.get(i).getTag_id()+"";
        }else if (type.equals("1")){
            tv_price.setText(adats.get(i).getName());
            member_lv_custom_key=adats.get(i).getTag_id()+"";
        }
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }
}
