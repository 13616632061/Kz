package adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Order_Entty;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;
import shujudb.Sqlite_Entity;

/**
 * Created by admin on 2017/4/21.
 */
public class Order_adapter extends BaseAdapter {

    public List<Order_Entty.ResponseBean.DataBean> adats;
    public Context context;
    public Onitmeclick onitmeclick;
    public int type=0;
    public boolean credit;


    public Order_adapter setOnitemclick(Onitmeclick onitmeclick){
        this.onitmeclick=onitmeclick;
        return Order_adapter.this;
    }


    public Order_adapter(Context context,boolean credit) {
        this.context = context;
        this.credit=credit;
        this.adats=new ArrayList<>();
    }

    public void getadats(List<Order_Entty.ResponseBean.DataBean> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void getType(int type){
        this.type=type;
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
            view = LayoutInflater.from(context).inflate(R.layout.order_itme, null);
            viewHored=new ViewHored();
            viewHored.rl_itme= (RelativeLayout) view.findViewById(R.id.rl_itme);
            viewHored.tv_1= (TextView) view.findViewById(R.id.tv_1);
            viewHored.tv_title= (TextView) view.findViewById(R.id.tv_title);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.but_order= (Button) view.findViewById(R.id.but_order);
            viewHored.ll_tv_mark_text= (LinearLayout) view.findViewById(R.id.ll_tv_mark_text);
            viewHored.tv_mark_text= (TextView) view.findViewById(R.id.tv_mark_text);
            viewHored.but_print= (Button) view.findViewById(R.id.but_print);
            viewHored.but_print_text= (Button) view.findViewById(R.id.but_print_text);
            viewHored.but_cancel= (Button) view.findViewById(R.id.but_cancel);
            viewHored.but_add= (Button) view.findViewById(R.id.but_add);
            view.setTag(viewHored);
        }

            if (type==0){
                viewHored.but_order.setText("取单");
            }else {
                viewHored.but_order.setText("解挂");
            }

        if (credit){
            viewHored.tv_title.setText("赊账");
            viewHored.tv_mark_text.setText(adats.get(i).getMark_text());
        }else {
            viewHored.tv_title.setText("挂单");
            viewHored.tv_mark_text.setText(adats.get(i).getMark_text());
        }
        viewHored.tv_1.setText((i+1)+"");
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_time.setText(TimeZoneUtil.getTime1(1000*Long.valueOf(adats.get(i).getCreatetime())));
        viewHored.but_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onitmeclick.setonitmeoncick(i);
            }
        });
        viewHored.but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onitmeclick.setCancel(i);
            }
        });

        viewHored.rl_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onitmeclick.setrlitmeclick(i,viewHored.ll_tv_mark_text);
            }
        });
        viewHored.but_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHored.ll_tv_mark_text.getVisibility()==View.GONE){
                    viewHored.ll_tv_mark_text.setVisibility(View.VISIBLE);
                }else {
                    viewHored.ll_tv_mark_text.setVisibility(View.GONE);
                }

            }
        });
        viewHored.but_print_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onitmeclick.setONprint(i);
            }
        });
        viewHored.but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmrk(viewHored.tv_mark_text,viewHored.tv_mark_text.getText().toString(),adats.get(i).getOrder_id());
            }
        });

        return view;
    }

    public void showmrk(final TextView textView, String text, final String order_id){
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("添加备注");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.refund_layout);
        RelativeLayout Rl_number = (RelativeLayout) window.findViewById(R.id.Rl_number);
        Rl_number.setVisibility(View.GONE);
        RelativeLayout Rl_fangshi= (RelativeLayout) window.findViewById(R.id.Rl_fangshi);
        Rl_fangshi.setVisibility(View.GONE);
        TextView tv_wenti= (TextView) window.findViewById(R.id.tv_wenti);
        tv_wenti.setText("备注编辑");
        final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
        ed_describe.setText(text);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sqlite_Entity sqlite_entity=new Sqlite_Entity(context);
                sqlite_entity.modifyguadan("remarks",ed_describe.getText().toString(),"hangup",order_id);
                textView.setText(ed_describe.getText().toString());
                dialog.dismiss();
            }
        });
    }


    class ViewHored {
        RelativeLayout rl_itme;
        TextView tv_title, tv_name, tv_time,tv_1,tv_mark_text;
        Button but_order,but_print,but_print_text,but_cancel,but_add;
        LinearLayout ll_tv_mark_text;
    }

    public interface Onitmeclick{
        void setrlitmeclick(int i, View view);
        void setonitmeoncick(int i);
        void setONprint(int i);
        void setCancel(int i);
    }


}
