package retail.yzx.com.kz;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Order_Entty;
import Utils.SysUtils;
import adapters.Leibu_adapter;
import adapters.Order_adapter;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2018/11/10.
 */

public class Take_outfood_Activity extends BaseActivity {

    @BindView(R.id.lv_takeout_foot)
    ListView lv_takeout_foot;
    @BindView(R.id.im_huanghui)
    ImageView im_huanghui;



    public Leibu_adapter leibu_adapter;
    public List<Order_Entty.ResponseBean.DataBean> list_order;
    public Order_Entty.ResponseBean.DataBean order_entty;

    public List<Order_Entty.ResponseBean.DataBean.GoodsBean> list_goodsBean;

    public Order_adapter adapter;

    @Override
    protected int getContentId() {
        return R.layout.take_outfoot_activity;
    }

    @Override
    protected void init() {
        super.init();
        Intent intent=getIntent();
        //type wei 0 的时候为订单详情页面  1  为订单列表页面
        if (intent.getStringExtra("type").equals("0")){
            String order = intent.getStringExtra("order");
            getTake_outfood_order(order);
        }
        list_goodsBean=new ArrayList<>();
        list_order=new ArrayList<>();
        adapter=new Order_adapter(this,true);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        //获取外面订单列表
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_info"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            JSONArray ja1 = jo1.getJSONArray("data");
                            list_goodsBean.clear();
                            for (int i = 0; i < ja1.length(); i++) {
                                order_entty = new Order_Entty.ResponseBean.DataBean();
                                JSONObject jo2 = ja1.getJSONObject(i);
                                JSONArray ja2 = jo2.getJSONArray("goods");
                                for (int j = 0; j < ja2.length(); j++) {
                                    Order_Entty.ResponseBean.DataBean.GoodsBean goodsBean = new Order_Entty.ResponseBean.DataBean.GoodsBean();
                                    JSONObject jo3 = ja2.getJSONObject(0);
                                    goodsBean.setGoods_name(jo3.getString("goods_name"));
                                    goodsBean.setCost(jo3.getString("cost"));
                                    goodsBean.setStore(jo3.getString("store"));
                                    goodsBean.setGoods_id(jo3.getString("goods_id"));
                                    goodsBean.setGoods_num(jo3.getString("goods_num"));
                                    goodsBean.setGoods_price(jo3.getString("goods_price"));
                                    list_goodsBean.add(goodsBean);
                                }
                                order_entty.setGoods(list_goodsBean);
                                order_entty.setOrder_id(jo2.getString("order_id"));
                                order_entty.setObj_id(jo2.getString("obj_id"));
                                order_entty.setPay_status(jo2.getString("pay_status"));
                                order_entty.setSeller_id(jo2.getString("seller_id"));
                                order_entty.setCreatetime(jo2.getString("createtime"));
                                order_entty.setName(jo2.getString("name"));
                                order_entty.setMark_text(jo2.getString("mark_text"));
                                order_entty.setNums(jo2.getString("nums"));
                                order_entty.setPrice(jo2.getString("price"));
                                order_entty.setGoods_id(jo2.getString("goods_id"));
                                order_entty.setItem_id(jo2.getString("item_id"));
                                list_order.add(order_entty);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.getadats(list_order);
                            adapter.setOnitemclick(null);
                            lv_takeout_foot.setAdapter(adapter);
                        }
                    }
                });
    }

    /**
     * 获取订单数据详情
     * @param order 订单号
     */
    public void getTake_outfood_order(String order){
        //加载外卖订单详情
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });
    }

    @OnClick(R.id.im_huanghui)
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.im_huanghui:
            finish();
            break;
        }
    }
    public void showdialog(){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(Take_outfood_Activity.this).create();
        dialog.show();
        final Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_recharge);
        Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
        but_cancel.setVisibility(View.GONE);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        leibu_adapter=new Leibu_adapter(this);
    }
}
