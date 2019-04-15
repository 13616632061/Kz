package retail.yzx.com.restaurant_self_service.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_self_service.Entty.GoodsNotes;
import retail.yzx.com.restaurant_self_service.Entty.GoodsStandardInfos;

/**
 * Created by Administrator on 2017/7/20.
 */

public class Self_Service_Rest_DialogAdapter extends SectionedBaseAdapter {
    private Context mContext;
    private ArrayList<GoodsStandardInfos> mGoodsStandardInfosList;//商品规格信息
    private ArrayList<GoodsNotes> mGoodsNotes;
    private boolean isFirst=false;
    private int nums=0;

    public Self_Service_Rest_DialogAdapter(Context mContext, ArrayList<GoodsStandardInfos> mGoodsStandardInfosList, ArrayList<GoodsNotes> mGoodsNotes) {
        this.mContext = mContext;
        this.mGoodsStandardInfosList = mGoodsStandardInfosList;
        this.mGoodsNotes = mGoodsNotes;
        isFirst=true;
        nums=0;
    }

    @Override
    public Object getItem(int section, int position) {
        return mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mGoodsStandardInfosList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return  (int)(Math.ceil((float) mGoodsStandardInfosList.get(section).getmGoodsStandardList().size() / 3));
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.self_res_dialog_popwindow_,null);
            holder=new Holder();
            holder.btn_standard1= (Button) convertView.findViewById(R.id.btn_standard1);
            holder.btn_standard2= (Button) convertView.findViewById(R.id.btn_standard2);
            holder.btn_standard3= (Button) convertView.findViewById(R.id.btn_standard3);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        setItem(holder.btn_standard1,section,3*position);
        setItem(holder.btn_standard2,section,3*position+1);
        setItem(holder.btn_standard3,section,3*position+2);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        HeadHolder headHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.head_item,null);
            headHolder=new HeadHolder();
            headHolder.textItem= (TextView) convertView.findViewById(R.id.textItem);
            convertView.setTag(headHolder);
        }else {
            headHolder= (HeadHolder) convertView.getTag();
        }
        convertView.setClickable(false);
        headHolder.textItem.setText(mGoodsStandardInfosList.get(section).getGoods_standard());
        return convertView;
    }
    private class HeadHolder{
        TextView textItem;
    }
    private class Holder{
        Button btn_standard1,btn_standard2,btn_standard3;
    }
    private void setItem(final Button btn, final int section , final int position){
        if(position<mGoodsStandardInfosList.get(section).getmGoodsStandardList().size()){
            btn.setVisibility(View.VISIBLE);
            btn.setText(mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position).getSpec_value());
//第一次弹出弹窗，显示默认的规格
            if(isFirst){
            for(int i=0;i<mGoodsNotes.size();i++){
                if("true".equals(mGoodsNotes.get(i).getIs_default())){
                    if(mGoodsNotes.get(i).getSpec_info_standard().
                            indexOf(mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position).getSpec_value())!=-1){
                        mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position).setSelect(true);
                        notifyDataSetChanged();
                        nums++;
                        if(nums>=mGoodsStandardInfosList.size()){
                            isFirst=false;
                        }
                    }
                }
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntGoodsAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntGoodsLinkAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureLinkAdapter.Action"));
            }
        }
            if( mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position).isSelect()){
                btn.setBackgroundResource(R.drawable.btn_corner_orange);

            }else{
                btn.setBackgroundResource(R.drawable.btn_select_gray);
            }
        }else {
            btn.setVisibility(View.INVISIBLE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i< mGoodsStandardInfosList.get(section).getmGoodsStandardList().size();i++){
                    if( mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(i).isSelect()){
                        mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(i).setSelect(false);
                    }
                }
                mGoodsStandardInfosList.get(section).getmGoodsStandardList().get(position).setSelect(true);
                notifyDataSetChanged();
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntGoodsAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntGoodsLinkAdapter.Action"));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntHideGoodsPictureLinkAdapter.Action"));
            }
        });
    }
}
