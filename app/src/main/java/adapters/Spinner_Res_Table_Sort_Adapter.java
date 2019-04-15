package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Entty.Res_Table_Sort;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/8/14.
 */

public class Spinner_Res_Table_Sort_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;

    public Spinner_Res_Table_Sort_Adapter(Context context, ArrayList<Res_Table_Sort> mRes_Table_SortList) {
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_add_res_table_spinner_view,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_res_table_name= (TextView) convertView.findViewById(R.id.tv_res_table_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_res_table_name.setText(mRes_Table_SortList.get(position).getRes_table_sort_name());
        return convertView;
    }
    private class ViewHolder{
        TextView tv_res_table_name;
    }
}
