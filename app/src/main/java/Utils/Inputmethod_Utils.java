package Utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by admin on 2017/8/23.
 */
public class Inputmethod_Utils {

    public static void getshow(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        //isOpen若返回true，则表示输入法打开，反之则关闭。
        if (isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
