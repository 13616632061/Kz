package adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/24.
 */
public class Adapter_Money extends BaseAdapter {

    public Context context;
    public List<Commodity> adats;
    public Setonclick setonclick;
    public Setnumsonclick setnumsonclick;
    public List<Integer> entty;
    public List<Boolean> Checkeds;
    public Dialog dialog;
    public int j=-1;

    public Adapter_Money Setitmeonclcik(Setonclick setonclick){
        this.setonclick=setonclick;
        return Adapter_Money.this;
    }

    public Adapter_Money Setitmenumsonclick(Setnumsonclick setnumsonclick){
        this.setnumsonclick=setnumsonclick;
        return Adapter_Money.this;
    }

    public void gettype(int j){
        this.j=j;
        notifyDataSetChanged();
    }
    public Adapter_Money(Context context) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.entty=new ArrayList<>();
        this.Checkeds=new ArrayList<>();
    }

    public void getAdats(List<Commodity> adats){
        this.adats=adats;
        notifyDataSetChanged();
    }

    public void getCheckeds(List<Boolean> Checkeds){
        this.Checkeds=Checkeds;
        notifyDataSetChanged();
    }

    public void getnums(List<Integer> entty){
        this.entty=entty;
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
            view= LayoutInflater.from(context).inflate(R.layout.money_layout,null);
            viewHored.tv_xuhao= (TextView) view.findViewById(R.id.tv_xuhao);
            viewHored.tv_code= (TextView) view.findViewById(R.id.tv_code);
            viewHored.tv_number= (TextView) view.findViewById(R.id.tv_number);
            viewHored.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHored.cb_box= (CheckBox) view.findViewById(R.id.cb_box);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.but_dispose= (Button) view.findViewById(R.id.but_dispose);
            view.setTag(viewHored);
        }
        viewHored.tv_xuhao.setText((i+1)+"");
        viewHored.tv_name.setText(adats.get(i).getName());
        viewHored.tv_code.setText(adats.get(i).getBncode());
//        if (!adats.get(i).getGood_limit().equals("null")&&!adats.get(i).getStore().equals("null")){
//            int nums= (int) (Float.parseFloat(adats.get(i).getGood_limit())-Float.parseFloat(adats.get(i).getStore()));
            viewHored.tv_number.setText(entty.get(i)+"");
//        }
        viewHored.tv_price.setText(StringUtils.stringpointtwo(adats.get(i).getCost()));
        if (Checkeds.size()!=0){
        viewHored.cb_box.setChecked(Checkeds.get(i));
        viewHored.cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setonclick.Onitmeclick(i);
            }
        });
        viewHored.tv_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setnumsonclick.Onnumsitmeclick(i);
                Log.d("print","点击里数量修改");
                dialog= new Dialog(context);
                dialog.setTitle("请输入报货数");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.money_dialog);
                final EditText editText= (EditText) window.findViewById(R.id.ed_nums);
                final TextView tv_name= (TextView) window.findViewById(R.id.tv_name);
                tv_name.setText(adats.get(i).getName());
                final TextView tv_oldnums= (TextView) window.findViewById(R.id.tv_oldnums);
                tv_oldnums.setText(entty.get(i)+"");
                Button but_goto= (Button) window.findViewById(R.id.but_goto);
                Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString()!=null&& StringUtils.isNumber(editText.getText().toString())){
                            entty.set(i, Integer.valueOf(editText.getText().toString()));

                            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();
                    }
                });



            }
        });
        }else {
            viewHored.cb_box.setVisibility(View.GONE);
        }

        viewHored.but_dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (j==-1){
                    setnumsonclick.addShopping(i);
                    notifyDataSetChanged();
                }

            }
        });

        return view;
    }

    class ViewHored{
        TextView but_edit,tv_price,tv_number,tv_code,tv_xuhao,tv_name;
        CheckBox cb_box;
        Button but_dispose;
    }

    public interface Setonclick {
        void Onitmeclick(int i);
    }
    public interface Setnumsonclick {
        void Onnumsitmeclick(int i);
        void addShopping(int i);
    }

}
