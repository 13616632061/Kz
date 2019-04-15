package retail.yzx.com.kz;

import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Entty.Cash_entty;
import Entty.Commodity;
import Entty.Mobile_pay;
import Entty.ShuliangEntty;
import Utils.FileSizeUtil;
import Utils.QRCode;
import Utils.SharedUtil;
import Utils.StringUtils;
import Utils.SysUtils;
import Utils.TlossUtils;
import adapters.ListFuping;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import okhttp3.Call;
import okhttp3.Response;
import retail.yzx.com.restaurant_nomal.Adapter.SecondaryScreenAdapter;
import retail.yzx.com.supper_self_service.Entty.Self_Service_GoodsInfo;

/**
 * Created by admin on 2017/3/1.
 */
public class DifferentDislay extends Presentation {
    public Context context;
    public static LinearLayout ll;
    public VideoView vv;
    public Button button;
    //    public UniversalMediaController media_actions;
    //    private int mSeekPosition;
    public boolean isgone = false;//买单详情是否显示
    public boolean iserweima = false;//二维码是否显示
    public LinearLayout.LayoutParams lp;
    public ListView list_fuping;
    public ListFuping listFuping;
    public List<Commodity> adats;
    public int banduan = 0;
    public TextView tv_fuping;
    public List<Integer> shuliang;
    //总金额
    public double j = 0;

    //现金支付的显示
    public LinearLayout ll_xianjin,ll_succeed,ll_payment,ll_code;
    //现金支付的显示
    public TextView tv_order_id, tv_payed_time, tv_amount, tv_change;
    //现金支付的确定
    public Button but_xianjin;
    public Handler handler;
    public AsyncTask<Void, Void, Bitmap> execute;
    public ImageView im_erweima;
    public TextView tv_jiner, spname,tv_paymaoney;
    //    买单得数量
    public ShuliangEntty shuliangEntty;
    public List<ShuliangEntty> entty;

    public ShimmerTextView tv_shimmer;

    public boolean sum = false;

    public ImageView img_alipay,img_wechat,imgage,img_member;

    public LinearLayout ll_member;

    private SecondaryScreenAdapter mSecondaryScreenAdapter;

