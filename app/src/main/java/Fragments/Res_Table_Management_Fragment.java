package Fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Entty.Res_Table;
import Entty.Res_Table_Sort;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Res_TableAdapter;
import adapters.Res_Table_SortAdapter2;
import adapters.Spinner_Res_Table_Sort_Adapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.ResDialog;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;

/**
 * Created by Administrator on 2017/8/12.
 * 餐桌管理
 */

public class Res_Table_Management_Fragment extends Fragment implements View.OnClickListener {

    private Button btn_add_table_sort;//新增餐桌分类
    private Button btn_add_table_edit;//餐桌分类管理
    private Button btn_cell_table_sort_edit;//取消餐桌分类管理
    private Button btn_add_table;//新增餐桌
    private TextView tv_res_add_sort_name;//填写的新增餐桌分类名
    private RecyclerView recyclerview_table_sort;//餐桌分类列表
    private RecyclerView recyclerview_table;//餐桌列表
    private ArrayList<Res_Table> mRes_TableList;//餐桌数据
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;//餐桌分类数据
    private Res_Table_SortAdapter2 mRes_Table_SortAdapter;//餐桌分类适配器
    private Res_TableAdapter TableAdapter;//餐桌适配器
    private  int cur_sort_position=0;//当前点击的分类餐桌
    private Spinner_Res_Table_Sort_Adapter mSpinner_Res_Table_Sort_Adapter;
    private boolean res_table_sort_edit=false;//餐桌分类是否编辑


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.res_table_managment_fragment_view,null);
        initView(view);
        initData();

        return view;
    }


    private void initView(View view) {
        btn_add_table_sort= (Button) view.findViewById(R.id.btn_add_table_sort);
        btn_add_table= (Button) view.findViewById(R.id.btn_add_table);
        btn_add_table_edit= (Button) view.findViewById(R.id.btn_add_table_edit);
        tv_res_add_sort_name= (TextView) view.findViewById(R.id.tv_res_add_sort_name);
        btn_cell_table_sort_edit= (Button) view.findViewById(R.id.btn_cell_table_sort_edit);
        recyclerview_table_sort= (RecyclerView) view.findViewById(R.id.recyclerview_table_sort);
        recyclerview_table= (RecyclerView) view.findViewById(R.id.recyclerview_table);

        btn_add_table_sort.setOnClickListener(this);
        btn_add_table.setOnClickListener(this);
        btn_add_table_edit.setOnClickListener(this);
        btn_cell_table_sort_edit.setOnClickListener(this);
    }
    private void initData() {
        GetTableInfo();

    }
    //设置adapter中事件的监听
    private void setLinstener() {
        if(mRes_Table_SortAdapter!=null&&TableAdapter!=null){
            //餐桌分类的点击事件
            mRes_Table_SortAdapter.setOnItemClick(new Res_Table_SortAdapter2.setOnItemClickListener() {
                @Override
                public void setOnItemClick(View view, final int position) {
                    String table_sort=mRes_Table_SortList.get(position).getRes_table_sort_name();
                   switch (view.getId()){
                       case R.id.btn_remove:
                           ResDialog.SureAndCellDialog(getActivity(),"删除 "+table_sort);
                           ResDialog r=new ResDialog();
                           r.setOnSureClick(new ResDialog.OnSureClick() {
                               @Override
                               public void OnSureClick(View v) {
                                   if(mRes_Table_SortList.get(position).getmRes_TableList().size()>0){
                                       retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"此分类含有餐桌不能删除！",20);
                                   }else {
                                       CellTableSort(mRes_Table_SortList.get(position).getRes_table_sort_id());
                                   }
                               }
                           });
                           break;
                       case R.id.btn_edit:
                           tv_res_add_sort_name.setText(table_sort);
                           ResDialog.AddNotesDialog(getActivity(),"编辑餐桌分类",table_sort,tv_res_add_sort_name);
                           ResDialog r_edit=new ResDialog();
                           r_edit.setOnClickListener(new ResDialog.onClickSure() {
                               @Override
                               public void onClickSure() {
                                   editTableSort(tv_res_add_sort_name.getText().toString().trim(),mRes_Table_SortList.get(position).getRes_table_sort_id());
                               }
                           });

                           break;
                      default:
                           cur_sort_position=position;
                          TableAdapter=new Res_TableAdapter(getActivity(),mRes_Table_SortList.get(position).getmRes_TableList());
                           recyclerview_table.setAdapter(TableAdapter);
                           recyclerview_table.setLayoutManager(new GridLayoutManager(getActivity(),9, LinearLayoutManager.VERTICAL,false));
                           break;
                   }
                }
            });
//            //餐桌的点击事件
//            TableAdapter.setOnItemClick(new Res_TableAdapter.setOnItemClickListener() {
//                @Override
//                public void setOnItemClick(View view, int position) {
//                    if (mRes_Table_SortList.size()>cur_sort_position&&mRes_Table_SortList.get(cur_sort_position).getmRes_TableList().size()>position){
//                        Log.e("print","打印的数据"+mRes_Table_SortList);
//                        if (view!=null){
////                            Dialog_Res_Table_Management(view,mRes_Table_SortList.get(cur_sort_position).getmRes_TableList().get(position));
//                        }
//                    }
//                }
//            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_table_sort:
                ResDialog.AddNotesDialog(getActivity(),"新增餐桌分类","请输入新增餐桌分类名",tv_res_add_sort_name);
                ResDialog r=new ResDialog();
                r.setOnClickListener(new ResDialog.onClickSure() {
                    @Override
                    public void onClickSure() {
                        AddTableSort(tv_res_add_sort_name.getText().toString().trim());
                    }
                });
                break;
            case R.id.btn_add_table:
                Add_Dialog_Res_Table(0,null);
                break;
            case R.id.btn_add_table_edit:
                if(mRes_Table_SortAdapter!=null){
                mRes_Table_SortAdapter.setRes_table_sort_edit(true);
                mRes_Table_SortAdapter.notifyDataSetChanged();
                btn_cell_table_sort_edit.setVisibility(View.VISIBLE);
                res_table_sort_edit=true;
                }
                break;
            case R.id.btn_cell_table_sort_edit:
                mRes_Table_SortAdapter.setRes_table_sort_edit(false);
                mRes_Table_SortAdapter.notifyDataSetChanged();
                btn_cell_table_sort_edit.setVisibility(View.GONE);
                res_table_sort_edit=false;
                break;

        }
    }
    //餐桌管理弹框
    private PopupWindow mPopupWindow;
    private View view_Dialog_Res_Table;
    private Button btn_res_table_ban;
    private Button btn_res_table_free;
    private void Dialog_Res_Table_Management(View itemview, final Res_Table res_table){
//        if(mPopupWindow==null){
//        if (getActivity()!=null){
            view_Dialog_Res_Table=View.inflate(getActivity(), R.layout.dialog_res_table_onclick_view,null);
            LinearLayout layout_dialog= (LinearLayout) view_Dialog_Res_Table.findViewById(R.id.layout_dialog);
            ImageView iv_close= (ImageView) view_Dialog_Res_Table.findViewById(R.id.iv_close);
            btn_res_table_ban= (Button) view_Dialog_Res_Table.findViewById(R.id.btn_res_table_ban);
            Button btn_res_table_edit= (Button) view_Dialog_Res_Table.findViewById(R.id.btn_res_table_edit);
            Button btn_res_table_remove= (Button) view_Dialog_Res_Table.findViewById(R.id.btn_res_table_remove);
            btn_res_table_free= (Button) view_Dialog_Res_Table.findViewById(R.id.btn_res_table_free);
            TextView tv_res_table_name= (TextView) view_Dialog_Res_Table.findViewById(R.id.tv_res_table_name);
            tv_res_table_name.setText(res_table.getRes_table_name());
            if("4".equals(res_table.getRes_table_status())){
                btn_res_table_ban.setText("取消禁桌");
            }else{
                btn_res_table_ban.setText("禁桌");
            }
         mPopupWindow=new PopupWindow(view_Dialog_Res_Table, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        ColorDrawable colorDrawable=new ColorDrawable(0x20000000);
        mPopupWindow.setBackgroundDrawable(colorDrawable);
        mPopupWindow.showAtLocation(itemview, Gravity.CENTER,0,0);
            btn_res_table_ban.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("0".equals(res_table.getRes_table_status())) {
                        ResDialog.SureAndCellDialog(getActivity(), "禁桌 " + res_table.getRes_table_name());
                        ResDialog r = new ResDialog();
                        r.setOnSureClick(new ResDialog.OnSureClick() {
                            @Override
                            public void OnSureClick(View v) {
                                editTableStatus("4", res_table.getRes_table_id());
                            }
                        });
                    }else if("4".equals(res_table.getRes_table_status())){
                        ResDialog.SureAndCellDialog(getActivity(), "取消禁桌 " + res_table.getRes_table_name());
                        ResDialog r = new ResDialog();
                        r.setOnSureClick(new ResDialog.OnSureClick() {
                            @Override
                            public void OnSureClick(View v) {
                                editTableStatus("0", res_table.getRes_table_id());
                            }
                        });
                    }else if("1".equals(res_table.getRes_table_status())){
                        retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌处于开桌状态不能禁桌，请先关桌",20);
                    }else if("2".equals(res_table.getRes_table_status())){
                        retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌正在使用不能禁桌",20);
                    }else if("3".equals(res_table.getRes_table_status())){
                        retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌已预定不能禁桌，请先关桌",20);
                    }
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();

                        mPopupWindow = null;
                    }

                }
            });
        btn_res_table_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResDialog.SureAndCellDialog(getActivity(),"空闲 "+res_table.getRes_table_name());
                ResDialog r=new ResDialog();
                r.setOnSureClick(new ResDialog.OnSureClick() {
                    @Override
                    public void OnSureClick(View v) {
                        editTableStatus("0", res_table.getRes_table_id());
                    }
                });
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();

                    mPopupWindow = null;
                }
            }
        });
            btn_res_table_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Add_Dialog_Res_Table(1,res_table);
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();

                        mPopupWindow = null;
                    }

                }
            });
            btn_res_table_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResDialog.SureAndCellDialog(getActivity(),"删除 "+res_table.getRes_table_name());
                    ResDialog r=new ResDialog();
                    r.setOnSureClick(new ResDialog.OnSureClick() {
                        @Override
                        public void OnSureClick(View v) {
                            delTableInfo(res_table.getRes_table_id());
                        }
                    });
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();

                        mPopupWindow = null;
                    }
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();

                        mPopupWindow = null;
                    }
                }
            });
        //设置弹框添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view_Dialog_Res_Table.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view_Dialog_Res_Table.findViewById(R.id.layout_dialog).getBottom();
                int height_top = view_Dialog_Res_Table.findViewById(R.id.layout_dialog).getTop();
                int weight = view_Dialog_Res_Table.findViewById(R.id.layout_dialog).getLeft();
                int weight_right = view_Dialog_Res_Table.findViewById(R.id.layout_dialog).getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || y < height_top) {
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();

                            mPopupWindow = null;
                        }
                    }
                    if (x < weight || x > weight_right) {
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();

                            mPopupWindow = null;
                        }
                    }
                }
                return true;
            }
        });
