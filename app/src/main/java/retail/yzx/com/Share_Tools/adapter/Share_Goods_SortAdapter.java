package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Adapter.listview.PinnedHeaderListView;

/**
 * 共享商品分类
 * Created by Administrator on 2017/9/8.
 */

public class Share_Goods_SortAdapter extends RecyclerView.Adapter<Share_Goods_SortAdapter.HolderView> {
    private Context mContext;
    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;
    private boolean isEdit=false;

    public Share_Goods_SortAdapter(Context mContext, ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList) {
        this.mContext = mContext;
        this.mShare_Goods_SortInfoList = mShare_Goods_SortInfoList;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    public int getItemCount() {
        if(mShare_Goods_SortInfoList==null){
            return 0;
        }else {
            return mShare_Goods_SortInfoList.size();
        }
    }
    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(mContext, R.layout.item_res_table_sort,null);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.tv_res_table_name.setText(mShare_Goods_SortInfoList.get(position).getType_name());
        if(isEdit){
            holder.layout_res_table_sort_management.setVisibility(View.VISIBLE);
        }else {
            holder.layout_res_table_sort_management.setVisibility(View.GONE);
        }
        TextPaint tp=holder.tv_res_table_name.getPaint();//设置字体画笔
        if(mShare_Goods_SortInfoList.get(position).isShare_goods_sort_click()){
            holder.tv_res_table_name.setTextColor(Color.parseColor("#ff0000"));
            tp.setFakeBoldText(true);//加粗
        }else {
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            tp.setFakeBoldText(false);
        }

    }
    public class HolderView extends RecyclerView.ViewHolder {
        TextView tv_res_table_name;
        Button btn_remove;
        Button btn_edit;
        RelativeLayout layout_res_table_sort_management;
        public HolderView(View itemView) {
            super(itemView);
            tv_res_table_name= (TextView) itemView.findViewById(R.id.tv_res_table_name);
            btn_remove= (Button) itemView.findViewById(R.id.btn_remove);
            btn_edit= (Button) itemView.findViewById(R.id.btn_edit);
            layout_res_table_sort_management= (RelativeLayout) itemView.findViewById(R.id.layout_res_table_sort_management);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnClick!=null){
                        msetOnClick.setOnClick(v,getPosition());
                    }
                }
            });
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnClick!=null){
                        msetOnClick.setOnClick(v,getPosition());
                    }
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnClick!=null){
                        msetOnClick.setOnClick(v,getPosition());
                    }
                }
            });
        }
    }
    public interface setOnClick{
        void setOnClick(View v,int position);
    }
    public setOnClick msetOnClick;
    public setOnClick setOnClick(setOnClick msetOnClick){
        return this.msetOnClick=msetOnClick;
    }
}
