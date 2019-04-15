package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import retail.yzx.com.kz.R;

/**
 * @author AudienL(591928179qq.com) on 2016/5/17
 */
public class Switch extends View {

    /** 底层颜色 */
    private int mBackColor = 0x80BDC3C7;
    /** 圆圈的颜色 */
    private int mCircleColor = 0xFFFFFFFF;
    /** 选中时的主颜色 */
    private int mMainColor = 0xFF1ABC9C;

    private int mWidth;
    private int mHeight;
    private int mStrokeWidth_px;
    private float mRadius;
    /** 触摸开始滑动的最小x */
    private int mMinX;
    /** 触摸开始滑动的最大x */
    private int mMaxX;
    /** 触摸点的x */
    private float mTouchX;


    /** 是否选中 */
    private boolean isChecked = false;
    /** 是否正在触摸这view */
    private boolean isTouching = false;

    private Paint mBackPaint;
    private Paint mCirclePaint;
    private Paint mStrokePaint;
    private RectF mBackRect;

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Switch);
        mBackColor = a.getColor(R.styleable.Switch_switch_back_color, mBackColor);
        mCircleColor = a.getColor(R.styleable.Switch_switch_circle_color, mCircleColor);
        mMainColor = a.getColor(R.styleable.Switch_switch_main_color, mMainColor);
        a.recycle();

        // 默认的大小
        mWidth = dp2px(context, 68);
        mHeight = dp2px(context, 32);
        mStrokeWidth_px = dp2px(context, 2);

        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(mCircleColor);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth_px);
    }

    private int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        invalidate();
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                mWidth = mWidth > widthSize ? widthSize : mWidth;
                break;
            case MeasureSpec.EXACTLY:
                mWidth = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                mHeight = mHeight > heightSize ? heightSize : mHeight;
                break;
            case MeasureSpec.EXACTLY:
                mHeight = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        int half = mStrokeWidth_px / 2;
        mBackRect = new RectF(half, half, mWidth - half, mHeight - half);
        mMinX = mHeight / 2;
        mMaxX = mWidth - mHeight / 2;
        mRadius = mHeight / 2 - mStrokeWidth_px / 2;

        setMeasuredDimension(mWidth, mHeight);
    }

    private void drawBackView(Canvas canvas) {
        // 画底图
        canvas.drawRoundRect(mBackRect, mHeight / 2 - mStrokeWidth_px / 2, mHeight / 2 - mStrokeWidth_px / 2, mBackPaint);
        // 画底图边框
        canvas.drawRoundRect(mBackRect, mHeight / 2 - mStrokeWidth_px / 2, mHeight / 2 - mStrokeWidth_px / 2, mStrokePaint);
    }

    private void drawCircle(Canvas canvas, int cx, int cy) {
        // 画圆圈
        canvas.drawCircle(cx, cy, mRadius, mCirclePaint);
        // 画圆圈边框
        canvas.drawCircle(cx, cy, mRadius, mStrokePaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circleX = 0;
        int circleY = mHeight / 2;
        if (isTouching) {
            // 如果手指正在 view 上面，则判断
            if (mTouchX < mMinX) circleX = mMinX;
            else if (mTouchX < mMaxX) circleX = (int) mTouchX;
            else circleX = mMaxX;
        } else {
            circleX = isChecked ? (mWidth - mHeight / 2) : (mHeight / 2);
        }

        mBackPaint.setColor(isChecked ? mMainColor : mBackColor);
        mStrokePaint.setColor(isChecked ? mMainColor : mBackColor);

        drawBackView(canvas);
        drawCircle(canvas, circleX, circleY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                // 无论有没有滑动，只有点击了都toggle
                isChecked = !isChecked;
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(isChecked);
                }
                break;
            default:
        }
        postInvalidate();
        return true;
    }


    // =========================================================
    // OnCheckedChangeListener
    // =========================================================
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        /**
         * 状态发生变化是回调。
         * setChecked 并不会触发回调。
         * @param isChecked 是否打开(选中)
         */
        void onCheckedChanged(boolean isChecked);
    }
}
