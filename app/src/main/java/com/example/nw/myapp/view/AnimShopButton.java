package com.example.nw.myapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.nw.myapp.R;

/**
 * Created by nw on 2017/10/18.
 */

public class AnimShopButton extends View {


    //控件 paddingLeft paddingTop + paint的width
    protected int mLeft, mTop;
    //宽高
    protected int mWidth, mHeight;
    protected static final String TAG = "zxt/" + AnimShopButton.class.getName();
    protected static final int DEFAULT_DURATION = 350;
    /**
     * 两个圆之间的间距(xml)
     */
    protected float mGapBetweenCircle;

    //加按钮是否开启fill模式 默认是stroke(xml)(false)
    protected boolean isAddFillMode;
    //加按钮的背景色前景色(xml)
    protected int mAddEnableBgColor;
    protected int mAddEnableFgColor;
    //加按钮不可用时的背景色前景色(xml)
    protected int mAddDisableBgColor;
    protected int mAddDisableFgColor;
    //按钮是否开启fill模式 默认是stroke(xml)(false)
    protected boolean isDelFillMode;
    //按钮的背景色前景色(xml)
    protected int mDelEnableBgColor;
    protected int mDelEnableFgColor;
    //按钮不可用时的背景色前景色(xml)
    protected int mDelDisableBgColor;
    protected int mDelDisableFgColor;

    //最大数量和当前数量(xml)
    protected int mMaxCount;
    protected int mCount;

    //圆的半径
    protected float mRadius;
    //圆圈的宽度
    protected float mCircleWidth;
    //线的宽度
    protected float mLineWidth;
    /**
     * 增加一个开关 ignoreHintArea：UI显示、动画是否忽略hint收缩区域
     */
    protected boolean ignoreHintArea;

    /**
     * 圆角值(xml)
     */
    protected int mHintBgRoundValue;

    //Feature : 显示正在补货中，此时不允许点击
    protected boolean isReplenish;
    //画笔、颜色、大小、文字
    protected Paint mReplenishPaint;
    protected int mReplenishTextColor;
    protected int mReplenishTextSize;
    protected String mReplenishText;


    //数量为0时，hint文字 背景色前景色(xml) 大小
    protected Paint mHintPaint;
    protected int mHintBgColor;
    protected int mHingTextSize;
    protected String mHintText;
    protected int mHintFgColor;



    protected int mPerAnimDuration = DEFAULT_DURATION;
    //普通模式时，应该是1， 只在 isHintMode true 才有效
    protected float mAnimExpandHintFraction;

    //是否处于HintMode下 count = 0 时，且第一段收缩动画做完了，是true
    protected boolean isHintMode;

    public AnimShopButton setReplenish(boolean replenish) {
        isReplenish = replenish;
        if (isReplenish && null == mReplenishPaint) {
            mReplenishPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mReplenishPaint.setTextSize(mReplenishTextSize);
            mReplenishPaint.setColor(mReplenishTextColor);
        }
        return this;
    }

    public AnimShopButton(Context context) {
        this(context, null);
    }

