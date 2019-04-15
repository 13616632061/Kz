package retail.yzx.com.Share_Tools.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.SysUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.Share_Tools.SectionedRecyclerViewAdapter.SectionedRecyclerViewAdapter;
import retail.yzx.com.Share_Tools.adapter.Share_Goods_SortAdapter;
import retail.yzx.com.Share_Tools.adapter.Share_Tools_Goods_InfoAdapter;
import retail.yzx.com.Share_Tools.adapter.Share_Tools_Goods_SortAdapter;
import retail.yzx.com.Share_Tools.entry.Share_GoodsInfo;
import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Fragment.BaseFragment;

/**
 * Created by Administrator on 2017/9/9.
 */

public class Share_Goods_Fragment extends BaseFragment {
    @BindView(R.id.recyclerview_sharegoods)
    RecyclerView recyclerviewSharegoods;
    @BindView(R.id.recyclerview_sharegoods_sort)
    RecyclerView recyclerviewSharegoodsSort;
    Unbinder unbinder;

    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;
    private ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;
    private Share_Tools_Goods_SortAdapter mShare_Tools_Goods_SortAdapter;
    private Share_Tools_Goods_InfoAdapter mShare_Tools_Goods_InfoAdapter;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    private  GridLayoutManager gridLayoutManager;
    private boolean move = false;
    private int mIndex = 0;
    private int cur_sort_position=0;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.view_share_goods_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        mSectionedRecyclerViewAdapter=new SectionedRecyclerViewAdapter();
        return view;
    }

    @Override
    protected void initData() {
        getShareGoodsSortInfo();

    }
    private void smoothMoveToPosition(int n) {
        int firstItem = gridLayoutManager.findFirstVisibleItemPosition();
        int lastItem =gridLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerviewSharegoods.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerviewSharegoods.getChildAt(n - firstItem).getTop();
            recyclerviewSharegoods.scrollBy(0, top);
        } else {
            recyclerviewSharegoods.scrollToPosition(n);
            move = true;
        }
    }
   private void setListener(){
       mShare_Tools_Goods_SortAdapter.setOnClick(new Share_Goods_SortAdapter.setOnClick() {
           @Override
           public void setOnClick(View v, int position) {
               cur_sort_position=position;
               int N=0;
               for(int i=0;i<mShare_Goods_SortInfoList.size();i++){
                   mShare_Goods_SortInfoList.get(i).setShare_goods_sort_click(false);
               }
               for(int i=0;i<cur_sort_position;i++){
                   N+=mShare_Goods_SortInfoList.get(i).getmShare_GoodsInfoList().size();
               }
               N+=position;
               mShare_Goods_SortInfoList.get(cur_sort_position).setShare_goods_sort_click(true);
               mShare_Tools_Goods_SortAdapter.notifyDataSetChanged();
               smoothMoveToPosition(N);
           }
       });
//       mShare_Tools_Goods_InfoAdapter.setOnItemClick(new Share_Tools_Goods_InfoAdapter.setOnItemClick() {
//           @Override
//           public void setOnItemClick(View view, int position) {
//               getActivity().sendBroadcast(new Intent("Share_Tools_Activity_Action").putExtra("type",1).putExtra("Share_GoodsInfo",mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(position)));
//           }
//       });
       recyclerviewSharegoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);
           }

           @Override
           public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               if( mShare_Goods_SortInfoList!=null) {
                   int firstItem = gridLayoutManager.findFirstVisibleItemPosition();
                   int N=0;
                   for (int i = 0; i < mShare_Goods_SortInfoList.size(); i++) {
                           if (N<=firstItem - i) {
                               N+=mShare_Goods_SortInfoList.get(i).getmShare_GoodsInfoList().size();
                               recyclerviewSharegoodsSort.smoothScrollToPosition(i);
                               for (int j = 0; j < mShare_Goods_SortInfoList.size(); j++) {
                                   mShare_Goods_SortInfoList.get(j).setShare_goods_sort_click(false);
                               }
                               mShare_Goods_SortInfoList.get(i).setShare_goods_sort_click(true);
                               mShare_Tools_Goods_SortAdapter.notifyDataSetChanged();
                           } else {
                               recyclerviewSharegoodsSort.smoothScrollToPosition(0);
                           }
                   }
               }
           }
       });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    //获取共享商品分类信息
    public void getShareGoodsSortInfo() {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .params("market","1")//值1 上架的 值2 下架的，值3 全部
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "获取共享商品信息：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray data = JsonUtil.getJsonArray(jo1, "data");
                                mShare_Goods_SortInfoList = new ArrayList<Share_Goods_Sort>();
                                if (data != null) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject data_obj = data.getJSONObject(i);
                                        String type_id = JsonUtil.getJsonString(data_obj, "type_id");
                                        String type_name = JsonUtil.getJsonString(data_obj, "type_name");
                                        JSONArray info = JsonUtil.getJsonArray(data_obj, "info");
                                        mShare_GoodsInfoList = new ArrayList<Share_GoodsInfo>();
                                        if (info != null) {
                                            for (int j = 0; j < info.length(); j++) {
                                                JSONObject info_obj = info.getJSONObject(j);
                                                String id = JsonUtil.getJsonString(info_obj, "goods_id");
                                                String name = JsonUtil.getJsonString(info_obj, "name");
                                                String price = JsonUtil.getJsonString(info_obj, "price");
                                                String store = JsonUtil.getJsonString(info_obj, "store");
                                                String bn = JsonUtil.getJsonString(info_obj, "bn");
                                                String image_default_url = JsonUtil.getJsonString(info_obj, "image_default_id");
                                                String share_status = JsonUtil.getJsonString(info_obj, "marketable");
                                                String cost_money= JsonUtil.getJsonString(info_obj, "cost_money");
                                                String free_time= JsonUtil.getJsonString(info_obj, "free_time");
                                                String cost_time= JsonUtil.getJsonString(info_obj, "cost_time");
                                                if (!TextUtils.isEmpty(id) && !"null".equals(id)) {
                                                    Share_GoodsInfo share_goodsInfo = new Share_GoodsInfo(id, name, price, store, bn, image_default_url,share_status,share_status,cost_money,free_time,cost_time);
                                                    mShare_GoodsInfoList.add(share_goodsInfo);
                                                }
                                            }
                                        }

                                        Share_Goods_Sort share_goods_sort = new Share_Goods_Sort(type_id, type_name, false, mShare_GoodsInfoList);
                                        mShare_Goods_SortInfoList.add(share_goods_sort);
                                    }
                                }
                                //分类
                                mShare_Goods_SortInfoList.get(cur_sort_position).setShare_goods_sort_click(true);
                                mShare_Tools_Goods_SortAdapter=new Share_Tools_Goods_SortAdapter(getActivity(),mShare_Goods_SortInfoList);
                                recyclerviewSharegoodsSort.setAdapter(mShare_Tools_Goods_SortAdapter);
                                recyclerviewSharegoodsSort.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                //分类商品
                                for(int i=0;i<mShare_Goods_SortInfoList.size();i++){
                                    mShare_Tools_Goods_InfoAdapter=new Share_Tools_Goods_InfoAdapter(getActivity(),
                                            mShare_Goods_SortInfoList.get(i).getType_name(),mShare_Goods_SortInfoList.get(i).getmShare_GoodsInfoList());
                                    mSectionedRecyclerViewAdapter.addSection(mShare_Tools_Goods_InfoAdapter);
                                }
                                recyclerviewSharegoods.setAdapter(mSectionedRecyclerViewAdapter);
                                 gridLayoutManager=new GridLayoutManager(getActivity(),4);
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        switch(mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                                            case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                                                return 4;
                                            default:
                                                return 1;
                                        }
                                    }
                                });
                                recyclerviewSharegoods.setLayoutManager(gridLayoutManager);

                                setListener();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
