package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.Spinner_Res_Table_Sort_Adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/9/15.
 */

public class Spinner_sharegoods_sortAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;

    public Spinner_sharegoods_sortAdapter(Context context, ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList) {
        this.context = context;
        this.mShare_Goods_SortInfoList = mShare_Goods_SortInfoList;
    }

    @Override
    public int getCount() {
        return mShare_Goods_SortInfoList==null?0:mShare_Goods_SortInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mShare_Goods_SortInfoList.get(position);
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
        viewHolder.tv_res_table_name.setText(mShare_Goods_SortInfoList.get(position).getType_name());
        return convertView;
    }
    private class ViewHolder{
        TextView tv_res_table_name;
    }
}
