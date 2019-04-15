package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Provide_Entty;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/31.
 */
public class Adapter_provider extends BaseAdapter {

    public Context context;
    public List<Provide_Entty> adats;
    public SetONClickedit setONClickedit;

    public Adapter_provider SetONClickedit(SetONClickedit setONClickedit){
        this.setONClickedit=setONClickedit;
        return Adapter_provider.this;
    }


    public Adapter_provider(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
    }

    public void setAdats(List<Provide_Entty> adats){
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
            viewHored=new ViewHored();
            view= LayoutInflater.from(context).inflate(R.layout.provide_listview,null);
            viewHored.tv_xiuhao= (TextView) view.findViewById(R.id.tv_xiuhao);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_linkman= (TextView) view.findViewById(R.id.tv_linkman);
            viewHored.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
            viewHored.but_edit= (Button) view.findViewById(R.id.but_edit);
            view.setTag(viewHored);
        }
        viewHored.tv_xiuhao.setText((i+1)+"");
        viewHored.tv_bncode.setText(adats.get(i).getProvider_bncode());
        viewHored.tv_name.setText(adats.get(i).getProvider_name());
        viewHored.tv_phone.setText(adats.get(i).getPhone());
        viewHored.tv_linkman.setText(adats.get(i).getContact());
        viewHored.but_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setONClickedit.setOnclickEdit(i);
            }
        });
        return view;
    }

    class ViewHored{
        TextView tv_bncode,tv_name,tv_linkman,tv_phone,tv_xiuhao;
        Button but_edit;
    }

  public interface SetONClickedit{
        void setOnclickEdit(int i);
    }

}
