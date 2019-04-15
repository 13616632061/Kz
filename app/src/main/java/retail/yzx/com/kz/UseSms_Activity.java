package retail.yzx.com.kz;

import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import Entty.SendLogsms_Entty;
import Utils.SysUtils;
import adapters.Sendsms_Adapter;
import base.BaseActivity;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2018/12/11.
 */

public class UseSms_Activity extends BaseActivity {
    @BindView(R.id.listview)
    ListView listview;

    SendLogsms_Entty sendLogsms_entty;
    Sendsms_Adapter sendsms_adapter;

    int page=1;
    @Override
    protected int getContentId() {
        return R.layout.usesms_activity;
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void loadDatas() {
        super.loadDatas();
        LoadDatas();
    }

    public void LoadDatas(){
        OkGo.post(SysUtils.getSms("sms-sendLog"))
                .tag(this)
                .params("page",page)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        sendLogsms_entty=gson.fromJson(s,SendLogsms_Entty.class);
                        if (sendLogsms_entty!=null){
                            sendsms_adapter=new Sendsms_Adapter(UseSms_Activity.this);
                            sendsms_adapter.SetAdats(sendLogsms_entty);
                            listview.setAdapter(sendsms_adapter);
                        }
                    }
                });

    }

}
