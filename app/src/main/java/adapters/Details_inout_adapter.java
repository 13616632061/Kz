package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import Entty.In_Out_Details;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/2.
 */
public class Details_inout_adapter extends BaseAdapter {
    public Context context;
    public In_Out_Details adats;



    public Details_inout_adapter(Context context) {
        this.context = context;
    }

    public void getAdats(In_Out_Details adats) {
        this.adats = adats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats.getResponse().getData().size();
    }

    @Override
    public Object getItem(int i) {
        return adats.getResponse().getData().get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.details_inout_itme, null);
            viewHored.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_cost = (TextView) view.findViewById(R.id.tv_cost);
            viewHored.tv_inout = (TextView) view.findViewById(R.id.tv_inout);
            viewHored.tv_code = (TextView) view.findViewById(R.id.tv_code);
            viewHored.tv_supplier = (TextView) view.findViewById(R.id.tv_supplier);
            view.setTag(viewHored);
        }
        viewHored.tv_name.setText(adats.getResponse().getData().get(i).getName());
        viewHored.tv_cost.setText(adats.getResponse().getData().get(i).getCost());
        viewHored.tv_inout.setText(adats.getResponse().getData().get(i).getNums());
        viewHored.tv_code.setText(adats.getResponse().getData().get(i).getBncode());
        Log.d("print","打印条码是多少"+adats.getResponse().getData().get(i).getBncode());
        viewHored.tv_supplier.setText(adats.getResponse().getData().get(i).getProvider_name());
        return view;
    }

    class ViewHored {
        TextView tv_name, tv_cost, tv_inout ,tv_code ,tv_supplier ;
    }



}
