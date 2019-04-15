package retail.yzx.com.restaurant_nomal.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import Utils.SharedUtil;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Restaurant_Nomal_MainAcitvity;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;

/**
 * Created by Administrator on 2017/7/30.
 */

public class ResDialog {
    //创建接口
    public  static interface onClickSure{
        void onClickSure();
    }
    //接口对象
    public static onClickSure monClickSure;
    //设置监听器 也就是实例化接口
    public void setOnClickListener(final onClickSure monClickSure) {
        this.monClickSure = monClickSure;
    }
    /**
     * 确定，取消 输入框弹窗
     */
    private static View view;
    private static android.support.v7.app.AlertDialog mAlertDialog_AddNotes;
    private static InputMethodManager imm;
    private static EditText et_notes;

    public static void AddNotesDialog(final Activity mContext, String title, String hint, final TextView textview){
        android.support.v7.app.AlertDialog.Builder notes_dialog= new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AlertDialog);
        view=View.inflate(mContext,R.layout.dialog_res_add_notes,null);
        et_notes= (EditText) view.findViewById(R.id.et_notes);
        String tv_content=textview.getText().toString().trim();
        if(title.indexOf("编辑")!=-1){
            et_notes.setText(tv_content);
            et_notes.setSelection(tv_content.length());
        }else {
            et_notes.setHint(hint);
        }

        TextView tv_search= (TextView) view.findViewById(R.id.tv_search);
        tv_search.setText(title);
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
                textview.setText(et_search_src);
                if(monClickSure!=null){
                    monClickSure.onClickSure();
                }
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
    //创建删除接口
    public  static interface onClickRemove{
        void onClickRemove();
    }
    //接口对象
    public static onClickRemove monClickRemove;
    //设置监听器 也就是实例化接口
    public void setOnClickListener(final onClickRemove monClickRemove) {
        this.monClickRemove= monClickRemove;
    }
    //创建编辑接口
    public  static interface onClickEdit{
        void onClickEdit();
    }
    //接口对象
    public static onClickEdit monClickEdit;
    //设置监听器 也就是实例化接口
    public void setOnClickListener(final onClickEdit monClickEdit) {
        this.monClickEdit= monClickEdit;
    }
    //删除编辑dialog
    private static android.support.v7.app.AlertDialog mRemoveAndEditDialog;
    private static View dialog_remove_edit_view;
public static void RemoveAndEditDialog(final Context context,String notes){
    android.support.v7.app.AlertDialog.Builder notes_dialog= new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialog);
    dialog_remove_edit_view=View.inflate(context,R.layout.dialog_remove_edit,null);
    TextView tv_notes= (TextView) dialog_remove_edit_view.findViewById(R.id.tv_notes);
    tv_notes.setText(notes);
    Button btn_notes_remove= (Button) dialog_remove_edit_view.findViewById(R.id.btn_notes_remove);
    Button btn_notes_edits= (Button) dialog_remove_edit_view.findViewById(R.id.btn_notes_edits);
    btn_notes_remove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRemoveAndEditDialog.dismiss();
            if(monClickRemove!=null){
                monClickRemove.onClickRemove();
            }
        }
    });
    btn_notes_edits.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mRemoveAndEditDialog.dismiss();
            if(monClickEdit!=null){
                monClickEdit.onClickEdit();
            }
        }
    });
    mRemoveAndEditDialog=notes_dialog.setView(dialog_remove_edit_view).show();
    mRemoveAndEditDialog.show();
}
    /**
     * 锁屏dialog
     * @param mContext
     */
    public static void LockDialog(final Activity mContext){
        View layout = View.inflate(mContext,R.layout.dialog_lockscreen, null);
        final EditText ed_password = (EditText) layout.findViewById(R.id.ed_password);
        ed_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        final TextView ed_text = (TextView) layout.findViewById(R.id.ed_user);
        ed_text.setText(SharedUtil.getString("seller_name"));
        final Button but_lock = (Button) layout.findViewById(R.id.but_lock);
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setTitle("锁屏");
        dialog.show();
        dialog.getWindow().setContentView(layout);

        but_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_password.getText().toString().equals(SharedUtil.getString("password"))) {
                    dialog.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(mContext)) {
                        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });
    }

    /**
     * 订单未处理dialog
     * @param mContext
     */
    public static void GotoDoOrderDialog(Context mContext){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_untreated);
        Button but_dimdis = (Button) window.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    //确定接口
    public interface OnSureClick{
        void OnSureClick(View v);
    }
    public static OnSureClick mOnSureClick;
    public void setOnSureClick(OnSureClick mOnSureClick){
        this.mOnSureClick=mOnSureClick;
    }
    /**
     * 确定取消
     * @param mContext
     */
    private static android.support.v7.app.AlertDialog malertDialog=null;
    public static void SureAndCellDialog(Context mContext,String title){
        if (NoDoubleClickUtils.isSoftShowing((Activity) mContext)) {
            imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        if(NoDoubleClickUtils.isDoubleClick()){
            return;
        }
        android.support.v7.app.AlertDialog.Builder alertDialog=new android.support.v7.app.AlertDialog.Builder(mContext,R.style.AlertDialog);
        View view=View.inflate(mContext,R.layout.dialog_remove,null);
        Button btn_addnotes_cell= (Button) view.findViewById(R.id.btn_addnotes_cell);
        Button btn_addnotes_sure= (Button) view.findViewById(R.id.btn_addnotes_sure);
        TextView tv_search= (TextView) view.findViewById(R.id.tv_search);
        tv_search.setText(title);
        btn_addnotes_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoDoubleClickUtils.isDoubleClick()){
                    return;
                }
                if(mOnSureClick!=null){
                    mOnSureClick.OnSureClick(v);
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

}
