package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Label_entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/8.
 */
public class Sp2adapter extends BaseAdapter {
    public Context context;
    public List<Label_entty> adats;

    public Sp2adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Label_entty> adats){
        this.adats=adats;
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return adats.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHored viewHored=null;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.sppin, null);
            viewHored = new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            view.setTag(viewHored);
        }
            viewHored.tv_name.setText(adats.get(i).getLabel_name());
        return view;
    }




    class ViewHored{
        TextView tv_name;
    }



}
