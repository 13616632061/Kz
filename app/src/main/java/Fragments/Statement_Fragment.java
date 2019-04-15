package Fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/5/2.
 */
public class Statement_Fragment extends BaseFragment implements View.OnClickListener {

    public View view;
    public RadioButton but_statement,but_statistics,tv_shifting,but_return,but_redeem;
    public TextView tv_volume;
    public ImageView im_di1,im_di2,im_di3,im_di4,im_di5;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.statement_fragment,null);
//        init();
//        Loaddata();
//
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.statement_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
        Loaddata();
    }

    private void init1(View view) {
        but_statement= (RadioButton) view.findViewById(R.id.but_statement);
        but_statistics= (RadioButton) view.findViewById(R.id.but_statistics);
        tv_shifting= (RadioButton) view.findViewById(R.id.tv_shifting);
        but_return= (RadioButton) view.findViewById(R.id.but_return);
        but_redeem= (RadioButton) view.findViewById(R.id.but_redeem);
        but_statement.setOnClickListener(this);
        but_statistics.setOnClickListener(this);
        tv_shifting.setOnClickListener(this);
        but_return.setOnClickListener(this);
        but_redeem.setOnClickListener(this);
        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        im_di3= (ImageView) view.findViewById(R.id.im_di3);
        im_di4= (ImageView) view.findViewById(R.id.im_di4);
        im_di5= (ImageView) view.findViewById(R.id.im_di5);
        but_statement.performClick();
    }

    private void Loaddata() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_statement:
//                but_statement.setTextColor(Color.parseColor("#FF6501"));
//                but_statistics.setTextColor(Color.parseColor("#434343"));
//                tv_shifting.setTextColor(Color.parseColor("#434343"));
//                but_return.setTextColor(Color.parseColor("#434343"));
//                but_redeem.setTextColor(Color.parseColor("#434343"));
                im_di1.setVisibility(View.VISIBLE);
                im_di2.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_status,new Fragment_nei_statement()).commit();
                break;
            case R.id.but_statistics:
//                but_statement.setTextColor(Color.parseColor("#434343"));
//                but_statistics.setTextColor(Color.parseColor("#FF6501"));
//                tv_shifting.setTextColor(Color.parseColor("#434343"));
//                but_return.setTextColor(Color.parseColor("#434343"));
//                but_redeem.setTextColor(Color.parseColor("#434343"));
                im_di2.setVisibility(View.VISIBLE);
                im_di1.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_status,new Fragment_nei_statistics()).commit();
                break;
            case R.id.tv_shifting:
//                but_statement.setTextColor(Color.parseColor("#434343"));
//                but_statistics.setTextColor(Color.parseColor("#434343"));
//                tv_shifting.setTextColor(Color.parseColor("#FF6501"));
//                but_return.setTextColor(Color.parseColor("#434343"));
//                but_redeem.setTextColor(Color.parseColor("#434343"));
                im_di2.setVisibility(View.GONE);
                im_di1.setVisibility(View.GONE);
                im_di3.setVisibility(View.VISIBLE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_status,new Shifting_nei_Fragment())
                        .commit();
                break;
            case R.id.but_return:
//                but_statement.setTextColor(Color.parseColor("#434343"));
//                but_statistics.setTextColor(Color.parseColor("#434343"));
//                tv_shifting.setTextColor(Color.parseColor("#434343"));
//                but_return.setTextColor(Color.parseColor("#FF6501"));
//                but_redeem.setTextColor(Color.parseColor("#434343"));
                im_di2.setVisibility(View.GONE);
                im_di1.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.VISIBLE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_status,new Fragment_return())
                        .commit();
                break;
            case R.id.but_redeem:
                im_di2.setVisibility(View.GONE);
                im_di1.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.VISIBLE);
//                but_statement.setTextColor(Color.parseColor("#434343"));
//                but_statistics.setTextColor(Color.parseColor("#434343"));
//                tv_shifting.setTextColor(Color.parseColor("#434343"));
//                but_return.setTextColor(Color.parseColor("#434343"));
//                but_redeem.setTextColor(Color.parseColor("#FF6501"));
                getFragmentManager().beginTransaction()
                        .replace(R.id.fl_status,new Fragment_redeem())
                        .commit();
                break;
        }
    }
}
