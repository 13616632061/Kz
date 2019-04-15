package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Entty.Template_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/3.
 */
public class Adapter_sms_Template extends BaseAdapter {
    public Context context;
    public Template_Entty adats;
    public SetOnclick setOnclick;

    public Adapter_sms_Template SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_sms_Template.this;
    }


    public Adapter_sms_Template(Context context) {
        this.context = context;
    }

    public void setAdats(Template_Entty adats){
        this.adats=adats;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return adats.getResponse().getData().size();
    }

    @Override
    public Object getItem(int i) {
        return adats.getResponse().getData().get(i);
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
            view= LayoutInflater.from(context).inflate(R.layout.sms_template_itme,null);
            viewHored=new ViewHored();
            viewHored.tv_context= (TextView) view.findViewById(R.id.tv_context);
            viewHored.Rl_layout= (RelativeLayout) view.findViewById(R.id.Rl_layout);
            view.setTag(viewHored);
        }
        viewHored.tv_context.setText(adats.getResponse().getData().get(i).getContent());
        viewHored.Rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclick.onclickdialog(i);
            }
        });

        return view;
    }



    class ViewHored{
        TextView tv_context;
        RelativeLayout Rl_layout;
    }


    public interface SetOnclick{
        void onclickdialog(int i);
    }

}
