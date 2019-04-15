package widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import retail.yzx.com.kz.R;


/**
 * Created by YGD on 2017/11/6.
 */

public class Home_Popupwindow extends PopupWindow {
    private Context mContext;
    private View view;

    public Home_Popupwindow(Context mContext, int id, final int layout, int height){
        this.view= LayoutInflater.from(mContext).inflate(id,null);

        //设置外部可点击
        this.setOutsideTouchable(true);

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
//        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//        this.setHeight(1000);
        this.setHeight(height);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }



}
