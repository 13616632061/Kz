package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Integral_Entty;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/27.
 */
public class Adapter_integral extends BaseAdapter {

    public Context context;
    public List<Integral_Entty> adats;
    public SetOnclickitme setOnclickitme;
    public int j=0;


    public Adapter_integral(Context context,int i) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.j=i;
        Log.d("print","类型为"+i);
    }

    public Adapter_integral SetOnclickitme(SetOnclickitme setOnclickitme){
        this.setOnclickitme=setOnclickitme;
        return Adapter_integral.this;
    }

    public void setAdats(List<Integral_Entty> adats){
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
            view= LayoutInflater.from(context).inflate(R.layout.integral_listview,null);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHored.tv_integral= (TextView) view.findViewById(R.id.tv_integral);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_discount= (TextView) view.findViewById(R.id.tv_discount);
            viewHored.tv_edit= (TextView) view.findViewById(R.id.tv_edit);
            viewHored.cc_box= (CheckBox) view.findViewById(R.id.cc_box);
            view.setTag(viewHored);
        }
        viewHored.tv_bncode.setText(adats.get(i).getBncode());
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_price.setText(StringUtils.stringpointtwo(adats.get(i).getPrice()));
        viewHored.tv_integral.setText(adats.get(i).getScore());
        if (j==0){
            viewHored.tv_nums.setVisibility(View.VISIBLE);
            viewHored.tv_nums.setText(adats.get(i).getNums());
            viewHored.cc_box.setVisibility(View.GONE);
            viewHored.tv_edit.setVisibility(View.VISIBLE);
            viewHored.tv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnclickitme.OnclickEdit(i);
                }
            });
        }else {
            viewHored.tv_nums.setVisibility(View.GONE);
            viewHored.tv_edit.setVisibility(View.GONE);
            viewHored.cc_box.setVisibility(View.VISIBLE);
            viewHored.cc_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b){
                                adats.get(i).setElect(true);
                            }else {
                                adats.get(i).setElect(false);
                            }
                }
            });
        }



        return view;
    }


    class ViewHored{
        TextView tv_bncode,tv_name,tv_price,tv_integral,tv_nums,tv_discount,tv_edit;
        CheckBox cc_box;
    }

   public interface SetOnclickitme{
        void OnclickEdit(int i);
    }

}
