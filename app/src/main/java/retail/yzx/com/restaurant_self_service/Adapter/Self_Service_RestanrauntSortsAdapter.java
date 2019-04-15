package retail.yzx.com.restaurant_self_service.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Entty.GoodsSortInfos;

/**
 * Created by Administrator on 2017/7/19.
 */

public class Self_Service_RestanrauntSortsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList;
    private int cur_pos;

    public Self_Service_RestanrauntSortsAdapter(Context mContext, ArrayList<GoodsSortInfos> mGoodsSortInfosList) {
        this.mContext = mContext;
        this.mGoodsSortInfosList = mGoodsSortInfosList;
    }

    @Override
    public int getCount() {
        if(mGoodsSortInfosList==null){
            return 0;
        }
        return mGoodsSortInfosList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsSortInfosList.get(position);
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
        holder.tv_sort_name.setText(mGoodsSortInfosList.get(position).getSort_name());
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
