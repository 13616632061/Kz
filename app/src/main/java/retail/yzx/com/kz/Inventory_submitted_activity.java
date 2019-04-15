package retail.yzx.com.kz;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Utils.SysUtils;
import adapters.Adapter_inventory;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/8/12.
 * 盘点提交
 */
public class Inventory_submitted_activity extends BaseActivity implements View.OnClickListener {

    public ListView lv_submit;
    public Adapter_inventory adapter;
    public ArrayList<Commodity> commodities;
    public ArrayList<String> liststate;

    public ArrayList<Commodity> adats;
    public ArrayList<String> listString;

    public List<Map<String,String>> listmap;

    public int normal=0,abnormality=0,inventory=0;
    public TextView tv_normal,tv_abnormality,tv_inventory;
    public ImageView tv_im1,tv_im2,tv_im3,im_huanghui;

    //提交
    public Button but_submit;



    @Override
    protected int getContentId() {
        return R.layout.inventory_activity;
    }

    @Override
    protected void init() {
        super.init();
        listmap=new ArrayList<>();

        adats=new ArrayList<>();
        listString=new ArrayList<>();

        tv_normal= (TextView) findViewById(R.id.tv_normal);
        tv_abnormality= (TextView) findViewById(R.id.tv_abnormality);
        tv_inventory= (TextView) findViewById(R.id.tv_inventory);
        tv_normal.setOnClickListener(this);
        tv_abnormality.setOnClickListener(this);
        tv_inventory.setOnClickListener(this);
        tv_im1= (ImageView) findViewById(R.id.tv_im1);
        tv_im2= (ImageView) findViewById(R.id.tv_im2);
        tv_im3= (ImageView) findViewById(R.id.tv_im3);

        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);

        but_submit= (Button) findViewById(R.id.but_submit);
        but_submit.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getCharSequenceArrayListExtra("commodities")!=null&&
                intent.getStringArrayListExtra("liststate")!=null){
            commodities= (ArrayList<Commodity>) intent.getSerializableExtra("commodities");
            liststate=intent.getStringArrayListExtra("liststate");
        }

        for (int i=0;i<commodities.size();i++){
            if (!liststate.get(i).equals("0")) {
                if (Integer.parseInt(commodities.get(i).getStore())==Integer.parseInt(liststate.get(i))){
                    normal++;
                }else {
                    abnormality++;
                }
            }else {
                inventory++;
            }
        }
        lv_submit= (ListView) findViewById(R.id.lv_submit);
        adapter=new Adapter_inventory(this);
        adapter.setAdats(commodities);
        adapter.setStringList(liststate);
        lv_submit.setAdapter(adapter);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        tv_normal.performClick();
        tv_normal.setText("库存正常("+normal+")");
        tv_abnormality.setText("库存异常("+abnormality+")");
        tv_inventory.setText("未盘点("+inventory+")");
    }


    //    @Override
//    protected void onStart() {
//        super.onStart();
//        StringUtils.HideBottomBar(Inventory_submitted_activity.this);
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        StringUtils.HideBottomBar(Inventory_submitted_activity.this);
//        setContentView(R.layout.inventory_activity);
//        init1();
//        LoadData();
//    }



    //  初始化
//    private void init1() {
//
//
//        listmap=new ArrayList<>();
//
//        adats=new ArrayList<>();
//        listString=new ArrayList<>();
//
//        tv_normal= (TextView) findViewById(R.id.tv_normal);
//        tv_abnormality= (TextView) findViewById(R.id.tv_abnormality);
//        tv_inventory= (TextView) findViewById(R.id.tv_inventory);
//        tv_normal.setOnClickListener(this);
//        tv_abnormality.setOnClickListener(this);
//        tv_inventory.setOnClickListener(this);
//        tv_im1= (ImageView) findViewById(R.id.tv_im1);
//        tv_im2= (ImageView) findViewById(R.id.tv_im2);
//        tv_im3= (ImageView) findViewById(R.id.tv_im3);
//
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//
//        but_submit= (Button) findViewById(R.id.but_submit);
//        but_submit.setOnClickListener(this);
//
//        Intent intent = getIntent();
//        if (intent.getCharSequenceArrayListExtra("commodities")!=null&&
//                intent.getStringArrayListExtra("liststate")!=null){
//            commodities= (ArrayList<Commodity>) intent.getSerializableExtra("commodities");
//            liststate=intent.getStringArrayListExtra("liststate");
//        }
//
//        for (int i=0;i<commodities.size();i++){
//            if (!liststate.get(i).equals("0")) {
//                if (Integer.parseInt(commodities.get(i).getStore())==Integer.parseInt(liststate.get(i))){
//                    normal++;
//                }else {
//                    abnormality++;
//                }
//            }else {
//                inventory++;
//            }
//        }
//        lv_submit= (ListView) findViewById(R.id.lv_submit);
//        adapter=new Adapter_inventory(this);
//        adapter.setAdats(commodities);
//        adapter.setStringList(liststate);
//        lv_submit.setAdapter(adapter);
//    }



