package retail.yzx.com.kz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Fenlei_Entty;
import Utils.SysUtils;
import adapters.Unit_adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import widget.MyGirdView;

/**
 * Created by admin on 2017/7/29.
 */
public class Provider_activity extends BaseActivity implements View.OnClickListener, Unit_adapter.DeliteOnclick {

    public ImageView im_huanghui;
    public MyGirdView gv_unit;
    public Unit_adapter adapter;
    public List<Fenlei_Entty> adats;
    public Button but_newunit;
    public Button im_delete,im_redact;
    public TextView tv_title;

    public List<Boolean> isckbox;
    public Button but_confirm;

    public List<Map<String, String>> mapList=new ArrayList<>();

    //    删除按钮的判断
    public boolean isVisibility=false;
    //    编辑按钮的判断
    public boolean isedit=false;



    @Override
    protected int getContentId() {
        return R.layout.unit;
    }

    @Override
    protected void init() {
        super.init();

        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(this);
        but_confirm= (Button) findViewById(R.id.but_confirm);

        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);
        gv_unit= (MyGirdView) findViewById(R.id.gv_unit);
        adats=new ArrayList<>();

//        复合选择
        isckbox=new ArrayList<>();



//        删除按钮
        im_delete= (Button) findViewById(R.id.im_delete);
        im_delete.setOnClickListener(this);
//        新增单位
        but_newunit= (Button) findViewById(R.id.but_newunit);
        but_newunit.setText("新增供应商");
        but_newunit.setOnClickListener(this);

//        编辑单位
        im_redact= (Button) findViewById(R.id.im_redact);
        im_redact.setOnClickListener(this);
        im_redact.setVisibility(View.GONE);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("供应商");
        adapter=new Unit_adapter(this);
        adapter.setDeliteOnclick(this);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        LoadAdats();
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unit);
////        init1();
//
////        Loaddata();
//
//
//    }

    //加载数据
    private void LoadAdats() {
        OkGo.post(SysUtils.getGoodsServiceUrl("provider_list"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","数据为"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo1=jsonobject.getJSONObject("response");
                            String status=jo1.getString("status");
                            if (status.equals("200")){
                                JSONArray ja1=jo1.getJSONArray("data");
                                adats.clear();
                                for (int i=0;i<ja1.length();i++){
                                    Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    fenlei_entty.setName(jo2.getString("provider_name"));
                                    fenlei_entty.setTag_id(Integer.parseInt(jo2.getString("provider_id")));
                                    adats.add(fenlei_entty);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.setAdats(adats);
                            gv_unit.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

//    private void Loaddata() {
//        OkGo.post(SysUtils.getGoodsServiceUrl("label_getlist"))
//                .tag(this)
//                .cacheKey("cacheKey")
//                .cacheMode(CacheMode.DEFAULT)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Log.e("Tag",s);
//                        try {
//                            JSONObject jo=new JSONObject(s);
//                            JSONObject jo1=jo.getJSONObject("response");
//                            JSONObject jo2=jo1.getJSONObject("data");
//                            JSONArray ja=jo2.getJSONArray("units_info");
//                            adats.clear();
//                            for (int i=0;i<ja.length();i++){
//                                Fenlei_Entty fenlei_entty=new Fenlei_Entty();
//                                JSONObject jo3=ja.getJSONObject(i);
//                                fenlei_entty.setName(jo3.getString("label_name"));
//                                fenlei_entty.setTag_id(jo3.getInt("label_id"));
//                                adats.add(fenlei_entty);
//                            }
//                            for (int i=0;i<adats.size();i++){
//                                isckbox.add(true);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }finally {
//
//                            adapter.setAdats(adats);
//                            adapter.setcheck(isckbox);
//                            gv_unit.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//
//    }

//    private void init1() {
//
//        but_confirm= (Button) findViewById(R.id.but_confirm);
//
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//        gv_unit= (MyGirdView) findViewById(R.id.gv_unit);
//        adats=new ArrayList<>();
//
////        复合选择
//        isckbox=new ArrayList<>();
//
////        删除按钮
//        im_delete= (Button) findViewById(R.id.im_delete);
//        im_delete.setOnClickListener(this);
//        im_delete.setVisibility(View.GONE);
////        新增单位
//        but_newunit= (Button) findViewById(R.id.but_newunit);
//        but_newunit.setText("新增供应商");
//        but_newunit.setOnClickListener(this);
//        but_newunit.setVisibility(View.GONE);
//
////        编辑单位
//        im_redact= (Button) findViewById(R.id.im_redact);
//        im_redact.setOnClickListener(this);
//        im_redact.setVisibility(View.GONE);
//        tv_title= (TextView) findViewById(R.id.tv_title);
//        tv_title.setText("供应商");
//        adapter=new Unit_adapter(this);
//        adapter.setDeliteOnclick(this);
//
//        //隐藏底部按钮
////        toggleHideyBar();
//
//    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.im_delete:
                if(isedit){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(false);
                        isedit=false;
                    }
                    adapter.notifyDataSetChanged();
                }
                if(!isVisibility){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(true);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("编辑供应商");
                    isVisibility=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("供应商");
                    isVisibility=false;
                }
                break;
            case R.id.im_redact:
                if(isVisibility){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                        isVisibility=false;
                    }
                    adapter.notifyDataSetChanged();
                }
                if (!isedit){
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(true);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("编辑供应商");
                    isedit=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("供应商");
                    isedit=false;
                }
                break;
//             新增单位
            case R.id.but_newunit:
//                final EditText editText=new EditText(this);
//                final Fenlei_Entty fenlei_entty=new Fenlei_Entty();
//                final AlertDialog dialog=new AlertDialog.Builder(this).create();
//                dialog.setView(new EditText(this));
//                dialog.show();
//                Window window=dialog.getWindow();
//                window.setContentView(R.layout.dialog_newunit);
//                TextView tv_provider= (TextView) window.findViewById(R.id.tv_provider);
//                tv_provider.setText("新增供应商");
//                final EditText ed_new= (EditText) window.findViewById(R.id.ed_new);
//                ed_new.setHint("请输入供应商名称");
//                Button but_newquxiao= (Button) window.findViewById(R.id.but_newquxiao);
//                Button but_newqueding= (Button) window.findViewById(R.id.but_newqueding);
//                but_newquxiao.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.cancel();
//                    }
//                });
//                but_newqueding.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (!ed_new.getText().toString().isEmpty()){
//                            fenlei_entty.setName(ed_new.getText().toString());
//                            if(isVisibility){
//                                fenlei_entty.setVisibility(true);
//                            }else {
//                                fenlei_entty.setVisibility(false);
//                            }
//                            if (isedit){
//                                fenlei_entty.setIsedit(true);
//                            }else {
//                                fenlei_entty.setIsedit(false);
//                            }
//                            NewUnit(ed_new.getText().toString());
//
////                            adats.add(fenlei_entty);
////                            adapter.notifyDataSetChanged();
//                            dialog.cancel();
//                        }else {
//                            Toast.makeText(Provider_activity.this,"请输入供应商名称",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });


                final Dialog dialog = new Dialog(this);
                dialog.setTitle("新增供应商");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_provider);
                Button but_bncode= (Button) window.findViewById(R.id.but_bncode);
                Button but_new_provide= (Button) window.findViewById(R.id.but_new_provide);
                final EditText ed_bncode= (EditText) window.findViewById(R.id.ed_bncode);
                final EditText ed_name= (EditText) window.findViewById(R.id.ed_name);
                final EditText ed_py= (EditText) window.findViewById(R.id.ed_py);
                final EditText ed_linkman= (EditText) window.findViewById(R.id.ed_linkman);
                final EditText ed_phone= (EditText) window.findViewById(R.id.ed_phone);
                final EditText ed_address= (EditText) window.findViewById(R.id.ed_address);
                final EditText ed_remark= (EditText) window.findViewById(R.id.ed_remark);
                final EditText ed_account= (EditText) window.findViewById(R.id.ed_account);
                ed_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            String py= PinyinHelper.getShortPinyin(ed_name.getText().toString()).toUpperCase();
                            ed_py.setText(py);
                        } catch (PinyinException e) {
                            e.printStackTrace();
                        }
                    }
                });

                but_bncode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.post(SysUtils.getGoodsServiceUrl("get_code"))
                                .tag(this)
                                .cacheKey("cacheKey")
                                .connTimeOut(1000)
                                .cacheMode(CacheMode.DEFAULT)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        Log.d("print", "生成成功" + s);
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                            JSONObject jo2 = jo1.getJSONObject("data");
                                            String code = jo2.getString("code");
                                            ed_bncode.setText(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                });
                but_new_provide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ed_name.getText().toString().isEmpty()&&!ed_bncode.getText().toString().isEmpty()){
                            List<Map<String,String>> listmap=new ArrayList<Map<String, String>>();
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("provider_name",ed_name.getText().toString());
                            map.put("provider_bncode",ed_bncode.getText().toString());
                            map.put("pym",ed_py.getText().toString());
                            map.put("contact",ed_linkman.getText().toString());
                            map.put("phone",ed_phone.getText().toString());
                            map.put("address",ed_address.getText().toString());
                            map.put("account",ed_account.getText().toString());
                            map.put("remark",ed_remark.getText().toString());
                            listmap.add(map);
                            Gson gson=new Gson();
                            String str=gson.toJson(listmap);
                            Log.d("print","添加"+str);
                            OkGo.post(SysUtils.getGoodsServiceUrl("add_provider"))
                                    .tag(this)
                                    .params("map",str)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.e("print","供应商"+s);
                                            JSONObject jsonobject= null;
                                            try {
                                                jsonobject = new JSONObject(s);
                                                JSONObject jo1=jsonobject.getJSONObject("response");
                                                String status=jo1.getString("status");
                                                String data=jo1.getString("data");
                                                if (status.equals("200")){
                                                    Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            LoadAdats();
                                            dialog.dismiss();
                                        }
                                    });

                        }
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });

                break;


        }
    }

