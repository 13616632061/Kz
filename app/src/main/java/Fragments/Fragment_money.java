package Fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/6/24.
 */
public class Fragment_money extends BaseFragment implements View.OnClickListener {

    public View view;
    public RadioButton but_money,but_moneydetails,but_replenish,but_provider,but_settlement;
    public ImageView im_di1,im_di2,im_di3,im_di4,im_di5;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.layout_money,null);
//        init();
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.layout_money;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        init1(view);
    }

    private void init1(View view) {
        but_money= (RadioButton) view.findViewById(R.id.but_money);
        but_moneydetails= (RadioButton) view.findViewById(R.id.but_moneydetails);
        but_replenish= (RadioButton) view.findViewById(R.id.but_replenish);
        but_provider= (RadioButton) view.findViewById(R.id.but_provider);
        but_settlement= (RadioButton) view.findViewById(R.id.but_settlement);
        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        im_di3= (ImageView) view.findViewById(R.id.im_di3);
        im_di4= (ImageView) view.findViewById(R.id.im_di4);
        im_di5= (ImageView) view.findViewById(R.id.im_di5);
        but_moneydetails.setOnClickListener(this);
        but_money.setOnClickListener(this);
        but_replenish.setOnClickListener(this);
        but_provider.setOnClickListener(this);
        but_settlement.setOnClickListener(this);
        but_moneydetails.performClick();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_money:
//                but_money.setTextColor(Color.parseColor("#FF6501"));
//                but_moneydetails.setTextColor(Color.parseColor("#434343"));
//                but_replenish.setTextColor(Color.parseColor("#434343"));
//                but_provider.setTextColor(Color.parseColor("#434343"));
//                but_settlement.setTextColor(Color.parseColor("#434343"));
                im_di1.setVisibility(View.VISIBLE);
                im_di2.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_money,new Fragment_lei_money()).commit();
                break;
            case R.id.but_moneydetails:
//                but_money.setTextColor(Color.parseColor("#434343"));
//                but_moneydetails.setTextColor(Color.parseColor("#FF6501"));
//                but_replenish.setTextColor(Color.parseColor("#434343"));
//                but_provider.setTextColor(Color.parseColor("#434343"));
//                but_settlement.setTextColor(Color.parseColor("#434343"));
                im_di1.setVisibility(View.GONE);
                im_di2.setVisibility(View.VISIBLE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_money,new Fragment_detailsmoney()).commit();
                break;
            case R.id.but_replenish:
//                but_money.setTextColor(Color.parseColor("#434343"));
//                but_moneydetails.setTextColor(Color.parseColor("#434343"));
//                but_replenish.setTextColor(Color.parseColor("#FF6501"));
//                but_provider.setTextColor(Color.parseColor("#434343"));
//                but_settlement.setTextColor(Color.parseColor("#434343"));
                im_di1.setVisibility(View.GONE);
                im_di2.setVisibility(View.GONE);
                im_di3.setVisibility(View.VISIBLE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_money,new Fragment_audit()).commit();
                break;
            case R.id.but_provider:
//                but_money.setTextColor(Color.parseColor("#434343"));
//                but_moneydetails.setTextColor(Color.parseColor("#434343"));
//                but_replenish.setTextColor(Color.parseColor("#434343"));
//                but_provider.setTextColor(Color.parseColor("#FF6501"));
//                but_settlement.setTextColor(Color.parseColor("#434343"));
                im_di1.setVisibility(View.GONE);
                im_di2.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.VISIBLE);
                im_di5.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_money,new Fragment_provider()).commit();
                break;
            case R.id.but_settlement:
//                but_money.setTextColor(Color.parseColor("#434343"));
//                but_moneydetails.setTextColor(Color.parseColor("#434343"));
//                but_replenish.setTextColor(Color.parseColor("#434343"));
//                but_provider.setTextColor(Color.parseColor("#434343"));
//                but_settlement.setTextColor(Color.parseColor("#FF6501"));
                im_di1.setVisibility(View.GONE);
                im_di2.setVisibility(View.GONE);
                im_di3.setVisibility(View.GONE);
                im_di4.setVisibility(View.GONE);
                im_di5.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_money,new Fragment_settlement()).commit();
                break;
        }
    }
}
