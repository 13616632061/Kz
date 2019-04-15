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

import Entty.Provider_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/2.
 */
public class Adapter_providers extends BaseAdapter {

    public Context context;
    public List<Provider_Entty> adats;
    public Setonclickitme setonclickitme;

    public Adapter_providers Setonclickitme(Setonclickitme setonclickitme){
        this.setonclickitme=setonclickitme;
        return Adapter_providers.this;
    }

    public Adapter_providers(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Provider_Entty> adats){
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
        ViewHored viewhored=null;
        if (view!=null){
            viewhored= (ViewHored) view.getTag();
        }else {
            viewhored=new ViewHored();
            view=LayoutInflater.from(context).inflate(R.layout.provider_itme,null);
            viewhored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewhored.ll= (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(viewhored);
        }
        viewhored.tv_name.setText(adats.get(i).getProvider_name());
        viewhored.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclickitme.setOnclick(i);
            }
        });
        return view;
    }

    class ViewHored{
        TextView tv_name;
        LinearLayout ll;
    }

    public interface Setonclickitme{
        void setOnclick(int i);
    }

}
