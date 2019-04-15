package Fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.feezu.liuli.timeselector.TimeSelector;
import org.feezu.liuli.timeselector.view.PickerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Day_xuanzhong;
import Entty.Fenlei_Entty;
import Entty.GridView_xuangzhong;
import Entty.Label_entty;
import Utils.ExcelUtils;
import Utils.LogUtils;
import Utils.Paging_Utils;
import Utils.SharedUtil;
import Utils.SortUtils;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Comm_adapter;
import adapters.Gridview_adapter;
import adapters.Sp2adapter;
import adapters.Sp3adapter;
import adapters.Spadapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.Addgoodgs_Activity;
import retail.yzx.com.kz.Goods_Common_Notes_Activity;
import retail.yzx.com.kz.Label_activity;
import retail.yzx.com.kz.Provider_activity;
import retail.yzx.com.kz.R;
import retail.yzx.com.kz.Unit_activity;
import shujudb.SqliteHelper;
import widget.MyFileManager;
import widget.MyGirdView;
import widget.ShapeLoadingDialog;

/**
 * Created by admin on 2017/4/1.
 */
public class Buyoff_Fragment extends Fragment implements View.OnClickListener, Comm_adapter.Oneidtextonclick {
    public static List<Map<String, String>> mapList = new ArrayList<>();
    public long insert;
    public TextView tv_filtrate;
    public PopupWindow popupWindow;
    public EditText tv_timestart, tv_timeend;
    public boolean isShowing = false;
    //    时间选择器
    public PickerView month_pv;
    public TimeSelector timeSelector;
    //    分类
    public MyGirdView gv, gv_2, gv_3;
    public Gridview_adapter gv_adapter;
    public List<GridView_xuangzhong> strings;
    public Button but_xiansi;
    public boolean iszhankai = false;
    //    时间选择
    public Button but_day, but_triduum, but_week, but_month, but_lastmonth;
    //    时间选中的判断
    public List<Day_xuanzhong> Day_list;
    public Button but_unit, but_next, but_last;
    public View view;
    public SqliteHelper sqliteHelper;
    public SQLiteDatabase database;
    //    商品的导入导出
    public Button but_derive, but_tolead;
    //供应商
    public Button but_provider;
    //排序
    public TextView tv_repertory, tv_price, tv_selling;
    public boolean sort = false, sort2 = false, sort3 = false;
    public boolean isRefreshing=false;
    public int last=0,first=0;
    public TwinklingRefreshLayout refreshLayout;
    public ListView lv_comm;
    public Comm_adapter adapter;
    //listView的动画
    public LayoutAnimationController layoutAnimationController;
    public ArrayList<Commodity> commodities = new ArrayList<>();
    public String url = "http://www.czxshop.net/rpc/service/?method=ks.goods.goods_total&vsn=1.0&format=json";
    public Button but_label;
    //    public Spinner sp1, sp2, sp3;
    public List<Fenlei_Entty> listFenlei;
    //标签的实体类
    public List<Label_entty> listlabel;
    public Label_entty label_entty;
    //判断sp3的选中
    public String str = "";
    //判断sp1的选中
    public int Sp1str = 0;
    public int SP2str = 0;
    public int intsp = 0;
    public String units;
    public String labels;
    //    Spinner的适配器
    public Spadapter spadapter;
    public Sp2adapter sp2adapter;
    //新增商品
    public Button but_ItemAdd;
    public int page = 1;
    public int total;
    public int k = 1;
    public boolean paging1 = false, paging2 = false;
    public EditText tv_seek;
    public TextView tv_page;
    //    分页的处理
    public Paging_Utils<Commodity> paging_utils;
    public widget.ShapeLoadingDialog loadingdialog;
    //总库存和显示的数量
    public TextView tv_shuliang, tv_zong, tv_jinjia, tv_zongshoujia;
    public int zong = 0, jinjia = 0, zongshoujia;
    public List<Commodity> aaa;
    List<Commodity> commodities1 = new ArrayList<Commodity>();
    //设置导入导出
    private String[] title = {"序号",  "商品名(必填)", "拼音码", "价格(必填)", "进价(必填)", "条形码(必填)", "库存(必填)", "库存上限", "库存下限", "生产日期", "到期日期", "上下架", "分类名", "单位","规格"};
    private String[] saveData;
    private ArrayList<ArrayList<String>> bill2List;
    private PopupWindow popLeft;
    private PopupWindow popRight;
    private PopupWindow popMiddle;
    private View layout_sp1;
    private ListView lv_pop;
    private TextView tv_sp1, tv_sp2, tv_sp3;
    private String sp1str = "", sp2str = "", sp3str = "";
    private List<String> adats = new ArrayList<>();
    private int selectIndex = 0;
    private Button  btn_common_notes;//常用备注



    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    @Override
    public void onStart() {
        super.onStart();
        page=1;
        Loadadats(page);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.buyoff_fragment, null);
//        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.filtrate, null);
        init();

//        gv = (MyGirdView) view1.findViewById(R.id.gv_1);
//        gv_2 = (MyGirdView) view1.findViewById(R.id.gv_2);
//        gv_3 = (MyGirdView) view1.findViewById(R.id.gv_3);

//        gv_adapter = new Gridview_adapter(getContext());

//        gv_adapter.notifyDataSetChanged(false, strings);
//        gv.setAdapter(gv_adapter);

