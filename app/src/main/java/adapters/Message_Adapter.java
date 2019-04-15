package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import Entty.Message_Beans2;
import retail.yzx.com.kz.R;
import widget.SwipeItemLayout;

/**
 * Created by YGD on 2017/12/23.
 */

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MyViewHolder> {

    private Context context;
    public SetItmeOnClickListener setItmeOnClickListener;
    public List<Message_Beans2> adtas;


    public Message_Adapter SetOnCLickItme(SetItmeOnClickListener setItmeOnClickListener){
        this.setItmeOnClickListener=setItmeOnClickListener;
        return Message_Adapter.this;
    }

    public Message_Adapter(Context context) {
        this.context = context;
        this.adtas=new ArrayList<>();
    }

    public void setData(List<Message_Beans2> adtas){
        this.adtas=adtas;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itme_message,null);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_msg_tilte.setText(adtas.get(position).getTitle());
        holder.tv_msg_context.setText(adtas.get(position).getContent());
        holder.tv_msg_time.setText(adtas.get(position).getTime());
//        holder.tv_select_nums.setText(adtas.get(position).getName());
//        if (position%2==0){
//            holder.ll_itme.setBackgroundColor(Color.parseColor("#ffffff"));
//        }else {
//            holder.ll_itme.setBackgroundColor(Color.parseColor("#f3f5f7"));
//        }


        if (adtas.get(position).getState().equals("1")){
            holder.img_msg_unread.setImageResource(R.drawable.unread_icon);
        }else {
            holder.img_msg_unread.setImageResource(R.drawable.read_icon);
        }

        //采购单的选择
        holder.ll_switme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItmeOnClickListener.ItmeonClickListener(position);
            }
        });



        //取消的按钮
//        holder.tv_msg_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setItmeOnClickListener.CancelOnClickListener(position);
//                holder.ll_switme.close();
//            }
//        });
        holder.tv_msg_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItmeOnClickListener.DeleteOnClickListener(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adtas.size();
    }



//    @Override
//    public int getItemViewType(int position) {
//        return position%2==0?0:1;
//    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView msg_list_item_icon,img_msg_unread;
        TextView tv_msg_tilte,tv_msg_context,tv_msg_time;
        TextView tv_msg_delete;
        SwipeItemLayout ll_switme;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_msg_tilte= (TextView) itemView.findViewById(R.id.tv_msg_tilte);
            tv_msg_context= (TextView) itemView.findViewById(R.id.tv_msg_context);
            tv_msg_time= (TextView) itemView.findViewById(R.id.tv_msg_time);
//            tv_msg_cancel=itemView.findViewById(R.id.tv_msg_cancel);
            tv_msg_delete= (TextView) itemView.findViewById(R.id.tv_msg_delete);
            msg_list_item_icon= (ImageView) itemView.findViewById(R.id.msg_list_item_icon);
            img_msg_unread= (ImageView) itemView.findViewById(R.id.img_msg_unread);
            ll_switme= (SwipeItemLayout) itemView.findViewById(R.id.ll_switme);
        }
    }

  public interface SetItmeOnClickListener{
      void ItmeonClickListener(int i);
//      void CancelOnClickListener(int i);
      void DeleteOnClickListener(int i);
  }

}
