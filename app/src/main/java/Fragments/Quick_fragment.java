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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Entty.Commodity;
import Entty.Maidan;
import Entty.Quick_Entty;
import Utils.SharedUtil;
import Utils.SortUtils;
import Utils.SysUtils;
import adapters.RecyclerView_adapter;
import base.BaseFragment;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;

import static base.BaseActivity.isnetworknew;

/**
 * Created by admin on 2017/3/27.
 */
public class Quick_fragment extends BaseFragment implements RecyclerView_adapter.OnItemClickListener {
    public SwipeRefreshLayout swipe_refresh_layout;
    public RecyclerView Re_view;
    public RecyclerView_adapter adapter;
    public List<Commodity> adats;
    private ItemTouchHelper touchhelper;
    public int i;
    public List<Maidan> maidanList=new ArrayList<>();
    public Button button;

    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;

    public Commodity commoditys;
    public ArrayList<Commodity> commoditylist;
    public View view;

//    快捷栏得选中数量
    public List<Quick_Entty> listquick;
    public Quick_Entty quick_entty;

    public boolean version=true;

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter intentFilter1=new IntentFilter();
        intentFilter1.addAction("com.yzx.reductionof");
        getContext().registerReceiver(broadcastReceiver,intentFilter1);

        //加号的广播
        IntentFilter intentFilter2=new IntentFilter();
        intentFilter2.addAction("com.yzx.add");
        getContext().registerReceiver(broadcastReceiver,intentFilter2);

//        删除的广播
        IntentFilter intentFilter3=new IntentFilter();
        intentFilter3.addAction("com.yzx.fupingdelete");
        getContext().registerReceiver(broadcastReceiver,intentFilter3);

        //        支付完成得广播
        IntentFilter intentFilter4=new IntentFilter();
        intentFilter4.addAction("com.yzx.clear");
        getContext().registerReceiver(broadcastReceiver,intentFilter4);


//        移动位置的广播
        IntentFilter intentFilter5=new IntentFilter();
        intentFilter5.addAction("com.yzx.yidongedit");
        getContext().registerReceiver(broadcastReceiver,intentFilter5);

        //现金支付成功的按钮
        IntentFilter intentFilter6=new IntentFilter();
        intentFilter6.addAction("com.yzx.determination");
        getContext().registerReceiver(broadcastReceiver,intentFilter6);

        //现金支付成功的按钮
        IntentFilter intentFilter7=new IntentFilter();
        intentFilter7.addAction("com.yzx.cut");
        getContext().registerReceiver(broadcastReceiver,intentFilter7);

    }
    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
