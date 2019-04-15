package retail.yzx.com.restaurant_self_service.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Entty.Self_Service_RestanrauntGoodsInfo;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/7/18.
 */

public class Self_Service_RestanrauntGoodsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList;
    private DisplayImageOptions options;
    private Self_Service_Rest_DialogAdapter mSelf_Service_Rest_DialogAdapter;
    private int cur_position;
    private TextView tv_price;
    private TextView tv_notes;

    public Self_Service_RestanrauntGoodsAdapter(Context mContext, ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList) {
        this.mContext = mContext;
        this.mSelf_Service_Restanraunt_GoodsInfoList = mSelf_Service_Restanraunt_GoodsInfoList;
        this.mSelf_Service_GoodsInfoList = mSelf_Service_GoodsInfoList;
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.picture_default)
                .showImageForEmptyUri(R.drawable.picture_default)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
    }


    @Override
    public int getCount() {
        if(mSelf_Service_Restanraunt_GoodsInfoList ==null){
            return 0;
        }
        return (int)(Math.ceil((float) mSelf_Service_Restanraunt_GoodsInfoList.size() / 4));
    }

    @Override
    public Object getItem(int position) {
        return mSelf_Service_Restanraunt_GoodsInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_self_service_restanraunt,null);
            holder=new Holder();
            holder.layout1= (RelativeLayout) convertView.findViewById(R.id.layout1);
            holder.layout2= (RelativeLayout) convertView.findViewById(R.id.layout2);
            holder.layout3= (RelativeLayout) convertView.findViewById(R.id.layout3);
            holder.layout4= (RelativeLayout) convertView.findViewById(R.id.layout4);
            holder.iv_picture1= (ImageView) convertView.findViewById(R.id.iv_picture1);
            holder.iv_picture2= (ImageView) convertView.findViewById(R.id.iv_picture2);
            holder.iv_picture3= (ImageView) convertView.findViewById(R.id.iv_picture3);
            holder.iv_picture4= (ImageView) convertView.findViewById(R.id.iv_picture4);
            holder.tv_price1= (TextView) convertView.findViewById(R.id.tv_price1);
            holder.tv_price2= (TextView) convertView.findViewById(R.id.tv_price2);
            holder.tv_price3= (TextView) convertView.findViewById(R.id.tv_price3);
            holder.tv_price4= (TextView) convertView.findViewById(R.id.tv_price4);
            holder.tv_goodsname1= (TextView) convertView.findViewById(R.id.tv_goodsname1);
            holder.tv_goodsname2= (TextView) convertView.findViewById(R.id.tv_goodsname2);
            holder.tv_goodsname3= (TextView) convertView.findViewById(R.id.tv_goodsname3);
            holder.tv_goodsname4= (TextView) convertView.findViewById(R.id.tv_goodsname4);
            holder.btn_nums1= (Button) convertView.findViewById(R.id.btn_nums1);
            holder.btn_nums2= (Button) convertView.findViewById(R.id.btn_nums2);
            holder.btn_nums3= (Button) convertView.findViewById(R.id.btn_nums3);
            holder.btn_nums4= (Button) convertView.findViewById(R.id.btn_nums4);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        setDataInfo(4*position,holder.iv_picture1,holder.layout1,holder.tv_price1,holder.tv_goodsname1,holder.btn_nums1);
        setDataInfo(4*position+1,holder.iv_picture2,holder.layout2,holder.tv_price2,holder.tv_goodsname2,holder.btn_nums2);
        setDataInfo(4*position+2,holder.iv_picture3,holder.layout3,holder.tv_price3,holder.tv_goodsname3,holder.btn_nums3);
        setDataInfo(4*position+3,holder.iv_picture4,holder.layout4,holder.tv_price4,holder.tv_goodsname4,holder.btn_nums4);
        return convertView;
    }
    private class Holder{
        RelativeLayout layout1,layout2,layout3,layout4;
        ImageView iv_picture1,iv_picture2,iv_picture3,iv_picture4;
        TextView tv_price1,tv_price2,tv_price3,tv_price4;
        TextView tv_goodsname1,tv_goodsname2,tv_goodsname3,tv_goodsname4;
        Button btn_nums1,btn_nums2,btn_nums3,btn_nums4;
    }

    /**
     * 设置item数据
     * @param position
     * @param imageView
     * @param layout
     * @param price
     * @param name
     * @param btn_nums
     */
    private void setDataInfo(final int position, ImageView imageView, final RelativeLayout layout, TextView price, TextView name, final Button btn_nums){
        if(position< mSelf_Service_Restanraunt_GoodsInfoList.size()){
            layout.setVisibility(View.VISIBLE);
            //加载图片
            ImageLoader.getInstance().displayImage(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getImage_default_id(),imageView,options);
            price.setText("￥"+ Utils.StringUtils.stringpointtwo( mSelf_Service_Restanraunt_GoodsInfoList.get(position).getPrice()));
            name.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName());

        }else {
            layout.setVisibility(View.INVISIBLE);
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_position=position;
                if(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList().size()>0){
                    //规格商品
                    setStandrandDialog(layout,position);
                }else{
                    //无规格商品
                    String goods_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getGoods_id();
                    String tag_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getTag_id();
                    String goods_name=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName();
                    String goods_price=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getPrice().trim().replace("￥","").trim();
                    String product_id="";
                    for(int i=0;i<mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().size();i++){
                            product_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getProduct_id();
                    }
                    boolean isHas=false;
                    for(int i=0;i<mSelf_Service_GoodsInfoList.size();i++){
                        if(mSelf_Service_GoodsInfoList.get(i).getGoods_id().equals(goods_id)){
                            String nums_src= mSelf_Service_GoodsInfoList.get(i).getNumber();
                            int nums=Integer.parseInt(nums_src);
                            nums++;
                            mSelf_Service_GoodsInfoList.get(i).setNumber(nums+"");
                            isHas=true;
                            break;
                        }else {
                            isHas=false;
                        }
                    }
                    if(!isHas){
                        Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,goods_name,"1","",goods_price,"","",product_id,tag_id);
                        mSelf_Service_GoodsInfoList.add(mSelf_Service_GoodsInfo);
                    }
                    mContext.sendBroadcast(new Intent("Self_Service_RestanrauntActivity.Action").putExtra("type",1));
                    setDismiss(position);
                }
            }
        });
    }
    private View view;
    private PopupWindow popupWindow;
    private void setStandrandDialog(RelativeLayout layout, final int position){
        if(popupWindow==null) {
            mContext.registerReceiver(broadcastReceiver,new IntentFilter("Self_Service_RestanrauntGoodsAdapter.Action"));
            view = View.inflate(mContext, R.layout.layout_self_service_restanraunt_standrand_dialog, null);
            LinearLayout layout_dialog = (LinearLayout) view.findViewById(R.id.layout_dialog);
            TextView tv_goodsname = (TextView) view.findViewById(R.id.tv_goodsname);
             tv_price = (TextView) view.findViewById(R.id.tv_price);
             tv_notes = (TextView) view.findViewById(R.id.tv_notes);
            ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
            Button btn_addorder = (Button) view.findViewById(R.id.btn_addorder);
            ListView list_content = (ListView) view.findViewById(R.id.list_content);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable mColorDrawable = new ColorDrawable(0x20000000);
            //设置弹框的背景
            popupWindow.setBackgroundDrawable(mColorDrawable);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.layout_dialog).getBottom();
                    int height_top = view.findViewById(R.id.layout_dialog).getTop();
                    int weight = view.findViewById(R.id.layout_dialog).getLeft();
                    int weight_right = view.findViewById(R.id.layout_dialog).getRight();
                    int y = (int) event.getY();
                    int x = (int) event.getX();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y > height || y < height_top) {
                            setDismiss(position);
                        }
                        if (x < weight || x > weight_right) {
                            setDismiss(position);
                        }
                    }
                    return true;
                }
            });
            //商品名字
            tv_goodsname.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName());
            //规格信息
            mSelf_Service_Rest_DialogAdapter = new Self_Service_Rest_DialogAdapter(mContext, mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList(),
                    mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists());
            list_content.setAdapter(mSelf_Service_Rest_DialogAdapter);
            //加入订单
            btn_addorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String goods_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getGoods_id();
                    String tag_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getTag_id();
                    String goods_name=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName();
                    String goods_price=tv_price.getText().toString().trim().replace("￥","").trim();
                    String goods_size=tv_notes.getText().toString().trim();
                    String product_id="";
                    for(int i=0;i<mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().size();i++){
                        if(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getIs_default().equals("true")){
                            product_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getProduct_id();
                        }
                    }
                    boolean isHas=false;
                    for(int i=0;i<mSelf_Service_GoodsInfoList.size();i++){
                        if(mSelf_Service_GoodsInfoList.get(i).getGoods_id().equals(goods_id)&&
                                mSelf_Service_GoodsInfoList.get(i).getNotes().equals(goods_size)){
                           String nums_src= mSelf_Service_GoodsInfoList.get(i).getNumber();
                            int nums=Integer.parseInt(nums_src);
                            nums++;
                            mSelf_Service_GoodsInfoList.get(i).setNumber(nums+"");
                            isHas=true;
                            break;
                        }else {
                            isHas=false;
                        }
                    }
                    if(!isHas){
                        Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,goods_name,"1","",goods_price,"",goods_size,product_id,tag_id);
                        mSelf_Service_GoodsInfoList.add(mSelf_Service_GoodsInfo);
                    }
                    mContext.sendBroadcast(new Intent("Self_Service_RestanrauntActivity.Action").putExtra("type",1));
                    setDismiss(position);
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDismiss(position);
                }
            });

        }

    }
    //弹窗消失
    private void setDismiss(int position){
        if (popupWindow != null) {
            popupWindow.dismiss();
            //初始化选择的规格
            for(int i=0;i< mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList().size();i++){
                for(int j=0;j< mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().size();j++){
                    mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().get(j).setSelect(false);
                }
            }
            popupWindow = null;
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("barcode", "计算价格: ");
            //每点击一个规格按钮，都需要重新设定规格所对应的价格
            Log.e("barcode", "cur_position: "+cur_position);
            Log.e("barcode", "数据: "+mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists());
            for(int t=0;t< mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().size();t++) {
                int isTrue=0;
                String notes="";
                for (int i = 0; i < mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size(); i++) {
                    for (int j = 0; j < mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().size(); j++) {
                        if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().get(j).isSelect()) {
                            if( mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard().
                                    indexOf(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().
                                            get(i).getmGoodsStandardList().get(j).getSpec_value())!=-1){
                                isTrue++;
                                //必须在同一个规格属性
                                notes+=mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().
                                        get(i).getmGoodsStandardList().get(j).getSpec_value()+" ";
                                if(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size()>1&&isTrue>1){
                                    tv_price.setText("￥"+ Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
//                                    tv_notes.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard());
                                    tv_notes.setText(notes);
                                    break;
                                }else if(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size()==1&&isTrue==1){
                                    tv_price.setText("￥"+ Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
//                                    tv_notes.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard());
                                    tv_notes.setText(notes);
                                    break;

                                }
                            }else
                                isTrue=0;
                        }
                    }
                }
                if(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size()>1&&isTrue>1){
                    break;
                }
            }
        }
    };
}
