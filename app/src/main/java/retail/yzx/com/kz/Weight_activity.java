package retail.yzx.com.kz;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

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
import Entty.Quick_Entty;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Adapter_Check;
import adapters.RecyclerView_adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2018/6/28.
 */

public class Weight_activity extends BaseActivity implements Adapter_Check.OnClickListener, RecyclerView_adapter.OnItemClickListener {

    public ListView lv_check;
    public List<Fenlei_Entty> listFenlei;
    public Adapter_Check leftadapter;
    public RecyclerView recyclerView;
    public RecyclerView_adapter adapter;
    public List<Commodity> commodities;

    //    快捷栏得选中数量
    public List<Quick_Entty> listquick;
    public Quick_Entty quick_entty;

    public String str;

    @Override
    protected int getContentId() {
        return R.layout.weight_activity;
    }

    @Override
    protected void init() {
        super.init();




        commodities =new ArrayList<>();
        listquick =new ArrayList<>();

        recyclerView= (RecyclerView) findViewById(R.id.Re_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        adapter = new RecyclerView_adapter(this);

        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.dimAmount =0f;

        listFenlei=new ArrayList<>();
        leftadapter=new Adapter_Check(Weight_activity.this);
        leftadapter.setOnClickListener(this);


        lv_check= (ListView) findViewById(R.id.lv_check);



        lv_check.setSelection(0);


        LoadAdats();

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

//                            liststate.clear();
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
                                commodity.setCook_position(jsonObject.getString("cook_position"));
                                commodities.add(commodity);
                                quick_entty=new Quick_Entty();
                                quick_entty.setNumber(0+"");
                                listquick.add(quick_entty);
//                                liststate.add(jsonObject.getString("reality_store"));
                            }

                            adapter.setListquick(listquick);
                            adapter.setAdats(commodities);
                            adapter.setOnItemClickListener(Weight_activity.this);
                            recyclerView.setAdapter(adapter);
//                            adapter_right.setAdats(commodities);
//                            adapter_right.setstate(liststate);
//                            lv_zhu.setAdapter(adapter_right);
//                            adapter_right.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
//                            for (int k=0;k<liststate.size();k++){
//                                if (!liststate.get(k).equals("0")){
//                                    stock_nums++;
//                                    if (Integer.parseInt(commodities.get(k).getStore())==Integer.parseInt(liststate.get(k))){
//                                        normal_nums++;
//                                    }
//                                }
//                            }
//                            tv_stock_nums.setText(stock_nums+"");
//                            tv_normal_nums.setText(normal_nums+"");
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

    @Override
    public void onClick(View view, int position) {
        if (commodities.get(position).getCook_position().equals("0")){

        }
    }
}