//            if (action.equals("com.yzx.cut")){
//                Bundle bundle=intent.getExtras();
//                version=bundle.getBoolean("line");
//                if (version){
//                    init1();
//                }else {
//                    init1();
//                }
//                adapter.notifyDataSetChanged();
//            }
            /**
             改了没写好  *
             *
             */
            if(action.equals("com.yzx.reductionof")){
                Bundle bundle=intent.getExtras();
                float i= Float.parseFloat((String) bundle.get("reductionof"));
                Commodity commodity= (Commodity) bundle.getSerializable("commodity");
                for (int j=0;j<commoditylist.size();j++){
                    if (commoditylist.get(j).getName().equals(commodity.getName())){
                        int k = Integer.valueOf((listquick.get(j).getNumber()));
                        k--;
                        listquick.get(j).setNumber(k+"");
                        Log.d("listquick","listquick"+listquick);

                        Log.d("print","刷新了数据");
                        adapter.setListquick(listquick);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            /**
           改了没写好  *
             *
             */
            if (action.equals("com.yzx.add")){
                Bundle bundle=intent.getExtras();
                Commodity commodity= (Commodity) bundle.getSerializable("commodity");
                for (int i=0;i<commoditylist.size();i++){
                    if (commoditylist.get(i).getName().equals(commodity.getName())){
                        int k = Integer.valueOf(listquick.get(i).getNumber());
                        k++;
                        listquick.get(i).setNumber(k+"");
                        Log.d("listquick","listquick"+listquick);
                        adapter.setListquick(listquick);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            if (action.equals("com.yzx.fupingdelete")){
                Bundle bundle=intent.getExtras();
                Log.d("print","ddhhhhhhdddccaca");
//                if (commoditys!=null) {
                    Commodity commodity = (Commodity) bundle.getSerializable("commoditys");
                    for (int i = 0; i < commoditylist.size(); i++) {
                        if (commoditylist.get(i).getName().equals(commodity.getName())) {
                            listquick.get(i).setNumber(0+"");
                            Log.d("print","刷新了数据");
                            adapter.setListquick(listquick);
                            adapter.notifyDataSetChanged();
                        }
//                    }
                }
            }
            if (action.equals("com.yzx.clear")){
                for (int i=0;i<listquick.size();i++){
                    listquick.get(i).setNumber(0+"");
                    adapter.setListquick(listquick);
                    adapter.notifyDataSetChanged();
                }
            }
            if (action.equals("com.yzx.determination")){
                for (int i=0;i<listquick.size();i++){
                    listquick.get(i).setNumber(0+"");
                    adapter.setListquick(listquick);
                    adapter.notifyDataSetChanged();
                }
            }

            if (action.equals("com.yzx.yidongedit")){
//                touchhelper.attachToRecyclerView(Re_view);
            }
        }
    };

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.quick_frament, null);
//
//        init();
//
//
//        /**
//         *
//         */
//
//
//        touchhelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
//                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//                    final int swipeFlags = 0;
//                    return makeMovementFlags(dragFlags, swipeFlags);
//                }
//                return 0;
//            }
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
//                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
//                if (fromPosition < toPosition) {
//                    for (int i = fromPosition; i < toPosition; i++) {
//                        Collections.swap(commoditylist, i, i + 1);
//                    }
//                } else {
//                    for (int i = fromPosition; i > toPosition; i--) {
//                        Collections.swap(commoditylist, i, i - 1);
//                    }
//                }
//                adapter.notifyItemMoved(fromPosition, toPosition);
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//            @Override
//            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
//                }
//                super.onSelectedChanged(viewHolder, actionState);
//            }
//
//            //当手指松开的时候（拖拽完成的时候）调用
//            @Override
//            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                super.clearView(recyclerView, viewHolder);
//                viewHolder.itemView.setBackgroundColor(0);
//            }
//        });
////        touchhelper.attachToRecyclerView(Re_view);//设置可拖动
//
//
//
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.quick_frament;
    }

    @Override
    protected void init(View view) {
        init1(view);

        touchhelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
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
                        Collections.swap(commoditylist, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(commoditylist, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
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
            }
        });

        super.init(view);
    }

    private void loaddata() {
        OkGo.post(SysUtils.getGoodsServiceUrl("altc_goods"))
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
                                    commodity.setCustom_member_price(jo2.getString("custom_member_price"));
                                    commodity.setIs_special_offer(jo2.getString("is_special"));
                                    commodity.setMember_price(jo2.getString("member_price"));
                                    commodity.setPosition(jo2.getInt("position"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setCost(jo2.getString("cost"));
//                                    adats.add(commodity);
                                    commoditylist.add(commodity);
                                }

//                                for (int i = 0; i < adats.size(); i++) {
//                                    if (adats.get(i).getPosition() != 0&&adats.size()>=adats.get(i).getPosition()) {
//                                        commoditylist.set((adats.get(i).getPosition() - 1), adats.get(i));
//                                    }
//                                }
                                SortUtils.sort7(commoditylist);

                                Log.d("print", "得到" + adats);
                                adapter.setAdats(commoditylist);
                                Re_view.setAdapter(adapter);

                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            if (swipe_refresh_layout!=null){
                                swipe_refresh_layout.setRefreshing(false);
                            }
                        }
                    }
                });


    }

    private void init1(View view) {

        adats=new ArrayList<>();
        Log.d("print","新建了");
        listquick=new ArrayList<>();

        for (int i=0;i<500;i++){
            quick_entty=new Quick_Entty();
            quick_entty.setNumber(0+"");
            listquick.add(quick_entty);
        }

        commoditylist=new ArrayList<>();
//        if (SysUtils.isWifiConnected(getActivity())){
//        if (isnetworknew){
////        if (SysUtils.isNetworkAvailable(getActivity())){
////        if (SysUtils.isNetworkOnline()){
//            loaddata();
//        }else {
//            Localdata();
//        }

        if (SharedUtil.getString("sw_weight")!=null){
            if (Boolean.parseBoolean(SharedUtil.getString("sw_weight"))){
                if (isnetworknew){
                    loaddata();
                }else {
                    Localdata();
                }
            }else {
                Localdata();
            }
        }else {
            Localdata();
        }

        swipe_refresh_layout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (SharedUtil.getString("sw_weight")!=null){
                    if (Boolean.parseBoolean(SharedUtil.getString("sw_weight"))){
                        if (isnetworknew){
                            loaddata();
                        }else {
                            Localdata();
                        }
                    }else {
                        Localdata();
                    }
                }else {
                    Localdata();
                }
            }
        });

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

        Re_view = (RecyclerView) view.findViewById(R.id.Re_view);
