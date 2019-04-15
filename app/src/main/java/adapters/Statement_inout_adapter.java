package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import Entty.In_out_boundEntty;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/2.
 */
public class Statement_inout_adapter extends BaseAdapter {
    public Context context;
    public In_out_boundEntty adats;
    public SetOnclick setOnclick;

    public Statement_inout_adapter setbutonclick(SetOnclick setOnclick) {
        this.setOnclick = setOnclick;
        return Statement_inout_adapter.this;
    }

    public Statement_inout_adapter(Context context) {
        this.context = context;
    }

    public void getAdats(In_out_boundEntty adats) {
        this.adats = adats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats.getResponse().getData().getList().size();
    }

    @Override
    public Object getItem(int i) {
        return adats.getResponse().getData().getList().get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.lv_inout_statement, null);
            viewHored.tv_xunhao = (TextView) view.findViewById(R.id.tv_xunhao);
            viewHored.tv_danhao = (TextView) view.findViewById(R.id.tv_danhao);
            viewHored.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_ures_name = (TextView) view.findViewById(R.id.tv_ures_name);
            viewHored.tv_payment = (TextView) view.findViewById(R.id.tv_payment);
            viewHored.tv_money = (TextView) view.findViewById(R.id.tv_money);
            viewHored.but_details = (Button) view.findViewById(R.id.but_details);
            view.setTag(viewHored);
        }
        viewHored.tv_xunhao.setText((1 + i) + "");
        viewHored.tv_danhao.setText(adats.getResponse().getData().getList().get(i).getOrder_id());
        viewHored.tv_time.setText(TimeZoneUtil.getTime1((Long.valueOf(adats.getResponse().getData().getList().get(i).getCreatetime()) * 1000)));
        if (adats.getResponse().getData().getList().get(i).getOparator()!=null){
            if (adats.getResponse().getData().getList().get(i).getOparator().equals("null")){
                viewHored.tv_ures_name.setText(SharedUtil.getString("seller_name"));
            }else {
                viewHored.tv_ures_name.setText(adats.getResponse().getData().getList().get(i).getOparator());
            }
        }
        if (adats.getResponse().getData().getList().get(i).getType().equals("0")) {
            viewHored.tv_payment.setText("入库");
        } else {
                viewHored.tv_payment.setText("出库");
        }
        viewHored.tv_money.setText(StringUtils.stringpointtwo(adats.getResponse().getData().getList().get(i).getMoney()));
        viewHored.but_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclick.Setbutonclick(i);
            }
        });


        return view;
    }

    class ViewHored {
        TextView tv_xunhao, tv_danhao, tv_time, tv_ures_name, tv_payment, tv_money;
        Button but_details;
    }

    public interface SetOnclick {
        void Setbutonclick(int i);


    }

}
