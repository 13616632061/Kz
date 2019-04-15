package retail.yzx.com.kz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Newitemadd_Entty;
import Utils.DateUtils;
import Utils.Inputmethod_Utils;
import Utils.ScanGunKeyEventHelper;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TimeZoneUtil;
import Utils.TlossUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;
import shujudb.SqliteHelper;
import widget.Switch;

/**
 * Created by admin on 2017/4/10.
 */
public class ItemAdd_Activity extends BaseActivity implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener {
    public ImageView im_huanghui;
    public RelativeLayout rl_unit, rl_danwei, rl_label,rl_provider;
    //    分类
    public TextView tv_shenyu;
    public EditText tv_bncode;
    public TextView tv_category, tv_lirun, tv_unit, tv_data, tv_day, tv_shenyuday, tv_lable,tv_provider;
    public EditText ed_name, ed_cost, ed_price, ed_store, ed_good_limit, ed_good_stock, ed_py, ed_baozhi, ed_describe, tv_specification,ed_Member;
    public Newitemadd_Entty newitemadd_entty;
    public widget.Switch sw1, sw2,sw4;
    public Button but_baochun, but_delete;
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


    @Override
    protected int getContentId() {
        return R.layout.itemadd_activitty;
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

        ItemAdd_Activity.this.setFinishOnTouchOutside(false);

        ed_Member= (EditText) findViewById(R.id.ed_Member);

        sqliteHelper = new SqliteHelper(this);
        sqLiteDatabase = sqliteHelper.getReadableDatabase();

        scanGunKeyEventHelper = new ScanGunKeyEventHelper(ItemAdd_Activity.this);

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
        im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);

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
        ed_store = (EditText) findViewById(R.id.ed_store);
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

                if (!ed_baozhi.getText().toString().equals("") && StringUtils.isNumber(ed_baozhi.getText().toString())) {
                    DateUtils.gettime(Integer.valueOf(ed_baozhi.getText().toString()));
                    tv_day.setText(DateUtils.gettime(Integer.valueOf(ed_baozhi.getText().toString())));
                }
            }
        });

//        tv_shenyuday= (TextView) findViewById(R.id.tv_shenyuday);
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

        sw1 = (widget.Switch) findViewById(R.id.sw1);
        sw2 = (widget.Switch) findViewById(R.id.sw2);
        sw4 = (widget.Switch) findViewById(R.id.sw4);
