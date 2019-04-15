package retail.yzx.com.kz;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Entty.Goods_Common_Notes;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Goods_Common_Notes_Adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.restaurant_nomal.Entry.ResDialog;
import retail.yzx.com.supper_self_service.Utils.StringUtils;

/**
 * 常用备注的页面
 */
public class Goods_Common_Notes_Activity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView iv_back;
    private Button btn_add_notes;
    private TextView tv_notes;
    private GridView gridview_content;
    private ArrayList<Goods_Common_Notes> mCommonNotesList;
    private Goods_Common_Notes_Adapter mGoods_Common_Notes_Adapter;

    @Override
    protected int getContentId() {
        return R.layout.activity_goods__common__notes_;
    }

    @Override
    protected void init() {
        super.init();
        StringUtils.setupUI(this, findViewById(R.id.activity_goods__common__notes_));//点击空白处隐藏软键盘

        iv_back= (ImageView) findViewById(R.id.iv_back);
        tv_notes= (TextView) findViewById(R.id.tv_notes);
        btn_add_notes= (Button) findViewById(R.id.btn_add_notes);
        gridview_content= (GridView) findViewById(R.id.gridview_content);

        iv_back.setOnClickListener(this);
        btn_add_notes.setOnClickListener(this);
        gridview_content.setOnItemClickListener(this);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        getCommonNotesInfo();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        retail.yzx.com.supper_self_service.Utils.StringUtils.initOKgo(Goods_Common_Notes_Activity.this);//初始化网络请求
//        setContentView(R.layout.activity_goods__common__notes_);
//        StringUtils.HideBottomBar(Goods_Common_Notes_Activity.this);
//        StringUtils.setupUI(this, findViewById(R.id.activity_goods__common__notes_));//点击空白处隐藏软键盘
//
////        initView();
////        inttData();
//    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Goods_Common_Notes_Activity.this);
//    }

//    private void initView() {
//        iv_back= (ImageView) findViewById(R.id.iv_back);
//        tv_notes= (TextView) findViewById(R.id.tv_notes);
//        btn_add_notes= (Button) findViewById(R.id.btn_add_notes);
//        gridview_content= (GridView) findViewById(R.id.gridview_content);
//
//        iv_back.setOnClickListener(this);
//        btn_add_notes.setOnClickListener(this);
//        gridview_content.setOnItemClickListener(this);
//    }
//    private void inttData() {
//
//        getCommonNotesInfo();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add_notes:
                ResDialog.AddNotesDialog(Goods_Common_Notes_Activity.this,"新增常用备注","请输入内容",tv_notes);
                ResDialog r=new ResDialog();
                r.setOnClickListener(new ResDialog.onClickSure() {
                    @Override
                    public void onClickSure() {
                        AddCommonNotesInfo(tv_notes.getText().toString().trim());
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        ResDialog.RemoveAndEditDialog(Goods_Common_Notes_Activity.this,mCommonNotesList.get(position).getNotes());
        ResDialog r=new ResDialog();
        r.setOnClickListener(new ResDialog.onClickRemove() {
            @Override
            public void onClickRemove() {
                RemoveCommonNotesInfo(position);
            }
        });
        r.setOnClickListener(new ResDialog.onClickEdit() {
            @Override
            public void onClickEdit() {
                tv_notes.setText(mCommonNotesList.get(position).getNotes());
                ResDialog.AddNotesDialog(Goods_Common_Notes_Activity.this,"编辑常用备注","请输入内容",tv_notes);
                ResDialog r=new ResDialog();
                r.setOnClickListener(new ResDialog.onClickSure() {
                    @Override
                    public void onClickSure() {
                        EditCommonNotesInfo(position,tv_notes.getText().toString().trim());
                    }
                });
            }
        });

    }

        //获取常用备注信息
    private void getCommonNotesInfo(){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                if(mCommonNotesList!=null){
                                    mCommonNotesList.clear();
                                }
                                JSONArray data=jo1.getJSONArray("data");
                                mCommonNotesList=new ArrayList<Goods_Common_Notes>();
                                if(data!=null){
                                    for(int i=0;i<data.length();i++){
                                        JSONObject dataobj=data.getJSONObject(i);
                                        String notes=dataobj.getString("notes");
                                        String notes_id=dataobj.getString("id");
                                        Goods_Common_Notes goods_common_notes=new Goods_Common_Notes(notes_id,notes);
                                        mCommonNotesList.add(goods_common_notes);
                                    }
                                }
                                mGoods_Common_Notes_Adapter=new Goods_Common_Notes_Adapter(Goods_Common_Notes_Activity.this,mCommonNotesList);
                                gridview_content.setAdapter(mGoods_Common_Notes_Adapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(Goods_Common_Notes_Activity.this,"服务器数据异常",20);
                            Log.e("print", "服务器数据异常: "+e.toString() );
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        StringUtils.showToast(Goods_Common_Notes_Activity.this,"网络不给力",20);
                    }
                });

    }
    //添加常用备注信息
    private void AddCommonNotesInfo(String notes){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_operation"))
                .tag(this)
                .params("notes",notes)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                getCommonNotesInfo();
                                StringUtils.showToast(Goods_Common_Notes_Activity.this,"新增成功",20);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(Goods_Common_Notes_Activity.this,"服务器数据异常",20);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        StringUtils.showToast(Goods_Common_Notes_Activity.this,"网络不给力",20);
                    }
                });
    }
    //删除常用备注信息
    private void RemoveCommonNotesInfo(final int position){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_operation"))
                .tag(this)
                .params("id",mCommonNotesList.get(position).getNotes_id())
                .params("type","remove")
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                getCommonNotesInfo();
                                StringUtils.showToast(Goods_Common_Notes_Activity.this,"删除成功",20);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(Goods_Common_Notes_Activity.this,"服务器数据异常",20);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        StringUtils.showToast(Goods_Common_Notes_Activity.this,"网络不给力",20);
                    }
                });
    }
    //编辑常用备注信息
    private void EditCommonNotesInfo(final int position,String editnotes){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_operation"))
                .tag(this)
                .params("id",mCommonNotesList.get(position).getNotes_id())
                .params("notes",editnotes)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                getCommonNotesInfo();
                                StringUtils.showToast(Goods_Common_Notes_Activity.this,"编辑成功",20);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(Goods_Common_Notes_Activity.this,"服务器数据异常",20);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        StringUtils.showToast(Goods_Common_Notes_Activity.this,"网络不给力",20);
                    }
                });
    }

}
