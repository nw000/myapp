package com.example.nw.myapp.view.customview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.nw.myapp.R;

/**
 * Created by nw on 2017/10/19.
 */

public class MyBesselView extends View {

    private int color;
    private int mWidth;
    private int mHeight;
    private int xSize;
    private int ySize;
    private int xWidth;
    private int yHeight;
    private Paint paint;
    private Path path;
    private Path arcPath;
    private AnimatorSet animSet;
    private Paint txtPaint;
    private Rect mRect;
    private String text = "火箭发射成功!!!";

    private boolean isSuccess;

    public MyBesselView(Context context) {
        this(context, null);
    }


    public MyBesselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MyBesselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取我们自定义属性的样式
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyBesselView);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            if (ta.getIndex(i) == R.styleable.MyBesselView_titleColor) {
                color = ta.getColor(i, Color.BLACK);
            }
        }
        ta.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = widthSize / 2;
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = heightSize / 2;
                break;
        }
        mWidth = widthSize;
        mHeight = heightSize;

        //火箭的宽高
        xSize = mWidth / 10;
        ySize = mHeight / 8;

        //手势的位置
        xWidth = xSize;
        yHeight = 0;
        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (xWidth < xSize) {
            xWidth = xSize;
        }
        if (xWidth > mWidth * 9 / 10) {
            xWidth = mWidth * 9 / 10;
        }
        if (yHeight > (mHeight * 8 / 10 - ySize)) {
            yHeight = (mHeight * 8 / 10 - ySize);
        }
        if (yHeight > (mHeight * 7 / 10 - ySize) && xWidth < mWidth * 4 / 10) {
            yHeight = (mHeight * 7 / 10 - ySize);
        }
        if (yHeight > (mHeight * 7 / 10 - ySize) && xWidth > mWidth * 6 / 10) {
            yHeight = (mHeight * 7 / 10 - ySize);
        }
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();

        //绘制小火箭
        path.moveTo(xWidth - xSize / 2, yHeight + ySize * 3 / 5);
        path.lineTo(xWidth, yHeight);
        path.lineTo(xWidth + xSize / 2, yHeight + ySize * 3 / 5);

        path.moveTo(xWidth - xSize / 4, yHeight + ySize * 3 / 10);
        path.lineTo(xWidth - xSize / 4, yHeight + ySize);
        path.lineTo(xWidth + xSize / 4, yHeight + ySize);
        path.lineTo(xWidth + xSize / 4, yHeight + ySize * 3 / 10);

        canvas.drawPath(path, paint);
        //绘制发射台
        paint.setStrokeWidth(10);
        arcPath.reset();
        arcPath.moveTo(mWidth / 10, mHeight * 7 / 10);
        int arcHeight;
        if (yHeight > (mHeight * 7 / 10 - ySize) && xWidth > mWidth * 4 / 10 && xWidth < mWidth * 6 / 10) {
            arcHeight = yHeight + ySize + ySize + yHeight - mHeight * 7 / 10;
        } else {
            arcHeight = mHeight * 7 / 10;
        }
        arcPath.quadTo(mWidth / 2, arcHeight, mWidth * 9 / 10, mHeight * 7 / 10);
        canvas.drawPath(arcPath, paint);
        //绘制成功后的文字
        if (isSuccess && yHeight < 0) {
            txtPaint.setTextSize(80);
            txtPaint.setColor(Color.BLACK);
            txtPaint.getTextBounds(text, 0, text.length(), mRect);
            canvas.drawText(text, mWidth * 1 / 2 - mRect.width() / 2, mHeight * 1 / 2 + mRect.height() * 1 / 2, txtPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSuccess = false;
                break;
            case MotionEvent.ACTION_MOVE:
                xWidth = x;
                yHeight = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (yHeight > (mHeight * 7 / 10 - ySize) && xWidth > mWidth * 4 / 10 && xWidth < mWidth * 6 / 10) {
                    startAnim();
                }
                break;
        }
        return true;
    }

    private void startAnim() {
        //动画实现
        ValueAnimator valueAnimator = ValueAnimator.ofInt(yHeight, -ySize);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                yHeight = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animSet.setDuration(1200);
        animSet.play(valueAnimator);
        animSet.start();
        isSuccess = true;
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        path = new Path();
        arcPath = new Path();

        animSet = new AnimatorSet();

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);

        mRect = new Rect();


    }


}






















