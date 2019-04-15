package adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Entty.ShuliangEntty;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2018/4/24.
 */

public class StockAdapter extends BaseAdapter {


    public List<Commodity> adats;
    public List<ShuliangEntty> nums;
    public boolean Show=false;
    public Context context;
    public Adapter_Money adapter;
    public List<Boolean> Checked;
    public StrOnoclick strOnoclick;

    public StockAdapter(Context context) {
        this.context = context;
        this.adapter=new Adapter_Money(context);
        this.adats=new ArrayList<>();
        this.nums=new ArrayList<>();
        this.Checked=new ArrayList<>();
    }

    public StockAdapter StrOnoclick(StrOnoclick strOnoclick){
        this.strOnoclick=strOnoclick;
        return StockAdapter.this;
    }


    public void setAdats(List<Commodity> adats,List<ShuliangEntty> entty){
        this.adats=adats;
        this.nums=entty;
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
        final StockAdapter.ViewHored viewHored;
        if (view!=null){
            viewHored= (StockAdapter.ViewHored) view.getTag();
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.itmestock,null);
            viewHored=new StockAdapter.ViewHored();
            viewHored.tv_stock= (TextView) view.findViewById(R.id.tv_stock);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_total= (TextView) view.findViewById(R.id.tv_total);
            viewHored.tv_delete= (TextView) view.findViewById(R.id.tv_delete);
            viewHored.tv_provider= (TextView) view.findViewById(R.id.tv_provider);
            view.setTag(viewHored);
        }
        Checked.add(false);
        viewHored.tv_xuhao.setText((adats.size()-i)+"");
        viewHored.tv_stock.setText(adats.get(i).getStore());
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_total.setText(StringUtils.stringpointtwo(adats.get(i).getCost()));

        viewHored.tv_nums.setText(nums.get(i).getNumber()+"");
        viewHored.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOnoclick.setbutonclick(i);
            }
        });


        viewHored.tv_provider.setText(adats.get(i).getProvider_name());

        viewHored.tv_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog1= new Dialog(context);
                dialog1.setTitle("进价");
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.show();
                Window window = dialog1.getWindow();
                window.setContentView(R.layout.money_dialog);
                final EditText editText= (EditText) window.findViewById(R.id.ed_nums);
                final TextView tv_name= (TextView) window.findViewById(R.id.tv_name);
                tv_name.setText(adats.get(i).getName());
                final TextView tv_oldnums= (TextView) window.findViewById(R.id.tv_oldnums);
                TextView tv_Prompt= (TextView) window.findViewById(R.id.tv_Prompt);
                tv_Prompt.setText("原进货价");
                tv_oldnums.setText(adats.get(i).getCost()+"");
                Button but_goto= (Button) window.findViewById(R.id.but_goto);
                Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString()!=null&&!editText.getText().toString().equals("")){

                            adats.get(i).setCost(editText.getText().toString());

                            setAdats(adats,nums);
                            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            dialog1.dismiss();
                        }
                    }
                });
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog1.dismiss();
                    }
                });

            }
        });

        viewHored.tv_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              strOnoclick.setnums(i);

            }
        });

        return view;
    }

    class ViewHored{
        TextView tv_xuhao,tv_name,tv_stock,tv_nums,tv_total,tv_delete,tv_provider;
    }

    public interface StrOnoclick{
        void setbutonclick(int i);
        void setnums(int i);
    }


}
