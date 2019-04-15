package retail.yzx.com.kz;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Entty.Message_Beans2;
import adapters.Message_Adapter;
import base.BaseActivity;
import shujudb.Sqlite_Entity;
import widget.SwipeMenuRecyclerView;

/**
 * Created by admin on 2018/7/17.
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener {



    public ImageView im_huanghui;
    public SwipeMenuRecyclerView recyclerView;
    public Sqlite_Entity sqlite_entity;
    public Message_Adapter adapter;
    public List<Message_Beans2> adats=new ArrayList<>();

    @Override
    protected int getContentId() {
        return R.layout.message_activity;
    }

    @Override
    protected void init() {
        super.init();

        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(this);

        adapter=new Message_Adapter(this);

        sqlite_entity=new Sqlite_Entity(this);
        im_huanghui= (ImageView) findViewById(R.id.im_huanghui);
        im_huanghui.setOnClickListener(this);
        recyclerView= (SwipeMenuRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
           case  R.id.im_huanghui:
            this.finish();
            break;
        }
    }


    @Override
    protected void loadDatas() {
        super.loadDatas();
        Log.d("print打印的消息数据",sqlite_entity.queryAllMsgs1().size()+"");
        adats=sqlite_entity.queryAllMsgs1();
        Collections.reverse(adats);
        adapter.setData(adats);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnCLickItme(new Message_Adapter.SetItmeOnClickListener() {
            @Override
            public void ItmeonClickListener(final int j) {
                adats.get(j).getContent();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.update_dialog,
                        (ViewGroup) findViewById(R.id.update_dialog));
//                new AlertDialog.Builder(MessageActivity.this).setTitle("有新版本是否更新")
//                        .setView(layout)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                AlertDialog downloadDialog;    //下载弹出框
//                                Intent intent=new Intent(MessageActivity.this, UPdate_MyServices.class);
//                                intent.putExtra("url","https://zjzc.oss-cn-beijing.aliyuncs.com/"+adats.get(j).getContent());
//                                startService(intent);
//                            }
//                        })
//                        .setNegativeButton("取消", null).show();
            }

            @Override
            public void DeleteOnClickListener(int i) {
                sqlite_entity.deleteMsgs(adats.get(i).getTime());
                adats=sqlite_entity.queryAllMsgs1();
                Collections.reverse(adats);
                adapter.setData(adats);
                adapter.notifyDataSetChanged();
            }
        });



    }




}
