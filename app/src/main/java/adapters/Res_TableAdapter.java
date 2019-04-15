package adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Entty.Res_Table;
import Utils.DateTimeUtil;
import retail.yzx.com.kz.R;

import static retail.yzx.com.restaurant_nomal.Fragment.Res_Table_Fragment.handlerList;
import static retail.yzx.com.restaurant_nomal.Fragment.Res_Table_Fragment.runnables;

/**
 * Created by Administrator on 2017/8/12.
 */

public class Res_TableAdapter extends RecyclerView.Adapter<Res_TableAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Res_Table> mRes_TableList;//餐桌数据

    public Res_TableAdapter(Context mContext, ArrayList<Res_Table> mRes_TableList) {
        this.mContext = mContext;
        this.mRes_TableList = mRes_TableList;
    }

    @Override
    public int getItemCount() {
        return mRes_TableList.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(mContext, R.layout.item_res_rable_view,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_res_table_name.setText(mRes_TableList.get(position).getRes_table_name());
        holder.tv_res_table_sort_name.setText(mRes_TableList.get(position).getRes_table_sort_name());
        holder.tv_res_table_sort_people_nums.setText(mRes_TableList.get(position).getRes_table_people_nums());
        if("0".equals(mRes_TableList.get(position).getRes_table_status())){
            holder.tv_res_table_name.setTextColor(Color.parseColor("#ff0000"));
            holder.tv_tablestatus.setText("空闲");
            holder.layout1.setBackgroundResource(R.drawable.btn_whilte_red);
        }else if("4".equals(mRes_TableList.get(position).getRes_table_status())){
            holder.tv_tablestatus.setText("禁桌");
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            holder.layout1.setBackgroundResource(R.drawable.btn_red_whilte);
        }else if("1".equals(mRes_TableList.get(position).getRes_table_status())){
            holder.tv_tablestatus.setText("已开桌");
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            holder.layout1.setBackgroundResource(R.drawable.btn_yellow_whitle);
        }else if("2".equals(mRes_TableList.get(position).getRes_table_status())){
            holder.tv_tablestatus.setText("使用中");
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            holder.layout1.setBackgroundResource(R.drawable.btn_greenlignt_whilte);
        }else if("3".equals(mRes_TableList.get(position).getRes_table_status())){
            holder.tv_time.setVisibility(View.VISIBLE);
            setTime(mRes_TableList.get(position).getReserve_time(),holder.tv_time);
            holder.tv_tablestatus.setText("已预订");
            holder.tv_res_table_name.setTextColor(Color.parseColor("#000000"));
            holder.layout1.setBackgroundResource(R.drawable.btn_pink_whilte);
        }
    }
    Handler handler;
    Runnable runnable;
    public void setTime(final String time,final TextView view) {
        if (handlerList.size()>0&&runnables.size()>0){
            for (int i=0;i<handlerList.size();i++){
                handlerList.get(i).removeCallbacks(runnables.get(i));
            }
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String day = "00";
                    String hot = "00";
                    String min = "00";
                    String ss = "00";
//                    final long end_time = DateTimeUtil.getTime();
//                    final long bg_time = DateTimeUtil.getTime();
                    final long end_time = Long.parseLong(time) * 1000;
                    final long bg_time = DateTimeUtil.getTime();
                    long d_time = end_time - bg_time;
                    if (d_time >= 0) {
                    long d = d_time / 1000 / 60 / 60 / 24;
                    long h = d_time / 1000 / 60 / 60 % 24;
                    long m = d_time / 1000 % 60;//丢失的秒数
                    long h1 = h % 60;
                    long s1 = d_time / 1000 / 60 % 60;
                    Log.d("print3322", d + "天" + h + "时" + s1 + "分" + m + "秒");
                    if (String.valueOf(d).length() > 1) {
                        day = d + "";
                    } else {
                        day = "0" + d;
                    }
                    if (String.valueOf(h).length() > 1) {
                        hot = h + "";
                    } else {
                        hot = "0" + h;
                    }
                    if (String.valueOf(s1).length() > 1) {
                        min = s1 + "";
                    } else {
                        min = "0" + s1;
                    }
                    if (String.valueOf(m).length() > 1) {
                        ss = m + "";
                    } else {
                        ss = "0" + m;
                    }
                    //  "dd - HH : mm : ss"
//                                                        tv_time.setText(DateTimeUtil.getTime(d_time));
                    view.setText(hot + "时" + min + "分" + ss + "秒");
                }else {
                        view.setText("00:00:00");
                    }
                    handler.postDelayed(this, 1000);
                    handlerList.add(handler);
                    runnables.add(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000); // 在初始化方法里.
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_res_table_name;
        TextView tv_res_table_sort_name;
        TextView tv_res_table_sort_people_nums;
        TextView tv_tablestatus;
        TextView tv_time;
        RelativeLayout layout1;
        public ViewHolder(final View itemView) {
            super(itemView);
            tv_res_table_name= (TextView) itemView.findViewById(R.id.tv_res_table_name);
            layout1= (RelativeLayout) itemView.findViewById(R.id.layout1);
            tv_tablestatus= (TextView) itemView.findViewById(R.id.tv_tablestatus);
            tv_res_table_sort_name= (TextView) itemView.findViewById(R.id.tv_res_table_sort_name);
            tv_res_table_sort_people_nums= (TextView) itemView.findViewById(R.id.tv_res_table_sort_people_nums);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
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
    public static setOnItemClickListener msetOnItemClickListener;
    //创建接口监听
    public setOnItemClickListener setOnItemClick(setOnItemClickListener msetOnItemClickListener){
        return this.msetOnItemClickListener=msetOnItemClickListener;
    }
}
