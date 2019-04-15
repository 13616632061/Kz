package Fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import Utils.SharedUtil;
import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/7/4.
 */
public class Fragment_member extends BaseFragment implements View.OnClickListener {

    public View view;
    public RadioButton but_member,but_detail,but_integral,but_specification,but_eductible;
    public ImageView im_di1,im_di2,im_di3,im_di4,im_di5;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.member_fragment,null);
//        init();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.member_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
    }

    private void init1(View view) {
        but_member= (RadioButton) view.findViewById(R.id.but_member);
        but_member.setOnClickListener(this);
        but_detail= (RadioButton) view.findViewById(R.id.but_detail);
        but_detail.setOnClickListener(this);
        but_integral= (RadioButton) view.findViewById(R.id.but_integral);
        but_integral.setOnClickListener(this);
        but_specification= (RadioButton) view.findViewById(R.id.but_specification);
        but_specification.setOnClickListener(this);
        but_eductible= (RadioButton) view.findViewById(R.id.but_eductible);
        but_eductible.setOnClickListener(this);
        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        im_di3= (ImageView) view.findViewById(R.id.im_di3);
        im_di4= (ImageView) view.findViewById(R.id.im_di4);
        im_di5= (ImageView) view.findViewById(R.id.im_di5);
//      点击第一
        but_member.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_member:
                Log.e("print","点击了明细");
                setSelectBtnStatus(but_member,but_detail,but_integral,but_specification,
                        im_di1,im_di2,im_di3,im_di4,im_di5,new Fragment_lei_member() );
                break;
            case R.id.but_detail:
                Log.e("print","点击了明细");
                setSelectBtnStatus(but_detail,but_member,but_integral,but_specification,
                        im_di2,im_di1,im_di3,im_di4,im_di5,new Fragment_nie_detail() );

                break;
            case R.id.but_integral:
                if (!SharedUtil.getString("type").equals("4")){
                    setSelectBtnStatus(but_integral,but_member,but_detail,but_specification,
                            im_di3,im_di1,im_di2,im_di4,im_di5,new Fragment_integral() );
                }else {
                    Toast.makeText(getContext(),"没有该项权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_specification:
                if (!SharedUtil.getString("type").equals("4")){
                    setSelectBtnStatus(but_specification,but_member,but_detail,but_integral,
                            im_di4,im_di1,im_di2,im_di3,im_di5,new Fragment_member_specification("1") );
                }else {
                    Toast.makeText(getContext(),"没有该项权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_eductible:
                if (!SharedUtil.getString("type").equals("4")){
                    setSelectBtnStatus(but_specification,but_member,but_detail,but_integral,
                            im_di5,im_di4,im_di1,im_di2,im_di3,new Fragment_member_specification("2") );
                }else {
                    Toast.makeText(getContext(),"没有该项权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //设置选择的按钮状态
    private void setSelectBtnStatus(RadioButton btn1,RadioButton btn2,RadioButton btn3,RadioButton btn4,
                                    ImageView iv1,ImageView iv2,ImageView iv3,ImageView iv4,ImageView iv5,Fragment fragment){
//        btn1.setTextColor(Color.parseColor("#FF6501"));
//        btn2.setTextColor(Color.parseColor("#313131"));
//        btn3.setTextColor(Color.parseColor("#313131"));
//        btn4.setTextColor(Color.parseColor("#313131"));
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        iv5.setVisibility(View.GONE);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.rl_fragment, fragment)
                .commit();
    }


}
