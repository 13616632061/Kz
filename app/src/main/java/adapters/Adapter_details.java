package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.Money_Entty;
import Utils.DateUtils;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/26.
 * 报货详情
 */
public class Adapter_details extends BaseAdapter {


    public List<Money_Entty> adats;
    public List<Commodity> commodities;
    public List<Integer> nums;
    public boolean Show=false;
    public Context context;
    public Adapter_Money adapter;
    public List<Boolean> Checked;
    public StrOnoclick strOnoclick;
    //判断是验收还是审核type=1为审核type=0为验收
    public int type;

    public Adapter_details(Context context,int type) {
        this.context = context;
        this.adapter=new Adapter_Money(context);
        this.adats=new ArrayList<>();
        this.commodities=new ArrayList<>();
        this.nums=new ArrayList<>();
        this.Checked=new ArrayList<>();
        this.type=type;
        Log.e("类型为",""+type);
    }

    public Adapter_details StrOnoclick(StrOnoclick strOnoclick){
        this.strOnoclick=strOnoclick;
        return Adapter_details.this;
    }


    public void setAdats(List<Money_Entty> adats){
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
        final ViewHored viewHored;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.money_details_layout,null);
            viewHored=new ViewHored();
            viewHored.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_state= (TextView) view.findViewById(R.id.tv_state);
            viewHored.but_details= (Button) view.findViewById(R.id.but_details);
            view.setTag(viewHored);
        }
        Checked.add(false);
        viewHored.tv_xuhao.setText((adats.size()-i)+"");
        viewHored.tv_name.setText(adats.get(i).getSeller_name());
        viewHored.tv_time.setText(DateUtils.getDateTimeFromMillisecond(1000*Long.parseLong(adats.get(i).getAddtime())));
        viewHored.tv_price.setText(StringUtils.stringpointtwo(adats.get(i).getTotal_amount()));

        if (type==0){
        if (adats.get(i).getStatus().equals("1")){
            viewHored.tv_state.setText("已验收");
            }else {
            viewHored.tv_state.setText("未验收");
            }

        }else {
            if (adats.get(i).getIs_verify().equals("1")){
                viewHored.tv_state.setText("已审核");
            }else {
                viewHored.tv_state.setText("未审核");
            }
        }
        viewHored.tv_nums.setText(adats.get(i).getNums());
        viewHored.but_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOnoclick.setbutonclick(i);
            }
        });
        return view;
    }

    class ViewHored{
        TextView tv_xuhao,tv_name,tv_time,tv_price,tv_nums,tv_state;
        Button but_details;
    }

    public interface StrOnoclick{
         void setbutonclick(int i);
    }


}