//    @Override
//    public void onImeclick(int i) {
//        DeleteUnit(adats.get(i).getTag_id());
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public void OnitmeEdit(final int i) {
//        if (isedit) {
//            String str = adats.get(i).getName();
//            final AlertDialog dialog = new AlertDialog.Builder(this).create();
//            dialog.setView(new EditText(this));
//            dialog.show();
//            Window window = dialog.getWindow();
//            window.setContentView(R.layout.dialog_editunit);
//            final EditText ed_edit = (EditText) window.findViewById(R.id.ed_edit);
//            ed_edit.setText(str);
////        确定按钮的点击事件
//            Button but_editqueding = (Button) window.findViewById(R.id.but_editqueding);
//            but_editqueding.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!ed_edit.getText().toString().isEmpty()) {
//                        EditUnit(ed_edit.getText().toString(),adats.get(i).getTag_id());
////                        adats.get(i).setName(ed_edit.getText().toString());
////                        adapter.notifyDataSetChanged();
//                        dialog.cancel();
//                    } else {
//                        Toast.makeText(Label_activity.this, "标签不能为空", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            Button but_edquxiao = (Button) window.findViewById(R.id.but_edquxiao);
//            but_edquxiao.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.cancel();
//                }
//            });
//        }else if (getIntent().getStringExtra("com.yzx.value")!=null){
//
//            if (isckbox.get(i)){
//                isckbox.set(i,false);
//            }else {
//                isckbox.set(i,true);
//            }
//            adapter.setcheck(isckbox);
//            adapter.notifyDataSetChanged();
//            but_confirm.setVisibility(View.VISIBLE);
//            but_confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String str_name="";
//                    String str_id="";
//
//                    for (int i=0;i<adats.size();i++){
//                        if (!isckbox.get(i)){
//                            Map<String, String> map1 = new HashMap<>();
//                            str_name+=adats.get(i).getName()+" ";
//                            str_id+=adats.get(i).getTag_id();
//                            map1.put("label_id",adats.get(i).getTag_id()+"");
//                            mapList.add(map1);
//                        }
//                    }
//                    Gson gson = new Gson();
//                    String str = gson.toJson(mapList);
//
//                    Intent intent=new Intent();
//                    intent.putExtra("lable_name",str_name);
//                    intent.putExtra("lable_id",str);
//                    Label_activity.this.setResult(206,intent);
//                    finish();
//                }
//            });
//
//        }
//    }
    //    隐藏底部按钮
//    public void toggleHideyBar() {
////        // BEGIN_INCLUDE (get_current_ui_flags) 
////        //  The UI options currently enabled are represented by a bitfield.
////        //  getSystemUiVisibility() gives us that bitfield.  
////        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
////        int newUiOptions = uiOptions;
////        // END_INCLUDE (get_current_ui_flags)
////        //  BEGIN_INCLUDE (toggle_ui_flags)  
////        boolean isImmersiveModeEnabled =
////                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
////        if (isImmersiveModeEnabled) {
////            Log.i("123", "Turning immersive mode mode off. ");
////        } else {
////            Log.i("123", "Turning immersive mode mode on.");
////        }
////        // Navigation bar hiding:  Backwards compatible to ICS.  
////        if (Build.VERSION.SDK_INT >= 14) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
////        }
////        // Status bar hiding: Backwards compatible to Jellybean  
////        if (Build.VERSION.SDK_INT >= 16) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
////        }
////        // Immersive mode: Backward compatible to KitKat.  
////        // Note that this flag doesn't do anything by itself, it only augments the behavior  
////        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample  
////        // all three flags are being toggled together.  
////        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".  
////        // Sticky immersive mode differs in that it makes the navigation and status bars  
////        // semi-transparent, and the UI flag does not get cleared when the user interacts with  
////        // the screen.  
////        if (Build.VERSION.SDK_INT >= 18) {
////            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
////        }
//////         getWindow().getDecorView().setSystemUiVisibility(newUiOptions);//上边状态栏和底部状态栏滑动都可以调出状态栏  
////        getWindow().getDecorView().setSystemUiVisibility(4108);//这里的4108可防止从底部滑动调出底部导航栏  
////        //END_INCLUDE (set_ui_flags)  
//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Provider_activity.this);
//    }

    public void NewUnit(String str){
        OkGo.post(SysUtils.getGoodsServiceUrl("add_provider"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("provider_name",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","NewUnit"+s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                String message=jo1.getString("message");
                                Toast.makeText(Provider_activity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
//                            Loaddata();
                        }
                    }
                });
    }


    public void DeleteUnit(int unit_id){
        OkGo.post(SysUtils.getGoodsServiceUrl("label_remove"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("label_id",unit_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","DeleteUnit"+s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_title.setText("供应商");
                            isedit=false;
                            isVisibility=false;
//                            Loaddata();
                        }
                    }
                });
    }
    public void EditUnit(String unit,int unit_id){
        OkGo.post(SysUtils.getGoodsServiceUrl("label_update"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("label_id",unit_id)
                .params("label_name",unit)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","EditUnit"+s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Provider_activity.this,data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_title.setText("供应商");
                            isedit=false;
                            isVisibility=false;
//                            Loaddata();
                        }
                    }
                });
    }

    @Override
    public void onImeclick(int i) {

    }

    @Override
    public void OnitmeEdit(int i) {
        Intent intent=new Intent();
        intent.putExtra("lable_name",adats.get(i).getName());
        intent.putExtra("lable_id",adats.get(i).getTag_id()+"");
        Provider_activity.this.setResult(208,intent);
        finish();
    }
}
