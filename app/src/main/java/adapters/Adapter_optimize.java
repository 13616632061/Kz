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

import Entty.Member_entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/10.
 */
public class Adapter_optimize extends BaseAdapter {

    public Context context;
    public List<Member_entty> adats;
    public OnClickListener onClickListener;

    public Adapter_optimize OnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
        return Adapter_optimize.this;
    }


    public Adapter_optimize(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Member_entty> adats){
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
            viewHored = new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.lv_optimize,null);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_number= (TextView) view.findViewById(R.id.tv_number);
            viewHored.ll= (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(viewHored);
        }
        viewHored.tv_name.setText(adats.get(i).getMember_name());
        viewHored.tv_number.setText(adats.get(i).getMobile());
        viewHored.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.setonclick(i);
            }
        });
        return view;
    }




    class ViewHored{
        TextView tv_name,tv_number;
        LinearLayout ll;
    }

    public interface OnClickListener{
        void setonclick(int i);
    }

}
