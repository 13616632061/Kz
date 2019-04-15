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

import Entty.Staff_entty;
import Utils.SharedUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/22.
 */
public class Staff_adapter extends BaseAdapter {

    public Context context;
    public List<Staff_entty> adats;
    public Oneidtextonclick oneidtextonclick;

    public Staff_adapter setOneidtextonclick(Oneidtextonclick oneidtextonclick){
        this.oneidtextonclick=oneidtextonclick;
        return Staff_adapter.this;
    }


    public Staff_adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Staff_entty> adats){
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
            view= LayoutInflater.from(context).inflate(R.layout.staff_listview,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
            viewHored.tv_role= (TextView) view.findViewById(R.id.tv_role);
            viewHored.tv_deduct= (TextView) view.findViewById(R.id.tv_deduct);
            viewHored.tv_state= (TextView) view.findViewById(R.id.tv_state);
            viewHored.but_edit= (Button) view.findViewById(R.id.but_edit);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
//        SharedUtil.getString("seller_name")
        viewHored.tv_bncode.setText(adats.get(i).getBn().replace(SharedUtil.getString("seller_name"),""));
        viewHored.tv_name.setText(adats.get(i).getLogin_name());
        viewHored.tv_phone.setText(adats.get(i).getPhone());
        if (!adats.get(i).getRate().equals("null")){
            viewHored.tv_deduct.setText(adats.get(i).getRate()+"%");
        }else {
            viewHored.tv_deduct.setText("0%");
        }
        if (adats.get(i).getDisable().equals("true")){
            viewHored.tv_state.setText("启用");
        }else if (adats.get(i).getDisable().equals("false")){
            viewHored.tv_state.setText("禁用");
        }
        viewHored.but_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneidtextonclick.itmeeidtonclick(i);
            }
        });
        return view;
    }



    class ViewHored{
        TextView tv_xuhao,tv_bncode,tv_name,tv_phone,tv_role,tv_deduct,tv_state;
        Button but_edit;
    }
    public interface Oneidtextonclick {
        void itmeeidtonclick(int i);
    }

}