    public DifferentDislay(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册买单详情的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("qwer");
        context.registerReceiver(broadcastReceiver, intentFilter);
        //注册二维码的广播
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("poiu");
        context.registerReceiver(broadcastReceiver, intentFilter1);

        //数量加的广播
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.yzx.add");
        context.registerReceiver(broadcastReceiver, intentFilter2);
        //数量减的广播
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.yzx.reductionof");
        context.registerReceiver(broadcastReceiver, intentFilter3);
        //删除的广播
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction("com.yzx.fupingdelete");
        context.registerReceiver(broadcastReceiver, intentFilter4);

        //现金的广播com.yzx.fupingxianjing
        IntentFilter intentFilter5 = new IntentFilter();
        intentFilter5.addAction("com.yzx.fupingxianjing");
        context.registerReceiver(broadcastReceiver, intentFilter5);

        //取单的广播
        IntentFilter intentFilter6 = new IntentFilter();
        intentFilter6.addAction("com.yzx.order");
        context.registerReceiver(broadcastReceiver, intentFilter6);

        //取单的广播
        IntentFilter intentFilter7 = new IntentFilter();
        intentFilter7.addAction("com.yzx.clear");
        context.registerReceiver(broadcastReceiver, intentFilter7);

        //现金支付的确定com.yzx.determination
        IntentFilter intentFilter8 = new IntentFilter();
        intentFilter8.addAction("com.yzx.determination");
        context.registerReceiver(broadcastReceiver, intentFilter8);
//        视屏播放的广播
        IntentFilter intentFilter9 = new IntentFilter();
        intentFilter9.addAction("com.yzx.video");
        context.registerReceiver(broadcastReceiver, intentFilter9);

//        com.yzx.timepay
        IntentFilter intentFilter10 = new IntentFilter();
        intentFilter10.addAction("com.yzx.timepay");
        context.registerReceiver(broadcastReceiver, intentFilter10);

        IntentFilter intentFilter11 = new IntentFilter();
        intentFilter11.addAction("com.yzx.changing");
        context.registerReceiver(broadcastReceiver, intentFilter11);

//        com.yzx.iswholesale

        IntentFilter intentFilter12 = new IntentFilter();
        intentFilter12.addAction("com.yzx.iswholesale");
        context.registerReceiver(broadcastReceiver, intentFilter12);


        IntentFilter intentFilter13 = new IntentFilter();
        intentFilter13.addAction("com.yzx.image");
        context.registerReceiver(broadcastReceiver, intentFilter13);


        context.registerReceiver(mBroadcastReceiver,new IntentFilter("DifferentDislay.Action"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuping);

        SharedUtil.init(context);

        imgage= (ImageView) findViewById(R.id.imgage);

        img_member= (ImageView) findViewById(R.id.img_member);

        ll_member= (LinearLayout) findViewById(R.id.ll_member);

        if (SharedUtil.getBoolean("showmemberpaw")){
            ll_member.setVisibility(View.VISIBLE);
        }else {
            ll_member.setVisibility(View.GONE);
        }

        img_alipay= (ImageView) findViewById(R.id.img_alipay);
        img_wechat= (ImageView) findViewById(R.id.img_wechat);

        listFuping = new ListFuping(context);

        im_erweima = (ImageView) findViewById(R.id.im_erweima);
        tv_jiner = (TextView) findViewById(R.id.tv_jiner);
//        二维码的生成
        spname = (TextView) findViewById(R.id.spname);

        tv_shimmer = (ShimmerTextView) findViewById(R.id.tv_shimmer);
        Shimmer shimmer = new Shimmer();
        shimmer.start(tv_shimmer);

        //买单的金额
        tv_fuping = (TextView) findViewById(R.id.tv_fuping);
        //买单的数据
        list_fuping = (ListView) findViewById(R.id.list_fuping);
//        listFuping=new ListFuping(context);

        ll_xianjin = (LinearLayout) findViewById(R.id.ll_xianjin);
        ll_succeed = (LinearLayout) findViewById(R.id.ll_succeed);
        ll_payment = (LinearLayout) findViewById(R.id.ll_payment);
        ll_code = (LinearLayout) findViewById(R.id.ll_code);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_payed_time = (TextView) findViewById(R.id.tv_payed_time);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_change = (TextView) findViewById(R.id.tv_change);
        tv_paymaoney = (TextView) findViewById(R.id.tv_paymaoney);//支付中显示的支付金额
        but_xianjin = (Button) findViewById(R.id.but_xianjin);

        adats = new ArrayList<>();
        shuliang = new ArrayList<>();
        entty = new ArrayList<>();

        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll = (LinearLayout) findViewById(R.id.ll);
        vv = (VideoView) findViewById(R.id.vv);
        Log.d("print", "下载路径" + Environment.getExternalStorageDirectory().getPath() + "/Download/");
//        if (SharedUtil.getString("path")!=null){
//            if (fileIsExists(SharedUtil.getString("path"))){
//                vv.setVideoPath(SharedUtil.getString("path"));
//            }else {
//                if (fileIsExists(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4")) {
//                    Log.d("print", "本地播放");
//                    Log.d("print", "本地路径是" + Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4");
//                    vv.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4");
//                } else {
//                    OkGo.get("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
//                            .tag(getContext())
//                            .execute(new FileCallback("HWP.mp4") {
//                                @Override
//                                public void onSuccess(File file, Call call, Response response) {
////                            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
//                                    Log.d("print", "下载完成");
//                                }
//                            });
//                    vv.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//                }
//            }
//        }else {

//        if (fileIsExists(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "test.jpg")){
//            imgage.setVisibility(View.VISIBLE);
//            File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
//            加载图片
//            Glide.with(context).load(file).into(imgage);

//            Glide.with(context).load(R.mipmap.test).into(imgage);
//        }

        if (SharedUtil.getString("path")!=null){
            if (!SharedUtil.getString("path").equals("")){
                vv.setVisibility(View.VISIBLE);
                imgage.setVisibility(View.GONE);
            }else {
                vv.setVisibility(View.GONE);
                imgage.setVisibility(View.VISIBLE);
                Glide.with(context).load(SharedUtil.getString("imagepath")).into(imgage);
            }
        }

        if (SharedUtil.getString("imagepath")!=null){
           if (!SharedUtil.getString("imagepath").equals("")){
               vv.setVisibility(View.GONE);
               imgage.setVisibility(View.VISIBLE);
               Glide.with(context).load(SharedUtil.getString("imagepath")).into(imgage);
           }else {
               vv.setVisibility(View.VISIBLE);
               imgage.setVisibility(View.GONE);
           }
        }

            if (fileIsExists(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4")&& FileSizeUtil.getFileOrFilesSize(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4",3)>1) {
//                Log.d("print", "本地播放");
//                Log.d("print", "本地路径是" + Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4");
                vv.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/Download/" + "HWP.mp4");
            } else {
                OkGo.get("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
                        .tag(getContext())
                        .execute(new FileCallback("HWP.mp4") {
                            @Override
                            public void onSuccess(File file, Call call, Response response) {
//                            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                                Log.d("print", "下载完成");
                            }
                        });
                vv.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//            }
        }
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vv.start();
            }
        });
        /**
         *
         */
//        OkGo.get("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
//                .execute(new FileCallback("HWP.mp4") {
//                    @Override
//                    public void onSuccess(File file, Call call, Response response) {
//                        Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
//                    }
//                });

