package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Return_Entty;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/25.
 */
public class Return_Lei_adapter extends BaseAdapter {

    public Context context;

    public List<Return_Entty.ItemsBean> adats;


    public Return_Lei_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void getAdats(List<Return_Entty.ItemsBean> adats){
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
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_name.setTextColor(Color.parseColor("#FF6501"));
        viewHored.tv_unms.setText(adats.get(i).getNums());
        viewHored.tv_unms.setTextColor(Color.parseColor("#FF6501"));
        viewHored.tv_jiner.setText(StringUtils.stringpointtwo(adats.get(i).getSum()));
        viewHored.tv_jiner.setTextColor(Color.parseColor("#FF6501"));
        viewHored.but_tuikuan.setVisibility(View.GONE);
        return view;
    }



    class ViewHored{

        TextView tv_name,tv_unms,tv_jiner,tv_ship_nums;
        Button but_tuikuan;
    }


}
