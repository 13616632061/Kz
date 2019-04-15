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

import Entty.Commodity;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/22.
 */
public class Adapter_Fuzzy extends BaseAdapter {
    public Context context;
    public List<Commodity> adats;
    public SetOnclick setOnclick;


    public Adapter_Fuzzy SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_Fuzzy.this;
    }


    public Adapter_Fuzzy(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Commodity> adats){
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
            if (context!=null){
                view=LayoutInflater.from(context).inflate(R.layout.fuzzy_itme,null);
            }
            viewHored=new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHored.ll_itme= (LinearLayout) view.findViewById(R.id.ll_itme);
            view.setTag(viewHored);
        }
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_price.setText(adats.get(i).getPrice());
        viewHored.ll_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclick.setClickListener(i);
            }
        });

        return view;
    }




    class ViewHored{
        TextView tv_name,tv_price;
        LinearLayout ll_itme;
    }


    public interface SetOnclick {
        void setClickListener(int i);
    }

}
