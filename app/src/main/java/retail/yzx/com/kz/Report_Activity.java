package retail.yzx.com.kz;

import android.view.View;
import android.widget.TextView;

import Fragments.Order_fragment;
import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/9/14.
 * 取单的界面
 */

public class Report_Activity extends BaseActivity {
    @BindView(R.id.tv_huanghui)
    TextView tv_huanghui;
    @Override
    protected int getContentId() {
        return R.layout.report_activity;
    }


    @Override
    protected void init() {
        super.init();
        showFragment(R.id.fl_report,new Order_fragment(false));
    }


    @OnClick({R.id.tv_huanghui})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.tv_huanghui:
                this.finish();
                break;
        }
    }

}
