package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Fenlei_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/7.
 */
public class Unit_adapter extends BaseAdapter {
    public List<Fenlei_Entty> adats;
    public Context context;
    public DeliteOnclick deliteOnclick;
    public List<Boolean> ischeck;

    public Unit_adapter setDeliteOnclick(DeliteOnclick deliteOnclick){
        this.deliteOnclick=deliteOnclick;
        return Unit_adapter.this;
    }

    public Unit_adapter(Context context) {
        this.context=context;
        this.adats = new ArrayList<>();
    }

    public void setAdats(List<Fenlei_Entty> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void setcheck(List<Boolean> ischeck){
        this.ischeck=ischeck;
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
            viewHored=new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.category_geitme,null);
            viewHored.tv_category= (TextView) view.findViewById(R.id.tv_category);
            viewHored.im_delete= (ImageView) view.findViewById(R.id.im_delete);
            viewHored.im_edit= (ImageView) view.findViewById(R.id.im_edit);
            view.setTag(viewHored);
        }
        viewHored.tv_category.setText(adats.get(i).getName());

        if (ischeck!=null){
            if (!ischeck.get(i)){
                viewHored.tv_category.setBackgroundColor(Color.parseColor("#ff0000"));
            }else {
                viewHored.tv_category.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        if (adats.get(i).isVisibility()){
            viewHored.im_delete.setVisibility(View.VISIBLE);
        }else {
            viewHored.im_delete.setVisibility(View.GONE);
        }

        if (adats.get(i).isedit()){
            viewHored.im_edit.setVisibility(View.VISIBLE);
        }else {
            viewHored.im_edit.setVisibility(View.GONE);
        }

        viewHored.tv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliteOnclick.OnitmeEdit(i);
            }
        });

        viewHored.im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliteOnclick.onImeclick(i);
            }
        });


        return view;
    }
    class ViewHored{
        ImageView im_delete,im_edit;
        TextView tv_category;
    }
    public interface DeliteOnclick{
        void onImeclick(int i);
        void OnitmeEdit(int i);
    }
}
