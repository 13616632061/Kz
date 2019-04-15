package Fragments;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/4.
 */
public class Staff_fragment extends BaseFragment implements View.OnClickListener {
    public View view;
    public FrameLayout rl_fragment;
    public RadioButton but_statement,Rl_operate;
    public ImageView im_di1,im_di2;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        view=inflater.inflate(R.layout.staff_fragment,null);
//        init();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.staff_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
    }

    private void init1(View view) {
        but_statement= (RadioButton) view.findViewById(R.id.but_statement);
        but_statement.setOnClickListener(this);
        Rl_operate= (RadioButton) view.findViewById(R.id.Rl_operate);
        Rl_operate.setOnClickListener(this);
        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        but_statement.performClick();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_statement:
                im_di1.setVisibility(View.VISIBLE);
                im_di2.setVisibility(View.GONE);
//                but_statement.setTextColor(Color.parseColor("#FF6501"));
//                Rl_operate.setTextColor(Color.parseColor("#313131"));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rl_fragment,new Staff_nei_Fragment()).commit();
                break;
            case R.id.Rl_operate:
                im_di1.setVisibility(View.GONE);
                im_di2.setVisibility(View.VISIBLE);
//                but_statement.setTextColor(Color.parseColor("#313131"));
//                Rl_operate.setTextColor(Color.parseColor("#FF6501"));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rl_fragment,new Operate_fragment()).commit();
                break;
        }
    }
}
