package Fragments;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Order_Entty;
import Utils.PrintUtil;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Order_adapter;
import base.BaseFragment;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import shujudb.Sqlite_Entity;

/**
 * Created by admin on 2017/4/21.
 */
public class Order_fragment extends BaseFragment implements Order_adapter.Onitmeclick {

    public View view;
    public ListView lv_order;
    public Order_adapter adapter;
    public int type = 0;

    //判断挂单是否成功
    public boolean isEntryorders=false;

    public boolean iszuofei = false;
    public boolean method = false;
    public boolean isshow = false;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;

    public List<Order_Entty.ResponseBean.DataBean> list_order;
    public Order_Entty.ResponseBean.DataBean order_entty;

    public List<Order_Entty.ResponseBean.DataBean.GoodsBean> list_goodsBean;

    @SuppressLint("ValidFragment")
    public Order_fragment(boolean method) {
        this.method = method;
    }


    @SuppressLint("ValidFragment")
    public Order_fragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.yzx.cancellation");
        getActivity().registerReceiver(broadcastReceiver, intentFilter1);
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.yzx.cancellation")) {
                if (!iszuofei) {
                    type = 1;
                    iszuofei = true;
                } else {
                    type = 0;
                    iszuofei = false;
                }
                adapter.getType(type);
                adapter.notifyDataSetChanged();
            }

        }
    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
//                            Toast.makeText(getApplicationContext(), "连接成功",
//                                    Toast.LENGTH_SHORT).show();

                            break;
                        case BluetoothService.STATE_CONNECTING:

                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
//                    Toast.makeText(getApplicationContext(), "设备连接丢失",
//                            Toast.LENGTH_SHORT).show();

                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
