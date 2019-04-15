package retail.yzx.com.restaurant_nomal.Fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.SysUtils;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntGoodsAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntHideGoodsPictureAdapter;
import retail.yzx.com.restaurant_self_service.Adapter.Self_Service_RestanrauntSortsAdapter;
import retail.yzx.com.restaurant_self_service.Entty.GoodsNotes;
import retail.yzx.com.restaurant_self_service.Entty.GoodsSortInfos;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandard;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandardInfos;
import retail.yzx.com.restaurant_self_service.Entty.Self_Service_RestanrauntGoodsInfo;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * Created by Administrator on 2017/7/31.
 */

public class Res_GoodsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private TextView tv_sort;
    private ListView list_goods,list_sort;
    private Dialog progressDialog = null;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList_Search;//商品搜索集合
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Search_Restanraunt_GoodsInfoList;
    private ArrayList<GoodsStandardInfos> mGoodsStandardInfosList;//商品规格信息
    private ArrayList<GoodsStandardInfos>  mGoodsStandardInfosList_Search;//商品规格信息
    private ArrayList<GoodsStandard> mGoodsStandardList;//具体的规格
    private ArrayList<GoodsNotes> mGoodsNotesLists;//商品备注信息
    private ArrayList<GoodsNotes> mGoodsNotesLists_Search;//商品备注信息
    private Self_Service_RestanrauntSortsAdapter mSelf_Service_RestanrauntSortsAdapter;
    private Self_Service_RestanrauntGoodsAdapter mSelf_Service_RestanrauntGoodsAdapter;
    private Self_Service_RestanrauntHideGoodsPictureAdapter mSelf_Service_RestanrauntHideGoodsPictureAdapter;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;
    private int showType=2;
    private int type;


    @Override
    protected View initView() {
        View view=View.inflate(mContext, R.layout.layout_res_goods_view,null);
        initview(view);
        mSelf_Service_GoodsInfo=new ArrayList<>();
        mSelf_Service_RestanrauntHideGoodsPictureAdapter=new Self_Service_RestanrauntHideGoodsPictureAdapter(mContext);
        return view;
    }