        loadat();

    }

    private void loadat() {
        if (SharedUtil.getString("seller_id")!=null){
            createChineseQRCode(SysUtils.getCoed(SharedUtil.getString("seller_id")),img_member);
        }
    }

    //设置二维码
    private void createChineseQRCode(final String string,final ImageView img) {
        execute = new AsyncTask<Void, Void, Bitmap>() {

            @Override

            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(string, BGAQRCodeUtil.dp2px(context, 150), Color.parseColor("#000000"));
            }


            @Override

            protected void onPostExecute(Bitmap bitmap) {

                if (bitmap != null) {

                    img.setImageBitmap(bitmap);

                } else {

//                    Toast.makeText(context, "生成英文二维码失败", Toast.LENGTH_SHORT).show();

                }

            }

        }.execute();
    }

    //二维码
    @Override
    protected void onStop() {
        super.onStop();
        if (execute != null) {
            execute.cancel(true);
        }
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //广播接收器
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getAction();
            Bundle bundle = intent.getExtras();
            //数量的减少
            if (a.equals("com.yzx.reductionof")) {
                float i = Float.parseFloat((String) bundle.get("reductionof"));
                //设置数量还没设置好
                float f = entty.get((int) i).getNumber();
                /**
                 *
                 */
//                float f=shuliang.get((int)i);
                f--;
                //设置数量还没设置好
//                shuliang.set((int)i,(int)f);
                entty.get((int) i).setNumber((int) f);
                listFuping.getshuliang(entty);
                listFuping.getadats(adats);
                list_fuping.setAdapter(listFuping);
                listFuping.notifyDataSetChanged();
                double j = Double.parseDouble(tv_fuping.getText().toString());

                if (adats.get((int)i).getIs_special_offer()!=null){
                    if (adats.get((int)i).getIs_special_offer().equals("no")){
                        if (adats.get((int)i).getType()!=null&&!adats.get((int)i).getType().equals("")) {
                            if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                if (adats.get((int)i).getCustom_member_price() != null && !adats.get((int)i).getCustom_member_price().equals("")&& !adats.get((int)i).getCustom_member_price().equals("null")) {
                                    j = TlossUtils.sub(j, Double.parseDouble(StringUtils.getStrings(adats.get((int)i).getCustom_member_price(), ",")[Integer.parseInt(adats.get((int)i).getType())-1]));
                                } else {
                                    j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getMember_price()));
                                }
                            }else {
                                j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getPrice()));
                            }
                        }else {
                            j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getPrice()));
                        }

                    }else {
                        j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getPrice()));
                    }
                }else {
                    j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getPrice()));
                }

//                j = TlossUtils.sub(j, Double.parseDouble(adats.get((int) i).getPrice()));
                tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
            }
            //数量的增加
            if (a.equals("com.yzx.add")) {
                int i = Integer.valueOf((String) bundle.get("add"));
//                设置数量还没设置好
                float f=0;
                if (entty.size()>i){
                    f = entty.get(i).getNumber();
                }
//                int f=shuliang.get(i);
                f++;
                //设置数量还没设置好
//                shuliang.set(i,f);
                entty.get(i).setNumber(f);
                listFuping.notifyDataSetChanged();
                double j = Double.parseDouble(tv_fuping.getText().toString());
                if (adats.get((int)i).getIs_special_offer()!=null){
                    if (adats.get((int)i).getIs_special_offer().equals("no")){
                        if (adats.get((int)i).getType()!=null&&!adats.get((int)i).getType().equals("")) {
                            if (SharedUtil.getfalseBoolean("sw_member_price")) {
                                if (adats.get((int)i).getCustom_member_price() != null && !adats.get((int)i).getCustom_member_price().equals("")&& !adats.get((int)i).getCustom_member_price().equals("null")) {
                                    j = TlossUtils.add(j, Double.parseDouble(StringUtils.getStrings(adats.get((int)i).getCustom_member_price(), ",")[Integer.parseInt(adats.get((int)i).getType())-1]));
                                } else {
                                    j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getMember_price()));
                                }
                            }else {
                                j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getPrice()));
                            }
                        }else {
                            j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getPrice()));
                        }

                    }else {
                        j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getPrice()));
                    }
                }else {
                    j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getPrice()));
                }
//                j = TlossUtils.add(j, Double.parseDouble(adats.get((int) i).getPrice()));
                tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
            }

            //删除的广播
            if (a.equals("poiu")) {
                    String url = bundle.getString("yaner");
                    if (url != null) {
                        if (url.equals(" ")) {
                            im_erweima.setImageBitmap(QRCode.createQRCode(SharedUtil.getString("url")));
                        }else if (url.equals("hhhhhh")){
                            ll.setVisibility(View.GONE);
                        }else {
                            createChineseQRCode(url,im_erweima);
                        }
                        if (ll.getVisibility()==View.GONE){
                            ll.setVisibility(View.VISIBLE);
                        }
                    }
//                    Glide.with(getContext()).load(url).into(im_erweima);
                    String jiner = bundle.getString("jinger");
                if (jiner!=null){
                    Mobile_pay mobile_pay = (Mobile_pay) bundle.getSerializable("mobile_pay");
                    if (mobile_pay != null) {
                        ll_xianjin.setVisibility(View.VISIBLE);
                        tv_order_id.setText(mobile_pay.getOrder_id());
                        tv_payed_time.setText(mobile_pay.getPayed_time());
                        new Thread(new ThreadShow()).start();
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 2) {
                                    ll_xianjin.setVisibility(View.GONE);
                                    entty.clear();
                                    adats.clear();
                                    listFuping.getshuliang(entty);
                                    listFuping.getadats(adats);
                                    list_fuping.setAdapter(listFuping);
                                    listFuping.notifyDataSetChanged();
                                }
                            }
                        };
                    }
                }else {
                    ll.setVisibility(View.GONE);
                }

                    tv_jiner.setText(jiner);
