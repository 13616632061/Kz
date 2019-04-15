package widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ken on 2016/9/21.15:51
 * 圆形ImageView
 */
public class CircleImageView extends ImageView {

    private Paint paint;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //获得ImageView显示的图片
        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();//获得当前ImageView显示的Bitmap对象

            //获得圆形的Bitmap
            Bitmap circleBitmap = getCirleBitmap(bitmap);

            //摆放圆形ImageView
            int viewWidth = getWidth();
            int viewHeight = getHeight();

            //设置图片显示的返回
            Rect rect1 = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
            Rect rect2 = null;
            if (viewWidth > viewHeight) {
                rect2 = new Rect(
                        viewWidth / 2 - viewHeight / 2,
                        0,
                        viewWidth / 2 + viewHeight / 2,
                        viewHeight
                );
            } else {
                rect2 = new Rect(
                        0,
                        viewHeight / 2 - viewWidth / 2,
                        viewWidth,
                        viewHeight / 2 + viewWidth / 2
                );
            }
            canvas.drawBitmap(circleBitmap, rect1, rect2, null);

        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 传一个矩形的Bitmap进去，返回一个圆形的Bitmap
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCirleBitmap(Bitmap bitmap) {
        int s = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();

        //获得一个宽高都为s的空白Bitmap
        Bitmap newBitmap = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888);
        //将该空白的Bitmap转化为画布
        Canvas canvas = new Canvas(newBitmap);

        //设置画布的抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        //绘制圆形
        canvas.drawCircle(s / 2, s / 2, s / 2, paint);

        //设置画笔的混合模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //将有内容的bitmap压缩至画布等宽高，然后绘制在画布上
        Rect rect1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect rect2 = new Rect(0, 0, s, s);
        canvas.drawBitmap(bitmap, rect1, rect2, paint);


        //重置画笔
        paint.reset();

        return newBitmap;
    }
}
