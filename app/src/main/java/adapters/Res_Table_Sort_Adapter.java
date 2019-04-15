package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Entty.Res_Table_Sort;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/8/15.
 * 餐桌分类--收银页面
 */

public class Res_Table_Sort_Adapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;//餐桌分类数据
    private int cur_pos;

    public Res_Table_Sort_Adapter(Context mContext, ArrayList<Res_Table_Sort> mRes_Table_SortList) {
        this.mContext = mContext;
        this.mRes_Table_SortList = mRes_Table_SortList;
    }

    @Override
    public int getCount() {
        if(mRes_Table_SortList==null){
            return 0;
        }
        return mRes_Table_SortList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRes_Table_SortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 被选中的分类
     * @param position
     */
    public void setDefSelect(int position) {
        this.cur_pos = position;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_self_service_restanraunt_sorts,null);
            holder=new Holder();
            holder.tv_sort_name= (TextView) convertView.findViewById(R.id.tv_sort_name);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.tv_sort_name.setText(mRes_Table_SortList.get(position).getRes_table_sort_name());
        if(position==cur_pos){
            convertView.setBackgroundResource(R.color.white);
            holder.tv_sort_name.setTextColor(Color.parseColor("#ffff8905"));
        }else {
            convertView.setBackgroundResource(R.drawable.select_click_grag);
            holder.tv_sort_name.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    private class Holder{
        TextView tv_sort_name;
    }
}
