package retail.yzx.com.restaurant_self_service.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Entty.GoodsSortInfos;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/7/25.
 */

public class Self_Service_RestanrauntHideGoodsPictureLinkAdapter extends SectionedBaseAdapter {
    private Context mContext;
    private ArrayList<GoodsSortInfos> mGoodsSortInfosList;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList;
    private Self_Service_Rest_DialogAdapter mSelf_Service_Rest_DialogAdapter;
    private int cur_position;
    private int cur_section;
    private TextView tv_price;
    private TextView tv_notes;

    public Self_Service_RestanrauntHideGoodsPictureLinkAdapter(Context mContext, ArrayList<GoodsSortInfos> mGoodsSortInfosList, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList) {
        this.mContext = mContext;
        this.mGoodsSortInfosList = mGoodsSortInfosList;
        this.mSelf_Service_GoodsInfoList = mSelf_Service_GoodsInfoList;
    }

    @Override
    public Object getItem(int section, int position) {
        return mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mGoodsSortInfosList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return   (int)(Math.ceil((float)mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().size() / 6));
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
       Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_self_service_res_hidegoodspicture,null);
            holder=new Holder();
            holder.layout1= (RelativeLayout) convertView.findViewById(R.id.layout1);
            holder.layout2= (RelativeLayout) convertView.findViewById(R.id.layout2);
            holder.layout3= (RelativeLayout) convertView.findViewById(R.id.layout3);
            holder.layout4= (RelativeLayout) convertView.findViewById(R.id.layout4);
            holder.layout5= (RelativeLayout) convertView.findViewById(R.id.layout5);
            holder.layout6= (RelativeLayout) convertView.findViewById(R.id.layout6);
            holder.tv_price1= (TextView) convertView.findViewById(R.id.tv_price1);
            holder.tv_price2= (TextView) convertView.findViewById(R.id.tv_price2);
            holder.tv_price3= (TextView) convertView.findViewById(R.id.tv_price3);
            holder.tv_price4= (TextView) convertView.findViewById(R.id.tv_price4);
            holder.tv_price5= (TextView) convertView.findViewById(R.id.tv_price5);
            holder.tv_price6= (TextView) convertView.findViewById(R.id.tv_price6);
            holder.tv_goodsname1= (TextView) convertView.findViewById(R.id.tv_goodsname1);
            holder.tv_goodsname2= (TextView) convertView.findViewById(R.id.tv_goodsname2);
            holder.tv_goodsname3= (TextView) convertView.findViewById(R.id.tv_goodsname3);
            holder.tv_goodsname4= (TextView) convertView.findViewById(R.id.tv_goodsname4);
            holder.tv_goodsname5= (TextView) convertView.findViewById(R.id.tv_goodsname5);
            holder.tv_goodsname6= (TextView) convertView.findViewById(R.id.tv_goodsname6);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        setDataInfo(section,6*position,holder.layout1,holder.tv_price1,holder.tv_goodsname1);
        setDataInfo(section,6*position+1,holder.layout2,holder.tv_price2,holder.tv_goodsname2);
        setDataInfo(section,6*position+2,holder.layout3,holder.tv_price3,holder.tv_goodsname3);
        setDataInfo(section,6*position+3,holder.layout4,holder.tv_price4,holder.tv_goodsname4);
        setDataInfo(section,6*position+4,holder.layout5,holder.tv_price5,holder.tv_goodsname5);
        setDataInfo(section,6*position+5,holder.layout6,holder.tv_price6,holder.tv_goodsname6);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
       HeadHolder headHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.self_res_headview,null);
            headHolder=new HeadHolder();
            headHolder.textItem= (TextView) convertView.findViewById(R.id.tv_sort);
            convertView.setTag(headHolder);
        }else {
            headHolder= (HeadHolder) convertView.getTag();
        }
        convertView.setClickable(false);
        headHolder.textItem.setText(mGoodsSortInfosList.get(section).getSort_name());
        return convertView;
    }
    private class HeadHolder{
        TextView textItem;
    }
    private class Holder{
        RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6;
        TextView tv_price1,tv_price2,tv_price3,tv_price4,tv_price5,tv_price6;
        TextView tv_goodsname1,tv_goodsname2,tv_goodsname3,tv_goodsname4,tv_goodsname5,tv_goodsname6;
    }
    /**
     * 设置item数据
     * @param position
     * @param layout
     * @param price
     * @param name
     */
    private void setDataInfo(final int section, final int position, final RelativeLayout layout, TextView price, TextView name){
        if(position< mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().size()){
            layout.setVisibility(View.VISIBLE);
            //加载图片
            price.setText("￥"+ Utils.StringUtils.stringpointtwo( mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getPrice()));
            name.setText(mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getName());

        }else {
            layout.setVisibility(View.INVISIBLE);
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur_position=position;
                cur_section=section;
                if(mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsStandardInfosList().size()>0){
                    //规格商品
                    setStandrandDialog(layout,section,position);
                }else{
                    //无规格商品
                    String goods_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getGoods_id();
                    String tag_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getTag_id();
                    String goods_name=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getName();
                    String goods_price=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getPrice().trim().replace("￥","").trim();
                    String product_id="";
                    for(int i=0;i<mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists().size();i++){
                            product_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists().get(i).getProduct_id();
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
                    setDismiss(section,position);
                }
            }
        });
    }
    private View view;
    private PopupWindow popupWindow;
    private void setStandrandDialog(RelativeLayout layout, final int section, final int position){
        if(popupWindow==null) {
            mContext.registerReceiver(broadcastReceiver,new IntentFilter("Self_Service_RestanrauntHideGoodsPictureLinkAdapter.Action"));
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
                            setDismiss(section,position);
                        }
                        if (x < weight || x > weight_right) {
                            setDismiss(section,position);
                        }
                    }
                    return true;
                }
            });
            //商品名字
            tv_goodsname.setText(mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getName());
            //规格信息
            mSelf_Service_Rest_DialogAdapter = new Self_Service_Rest_DialogAdapter(mContext, mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsStandardInfosList(),
                    mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists());
            list_content.setAdapter(mSelf_Service_Rest_DialogAdapter);
            //加入订单
            btn_addorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String goods_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getGoods_id();
                    String tag_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getTag_id();
                    String goods_name=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getName();
                    String goods_price=tv_price.getText().toString().trim().replace("￥","").trim();
                    String goods_size=tv_notes.getText().toString().trim();
                    String product_id="";
                    for(int i=0;i<mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists().size();i++){
                        if(mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists().get(i).getIs_default().equals("true")){
                            product_id=mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsNotesLists().get(i).getProduct_id();
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
                    setDismiss(section,position);
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDismiss(section,position);
                }
            });

        }

    }
    //弹窗消失
    private void setDismiss(int section ,int position){
        if (popupWindow != null) {
            popupWindow.dismiss();
            //初始化选择的规格
            for(int i=0;i< mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsStandardInfosList().size();i++){
                for(int j=0;j<mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().size();j++){
                    mGoodsSortInfosList.get(section).getmSelf_Service_Restanraunt_GoodsInfoList().get(position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().get(j).setSelect(false);
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
            Log.e("barcode", "数据: "+mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsNotesLists());
            for(int t=0;t< mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsNotesLists().size();t++) {
                int isTrue=0;
                String notes="";
                for (int i = 0; i < mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().size(); i++) {
                    for (int j = 0; j < mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().size(); j++) {
                        if (mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().get(j).isSelect()) {
                            if( mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard().
                                    indexOf(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().
                                            get(i).getmGoodsStandardList().get(j).getSpec_value())!=-1){
                                isTrue++;
                                //必须在同一个规格属性
                                notes+=mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().
                                        get(i).getmGoodsStandardList().get(j).getSpec_value()+" ";
                                if(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().size()>1&&isTrue>1){
                                    tv_price.setText("￥"+ Utils.StringUtils.stringpointtwo(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
//                                    tv_notes.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard());
                                    tv_notes.setText(notes);
                                    break;
                                }else if(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().size()==1&&isTrue==1){
                                    tv_price.setText("￥"+ Utils.StringUtils.stringpointtwo(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
//                                    tv_notes.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard());
                                    tv_notes.setText(notes);
                                    break;

                                }
                            }else
                                isTrue=0;
                        }
                    }
                }
                if(mGoodsSortInfosList.get(cur_section).getmSelf_Service_Restanraunt_GoodsInfoList().get(cur_position).getmGoodsStandardInfosList().size()>1&&isTrue>1){
                    break;
                }
            }
        }
    };
}
