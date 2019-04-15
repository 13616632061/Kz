package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.redeem_Entty;
import Utils.DateUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/8.
 */
public class Adapter_redeem extends BaseAdapter {
    public Context context;
    public SetOnclick setOnclick;


    public List<redeem_Entty> adats;


    public Adapter_redeem(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<redeem_Entty> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }



    public Adapter_redeem SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_redeem.this;
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
        ViewHored viewHored=null;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.redeem_list, null);
            viewHored=new ViewHored();
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_ures_name= (TextView) view.findViewById(R.id.tv_ures_name);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_unms= (TextView) view.findViewById(R.id.tv_unms);
            viewHored.tv_cost= (TextView) view.findViewById(R.id.tv_cost);
            viewHored.tv_money= (TextView) view.findViewById(R.id.tv_money);
            viewHored.tv_state= (TextView) view.findViewById(R.id.tv_state);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((1+i)+"");
        viewHored.tv_time.setText(DateUtils.getDateTimeFromMillisecond(Long.parseLong(adats.get(i).getAddtime())*1000));
        viewHored.tv_ures_name.setText(adats.get(i).getOprator());
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_unms.setText(adats.get(i).getNums());
        viewHored.tv_cost.setText(adats.get(i).getCost());
        viewHored.tv_money.setText(adats.get(i).getTotal_amount());

        if (adats.get(i).getStatus().equals("0")){
            viewHored.tv_state.setText("未处理");
        }else if (adats.get(i).getStatus().equals("1")){
            viewHored.tv_state.setText("退钱处理");
        }else {
            viewHored.tv_state.setText("退货处理");
        }

        viewHored.tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adats.get(i).getStatus().equals("0")){
                    setOnclick.setOnClickListener(i);
                }else {
                }

            }
        });
        return view;
    }

   class ViewHored{
       TextView tv_xuhao,tv_time,tv_ures_name,tv_name,tv_unms,tv_cost,tv_money,tv_state;
   }
    public interface SetOnclick {
        void setOnClickListener(int i);
    }

}
