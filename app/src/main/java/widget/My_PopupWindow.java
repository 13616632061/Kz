package widget;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by admin on 2017/5/9.
 */
public class My_PopupWindow {
    public View layout_tv_dingdan;
    public PopupWindow popupWindow;

    public void getpoplayout(Activity context, int popmenulist, View view) {
        layout_tv_dingdan = context.getLayoutInflater().inflate(popmenulist, null);
        popupWindow = new PopupWindow(layout_tv_dingdan, view.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);// 设置popupwindow可点击  
        popupWindow.setOutsideTouchable(true);// 设置popupwindow外部可点击  
        popupWindow.setFocusable(true);// 获取焦点 
        popupWindow.showAsDropDown(view);
        popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.setFocusable(false);//失去焦点  
                popupWindow.dismiss();//消除pw 
                return true;
            }
        });


    }


}