        //常用备注
        btn_common_notes= (Button) view.findViewById(R.id.btn_common_notes);
        btn_common_notes.setOnClickListener(this);
//        时间的监听
//        but_day = (Button) view1.findViewById(R.id.but_day);
//        but_day.setOnClickListener(this);
//        but_triduum = (Button) view1.findViewById(R.id.but_triduum);
//        but_triduum.setOnClickListener(this);
//        but_week = (Button) view1.findViewById(R.id.but_week);
//        but_week.setOnClickListener(this);
//        but_month = (Button) view1.findViewById(R.id.but_month);
//        but_month.setOnClickListener(this);
//        but_lastmonth = (Button) view1.findViewById(R.id.but_lastmonth);
//        but_lastmonth.setOnClickListener(this);


//        分类的展开的监听
//        but_xiansi = (Button) view1.findViewById(R.id.but_xiansi);
//        but_xiansi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!iszhankai) {
//                    gv_adapter.notifyDataSetChanged(true, strings);
//                    iszhankai = true;
//                } else {
//                    gv_adapter.notifyDataSetChanged(false, strings);
//                    iszhankai = false;
//                }
//
//            }
//        });

//        popupWindow = new PopupWindow(view1, 1000, 800);
//        开始时间
//        tv_timestart = (EditText) view1.findViewById(R.id.tv_timestart);
//        tv_timestart.setOnClickListener(this);

//        tv_timeend = (EditText) view1.findViewById(R.id.tv_timeend);
//        tv_timeend.setOnClickListener(this);

//        month_pv = (PickerView) view1.findViewById(R.id.month_pv);

        //搜索的监听
        tv_seek = (EditText) view.findViewById(R.id.tv_seek);
        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    tv_seek.getText().toString();
                    getseek(tv_seek.getText().toString());
                }
                return true;
            }
        });


        //筛选
        tv_filtrate = (TextView) view.findViewById(R.id.tv_filtrate);
        tv_filtrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                    isShowing = true;
