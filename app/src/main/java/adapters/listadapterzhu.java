package adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.ListEntty;
import Entty.ShuliangEntty;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.TlossUtils;
import retail.yzx.com.kz.MainActivity;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;

/**
 * Created by admin on 2017/3/17.
 */
public class listadapterzhu extends BaseAdapter {
    public List<Commodity> adats;
    private Context context;
    boolean isbianhua = false;
    public SQLiteDatabase sqLiteDatabase;
    public SqliteHelper sqliteHelper;
    public List<ListEntty> Entty;
    public boolean is = false;
    public List<ShuliangEntty> shuliangEntties;
    public Onremove onremove;
    public boolean version=true;
    //判断单选
    public boolean danxuan = false;

    //判断是否是零售
    public boolean type_member=false;

//    private  ViewHoed viewHoed=null;


    public listadapterzhu setOnremove(Onremove onremove) {
        this.onremove = onremove;
        return listadapterzhu.this;
    }

    public void setversion(boolean version){
        this.version=version;
        notifyDataSetChanged();
    }

    public listadapterzhu(Context context) {
//        if (context!=null){
////            IntentFilter intentFilter= new IntentFilter();
////            intentFilter.addAction("com.yzx.kuaijieadd");
////            context.registerReceiver(broadcastReceiver, intentFilter);
//
//
//            IntentFilter intentFilter1= new IntentFilter();
//            intentFilter1.addAction("com.yzx.checked");
//            context.registerReceiver(broadcastReceiver, intentFilter1);
//
//            IntentFilter intentFilter2= new IntentFilter();
//            intentFilter2.addAction("com.yzx.clear");
//            context.registerReceiver(broadcastReceiver, intentFilter2);
//        }
        adats = new ArrayList<>();
        this.sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        this.Entty = new ArrayList<>();
        this.shuliangEntties = new ArrayList<>();
        this.context = context;
    }

    public void getEntty(List<ShuliangEntty> shuliangEntties) {
        this.shuliangEntties = shuliangEntties;
        notifyDataSetChanged();
    }

    public void getadats(List<Commodity> adats) {
//      Collections.reverse(adats);
        this.adats = adats;
//        for (int j = 0; j < adats.size(); j++) {
//            ShuliangEntty shuliangEntty = new ShuliangEntty();
//            shuliangEntty.setNumber(1);
//            shuliangEntties.add(shuliangEntty);
//        }
        for (int j = 0; j < adats.size(); j++) {
            ListEntty listEntty = new ListEntty();
            listEntty.setChecked(false);
            Entty.add(listEntty);
        }
        notifyDataSetChanged();
    }

