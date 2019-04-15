package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Fenlei_Entty;
import Entty.Member_entty;
import Entty.Pay_success;
import Entty.Recharge_smsEntty;
import Entty.Specification_Entty;
import Entty.Template_Entty;
import Utils.ExcelUtils;
import Utils.QRCode;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Adapter_member;
import adapters.Adapter_sms_Template;
import adapters.Gridview_member_adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.Member_Activity;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import widget.MyFileManager;
import widget.MyGirdView;
import widget.ShapeLoadingDialog;

import static retail.yzx.com.kz.NetWorkService.isNetBad;

/**
 * Created by admin on 2017/7/6.
 */
public class Fragment_lei_member extends Fragment implements View.OnClickListener, Adapter_member.SetOnclickitme, Adapter_sms_Template.SetOnclick, Gridview_member_adapter.SetOnclickitme {

    public View view;
    public ListView lv_member;
    public Button but_derive,but_last,but_next;
    public int page=1;
    public int total=0;
    public boolean paging1=false,paging2=false;
    //新增会员
    public Button but_Newmembers,but_lv_member;
    public static List<Map<String, String>> mapList = new ArrayList<>();
    //会员列表
    public List<Member_entty> adats;
    public Adapter_member adapter;

    public TextView tv_page,tv_shuliang;
    public EditText tv_seek;
    public Button but_sms;
    boolean isSend=false;
    //加载数据的弹窗
    public ShapeLoadingDialog loadingdialog;
    //订单商品添加数量与备注
    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    InputMethodManager  imm;

    //充值的定时器
    Handler handlernew;
    Runnable runnablenew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_lei_member,null);
        init();
        LoadDatas();
        return view;
    }

    private void init() {

        adats=new ArrayList<>();
        lv_member= (ListView) view.findViewById(R.id.lv_member);
        adapter=new Adapter_member(getActivity());
        adapter.SetOnclickitme(this);
        tv_page= (TextView) view.findViewById(R.id.tv_page);
        tv_shuliang= (TextView) view.findViewById(R.id.tv_shuliang);


        but_sms= (Button) view.findViewById(R.id.but_sms);
        but_sms.setOnClickListener(this);
        but_last= (Button) view.findViewById(R.id.but_last);
        but_next= (Button) view.findViewById(R.id.but_next);
        but_derive= (Button) view.findViewById(R.id.but_derive);
        but_Newmembers= (Button) view.findViewById(R.id.but_Newmembers);
        but_lv_member= (Button) view.findViewById(R.id.but_lv_member);
        but_last.setOnClickListener(this);
        but_next.setOnClickListener(this);
        but_derive.setOnClickListener(this);
        but_Newmembers.setOnClickListener(this);
        but_lv_member.setOnClickListener(this);
        tv_seek= (EditText) view.findViewById(R.id.tv_seek);
        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    tv_seek.getText().toString();
                    //搜索的接口
                    SeekMember(tv_seek.getText().toString());
                }
                return true;
            }
        });
    }