//                    Log.d("TAG", "二维码输出");
//                    ll.setVisibility(View.VISIBLE);
//                    iserweima = true;

//                    if (adats.size() > 0) {
//                        String url2 = bundle.getString("yaner");
//                        if (url2!=null){
//                           if (!url2.equals("hhhhhh")){
////                               ll_xianjin.setVisibility(View.VISIBLE);
//                           }
//                        }else {
//                            ll_xianjin.setVisibility(View.VISIBLE);
//                        }
//                        Mobile_pay mobile_pay1 = (Mobile_pay) bundle.getSerializable("mobile_pay");
//                        if (mobile_pay1 != null) {
//                            tv_order_id.setText(mobile_pay.getOrder_id());
//                            tv_payed_time.setText(mobile_pay.getPayed_time());
//                        }
//                        new Thread(new ThreadShow()).start();
//                        handler = new Handler() {
//                            @Override
//                            public void handleMessage(Message msg) {
//                                super.handleMessage(msg);
//                                if (msg.what == 2) {
//                                    ll_xianjin.setVisibility(View.GONE);
//                                }
//                            }
//                        };
//                    }
            }
            if (a.equals("qwer")) {
                if (!bundle.getSerializable("yaner").equals("发送广播，相当于在这里传送数据")) {

                    float weigt=1;
                    if (bundle.getString("weigt")!=null){
                        weigt=Float.parseFloat(bundle.getString("weigt"));
                    }

                    int in = 0;
                    Commodity commodity = (Commodity) bundle.getSerializable("yaner");
                    if (adats.size() > 0) {
                        aa:
                        for (int k = 0; k < adats.size(); k++) {
                            if (adats.get(k).getGoods_id().equals(commodity.getGoods_id())) {
                                in = in + (k + 1);
                                break aa;
                            }
                        }
                        if (in == 0) {
                            shuliangEntty = new ShuliangEntty();
                            shuliangEntty.setNumber(weigt);
                            entty.add(shuliangEntty);
                            adats.add(commodity);
                        } else {
                            float i = entty.get(in - 1).getNumber();
                            i = i + weigt;
                            entty.get(in - 1).setNumber(i);

                            listFuping.getadats(adats);
                            listFuping.getshuliang(entty);
                            list_fuping.setAdapter(listFuping);
                            listFuping.notifyDataSetChanged();
                            list_fuping.setSelection(listFuping.getCount() - 1);
                        }
                    } else {
                            shuliangEntty = new ShuliangEntty();
                            shuliangEntty.setNumber(weigt);
                            entty.add(0, shuliangEntty);
                            adats.add(0, commodity);
                    }

//                    adats.add(commodity);
//                    shuliang.add(1);

                    if (sum) {
                        int k = 0;
                        if (k == 0) {
                            j = Double.parseDouble(tv_fuping.getText().toString());
//                            j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
                            j = TlossUtils.add(j, TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(weigt+"")));
                            tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                            k++;
                        } else {
                            j = Double.parseDouble(tv_fuping.getText().toString());
//                            j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
                            j = TlossUtils.add(j, TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(weigt+"")));
                            tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                        }
                        k = 0;
                        sum = false;
                    } else {
                        j = Double.parseDouble(tv_fuping.getText().toString());
//                        j = TlossUtils.add(j, Double.parseDouble(commodity.getPrice()));
                        j = TlossUtils.add(j, TlossUtils.mul(Double.parseDouble(commodity.getPrice()),Double.parseDouble(weigt+"")));
                        tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                    }

//                    list_fuping.setSelection(listFuping.getCount()-1);

                    /**   }else if(banduan>0){
                     TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
                     animation1.setFillAfter(true);
                     animation1.setDuration(200);
                     vv.startAnimation(animation1);
                     banduan=0;
                     }*/

                    listFuping = new ListFuping(context);
                    listFuping.getadats(adats);
                    listFuping.getshuliang(entty);
                    listFuping.notifyDataSetChanged();
                    list_fuping.setAdapter(listFuping);
                    list_fuping.setSelection(listFuping.getCount() - 1);

                    if (banduan == 0) {
                        TranslateAnimation animation1 = new TranslateAnimation(0, 400, 0, 0);
                        animation1.setFillAfter(true);
                        animation1.setDuration(200);

                        if (!SharedUtil.getString("imagepath").equals("")){
                            imgage.startAnimation(animation1);
                        }else {
                            vv.startAnimation(animation1);
                        }

                        if (SharedUtil.getString("name") != null) {
                            spname.setText(SharedUtil.getString("name"));
                        }
                        isgone = true;
                    }
                    banduan++;
                    /**else {
                     TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
                     animation1.setFillAfter(true);
                     animation1.setDuration(2000);
                     vv.startAnimation(animation1);
                     isgone = false;
                     }*/
                }
            }
            if (a.equals("com.yzx.fupingdelete")) {
                int i = (int) bundle.get("delete");
                double f = Double.parseDouble(tv_fuping.getText().toString());
                if (i < adats.size()) {
                    f = TlossUtils.sub(f, Double.parseDouble(adats.get(i).getPrice()) * entty.get(i).getNumber());
                    if (adats.size() == 1) {
                        j = 0;
                        tv_fuping.setText("0");
                        TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
                        animation1.setFillAfter(true);
                        animation1.setDuration(200);
                        if (!SharedUtil.getString("imagepath").equals("")){
                            imgage.startAnimation(animation1);
                        }else {
                            vv.startAnimation(animation1);
                        }
                        if (SharedUtil.getString("name") != null) {
                            spname.setText(SharedUtil.getString("name"));
                        }
                        banduan = 0;
                    } else {
                        tv_fuping.setText(StringUtils.stringpointtwo(f + ""));
                    }
                    adats.remove(i);
                    entty.remove(i);
                    listFuping.notifyDataSetChanged();
                }

                /**
                 *
                 */
//                shuliang.remove(shuliang.size()-1);
                if (listFuping!=null){
                    listFuping.notifyDataSetChanged();
                }
            }
