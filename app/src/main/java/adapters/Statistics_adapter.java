package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Market_entty;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/11.
 */
public class Statistics_adapter extends BaseAdapter {

    public Context context;
    public List<Market_entty> adats;

    public Statistics_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Market_entty> adats){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoerd viewHoerd;
        if (view!=null){
            viewHoerd= (ViewHoerd) view.getTag();
        }else {
            viewHoerd=new ViewHoerd();
            view= LayoutInflater.from(context).inflate(R.layout.statistics_listview,null);
            viewHoerd.tv_serial= (TextView) view.findViewById(R.id.tv_serial);
            viewHoerd.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHoerd.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHoerd.tv_classify= (TextView) view.findViewById(R.id.tv_classify);
            viewHoerd.tv_repertory= (TextView) view.findViewById(R.id.tv_repertory);
            viewHoerd.tv_number= (TextView) view.findViewById(R.id.tv_number);
            viewHoerd.tv_total= (TextView) view.findViewById(R.id.tv_total);
            viewHoerd.tv_received= (TextView) view.findViewById(R.id.tv_received);
            viewHoerd.tv_lirun= (TextView) view.findViewById(R.id.tv_lirun);
            viewHoerd.tv_lirunli= (TextView) view.findViewById(R.id.tv_lirunli);
            viewHoerd.ll= (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(viewHoerd);
        }
        viewHoerd.tv_serial.setText((i+1)+"");
        viewHoerd.tv_name.setText(adats.get(i).getName());
        viewHoerd.tv_bncode.setText(adats.get(i).getBncode());
        viewHoerd.tv_classify.setText(adats.get(i).getCat_name());
        viewHoerd.tv_repertory.setText(adats.get(i).getStore());
        viewHoerd.tv_number.setText(adats.get(i).getNums());
        viewHoerd.tv_total.setText(""+Double.parseDouble(adats.get(i).getTotal()));
        viewHoerd.tv_received.setText(""+Double.parseDouble(adats.get(i).getTotal()));

        switch (getItemViewType(i)){
            case 0:
                viewHoerd.ll.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case 1:
                viewHoerd.ll.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }

        if (!adats.get(i).getCost().equals("null")&&!adats.get(i).getPrice().equals("null")){
            viewHoerd.tv_lirun.setText(StringUtils.stringpointtwo(((Float.valueOf(StringUtils.stringpointtwo(adats.get(i).getPrice()))-Float.valueOf(StringUtils.stringpointtwo(adats.get(i).getCost())))*Float.parseFloat(StringUtils.stringpointtwo(adats.get(i).getNums())))+""));
            if (Float.valueOf(adats.get(i).getCost())>0){
//                viewHoerd.tv_lirunli.setText(StringUtils.stringpointtwo((Float.valueOf(adats.get(i).getPrice())-Float.valueOf(adats.get(i).getCost()))/Float.valueOf(adats.get(i).getCost())+"")+"%");
            }else {
                viewHoerd.tv_lirunli.setText(0+"");
            }
        }else {
            viewHoerd.tv_lirun.setText("0");
            viewHoerd.tv_lirunli.setText("0");
        }
        return view;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    class ViewHoerd{
        TextView tv_serial,tv_name,tv_bncode,tv_classify,tv_repertory,tv_number,tv_total,tv_received,tv_lirun,tv_lirunli;
        LinearLayout ll;
    }

}
