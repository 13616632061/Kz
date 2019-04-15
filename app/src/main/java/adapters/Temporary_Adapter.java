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

import Entty.Temporary_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/5.
 */
public class Temporary_Adapter extends BaseAdapter {

    public Context context;
    public List<Temporary_Entty> adats;
    public OnClickListener onClickListener;

    public Temporary_Adapter setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
        return Temporary_Adapter.this;
    }

    public Temporary_Adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Temporary_Entty> adats){
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
            viewHored=new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.print_itme,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_Price= (TextView) view.findViewById(R.id.tv_Price);
            viewHored.tv_Specifications= (TextView) view.findViewById(R.id.tv_Specifications);
            viewHored.tv_unit= (TextView) view.findViewById(R.id.tv_unit);
            viewHored.tv_code= (TextView) view.findViewById(R.id.tv_code);
            viewHored.tv_id= (TextView) view.findViewById(R.id.tv_id);
            viewHored.but_print= (Button) view.findViewById(R.id.but_print);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_name.setText(adats.get(i).getGoods_name());
        viewHored.tv_Price.setText(adats.get(i).getTotal());
        viewHored.tv_unit.setVisibility(View.GONE);
        viewHored.tv_code.setText(adats.get(i).getBncode());
        viewHored.tv_id.setVisibility(View.GONE);
        viewHored.tv_Specifications.setVisibility(View.GONE);
        viewHored.but_print.setText("编辑");
        viewHored.but_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.setonclick(i);
            }
        });
        return view;
    }
    class ViewHored{
        TextView tv_name,tv_Price,tv_Specifications,tv_unit,tv_code,tv_id,tv_xuhao;
        Button but_print;
    }

    public interface OnClickListener{
        void setonclick(int i);
    }
}
