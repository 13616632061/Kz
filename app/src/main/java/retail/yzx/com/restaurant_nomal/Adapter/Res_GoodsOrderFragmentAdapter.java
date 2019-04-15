package retail.yzx.com.restaurant_nomal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.SysUtils;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import shujudb.Sqlite_Entity;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Res_GoodsOrderFragmentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Res_GoodsOrders> mRes_GoodsOrderslist;
    private AlertDialog malertDialog=null;
    private  InputMethodManager imm;

    public Res_GoodsOrderFragmentAdapter(Context mContext, ArrayList<Res_GoodsOrders> mRes_GoodsOrderslist) {
        this.mContext = mContext;
        this.mRes_GoodsOrderslist = mRes_GoodsOrderslist;
    }

    @Override
    public int getCount() {
        if(mRes_GoodsOrderslist==null){
            return 0;
        }
        return mRes_GoodsOrderslist.size();
    }

    @Override
    public Object getItem(int position) {
        return mRes_GoodsOrderslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        HorlderView horlderView=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_get_res_goodsorder,null);
            horlderView=new HorlderView();
            horlderView.tv_nums= (TextView) convertView.findViewById(R.id.tv_nums);
            horlderView.tv_tablenums= (TextView) convertView.findViewById(R.id.tv_tablenums);
            horlderView.tv_dopackage= (TextView) convertView.findViewById(R.id.tv_dopackage);
            horlderView.tv_ordergoodsname= (TextView) convertView.findViewById(R.id.tv_ordergoodsname);
            horlderView.btn_ordersnotes= (Button) convertView.findViewById(R.id.btn_ordersnotes);
            horlderView.btn_add_ordernotes= (Button) convertView.findViewById(R.id.btn_add_ordernotes);
            horlderView.btn_remove_order= (Button) convertView.findViewById(R.id.btn_remove_order);
            horlderView.btn_order_receipt_print= (Button) convertView.findViewById(R.id.btn_order_receipt_print);
            horlderView.btn_order_label_print= (Button) convertView.findViewById(R.id.btn_order_label_print);
            horlderView.btn_bookkeeping= (Button) convertView.findViewById(R.id.btn_bookkeeping);
            horlderView.tv_ordertime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            horlderView.tv_notesinfo= (EditText) convertView.findViewById(R.id.tv_notesinfo);
            horlderView.layout_order_item= (RelativeLayout) convertView.findViewById(R.id.layout_order_item);
            horlderView.layout_notes_info= (RelativeLayout) convertView.findViewById(R.id.layout_notes_info);
            convertView.setTag(horlderView);
        }else {
            horlderView= (HorlderView) convertView.getTag();
        }
        horlderView.tv_nums.setText(position+1+"");
        horlderView.tv_tablenums.setText(mRes_GoodsOrderslist.get(position).getOrder_table_nums());
        if("true".equals(mRes_GoodsOrderslist.get(position).getOrder_dopackage())){
            horlderView.tv_dopackage.setText("打包");
        }else {
            horlderView.tv_dopackage.setText(" ");
        }
        horlderView.tv_ordergoodsname.setText(mRes_GoodsOrderslist.get(position).getOrder_name());
        horlderView.tv_ordertime.setText(mRes_GoodsOrderslist.get(position).getOrder_time());
        horlderView.tv_notesinfo.setText(mRes_GoodsOrderslist.get(position).getOrder_notes());
        horlderView.layout_order_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 3).putExtra("position",position).putParcelableArrayListExtra("mRes_GoodsOrderslist",mRes_GoodsOrderslist));
            }
        });
        if(mRes_GoodsOrderslist.get(position).isOpen_goodnote()){
            horlderView.layout_notes_info.setVisibility(View.VISIBLE);
            horlderView.btn_ordersnotes.setText("关闭更多");

        }else {
            horlderView.layout_notes_info.setVisibility(View.GONE);
            horlderView.btn_ordersnotes.setText("显示更多");
            if (NoDoubleClickUtils.isSoftShowing((Activity) mContext)) {
                imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }

        horlderView.btn_ordersnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRes_GoodsOrderslist.get(position).isOpen_goodnote()){
                    for(int i=0;i<mRes_GoodsOrderslist.size();i++){
                        mRes_GoodsOrderslist.get(i).setOpen_goodnote(false);
                    }
                }else {
                    for(int i=0;i<mRes_GoodsOrderslist.size();i++){
                        mRes_GoodsOrderslist.get(i).setOpen_goodnote(false);
                    }
                    mRes_GoodsOrderslist.get(position).setOpen_goodnote(true);
                }
                notifyDataSetChanged();
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
//                        delete_order(mRes_GoodsOrderslist.get(position).getOrder_id(),position);
                        Sqlite_Entity sqlite_entity=new Sqlite_Entity(mContext);
                        sqlite_entity.deleteguadan(mRes_GoodsOrderslist.get(position).getOrder_id());
                        if(!TextUtils.isEmpty(mRes_GoodsOrderslist.get(position).getTable_id())){
                            editTableStatus("0",mRes_GoodsOrderslist.get(position).getTable_id());
                        }
                        mRes_GoodsOrderslist.remove(position);
                        notifyDataSetChanged();
                        StringUtils.showToast(mContext,"订单作废成功！",20);
                        mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 6));

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
        final HorlderView finalHorlderView = horlderView;
        horlderView.btn_add_ordernotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                String notes= finalHorlderView.tv_notesinfo.getText().toString().trim();
                if(TextUtils.isEmpty(notes)){
                    StringUtils.showToast(mContext,"添加备注不能为空！",20);
                }else {
                    mRes_GoodsOrderslist.get(position).setOrder_notes(notes);
                    Sqlite_Entity sqlite_entity=new Sqlite_Entity(mContext);
                    sqlite_entity.modifyguadan("remarks",notes,"hangup",mRes_GoodsOrderslist.get(position).getOrder_id());
//                    Add_OrderNotes(mRes_GoodsOrderslist.get(position).getOrder_id(),notes,finalHorlderView.tv_notesinfo);
                }
            }
        });
        //小票打印
        horlderView.btn_order_receipt_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",5).
                        putExtra("mRes_GoodsOrderslist",mRes_GoodsOrderslist).putExtra("position",position));
            }
        });
        //标签打印
        horlderView.btn_order_label_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",4)
                       .putExtra("mRes_GoodsOrderslist",mRes_GoodsOrderslist).putExtra("position",position));
            }
        });
        horlderView.btn_bookkeeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bookkeeping_order(mRes_GoodsOrderslist.get(position).getOrder_id(),mRes_GoodsOrderslist.get(position).getOrder_notes(),position);
            }
        });


        return convertView;
    }
    private class HorlderView{
        TextView tv_nums;
        TextView tv_tablenums;
        TextView tv_dopackage;
        TextView tv_ordergoodsname;
        Button btn_ordersnotes,btn_add_ordernotes,btn_remove_order;
        Button btn_order_label_print,btn_order_receipt_print,btn_bookkeeping;
        TextView tv_ordertime;
        RelativeLayout layout_order_item;
        RelativeLayout layout_notes_info;
        EditText tv_notesinfo;

    }


    //记账订单
    private void Bookkeeping_order(String order_id,String remark,final int position){
        OkGo.post(SysUtils.getSellerServiceUrl("guadanToShezhang"))
                .tag(this)
                .params("order_id",order_id)
                .params("remark",remark)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印的数据挂单的数据"+s);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                        JSONObject jo1 = jsonObject.getJSONObject("response");
                        String status = jo1.getString("status");
                        if (status.equals("200")) {
                            if(!TextUtils.isEmpty(mRes_GoodsOrderslist.get(position).getTable_id())){
                                editTableStatus("0",mRes_GoodsOrderslist.get(position).getTable_id());
                            }
                            mRes_GoodsOrderslist.remove(position);
                            notifyDataSetChanged();
                            StringUtils.showToast(mContext,"订单作废成功！",20);
                            mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 6));

                        }else {
                            StringUtils.showToast(mContext,"订单作废失败！",20);
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //订单作废
    private void delete_order(String order_id, final int position){
        OkGo.post(SysUtils.getSellerServiceAPPUrl("delete_order"))
                .tag(this)
                .params("order_id",order_id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                if(!TextUtils.isEmpty(mRes_GoodsOrderslist.get(position).getTable_id())){
                                    editTableStatus("0",mRes_GoodsOrderslist.get(position).getTable_id());
                                }
                                mRes_GoodsOrderslist.remove(position);
                                notifyDataSetChanged();
                                StringUtils.showToast(mContext,"订单作废成功！",20);
                                mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 6));

                            }else {
                                StringUtils.showToast(mContext,"订单作废失败！",20);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //添加订单备注
    private void Add_OrderNotes(String order_id, final String mark_text, final EditText edit){
        OkGo.post(SysUtils.getSellerServiceUrl("tianjiabeizhu"))
                .tag(this)
                .params("order_id",order_id)
                .params("mark_text",mark_text)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "添加备注: "+jsonObject.toString() );
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                StringUtils.showToast(mContext,"添加备注成功！",20);
                                edit.setText(mark_text);
                                mContext.sendBroadcast(new Intent("Res_GoodsOrderFragment").putExtra("type", 1));
                                if (NoDoubleClickUtils.isSoftShowing((Activity) mContext)) {
                                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     *餐桌状态
     * 0空闲，1开桌，2使用中，3预定，4禁桌
     */

    private void editTableStatus(final String table_status, final String id){
        OkGo.post(SysUtils.getTableServiceUrl("table_insert"))
                .tag(this)
                .params("id",id)
                .params("status",table_status)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
