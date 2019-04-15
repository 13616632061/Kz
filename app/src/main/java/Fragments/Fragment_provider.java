package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Provide_Entty;
import Utils.SysUtils;
import adapters.Adapter_provider;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/31.
 */
public class Fragment_provider extends Fragment implements View.OnClickListener, Adapter_provider.SetONClickedit {
    public View view;
    public Button but_supplier;
    public ListView lv_provide;
    public List<Provide_Entty> adats;
    public Adapter_provider adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.pragment_fragment,null);
        init();
        LoadAdats();
        return view;
    }



    private void init() {
        but_supplier= (Button) view.findViewById(R.id.but_supplier);
        but_supplier.setOnClickListener(this);
        lv_provide= (ListView) view.findViewById(R.id.lv_provide);
        adapter=new Adapter_provider(getActivity());
        adapter.SetONClickedit(this);
        adats=new ArrayList<>();
    }

    //加载数据
    private void LoadAdats() {
        OkGo.post(SysUtils.getGoodsServiceUrl("provider_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","数据为"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Provide_Entty provide_entty=new Provide_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    provide_entty.setProvider_name(jo2.getString("provider_name"));
                                    provide_entty.setProvider_bncode(jo2.getString("provider_bncode"));
                                    provide_entty.setProvider_name(jo2.getString("provider_name"));
                                    provide_entty.setPhone(jo2.getString("phone"));
                                    provide_entty.setAddress(jo2.getString("address"));
                                    provide_entty.setRemark(jo2.getString("remark"));
                                    provide_entty.setContact(jo2.getString("contact"));
                                    provide_entty.setAccount(jo2.getString("account"));
                                    provide_entty.setPy(jo2.getString("pym"));
                                    provide_entty.setProvider_id(jo2.getString("provider_id"));
                                    adats.add(provide_entty);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.setAdats(adats);
                            lv_provide.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_supplier:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("新增供应商");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_provider);
                Button but_bncode= (Button) window.findViewById(R.id.but_bncode);
                Button but_new_provide= (Button) window.findViewById(R.id.but_new_provide);
                final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
                final EditText ed_name= (EditText) window.findViewById(R.id.ed_name);
                final EditText ed_py= (EditText) window.findViewById(R.id.ed_py);
                final EditText ed_linkman= (EditText) window.findViewById(R.id.ed_linkman);
                final EditText ed_phone= (EditText) window.findViewById(R.id.ed_phone);
                final EditText ed_address= (EditText) window.findViewById(R.id.ed_address);
                final EditText ed_remark= (EditText) window.findViewById(R.id.ed_remark);
                final EditText ed_account= (EditText) window.findViewById(R.id.ed_account);
                ed_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            String py=PinyinHelper.getShortPinyin(ed_name.getText().toString()).toUpperCase();
                            ed_py.setText(py);
                        } catch (PinyinException e) {
                            e.printStackTrace();
                        }
                    }
                });

                but_bncode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.post(SysUtils.getGoodsServiceUrl("get_code"))
                                .tag(this)
                                .cacheKey("cacheKey")
                                .connTimeOut(1000)
                                .cacheMode(CacheMode.DEFAULT)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print", "生成成功" + s);
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                            JSONObject jo2 = jo1.getJSONObject("data");
                                            String code = jo2.getString("code");
                                            ed_bncode.setText(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                });
                but_new_provide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ed_name.getText().toString().isEmpty()&&!ed_bncode.getText().toString().isEmpty()){
                            List<Map<String,String>> listmap=new ArrayList<Map<String, String>>();
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("provider_name",ed_name.getText().toString());
                            map.put("provider_bncode",ed_bncode.getText().toString());
                            map.put("pym",ed_py.getText().toString());
                            map.put("contact",ed_linkman.getText().toString());
                            map.put("phone",ed_phone.getText().toString());
                            map.put("address",ed_address.getText().toString());
                            map.put("account",ed_account.getText().toString());
                            map.put("remark",ed_remark.getText().toString());
                            listmap.add(map);
                            Gson gson=new Gson();
                            String str=gson.toJson(listmap);
                            Log.d("print","添加"+str);
                            OkGo.post(SysUtils.getGoodsServiceUrl("add_provider"))
                                    .tag(getActivity())
                                    .params("map",str)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.e("print","供应商"+s);
                                            JSONObject jsonobject= null;
                                            try {
                                                jsonobject = new JSONObject(s);
                                                JSONObject jo1=jsonobject.getJSONObject("response");
                                                String status=jo1.getString("status");
                                                String data=jo1.getString("data");
                                                if (status.equals("200")){
                                                    Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            LoadAdats();
                                            dialog.dismiss();
                                        }
                                    });

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
                break;
        }
    }

    @Override
    public void setOnclickEdit(final int i) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("编辑供应商");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_provider);
        Button but_bncode= (Button) window.findViewById(R.id.but_bncode);
        Button but_delete_provide= (Button) window.findViewById(R.id.but_delete_provide);
        but_delete_provide.setVisibility(View.VISIBLE);
        but_delete_provide.setText("删除供应商");
        Button but_new_provide= (Button) window.findViewById(R.id.but_new_provide);
        but_new_provide.setText("保存");
        final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
        final EditText ed_name= (EditText) window.findViewById(R.id.ed_name);
        final EditText ed_py= (EditText) window.findViewById(R.id.ed_py);
        final EditText ed_linkman= (EditText) window.findViewById(R.id.ed_linkman);
        final EditText ed_phone= (EditText) window.findViewById(R.id.ed_phone);
        final EditText ed_address= (EditText) window.findViewById(R.id.ed_address);
        final EditText ed_remark= (EditText) window.findViewById(R.id.ed_remark);

        ed_bncode.setText(adats.get(i).getProvider_bncode());
        ed_name.setText(adats.get(i).getProvider_name());
        ed_py.setText(adats.get(i).getPy());
        ed_linkman.setText(adats.get(i).getContact());
        ed_phone.setText(adats.get(i).getPhone());
        ed_address.setText(adats.get(i).getAddtime());
        ed_address.setText(adats.get(i).getAccount());
        ed_remark.setText(adats.get(i).getRemark());
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String py=PinyinHelper.getShortPinyin(ed_name.getText().toString()).toUpperCase();
                    ed_py.setText(py);
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
            }
        });

        but_new_provide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkGo.post(SysUtils.getGoodsServiceUrl("edit_provider"))
                        .tag(getActivity())
                        .params("provider_id",adats.get(i).getProvider_id())
                        .params("provider_name",ed_name.getText().toString())
                        .params("provider_bncode",ed_bncode.getText().toString())
                        .params("pym",ed_py.getText().toString())
                        .params("contact",ed_linkman.getText().toString())
                        .params("phone",ed_phone.getText().toString())
                        .params("address",ed_address.getText().toString())
                        .params("remark",ed_remark.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print","编辑"+s);
                                try {
                                    JSONObject jsonobject=new JSONObject(s);
                                    JSONObject jo1=jsonobject.getJSONObject("response");
                                    String status=jo1.getString("status");
                                    String data=jo1.getString("data");
                                    if (status.equals("200")){
                                        Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                                    }
                                    LoadAdats();
                                    dialog.dismiss();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }finally {

                                }

                            }
                        });
            }
        });
        but_delete_provide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDelete(adats.get(i).getProvider_id());
                LoadAdats();
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

    public void setDelete(String id){
        OkGo.post(SysUtils.getGoodsServiceUrl("del_provider"))
                .tag(getActivity())
                .params("provider_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



    }


}