//                    if (getApplicationContext()!=null){
////                        Toast.makeText(getApplicationContext(), "关闭设备",
////                                Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }

    };


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.order_fragment, null);
//
//
//        //蓝牙打印
//        mService = new BluetoothService(getActivity(), mHandler);
//        if (mService.isAvailable() == false) {
////            Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_LONG).show();
//        }
//
//        init();
//
//        Loadadats();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.order_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);

        //蓝牙打印
        mService = new BluetoothService(getActivity(), mHandler);
        if (mService.isAvailable() == false) {
//            Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_LONG).show();
        }

        init1(view);

        Loadadats();
    }

    private void init1(View view) {
        list_goodsBean = new ArrayList<>();
        list_order = new ArrayList<>();
        adapter = new Order_adapter(getContext(), method);
        adapter.setOnitemclick(Order_fragment.this);
        lv_order = (ListView) view.findViewById(R.id.lv_order);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            Loadadats();
        }
    }

    private void Loadadats() {
        if (method) {
            list_order.clear();
            OkGo.post(SysUtils.getSellerServiceUrl("get_order_info"))
                    .tag(getActivity())
                    .params("flag", "true")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d("print", "赊账的信息"+SharedUtil.getString("seller_token")+ "    "+ s);
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
                                Log.d("print", "数据" + list_order);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                adapter.getadats(list_order);
                                lv_order.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            list_order.clear();
            Sqlite_Entity sqlite_entity=new Sqlite_Entity(getActivity());
            sqlite_entity.queryguadanorder();
            try {
                JSONArray jsonArray=new JSONArray(sqlite_entity.queryguadanorder());
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    order_entty = new Order_Entty.ResponseBean.DataBean();
                    order_entty.setOrder_id(jsonObject.getString("order_id"));
//                    order_entty.setObj_id(jsonObject.getString("obj_id"));
//                    order_entty.setPay_status(jsonObject.getString("pay_status"));
//                    order_entty.setSeller_id(jsonObject.getString("seller_id"));
//                    order_entty.setCreatetime(jsonObject.getString("createtime"));
                    order_entty.setName(jsonObject.getString("name"));
//                    order_entty.setNums(jsonObject.getString("nums"));
                    order_entty.setMark_text(jsonObject.getString("remarks"));
                    order_entty.setCreatetime(jsonObject.getString("createtime"));
//                    order_entty.setPrice(jsonObject.getString("price"));
//                    order_entty.setGoods_id(jsonObject.getString("goods_id"));
//                    order_entty.setItem_id(jsonObject.getString("item_id"));
                    list_order.add(order_entty);
                    Log.d("print","打印挂单的数量"+i);
                }
                adapter.getadats(list_order);
                lv_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            OkGo.post(SysUtils.getSellerServiceUrl("get_order_info"))
//                    .tag(getActivity())
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                            Log.d("print", "赊账的信息" + s);
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                JSONObject jo1 = jsonObject.getJSONObject("response");
//                                JSONArray ja1 = jo1.getJSONArray("data");
//                                for (int i = 0; i < ja1.length(); i++) {
//                                    order_entty = new Order_Entty.ResponseBean.DataBean();
//                                    JSONObject jo2 = ja1.getJSONObject(i);
//                                    JSONArray ja2 = jo2.getJSONArray("goods");
//                                    for (int j = 0; j < ja2.length(); j++) {
//                                        Order_Entty.ResponseBean.DataBean.GoodsBean goodsBean = new Order_Entty.ResponseBean.DataBean.GoodsBean();
//                                        JSONObject jo3 = ja2.getJSONObject(0);
//                                        goodsBean.setGoods_name(jo3.getString("goods_name"));
//                                        goodsBean.setCost(jo3.getString("cost"));
//                                        goodsBean.setGoods_id(jo3.getString("goods_id"));
//                                        goodsBean.setStore(jo3.getString("store"));
//                                        goodsBean.setGoods_num(jo3.getString("goods_num"));
//                                        goodsBean.setGoods_price(jo3.getString("goods_price"));
//                                        list_goodsBean.add(goodsBean);
//                                    }
//                                    order_entty.setGoods(list_goodsBean);
//                                    order_entty.setOrder_id(jo2.getString("order_id"));
//                                    order_entty.setObj_id(jo2.getString("obj_id"));
//                                    order_entty.setPay_status(jo2.getString("pay_status"));
//                                    order_entty.setSeller_id(jo2.getString("seller_id"));
//                                    order_entty.setCreatetime(jo2.getString("createtime"));
//                                    order_entty.setName(jo2.getString("name"));
//                                    order_entty.setNums(jo2.getString("nums"));
//                                    order_entty.setMark_text(jo2.getString("mark_text"));
//                                    order_entty.setPrice(jo2.getString("price"));
//                                    order_entty.setGoods_id(jo2.getString("goods_id"));
//                                    order_entty.setItem_id(jo2.getString("item_id"));
//                                    list_order.add(order_entty);
//                                }
//                                Log.d("print", "数据" + list_order);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } finally {
//                                adapter.getadats(list_order);
//                                lv_order.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
        }
    }
    @Override
    public void setrlitmeclick(int i, View view) {
        if (method) {
            if (!isshow) {
                view.setVisibility(View.VISIBLE);
                isshow = true;
            } else {
                view.setVisibility(View.GONE);
                isshow = false;
            }
        } else {
            Intent mIntent = new Intent();
            mIntent.setAction("com.yzx.chech");
            Log.d("print", "id" + list_order.get(i).getOrder_id());
            mIntent.putExtra("order", list_order.get(i).getOrder_id());
            getActivity().sendBroadcast(mIntent);
        }
    }

    @Override
    public void setonitmeoncick(int i) {
        if (!isEntryorders){
            isEntryorders=true;
        if (type == 0) {
            //发送广播  
            Intent mIntent = new Intent();
            mIntent.setAction("com.yzx.order");
            Log.d("print", "id" + list_order.get(i).getOrder_id());
            mIntent.putExtra("order", list_order.get(i).getOrder_id());
            getActivity().sendBroadcast(mIntent);
            isEntryorders=false;
            Loadadats();
        }
//        else {
//            if (!method) {
//                Toast.makeText(getContext(), "赊账订单不能解挂", Toast.LENGTH_SHORT).show();
//            } else {
//                OkGo.post(SysUtils.getSellerServiceUrl("delete_order"))
//                        .tag(this)
//                        .cacheKey("cacheKey")
//                        .cacheMode(CacheMode.DEFAULT)
//                        .params("order_id", list_order.get(i).getOrder_id())
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(String s, Call call, Response response) {
//                                Log.d("print", "作费" + s);
//                                try {
//                                    JSONObject jsonObject = new JSONObject(s);
//                                    JSONObject jo1 = jsonObject.getJSONObject("response");
//                                    String status = jo1.getString("status");
//                                    if (status.equals("200")) {
//                                        isEntryorders=false;
//                                        Toast.makeText(getActivity(), "解挂成功", Toast.LENGTH_SHORT).show();
//                                        Loadadats();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            }
//        }
        }else {
            Toast.makeText(getActivity(), "正在解挂请稍等", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoorderlist=new ArrayList<>();



    //打印的方法
    @Override
    public void setONprint(int i) {
        getPrint(list_order.get(i).getOrder_id());
    }

    @Override
    public void setCancel(int i) {
        if (!method){
            Sqlite_Entity sqlite_entity=new Sqlite_Entity(getActivity());
            int deleteguadan = sqlite_entity.deleteguadan(list_order.get(i).getOrder_id());
            if (deleteguadan==1){
                isEntryorders=false;
                Toast.makeText(getActivity(), "解挂成功", Toast.LENGTH_SHORT).show();
                Loadadats();
            }
        }else {
            OkGo.post(SysUtils.getSellerServiceUrl("delete_order"))
                    .tag(this)
                    .cacheKey("cacheKey")
                    .cacheMode(CacheMode.DEFAULT)
                    .params("order_id", list_order.get(i).getOrder_id())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d("print", "作费" + s);
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONObject jo1 = jsonObject.getJSONObject("response");
                                String status = jo1.getString("status");
                                if (status.equals("200")) {
                                    isEntryorders=false;
                                    Toast.makeText(getActivity(), "解挂成功", Toast.LENGTH_SHORT).show();
                                    Loadadats();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    public void getPrint(String order_id){
        final String[] order1_id = new String[1];
        OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id", order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("print","设置挂单的打印的"+s);
                        try {
                            int unm = 0;
                            double t = 0;
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jso=jsonObject.getJSONObject("response");
                            String status=jso.getString("status");
                            if (status.equals("200")){
                                mSelf_Service_GoodsInfoorderlist.clear();
                                JSONArray jsonArray=jso.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jo=jsonArray.getJSONObject(i);
                                    Self_Service_GoodsInfo self_service_goodsInfo=new Self_Service_GoodsInfo();
                                    self_service_goodsInfo.setCost(jo.getString("cost"));
                                    self_service_goodsInfo.setName(jo.getString("name"));
                                    self_service_goodsInfo.setNumber(jo.getString("nums"));
                                    self_service_goodsInfo.setPrice(jo.getString("price"));
//                                    self_service_goodsInfo.setSize(jo.getString("amount"));
                                    self_service_goodsInfo.setProduct_id(jo.getString("product_id"));
                                    order1_id[0] =jo.getString("order_id");
                                    mSelf_Service_GoodsInfoorderlist.add(self_service_goodsInfo);
                                    for (int j=0;j<mSelf_Service_GoodsInfoorderlist.size();j++){
                                        unm += Double.parseDouble(mSelf_Service_GoodsInfoorderlist.get(i).getNumber());
                                        t = TlossUtils.add(t, (Double.parseDouble(StringUtils.stringpointtwo(mSelf_Service_GoodsInfoorderlist.get(i).getPrice())) * Float.parseFloat(mSelf_Service_GoodsInfoorderlist.get(i).getNumber())));
                                    }

                                }
                                String address_Mac= SharedUtil.getString("ReceiptPrint_BluetoothMac_address");
                                if(TextUtils.isEmpty(address_Mac)){
                                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"如需打印小票，请先点击打印按钮，连接小票打印机",20);
                                }else {
                                    con_dev = mService.getDevByMac(address_Mac);
                                    mService.connect(con_dev);
                                }

                                if (mSelf_Service_GoodsInfoorderlist.size()>0) {
                                    PrintUtil.receiptPrint(getActivity(), mService, "", order1_id[0], 0, "", 0, "" + t, "",
                                            "", 3, (ArrayList<Self_Service_GoodsInfo>) mSelf_Service_GoodsInfoorderlist,(ArrayList<Self_Service_GoodsInfo>) mSelf_Service_GoodsInfoorderlist, "", "", "","1","");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//        PrintUtil printUtil1 = new PrintUtil(getActivity());
//        printUtil1.openButtonClicked();
//        String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg(SharedUtil.getString("name"), SharedUtil.getString("seller_tel"), order_id, time1, commodities, entty,
//                1, Double.parseDouble(tv_netreceipts.getText().toString()),"", tv_netreceipts.getText().toString(), tv_netreceipts.getText().toString(), tv_Surplus.getText().toString(),"0","111","22222");
//
//        if (PrintWired.usbPrint(getActivity(),syy)){
//
//        }else {
//            printUtil1.printstring(syy);
//
//            mService.sendMessage(syy, "GBK");
//        }
    }


}
