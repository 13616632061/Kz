package Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Entty.Commodity;
import Entty.Goods_Common_Notes;
import Entty.ShuliangEntty;
import Utils.BluetoothPrintFormatUtil;
import Utils.DateUtils;
import Utils.LogUtils;
import Utils.PrintWired;
import Utils.ScanGunKeyEventHelper;
import Utils.SharedUtil;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.Adapter_Fuzzy;
import adapters.Adapter_details;
import adapters.Goods_Common_Notes_Adapter;
import adapters.StockAdapter;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.kz.DemoApplication;
import retail.yzx.com.kz.Goods_Common_Notes_Activity;
import retail.yzx.com.kz.R;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import widget.MyGirdView;

/**
 * Created by admin on 2017/10/26.
 */
public class Fragment_outbound extends Fragment implements ScanGunKeyEventHelper.OnScanSuccessListener, Adapter_details.StrOnoclick, View.OnClickListener, StockAdapter.StrOnoclick {

    private View view;
    private ListView list_outbound;
    private Button but_outbound;
    private EditText tv_seek;


    public ScanGunKeyEventHelper scanGunKeyEventHelper;
    public List<Commodity> list_fuzzy=new ArrayList<>();
    public List<Commodity> commodities=new ArrayList<>();
    public List<ShuliangEntty> entty=new ArrayList<>();
    public StockAdapter adapterzhu;
    public List<Map<String,String>> listmap=new ArrayList<>();
    private ArrayList<Goods_Common_Notes> mCommonNotesList;
    private MyGirdView My_girdview;
    private Goods_Common_Notes_Adapter mGoods_Common_Notes_Adapter;
    private boolean issearch=false;
    private Button but_ItemAdd;

    public Adapter_Fuzzy adapter_fuzzy ;
    public Dialog dialogfuzzy;

