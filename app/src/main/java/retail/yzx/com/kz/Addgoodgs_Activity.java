package retail.yzx.com.kz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Custom_Entty;
import Entty.Fenlei_Entty;
import Entty.GridView_xuangzhong;
import Entty.Newitemadd_Entty;
import Utils.Bitmap_Utils;
import Utils.DateUtils;
import Utils.EscposUtil;
import Utils.Inputmethod_Utils;
import Utils.ScanGunKeyEventHelper;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import Utils.UsbPrinter;
import Utils.UsbPrinterUtil;
import adapters.Spadapter;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.supper_self_service.Utils.DateTimeUtils;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import shujudb.SqliteHelper;
import widget.ShapeLoadingDialog;
import widget.Switch;

import static Utils.Bitmap_Utils.addBitmap;

/**
 * Created by admin on 2017/4/10.
 */
public class Addgoodgs_Activity extends BaseActivity implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener, Spadapter.Oneidtextonclick {
    public TextView tv_huanghui;
    public RelativeLayout rl_unit, rl_danwei, rl_label,rl_provider;
    //    分类
    public TextView tv_shenyu;
    public EditText tv_bncode;
    public TextView tv_category, tv_lirun, tv_unit, tv_data, tv_day, tv_shenyuday, tv_lable,tv_provider;
    public EditText ed_name, ed_cost, ed_price, ed_store, ed_good_limit, ed_good_stock, ed_py, ed_baozhi, ed_describe, tv_specification,ed_Member,ed_discount,tv_produce_addr;
    public Newitemadd_Entty newitemadd_entty;
    public Switch sw1, sw2,sw4,sw_special;
    public Button but_baochun, but_delete,butPrint;
    public Button but_create;

    //扫码支付
    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    //是否上架
    public boolean isup = true;
    //是否快捷键
    public int iskuaijiejian;
    //是否煮物栏
    public String Cook;
    //单位
    public String unit_id = "";
    public String unit = "";
    //标签
    public String lable_name = "";
    public String lable_id = "";

    public String provider_id ="";
    public String provider_name="";

    //分类id
    public String tag_id = "";
    public String tag_name = "";
    public String url;
    public Commodity commodity;
    public String goods_id="";
    //提示是删除还是修改的
    public String Title = "";

    public TextView tv_biaoti;
    public List<Map<String, String>> mapList = new ArrayList<>();

    public ImageView im_data;
    public TimeSelector timeSelector;
    public Button but_supplement;

    public SqliteHelper sqliteHelper;
    public SQLiteDatabase sqLiteDatabase;


    //进价敏感操作
    public float sensitivity_price = 0;
    //售价的敏感操作
    public float sensitivity_selling = 0;
    //库存的敏感操作
    public float sensitivity_store = 0;
    //反退货
    public Button but_return;
    public Button but_devanning;

    public Map<String, String> stringMap = new HashMap<>();
    public String stringcontext = "";
    public boolean returned = false;

    public String ed_code="";
    public String disable_name="";
    public TextView tv_edit;
    public LinearLayout ll_attribute;

    public String order="0";

    String discount="1";

    String special="no";
    String PD="";
    Custom_Entty custom_entty;

    EditText ed_print1,ed_print2,ed_print3,ed_print4,ed_print5;

    String intro="";

    @Override
    protected int getContentId() {
        return R.layout.addgoods_activitty;
    }

    //点击的是分类还是单位
    @Override
    public void itmeeidtonclick(int i, String type) {
        Log.d("print","点击的数量为"+type);
        if (type.equals("1")){
            if (listFenlei.size()>0){
                tv_category.setText(listFenlei.get(i).getName());
                tag_id=listFenlei.get(i).getTag_id()+"";
                popupWindow.dismiss();
            }
        }else if (type.equals("2")){
            if (adats.size()>0){
                tv_unit.setText(adats.get(i).getName());
                unit_id=adats.get(i).getTag_id()+"";
                popupWindow.dismiss();
            }
        }
    }

    public class InputMoney implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // TODO Auto-generated method stub
            if (source.toString().equals(".") && dstart == 0 && dend == 0) {//判断小数点是否在第一位
                ed_price.setText(0+""+source+dest);//给小数点前面加0
                ed_price.setSelection(2);//设置光标
            }

            if (dest.toString().indexOf(".") != -1 && (dest.length() - dest.toString().indexOf(".")) > 2) {//判断小数点是否存在并且小数点后面是否已有两个字符

                if ((dest.length() - dstart) < 3) {//判断现在输入的字符是不是在小数点后面
                    return "";//过滤当前输入的字符
                }
            }

            return null;
        }

    }

    @Override
    protected void init() {
        super.init();
        Addgoodgs_Activity.this.setFinishOnTouchOutside(false);
        ed_discount= (EditText) findViewById(R.id.ed_discount);
        tv_produce_addr= (EditText) findViewById(R.id.tv_produce_addr);

        ed_Member= (EditText) findViewById(R.id.ed_Member);

        ed_print1= (EditText) findViewById(R.id.ed_print1);
        ed_print2= (EditText) findViewById(R.id.ed_print2);
        ed_print3= (EditText) findViewById(R.id.ed_print3);
        ed_print4= (EditText) findViewById(R.id.ed_print4);
        ed_print5= (EditText) findViewById(R.id.ed_print5);


        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        scanGunKeyEventHelper = new ScanGunKeyEventHelper(Addgoodgs_Activity.this);

        but_return = (Button) findViewById(R.id.but_return);
        but_return.setOnClickListener(this);

        but_devanning= (Button) findViewById(R.id.but_devanning);
        but_devanning.setOnClickListener(this);

        but_supplement = (Button) findViewById(R.id.but_supplement);
        but_supplement.setOnClickListener(this);

        im_data = (ImageView) findViewById(R.id.im_data);
        im_data.setOnClickListener(this);

        tv_biaoti = (TextView) findViewById(R.id.tv_biaoti);

        but_create = (Button) findViewById(R.id.but_create);
        but_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBn();
            }
        });
        tv_category = (TextView) findViewById(R.id.tv_category);
        tv_unit = (TextView) findViewById(R.id.tv_unit);

        newitemadd_entty = new Newitemadd_Entty();
        tv_huanghui = (TextView) findViewById(R.id.tv_huanghui);
        tv_huanghui.setOnClickListener(this);

        rl_provider = (RelativeLayout) findViewById(R.id.rl_provider);
        rl_provider.setOnClickListener(this);

        rl_unit = (RelativeLayout) findViewById(R.id.rl_unit);
        rl_unit.setOnClickListener(this);

        rl_danwei = (RelativeLayout) findViewById(R.id.rl_danwei);
        rl_danwei.setOnClickListener(this);

        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
        rl_label.setOnClickListener(this);

        tv_lable = (TextView) findViewById(R.id.tv_lable);
        tv_provider = (TextView) findViewById(R.id.tv_provider);


        tv_bncode = (EditText) findViewById(R.id.tv_bncode);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_py = (EditText) findViewById(R.id.ed_py);
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                ed_py.setText(Pinyin.getShortString(ed_name.getText().toString()+""));

                try {
                    String py = PinyinHelper.getShortPinyin(ed_name.getText().toString());
                    ed_py.setText(py.toUpperCase());
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
            }
        });
        ed_cost = (EditText) findViewById(R.id.ed_cost);


        tv_edit= (TextView) findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(this);

        ll_attribute= (LinearLayout) findViewById(R.id.ll_attribute);

        tv_lirun = (TextView) findViewById(R.id.tv_lirun);
        ed_price = (EditText) findViewById(R.id.ed_price);

        //设置过滤器
        ed_price.setFilters(new InputFilter[]{new InputMoney()});

        ed_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ed_price.getText().toString().isEmpty() && !ed_cost.getText().toString().isEmpty()) {
                    tv_lirun.setText(TlossUtils.sub(Double.parseDouble(ed_price.getText().toString()), Double.parseDouble(ed_cost.getText().toString())) + "");
                }
            }
        });
        ed_store = (EditText) findViewById(R.id.ed_store);
        ed_good_limit = (EditText) findViewById(R.id.ed_good_limit);
        ed_good_stock = (EditText) findViewById(R.id.ed_good_stock);


        tv_data = (TextView) findViewById(R.id.tv_data);
        //到期日期
        tv_day = (TextView) findViewById(R.id.tv_day);
        ed_baozhi = (EditText) findViewById(R.id.ed_baozhi);
        ed_baozhi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                Log.d("print","打印时间的时间"+DateUtils.getCurTimeStr());

                if (!ed_baozhi.getText().toString().equals("") && StringUtils.isNumber(ed_baozhi.getText().toString())) {
                    DateUtils.gettime(Integer.valueOf(ed_baozhi.getText().toString()));
                    tv_day.setText(DateUtils.gettime(tv_data.getText().toString(),Integer.valueOf(ed_baozhi.getText().toString())));
                    tv_shenyuday.setText(DateUtils.getDateSpan(DateUtils.getDate(),tv_day.getText().toString(),1)+"");
                }
            }
        });

        tv_shenyuday= (TextView) findViewById(R.id.tv_shenyuday);
        ed_describe = (EditText) findViewById(R.id.ed_describe);
        tv_specification = (EditText) findViewById(R.id.tv_specification);
        tv_shenyu = (TextView) findViewById(R.id.tv_shenyu);
        ed_describe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        ed_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ed_describe.getText().length() < 50) {
                    tv_shenyu.setText("剩余" + (50 - ed_describe.getText().length()) + "字");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        but_delete = (Button) findViewById(R.id.but_delete);
        but_delete.setOnClickListener(this);

        butPrint= (Button) findViewById(R.id.butPrint);
        butPrint.setOnClickListener(this);

        sw1 = (Switch) findViewById(R.id.sw1);
        sw2 = (Switch) findViewById(R.id.sw2);
        sw4 = (Switch) findViewById(R.id.sw4);
        sw_special = (Switch) findViewById(R.id.sw_special);
        sw1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    iskuaijiejian = 1;
                } else {
                    iskuaijiejian = 0;
                }
            }
        });

        sw4.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    Cook="1";
                }else {
                    Cook="0";
                }
            }
        });

        sw2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    isup = true;
                } else {
                    isup = false;
                }
            }
        });

        sw_special.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    special="yes";
                }else {
                    special="no";
                }
            }
        });

        Intent intent = getIntent();
        if (intent.getSerializableExtra("commodity") != null) {
            commodity = (Commodity) intent.getSerializableExtra("commodity");
            Log.d("print", "分类是" + commodity);
            if (commodity.getGoods_id().equals("null")||commodity.getGoods_id().equals("") || commodity.getGoods_id() == null) {
                tv_bncode.setText(commodity.getBncode());
                if (commodity.getPrice()!=null){
                    ed_price.setText(commodity.getPrice());
                }
                if (commodity.getIntro()!=null){
                    intro=commodity.getIntro();
                }
                Seekbank(commodity.getBncode());
                but_devanning.setText("开启拆箱");
                but_supplement.setVisibility(View.GONE);
                sw2.setChecked(isup);
                but_delete.setVisibility(View.GONE);
                butPrint.setVisibility(View.GONE);
                url = SysUtils.getGoodsServiceUrl("goodsToAdd");
                Title = "确定保存";
            } else {
            if (commodity.getAdats() != null && lable_id.equals("")) {
                for (int i = 0; i < commodity.getAdats().size(); i++) {
                    Map<String, String> map1 = new HashMap<>();
                    lable_id += commodity.getAdats().get(i).getLabel_name() + " ";
                    lable_id += commodity.getAdats().get(i).getLabel_id() + "";
                    map1.put("label_name", commodity.getAdats().get(i).getLabel_name());
                    map1.put("label_id", commodity.getAdats().get(i).getLabel_id());
                    mapList.add(map1);
                }
                Gson gson = new Gson();
                lable_id = gson.toJson(mapList);
            }

            if (commodity.isBox_disable()) {
                but_devanning.setText("拆箱");
            } else {
                but_devanning.setText("开启拆箱");
            }

            if (commodity.getIs_special_offer().equals("yes")) {
                sw_special.setChecked(true);
                special = "yes";
            } else {
                sw_special.setChecked(false);
                special = "no";
            }

            ed_discount.setText(commodity.getDiscount());
            tv_produce_addr.setText(commodity.getProduce_addr());

            if (commodity.getAuth().equals("open")) {
                but_create.setOnClickListener(null);
                but_supplement.setOnClickListener(this);

                //设置为不可编辑
//                tv_bncode.setFocusable(false);
//                tv_bncode.setFocusableInTouchMode(false);

                //设置可以编辑
                ed_store.setFocusableInTouchMode(true);
                ed_store.setFocusable(true);
                ed_store.requestFocus();

                //设置可以编辑
                ed_good_limit.setFocusableInTouchMode(true);
                ed_good_limit.setFocusable(true);
                ed_good_limit.requestFocus();

                //设置可以编辑
                ed_good_stock.setFocusableInTouchMode(true);
                ed_good_stock.setFocusable(true);
                ed_good_stock.requestFocus();

            } else if (commodity.getAuth().equals("close")) {

                but_create.setOnClickListener(null);
                but_supplement.setOnClickListener(null);

                //设置为不可编辑
//                tv_bncode.setFocusable(false);
//                tv_bncode.setFocusableInTouchMode(false);

                ed_store.setFocusable(false);
                ed_store.setFocusableInTouchMode(false);

                ed_good_limit.setFocusable(false);
                ed_good_limit.setFocusableInTouchMode(false);

                ed_good_stock.setFocusable(false);
                ed_good_stock.setFocusableInTouchMode(false);

            }


            tv_bncode.setText(commodity.getBncode());
            if (Integer.valueOf(commodity.getAltc()) == 0) {
                sw1.setChecked(false);
                iskuaijiejian = 0;
            } else {
                sw1.setChecked(true);
                iskuaijiejian = 1;
            }

            if (commodity.getCook_position() != null && !commodity.getCook_position().equals("null")) {
                if (Integer.valueOf(commodity.getCook_position()) == 0) {
                    sw4.setChecked(false);
                    Cook = "0";
                    Log.d("print", "数据为编辑" + Cook);
                } else {
                    sw4.setChecked(true);
                    Cook = commodity.getCook_position();
                }
            }


            tv_biaoti.setText("编辑商品");
            isup = Boolean.parseBoolean(commodity.getMarketable());
            sw2.setChecked(Boolean.parseBoolean(commodity.getMarketable()));
            ed_name.setText(commodity.getName());
            ed_name.setSelection(commodity.getName().length());//将光标移至文字末尾
            ed_Member.setText(commodity.getMember_price());
            if (StringUtils.getStrings(commodity.getCustom_member_price(), ",").length == 5) {
                ed_print1.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[0]);
                ed_print2.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[1]);
                ed_print3.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[2]);
                ed_print4.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[3]);
                ed_print5.setText(StringUtils.getStrings(commodity.getCustom_member_price(), ",")[4]);
            }else {
                ed_print1.setText(commodity.getMember_price());
            }
            ed_cost.setText(StringUtils.stringpointtwo(commodity.getCost()));
            ed_cost.setSelection(StringUtils.stringpointtwo(commodity.getCost()).length());//将光标移至文字末尾
            ed_price.setText(StringUtils.stringpointtwo(commodity.getPrice()));
            ed_price.setSelection(StringUtils.stringpointtwo(commodity.getPrice()).length());//将光标移至文字末尾
            tv_bncode.setText(commodity.getBncode());
            ed_good_limit.setText(commodity.getGood_limit());
            ed_good_limit.setSelection(commodity.getGood_limit().length());//将光标移至文字末尾
            ed_describe.setText(commodity.getGood_remark());
