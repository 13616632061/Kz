package retail.yzx.com.restaurant_self_service.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Map;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.PreferencesService;
import retail.yzx.com.restaurant_self_service.Entty.Self_Service_RestanrauntGoodsInfo;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/7/22.
 */

public class Self_Service_RestanrauntHideGoodsPictureAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList=new ArrayList<>();
    private ArrayList<Self_Service_GoodsInfo> mActivity_Self_Service_GoodsInfoList=new ArrayList<>();
    private Self_Service_Rest_DialogAdapter mSelf_Service_Rest_DialogAdapter;
    private int cur_position;
    private TextView tv_price;
    private TextView tv_notes;
    private PreferencesService mPreferencesService;//获取商品是否可以被点击的状态
    private boolean Goods_isClick=true;

    public Self_Service_RestanrauntHideGoodsPictureAdapter(Context mContext) {
        this.mContext = mContext;
        mPreferencesService=new PreferencesService(mContext);
        mContext.registerReceiver(broadcastReceiver,new IntentFilter("Self_Service_RestanrauntHideGoodsPictureAdapter.Action"));
    }


    /**
     *
     */
    public void setAdatas( ArrayList<Self_Service_RestanrauntGoodsInfo> mSelf_Service_Restanraunt_GoodsInfoList, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfoList){
        this.mSelf_Service_Restanraunt_GoodsInfoList = mSelf_Service_Restanraunt_GoodsInfoList;
//        this.mSelf_Service_GoodsInfoList = mSelf_Service_GoodsInfoList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if(mSelf_Service_Restanraunt_GoodsInfoList ==null){
            return 0;
        }
        return (int)(Math.ceil((float) mSelf_Service_Restanraunt_GoodsInfoList.size() / 5));
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
            convertView=View.inflate(mContext, R.layout.item_res_hidegoodspicture,null);
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
        setDataInfo(5*position,holder.layout1,holder.tv_price1,holder.tv_goodsname1);
        setDataInfo(5*position+1,holder.layout2,holder.tv_price2,holder.tv_goodsname2);
        setDataInfo(5*position+2,holder.layout3,holder.tv_price3,holder.tv_goodsname3);
        setDataInfo(5*position+3,holder.layout4,holder.tv_price4,holder.tv_goodsname4);
        setDataInfo(5*position+4,holder.layout5,holder.tv_price5,holder.tv_goodsname5);
        return convertView;
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
    private void setDataInfo(final int position, final RelativeLayout layout, TextView price, TextView name){
        if(position< mSelf_Service_Restanraunt_GoodsInfoList.size()){
            layout.setVisibility(View.VISIBLE);
            price.setText("￥"+ Utils.StringUtils.stringpointtwo( mSelf_Service_Restanraunt_GoodsInfoList.get(position).getPrice()));
            name.setText(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName());

        }else {
            layout.setVisibility(View.INVISIBLE);
        }
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> goods_isClick = mPreferencesService.getGoodsIsClick();
                    Goods_isClick = Boolean.valueOf(goods_isClick.get("icClick"));
                    if (Goods_isClick) {
                        cur_position = position;
                        if (mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsStandardInfosList().size() > 0) {
                            //规格商品
                            setStandrandDialog(layout, position);
                        } else {
                            //无规格商品
                            String goods_id = mSelf_Service_Restanraunt_GoodsInfoList.get(position).getGoods_id();
                            String goods_name = mSelf_Service_Restanraunt_GoodsInfoList.get(position).getName();
                            String tag_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getTag_id();
                            String product_id="";

                            for(int i=0;i<mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().size();i++){
                                    product_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getProduct_id();
                            }
                            String goods_price = mSelf_Service_Restanraunt_GoodsInfoList.get(position).getPrice().trim().replace("￥", "").trim();
                            boolean isHas = false;

                            mSelf_Service_GoodsInfoList.clear();
                            for (int i = 0; i < mSelf_Service_GoodsInfoList.size(); i++) {
                                if (mSelf_Service_GoodsInfoList.get(i).getGoods_id().equals(goods_id)) {
                                    String nums_src = mSelf_Service_GoodsInfoList.get(i).getNumber();
                                    int nums = Integer.parseInt(nums_src);
                                    nums++;
                                    mSelf_Service_GoodsInfoList.get(i).setNumber(nums + "");
                                    isHas = true;
                                    break;
                                } else {
                                    isHas = false;
                                }
                            }
                            if (!isHas) {
                                Self_Service_GoodsInfo mSelf_Service_GoodsInfo = new Self_Service_GoodsInfo(goods_id, goods_name, "1", "", goods_price, "","",product_id,tag_id);
                                mSelf_Service_GoodsInfoList.add(mSelf_Service_GoodsInfo);
                            }
                            Log.d("print","打印广播的大小"+mSelf_Service_GoodsInfoList.size());
                            mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 1).putParcelableArrayListExtra("mSelf_Service_GoodsInfoList",mSelf_Service_GoodsInfoList));
                            setDismiss(position);
                        }
                    }
                }
            });
    }
    private View view;
    private PopupWindow popupWindow;
    private void setStandrandDialog(RelativeLayout layout, final int position){
        if(popupWindow==null) {

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
                    String goods_size_pre="";
                    String goods_size_last="";

                    Log.d("print","打印的产品"+goods_size);
                    Log.d("print","打印的产品"+goods_size.length());
                    Log.d("print","打印的产品"+goods_size.indexOf(","));

                    if (goods_size.indexOf(",")>0){
                        goods_size_pre=goods_size.substring(0,goods_size.indexOf(","));
                    }
                    if (goods_size.indexOf(",")<goods_size.length()&&goods_size.indexOf(",")>0){
                        goods_size_last=goods_size.substring(goods_size.indexOf(","),goods_size.length());
                    }
                    String product_id="";
                    //获取规格信息的产品id
                    for(int i=0;i<mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().size();i++){
                        if(mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getSpec_info_standard().indexOf(goods_size_pre.trim())!=-1&&
                                mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getSpec_info_standard().indexOf(goods_size_last.trim())!=-1){
                            product_id=mSelf_Service_Restanraunt_GoodsInfoList.get(position).getmGoodsNotesLists().get(i).getProduct_id();
                        }
                    }
                    boolean isHas=false;
                    for(int i=0;i<mSelf_Service_GoodsInfoList.size();i++){
                        if(mSelf_Service_GoodsInfoList.get(i).getGoods_id().equals(goods_id)&&
                                mSelf_Service_GoodsInfoList.get(i).getSize().equals(goods_size)){
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
                    mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",1).putParcelableArrayListExtra("mSelf_Service_GoodsInfoList",mSelf_Service_GoodsInfoList));
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
        }
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
                    if(type==1){
//                        mSelf_Service_GoodsInfoList=intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfo");
//                        mSelf_Service_Restanraunt_GoodsInfoList=intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfo");
                    }else if(type==2){
                        Log.e("print","打印商品数据的大小"+mSelf_Service_GoodsInfoList.size());
                        mSelf_Service_GoodsInfoList.clear();
                    }else {
                            //每点击一个规格按钮，都需要重新设定规格所对应的价格
                            for (int t = 0; t < mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().size(); t++) {
                                int isTrue = 0;
                                String notes = "";
                                for (int i = 0; i < mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size(); i++) {
                                    for (int j = 0; j < mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().size(); j++) {
                                        if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().get(i).getmGoodsStandardList().get(j).isSelect()) {
                                            if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getSpec_info_standard().
                                                    indexOf(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().
                                                            get(i).getmGoodsStandardList().get(j).getSpec_value()) != -1) {
                                                isTrue++;
                                                //必须在同一个规格属性
                                                notes += mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().
                                                        get(i).getmGoodsStandardList().get(j).getSpec_value() + ",";
                                                if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size() > 1 && isTrue > 1) {
                                                    if(!TextUtils.isEmpty(Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()))){
                                                        tv_price.setText("￥" + Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
                                                    }
                                                    tv_notes.setText(notes);
                                                    break;
                                                } else if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size() == 1 && isTrue == 1) {
                                                    if(!TextUtils.isEmpty(Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()))){
                                                        tv_price.setText("￥" + Utils.StringUtils.stringpointtwo(mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsNotesLists().get(t).getPrice_standard()));
                                                    }
                                                    tv_notes.setText(notes);
                                                    break;

                                                }
                                            } else
                                                isTrue = 0;
                                        }
                                    }
                                }
                                if (mSelf_Service_Restanraunt_GoodsInfoList.get(cur_position).getmGoodsStandardInfosList().size() > 1 && isTrue > 1) {
                                    break;
                                }
                            }
                    }
        }
    };
}
