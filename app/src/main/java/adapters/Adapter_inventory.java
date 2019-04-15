package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/14.
 */
public class Adapter_inventory extends BaseAdapter {

    public Context context;
    public List<Commodity> adats;
    public List<String> stringList;

    public Adapter_inventory(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.stringList=new ArrayList<>();
    }

    public void setAdats(List<Commodity> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void setStringList(List<String> stringList){
        this.stringList=stringList;
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
        ViewHoerd viewHoerd=null;
        if (view!=null){
            viewHoerd = (ViewHoerd) view.getTag();
        }else {
            viewHoerd=new ViewHoerd();
            view= LayoutInflater.from(context).inflate(R.layout.list_inventory,null);
            viewHoerd.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHoerd.tv_store= (TextView) view.findViewById(R.id.tv_store);
            view.setTag(viewHoerd);
        }
        viewHoerd.tv_name.setText(adats.get(i).getName());
        viewHoerd.tv_store.setText(stringList.get(i));

        return view;
    }

    public class ViewHoerd{
        TextView tv_name,tv_store;
    }

}