//                } else {
//                    popupWindow.showAsDropDown(tv_filtrate, 1000, 0);
//
//                }
            }
        });

        Loadadats(page);

        return view;
    }

    private void getseek(String str) {
        String name="search";
        if (StringUtils.isNumber1(str)){
            name="bncode";
        }else {
            name="search";
        }
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params(name,str)
                .params("seller_token",SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("print搜索的结果",s);
                        commodities.clear();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            String message=jsonObject1.getString("message");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                JSONArray ja2=jo1.getJSONArray("total");
                                for (int j = 0; j < ja1.length(); j++) {
                                    Commodity commodity = new Commodity();
                                    JSONObject jo2 = ja1.getJSONObject(j);
                                    commodity.setAuth(jo2.getString("auth"));
                                    commodity.setCustom_member_price(jo2.getString("custom_member_price"));
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setIs_special_offer(jo2.getString("is_special"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setTag_id(jo2.getString("tag_id"));
                                    commodity.setTag_name(jo2.getString("tag_name"));
                                    commodity.setUnit(jo2.getString("unit"));
                                    commodity.setUnit_id(jo2.getInt("unit_id"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setProvider_name(jo2.getString("provider_name"));
                                    commodity.setProvider_id(jo2.getString("provider_id"));
                                    commodity.setSpecification(jo2.getString("specification"));
                                    commodity.setGood_limit(jo2.getString("good_limit"));
                                    commodity.setGood_stock(jo2.getString("good_stock"));
                                    commodity.setPD(jo2.getString("PD"));
                                    commodity.setGD(jo2.getString("GD"));
                                    commodity.setMarketable(jo2.getString("marketable"));
                                    commodity.setAltc(jo2.getString("ALTC"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    commodity.setBox_disable(jo2.getBoolean("box_disable"));
                                    commodity.setDiscount(jo2.getString("discount"));
                                    commodity.setProduce_addr(jo2.getString("produce_addr"));
                                    JSONArray ja3 = jo2.getJSONArray("label");
                                    List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                    for (int k = 0; k < ja3.length(); k++) {
                                        Commodity.Labels labels = new Commodity.Labels();
                                        JSONObject jo3 = ja3.getJSONObject(k);
                                        String label_id = jo3.getString("label_id");
                                        String label_name = jo3.getString("label_name");
                                        labels.setLabel_id(label_id);
                                        labels.setLabel_name(label_name);
                                        List_labels.add(labels);
                                    }
                                    commodity.setAdats(List_labels);
                                    commodities.add(commodity);
                                }
                            }
                            adapter.setAdats(commodities);
                            lv_comm.setLayoutAnimation(layoutAnimationController);
                            lv_comm.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            tv_seek.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void init() {
        tv_repertory = (TextView) view.findViewById(R.id.tv_repertory);
        tv_repertory.setOnClickListener(this);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_price.setOnClickListener(this);
        tv_selling = (TextView) view.findViewById(R.id.tv_selling);
        tv_selling.setOnClickListener(this);
        sqliteHelper = new SqliteHelper(getActivity());
        database = sqliteHelper.getReadableDatabase();
        bill2List = new ArrayList<>();

//供应商
        but_provider= (Button) view.findViewById(R.id.but_provider);
        but_provider.setOnClickListener(this);

        but_derive = (Button) view.findViewById(R.id.but_derive);
        but_tolead = (Button) view.findViewById(R.id.but_tolead);
//        导出按钮
        but_derive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), MyFileManager.class);
                startActivityForResult(intent, 1);
                List<Commodity> aaa = new ArrayList<Commodity>();
            }
        });
        //    导入商品
        but_tolead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData = new String[]{
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        "11",
                        "12",
                        "13"
                };

                if (canSave(saveData)) {
                    insert = 0;
                    database.execSQL(("delete from  exls"));
                    for (int i = 0; i < 2; i++) {
                        ContentValues values = new ContentValues();
                        values.put("name", "模板");
                        values.put("py", "MB");
                        values.put("price", "1");
                        values.put("cost", "0");
                        values.put("bncode", "123456789");
                        values.put("store", "1");
                        values.put("good_limit", "2");
                        values.put("good_stock", "3");
                        values.put("PD", "");
                        values.put("GD", "");
                        values.put("marketable", "true");
                        insert = database.insert("exls", null, values);
                    }
                    if (insert > 0) {
                        initData();
                    }
                } else {
                    Toast.makeText(getContext(), "请填写任意一项内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        商品总字段
        tv_shuliang = (TextView) view.findViewById(R.id.tv_shuliang);
        tv_zong = (TextView) view.findViewById(R.id.tv_zong);
        tv_jinjia = (TextView) view.findViewById(R.id.tv_jinjia);
        tv_zongshoujia = (TextView) view.findViewById(R.id.tv_zongshoujia);

        tv_page = (TextView) view.findViewById(R.id.tv_page);
        tv_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setTitle("请输入页面");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.page_layout);
                final EditText editText= (EditText) window.findViewById(R.id.ed_page);
                Button but_goto= (Button) window.findViewById(R.id.but_goto);
                Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editText.getText().toString().equals("")&& StringUtils.isNumber(editText.getText().toString())){
                            page=Integer.parseInt(editText.getText().toString());
                            getTag_name(sp1str,sp2str,sp3str);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            dialog.dismiss();
                        }

                    }
                });
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    }
                });
            }
        });

        adats.clear();
        adats.add("全部状态");
        adats.add("上架");
        adats.add("下架");

        tv_sp1 = (TextView) view.findViewById(R.id.tv_sp1);
        tv_sp2 = (TextView) view.findViewById(R.id.tv_sp2);
        tv_sp3 = (TextView) view.findViewById(R.id.tv_sp3);
        tv_sp1.setOnClickListener(this);
        tv_sp2.setOnClickListener(this);
        tv_sp3.setOnClickListener(this);


        but_label = (Button) view.findViewById(R.id.but_label);
        but_label.setOnClickListener(this);

        listlabel = new ArrayList<>();

        listFenlei = new ArrayList<>();
        but_ItemAdd = (Button) view.findViewById(R.id.but_ItemAdd);
        but_ItemAdd.setOnClickListener(this);

        strings = new ArrayList<>();
        getLabel();
