package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/10/23.
 */
public class Fragment_forthe extends BaseFragment implements View.OnClickListener {

    public View view;
    public FrameLayout rl_fragment;
    private RadioButton but_outbound,but_warehousing,but_Thereport;
    private ImageView im_di1,im_di2,im_di3;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.fragment_forthe,null);
//        init();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_forthe;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
    }

    private void init1(View view) {
        rl_fragment= (FrameLayout) view.findViewById(R.id.rl_fragment);
        but_outbound= (RadioButton) view.findViewById(R.id.but_outbound);
        but_outbound.setOnClickListener(this);
        but_warehousing= (RadioButton) view.findViewById(R.id.but_warehousing);
        but_warehousing.setOnClickListener(this);
        but_Thereport= (RadioButton) view.findViewById(R.id.but_Thereport);
        but_Thereport.setOnClickListener(this);
        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        im_di3= (ImageView) view.findViewById(R.id.im_di3);
        but_outbound.performClick();

        arguments.putInt("current_id", 1);
        fragment.setArguments(arguments);
    }
    Fragment fragment=new Fragment_outbound();
    Fragment fragment1=new Fragment_intobound();
    Bundle arguments = new Bundle();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_outbound:
                setSelectBtnStatus(im_di1,im_di2,im_di3,new Fragment_outbound());
                break;
            case R.id.but_warehousing:
                setSelectBtnStatus(im_di2,im_di1,im_di3,new Fragment_intobound());
                break;
            case R.id.but_Thereport:
                setSelectBtnStatus(im_di3,im_di1,im_di2,new Fragment_out_statement());
                break;

        }

    }

    private void setSelectBtnStatus(ImageView iv1, ImageView iv2, ImageView iv3, Fragment fragment){
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.rl_fragment, fragment)
                .commit();
    }

}
