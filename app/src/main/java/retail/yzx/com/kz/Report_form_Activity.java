package retail.yzx.com.kz;


import android.content.Intent;
import android.view.View;

import Fragments.Breakage_fragment;
import Fragments.Commodity_fragment;
import Fragments.Fragment_check;
import Fragments.Fragment_forthe;
import Fragments.Fragment_member;
import Fragments.Fragment_money;
import Fragments.Staff_fragment;
import Fragments.Statement_Fragment;
import base.BaseActivity;
import butterknife.OnClick;

/**
 * Created by admin on 2018/9/12.
 * 报表数据
 */

public class Report_form_Activity extends BaseActivity {


    @Override
    protected int getContentId() {
        return R.layout.report_form_activity;
    }

    @Override
    protected void init() {
        super.init();
        //隐藏底部按钮
        retail.yzx.com.supper_self_service.Utils.StringUtils.HideBottomBar(Report_form_Activity.this);
        retail.yzx.com.supper_self_service.Utils.StringUtils.setupUI(this, findViewById(R.id.ll_report));//点击空白处隐藏软键盘

        Intent intent=getIntent();
        switch (intent.getStringExtra("type")){
            case "1":
                showFragment(R.id.fl_report,new Statement_Fragment());
                break;
            case "2":
                showFragment(R.id.fl_report,new Commodity_fragment());
                break;
            case "3":
                showFragment(R.id.fl_report,new Fragment_forthe());
                break;
            case "4":
                showFragment(R.id.fl_report,new Fragment_check());
                break;
            case "5":
                showFragment(R.id.fl_report,new Breakage_fragment());
                break;
            case "6":
                showFragment(R.id.fl_report,new Staff_fragment());
                break;
            case "7":
                showFragment(R.id.fl_report,new Fragment_money());
                break;
            case "8":
                showFragment(R.id.fl_report,new Fragment_member());
                break;
            case "9":
                showFragment(R.id.fl_report,new Fragment_check());
                break;
        }



    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
    }

    @OnClick({R.id.im_huanghui})
    public void ONclick(View view){
        switch (view.getId()){
            case R.id.im_huanghui:
                this.finish();
                break;
        }
    }


}
