package retail.yzx.com.restaurant_nomal.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Res_Table;
import Entty.Res_Table_Sort;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Res_TableAdapter;
import adapters.Res_Table_Sort_Adapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.ResDialog;
import retail.yzx.com.restaurant_nomal.Entry.Res_GoodsOrders;
import retail.yzx.com.restaurant_nomal.Entry.Reserve_Entty;
import retail.yzx.com.restaurant_nomal.Reserve_Activity;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import shujudb.Sqlite_Entity;

/**
 * Created by Administrator on 2017/7/31.
 * 餐桌页面
 */

public class Res_Table_Fragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.list_res_table_sort)
    ListView listResTableSort;
    @BindView(R.id.list_res_table)
    RecyclerView listResTable;
    Unbinder unbinder;
    @BindView(R.id.tv_free)
    TextView tv_free;
    @BindView(R.id.tv_reserve_num)
    TextView tv_reserve_num;
    @BindView(R.id.tv_table_refresh)
    TextView tv_table_refresh;
    public static final List<Handler> handlerList=new ArrayList<>();
    public static final List<Runnable> runnables=new ArrayList<>();

    private ArrayList<Res_Table> mRes_TableList;//餐桌数据
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;//餐桌分类数据
    private Res_Table_Sort_Adapter mRes_Table_Sort_Adapter;//餐桌分类适配器
    private Res_TableAdapter mRes_TableAdapter;//
    private int cur_table_sort_position=0;//当前餐桌位置

    private int free_num=0;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.layout_res_table_fragment, null);
        ButterKnife.bind(this,view);
        listResTableSort.setOnItemClickListener(this);
        return view;
    }
    @Override
    protected void initData() {
        GetTableInfo();
        getfreenums();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRes_TableAdapter!=null){
            GetTableInfo();
        }
    }

    public void getfreenums(){
        OkGo.post(SysUtils.getTableServiceUrl("table_list_schedule"))
                .tag(this)
                .params("page","1")
                .params("schedule_status","0")
                .params("status",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        Reserve_Entty reserve_entty = gson.fromJson(s, Reserve_Entty.class);
                        tv_reserve_num.setText("："+reserve_entty.getResponse().getData().getCount());
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }
//餐桌分类点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cur_table_sort_position=position;
        mRes_Table_Sort_Adapter.setDefSelect(position);
        mRes_TableAdapter=new Res_TableAdapter(getActivity(),mRes_Table_SortList.get(position).getmRes_TableList());
        listResTable.setAdapter(mRes_TableAdapter);
        listResTable.setLayoutManager(new GridLayoutManager(getActivity(),5, LinearLayoutManager.VERTICAL,false));
    }
    private Dialog progressDialog = null;
    //获取餐桌信息
    private void GetTableInfo(){
        OkGo.post(SysUtils.getTableServiceUrl("table_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        progressDialog = retail.yzx.com.supper_self_service.Utils.StringUtils.createLoadingDialog(mContext, "请稍等...", true);
                        progressDialog.show();
                        Log.e("print", "餐桌信息Url：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "餐桌信息：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = JsonUtil.getJsonString(jo1,"status");
                            if (status.equals("200")) {
                                free_num=0;
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
                                                if (table_status.equals("0")){
                                                    free_num=free_num+1;
                                                }
                                            }
                                        }
                                        Res_Table_Sort mRes_Table_Sort=new Res_Table_Sort(type_id,type_name,false, false,mRes_TableList);
                                        mRes_Table_SortList.add(mRes_Table_Sort);
                                    }
                                    mRes_Table_Sort_Adapter =new Res_Table_Sort_Adapter(getActivity(),mRes_Table_SortList);
                                    listResTableSort.setAdapter(mRes_Table_Sort_Adapter);

                                    mRes_Table_SortList.get(cur_table_sort_position).setRes_table_sort_click(true);//默认第一个选中

                                    mRes_TableAdapter=new Res_TableAdapter(getActivity(),mRes_Table_SortList.get(cur_table_sort_position).getmRes_TableList());
                                    listResTable.setAdapter(mRes_TableAdapter);
                                    listResTable.setLayoutManager(new GridLayoutManager(getActivity(),5, LinearLayoutManager.VERTICAL,false));

                                    mRes_TableAdapter.setOnItemClick(new Res_TableAdapter.setOnItemClickListener() {
                                        @Override
                                        public void setOnItemClick(View view, int position) {
                                            Res_Table_Dialog(view,mRes_Table_SortList.get(cur_table_sort_position).getmRes_TableList().get(position));
                                        }
                                    });
                                }
                                tv_free.setText("空闲："+free_num);
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

    private void editTableStatus(final String table_status, final String id,final String table_name){
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
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"操作成功",20);
                                if("1".equals(table_status)){
                                    getActivity().sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",7).
                                            putExtra("table_id",id).putExtra("table_name",table_name).putExtra("peoplenums",""));
                                }else if("0".equals(table_status)){
                                    getActivity().sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",7).
                                            putExtra("table_id","").putExtra("table_name","").putExtra("peoplenums",""));
                                }

                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     *餐桌预定
     * 0空闲，1开桌，2使用者，3预定，4禁桌
     */

    private void BookTable(final String table_status, final String id,final String person_name,String nums,String mobile,String money,String reserve_time){
        OkGo.post(SysUtils.getTableServiceUrl("table_insert"))
                .tag(this)
                .params("id",id)
                .params("status",table_status)
                .params("person_name",person_name)
                .params("nums",nums)
                .params("mobile",mobile)
                .params("money",money)
                .params("reserve_time",reserve_time)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("print", "预定餐桌：" + request.getUrl());
                        Log.e("print", "预定餐桌：" + request.getParams());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "预定餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"操作成功",20);
                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     *获取预定详情
     * 0空闲，1开桌，2使用者，3预定，4禁桌
     */

    private void getBookTableDetail(final String table_status, final String id,final String table_name){
        OkGo.post(SysUtils.getTableServiceUrl("get_table_person_info"))
                .tag(this)
                .params("id",id)
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
                                JSONObject data=jo1.getJSONObject("data");
                                if(data!=null){
                                    String money=JsonUtil.getJsonString(data,"money");
                                    String person_name=JsonUtil.getJsonString(data,"person_name");
                                    String nums=JsonUtil.getJsonString(data,"nums");
                                    String reserve_time=JsonUtil.getJsonString(data,"reserve_time");
                                    String mobile=JsonUtil.getJsonString(data,"mobile");
                                    Book_Table_Dialog(1,id,table_name,person_name,mobile,nums,reserve_time,money);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //餐桌点击
    private View View_Res_Table_Dialog;
    private PopupWindow mPopupWindow;
    private Button btn_res_table_open;
    private Button btn_res_table_book;
    private ImageView iv_close;
    private void Res_Table_Dialog(View itemview, final Res_Table res_table){
        View_Res_Table_Dialog=View.inflate(getActivity(),R.layout.dialog_res_table_list_onclick_view,null);
        btn_res_table_open= (Button) View_Res_Table_Dialog.findViewById(R.id.btn_res_table_open);
        btn_res_table_book= (Button) View_Res_Table_Dialog.findViewById(R.id.btn_res_table_book);
        iv_close= (ImageView) View_Res_Table_Dialog.findViewById(R.id.iv_close);
        if("0".equals(res_table.getRes_table_status())){
            btn_res_table_open.setText("开  桌");
            btn_res_table_book.setText("预  定");
        }else if("1".equals(res_table.getRes_table_status())){
//            btn_res_table_book.setVisibility(View.GONE);
            btn_res_table_open.setText("关  桌");
            btn_res_table_book.setText("点  菜");
//            getActivity().sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",7).
//                    putExtra("table_id",res_table.getRes_table_id()).putExtra("table_name",res_table.getRes_table_name()).putExtra("peoplenums",""));
        }else if ("2".equals(res_table.getRes_table_status())){
            btn_res_table_open.setText("加菜/买单");
//            btn_res_table_book.setVisibility(View.GONE);
        }else if("3".equals(res_table.getRes_table_status())){
            btn_res_table_open.setVisibility(View.GONE);
            btn_res_table_book.setText("预定详情");
        }
        mPopupWindow=new PopupWindow(View_Res_Table_Dialog, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        ColorDrawable drawable=new ColorDrawable(0x20000000);
        mPopupWindow.setBackgroundDrawable(drawable);
        mPopupWindow.showAtLocation(itemview, Gravity.CENTER,0,0);
        btn_res_table_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(res_table.getRes_table_status())){
                    ResDialog.SureAndCellDialog(getActivity(),"开桌 "+res_table.getRes_table_name());
                    ResDialog r=new ResDialog();
                    r.setOnSureClick(new ResDialog.OnSureClick() {
                        @Override
                        public void OnSureClick(View v) {
                            editTableStatus("1",res_table.getRes_table_id(),res_table.getRes_table_name());
                        }
                    });
                }else if("1".equals(res_table.getRes_table_status())){
                    ResDialog.SureAndCellDialog(getActivity(),"关桌 "+res_table.getRes_table_name());
                    ResDialog r=new ResDialog();
                    r.setOnSureClick(new ResDialog.OnSureClick() {
                        @Override
                        public void OnSureClick(View v) {
                            editTableStatus("0",res_table.getRes_table_id(),res_table.getRes_table_name());
                        }
                    });
                }else if("2".equals(res_table.getRes_table_status())){
                    /**
                     * 点击加菜
                     */
                    Log.d("print", "点击了餐桌");
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"桌位正在使用中！",20);
                    getble_order(res_table.getRes_table_id());
                }else if("3".equals(res_table.getRes_table_status())){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"桌位已预订，请选择其他桌位！",20);
                }

                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();

                    mPopupWindow = null;
                }
            }
        });
        btn_res_table_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(res_table.getRes_table_status())){
                    Book_Table_Dialog(0,res_table.getRes_table_id(),"","","","","","");
                }else if ("1".equals(res_table.getRes_table_status())){
                                getActivity().sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",8).
                    putExtra("table_id",res_table.getRes_table_id()).putExtra("table_name",res_table.getRes_table_name()).putExtra("peoplenums",""));
                }
                else if("3".equals(res_table.getRes_table_status())){

                    getBookTableDetail("3",res_table.getRes_table_id(),res_table.getRes_table_name());

                }else {
                    getBookTableDetail("3",res_table.getRes_table_id(),res_table.getRes_table_name());
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定餐桌请选择空闲桌位！",20);
                }
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
        View_Res_Table_Dialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = View_Res_Table_Dialog.findViewById(R.id.layout_dialog).getBottom();
                int height_top = View_Res_Table_Dialog.findViewById(R.id.layout_dialog).getTop();
                int weight = View_Res_Table_Dialog.findViewById(R.id.layout_dialog).getLeft();
                int weight_right = View_Res_Table_Dialog.findViewById(R.id.layout_dialog).getRight();
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

    }
    //预定餐桌
    private  InputMethodManager imm;
    private View view_Book_Table_Dialog;
    private AlertDialog  mBook_Table_Dialog;
    private TextView tv_title;
    private TextView tv_edit;
    private EditText et_book_table_people;
    private EditText et_book_table_phone;
    private EditText et_book_table_peoplenums;
    private EditText et_table_book_money;
    private TextView et_book_table_date;
    private Button btn_cell;
    private Button btn_sure;
    private int book_type=0;
    private void Book_Table_Dialog(final int type, final String table_id, final String table_name,
                                   String person_name, String mobile, String nums, String reserve_time, final String money){//0直接预定，1预定编辑
        view_Book_Table_Dialog=View.inflate(getActivity(),R.layout.dialog_res_table_book_view,null);
        tv_title= (TextView) view_Book_Table_Dialog.findViewById(R.id.tv_title);
        tv_edit= (TextView) view_Book_Table_Dialog.findViewById(R.id.tv_edit);
        et_book_table_people= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_people);
        et_book_table_phone= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_phone);
        et_book_table_peoplenums= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_peoplenums);
        et_table_book_money= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_table_book_money);
        et_book_table_date= (TextView) view_Book_Table_Dialog.findViewById(R.id.et_book_table_date);
        btn_cell= (Button) view_Book_Table_Dialog.findViewById(R.id.btn_cell);
        btn_sure= (Button) view_Book_Table_Dialog.findViewById(R.id.btn_sure);
        if(type==0){
            tv_title.setText("餐桌预定");
            btn_cell.setText("取消");
            btn_sure.setText("确定");
            tv_edit.setVisibility(View.GONE);
            book_type=0;
        }else if(type==1){
            tv_title.setText("预定详情");
            btn_cell.setText("关闭");
            btn_sure.setText("就餐");
            tv_edit.setVisibility(View.VISIBLE);
            et_book_table_people.setInputType(InputType.TYPE_NULL);
            et_book_table_phone.setInputType(InputType.TYPE_NULL);
            et_book_table_peoplenums.setInputType(InputType.TYPE_NULL);
            et_table_book_money.setInputType(InputType.TYPE_NULL);
            et_book_table_people.setText(person_name);
            et_book_table_phone.setText(mobile);
            et_book_table_peoplenums.setText(nums);
            et_book_table_date.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(reserve_time)*1000));
            et_table_book_money.setText(StringUtils.stringpointtwo(money));
            book_type=1;
        }
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_type=0;
                tv_title.setText("编辑预定");
                btn_cell.setText("取消");
                btn_sure.setText("确定");
                String src_book_table_people=et_book_table_people.getText().toString().trim();
                et_book_table_people.setInputType(InputType.TYPE_CLASS_TEXT);
                et_book_table_phone.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_book_table_peoplenums.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_table_book_money.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(TextUtils.isEmpty(src_book_table_people)){
                    et_book_table_people.setSelection(src_book_table_people.length());
                }
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
       et_book_table_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_type==0){
                    DateTimeUtils.runTime(getActivity(),et_book_table_date);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_table_people=et_book_table_people.getText().toString().trim();
                String book_table_phone=et_book_table_phone.getText().toString().trim();
                String book_table_peoplenums=et_book_table_peoplenums.getText().toString().trim();
                String table_book_money=et_table_book_money.getText().toString().trim();
                String book_table_time=et_book_table_date.getText().toString().trim();
                String str_book_table_time;
                if(TextUtils.isEmpty(book_table_people)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定人姓名不能为空！",20);
                    return;
                } if(TextUtils.isEmpty(book_table_phone)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定人联系方式不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_peoplenums)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定人数不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(table_book_money)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定定金不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_time)||"选择就餐时间".equals(book_table_time)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(getActivity(),"预定就餐时间不能为空！",20);
                    return;
                }else {
                    str_book_table_time=DateTimeUtils.dataOne(book_table_time);
                }
                if(book_type==0){
                    BookTable("3",table_id,book_table_people,book_table_peoplenums,book_table_phone,table_book_money,str_book_table_time);
                    if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }else if(book_type==1){
                    BookTable("1",table_id,book_table_people,book_table_peoplenums,book_table_phone,table_book_money,str_book_table_time);
                    getActivity().sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type",7).putExtra("money",money).
                            putExtra("table_id",table_id).putExtra("table_name",table_name).putExtra("peoplenums",book_table_peoplenums));
                }
                mBook_Table_Dialog.dismiss();
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBook_Table_Dialog.dismiss();
                if(book_type==0){
                    if (NoDoubleClickUtils.isSoftShowing(getActivity())) {
                        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),R.style.AlertDialog);
        mBook_Table_Dialog=dialog.setView(view_Book_Table_Dialog).show();
        mBook_Table_Dialog.setCancelable(false);
        mBook_Table_Dialog.show();

    }

    private ArrayList<Self_Service_GoodsInfo> mSelf_Service_GoodsInfolist;
    private ArrayList<Res_GoodsOrders> mRes_GoodsOrderslist;
    /**
     *  根据桌号获取商品的方法
     */
    public void getble_order(String table_id){

        Sqlite_Entity sqlite_entity=new Sqlite_Entity(getActivity());
        String json=sqlite_entity.queryguadanorder(table_id);
        try {
            JSONArray jsonArray=new JSONArray(json);
            mRes_GoodsOrderslist=new ArrayList<Res_GoodsOrders>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String order_id=jsonObject.getString("order_id");
                String createtime=jsonObject.getString("createtime");
                String order_name=jsonObject.getString("name");
                String total_amount=jsonObject.getString("total_amount");
                String desk_num=jsonObject.getString("table_name");
                String customer_num=jsonObject.getString("customer_num");
                String dopackage=jsonObject.getString("package");
                String mark_text=jsonObject.getString("remarks");
//                String order_nums=jsonObject.getString("order_nums");
                String table_id1=jsonObject.getString("table_id");
                String Jsongoods=sqlite_entity.queryguadangoods(order_id);
                Log.e("print","打印挂单数据json"+Jsongoods);
                JSONArray jsonArray1=new JSONArray(Jsongoods);
                mSelf_Service_GoodsInfolist=new ArrayList<Self_Service_GoodsInfo>();
                for (int j=0;j<jsonArray1.length();j++){
                    JSONObject object=jsonArray1.getJSONObject(j);
                    String goods_id=object.getString("goods_id");
//                    String product_id=object.getString("product_id");
                    String name=object.getString("name");
                    String cost=object.getString("cost");
                    String price=object.getString("price");
                    String nums=object.getString("number");
                    String goods_size=object.getString("goods_size");
                    String goods_notes=object.getString("marketable");
                    String tag_id=object.getString("tag_id");
                    Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,nums,cost,price,goods_notes,goods_size,"1",tag_id);
                    mSelf_Service_GoodsInfolist.add(mSelf_Service_GoodsInfo);
                }
                Res_GoodsOrders mRes_GoodsOrders=new Res_GoodsOrders(order_id,createtime,desk_num,customer_num,mark_text,"1",total_amount,dopackage,order_name,table_id,false,mSelf_Service_GoodsInfolist);
                mRes_GoodsOrderslist.add(mRes_GoodsOrders);
                mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 3).putExtra("position",0).putParcelableArrayListExtra("mRes_GoodsOrderslist",mRes_GoodsOrderslist));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        OkGo.post(SysUtils.getSellerServiceUrl("catering_order_info_table_id"))
