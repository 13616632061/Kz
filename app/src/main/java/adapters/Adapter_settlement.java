package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Settlement_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/7.
 */
public class Adapter_settlement extends BaseAdapter {

    private Context context;
    private List<Settlement_Entty> adats;
    private Setonclick setonclick;
    private List<Boolean> Checkeds;

    public Adapter_settlement Setonclick(Setonclick setonclick){
        this.setonclick=setonclick;
        return Adapter_settlement.this;
    }

    public Adapter_settlement(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.Checkeds=new ArrayList<>();
    }

    public void setAdats(List<Settlement_Entty> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void setCheckeds(List<Boolean> Checkeds){
        this.Checkeds=Checkeds;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adats.size();
    }

    @Override
    public Object getItem(int i) {
        return adats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHored viewHored=null;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            viewHored=new ViewHored();
            view=LayoutInflater.from(context).inflate(R.layout.list_settlement,null);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_linkman= (TextView) view.findViewById(R.id.tv_linkman);
            viewHored.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
            viewHored.tv_total= (TextView) view.findViewById(R.id.tv_total);
            viewHored.tv_status= (TextView) view.findViewById(R.id.tv_status);
            viewHored.cc_box= (CheckBox) view.findViewById(R.id.cc_box);
            viewHored.but_operation= (Button) view.findViewById(R.id.but_operation);
            view.setTag(viewHored);
        }
        viewHored.tv_name.setText(adats.get(i).getProvider_name());
        viewHored.tv_bncode.setText(adats.get(i).getReport_order());
        viewHored.tv_time.setText(adats.get(i).getAddtime());
        viewHored.tv_nums.setText(adats.get(i).getGoods_num());
        viewHored.tv_linkman.setText(adats.get(i).getSeller_name());
        viewHored.tv_time.setText(adats.get(i).getAddtime());
        viewHored.tv_phone.setText(adats.get(i).getPhone());
        viewHored.tv_total.setText(adats.get(i).getTotal_amount());
        if (adats.get(i).getOrder_status().equals("0")){
            viewHored.tv_status.setText("待对账");
        }
        if (adats.get(i).getOrder_status().equals("1")){
            viewHored.tv_status.setText("待结算");
        }
        if (adats.get(i).getOrder_status().equals("")){
            viewHored.tv_status.setText("已结算");
        }
        viewHored.cc_box.setChecked(Checkeds.get(i));
        viewHored.cc_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclick.SetonChecked(i);
            }
        });
        viewHored.but_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclick.Setonclick(i);
            }
        });
        return view;
    }


    class ViewHored{
        TextView tv_name,tv_bncode,tv_time,tv_nums,tv_linkman,tv_phone,tv_total,tv_status;
        CheckBox cc_box;
        Button but_operation;
    }

    public interface Setonclick{
        void SetonChecked(int i);
        void Setonclick(int i);
    }

}
