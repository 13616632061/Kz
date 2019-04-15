package Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.redeem_Entty;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_redeem;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/8.
 */
public class Fragment_redeem extends Fragment implements Adapter_redeem.SetOnclick {

    public View view;
    public ListView lv;
    public Adapter_redeem adapter;
    public List<redeem_Entty> adats;
    public String statre = "0";
    public TextView tv_zong,tv_return_amount,tv_money_amount;
    public Double total_amount=Double.parseDouble(0+""),return_amount=Double.parseDouble(0+""),money_amount=Double.parseDouble(0+"");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_redeem,null);
        init();
        LoadAdats();
        return view;
    }
    private void init() {
        adats=new ArrayList<>();
        lv= (ListView) view.findViewById(R.id.lv);
        adapter=new Adapter_redeem(getActivity());
        adapter.SetOnclick(this);

        tv_zong= (TextView) view.findViewById(R.id.tv_zong);
        tv_return_amount= (TextView) view.findViewById(R.id.tv_return_amount);
        tv_money_amount= (TextView) view.findViewById(R.id.tv_money_amount);

    }
    private void LoadAdats() {
        Log.d("print","兑奖的");
        OkGo.post(SysUtils.getSellerServiceUrl("redeem_detail"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","兑奖的"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    redeem_Entty redeem=new redeem_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    redeem.setName(jo2.getString("name"));
                                    redeem.setCost(jo2.getString("cost"));
                                    redeem.setRedeem_id(jo2.getString("redeem_id"));
                                    redeem.setAddtime(jo2.getString("addtime"));
                                    redeem.setNums(jo2.getString("nums"));
                                    redeem.setOprator(jo2.getString("oprator"));
                                    redeem.setStatus(jo2.getString("status"));
                                    if (jo2.getString("status").equals("0")){
//                                        viewHored.tv_state.setText("未处理");
                                    }else if (jo2.getString("status").equals("1")){
//                                        viewHored.tv_state.setText("退钱处理");
                                        money_amount= TlossUtils.add(money_amount,Double.parseDouble(StringUtils.stringpointtwo(jo2.getString("cost"))));
                                    }else {
                                        return_amount= TlossUtils.add(return_amount,Double.parseDouble(StringUtils.stringpointtwo(jo2.getString("cost"))));
//                                        viewHored.tv_state.setText("退货处理");
                                    }

                                    redeem.setTotal_amount(jo2.getString("total_amount"));
                                    total_amount= TlossUtils.add(total_amount,Double.parseDouble(StringUtils.stringpointtwo(jo2.getString("total_amount"))));
                                    adats.add(redeem);
                                }
                                tv_money_amount.setText(money_amount+"");
                                tv_return_amount.setText(return_amount+"");
                                tv_zong.setText(total_amount+"");
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
    public void setOnClickListener(final int i) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("请选择处理状态");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.redeem_state);
        final RadioButton but_Test= (RadioButton) window.findViewById(R.id.but_Test);
        final RadioButton but_Online= (RadioButton) window.findViewById(R.id.but_Online);
        Button but_confirm= (Button) window.findViewById(R.id.but_confirm);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_Online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    but_Online.setChecked(true);
                    statre ="1";
                }
            }
        });
        but_Test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    but_Test.setChecked(true);
                    statre ="2";
                }
            }
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        but_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkGo.post(SysUtils.getSellerServiceUrl("redeem_handle"))
                        .tag(getActivity())
                        .params("status",statre)
                        .params("redeem_id",adats.get(i).getRedeem_id())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject jo1=new JSONObject(s);
                                    JSONObject jo2=jo1.getJSONObject("response");
                                    String status=jo2.getString("status");
                                    if (status.equals("200")){
                                     dialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }
}
