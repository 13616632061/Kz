package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Entty.Fenlei_Entty;
import Entty.Kitchen_IP;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Category_gvadapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/1.
 * 分类的页面
 */
public class Category_fragment extends Fragment implements  View.OnClickListener, Category_gvadapter.DeliteOnclick {
    public GridView gv_category;
    private Category_gvadapter adapter;
    public List<Fenlei_Entty> adats;
    public Button but_delete,but_newcategory,but_redact;
    public TextView tv_category;
//    删除按钮的判断
    public boolean isVisibility=false;
//    编辑按钮的判断
    public boolean isedit=false;
    public String string,string1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.category_fragment,null);

        adats=new ArrayList<>();
        gv_category= (GridView) view.findViewById(R.id.gv_category);
        adapter=new Category_gvadapter(getContext());

        tv_category= (TextView) view.findViewById(R.id.tv_category);

        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("query", "2")
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
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                adats.add(fenlei);
                            }
                            adapter.setAdats(adats);
                            gv_category.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            tv_category.setText(adats.size()+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        adapter.setAdats(adats);
        gv_category.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setDeliteOnclick(this);
//        删除按钮
        but_delete= (Button) view.findViewById(R.id.but_delete);
        but_delete.setOnClickListener(this);
//        新增分类
        but_newcategory= (Button) view.findViewById(R.id.but_newcategory);
        but_newcategory.setOnClickListener(this);
//        编辑分类
        but_redact= (Button) view.findViewById(R.id.but_redact);
        but_redact.setOnClickListener(this);
        return view;
    }

