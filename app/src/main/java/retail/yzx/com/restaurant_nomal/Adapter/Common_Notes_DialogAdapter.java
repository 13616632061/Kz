package retail.yzx.com.restaurant_nomal.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

import Entty.Goods_Common_Notes;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/8/10.
 */

public class Common_Notes_DialogAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Goods_Common_Notes> mCommonNotesList;

    public Common_Notes_DialogAdapter(Context mContext, ArrayList<Goods_Common_Notes> mCommonNotesList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_dialog_common_notes,null);
            holderView=new HolderView();
            holderView.btn_common_notes= (Button) convertView.findViewById(R.id.btn_common_notes);
            convertView.setTag(holderView);
        }else {
            holderView= (HolderView) convertView.getTag();
        }
        holderView.btn_common_notes.setText(mCommonNotesList.get(position).getNotes());
        holderView.btn_common_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monBtnClick!=null){
                    monBtnClick.onBtnClick(position);
                }
            }
        });
        return convertView;
    }
    private class HolderView{
        Button btn_common_notes;
    }

    public interface onBtnClick {
        void onBtnClick(int position);
    }
    public onBtnClick monBtnClick;
    public void setonBtnClick(onBtnClick monBtnClick){
        this.monBtnClick=monBtnClick;
    }
}
