package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Seek_entty;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/20.
 */
public class Seek_adapter extends BaseAdapter {
    public List<Seek_entty> adats;
    public Context context;

    public Seek_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Seek_entty> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    private void getAdats(List<Seek_entty> adats){
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
        ViewHored viewHored = null;
        if (view != null) {
            viewHored = (ViewHored) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.lv_seek, null);
            viewHored=new ViewHored();
            viewHored.tv_xunhao = (TextView) view.findViewById(R.id.tv_xunhao);
            viewHored.tv_bncode = (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_py = (TextView) view.findViewById(R.id.tv_py);
            viewHored.tv_unit = (TextView) view.findViewById(R.id.tv_unit);
            viewHored.tv_marketable = (TextView) view.findViewById(R.id.tv_marketable);
            viewHored.tv_store = (TextView) view.findViewById(R.id.tv_store);
            viewHored.tv_cost = (TextView) view.findViewById(R.id.tv_cost);
            viewHored.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewHored.tv_baozhi = (TextView) view.findViewById(R.id.tv_baozhi);
            view.setTag(viewHored);
        }
        viewHored.tv_xunhao.setText((i+1)+"");
        viewHored.tv_bncode.setText(adats.get(i).getBncode());
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_py.setText(adats.get(i).getPy()+"");
        if (adats.get(i).getCat_name().equals("null")){
            viewHored.tv_unit.setText("--");
        }else {
            viewHored.tv_unit.setText(adats.get(i).getCat_name());
        }
//        viewHored.tv_unit.setText(adats.get(i).getUnit());
        if (adats.get(i).getMarketable().equals("true")) {
            viewHored.tv_marketable.setText("上架");
        }else {
            viewHored.tv_marketable.setText("下架");
        }
        viewHored.tv_store.setText(adats.get(i).getStore());
        viewHored.tv_cost.setText(adats.get(i).getCost());
        viewHored.tv_price.setText(adats.get(i).getPrice());
        long adat= (long) (Float.parseFloat(adats.get(i).getGD())*1000);
        String day= TimeZoneUtil.getDay(TimeZoneUtil.getTime(adat), TimeZoneUtil.getTime(System.currentTimeMillis()));

        viewHored.tv_baozhi.setText(day);
        return view;
    }

    class ViewHored {
        TextView tv_xunhao, tv_bncode, tv_name, tv_py, tv_unit, tv_marketable, tv_store, tv_cost, tv_price, tv_baozhi;
    }
}