//            ed_describe.setSelection(commodity.getGood_remark().length());//将光标移至文字末尾
            Log.d("print", "分类是" + commodity.getTag_name());
            tv_category.setText(commodity.getTag_name());

            if (commodity.getUnit().equals("null")) {
                tv_unit.setText("");
            } else {
                tv_unit.setText(commodity.getUnit());
            }

            tv_provider.setText(commodity.getProvider_name());
            provider_id = commodity.getProvider_id();
            ed_store.setText(commodity.getStore());
            ed_good_limit.setText(commodity.getGood_limit());
            ed_good_stock.setText(commodity.getGood_stock());
            tv_specification.setText(commodity.getSpecification());
            if (commodity.getPy().equals("null")) {
                ed_py.setText("");
            } else {
                ed_py.setText(commodity.getPy());
            }
            String label = "";
            if (commodity.getAdats() != null) {
                if (commodity.getAdats().size() > 0) {
                    for (int i = 0; i < commodity.getAdats().size(); i++) {
                        label += commodity.getAdats().get(i).getLabel_name();
                        Log.d("print", "打印的标签名字" + commodity.getAdats().get(i).getLabel_name());
                    }
                    tv_lable.setText(label);
                }
            }


            if (!commodity.getGD().equals("null")) {
                tv_data.setText(TimeZoneUtil.getTime(1000 * Long.valueOf(commodity.getGD())));
            }

            if (!commodity.getPD().equals("null")) {
                ed_baozhi.setText(commodity.getPD());
            }

            if (!commodity.getPD().equals("null") && !commodity.getGD().equals("null")) {
                tv_shenyuday.setText(DateUtils.getDateSpan(DateUtils.getDate(), tv_day.getText().toString(), 1) + "");
            }

//            tv_day.setText(TimeZoneUtil.getTime(1000*Long.valueOf(commodity.getGD())));
            url = SysUtils.getGoodsServiceUrl("goodsToAdd") + "&edit=edit&goods_id=" + commodity.getGoods_id() + "product_id=" + commodity.getProduct_id();
            Title = "确定修改";
//            but_supplement.setVisibility(View.VISIBLE);
        }
        } else {
            but_devanning.setText("开启拆箱");
            but_supplement.setVisibility(View.GONE);
            sw2.setChecked(isup);
            but_delete.setVisibility(View.GONE);
            butPrint.setVisibility(View.GONE);
            url = SysUtils.getGoodsServiceUrl("goodsToAdd");
            Title = "确定保存";
            showSoftInputFromWindow(Addgoodgs_Activity.this,tv_bncode);
            tv_bncode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i== EditorInfo.IME_ACTION_SEARCH){
                        tv_bncode.getText().toString();
                        Seekbank(tv_bncode.getText().toString());
                    }
                    return true;
                }
            });
        }
        Showunit();
        ShowTag();