//    删除
    @Override
    public void onImeclick(final int i) {
        OkGo.post(SysUtils.getGoodsServiceUrl("cat_remove"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("query", "2")
                .params("tag_id",adats.get(i).getTag_id())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("TAG","adats"+s);
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            string=j1.getString("status");
                            string1=j1.getString("message");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray ja = j2.getJSONArray("nav_info");
                            adats.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                adats.add(fenlei);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (string.equals("200")){
                            adats.remove(i);
                        }else {
                            Toast.makeText(getActivity(),string1,Toast.LENGTH_SHORT).show();
                        }
                        adapter.setAdats(adats);
                        gv_category.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    //记录分类是否厨打
    List<Map<String,String>> listmap=new ArrayList<>();
//    编辑的dialog
    @Override
    public void OnitmeEdit(final int i) {
        if(isedit) {
            String str = adats.get(i).getName();
            final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setView(new EditText(getContext()));
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_edit);
            final EditText ed_edit = (EditText) window.findViewById(R.id.ed_edit);
            final EditText ed_mstrIp = (EditText) window.findViewById(R.id.ed_mstrIp);
            ed_edit.setText(str);
            if (SharedUtil.getMole()!=null){
                for (int j=0;j<SharedUtil.getMole().size();j++){
                    String ip=SharedUtil.getMole().get(j).getIP();
                    String tagip=SharedUtil.getMole().get(j).getTag_id();
                    String tag=adats.get(i).getTag_id()+"";
                    if (tagip.equals(tag)){
                        ed_mstrIp.setText(ip);
                    }
                }
            }
//        确定按钮的点击事件
            Button but_editqueding = (Button) window.findViewById(R.id.but_editqueding);
            but_editqueding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        setIP(adats.get(i).getTag_id()+"",ed_mstrIp.getText().toString());
                    if (!ed_edit.getText().toString().isEmpty()) {
                        OkGo.post(SysUtils.getGoodsServiceUrl("cat_edit"))
                                .tag(this)
                                .cacheKey("cacheKey")
                                .cacheMode(CacheMode.DEFAULT)
                                .params("seller_id", SharedUtil.getString("seller_id"))
                                .params("query", "2")
                                .params("nav",ed_edit.getText().toString())
                                .params("tag_id",adats.get(i).getTag_id())
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
                                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                                JSONObject jo = ja.getJSONObject(i);
                                                fenlei.setName(jo.getString("tag_name"));
                                                fenlei.setTag_id(jo.getInt("tag_id"));
                                                adats.add(fenlei);
                                            }
                                            adapter.setAdats(adats);
                                            gv_category.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        adats.get(i).setName(ed_edit.getText().toString());
                        adapter.notifyDataSetChanged();
                        //判断软键盘是否显示
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.cancel();
                    } else {
                        Toast.makeText(getContext(), "分类不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button but_edquxiao = (Button) window.findViewById(R.id.but_edquxiao);
            but_edquxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }

    }

    List<Kitchen_IP> list=new ArrayList<>();
    public void setIP(String tag_id,String ip){
        if (SharedUtil.getMole()!=null){
            list=SharedUtil.getMole();
            for (int i=0;i<list.size();i++){
                if (list.get(i).getTag_id().equals(tag_id)){
                    if (ip.equals("")||ip.equals("null")||ip==null){
                        list.remove(i);
                    }else {
                        if (StringUtils.IsIp(ip)) {
                            list.get(i).setIP(ip);
                        }
                    }
                    i=0;
                    break;
                }else {
                    if (StringUtils.IsIp(ip)) {
                        Kitchen_IP kitchen_ip = new Kitchen_IP();
                        kitchen_ip.setIP(ip);
                        kitchen_ip.setTag_id(tag_id);
                        list.add(kitchen_ip);
                    }
                }
            }
        }else {
            if (StringUtils.IsIp(ip)) {
                Kitchen_IP kitchen_ip = new Kitchen_IP();
                kitchen_ip.setIP(ip);
                kitchen_ip.setTag_id(tag_id);
                list.add(kitchen_ip);
            }
        }
        SharedUtil.setMole(list);
        Log.d("print","打印商品区分不同分类"+list.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_delete:
                if(isedit){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(false);
                        isedit=false;
                    }
                    adapter.notifyDataSetChanged();
                }
                if(!isVisibility){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(true);
                    }
                    adapter.notifyDataSetChanged();
                    isVisibility=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                    }
                    adapter.notifyDataSetChanged();
                    isVisibility=false;
                }
                break;
            case R.id.but_newcategory:
                final EditText editText=new EditText(getContext());
                final Fenlei_Entty fenlei_entty=new Fenlei_Entty();

                final AlertDialog dialog=new AlertDialog.Builder(getContext()).create();
                dialog.setView(new EditText(getContext()));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Window window=dialog.getWindow();
                window.setContentView(R.layout.dialog_newcategory);
                final EditText ed_new= (EditText) window.findViewById(R.id.ed_new);
                Button but_newquxiao= (Button) window.findViewById(R.id.but_newquxiao);
                Button but_newqueding= (Button) window.findViewById(R.id.but_newqueding);
                but_newquxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.cancel();
                    }
                });
                but_newqueding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ed_new.getText().toString().isEmpty()){
                            fenlei_entty.setName(ed_new.getText().toString());
                            if(isVisibility){
                                fenlei_entty.setVisibility(true);
                            }else {
                                fenlei_entty.setVisibility(false);
                            }
                            if (isedit){
                                fenlei_entty.setIsedit(true);
                            }else {
                                fenlei_entty.setIsedit(false);
                            }
                            OkGo.post(SysUtils.getGoodsServiceUrl("cat_add"))
                                    .tag(this)
                                    .cacheKey("cacheKey")
                                    .cacheMode(CacheMode.DEFAULT)
                                    .params("seller_id", SharedUtil.getString("seller_id"))
                                    .params("query", "2")
                                    .params("nav",ed_new.getText().toString())
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.d("print","新增分类"+s);
                                            JSONObject jsonobject = null;
                                            try {
                                                jsonobject = new JSONObject(s);
                                                JSONObject j1 = jsonobject.getJSONObject("response");
                                                String status=j1.getString("status");
                                                if (status.equals("200")){
                                                    JSONObject j2 = j1.getJSONObject("data");
                                                    String msg=j2.getString("msg");
                                                    Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                                                }else {
                                                    String message=j1.getString("message");
                                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                                                }
//                                                JSONArray ja = j2.getJSONArray("nav_info");
//                                                for (int i = 0; i < ja.length(); i++) {
//                                                    Fenlei_Entty fenlei = new Fenlei_Entty();
//                                                    JSONObject jo = ja.getJSONObject(i);
//                                                    fenlei.setName(jo.getString("tag_name"));
//                                                    fenlei.setTag_id(jo.getInt("tag_id"));
//                                                    adats.add(fenlei);
//                                                }
                                                adapter.setAdats(adats);
                                                gv_category.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }finally {
                                                adapter.setAdats(adats);
                                                gv_category.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }

                                        }
                                    });
                            adats.add(fenlei_entty);
                            adapter.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            dialog.cancel();
                        }else {
                            Toast.makeText(getContext(),"请输入分类名称",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.but_redact:
                if(isVisibility){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                        isVisibility=false;
                    }
                    adapter.notifyDataSetChanged();
                }
                    if (!isedit){
                        for (int i=0;i<adats.size();i++){
                            adats.get(i).setIsedit(true);
                        }
                        adapter.notifyDataSetChanged();
                        isedit=true;
                    }else {
                        for (int i=0;i<adats.size();i++){
                            adats.get(i).setIsedit(false);
                        }
                        adapter.notifyDataSetChanged();
                        isedit=false;
                    }
                break;
        }
    }
}
