package retail.yzx.com.kz;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entty.Seek_entty;
import Utils.StringUtils;
import Utils.SysUtils;
import adapters.Seek_adapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/3/30.
 */
public class SeekActivity extends BaseActivity implements View.OnClickListener {
    public ImageView im_huanghui;
    public ListView lv_seek;
    public Seek_adapter seek_adapter;
    public EditText ed_seek;
    public Button but_seek;
    public List<Seek_entty> adats;


    @Override
    protected int getContentId() {
        return R.layout.seek_layout;
    }

    @Override
    protected void init() {
        super.init();
        adats=new ArrayList<>();

        seek_adapter=new Seek_adapter(this);
        lv_seek= (ListView) findViewById(R.id.lv_seek);
        ed_seek= (EditText) findViewById(R.id.ed_seek);
        but_seek= (Button) findViewById(R.id.but_seek);
        but_seek.setOnClickListener(this);
        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.seek_layout);
//
//        //初始化
//        init1();
//
//
//        //    隐藏底部按钮
//        toggleHideyBar();
//    }
//    private void init1() {
//        adats=new ArrayList<>();
//
//        seek_adapter=new Seek_adapter(this);
//        lv_seek= (ListView) findViewById(R.id.lv_seek);
//        ed_seek= (EditText) findViewById(R.id.ed_seek);
//        but_seek= (Button) findViewById(R.id.but_seek);
//        but_seek.setOnClickListener(this);
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//    }



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
            int[] leftTop = { 0, 0 };
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
//
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
//
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(SeekActivity.this);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            搜索的确定按钮
            case R.id.but_seek:
                if (ed_seek.getText().toString().equals("")){
                    Toast.makeText(SeekActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    getseek(ed_seek.getText().toString());
                }
                break;
        }
    }

    private void getseek(String str) {
        String name="search";
        if (StringUtils.isNumber1(str)){
            name="bncode";
        }else {
            name="search";
        }
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params(name,str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","搜索的结果"+s);
                        adats.clear();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            String message=jsonObject1.getString("message");
                            if (status.equals("200")){
                                JSONObject jo1=jsonObject1.getJSONObject("data");
                                JSONArray ja1=jo1.getJSONArray("goods_info");
                                for (int i=0;i<ja1.length();i++){
                                    Seek_entty seek_entty=new Seek_entty();
                                    JSONObject jo2=ja1.getJSONObject(i);
                                    String btn_switch_type=jo2.getString("btn_switch_type");
                                    seek_entty.setBtn_switch_type(btn_switch_type);
                                    String name=jo2.getString("name");
                                    seek_entty.setName(name);
                                    String price=jo2.getString("price");
                                    seek_entty.setPrice(price);
                                    String goods_id=jo2.getString("goods_id");
                                    seek_entty.setGoods_id(goods_id);
                                    String store=jo2.getString("store");
                                    seek_entty.setStore(store);
                                    String buy_count=jo2.getString("buy_count");
                                    seek_entty.setBuy_count(buy_count);
                                    String marketable=jo2.getString("marketable");
                                    seek_entty.setMarketable(marketable);
                                    String cost=jo2.getString("cost");
                                    seek_entty.setCost(cost);
                                    String cat_name=jo2.getString("cat_name");
                                    seek_entty.setCat_name(cat_name);
                                    String py=jo2.getString("py");
                                    seek_entty.setPy(py);
                                    String GD=jo2.getString("GD");
                                    seek_entty.setGD(GD);
                                    String img_src=jo2.getString("img_src");
                                    seek_entty.setImg_src(img_src);
                                    String bncode=jo2.getString("bncode");
                                    seek_entty.setBncode(bncode);
                                    adats.add(seek_entty);
                                }
                            }else {
                                Toast.makeText(SeekActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                            seek_adapter.setAdats(adats);
                            lv_seek.setAdapter(seek_adapter);
                            seek_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

}
