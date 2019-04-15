package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Specification_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/3.
 */
public class Adapter_specification extends BaseAdapter {
    public Context context;
    public List<Specification_Entty> adats;
    public SetOnclick setOnclick;
    public String type="1";
    public int j=0;

    public Adapter_specification SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Adapter_specification.this;
    }


    public Adapter_specification(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Specification_Entty> adats,int j){
        this.adats=adats;
        this.j=j;
        notifyDataSetChanged();
    }
    public void setType(String type){
        this.type=type;
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
            view= LayoutInflater.from(context).inflate(R.layout.adapter_specification,null);
            viewHored=new ViewHored();
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_repertory= (TextView) view.findViewById(R.id.tv_repertory);
            viewHored.tv_number= (TextView) view.findViewById(R.id.tv_number);
            viewHored.Rl_layout= (RelativeLayout) view.findViewById(R.id.Rl_layout);
            viewHored.tv_1= (TextView) view.findViewById(R.id.tv_1);
            view.setTag(viewHored);
        }
        if (j==0){
            if (type.equals("1")){
                    viewHored.tv_1.setText("充值金额");
                    viewHored.tv_repertory.setText("赠送金额");
                }else if (type.equals("2")){
                    viewHored.tv_1.setText("抵扣积分");
                    viewHored.tv_repertory.setText("抵扣金额");
            }
        }else {
            viewHored.tv_1.setText("分类名");
            viewHored.tv_repertory.setText("销售金额");
        }
        viewHored.tv_name.setText(adats.get(i).getVal());
        viewHored.tv_number.setText(adats.get(i).getGive());
        viewHored.Rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnclick.onclickdialog(i);
            }
        });
        return view;
    }
    class ViewHored{
        TextView tv_name,tv_repertory,tv_number,tv_1;
        RelativeLayout Rl_layout;
    }

    public interface SetOnclick{
        void onclickdialog(int i);
    }
}