    public void setType(boolean type_member){
        this.type_member=type_member;
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
    public View getView(final int i, View v, ViewGroup viewGroup) {
//        if(viewGroup.getChildCount() == i);
            final ViewHoed viewHoed;
        if (v != null) {
            viewHoed = (ViewHoed) v.getTag();
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.listviewzhu, null);
            viewHoed = new ViewHoed();
            viewHoed.ll_listitme = (LinearLayout) v.findViewById(R.id.ll_listitme);
            viewHoed.im_reductionof = (ImageView) v.findViewById(R.id.im_reductionof);
            viewHoed.im_add = (ImageView) v.findViewById(R.id.im_add);
            viewHoed.tv_xuhao = (TextView) v.findViewById(R.id.tv_xuhao);
            viewHoed.tv_caimane = (TextView) v.findViewById(R.id.tv_caimane);
            viewHoed.tv_jiage = (TextView) v.findViewById(R.id.tv_jiage);
            viewHoed.tv_xiaoji = (TextView) v.findViewById(R.id.tv_xiaoji);
            viewHoed.tv_jinjia = (TextView) v.findViewById(R.id.tv_jinjia);
            viewHoed.tv_lirun = (TextView) v.findViewById(R.id.tv_lirun);
            viewHoed.tv_lirunli= (TextView) v.findViewById(R.id.tv_lirunli);
            viewHoed.ed_shuliang = (TextView) v.findViewById(R.id.ed_shuliang);
            viewHoed.tv_store = (TextView) v.findViewById(R.id.tv_store);
            viewHoed.tv_lv = (TextView) v.findViewById(R.id.tv_lv);
            v.setTag(viewHoed);
        }
        if (!Entty.get(i).getChecked()) {
            viewHoed.im_reductionof.setVisibility(View.GONE);
            viewHoed.im_add.setVisibility(View.GONE);
        } else {
            viewHoed.im_reductionof.setVisibility(View.VISIBLE);
            viewHoed.im_add.setVisibility(View.VISIBLE);
        }

        if (version){
            viewHoed.tv_lirunli.setVisibility(View.VISIBLE);
            viewHoed.tv_lirun.setVisibility(View.VISIBLE);
            viewHoed.tv_jinjia.setVisibility(View.VISIBLE);
            viewHoed.tv_store.setVisibility(View.VISIBLE);
        }else {
            viewHoed.tv_lirunli.setVisibility(View.GONE);
            viewHoed.tv_lirun.setVisibility(View.GONE);
            viewHoed.tv_jinjia.setVisibility(View.GONE);
            viewHoed.tv_store.setVisibility(View.GONE);
        }
        viewHoed.tv_lirunli.setVisibility(View.GONE);
        viewHoed.tv_lirun.setVisibility(View.GONE);
        viewHoed.tv_jinjia.setVisibility(View.GONE);
        viewHoed.tv_store.setVisibility(View.GONE);

        switch (getItemViewType(i)) {
            case 0:
                viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
                viewHoed.ed_shuliang.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHoed.ed_shuliang.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
        }

//        加号的监听
        viewHoed.im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.issucceed) {
                    if (adats.get(i).getName().equals("会员充值")) {

                    } else {
                        float shuliang = Float.valueOf(viewHoed.ed_shuliang.getText().toString());
                        ListEntty listEntty = new ListEntty();
                        shuliang++;
                        shuliangEntties.get(i).setNumber(shuliang);
                        if (!viewHoed.ed_shuliang.getText().toString().equals("")) {
                            viewHoed.ed_shuliang.setText(shuliang + "");
                        } else {
                            viewHoed.ed_shuliang.setText(1 + "");
                        }

                        Intent intent = new Intent("com.yzx.add");
                        intent.putExtra("add", i + "");
                        intent.putExtra("commodity", adats.get(i));
                        context.sendBroadcast(intent);
//                listEntty.setIndex(i+"");
//                listEntty.setNumber(shuliang);
//                Entty.add(listEntty);
                    }
                }
            }
        });