//    private void LoadData() {
//        tv_normal.performClick();
//        tv_normal.setText("库存正常("+normal+")");
//        tv_abnormality.setText("库存异常("+abnormality+")");
//        tv_inventory.setText("未盘点("+inventory+")");
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_normal:
                tv_im1.setVisibility(View.VISIBLE);
                tv_im2.setVisibility(View.INVISIBLE);
                tv_im3.setVisibility(View.INVISIBLE);
                adats.clear();
                listString.clear();
                for (int i=0;i<commodities.size();i++){
                    if (!liststate.get(i).equals("0")) {
                        if (Integer.parseInt(commodities.get(i).getStore())==Integer.parseInt(liststate.get(i))){
                                adats.add(commodities.get(i));
                                listString.add(liststate.get(i));
                        }
                    }
                }
                adapter.setAdats(adats);
                adapter.setStringList(listString);
                lv_submit.setAdapter(adapter);

                break;
            case R.id.tv_abnormality:
                tv_im1.setVisibility(View.INVISIBLE);
                tv_im2.setVisibility(View.VISIBLE);
                tv_im3.setVisibility(View.INVISIBLE);
                adats.clear();
                listString.clear();
                for (int i=0;i<commodities.size();i++){
                    if (!liststate.get(i).equals("0")) {
                        if (Integer.parseInt(commodities.get(i).getStore())!=Integer.parseInt(liststate.get(i))){
                            adats.add(commodities.get(i));
                            listString.add(liststate.get(i));
                        }
                    }
                }

                adapter.setAdats(adats);
                adapter.setStringList(listString);
                lv_submit.setAdapter(adapter);
                break;
            case R.id.tv_inventory:
                tv_im1.setVisibility(View.INVISIBLE);
                tv_im2.setVisibility(View.INVISIBLE);
                tv_im3.setVisibility(View.VISIBLE);
                adats.clear();
                listString.clear();
                for (int i=0;i<commodities.size();i++){
                    if (liststate.get(i).equals("0")) {
                            adats.add(commodities.get(i));
                            listString.add(liststate.get(i));
                    }
                }
                adapter.setAdats(adats);
                adapter.setStringList(listString);
                lv_submit.setAdapter(adapter);
                break;
            case R.id.im_huanghui:
                finish();
                break;
            //提交数据
            case R.id.but_submit:
                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this).create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_submit);
                TextView tv_total= (TextView) window.findViewById(R.id.tv_total);
                TextView tv_inventory_nums= (TextView) window.findViewById(R.id.tv_inventory_nums);
                TextView tv_correct= (TextView) window.findViewById(R.id.tv_correct);
                TextView tv_alter= (TextView) window.findViewById(R.id.tv_alter);
                TextView tv_Noinventory= (TextView) window.findViewById(R.id.tv_Noinventory);
                tv_total.setText(commodities.size()+"");
                tv_inventory_nums.setText((abnormality+normal)+"");
                tv_correct.setText(normal+"");
                tv_alter.setText((abnormality+normal)+"");
                tv_Noinventory.setText(inventory+"");
                Button but_cancel= (Button) window.findViewById(R.id.but_cancel);
                Button but_submit= (Button) window.findViewById(R.id.but_submit);
                but_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                but_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listmap.clear();
                        for (int i=0;i<commodities.size();i++){
                            if (!liststate.get(i).equals("0")){
                                Map<String,String> map=new HashMap<String, String>();
                                map.put("goods_id",commodities.get(i).getGoods_id());
                                map.put("store",liststate.get(i));
                                listmap.add(map);
                            }
                        }
                        Gson gson=new Gson();
                        String str=gson.toJson(listmap);
                        upinventory(str);
                        dialog.dismiss();
                    }
                });
                break;
        }
    }


    public void upinventory(String map){
        OkGo.post(SysUtils.getGoodsServiceUrl("change_store"))
                .tag(this)
                .params("map",map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","上传的数据为"+s);
                        try {
                            JSONObject jsonobject=new JSONObject(s);
                            JSONObject jo=jsonobject.getJSONObject("response");
                            String status=jo.getString("status");
                            String data=jo.getString("data");
                            if (status.equals("200")){
                                Toast.makeText(Inventory_submitted_activity.this, data, Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(Inventory_submitted_activity.this, data, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}
