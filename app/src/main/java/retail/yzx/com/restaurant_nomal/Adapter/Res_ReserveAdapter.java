package retail.yzx.com.restaurant_nomal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import Utils.SysUtils;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.Reserve_Entty;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Res_ReserveAdapter extends BaseAdapter {
    private Context mContext;
    private List<Reserve_Entty.ResponseBean.DataBean.ListsBean> reserve_entty;
    private AlertDialog malertDialog=null;
    private  InputMethodManager imm;

    public SetOnclick setOnclick;

    public Res_ReserveAdapter SetOnclick(SetOnclick setOnclick){
        this.setOnclick=setOnclick;
        return Res_ReserveAdapter.this;
    }

    public Res_ReserveAdapter(Context mContext, List<Reserve_Entty.ResponseBean.DataBean.ListsBean> reserve_entty) {
        this.mContext = mContext;
        this.reserve_entty = reserve_entty;
    }

    @Override
    public int getCount() {
        if(reserve_entty==null){
            return 0;
        }
        return reserve_entty.size();
    }

    @Override
    public Object getItem(int position) {
        return reserve_entty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        HorlderView horlderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_reserve,null);
            horlderView=new HorlderView();
            horlderView.tv_nums= (TextView) convertView.findViewById(R.id.tv_nums);
            horlderView.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            horlderView.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            horlderView.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            horlderView.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            horlderView.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            horlderView.btn_ordersnotes= (Button) convertView.findViewById(R.id.btn_ordersnotes);
            horlderView.btn_add_ordernotes= (Button) convertView.findViewById(R.id.btn_add_ordernotes);
            horlderView.btn_remove_order= (Button) convertView.findViewById(R.id.btn_remove_order);
            horlderView.btn_order_receipt_print= (Button) convertView.findViewById(R.id.btn_order_receipt_print);
            horlderView.btn_order_label_print= (Button) convertView.findViewById(R.id.btn_order_label_print);
            horlderView.tv_notesinfo= (EditText) convertView.findViewById(R.id.tv_notesinfo);
            horlderView.layout_notes_info= (RelativeLayout) convertView.findViewById(R.id.layout_notes_info);
            horlderView.ll_edit= (LinearLayout) convertView.findViewById(R.id.ll_edit);
            convertView.setTag(horlderView);
        }else {
            horlderView= (HorlderView) convertView.getTag();
        }
        horlderView.tv_nums.setText(position+1+"");
        horlderView.tv_name.setText(reserve_entty.get(position).getPerson_name());
        horlderView.tv_phone.setText(reserve_entty.get(position).getMobile());
        horlderView.tv_time.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(reserve_entty.get(position).getReserve_time())*1000)+"");
        horlderView.tv_price.setText(SysUtils.priceFormat(Double.parseDouble(reserve_entty.get(position).getMoney()), false)+"元");
        horlderView.tv_num.setText(reserve_entty.get(position).getNums()+"人");
        if (reserve_entty.get(position).getStatus().equals("1")){
        }else if (reserve_entty.get(position).getStatus().equals("2")){
            horlderView.btn_ordersnotes.setBackgroundColor(Color.parseColor("#ffedf0f3"));
            horlderView.btn_ordersnotes.setText("已完成");
        }else if (reserve_entty.get(position).getStatus().equals("3")){
            horlderView.btn_ordersnotes.setBackgroundColor(Color.parseColor("#ffedf0f3"));
            horlderView.btn_ordersnotes.setText("已取消");
        }

//        if(false){
//            horlderView.layout_notes_info.setVisibility(View.VISIBLE);
//            horlderView.btn_ordersnotes.setText("关闭备注");
//
//        }else {
//            horlderView.layout_notes_info.setVisibility(View.GONE);
//            horlderView.btn_ordersnotes.setText("显示备注");
//            if (NoDoubleClickUtils.isSoftShowing((Activity) mContext)) {
//                imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            }
//        }
        final HorlderView finalHorlderView = horlderView;
        horlderView.btn_ordersnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reserve_entty.get(position).getStatus().equals("1")){
                    setOnclick.onclickdialog(position);
                }else {
                }
            }
        });

        horlderView.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reserve_entty.get(position).getStatus().equals("1")){
                    setOnclick.OnlickEdit(position);
                }else {
                }

            }
        });

        horlderView.btn_remove_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoDoubleClickUtils.isSoftShowing((Activity) mContext)) {
                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext,R.style.AlertDialog);
                        View view=View.inflate(mContext,R.layout.dialog_remove,null);
                Button btn_addnotes_cell= (Button) view.findViewById(R.id.btn_addnotes_cell);
                Button btn_addnotes_sure= (Button) view.findViewById(R.id.btn_addnotes_sure);
                btn_addnotes_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NoDoubleClickUtils.isDoubleClick()){
                            return;
                        }
                        malertDialog.dismiss();
                    }
                });
                btn_addnotes_cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        malertDialog.dismiss();
                    }
                });
                malertDialog=alertDialog.setView(view).show();
                malertDialog.show();

            }
        });
        return convertView;
    }
    private class HorlderView{
        TextView tv_nums;
        TextView tv_name;
        TextView tv_phone;
        TextView tv_time;
        TextView tv_price;
        TextView tv_num;
        Button btn_ordersnotes,btn_add_ordernotes,btn_remove_order;
        Button btn_order_label_print,btn_order_receipt_print;
        TextView tv_ordertime;
        RelativeLayout layout_notes_info;
        EditText tv_notesinfo;
        LinearLayout ll_edit;
    }

    public interface SetOnclick{
        void onclickdialog(int i);
        void OnlickEdit(int i);
    }
}