    public Button but_note;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_outbound,null);
        forceOpenSoftKeyboard(getActivity());
        init();
        setRetainInstance(true);
        return view;
    }

    private void init() {


        but_ItemAdd= (Button) view.findViewById(R.id.but_ItemAdd);
        but_ItemAdd.setVisibility(View.GONE);

        but_note= (Button) view.findViewById(R.id.but_note);
        but_note.setOnClickListener(this);

        mCommonNotesList=new ArrayList<>();

        adapterzhu=new StockAdapter(getActivity());
        adapterzhu.StrOnoclick(Fragment_outbound.this);
        //条形码拦截
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(Fragment_outbound.this);

        list_outbound= (ListView) view.findViewById(R.id.list_outbound);
        but_outbound= (Button) view.findViewById(R.id.but_outbound);
        but_outbound.setOnClickListener(this);

        tv_seek = (EditText) view.findViewById(R.id.ed_seek);
//        tv_seek.performClick();
        tv_seek.setFocusable(true);
        tv_seek.setFocusableInTouchMode(true);
        tv_seek.requestFocus();

        showSoftInputFromWindow(getActivity(),tv_seek);

        tv_seek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                    if (editable.toString().length()>=13){
//                        if (!issearch){
//                            issearch=true;
//                            getSeek(editable.toString());
//                        }
//
//                    }
            }
        });

        tv_seek.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    if (!issearch){
                        issearch=true;
                        getSeek(tv_seek.getText().toString());
                    }
                }
                return true;
            }
        });

    }



    public  void forceOpenSoftKeyboard(Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //弹出键盘
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onScanSuccess(String barcode) {
        getSeek(barcode);
    }


    //搜索的接口
    public void getSeek(String str) {
        String name="search";
        if (Utils.StringUtils.isNumber1(str)){
            name="bncode";
        }else {
            name="search";
        }
        OkGo.post(SysUtils.getGoodsServiceUrl("goods_search"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params(name,str)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("print","搜索的结果"+s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            String status=jsonObject1.getString("status");
                            if (status.equals("200")) {
                                JSONObject jo1 = jsonObject1.getJSONObject("data");
                                JSONArray ja1 = jo1.getJSONArray("goods_info");
                                list_fuzzy.clear();
                                if (ja1.length() <= 1) {
                                for (int j = 0; j < ja1.length(); j++) {
                                    Commodity commodity = new Commodity();
                                    JSONObject jo2 = ja1.getJSONObject(j);
                                    commodity.setGoods_id(jo2.getString("goods_id"));
                                    commodity.setName(jo2.getString("name"));
                                    commodity.setPy(jo2.getString("py"));
                                    commodity.setPrice(jo2.getString("price"));
                                    commodity.setCost(jo2.getString("cost"));
                                    commodity.setBncode(jo2.getString("bncode"));
                                    commodity.setStore(jo2.getString("store"));
                                    commodity.setProvider_name(jo2.getString("provider_name"));

                                    if (commodities.size() > 0) {
                                        int in = 0;
                                        aa:
                                        for (int k = 0; k < commodities.size(); k++) {
                                            if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                                in = in + (k + 1);
                                                break aa;
                                            }
                                        }
                                        if (in == 0) {
                                            ShuliangEntty shuliangEntty = new ShuliangEntty();
                                            shuliangEntty.setNumber(1);
                                            entty.add(0, shuliangEntty);
                                            commodities.add(0, commodity);
                                        } else {
                                            float i = entty.get(in - 1).getNumber();
                                            i += 1;
                                            entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);

                                        }
                                    } else {
                                        if (commodity != null) {
                                            if (commodities.size() > 0) {
                                                ShuliangEntty shuliangEntty = new ShuliangEntty();
                                                shuliangEntty.setNumber(1);
                                                entty.add(0, shuliangEntty);
                                                commodities.add(0, commodity);
                                            } else {
                                                ShuliangEntty shuliangEntty = new ShuliangEntty();
                                                shuliangEntty.setNumber(1);
                                                entty.add(0, shuliangEntty);
                                                commodities.add(0, commodity);
                                            }
                                        }
                                    }
                                }
                                //判断数量大于一或更多
                            }else {
                                    dialogfuzzy = new Dialog(getActivity());
                                    dialogfuzzy.setCanceledOnTouchOutside(false);
                                    dialogfuzzy.show();
                                    Window window = dialogfuzzy.getWindow();
                                    window.setContentView(R.layout.fuzzy);
                                    LinearLayout ll_check= (LinearLayout) window.findViewById(R.id.ll_check);
                                    ll_check.setVisibility(View.GONE);
                                    final ListView lv_fuzzy = (ListView) window.findViewById(R.id.lv_fuzzy);
                                    for (int j = 0; j < ja1.length(); j++) {
                                        Commodity commodity = new Commodity();
                                        JSONObject jo2 = ja1.getJSONObject(j);
                                        commodity.setGoods_id(jo2.getString("goods_id"));
                                        commodity.setName(jo2.getString("name"));
                                        commodity.setPy(jo2.getString("py"));
                                        commodity.setPrice(jo2.getString("price"));
                                        commodity.setCost(jo2.getString("cost"));
                                        commodity.setBncode(jo2.getString("bncode"));
                                        commodity.setStore(jo2.getString("store"));
                                        commodity.setProvider_name(jo2.getString("provider_name"));
                                        list_fuzzy.add(commodity);
                                    }
                                    adapter_fuzzy = new Adapter_Fuzzy(getActivity());
                                    adapter_fuzzy.setAdats(list_fuzzy);
                                    adapter_fuzzy.SetOnclick(new Adapter_Fuzzy.SetOnclick() {
                                        @Override
                                        public void setClickListener(int p) {
                                            Commodity commodity=list_fuzzy.get(p);
                                            if (commodities.size() > 0) {
                                                int in = 0;
                                                aa:
                                                for (int k = 0; k < commodities.size(); k++) {
                                                    if (commodities.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                                        in = in + (k + 1);
                                                        break aa;
                                                    }
                                                }
                                                if (in == 0) {
                                                    ShuliangEntty shuliangEntty = new ShuliangEntty();
                                                    shuliangEntty.setNumber(1);
                                                    entty.add(0, shuliangEntty);
                                                    commodities.add(0, commodity);
                                                } else {
                                                    float i = entty.get(in - 1).getNumber();
                                                    i += 1;
                                                    entty.get(in - 1).setNumber(i);
//                        listmaidan.get(in - 1).setNumber(i);
                                                }
                                            } else {
                                                if (commodity != null) {
                                                    if (commodities.size() > 0) {
                                                        ShuliangEntty shuliangEntty = new ShuliangEntty();
                                                        shuliangEntty.setNumber(1);
                                                        entty.add(0, shuliangEntty);
                                                        commodities.add(0, commodity);
                                                    } else {
                                                        ShuliangEntty shuliangEntty = new ShuliangEntty();
                                                        shuliangEntty.setNumber(1);
                                                        entty.add(0, shuliangEntty);
                                                        commodities.add(0, commodity);
                                                    }
                                                }
                                            }
                                            dialogfuzzy.dismiss();
                                        }
                                    });
                                    lv_fuzzy.setAdapter(adapter_fuzzy);
                                    adapter_fuzzy.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            issearch=false;
                            adapterzhu.setAdats(commodities,entty);
                            adapterzhu.notifyDataSetChanged();
                            list_outbound.setAdapter(adapterzhu);
//                            list_outbound.setSelection(adapterzhu.getCount() - 1);
                            tv_seek.setText("");
                        }
                    }
                });
    }


