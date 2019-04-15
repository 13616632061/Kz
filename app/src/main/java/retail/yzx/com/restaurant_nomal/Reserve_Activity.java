package retail.yzx.com.restaurant_nomal;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Res_Table;
import Entty.Res_Table_Sort;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Res_TableAdapter;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.R;
import retail.yzx.com.restaurant_nomal.Adapter.Res_ReserveAdapter;
import retail.yzx.com.restaurant_nomal.Entry.JsonUtil;
import retail.yzx.com.restaurant_nomal.Entry.Reserve_Entty;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import widget.MylistView;

/**
 * Created by admin on 2018/12/17.
 */

public class Reserve_Activity extends BaseActivity implements Res_ReserveAdapter.SetOnclick, Res_TableAdapter.setOnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private View view_Book_Table_Dialog;
    private AlertDialog mBook_Table_Dialog;
    @BindView(R.id.lv_reserve)
    MylistView lv_reserve;
    @BindView(R.id.cc_box)
    CheckBox cc_box;
    @BindView(R.id.but_next)
    Button but_next;
    @BindView(R.id.but_last)
    Button but_last;
    @BindView(R.id.tv_page)
    TextView tv_page;

    int page=0;
    String schedule_status="1";

    Reserve_Entty reserve_entty;
    Res_ReserveAdapter adapter;

    @Override
    protected int getContentId() {
        return R.layout.reserve_activity;
    }


    @Override
    protected void init() {
        super.init();
        cc_box.setOnCheckedChangeListener(Reserve_Activity.this);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        getReserve();
        mRes_Table=new ArrayList<>();
    }

    /**
     * 获取预定订单列表
     */
    public void getReserve(){
        OkGo.post(SysUtils.getTableServiceUrl("table_list_schedule"))
                .tag(this)
                .params("page",page)
                .params("schedule_status","0")
                .params("status",schedule_status)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","获取预定的订单列表"+s);
                        Gson gson=new Gson();
                        reserve_entty=gson.fromJson(s,Reserve_Entty.class);
                        List<Reserve_Entty.ResponseBean.DataBean.ListsBean> lists = reserve_entty.getResponse().getData().getLists();
                        adapter=new Res_ReserveAdapter(Reserve_Activity.this,lists);
                        adapter=new Res_ReserveAdapter(Reserve_Activity.this,lists);
                        adapter.SetOnclick(Reserve_Activity.this);
                        lv_reserve.setAdapter(adapter);
                        tv_page.setText(page+"/"+reserve_entty.getResponse().getData().getTotalPage());
                    }
                });
    }

    @OnClick({R.id.tv_huanghui,R.id.btn_reserve,R.id.but_next,R.id.but_last})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.tv_huanghui:
                this.finish();
                break;
            case R.id.btn_reserve:
                Book_Table_Dialog(false);
                break;
                //下一页
            case R.id.but_next:
                if (reserve_entty.getResponse().getData().getTotalPage()>page){
                    page++;
                    loadDatas();
                }else {

                }
                break;
                //上一页
            case R.id.but_last:
                if (reserve_entty.getResponse().getData().getTotalPage()>1){
                    page--;
                    loadDatas();
                }else {

                }
                break;
        }
    }

    /**
     * 餐桌预定的弹窗
     */
    private TextView tv_title;
    private TextView tv_edit;
    private EditText et_book_table_people;
    private EditText et_book_table_phone;
    private EditText et_book_table_peoplenums;
    private EditText et_table_book_money;
    private TextView et_book_table_date;
    private Button btn_cell;
    private Button btn_sure;
    public void Book_Table_Dialog(final boolean edit){
        view_Book_Table_Dialog=View.inflate(Reserve_Activity.this,R.layout.dialog_res_table_book_view,null);
        AlertDialog.Builder dialog=new AlertDialog.Builder(Reserve_Activity.this,R.style.AlertDialog);
        mBook_Table_Dialog=dialog.setView(view_Book_Table_Dialog).show();
        mBook_Table_Dialog.setCancelable(false);
        tv_title= (TextView) view_Book_Table_Dialog.findViewById(R.id.tv_title);
        tv_edit= (TextView) view_Book_Table_Dialog.findViewById(R.id.tv_edit);
        tv_edit.setText("取消预定");
        et_book_table_people= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_people);
        et_book_table_phone= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_phone);
        et_book_table_peoplenums= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_book_table_peoplenums);
        et_table_book_money= (EditText) view_Book_Table_Dialog.findViewById(R.id.et_table_book_money);
        et_book_table_date= (TextView) view_Book_Table_Dialog.findViewById(R.id.et_book_table_date);
        btn_cell= (Button) view_Book_Table_Dialog.findViewById(R.id.btn_cell);
        btn_sure= (Button) view_Book_Table_Dialog.findViewById(R.id.btn_sure);
        et_book_table_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DateTimeUtils.runTime(Reserve_Activity.this,et_book_table_date);
            }
        });

        if (edit){
            if (dataBean!=null){
                et_book_table_people.setText(dataBean.getPerson_name());
                et_book_table_phone.setText(dataBean.getMobile());
                et_book_table_peoplenums.setText(dataBean.getNums());
                et_table_book_money.setText(dataBean.getMoney());
                et_book_table_date.setText(DateTimeUtils.getDateTimeFromMillisecond(Long.parseLong(dataBean.getReserve_time())*1000)+"");
            }
        }

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book_table_people=et_book_table_people.getText().toString().trim();
                String book_table_phone=et_book_table_phone.getText().toString().trim();
                String book_table_peoplenums=et_book_table_peoplenums.getText().toString().trim();
                String table_book_money=et_table_book_money.getText().toString().trim();
                String book_table_time=et_book_table_date.getText().toString().trim();
                String str_book_table_time;
                if(TextUtils.isEmpty(book_table_people)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人姓名不能为空！",20);
                    return;
                } if(TextUtils.isEmpty(book_table_phone)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人联系方式不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_peoplenums)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人数不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(table_book_money)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定定金不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_time)||"选择就餐时间".equals(book_table_time)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定就餐时间不能为空！",20);
                    return;
                }else {
                    str_book_table_time=DateTimeUtils.dataOne(book_table_time);
                }
                if (dataBean.getStatus().equals("1")){
                    BookTable("3",true,dataBean.getId(),"3",book_table_people,book_table_peoplenums,book_table_phone,table_book_money,str_book_table_time);
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
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人姓名不能为空！",20);
                    return;
                } if(TextUtils.isEmpty(book_table_phone)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人联系方式不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_peoplenums)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定人数不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(table_book_money)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定定金不能为空！",20);
                    return;
                }if(TextUtils.isEmpty(book_table_time)||"选择就餐时间".equals(book_table_time)){
                    retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定就餐时间不能为空！",20);
                    return;
                }else {
                    str_book_table_time=DateTimeUtils.dataOne(book_table_time);
                }
                if (edit){
                    BookTable("1",true,dataBean.getId(),"3",book_table_people,book_table_peoplenums,book_table_phone,table_book_money,str_book_table_time);
                }else {
                    BookTable("1",false,"","3",book_table_people,book_table_peoplenums,book_table_phone,table_book_money,str_book_table_time);
                }
                    if (NoDoubleClickUtils.isSoftShowing(Reserve_Activity.this)) {
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                mBook_Table_Dialog.dismiss();
            }
        });
        btn_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBook_Table_Dialog.dismiss();
                    if (NoDoubleClickUtils.isSoftShowing(Reserve_Activity.this)) {
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
            }
        });
        mBook_Table_Dialog.show();
    }

    public void BookTable(String status,boolean edit,String id,final String table_status,final String person_name,String nums,String mobile,String money,String reserve_time){
        PostRequest table_insert_schedule = OkGo.post(SysUtils.getTableServiceUrl("table_insert_schedule"))
                .tag(this);

        if (edit){
            table_insert_schedule.params("id",id);
        }

        table_insert_schedule.params("status",table_status)
                .params("person_name",person_name)
                .params("status",status)
                .params("nums",nums)
                .params("mobile",mobile)
                .params("money",money)
                .params("reserve_time",reserve_time)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印的预定订单为"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject response1=jsonObject.getJSONObject("response");
                            String status=response1.getString("status");
                            if (status.equals("200")){
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"预定成功",20);
                                getReserve();
                            }else {
                                String data=jsonObject.getString("data");
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,data,20);
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
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("print", "预定餐桌：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                String data=jo1.getString("data");
                                if (mAlertDialog_add_nums_notes!=null){
                                    mAlertDialog_add_nums_notes.dismiss();
                                }
                                if (dataBean.getStatus().equals("1")){
                                    BookTable("2",true,dataBean.getId(),"3",dataBean.getPerson_name(),dataBean.getNums(),dataBean.getMobile(),dataBean.getMoney(),dataBean.getReserve_time());
                                }
                                retail.yzx.com.supper_self_service.Utils.StringUtils.showToast(Reserve_Activity.this,"操作成功",20);
//                                GetTableInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //点击绑定餐桌
    Reserve_Entty.ResponseBean.DataBean.ListsBean dataBean;
    @Override
    public void onclickdialog(int i) {
        if (reserve_entty!=null){
            dataBean= reserve_entty.getResponse().getData().getLists().get(i);
            GetTableInfo(false);
        }
    }

    //编辑预定列表
    @Override
    public void OnlickEdit(int i) {
        if (reserve_entty!=null){
            dataBean= reserve_entty.getResponse().getData().getLists().get(i);
            Book_Table_Dialog(true);
        }
    }

    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    private InputMethodManager imm;
    RecyclerView recyclerView;
    private Res_TableAdapter mRes_TableAdapter;//
    //绑定桌号
    private void Bindingtable(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Reserve_Activity.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(Reserve_Activity.this, R.layout.reserve_table, null);
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
            mRes_Table.addAll(mRes_Table_SortList.get(i).getmRes_TableList());
        }
        mRes_TableAdapter=new Res_TableAdapter(Reserve_Activity.this,mRes_Table);
        mRes_TableAdapter.setOnItemClick(this);
        recyclerView.setAdapter(mRes_TableAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(Reserve_Activity.this,5, LinearLayoutManager.VERTICAL,false));
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(false);
        mAlertDialog_add_nums_notes.show();
    }

    private ArrayList<Res_Table> mRes_TableList;//餐桌数据
    private ArrayList<Res_Table> mRes_Table;//餐桌数据
    private ArrayList<Res_Table_Sort> mRes_Table_SortList;//餐桌分类数据
    //获取餐桌信息
    private void GetTableInfo(final boolean iswhole){
        OkGo.post(SysUtils.getTableServiceUrl("table_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
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
                                                    if (iswhole){
                                                        mRes_TableList.add(mRes_Table);
                                                    }else {
                                                        if (table_status.equals("0")){
                                                            mRes_TableList.add(mRes_Table);
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                        Res_Table_Sort mRes_Table_Sort=new Res_Table_Sort(type_id,type_name,false, false,mRes_TableList);
                                        mRes_Table_SortList.add(mRes_Table_Sort);
                                    }
                                    Bindingtable();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 绑定桌号
     * @param view
     * @param position
     */
    @Override
    public void setOnItemClick(View view, int position) {
        BookTable("3",mRes_Table.get(position).getRes_table_id(),dataBean.getPerson_name(),dataBean.getNums(),dataBean.getMobile(),dataBean.getMoney(),dataBean.getReserve_time());
    }

    //选择全部的按钮
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            schedule_status="0";
            getReserve();
        }else {
            schedule_status="1";
            getReserve();
        }
    }
}
