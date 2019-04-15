package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Detail_Entty;
import Utils.DateUtils;
import Utils.SharedUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/26.
 */
public class Adapter_detail extends BaseAdapter {

    public Context context;
    public List<Detail_Entty> adats;

    public Adapter_detail(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }


    public void setAdats(List<Detail_Entty> adats){
        this.adats=adats;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHored viewHored;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.adapter_detail,null);
            viewHored=new ViewHored();
            viewHored.tv_operator= (TextView) view.findViewById(R.id.tv_operator);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.but_Integral_change= (TextView) view.findViewById(R.id.but_Integral_change);
            viewHored.tv_balance_changes= (TextView) view.findViewById(R.id.tv_balance_changes);
            viewHored.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_name.setText(adats.get(i).getMember_name());
        viewHored.tv_phone.setText(adats.get(i).getMobile());

        if (Float.parseFloat(adats.get(i).getSurplus())>0){
            viewHored.tv_balance_changes.setText("+"+adats.get(i).getSurplus());
        }else {
            viewHored.tv_balance_changes.setText(adats.get(i).getSurplus());
        }

        if (Float.parseFloat(adats.get(i).getScore())>0){
            viewHored.but_Integral_change.setText("+"+adats.get(i).getScore());
        }else {
            viewHored.but_Integral_change.setText(adats.get(i).getScore());
        }

        if (adats.get(i).getLogin_name().equals("null")){
            viewHored.tv_operator.setText(SharedUtil.getString("name"));
        }else {
            viewHored.tv_operator.setText(adats.get(i).getLogin_name());
        }
        viewHored.tv_time.setText(DateUtils.getDateTimeFromMillisecond((long) Integer.parseInt(adats.get(i).getAddtime())*1000));
        return view;
    }


    class ViewHored{
        TextView tv_xuhao,tv_name,tv_time,tv_operator,but_Integral_change,tv_balance_changes,tv_phone;
    }
}
