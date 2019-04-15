package retail.yzx.com.kz;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entty.Fenlei_Entty;
import Utils.SharedUtil;
import Utils.SysUtils;
import adapters.Category_gvadapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/4/10.
 * 选择分类界面
 */
public class Category_activity extends BaseActivity implements View.OnClickListener, Category_gvadapter.DeliteOnclick {
    public GridView gv_category;
    private Category_gvadapter adapter;
    public List<Fenlei_Entty> adats;
    public ImageView im_huanghui;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.category_activity);
//        init1();
//    }

    @Override
    protected int getContentId() {
        return R.layout.category_activity;
    }


    @Override
    protected void init() {
        super.init();
        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);

        adats=new ArrayList<>();
        gv_category= (GridView) findViewById(R.id.gv_category);
        adapter=new Category_gvadapter(this);
        adapter.setDeliteOnclick(this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Category_activity.this);
//    }


    @Override
    protected void loadDatas() {
        super.loadDatas();
        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("query", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray ja = j2.getJSONArray("nav_info");
                            for (int i = 0; i < ja.length(); i++) {
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                adats.add(fenlei);
                            }
                            adapter.setAdats(adats);
                            gv_category.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        adapter.setAdats(adats);
        gv_category.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

//    private void init1() {
////        adapter.setDeliteOnclick(this);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_huanghui:
                finish();
                break;
        }
    }

    @Override
    public void onImeclick(int i) {

    }

    @Override
    public void OnitmeEdit(int i) {
        Intent intent1=new Intent();
        intent1.putExtra("tag_id",adats.get(i).getTag_id()+"");
        intent1.putExtra("name",adats.get(i).getName());
        Category_activity.this.setResult(202,intent1);
        Log.d("print","TAG"+adats.get(i).getTag_id());
        finish();
    }
}
