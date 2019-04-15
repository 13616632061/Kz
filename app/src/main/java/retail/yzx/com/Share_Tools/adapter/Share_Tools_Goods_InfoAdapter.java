package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retail.yzx.com.Share_Tools.SectionedRecyclerViewAdapter.SectionParameters;
import retail.yzx.com.Share_Tools.SectionedRecyclerViewAdapter.StatelessSection;
import retail.yzx.com.Share_Tools.entry.Share_GoodsInfo;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/9/11.
 */

public class Share_Tools_Goods_InfoAdapter extends StatelessSection {


    private Context mContext;
    private String title;
    private ArrayList<Share_GoodsInfo> mShare_GoodsInfoList;
    private DisplayImageOptions options;

    public Share_Tools_Goods_InfoAdapter(Context mContext,String title, ArrayList<Share_GoodsInfo> mShare_GoodsInfoList) {
        super(new SectionParameters.Builder(R.layout.item_share_tools_goods_info_view).
                headerResourceId(R.layout.self_res_headview).build());
        this.mContext = mContext;
        this.title = title;
        this.mShare_GoodsInfoList = mShare_GoodsInfoList;

        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
    }

    @Override
    public int getContentItemsTotal() {
        if (mShare_GoodsInfoList == null) {
            return 0;
        }
        return mShare_GoodsInfoList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
        ImageLoader.getInstance().displayImage(mShare_GoodsInfoList.get(position).getImage_default_url(),
                itemViewHolder.ivPicture,options);
        itemViewHolder.tvGoodsname.setText(mShare_GoodsInfoList.get(position).getName());
        itemViewHolder.tvPrice.setText(Utils.StringUtils.stringpointtwo(mShare_GoodsInfoList.get(position).getPrice()));

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder= (HeaderViewHolder) holder;
        headerViewHolder.tvSort.setText(title);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_goodsname)
        TextView tvGoodsname;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sort)
        TextView tvSort;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