//        保存商品的监听
        but_baochun = (Button) findViewById(R.id.but_baochun);
        but_baochun.setOnClickListener(this);


        ed_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ed_price.getText().toString()!=null&&!ed_price.getText().toString().equals("")&&!editable.toString().equals("")&&editable.toString()!=null){
                    ed_cost.setText(TlossUtils.mul(Double.parseDouble(ed_price.getText().toString()),Double.parseDouble(editable.toString()))+"");
                }
            }
        });
        //设置过滤器
        ed_discount.setFilters(new InputFilter[]{new InputMoney()});

        if (SharedUtil.getString("sw_order")!=null){
            if (SharedUtil.getString("sw_order").equals("0")){
                order="0";
            }else {
                order="1";
            }
        }else {
            order="0";
        }

    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        spadapter = new Spadapter(this);
        spadapter.setOneidtextonclick(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqliteHelper.close();
        sqLiteDatabase.close();
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
    String prices="";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                if (ll_attribute.getVisibility()==View.GONE){
                    ll_attribute.setVisibility(View.VISIBLE);
                }else {
                    ll_attribute.setVisibility(View.GONE);
                }
                break;
            case R.id.but_devanning:
               Unbox();
                break;
            case R.id.tv_huanghui:
                finish();
                break;
            case R.id.rl_unit:
                ShowPop(listFenlei,tv_category,"1");
                break;
            case R.id.rl_danwei:
                ShowPop(adats,tv_unit,"2");
                break;
            case R.id.rl_label:
                Intent intent3 = new Intent(Addgoodgs_Activity.this, Label_activity.class);
                intent3.putExtra("com.yzx.value", "value");
                startActivityForResult(intent3, 205);
                break;
            case R.id.but_baochun:
                if (ed_name.getText().toString().equals("")) {
                    Toast.makeText(Addgoodgs_Activity.this, "商品名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ed_price.getText().toString().equals("")) {
                    Toast.makeText(Addgoodgs_Activity.this, "售价不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ed_baozhi.getText().toString().equals("")&&ed_baozhi.getText().toString()==null){
                    PD="0";
                }else {
                    PD=ed_baozhi.getText().toString();
                }
                if (tv_bncode.getText().toString().length()<=5){
                    Toast.makeText(Addgoodgs_Activity.this, "条码不能低于5位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tag_id.equals("")&&tag_id==null){
                    Toast.makeText(Addgoodgs_Activity.this, "请选择分类", Toast.LENGTH_SHORT).show();
                }
                if (tv_category.getText().toString().equals("")&&tv_category.getText().toString().equals("null")) {
                    Toast.makeText(Addgoodgs_Activity.this, "分类不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                    if (but_delete.getVisibility() == View.VISIBLE) {

                    prices="";
                    if (!ed_print1.getText().toString().equals("")&&ed_print1.getText().toString()!=null){
                        prices+=ed_print1.getText().toString()+",";
                    }else {
                        prices+=ed_price.getText().toString()+",";
                    }
                    if (!ed_print2.getText().toString().equals("")&&ed_print2.getText().toString()!=null){
                        prices+=ed_print2.getText().toString()+",";
                    }else {
                        prices+=ed_price.getText().toString()+",";
                    }
                    if (!ed_print3.getText().toString().equals("")&&ed_print3.getText().toString()!=null){
                        prices+=ed_print3.getText().toString()+",";
                    }else {
                        prices+=ed_price.getText().toString()+",";
                    }
                    if (!ed_print4.getText().toString().equals("")&&ed_print4.getText().toString()!=null){
                        prices+=ed_print4.getText().toString()+",";
                    }else {
                        prices+=ed_price.getText().toString()+",";
                    }
                    if (!ed_print5.getText().toString().equals("")&&ed_print5.getText().toString()!=null){
                        prices+=ed_print5.getText().toString();
                    }else {
                        prices+=ed_price.getText().toString();
                    }


                        if (commodity != null && tag_id.equals("")) {
                            tag_id = commodity.getTag_id();
                        }
                        Log.d("print", "标签" + lable_id);
//                        Log.d("print", "标签" + unit_id);
                        tv_biaoti.setText("编辑商品");
                        if (!ed_discount.getText().toString().equals("")&&ed_discount.getText().toString()!=null){
                            discount=ed_discount.getText().toString();
                        }
                        sensitivity_price = Float.parseFloat(commodity.getCost());
                        sensitivity_selling = Float.parseFloat(commodity.getPrice());
                        sensitivity_store = Float.parseFloat(commodity.getStore());

                        if (!commodity.getGoods_id().equals("null")){
                            goods_id=commodity.getGoods_id();
                        }else {
                            goods_id="";
                        }
                        if (ed_cost.getText().toString().equals("")){
                            Toast.makeText(Addgoodgs_Activity.this,"请填写进价",Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        but_supplement.setVisibility(View.VISIBLE);
//                                        Log.d("Cook","打印组污染1   "+SharedUtil.getString("seller_id"));
//                                        Log.d("Cook","打印组污染2   "+commodity.getGoods_id());
//                                        Log.d("Cook","打印组污染3   "+ed_Member.getText().toString());
                                        showLoad("正在上传数据...");
                                        OkGo.post(SysUtils.getGoodsServiceUrl("goodsToAdd"))
                                                .tag(this)
                                                .cacheKey("cacheKey")
                                                .connTimeOut(1000)
                                                .cacheMode(CacheMode.DEFAULT)
                                                .params("custom_member_price",prices)
                                                .params("name", ed_name.getText().toString())
                                                .params("price", ed_price.getText().toString())
                                                .params("member_price", ed_Member.getText().toString())
                                                .params("cost", ed_cost.getText().toString())
                                                .params("bncode", tv_bncode.getText().toString())
                                                .params("edit", "edit")
                                                .params("provider_id", provider_id)
                                                .params("ALTC", iskuaijiejian)
                                                .params("cook_position", Cook)
                                                .params("goods_id",goods_id )
                                                .params("product_id", commodity.getProduct_id())
                                                .params("little_profit", Float.parseFloat(ed_price.getText().toString()) - Float.parseFloat(ed_cost.getText().toString()) + "")
                                                .params("store", ed_store.getText().toString())
                                                .params("GD",DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm",tv_data.getText().toString()))
                                                .params("PD",PD)
                                                .params("isup", isup)
                                                .params("good_limit", ed_good_limit.getText().toString())
                                                .params("label_id", lable_id)
                                                .params("py", ed_py.getText().toString())
                                                .params("good_stock", ed_good_stock.getText().toString())
                                                .params("good_remark", ed_describe.getText().toString())
//                                                .params("tag_id", commodity.getTag_id())
                                                .params("unit_id", unit_id)
                                                .params("tag_id", tag_id)
                                                .params("is_special",special)
                                                .params("btn_switch_type", 1 + "")
                                                .params("image_id", "")
                                                .params("discount",discount)
                                                .params("specification", tv_specification.getText().toString())
                                                .params("produce_addr", tv_produce_addr.getText().toString())
//                                                .params("intro", intro)
                                                .execute(new StringCallback() {

                                                    @Override
                                                    public void onError(Call call, Response response, Exception e) {
                                                        super.onError(call, response, e);
                                                        dismissLoad();
                                                    }
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(s);
                                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                                            String status = jo1.getString("status");
                                                            if (status.equals("200")) {
                                                                if (sensitivity_price != Float.parseFloat(ed_cost.getText().toString())) {
                                                                    //进价的敏感操作
                                                                    stringcontext = "修改了进价由" + sensitivity_price + "变成了" + ed_cost.getText().toString();
                                                                }
                                                                if (sensitivity_selling != Float.parseFloat(ed_price.getText().toString())) {
                                                                    //售价的敏感操作
                                                                    stringcontext = stringcontext + "修改了售价" + sensitivity_selling + "变成了" + ed_price.getText().toString();
                                                                }
                                                                if (sensitivity_store != Float.parseFloat(ed_store.getText().toString())) {
                                                                    //库存的敏感操作
                                                                    stringcontext = stringcontext + "修改了库存" + sensitivity_store + "变成了" + ed_store.getText().toString();
                                                                }
                                                                stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                                                                stringMap.put("work_name", SharedUtil.getString("name"));
                                                                stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                                                                if (returned) {
                                                                    stringMap.put("operate_type", "反退货" + ed_name.getText().toString());
                                                                } else {
                                                                    stringMap.put("operate_type", "编辑" + ed_name.getText().toString());
                                                                }
                                                                stringMap.put("content", stringcontext);
                                                                Gson gson = new Gson();
                                                                String ing = gson.toJson(stringMap);
                                                                Log.e("print", "数据为" + SharedUtil.getString("name"));
                                                                getsensitivity(ing);

                                                                sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                                                Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodity.getGoods_id()});
                                                                while (cursor.moveToNext()) {
                                                                    ContentValues values = new ContentValues();
                                                                    values.put("name", ed_name.getText().toString());
                                                                    values.put("price", ed_price.getText().toString());
                                                                    values.put("cost", ed_cost.getText().toString());
                                                                    values.put("bncode", tv_bncode.getText().toString());
                                                                    values.put("ALTC", iskuaijiejian);
                                                                    values.put("cook_position", Cook);
                                                                    values.put("store", ed_store.getText().toString());
                                                                    values.put("member_price", ed_Member.getText().toString());

                                                                    values.put("is_special_offer", special);
//                                                                    values.put("good_limit", ed_good_limit.getText().toString() );
//                                                                    values.put("py", ed_py.getText().toString() );
//                                                                    values.put("good_stock", ed_good_stock.getText().toString() );
//                                                                    values.put("unit_id", unit_id );
//                                                                    values.put("unit_name", unit );
//                                                                    values.put("tag_name", tag_name );
                                                                    sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodity.getGoods_id()});
                                                                }

                                                                JSONObject data = jo1.getJSONObject("data");
                                                                String msg = data.getString("msg");
                                                                Toast.makeText(Addgoodgs_Activity.this, msg, Toast.LENGTH_SHORT).show();
                                                                cursor.close();
                                                                dismissLoad();
                                                                finish();
                                                            }else {
                                                                String message = jo1.getString("message");
                                                                Toast.makeText(Addgoodgs_Activity.this, message, Toast.LENGTH_SHORT).show();
                                                                dismissLoad();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                    } else {
                        tv_biaoti.setText("新增商品");
                        final String cost;
                        final String store;
                        final String little_profit;
                        final String good_limit;
                        final String good_stock;
                        if (ed_cost.getText().toString().equals("")){
                            cost=ed_price.getText().toString();
                            little_profit="0";
                        }else {
                            cost= ed_cost.getText().toString();
                            little_profit=Float.parseFloat(ed_price.getText().toString()) - Float.parseFloat(ed_cost.getText().toString())+"";
                        }

                        if (ed_store.getText().toString().equals("")){
                            store="0";
                        }else {
                            store=ed_store.getText().toString();
                        }

                        if (tv_bncode.getText().toString().length()<=5){
                            Toast.makeText(Addgoodgs_Activity.this, "条码不能低于5位", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (ed_good_limit.getText().toString().equals("")){
                            good_limit="0";
                        }else {
                            good_limit=ed_good_limit.getText().toString();
                        }
                        if (ed_good_stock.getText().toString().equals("")){
                            good_stock="0";
                        }else {
                            good_stock=ed_good_stock.getText().toString();
                        }

                        if (ed_baozhi.getText().toString().equals("")&&ed_baozhi.getText().toString()==null){
                            PD="0";
                        }else {
                            PD=ed_baozhi.getText().toString();
                        }

                        prices="";
                        if (!ed_print1.getText().toString().equals("")&&ed_print1.getText().toString()!=null){
                            prices+=ed_print1.getText().toString()+",";
                        }else {
                            prices+=ed_price.getText().toString()+",";
                        }
                        if (!ed_print2.getText().toString().equals("")&&ed_print2.getText().toString()!=null){
                            prices+=ed_print2.getText().toString()+",";
                        }else {
                            prices+=ed_price.getText().toString()+",";
                        }
                        if (!ed_print3.getText().toString().equals("")&&ed_print3.getText().toString()!=null){
                            prices+=ed_print3.getText().toString()+",";
                        }else {
                            prices+=ed_price.getText().toString()+",";
                        }
                        if (!ed_print4.getText().toString().equals("")&&ed_print4.getText().toString()!=null){
                            prices+=ed_print4.getText().toString()+",";
                        }else {
                            prices+=ed_price.getText().toString()+",";
                        }
                        if (!ed_print5.getText().toString().equals("")&&ed_print5.getText().toString()!=null){
                            prices+=ed_print5.getText().toString();
                        }else {
                            prices+=ed_price.getText().toString();
                        }

                        if (!ed_discount.getText().toString().equals("")&&ed_discount.getText().toString()!=null){
                            discount=ed_discount.getText().toString();
                        }

                                        Log.d("Day","打印生产日期"+DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm",tv_data.getText().toString()));
                                        Log.d("Day","打印生产日期"+ed_baozhi.getText().toString());
                                        showLoad("正在上传数据...");
                                        OkGo.post(SysUtils.getGoodsServiceUrl("goodsToAdd"))
                                                .tag(this)
                                                .cacheKey("cacheKey")
                                                .connTimeOut(1000)
                                                .cacheMode(CacheMode.DEFAULT)
                                                .params("custom_member_price",prices)
                                                .params("name", ed_name.getText().toString())
                                                .params("price", ed_price.getText().toString())
                                                .params("member_price", ed_Member.getText().toString())
                                                .params("cost", cost)
                                                .params("bncode", tv_bncode.getText().toString())
                                                .params("little_profit", little_profit)
                                                .params("store", store)
                                                .params("GD",DateUtils.getFormatedDateTime("yyyy-MM-dd HH:mm",tv_data.getText().toString()))
                                                .params("PD",ed_baozhi.getText().toString())
                                                .params("isup", isup)
                                                .params("provider_id", provider_id)
                                                .params("cook_position",Cook)
                                                .params("label_id", lable_id)
                                                .params("good_limit", good_limit)
                                                .params("py", ed_py.getText().toString())
                                                .params("ALTC", iskuaijiejian)
                                                .params("cook_position", Cook)
                                                .params("good_stock", good_stock)
                                                .params("good_remark", ed_describe.getText().toString())
                                                .params("tag_id", tag_id)
                                                .params("unit_id", unit_id)
                                                .params("is_special",special)
                                                .params("btn_switch_type", 1 + "")
                                                .params("image_id", "")
                                                .params("specification", tv_specification.getText().toString())
                                                .params("discount",discount)
                                                .params("produce_addr",tv_produce_addr.getText().toString())
//                                                .params("intro", intro)
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onBefore(BaseRequest request) {
                                                        super.onBefore(request);
                                                                Log.d("print","新增商品的"+request.getParams().toString());
                                                                Log.d("print","provider_id"+provider_id);
                                                    }

                                                    @Override
                                                    public void onError(Call call, Response response, Exception e) {
                                                        super.onError(call, response, e);
                                                        dismissLoad();
                                                    }

                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        Log.d("print", s);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(s);
                                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                                            String status = jo1.getString("status");
                                                            if (status.equals("200")) {
                                                                JSONObject data = jo1.getJSONObject("data");
                                                                String msg = data.getString("msg");
                                                                Toast.makeText(Addgoodgs_Activity.this, msg, Toast.LENGTH_SHORT).show();
                                                                dismissLoad();
                                                                finish();
                                                            }else {
                                                                dismissLoad();
                                                                String message = jo1.getString("message");
                                                                Toast.makeText(Addgoodgs_Activity.this,message,Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                    }
                break;
                //打印标签
            case R.id.butPrint:
                LablePrint();
                break;
            case R.id.but_delete:
                tv_biaoti.setText("编辑商品");
                tv_bncode.setText(commodity.getBncode());
                ed_name.setText(commodity.getName());
                ed_Member.setText(commodity.getMember_price());
                ed_cost.setText(commodity.getCost());
                ed_price.setText(commodity.getPrice());
                if (StringUtils.getStrings(commodity.getCustom_member_price(),",").length==5){
                    ed_print1.setText(StringUtils.getStrings(commodity.getCustom_member_price(),",")[0]);
                    ed_print2.setText(StringUtils.getStrings(commodity.getCustom_member_price(),",")[1]);
                    ed_print3.setText(StringUtils.getStrings(commodity.getCustom_member_price(),",")[2]);
                    ed_print4.setText(StringUtils.getStrings(commodity.getCustom_member_price(),",")[3]);
                    ed_print5.setText(StringUtils.getStrings(commodity.getCustom_member_price(),",")[4]);
                }
                if (commodity.getTag_name().equals("")) {
                    tv_category.setText(commodity.getTag_name() + "");
                }
                ed_store.setText(commodity.getStore());
                if (commodity.getUnit().equals("null")) {
                    tv_unit.setText("");
                } else {
                    tv_unit.setText(commodity.getUnit());
                }
                tv_provider.setText(commodity.getProvider_name());
                provider_id=commodity.getProvider_id();
                ed_good_limit.setText(commodity.getGood_limit());
                ed_good_stock.setText(commodity.getGood_stock());
                Log.d("print", "eeee" + commodity);
                if (commodity.getPy().equals("null")) {
                    ed_py.setText("");
                } else {
                    ed_py.setText(commodity.getPy());
                }
                String label = "";
                if (commodity.getAdats() != null) {
                    if (commodity.getAdats().size() > 0) {
                        for (int i = 0; i < commodity.getAdats().size(); i++) {
                            label += commodity.getAdats().get(i).getLabel_name();
                            Log.d("print","打印的标签名字"+commodity.getAdats().get(i).getLabel_name());
                        }
                        tv_lable.setText(label);
                    }
                }

                if (!commodity.getGD().equals("null")) {
                    tv_data.setText(TimeZoneUtil.getTime(1000 * Long.valueOf(commodity.getGD())));
                }

                if (!commodity.getPD().equals("null")){
                    ed_baozhi.setText(commodity.getPD());
                }

                if (!commodity.getPD().equals("null")&&!commodity.getGD().equals("null")){
                    tv_shenyuday.setText(DateUtils.getDateSpan(DateUtils.getDate(),tv_day.getText().toString(),1)+"");
                }

//                tv_day.setText(TimeZoneUtil.getTime(1000*Long.valueOf(commodity.getGD())));
                url = SysUtils.getGoodsServiceUrl("goodsToAdd");
                Title = "确定删除";
                new AlertDialog.Builder(this).setTitle(Title)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("print", "" + commodity.getGoods_id());
                                showLoad("正在上传数据...");
                                OkGo.post(SysUtils.getGoodsServiceUrl("remove_goods"))
                                        .tag(this)
                                        .cacheKey("cacheKey")
                                        .connTimeOut(1000)
                                        .cacheMode(CacheMode.DEFAULT)
                                        .params("goods_id", commodity.getGoods_id() + "")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onCacheError(Call call, Exception e) {
                                                super.onCacheError(call, e);
                                                Log.d("print", "请求超时");
                                            }

                                            @Override
                                            public void onError(Call call, Response response, Exception e) {
                                                super.onError(call, response, e);
                                                dismissLoad();
                                            }

                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                Log.d("print", "删除" + s);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(s);
                                                    JSONObject jo1 = jsonObject.getJSONObject("response");
                                                    String status = jo1.getString("status");
                                                    if (status.equals("200")) {
                                                        stringMap.put("seller_id", SharedUtil.getString("seller_id"));
                                                        stringMap.put("work_name", SharedUtil.getString("name"));
                                                        stringMap.put("seller_name", SharedUtil.getString("seller_name"));
                                                        stringMap.put("operate_type", "删除" + ed_name.getText().toString());
                                                        stringcontext = ed_name.getText().toString() + "被删除了";
                                                        stringMap.put("content", stringcontext);
                                                        Gson gson = new Gson();
                                                        String ing = gson.toJson(stringMap);
                                                        Log.e("print", "数据为" + SharedUtil.getString("name"));
                                                        getsensitivity(ing);

                                                        dismissLoad();
                                                        Toast.makeText(Addgoodgs_Activity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else {
                                                        dismissLoad();
                                                        String message = jo1.getString("message");
                                                        Toast.makeText(Addgoodgs_Activity.this, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

                break;
            case R.id.rl_provider:
                Intent intent4 = new Intent(Addgoodgs_Activity.this, Provider_activity.class);
                intent4.putExtra("com.yzx.value", "value");
                startActivityForResult(intent4, 207);
                break;
            case R.id.im_data:
                DateTimeUtils.runTime(Addgoodgs_Activity.this,tv_data);

//                timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        tv_data.setText(time);
//                    }
//                }, "2015-11-22 17:34", getTime());
//                timeSelector.show();
                break;
            case R.id.but_supplement:
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("补充库存");
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_supplement);
                Button but_add = (Button) window.findViewById(R.id.but_add);
                final EditText ed_add = (EditText) window.findViewById(R.id.ed_add);
                but_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ed_add.getText().toString() != null && StringUtils.isNumber(ed_add.getText().toString())) {
                            ed_store.setText((Double.parseDouble(ed_store.getText().toString()) + Integer.parseInt(ed_add.getText().toString())) + "");
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Inputmethod_Utils.getshow(getApplication());
                    }
                });
                break;
            case R.id.but_return:
                //反退货的敏感操作
                final Dialog dialog1 = new Dialog(this);
                dialog1.setTitle("反退货");
                dialog1.show();
                Window window1 = dialog1.getWindow();
                window1.setContentView(R.layout.dialog_supplement);
                Button but_add1 = (Button) window1.findViewById(R.id.but_add);
                TextView tv_t1 = (TextView) window1.findViewById(R.id.tv_t1);
                tv_t1.setText("反退货的数量");
                final EditText ed_add1 = (EditText) window1.findViewById(R.id.ed_add);
                ed_add1.setHint("输入反退货数量");
                but_add1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ed_add1.getText().toString() != null && StringUtils.isNumber(ed_add1.getText().toString())) {
                            ed_store.setText((Integer.parseInt(ed_store.getText().toString()) - Integer.parseInt(ed_add1.getText().toString())) + "");
                            returned = true;
                            showreturn(Integer.parseInt(ed_add1.getText().toString()));
                        }
                        dialog1.dismiss();
                    }
                });
                dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Inputmethod_Utils.getshow(getApplication());
                    }
                });
                break;
        }
    }

    //订单商品添加数量与备注
    private View view_add_nums_notes;
    InputMethodManager  imm;
    private android.support.v7.app.AlertDialog mAlertDialog_add_nums_notes;
    /**
     * 拆箱的方法
     */
    public void Unbox(){
        if (but_devanning.getText().toString().equals("拆箱")){
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Addgoodgs_Activity.this, R.style.AlertDialog);
            view_add_nums_notes = View.inflate(Addgoodgs_Activity.this, R.layout.dialog_disable, null);
            final EditText ed_add= (EditText) view_add_nums_notes.findViewById(R.id.ed_add);
            ed_add.setHint("请输入拆箱数");
            Button but_add= (Button) view_add_nums_notes.findViewById(R.id.but_add);
            but_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(ed_add.getText().toString())){
                        Log.d("print","拆箱的数据为"+ed_add.getText().toString());
                        Split_open_a_case(ed_add.getText().toString(),mAlertDialog_add_nums_notes);
                    }
                }
            });
            mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
            mAlertDialog_add_nums_notes.setCancelable(false);
            mAlertDialog_add_nums_notes.show();