//com.yzx.fupingxianjing
            if (a.equals("com.yzx.determination")) {
                ll_succeed.setVisibility(View.VISIBLE);
                if (SharedUtil.getBoolean("code")){
                    ll_payment.setVisibility(View.GONE);
                }else {
                    ll_code.setVisibility(View.GONE);
                }
                new Thread(new ThreadShow()).start();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 2) {
                            ll_xianjin.setVisibility(View.GONE);
                            TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
                            animation1.setFillAfter(true);
                            animation1.setDuration(200);
                            if (!SharedUtil.getString("imagepath").equals("")){
                                imgage.startAnimation(animation1);
                            }else {
                                vv.startAnimation(animation1);
                            }

                            if (SharedUtil.getString("name") != null) {
                                spname.setText(SharedUtil.getString("name"));
                            }
                            j = 0;
                            tv_fuping.setText("0");
                            tv_change.setText("0");
                            tv_amount.setText("0");
                            banduan = 0;
                            entty.clear();
                            adats.clear();
                            if (listFuping!=null){
                                listFuping.notifyDataSetChanged();
                            }
                        }
                    }
                };
            }

            //现金的广播com.yzx.determination
            if (a.equals("com.yzx.fupingxianjing")) {
                if (SharedUtil.getString("alipay")!=null){
                    File file=new File(SharedUtil.getString("alipay"));
                    Glide.with(context).load(file).into(img_alipay);
                }
                if (SharedUtil.getString("wechat")!=null){
                    File file=new File(SharedUtil.getString("wechat"));
                    Glide.with(context).load(file).into(img_wechat);
                }
                if(bundle!=null){
                    Cash_entty cash_entty = (Cash_entty) bundle.get("cash_entty");
                    ll_xianjin.setVisibility(View.VISIBLE);
                    ll_succeed.setVisibility(View.GONE);
                    if (SharedUtil.getBoolean("code")){
                        ll_payment.setVisibility(View.VISIBLE);
                    }else {
                        ll_code.setVisibility(View.VISIBLE);
                    }
//                tv_order_id.setText(cash_entty.getOrder_id());
//                tv_payed_time.setText(cash_entty.getPayed_time());
                    tv_amount.setText(cash_entty.getAmount());
                    tv_change.setText(cash_entty.getChange());
                }else {
                    ll_xianjin.setVisibility(View.GONE);
                    ll_succeed.setVisibility(View.GONE);
                    if (SharedUtil.getBoolean("code")){
                        ll_payment.setVisibility(View.GONE);
                    }else {
                        ll_code.setVisibility(View.GONE);
                    }
                }
//                new Thread(new ThreadShow()).start();
//                handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        if (msg.what == 2) {
//                            ll_xianjin.setVisibility(View.GONE);
//                            TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
//                            animation1.setFillAfter(true);
//                            animation1.setDuration(200);
//                            vv.startAnimation(animation1);
//                            tv_fuping.setText("0");
//                            banduan = 0;
//                            entty.clear();
//                            adats.clear();
//                        }
//
//                    }
//                };

////                            TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
////                            animation1.setFillAfter(true);
////                            animation1.setDuration(200);
////                            vv.startAnimation(animation1);
//
            }

            //清空的广播
            if (a.equals("com.yzx.clear")) {
                entty.clear();
                adats.clear();
                shuliang.clear();
                if (listFuping!=null){
                    listFuping.getshuliang(entty);
                    listFuping.getadats(adats);
                    list_fuping.setAdapter(listFuping);
                    listFuping.notifyDataSetChanged();
                }
                j = 0;
                tv_fuping.setText("0");

                ll.setVisibility(View.GONE);
//                vv.setVisibility(View.VISIBLE);
                if (!SharedUtil.getString("imagepath").equals("")){
                    imgage.setVisibility(View.VISIBLE);
                }else {
                    vv.setVisibility(View.VISIBLE);
                }
                TranslateAnimation animation1 = new TranslateAnimation(400, 0, 0, 0);
                animation1.setFillAfter(true);
                animation1.setDuration(10);
                if (!SharedUtil.getString("imagepath").equals("")){
                    imgage.startAnimation(animation1);
                }else {
                    vv.startAnimation(animation1);
                }
                if (SharedUtil.getString("name") != null) {
                    spname.setText(SharedUtil.getString("name"));
                }
                tv_fuping.setText("0");
                banduan = 0;
            }


