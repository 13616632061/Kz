package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Stat_Zong_ENtty;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/2.
 */
public class Statement_adapter extends BaseAdapter {
    public Context context;
    public List<Stat_Zong_ENtty> adats;
    public SetOnclick setOnclick;

    public Statement_adapter setbutonclick(SetOnclick setOnclick) {
        this.setOnclick = setOnclick;
        return Statement_adapter.this;
    }

    public Statement_adapter(Context context) {
        this.context = context;
        this.adats = new ArrayList<>();
    }

    public void getAdats(List<Stat_Zong_ENtty> adats) {
        this.adats = adats;
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
        ViewHored viewHored = null;
        if (view != null) {
            viewHored = (ViewHored) view.getTag();
        } else {
            viewHored = new ViewHored();
            view = LayoutInflater.from(context).inflate(R.layout.lv_statement, null);
            viewHored.tv_xunhao = (TextView) view.findViewById(R.id.tv_xunhao);
            viewHored.tv_danhao = (TextView) view.findViewById(R.id.tv_danhao);
            viewHored.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_ures_name = (TextView) view.findViewById(R.id.tv_ures_name);
            viewHored.tv_payment = (TextView) view.findViewById(R.id.tv_payment);
            viewHored.tv_order = (TextView) view.findViewById(R.id.tv_order);
            viewHored.tv_money = (TextView) view.findViewById(R.id.tv_money);
            viewHored.tv_lirun = (TextView) view.findViewById(R.id.tv_lirun);
            viewHored.but_details = (Button) view.findViewById(R.id.but_details);
            view.setTag(viewHored);
        }
        viewHored.tv_xunhao.setText((1 + i) + "");
        viewHored.tv_danhao.setText(adats.get(i).getId());
        viewHored.tv_time.setText(TimeZoneUtil.getTime1((Long.valueOf(adats.get(i).getTime()) * 1000)));
        if (adats.get(i).getWork_name()!=null){
            if (adats.get(i).getWork_name().equals("null")){
                viewHored.tv_ures_name.setText(SharedUtil.getString("seller_name"));
            }else {
                viewHored.tv_ures_name.setText(adats.get(i).getWork_name());
            }
        }
        if (adats.get(i).getPayment().equals("null")) {
            viewHored.tv_payment.setText("会员支付");
        } else {
            if (!StringUtils.isNumber1(adats.get(i).getPayment())) {
                viewHored.tv_payment.setText("移动支付");
            } else {
                viewHored.tv_payment.setText("现金支付");
            }
        }
//        viewHored.tv_order.setText(adats.get(i).getOrderType());
        viewHored.tv_money.setText(StringUtils.stringpointtwo(adats.get(i).getMoney()));

        if (SharedUtil.getString("type").equals("4")){
            viewHored.tv_lirun.setText("--");
        }else {
            viewHored.tv_lirun.setText(adats.get(i).getLirun());
        }

        viewHored.but_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclick.Setbutonclick(i);
            }
        });


        return view;
    }

    class ViewHored {
        TextView tv_xunhao, tv_danhao, tv_time, tv_ures_name, tv_payment, tv_order, tv_money, tv_lirun;
        Button but_details;
    }

    public interface SetOnclick {
        void Setbutonclick(int i);


    }

}
