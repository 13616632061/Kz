package retail.yzx.com.restaurant_nomal.Adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.PreferencesService;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/7/28.
 */

public class Res_Nomal_PalyOrderAdapter extends BaseSwipListAdapter {
    private Activity mContext;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist;
    private boolean isClick;
    private PreferencesService mPreferencesService;//保存商品是否可以被点击的状态


    public Res_Nomal_PalyOrderAdapter(Activity mContext, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist, boolean isClick) {
        this.mContext = mContext;
        this.mSelf_Service_GoodsInfolist = mSelf_Service_GoodsInfolist;
        this.isClick = isClick;

        mPreferencesService=new PreferencesService(mContext);
    }
    //获取数据栏是否可以被编辑
    public boolean getisClick(){
        return isClick;
    }

    public void setClick(boolean click) {
        mPreferencesService.setGoodsIsClick(click);
        isClick = click;
    }

    @Override
    public int getCount() {
        if(mSelf_Service_GoodsInfolist==null){
            return 0;
        }
        return mSelf_Service_GoodsInfolist.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelf_Service_GoodsInfolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_res_nomal_palyorder,null);
            holderView=new HolderView();
            holderView.tv_goodsname= (TextView) convertView.findViewById(R.id.tv_goodsname);
            holderView.tv_goodsprice= (TextView) convertView.findViewById(R.id.tv_goodsprice);
            holderView.tv_goodsnums= (TextView) convertView.findViewById(R.id.tv_goodsnums);
            holderView.tv_goodsnotes= (TextView) convertView.findViewById(R.id.tv_goodsnotes);
            holderView.tv_goods_size= (TextView) convertView.findViewById(R.id.tv_goods_size);
            convertView.setTag(holderView);
        }else {
            holderView= (HolderView) convertView.getTag();
        }
            holderView.tv_goodsname.setText(mSelf_Service_GoodsInfolist.get(position).getName());
            holderView.tv_goodsprice.setText(Float.parseFloat(mSelf_Service_GoodsInfolist.get(position).getPrice())+"");
            holderView.tv_goodsnums.setText(mSelf_Service_GoodsInfolist.get(position).getNumber());
        if(TextUtils.isEmpty(mSelf_Service_GoodsInfolist.get(position).getSize())||"null".equals(mSelf_Service_GoodsInfolist.get(position).getSize())||
                mSelf_Service_GoodsInfolist.get(position).getSize()=="null"){
            holderView.tv_goods_size.setText("");
        }else {
            holderView.tv_goods_size.setText(mSelf_Service_GoodsInfolist.get(position).getSize());
        }
        if(TextUtils.isEmpty(mSelf_Service_GoodsInfolist.get(position).getNotes())||"null".equals(mSelf_Service_GoodsInfolist.get(position).getNotes())||
                mSelf_Service_GoodsInfolist.get(position).getNotes()=="null"){
            holderView.tv_goodsnotes.setText("");
        }else {
            holderView.tv_goodsnotes.setText(mSelf_Service_GoodsInfolist.get(position).getNotes());
        }

        return convertView;
    }
    private class HolderView{
        TextView tv_goodsname;
        TextView tv_goodsprice;
        TextView tv_goodsnums;
        TextView tv_goodsnotes;
        TextView tv_goods_size;
    }
}
