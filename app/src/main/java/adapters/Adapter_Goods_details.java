package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import Entty.Goods_details;
import Utils.StringUtils;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/8/3.
 */
public class Adapter_Goods_details extends BaseAdapter {

    public Context context;
    public List<Goods_details> adats;
    public SetOnclickbut setOnclickbut;

    //判断是审核还是验收
    public int type;

    public Adapter_Goods_details(Context context,int type) {
        this.context = context;
        this.adats=new ArrayList<>();
        this.type=type;
    }

    public Adapter_Goods_details SetOnclickbut(SetOnclickbut setOnclickbut){
        this.setOnclickbut=setOnclickbut;
        return Adapter_Goods_details.this;
    }


    public void setAdats(List<Goods_details> adats){
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
            view=LayoutInflater.from(context).inflate(R.layout.goods_details_lv,null);
            viewHored.tv_bncode= (TextView) view.findViewById(R.id.tv_bncode);
            viewHored.tv_name= (TextView) view.findViewById(R.id.tv_name);
            viewHored.tv_tagname= (TextView) view.findViewById(R.id.tv_tagname);
            viewHored.tv_nums= (TextView) view.findViewById(R.id.tv_nums);
            viewHored.tv_price= (TextView) view.findViewById(R.id.tv_price);
            viewHored.tv_before_nums= (TextView) view.findViewById(R.id.tv_before_nums);
            viewHored.but_acceptance= (Button) view.findViewById(R.id.but_acceptance);
            view.setTag(viewHored);
        }
        viewHored.tv_bncode.setText(adats.get(i).getBncode());
        viewHored.tv_name.setText(adats.get(i).getGoods_name());
        viewHored.tv_tagname.setText(adats.get(i).getMenu());
        viewHored.tv_nums.setText(adats.get(i).getBefore_nums());
        viewHored.tv_price.setText(StringUtils.stringpointtwo(adats.get(i).getPrice()));
        viewHored.tv_before_nums.setText(adats.get(i).getNums());

        viewHored.tv_before_nums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adats.get(i).getGoods_status().equals("1")) {
                } else {
                    final Dialog dialog = new Dialog(context);
                    dialog.setTitle("补充库存");
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.dialog_supplement);
                    TextView tv_t1 = (TextView) window.findViewById(R.id.tv_t1);
                    final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
                    Button but_add = (Button) window.findViewById(R.id.but_add);
                    tv_t1.setText("到货数量");
                    ed_add.setHint("请输入到货数量");
                    but_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!ed_add.getText().toString().isEmpty()) {
                                adats.get(i).setNums(ed_add.getText().toString());
                                notifyDataSetChanged();
                                dialog.dismiss();
                            } else {

                            }
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    });
                }
            }
        });


        if (type==0) {

            if (adats.get(i).getGoods_status().equals("1")) {
                viewHored.but_acceptance.setText("已验收");
            } else {
                viewHored.but_acceptance.setText("验收");
                viewHored.but_acceptance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnclickbut.Setonclick(i);
                    }
                });
            }
        }else {
            if (adats.get(i).getIs_verify().equals("1")) {
                viewHored.but_acceptance.setText("已审核");
            } else {
                viewHored.but_acceptance.setText("审核");
                viewHored.but_acceptance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnclickbut.Setonclick(i);
                    }
                });
            }
        }


        return view;
    }


       class ViewHored{
           TextView tv_bncode,tv_name,tv_tagname,tv_nums,tv_price,tv_before_nums;
           Button but_acceptance;
       }

   public interface SetOnclickbut{
       void Setonclick(int i);
   }

}