//            取单的广播
            if (a.equals("com.yzx.order")) {
                TranslateAnimation animation1 = new TranslateAnimation(0, 400, 0, 0);
                animation1.setFillAfter(true);
                animation1.setDuration(200);
                if (!SharedUtil.getString("imagepath").equals("")){
                    imgage.startAnimation(animation1);
                }else {
                    vv.startAnimation(animation1);
                }
                if (SharedUtil.getString("name") != null) {
                    spname.setText(SharedUtil.getString("name"));
                }
                sum = true;
                Bundle bundle2 = intent.getExtras();
                final String order_id = (String) bundle2.getSerializable("order");
                OkGo.post(SysUtils.getSellerServiceUrl("get_order_goods_info"))
                        .tag(this)
                        .cacheKey("cacheKey")
                        .cacheMode(CacheMode.DEFAULT)
                        .params("order_id", order_id)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                adats.clear();
                                entty.clear();
                                int unm = 0;
                                double j = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONObject jo1 = jsonObject.getJSONObject("response");
                                    JSONArray ja1 = jo1.getJSONArray("data");
                                    for (int i = 0; i < ja1.length(); i++) {
                                        Commodity commoditys = new Commodity();
                                        shuliangEntty = new ShuliangEntty();
                                        JSONObject jo2 = ja1.getJSONObject(i);
                                        commoditys.setName(jo2.getString("name"));
                                        commoditys.setPrice(jo2.getString("price"));
                                        commoditys.setMember_price(jo2.getString("member_price"));
                                        commoditys.setGoods_id(jo2.getString("goods_id"));
                                        shuliangEntty.setNumber(Float.parseFloat(jo2.getString("nums")));
                                        commoditys.setCost(jo2.getString("cost"));
                                        adats.add(commoditys);
                                        entty.add(shuliangEntty);
                                    }
                                    for (int i = 0; i < adats.size(); i++) {
                                        unm += entty.get(i).getNumber();
                                        j = TlossUtils.add(j, Double.parseDouble(adats.get(i).getPrice()) * entty.get(i).getNumber());
                                    }
                                    listFuping = new ListFuping(getContext());
                                    tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                                    listFuping.getadats(adats);
                                    listFuping.getshuliang(entty);
                                    list_fuping.setAdapter(listFuping);
                                    listFuping.notifyDataSetChanged();
                                    banduan = 1;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }

            if (a.equals("com.yzx.iswholesale")){
                Bundle bundle2 = intent.getExtras();
                String iswholesale=bundle2.getString("iswholesale");
//                if (iswholesale){
                    listFuping.settype("0");
                    listFuping.notifyDataSetChanged();
                float nums=0;
                double totalMember=0;
                if (bundle2.getString("type").equals("1")){
                    adats.get(Integer.parseInt(bundle2.getString("subscript"))).setType(iswholesale);
                    for (int k=0;k<adats.size();k++){
//                        nums=nums+entty.get(k).getNumber();
//                        totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                        Log.d("print","副屏的数据会员价"+adats.get(k).getType());

                        nums=nums+entty.get(k).getNumber();
                        if (adats.get(k).getType()!=null&&!adats.get(k).getType().equals("")&&!adats.get(k).getType().equals("0")) {
                            if (adats.get(k).getIs_special_offer()!=null){
                                if (adats.get(k).getIs_special_offer().equals("no")) {
                                    if (adats.get(k).getCustom_member_price() != null && !adats.get(k).getCustom_member_price().equals("")&& !adats.get(k).getCustom_member_price().equals("null")) {
                                        if (!StringUtils.getStrings(adats.get(k).getCustom_member_price(), ",")[Integer.parseInt(adats.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(adats.get(k).getCustom_member_price(), ",")[Integer.parseInt(adats.get(k).getType()) - 1]), Double.parseDouble(entty.get(k).getNumber() + "")));
                                        }
                                    } else {
                                        if (!adats.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                        }
                                    }
                                } else {
                                    if (!adats.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                    }
                                }
                            }else {
                                if (!adats.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                    totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                }
                            }
                        }else {
                            if (!adats.get(k).getPrice().equals("")&&!(entty.get(k).getNumber()+"").equals("")){
                                totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                            }
                        }
                    }
                }else {
                    for (int k=0;k<adats.size();k++){
                        adats.get(k).setType(iswholesale);
//                        nums=nums+entty.get(k).getNumber();
//                        totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));

                        nums=nums+entty.get(k).getNumber();
                        if (adats.get(k).getType()!=null&&!adats.get(k).getType().equals("")&&!adats.get(k).getType().equals("0")) {
                            if (adats.get(k).getIs_special_offer()!=null){
                                if (adats.get(k).getIs_special_offer().equals("no")) {
                                    if (adats.get(k).getCustom_member_price() != null && !adats.get(k).getCustom_member_price().equals("")&& !adats.get(k).getCustom_member_price().equals("null")) {
                                        if (!StringUtils.getStrings(adats.get(k).getCustom_member_price(), ",")[Integer.parseInt(adats.get(k).getType()) - 1].equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(StringUtils.getStrings(adats.get(k).getCustom_member_price(), ",")[Integer.parseInt(adats.get(k).getType()) - 1]), Double.parseDouble(entty.get(k).getNumber() + "")));
                                        }
                                    } else {
                                        if (!adats.get(k).getMember_price().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                            totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getMember_price()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                        }
                                    }
                                } else {
                                    if (!adats.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                        totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                    }
                                }
                            }else {
                                if (!adats.get(k).getPrice().equals("") && !(entty.get(k).getNumber() + "").equals("")) {
                                    totalMember = TlossUtils.add(totalMember, TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()), Double.parseDouble(entty.get(k).getNumber() + "")));
                                }
                            }
                        }else {
                            if (!adats.get(k).getPrice().equals("")&&!(entty.get(k).getNumber()+"").equals("")){
                                totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(adats.get(k).getPrice()),Double.parseDouble(entty.get(k).getNumber()+"")));
                            }
                        }
                    }
                }


                    tv_fuping.setText(totalMember+"");
                listFuping.notifyDataSetChanged();
