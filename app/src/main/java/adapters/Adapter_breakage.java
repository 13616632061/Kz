package adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Breakage_entty;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.TlossUtils;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;

/**
 * Created by admin on 2017/8/10.
 */
public class Adapter_breakage extends BaseAdapter {

    public Context context;
    public List<Breakage_entty> adats;


    public Adapter_breakage(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Breakage_entty> adats){
        this.adats=adats;
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
            viewHored = new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.listview_breakage,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_cost= (TextView) view.findViewById(R.id.tv_cost);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_money= (TextView) view.findViewById(R.id.tv_money);
            viewHored.tv_man= (TextView) view.findViewById(R.id.tv_man);
            viewHored.tv_particulars= (TextView) view.findViewById(R.id.tv_particulars);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_time.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(adats.get(i).getAddtime())*1000));
        viewHored.tv_bncode.setText(adats.get(i).getBncode());
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_cost.setText(adats.get(i).getCost());
        viewHored.tv_nums.setText(adats.get(i).getNums());
        viewHored.tv_money.setText(StringUtils.stringpointtwo(TlossUtils.mul(Double.parseDouble(adats.get(i).getCost()),Double.parseDouble(adats.get(i).getNums()))+""));

        if (adats.get(i).getLogin_name().equals("null")){
            viewHored.tv_man.setText(SharedUtil.getString("seller_name"));
        }else {
            viewHored.tv_man.setText(adats.get(i).getLogin_name());
        }
        viewHored.tv_particulars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setTitle("报损详情");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_particulars);
                TextView tv_particulars= (TextView) window.findViewById(R.id.tv_particulars);
                tv_particulars.setText(adats.get(i).getDesc());
            }
        });

        return view;
    }

    class ViewHored{
        TextView tv_xuhao,tv_time,tv_bncode,tv_name,tv_cost,tv_nums,tv_money,tv_man,tv_particulars;
    }

}
