package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.SysUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.Share_Tools.adapter.Share_Goods_InfoAdapter;
import retail.yzx.com.Share_Tools.adapter.Share_Goods_SortAdapter;
import retail.yzx.com.Share_Tools.adapter.Spinner_sharegoods_sortAdapter;
import retail.yzx.com.Share_Tools.entry.Share_GoodsInfo;
import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.ResDialog;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * Created by Administrator on 2017/9/7.
 */

public class Share_Goods_Fragment extends Fragment  {


    @BindView(R.id.btn_sharegood_sort)
    Button btnSharegoodSort;
    @BindView(R.id.btn_sharegood_edit)
    Button btnSharegoodEdit;
    @BindView(R.id.btn_add_sharegoods)
    Button btnAddSharegoods;
    @BindView(R.id.tv_sharegoods_name)
    TextView tvSharegoodsName;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_sharegoods_sort)
    TextView tvSharegoodsSort;
    @BindView(R.id.btn_cell_sharegoods_sort_edit)
    Button btnCellSharegoodsSortEdit;
    @BindView(R.id.layout_midle_left)
    RelativeLayout layoutMidleLeft;
    @BindView(R.id.tv_sharegoods)
    TextView tvSharegoods;
    @BindView(R.id.recyclerview_sharegoods)
    RecyclerView recyclerviewSharegoods;
    @BindView(R.id.layout_midle_right)
    RelativeLayout layoutMidleRight;
    @BindView(R.id.layout_midle)
    RelativeLayout layoutMidle;
    @BindView(R.id.recyclerview_sharegoods_sort)
    RecyclerView recyclerviewSharegoodsSort;
    Unbinder unbinder;


    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;
    private ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;
    private Share_Goods_SortAdapter mShare_Goods_SortAdapter;
    private Share_Goods_InfoAdapter mShare_Goods_InfoAdapter;
    private int cur_sort_position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.share_goods_fragment_view, null);
        unbinder = ButterKnife.bind(this, view);
        getShareGoodsSortInfo();

        return view;
    }

    @OnClick({R.id.btn_sharegood_sort, R.id.btn_sharegood_edit, R.id.btn_add_sharegoods, R.id.btn_cell_sharegoods_sort_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sharegood_sort:
                ResDialog.AddNotesDialog(getActivity(), "新增商品分类", "请输入新增商品分类", tvSharegoodsName);
                ResDialog r = new ResDialog();
                r.setOnClickListener(new ResDialog.onClickSure() {
                    @Override
                    public void onClickSure() {
                        AddShareGoodsSort(tvSharegoodsName.getText().toString().trim());
                    }
                });
                break;
            case R.id.btn_sharegood_edit:
                if (mShare_Goods_SortAdapter != null) {
                    mShare_Goods_SortAdapter.setEdit(true);
                    mShare_Goods_SortAdapter.notifyDataSetChanged();
                    btnCellSharegoodsSortEdit.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_cell_sharegoods_sort_edit:
                if (mShare_Goods_SortAdapter != null) {
                    mShare_Goods_SortAdapter.setEdit(false);
                    mShare_Goods_SortAdapter.notifyDataSetChanged();
                    btnCellSharegoodsSortEdit.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_add_sharegoods:
                DialogAddShareGoodsInfo(0,0,0);//0表示新增，1表示编辑
                break;
        }
    }
    private void setListrner(){
        mShare_Goods_SortAdapter.setOnClick(new Share_Goods_SortAdapter.setOnClick() {
            @Override
            public void setOnClick(View v, final int position) {
                String table_sort=mShare_Goods_SortInfoList.get(position).getType_name();
                switch (v.getId()){
                    case R.id.btn_edit:
                        tvSharegoodsName.setText(table_sort);
                        ResDialog.AddNotesDialog(getActivity(),"编辑商品分类",table_sort,tvSharegoodsName);
                        ResDialog r_edit=new ResDialog();
                        r_edit.setOnClickListener(new ResDialog.onClickSure() {
                            @Override
                            public void onClickSure() {
                                EditShareGoodsSort(mShare_Goods_SortInfoList.get(position).getType_id(),tvSharegoodsName.getText().toString().trim());

                            }
                        });

                        break;
                    case R.id.btn_remove:
                        ResDialog.SureAndCellDialog(getActivity(),"删除 "+table_sort);
                        ResDialog r=new ResDialog();
                        r.setOnSureClick(new ResDialog.OnSureClick() {
                            @Override
                            public void OnSureClick(View v) {
                                if(mShare_Goods_SortInfoList.get(position).getmShare_GoodsInfoList().size()>0){
                                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"此分类含有餐桌不能删除！",20);
                                }else {
                                    DelShareGoodsSort(mShare_Goods_SortInfoList.get(position).getType_id());

                                }
                            }
                        });
                        break;
                    default:
                        cur_sort_position=position;
                        for(int i=0;i<mShare_Goods_SortInfoList.size();i++){
                            if(mShare_Goods_SortInfoList.get(i).isShare_goods_sort_click()){
                                mShare_Goods_SortInfoList.get(i).setShare_goods_sort_click(false);
                            }
                        }
                        mShare_Goods_SortInfoList.get(position).setShare_goods_sort_click(true);
                        mShare_Goods_SortAdapter.notifyDataSetChanged();

                        mShare_Goods_InfoAdapter=new Share_Goods_InfoAdapter(getActivity(),mShare_Goods_SortInfoList.get(position).getmShare_GoodsInfoList());
                        recyclerviewSharegoods.setAdapter(mShare_Goods_InfoAdapter);
                        recyclerviewSharegoods.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        recyclerviewSharegoods.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

                        break;
                }
            }
        });
        mShare_Goods_InfoAdapter.setOnClick(new Share_Goods_InfoAdapter.setOnClick() {
            @Override
            public void setOnClick(View view, final int position) {
                Log.e("print", "编辑共享商品：");
                String goods_name=mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(position).getName();
                final String goods_id=mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(position).getId();
                switch (view.getId()){
                    case R.id.btn_edit:
                        DialogAddShareGoodsInfo(1,cur_sort_position,position);//0表示新增，1表示编辑
                        break;
                    case R.id.btn_del:
                        ResDialog.SureAndCellDialog(getActivity(),"删除 "+goods_name);
                        ResDialog r=new ResDialog();
                        r.setOnSureClick(new ResDialog.OnSureClick() {
                            @Override
                            public void OnSureClick(View v) {
                                if("true".equals(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(position).getStatus())){
                                    DelShareGoods(goods_id);
                                }else {
                                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"此商品正在使用，不能删除！",20);

                                }
                            }
                        });
                        break;
                }
            }
        });

    }
    private void DelShareGoods(String goods_id){
        OkGo.post(SysUtils.getShareGoodsServiceUrl("remove_goods"))
                .tag(this)
                .params("goods_id", goods_id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "删除共享商品：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                StringUtils.showToast(getActivity(), "删除成功", 20);
                                getShareGoodsSortInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //添加共享商品
    private void AddShareGoods(final String goods_id, String bn, String name, String bncode, String nav_id, String store, String price, String free_time,
                               String cost_time, String cost_money, String image_default_id, String marketable) {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("goodsToAdd"))
                .tag(this)
                .params("goods_id", goods_id)
                .params("bn", bn)
                .params("name", name)
                .params("bncode", bncode)
                .params("nav_id", nav_id)
                .params("store", store)
                .params("price", price)
                .params("free_time", free_time)
                .params("cost_time", cost_time)
                .params("cost_money",cost_money)
                .params("image_default_id",image_default_id)
                .params("marketable",marketable)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "新增共享商品URL：" + request.getUrl());
                        Log.e("print", "新增共享商品Params：" + request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "新增共享商品：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                if(TextUtils.isEmpty(goods_id)){
                                    StringUtils.showToast(getActivity(), "商品添加成功", 20);
                                }else {
                                    StringUtils.showToast(getActivity(), "商品编辑成功", 20);
                                }

                                getShareGoodsSortInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //添加共享商品分类
    private void AddShareGoodsSort(String str) {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cat_add"))
                .tag(this)
                .params("nav", str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "新增共享商品：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                StringUtils.showToast(getActivity(), "分类添加成功", 20);
                                getShareGoodsSortInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取共享商品分类信息
    private void getShareGoodsSortInfo() {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .params("market","3")//值1 上架的 值2 下架的，值3 全部
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "获取共享商品信息url：" + request.getUrl());
                        Log.e("print", "获取共享商品信息Params：" + request.getParams());
                    }

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
                                mShare_Goods_SortAdapter = new Share_Goods_SortAdapter(getActivity(), mShare_Goods_SortInfoList);
                                recyclerviewSharegoodsSort.setAdapter(mShare_Goods_SortAdapter);
                                recyclerviewSharegoodsSort.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                mShare_Goods_SortInfoList.get(cur_sort_position).setShare_goods_sort_click(true);//默认第一个选中

                                mShare_Goods_InfoAdapter=new Share_Goods_InfoAdapter(getActivity(),mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList());
                                recyclerviewSharegoods.setAdapter(mShare_Goods_InfoAdapter);
                                recyclerviewSharegoods.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                                recyclerviewSharegoods.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

                                setListrner();
                            }else {
                                String message=jo1.getString("message");
                                if("没有数据".equals(message)){
                                    if(mShare_Goods_SortAdapter!=null){
                                        mShare_Goods_SortInfoList.clear();
                                        mShare_Goods_SortAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //删除共享商品分类
    private void DelShareGoodsSort(String str) {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cat_remove"))
                .tag(this)
                .params("tag_id", str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "删除共享商品分类：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                StringUtils.showToast(getActivity(), "删除成功", 20);
                                btnCellSharegoodsSortEdit.setVisibility(View.GONE);
                                    getShareGoodsSortInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //编辑共享商品分类
    private void  EditShareGoodsSort(String id,String str) {
        OkGo.post(SysUtils.getShareGoodsServiceUrl("cat_edit"))
                .tag(this)
                .params("tag_id", id)
                .params("nav",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "编辑共享商品分类：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                StringUtils.showToast(getActivity(), "编辑成功", 20);
                                btnCellSharegoodsSortEdit.setVisibility(View.GONE);
                                getShareGoodsSortInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private View view_DialogAddShareGoodsInfo;
    private AlertDialog mDialogAddShareGoodsInfo;
    private TextView tv_title;
    private EditText et_sharegoods_name;
    private EditText et_sharegoods_bn;
    private Spinner Spinner_sharegoods_sort;
    private EditText et_sharegoods_store;
    private EditText et_sharegoods_price;
    private widget.Switch switch_share_goods;
    private EditText et_sharegoods_freetime;
    private EditText et_sharegoods_cost_time;
    private EditText et_sharegoods_cost_price;
    private Button btn_cell;
    private Button btn_sure;
    private Spinner_sharegoods_sortAdapter mSpinner_sharegoods_sortAdapter;
    private String select_sort_id="";
    private String select_sort_name="";
    private  InputMethodManager imm;
    private boolean Marketable=true;//共享商品是否开启
    private void DialogAddShareGoodsInfo(final int type, final int cur_sort_position, final int goods_position){//0表示新增，1表示编辑
        view_DialogAddShareGoodsInfo=View.inflate(getActivity(),R.layout.dialog_add_share_good_info,null);
        tv_title= (TextView) view_DialogAddShareGoodsInfo.findViewById(R.id.tv_title);
        et_sharegoods_name= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_name);
        et_sharegoods_bn= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_bn);
        Spinner_sharegoods_sort= (Spinner) view_DialogAddShareGoodsInfo.findViewById(R.id.Spinner_sharegoods_sort);
        et_sharegoods_store= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_store);
        et_sharegoods_price= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_price);
        switch_share_goods= (widget.Switch) view_DialogAddShareGoodsInfo.findViewById(R.id.switch_share_goods);
        et_sharegoods_freetime= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_freetime);
        et_sharegoods_cost_time= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_cost_time);
        et_sharegoods_cost_price= (EditText) view_DialogAddShareGoodsInfo.findViewById(R.id.et_sharegoods_cost_price);
        btn_cell= (Button) view_DialogAddShareGoodsInfo.findViewById(R.id.btn_cell);
        btn_sure= (Button) view_DialogAddShareGoodsInfo.findViewById(R.id.btn_sure);

        if(type==1){
            tv_title.setText("编辑商品");
            et_sharegoods_name.setText(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getName());
            et_sharegoods_name.setSelection(et_sharegoods_name.getText().toString().trim().length());
            et_sharegoods_bn.setText(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getBn());
            et_sharegoods_store.setText(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getStore());
            et_sharegoods_price.setText(Utils.StringUtils.stringpointtwo(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getPrice()));
            et_sharegoods_cost_price.setText(Utils.StringUtils.stringpointtwo(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getCost_money()));
            et_sharegoods_cost_time.setText(Utils.StringUtils.stringpointtwo(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getCost_time()));
            et_sharegoods_freetime.setText(Utils.StringUtils.stringpointtwo(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getFree_time()));
            if("true".equals(mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getMarketable())){
                Marketable=true;
            }else {
                Marketable=false;
            }
            switch_share_goods.setChecked(Marketable);

        }

        mSpinner_sharegoods_sortAdapter=new Spinner_sharegoods_sortAdapter(getActivity(),mShare_Goods_SortInfoList);
        Spinner_sharegoods_sort.setDropDownWidth(300);
        Spinner_sharegoods_sort.setAdapter(mSpinner_sharegoods_sortAdapter);
        Spinner_sharegoods_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_sort_id=mShare_Goods_SortInfoList.get(position).getType_id();
                select_sort_name=mShare_Goods_SortInfoList.get(position).getType_name();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(type==1){
            Spinner_sharegoods_sort.setSelection(cur_sort_position);
        }
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharegoods_name=et_sharegoods_name.getText().toString().trim();
                String sharegoods_bn=et_sharegoods_bn.getText().toString().trim();
                String sharegoods_store=et_sharegoods_store.getText().toString().trim();
                String sharegoods_price=et_sharegoods_price.getText().toString().trim();
                String sharegoods_freetime=et_sharegoods_freetime.getText().toString().trim();
                String sharegoods_cost_time=et_sharegoods_cost_time.getText().toString().trim();
                String sharegoods_cost_price=et_sharegoods_cost_price.getText().toString().trim();
                boolean  is_switch=switch_share_goods.isChecked();
                if(TextUtils.isEmpty(sharegoods_name)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品名不能为空！",20);
                    return;
                } if(TextUtils.isEmpty(sharegoods_bn)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品编码不能为空！",20);
                    return;
                } if(TextUtils.isEmpty(sharegoods_store)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品库存不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(sharegoods_price)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品价格不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(sharegoods_freetime)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品免租时间不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(sharegoods_cost_time)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品收费时间不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(sharegoods_cost_price)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品租金不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(select_sort_name)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"商品分类不能为空！",20);
                    return;
                }
                if (type == 0) {
                    AddShareGoods("",sharegoods_bn,sharegoods_name,"",select_sort_id,sharegoods_store,sharegoods_price,
                            sharegoods_freetime,sharegoods_cost_time,sharegoods_cost_price,"",is_switch+"");
                }else {
                    String goods_id=mShare_Goods_SortInfoList.get(cur_sort_position).getmShare_GoodsInfoList().get(goods_position).getId();
                    AddShareGoods(goods_id,sharegoods_bn,sharegoods_name,"",select_sort_id,sharegoods_store,sharegoods_price,
                            sharegoods_freetime,sharegoods_cost_time,sharegoods_cost_price,"",is_switch+"");
                }

                mDialogAddShareGoodsInfo.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAddShareGoodsInfo.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),R.style.AlertDialog);
        mDialogAddShareGoodsInfo=dialog.setView(view_DialogAddShareGoodsInfo).show();
        mDialogAddShareGoodsInfo.setCancelable(false);
        mDialogAddShareGoodsInfo.show();


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
