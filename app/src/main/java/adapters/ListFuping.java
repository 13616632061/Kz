package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.ShuliangEntty;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/21.
 */
public class ListFuping extends BaseAdapter {

    public List<Commodity> adats;
    public Context context;
    public List<ShuliangEntty> shuling;
    public String type="0";


    public ListFuping(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void getadats(List<Commodity> adats){
//        Collections.reverse(adats);
        this.adats=adats;
        this.notifyDataSetChanged();
    }

    public void getshuliang( List<ShuliangEntty> shuling){
        this.shuling=shuling;
        this.notifyDataSetChanged();
    }

    public void settype(String type){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder=null;
        if(view!=null){
            viewHoder= (ViewHoder) view.getTag();
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.listfuping,null);
            viewHoder=new ViewHoder();
            viewHoder.tv_fuping1= (TextView) view.findViewById(R.id.tv_fuping1);
            viewHoder.tv_fuping2= (TextView) view.findViewById(R.id.tv_fuping2);
            viewHoder.tv_fuping3= (TextView) view.findViewById(R.id.tv_fuping3);
            viewHoder.tv_fuping4= (TextView) view.findViewById(R.id.tv_fuping4);
            view.setTag(viewHoder);
        }

        viewHoder.tv_fuping1.setText((i+1)+"");
        viewHoder.tv_fuping2.setText(adats.get(i).getName());
        viewHoder.tv_fuping3.setText(shuling.get(i).getNumber()+"");

//        if (type.equals("0")){
//            viewHoder.tv_fuping4.setText((float)(TlossUtils.mul(Double.parseDouble(shuling.get(i).getNumber()+""),Double.parseDouble(StringUtils.stringpointtwo(adats.get(i).getPrice()+""))))+"");
//        }else {
//            viewHoder.tv_fuping4.setText((float)(TlossUtils.mul(Double.parseDouble(shuling.get(i).getNumber()+""),Double.parseDouble(adats.get(i).getMember_price())))+"");
//        }

        if (adats.get(i).getType()!=null&&!adats.get(i).getType().equals("0")){
            if (adats.get(i).getCustom_member_price()!=null&&!adats.get(i).getCustom_member_price().equals("")&&!adats.get(i).getCustom_member_price().equals("null")){
                viewHoder.tv_fuping4.setText(StringUtils.getStrings(adats.get(i).getCustom_member_price(),",")[Integer.parseInt(adats.get(i).getType())-1]);
            }else {
                viewHoder.tv_fuping4.setText(Float.parseFloat(adats.get(i).getMember_price()) + "");
            }
        }else {
            viewHoder.tv_fuping4.setText(Float.parseFloat(adats.get(i).getPrice()) + "");
        }
        return view;
    }

    class ViewHoder{
            TextView tv_fuping1,tv_fuping2,tv_fuping3,tv_fuping4;
    }
}
