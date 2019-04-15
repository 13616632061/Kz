package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Entty.Goods_Common_Notes;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/8/10.
 */

public class Goods_Common_Notes_Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Goods_Common_Notes> mCommonNotesList;

    public Goods_Common_Notes_Adapter(Context mContext,ArrayList<Goods_Common_Notes> mCommonNotesList) {
        this.mContext = mContext;
        this.mCommonNotesList = mCommonNotesList;
    }

    @Override
    public int getCount() {
        if(mCommonNotesList==null){
            return 0;
        }
        return mCommonNotesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommonNotesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_goods_common_notes,null);
            holderView=new HolderView();
            holderView.tv_notes= (TextView) convertView.findViewById(R.id.tv_notes);
            convertView.setTag(holderView);
        }else {
            holderView= (HolderView) convertView.getTag();
        }
        holderView.tv_notes.setText(mCommonNotesList.get(position).getNotes());
        return convertView;
    }
    private class HolderView{
        TextView tv_notes;
    }
}
