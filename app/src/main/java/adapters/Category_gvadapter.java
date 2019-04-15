package adapters;

import android.content.Context;
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
 * Created by admin on 2017/4/1.
 */
public class Category_gvadapter extends BaseAdapter {

    public List<Fenlei_Entty> adats;
    public Context context;
    public DeliteOnclick deliteOnclick;

    public Category_gvadapter setDeliteOnclick(DeliteOnclick deliteOnclick){
        this.deliteOnclick=deliteOnclick;
        return Category_gvadapter.this;
    }

    public Category_gvadapter(Context context) {
        this.context=context;
        this.adats = new ArrayList<>();
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
