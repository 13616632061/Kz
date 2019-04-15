package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Entty.Res_Table_Sort;
import retail.yzx.com.kz.R;

/**
 * Created by Administrator on 2017/8/12.
 * 餐桌分类--管理页面
 */

public class Res_Table_SortAdapter2 extends RecyclerView.Adapter<Res_Table_SortAdapter2.ViewHolder> {

    private Context mContext;
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;
    private boolean res_table_sort_edit=false;

    public Res_Table_SortAdapter2(Context mContext, ArrayList<Res_Table_Sort> mRes_Table_SortList) {
        this.mContext = mContext;
        this.mRes_Table_SortList = mRes_Table_SortList;
    }
    //c餐桌分类编辑
    public boolean setRes_table_sort_edit(boolean res_table_sort_edit){
        return this.res_table_sort_edit=res_table_sort_edit;
    }
    @Override
    public int getItemCount() {
        return mRes_Table_SortList.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(mContext, R.layout.item_res_table_sort,null);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_res_table_name.setText(mRes_Table_SortList.get(position).getRes_table_sort_name());
        if(res_table_sort_edit){
            holder.layout_res_table_sort_management.setVisibility(View.VISIBLE);
        }else {
            holder.layout_res_table_sort_management.setVisibility(View.GONE);
        }
        TextPaint tp=holder.tv_res_table_name.getPaint();//设置字体画笔
        if(mRes_Table_SortList.get(position).isRes_table_sort_click()){
            holder.tv_res_table_name.setTextColor(Color.parseColor("#ff0000"));
            tp.setFakeBoldText(true);//加粗
        }else {
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            tp.setFakeBoldText(false);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_res_table_name;
        Button btn_remove;
        Button btn_edit;
        RelativeLayout layout_res_table_sort_management;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_res_table_name= (TextView) itemView.findViewById(R.id.tv_res_table_name);
            btn_remove= (Button) itemView.findViewById(R.id.btn_remove);
            btn_edit= (Button) itemView.findViewById(R.id.btn_edit);
            layout_res_table_sort_management= (RelativeLayout) itemView.findViewById(R.id.layout_res_table_sort_management);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i=0;i<mRes_Table_SortList.size();i++){
                        if(mRes_Table_SortList.get(i).isRes_table_sort_click()){
                            mRes_Table_SortList.get(i).setRes_table_sort_click(false);
                        }
                    }
                    mRes_Table_SortList.get(getAdapterPosition()).setRes_table_sort_click(true);
                    notifyDataSetChanged();

                    if(msetOnItemClickListener!=null){
                        msetOnItemClickListener.setOnItemClick(v,getPosition());
                    }
                }
            });
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnItemClickListener!=null){
                        msetOnItemClickListener.setOnItemClick(v,getPosition());
                    }
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(msetOnItemClickListener!=null){
                        msetOnItemClickListener.setOnItemClick(v,getPosition());
                    }
                }
            });
        }
    }
    //创建接口
    public interface setOnItemClickListener{
        public void setOnItemClick(View view, int position);
    }
    //设置对象
    public setOnItemClickListener msetOnItemClickListener;
    //创建接口监听
    public setOnItemClickListener setOnItemClick(setOnItemClickListener msetOnItemClickListener){
        return this.msetOnItemClickListener=msetOnItemClickListener;
    }
}
