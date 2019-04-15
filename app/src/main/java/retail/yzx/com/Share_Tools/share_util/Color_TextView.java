package retail.yzx.com.Share_Tools.share_util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/24.
 * 自定义彩色字
 */

public class Color_TextView extends TextView {
    private int TextViewWidth;
    private Paint paint;
    private LinearGradient linearGradient;

    public Color_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(TextViewWidth==0){
            TextViewWidth=getMeasuredWidth();
            if(TextViewWidth>0){
                paint=getPaint();
                linearGradient=new LinearGradient(0, 0, TextViewWidth, 0,
                        new int[]{Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.GRAY}, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);

            }
        }

    }
}
