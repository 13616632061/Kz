package retail.yzx.com.restaurant_nomal.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by Administrator on 2017/8/2.
 */

public class SecondaryScreenAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo;

    public SecondaryScreenAdapter(Context mContext, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfo) {
        this.mContext = mContext;
        this.mSelf_Service_GoodsInfo = mSelf_Service_GoodsInfo;
    }

    @Override
    public int getCount() {
        if(mSelf_Service_GoodsInfo==null){
            return  0;
        }
        return mSelf_Service_GoodsInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelf_Service_GoodsInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.listfuping,null);
            holderView=new HolderView();
            holderView.tv_fuping1= (TextView) convertView.findViewById(R.id.tv_fuping1);
            holderView.tv_fuping2= (TextView) convertView.findViewById(R.id.tv_fuping2);
            holderView.tv_fuping3= (TextView) convertView.findViewById(R.id.tv_fuping3);
            holderView.tv_fuping4= (TextView) convertView.findViewById(R.id.tv_fuping4);
            convertView.setTag(holderView);
        }else {
            holderView= (HolderView) convertView.getTag();
        }
        holderView.tv_fuping1.setText(position+1+"");
        holderView.tv_fuping2.setText(mSelf_Service_GoodsInfo.get(position).getName());
        holderView.tv_fuping3.setText(mSelf_Service_GoodsInfo.get(position).getNumber());
        holderView.tv_fuping4.setText(mSelf_Service_GoodsInfo.get(position).getPrice());
        return convertView;
    }
    private class  HolderView{
        TextView tv_fuping1,tv_fuping2,tv_fuping3,tv_fuping4;

    }
}