//    //条形码的拦截
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
////            if(event.getKeyCode()!=KeyEvent.KEYCODE_BACK){
//        scanGunKeyEventHelper.analysisKeyEvent(event);
////            }
//        return true;
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        //在该Fragment的构造函数中注册mTouchListener的回调
//        if(mTouchListener!=null){
//            if(getActivity()!=null){
//                ((Commoditymanagement_Activity)getActivity()).registerMyTouchListener(mTouchListener);
//            }
//        }
//    }


    /**
     * 加备注 加记忆功能
     */


//    /**
//     * 保存数据
//     * @param outState
//     */
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        saveStateToArguments();
//        super.onSaveInstanceState(outState);
//
//        Log.d("print","执行保存数据的操作onSaveInstanceState");
//        outState.putSerializable("commodities", (Serializable)commodities);
//        outState.putSerializable("entty", (Serializable)entty);
//    }

    @Override
    public void onStop() {
        super.onStop();
        DemoApplication.adats.clear();
        DemoApplication.enttys.clear();
        for (int i=0;i<commodities.size();i++){
            DemoApplication.adats.add(commodities.get(i));
            DemoApplication.enttys.add(entty.get(i));
        }

    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        saveStateToArguments();
//    }



//    private boolean restoreStateFromArguments() {
//        Bundle b = getArguments();
//        if (null != b
//                && b.getBundle("Fragment_Luntan_InternalSavedViewState") != null) {
//            savedState = b
//                    .getBundle("Fragment_Luntan_InternalSavedViewState");
//            restoreState();
//            return true;
//        }
//        return false;
//    }


//    private void restoreState() {
//        if (savedState != null) {
//            onRestoreState(savedState);
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
        commodities.clear();
        entty.clear();
        if (DemoApplication.adats!=null){
            for (int i=0;i<DemoApplication.adats.size();i++){
                commodities.add(DemoApplication.adats.get(i));
                entty.add(DemoApplication.enttys.get(i));
            }
        }
        adapterzhu.setAdats(commodities,entty);
        adapterzhu.notifyDataSetChanged();
        list_outbound.setAdapter(adapterzhu);
//        list_outbound.setSelection(adapterzhu.getCount() - 1);
    }

    // 恢复数据
    protected void onRestoreState(Bundle savedInstanceState) {
//            commodities = (List<Commodity>) savedInstanceState.getSerializable("commodities");
//            entty = (List<ShuliangEntty>) savedInstanceState.getSerializable("entty");

                adapterzhu.setAdats(commodities, entty);
                adapterzhu.notifyDataSetChanged();
                list_outbound.setAdapter(adapterzhu);
//                list_outbound.setSelection(adapterzhu.getCount() - 1);
    }


//    private Bundle savedState;
//    private void saveStateToArguments() {
//        if (getView() != null)
//            savedState = saveState();
//        if (savedState != null) {
//            Bundle b = getArguments();
//            if (b!=null){
//                b.putBundle("Fragment_Luntan_InternalSavedViewState",
//                        savedState);
//            }
//        }
//    }