//        Re_view.addItemDecoration(new DividerGridItemDecoration(getActivity()));//设置分割线
        adapter = new RecyclerView_adapter(getActivity());
//        if (version){
//            Re_view.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        }else {
//            Re_view.setLayoutManager(new GridLayoutManager(getContext(), 7));
//        }

        Re_view.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.setListquick(listquick);
        adapter.setAdats(commoditylist);
        adapter.setOnItemClickListener(this);
        Re_view.setAdapter(adapter);

    }


    public void Localdata() {
        commoditylist.clear();
        sqliteHelper = new SqliteHelper(getActivity());
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where ALTC=?", new String[]{"1"});
        if (cursor.getCount() == 0) {
            loaddata();
        } else {
        while (cursor.moveToNext()) {
            commoditys = new Commodity();
            commoditys.setGoods_id(cursor.getString(cursor.getColumnIndex("goods_id")));
            commoditys.setIs_special_offer(cursor.getString(cursor.getColumnIndex("is_special_offer")));
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
            commoditys.setAltc(cursor.getString(cursor.getColumnIndex("ALTC")));
            commoditys.setMember_price(cursor.getString(cursor.getColumnIndex("member_price")));
            commoditys.setCustom_member_price(cursor.getString(cursor.getColumnIndex("custom_member_price")));
            commoditylist.add(commoditys);
        }
    }
    if (swipe_refresh_layout!=null){
        swipe_refresh_layout.setRefreshing(false);
    }
    }


    @Override
    public void onClick(View view, int position) {
        if (SharedUtil.getString("operational").equals("0")) {
//        广播的发送
            Intent intent = new Intent("com.yzx.kuaijie");
            intent.putExtra("maidan", commoditylist.get(position));
            intent.putExtra("type","quick");
            int k = Integer.valueOf(listquick.get(position).getNumber()) + 1;
            listquick.get(position).setNumber(k + "");
            intent.putExtra("position", position);
            getContext().sendBroadcast(intent);

            Intent mIntent = new Intent();
            mIntent.setAction("qwer");
            mIntent.putExtra("yaner", commoditylist.get(position));
            //发送广播  
            getContext().sendBroadcast(mIntent);

//            button = (Button) view.findViewById(R.id.but_shuliang);
//            if (button.getVisibility() == View.VISIBLE && position < commoditylist.size()) {
//                int j = Integer.valueOf(button.getText().toString());
//                button.setTag(position);
//                j++;
//                button.setText(j + "");
//            }
//            if (button.getVisibility() == View.GONE && position <= commoditylist.size()) {
//                button.setVisibility(View.VISIBLE);
//                button.setTag(position);
//                button.setText("" + 1);
//            }
            //改变位置
            if (position == commoditylist.size() - 1) {
                if (commoditylist.size() < 16) {
//                Intent intent1=new Intent(getContext(), Commoditymanagement_Activity.class);
//                startActivity(intent1);
//                adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "快捷栏已满", Toast.LENGTH_SHORT).show();
                }
            }

//        String str =
//        adats.add(0, str);
//        adapter.notifyItemMoved(position, 0);
//        Re_view.scrollToPosition(0);
        }

    }
}