    public AnimShopButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimShopButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //模拟参数传入（设置初始化值）
        initDefaultValue(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimShopButton);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.AnimShopButton_gapBetweenCircle) {
                mGapBetweenCircle = ta.getDimension(index, mGapBetweenCircle);
            } else if (index == R.styleable.AnimShopButton_isAddFillMode) {
                isAddFillMode = ta.getBoolean(index, isAddFillMode);
            } else if (index == R.styleable.AnimShopButton_addEnableBgColor) {
                mAddEnableBgColor = ta.getColor(index, mAddEnableBgColor);
            } else if (index == R.styleable.AnimShopButton_addEnableFgColor) {
                mAddEnableFgColor = ta.getColor(index, mAddEnableFgColor);
            } else if (index == R.styleable.AnimShopButton_addDisableBgColor) {
                mAddDisableBgColor = ta.getColor(index, mAddDisableBgColor);
            } else if (index == R.styleable.AnimShopButton_addDisableFgColor) {
                mAddDisableFgColor = ta.getColor(index, mAddDisableFgColor);
            } else if (index == R.styleable.AnimShopButton_isDelFillMode) {
                isDelFillMode = ta.getBoolean(index, isDelFillMode);
            } else if (index == R.styleable.AnimShopButton_delEnableBgColor) {
                mDelEnableBgColor = ta.getColor(index, mDelEnableBgColor);
            } else if (index == R.styleable.AnimShopButton_delEnableFgColor) {
                mDelEnableFgColor = ta.getColor(index, mDelEnableFgColor);
            } else if (index == R.styleable.AnimShopButton_delDisableBgColor) {
                mDelDisableBgColor = ta.getColor(index, mDelDisableBgColor);
            } else if (index == R.styleable.AnimShopButton_delDisableFgColor) {
                mDelDisableFgColor = ta.getColor(index, mDelDisableFgColor);
            } else if (index == R.styleable.AnimShopButton_maxCount) {
                mMaxCount = ta.getInteger(index, mMaxCount);
            } else if (index == R.styleable.AnimShopButton_count) {
                mCount = ta.getInteger(index, mCount);
            } else if (index == R.styleable.AnimShopButton_radius) {
                mRadius = ta.getDimension(index, mRadius);
            } else if (index == R.styleable.AnimShopButton_circleStrokeWidth) {
                mCircleWidth = ta.getDimension(index, mCircleWidth);
            } else if (index == R.styleable.AnimShopButton_lineWidth) {
                mLineWidth = ta.getDimension(index, mLineWidth);
            } else if (index == R.styleable.AnimShopButton_numTextSize) {
                mTextSize = ta.getDimension(index, mTextSize);
            } else if (index == R.styleable.AnimShopButton_hintText) {
                mHintText = ta.getString(index);
            } else if (index == R.styleable.AnimShopButton_hintBgColor) {
                mHintBgColor = ta.getColor(index, mHintBgColor);
            } else if (index == R.styleable.AnimShopButton_hintFgColor) {
                mHintFgColor = ta.getColor(index, mHintFgColor);
            } else if (index == R.styleable.AnimShopButton_hingTextSize) {
                mHingTextSize = ta.getDimensionPixelSize(index, mHingTextSize);
            } else if (index == R.styleable.AnimShopButton_hintBgRoundValue) {
                mHintBgRoundValue = ta.getDimensionPixelSize(index, mHintBgRoundValue);
            } else if (index == R.styleable.AnimShopButton_ignoreHintArea) {
                ignoreHintArea = ta.getBoolean(index, false);
            } else if (index == R.styleable.AnimShopButton_perAnimDuration) {
                mPerAnimDuration = ta.getInteger(index, DEFAULT_DURATION);
            } else if (index == R.styleable.AnimShopButton_replenishText) {
                mReplenishText = ta.getString(index);
            } else if (index == R.styleable.AnimShopButton_replenishTextColor) {
                mReplenishTextColor = ta.getColor(index, mReplenishTextColor);
            } else if (index == R.styleable.AnimShopButton_replenishTextSize) {
                mReplenishTextSize = ta.getDimensionPixelSize(index, mReplenishTextSize);
            }
        }
        ta.recycle();
        initPaints();
    }

    /**
     * 加按钮
     */
    protected Paint mAddPaint;
    /**
     * 减按钮
     */
    protected Paint mDelPaint;
    //绘制数量的textSize
    protected float mTextSize;
    protected Paint mTextPaint;
    protected Paint.FontMetrics mFontMetrics;



    private void initPaints() {
        mAddPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isAddFillMode) {
            mAddPaint.setStyle(Paint.Style.FILL);
        } else {
            mAddPaint.setStyle(Paint.Style.STROKE);
        }
        mDelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isDelFillMode) {
            mDelPaint.setStyle(Paint.Style.FILL);
        } else {
            mDelPaint.setStyle(Paint.Style.STROKE);
        }

        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHintPaint.setStyle(Paint.Style.FILL);
        mHintPaint.setTextSize(mHingTextSize);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mFontMetrics = mTextPaint.getFontMetrics();
    }



    private void initDefaultValue(Context context) {
        mGapBetweenCircle = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34, context.getResources().getDisplayMetrics());

        isAddFillMode = true;
        mAddEnableBgColor = 0xFFFFDC5B;
        mAddEnableFgColor = Color.BLACK;
        mAddDisableBgColor = 0xff979797;
        mAddDisableFgColor = Color.BLACK;

        isDelFillMode = false;
        mDelEnableBgColor = 0xff979797;
        mDelEnableFgColor = 0xff979797;
        mDelDisableBgColor = 0xff979797;
        mDelDisableFgColor = 0xff979797;

