package retail.yzx.com.supper_self_service.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Utils.SharedUtil;
import Utils.StringUtils;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;

/**
 * Created by Administrator on 2017/7/11.
 */

public class Self_Service_PlayOrdersAdapter extends BaseAdapter {
    private Activity mContext;
    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist;
    private boolean isClick;
    private AlertDialog mAlertDialog;
    private int type;
    private int selfservice_nums;//自助收银商品限制0表示不限制

    public Self_Service_PlayOrdersAdapter(Activity mContext, ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist, boolean isClick,int type) {
        this.mContext = mContext;
        this.mSelf_Service_GoodsInfolist = mSelf_Service_GoodsInfolist;
        this.isClick = isClick;
        this.type = type;
        String selfservice_nums_str= SharedUtil.getString("selfservice_nums");
        if(TextUtils.isEmpty(selfservice_nums_str)){
            selfservice_nums=0;
        }else {
            selfservice_nums=Integer.parseInt(selfservice_nums_str);
        }
    }



    @Override
    public int getCount() {
        if(mSelf_Service_GoodsInfolist==null){
            return 0;
        }
        return mSelf_Service_GoodsInfolist.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelf_Service_GoodsInfolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Hordler mHordler=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_self_service_playorders,null);
            mHordler=new Hordler();
            mHordler.tv_goodsname= (TextView) convertView.findViewById(R.id.tv_goodsname);
            mHordler.tv_goodsnotes= (TextView) convertView.findViewById(R.id.tv_goodsnotes);
            mHordler.tv_goods_size= (TextView) convertView.findViewById(R.id.tv_goods_size);
            mHordler.tv_goodsprice= (TextView) convertView.findViewById(R.id.tv_goodsprice);
            mHordler.btn_add= (Button) convertView.findViewById(R.id.btn_add);
            mHordler.btn_addnotes= (Button) convertView.findViewById(R.id.btn_addnotes);
            mHordler.btn_cell= (Button) convertView.findViewById(R.id.btn_cell);
            mHordler.et_nums= (EditText) convertView.findViewById(R.id.et_nums);
            convertView.setTag(mHordler);
        }else {
            mHordler= (Hordler) convertView.getTag();
        }
        if(type==2){
            mHordler.btn_addnotes.setVisibility(View.VISIBLE);
        }else {
            mHordler.btn_addnotes.setVisibility(View.GONE);
        }
        mHordler.tv_goodsname.setText(mSelf_Service_GoodsInfolist.get(position).getName());
        mHordler.tv_goodsprice.setText(""+(Double.parseDouble(StringUtils.stringpointtwo(mSelf_Service_GoodsInfolist.get(position).getPrice()))*Integer.parseInt(mSelf_Service_GoodsInfolist.get(position).getNumber())));
        mHordler.et_nums.setText(mSelf_Service_GoodsInfolist.get(position).getNumber());
        mHordler.tv_goodsnotes.setText(mSelf_Service_GoodsInfolist.get(position).getNotes());
        mHordler.tv_goods_size.setText(mSelf_Service_GoodsInfolist.get(position).getSize());
        mHordler.et_nums.setFocusable(false);
        final Hordler finalMHordler = mHordler;
        if(isClick){
        mHordler.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total_nums=0;
                for(int i=0;i<mSelf_Service_GoodsInfolist.size();i++){
                    String num_str= mSelf_Service_GoodsInfolist.get(i).getNumber();
                    int num=Integer.parseInt(num_str);
                    total_nums+=num;
                }
                if(selfservice_nums>total_nums||selfservice_nums==0){
                    String nums_str= finalMHordler.et_nums.getText().toString().trim();
                    int nums=Integer.parseInt(nums_str);
                    nums++;
                    mSelf_Service_GoodsInfolist.get(position).setNumber(nums+"");
                    finalMHordler.et_nums.setText(mSelf_Service_GoodsInfolist.get(position).getNumber());
                    finalMHordler.tv_goodsprice.setText(""+(Double.parseDouble(StringUtils.stringpointtwo(mSelf_Service_GoodsInfolist.get(position).getPrice()))*Integer.parseInt(mSelf_Service_GoodsInfolist.get(position).getNumber())));
                    mContext.sendBroadcast(new Intent("Self_Service_PlayOrdersActivity").putExtra("type",1));
                    mContext.sendBroadcast(new Intent("Self_Service_RestanrauntActivity.Action").putExtra("type",2));
                }else {
                    Toast.makeText(mContext,"自助买单限"+selfservice_nums+"件商品,请前往人工收银台收银，谢谢配合！",Toast.LENGTH_SHORT).show();
                }


            }
        });

        mHordler.btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nums_str= finalMHordler.et_nums.getText().toString().trim();
                int nums=Integer.parseInt(nums_str);
                if(nums>1){
                    nums--;
                    mSelf_Service_GoodsInfolist.get(position).setNumber(nums+"");
                    finalMHordler.et_nums.setText(mSelf_Service_GoodsInfolist.get(position).getNumber());
                    finalMHordler.tv_goodsprice.setText(""+(Double.parseDouble(StringUtils.stringpointtwo(mSelf_Service_GoodsInfolist.get(position).getPrice()))*Integer.parseInt(mSelf_Service_GoodsInfolist.get(position).getNumber())));
                }else {
                    mSelf_Service_GoodsInfolist.remove(position);
                    notifyDataSetChanged();
                }
                mContext.sendBroadcast(new Intent("Self_Service_PlayOrdersActivity").putExtra("type",1));
                mContext.sendBroadcast(new Intent("Self_Service_RestanrauntActivity.Action").putExtra("type",2));

            }
        });
    }
        mHordler.btn_addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNotesDialog(position);
            }
        });

        return convertView;
    }
    private class Hordler{
        TextView tv_goodsname;
        TextView tv_goodsprice;
        TextView tv_goodsnotes;
        TextView tv_goods_size;
        Button btn_add,btn_addnotes;
        Button btn_cell;
        EditText et_nums;
    }

    /**
     * 添加备注弹窗
     */
    private View view;
    private android.support.v7.app.AlertDialog mAlertDialog_AddNotes;
    private InputMethodManager imm;
    private EditText et_notes;
    private void AddNotesDialog(final int position){
        android.support.v7.app.AlertDialog.Builder notes_dialog= new android.support.v7.app.AlertDialog.Builder(mContext,R.style.AlertDialog);
        view=View.inflate(mContext,R.layout.dialog_res_add_notes,null);
        et_notes= (EditText) view.findViewById(R.id.et_notes);
        et_notes.setText( mSelf_Service_GoodsInfolist.get(position).getNotes());
        et_notes.setSelection(et_notes.getText().toString().trim().length());
        Button btn_addnotes_cell= (Button) view.findViewById(R.id.btn_addnotes_cell);
        Button btn_addnotes_sure= (Button) view.findViewById(R.id.btn_addnotes_sure);
        //没有键盘自动弹出键盘
        if (!NoDoubleClickUtils.isSoftShowing(mContext)) {
            imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        btn_addnotes_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog_AddNotes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(mContext)) {
                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_addnotes_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_search_src=et_notes.getText().toString().trim();
                if (TextUtils.isEmpty(et_search_src)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(mContext,"输入内容不能为空！",25);
                    return;
                }
                mSelf_Service_GoodsInfolist.get(position).setNotes(et_search_src);
                notifyDataSetChanged();
                mAlertDialog_AddNotes.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(mContext)) {
                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        mAlertDialog_AddNotes = notes_dialog.setView(view).show();
        mAlertDialog_AddNotes.setCancelable(false);
        mAlertDialog_AddNotes.show();

    }
}