//            final Dialog dialog1 = new Dialog(this);
//            dialog1.setTitle("拆箱");
//            dialog1.show();
//            Window window1 = dialog1.getWindow();
//            window1.setContentView(R.layout.dialog_disable);
//            final EditText ed_add= (EditText) window1.findViewById(R.id.ed_add);
//            ed_add.setHint("请输入拆箱数");
//            Button but_add= (Button) window1.findViewById(R.id.but_add);
//            but_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!TextUtils.isEmpty(ed_add.getText().toString())){
//                        Log.d("print","拆箱的数据为"+ed_add.getText().toString());
//                        Split_open_a_case(ed_add.getText().toString(),dialog1);
//                    }
//                }
//            });
//            dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialogInterface) {
//                    Inputmethod_Utils.getshow(Addgoodgs_Activity.this);
//                }
//            });
        }else {
            opendisable();
        }
    }

    public ShapeLoadingDialog loadingdialog;

    /**
     * 显示加载框
     */
    public void showLoad(String str){
        ShapeLoadingDialog.Builder builder = new ShapeLoadingDialog.Builder(Addgoodgs_Activity.this);
        builder.delay(1);
        loadingdialog = new ShapeLoadingDialog(builder);
        loadingdialog.getBuilder().loadText(str);
        loadingdialog.show();
    }

    /**
     * 关闭加载框
     */
    public void dismissLoad(){
        loadingdialog.dismiss();
    }

    public void LablePrint(){
        UsbPrinterUtil mUtil=null;
        mUtil = new UsbPrinterUtil(Addgoodgs_Activity.this);
        List<UsbDevice> devs = mUtil.getUsbPrinterList();
        if (devs.size() > 0) {
            mUtil.requestPermission(devs.get(0), null);
        }
        if (mUtil.getUsbPrinterList().size()>0) {
            UsbDevice dev = mUtil.getUsbPrinterList().get(0);
            UsbPrinter prt = null;
            UsbPrinter.FONT font = UsbPrinter.FONT.FONT_B;
            Boolean bold = false;
            Boolean underlined = false;
            Boolean doubleHeight = false;
            Boolean doubleWidth = false;
            try {
                prt = new UsbPrinter(this, dev);
                if (!SharedUtil.getBoolean("printlabel1")) {
                    String str1 = "SIZE 40mm,30mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                    String str2 = "\\nDENSITY 3\\nHOME\\nBARCODE 20,30,\"128\",80,1,0,2,4,\"" + commodity.getBncode() + "\"";
                    String str6 = "";
                    String str = " ";
                    int j = 40 - (commodity.getName().length() * 3);
                    if (j <= 0) {
                    } else {
                        for (int k = 0; k < (j / 3); k++) {
                            str += " ";
                        }
                    }
                    str6 = "\\nDENSITY 15\\nTEXT 0,170,\"TSS24.BF2\",0,1,1,\"" + str + commodity.getName() + "\"" + "\\nPRINT 1,1";
                    try {
                        List<String> strings=new ArrayList<>();
//                        prt.printString(str1, font, bold, underlined, doubleHeight,
//                                doubleWidth);
//                        prt.printString(str2, font, bold, underlined, doubleHeight,
//                                doubleWidth);
//                        prt.printString(str6, font, bold, underlined, doubleHeight,
//                                doubleWidth);
                        strings.add(str1);
                        strings.add(str2);
                        strings.add(str6);
                        setPrint(prt,strings);
                    } catch (Exception e) {
                        Log.e("barcode", "标签打印异常：" + e.toString());
                    }
                } else {
                    if (SharedUtil.getString("is_price") != null && SharedUtil.getString("is_price").equals("")) {
                        Bitmap bitmap2=null;
                        Bitmap bitmap3=null;
                        Bitmap bitmap4=null;
                        Bitmap bitmap5=null;
                        Bitmap bitmap8=null;
                        String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                        String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                        bitmap2= Bitmap_Utils.fromText(SharedUtil.getString("name"),40);
                        String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
                        bitmap3=Bitmap_Utils.fromText(commodity.getName(),40);
                        String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"" + "\\r\\n";
                        bitmap8=Bitmap_Utils.fromText(Double.parseDouble(commodity.getPrice())+"",80);
                        String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
                        bitmap4=Bitmap_Utils.fromText(commodity.getSpecification()+"",40);
                        String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
                        bitmap5=Bitmap_Utils.fromText(commodity.getUnit(),20);
                        String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
                        prt.printString(str1, font, bold, underlined, doubleHeight,
                                doubleWidth);
                        prt.write(addBitmap(280,15, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                        prt.write(addBitmap(95,65, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                        prt.write(addBitmap(400,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap8.getWidth(), bitmap8));
                        prt.write(addBitmap(85,155, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                        prt.write(addBitmap(220,125, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
//                                prt.printString(str2, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str3, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str8, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str4, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str5, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
                        prt.printString(str7, font, bold, underlined, doubleHeight,
                                doubleWidth);
//                        List<String> string2=new ArrayList<>();
//                        String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
//                        String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
//                        String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
//                        String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"" + "\\r\\n";
//                        String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
//                        String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
//                        String str7 = "\\nDENSITY 3\\nHOME\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
////                        prt.printString(str1, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str2, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str3, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str8, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str4, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str5, font, bold, underlined, doubleHeight,
////                                doubleWidth);
////                        prt.printString(str7, font, bold, underlined, doubleHeight,
////                                doubleWidth);
//                        string2.add(str1);
//                        string2.add(str2);
//                        string2.add(str3);
//                        string2.add(str8);
//                        string2.add(str4);
//                        string2.add(str5);
//                        string2.add(str7);
//                        setPrint(prt,string2);
                    } else {
                        switch (SharedUtil.getString("is_price")) {
                            case "1":
                                Bitmap bitmap2=null;
                                Bitmap bitmap3=null;
                                Bitmap bitmap4=null;
                                Bitmap bitmap5=null;
                                Bitmap bitmap8=null;
                                String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
                                String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                bitmap2= Bitmap_Utils.fromText(SharedUtil.getString("name"),40);
                                String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
                                bitmap3=Bitmap_Utils.fromText(commodity.getName(),40);
                                String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"" + "\\r\\n";
                                bitmap8=Bitmap_Utils.fromText(Double.parseDouble(commodity.getPrice())+"",80);
                                String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
                                bitmap4=Bitmap_Utils.fromText(commodity.getSpecification()+"",40);
                                String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
                                bitmap5=Bitmap_Utils.fromText(commodity.getUnit(),20);
                                String str7 = "\\nDENSITY 3\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
                                prt.printString(str1, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.write(addBitmap(280,15, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap2.getWidth(), bitmap2));
                                prt.write(addBitmap(95,65, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap3.getWidth(), bitmap3));
                                prt.write(addBitmap(400,180, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap8.getWidth(), bitmap8));
                                prt.write(addBitmap(85,155, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap4.getWidth(), bitmap4));
                                prt.write(addBitmap(220,125, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitmap5.getWidth(), bitmap5));
//                                prt.printString(str2, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str3, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str8, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str4, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str5, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
                                prt.printString(str7, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                break;
                            case "2":
                                Bitmap bit20=null;
                                Bitmap bit30=null;
                                Bitmap bit40=null;
                                Bitmap bit50=null;
                                String str10 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                String str20 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodity.getPrice()) + "\"";
                                bit20=Bitmap_Utils.fromText(StringUtils.setPointone(commodity.getPrice()) ,100);
                                String str30 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"";
                                bit30=Bitmap_Utils.fromText(commodity.getName(),40);
                                String str40 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"";
                                bit40=Bitmap_Utils.fromText(commodity.getSpecification(),20);
                                String str50 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"";
                                bit50=Bitmap_Utils.fromText(commodity.getUnit() ,20);
                                String str70 = "\\nDENSITY 3\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";

                                prt.printString(str10, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.write(addBitmap(320,12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit20.getWidth(), bit20));
                                prt.write(addBitmap(100,70, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit30.getWidth(), bit30));
                                prt.write(addBitmap(340,150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit40.getWidth(), bit40));
                                prt.write(addBitmap(140,150, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit50.getWidth(), bit50));
//                                prt.printString(str20, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str30, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
////                        prt.printString(str8, font, bold, underlined, doubleHeight,
////                                doubleWidth);
//                                prt.printString(str40, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str50, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
                                prt.printString(str70, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                break;
                            case "3":
                                Bitmap bit12=null;
                                Bitmap bit13=null;
                                Bitmap bit14=null;
                                Bitmap bit16=null;
                                Bitmap bit18=null;
                                Bitmap bit19=null;
                                String str11 = "SIZE 50mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
                                String str21 = "\\nDENSITY 7\\nTEXT 100,12,\"TSS24.BF2\",0,1,2,\"" + commodity.getName()+ "\"";
                                bit12=Bitmap_Utils.fromText(commodity.getName() ,40);
//                                    String str31 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
                                String str41 = "\\nDENSITY 15\\nTEXT 250,110,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"";
                                bit14=Bitmap_Utils.fromText(Double.parseDouble(commodity.getPrice())+"" ,100);
                                String str31 = "\\nDENSITY 15\\nTEXT 20,240,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"";
                                bit13=Bitmap_Utils.fromText(SharedUtil.getString("name") ,40);
                                String str61;
                                String str81;
                                String str91;
                                String str100="\\nPRINT 1,1";
                                if (commodity.getCook_position().equals("0")){
                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\"" +"单位:"+ commodity.getUnit() + "\"";
                                    bit16=Bitmap_Utils.fromText("单位:"+ commodity.getUnit() ,20);
                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:"+commodity.getSpecification() + "\"";
                                    bit18=Bitmap_Utils.fromText("规格:"+commodity.getSpecification() ,20);
                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装" + "\""+"\\nPRINT 1,1";
                                    bit19=Bitmap_Utils.fromText("产地:见包装" ,20);
                                }else {
                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\""  +"单位:散称" + "\"";
                                    bit16=Bitmap_Utils.fromText("单位:散称" ,20);
                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:见包装" + "\"";
                                    bit18=Bitmap_Utils.fromText("规格:见包装" ,20);
                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装"+ "\""+"\\nPRINT 1,1";
                                    bit19=Bitmap_Utils.fromText("产地:见包装" ,20);
                                }

                                prt.printString(str11, font, bold, underlined, doubleHeight,
                                        doubleWidth);
                                prt.write(addBitmap(100,12, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit12.getWidth(), bit12));
                                prt.write(addBitmap(250,110, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit14.getWidth(), bit14));
                                prt.write(addBitmap(20,240, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit13.getWidth(), bit13));
                                prt.write(addBitmap(40,80, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit16.getWidth(), bit16));
                                prt.write(addBitmap(40,120, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit18.getWidth(), bit18));
                                prt.write(addBitmap(40,160, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit19.getWidth(), bit19));
//                                prt.printString(str21, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                    prt.printString(str31, font, bold, underlined, doubleHeight,
//                                            doubleWidth);
////                        prt.printString(str8, font, bold, underlined, doubleHeight,
////                                doubleWidth);
//                                prt.printString(str41, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str61, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str81, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
//                                prt.printString(str91, font, bold, underlined, doubleHeight,
//                                        doubleWidth);
                                prt.printString(str100,font,bold,underlined,doubleHeight,doubleWidth);
                                break;
                            case "4":
                                setdistance();
                                if (custom_entty!=null) {
                                    Bitmap bit=null;
                                    Bitmap bit2=null;
                                    Bitmap bit3=null;
                                    Bitmap bit4=null;
                                    Bitmap bitm4=null;
                                    Bitmap bit5=null;
                                    Bitmap bit6=null;
                                    Bitmap bit7=null;
                                    String strin1 = "SIZE " + custom_entty.getSize().substring(0, custom_entty.getSize().indexOf("*")) + "mm," + custom_entty.getSize().substring(custom_entty.getSize().indexOf("*") + 1) + "mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION " + order + "\\nCLS";
                                    String strin2 = "\\nDENSITY 15\\nTEXT " + custom_entty.getShopY() + "," + custom_entty.getShopX() + ",\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
                                    bit2=Bitmap_Utils.fromText(SharedUtil.getString("name") ,custom_entty.getShopsize());
                                    String strin3 = "\\nDENSITY 15\\nTEXT " + custom_entty.getNameY() + "," + custom_entty.getNameX() + ",\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
                                    bit3=Bitmap_Utils.fromText(commodity.getName() ,custom_entty.getNamesize());
                                    String strin4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getPriceY() + "," + custom_entty.getPriceX() + ",\"TSS24.BF2\",0,4,4,\"" + commodity.getPrice() + "\"" + "\\r\\n";
                                    bit4=Bitmap_Utils.fromText(Double.parseDouble(commodity.getPrice())+"" ,custom_entty.getPricesize());
                                    String strinm4 = "\\nDENSITY 15\\nTEXT " + custom_entty.getMemberpriceY() + "," + custom_entty.getMemberpriceX() + ",\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getMember_price()) + "\"" + "\\r\\n";
                                    bitm4=Bitmap_Utils.fromText(commodity.getMember_price() ,custom_entty.getMemberpricesize());
                                    String strin5 = "\\nDENSITY 15\\nTEXT " + custom_entty.getSpecificationsY() + "," + custom_entty.getSpecificationsX() + ",\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
                                    bit5=Bitmap_Utils.fromText(commodity.getSpecification() ,custom_entty.getSpecificationssize());
                                    String strin6 = "\\nDENSITY 15\\nTEXT " + custom_entty.getCompanyY() + "," + custom_entty.getCompanyX() + ",\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
                                    bit6=Bitmap_Utils.fromText(commodity.getUnit() ,custom_entty.getCompanysize());
                                    String strin9="\\nDENSITY 15\\nTEXT " + custom_entty.getNameproduce_addrY() + "," + custom_entty.getNameproduce_addrX() + ",\"TSS24.BF2\",0,1,1,\"" + commodity.getProduce_addr() + "\"" + "\\r\\n";
                                    bit7=Bitmap_Utils.fromText(commodity.getProduce_addr() ,custom_entty.getNameproduce_addrsize());
                                    String strin7 = "\\nDENSITY 3\\nBARCODE " + custom_entty.getCodeY() + "," + custom_entty.getCodeX() + ",\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" +"\\nPRINT 1,1";
                                    prt.printString(strin1, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                    if (custom_entty.getShopY() != null) {
                                        if (!custom_entty.getShopY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin2, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getShopY()) - 20, Integer.parseInt(custom_entty.getShopX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit2.getWidth(), bit2));
                                            }
                                        }
                                    }
                                    if (custom_entty.getNameY() != null) {
                                        if (!custom_entty.getNameY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin3, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getNameY()) - 20, Integer.parseInt(custom_entty.getNameX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit3.getWidth(), bit3));
                                            }
                                        }
                                    }
                                    if (custom_entty.getPriceY() != null) {
                                        if (!custom_entty.getPriceY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin4, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getPriceY()) - 50, Integer.parseInt(custom_entty.getPriceX()) - 20, com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit4.getWidth(), bit4));
                                            }
                                        }
                                    }
                                    if (custom_entty.getMemberpriceY() != null) {
                                        if (!custom_entty.getMemberpriceY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strinm4, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getMemberpriceY()) - 20, Integer.parseInt(custom_entty.getMemberpriceX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bitm4.getWidth(), bitm4));
                                            }
                                        }
                                    }
                                    if (custom_entty.getSpecificationsY() != null) {
                                        if (!custom_entty.getSpecificationsY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")){
                                                prt.printString(strin5, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            }else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getSpecificationsY()) - 20, Integer.parseInt(custom_entty.getSpecificationsX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit5.getWidth(), bit5));
                                            }
                                        }
                                    }

                                    if (custom_entty.getNameproduce_addrY() != null) {
                                        if (!custom_entty.getNameproduce_addrY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")) {
                                                prt.printString(strin9, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            } else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getNameproduce_addrY()) - 20, Integer.parseInt(custom_entty.getNameproduce_addrX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit7.getWidth(), bit7));
                                            }
                                        }
                                    }

                                    if (custom_entty.getCompanyY() != null) {
                                        if (!custom_entty.getCompanyY().equals("")) {
                                            if (SharedUtil.getfalseBoolean("old_version")) {
                                                prt.printString(strin6, font, bold, underlined, doubleHeight,
                                                        doubleWidth);
                                            } else {
                                                prt.write(addBitmap(Integer.parseInt(custom_entty.getCompanyY()) - 20, Integer.parseInt(custom_entty.getCompanyX()), com.gprinter.command.LabelCommand.BITMAP_MODE.OVERWRITE, bit6.getWidth(), bit6));
                                            }
                                        }
                                    }
                                    prt.printString(strin7, font, bold, underlined, doubleHeight,
                                            doubleWidth);
                                }
                                break;
