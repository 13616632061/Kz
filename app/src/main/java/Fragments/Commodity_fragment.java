package Fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import base.BaseFragment;
import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/3/11.
 */
public class Commodity_fragment extends BaseFragment implements View.OnClickListener {

    private RadioButton but_buyoff,but_category,but_shortcuts,btn_res_table,btn_sharegood;
    private ImageView im_di1,im_di2,im_di3,im_di4,im_di5;
    private Category_fragment category_fragment;
    private Buyoff_Fragment buyoff_fragment;
    private Shortcuts_Fragment mShortcuts_Fragment;
    private Res_Table_Management_Fragment mRes_Table_Management_Fragment;
    private Share_Goods_Fragment mShare_Goods_Fragment;//共享商品
    private Fragment_provider fragment_provider;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.commodity_fragment, null);
//        initView(view);
//        initFragment();
//
//        return view;
//    }

    @Override
    protected int getContentId() {
        return R.layout.commodity_fragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        initView(view);
        initFragment();
    }

    private void initView(View view ) {
        but_buyoff = (RadioButton) view.findViewById(R.id.but_buyoff);
        but_category = (RadioButton) view.findViewById(R.id.but_category);
        but_shortcuts = (RadioButton) view.findViewById(R.id.but_shortcuts);
        btn_res_table = (RadioButton) view.findViewById(R.id.btn_res_table);
        btn_sharegood = (RadioButton) view.findViewById(R.id.btn_sharegood);

        im_di1= (ImageView) view.findViewById(R.id.im_di1);
        im_di2= (ImageView) view.findViewById(R.id.im_di2);
        im_di3= (ImageView) view.findViewById(R.id.im_di3);
        im_di4= (ImageView) view.findViewById(R.id.im_di4);
        im_di5= (ImageView) view.findViewById(R.id.im_di5);


        but_shortcuts.setOnClickListener(this);
        but_category.setOnClickListener(this);
        but_buyoff.setOnClickListener(this);
        btn_res_table.setOnClickListener(this);
        btn_sharegood.setOnClickListener(this);
    }
    private void initFragment() {
        category_fragment=new Category_fragment();
        buyoff_fragment=new Buyoff_Fragment();
        mShortcuts_Fragment=new Shortcuts_Fragment();
        mRes_Table_Management_Fragment=new Res_Table_Management_Fragment();
        mShare_Goods_Fragment=new Share_Goods_Fragment();
        fragment_provider=new Fragment_provider();

        but_buyoff.performClick();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_buyoff:
                setSelectBtnStatus(but_buyoff,but_category,but_shortcuts,btn_res_table,btn_sharegood,
                        im_di1,im_di2,im_di3,im_di4,im_di5,buyoff_fragment);
                break;
            case R.id.but_category:
                setSelectBtnStatus(but_category,but_buyoff,but_shortcuts,btn_res_table,btn_sharegood,
                        im_di2,im_di1,im_di3,im_di4,im_di5,category_fragment);
                break;
            case R.id.but_shortcuts:
                setSelectBtnStatus(but_shortcuts,but_category,but_buyoff,btn_res_table,btn_sharegood,
                        im_di3,im_di1,im_di2,im_di4,im_di5,mShortcuts_Fragment);
                break;
            case R.id.btn_res_table:
                setSelectBtnStatus(btn_res_table,but_shortcuts,but_category,but_buyoff,btn_sharegood,
                        im_di4,im_di1,im_di2,im_di3,im_di5,mRes_Table_Management_Fragment);
                break;
            case R.id.btn_sharegood:
                setSelectBtnStatus(btn_sharegood,but_shortcuts,but_category,but_buyoff,btn_res_table,
                        im_di5,im_di4,im_di1,im_di2,im_di3,fragment_provider);
                break;
        }
    }
    //设置选择的按钮状态
    private void setSelectBtnStatus(RadioButton btn1,RadioButton btn2,RadioButton btn3,RadioButton btn4,RadioButton btn5,
                                    ImageView iv1,ImageView iv2,ImageView iv3,ImageView iv4,ImageView iv5,Fragment fragment){
//        btn1.setTextColor(Color.parseColor("#FF6501"));
//        btn2.setTextColor(Color.parseColor("#313131"));
//        btn3.setTextColor(Color.parseColor("#313131"));
//        btn4.setTextColor(Color.parseColor("#313131"));
//        btn5.setTextColor(Color.parseColor("#313131"));
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.GONE);
        iv3.setVisibility(View.GONE);
        iv4.setVisibility(View.GONE);
        iv5.setVisibility(View.GONE);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fl_buyoff, fragment)
                .commit();
    }
}
