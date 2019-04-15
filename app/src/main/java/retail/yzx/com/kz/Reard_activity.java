package retail.yzx.com.kz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utils.StringUtils;
import Utils.SysUtils;
import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2017/4/29.
 */
public class Reard_activity extends BaseActivity implements View.OnClickListener {


    public ImageView im_huanghui;
    public Button but_code;
    public ImageView im_code;
    public boolean show=false;
    public RelativeLayout rl_update;
    public int versionCode;
    public String versions;

    @Override
    protected int getContentId() {
        return R.layout.reard_layout;
    }

    @Override
    protected void init() {
        super.init();
        rl_update= (RelativeLayout) findViewById(R.id.rl_update);
        rl_update.setOnClickListener(this);

        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);

        but_code= (Button) findViewById(R.id.but_code);
        but_code.setOnClickListener(this);
        im_code= (ImageView) findViewById(R.id.im_code);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.reard_layout);
//        init1();
//    }

//    private void init1() {
//
//        rl_update= (RelativeLayout) findViewById(R.id.rl_update);
//        rl_update.setOnClickListener(this);
//
//        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
//        im_huanghui.setOnClickListener(this);
//
//        but_code= (Button) findViewById(R.id.but_code);
//        but_code.setOnClickListener(this);
//        im_code= (ImageView) findViewById(R.id.im_code);
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_huanghui:
                finish();
                break;
            case R.id.but_code:
                if (!show){
                    im_code.setVisibility(View.VISIBLE);
                    show=true;
                }else {
                    im_code.setVisibility(View.GONE);
                    show=false;
                }
                break;
            case R.id.rl_update:
                update();
                break;
        }
    }


    String app_password="";
    String app_version="";
    String uri="";

    //软件的检查更新
    private void update() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        OkGo.post(SysUtils.getUpDatas())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d("print", versionCode+"更新数据"+s);
                        try {
                            JSONArray jsonArray=new JSONArray(s);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                app_password=jsonObject.getString("app_password");
                                app_version=jsonObject.getString("app_version");
                                uri=jsonObject.getString("uri");
                            }
//                            JSONObject jsonObject=new JSONObject(s);
//                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
//                            String status=jsonObject1.getString("status");
//                            if (status.equals("200")){
//                                setpasswored("");
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            if (StringUtils.isNumber(app_version)){
                                if (Integer.parseInt(app_version)>versionCode){
                                    setpasswored(app_password);
                                }else {
                                    Toast.makeText(Reard_activity.this,"已经是最新版",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Reard_activity.this,"已经是最新版",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }


    private View view_add_nums_notes;
    private AlertDialog mAlertDialog_add_nums_notes;
    public void setpasswored(final String password){
        if (mAlertDialog_add_nums_notes!=null){
            mAlertDialog_add_nums_notes.dismiss();
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(Reard_activity.this, R.style.AlertDialog);
        view_add_nums_notes = View.inflate(Reard_activity.this, R.layout.password_update, null);
        final EditText ed_paw= (EditText) view_add_nums_notes.findViewById(R.id.ed_paw);
        Button but_dimdis= (Button) view_add_nums_notes.findViewById(R.id.but_dimdis);
        but_dimdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_paw.getText().toString().equals(password)){
                    LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.update_dialog,
                        (ViewGroup) findViewById(R.id.update_dialog));
                    new AlertDialog.Builder(Reard_activity.this).setTitle("有新版本是否更新")
                        .setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog downloadDialog;    //下载弹出框
                                Intent intent=new Intent(Reard_activity.this, UPdate_MyServices.class);
                                intent.putExtra("url",uri);
                                startService(intent);
                                mAlertDialog_add_nums_notes.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                }
            }
        });
        mAlertDialog_add_nums_notes = dialog.setView(view_add_nums_notes).show();
        mAlertDialog_add_nums_notes.setCancelable(true);
        mAlertDialog_add_nums_notes.show();
    }


//    OkGo.get("http://www.yzx6868.com/apk/pingban.xml")
//            .tag(this)
//                .execute(new FileCallback("text.xml") {
//        @Override
//        public void onSuccess(File file, Call call, Response response) {
//            versions = MyXMLReader.pagetee();
//            if (versionCode != Integer.parseInt(versions)) {
//                Log.d("print", "版本号是" + versions);
//                LayoutInflater inflater = getLayoutInflater();
//                View layout = inflater.inflate(R.layout.update_dialog,
//                        (ViewGroup) findViewById(R.id.update_dialog));
//                new AlertDialog.Builder(Reard_activity.this).setTitle("有新版本是否更新")
//                        .setView(layout)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                AlertDialog downloadDialog;    //下载弹出框
//                                startService(new Intent(Reard_activity.this, UPdate_MyServices.class));
//                            }
//                        })
//                        .setNegativeButton("取消", null).show();
//            }else {
//                Toast.makeText(Reard_activity.this,"已是最新版本",Toast.LENGTH_SHORT).show();
//            }
//        }
//    });

}