//        减号的监听
        viewHoed.im_reductionof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.issucceed) {
                    if (adats.get(i).getName().equals("会员充值")) {

                    } else {
                    float shuliang = Float.valueOf(viewHoed.ed_shuliang.getText().toString());
                    if (shuliang > 1) {
                        shuliang--;
                        shuliangEntties.get(i).setNumber(shuliang);
                        viewHoed.ed_shuliang.setText(shuliang + "");
//                        Float.parseFloat(viewHoed.tv_xiaoji.getText().toString());
//                    adats.get(i).setPrice(shuliang+"");
                        Intent intent = new Intent("com.yzx.reductionof");
                        intent.putExtra("reductionof", i + "");
                        intent.putExtra("commodity", adats.get(i));
                        context.sendBroadcast(intent);
                    } else {
                        Toast.makeText(context, "不能再减了", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            }
        });


        //itme的点击事件
        viewHoed.ll_listitme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < adats.size(); j++) {
                    if (Entty.get(j).getChecked()) {
                        danxuan = true;
                    }
                    if (danxuan) {
                        Entty.get(j).setChecked(false);
                        viewHoed.im_add.setVisibility(View.GONE);
                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        Entty.get(i).setChecked(true);
                        notifyDataSetChanged();
                        danxuan = false;
                    }
                }

                if (Entty.get(i).getChecked()) {
                    int buk = i % 2 == 0 ? 0 : 1;
                    if (buk == 0) {
                        viewHoed.im_add.setVisibility(View.GONE);
                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
                        Entty.get(i).setChecked(false);
                    } else {
                        viewHoed.im_add.setVisibility(View.GONE);
                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
                        Entty.get(i).setChecked(false);
                    }
                } else {
                    viewHoed.im_add.setVisibility(View.VISIBLE);
                    viewHoed.im_reductionof.setVisibility(View.VISIBLE);
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#d4d4d4"));
                    Entty.get(i).setChecked(true);
                    onremove.setOnClickListener(Entty, i, viewHoed.im_add, viewHoed.im_reductionof);
                }
            }
        });

        if (Entty.size() != 0) {
            if (adats.size() == Entty.size()) {
                if (Entty.get(i).getChecked()) {
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#d4d4d4"));
                    viewHoed.im_add.setVisibility(View.VISIBLE);
                    viewHoed.im_reductionof.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                } else {
                    viewHoed.im_add.setVisibility(View.GONE);
                    viewHoed.im_reductionof.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            }
        }
        if (!adats.get(i).getStore().equals("null")){
            viewHoed.tv_store.setText(adats.get(i).getStore());
        }

        viewHoed.tv_caimane.setText(adats.get(i).getName());

//        if (SharedUtil.getfalseBoolean("sw_member_price")) {
            if (adats.get(i).getIs_special_offer()!=null) {
                if (adats.get(i).getIs_special_offer().equals("no")) {
                if (adats.get(i).getType() != null && !adats.get(i).getType().equals("0")) {
                    if (type_member){
                    if (adats.get(i).getCustom_member_price() != null && !adats.get(i).getCustom_member_price().equals("")&& !adats.get(i).getCustom_member_price().equals("null")) {
                        Log.d("print", "打印会员价格" + adats.get(i).getCustom_member_price());
                        viewHoed.tv_jiage.setText(StringUtils.getStrings(adats.get(i).getCustom_member_price(), ",")[Integer.parseInt(adats.get(i).getType()) - 1]);
                        viewHoed.tv_lv.setText("V"+Integer.parseInt(adats.get(i).getType()));
                        viewHoed.tv_lv.setVisibility(View.VISIBLE);
                    } else {
                        viewHoed.tv_lv.setVisibility(View.GONE);
                        viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getMember_price()) + "");
                    }
                    }else {
                        viewHoed.tv_lv.setVisibility(View.GONE);
                        viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
                    }
                } else {
                    viewHoed.tv_lv.setVisibility(View.GONE);
                    viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
                }
            }else {
                    viewHoed.tv_lv.setVisibility(View.GONE);
                    viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
                }
        }else {
                viewHoed.tv_lv.setVisibility(View.GONE);
                viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
            }
//        }else {
//            viewHoed.tv_lv.setVisibility(View.GONE);
//            viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
//        }
//        if (type_member.equals("")){
//            viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
//        }else{
////            viewHoed.tv_jiage.setText(adats.get(i).getMember_price() + "");
//            viewHoed.tv_jiage.setText(StringUtils.getStrings(adats.get(i).getCustom_member_price(),",")[Integer.parseInt(type_member)]);
//        }
//        for (int j=0;j<adats.size();j++){
//            viewHoed.ed_shuliang.setText(Entty.get(i).getNumber()+"");
//        }

        if (shuliangEntties.size()>i){
            viewHoed.ed_shuliang.setText(shuliangEntties.get(i).getNumber() + "");
        }else {
            viewHoed.ed_shuliang.setText(1+"");
        }

        onremove.getEntty(shuliangEntties);
