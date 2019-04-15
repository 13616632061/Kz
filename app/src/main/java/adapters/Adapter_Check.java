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

import Entty.Fenlei_Entty;
import Utils.SharedUtil;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/3.
 * 盘点分类的适配器
 */
public class Adapter_Check extends BaseAdapter {

    public Context context;
    public List<Fenlei_Entty> adats;
    public OnClickListener onClickListener;

    public Adapter_Check setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
        return Adapter_Check.this;
    }

    public Adapter_Check(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Fenlei_Entty> adats){
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
            view=LayoutInflater.from(context).inflate(R.layout.itme_check,null);
            viewHored=new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            view.setTag(viewHored);
        }
        if (adats.get(i).isVisibility()){
            if (SharedUtil.getString("power")!=null){
                if (SharedUtil.getString("power").equals("")||SharedUtil.getString("power").equals("1")){
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#ffffff"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#d6d6d6"));
                }else if (SharedUtil.getString("power").equals("2")){
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#ffffff"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#a6a6a6"));
                }else {
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#ffffff"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#060606"));
                }
            }else {
                viewHored.tv_name.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHored.tv_name.setTextColor(Color.parseColor("#d6d6d6"));
            }

        }else {

            if (SharedUtil.getString("power")!=null){
                if (SharedUtil.getString("power").equals("")||SharedUtil.getString("power").equals("1")){
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#d6d6d6"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#ffffff"));
                }else if (SharedUtil.getString("power").equals("2")){
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#a6a6a6"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#ffffff"));
                }else {
                    viewHored.tv_name.setBackgroundColor(Color.parseColor("#060606"));
                    viewHored.tv_name.setTextColor(Color.parseColor("#ffffff"));
                }
            }else {
                viewHored.tv_name.setBackgroundColor(Color.parseColor("#d6d6d6"));
                viewHored.tv_name.setTextColor(Color.parseColor("#ffffff"));
            }
        }
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.setonclick(i);
            }
        });
        return view;
    }

    class ViewHored{
        TextView tv_name;
    }

    public interface OnClickListener{
        void setonclick(int i);
    }

}
