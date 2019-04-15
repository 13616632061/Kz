package adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/6.
 */
public class Quick_edit_Apapter extends RecyclerView.Adapter<Quick_edit_Apapter.MyViewHolder> {

    public List<Commodity> adats;
    public Context context;
    //是否显示删除按钮
    public boolean isshow=false;
    public Ondeleteonclick ondeleteonclick;


    public Quick_edit_Apapter setOndeleteonclick(Ondeleteonclick ondeleteonclick){
        this.ondeleteonclick=ondeleteonclick;
        return Quick_edit_Apapter.this;
    }


    public Quick_edit_Apapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction("com.yzx.edit");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals("com.yzx.edit")){
                if (!isshow){
                    isshow=true;
                }else {
                    isshow=false;
                }
                notifyDataSetChanged();
            }
        }
    };
    public void setAdats(List<Commodity> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_recyclerview_itme, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_name.setText(adats.get(position).getName());
        holder.tv_jiage.setText(StringUtils.stringpointtwo(adats.get(position).getPrice()));
        if (!isshow) {
            holder.but_shuliang.setVisibility(View.GONE);
        }else {
            holder.but_shuliang.setVisibility(View.VISIBLE);
        }
        holder.but_shuliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ondeleteonclick.Deleteclick(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return adats.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_jiage;
        public ImageView but_shuliang;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_jiage = (TextView) itemView.findViewById(R.id.tv_jiage);
            but_shuliang = (ImageView) itemView.findViewById(R.id.but_shuliang);
        }
    }
    public interface Ondeleteonclick{
        void Deleteclick(int position);

    }

    }