//                }
//                else {
//                    listFuping.settype("1");
//
//                    listFuping.notifyDataSetChanged();
//                    float nums=0;
//                    double totalMember=0;
//                    for (int k=0;k<adats.size();k++){
//                        nums=nums+entty.get(k).getNumber();
//                        totalMember=TlossUtils.add(totalMember,TlossUtils.mul(Double.parseDouble(adats.get(k).getMember_price()),Double.parseDouble(entty.get(k).getNumber()+"")));
//                        Log.d("print","副屏的数据会员价"+adats.get(k).getType());
//                    }
//                    tv_fuping.setText(totalMember+"");
//                }
            }


            if (a.equals("com.yzx.video")){
                Bundle bundle2 = intent.getExtras();
                String path=bundle2.getString("path");
                SharedUtil.putString("path", path);
                SharedUtil.putString("imagepath","");
                vv.setVisibility(View.VISIBLE);
                imgage.setVisibility(View.GONE);
                vv.setVideoPath(path);
            }

            if (a.equals("com.yzx.image")){
                Bundle bundle2 = intent.getExtras();
                if (bundle2.getString("type").equals("0")){
                String path=bundle2.getString("path");
                SharedUtil.putString("imagepath",path);
                SharedUtil.putString("path", "");
                vv.setVisibility(View.GONE);
                imgage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(path)
                        .into(imgage);
                }else if (bundle2.getString("type").equals("1")){
                    boolean isChecked= bundle2.getBoolean("memberpaw");
                    SharedUtil.putBoolean("showmemberpaw",isChecked);
                    if (isChecked){
                        ll_member.setVisibility(View.VISIBLE);
                        if (SharedUtil.getString("seller_id")!=null){
                            createChineseQRCode(SysUtils.getCoed(SharedUtil.getString("seller_id")),img_member);
                        }
                    }else {
                        ll_member.setVisibility(View.GONE);
                        if (SharedUtil.getString("seller_id")!=null){
                            createChineseQRCode(SysUtils.getCoed(SharedUtil.getString("seller_id")),img_member);
                        }
                    }
                }
            }

            //数量改变的广播
            if (a.equals("com.yzx.changing")){
                String type= (String) bundle.get("type");
//                int tnum=0;
                if (type.equals("0")){
                double j = 0;
                int i= (int) bundle.get("item");
                float num= (float) bundle.get("num");
                entty.get(i).setNumber(num);
                for (int k = 0; k < adats.size(); k++) {
//                    tnum += entty.get(k).getNumber();
                    j = TlossUtils.add(j, Double.parseDouble(adats.get(k).getPrice()) * entty.get(k).getNumber());
                }
                tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                listFuping.notifyDataSetChanged();
                }else if (type.equals("1")){
                    double j = 0;
                    int i= (int) bundle.get("item");
                    float num= (float) bundle.get("num");
                    adats.get(i).setPrice(num+"");
                    for (int k = 0; k < adats.size(); k++) {
//                    tnum += entty.get(k).getNumber();
                        j = TlossUtils.add(j, Double.parseDouble(adats.get(k).getPrice()) * entty.get(k).getNumber());
                    }
                    tv_fuping.setText(StringUtils.stringpointtwo(j + ""));
                    listFuping.notifyDataSetChanged();
                }else if (type.equals("2")){
                    Double num= (Double) bundle.get("Total");
                    tv_fuping.setText(StringUtils.stringpointtwo(num + ""));
                }
            }
        }
    };

    public class ThreadShow implements Runnable {
        @Override
        public void run() {

            try {
                Thread.sleep(100);
                Message mag = new Message();
                mag.what = 2;
                handler.sendMessage(mag);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            handler.sendMessageDelayed(mag,1000);

        }
    }

    private ArrayList<Self_Service_GoodsInfo>   mSelf_Service_GoodsInfo=new ArrayList<>();;
    TranslateAnimation animationorderlist=null;
    TranslateAnimation animationoRight=null;
    private  Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    ll_xianjin.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    hideAnimation();
                    break;
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context mcontext, Intent intent) {
            /**
             * type:
             * 1 点击挂单列表，2选择商品，3点击取消，4点击现金支付按钮，5现金支付完成
             * 6 点击移动支付 7取消移动支付
             *
             *
             */


            if (SharedUtil.getString("name") != null) {
                spname.setText(SharedUtil.getString("name"));
            }

            int type=intent.getIntExtra("type",0);

            if(type==1){
                mSelf_Service_GoodsInfo=intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfo");
                showAnimation();
                setAdapter();
            }else if(type==2){
                mSelf_Service_GoodsInfo=intent.getParcelableArrayListExtra("mSelf_Service_GoodsInfo");
                showAnimation();
                setAdapter();
            }else if(type==3){
                hideAnimation();
            }else if(type==4){
                ll_xianjin.setVisibility(View.VISIBLE);
                ll_succeed.setVisibility(View.GONE);
                if (SharedUtil.getBoolean("code")){
                    ll_payment.setVisibility(View.VISIBLE);
                }else {
                    ll_code.setVisibility(View.VISIBLE);
                }
                tv_paymaoney.setVisibility(View.VISIBLE);

                    String RealedPrice=intent.getStringExtra("RealedPrice");
                    String ChangeCash=intent.getStringExtra("ChangeCash");
                    String paymoney=intent.getStringExtra("paymoney");
                    tv_amount.setText(RealedPrice);
                    tv_change.setText(ChangeCash);
                    tv_paymaoney.setText(paymoney);
            }else if(type==5){
                ll_succeed.setVisibility(View.VISIBLE);
                if (SharedUtil.getBoolean("code")){
                    ll_payment.setVisibility(View.GONE);
                }else {
                    ll_code.setVisibility(View.GONE);
                }
                mHandler.sendEmptyMessageDelayed(200,800);

            }else if(type==6){
                String url=intent.getStringExtra("url");
                createChineseQRCode(url,im_erweima);
                ll.setVisibility(View.VISIBLE);
                tv_jiner.setText(tv_fuping.getText().toString().trim());
                tv_amount.setText(tv_fuping.getText().toString().trim());
            }else if(type==7){
                ll.setVisibility(View.GONE);
            }
            setTotalPrice();

        }
    };
    //隐藏动画