//                            case "1":
//                                List<String> string1=new ArrayList<>();
//                                String str1 = "SIZE 70mm,38mm\\nGAP 3mm,0\\n@1=\"0001\"\\nCLS\\nDIRECTION "+order+"\\nCLS";
//                                String str2 = "\\nDENSITY 15\\nTEXT 280,15,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
//                                String str3 = "\\nDENSITY 15\\nTEXT 95,65,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
//                                String str8 = "\\nDENSITY 15\\nTEXT 400,180,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"" + "\\r\\n";
//                                String str4 = "\\nDENSITY 15\\nTEXT 85,155,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
//                                String str5 = "\\nDENSITY 15\\nTEXT 220,125,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
//                                String str7 = "\\nDENSITY 3\\nHOME\\nBARCODE 100,180,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
////                                prt.printString(str1, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str2, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str3, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str8, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str4, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str5, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str7, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//                                string1.add(str1);
//                                string1.add(str2);
//                                string1.add(str3);
//                                string1.add(str8);
//                                string1.add(str4);
//                                string1.add(str5);
//                                string1.add(str7);
//                                setPrint(prt,string1);
//                                break;
//                            case "2":
//                                List<String> stringList=new ArrayList<>();
//                                String str10 = "SIZE 60mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
//                                String str20 = "\\nDENSITY 7\\nTEXT 320,12,\"TSS24.BF2\",0,2,2,\"" + StringUtils.setPointone(commodity.getPrice()) + "\"";
//                                String str30 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"";
//                                String str40 = "\\nDENSITY 15\\nTEXT 340,150,\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"";
//                                String str50 = "\\nDENSITY 15\\nTEXT 140,150,\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"";
//                                String str70 = "\\nDENSITY 3\\nHOME\\nBARCODE 120,200,\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
////                                prt.printString(str10, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str20, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str30, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//////                        prt.printString(str8, font, bold, underlined, doubleHeight,
//////                                doubleWidth);
////                                prt.printString(str40, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str50, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str70, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//                                stringList.add(str10);
//                                stringList.add(str20);
//                                stringList.add(str30);
//                                stringList.add(str40);
//                                stringList.add(str50);
//                                stringList.add(str70);
//                                setPrint(prt,stringList);
//                                break;
//                            case "3":
//                                List<String> strings=new ArrayList<>();
//                                String str11 = "SIZE 50mm,35mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
//                                String str21 = "\\nDENSITY 7\\nTEXT 100,12,\"TSS24.BF2\",0,1,2,\"" + commodity.getName()+ "\"";
////                                    String str31 = "\\nDENSITY 15\\nTEXT 100,70,\"TSS24.BF2\",0,1,2,\"" + commodities.get(n).getName() + "\"";
//                                String str41 = "\\nDENSITY 15\\nTEXT 250,110,\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getPrice()) + "\"";
//                                String str31 = "\\nDENSITY 15\\nTEXT 20,240,\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"";
//                                String str61;
//                                String str81;
//                                String str91;
//                                if (commodity.getCook_position().equals("0")){
//                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\"" +"单位:"+ commodity.getUnit() + "\"";
//                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:"+commodity.getSpecification() + "\"";
//                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装" + "\""+"\\nPRINT 1,1";
//                                }else {
//                                    str61 = "\\nDENSITY 15\\nTEXT 40,80,\"TSS24.BF2\",0,1,1,\""  +"单位:散称" + "\"";
//                                    str81 = "\\nDENSITY 15\\nTEXT 40,120,\"TSS24.BF2\",0,1,1,\"" +"规格:见包装" + "\"";
//                                    str91 = "\\nDENSITY 15\\nTEXT 40,160,\"TSS24.BF2\",0,1,1,\"" +"产地:见包装"+ "\""+"\\nPRINT 1,1";
//                                }
//                                strings.add(str11);
//                                strings.add(str21);
//                                strings.add(str41);
//                                strings.add(str31);
//                                strings.add(str61);
//                                strings.add(str81);
//                                strings.add(str91);
//                                setPrint(prt,strings);
////                                prt.printString(str11, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str21, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str31, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//////                        prt.printString(str8, font, bold, underlined, doubleHeight,
//////                                doubleWidth);
////                                prt.printString(str41, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str61, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str81, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
////                                prt.printString(str91, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//                                break;
//                            case "4":
//                                List<String> strings1=new ArrayList<>();
//                                setdistance();
//                                String strin1 = "SIZE "+custom_entty.getSize().substring(0, custom_entty.getSize().indexOf("*"))+"mm,"+custom_entty.getSize().substring(custom_entty.getSize().indexOf("*")+1)+"mm\\nGAP 3mm,0\\n@1=\"0001\"\\nDIRECTION "+order+"\\nCLS";
//                                String strin2 = "\\nDENSITY 15\\nTEXT "+custom_entty.getShopY()+","+custom_entty.getShopX()+",\"TSS24.BF2\",0,1,2,\"" + SharedUtil.getString("name") + "\"" + "\\r\\n";
//                                String strin3 = "\\nDENSITY 15\\nTEXT "+custom_entty.getNameY()+","+custom_entty.getNameX()+",\"TSS24.BF2\",0,1,2,\"" + commodity.getName() + "\"" + "\\r\\n";
//                                String strin4 = "\\nDENSITY 15\\nTEXT "+custom_entty.getPriceY()+","+custom_entty.getPriceX()+",\"TSS24.BF2\",0,3,3,\"" + Double.parseDouble(commodity.getPrice()) + "\"" + "\\r\\n";
//                                String strinm4 = "\\nDENSITY 15\\nTEXT "+custom_entty.getMemberpriceY()+","+custom_entty.getMemberpriceX()+",\"TSS24.BF2\",0,2,2,\"" + Double.parseDouble(commodity.getMember_price()) + "\"" + "\\r\\n";
//                                String strin5 = "\\nDENSITY 15\\nTEXT "+custom_entty.getSpecificationsY()+","+custom_entty.getSpecificationsX()+",\"TSS24.BF2\",0,1,1,\"" + commodity.getSpecification() + "\"" + "\\r\\n";
//                                String strin6 = "\\nDENSITY 15\\nTEXT "+custom_entty.getCompanyY()+","+custom_entty.getCompanyX()+",\"TSS24.BF2\",0,1,1,\"" + commodity.getUnit() + "\"" + "\\r\\n";
//                                String strin7 = "\\nDENSITY 3\\nHOME\\nBARCODE "+custom_entty.getCodeY()+","+custom_entty.getCodeX()+",\"128\",50,1,0,2,4,\"" + commodity.getBncode() + "\"" + "\\nPRINT 1,1";
////                                prt.printString(strin1, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//                                strings1.add(strin1);
//                                if (custom_entty.getShopY()!=null){
//                                    if (!custom_entty.getShopY().equals("")){
////                                        prt.printString(strin2, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strin2);
//                                    }
//                                }
//                                if (custom_entty.getNameY()!=null){
//                                    if (!custom_entty.getNameY().equals("")){
////                                        prt.printString(strin3, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strin3);
//                                    }
//                                }
//                                if (custom_entty.getPriceY()!=null){
//                                    if (!custom_entty.getPriceY().equals("")){
////                                        prt.printString(strin4, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strin4);
//                                    }
//                                }
//                                if (custom_entty.getMemberpriceY()!=null){
//                                    if (!custom_entty.getMemberpriceY().equals("")){
////                                        prt.printString(strinm4, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strinm4);
//                                    }
//                                }
//                                if (custom_entty.getSpecificationsY()!=null){
//                                    if (!custom_entty.getSpecificationsY().equals("")){
////                                        prt.printString(strin5, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strin5);
//                                    }
//                                }
//                                if (custom_entty.getCompanyY()!=null){
//                                    if (!custom_entty.getCompanyY().equals("")){
////                                        prt.printString(strin6, font, bold, underlined, doubleHeight,
////                                                doubleWidth);
//                                        strings1.add(strin6);
//                                    }
//                                }
////                                prt.printString(strin7, font, bold, underlined, doubleHeight,
////                                        doubleWidth);
//                                strings1.add(strin7);
//                                setPrint(prt,strings1);
//                                break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //查询商品库是否有该商品
    public void Seekbank(final String bncode){
        OkGo.post(SysUtils.getGoodsServiceUrl("getWarehouseByBncode"))
                .tag("order_status")
                .params("bncode",bncode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print","打印商品库的数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject object=jsonObject.getJSONObject("response");
                            if(object.getString("status").equals("200")){
                                JSONObject json=object.getJSONObject("data");
                                ed_name.setText(json.getString("name"));
                                ed_price.setText(json.getString("price"));
                            }else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 设置打印
     * @param print
     * @param str
     */
    public void setPrint(UsbPrinter print,List<String> str){
        if (print!=null){
            String string="";
            String command = "";
            for (int i=0;i<str.size();i++){
                string=string+str.get(i);
            }
            try {
            byte[] b = EscposUtil.convertEscposToBinary(string);
            if(b != null) print.write(b);

            command = String.format("'%s' LF", string );
            b = EscposUtil.convertEscposToBinary(command);
            if(b != null)
                print.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //设置打印标签距离
    public void setdistance(){
        if (SharedUtil.getString("custom")!=null){
            if (!SharedUtil.getString("custom").equals("")) {
                try {
                    custom_entty = new Custom_Entty();
                    JSONObject jsonObject = new JSONObject(SharedUtil.getString("custom"));
                    custom_entty.setCodeX(jsonObject.getString("codeX"));
                    custom_entty.setCodeY(jsonObject.getString("codeY"));
                    custom_entty.setCompanyX(jsonObject.getString("companyX"));
                    custom_entty.setCompanyY(jsonObject.getString("companyY"));
                    custom_entty.setCompanysize(jsonObject.getInt("companysize"));
                    custom_entty.setNameX(jsonObject.getString("nameX"));
                    custom_entty.setNameY(jsonObject.getString("nameY"));
                    custom_entty.setNamesize(jsonObject.getInt("namesize"));
                    custom_entty.setPriceX(jsonObject.getString("priceX"));
                    custom_entty.setPriceY(jsonObject.getString("priceY"));
                    custom_entty.setPricesize(jsonObject.getInt("pricesize"));
                    custom_entty.setShopX(jsonObject.getString("shopX"));
                    custom_entty.setShopY(jsonObject.getString("shopY"));
                    custom_entty.setShopsize(jsonObject.getInt("shopsize"));
                    custom_entty.setSize(jsonObject.getString("size"));
                    custom_entty.setMemberpriceX(jsonObject.getString("memberpriceX"));
                    custom_entty.setMemberpriceY(jsonObject.getString("memberpriceY"));
                    custom_entty.setMemberpricesize(jsonObject.getInt("memberpricesize"));
                    custom_entty.setSpecificationsX(jsonObject.getString("specificationsX"));
                    custom_entty.setSpecificationsY(jsonObject.getString("specificationsY"));
                    custom_entty.setSpecificationssize(jsonObject.getInt("specificationssize"));
                    custom_entty.setNameproduce_addrX(jsonObject.getString("nameproduce_addrX"));
                    custom_entty.setNameproduce_addrY(jsonObject.getString("nameproduce_addrY"));
                    custom_entty.setNameproduce_addrsize(jsonObject.getInt("nameproduce_addrsize"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    PopupWindow popupWindow;
    ListView popListView;
    Spadapter spadapter;
    List<Fenlei_Entty> listFenlei=new ArrayList<>();
    List<Fenlei_Entty> adats=new ArrayList<>();
    public void ShowPop(List<Fenlei_Entty> adats,View view,String type){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
        View popView=View.inflate(this,R.layout.pop_listview,null);

        /**

         * 第一个参数：View contenView（布局）

         * 第二个参数：int width（宽度）

         * 第三个参数：int height（高度）

         *      宽高参数：-2 和 ViewGroup.LayoutParams.WRAP_CONTENT 一样

         *                -1 和 ViewGroup.LayoutParams.MATCH_PARENT 一样

         *

         * 三个参数缺少任意一个都不可能弹出来PopWindow；

         *

         */

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //popupWindow是否响应touch事件

        popupWindow.setTouchable(true);

        //popupWindow是否具有获取焦点的能力

        popupWindow.setFocusable(false);

        //这个方法是重中之重，不仅仅是设置背景,不设置背景上面两行代码无效

        popupWindow.setBackgroundDrawable(null);

        popListView= (ListView) popView.findViewById(R.id.listview);

        //这里有个小坑，代码设置分割线，必须先设置颜色，再设置高度，不然不生效

        popListView.setDivider(new ColorDrawable(Color.WHITE));

        popListView.setDividerHeight(1);

        spadapter.setAdats(adats);
        spadapter.setType(type);
        popListView.setAdapter(spadapter);

        /**

         * 第一个参数：显示在ｉｖ布局下面

         * 第二个参数：xoff表示x轴的偏移，正值表示向左，负值表示向右；

         * 第三个参数：yoff表示相对y轴的偏移，正值是向下，负值是向上；

         */

        popupWindow.showAsDropDown(view,80,-33);
    }

    /**
     * 判断点击popupwindow外消失
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        return super.onTouchEvent(event);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    //获取分类的
    public void Showunit(){
        OkGo.post(SysUtils.getGoodsServiceUrl("cat_getlist"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("seller_id", SharedUtil.getString("seller_id"))
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = new JSONObject(s);
                            JSONObject j1 = jsonobject.getJSONObject("response");
                            JSONObject j2 = j1.getJSONObject("data");
                            JSONArray ja = j2.getJSONArray("nav_info");
                            listFenlei.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                GridView_xuangzhong xuangzhong = new GridView_xuangzhong();
                                Fenlei_Entty fenlei = new Fenlei_Entty();
                                JSONObject jo = ja.getJSONObject(i);
                                fenlei.setName(jo.getString("tag_name"));
                                xuangzhong.setCategory(jo.getString("tag_name"));
                                fenlei.setTag_id(jo.getInt("tag_id"));
                                listFenlei.add(fenlei);
                            }
                            if (commodity==null){
                                if (listFenlei.size()>0){
                                    tv_category.setText(listFenlei.get(0).getName());
                                    tag_id=listFenlei.get(0).getTag_id()+"";
                                }
                            }else {
                                if (listFenlei.size()>0){
                                    if (commodity.getGoods_id()==null||commodity.getGoods_id().equals("")||commodity.getGoods_id().equals("null")){
                                        tv_category.setText(listFenlei.get(0).getName());
                                        tag_id=listFenlei.get(0).getTag_id()+"";
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
    }

    //获取单位
    public void ShowTag(){
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
                            if (commodity==null){
                                if (adats.size()>0){
                                    tv_unit.setText(adats.get(0).getName());
                                    unit_id=adats.get(0).getTag_id()+"";
                                }
                            }else {
                                if (adats.size()>0){
                                    if (commodity.getGoods_id()==null||commodity.getGoods_id().equals("")||commodity.getGoods_id().equals("null")){
                                        tv_unit.setText(adats.get(0).getName());
                                        unit_id=adats.get(0).getTag_id()+"";
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void Split_open_a_case(String nums,final Dialog dialog1){
        OkGo.post(SysUtils.getGoodsServiceUrl("open_box"))
                .tag(this)
                .params("box_id",commodity.getGoods_id())
                .params("box_nums",nums)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("","拆箱的"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo=jsonObject.getJSONObject("response");
                            String status=jo.getString("status");
                            String data=jo.getString("data");
                            if (status.equals("200")){
                                if (dialog1!=null){
                                    dialog1.dismiss();
                                }
                                if (NoDoubleClickUtils.isSoftShowing(Addgoodgs_Activity.this)) {
                                    imm = (InputMethodManager) Addgoodgs_Activity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                }
                                but_devanning.setText("拆箱");
                            }
                            Toast.makeText(Addgoodgs_Activity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    //开启拆箱
    public void opendisable(){
        final Dialog dialog1 = new Dialog(this);
        dialog1.setTitle("拆箱");
        dialog1.show();
        Window window1 = dialog1.getWindow();
        window1.setContentView(R.layout.dialog_disable);
        final EditText ed_add= (EditText) window1.findViewById(R.id.ed_add);
//        ed_code=ed_add.getText().toString();

        ed_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("print","打印的数据为"+editable.toString());

            }
        });

        Button but_add= (Button) window1.findViewById(R.id.but_add);
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("print","条码为"+ed_add.getText().toString());
                getAdatname(ed_add.getText().toString(),dialog1);
            }
        });
//

    }

    public void getAdatname(final String code, final Dialog dialog1){
        sqLiteDatabase = sqliteHelper.getReadableDatabase();
//        Log.e("print","条码为"+ed_code);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where bncode=?", new String[]{code});
        if (cursor.getCount()!=0){
            dialog1.dismiss();
        while (cursor.moveToNext()) {
            disable_name=cursor.getString(cursor.getColumnIndex("name"));
            }
            final Dialog dialog = new Dialog(this);
            dialog.show();
            dialog.setTitle("输入拆箱数");
            Window window1 = dialog.getWindow();
            window1.setContentView(R.layout.dialog_disable);
            TextView tv_name= (TextView) window1.findViewById(R.id.tv_name);
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(disable_name);
            final EditText ed_add= (EditText) window1.findViewById(R.id.ed_add);
            ed_add.setHint("请输入一箱的数量");
            Button but_add= (Button) window1.findViewById(R.id.but_add);
            but_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(ed_add.getText().toString())){
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("box_id",commodity.getGoods_id());
                        map.put("goods_code",code);
                        map.put("nums",ed_add.getText().toString());
                        Gson gson=new Gson();
                        String str=gson.toJson(map);
                        up_disable(str,dialog);
                    }
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Inputmethod_Utils.getshow(Addgoodgs_Activity.this);
                }
            });

        }

    }

    public void up_disable(String map,final Dialog dialog){
        OkGo.post(SysUtils.getGoodsServiceUrl("box_on"))
                .tag(this)
                .params("map",map)
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        Log.d("print接口的数据",""+request.getParams().toString());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {


                            Log.d("print","拆箱数据"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jo=jsonObject.getJSONObject("response");
                            String status=jo.getString("status");
                            String data=jo.getString("data");
                            if (status.equals("200")){
                                dialog.dismiss();
                                but_devanning.setText("拆箱");
                            }
                            Toast.makeText(Addgoodgs_Activity.this,data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == 202) {
            tag_id = data.getStringExtra("tag_id");
            tag_name = data.getStringExtra("name");
            tv_category.setText(tag_name);
            Log.d("print", "tag_id" + tag_id);
        }
        if (requestCode == 203 && resultCode == 204) {
            unit_id = String.valueOf(data.getIntExtra("unit_id", 0));
            if (unit_id.equals("0")){
                unit_id="";
            }
            unit = data.getStringExtra("unit");
            tv_unit.setText(unit);
        }
        if (requestCode == 205 && resultCode == 206) {
            lable_name = data.getStringExtra("lable_name");
            lable_id = data.getStringExtra("lable_id");
            tv_lable.setText(lable_name);
        }
        if (requestCode ==207 && resultCode ==208){
            provider_name = data.getStringExtra("lable_name");
            provider_id = data.getStringExtra("lable_id");
            Log.e("provider_id",provider_id);
            tv_provider.setText(provider_name);
        }
    }

    /**
     * 退货的方法
     * @param
     */
    public void showreturn(final int unms) {
        final Dialog dialog = new Dialog(Addgoodgs_Activity.this);
        dialog.setTitle("退货详情");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.refund_layout);
        RelativeLayout Rl_number = (RelativeLayout) window.findViewById(R.id.Rl_number);
        Rl_number.setVisibility(View.GONE);
        final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_describe.getText().toString().equals("")) {
                    Double sums = Double.valueOf(0);
                    Double profit = Double.valueOf(0);
                    Double profits = Double.valueOf(0);
                    List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", commodity.getName());
                    map.put("nums", unms + "");
                    map.put("price", StringUtils.stringpointtwo(Float.valueOf(commodity.getCost()) + ""));
                    profit = TlossUtils.mul(Double.parseDouble(commodity.getCost()), Double.parseDouble(unms + ""));
                    map.put("sum", profit + "");
                    map.put("goods_id", commodity.getGoods_id());
                    listmap.add(map);
                    Gson gson = new Gson();
                    String str = gson.toJson(listmap);
                    Log.d("print", "退货的数据" + str);
                    OkGo.post(SysUtils.getSellerServiceUrl("back_goods"))
                            .tag(this)
                            .cacheKey("cacheKey")
                            .cacheMode(CacheMode.DEFAULT)
                            .params("operator", SharedUtil.getString("operator_id"))
                            .params("memo", ed_describe.getText().toString())
                            .params("items", str)
                            .params("sums", sums)
                            .params("type", "seller_back")
                            .params("work_id", SharedUtil.getString("work_id"))
                            .params("cost_sums", profit)
                            .params("worker_name", SharedUtil.getString("name"))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d("print", "退货的" + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONObject jo1 = jsonObject.getJSONObject("response");
                                        String status = jo1.getString("status");
                                        String message = jo1.getString("message");
                                        if (status.equals("200")) {
                                            sqLiteDatabase = sqliteHelper.getReadableDatabase();
                                            Cursor cursor = sqLiteDatabase.rawQuery("select * from commodity where goods_id=?", new String[]{commodity.getGoods_id()});
                                            while (cursor.moveToNext()) {
                                                String nums = cursor.getString(cursor.getColumnIndex("store"));
                                                String newnums = (Integer.parseInt(nums) - unms) + "";
                                                ContentValues values = new ContentValues();
                                                values.put("store", newnums);
                                                sqLiteDatabase.update("commodity", values, "goods_id=?", new String[]{commodity.getGoods_id()});
                                            }

                                            dialog.dismiss();
                                            but_baochun.performClick();
                                            Toast.makeText(Addgoodgs_Activity.this, "退货成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Addgoodgs_Activity.this, message, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Inputmethod_Utils.getshow(getApplication());
                        }
                    });
                } else {
                    Toast.makeText(Addgoodgs_Activity.this, "请输入备注", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getBn() {
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
                            tv_bncode.setText(code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //扫条形码
    @Override
    public void onScanSuccess(String barcode) {
        if (barcode.length() > 14) {
            barcode = barcode.substring(0, 13);
            Log.e("", "得到得商品是" + barcode.substring(13));
            tv_bncode.setText(barcode);
        }

    }

    //获取时间 yyyy-MM-dd  HH:mm
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public String getTime1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public void getsensitivity(String stringcontext) {
        Log.e("print", "敏感操作数据" + stringcontext);
        OkGo.post(SysUtils.getSellerServiceUrl("log_insert"))
                .tag(this)
                .params("map", stringcontext)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", "敏感操作的数据" + s);

                    }
                });
    }

}
