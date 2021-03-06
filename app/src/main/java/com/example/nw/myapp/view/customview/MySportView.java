package com.example.nw.myapp.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.nw.myapp.R;

/**
 * Created by nw on 2017/10/19.
 */

public class MySportView extends View {

    private String text;
    private int textColor;
    private int textSize;
    private int outCircleColor;
    private int inCircleColor;


    //绘制时控制文本绘制的范围
    private Paint mPaint, circlePaint;

    private Rect mBound;
    private RectF circleRect;
    private float mCurrentAngle;
    private float mStartSweepValue;
    private int mCurrentPercent, mTargetPercent;
    private int mWidth;
    private int mHeight;
    private int mRaduis;

    public MySportView(Context context) {
        this(context, null);
    }

    public MySportView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取我们自定义的样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySportView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MySportView_titleSportColor:
                    // 默认颜色设置为黑色
                    textColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MySportView_titleSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    textSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MySportView_outCircleColor:
                    // 默认颜色设置为黑色
                    outCircleColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MySportView_inCircleColor:
                    // 默认颜色设置为黑色
                    inCircleColor = array.getColor(attr, Color.BLACK);
                    break;

            }

        }
        array.recycle();
        init();
    }

    private void init() {
        //创建画笔
        circlePaint = new Paint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //圆环开始角度 -90正北方向
        mStartSweepValue = -90;
        //当前角度
        mCurrentAngle = 0;
        //当前百分比
        mCurrentPercent = 0;
        mBound = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOutCircle(canvas);

        drawText(canvas);

        drawInCircle(canvas);

    }

    private void drawInCircle(Canvas canvas) {
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(10);
        circlePaint.setColor(inCircleColor);
        circleRect = new RectF(20, 20, getWidth() - 20, getWidth() - 20);
        canvas.drawArc(circleRect, mStartSweepValue, mCurrentAngle, false, circlePaint);

        if (mCurrentPercent < mTargetPercent) {
            //当前百分比+1
            mCurrentPercent += 1;
            //当前的角度
            mCurrentAngle += 3.6;
            //每100ms重画一次
            postInvalidateDelayed(100);
        }


    }

    private void drawText(Canvas canvas) {
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        text = String.valueOf(mCurrentPercent);
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        canvas.drawText(text, mWidth / 2 - mBound.width() / 2, mHeight / 2 + mBound.height() / 2, mPaint);
        //设置字体大小
        mPaint.setTextSize(textSize / 3);
        //绘制字体
        canvas.drawText("分", getWidth() * 3 / 5, getWidth() / 3, mPaint);
    }

    private void drawOutCircle(Canvas canvas) {
        mPaint.setColor(outCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRaduis, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        //如果布局里面设置的是固定值,这里取布局里面的固定值;如果设置的是match_parent,则取父布局的大小
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            //如果布局里面没有设置固定值,这里取字体的宽度
            width = widthSize * 1 / 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }
        setMeasuredDimension(width, height);
    }

    public void setNumber(int size) {
        mCurrentPercent = 0;
        mTargetPercent = size;
        mCurrentAngle = 0;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();

        mRaduis = Math.min(mWidth / 2, mHeight / 2);
    }
}




