//                    viewHoed.ed_shuliang.setText(adats.get(i).getPrice() + "");

        if (!viewHoed.ed_shuliang.getText().toString().equals("")) {
//            if (SharedUtil.getfalseBoolean("sw_member_price")) {
                if (adats.get(i).getIs_special_offer()!=null){
                    if (adats.get(i).getIs_special_offer().equals("no")) {
                        if (type_member){
                        if (adats.get(i).getType().equals("0")) {
                            viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                            viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                            viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                        } else {
                            boolean is = false;
                            if (adats.get(i).getCustom_member_price() != null && !adats.get(i).getCustom_member_price().equals("")&& !adats.get(i).getCustom_member_price().equals("null")) {
                                if (Float.parseFloat(adats.get(i).getPrice()) == Float.parseFloat(StringUtils.getStrings(adats.get(i).getCustom_member_price(), ",")[Integer.parseInt(adats.get(i).getType()) - 1])) {
                                    is = true;
                                } else {
                                    is = false;
                                }
                                viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(adats.get(i).getCustom_member_price(), ",")[Integer.parseInt(adats.get(i).getType()) - 1]), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                            } else {
                                if (Float.parseFloat(adats.get(i).getMember_price()) == Float.parseFloat(adats.get(i).getPrice())) {
                                    is = true;
                                } else {
                                    is = false;
                                }
                                viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getMember_price()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                            }
                            if (is) {
                                viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                                viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                            } else {
                                viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#ff0000"));
                                viewHoed.tv_jiage.setTextColor(Color.parseColor("#ff0000"));
                            }
//                viewHoed.tv_xiaoji.setText((float)(TlossUtils.mul(Double.parseDouble(adats.get(i).getMember_price()) , Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
//                viewHoed.tv_xiaoji.setText((float)(TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(adats.get(i).getCustom_member_price(),",")[Integer.parseInt(type_member)]) , Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                        }
                    }else {
                            viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                            viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                            viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                        }
                    }else {
                        viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                        viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                        viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                    }
            }else {
                    viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                    viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                    viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
                }
//        }else {
//                viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
//                viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
//                viewHoed.tv_xiaoji.setText((float) (TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()), Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
//            }
        }else {
            if (type_member){
            if (adats.get(i).getType().equals("0")) {
                viewHoed.tv_xiaoji.setText((Float.parseFloat(adats.get(i).getPrice()) * 1) + "");
            } else {
                if (adats.get(i).getCustom_member_price() != null && !adats.get(i).getCustom_member_price().equals("")&& !adats.get(i).getCustom_member_price().equals("null")) {
                    viewHoed.tv_xiaoji.setText((Float.parseFloat(StringUtils.getStrings(adats.get(i).getCustom_member_price(), ",")[Integer.parseInt(adats.get(i).getType())-1]) * 1) + "");
                } else {
                    viewHoed.tv_xiaoji.setText((Float.parseFloat(adats.get(i).getMember_price()) * 1) + "");
                }
            }
        }else {
                viewHoed.tv_xiaoji.setText((Float.parseFloat(adats.get(i).getPrice()) * 1) + "");
            }
        }
        if (!adats.get(i).getCost().equals("null")){
            viewHoed.tv_jinjia.setText(StringUtils.stringpointtwo(Float.parseFloat(adats.get(i).getCost()) + ""));
        }
        viewHoed.tv_xuhao.setText((i + 1) + "");
        if (!adats.get(i).getCost().equals("null")&&!adats.get(i).getPrice().equals("null")){
            viewHoed.tv_lirun.setText(TlossUtils.sub(Double.parseDouble(adats.get(i).getPrice()),Double.parseDouble(adats.get(i).getCost()))+"");
            try {
                if (Float.parseFloat(adats.get(i).getPrice())>0){
                    viewHoed.tv_lirunli.setText((int)(TlossUtils.div(TlossUtils.sub(Double.parseDouble(adats.get(i).getPrice()),Double.parseDouble(adats.get(i).getCost())),Double.parseDouble(adats.get(i).getPrice()),2)*100)+"%");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (SharedUtil.getString("type") != null) {
            if (SharedUtil.getString("type").equals("4")) {
                if (SharedUtil.getString("cost_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("cost_disable"))) {
                        viewHoed.tv_jinjia.setVisibility(View.GONE);
                    }
                }
                if (SharedUtil.getString("store_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("store_disable"))) {
                        viewHoed.tv_store.setVisibility(View.GONE);
                    }
                }
                if (SharedUtil.getString("profit_disable") != null) {
                    if (!Boolean.parseBoolean(SharedUtil.getString("profit_disable"))) {
                        viewHoed.tv_lirun.setVisibility(View.GONE);
                        viewHoed.tv_lirunli.setVisibility(View.GONE);
                    }
                }
            }
        }

        viewHoed.ed_shuliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onremove.setnums(i);
            }
        });
