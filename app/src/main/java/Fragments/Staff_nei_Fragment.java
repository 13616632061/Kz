package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Staff_entty;
import Utils.SysUtils;
import adapters.Sp3adapter;
import adapters.Staff_adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.New_employeeactivity;
import retail.yzx.com.kz.New_role;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/6.
 */
public class Staff_nei_Fragment extends Fragment implements View.OnClickListener, Staff_adapter.Oneidtextonclick {

    public View view;
    public TextView ed_seek;
    public Button but_newemployee, but_role;
    public ListView lv;
    public Staff_adapter adapter;
    public List<Staff_entty> adats;
    //启用状态
    public TextView tv_state;

    public View layout_tv_state;
    public ListView lv_pop;
    public List<String> sp2adats;
    public Sp3adapter sp3adapter;
    public PopupWindow pop_state;
    public String state = "all";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.staff_nei_fragment, null);
        init();
        Loadats();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Loadats();
    }

    private void init() {

        adats = new ArrayList<>();
        adapter = new Staff_adapter(getContext());
        adapter.setOneidtextonclick(this);

        sp2adats = new ArrayList<>();
        sp2adats.add("全部状态");
        sp2adats.add("启用");
        sp2adats.add("禁用");

        tv_state = (TextView) view.findViewById(R.id.tv_state);
        tv_state.setOnClickListener(this);
        lv = (ListView) view.findViewById(R.id.lv);
        //员工搜索
        ed_seek = (TextView) view.findViewById(R.id.ed_seek);
        ed_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    ed_seek.getText().toString();
                    getSeek(ed_seek.getText().toString());
                }
                return true;
            }
        });
        but_newemployee = (Button) view.findViewById(R.id.but_newemployee);
        but_newemployee.setOnClickListener(this);
        but_role = (Button) view.findViewById(R.id.but_role);
        but_role.setOnClickListener(this);
    }

    public void getSeek(String string) {
        OkGo.post(SysUtils.getSellerServiceUrl("search_employee"))
                .tag(getActivity())
                .params("map", string)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","员工表"+s);
                        try {
                            adats.clear();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Staff_entty staff_entty = new Staff_entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    staff_entty.setBn(jo2.getString("bn"));
                                    staff_entty.setLogin_name(jo2.getString("login_name"));
                                    staff_entty.setPhone(jo2.getString("phone"));
                                    staff_entty.setRate(jo2.getString("rate"));
                                    staff_entty.setDisable(jo2.getString("disable"));
                                    staff_entty.setWork_id(jo2.getString("work_id"));
                                    staff_entty.setProfit_disable(jo2.getString("profit_disable"));
                                    staff_entty.setCost_disable(jo2.getString("cost_disable"));
                                    staff_entty.setStore_disable(jo2.getString("store_disable"));
                                    adats.add(staff_entty);
                                }
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

    public void filtrateSeek(String str) {
        OkGo.post(SysUtils.getSellerServiceUrl("search_employee"))
                .tag(getActivity())
                .params("disable", str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "搜索的员工为" + s);
                        try {
                            adats.clear();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Staff_entty staff_entty = new Staff_entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    staff_entty.setBn(jo2.getString("bn"));
                                    staff_entty.setLogin_name(jo2.getString("login_name"));
                                    staff_entty.setPhone(jo2.getString("phone"));
                                    staff_entty.setRate(jo2.getString("rate"));
                                    staff_entty.setDisable(jo2.getString("disable"));
                                    staff_entty.setWork_id(jo2.getString("work_id"));
                                    staff_entty.setProfit_disable(jo2.getString("profit_disable"));
                                    staff_entty.setCost_disable(jo2.getString("cost_disable"));
                                    staff_entty.setStore_disable(jo2.getString("store_disable"));
                                    adats.add(staff_entty);
                                }
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

    private void Loadats() {
        OkGo.post(SysUtils.getSellerServiceUrl("employee_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "员工表" + s);
                        try {
                            adats.clear();
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject jo1 = jsonobject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Staff_entty staff = new Staff_entty();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    staff.setBn(jo2.getString("bn"));
                                    staff.setLogin_name(jo2.getString("login_name"));
                                    staff.setPhone(jo2.getString("phone"));
                                    staff.setRate(jo2.getString("rate"));
                                    staff.setDisable(jo2.getString("disable"));
                                    staff.setWork_id(jo2.getString("work_id"));
                                    staff.setProfit_disable(jo2.getString("profit_disable"));
                                    staff.setCost_disable(jo2.getString("cost_disable"));
                                    staff.setStore_disable(jo2.getString("store_disable"));
                                    adats.add(staff);
                                }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            新增员工按钮
            case R.id.but_newemployee:
                startActivity(new Intent(getActivity(), New_employeeactivity.class));
                break;
            case R.id.but_role:
                startActivity(new Intent(getActivity(), New_role.class));
                break;
            case R.id.tv_state:
                layout_tv_state = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                lv_pop = (ListView) layout_tv_state.findViewById(R.id.lv_pop);
                sp3adapter = new Sp3adapter(getContext());
                sp3adapter.setAdats(sp2adats);
                lv_pop.setAdapter(sp3adapter);
                lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_state.setText(sp2adats.get(i));
                        if (i == 0) {
                            state = "all";
                        } else if (i == 1) {
                            state = "true";
                        } else if (i == 2) {
                            state = "false";
                        }
                        filtrateSeek(state);
                        pop_state.dismiss();
                    }
                });
                pop_state = new PopupWindow(layout_tv_state, tv_state.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                pop_state.setTouchable(true);// 设置popupwindow可点击  
                pop_state.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                pop_state.setFocusable(true);// 获取焦点  

                pop_state.showAsDropDown(tv_state);
                pop_state.getContentView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        pop_state.setFocusable(false);//失去焦点  
                        pop_state.dismiss();//消除pw 
                        return true;
                    }
                });
                break;
        }
    }

    @Override
    public void itmeeidtonclick(int i) {
        Intent intent = new Intent(getContext(), New_employeeactivity.class);
        adats.get(i);
        intent.putExtra("staff", adats.get(i));
        startActivity(intent);
    }
}