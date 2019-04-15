package adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Check_tuihuo;
import Entty.Refund_entty;
import Utils.PrintUtil;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import widget.MylistView;

/**
 * Created by admin on 2017/4/24.
 * 退货
 */
public class Check_Adapter extends BaseAdapter implements Leibu_adapter.Ontuihuoonclick {


    public Context context;
    public List<Check_tuihuo> adats;
    public Leibu_adapter adapter;
    public boolean iszhangkai = false;
    public List<Refund_entty> list_refund;

    public List<Check_tuihuo> check_List;

    //判断是否展开
    public List<Boolean> iszhankai;


    public int j=-1;
    public String payment = "";

    public Check_Adapter(Context context) {
        this.context = context;
        adapter = new Leibu_adapter(context);
        list_refund = new ArrayList<Refund_entty>();
        this.check_List = new ArrayList<>();
        this.adats = new ArrayList<>();
        this.iszhankai=new ArrayList<>();
    }

    public void getAdats(List<Check_tuihuo> adats) {
        this.adats = adats;
        for (int i=0;i<adats.size();i++){
            iszhankai.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats.size();
    }

    @Override
    public Object getItem(int i) {
        return adats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHored viewHored;
        if (view != null) {
            viewHored = (ViewHored) view.getTag();
        } else {
            viewHored = new ViewHored();
            view = LayoutInflater.from(context).inflate(R.layout.lv_check, null);
            viewHored.tv_odd = (TextView) view.findViewById(R.id.tv_odd);
            viewHored.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_ures_name = (TextView) view.findViewById(R.id.tv_ures_name);
            viewHored.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            viewHored.tv_money = (TextView) view.findViewById(R.id.tv_money);
            viewHored.tv_serial = (TextView) view.findViewById(R.id.tv_serial);
            viewHored.tv_lei_time = (TextView) view.findViewById(R.id.tv_lei_time);
            viewHored.tv_lei_mode = (TextView) view.findViewById(R.id.tv_lei_mode);
            viewHored.tv_lei_name = (TextView) view.findViewById(R.id.tv_lei_name);
            viewHored.tv_zong = (TextView) view.findViewById(R.id.tv_zong);
            viewHored.tv_shiji = (TextView) view.findViewById(R.id.tv_shiji);
            viewHored.tv_zhaoling = (TextView) view.findViewById(R.id.tv_zhaoling);
            viewHored.tv_beizhu = (TextView) view.findViewById(R.id.tv_beizhu);
            viewHored.lv_check = (MylistView) view.findViewById(R.id.lv_check);
            viewHored.ll_itme = (LinearLayout) view.findViewById(R.id.ll_itme);
            viewHored.ll_xiangcitem = (LinearLayout) view.findViewById(R.id.ll_xiangcitem);
            viewHored.but_refund = (Button) view.findViewById(R.id.but_refund);
            view.setTag(viewHored);
        }
        if (adats.get(i).getPayment() != null) {
            if (adats.get(i).getPayment().equals("cash")) {
                viewHored.tv_odd.setText(adats.get(i).getCash_id());
                viewHored.tv_time.setText(TimeZoneUtil.getTime1((1000 * Long.valueOf(adats.get(i).getCash_time()))));
                viewHored.tv_ures_name.setText(adats.get(i).getSeller_cash_name());
                viewHored.tv_mode.setText("现金支付");
                viewHored.tv_money.setText(StringUtils.stringpointtwo(adats.get(i).getAmount_receivable()));
                viewHored.tv_serial.setText(adats.get(i).getCash_id());
                viewHored.tv_lei_time.setText(TimeZoneUtil.getTime1((1000 * Long.valueOf(adats.get(i).getCash_time()))));
                viewHored.tv_lei_mode.setText("现金支付");
                viewHored.tv_lei_name.setText(adats.get(i).getSeller_cash_name());
                viewHored.tv_zong.setText(StringUtils.stringpointtwo(adats.get(i).getAmount_receivable()));
                viewHored.tv_shiji.setText(StringUtils.stringpointtwo(adats.get(i).getReceive_amount()));
                viewHored.tv_zhaoling.setText(StringUtils.stringpointtwo(adats.get(i).getAdd_change()));
                viewHored.tv_beizhu.setText(adats.get(i).getMark_text());
            }

            if (adats.get(i).getPayment().equals("micro")) {
                viewHored.tv_odd.setText(adats.get(i).getOrder_id());
                viewHored.tv_time.setText(TimeZoneUtil.getTime1((1000 * Long.valueOf(adats.get(i).getCreatetime()))));
                viewHored.tv_ures_name.setText(adats.get(i).getSeller_name());
                viewHored.tv_mode.setText("移动支付");
                viewHored.tv_money.setText(StringUtils.stringpointtwo(adats.get(i).getFinal_amount()));
                viewHored.tv_serial.setText(adats.get(i).getOrder_id());
                viewHored.tv_lei_time.setText(TimeZoneUtil.getTime1((1000 * Long.valueOf(adats.get(i).getCreatetime()))));
                viewHored.tv_lei_mode.setText("移动支付");
                viewHored.tv_lei_name.setText(adats.get(i).getSeller_name());
                viewHored.tv_zong.setText(StringUtils.stringpointtwo(adats.get(i).getFinal_amount()));
                viewHored.tv_shiji.setText(StringUtils.stringpointtwo(adats.get(i).getFinal_amount()));
                viewHored.tv_zhaoling.setText("0");
                viewHored.tv_beizhu.setText("无");
            }
        }
        if (iszhankai.get(i)){
            viewHored.ll_xiangcitem.setVisibility(View.VISIBLE);
        }else {
            viewHored.ll_xiangcitem.setVisibility(View.GONE);
        }
        viewHored.ll_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iszhangkai) {
                    final String order_id;
                    if (adats.get(i).getPayment().equals("micro")) {
                        j=0;
                        order_id = adats.get(i).getOrder_id();
                    } else {
                        j=1;
                        order_id = adats.get(i).getCash_id();
                    }
                    viewHored.ll_xiangcitem.setVisibility(View.VISIBLE);
                    OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                            .tag(context)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.DEFAULT)
                            .params("order_id",order_id)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d("print", "shuju" + s);
                                    try {
                                        check_List.clear();
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONObject jo1 = jsonObject.getJSONObject("response");
                                        String status=jo1.getString("status");
                                        if (status.equals("200")) {
                                            JSONArray ja1 = jo1.getJSONArray("data");
                                            for (int i = 0; i < ja1.length(); i++) {
                                                Check_tuihuo check_tuihuo = new Check_tuihuo();
                                                JSONObject jo2 = ja1.getJSONObject(i);
                                                check_tuihuo.setItem_id(jo2.getString("item_id"));
                                                check_tuihuo.setUnms(jo2.getString("nums"));
                                                check_tuihuo.setShip_nums(jo2.getString("ship_nums"));
                                                check_tuihuo.setPrice(jo2.getString("price"));
                                                check_tuihuo.setGoods_id(jo2.getString("goods_id"));
                                                check_tuihuo.setOrder_id(order_id + "");
                                                check_tuihuo.setSeller_name(jo2.getString("name"));
                                                int in = 0;
                                                if (check_List.size() > 0) {
                                                    aa:
                                                    for (int k = 0; k < check_List.size(); k++) {
                                                        if (check_List.get(k).getGoods_id().equals(check_tuihuo.getGoods_id())) {
                                                            in = in + (k + 1);
                                                            check_List.get(k).setUnms((Integer.parseInt(check_tuihuo.getUnms()) + 1) + "");
                                                            break aa;
                                                        }
                                                    }
                                                    if (in == 0) {
                                                        check_List.add(check_tuihuo);
                                                    }
                                                } else {

                                                    if (check_List.size() > 0) {
                                                        check_List.add(check_List.size() - 1, check_tuihuo);
                                                    } else {
                                                        check_List.add(0, check_tuihuo);
                                                    }
                                                }
                                            }
                                        }
                                        adapter.getAdats(check_List);
                                        adapter.setOntuihuoonclick(Check_Adapter.this);
                                        viewHored.lv_check.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

//                    全部退款
                    viewHored.but_refund.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!SharedUtil.getString("type").equals("4")) {
                                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                                dialog.show();
                                Window window = dialog.getWindow();
                                window.setContentView(R.layout.dialog_but_refund);
                                Button but_cancel = (Button) window.findViewById(R.id.but_cancel);
                                but_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
//                           全部退货
                                Button but_confirm = (Button) window.findViewById(R.id.but_confirm);
                                but_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
                                        for (int i = 0; i < check_List.size(); i++) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("name", check_List.get(i).getSeller_name());
                                            map.put("nums", check_List.get(i).getUnms());
                                            map.put("item_id",check_List.get(i).getItem_id());
                                            map.put("price", check_List.get(i).getPrice());
                                            map.put("sum", TlossUtils.mul(Double.parseDouble(check_List.get(i).getPrice()) , Double.parseDouble(check_List.get(i).getUnms().toString()))+"");
                                            map.put("operator", SharedUtil.getString("operator_id"));
                                            map.put("goods_id", check_List.get(i).getItem_id());
                                            mapList.add(map);
                                        }
                                        Gson gson = new Gson();
                                        String str = gson.toJson(mapList);


                                        OkGo.post(SysUtils.getSellerServiceUrl("refund"))
                                                .tag(context)
                                                .cacheKey("cacheKey")
                                                .cacheMode(CacheMode.DEFAULT)
                                                .params("type", 1)
                                                .params("order_id", order_id)
                                                .params("items", str)
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        Log.d("print", "反结账" + s);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(s);
                                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                                            JSONObject jo2 = jo1.getJSONObject("data");
                                                            String usersname = jo2.getString("usersname");
                                                            String memo = jo2.getString("memo");
                                                            String order_id1 = jo2.getString("order_id");
                                                            String time = jo2.getString("time");
                                                            list_refund.clear();
                                                            JSONArray ja1 = jo2.getJSONArray("name_price_nums");
                                                            Float heji = 0f;
                                                            for (int j = 0; j < ja1.length(); j++) {
                                                                Refund_entty refund_entty = new Refund_entty();
                                                                JSONObject jo3 = ja1.getJSONObject(j);
                                                                refund_entty.setName(jo3.getString("name"));
                                                                String nums = jo3.getString("nums");
                                                                refund_entty.setNums(nums);
                                                                String price = jo3.getString("price");
                                                                refund_entty.setPrice(price);
                                                                heji += (Float.parseFloat(nums) * Float.parseFloat(price));
                                                                list_refund.add(refund_entty);
                                                                Toast.makeText(context, "退货成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                            PrintUtil printUtil1 = new PrintUtil(context);

                                                            printUtil1.openButtonClicked();
                                                            printUtil1.PrintString(StringUtils.stringpointtwo(heji + ""), "-" + StringUtils.stringpointtwo(heji + ""), "0", order_id1, time, 0, list_refund);
                                                            dialog.dismiss();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                });
                            } else {
                                Toast.makeText(context, "你没有该权限", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    iszhangkai = true;
                    for (int j=0;j<iszhankai.size();j++){
                        if (j==i){
                            iszhankai.set(i,true);
                        }else {
                            iszhankai.set(j,false);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    viewHored.ll_xiangcitem.setVisibility(View.GONE);
                    iszhangkai = false;
                    iszhankai.set(i,false);
                }


//                viewHored.lv_check.setAdapter(adapter);
            }
        });