//        float c=Float.parseFloat(viewHoed.tv_jiage.getText().toString()) * Float.parseFloat(viewHoed.ed_shuliang.getText().toString());
//        viewHoed.tv_xiaoji.setText(c + "");

//        viewHoed.im_reductionof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
////                if(Integer.valueOf(finalViewHoed.ed_shuliang.getText().toString())>1){
////                    Toast.makeText(context, finalViewHoed.ed_shuliang.getText().toString(),Toast.LENGTH_SHORT).show();
////                    int a=Integer.valueOf(finalViewHoed.ed_shuliang.getText().toString().trim())-1;
////                    Toast.makeText(context,a+"",Toast.LENGTH_SHORT).show();
////                    finalViewHoed.ed_shuliang.setText("sdsada");
////                    finalViewHoed.ed_shuliang.setText(a+"");
////                }else {
////                    Toast.makeText(context,"不可以在减了",Toast.LENGTH_SHORT).show();
////                }
//            }
//        });

//        viewHoed.im_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                float j=Float.parseFloat(viewHoed.ed_shuliang.getText().toString());
//                j++;
//                float z=Float.parseFloat(viewHoed.tv_jiage.getText().toString()) * Float.parseFloat(viewHoed.ed_shuliang.getText().toString());
//                viewHoed.ed_shuliang.setText((int)j+"");
//                viewHoed.tv_xiaoji.setText(z+"");
//            }
//        });
//        viewHoed.im_reductionof.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Float.parseFloat(viewHoed.tv_jiage.getText().toString());
//
//                if(Float.parseFloat(viewHoed.ed_shuliang.getText().toString())>1) {
//                   float j=Float.parseFloat(viewHoed.ed_shuliang.getText().toString());
//                    j--;
//                    float z=Float.parseFloat(viewHoed.tv_jiage.getText().toString()) * Float.parseFloat(viewHoed.ed_shuliang.getText().toString());
//                    viewHoed.ed_shuliang.setText((int)j+"");
//                    viewHoed.tv_xiaoji.setText(z+"");
//                    isbianhua=true;
//                }else {
//                    Toast.makeText(context,"不能再减了",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        viewHoed.tv_jiage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onremove.setprice(i);
            }
        });

        return v;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    private class ViewHoed {
        LinearLayout ll_listitme;
        ImageView im_reductionof, im_add;
        TextView tv_store,tv_xuhao, tv_caimane, tv_jiage, tv_xiaoji, tv_jinjia, tv_lirun,tv_lirunli,tv_lv;
        TextView ed_shuliang;
    }

    //选中的接口回掉
    public interface Onremove {
        void setOnClickListener(List<ListEntty> entties, int i, ImageView im, ImageView iv);

        void getEntty(List<ShuliangEntty> entty);

        void setnums(int i);

        void setprice(int i);

    }


//    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            context.unregisterReceiver(this);
//            String action = intent.getAction();
////            if (action.equals("com.yzx.kuaijieadd")){
////                Bundle bundle = intent.getExtras();
////                int index=bundle.getInt("index");
////                int number=bundle.getInt("number");
////                Log.d("print","数量是"+number);
////                Log.d("print","下标是"+(index-1));
////                shuliangEntties.get(index-1).setNumber(number);
////                onremove.getEntty(shuliangEntties);
////            }
//
////            if (action.equals("com.yzx.checked")){
////                Bundle bundle = intent.getExtras();
////                int k=bundle.getInt("index");
////                if (k!=-1&&Entty.size()>k){
////                    Entty.get(k).setChecked(false);
////                    }
////                notifyDataSetChanged();
////            }
//
////            if (action.equals("com.yzx.clear")){
////                if (Entty.size()>0){
////                    for (int j=0;j<Entty.size();j++){
////                        Entty.get(j).setChecked(false);
////                    }
////                }
////
////
////            }
//        }
//    };

}
