package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Return_Entty;
import Utils.SharedUtil;
import Utils.TimeZoneUtil;
import retail.yzx.com.kz.R;
import widget.MylistView;

/**
 * Created by admin on 2017/6/9.
 */
public class Return_Adapter extends BaseAdapter {

    public Context context;
    public List<Return_Entty> adats;
    public List<Boolean> isunfold;
    public Return_Lei_adapter adapter;


    public Return_Adapter(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.isunfold=new ArrayList<>();
        adapter=new Return_Lei_adapter(context);
    }

    public void setAdats(List<Return_Entty> adats){
        this.adats=adats;
        for (int i=0;i<adats.size();i++){
            isunfold.add(false);
        }
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
        final ViewHored viewHored;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            viewHored=new ViewHored();
            view=LayoutInflater.from(context).inflate(R.layout.return_itme,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_order= (TextView) view.findViewById(R.id.tv_order);
            viewHored.tv_returnorder= (TextView) view.findViewById(R.id.tv_returnorder);
            viewHored.tv_ures_name= (TextView) view.findViewById(R.id.tv_ures_name);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.ll= (LinearLayout) view.findViewById(R.id.ll);
            viewHored.ll_xiangcitem= (LinearLayout) view.findViewById(R.id.ll_xiangcitem);
            viewHored.lv_details= (MylistView) view.findViewById(R.id.lv_details);
            viewHored.tv_serial= (TextView) view.findViewById(R.id.tv_serial);
            viewHored.tv_lei_time= (TextView) view.findViewById(R.id.tv_lei_time);
            viewHored.tv_lei_mode= (TextView) view.findViewById(R.id.tv_lei_mode);
            viewHored.tv_lei_name= (TextView) view.findViewById(R.id.tv_lei_name);
            viewHored.tv_shiji= (TextView) view.findViewById(R.id.tv_shiji);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_order.setText(adats.get(i).getOrder_id());
        viewHored.tv_returnorder.setText(adats.get(i).getShip_id());
        viewHored.tv_serial.setText(adats.get(i).getShip_id());
        if (adats.get(i).getWorker_name().equals("0")||adats.get(i).getWorker_name().equals("null")){
            viewHored.tv_ures_name.setText(SharedUtil.getString("name"));
        }else {
            viewHored.tv_ures_name.setText(adats.get(i).getWorker_name());
        }
        viewHored.tv_time.setText(TimeZoneUtil.getTime(1000*Long.parseLong(adats.get(i).getAddtime())));
        viewHored.tv_lei_time.setText(TimeZoneUtil.getTime(1000*Long.parseLong(adats.get(i).getAddtime())));
        viewHored.tv_lei_mode.setText("现金支付");
        if (adats.get(i).getWorker_name().equals("0")||adats.get(i).getWorker_name().equals("null")){
            viewHored.tv_lei_name.setText(SharedUtil.getString("name"));
        }else {
            viewHored.tv_lei_name.setText(adats.get(i).getWorker_name());
        }
        if (isunfold.get(i)){
            viewHored.ll.setVisibility(View.GONE);
        }else {
            viewHored.ll.setVisibility(View.VISIBLE);
        }

        viewHored.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isunfold.get(i)){
                    for (int j=0;j<adats.size();j++){
                        isunfold.set(j,false);
                    }
                    adapter.getAdats(adats.get(i).getItems());
                    viewHored.lv_details.setAdapter(adapter);
                    viewHored.ll_xiangcitem.setVisibility(View.GONE);
                }else {
                    for (int j=0;j<adats.size();j++){
                        if (j==i){
                            isunfold.set(j,true);
                        }else {
                            isunfold.set(j,false);
                        }
                    }
                    viewHored.ll_xiangcitem.setVisibility(View.VISIBLE);
                    double sum=0;
                    for (int j=0;j<adats.get(i).getItems().size();j++){
//                        TlossUtils.add(sum,Double.parseDouble(adats.get(i).getItems().get(j).getSum()));
                        sum+=Double.parseDouble(adats.get(i).getItems().get(j).getSum());
                        Log.d("print","总和为"+sum);
                    }
                    viewHored.tv_shiji.setText(sum+"");
                    adapter.getAdats(adats.get(i).getItems());
                    viewHored.lv_details.setAdapter(adapter);
                }

            }
        });

        return view;

    }


    class ViewHored{

        TextView tv_xuhao,tv_returnorder,tv_order,tv_Price,tv_ures_name,tv_time;
        TextView tv_serial,tv_lei_time,tv_lei_mode,tv_lei_name,tv_shiji;
        LinearLayout ll,ll_xiangcitem;
        MylistView lv_details;
    }


}
