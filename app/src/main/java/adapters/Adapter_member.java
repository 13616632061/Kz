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

import Entty.Member_entty;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/27.
 */
public class Adapter_member extends BaseAdapter {

    public Context context;
    public List<Member_entty> adats;
    public SetOnclickitme setOnclickitme;


    public Adapter_member(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public Adapter_member SetOnclickitme(SetOnclickitme setOnclickitme){
        this.setOnclickitme=setOnclickitme;
        return Adapter_member.this;
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
            viewHored=new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.member_list,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_grade= (TextView) view.findViewById(R.id.tv_grade);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
            viewHored.tv_score= (TextView) view.findViewById(R.id.tv_score);
            viewHored.tv_discount= (TextView) view.findViewById(R.id.tv_discount);
            viewHored.tv_balance= (TextView) view.findViewById(R.id.tv_balance);
            viewHored.but_edit= (Button) view.findViewById(R.id.but_edit);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_name.setText(adats.get(i).getMember_name());
        viewHored.tv_grade.setText(adats.get(i).getMember_lv_custom_name());
        if (!adats.get(i).getTime().equals("null")&&adats.get(i).getTime()!=null){
            viewHored.tv_time.setText(TimeZoneUtil.getTime1(Long.parseLong(adats.get(i).getTime())*1000));
        }
        viewHored.tv_phone.setText(adats.get(i).getMobile());
        viewHored.tv_score.setText(adats.get(i).getScore());
        viewHored.tv_balance.setText(adats.get(i).getSurplus());
        viewHored.tv_discount.setText(adats.get(i).getDiscount_rate());
        viewHored.but_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclickitme.OnclickEdit(i);
            }
        });
        return view;
    }


    class ViewHored{
        TextView tv_grade,tv_xuhao,tv_name,tv_time,tv_phone,tv_score,tv_discount,tv_balance;
        Button but_edit;
    }

   public interface SetOnclickitme{
        void OnclickEdit(int i);
    }

}
