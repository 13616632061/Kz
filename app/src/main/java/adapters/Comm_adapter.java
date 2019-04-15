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

import Entty.Commodity;
import Utils.SharedUtil;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/7.
 */
public class Comm_adapter extends BaseAdapter {
    public Context context;
    public List<Commodity> adats;
    public Oneidtextonclick oneidtextonclick;

    public Comm_adapter(Context context) {
        this.context = context;
        this.adats = new ArrayList<>();
    }

    public Comm_adapter setOneidtextonclick(Oneidtextonclick oneidtextonclick) {
        this.oneidtextonclick = oneidtextonclick;
        return Comm_adapter.this;
    }

    public void setAdats(List<Commodity> adats) {
        this.adats = adats;
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
        ViewHored viewHored = null;
        if (view != null) {
            viewHored = (ViewHored) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_comm, null);
            viewHored = new ViewHored();
            viewHored.tv_xuhao = (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_bncode = (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_py = (TextView) view.findViewById(R.id.tv_py);
            viewHored.tv_cat = (TextView) view.findViewById(R.id.tv_cat);
            viewHored.tv_unit = (TextView) view.findViewById(R.id.tv_unit);
            viewHored.tv_marketable = (TextView) view.findViewById(R.id.tv_marketable);
            viewHored.tv_store = (TextView) view.findViewById(R.id.tv_store);
            viewHored.tv_cost = (TextView) view.findViewById(R.id.tv_cost);
            viewHored.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewHored.but_filtrate = (Button) view.findViewById(R.id.but_filtrate);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i + 1) + "");
        if (adats.get(i).getBncode().equals("0")) {
            viewHored.tv_bncode.setText("--");
        } else {
            viewHored.tv_bncode.setText(adats.get(i).getBncode());
        }
        if (adats.get(i).getName().equals("null")) {
            viewHored.tv_name.setText("--");
        } else {
            viewHored.tv_name.setText(adats.get(i).getName());
        }
        if (adats.get(i).getPy().equals("null")) {
            viewHored.tv_py.setText("--");
        } else {
            viewHored.tv_py.setText(adats.get(i).getPy());
        }
        if (adats.get(i).getTag_name().isEmpty()) {
            viewHored.tv_cat.setText("--");
        } else {
            viewHored.tv_cat.setText(adats.get(i).getTag_name());
        }
        if (adats.get(i).getUnit().equals("null")) {
            viewHored.tv_unit.setText("--");
        } else {
            viewHored.tv_unit.setText(adats.get(i).getUnit());
        }
        if (adats.get(i).getMarketable().equals("true")) {
            viewHored.tv_marketable.setText("上架");
        }
        if (adats.get(i).getMarketable().equals("false")) {
            viewHored.tv_marketable.setText("下架");
        }


        if (!adats.get(i).getStore().equals("null") ) {
//            if (Integer.valueOf(adats.get(i).getStore()) <= Integer.valueOf(adats.get(i).getGood_stock())) {
                viewHored.tv_store.setText(adats.get(i).getStore());
//                viewHored.tv_store.setBackgroundColor(Color.parseColor("#ff0000"));
//            } else {
//                viewHored.tv_store.setText(adats.get(i).getStore());
//                viewHored.tv_store.setBackgroundColor(Color.parseColor("#ffffff"));
//            }
        }
        viewHored.tv_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneidtextonclick.Storeonclick(i);
            }
        });

        if (SharedUtil.getString("type").equals("4")){
            viewHored.tv_cost.setText("--");
        }else {
            viewHored.tv_cost.setText(StringUtils.stringpointtwo(adats.get(i).getCost()));
        }
        viewHored.tv_price.setText(StringUtils.stringpointtwo(adats.get(i).getPrice()));
        viewHored.but_filtrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneidtextonclick.itmeeidtonclick(i);
            }
        });

        return view;

    }


    class ViewHored {
        Button but_filtrate;
        TextView tv_xuhao, tv_bncode, tv_name, tv_py, tv_cat, tv_unit, tv_marketable, tv_store, tv_cost, tv_price;
    }

    public interface Oneidtextonclick {
        void itmeeidtonclick(int i);
        void Storeonclick(int i);
    }
}
