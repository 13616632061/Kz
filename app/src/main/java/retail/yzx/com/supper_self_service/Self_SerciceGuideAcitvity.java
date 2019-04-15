package retail.yzx.com.supper_self_service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Utils.SharedUtil;
import retail.yzx.com.restaurant_self_service.Self_Service_RestanrauntActivity;
import retail.yzx.com.supper_self_service.Utils.NoDoubleClickUtils;
import retail.yzx.com.supper_self_service.Utils.StringUtils;
import retail.yzx.com.kz.Commoditymanagement_Activity;
import retail.yzx.com.kz.R;

public class Self_SerciceGuideAcitvity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_startpay;
    private View activity_self__sercice_guide_acitvity;
    private EditText et_pwd;
    long[] mHits = new long[5];//点击5下
    private InputMethodManager imm;
    private AlertDialog mAlertDialog;
    private int logntype;//2表示餐饮自助

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self__sercice_guide_acitvity);
        StringUtils.HideBottomBar(Self_SerciceGuideAcitvity.this);
        SharedUtil.init(this);
        Intent intent=getIntent();
        if(intent!=null){
            logntype=intent.getIntExtra("logntype",0);
        }
        initView();
    }

    private void initView() {
        btn_startpay= (Button) findViewById(R.id.btn_startpay);
        activity_self__sercice_guide_acitvity= findViewById(R.id.activity_self__sercice_guide_acitvity);
        btn_startpay.setOnClickListener(this);
        activity_self__sercice_guide_acitvity.setOnClickListener(this);

        if(logntype==2){
            activity_self__sercice_guide_acitvity.setBackgroundResource(R.drawable.guidebackground_restanraunt);
        }else{
            activity_self__sercice_guide_acitvity.setBackgroundResource(R.drawable.guidebackground_nums);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startpay:
                if(logntype==2){
                    startActivity(new Intent(Self_SerciceGuideAcitvity.this, Self_Service_RestanrauntActivity.class));
                }else {
                    startActivity(new Intent(Self_SerciceGuideAcitvity.this, Self_Service_PlayOrdersActivity.class));
                }

                break;
            case R.id.activity_self__sercice_guide_acitvity:
                GoToGoodsManage();
                break;
        }
    }
    /**
     * 进入商品管理页面弹窗
     */
    private void GoToGoodsManage() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于500，即双击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Self_SerciceGuideAcitvity.this);
            View view = View.inflate(Self_SerciceGuideAcitvity.this, R.layout.layout_gotogoodsmanage, null);
            et_pwd = (EditText) view.findViewById(R.id.et_pwd);
            et_pwd.requestFocus();
            et_pwd.setFocusable(true);
            //软键盘显示
            imm = (InputMethodManager) Self_SerciceGuideAcitvity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            Button but_cancel = (Button) view.findViewById(R.id.but_cancel);
            but_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            });
            Button but_confirm = (Button) view.findViewById(R.id.but_confirm);
            but_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
                        Toast.makeText(Self_SerciceGuideAcitvity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!SharedUtil.getString("password").equals(et_pwd.getText().toString().trim())) {
                        Toast.makeText(Self_SerciceGuideAcitvity.this, "密码输入错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent mIntent = new Intent(Self_SerciceGuideAcitvity.this, Commoditymanagement_Activity.class);
                    mIntent.putExtra("logntype",logntype);
                    startActivity(mIntent);
                    mAlertDialog.dismiss();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            });
            mAlertDialog = dialog.setView(view).show();
            mAlertDialog.setCancelable(false);
            mAlertDialog.show();
        }
    }
    //点击编辑框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            NoDoubleClickUtils.hideKeyBoard(Self_SerciceGuideAcitvity.this,ev);
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
}
