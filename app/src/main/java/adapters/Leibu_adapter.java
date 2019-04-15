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

import Entty.Check_tuihuo;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/25.
 */
public class Leibu_adapter extends BaseAdapter {

    public Context context;

    public List<Check_tuihuo> adats;

    public Ontuihuoonclick ontuihuoonclick;

    public Leibu_adapter setOntuihuoonclick(Ontuihuoonclick ontuihuoonclick){
        this.ontuihuoonclick=ontuihuoonclick;
        return Leibu_adapter.this;
    }

    public Leibu_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void getAdats(List<Check_tuihuo> adats){
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
        ViewHored viewHored=null;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.lv_leibu,null);
            viewHored=new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_ship_nums= (TextView) view.findViewById(R.id.tv_ship_nums);
            viewHored.tv_unms= (TextView) view.findViewById(R.id.tv_unms);
            viewHored.tv_jiner= (TextView) view.findViewById(R.id.tv_jiner);
            viewHored.but_tuikuan= (Button) view.findViewById(R.id.but_tuikuan);
            view.setTag(viewHored);
        }

        if (Integer.parseInt(adats.get(i).getShip_nums())>0){
            viewHored.tv_ship_nums.setText("已退货"+adats.get(i).getShip_nums()+"件");
        }else {
            viewHored.tv_ship_nums.setText("");
        }
        viewHored.tv_name.setText(adats.get(i).getSeller_name());
        viewHored.tv_unms.setText(adats.get(i).getUnms());
        viewHored.tv_jiner.setText(StringUtils.stringpointtwo((Float.valueOf(adats.get(i).getPrice())*Float.valueOf(adats.get(i).getUnms()))+""));
        viewHored.but_tuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(adats.get(i).getShip_nums())<Integer.parseInt(adats.get(i).getUnms())) {
                    ontuihuoonclick.itmeonclick(i);
                }
            }
        });
        return view;
    }



    class ViewHored{

        TextView tv_name,tv_unms,tv_jiner,tv_ship_nums;
        Button but_tuikuan;
    }
    public interface Ontuihuoonclick{
        void itmeonclick(int i);
    }


}
