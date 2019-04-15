package retail.yzx.com.Share_Tools.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.SysUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.Share_Tools.adapter.Share_Orders_FragmentAdapter;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.restaurant_nomal.Fragment.BaseFragment;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * Created by Administrator on 2017/9/23.
 */

public class Share_Orders_Fragment extends BaseFragment {

    @BindView(R.id.list_goodsorder)
    ListView listGoodsorder;
    Unbinder unbinder;

    private ArrayList<Self_Service_GoodsInfo> mShare_Tools_GoodsInfolist;
    private ArrayList<Res_GoodsOrders> mShare_Tools_GoodsOrderslist;
    private Share_Orders_FragmentAdapter mShare_Orders_FragmentAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.layout_res_goodsorderfragment, null);
        unbinder = ButterKnife.bind(this,  view);
        return view;
    }

    @Override
    protected void initData() {
        getRes_GoodsOrders();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void getRes_GoodsOrders(){
        OkGo.post(SysUtils.getShareGoodsServiceUrl("catering_order_info"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求发送：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "共享订单数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray data=jo1.getJSONArray("data");
                                mShare_Tools_GoodsOrderslist=new ArrayList<Res_GoodsOrders>();
                                if(data!=null){
                                    for(int i=0;i<data.length();i++){
                                        JSONObject data_obj=data.getJSONObject(i);
                                        String order_id= JsonUtil.getJsonString(data_obj,"order_id");
                                        String order_name= JsonUtil.getJsonString(data_obj,"order_name");
                                        String createtime= JsonUtil.getJsonString(data_obj,"createtime");
                                        String total_amount= JsonUtil.getJsonString(data_obj,"total_amount");
                                        String mark_text= JsonUtil.getJsonString(data_obj,"mark_text");
                                        String order_nums= JsonUtil.getJsonString(data_obj,"order_nums");
                                        JSONArray goods_info=JsonUtil.getJsonArray(data_obj,"goods_info");
                                        mShare_Tools_GoodsInfolist=new ArrayList<Self_Service_GoodsInfo>();
                                        if(goods_info!=null){
                                            for(int j=0;j<goods_info.length();j++){
                                                JSONObject goods_info_obj=goods_info.getJSONObject(j);
                                                String goods_id=JsonUtil.getJsonString(goods_info_obj,"goods_id");
                                                String name=JsonUtil.getJsonString(goods_info_obj,"name");
                                                String price=JsonUtil.getJsonString(goods_info_obj,"price");
                                                String nums=JsonUtil.getJsonString(goods_info_obj,"nums");
                                                String goods_notes=JsonUtil.getJsonString(goods_info_obj,"goods_notes");
                                                Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,nums,"",price,goods_notes,"","","");
                                                mShare_Tools_GoodsInfolist.add(mSelf_Service_GoodsInfo);
                                            }
                                        }
                                        Res_GoodsOrders mRes_GoodsOrders=new Res_GoodsOrders(order_id,createtime,"","",mark_text,order_nums,total_amount,"",order_name,"",false,mShare_Tools_GoodsInfolist);
                                        mShare_Tools_GoodsOrderslist.add(mRes_GoodsOrders);
                                    }
                                }
                                mShare_Orders_FragmentAdapter=new Share_Orders_FragmentAdapter(getActivity(),mShare_Tools_GoodsOrderslist);
                                listGoodsorder.setAdapter(mShare_Orders_FragmentAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(mContext,"服务器数据异常"+e.toString(),20);
                            Log.d("barcode", "返回错误数据：" + e.toString());

                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d("barcode", "返回错误数据：" + e.toString());
                    }
                });
    }
}