/**
 *  分类数据的获取
 */
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
                            strings.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                xuangzhong.setCategory(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                xuangzhong.setId(jo.getInt("tag_id"));
                                xuangzhong.setChecked(false);
                                strings.add(xuangzhong);
                                listFenlei.add(fenlei);
                            }
                            Fenlei_Entty fenlei_entty = new Fenlei_Entty();
                            fenlei_entty.setName("全部分类");
                            fenlei_entty.setVisibility(false);
                            fenlei_entty.setTag_id(1);
                            listFenlei.add(0, fenlei_entty);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });

        but_next = (Button) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        but_last = (Button) view.findViewById(R.id.but_last);
        but_last.setOnClickListener(this);


        but_unit = (Button) view.findViewById(R.id.but_unit);
        but_unit.setOnClickListener(this);

        adapter = new Comm_adapter(getContext());
        lv_comm = (ListView) view.findViewById(R.id.lv_comm);

        //动画效果
        layoutAnimationController=new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.listview_anim));
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);

        refreshLayout= (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);

                getTag_name(sp1str,sp2str,sp3str);
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadmore();
            }
        });
    }

    private void Loadadats(int str) {
//        获得全部数据
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("page", str)
                .params("pagelimit",20)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "11111加载" + s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            total=j2.getInt("total");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            JSONArray ja2=j2.getJSONArray("sum");
                            JSONObject jo4=ja2.getJSONObject(0);
                            jinjia=jo4.getInt("sum_store");
                            zongshoujia=jo4.getInt("sum_price");
                            zong=jo4.getInt("sum_cost");
                            commodities.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Commodity commodity = new Commodity();
                                JSONObject jo1 = jsonArray.getJSONObject(j);
                                commodity.setAuth(jo1.getString("auth"));
                                commodity.setCustom_member_price(jo1.getString("custom_member_price"));
                                commodity.setGoods_id(jo1.getString("goods_id"));
                                commodity.setIs_special_offer(jo1.getString("is_special"));
                                commodity.setMember_price(jo1.getString("member_price"));
                                commodity.setName(jo1.getString("name"));
                                commodity.setPy(jo1.getString("py"));
                                commodity.setPrice(jo1.getString("price"));
                                commodity.setCost(jo1.getString("cost"));
                                commodity.setBncode(jo1.getString("bncode"));
                                commodity.setTag_id(jo1.getString("tag_id"));
                                commodity.setTag_name(jo1.getString("tag_name"));
                                commodity.setUnit(jo1.getString("unit"));
                                commodity.setUnit_id(jo1.getInt("unit_id"));
                                commodity.setStore(jo1.getString("store"));
                                commodity.setProvider_name(jo1.getString("provider_name"));
                                commodity.setProvider_id(jo1.getString("provider_id"));
                                commodity.setCook_position(jo1.getString("cook_position"));
                                commodity.setSpecification(jo1.getString("specification"));
                                commodity.setGood_limit(jo1.getString("good_limit"));
                                commodity.setGood_stock(jo1.getString("good_stock"));
                                commodity.setPD(jo1.getString("PD"));
                                commodity.setGD(jo1.getString("GD"));
                                commodity.setMarketable(jo1.getString("marketable"));
                                commodity.setAltc(jo1.getString("ALTC"));
                                commodity.setBox_disable(jo1.getBoolean("box_disable"));
                                commodity.setDiscount(jo1.getString("discount"));
                                commodity.setProduce_addr(jo1.getString("produce_addr"));
                                JSONArray ja3 = jo1.getJSONArray("label");
                                List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                for (int k = 0; k < ja3.length(); k++) {
                                    Commodity.Labels labels = new Commodity.Labels();
                                    JSONObject jo3 = ja3.getJSONObject(k);
                                    String label_id = jo3.getString("label_id");
                                    String label_name = jo3.getString("label_name");
                                    labels.setLabel_id(label_id);
                                    labels.setLabel_name(label_name);
                                    List_labels.add(labels);
                                }
                                commodity.setAdats(List_labels);
                                commodities.add(commodity);

                            }
                            Log.d("print", "uuuu" + commodities);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            tv_shuliang.setText(total + "");
                            tv_zong.setText(zong + "");
                            tv_jinjia.setText(jinjia + "");
                            tv_zongshoujia.setText(zongshoujia + "");

                            if (total % 20 == 0) {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 20));
                            } else {
                                tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 20 + 1));
                            }
                            adapter.setAdats(commodities);
                            adapter.setOneidtextonclick(Buyoff_Fragment.this);
                            lv_comm.setLayoutAnimation(layoutAnimationController);
                            lv_comm.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_common_notes://常用备注
                Intent intent_common_notes=new Intent(getActivity(), Goods_Common_Notes_Activity.class);
                startActivity(intent_common_notes);
                break;
            case R.id.tv_repertory:
                if (!sort) {
                    SortUtils.upsales_num(commodities,sort);
                    sort = true;
                } else {
                    SortUtils.upsales_num(commodities,sort);
                    sort = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_price:
                if (!sort2) {
                    SortUtils.sort2(commodities, sort2);
                    sort2 = true;
                } else {
                    SortUtils.sort2(commodities, sort2);
                    sort2 = false;
                }
                adapter.setAdats(commodities);
                lv_comm.setLayoutAnimation(layoutAnimationController);
                lv_comm.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_selling:
                if (!sort3) {
                    SortUtils.sort3(commodities, sort3);
                    sort3 = true;
                } else {
                    SortUtils.sort3(commodities, sort3);
                    sort3 = false;
                }
                adapter.setAdats(commodities);
                lv_comm.setLayoutAnimation(layoutAnimationController);
                lv_comm.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_sp1:
                if (popRight != null && popRight.isShowing()) {
                    popRight.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    spadapter = new Spadapter(getContext());
                    spadapter.setAdats(listFenlei);
                    lv_pop.setAdapter(spadapter);
                    spadapter.setOneidtextonclick(new Spadapter.Oneidtextonclick() {
                        @Override
                        public void itmeeidtonclick(int i, String type) {
                            tv_sp1.setText(listFenlei.get(i).getName());
                            page=1;
                            if (i == 0) {
                                sp1str = "";
                            } else {
                                sp1str = listFenlei.get(i).getTag_id() + "";
                            }
                            commodities.clear();
                            getTag_name(sp1str, sp2str, sp3str);
                            popRight.dismiss();
                        }
                    });
//                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        }
//                    });
                    popRight = new PopupWindow(layout_sp1, tv_sp1.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popRight.setTouchable(true);// 设置popupwindow可点击  
                    popRight.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popRight.setFocusable(true);// 获取焦点  

                    popRight.showAsDropDown(tv_sp1);
                    popRight.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popRight.setFocusable(false);//失去焦点  
                            popRight.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popRight.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });
                }
                break;
            case R.id.tv_sp2:
                if (popMiddle != null && popMiddle.isShowing()) {
                    popMiddle.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    sp2adapter = new Sp2adapter(getContext());
                    sp2adapter.setAdats(listlabel);
                    lv_pop.setAdapter(sp2adapter);
                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            page=1;
                            tv_sp2.setText(listlabel.get(i).getLabel_name());
                            if (i == 0) {
                                sp2str = "";
                            } else {
                                sp2str = listlabel.get(i).getLabel_id() + "";
                            }
                            commodities.clear();
                            getTag_name(sp1str, sp2str, sp3str);
                            popMiddle.dismiss();
                        }
                    });
                    popMiddle = new PopupWindow(layout_sp1, tv_sp2.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popMiddle.setTouchable(true);// 设置popupwindow可点击  
                    popMiddle.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popMiddle.setFocusable(true);// 获取焦点  

                    popMiddle.showAsDropDown(tv_sp2);
                    popMiddle.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popMiddle.setFocusable(false);//失去焦点  
                            popMiddle.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popMiddle.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });
                }
                break;
            case R.id.tv_sp3:
                if (popLeft != null && popLeft.isShowing()) {
                    popLeft.dismiss();
                } else {
                    layout_sp1 = getActivity().getLayoutInflater().inflate(R.layout.popmenulist, null);
                    lv_pop = (ListView) layout_sp1.findViewById(R.id.lv_pop);
                    Sp3adapter sp3adapter = new Sp3adapter(getContext());
                    sp3adapter.setAdats(adats);
                    lv_pop.setAdapter(sp3adapter);
                    lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            page=1;
                            tv_sp3.setText(adats.get(i));
                            if (i == 0) {
                                sp3str = "";
                                commodities.clear();
                                getTag_name(sp1str, sp2str, sp3str);
                            }
                            if (i == 1) {
                                sp3str = "true";
                                commodities.clear();
                                getTag_name(sp1str, sp2str, sp3str);
                            }
                            if (i == 2) {
                                sp3str = "false";
                                commodities.clear();
                                getTag_name(sp1str, sp2str, sp3str);
                            }
                            popLeft.dismiss();
                        }
                    });
                    popLeft = new PopupWindow(layout_sp1, tv_sp3.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                    popLeft.setTouchable(true);// 设置popupwindow可点击  
                    popLeft.setOutsideTouchable(true);// 设置popupwindow外部可点击  
                    popLeft.setFocusable(true);// 获取焦点  

                    popLeft.showAsDropDown(tv_sp3);
                    popLeft.getContentView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            popLeft.setFocusable(false);//失去焦点  
                            popLeft.dismiss();//消除pw 
                            return true;
                        }
                    });
                    popLeft.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1f);
                        }
                    });
                }
                break;
