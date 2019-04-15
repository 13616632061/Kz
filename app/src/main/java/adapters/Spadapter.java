package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Fenlei_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/8.
 */
public class Spadapter extends BaseAdapter {
    public Context context;
    public List<Fenlei_Entty> adats;
    public Oneidtextonclick oneidtextonclick;
    public String type="0";


    public Spadapter setOneidtextonclick(Oneidtextonclick oneidtextonclick) {
        this.oneidtextonclick = oneidtextonclick;
        return Spadapter.this;
    }

    public Spadapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Fenlei_Entty> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void setType(String i){
        this.type=i;
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
            viewHored.ll_itme= (LinearLayout) view.findViewById(R.id.ll_itme);
            view.setTag(viewHored);
        }
            viewHored.tv_name.setText(adats.get(i).getName());

        viewHored.ll_itme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("print", "onClick: "+"qqqqqqqqqq");
                oneidtextonclick.itmeeidtonclick(i,type);
            }
        });

        return view;
    }


    public interface Oneidtextonclick {
        void itmeeidtonclick(int i, String type);
    }

    class ViewHored{
        TextView tv_name;
        LinearLayout ll_itme;
    }



}
