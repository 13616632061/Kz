package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.GridView_xuangzhong;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/31.
 */
public class Gridview_adapter extends BaseAdapter {

    public Context context;
    public List<GridView_xuangzhong> adats;


    public Gridview_adapter(Context context) {
        this.context=context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<GridView_xuangzhong> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats==null?0:adats.size();
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
        ViewHoed viewHoed;
        if (view != null) {
            viewHoed = (ViewHoed) view.getTag();

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_itme, null);
            viewHoed = new ViewHoed();
            viewHoed.gv_itme= (TextView) view.findViewById(R.id.gv_itme);
            view.setTag(viewHoed);
        }
        viewHoed.gv_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adats.get(i).isChecked()){
                    adats.get(i).setChecked(false);
                    notifyDataSetChanged();
                }else {
                    adats.get(i).setChecked(true);
                    notifyDataSetChanged();
                }


            }
        });

        if(adats.get(i).isChecked()){
            viewHoed.gv_itme.setBackgroundColor(Color.parseColor("#ff0000"));
            notifyDataSetChanged();
        }else {
            viewHoed.gv_itme.setBackgroundColor(Color.parseColor("#ffffff"));
            notifyDataSetChanged();
        }
        viewHoed.gv_itme.setText(adats.get(i).getCategory());
        return view;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(boolean isUnfold,
                                     final List<GridView_xuangzhong> tempData) {

        if (tempData == null || 0 == tempData.size()) {
            return;
        }
        adats.clear();

        if (isUnfold) {
            adats.addAll(tempData);
        } else {
            adats.add(tempData.get(0));
            adats.add(tempData.get(1));
            adats.add(tempData.get(2));
            adats.add(tempData.get(3));
        }
        notifyDataSetChanged();
    }

    private class ViewHoed {
        TextView gv_itme;
    }
}
