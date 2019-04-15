package retail.yzx.com.kz;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entty.Temporary_Entty;
import Utils.Bitmap_Utils;
import Utils.DateUtils;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.UsbPrinter;
import Utils.UsbPrinterUtil;
import adapters.Temporary_Adapter;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import widget.Switch;

import static Utils.Bitmap_Utils.addBitmap;

/**
 * Created by admin on 2019/3/7.
 */

public class Temporary_GoodsActivity extends BaseActivity implements Temporary_Adapter.OnClickListener {

    @BindView(R.id.tv_label)
    TextView tv_label;
    @BindView(R.id.sw6)
    Switch sw;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.sw_price)
    Switch sw_price;
    @BindView(R.id.tv_seek)
    EditText tv_seek;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.but_setting)
    Button but_setting;
    @BindView(R.id.but_last)
    Button but_last;
    @BindView(R.id.tv_page)
    TextView tv_page;
    @BindView(R.id.but_next)
    Button but_next;
    @BindView(R.id.im_huanghui)
    ImageView im_huanghui;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_Specifications)
    TextView tv_Specifications;
    @BindView(R.id.tv_unit)
    TextView tv_unit;
    @BindView(R.id.tv_id)
    TextView tv_id;

    @Override
    protected int getContentId() {
        return R.layout.printer_activity;
    }

    @Override
    protected void init() {
        super.init();
        tv_label.setVisibility(View.GONE);
        sw.setVisibility(View.GONE);
        tv_price.setVisibility(View.GONE);
        sw_price.setVisibility(View.GONE);
        tv_Specifications.setVisibility(View.GONE);
        tv_unit.setVisibility(View.GONE);
        tv_id.setVisibility(View.GONE);
        but_setting.setVisibility(View.GONE);
        tv_title.setText("临时商品");
        adapter.setOnClickListener(this);
        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    tv_seek.getText().toString();
                    getseek(tv_seek.getText().toString());
                }

                return true;
            }
        });
    }

    public void getseek(String bncode){
        OkGo.post(SysUtils.getSellerorderUrl("getTempGoodsByBncode"))
                .tag("temporary")
                .cacheKey("cacheKey")
                .params("bncode",bncode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印临时条码的数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                enttyList.clear();
                                JSONObject data=jsonObject1.getJSONObject("data");
                                Temporary_Entty temporary_entty=new Temporary_Entty();
                                temporary_entty.setBncode(data.getString("bncode"));
                                temporary_entty.setCreatetime(data.getString("createtime"));
                                temporary_entty.setGoods_name(data.getString("goods_name"));
                                temporary_entty.setId(data.getString("id"));
                                temporary_entty.setTotal(data.getString("total"));
                                String array1=data.getString("goods_info");
                                JSONArray array=new JSONArray(array1);
                                List<Temporary_Entty.Goods_info> goods_infoList=new ArrayList<>();
                                for (int j=0;j<array.length();j++){
                                    Temporary_Entty.Goods_info goods_info=new Temporary_Entty.Goods_info();
                                    JSONObject object1=array.getJSONObject(j);
                                    goods_info.setGoods_id(object1.getString("goods_id"));
                                    goods_info.setName(object1.getString("name"));
                                    goods_info.setNum(object1.getString("num"));
                                    goods_infoList.add(goods_info);
                                }
                                temporary_entty.setData(goods_infoList);
                                enttyList.add(temporary_entty);
                                adapter.setAdats(enttyList);
                                lv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        Loaddatas();
    }

    /**
     * 加载数据
     */
    List<Temporary_Entty> enttyList=new ArrayList<>();
    Temporary_Adapter adapter=new Temporary_Adapter(Temporary_GoodsActivity.this);
    int total=1;
    public boolean paging1 = false, paging2 = false;

    public int page=1;
    public void Loaddatas(){
        OkGo.post(SysUtils.getSellerorderUrl("tempGoodsList"))
                .tag("temporary")
                .cacheKey("cacheKey")
                .params("page", page)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","临时商品的数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")){
                                enttyList.clear();
                            JSONObject data=jsonObject1.getJSONObject("data");
                            JSONArray list=data.getJSONArray("list");
                            for (int i=0;i<list.length();i++){
                             JSONObject object=list.getJSONObject(i);
                             Temporary_Entty temporary_entty=new Temporary_Entty();
                             temporary_entty.setBncode(object.getString("bncode"));
                             temporary_entty.setCreatetime(object.getString("createtime"));
                             temporary_entty.setGoods_name(object.getString("goods_name"));
                             temporary_entty.setId(object.getString("id"));
                             temporary_entty.setTotal(object.getString("total"));
                             String array1=object.getString("goods_info");
                             JSONArray array=new JSONArray(array1);
                             List<Temporary_Entty.Goods_info> goods_infoList=new ArrayList<>();
                             for (int j=0;j<array.length();j++){
                                 Temporary_Entty.Goods_info goods_info=new Temporary_Entty.Goods_info();
                                 JSONObject object1=array.getJSONObject(j);
                                 goods_info.setGoods_id(object1.getString("goods_id"));
                                 goods_info.setName(object1.getString("name"));
                                 goods_info.setNum(object1.getString("num"));
                                 goods_infoList.add(goods_info);
                             }
                                temporary_entty.setData(goods_infoList);
                                enttyList.add(temporary_entty);
                                }
                                adapter.setAdats(enttyList);
                                lv.setAdapter(adapter);
                                total=data.getInt("all");
                                if (total % 10 == 0) {
                                    tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10));
                                } else {
                                    tv_page.setText((page ) + "/" + (Integer.valueOf(total) / 10 + 1));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.im_huanghui,R.id.but_next,R.id.but_last})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.im_huanghui:
                this.finish();
                break;
            case R.id.but_next:
                if (Integer.valueOf(total) % 10 == 0) {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10));
                    if (page < (Integer.valueOf(total) / 10)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            Loaddatas();
                            paging1 = false;
                        }
                    }
                } else {
                    tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                    if (page < (Integer.valueOf(total) / 10 + 1)) {
                        page++;
                        if (!paging1) {
                            paging1 = true;
                            Loaddatas();
                            paging1 = false;
                        }
                    }
                }
                break;
            //上一页
            case R.id.but_last:
                if (page > 1) {
                    if (Integer.valueOf(total) % 10 == 0) {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 10));
                        if (!paging2) {
                            paging2 = true;
                            Loaddatas();
                            paging2 = false;
                        }
                    } else {
                        page--;
                        tv_page.setText(page + "/" + (Integer.valueOf(total) / 10 + 1));
                        if (!paging2) {
                            paging2 = true;
                            Loaddatas();
                            paging2 = false;
                        }
                    }
                }
                break;
        }
    }

    /**
     * 编辑临时商品
     * @param i 编辑的是哪条数据
     */
    @Override
    public void setonclick(final int i) {
        final Dialog dialog = new Dialog(Temporary_GoodsActivity.this);
        dialog.setTitle("编辑");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.edit_temporary);
        final EditText ed_name= (EditText) window.findViewById(R.id.ed_name);
        final EditText ed_price= (EditText) window.findViewById(R.id.ed_price);
        Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
        Button but_submit= (Button) window.findViewById(R.id.but_submit);
        ed_name.setHint(enttyList.get(i).getGoods_name());
        ed_price.setHint(enttyList.get(i).getTotal());
        //回收临时数据
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setdeleteGoodsTempCode(enttyList.get(i).getId(),dialog,view);
            }
        });
        //确定修改
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edname="";
                String edprice="";
                if (ed_name.getText().equals("")){
                    edname=ed_name.getHint().toString();
                }else {
                    edname=ed_name.getText().toString();
                }
                if (ed_price.getText().toString().equals("")){
                    edprice=ed_price.getHint().toString();
                }else {
                    edprice=ed_price.getText().toString();
                }
                editGoodsTempCode(enttyList.get(i).getId(),edprice,edname,enttyList.get(i).getBncode(),dialog,view);
            }
        });
    }

    /**
     * 回收临时商品的数据
     * @param id 回收临时商品的id
     */
    private void setdeleteGoodsTempCode(String id, final Dialog dialog, final View view){
        OkGo.post(SysUtils.getSellerorderUrl("deleteGoodsTempCode"))
                .tag("temporary")
                .cacheKey("cacheKey")
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject object=jsonObject.getJSONObject("response");
                            String status=object.getString("status");
                            if (status.equals("200")){
                                dialog.dismiss();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                boolean isOpen = imm.isActive();
                                //isOpen若返回true，则表示输入法打开，反之则关闭。
                                if (isOpen) {
                                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 编辑临时商品的价格
     * @param id 临时商品的id
     * @param total 临时商品的价格
     */
    private void editGoodsTempCode(String id, final String total,final String goods_name,final String code, final Dialog dialog, final View view){
        OkGo.post(SysUtils.getSellerorderUrl("editGoodsTempCode"))
                .tag("temporary")
                .cacheKey("cacheKey")
                .params("id",id)
                .params("total",total)
                .params("goods_name",goods_name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject object=jsonObject.getJSONObject("response");
                            String status=object.getString("status");
                            if (status.equals("200")){
                                setPrint(goods_name,code,total);
                                dialog.dismiss();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                boolean isOpen = imm.isActive();
                                //isOpen若返回true，则表示输入法打开，反之则关闭。
                                if (isOpen) {
                                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void setPrint(String name,String code,String price){
        Log.d("print","打印随机数为"+code);
        UsbPrinterUtil mUtil=null;
        if (mUtil==null){
            mUtil = new UsbPrinterUtil(Temporary_GoodsActivity.this);
            List<UsbDevice> devs = mUtil.getUsbPrinterList();
            if (devs.size() > 0) {
                mUtil.requestPermission(devs.get(0), null);
            }
        }
        if (mUtil.getUsbPrinterList().size()>0){
            UsbDevice dev = mUtil.getUsbPrinterList().get(0);
            UsbPrinter prt = null;
            UsbPrinter.FONT font = UsbPrinter.FONT.FONT_B;
            Boolean bold = false;
            Boolean underlined = false;
            Boolean doubleHeight = false;
            Boolean doubleWidth = false;
            try {
                prt = new UsbPrinter(this, dev);
                Bitmap bitmap=null;
                Bitmap bitmap1=null;
                Bitmap bitmap2=null;
                Bitmap bitmap3=null;
                Bitmap bitmap4=null;
                Bitmap bitmap5=null;
                Bitmap bitmap6=null;
                String str1 = "SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+ SharedUtil.getString("sw_order")+"\\nCLS";
                bitmap= Bitmap_Utils.fromText(name,40);
                bitmap1= Bitmap_Utils.fromText("单价："+price,20);
                bitmap2= Bitmap_Utils.fromText("净含量："+1,20);
                bitmap3= Bitmap_Utils.fromText("日期："+ DateUtils.getCurDate1(),20);
                bitmap4= Bitmap_Utils.fromText(SharedUtil.getString("name"),20);
                bitmap5= Bitmap_Utils.fromText("金额/元",20);
                bitmap6= Bitmap_Utils.fromText(price,50);
                String str7 = "\\nDENSITY 3\\nBARCODE 20,55,\"EAN13\",60,1,0,2,4,\"" +code + "\"" + "\\nPRINT 1,1";

                prt.printString(str1, font, bold, underlined, doubleHeight,
                        doubleWidth);
                prt.write(addBitmap(20,5, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap.getWidth(), bitmap));
                prt.write(addBitmap(20,140, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap1.getWidth(), bitmap1));
                prt.write(addBitmap(20,160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                prt.write(addBitmap(20,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                prt.write(addBitmap(20,200, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                prt.write(addBitmap(160,140, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
                prt.write(addBitmap(220,160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap6.getWidth(), bitmap6));
                prt.printString(str7, font, bold, underlined, doubleHeight,
                        doubleWidth);
                Log.d("print","打印随机数为"+code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