//    private Bundle saveState() {
//        Bundle state = new Bundle();
//        onSaveState(state);
//        return state;
//    }


//    // 保存数据
//    protected void onSaveState(Bundle outState) {
//        outState.putSerializable("commodities", (Serializable)commodities);
//        outState.putSerializable("entty", (Serializable)entty);
//    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        if (savedInstanceState!=null){
////            Log.d("print","生成的数据为"+commodities.size());
////            commodities = (List<Commodity>) savedInstanceState.getSerializable("commodities");
////            entty = (List<ShuliangEntty>) savedInstanceState.getSerializable("entty");
////            adapterzhu.setAdats(commodities,entty);
////            adapterzhu.notifyDataSetChanged();
////            list_outbound.setAdapter(adapterzhu);
////            list_outbound.setSelection(adapterzhu.getCount() - 1);
////        }
////        Log.d("print","生成的数据为");
//        restoreStateFromArguments();
//    }



    /**
     * Fragment中，注册
     * 接收ChatActivity的Touch回调的对象
     * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
     */
//    private Commoditymanagement_Activity.MyTouchListener mTouchListener = new Commoditymanagement_Activity.MyTouchListener() {
//        @Override
//        public void onTouchEvent(KeyEvent event) {
//            // TODO Auto-generated method stub
////            if(event.getKeyCode()!=KeyEvent.KEYCODE_BACK){
//        scanGunKeyEventHelper.analysisKeyEvent(event);
////            }
//        }
//    };



    //点击删除按钮
    @Override
    public void setbutonclick(int i) {
        if (DemoApplication.adats!=null){
            if (DemoApplication.adats.size()==commodities.size()){
                DemoApplication.adats.remove(i);
                DemoApplication.enttys.remove(i);
            }
        }
        commodities.remove(i);
        entty.remove(i);
        adapterzhu.notifyDataSetChanged();
    }

    @Override
    public void setnums(final int i) {
        final Dialog dialog= new Dialog(getActivity());
        dialog.setTitle("入库数量");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.money_dialog);
        final EditText editText= (EditText) window.findViewById(R.id.ed_nums);
        final TextView tv_name= (TextView) window.findViewById(R.id.tv_name);
        editText.setHint("输入出库数");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        tv_name.setText(commodities.get(i).getName());
        final TextView tv_oldnums= (TextView) window.findViewById(R.id.tv_oldnums);
        TextView tv_Prompt= (TextView) window.findViewById(R.id.tv_Prompt);
        tv_Prompt.setText("原入库数");
        tv_oldnums.setText(entty.get(i).getNumber()+"");
        Button but_goto= (Button) window.findViewById(R.id.but_goto);
        Button but_abolish= (Button) window.findViewById(R.id.but_abolish);
        but_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString()!=null&& Utils.StringUtils.isNumber(editText.getText().toString())){

                    entty.get(i).setNumber(Integer.valueOf(editText.getText().toString()));


                    adapterzhu.notifyDataSetChanged();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialog.dismiss();
                }
            }
        });
        but_abolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_outbound:
                //出库单的提交
                if(commodities.size()>0){
                    ShowDialog();
                }
