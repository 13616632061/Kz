package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Shifting_Entty;
import Utils.SharedUtil;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/11.
 */
public class Shifting_adapter extends BaseAdapter {

    public Context context;
    public List<Shifting_Entty> adats;
    public SetLinearLayoutclick setLinearLayoutclick;


    public Shifting_adapter SetLinearLayoutclick(SetLinearLayoutclick setLinearLayoutclick){
        this.setLinearLayoutclick=setLinearLayoutclick;
        return Shifting_adapter.this;
    }

    public Shifting_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }


    public void setAdats(List<Shifting_Entty> adats){
        this.adats=adats;
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
        ViewHoerd viewHoerd;
        if (view!=null){
            viewHoerd= (ViewHoerd) view.getTag();
        }else {
            viewHoerd=new ViewHoerd();
            view= LayoutInflater.from(context).inflate(R.layout.lv_shifting,null);
            viewHoerd.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHoerd.tv_timestart= (TextView) view.findViewById(R.id.tv_timestart);
            viewHoerd.tv_timeend= (TextView) view.findViewById(R.id.tv_timeend);
//            viewHoerd.tv_cashier= (TextView) view.findViewById(R.id.tv_cashier);
            viewHoerd.tv_Total= (TextView) view.findViewById(R.id.tv_Total);
            viewHoerd.tv_cash= (TextView) view.findViewById(R.id.tv_cash);
            viewHoerd.tv_micro= (TextView) view.findViewById(R.id.tv_micro);
            viewHoerd.tv_standby= (TextView) view.findViewById(R.id.tv_standby);
            viewHoerd.tv_cashier= (TextView) view.findViewById(R.id.tv_cashier);
            viewHoerd.tv_money= (TextView) view.findViewById(R.id.tv_money);
            viewHoerd.tv_other= (TextView) view.findViewById(R.id.tv_other);
            viewHoerd.tv_cash_hand= (TextView) view.findViewById(R.id.tv_cash_hand);
            viewHoerd.ll_reserve= (LinearLayout) view.findViewById(R.id.ll_reserve);
            view.setTag(viewHoerd);
        }
        viewHoerd.tv_xuhao.setText((i+1)+"");
        viewHoerd.tv_timestart.setText(adats.get(i).getBegin_time());
        viewHoerd.tv_timeend.setText(adats.get(i).getEnd_time());
        viewHoerd.tv_Total.setText(StringUtils.stringpointtwo(adats.get(i).getTotal_money()));
        viewHoerd.tv_cash.setText(StringUtils.stringpointtwo(adats.get(i).getCash_money()));
        viewHoerd.tv_micro.setText(StringUtils.stringpointtwo(adats.get(i).getMicro_money()));
        if(adats.get(i).getSpare_money().equals("null")){
            viewHoerd.tv_standby.setText("0");
        }else {
            viewHoerd.tv_standby.setText(StringUtils.stringpointtwo(adats.get(i).getSpare_money()));
        }

        if (adats.get(i).getTurn_in_money()!=null){
            if (adats.get(i).getTurn_in_money().equals("null")){
                viewHoerd.tv_money.setText("0");
            }else {
                viewHoerd.tv_money.setText(StringUtils.stringpointtwo(adats.get(i).getTurn_in_money()));
            }
        }
        if (adats.get(i).getOther_money()!=null){
            if (adats.get(i).getOther_money().equals("null")){
                viewHoerd.tv_other.setText("0");
            }else {
                viewHoerd.tv_other.setText(StringUtils.stringpointtwo(adats.get(i).getOther_money()));
            }
        }
        if (adats.get(i).getWorker_name()!=null&&adats.get(i).getWorker_name().equals("null")){
            viewHoerd.tv_cashier.setText(SharedUtil.getString("seller_name"));
        }else {
            viewHoerd.tv_cashier.setText(adats.get(i).getWorker_name());
        }
        viewHoerd.ll_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setLinearLayoutclick.setOnClickListener(i);
            }
        });
        if (adats.get(i).getStore_money()!=null){
        if (adats.get(i).getStore_money().equals("null")){
            viewHoerd.tv_cash_hand.setText("0");
        }else {
            viewHoerd.tv_cash_hand.setText(adats.get(i).getStore_money());
        }
        }

        return view;
    }
    class ViewHoerd{
        TextView tv_other,tv_cash_hand,tv_xuhao,tv_timestart,tv_timeend,tv_cashier,tv_Total,tv_cash,tv_micro,tv_standby,tv_money;
        LinearLayout ll_reserve;
    }

    //选中的接口回掉
    public interface SetLinearLayoutclick {
        void setOnClickListener(int i);
    }
}