//            单位的点击事件
            case R.id.but_unit:
                Intent intent = new Intent(getActivity(), Unit_activity.class);
                startActivity(intent);
                break;
            case R.id.but_next:
                if (Integer.valueOf(total) % 20 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                    if (page < (Integer.valueOf(total) / 20)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            getTag_name(sp1str,sp2str,sp3str);
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 20 + 1));
                    if (page < (Integer.valueOf(total) / 20 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                           getTag_name(sp1str,sp2str,sp3str);
                            paging1 = false;
                        }
                    }
                }
                break;
            //上一页
            case R.id.but_last:
                if (page > 1) {
                    if (Integer.valueOf(total) % 20 == 0) {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                        if (!paging2) {
                            paging2 = true;
                            getTag_name(sp1str,sp2str,sp3str);
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 20 + 1));
                        if (!paging2) {
                            paging2 = true;
                            getTag_name(sp1str,sp2str,sp3str);
                            paging2 = false;
                        }
                    }
                }
                break;
//            新增商品
            case R.id.but_ItemAdd:
//                    Intent intent1 = new Intent(getActivity(), ItemAdd_Activity.class);
//                    startActivity(intent1);
                Intent intent1 = new Intent(getActivity(), Addgoodgs_Activity.class);
                startActivity(intent1);
                break;
