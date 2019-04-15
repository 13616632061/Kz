package Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Specification_Entty;
import Utils.Inputmethod_Utils;
import Utils.SysUtils;
import adapters.Adapter_specification;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import widget.Switch;

/**
 * Created by admin on 2017/8/23.
 */
public class Fragment_member_specification extends Fragment implements Adapter_specification.SetOnclick {


    @BindView(R.id.gv_specification)
    GridView gvSpecification;
    @BindView(R.id.tv_shuliang)
    TextView tvShuliang;
    @BindView(R.id.but_Newintegral)
    Button butNewintegral;
    Unbinder unbinder;
    private View view;
    private List<Specification_Entty> adats;
    private Adapter_specification adapter;
    private String type="1";
    private String is_show="yes";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.member_specification_fragment, null);

        unbinder = ButterKnife.bind(this, view);
        init();
        getData();
        Loadadats();

        return view;
    }

    public Fragment_member_specification() {

    }


    @SuppressLint("ValidFragment")
    public Fragment_member_specification(String type) {
        this.type=type;
    }

    private void init() {
        adats=new ArrayList<>();
        adapter=new Adapter_specification(getActivity());
        adapter.SetOnclick(this);
        gvSpecification.setNumColumns(2);
    }

    private void Loadadats() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.but_Newintegral)
    public void onViewClicked() {
        final Dialog dialog= new Dialog(getActivity());
        dialog.setTitle("充值金额");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.new_integral);
        TextView tv_name1= (TextView) window.findViewById(R.id.tv_name1);
        TextView tv_name2= (TextView) window.findViewById(R.id.tv_name2);
        Button but_preserve= (Button) window.findViewById(R.id.but_preserve);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        RelativeLayout ll_3= (RelativeLayout) window.findViewById(R.id.ll_3);
        Switch sw_price= (Switch) window.findViewById(R.id.sw_price);
        final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
        final EditText ed_integral= (EditText) window.findViewById(R.id.ed_integral);

        if (type.equals("1")){
            ll_3.setVisibility(View.VISIBLE);
            tv_name1.setText("充值金额");
            tv_name2.setText("赠送金额");
        }else if (type.equals("2")){
            ll_3.setVisibility(View.GONE);
            tv_name1.setText("抵扣积分");
            tv_name2.setText("抵扣金额");
        }
        sw_price.setChecked(true);
        sw_price.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    is_show="yes";
                }else {
                    is_show="no";
                }
            }
        });


//        type 判断是抵扣：1
//              不是抵扣：2

        but_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map=new HashMap<String, String>();
                map.put("val",ed_bncode.getText().toString());
                map.put("give",ed_integral.getText().toString());
                map.put("is_show",is_show);
                map.put("type",type);
                Gson gson=new Gson();
                String str=gson.toJson(map);
                Uprecharge_type_add(str);
                dialog.dismiss();
            }
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Inputmethod_Utils.getshow(getActivity());
            }
        });
    }

    //新增接口
    private void Uprecharge_type_add(String map){
        OkGo.post(SysUtils.getSellerServiceUrl("recharge_type_add"))
                .tag(getActivity())
                .params("map",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","添加的数据为"+s);
                        Log.d("print","type"+type);
                        try {
                            JSONObject jsonbject=new JSONObject(s);
                            JSONObject jo1=jsonbject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getData();
                    }
                });
    }

    private void getData(){
        OkGo.post(SysUtils.getSellerServiceUrl("recharge_list"))
                .tag(getActivity())
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                            Log.e("print","规格的"+s);
                            Log.e("print","type"+type);
                        try {
                            adats.clear();
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    Specification_Entty specification=new Specification_Entty();
                                    specification.setRecharge_id(jo2.getString("recharge_id"));
                                    specification.setGive(jo2.getString("give"));
                                    specification.setIs_show(jo2.getString("is_show"));
                                    specification.setVal(jo2.getString("val"));
                                    adats.add(specification);
                                }
                            }
                            if (gvSpecification!=null){
                                adapter.setAdats(adats,0);
                                adapter.setType(type);
                                gvSpecification.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //编辑
    @Override
    public void onclickdialog(final int i) {
        final Dialog dialog= new Dialog(getActivity());
        dialog.setTitle("充值金额");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.new_integral);
        TextView tv_name1= (TextView) window.findViewById(R.id.tv_name1);
        TextView tv_name2= (TextView) window.findViewById(R.id.tv_name2);
        RelativeLayout ll_3= (RelativeLayout) window.findViewById(R.id.ll_3);
        if (type.equals("1")){
            ll_3.setVisibility(View.VISIBLE);
            tv_name1.setText("充值金额");
            tv_name2.setText("赠送金额");
        }else if (type.equals("2")){
            ll_3.setVisibility(View.GONE);
            tv_name1.setText("抵扣积分");
            tv_name2.setText("抵扣金额");
        }
        Button but_preserve= (Button) window.findViewById(R.id.but_preserve);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_cancel.setText("删除");
        final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
        final EditText ed_integral= (EditText) window.findViewById(R.id.ed_integral);
         Switch sw_price= (Switch) window.findViewById(R.id.sw_price);
        ed_bncode.setText(adats.get(i).getVal());
        ed_integral.setText(adats.get(i).getGive());
        if (adats.get(i).getIs_show().equals("no")){
            sw_price.setChecked(false);
        }else {
            sw_price.setChecked(true);
        }
        sw_price.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    is_show="yes";
                }else {
                    is_show="no";
                }
            }
        });

        but_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map=new HashMap<String, String>();
                map.put("val",ed_bncode.getText().toString());
                map.put("give",ed_integral.getText().toString());
                map.put("is_show",is_show);
                map.put("recharge_id",adats.get(i).getRecharge_id());
                Gson gson=new Gson();
                String str=gson.toJson(map);
                Uprecharge_type_add(str);
                dialog.dismiss();
            }
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletespecification(adats.get(i).getRecharge_id());
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Inputmethod_Utils.getshow(getActivity());
            }
        });
    }
    private void Deletespecification(String id){
        OkGo.post(SysUtils.getSellerServiceUrl("recharge_del"))
                .tag(getActivity())
                .params("recharge_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","添加的数据为"+s);
                        try {
                            JSONObject jsonbject=new JSONObject(s);
                            JSONObject jo1=jsonbject.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getData();

                    }
                });
    }

}