//初始化视图是222222
    private void initview(View view) {
        tv_sort= (TextView) view.findViewById(R.id.tv_sort);
        list_goods= (ListView) view.findViewById(R.id.list_goods);
        list_sort= (ListView) view.findViewById(R.id.list_sort);

        list_sort.setOnItemClickListener(this);

        Bundle mBundle=getArguments();
        if(mBundle!=null){
            type=mBundle.getInt("type",0);
            if(type==1){
                GetGoods_info();
            }
        }

    }
    @Override
    protected void initData() {
        GetGoods_info();
        Log.e("barcode", " mGoodsSortInfosList：" +  mGoodsSortInfosList);

    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            if(type==1){
//                if(mSelf_Service_GoodsInfo!=null){
//                    mSelf_Service_GoodsInfo.clear();
//                }
//                mSelf_Service_GoodsInfo=intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfo");
            }
            else if (type==2){
                mSelf_Service_GoodsInfo.clear();
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        mContext.registerReceiver(broadcastReceiver,new IntentFilter("Res_GoodsFragment.Action"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        showType=SharedUtil.getInt("showType");
        mSelf_Service_RestanrauntSortsAdapter.setDefSelect(position);

//        if(showType==-1){
//            showType=2;
//        }
        if(showType==2){
//            mSelf_Service_RestanrauntHideGoodsPictureAdapter=new Self_Service_RestanrauntHideGoodsPictureAdapter(mContext,mGoodsSortInfosList.get(position).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
            mSelf_Service_RestanrauntHideGoodsPictureAdapter.setAdatas(mGoodsSortInfosList.get(position).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
//            list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureAdapter);
            mSelf_Service_RestanrauntHideGoodsPictureAdapter.notifyDataSetChanged();
        }
        tv_sort.setText(mGoodsSortInfosList.get(position).getSort_name());
    }
    //获取商品信息
    String spec_name;
    public void GetGoods_info() {

        OkGo.post(SysUtils.getGoodsServiceUrl("goods_info"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "Params：" + request.getUrl());
                        if(progressDialog!=null){
                            progressDialog=null;
                        }
                        progressDialog = StringUtils.createLoadingDialog(mContext, "请稍等...", true);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "goods_info" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONObject data=jo1.getJSONObject("data");
                                JSONArray catagory_infos=data.getJSONArray("catagory_infos");
                                mGoodsSortInfosList=new ArrayList<>();
                                if(catagory_infos!=null&&catagory_infos.length()>0){
                                    for(int i=0;i<catagory_infos.length();i++){
                                        JSONObject catagory_infos_object=catagory_infos.getJSONObject(i);
                                        String tag_id=catagory_infos_object.getString("tag_id");
                                        String tag_name=catagory_infos_object.getString("tag_name");
                                        JSONArray pro_info=catagory_infos_object.getJSONArray("pro_info");
                                        mSelf_Service_Restanraunt_GoodsInfoList =new ArrayList<>();
                                        if(pro_info!=null&&pro_info.length()>0){
                                            for(int j=0;j<pro_info.length();j++){
                                                JSONObject pro_info_object=pro_info.getJSONObject(j);
                                                String goods_id=pro_info_object.getString("goods_id");//id
                                                String name=pro_info_object.getString("name");//名字
                                                String price=pro_info_object.getString("price");//显示的价格
                                                String image_default_id=pro_info_object.getString("image_default_id");//商品图片
                                                JSONArray spec_desc=null;
                                                try{
                                                    spec_desc=pro_info_object.getJSONArray("spec_desc");
                                                }catch (Exception e){
                                                    Log.e("barcode", " spec_desc：" + e.toString());
                                                }
                                                mGoodsStandardInfosList = new ArrayList<>();
                                                if(spec_desc!=null&&spec_desc.length()>0){
                                                    for(int x=0;x<spec_desc.length();x++) {
                                                        JSONObject spec_desc_object = spec_desc.getJSONObject(x);
                                                        spec_name = spec_desc_object.getString("spec_name");//规格型号
                                                        JSONArray spec_info = null;
                                                        try {
                                                            spec_info = spec_desc_object.getJSONArray("spec_info");
                                                        } catch (Exception e) {
                                                            Log.e("barcode", " spec_info：" + e.toString());
                                                        }
                                                        mGoodsStandardList = new ArrayList<GoodsStandard>();
                                                        if (spec_info != null && spec_info.length() > 0) {
                                                            for (int a = 0; a < spec_info.length(); a++) {
                                                                JSONObject spec_info_object = spec_info.getJSONObject(a);
                                                                String spec_value = spec_info_object.getString("spec_value");//规格
                                                                GoodsStandard goodsStandard = new GoodsStandard(spec_value,false);
                                                                mGoodsStandardList.add(goodsStandard);
                                                            }
                                                        }
                                                        GoodsStandardInfos goodsStandardInfos=new GoodsStandardInfos(spec_name,mGoodsStandardList);
                                                        mGoodsStandardInfosList.add(goodsStandardInfos);
                                                    }
                                                }
                                                JSONArray spec_infos=null;
                                                try {
                                                    spec_infos=pro_info_object.getJSONArray("spec_infos");
                                                }catch (Exception e){
                                                    Log.e("barcode", " spec_infos：" + e.toString());
                                                }

                                                mGoodsNotesLists=new ArrayList<GoodsNotes>();
                                                for(int a=0;a<spec_infos.length();a++){
                                                    JSONObject spec_infos_object=spec_infos.getJSONObject(a);
                                                    String price_standard=spec_infos_object.getString("price");//不同规格商品的价格
                                                    String spec_info_standard=spec_infos_object.getString("spec_info");//含有的规格信息
                                                    String is_default=spec_infos_object.getString("is_default");//是否默认的规格
                                                    String product_id=spec_infos_object.getString("product_id");//产品id
                                                    GoodsNotes goodsNotes=new GoodsNotes(price_standard,spec_info_standard,is_default,product_id);
                                                    mGoodsNotesLists.add(goodsNotes);
                                                }
                                                Self_Service_RestanrauntGoodsInfo self_service_restanrauntGoodsInfo=new Self_Service_RestanrauntGoodsInfo(goods_id,name,price,image_default_id,"0",mGoodsStandardInfosList,mGoodsNotesLists,tag_id);
                                                mSelf_Service_Restanraunt_GoodsInfoList.add(self_service_restanrauntGoodsInfo);
                                            }
                                        }
                                        GoodsSortInfos goodsSortInfos=new GoodsSortInfos(tag_id,tag_name,mSelf_Service_Restanraunt_GoodsInfoList);
                                        mGoodsSortInfosList.add(goodsSortInfos);
                                    }

                                }
                                mSelf_Service_RestanrauntSortsAdapter=new Self_Service_RestanrauntSortsAdapter(mContext,mGoodsSortInfosList);
                                list_sort.setAdapter(mSelf_Service_RestanrauntSortsAdapter);

                                if(mGoodsSortInfosList.size()>0){
                                    if(showType==1){
                                        mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
                                        mSelf_Service_RestanrauntGoodsAdapter=new Self_Service_RestanrauntGoodsAdapter(mContext,mGoodsSortInfosList.get(0).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
                                        list_goods.setAdapter(mSelf_Service_RestanrauntGoodsAdapter);

                                        mSelf_Service_RestanrauntGoodsAdapter.notifyDataSetChanged();
//                                        mSelf_Service_RestanrauntGoodsLinkAdapter=new Self_Service_RestanrauntGoodsLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
//                                        list_goods.setAdapter( mSelf_Service_RestanrauntGoodsLinkAdapter);
                                        tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                                    }else if(showType==2){
                                        mSelf_Service_RestanrauntSortsAdapter.setDefSelect(0);
//                                        mSelf_Service_RestanrauntHideGoodsPictureAdapter=new Self_Service_RestanrauntHideGoodsPictureAdapter(mContext,mGoodsSortInfosList.get(0).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
                                        mSelf_Service_RestanrauntHideGoodsPictureAdapter.setAdatas(mGoodsSortInfosList.get(0).getmSelf_Service_Restanraunt_GoodsInfoList(),mSelf_Service_GoodsInfo);
                                        list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureAdapter);
                                        mSelf_Service_RestanrauntHideGoodsPictureAdapter.notifyDataSetChanged();

//                                        mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter=new Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Self_Service_RestanrauntActivity.this,mGoodsSortInfosList,mSelf_Service_GoodsInfo);
//                                        list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureLinkAdapter);
                                        tv_sort.setText(mGoodsSortInfosList.get(0).getSort_name());
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext,"服务器数据异常！",Toast.LENGTH_SHORT).show();
                            Log.e("barcode", " spec_infos：" + e.toString());
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                });
    }

    /**
     * 商品搜索
     * @param et_search_src
     */
    public void GoodsSearch(String et_search_src){
        if( mGoodsSortInfosList_Search!=null){
            mGoodsSortInfosList_Search.clear();
        }
        mGoodsSortInfosList_Search=new ArrayList<>();
        mSelf_Service_Search_Restanraunt_GoodsInfoList=new ArrayList<>();
        for(int i=0;i<mGoodsSortInfosList.size();i++){
            for(int j=0;j<mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().size();j++){
                if(mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getName().indexOf(et_search_src)!=-1){
                    String goods_id=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getGoods_id();
                    String name=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getName();
                    String price=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getPrice();
                    String image_default_id=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getImage_default_id();
                    String tag_id=mGoodsSortInfosList.get(i).getSort_id();
                    mGoodsStandardInfosList_Search=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getmGoodsStandardInfosList();
                    mGoodsNotesLists_Search=mGoodsSortInfosList.get(i).getmSelf_Service_Restanraunt_GoodsInfoList().get(j).getmGoodsNotesLists();
                    Self_Service_RestanrauntGoodsInfo self_service_restanrauntGoodsInfo=new Self_Service_RestanrauntGoodsInfo(goods_id,name,price,image_default_id,"0",mGoodsStandardInfosList_Search,mGoodsNotesLists_Search,tag_id);
                    mSelf_Service_Search_Restanraunt_GoodsInfoList.add(self_service_restanrauntGoodsInfo);
                }
            }
        }
        if(mSelf_Service_Search_Restanraunt_GoodsInfoList.size()>0){
//            mSelf_Service_RestanrauntHideGoodsPictureAdapter=new Self_Service_RestanrauntHideGoodsPictureAdapter(mContext,mSelf_Service_Search_Restanraunt_GoodsInfoList,mSelf_Service_GoodsInfo);
            mSelf_Service_RestanrauntHideGoodsPictureAdapter.setAdatas(mSelf_Service_Search_Restanraunt_GoodsInfoList,mSelf_Service_GoodsInfo);
            list_goods.setAdapter(mSelf_Service_RestanrauntHideGoodsPictureAdapter);
        }else {
            StringUtils.showToast(mContext,"此商品不存在，请重新输入或咨询工作人员！",25);
        }
    }
}
