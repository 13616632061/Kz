package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retail.yzx.com.Share_Tools.entry.Share_GoodsInfo;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * Created by Administrator on 2017/9/9.
 */

public class Share_Goods_InfoAdapter extends RecyclerView.Adapter<Share_Goods_InfoAdapter.HolderView> {

    private Context mContext;
    private ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;

    public Share_Goods_InfoAdapter(Context mContext, ArrayList<Share_GoodsInfo> mShare_GoodsInfoList) {
        this.mContext = mContext;
        this.mShare_GoodsInfoList = mShare_GoodsInfoList;
    }

    @Override
    public int getItemCount() {
        if(mShare_GoodsInfoList==null){
            return 0;
        }else {
            return mShare_GoodsInfoList.size();
        }
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(mContext, R.layout.item_share_goods_view, null);
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_share_goods_view,parent,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.tvBn.setText(mShare_GoodsInfoList.get(position).getBn());
        holder.tvSharegoodsname.setText(mShare_GoodsInfoList.get(position).getName());
        holder.tvStore.setText(mShare_GoodsInfoList.get(position).getStore());
        holder.tvPrice.setText(Utils.StringUtils.stringpointtwo(mShare_GoodsInfoList.get(position).getPrice()));
        if("true".equals(mShare_GoodsInfoList.get(position).getStatus())){
            holder.tvStatus.setText("空闲");
        }else {
            holder.tvStatus.setText("使用中");
        }


    }


    public class HolderView extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_bn)
        TextView tvBn;
        @BindView(R.id.tv_sharegoodsname)
        TextView tvSharegoodsname;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.btn_edit)
        Button btnEdit;
        @BindView(R.id.btn_del)
        Button btnDel;
        public HolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnClick!=null){
                        msetOnClick.setOnClick(v,getPosition());
                    }
                }
            });
            btnDel.setOnClickListener(new View.OnClickListener() {
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
        void setOnClick(View view,int position);
    }
    public static setOnClick msetOnClick;
    public void setOnClick(setOnClick msetOnClick){
        this.msetOnClick=msetOnClick;
    }
}