//  加载数据
    private void LoadDatas() {
        OkGo.post(SysUtils.getSellerServiceUrl("members_list"))
                .tag(getActivity())
                .params("page",page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","会员列表"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONObject jo2=jo1.getJSONObject("data");
                                JSONArray ja1=jo2.getJSONArray("info");
                                total =jo2.getInt("nums");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Member_entty member=new Member_entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    member.setDiscount_rate(jo3.getString("discount_rate"));
                                    member.setMember_name(jo3.getString("member_name"));
                                    member.setMobile(jo3.getString("mobile"));
                                    member.setScore(jo3.getString("score"));
                                    member.setBirthday(jo3.getString("birthday"));
                                    member.setTime(jo3.getString("addtime"));
                                    member.setSurplus(jo3.getString("surplus"));
                                    member.setMember_id(jo3.getString("member_id"));
                                    member.setMember_lv_custom_id(jo3.getString("member_lv_custom_id"));
                                    member.setMember_lv_custom_key(jo3.getString("member_lv_custom_key"));
                                    if (!jo3.getString("member_lv_custom_id").equals("0")){
                                        member.setMember_lv_custom_name(jo3.getString("member_lv_custom_name"));
                                    }else {
                                        member.setMember_lv_custom_name("普通会员");
                                    }
                                    member.setRemark(jo3.getString("remark"));
                                    member.setIs_require_pass(jo3.getString("is_require_pass"));
                                    adats.add(member);
                                }
                                adapter.setAdats(adats);
                                lv_member.setAdapter(adapter);
                                tv_shuliang.setText(total+"");
                                if (Integer.valueOf(total) % 10 == 0) {
                                    tv_page.setText(page + "/" + (Integer.valueOf(total) /10));
                                } else {
                                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                                }
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
            case R.id.but_derive:
                Intent intent = new Intent(getActivity(), MyFileManager.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.but_Newmembers:
//                final Dialog dialog = new Dialog(getActivity());
//                dialog.setTitle("新增会员");
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setContentView(R.layout.dialog_member);
//                Button but_new_members= (Button) window.findViewById(R.id.but_new_members);
//                final EditText ed_member_name= (EditText) window.findViewById(R.id.ed_member_name);
//                final EditText ed_mobile= (EditText) window.findViewById(R.id.ed_mobile);
//                final EditText ed_score= (EditText) window.findViewById(R.id.ed_score);
//                final EditText ed_discount_rate= (EditText) window.findViewById(R.id.ed_discount_rate);
//                but_new_members.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (!ed_member_name.getText().toString().isEmpty()&&!ed_mobile.getText().toString().isEmpty()
//                                &&!ed_score.getText().toString().isEmpty()&&!ed_discount_rate.getText().toString().isEmpty()){
//                            List<Map<String,String>> listmap=new ArrayList<Map<String, String>>();
//                            Map<String,String> map=new HashMap<String, String>();
//                            map.put("mobile",ed_mobile.getText().toString());
//                            map.put("score",ed_score.getText().toString());
//                            map.put("discount_rate",ed_discount_rate.getText().toString());
//                            map.put("member_name",ed_member_name.getText().toString());
//                            listmap.add(map);
//                            Gson gson=new Gson();
//                            String str=gson.toJson(listmap);
//                            Log.e("print","插入的数据为"+str);
//                            setBut_Newmembers(str);
//                            dialog.dismiss();
//                            LoadDatas();
//                        }
//
//                    }
//                });
//                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                });
                Intent intent1 = new Intent(getActivity(), Member_Activity.class);
                startActivity(intent1);
                break;
            case R.id.but_next:
                if (Integer.valueOf(total) % 10 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10));
                    if (page < (Integer.valueOf(total) / 10)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
                            LoadDatas();
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                    if (page < (Integer.valueOf(total) / 10 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
//                            Loaddata();
                            LoadDatas();
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
//                            Loaddata();
                            LoadDatas();
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                        if (!paging2) {
                            paging2 = true;
//                            Loaddata();
                            LoadDatas();
                            paging2 = false;
                        }
                    }
                }
                break;
                //发送短信的按钮
            case R.id.but_sms:
                getSMSTemplate();
//                setSMS();
//                if (!isSend){
//                        SendSMS();
//                }else {
//                    Toast.makeText(getActivity(),"正在发送短信请稍后",Toast.LENGTH_SHORT).show();
//                }
                break;
            //点击充值按钮
            case R.id.but_Recharge:
                getRecharge();
                break;
            case R.id.but_lv_member:
                getMemberGrade();
                break;
        }
    }

    List<Fenlei_Entty> listFenlei=new ArrayList<>();
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
                                JSONArray ja=respons.getJSONArray("data");
                                if (ja.length()>0){
                                    for (int i=0;i<ja.length();i++){
                                        JSONObject jo=ja.getJSONObject(i);
                                        Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                        Fenlei_Entty fenlei_entty1=new Fenlei_Entty();
                                        fenlei_entty.setName(jo.getString("name"));
                                        fenlei_entty.setTag_id(jo.getInt("member_lv_custom_id"));
                                        fenlei_entty1.setName(jo.getString("name")+"价");
                                        fenlei_entty1.setTag_id(i+1);
                                        listFenlei.add(fenlei_entty);
                                    }
                                    Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                    Fenlei_Entty fenlei_entty1=new Fenlei_Entty();
                                    fenlei_entty.setName("普通会员");
                                    fenlei_entty.setTag_id(0);
                                    fenlei_entty1.setName("普通价");
                                    fenlei_entty1.setTag_id(0);
                                    listFenlei.add(0,fenlei_entty);
                                    ShowmemberLv();
                                }else {
                                    ShowmemberLv();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void ShowmemberLv(){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        dialog.setView(new EditText(getActivity()));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.member_lv);
        EditText tv_lv0= (EditText) window.findViewById(R.id.tv_lv0);
        final EditText tv_lv1= (EditText) window.findViewById(R.id.tv_lv1);
        final EditText tv_lv2= (EditText) window.findViewById(R.id.tv_lv2);
        final EditText tv_lv3= (EditText) window.findViewById(R.id.tv_lv3);
        final EditText tv_lv4= (EditText) window.findViewById(R.id.tv_lv4);
        final EditText tv_lv5= (EditText) window.findViewById(R.id.tv_lv5);
        Button but_new_member= (Button) window.findViewById(R.id.but_new_member);
        but_new_member.setVisibility(View.VISIBLE);
        tv_lv0.setFocusable(true);
        tv_lv0.requestFocus();
        tv_lv1.setFocusable(true);
        tv_lv1.requestFocus();
        tv_lv2.setFocusable(true);
        tv_lv2.requestFocus();
        tv_lv3.setFocusable(true);
        tv_lv3.requestFocus();
        tv_lv4.setFocusable(true);
        tv_lv4.requestFocus();
        tv_lv5.setFocusable(true);
        tv_lv5.requestFocus();

        if (listFenlei.size()>0){
            tv_lv0.setText(listFenlei.get(0).getName());
            tv_lv1.setText(listFenlei.get(1).getName());
            tv_lv2.setText(listFenlei.get(2).getName());
            tv_lv3.setText(listFenlei.get(3).getName());
            tv_lv4.setText(listFenlei.get(4).getName());
            tv_lv5.setText(listFenlei.get(5).getName());
        }


        but_new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Map<String,String>> list=new ArrayList<>();

                if (!tv_lv1.getText().toString().equals("")
                        &&!tv_lv1.getText().toString().equals("null")
                        &&tv_lv1.getText().toString()!=null){
                    Map<String,String> map=new HashMap<>();
                    map.put("name",tv_lv1.getText().toString());
                    map.put("order_limit",5+"");
                    map.put("discount","1");
                    list.add(map);
                }
                if (!tv_lv2.getText().toString().equals("")
                        &&!tv_lv2.getText().toString().equals("null")
                        &&tv_lv2.getText().toString()!=null){
                    Map<String,String> map=new HashMap<>();
                    map.put("name",tv_lv2.getText().toString());
                    map.put("order_limit",4+"");
                    map.put("discount","1");
                    list.add(map);
                }
                if (!tv_lv3.getText().toString().equals("")
                        &&!tv_lv3.getText().toString().equals("null")
                        &&tv_lv3.getText().toString()!=null){
                    Map<String,String> map=new HashMap<>();
                    map.put("name",tv_lv3.getText().toString());
                    map.put("order_limit",3+"");
                    map.put("discount","1");
                    list.add(map);
                }
                if (!tv_lv4.getText().toString().equals("")
                        &&!tv_lv4.getText().toString().equals("null")
                        &&tv_lv4.getText().toString()!=null){
                    Map<String,String> map=new HashMap<>();
                    map.put("name",tv_lv4.getText().toString());
                    map.put("order_limit",2+"");
                    map.put("discount","1");
                    list.add(map);
                }
                if (!tv_lv5.getText().toString().equals("")
                        &&!tv_lv5.getText().toString().equals("null")
                        &&tv_lv5.getText().toString()!=null){
                    Map<String,String> map=new HashMap<>();
                    map.put("name",tv_lv5.getText().toString());
                    map.put("order_limit",1+"");
                    map.put("discount","1");
                    list.add(map);
                }
                Gson gson=new Gson();
                String json=gson.toJson(list);
                setLv_member(json);
                dialog.dismiss();
            }
        });
    }

    public void setLv_member(final String json){
        OkGo.post(SysUtils.getSellerServiceUrl("member_lv_custom_set"))
                .tag(this)
                .params("jsonData",json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印会员的等级的新增情况"+s);
//                        {"response":{"status":"200","message":"ok","data":""}}
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(getActivity(),"添加会员等级价成功",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 弹出加载的框
     */
    public void showdialog(String text){
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(getActivity());
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText(text);
        loadingdialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//                Uri uri = data.getData();
//                uri.getPath();
            Bundle bundle = null;
            if (data != null && (bundle = data.getExtras()) != null) {
                if (bundle.getString("file").endsWith(".xls")){
                mapList= ExcelUtils.read2DB3(new File(bundle.getString("file")), getContext());
                Gson gson = new Gson();
                String str = gson.toJson(mapList);
                /**
                 * 批量导入商品的方法
                 */
                Log.d("print","导入的商品是"+str);
                    setBut_Newmembers(str);
                    LoadDatas();
                }
            }
        }



    }


    @Override
    public void onStart() {
        super.onStart();
        LoadDatas();
    }

    public void setBut_Newmembers(String map){
        OkGo.post(SysUtils.getSellerServiceUrl("add_members"))
                .tag(getActivity())
                .params("map",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","新增会员的结果"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(getContext(),"添加会员成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"添加会员失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    //编辑会员
    public void Edit_members(String map){
        OkGo.post(SysUtils.getSellerServiceUrl("edit_members"))
                .tag(getActivity())
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
                             Toast.makeText(getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"修改失败",Toast.LENGTH_SHORT).show();
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
                .tag(getActivity())
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
                                Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //搜索会员
    public void SeekMember(String str){
        OkGo.post(SysUtils.getSellerServiceUrl("search_members"))
                .tag(getActivity())
                .params("mobile",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                            Log.d("print","搜索的方法"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONObject jo2=jo1.getJSONObject("data");
                                JSONArray ja1=jo2.getJSONArray("info");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Member_entty member=new Member_entty();
                                    JSONObject jo3=ja1.getJSONObject(i);
                                    member.setDiscount_rate(jo3.getString("discount_rate"));
                                    member.setMember_name(jo3.getString("member_name"));
                                    member.setMobile(jo3.getString("mobile"));
                                    member.setScore(jo3.getString("score"));
                                    member.setBirthday(jo3.getString("birthday"));
                                    member.setTime(jo3.getString("addtime"));
                                    member.setSurplus(jo3.getString("surplus"));
                                    member.setMember_id(jo3.getString("member_id"));
                                    member.setMember_lv_custom_id(jo3.getString("member_lv_custom_id"));
                                    member.setMember_lv_custom_key(jo3.getString("member_lv_custom_key"));
                                    if (!jo3.getString("member_lv_custom_id").equals("0")){
                                        member.setMember_lv_custom_name(jo3.getString("member_lv_custom_name"));
                                    }else {
                                        member.setMember_lv_custom_name("普通会员");
                                    }
                                    member.setRemark(jo3.getString("remark"));
                                    member.setIs_require_pass(jo3.getString("is_require_pass"));
                                    adats.add(member);
                                }
                                adapter.setAdats(adats);
                                lv_member.setAdapter(adapter);
                                tv_shuliang.setText(total+"");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void OnclickEdit(final int i) {
        if (!SharedUtil.getString("type").equals("4")){
            Intent intent = new Intent(getActivity(), Member_Activity.class);
            intent.putExtra("member", adats.get(i));
            startActivity(intent);
        }else {
            Toast.makeText(getContext(),"没有该项权限",Toast.LENGTH_SHORT).show();
        }
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.setTitle("编辑会员");
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setContentView(R.layout.dialog_member);
//        Button but_new_members= (Button) window.findViewById(R.id.but_new_members);
//        but_new_members.setText("保存");
//        Button but_delete_members= (Button) window.findViewById(R.id.but_delete_members);
//        but_delete_members.setVisibility(View.VISIBLE);
//        final EditText ed_member_name= (EditText) window.findViewById(R.id.ed_member_name);
//        final EditText ed_mobile= (EditText) window.findViewById(R.id.ed_mobile);
//        final EditText ed_score= (EditText) window.findViewById(R.id.ed_score);
//        final EditText ed_discount_rate= (EditText) window.findViewById(R.id.ed_discount_rate);
//        ed_member_name.setText(adats.get(i).getMember_name());
//        ed_mobile.setText(adats.get(i).getMobile());
//        ed_score.setText(adats.get(i).getScore());
//        ed_discount_rate.setText(adats.get(i).getDiscount_rate());
//        but_new_members.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!ed_member_name.getText().toString().isEmpty()&&!ed_mobile.getText().toString().isEmpty()
//                        &&!ed_score.getText().toString().isEmpty()&&!ed_discount_rate.getText().toString().isEmpty()){
//                    Map<String,String> map=new HashMap<String, String>();
//                    map.put("mobile",ed_mobile.getText().toString());
//                    map.put("score",ed_score.getText().toString());
//                    map.put("discount_rate",ed_discount_rate.getText().toString());
//                    map.put("member_name",ed_member_name.getText().toString());
//                    map.put("member_id",adats.get(i).getMember_id());
//                    Gson gson=new Gson();
//                    String str=gson.toJson(map);
//                    Log.e("print","插入的数据为"+str);
//                    Edit_members(str);
//                    dialog.dismiss();
//                    LoadDatas();
//                }
//            }
//        });
//        but_delete_members.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Delete_members(adats.get(i).getMember_id());
//                dialog.dismiss();
//                LoadDatas();
//            }
//        });
//
//
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        });
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
                        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                        view_add_nums_notes = View.inflate(getActivity(), R.layout.dialog_recharge, null);
                        lv_recharge = (ListView) view_add_nums_notes.findViewById(R.id.lv_recharge);
                        adapter_sms_template=new Adapter_sms_Template(getActivity());
                        adapter_sms_template.setAdats(template_entty);
                        lv_recharge.setAdapter(adapter_sms_template);
                        adapter_sms_template.SetOnclick(Fragment_lei_member.this);
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
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        View view_notes= View.inflate(getActivity(), R.layout.smstemplate, null);
        final EditText ed_context= (EditText) view_notes.findViewById(R.id.ed_context);
        TextView tv_front= (TextView) view_notes.findViewById(R.id.tv_front);
        TextView tv_after= (TextView) view_notes.findViewById(R.id.tv_after);
        tv_front.setText(bean.getContent().substring(1,bean.getContent().indexOf("{2}")));
        tv_after.setText(bean.getContent().substring(bean.getContent().indexOf("{2}")+3,bean.getContent().length()));
        ed_context.clearFocus();
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
                                isSend = true;
                                SendSMS(str,bean.getTemplate_id());
                        } else {
                            Toast.makeText(getActivity(), "正在发送短信请稍后", Toast.LENGTH_SHORT).show();
                        }
                        mAlertDialog_notes.dismiss();
                        if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }else {
                        Toast.makeText(getActivity(),"短信不足请充值",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mAlertDialog_notes.show();
    }

    /**
     * 发送短信的方法
     */
    public void SendSMS(String str,String tid){
        String tels="";
        for (int i=0;i<adats.size();i++){
            if (StringUtils.isPhone(adats.get(i).getMobile())){
                if (i==adats.size()){
                    tels+=adats.get(i).getMobile();
                }else {
                    tels+=adats.get(i).getMobile()+",";
                }
            }
        }
        isSend=true;
        showdialog("正在发送短信...");
        OkGo.post(SysUtils.getSms("sms-send"))
                .tag(this)
                .params("tels",tels)
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
                                Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
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

    //充值规格的按钮
    private MyGirdView gl_recharge;
    private Gridview_member_adapter adapter1;
    private List<Specification_Entty> specification_entties;
    private Recharge_smsEntty recharge_smsEntty;
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
                        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                        view_add_nums_notes = View.inflate(getActivity(), R.layout.member_recharge, null);
                        gl_recharge= (MyGirdView) view_add_nums_notes.findViewById(R.id.gl_recharge);
                        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
                        adapter1=new Gridview_member_adapter(getActivity());
                        adapter1.setAdats(recharge_smsEntty);
                        adapter1.setOnclickitme(Fragment_lei_member.this);
                        gl_recharge.setAdapter(adapter1);
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

    /**
     * 获取订单
     */
    public void getorder(final Recharge_smsEntty.ResponseBean.DataBean.ListBean bean){
        android.support.v7.app.AlertDialog.Builder dialog1 = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        view_add_nums_notes = View.inflate(getActivity(), R.layout.code_pay, null);
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
                                            Toast.makeText(getActivity(),"当前网络较差",Toast.LENGTH_SHORT).show();
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


    /**
     * 选择短信的模板
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
}
