package retail.yzx.com.Share_Tools.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;

/**
 * Created by Administrator on 2017/9/27.
 */

public class Share_Orders_FragmentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Res_GoodsOrders> mShare_Tools_GoodsOrderslist;

    public Share_Orders_FragmentAdapter(Context mContext, ArrayList<Res_GoodsOrders> mShare_Tools_GoodsOrderslist) {
        this.mContext = mContext;
        this.mShare_Tools_GoodsOrderslist = mShare_Tools_GoodsOrderslist;
    }

    @Override
    public int getCount() {
        return mShare_Tools_GoodsOrderslist == null ? 0 : mShare_Tools_GoodsOrderslist.size();
    }

    @Override
    public Object getItem(int position) {
        return mShare_Tools_GoodsOrderslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            convertView = View.inflate(mContext, R.layout.item_share_orders_fragmentadapter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNums.setText((position + 1) + "");
        viewHolder.tvDopackage.setText(mShare_Tools_GoodsOrderslist.get(position).getOrder_id());
        viewHolder.tvOrdergoodsname.setText(mShare_Tools_GoodsOrderslist.get(position).getOrder_name());
        if (mShare_Tools_GoodsOrderslist.get(position).isOpen_goodnote()) {
            viewHolder.layoutNotesInfo.setVisibility(View.VISIBLE);
            viewHolder.btnOrdersnotes.setText("关闭");
        } else {
            viewHolder.layoutNotesInfo.setVisibility(View.GONE);
            viewHolder.btnOrdersnotes.setText("订单处理");
        }
        //订单处理
        viewHolder.btnOrdersnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShare_Tools_GoodsOrderslist.get(position).isOpen_goodnote()) {
                    for (int i = 0; i < mShare_Tools_GoodsOrderslist.size(); i++) {
                        mShare_Tools_GoodsOrderslist.get(i).setOpen_goodnote(false);
                    }
                } else {
                    for (int i = 0; i < mShare_Tools_GoodsOrderslist.size(); i++) {
                        mShare_Tools_GoodsOrderslist.get(i).setOpen_goodnote(false);
                    }
                    mShare_Tools_GoodsOrderslist.get(position).setOpen_goodnote(true);
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.btnOrderReceiptPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent("Share_Tools_Activity_Action").putExtra("type", 2).
                        putParcelableArrayListExtra("mShare_Tools_GoodsOrderslist", mShare_Tools_GoodsOrderslist).
                        putExtra("position", position));
            }
        });
        return convertView;
    }

    static

    public class ViewHolder {
        @BindView(R.id.tv_nums)
        TextView tvNums;
        @BindView(R.id.tv_dopackage)
        TextView tvDopackage;
        @BindView(R.id.tv_ordergoodsname)
        TextView tvOrdergoodsname;
        @BindView(R.id.tv_ordertime)
        TextView tvOrdertime;
        @BindView(R.id.btn_ordersnotes)
        Button btnOrdersnotes;
        @BindView(R.id.tv_notes)
        TextView tvNotes;
        @BindView(R.id.tv_notesinfo)
        EditText tvNotesinfo;
        @BindView(R.id.btn_add_ordernotes)
        Button btnAddOrdernotes;
        @BindView(R.id.btn_remove_order)
        Button btnRemoveOrder;
        @BindView(R.id.btn_order_receipt_print)
        Button btnOrderReceiptPrint;
        @BindView(R.id.layout_notes_info)
        RelativeLayout layoutNotesInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
