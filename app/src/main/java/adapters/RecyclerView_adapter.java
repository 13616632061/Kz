package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.Quick_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/28.
 */
public class RecyclerView_adapter extends RecyclerView.Adapter<RecyclerView_adapter.MyViewHolder> {


    public List<Commodity> adats;
    public Context context;
    public Commodity commoditys;
    public Button button;
//    public List<Quick_Entty> listquick;


    int i=0;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerView_adapter(Context context) {
        this.adats = new ArrayList<>();
        this.context = context;
    }

    public void setListquick(List<Quick_Entty> listquick){
//        this.listquick=listquick;
        notifyDataSetChanged();
    }
    public void setAdats(List<Commodity> adats) {
        button = new Button(context);
        //        删除的广播
//        IntentFilter intentFilter3 = new IntentFilter();
//        intentFilter3.addAction("com.yzx.fupingdelete");
//        context.registerReceiver(broadcastReceiver, intentFilter3);
        this.adats = adats;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_itme, null);
        i++;
        Log.d("print", "onCreateViewHolder: "+i);


        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_name.setText(adats.get(position).getName());
//        holder.tv_jiage.setText(StringUtils.stringpointtwo(adats.get(position).getPrice()));
        holder.tv_jiage.setText(Double.parseDouble(adats.get(position).getPrice())+" ");
//        holder.but_shuliang.setText(listquick.get(position).getNumber()+"");
//        this.button = holder.but_shuliang;
//        if (!TextUtils.isEmpty(listquick.get(position).getNumber())&&Integer.parseInt(listquick.get(position).getNumber())> 0) {
//            holder.but_shuliang.setVisibility(View.VISIBLE);
//        } else {
//            holder.but_shuliang.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return adats.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_name, tv_jiage;
        public Button but_shuliang;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_jiage = (TextView) itemView.findViewById(R.id.tv_jiage);
            but_shuliang = (Button) itemView.findViewById(R.id.but_shuliang);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(view, getAdapterPosition());
            }
        }

    }


    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

//    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("com.yzx.fupingdelete")) {
//                Bundle bundle = intent.getExtras();
//                if (button!=null) {
//                    commoditys = (Commodity) bundle.getSerializable("commoditys");
//                    Log.d("print","qqqqqqqqq");
//                    for (int i = 0; i < adats.size(); i++) {
//                        if (adats.get(i).getName().equals(commoditys.getName()) && button.getVisibility() == View.VISIBLE) {
//                            Log.d("print","qqqqqqqqq");
//                            button.setText(listquick.get(i) + "");
//                            button.setVisibility(View.GONE);
//                            notifyDataSetChanged();
//                        }
//                    }
//                }
//            }
//        }
//    };
}
