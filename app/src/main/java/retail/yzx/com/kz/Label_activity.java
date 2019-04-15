package retail.yzx.com.kz;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import widget.MyGirdView;

/**
 * Created by admin on 2017/4/7.
 *  标签
 */
public class Label_activity extends BaseActivity implements View.OnClickListener, Unit_adapter.DeliteOnclick {
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
        but_newunit.setText("新增标签");
        but_newunit.setOnClickListener(this);

//        编辑单位
        im_redact= (Button) findViewById(R.id.im_redact);
        im_redact.setOnClickListener(this);

        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("标签");
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
////        init1();
//
//        Loaddata();
//    }

    private void Loaddata() {
        OkGo.post(SysUtils.getGoodsServiceUrl("label_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("Tag",s);
                        try {
                            JSONObject jo=new JSONObject(s);
                            JSONObject jo1=jo.getJSONObject("response");
                            JSONObject jo2=jo1.getJSONObject("data");
                            JSONArray ja=jo2.getJSONArray("units_info");
                            adats.clear();
                            for (int i=0;i<ja.length();i++){
                                Fenlei_Entty fenlei_entty=new Fenlei_Entty();
                                JSONObject jo3=ja.getJSONObject(i);
                                fenlei_entty.setName(jo3.getString("label_name"));
                                fenlei_entty.setTag_id(jo3.getInt("label_id"));
                                adats.add(fenlei_entty);
                            }
                            for (int i=0;i<adats.size();i++){
                                isckbox.add(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

                            adapter.setAdats(adats);
                            adapter.setcheck(isckbox);
                            gv_unit.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

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
////        新增单位
//        but_newunit= (Button) findViewById(R.id.but_newunit);
//        but_newunit.setText("新增标签");
//        but_newunit.setOnClickListener(this);
//
////        编辑单位
//        im_redact= (Button) findViewById(R.id.im_redact);
//        im_redact.setOnClickListener(this);
//
//        tv_title= (TextView) findViewById(R.id.tv_title);
//        tv_title.setText("标签");
//        adapter=new Unit_adapter(this);
//        adapter.setDeliteOnclick(this);
//
//        //隐藏底部按钮
//        toggleHideyBar();
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
                    tv_title.setText("编辑标签");
                    isVisibility=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setVisibility(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("标签");
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
                    tv_title.setText("编辑标签");
                    isedit=true;
                }else {
                    for (int i=0;i<adats.size();i++){
                        adats.get(i).setIsedit(false);
                    }
                    adapter.notifyDataSetChanged();
                    tv_title.setText("标签");
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
                            Toast.makeText(Label_activity.this,"请输入标签名称",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Label_activity.this, "标签不能为空", Toast.LENGTH_SHORT).show();
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

            if (isckbox.get(i)){
                isckbox.set(i,false);
            }else {
                isckbox.set(i,true);
            }
            adapter.setcheck(isckbox);
            adapter.notifyDataSetChanged();
            but_confirm.setVisibility(View.VISIBLE);
            but_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str_name="";
                    String str_id="";

                    for (int i=0;i<adats.size();i++){
                        if (!isckbox.get(i)){
                            Map<String, String> map1 = new HashMap<>();
                            str_name+=adats.get(i).getName()+" ";
                            str_id+=adats.get(i).getTag_id();
                            map1.put("label_id",adats.get(i).getTag_id()+"");
                            mapList.add(map1);
                        }
                    }
                    Gson gson = new Gson();
                    String str = gson.toJson(mapList);

                    Intent intent=new Intent();
                    intent.putExtra("lable_name",str_name);
                    intent.putExtra("lable_id",str);
                    Label_activity.this.setResult(206,intent);
                    finish();
                }
            });

        }
    }
    //    隐藏底部按钮
    public void toggleHideyBar() {
//        // BEGIN_INCLUDE (get_current_ui_flags) 
//        //  The UI options currently enabled are represented by a bitfield.
//        //  getSystemUiVisibility() gives us that bitfield.  
//        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
//        int newUiOptions = uiOptions;
//        // END_INCLUDE (get_current_ui_flags)
//        //  BEGIN_INCLUDE (toggle_ui_flags)  
//        boolean isImmersiveModeEnabled =
//                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
//        if (isImmersiveModeEnabled) {
//            Log.i("123", "Turning immersive mode mode off. ");
//        } else {
//            Log.i("123", "Turning immersive mode mode on.");
//        }
//        // Navigation bar hiding:  Backwards compatible to ICS.  
//        if (Build.VERSION.SDK_INT >= 14) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        }
//        // Status bar hiding: Backwards compatible to Jellybean  
//        if (Build.VERSION.SDK_INT >= 16) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
//        }
//        // Immersive mode: Backward compatible to KitKat.  
//        // Note that this flag doesn't do anything by itself, it only augments the behavior  
//        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample  
//        // all three flags are being toggled together.  
//        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".  
//        // Sticky immersive mode differs in that it makes the navigation and status bars  
//        // semi-transparent, and the UI flag does not get cleared when the user interacts with  
//        // the screen.  
//        if (Build.VERSION.SDK_INT >= 18) {
//            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        }
////         getWindow().getDecorView().setSystemUiVisibility(newUiOptions);//上边状态栏和底部状态栏滑动都可以调出状态栏  
//        getWindow().getDecorView().setSystemUiVisibility(4108);//这里的4108可防止从底部滑动调出底部导航栏  
//        //END_INCLUDE (set_ui_flags)  

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        StringUtils.HideBottomBar(Label_activity.this);
    }

    public void NewUnit(String str){
        OkGo.post(SysUtils.getGoodsServiceUrl("label_add"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("label_name",str)
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
                                Toast.makeText(Label_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                String message=jo1.getString("message");
                                Toast.makeText(Label_activity.this,message,Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Label_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Label_activity.this,data,Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Label_activity.this,data,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Label_activity.this,data,Toast.LENGTH_SHORT).show();
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
