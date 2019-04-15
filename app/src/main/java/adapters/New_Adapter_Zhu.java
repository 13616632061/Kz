package adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import Entty.New_NumberEntty;
import Entty.ShuliangEntty;
import Utils.StringUtils;
import Utils.TlossUtils;
import retail.yzx.com.kz.MainActivity;
import retail.yzx.com.kz.R;
import shujudb.SqliteHelper;

/**
 * Created by admin on 2018/8/16.
 */

public class New_Adapter_Zhu extends BaseAdapter {
    public List<Commodity> adats;
    private Context context;
    boolean isbianhua = false;
    public SQLiteDatabase sqLiteDatabase;
    public SqliteHelper sqliteHelper;
    public List<New_NumberEntty> new_numberEntties;
    public boolean is = false;
    public Onremove onremove;
    public boolean version=true;
    //判断单选
    public boolean danxuan = false;

    //判断是否是零售
    public String type_member="0";



    public New_Adapter_Zhu setOnremove(Onremove onremove) {
        this.onremove = onremove;
        return New_Adapter_Zhu.this;
    }

    public void setversion(boolean version){
        this.version=version;
        notifyDataSetChanged();
    }

    public New_Adapter_Zhu(Context context) {
        adats = new ArrayList<>();
        this.sqliteHelper = new SqliteHelper(context);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
        this.new_numberEntties = new ArrayList<>();
        this.context = context;
    }

    /**
     * 设置商品
     * @param adats
     */
    public void getadats(List<Commodity> adats) {
        this.adats = adats;
        notifyDataSetChanged();
    }

    /**
     * 设置数量数组以及是否选中状态
     * @param new_numberEntties
     */
    public void getnumber(List<New_NumberEntty> new_numberEntties){
        this.new_numberEntties=new_numberEntties;
        notifyDataSetChanged();
    }

    public void setType(String type_member){
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
            v.setTag(viewHoed);
        }
//        if (!new_numberEntties.get(i).isChecked()) {
//            viewHoed.im_reductionof.setVisibility(View.GONE);
//            viewHoed.im_add.setVisibility(View.GONE);
//        } else {
//            viewHoed.im_reductionof.setVisibility(View.VISIBLE);
//            viewHoed.im_add.setVisibility(View.VISIBLE);
//        }

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
                if (!new_numberEntties.get(i).isChecked()) {
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    viewHoed.ed_shuliang.setBackgroundColor(Color.parseColor("#ffffff"));
                }else {
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#d4d4d4"));
                    new_numberEntties.get(i).setChecked(true);
                    onremove.setOnClickListener(i);
                }
                break;
            case 1:
                if (!new_numberEntties.get(i).isChecked()) {
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
                    viewHoed.ed_shuliang.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }else {
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#d4d4d4"));
                    new_numberEntties.get(i).setChecked(true);
                    onremove.setOnClickListener(i);
                }
                break;
        }

        viewHoed.ed_shuliang.setText(new_numberEntties.get(i).getNumber()+"");

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
                        new_numberEntties.get(i).setNumber(shuliang);
                        if (!viewHoed.ed_shuliang.getText().toString().equals("")) {
                            viewHoed.ed_shuliang.setText(shuliang + "");
                        } else {
                            viewHoed.ed_shuliang.setText(1 + "");
                        }
                        New_Adapter_Zhu.this.notifyDataSetChanged();
                        Intent intent = new Intent("com.yzx.add");
                        intent.putExtra("add", i + "");
                        intent.putExtra("commodity", adats.get(i));
                        context.sendBroadcast(intent);
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
                            new_numberEntties.get(i).setNumber(shuliang);
                            viewHoed.ed_shuliang.setText(shuliang + "");
                            New_Adapter_Zhu.this.notifyDataSetChanged();
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
                    if (new_numberEntties.get(j).isChecked()) {
                        danxuan = true;
                    }
                    if (danxuan) {
                        new_numberEntties.get(j).setChecked(false);
//                        viewHoed.im_add.setVisibility(View.GONE);
//                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        new_numberEntties.get(i).setChecked(true);
                        notifyDataSetChanged();
                        danxuan = false;
                    }
                }

                if (new_numberEntties.get(i).isChecked()) {
                    int buk = i % 2 == 0 ? 0 : 1;
                    if (buk == 0) {
//                        viewHoed.im_add.setVisibility(View.GONE);
//                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#f1f1f1"));
                        new_numberEntties.get(i).setChecked(false);
                    } else {

//                        viewHoed.im_add.setVisibility(View.GONE);
//                        viewHoed.im_reductionof.setVisibility(View.GONE);
                        viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#ffffff"));
                        new_numberEntties.get(i).setChecked(false);

                    }
                } else {
//                    viewHoed.im_add.setVisibility(View.VISIBLE);
//                    viewHoed.im_reductionof.setVisibility(View.VISIBLE);
                    viewHoed.ll_listitme.setBackgroundColor(Color.parseColor("#d4d4d4"));
                    new_numberEntties.get(i).setChecked(true);
                    onremove.setOnClickListener(i);

                }
            }
        });

        if (!adats.get(i).getStore().equals("null")){
            viewHoed.tv_store.setText(adats.get(i).getStore());
        }

        viewHoed.tv_caimane.setText(adats.get(i).getName());
        if (type_member.equals("0")){
            viewHoed.tv_jiage.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
        }else {
            viewHoed.tv_jiage.setText(adats.get(i).getMember_price() + "");
        }


        if (!viewHoed.ed_shuliang.getText().toString().equals("")){
            if (type_member.equals("0")){
                viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                viewHoed.tv_xiaoji.setText((float)(TlossUtils.mul(Double.parseDouble(adats.get(i).getPrice()) , Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
            }else {
                if (Float.parseFloat(adats.get(i).getPrice())==Float.parseFloat(adats.get(i).getMember_price())){
                    viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#212121"));
                    viewHoed.tv_jiage.setTextColor(Color.parseColor("#212121"));
                }else {
                    viewHoed.tv_xiaoji.setTextColor(Color.parseColor("#ff0000"));
                    viewHoed.tv_jiage.setTextColor(Color.parseColor("#ff0000"));
                }
                viewHoed.tv_xiaoji.setText((float)(TlossUtils.mul(Double.parseDouble(adats.get(i).getMember_price()) , Double.parseDouble(viewHoed.ed_shuliang.getText().toString()))) + "");
            }
        }else {
            if (type_member.equals("0")){
                viewHoed.tv_xiaoji.setText((Float.parseFloat(adats.get(i).getPrice()) * 1) + "");
            }else {
                viewHoed.tv_xiaoji.setText((Float.parseFloat(adats.get(i).getMember_price()) * 1) + "");
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

        viewHoed.ed_shuliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onremove.setnums(i);
            }
        });

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
        TextView tv_store,tv_xuhao, tv_caimane, tv_jiage, tv_xiaoji, tv_jinjia, tv_lirun,tv_lirunli;
        TextView ed_shuliang;
    }

    //选中的接口回掉
    public interface Onremove {
        void setOnClickListener(int i);

        void getEntty(List<ShuliangEntty> entty);

        void setnums(int i);

        void setprice(int i);

    }

}
