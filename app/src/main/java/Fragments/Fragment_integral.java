package Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import Entty.Commodity;
import Entty.Integral_Entty;
import Entty.ShuliangEntty;
import Utils.Inputmethod_Utils;
import Utils.LogUtils;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_integral;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.Addgoodgs_Activity;
import retail.yzx.com.kz.MainActivity;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;

/**
 * Created by admin on 2017/8/21.
 */
public class Fragment_integral extends Fragment implements View.OnClickListener, Adapter_integral.SetOnclickitme {


    private View view;
    private ListView lv_integral;
    private Button but_Newintegral;
    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;
    private List<Integral_Entty> adats;
    private Adapter_integral adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.integral_fragment,null);

        init();
        
        LoadAdats();
        
        return view;
    }
    //  初始化
    private void init() {

        adats=new ArrayList<>();
        adapter=new Adapter_integral(getActivity(),0);
        adapter.SetOnclickitme(this);

        //数据库操作
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        lv_integral= (ListView) view.findViewById(R.id.lv_integral);
        but_Newintegral= (Button) view.findViewById(R.id.but_Newintegral);
        but_Newintegral.setOnClickListener(this);
    }


    //加载数据
    private void LoadAdats() {
        OkGo.post(SysUtils.getGoodsServiceUrl("swap_goods_list"))
                .tag(getActivity())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","积分兑换的商品是"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            adats.clear();
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                for (int i=0;i<ja1.length();i++){
                                    Integral_Entty integral=new Integral_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    integral.setBncode(jo2.getString("bncode"));
                                    integral.setPrice(jo2.getString("price"));
                                    integral.setName(jo2.getString("name"));
                                    integral.setScore(jo2.getString("score"));
                                    integral.setNums(jo2.getString("nums"));
                                    integral.setGoods_id(jo2.getString("goods_id"));
                                    integral.setSwap_goods_id(jo2.getString("swap_goods_id"));
                                    adats.add(integral);
                                }
                            }
                                adapter.setAdats(adats);
                                lv_integral.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_Newintegral:
                final Dialog dialog= new Dialog(getActivity());
                dialog.setTitle("新增兑换商品");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.new_integral);
                final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
                Button but_preserve= (Button) window.findViewById(R.id.but_preserve);
                final EditText ed_integral= (EditText) window.findViewById(R.id.ed_integral);
                but_preserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String goods_id = "";
                        if (ed_bncode.getText().toString()!=null){
                            Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode=?", new String[]{ed_bncode.getText().toString()});
                            if (cursor.getCount()!=0){
                            while (cursor.moveToNext()){
                               goods_id = cursor.getString(cursor.getColumnIndex("goods_id"));
                            }
                            UpIntegral(goods_id,ed_integral.getText().toString(),"0");
                            }else {
                                getSeek(ed_bncode.getText().toString(),ed_integral.getText().toString());
//                                Toast.makeText(getActivity(),"没有该条码对应的商品",Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();

                    }

                });
                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
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

                break;
        }
    }


    public void getSeek(final String str, final String string) {
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("bncode",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                if (ja1.length() == 0) {
                                } else if (ja1.length()==1){
                                    for (int j = 0; j < ja1.length(); j++) {
                                        Commodity commodity = new Commodity();
                                        JSONObject jo2 = ja1.getJSONObject(j);
                                        commodity.setGoods_id(jo2.getString("goods_id"));
                                        commodity.setIs_special_offer(jo2.getString("is_special"));
                                        commodity.setName(jo2.getString("name"));
                                        commodity.setPy(jo2.getString("py"));
                                        commodity.setPrice(jo2.getString("price"));
                                        commodity.setMember_price(jo2.getString("member_price"));
                                        commodity.setCost(jo2.getString("cost"));
                                        commodity.setBncode(jo2.getString("bncode"));
                                        commodity.setStore(jo2.getString("store"));
                                        UpIntegral(str,string,"0");
                                    }
                                }
                            }else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void UpIntegral(String goods_id,String score,String j){
        OkGo.post(SysUtils.getGoodsServiceUrl("swap_goods_add"))
                .tag(getActivity())
                .params("goods_id",goods_id)
                .params("score",score)
                .params("swap_goods_id",j)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","积分兑换的商品是"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String data=jo1.getString("data");
                            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            LoadAdats();
                        }
                    }
                });
    }

    //点击编辑
    @Override
    public void OnclickEdit(final int i) {
        final Dialog dialog1= new Dialog(getActivity());
        dialog1.setTitle("新增兑换商品");
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setContentView(R.layout.new_integral);
        EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
        final EditText ed_integral= (EditText) window.findViewById(R.id.ed_integral);
        ed_bncode.setText(adats.get(i).getBncode());
        ed_integral.setText(adats.get(i).getScore());
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_cancel.setText("删除");
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteIntegral(adats.get(i).getSwap_goods_id());
                dialog1.dismiss();
                LoadAdats();
            }
        });


        Button but_preserve= (Button) window.findViewById(R.id.but_preserve);
        but_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpIntegral(adats.get(i).getGoods_id(),ed_integral.getText().toString(),adats.get(i).getSwap_goods_id());
                dialog1.dismiss();

            }
        });
        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Inputmethod_Utils.getshow(getActivity());
            }
        });
    }



    public void DeleteIntegral(String id){
        OkGo.post(SysUtils.getGoodsServiceUrl("swap_goods_del"))
                .tag(getActivity())
                .params("swap_goods_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String data=jo1.getString("data");
                            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
