package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/3.
 */
public class Adapter_right_check extends BaseAdapter {
    public Context context;
    public List<Commodity> adats;
    public SetOnclick setOnclick;
    public List<String> liststate;

    public Adapter_right_check SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_right_check.this;
    }


    public Adapter_right_check(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.liststate=new ArrayList<>();
    }

    public void setAdats(List<Commodity> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    //设置盘点的值
    public void setstate(List<String> liststate){
        this.liststate=liststate;
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
            view= LayoutInflater.from(context).inflate(R.layout.right_layout,null);
            viewHored=new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_repertory= (TextView) view.findViewById(R.id.tv_repertory);
            viewHored.tv_number= (TextView) view.findViewById(R.id.tv_number);
            viewHored.im_unusual= (ImageView) view.findViewById(R.id.im_unusual);
            viewHored.Rl_layout= (RelativeLayout) view.findViewById(R.id.Rl_layout);
            view.setTag(viewHored);
        }
        viewHored.tv_name.setText(adats.get(i).getName());
        if (liststate.get(i).equals("0")){
            viewHored.tv_number.setText(adats.get(i).getStore());
            viewHored.im_unusual.setImageResource(R.drawable.unfinished);

            viewHored.Rl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnclick.onclickdialog(i);
                }
            });
        }else {
            viewHored.tv_number.setText(adats.get(i).getStore()+"/"+liststate.get(i));
            if (Integer.parseInt(adats.get(i).getStore())==Integer.parseInt(liststate.get(i))){
                viewHored.im_unusual.setImageResource(R.drawable.normal);
            }else {
                viewHored.im_unusual.setImageResource(R.drawable.abnormal);
            }
        }


        return view;
    }

    class ViewHored{
        TextView tv_name,tv_repertory,tv_number;
        ImageView im_unusual;
        RelativeLayout Rl_layout;
    }


    public interface SetOnclick{
        void onclickdialog(int i);
    }

}