//                .params("table_id",table_id)
//                .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                Log.e("barcode", "返回餐桌绑定的数据：" + jsonObject);
//                                JSONObject jo1 = jsonObject.getJSONObject("response");
//                                String status = jo1.getString("status");
//                                if (status.equals("200")) {
//                                    JSONArray data=jo1.getJSONArray("data");
//                                    mRes_GoodsOrderslist=new ArrayList<Res_GoodsOrders>();
//                                    if(data!=null){
//                                        for(int i=0;i<data.length();i++){
//                                            JSONObject data_object=data.getJSONObject(i);
//                                            String order_id=data_object.getString("order_id");
//                                            String createtime=data_object.getString("createtime");
//                                            String order_name=data_object.getString("order_name");
//                                            String total_amount=data_object.getString("total_amount");
//                                            String desk_num=data_object.getString("desk_num");
//                                            String customer_num=data_object.getString("customer_num");
//                                            String dopackage=data_object.getString("package");
//                                            String mark_text=data_object.getString("mark_text");
//                                            String order_nums=data_object.getString("order_nums");
//                                            String table_id=data_object.getString("table_id");
//                                            JSONArray goods_info=null;
//                                            try{
//                                                goods_info=data_object.getJSONArray("goods_info");
//                                            }catch (Exception e){}
//                                            mSelf_Service_GoodsInfolist=new ArrayList<Self_Service_GoodsInfo>();
//                                            if(goods_info!=null){
//                                                for(int j=0;j<goods_info.length();j++){
//                                                    JSONObject goods_info_obj=goods_info.getJSONObject(j);
//                                                    String goods_id=goods_info_obj.getString("goods_id");
//                                                    String product_id=goods_info_obj.getString("product_id");
//                                                    String name=goods_info_obj.getString("name");
//                                                    String cost=goods_info_obj.getString("cost");
//                                                    String price=goods_info_obj.getString("price");
//                                                    String nums=goods_info_obj.getString("nums");
//                                                    String goods_size=goods_info_obj.getString("goods_size");
//                                                    String goods_notes=goods_info_obj.getString("goods_notes");
//                                                    Self_Service_GoodsInfo mSelf_Service_GoodsInfo=new Self_Service_GoodsInfo(goods_id,name,nums,cost,price,goods_notes,goods_size,product_id);
//                                                    mSelf_Service_GoodsInfolist.add(mSelf_Service_GoodsInfo);
//                                                }
//                                            }
//                                            Res_GoodsOrders mRes_GoodsOrders=new Res_GoodsOrders(order_id,createtime,desk_num,customer_num,mark_text,order_nums,total_amount,dopackage,order_name,table_id,false,mSelf_Service_GoodsInfolist);
//                                            mRes_GoodsOrderslist.add(mRes_GoodsOrders);
//                                            mContext.sendBroadcast(new Intent("Restaurant_Nomal_MainAcitvity.Action").putExtra("type", 3).putExtra("position",0).putParcelableArrayListExtra("mRes_GoodsOrderslist",mRes_GoodsOrderslist));
//                                        }
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(mContext,"服务器数据异常"+e.toString(),20);
//                                Log.d("barcode", "返回错误数据：" + e.toString());
//                            }
//                    }
//                });
    }

    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    RecyclerView recyclerView;
    private ArrayList<Res_Table> mRes_Table=new ArrayList<>();//餐桌数据
    //绑定桌号
    private void Bindingtable(){
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        view_add_nums_notes = View.inflate(getActivity(), R.layout.reserve_table, null);
        recyclerView= (RecyclerView) view_add_nums_notes.findViewById(R.id.recyclerView);
        Button but_cancel= (Button) view_add_nums_notes.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDialog_add_nums_notes!=null){
                    mAlertDialog_add_nums_notes.dismiss();
                }
            }
        });
        mRes_Table.clear();
        for (int i=0;i<mRes_Table_SortList.size();i++){
            for (int k=0;k<mRes_Table_SortList.get(i).getmRes_TableList().size();k++){
                if (mRes_Table_SortList.get(i).getmRes_TableList().get(k).getRes_table_status().equals("0")){
                    mRes_Table.add(mRes_Table_SortList.get(i).getmRes_TableList().get(k));
                }
            }
        }
        mRes_TableAdapter=new Res_TableAdapter(getActivity(),mRes_Table);
        mRes_TableAdapter.setOnItemClick(null);
        recyclerView.setAdapter(mRes_TableAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5, LinearLayoutManager.VERTICAL,false));
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    @OnClick({R.id.tv_reserve,R.id.tv_free,R.id.tv_table_refresh})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.tv_reserve:
                startActivity(new Intent(getActivity(), Reserve_Activity.class));
                break;
            case R.id.tv_free:
                Bindingtable();
                break;
            case R.id.tv_table_refresh:
                initData();
                break;
        }
    }

}