//            标签的点击事件
            case R.id.but_label:
                Intent intent2 = new Intent(getActivity(), Label_activity.class);
                startActivity(intent2);
                break;
            case R.id.but_provider:
                Intent intent3= new Intent(getActivity(), Provider_activity.class);
                startActivity(intent3);
                break;


        }
    }

    public void getTag_name(String s1, String s2, String s3) {
        Log.d("print", "tag_id" + s1 + "   " + s2 + "   " + s3);
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("marketable", s3)
                .params("tag_id", s1)
                .params("pagelimit",20)
                .params("label_id", s2)
                .params("page", page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "11111筛选" + s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            commodities.clear();
                            JSONArray ja2=j2.getJSONArray("sum");
                            JSONObject jo4=ja2.getJSONObject(0);
                            total=j2.getInt("total");
                            jinjia=jo4.getInt("sum_store");
                            zongshoujia=jo4.getInt("sum_price");
                            zong=jo4.getInt("sum_cost");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Commodity commodity = new Commodity();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                commodity.setAuth(jsonObject.getString("auth"));
                                commodity.setCustom_member_price(jsonObject.getString("custom_member_price"));
                                commodity.setGoods_id(jsonObject.getString("goods_id"));
                                commodity.setIs_special_offer(jsonObject.getString("is_special"));
                                commodity.setMember_price(jsonObject.getString("member_price"));
                                commodity.setName(jsonObject.getString("name"));
                                commodity.setPy(jsonObject.getString("py"));
                                commodity.setPrice(jsonObject.getString("price"));
                                commodity.setCost(jsonObject.getString("cost"));
                                commodity.setBncode(jsonObject.getString("bncode"));
                                commodity.setTag_id(jsonObject.getString("tag_id"));
                                commodity.setTag_name(jsonObject.getString("tag_name"));
                                commodity.setUnit(jsonObject.getString("unit"));
                                commodity.setStore(jsonObject.getString("store"));
                                commodity.setSpecification(jsonObject.getString("specification"));
                                commodity.setGood_limit(jsonObject.getString("good_limit"));
                                commodity.setGood_stock(jsonObject.getString("good_stock"));
                                commodity.setPD(jsonObject.getString("PD"));
                                commodity.setProvider_name(jsonObject.getString("provider_name"));
                                commodity.setProvider_id(jsonObject.getString("provider_id"));
                                commodity.setCook_position(jsonObject.getString("cook_position"));
                                commodity.setGD(jsonObject.getString("GD"));
                                commodity.setMarketable(jsonObject.getString("marketable"));
                                commodity.setAltc(jsonObject.getString("ALTC"));
                                commodity.setBox_disable(jsonObject.getBoolean("box_disable"));
                                commodity.setProduce_addr(jsonObject.getString("produce_addr"));
                                JSONArray ja3 = jsonObject.getJSONArray("label");
                                List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                for (int k = 0; k < ja3.length(); k++) {
                                    Commodity.Labels labels = new Commodity.Labels();
                                    JSONObject jo3 = ja3.getJSONObject(k);
                                    String label_id = jo3.getString("label_id");
                                    String label_name = jo3.getString("label_name");
                                    labels.setLabel_id(label_id);
                                    labels.setLabel_name(label_name);
                                    List_labels.add(labels);
                                }
                                commodity.setAdats(List_labels);
                                commodities.add(commodity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            tv_shuliang.setText(total+"");
                            if (Integer.valueOf(total) % 20 == 0) {
                                tv_page.setText(page + "/" + (Integer.valueOf(total) / 20));
                            } else {
                                tv_page.setText(page + "/" + (Integer.valueOf(total) / 20 + 1));
                            }
                            adapter.setOneidtextonclick(Buyoff_Fragment.this);
                            adapter.setAdats(commodities);
                            lv_comm.setLayoutAnimation(layoutAnimationController);
                            lv_comm.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public Map<String, String> stringMap = new HashMap<>();
    //补库存
    public void getaddstore(final double i,String id,final String context,final String sum){
        OkGo.post(SysUtils.getGoodsServiceUrl("edit_store"))
                .tag(getActivity())
                .params("store",i)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .params("goods_id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                                Log.e("print","修改库存成功"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                String stringcontext="商品"+context+"增加了"+sum+"个库存";
                                stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                                stringMap.put("work_name", SharedUtil.getString("name"));
                                stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                                stringMap.put("operate_type", "编辑" + context);
                                stringMap.put("content", stringcontext);
                                Gson gson = new Gson();
                                String ing = gson.toJson(stringMap);

                                getsensitivity(ing);
                                Toast.makeText(getActivity(),"补充库存成功",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    public void getLabel() {
        OkGo.post(SysUtils.getGoodsServiceUrl("label_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("Tag", s);
                        try {
                            JSONObject jo = new JSONObject(s);
                            JSONObject jo1 = jo.getJSONObject("response");
                            JSONObject jo2 = jo1.getJSONObject("data");
                            JSONArray ja = jo2.getJSONArray("units_info");
                            commodities.clear();
                            strings.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                label_entty = new Label_entty();
                                JSONObject jo3 = ja.getJSONObject(i);
                                label_entty.setLabel_name(jo3.getString("label_name"));
                                label_entty.setLabel_id(jo3.getString("label_id"));
                                xuangzhong.setCategory(jo3.getString("label_name"));
                                xuangzhong.setId(Integer.valueOf(jo3.getString("label_id")));
                                xuangzhong.setChecked(false);
                                strings.add(xuangzhong);
                                listlabel.add(label_entty);
                            }
                            Label_entty label=new Label_entty();
                            label.setLabel_name("全部标签");
                            label.setLabel_id("");
                            listlabel.add(0,label);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;//0.0-1.0  
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void itmeeidtonclick(int i) {
        if (SharedUtil.getString("type").equals("4")){
            Toast.makeText(getActivity(),"你没有该权限",Toast.LENGTH_SHORT).show();
        }else {
            if (i >= 0) {
//                Intent intent = new Intent(getActivity(), ItemAdd_Activity.class);
//                intent.putExtra("commodity", commodities.get(i));
//                startActivity(intent);
                Log.d("print","商品的折扣为多少"+commodities.get(i).getDiscount());
                Intent intent = new Intent(getActivity(), Addgoodgs_Activity.class);
                intent.putExtra("commodity", commodities.get(i));
                startActivity(intent);
            }
        }
    }

    @Override
    public void Storeonclick(final int i) {
        if (commodities.get(i).getAuth().equals("open")) {
        //补充库存
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("补充库存");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_supplement);
        Button but_add = (Button) window.findViewById(R.id.but_add);
        final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_add.getText().toString() != null && StringUtils.isNumber(ed_add.getText().toString())) {
                    double total = 0;
                    total = Double.parseDouble(commodities.get(i).getStore()) + Integer.parseInt(ed_add.getText().toString());
                    commodities.get(i).setStore(total + "");
                    getaddstore(total, commodities.get(i).getGoods_id(), commodities.get(i).getName(), ed_add.getText().toString());
                    adapter.notifyDataSetChanged();
                }
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
    }else if (commodities.get(i).getAuth().equals("close")){
            Toast.makeText(getContext(),"该商品未开放请联系管理员开放",Toast.LENGTH_SHORT).show();
        }
    }

    //敏感操作记录
    public void getsensitivity(String stringcontext) {
        Log.e("print", "敏感操作数据" + stringcontext);
        OkGo.post(SysUtils.getSellerServiceUrl("log_insert"))
                .tag(this)
                .params("map", stringcontext)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "敏感操作的数据" + s);

                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    Thread file;
    //    获取文件路径
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            final Bundle bundle ;
            if (data != null && data.getExtras() != null) {
                bundle=data.getExtras();
                file = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mapList = ExcelUtils.read2DB(new File(bundle.getString("file")), getContext());
                        file.interrupt();
                    }
                });
                file.start();
                Gson gson = new Gson();
                String str = gson.toJson(mapList);
                /**
                 * 批量导入商品的方法
                 */
                Log.d("print", "数据为" + str);
                Setimport(str);
                Log.d("print", "数据为" + str);
                Log.d("print", "获取路径" + bundle.getString("file"));
            }
        }

        getTag_name(sp1str,sp2str,sp3str);
    }

    private boolean canSave(String[] data) {
        boolean isOk = false;
        for (int i = 0; i < data.length; i++) {
            if (i > 0 && i < data.length) {
                if (!TextUtils.isEmpty(data[i])) {
                    isOk = true;
                }
            }
        }
        return isOk;
    }

    private void initData() {
        File file = new File(getSDPath() + "/Family");
        Log.d("print", "文件的名字为" + file.toString());
        makeDir(file);
        Log.d("print", "文件的名字为" + file.toString());
        ExcelUtils.initExcel(file.toString() + "/bill.xls", title);
        ExcelUtils.writeObjListToExcel(getBillData(), getSDPath()
                + "/Family/bill.xls", getActivity());
    }

    private ArrayList<ArrayList<String>> getBillData() {
        Cursor mCrusor = database.rawQuery("select * from exls", null);
        while (mCrusor.moveToNext()) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(mCrusor.getString(0));
            beanList.add(mCrusor.getString(2));
            beanList.add(mCrusor.getString(3));
            beanList.add(mCrusor.getString(4));
            beanList.add(mCrusor.getString(5));
            beanList.add(mCrusor.getString(6));
            beanList.add(mCrusor.getString(7));
            beanList.add(mCrusor.getString(8));
            beanList.add(mCrusor.getString(9));
            beanList.add(mCrusor.getString(10));
            beanList.add(mCrusor.getString(11));
            beanList.add(mCrusor.getString(12));
            beanList.add("分类名");
            bill2List.add(beanList);
        }
        mCrusor.close();
        return bill2List;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }


    public static String toUtf8(String str) {
                String result = null;
                try {
                        result = new String(str.getBytes("UTF-8"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                       e.printStackTrace();
                    }
                return result;
            }

    //批量导入商品
    public void Setimport(String str) {
        String strutf="";
        try {
            strutf=toUtf8(str);
            LogUtils.d("print数据为", "" + strutf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(getActivity());
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在导入数据...");
        loadingdialog.show();
        OkGo.post(SysUtils.getGoodsServiceUrl("enter"))
                .tag(getActivity())
                .params("map", strutf)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "导入成功" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            String message=jo1.getString("message");
                            if (status.equals("200")) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            loadingdialog.dismiss();
                        }
                    }
                });
    }
}
