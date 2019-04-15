package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import Entty.Fenlei_Entty;
import Entty.GridView_xuangzhong;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Adapter_Check;
import adapters.Adapter_right_check;
import base.BaseFragment;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.Inventory_submitted_activity;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/3.
 * 盘点
 */
public class Fragment_check extends BaseFragment implements Adapter_Check.OnClickListener, Adapter_right_check.SetOnclick, View.OnClickListener {

    public View view;
    public GridView lv_zhu;
    public ListView lv_check;
    public Adapter_right_check adapter_right;
    public Adapter_Check leftadapter;
    public List<Fenlei_Entty> listFenlei;
    public String str="";
    public ArrayList<Commodity> commodities;

    public ArrayList<String> liststate;
    public TextView tv_stock_nums,tv_total_nums,tv_normal_nums;

    public int stock_nums=0,total_nums=0,normal_nums=0;
    public Button but_nextstep;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.fragment_check_layout,null);
//        init();
//        LoadAdats();
//        lv_check.setSelection(0);
//
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_check_layout;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
        LoadAdats();
        lv_check.setSelection(0);
    }

    private void init1(View view) {

        but_nextstep= (Button) view.findViewById(R.id.but_nextstep);
        but_nextstep.setOnClickListener(this);
        tv_total_nums= (TextView) view.findViewById(R.id.tv_total_nums);
        tv_stock_nums= (TextView) view.findViewById(R.id.tv_stock_nums);
        tv_normal_nums= (TextView) view.findViewById(R.id.tv_normal_nums);

        liststate=new ArrayList<>();
        commodities=new ArrayList<>();
        listFenlei=new ArrayList<>();
        lv_check= (ListView) view.findViewById(R.id.lv_check);
        lv_zhu= (GridView) view.findViewById(R.id.lv_zhu);
        lv_zhu.setNumColumns(2);

        leftadapter=new Adapter_Check(getActivity());
        leftadapter.setOnClickListener(this);
        adapter_right=new Adapter_right_check(getContext());
        adapter_right.SetOnclick(this);
    }
    private void LoadAdats() {
        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("seller_token", SharedUtil.getString("seller_token"))
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
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                if (listFenlei.size()==0){
                                    fenlei.setVisibility(true);
                                }else {
                                    fenlei.setVisibility(false);
                                }
                                listFenlei.add(fenlei);
                            }
                            leftadapter.setAdats(listFenlei);
                            lv_check.setAdapter(leftadapter);
                            getadats(listFenlei.get(0).getTag_id()+"");
//                            leftadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    @Override
    public void setonclick(int i) {
        str=listFenlei.get(i).getTag_id()+"";
        for (int j=0;j<listFenlei.size();j++){
            if (j==i){
                listFenlei.get(j).setVisibility(true);
            }else {
                listFenlei.get(j).setVisibility(false);
            }
        }
        leftadapter.setAdats(listFenlei);
        lv_check.setAdapter(leftadapter);
        lv_check.setSelection(i);
        getadats(str);
    }

    //获取分类下的商品
    public void getadats(String s1){
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("tag_id", s1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","该分类下的商品是"+s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            commodities.clear();
                            liststate.clear();
                            JSONArray ja2=j2.getJSONArray("sum");
                            JSONObject jo4=ja2.getJSONObject(0);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Commodity commodity = new Commodity();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                commodity.setGoods_id(jsonObject.getString("goods_id"));
                                commodity.setName(jsonObject.getString("name"));
                                commodity.setPy(jsonObject.getString("py"));
//                                commodity.setSeller_id(jsonObject.getString("seller_id"));
                                commodity.setPrice(jsonObject.getString("price"));
                                commodity.setCost(jsonObject.getString("cost"));
                                commodity.setBncode(jsonObject.getString("bncode"));
                                commodity.setTag_id(jsonObject.getString("tag_id"));
                                commodity.setTag_name(jsonObject.getString("tag_name"));
                                commodity.setUnit(jsonObject.getString("unit"));
                                commodity.setStore(jsonObject.getString("store"));
                                commodity.setGood_limit(jsonObject.getString("good_limit"));
                                commodity.setGood_stock(jsonObject.getString("good_stock"));
                                commodity.setPD(jsonObject.getString("PD"));
                                commodity.setGD(jsonObject.getString("GD"));
                                commodity.setMarketable(jsonObject.getString("marketable"));
                                commodity.setAltc(jsonObject.getString("ALTC"));
                                commodities.add(commodity);
                                liststate.add(jsonObject.getString("reality_store"));
                            }
                            adapter_right.setAdats(commodities);
                            adapter_right.setstate(liststate);
                            lv_zhu.setAdapter(adapter_right);
                            adapter_right.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            for (int k=0;k<liststate.size();k++){
                                if (!liststate.get(k).equals("0")){
                                    stock_nums++;
                                    if (Float.parseFloat(commodities.get(k).getStore())==Float.parseFloat(liststate.get(k))){
                                        normal_nums++;
                                    }
                                }
                            }
                            tv_stock_nums.setText(stock_nums+"");
                            tv_normal_nums.setText(normal_nums+"");
                        }
                    }
                });



    }

    @Override
    public void onclickdialog(final int i) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("盘点");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_check);
        TextView tv_tv_inventory_nums= (TextView) window.findViewById(R.id.tv_tv_inventory_nums);
        tv_tv_inventory_nums.setText(commodities.get(i).getStore());
        final EditText tv_stock_nums= (EditText) window.findViewById(R.id.tv_stock_nums);
        Button but_determine= (Button) window.findViewById(R.id.but_determine);
        but_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upstock(tv_stock_nums.getText().toString(),commodities.get(i).getGoods_id());

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

    public void upstock(String stock,String id){
        OkGo.post(SysUtils.getGoodsServiceUrl("take_stock"))
                .tag(getActivity())
                .params("reality_store",stock)
                .params("goods_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","上传的参数"+s);
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
                            getadats(str);
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_nextstep:
                Intent intent=new Intent(getActivity(), Inventory_submitted_activity.class);
//                intent.putCharSequenceArrayListExtra("",commodities);
                intent.putExtra("commodities",commodities);
                intent.putStringArrayListExtra("liststate",liststate);
                startActivity(intent);
                break;
        }

    }
}
