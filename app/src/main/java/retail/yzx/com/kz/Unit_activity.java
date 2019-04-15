package retail.yzx.com.kz;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Fenlei_Entty;
import Utils.SysUtils;
import adapters.Unit_adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import widget.MyGirdView;

/**
 * Created by admin on 2017/4/7.
 */
public class Unit_activity extends BaseActivity implements View.OnClickListener, Unit_adapter.DeliteOnclick {
    public ImageView im_huanghui;
    public MyGirdView gv_unit;
    public Unit_adapter adapter;
    public List<Fenlei_Entty> adats;
    public Button but_newunit;
    public Button im_delete,im_redact;
    public TextView tv_title;

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
        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);
        gv_unit= (MyGirdView) findViewById(R.id.gv_unit);
        adats=new ArrayList<>();

//        删除按钮
        im_delete= (Button) findViewById(R.id.im_delete);
        im_delete.setOnClickListener(this);
//        新增单位
        but_newunit= (Button) findViewById(R.id.but_newunit);
        but_newunit.setOnClickListener(this);

//        编辑单位
        im_redact= (Button) findViewById(R.id.im_redact);
        im_redact.setOnClickListener(this);

        tv_title= (TextView) findViewById(R.id.tv_title);
        adapter=new Unit_adapter(this);
        adapter.setDeliteOnclick(this);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        Loaddata();
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unit);
//        init1();
//
//        Loaddata();
//    }

    private void Loaddata() {
        OkGo.post(SysUtils.getGoodsServiceUrl("unit_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print",s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            JSONObject jo2=jo1.getJSONObject("data");
                            JSONArray ja=jo2.getJSONArray("units_info");
                            adats.clear();
                            for (int i=0;i<ja.length();i++){
                                Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                JSONObject jo3=ja.getJSONObject(i);
                                fenlei_entty.setName(jo3.getString("unit"));
                                fenlei_entty.setTag_id(jo3.getInt("unit_id"));
                                adats.add(fenlei_entty);
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

//    private void init1() {
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//        gv_unit= (MyGirdView) findViewById(R.id.gv_unit);
//        adats=new ArrayList<>();
//
////        删除按钮
//        im_delete= (Button) findViewById(R.id.im_delete);
//        im_delete.setOnClickListener(this);
////        新增单位
//        but_newunit= (Button) findViewById(R.id.but_newunit);
//        but_newunit.setOnClickListener(this);
//
////        编辑单位
//        im_redact= (Button) findViewById(R.id.im_redact);
//        im_redact.setOnClickListener(this);
//
//        tv_title= (TextView) findViewById(R.id.tv_title);
//        adapter=new Unit_adapter(this);
//        adapter.setDeliteOnclick(this);
//
//        //隐藏底部按钮
//        StringUtils.HideBottomBar(Unit_activity.this);
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
                    tv_title.setText("编辑单位");
                    isVisibility=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("单位");
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
                    tv_title.setText("编辑单位");
                    isedit=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("单位");
                    isedit=false;
                }
                break;
//             新增单位
            case R.id.but_newunit:
                final EditText editText=new EditText(this);
                final Fenlei_Entty fenlei_entty=new Fenlei_Entty();

                final AlertDialog dialog=new AlertDialog.Builder(this).create();
                dialog.setView(new EditText(this));
                dialog.show();

                Window window=dialog.getWindow();
                window.setContentView(R.layout.dialog_newunit);
                final EditText ed_new= (EditText) window.findViewById(R.id.ed_new);
                Button but_newquxiao= (Button) window.findViewById(R.id.but_newquxiao);
                Button but_newqueding= (Button) window.findViewById(R.id.but_newqueding);
                but_newquxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                but_newqueding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!ed_new.getText().toString().isEmpty()){
                            fenlei_entty.setName(ed_new.getText().toString());
                            if(isVisibility){
                                fenlei_entty.setVisibility(true);
                            }else {
                                fenlei_entty.setVisibility(false);
                            }
                            if (isedit){
                                fenlei_entty.setIsedit(true);
                            }else {
                                fenlei_entty.setIsedit(false);
                            }
                            NewUnit(ed_new.getText().toString());

//                            adats.add(fenlei_entty);
//                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }else {
                            Toast.makeText(Unit_activity.this,"请输入分类名称",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;


        }
    }

    @Override
    public void onImeclick(int i) {
        DeleteUnit(adats.get(i).getTag_id());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnitmeEdit(final int i) {
        if (isedit) {

            String str = adats.get(i).getName();
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setView(new EditText(this));
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_editunit);
            final EditText ed_edit = (EditText) window.findViewById(R.id.ed_edit);
            ed_edit.setText(str);
//        确定按钮的点击事件
            Button but_editqueding = (Button) window.findViewById(R.id.but_editqueding);
            but_editqueding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ed_edit.getText().toString().isEmpty()) {
                        EditUnit(ed_edit.getText().toString(),adats.get(i).getTag_id());
//                        adats.get(i).setName(ed_edit.getText().toString());
//                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    } else {
                        Toast.makeText(Unit_activity.this, "单位不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button but_edquxiao = (Button) window.findViewById(R.id.but_edquxiao);
            but_edquxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }else if (getIntent().getStringExtra("com.yzx.value")!=null){
            Intent intent=new Intent();
            intent.putExtra("unit",adats.get(i).getName());
            intent.putExtra("unit_id",adats.get(i).getTag_id());
            Unit_activity.this.setResult(204,intent);
            finish();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Unit_activity.this);
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

    public void NewUnit(String str){
        OkGo.post(SysUtils.getGoodsServiceUrl("unit_add"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("unit",str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print",s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            Loaddata();
                        }
                    }
                });
    }
    public void DeleteUnit(int unit_id){
        OkGo.post(SysUtils.getGoodsServiceUrl("unit_remove"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("unit_id",unit_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print",s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_title.setText("标签");
                            isedit=false;
                            isVisibility=false;
                            Loaddata();
                        }
                    }
                });
    }
    public void EditUnit(String unit,int unit_id){
        OkGo.post(SysUtils.getGoodsServiceUrl("unit_update"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("unit_id",unit_id)
                .params("unit",unit)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print",s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            String status=jo1.getString("status");
                            String data=jo1.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Unit_activity.this,data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            tv_title.setText("标签");
                            isedit=false;
                            isVisibility=false;
                            Loaddata();
                        }
                    }
                });
    }

}
