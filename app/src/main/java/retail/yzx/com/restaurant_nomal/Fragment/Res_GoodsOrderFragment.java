package retail.yzx.com.restaurant_nomal.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Adapter.Res_GoodsOrderFragmentAdapter;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import shujudb.Sqlite_Entity;

/**
 * Created by Administrator on 2017/7/31.
 */

public class Res_GoodsOrderFragment extends BaseFragment  {
    private ListView list_goodsorder;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist;
    private ArrayList<Res_GoodsOrders> mRes_GoodsOrderslist;

    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.layout_res_goodsorderfragment,null);
        list_goodsorder= (ListView) view.findViewById(R.id.list_goodsorder);
        return view;
    }

    @Override
    protected void initData() {
        mContext.registerReceiver(broadcastReceiver,new IntentFilter("Res_GoodsOrderFragment"));
        getRes_GoodsOrders();
    }

    private void getRes_GoodsOrders(){

        Sqlite_Entity sqlite_entity=new Sqlite_Entity(getActivity());
        String json=sqlite_entity.queryguadanorder();
        try {
            JSONArray jsonArray=new JSONArray(json);
            mRes_GoodsOrderslist=new ArrayList<Res_GoodsOrders>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String order_id=jsonObject.getString("order_id");
                String createtime=jsonObject.getString("createtime");
                String order_name=jsonObject.getString("name");
                String total_amount=jsonObject.getString("total_amount");
                String desk_num=jsonObject.getString("table_name");
                String customer_num=jsonObject.getString("customer_num");
                String dopackage=jsonObject.getString("package");
                String mark_text=jsonObject.getString("remarks");
//                String order_nums=jsonObject.getString("order_nums");
                String table_id=jsonObject.getString("table_id");
                String Jsongoods=sqlite_entity.queryguadangoods(order_id);
                Log.e("print","打印挂单数据json"+Jsongoods);
                JSONArray jsonArray1=new JSONArray(Jsongoods);
                mSelf_Service_GoodsInfolist=new ArrayList<Self_Service_GoodsInfo>();
                for (int j=0;j<jsonArray1.length();j++){
                    JSONObject object=jsonArray1.getJSONObject(j);
                    String goods_id=object.getString("goods_id");
//                    String product_id=object.getString("product_id");
                    String name=object.getString("name");
                    String cost=object.getString("cost");
                    String price=object.getString("price");
                    String nums=object.getString("number");
                    String goods_size=object.getString("goods_size");
                    String goods_notes=object.getString("marketable");
                    String tag_id=object.getString("tag_id");
                    Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,nums,cost,price,goods_notes,goods_size,"1",tag_id);
                    mSelf_Service_GoodsInfolist.add(mSelf_Service_GoodsInfo);
                }
                Res_GoodsOrders mRes_GoodsOrders=new Res_GoodsOrders(order_id,createtime,desk_num,customer_num,mark_text,"0",total_amount,dopackage,order_name,table_id,false,mSelf_Service_GoodsInfolist);
                mRes_GoodsOrderslist.add(mRes_GoodsOrders);
            }
            Res_GoodsOrderFragmentAdapter mRes_GoodsOrderFragmentAdapter=new Res_GoodsOrderFragmentAdapter(mContext,mRes_GoodsOrderslist);
            list_goodsorder.setAdapter(mRes_GoodsOrderFragmentAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.e("barcode", "返回数据：");
//        OkGo.post(SysUtils.getSellerServiceUrl("catering_order_info"))
//                .tag(this)
//                .params("seller_token", SharedUtil.getString("seller_token"))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onBefore(BaseRequest request) {
//                        super.onBefore(request);
//                        Log.e("barcode", "请求发送：" + request.getUrl());
//                    }
//
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            Log.e("barcode", "返回数据：" + jsonObject);
//                            JSONObject jo1 = jsonObject.getJSONObject("response");
//                            String status = jo1.getString("status");
//                            if (status.equals("200")) {
//                                JSONArray data=jo1.getJSONArray("data");
//                                mRes_GoodsOrderslist=new ArrayList<Res_GoodsOrders>();
//                                if(data!=null){
//                                    for(int i=0;i<data.length();i++){
//                                        JSONObject data_object=data.getJSONObject(i);
//                                        String order_id=data_object.getString("order_id");
//                                        String createtime=data_object.getString("createtime");
//                                        String order_name=data_object.getString("order_name");
//                                        String total_amount=data_object.getString("total_amount");
//                                        String desk_num=data_object.getString("desk_num");
//                                        String customer_num=data_object.getString("customer_num");
//                                        String dopackage=data_object.getString("package");
//                                        String mark_text=data_object.getString("mark_text");
//                                        String order_nums=data_object.getString("order_nums");
//                                        String table_id=data_object.getString("table_id");
//                                        JSONArray goods_info=null;
//                                                try{
//                                                    goods_info=data_object.getJSONArray("goods_info");
//                                                }catch (Exception e){}
//                                         mSelf_Service_GoodsInfolist=new ArrayList<Self_Service_GoodsInfo>();
//                                        if(goods_info!=null){
//                                            for(int j=0;j<goods_info.length();j++){
//                                                JSONObject goods_info_obj=goods_info.getJSONObject(j);
//                                                String goods_id=goods_info_obj.getString("goods_id");
//                                                String product_id=goods_info_obj.getString("product_id");
//                                                String name=goods_info_obj.getString("name");
//                                                String cost=goods_info_obj.getString("cost");
//                                                String price=goods_info_obj.getString("price");
//                                                String nums=goods_info_obj.getString("nums");
//                                                String goods_size=goods_info_obj.getString("goods_size");
//                                                String goods_notes=goods_info_obj.getString("goods_notes");
//                                                Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,nums,cost,price,goods_notes,goods_size,product_id);
//                                                mSelf_Service_GoodsInfolist.add(mSelf_Service_GoodsInfo);
//                                            }
//                                        }
//                                        Res_GoodsOrders mRes_GoodsOrders=new Res_GoodsOrders(order_id,createtime,desk_num,customer_num,mark_text,order_nums,total_amount,dopackage,order_name,table_id,false,mSelf_Service_GoodsInfolist);
//                                        mRes_GoodsOrderslist.add(mRes_GoodsOrders);
//                                    }
//                                    Log.e("barcode", "mRes_GoodsOrderslist：" + mRes_GoodsOrderslist);
//                                    Res_GoodsOrderFragmentAdapter mRes_GoodsOrderFragmentAdapter=new Res_GoodsOrderFragmentAdapter(mContext,mRes_GoodsOrderslist);
//                                    list_goodsorder.setAdapter(mRes_GoodsOrderFragmentAdapter);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            StringUtils.showToast(mContext,"服务器数据异常"+e.toString(),20);
//                            Log.d("barcode", "返回错误数据：" + e.toString());
//
//                        }
//                    }
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        Log.d("barcode", "返回错误数据：" + e.toString());
//                    }
//                });
    }

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            Log.e("print", "type: "+type);
            if(type==1){
                getRes_GoodsOrders();
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }
}
