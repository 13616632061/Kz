package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retail.yzx.com.Share_Tools.entry.Share_Goods_Sort;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/9/9.
 */

public class Share_Tools_Goods_SortAdapter extends RecyclerView.Adapter<Share_Tools_Goods_SortAdapter.HolderView> {


    private Context mContext;
    private ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList;

    public Share_Tools_Goods_SortAdapter(Context mContext, ArrayList<Share_Goods_Sort> mShare_Goods_SortInfoList) {
        this.mContext = mContext;
        this.mShare_Goods_SortInfoList = mShare_Goods_SortInfoList;
    }

    @Override
    public int getItemCount() {
        if (mShare_Goods_SortInfoList == null) {
            return 0;
        } else {
            return mShare_Goods_SortInfoList.size();
        }
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_self_service_restanraunt_sorts, parent, false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.tvSortName.setText(mShare_Goods_SortInfoList.get(position).getType_name());
        if(mShare_Goods_SortInfoList.get(position).isShare_goods_sort_click()){
            holder.itemView.setBackgroundResource(R.color.white);
            holder.tvSortName.setTextColor(Color.parseColor("#ffff8905"));
        }else {
            holder.itemView.setBackgroundResource(R.drawable.select_click_grag);
            holder.tvSortName.setTextColor(Color.BLACK);
        }

    }

    public class HolderView extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sort_name)
        TextView tvSortName;
        public HolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
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
    public Share_Goods_SortAdapter.setOnClick msetOnClick;
    public Share_Goods_SortAdapter.setOnClick setOnClick(Share_Goods_SortAdapter.setOnClick msetOnClick){
        return this.msetOnClick=msetOnClick;
    }
}