        return view;
    }

    @Override
    public void itmeonclick(int i) {

        if (!SharedUtil.getString("type").equals("4")) {

            showDialog(i);
        } else {
            Toast.makeText(context, "你没有该权限", Toast.LENGTH_SHORT).show();
        }

    }

    class ViewHored {
        TextView tv_odd, tv_time, tv_ures_name, tv_mode, tv_money;
        TextView tv_serial, tv_lei_time, tv_lei_mode, tv_lei_name, tv_zong, tv_shiji, tv_zhaoling, tv_beizhu;
        MylistView lv_check;
        LinearLayout ll_itme, ll_xiangcitem;
        Button but_refund;
    }
    public void showDialog(final int i) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("退货详情");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.refund_layout);
        final TextView tv_nums = (TextView) window.findViewById(R.id.tv_nums);
        ImageView im_reductionof = (ImageView) window.findViewById(R.id.im_reductionof);
        ImageView im_add = (ImageView) window.findViewById(R.id.im_add);
        final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);
        tv_nums.setText(check_List.get(i).getUnms());
        im_reductionof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tv_nums.getText().toString()) > 1) {
                    int j = Integer.valueOf(tv_nums.getText().toString()) - 1;
                    tv_nums.setText(j + "");
                }
            }
        });
        im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(tv_nums.getText().toString()) < Integer.valueOf(check_List.get(i).getUnms())) {
                    int j = Integer.valueOf(tv_nums.getText().toString()) + 1;
                    tv_nums.setText(j + "");
                }
            }
        });
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_describe.getText().toString().equals("")) {
                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                    for (int k = 0; k < 1; k++) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", check_List.get(i).getSeller_name());
                        map.put("nums", tv_nums.getText().toString());
                        map.put("price", StringUtils.stringpointtwo(Float.valueOf(check_List.get(i).getPrice()) + ""));
                        map.put("sum", TlossUtils.mul(Double.parseDouble(check_List.get(i).getPrice()) , Double.parseDouble(tv_nums.getText().toString()))+"");
                        map.put("item_id", check_List.get(i).getItem_id());
                        map.put("goods_id", check_List.get(i).getGoods_id());
                        list.add(map);
                        if (j == 0) {
                            payment = "micro";
                        }
                        if (j == 1) {
                            payment = "cash";
                        }
                    }
                    Gson gson = new Gson();
                    String str = gson.toJson(list);

                    OkGo.post(SysUtils.getSellerServiceUrl("refund"))
                            .tag(context)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.DEFAULT)
                            .params("operator", SharedUtil.getString("operator_id"))
                            .params("order_id", check_List.get(i).getOrder_id())
                            .params("nums", tv_nums.getText().toString())
                            .params("memo", ed_describe.getText().toString())
                            .params("type", 0)
                            .params("payment", payment)
                            .params("items", str)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d("print", "退货的" + s);
                                    try {
                                        list_refund.clear();
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONObject jo1 = jsonObject.getJSONObject("response");
                                        String status = jo1.getString("status");
                                        if (status.equals("200")) {
                                            Refund_entty refund_entty = new Refund_entty();
                                            JSONObject jo2 = jo1.getJSONObject("data");
                                            String usersname = jo2.getString("usersname");
                                            refund_entty.setUsersname(usersname);
                                            String name = jo2.getString("name");
                                            refund_entty.setName(name);
                                            String price = jo2.getString("price");
                                            refund_entty.setPrice(price);
                                            String nums = jo2.getString("nums");
                                            refund_entty.setNums(nums);
                                            String order_id1 = jo2.getString("order_id");
                                            refund_entty.setOrder_id(order_id1);
                                            String time = jo2.getString("time");
                                            refund_entty.setTime(time);
                                            Log.d("print", "usersname" + usersname + "name" + name + "price" + price
                                                    + "nums" + nums + "order_id" + order_id1 + "time" + time);
                                            list_refund.add(refund_entty);
                                            PrintUtil printUtil1 = new PrintUtil(context);
//
                                            Float heji = (Float.parseFloat(nums) * Float.parseFloat(price));
                                            printUtil1.openButtonClicked();
                                            printUtil1.PrintString(StringUtils.stringpointtwo(heji + ""), "-" + StringUtils.stringpointtwo(heji + ""), "0", order_id1, time, 0, list_refund);
                                            //判断软键盘是否显示
//                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                                            Toast.makeText(context, "退货成功", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                }
            }
        });


    }
}