//        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    iskuaijiejian = 1;
//                } else {
//                    iskuaijiejian = 0;
//                }
//            }
//        });
        sw1.setOnCheckedChangeListener(new widget.Switch.OnCheckedChangeListener() {
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

//        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    isup = true;
//                } else {
//                    isup = false;
//                }
//            }
//        });

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

        Intent intent = getIntent();
        if (intent.getSerializableExtra("commodity") != null) {

            commodity = (Commodity) intent.getSerializableExtra("commodity");
            Log.d("print", "分类是" + commodity);
            if (commodity.isBox_disable()){
                but_devanning.setText("拆箱");
            }else {
                but_devanning.setText("开启拆箱");
            }

            if (commodity.getAuth().equals("open")){
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

            }else if (commodity.getAuth().equals("close")){

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

            if (commodity.getCook_position()!=null&&!commodity.getCook_position().equals("null")){
                if (Integer.valueOf(commodity.getCook_position()) == 0) {
                    sw4.setChecked(false);
                    Cook = "0";
                    Log.d("print","数据为编辑"+Cook);
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
//            ed_Member.setSelection(commodity.getMember_price().length());//将光标移至文字末尾
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
            provider_id=commodity.getProvider_id();
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
                    }
                    tv_lable.setText(label);
                }
            }
            if (!commodity.getGD().equals("null")) {
                tv_data.setText(TimeZoneUtil.getTime(1000 * Long.valueOf(commodity.getGD())));
            }

            Log.d("print","Day"+commodity.getPD());
            if (!commodity.getPD().equals("null")){
                ed_baozhi.setText(commodity.getPD());
            }
//            tv_day.setText(TimeZoneUtil.getTime(1000*Long.valueOf(commodity.getGD())));

            url = SysUtils.getGoodsServiceUrl("goodsToAdd") + "&edit=edit&goods_id=" + commodity.getGoods_id() + "product_id=" + commodity.getProduct_id();
            Title = "确定修改";
            but_supplement.setVisibility(View.VISIBLE);
        } else {
            but_devanning.setText("开启拆箱");
            but_supplement.setVisibility(View.GONE);
            sw2.setChecked(isup);
            but_delete.setVisibility(View.GONE);
            url = SysUtils.getGoodsServiceUrl("goodsToAdd");
            Title = "确定保存";
        }


//        保存商品的监听
        but_baochun = (Button) findViewById(R.id.but_baochun);
        but_baochun.setOnClickListener(this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.itemadd_activitty);
//        init1();
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(ItemAdd_Activity.this);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqliteHelper.close();
        sqLiteDatabase.close();
    }

    //    @Override
//    protected void onRestart() {
//        super.onRestart();
//        tv_bncode.setText(newitemadd_entty.getBncode());
//        ed_name.setText(newitemadd_entty.getName());
//        ed_cost.setText(newitemadd_entty.getCost());
//        tv_lirun.setText(newitemadd_entty.getLirun());
//        ed_price.setText(newitemadd_entty.getPrice());
//        ed_store.setText(newitemadd_entty.getStore());
//        ed_good_limit.setText(newitemadd_entty.getGood_limit());
//        ed_good_stock.setText(newitemadd_entty.getGood_stock());
//        tv_unit.setText(newitemadd_entty.getUnit());
//        ed_py.setText(newitemadd_entty.getPy());
//        tv_data.setText(newitemadd_entty.getData());
//        tv_day.setText(newitemadd_entty.getDay());
//        ed_baozhi.setText(newitemadd_entty.getBaozhi());
//        tv_shenyuday.setText(newitemadd_entty.getShenyuday());
//        ed_describe.setText(newitemadd_entty.getDescribe());
//
//
//    }
//
//    //    保存信息
//    @Override
//    protected void onStop() {
//        super.onStop();
//        newitemadd_entty.setBncode(tv_bncode.getText().toString());
//        newitemadd_entty.setName(ed_name.getText().toString());
//        newitemadd_entty.setCost(ed_cost.getText().toString());
//        newitemadd_entty.setLirun(tv_lirun.getText().toString());
//        newitemadd_entty.setPrice(ed_price.getText().toString());
//        newitemadd_entty.setStore(ed_store.getText().toString());
//        newitemadd_entty.setGood_limit(ed_good_limit.getText().toString());
//        newitemadd_entty.setGood_stock(ed_good_stock.getText().toString());
//        newitemadd_entty.setUnit(tv_unit.getText().toString());
//        newitemadd_entty.setPy(ed_py.getText().toString());
//        newitemadd_entty.setData(tv_data.getText().toString());
//        newitemadd_entty.setDay(tv_day.getText().toString());
//        newitemadd_entty.setBaozhi(ed_baozhi.getText().toString());
//        newitemadd_entty.setShenyuday(tv_shenyuday.getText().toString());
//        newitemadd_entty.setDescribe(ed_describe.getText().toString());
//
//
//    }

//    private void init1() {
//        sqliteHelper = new SqliteHelper(this);
//        sqLiteDatabase = sqliteHelper.getReadableDatabase();
//
//        scanGunKeyEventHelper = new ScanGunKeyEventHelper(ItemAdd_Activity.this);
//
//        but_return = (Button) findViewById(R.id.but_return);
//        but_return.setOnClickListener(this);
//
//        but_devanning= (Button) findViewById(R.id.but_devanning);
//        but_devanning.setOnClickListener(this);
//
//        but_supplement = (Button) findViewById(R.id.but_supplement);
//        but_supplement.setOnClickListener(this);
//
//        im_data = (ImageView) findViewById(R.id.im_data);
//        im_data.setOnClickListener(this);
//
//        tv_biaoti = (TextView) findViewById(R.id.tv_biaoti);
//
//        but_create = (Button) findViewById(R.id.but_create);
//        but_create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getBn();
//            }
//        });
//        tv_category = (TextView) findViewById(R.id.tv_category);
//        tv_unit = (TextView) findViewById(R.id.tv_unit);
//
//        newitemadd_entty = new Newitemadd_Entty();
//        im_huanghui = (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//
//        rl_provider = (RelativeLayout) findViewById(R.id.rl_provider);
//        rl_provider.setOnClickListener(this);
//
//        rl_unit = (RelativeLayout) findViewById(R.id.rl_unit);
//        rl_unit.setOnClickListener(this);
//
//        rl_danwei = (RelativeLayout) findViewById(R.id.rl_danwei);
//        rl_danwei.setOnClickListener(this);
//
//        rl_label = (RelativeLayout) findViewById(R.id.rl_label);
//        rl_label.setOnClickListener(this);
//
//        tv_lable = (TextView) findViewById(R.id.tv_lable);
//        tv_provider = (TextView) findViewById(R.id.tv_provider);
//
//
//        tv_bncode = (EditText) findViewById(R.id.tv_bncode);
//        ed_name = (EditText) findViewById(R.id.ed_name);
//        ed_py = (EditText) findViewById(R.id.ed_py);
//        ed_name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
////                ed_py.setText(Pinyin.getShortString(ed_name.getText().toString()+""));
//
//                try {
//                    String py = PinyinHelper.getShortPinyin(ed_name.getText().toString());
//                    ed_py.setText(py.toUpperCase());
//                } catch (PinyinException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        ed_cost = (EditText) findViewById(R.id.ed_cost);
//        tv_lirun = (TextView) findViewById(R.id.tv_lirun);
//        ed_price = (EditText) findViewById(R.id.ed_price);
//        ed_price.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!ed_price.getText().toString().isEmpty() && !ed_cost.getText().toString().isEmpty()) {
//                    tv_lirun.setText(TlossUtils.sub(Double.parseDouble(ed_price.getText().toString()), Double.parseDouble(ed_cost.getText().toString())) + "");
//                }
//            }
//        });
//        ed_store = (EditText) findViewById(R.id.ed_store);
//        ed_store = (EditText) findViewById(R.id.ed_store);
//        ed_store = (EditText) findViewById(R.id.ed_store);
//        ed_good_limit = (EditText) findViewById(R.id.ed_good_limit);
//        ed_good_stock = (EditText) findViewById(R.id.ed_good_stock);
//
//
//        tv_data = (TextView) findViewById(R.id.tv_data);
//        //到期日期
//        tv_day = (TextView) findViewById(R.id.tv_day);
//        ed_baozhi = (EditText) findViewById(R.id.ed_baozhi);
//        ed_baozhi.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (!ed_baozhi.getText().toString().equals("") && StringUtils.isNumber(ed_baozhi.getText().toString())) {
//                    DateUtils.gettime(Integer.valueOf(ed_baozhi.getText().toString()));
//                    tv_day.setText(DateUtils.gettime(Integer.valueOf(ed_baozhi.getText().toString())));
//                }
//            }
//        });
//
////        tv_shenyuday= (TextView) findViewById(R.id.tv_shenyuday);
//        ed_describe = (EditText) findViewById(R.id.ed_describe);
//        tv_specification = (EditText) findViewById(R.id.tv_specification);
//        tv_shenyu = (TextView) findViewById(R.id.tv_shenyu);
//        ed_describe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
//        ed_describe.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (ed_describe.getText().length() < 50) {
//                    tv_shenyu.setText("剩余" + (50 - ed_describe.getText().length()) + "字");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//        but_delete = (Button) findViewById(R.id.but_delete);
//        but_delete.setOnClickListener(this);
//
//        sw1 = (widget.Switch) findViewById(R.id.sw1);
//        sw2 = (widget.Switch) findViewById(R.id.sw2);
//        sw4 = (widget.Switch) findViewById(R.id.sw4);
////        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                if (b) {
////                    iskuaijiejian = 1;
////                } else {
////                    iskuaijiejian = 0;
////                }
////            }
////        });
//        sw1.setOnCheckedChangeListener(new widget.Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                if (isChecked) {
//                    iskuaijiejian = 1;
//                } else {
//                    iskuaijiejian = 0;
//                }
//            }
//        });
//
//        sw4.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                if (isChecked){
//                    Cook="1";
//                }else {
//                    Cook="";
//                }
//            }
//        });
//
////        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                if (b) {
////                    isup = true;
////                } else {
////                    isup = false;
////                }
////            }
////        });
//
//        sw2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                if (isChecked) {
//                    isup = true;
//                } else {
//                    isup = false;
//                }
//            }
//        });
//
//        Intent intent = getIntent();
//        if (intent.getSerializableExtra("commodity") != null) {
//
//            commodity = (Commodity) intent.getSerializableExtra("commodity");
//            Log.d("print", "分类是" + commodity);
//            if (commodity.isBox_disable()){
//                but_devanning.setText("拆箱");
//            }else {
//                but_devanning.setText("开启拆箱");
//            }
//
//            if (commodity.getAuth().equals("open")){
//
//                but_create.setOnClickListener(null);
//                but_supplement.setOnClickListener(this);
//
//                //设置为不可编辑
//                tv_bncode.setFocusable(false);
//                tv_bncode.setFocusableInTouchMode(false);
//
//                //设置可以编辑
//                ed_store.setFocusableInTouchMode(true);
//                ed_store.setFocusable(true);
//                ed_store.requestFocus();
//
//                //设置可以编辑
//                ed_good_limit.setFocusableInTouchMode(true);
//                ed_good_limit.setFocusable(true);
//                ed_good_limit.requestFocus();
//
//                //设置可以编辑
//                ed_good_stock.setFocusableInTouchMode(true);
//                ed_good_stock.setFocusable(true);
//                ed_good_stock.requestFocus();
//
//            }else if (commodity.getAuth().equals("close")){
//
//                but_create.setOnClickListener(null);
//                but_supplement.setOnClickListener(null);
//
//                //设置为不可编辑
//                tv_bncode.setFocusable(false);
//                tv_bncode.setFocusableInTouchMode(false);
//
//                ed_store.setFocusable(false);
//                ed_store.setFocusableInTouchMode(false);
//
//                ed_good_limit.setFocusable(false);
//                ed_good_limit.setFocusableInTouchMode(false);
//
//                ed_good_stock.setFocusable(false);
//                ed_good_stock.setFocusableInTouchMode(false);
//
//            }
//
//
//
//            tv_bncode.setText(commodity.getBncode());
//            if (Integer.valueOf(commodity.getAltc()) == 0) {
//                sw1.setChecked(false);
//                iskuaijiejian = 0;
//            } else {
//                sw1.setChecked(true);
//                iskuaijiejian = 1;
//            }
//
//            if (commodity.getCook_position()!=null&&!commodity.getCook_position().equals("null")){
//                if (Integer.valueOf(commodity.getCook_position()) == 0) {
//                    sw4.setChecked(false);
//                    Cook = "";
//                    Log.d("print","数据为编辑"+Cook);
//                } else {
//                    sw4.setChecked(true);
//                    Cook = commodity.getCook_position();
//                }
//            }
//
//
//            tv_biaoti.setText("编辑商品");
//            isup = Boolean.parseBoolean(commodity.getMarketable());
//            sw2.setChecked(Boolean.parseBoolean(commodity.getMarketable()));
//            ed_name.setText(commodity.getName());
//            ed_name.setSelection(commodity.getName().length());//将光标移至文字末尾
//            ed_cost.setText(StringUtils.stringpointtwo(commodity.getCost()));
//            ed_cost.setSelection(StringUtils.stringpointtwo(commodity.getCost()).length());//将光标移至文字末尾
//            ed_price.setText(StringUtils.stringpointtwo(commodity.getPrice()));
//            ed_price.setSelection(StringUtils.stringpointtwo(commodity.getPrice()).length());//将光标移至文字末尾
//            tv_bncode.setText(commodity.getBncode());
//            ed_good_limit.setText(commodity.getGood_limit());
//            ed_good_limit.setSelection(commodity.getGood_limit().length());//将光标移至文字末尾
//            ed_describe.setText(commodity.getGood_remark());
////            ed_describe.setSelection(commodity.getGood_remark().length());//将光标移至文字末尾
//            Log.d("print", "分类是" + commodity.getTag_name());
//            tv_category.setText(commodity.getTag_name());
//
//            if (commodity.getUnit().equals("null")) {
//                tv_unit.setText("");
//            } else {
//                tv_unit.setText(commodity.getUnit());
//            }
//
//            tv_provider.setText(commodity.getProvider_name());
//            provider_id=commodity.getProvider_id();
//            ed_store.setText(commodity.getStore());
//            ed_good_limit.setText(commodity.getGood_limit());
//            ed_good_stock.setText(commodity.getGood_stock());
//            tv_specification.setText(commodity.getSpecification());
//            if (commodity.getPy().equals("null")) {
//                ed_py.setText("");
//            } else {
//                ed_py.setText(commodity.getPy());
//            }
//            String label = "";
//            if (commodity.getAdats() != null) {
//                if (commodity.getAdats().size() > 0) {
//                    for (int i = 0; i < commodity.getAdats().size(); i++) {
//                        label += commodity.getAdats().get(i).getLabel_name();
//                    }
//                    tv_lable.setText(label);
//                }
//            }
//            if (!commodity.getPD().equals("null")) {
//                tv_data.setText(TimeZoneUtil.getTime(1000 * Long.valueOf(commodity.getPD())));
//            }
////            tv_day.setText(TimeZoneUtil.getTime(1000*Long.valueOf(commodity.getGD())));
//            url = SysUtils.getGoodsServiceUrl("goodsToAdd") + "&edit=edit&goods_id=" + commodity.getGoods_id() + "product_id=" + commodity.getProduct_id();
//            Title = "确定修改";
//            but_supplement.setVisibility(View.VISIBLE);
//        } else {
//            but_devanning.setText("开启拆箱");
//            but_supplement.setVisibility(View.GONE);
//            sw2.setChecked(isup);
//            but_delete.setVisibility(View.GONE);
//            url = SysUtils.getGoodsServiceUrl("goodsToAdd");
//            Title = "确定保存";
//        }
//
//
////        保存商品的监听
//        but_baochun = (Button) findViewById(R.id.but_baochun);
//        but_baochun.setOnClickListener(this);
////        选中分类的名字和id
////        Intent intent=getIntent();
////        tag_id=intent.getStringExtra("tag_id");
////        name=intent.getStringExtra("name");
////        Log.d("print","name"+name);
////        tv_category.setText(name);
//
//
//    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(ItemAdd_Activity.this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_devanning:
                if (but_devanning.getText().toString().equals("拆箱")){
                    final Dialog dialog1 = new Dialog(this);
                    dialog1.setTitle("拆箱");
                    dialog1.show();
                    Window window1 = dialog1.getWindow();
                    window1.setContentView(R.layout.dialog_disable);
                    final EditText ed_add= (EditText) window1.findViewById(R.id.ed_add);
                    ed_add.setHint("请输入拆箱数");
                    Button but_add= (Button) window1.findViewById(R.id.but_add);
                    but_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(ed_add.getText().toString())){
                                Log.d("print","拆箱的数据为"+ed_add.getText().toString());
                                Split_open_a_case(ed_add.getText().toString(),dialog1);
                            }
                        }
                    });
                    dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Inputmethod_Utils.getshow(ItemAdd_Activity.this);
                        }
                    });
                }else {
                    opendisable();
                }
                break;
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.rl_unit:
                Intent intent = new Intent(ItemAdd_Activity.this, Category_activity.class);
                intent.putExtra("com.yzx.value", "value");
                startActivityForResult(intent, 201);
                break;
            case R.id.rl_danwei:
                Intent intent1 = new Intent(ItemAdd_Activity.this, Unit_activity.class);
                intent1.putExtra("com.yzx.value", "value");
                startActivityForResult(intent1, 203);
                break;
            case R.id.rl_label:
                Intent intent3 = new Intent(ItemAdd_Activity.this, Label_activity.class);
                intent3.putExtra("com.yzx.value", "value");
                startActivityForResult(intent3, 205);
                break;
            case R.id.but_baochun:
                if (ed_name.getText().toString().equals("")) {
                    Toast.makeText(ItemAdd_Activity.this, "商品名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                else if (ed_cost.getText().toString().equals("")) {
//                    Toast.makeText(ItemAdd_Activity.this, "进货价不能为空", Toast.LENGTH_SHORT).show();
//                }
                if (ed_price.getText().toString().equals("")) {
                    Toast.makeText(ItemAdd_Activity.this, "售价不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    if (ed_Member.getText().toString().equals("")){
//                        ed_Member.setText(ed_price.getText().toString());
//                    }
                }

                if (tv_category.getText().toString().equals("")) {
                    Toast.makeText(ItemAdd_Activity.this, "分类不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
//                else if (ed_store.getText().toString().equals("")) {
//                    Toast.makeText(ItemAdd_Activity.this, "库存不能为空", Toast.LENGTH_SHORT).show();
//                }
                else {
                    if (but_delete.getVisibility() == View.VISIBLE) {
                        if (commodity != null && tag_id.equals("")) {
                            tag_id = commodity.getTag_id();
                        }
                        if (commodity.getAdats() != null && lable_id.equals("")) {
                            for (int i = 0; i < commodity.getAdats().size(); i++) {
                                Map<String, String> map1 = new HashMap<>();
                                lable_id += commodity.getAdats().get(i).getLabel_name() + " ";
                                lable_id += commodity.getAdats().get(i).getLabel_id() + "";
                                mapList.add(map1);
                            }
                            Gson gson = new Gson();
                            lable_id = gson.toJson(mapList);
                        }
                        Log.d("print", "标签" + lable_id);
//                        Log.d("print", "标签" + unit_id);
                        tv_biaoti.setText("编辑商品");
                        sensitivity_price = Float.parseFloat(commodity.getCost());
                        sensitivity_selling = Float.parseFloat(commodity.getPrice());
                        sensitivity_store = Float.parseFloat(commodity.getStore());
                        but_supplement.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(this).setTitle(Title)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.d("Cook","打印组污染1   "+SharedUtil.getString("seller_id"));
                                        Log.d("Cook","打印组污染2   "+commodity.getGoods_id());
                                        Log.d("Cook","打印组污染3   "+ed_Member.getText().toString());
                                        OkGo.post(SysUtils.getGoodsServiceUrl("goodsToAdd"))
                                                .tag(this)
                                                .cacheKey("cacheKey")
                                                .connTimeOut(1000)
                                                .cacheMode(CacheMode.DEFAULT)
                                                .params("name", ed_name.getText().toString())
                                                .params("price", ed_price.getText().toString())
                                                .params("member_price", ed_Member.getText().toString())
                                                .params("cost", ed_cost.getText().toString())
                                                .params("bncode", tv_bncode.getText().toString())
                                                .params("edit", "edit")
                                                .params("provider_id", provider_id)
                                                .params("ALTC", iskuaijiejian)
                                                .params("cook_position", Cook)
                                                .params("goods_id", commodity.getGoods_id())
                                                .params("product_id", commodity.getProduct_id())
                                                .params("little_profit", Float.parseFloat(ed_price.getText().toString()) - Float.parseFloat(ed_cost.getText().toString()) + "")
                                                .params("store", ed_store.getText().toString())
                                                .params("isup", isup)
                                                .params("good_limit", ed_good_limit.getText().toString())
                                                .params("label_id", lable_id)
                                                .params("py", ed_py.getText().toString())
                                                .params("good_stock", ed_good_stock.getText().toString())
                                                .params("good_remark", ed_describe.getText().toString())
//                                                .params("tag_id", commodity.getTag_id())
                                                .params("unit_id", unit_id)
                                                .params("tag_id", tag_id)
                                                .params("btn_switch_type", 1 + "")
                                                .params("image_id", "")
                                                .params("specification", tv_specification.getText().toString())
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onBefore(BaseRequest request) {
                                                        super.onBefore(request);
                                                        Log.e("print", "数据为" + request.getParams().toString());
                                                    }

                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        Log.d("print", s);
                                                        Log.d("print", "rrrrrr" + ed_good_stock.getText().toString());
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
                                                                Toast.makeText(ItemAdd_Activity.this, msg, Toast.LENGTH_SHORT).show();
                                                                cursor.close();
                                                                finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    } else {
                        tv_biaoti.setText("新增商品");
                        final String cost;
                        final String store;
                        final String little_profit;
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
                        new AlertDialog.Builder(this).setTitle(Title)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Log.d("Cook","打印组污染1"+SharedUtil.getString("seller_id"));
//                                        Log.d("Cook","打印组污染1"+commodity.getGoods_id());
                                        OkGo.post(SysUtils.getGoodsServiceUrl("goodsToAdd"))
                                                .tag(this)
                                                .cacheKey("cacheKey")
                                                .connTimeOut(1000)
                                                .cacheMode(CacheMode.DEFAULT)
                                                .params("name", ed_name.getText().toString())
                                                .params("price", ed_price.getText().toString())
                                                .params("member_price", ed_Member.getText().toString())
                                                .params("cost", cost)
                                                .params("bncode", tv_bncode.getText().toString())
                                                .params("little_profit", little_profit)
                                                .params("store", store)
                                                .params("isup", isup)
                                                .params("provider_id", provider_id)
                                                .params("cook_position",Cook)
                                                .params("label_id", lable_id)
                                                .params("good_limit", ed_good_limit.getText().toString())
                                                .params("py", ed_py.getText().toString())
                                                .params("ALTC", iskuaijiejian)
                                                .params("cook_position", Cook)
                                                .params("good_stock", ed_good_stock.getText().toString())
                                                .params("good_remark", ed_describe.getText().toString())
                                                .params("tag_id", tag_id)
                                                .params("unit_id", unit_id)
                                                .params("btn_switch_type", 1 + "")
                                                .params("image_id", "")
                                                .params("specification", tv_specification.getText().toString())
                                                .execute(new StringCallback() {

                                                    @Override
                                                    public void onBefore(BaseRequest request) {
                                                        super.onBefore(request);
                                                                Log.d("print","新增商品的"+request.getParams().toString());
                                                                Log.d("print","provider_id"+provider_id);
                                                    }

                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        Log.d("print", s);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(s);
                                                            JSONObject jo1 = jsonObject.getJSONObject("response");
                                                            String status = jo1.getString("status");
                                                            if (status.equals("200")) {
//                                                                sqLiteDatabase = sqliteHelper.getReadableDatabase();
//                                                                ContentValues values=new ContentValues();
//                                                                values.put("name",ed_name.getText().toString());
//                                                                values.put("bncode",tv_bncode.getText().toString());
//                                                                values.put("py",ed_py.getText().toString());
//                                                                values.put("price",ed_price.getText().toString());
//                                                                values.put("cost",ed_cost.getText().toString());
//                                                                values.put("store",ed_store.getText().toString());
//                                                                if (iskuaijiejian==0){
//                                                                    values.put("ALTC",true);
//                                                                }else {
//                                                                    values.put("ALTC",false);
//                                                                }
//                                                                sqLiteDatabase.insert("commodity",null,values);
                                                                JSONObject data = jo1.getJSONObject("data");
                                                                String msg = data.getString("msg");
                                                                Toast.makeText(ItemAdd_Activity.this, msg, Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }else {
                                                                String message = jo1.getString("message");
                                                                Toast.makeText(ItemAdd_Activity.this,message,Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                }
                break;
            case R.id.but_delete:
                tv_biaoti.setText("编辑商品");
                tv_bncode.setText(commodity.getBncode());
                ed_name.setText(commodity.getName());
                ed_Member.setText(commodity.getMember_price());
                ed_cost.setText(commodity.getCost());
                ed_price.setText(commodity.getPrice());
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
//                String label = "";
//                for (int i = 0; i < commodity.getAdats().size(); i++) {
//                    label += commodity.getAdats().get(i).getLabel_name();
//                }
//                tv_lable.setText(label);
                if (!commodity.getGD().equals("null")) {
                    tv_data.setText(TimeZoneUtil.getTime(1000 * Long.valueOf(commodity.getGD())));
                }
                Log.d("print","Day"+commodity.getPD());

                if (!commodity.getPD().equals("null")){
                    ed_baozhi.setText(commodity.getPD());
                }

//                tv_day.setText(TimeZoneUtil.getTime(1000*Long.valueOf(commodity.getGD())));
                url = SysUtils.getGoodsServiceUrl("goodsToAdd");
                Title = "确定删除";
                new AlertDialog.Builder(this).setTitle(Title)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("print", "" + commodity.getGoods_id());
                                OkGo.post(SysUtils.getGoodsServiceUrl("remove_goods"))
                                        .tag(this)
                                        .cacheKey("cacheKey")
                                        .connTimeOut(1000)
                                        .cacheMode(CacheMode.DEFAULT)
                                        .params("goods_id", commodity.getGoods_id() + "")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onBefore(BaseRequest request) {
                                                super.onBefore(request);
                                                Log.e("上传的商品信息是",""+request.getParams().toString());
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

                                                        Toast.makeText(ItemAdd_Activity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                        finish();
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
                Intent intent4 = new Intent(ItemAdd_Activity.this, Provider_activity.class);
                intent4.putExtra("com.yzx.value", "value");
                startActivityForResult(intent4, 207);
                break;
            case R.id.im_data:
                timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        tv_data.setText(time);
                    }
                }, "2015-11-22 17:34", getTime());
                timeSelector.show();
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
                                dialog1.dismiss();
                                but_devanning.setText("拆箱");
                            }
                            Toast.makeText(ItemAdd_Activity.this,data,Toast.LENGTH_SHORT).show();
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
                    Inputmethod_Utils.getshow(ItemAdd_Activity.this);
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
                            Toast.makeText(ItemAdd_Activity.this,data,Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(ItemAdd_Activity.this);
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
                                            Toast.makeText(ItemAdd_Activity.this, "退货成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ItemAdd_Activity.this, message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ItemAdd_Activity.this, "请输入备注", Toast.LENGTH_SHORT).show();
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
