package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import Entty.Select_Tag;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/19.
 */
public class Adapter_tag_dialog extends RecyclerView.Adapter<Adapter_tag_dialog.MyViewHolder> {


    public Context context;
    public List<Select_Tag> adats;
    public SetOnclick setOnclick;

    public Adapter_tag_dialog SetONclickCC(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_tag_dialog.this;
    }


    public Adapter_tag_dialog(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Select_Tag> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rl_tag_dialog,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.cc_box.setText(adats.get(position).getName());
        holder.cc_box.setChecked(adats.get(position).isSelect());
        holder.cc_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setOnclick.setOnclickCheck(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adats.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox cc_box;

        public MyViewHolder(View itemView) {
            super(itemView);
            cc_box= (CheckBox) itemView.findViewById(R.id.cc_box);
        }
    }
    //选中的接口回掉
    public interface SetOnclick {
        void setOnclickCheck(int i);

    }
}