/*        mMaxCount = 4;
        mCount = 1;*/

        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.5f, getResources().getDisplayMetrics());
        mCircleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.5f, getResources().getDisplayMetrics());

        mHintText = "加入购物车";
        mHintBgColor = mAddEnableBgColor;
        mHintFgColor = mAddEnableFgColor;
        mHingTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics());
        mHintBgRoundValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());

        mReplenishText = "补货中";
        mReplenishTextColor = 0xfff32d3b;
        mReplenishTextSize = mHingTextSize;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (wMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                //不超过父控件给的范围内，自由发挥
                int computeSize = (int) (getPaddingLeft() + mRadius * 2 +/* mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle + mRadius * 2 + getPaddingRight() + mCircleWidth * 2);
                wSize = computeSize < wSize ? computeSize : wSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                //自由发挥
                computeSize = (int) (getPaddingLeft() + mRadius * 2 + /*mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle + mRadius * 2 + getPaddingRight() + mCircleWidth * 2);
                wSize = computeSize;
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                int computeSize = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom() + mCircleWidth * 2);
                hSize = computeSize < hSize ? computeSize : hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                computeSize = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom() + mCircleWidth * 2);
                hSize = computeSize;
                break;
        }
        setMeasuredDimension(wSize, hSize);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = (int) (getPaddingLeft() + mCircleWidth);
        mTop = (int) (getPaddingTop() + mCircleWidth);
        mWidth = w;
        mHeight = h;
    }

    //展开动画结束后 才显示文字
    protected boolean isShowHintText;
    @Override
    protected void onDraw(Canvas canvas) {
        drawReplenish(canvas);
        if (!ignoreHintArea && isHintMode) {
            //add hint 展开动画
            //if (mCount == 0) {
            //背景
            mHintPaint.setColor(mHintBgColor);
            RectF rectF = new RectF(mLeft + (mWidth - mRadius * 2) * mAnimExpandHintFraction, mTop
                    , mWidth - mCircleWidth, mHeight - mCircleWidth);
            canvas.drawRoundRect(rectF, mHintBgRoundValue, mHintBgRoundValue, mHintPaint);
            if (isShowHintText) {
                //前景文字
                mHintPaint.setColor(mHintFgColor);
                // 计算Baseline绘制的起点X轴坐标
                int baseX = (int) (mWidth / 2 - mHintPaint.measureText(mHintText) / 2);
                // 计算Baseline绘制的Y坐标
                int baseY = (int) ((mHeight / 2) - ((mHintPaint.descent() + mHintPaint.ascent()) / 2));
                canvas.drawText(mHintText, baseX, baseY, mHintPaint);
            }
            //}
        } else {
            //动画 mAnimFraction ：减 0~1, 加 1~0 ,
            //动画位移Max,
            float animOffsetMax = (mRadius * 2 + /*mGap * 2 + mTextPaint.measureText(mCount + "")*/mGapBetweenCircle);
            //透明度动画的基准
            int animAlphaMax = 255;
            int animRotateMax = 360;


        }

    }

    private void drawReplenish(Canvas canvas) {
        if (isReplenish) {
            //计算baseline绘制的起点x轴坐标
            int baseX = (int) (mWidth / 2 - mReplenishPaint.measureText(mReplenishText) / 2);
            //计算baseline绘制的y坐标
            int baseY = (int) ((mHeight / 2) - ((mReplenishPaint.descent() + mReplenishPaint.ascent()) / 2));
            canvas.drawText(mReplenishText, baseX, baseY, mReplenishPaint);
            return;
        }
    }
}





























































