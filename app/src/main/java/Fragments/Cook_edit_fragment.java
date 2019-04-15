package Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Maidan;
import Entty.Quick_Entty;
import Utils.SortUtils;
import Utils.SysUtils;
import adapters.Quick_edit_Apapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;

/**
 * Created by admin on 2017/3/27.
 */
public class Cook_edit_fragment extends Fragment implements Quick_edit_Apapter.Ondeleteonclick {
    public RecyclerView Re_view;
    public Quick_edit_Apapter adapter;
    public ArrayList<Commodity> adats;
    private ItemTouchHelper touchhelper;
    public int i;
    public List<Maidan> maidanList = new ArrayList<>();
    public Button button;

    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;

    public Commodity commoditys;
    public List<Commodity> commoditylist;


    //    快捷栏得选中数量
    public List<Quick_Entty> listquick;
    public Quick_Entty quick_entty;


    public List<Map<String, String>> mapList;


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onStart() {
        super.onStart();
//        IntentFilter intentFilter1=new IntentFilter();
//        intentFilter1.addAction("com.yzx.reductionof");
//        getContext().registerReceiver(broadcastReceiver,intentFilter1);

//        //加号的广播
//        IntentFilter intentFilter2=new IntentFilter();
//        intentFilter2.addAction("com.yzx.add");
//        getContext().registerReceiver(broadcastReceiver,intentFilter2);
//
////        删除的广播
//        IntentFilter intentFilter3=new IntentFilter();
//        intentFilter3.addAction("com.yzx.fupingdelete");
//        getContext().registerReceiver(broadcastReceiver,intentFilter3);

//        //        支付完成得广播
//        IntentFilter intentFilter4=new IntentFilter();
//        intentFilter4.addAction("com.yzx.clear");
//        getContext().registerReceiver(broadcastReceiver,intentFilter4);


//        移动位置的广播
        IntentFilter intentFilter5 = new IntentFilter();
        intentFilter5.addAction("com.yzx.yidongedit");
        getContext().registerReceiver(broadcastReceiver, intentFilter5);

        IntentFilter intentFilter6 = new IntentFilter();
        intentFilter6.addAction("com.yzx.edit");
        getContext().registerReceiver(broadcastReceiver, intentFilter6);

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if(action.equals("com.yzx.reductionof")){
//                Bundle bundle=intent.getExtras();
//                float i= Float.parseFloat((String) bundle.get("reductionof"));
//                Commodity commodity= (Commodity) bundle.getSerializable("commodity");
//                for (int j=0;j<commoditylist.size();j++){
//                    if (commoditylist.get(j).getName().equals(commodity.getName())){
//                        int k = Integer.valueOf((listquick.get(j).getNumber()));
//                        k--;
//                        listquick.get(j).setNumber(k+"");
//                        Log.d("listquick","listquick"+listquick);
//                        adapter.setListquick(listquick);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//
//            }
//            /**
//           改了没写好  *
//             *
//             */
//
//            if (action.equals("com.yzx.add")){
//                Bundle bundle=intent.getExtras();
//                Commodity commodity= (Commodity) bundle.getSerializable("commodity");
//                for (int i=0;i<commoditylist.size();i++){
//                    if (commoditylist.get(i).getName().equals(commodity.getName())){
//                        int k = Integer.valueOf(listquick.get(i).getNumber());
//                        k++;
//                        listquick.get(i).setNumber(k+"");
//                        Log.d("listquick","listquick"+listquick);
//                        adapter.setListquick(listquick);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            if (action.equals("com.yzx.fupingdelete")){
//                Bundle bundle=intent.getExtras();
//                Log.d("print","ddhhhhhhdddccaca");
//                if (commoditys!=null) {
//                    Commodity commodity = (Commodity) bundle.getSerializable("commoditys");
//                    for (int i = 0; i < commoditylist.size(); i++) {
//                        if (commoditylist.get(i).getName().equals(commodity.getName())) {
//                            listquick.get(i).setNumber(0+"");
//                            adapter.setListquick(listquick);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            }
//            if (action.equals("com.yzx.clear")){
//                for (int i=0;i<16;i++){
//                    listquick.get(i).setNumber(0+"");
//                    adapter.setListquick(listquick);
//                    adapter.notifyDataSetChanged();
//                }
//            }
            if (action.equals("com.yzx.yidongedit")) {
                touchhelper.attachToRecyclerView(Re_view);

            }
            //控制删除的
            if (action.equals("com.yzx.edit")) {
            }

        }
    };

    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quick_frament, null);

        init();
        loaddata();

        Re_view = (RecyclerView) view.findViewById(R.id.Re_view);
//        Re_view.addItemDecoration(new DividerGridItemDecoration(getActivity()));//设置分割线
        adapter = new Quick_edit_Apapter(getActivity());
        adapter.setOndeleteonclick(this);
        Re_view.setLayoutManager(new GridLayoutManager(getContext(), 2));

        /**
         *
         */
        adapter.setAdats(adats);
        Re_view.setAdapter(adapter);

        touchhelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adats, i, i + 1);
                    }
//                    adats.get(toPosition).setPosition((toPosition-fromPosition));
//                    for (;fromPosition<toPosition;fromPosition++){
//                        adats.get(fromPosition).setPosition((adats.get(fromPosition).getPosition()-1));
//                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adats, i, i - 1);
                    }
