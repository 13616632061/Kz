package retail.yzx.com.kz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Commodity;
import Fragments.Breakage_fragment;
import Fragments.Commodity_fragment;
import Fragments.Fragment_check;
import Fragments.Fragment_forthe;
import Fragments.Fragment_member;
import Fragments.Fragment_money;
import Fragments.Staff_fragment;
import Fragments.Statement_Fragment;
import Utils.SharedUtil;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import shujudb.SqliteHelper;
import widget.ShapeLoadingDialog;

/**
 * Created by admin on 2017/3/9.
 * 更多的页面
 */
public class Commoditymanagement_Activity extends BaseActivity implements View.OnClickListener {

    public RadioButton but_commodity,rl_forthe,rb_statement,rl_staff,rl_money,rb_settings,Rl_regard,rb_check,rb_breakage,rl_member,rb_message;
    private Button btn_refresh,btn_loginout;
    //商品的实体类
    public List<Commodity> commodities;
    public List<Commodity> Datas;
    public widget.ShapeLoadingDialog loadingdialog;
    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;
    private int logntype;


    @Override
    protected int getContentId() {
        return R.layout.commoditymanagement;
    }

    @Override
    protected void init() {
        super.init();
        but_commodity= (RadioButton) findViewById(R.id.but_commodity);
        rl_money= (RadioButton) findViewById(R.id.rl_money);
//        but_commodity.performClick();
        rb_statement= (RadioButton) findViewById(R.id.rb_statement);
        rb_settings= (RadioButton) findViewById(R.id.rb_settings);
        Rl_regard= (RadioButton) findViewById(R.id.Rl_regard);
        rb_check= (RadioButton) findViewById(R.id.rb_check);
        rb_breakage= (RadioButton) findViewById(R.id.rb_breakage);
        rl_staff= (RadioButton) findViewById(R.id.rl_staff);
        rl_member= (RadioButton) findViewById(R.id.rl_member);
        rl_forthe= (RadioButton) findViewById(R.id.rl_forthe);
        rb_message= (RadioButton) findViewById(R.id.rb_message);
        btn_refresh= (Button) findViewById(R.id.btn_refresh);
        btn_loginout= (Button) findViewById(R.id.btn_loginout);
        rb_statement.performClick();
        btn_refresh.setOnClickListener(this);
        btn_loginout.setOnClickListener(this);

        //数据库的操作
        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        Intent intent=getIntent();
        if(intent!=null){
            logntype=intent.getIntExtra("logntype",0);
            if(logntype==1||logntype==2){
                btn_refresh.setVisibility(View.VISIBLE);
                btn_loginout.setVisibility(View.VISIBLE);

            }else {
                btn_refresh.setVisibility(View.GONE);
                btn_loginout.setVisibility(View.GONE);
            }
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 要在setContentView之前设置，否则会异常。
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		/* 全屏显示 */
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // 指定使用竖屏模式， 在xml配置 失效了（可能是因为屏幕适配重新加载计算的原因），只能代码指定了.
////        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////		tipWindow = new TipWindow( this);
//        setContentView(R.layout.commoditymanagement);
//
//        //初始化
////        init1();
//
//
//    }



//    public interface MyTouchListener
//    {
//        public void onTouchEvent(KeyEvent event);
//    }
    /*
    * 保存MyTouchListener接口的列表
    */
//    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<Commoditymanagement_Activity.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
//    public void registerMyTouchListener(MyTouchListener listener)
//    {
//        myTouchListeners.add(listener);
//    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
//    public void unRegisterMyTouchListener(MyTouchListener listener)
//    {
//        myTouchListeners.remove(listener);
//    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent ev) {
//        // TODO Auto-generated method stub
//        for (MyTouchListener listener : myTouchListeners) {
//            listener.onTouchEvent(ev);
//        }
//        return true;
//    }


//    private void init1() {
//        //    隐藏底部按钮
////        StringUtils.HideBottomBar(Commoditymanagement_Activity.this);
//
//
//        but_commodity= (RadioButton) findViewById(R.id.but_commodity);
//        rl_money= (RadioButton) findViewById(R.id.rl_money);
////        but_commodity.performClick();
//        rb_statement= (RadioButton) findViewById(R.id.rb_statement);
//        rb_settings= (RadioButton) findViewById(R.id.rb_settings);
//        Rl_regard= (RadioButton) findViewById(R.id.Rl_regard);
//        rb_check= (RadioButton) findViewById(R.id.rb_check);
//        rb_breakage= (RadioButton) findViewById(R.id.rb_breakage);
//        rl_staff= (RadioButton) findViewById(R.id.rl_staff);
//        rl_member= (RadioButton) findViewById(R.id.rl_member);
//        rl_forthe= (RadioButton) findViewById(R.id.rl_forthe);
//        btn_refresh= (Button) findViewById(R.id.btn_refresh);
//        btn_loginout= (Button) findViewById(R.id.btn_loginout);
//        rb_statement.performClick();
//        btn_refresh.setOnClickListener(this);
//        btn_loginout.setOnClickListener(this);
//
//        //数据库的操作
//        sqliteHelper = new SqliteHelper(this);
//        sqLiteDatabase = sqliteHelper.getReadableDatabase();
//
//        Intent intent=getIntent();
//        if(intent!=null){
//            logntype=intent.getIntExtra("logntype",0);
//            if(logntype==1||logntype==2){
//                btn_refresh.setVisibility(View.VISIBLE);
//                btn_loginout.setVisibility(View.VISIBLE);
//
//            }else {
//                btn_refresh.setVisibility(View.GONE);
//                btn_loginout.setVisibility(View.GONE);
//            }
//        }
//
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                final Dialog dialog = new Dialog(Commoditymanagement_Activity.this);
                dialog.setTitle("同步数据等待时间较长是否继续？");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.refresh_dialog);
                Button but_goto = (Button) window.findViewById(R.id.but_goto);
                Button but_abolish = (Button) window.findViewById(R.id.but_abolish);
                but_abolish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                but_goto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        getAdats();
                    }
                });
                break;
            case R.id.btn_loginout:
                Intent intent_restanraunt=new Intent(Commoditymanagement_Activity.this, Handover_activity.class);
                intent_restanraunt.putExtra("logntype",logntype);
                startActivity(intent_restanraunt);
                overridePendingTransition(R.anim.main_in, R.anim.main_out);
                break;
        }
    }
    public void onclick(View v){
        switch (v.getId()){
            case R.id.but_Cashier:
                //跳转到收银界面
//                startActivity(new Intent(Commoditymanagement_Activity.this,MainActivity.class));
                finish();
                break;
            case R.id.but_commodity:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl,new Commodity_fragment())
                        .addToBackStack(null).commit();
                break;
            case R.id.rb_settings:
//                if (!SharedUtil.getString("type").equals("4")) {
                    startActivity(new Intent(Commoditymanagement_Activity.this,Setting_Activity.class));
//                } else {
//                    Toast.makeText(Commoditymanagement_Activity.this, "你还没有这个权限", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.Rl_regard:
                startActivity(new Intent(Commoditymanagement_Activity.this,Reard_activity.class));
                break;
            case R.id.rb_statement:
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl,new Statement_Fragment()).commit();
                break;
            case R.id.rl_staff:
                if (!SharedUtil.getString("type").equals("4")) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fl,new Staff_fragment()).commit();
                } else {
                    Toast.makeText(Commoditymanagement_Activity.this, "你还没有这个权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_breakage:
                if (SharedUtil.getString("type").equals("4")){
                  Toast.makeText(Commoditymanagement_Activity.this,"您没有该权限",Toast.LENGTH_SHORT).show();
                }else {
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.fl, new Breakage_fragment()).commit();
                }
                break;
            case R.id.rb_check:
                if (SharedUtil.getString("type").equals("4")){
                    Toast.makeText(Commoditymanagement_Activity.this,"您没有该权限",Toast.LENGTH_SHORT).show();
                }else {
                    getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .replace(R.id.fl, new Fragment_check()).commit();
                }
                break;
            case R.id.rl_money:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.fl,new Fragment_money()).commit();
                break;
            case R.id.rl_member:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fl,new Fragment_member()).commit();
                break;
            case R.id.rl_forthe:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fl,new Fragment_forthe()).commit();
                break;
            case R.id.rb_message:
                startActivity(new Intent(Commoditymanagement_Activity.this,MessageActivity.class));
                break;
        }
    }

    //点击编辑框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        StringUtils.HideBottomBar(Commoditymanagement_Activity.this);
    }

    //    加载数据
    private void getAdats() {
        commodities = new ArrayList<>();
        Datas = new ArrayList<>();
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(Commoditymanagement_Activity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText("正在初始化数据...");
        loadingdialog.show();
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_pb"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("type", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "String" + s);
                        try {
                            JSONObject jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray jsonArray = j2.getJSONArray("goods_info");
                            JSONArray ja2 = j2.getJSONArray("sum");
                            JSONObject jo4 = ja2.getJSONObject(0);
                            commodities.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Commodity commodity = new Commodity();
                                JSONObject jo1 = jsonArray.getJSONObject(j);
                                commodity.setGoods_id(jo1.getString("goods_id"));
                                commodity.setName(jo1.getString("name"));
                                commodity.setPy(jo1.getString("py"));
                                commodity.setPrice(jo1.getString("price"));
                                commodity.setCost(jo1.getString("cost"));
                                commodity.setBncode(jo1.getString("bncode"));
                                commodity.setBn(jo1.getString("bn"));
                                commodity.setTag_id(jo1.getString("tag_id"));
                                commodity.setTag_name(jo1.getString("tag_name"));
                                commodity.setUnit(jo1.getString("unit"));
                                commodity.setUnit_id(jo1.getInt("unit_id"));
                                commodity.setStore(jo1.getString("store"));
                                commodity.setGood_limit(jo1.getString("good_limit"));
                                commodity.setGood_stock(jo1.getString("good_stock"));
                                commodity.setPD(jo1.getString("PD"));
                                commodity.setGD(jo1.getString("GD"));
                                commodity.setMarketable(jo1.getString("marketable"));
                                commodity.setAltc(jo1.getString("ALTC"));
                                JSONArray ja3 = jo1.getJSONArray("label");
                                List<Commodity.Labels> List_labels = new ArrayList<Commodity.Labels>();
                                for (int k = 0; k < ja3.length(); k++) {
                                    Commodity.Labels labels = new Commodity.Labels();
                                    JSONObject jo3 = ja3.getJSONObject(k);
                                    String label_id = jo3.getString("label_id");
                                    String label_name = jo3.getString("label_name");
                                    labels.setLabel_id(label_id);
                                    labels.setLabel_name(label_name);
                                    List_labels.add(labels);
                                }
                                commodity.setAdats(List_labels);
                                Datas.add(commodity);
                            }
                            Log.d("print", "商品是" + Datas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
//                            if (SysUtils.isWifiConnected(Commoditymanagement_Activity.this)) {
                            if (isnetworknew) {
//                            if (SysUtils.isNetworkAvailable(Commoditymanagement_Activity.this)) {
//                            if (SysUtils.isNetworkOnline()) {
                                sqLiteDatabase.execSQL(("delete from  commodity"));
                            }
                            for (int i = 0; i < Datas.size(); i++) {
                                sqLiteDatabase.execSQL("insert into commodity (goods_id,name," +
                                        "py,price" +
                                        ",cost,bncode," +
                                        "tag_id,unit," +
                                        "store,good_limit," +
                                        "good_stock,PD,GD," +
                                        "marketable,tag_name,ALTC,product_id,bn)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                                        Datas.get(i).getGoods_id(), Datas.get(i).getName()
                                        , Datas.get(i).getPy(), Datas.get(i).getPrice(),
                                        Datas.get(i).getCost(), Datas.get(i).getBncode().replaceAll(" ","")
                                        , Datas.get(i).getTag_id(), Datas.get(i).getUnit(),
                                        Datas.get(i).getStore(), Datas.get(i).getGood_limit()
                                        , Datas.get(i).getGood_stock(), Datas.get(i).getPD(),
                                        Datas.get(i).getGD(), Datas.get(i).getMarketable(), Datas.get(i).getTag_name()
                                        , Datas.get(i).getAltc(), Datas.get(i).getProduct_id(), Datas.get(i).getBn()});
                            }
                            loadingdialog.dismiss();
                        }
                        Datas.clear();
                    }
                });
    }

}