private void hideAnimation(){
    animationorderlist= new TranslateAnimation(400, 0, 0, 0);
    animationorderlist.setFillAfter(true);
    animationorderlist.setDuration(200);
//    if (vv.getVisibility()==View.VISIBLE){
//        vv.startAnimation(animationorderlist);
//    }
    if (!SharedUtil.getString("imagepath").equals("")){
        imgage.startAnimation(animationorderlist);
    }else {
        vv.startAnimation(animationorderlist);
    }

    animationorderlist=null;
    mSelf_Service_GoodsInfo.clear();
    tv_amount.setText("");
    tv_change.setText("");
}
    /**
     * 显示订单栏
     */
    private void showAnimation(){
        if(animationorderlist==null&&mSelf_Service_GoodsInfo.size()>0){
            animationorderlist = new TranslateAnimation(0, 400, 0, 0);
            animationorderlist.setFillAfter(true);
            animationorderlist.setDuration(200);
//            vv.startAnimation(animationorderlist);
            if (!SharedUtil.getString("imagepath").equals("")){
                imgage.startAnimation(animationorderlist);
            }else {
                vv.startAnimation(animationorderlist);
            }
        }
    }
    //设置适配器
    private void setAdapter(){
            mSecondaryScreenAdapter=new SecondaryScreenAdapter(context,mSelf_Service_GoodsInfo);
            list_fuping.setAdapter(mSecondaryScreenAdapter);
    }
    //计算商品总数据，总价格
    private void setTotalPrice(){
        int total_nums=0;
        double total_price=0.00;
        for(int i=0;i<mSelf_Service_GoodsInfo.size();i++){
            String goods_name=mSelf_Service_GoodsInfo.get(i).getName();
            String nums_str= mSelf_Service_GoodsInfo.get(i).getNumber();
            String price_str= mSelf_Service_GoodsInfo.get(i).getPrice();
            double nums=Double.parseDouble(nums_str);
            double price=0.00;
            try{
                price=Double.parseDouble(price_str);
            }catch (Exception e){
            }
            total_nums+=nums;
            total_price+=price*nums;
        }

//        tv_change = (TextView) findViewById(R.id.tv_change);
        tv_fuping.setText("￥ "+ Utils.StringUtils.stringpointtwo(total_price+""));//应收

    }
    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
        context.unregisterReceiver(broadcastReceiver);
    }
}