//                    adats.get(fromPosition).setPosition(fromPosition-toPosition);
//                    for (;toPosition<fromPosition;toPosition++){
//                        adats.get(toPosition).setPosition((adats.get(fromPosition).getPosition()+1));
//                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                adats.get(fromPosition).setPosition(toPosition);
                getyidongweiz();
                Log.d("print", "移动的前坐标是" + fromPosition + "移动的后坐标是" + toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            //当手指松开的时候（拖拽完成的时候）调用
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
//                getyidongweiz();
            }
        });
//        touchhelper.attachToRecyclerView(Re_view);//设置可拖动


        return view;
    }

    private void init() {

        mapList = new ArrayList<>();

        listquick = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            quick_entty = new Quick_Entty();
            quick_entty.setNumber(0 + "");
            listquick.add(quick_entty);
        }

        adats = new ArrayList<>();
        commoditylist = new ArrayList<>();

        adats.clear();
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where ALTC=?", new String[]{"1"});
        while (cursor.moveToNext()) {
            commoditys = new Commodity();
            commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
            commoditys.setName(cursor.getString(cursor.getColumnIndex("name")));
            commoditys.setPy(cursor.getString(cursor.getColumnIndex("py")));
            commoditys.setBn(cursor.getString(cursor.getColumnIndex("bn")));
            commoditys.setProduct_id(cursor.getString(cursor.getColumnIndex("product_id")));
            commoditys.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            commoditys.setCost(cursor.getString(cursor.getColumnIndex("cost")));
            commoditys.setBncode(cursor.getString(cursor.getColumnIndex("bncode")));
            commoditys.setTag_id(cursor.getString(cursor.getColumnIndex("tag_id")));
            commoditys.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
            commoditys.setStore(cursor.getString(cursor.getColumnIndex("store")));
            commoditys.setGood_limit(cursor.getString(cursor.getColumnIndex("good_limit")));
            commoditys.setGood_stock(cursor.getString(cursor.getColumnIndex("good_stock")));
            commoditys.setPD(cursor.getString(cursor.getColumnIndex("PD")));
            commoditys.setGD(cursor.getString(cursor.getColumnIndex("GD")));
            commoditys.setMarketable(cursor.getString(cursor.getColumnIndex("marketable")));
            commoditys.setTag_name(cursor.getString(cursor.getColumnIndex("tag_name")));
            commoditylist.add(commoditys);
        }

        Log.d("print", "iiiiii" + commoditylist);


//        OkGo.post("")
//                .tag(this)
//                .cacheKey("cacheKey")
//                .connTimeOut(1000)
//                .cacheMode(CacheMode.DEFAULT)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d("",""+s);
//
//
//                    }
//                });


    }

    private void loaddata() {
        OkGo.post(SysUtils.getGoodsServiceUrl("cook_goods"))
                .tag(getContext())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "快捷栏信息是" + s);
                        try {
                            commoditylist.clear();
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject jo1 = jsonobject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                JSONArray ja1 = jo1.getJSONArray("data");
                                for (int i = 0; i < ja1.length(); i++) {
                                    Commodity commodity = new Commodity();
                                    JSONObject jo2 = ja1.getJSONObject(i);
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setCook_position(jo2.getString("cook_position"));
                                    adats.add(commodity);
//                                    commoditylist.add(commodity);
                                }

//                                for (int i = 0; i < adats.size(); i++) {
//                                    if (adats.get(i).getPosition() != 0&&adats.size()>=adats.get(i).getPosition()) {
//                                        commoditylist.set((adats.get(i).getPosition() - 1), adats.get(i));
//                                    }
//                                }
                                SortUtils.sort8(adats);
                                SortUtils.sort8(adats);

                                Log.d("print", "得到" + adats);
                                adapter.setAdats(adats);
                                Re_view.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    public void getyidongweiz() {
        mapList.clear();
        for (int j = 0; j < adats.size(); j++) {
            Map<String, String> map1 = new HashMap<>();
            map1.put("goods_id", adats.get(j).getGoods_id());
            map1.put("cook_position", (j + 1) + "");
            mapList.add(map1);
        }
        Gson gson = new Gson();
        String str = gson.toJson(mapList);
        Log.d("print", "变化的是" + str);
        OkGo.post(SysUtils.getGoodsServiceUrl("altc_sort"))
                .tag(getActivity())
                .params("map", str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "数据是多少" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data = jo1.getString("data");
//                                Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "移动失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    @Override
    public void Deleteclick(int position) {
        deleteQuick(position);
    }

    public void deleteQuick(int position) {
//        mapList.clear();
//        Map<String, String> map1 = new HashMap<>();
//        map1.put("goods_id", adats.get(position).getGoods_id());
//        map1.put("alt", (position + 1) + "");
        String str="{"+"\""+"goods_id"+"\""+":"+adats.get(position).getGoods_id()
                +","+"\""+"cook_position"+"\""+":"+"0"+"}";
        Log.d("print","删除的数据是"+str);
        adats.get(position).getPosition();
        adats.get(position).getGoods_id();
        Log.d("print", "编辑的商品位置是  " + adats.get(position).getPosition() + "编辑的名字是  " + adats.get(position).getName());
        OkGo.post(SysUtils.getGoodsServiceUrl("delete_altc"))
                .tag(getActivity())
                .params("map",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo1=jsonObject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                getyidongweiz();
                                adats.clear();
                                loaddata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

