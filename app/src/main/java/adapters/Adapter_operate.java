package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Operate_Entty;
import Utils.DateUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/1.
 * 敏感操作适配器
 */
public class Adapter_operate extends BaseAdapter{


    public Context context;
    public List<Operate_Entty> adats;

    public Adapter_operate(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void getAdats(List<Operate_Entty> adats){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHored viewHored=null;
        if (view!=null){
            viewHored= (ViewHored) view.getTag();
        }else {
            viewHored=new ViewHored();
            view=LayoutInflater.from(context).inflate(R.layout.operate_itme,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_user= (TextView) view.findViewById(R.id.tv_user);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_way= (TextView) view.findViewById(R.id.tv_way);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_context= (TextView) view.findViewById(R.id.tv_context);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_user.setText(adats.get(i).getSeller_name());
        viewHored.tv_name.setText(adats.get(i).getWork_name());
        viewHored.tv_way.setText(adats.get(i).getOperate_type());
        viewHored.tv_time.setText(DateUtils.getDateTimeFromMillisecond((Long.parseLong(adats.get(i).getAddtime()))*1000));
        viewHored.tv_context.setText(adats.get(i).getContent());
        return view;
    }

    class ViewHored{
        TextView tv_xuhao,tv_user,tv_name,tv_way,tv_time,tv_context;
    }

}
