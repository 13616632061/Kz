package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Entty.Recharge_smsEntty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/31.
 */
public class Gridview_member_adapter extends BaseAdapter {

//    recharge_smsEntty
    public Context context;
    public Recharge_smsEntty adats;


    public SetOnclickitme setOnclickitme;

    public Gridview_member_adapter setOnclickitme(SetOnclickitme setOnclickitme) {
        this.setOnclickitme = setOnclickitme;
        return Gridview_member_adapter.this;
    }

    public Gridview_member_adapter(Context context) {
        this.context=context;
    }

    public void setAdats(Recharge_smsEntty adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats==null?0:adats.getResponse().getData().getList().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoed viewHoed;
        if (view != null) {
            viewHoed = (ViewHoed) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.member_recharge_itme, null);
            viewHoed = new ViewHoed();
            viewHoed.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHoed.tv_num= (TextView) view.findViewById(R.id.tv_num);
            viewHoed.tv_rl_price= (RelativeLayout) view.findViewById(R.id.tv_rl_price);
            view.setTag(viewHoed);
        }
        viewHoed.tv_rl_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclickitme.Onclickitme(i);
            }
        });

        viewHoed.tv_num.setText(adats.getResponse().getData().getList().get(i).getNum());
        viewHoed.tv_price.setText("￥"+adats.getResponse().getData().getList().get(i).getPrice());
        return view;
    }

    public interface SetOnclickitme{
        void Onclickitme(int i);
    }

    private class ViewHoed {
        TextView tv_num,tv_price;
        RelativeLayout tv_rl_price;
    }
}