//                Updatas();
                break;
            case R.id.but_note:
                Intent intent_common_notes=new Intent(getActivity(), Goods_Common_Notes_Activity.class);
                startActivity(intent_common_notes);
                break;
        }
    }


    public void ShowDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("出库备注");
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.remarks);
        RelativeLayout Rl_number = (RelativeLayout) window.findViewById(R.id.Rl_number);
        RelativeLayout Rl_fangshi = (RelativeLayout) window.findViewById(R.id.Rl_fangshi);
        Rl_number.setVisibility(View.GONE);
        Rl_fangshi.setVisibility(View.GONE);
        final EditText ed_describe = (EditText) window.findViewById(R.id.ed_describe);
        final EditText ed_phone = (EditText) window.findViewById(R.id.ed_phone);

        ed_phone.setVisibility(View.VISIBLE);
        Button but_submit = (Button) window.findViewById(R.id.but_submit);

        My_girdview= (MyGirdView) window.findViewById(R.id.My_girdview);
        getCommonNotesInfo();

        My_girdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ed_describe.setText(mCommonNotesList.get(i).getNotes());
                Updatas(mCommonNotesList.get(i).getNotes());
                dialog.dismiss();
            }
        });


        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_describe.getText().toString().equals("")&&!ed_phone.getText().toString().equals("")){
                    Updatas(ed_describe.getText().toString()+","+ed_phone.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen = imm.isActive();
                    //isOpen若返回true，则表示输入法打开，反之则关闭。
                    if (isOpen) {
                        InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    dialog.dismiss();
                }
            }
        });

    }


    //获取常用备注信息
    private void getCommonNotesInfo(){
        OkGo.post(SysUtils.getSellerServiceUrl("remarks_list"))
                .tag(this)
                .params("seller_token", SharedUtil.getString("seller_token"))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        Log.e("barcode", "请求URL：" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.e("barcode", "返回数据：" + jsonObject);
                            JSONObject jo1 = jsonObject.getJSONObject("response");
                            String status = jo1.getString("status");
                            if (status.equals("200")) {
                                if(mCommonNotesList!=null){
                                    mCommonNotesList.clear();
                                }
                                JSONArray data=jo1.getJSONArray("data");
                                mCommonNotesList=new ArrayList<Goods_Common_Notes>();
                                if(data!=null){
                                    for(int i=0;i<data.length();i++){
                                        JSONObject dataobj=data.getJSONObject(i);
                                        String notes=dataobj.getString("notes");
                                        String notes_id=dataobj.getString("id");
                                        Goods_Common_Notes goods_common_notes=new Goods_Common_Notes(notes_id,notes);
                                        mCommonNotesList.add(goods_common_notes);
                                    }
                                }
                                mGoods_Common_Notes_Adapter=new Goods_Common_Notes_Adapter(getContext(),mCommonNotesList);
                                My_girdview.setAdapter(mGoods_Common_Notes_Adapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            StringUtils.showToast(getActivity(),"服务器数据异常",20);
                            Log.e("print", "服务器数据异常: "+e.toString() );
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
//                        StringUtils.showToast(Goods_Common_Notes_Activity.this,"网络不给力",20);
                    }
                });

    }

    //上传数据
    public void Updatas(final String remark){
        double money=0;
        double nums=0;
        listmap.clear();
        for (int i=0;i<commodities.size();i++){
            Map<String,String> map=new HashMap<>();
            map.put("name", commodities.get(i).getName());
            map.put("nums", ""+entty.get(i).getNumber());
            nums=TlossUtils.add(nums,entty.get(i).getNumber());
            map.put("cost",commodities.get(i).getCost()+"");
            map.put("id", commodities.get(i).getGoods_id());
            map.put("money", TlossUtils.mul(Double.parseDouble(entty.get(i).getNumber()+""),Double.parseDouble(commodities.get(i).getCost()+""))+"");
            money=TlossUtils.add(money,TlossUtils.mul(Double.parseDouble(entty.get(i).getNumber()+""),Double.parseDouble(commodities.get(i).getCost()+"")));
            map.put("type","1");
            listmap.add(map);
        }


        Gson gson=new Gson();
        String json=gson.toJson(listmap);

        LogUtils.d("上传的出库详情",json);

        OkGo.post(SysUtils.getSellerServiceUrl("store_detail"))
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("goods_map",json)
                .params("oparator",SharedUtil.getString("seller_name"))
                .params("type","1")
                .params("money",money)
                .params("nums",nums)
                .params("remark",remark)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d("上传的出库详情",s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            JSONObject json=jsonObject.getJSONObject("response");
                            String status=json.getString("status");
                            String data=json.getString("data");
                            if (status.equals("200")){
                                String syy = BluetoothPrintFormatUtil.getSelfServicePrinterMsgg("出库单",SharedUtil.getString("name"), SharedUtil.getString("phone"), DateUtils.getCurDate(),commodities,entty,remark,remark,"");
                                PrintWired.usbPrint(getActivity(),syy);
                                PrintWired.usbPrint(getActivity(),syy);

                                commodities.clear();
                                entty.clear();
                                DemoApplication.adats.clear();
                                DemoApplication.enttys.clear();
                                adapterzhu.notifyDataSetChanged();
                                Toast.makeText(getActivity(),"出库成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

}
