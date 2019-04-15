package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import Entty.SendLogsms_Entty;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;

/**
 * Created by admin on 2018/12/11.
 */

public class Sendsms_Adapter extends BaseAdapter {
    Context context;
    SendLogsms_Entty sendLogsms_entty;

    public Sendsms_Adapter(Context context) {
        this.context=context;
    }

    public void SetAdats(SendLogsms_Entty sendLogsms_entty){
        this.sendLogsms_entty=sendLogsms_entty;
    }

    @Override
    public int getCount() {
        return sendLogsms_entty.getResponse().getData().getList().size();
    }

    @Override
    public Object getItem(int i) {
        return sendLogsms_entty.getResponse().getData().getList().get(i);
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
            view= LayoutInflater.from(context).inflate(R.layout.sendsmslog_itme,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_time= (TextView) view.findViewById(R.id.tv_time);
            viewHored.tv_send_num= (TextView) view.findViewById(R.id.tv_send_num);
            viewHored.tv_success= (TextView) view.findViewById(R.id.tv_success);
            viewHored.tv_user= (TextView) view.findViewById(R.id.tv_user);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText(sendLogsms_entty.getResponse().getData().getList().get(i).getId());
        viewHored.tv_time.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(sendLogsms_entty.getResponse().getData().getList().get(i).getAdd_time())*1000)+"");
        viewHored.tv_send_num.setText(sendLogsms_entty.getResponse().getData().getList().get(i).getFail());
        viewHored.tv_success.setText(sendLogsms_entty.getResponse().getData().getList().get(i).getSuccess());
        viewHored.tv_user.setText(sendLogsms_entty.getResponse().getData().getList().get(i).getSeller_id());

        return view;
    }

    class ViewHored{
        TextView tv_xuhao,tv_time,tv_send_num,tv_success,tv_user;
    }

}
