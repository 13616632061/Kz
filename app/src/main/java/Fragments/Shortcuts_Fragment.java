package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import retail.yzx.com.kz.R;

/**
 * Created by admin on 2017/4/6.
 * 快捷栏编辑
 */
public class Shortcuts_Fragment extends Fragment implements View.OnClickListener {
    public View view;
    public RadioButton but_Quick,but_Cook;

    public Button but_yidong,but_edit;
    public Fragment mcontext;

    public Fragment quick_fragment = new Quick_edit_fragment();
    public Fragment cook_fragment = new Cook_edit_fragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.shortcuts_fragment,null);
        init();
        return view;
    }

    private void init() {


        but_yidong= (Button) view.findViewById(R.id.but_yidong);
        but_edit= (Button) view.findViewById(R.id.but_edit);

        but_yidong.setOnClickListener(this);
        but_edit.setOnClickListener(this);

        but_Quick= (RadioButton) view.findViewById(R.id.but_Quick);
        but_Quick.setOnClickListener(this);
        but_Cook= (RadioButton) view.findViewById(R.id.but_Cook);
        but_Cook.setOnClickListener(this);
        but_Quick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    but_Quick.setBackgroundResource(R.drawable.kuaijie2);
                    but_Cook.setBackgroundResource(R.drawable.chuwu);


//                    Fragment fragment = (Fragment) fragmentPagerAdapter.instantiateItem(Rl_quick, compoundButton.getId());
                    switchFrament(mcontext,quick_fragment);
//                    fragmentPagerAdapter.setPrimaryItem(Rl_quick, 0, fragment);
//                    fragmentPagerAdapter.finishUpdate(Rl_quick);
                }
            }
        });

        but_Cook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    but_Quick.setBackgroundResource(R.drawable.chuwu);
                    but_Cook.setBackgroundResource(R.drawable.kuaijie2);
                    switchFrament(mcontext,cook_fragment);
//                    Fragment fragment= (Fragment) fragmentPagerAdapter.instantiateItem(Rl_quick,compoundButton.getId());
//                    fragmentPagerAdapter.setPrimaryItem(Rl_quick, 1, fragment);
//                    fragmentPagerAdapter.finishUpdate(Rl_quick);
                }
            }
        });



        but_Quick.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.but_Quick:
//                but_Quick.setBackgroundResource(R.drawable.kuaijie2);
//                but_Cook.setBackgroundResource(R.drawable.chuwu);
//                switchFrament(mcontext,quick_fragment);

//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fl_shortcuts,new Quick_edit_fragment())
//                        .commit();

//                break;
//            case R.id.but_Cook:
//                but_Quick.setBackgroundResource(R.drawable.chuwu);
//                but_Cook.setBackgroundResource(R.drawable.kuaijie2);
//                switchFrament(mcontext,cook_fragment);

//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fl_shortcuts,new Quick_fragment())
//                        .commit();
//                break;
            case R.id.but_yidong:
                Intent intent =new Intent();
                intent.setAction("com.yzx.yidongedit");
                getActivity().sendBroadcast(intent);
                break;
            case R.id.but_edit:
                Intent intent1 =new Intent();
                intent1.setAction("com.yzx.edit");
                getActivity().sendBroadcast(intent1);

                break;

        }

    }
    public void switchFrament(Fragment from, Fragment to) {
        if (from != to) {
            mcontext = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.add(R.id.fl_shortcuts, to).commit();
                }

            } else {
                if (from != null) {
                    ft.hide(from);
                }
                if (to != null) {
                    ft.show( to).commitAllowingStateLoss();
                }


            }

        }
    }
}