//        }
        }
    //新增餐桌弹框
    private AlertDialog madd_res_table_dialog;
    private View view_Dialog_Res_Table_add;
    private  InputMethodManager imm;
    private String table_sort_id="";
    private void Add_Dialog_Res_Table(final int type, final Res_Table res_table){//0表示添加，1表示编辑
            view_Dialog_Res_Table_add=View.inflate(getActivity(), R.layout.dialog_add_res_table_onclick_view,null);
        Spinner spinner_res_table_sort= (Spinner) view_Dialog_Res_Table_add.findViewById(R.id.spinner_res_table_sort);
        //将可选内容与ArrayAdapter连接起来
        mSpinner_Res_Table_Sort_Adapter = new Spinner_Res_Table_Sort_Adapter(getActivity(),mRes_Table_SortList);

        //设置下拉列表的风格
        spinner_res_table_sort.setDropDownWidth(300);
        //将adapter 添加到spinner中
        spinner_res_table_sort.setAdapter(mSpinner_Res_Table_Sort_Adapter);
        spinner_res_table_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table_sort_id=mRes_Table_SortList.get(position).getRes_table_sort_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText et_tablename= (EditText) view_Dialog_Res_Table_add.findViewById(R.id.et_tablename);
        final EditText et_tablenums= (EditText) view_Dialog_Res_Table_add.findViewById(R.id.et_tablenums);
        TextView tv_title= (TextView) view_Dialog_Res_Table_add.findViewById(R.id.tv_title);
        Button btn_cell= (Button) view_Dialog_Res_Table_add.findViewById(R.id.btn_cell);
        Button btn_sure= (Button) view_Dialog_Res_Table_add.findViewById(R.id.btn_sure);

        if(type==1){
            tv_title.setText("编辑餐桌");
            et_tablename.setText(res_table.getRes_table_name());
            et_tablename.setSelection(res_table.getRes_table_name().length());
            et_tablenums.setText(res_table.getRes_table_people_nums());
            for(int i=0;i<mRes_Table_SortList.size();i++){
                if(res_table.getRes_table_sort_id().equals(mRes_Table_SortList.get(i).getRes_table_sort_id())){
                    spinner_res_table_sort.setSelection(i);
                }
            }
        }
        //没有键盘自动弹出键盘
        if (!NoDoubleClickUtils.isSoftShowing(getActivity())) {
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madd_res_table_dialog.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tablename=et_tablename.getText().toString().trim();
                String tablenums=et_tablenums.getText().toString().trim();
                if(TextUtils.isEmpty(tablename)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌名不能为空！",20);
                    return;
                }
                if(TextUtils.isEmpty(tablenums)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌人数不能为空！",20);
                    return;
                }
                if(TextUtils.isEmpty(table_sort_id)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"餐桌分类不能为空！",20);
                    return;
                }
                if(type==0){
                    AddTable(tablename,tablenums,table_sort_id,"");
                }else {
                    AddTable(tablename,tablenums,table_sort_id,res_table.getRes_table_id());
                }

                madd_res_table_dialog.dismiss();
                if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });

            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(), R.style.AlertDialog);


        madd_res_table_dialog=dialog.setView(view_Dialog_Res_Table_add).show();
        madd_res_table_dialog.setCancelable(false);
        madd_res_table_dialog.show();
        }
    //添加餐桌,及编辑餐桌
    private void AddTable(String tablename,String tablenums,String tablesort,String id){
      OkGo.post(SysUtils.getTableServiceUrl("table_insert"))
                .tag(this)
                .params("table_name",tablename)
                .params("type_id",tablesort)
                .params("id",id)
                .params("nums",tablenums)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "新增餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),data,20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //禁桌
    private void editTableStatus(String status,String id){
      OkGo.post(SysUtils.getTableServiceUrl("table_insert"))
                .tag(this)
                .params("id",id)
                .params("status",status)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "禁桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"操作成功",20);
                                if("4".equals(status)){
                                    btn_res_table_ban.setText("取消禁桌");
                                }else if("0".equals(status)){
                                    btn_res_table_ban.setText("禁桌");
                                }
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //删除餐桌
    private void delTableInfo(String id){
        OkGo.post(SysUtils.getTableServiceUrl("table_del"))
                .tag(this)
                .params("id",id)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "新增餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),data,20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //添加餐桌分类
    private void AddTableSort(String str){
        OkGo.post(SysUtils.getTableServiceUrl("type_insert"))
                .tag(this)
                .params("type_name",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "新增餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),data,20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //删除餐桌分类
    private void CellTableSort(String str){
        OkGo.post(SysUtils.getTableServiceUrl("type_del"))
                .tag(this)
                .params("type_id",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "删除餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),data,20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //编辑餐桌分类
    private void editTableSort(String str,String id){
        OkGo.post(SysUtils.getTableServiceUrl("type_edit"))
                .tag(this)
                .params("type_id",id)
                .params("type_name",str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "编辑餐桌：" + request.getUrl());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "编辑餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),data,20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //获取餐桌信息
    private void GetTableInfo(){
        OkGo.post(SysUtils.getTableServiceUrl("table_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "餐桌信息Url：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "餐桌信息：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = JsonUtil.getJsonString(jo1,"status");
                            if (status.equals("200")) {
                                JSONArray data=JsonUtil.getJsonArray(jo1,"data");
                                mRes_Table_SortList=new ArrayList<>();
                                if(data!=null){
                                    for(int i=0;i<data.length();i++){
                                        JSONObject data_obj=data.getJSONObject(i);
                                        String type_name=JsonUtil.getJsonString(data_obj,"type_name");
                                        String type_id=JsonUtil.getJsonString(data_obj,"type_id");
                                        JSONArray info=JsonUtil.getJsonArray(data_obj,"info");
                                        mRes_TableList=new ArrayList<>();
                                        if(info!=null){
                                            for(int j=0;j<info.length();j++){
                                                JSONObject info_obj=info.getJSONObject(j);
                                                String table_id=JsonUtil.getJsonString(info_obj,"id");
                                                String table_name=JsonUtil.getJsonString(info_obj,"table_name");
                                                String table_status=JsonUtil.getJsonString(info_obj,"status");
                                                String table_nums=JsonUtil.getJsonString(info_obj,"nums");
                                                String reserve_time=JsonUtil.getJsonString(info_obj,"reserve_time");
                                                if(!TextUtils.isEmpty(table_id)&&!"null".equals(table_id)){
                                                    Res_Table mRes_Table=new Res_Table(type_id,type_name,table_id,table_name,table_nums,"",table_status,reserve_time);
                                                    mRes_TableList.add(mRes_Table);
                                                }
                                            }
                                        }
                                        Res_Table_Sort mRes_Table_Sort=new Res_Table_Sort(type_id,type_name,false, res_table_sort_edit,mRes_TableList);
                                        mRes_Table_SortList.add(mRes_Table_Sort);
                                    }

                                    mRes_Table_SortAdapter=new Res_Table_SortAdapter2(getActivity(),mRes_Table_SortList);
                                    recyclerview_table_sort.setAdapter(mRes_Table_SortAdapter);
                                    LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                                    recyclerview_table_sort.setLayoutManager(mLinearLayoutManager);
                                    mRes_Table_SortList.get(0).setRes_table_sort_click(true);//默认第一个选中

                                    TableAdapter=new Res_TableAdapter(getActivity(),mRes_Table_SortList.get(cur_sort_position).getmRes_TableList());
                                    recyclerview_table.setAdapter(TableAdapter);
                                    recyclerview_table.setLayoutManager(new GridLayoutManager(getActivity(),9, LinearLayoutManager.VERTICAL,false));
                                    TableAdapter.setOnItemClick(new Res_TableAdapter.setOnItemClickListener() {
                                        @Override
                                        public void setOnItemClick(View view, int position) {
                                            Log.e("print","打印的数据"+mRes_Table_SortList);
                                            Dialog_Res_Table_Management(view,mRes_Table_SortList.get(cur_sort_position).getmRes_TableList().get(position));
                                        }
                                    });
                                    mRes_Table_SortAdapter.setRes_table_sort_edit(res_table_sort_edit);
                                    setLinstener();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
