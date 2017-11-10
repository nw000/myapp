package com.example.nw.myapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.example.nw.myapp.R;

import static android.R.attr.radius;


/**
 * Created by nw on 2017/10/12.
 */

public class SaleProgressView extends View {

    //商品总数
    private int totalCount;
    //当前卖出数
    private int currentCount;
    //动画需要的
    private int progressCount;
    //售出比例
    private float scale;
    //边框颜色
    private int sideColor;
    //文字颜色
    private int textColor;
    //边框粗细
    private float sideWidth;
    private float textSize;
    private boolean isNeedAnim;

    private String nearOverText;
    private String overText;

    //边框所在的矩形
    private Paint sidePaint;
    private Paint srcPaint;
    private Paint textPaint;

    private PorterDuffXfermode mPorterDuffXfermode;
    private float nearOverTextWidth;
    private float overTextWidth;
    private int width;
    private int heigth;

    //背景矩形
    private RectF bgRectF;

    private float baseLineY;


    public SaleProgressView(Context context) {
        super(context);
    }

    public SaleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView);
        sideColor = ta.getColor(R.styleable.SaleProgressView_sideColor, 0xffff3c32);
        textColor = ta.getColor(R.styleable.SaleProgressView_textColor, 0xffff3c32);
        sideWidth = ta.getDimension(R.styleable.SaleProgressView_sideWidth, dp2px(2));
        overText = ta.getString(R.styleable.SaleProgressView_overText);
        nearOverText = ta.getString(R.styleable.SaleProgressView_nearOverText);
        textSize = ta.getDimension(R.styleable.SaleProgressView_textSize, sp2px(16));
        isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_isNeedAnim, true);
        ta.recycle();
    }


    private void initPaint() {
        sidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sidePaint.setStyle(Paint.Style.STROKE);
        sidePaint.setAntiAlias(true);
        sidePaint.setStrokeWidth(sideWidth);
        sidePaint.setColor(Color.RED);

        srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        nearOverTextWidth = textPaint.measureText(nearOverText);
        overTextWidth = textPaint.measureText(overText);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取view的宽高
        width = getWidth();
        heigth = getHeight();
        if (bgRectF == null) {
            bgRectF = new RectF(sideWidth, sideWidth, width - sideWidth, heigth - sideWidth);
        }
        if (baseLineY == 0.0f) {
            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            baseLineY = heigth / 2 - (fm.descent / 2 + fm.ascent / 2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isNeedAnim) {
            progressCount = currentCount;
        }

        if (totalCount == 0) {
            scale = 0;
        } else {
            scale = Float.parseFloat(new DecimalFormat("0.00").format((float) progressCount / (float) totalCount));
        }
        drawSide(canvas);
        drawBg(canvas);
        drawFg(canvas);
        drawText(canvas);
        if (progressCount != currentCount) {
            if (progressCount < currentCount) {
                progressCount++;
            } else {
                progressCount--;
            }
        }
        postInvalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawText(Canvas canvas) {
        String scaleText = new DecimalFormat("#%").format(scale);
        String saleText = String.format("已抢%s件", progressCount);

        float scaleTextWidth = textPaint.measureText(scaleText);

        Bitmap textBitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textPaint.setColor(textColor);

        if (scale < 0.8f) {
            textCanvas.drawText(saleText, dp2px(10), baseLineY, textPaint);
            textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
        } else if (scale < 1.0f) {
            textCanvas.drawText(nearOverText, width / 2 - nearOverTextWidth / 2, baseLineY, textPaint);
            textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
        } else {
            textCanvas.drawText(overText, width / 2 - overTextWidth / 2, baseLineY, textPaint);
        }

        textPaint.setXfermode(mPorterDuffXfermode);
        textPaint.setColor(Color.WHITE);
        textCanvas.drawRoundRect(
                new RectF(sideWidth, sideWidth, (width - sideWidth) * scale, heigth - sideWidth),
                radius, radius, textPaint);
        canvas.drawBitmap(textBitmap, 0, 0, null);
        textPaint.setXfermode(null);

    }

    private void drawBg(Canvas canvas) {
        //绘制背景
        Bitmap bgBitmap = null;
        if (bgBitmap == null) {
            bgBitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
        }
        Canvas bgCanvas = new Canvas(bgBitmap);
        Bitmap bgSrc = null;
        if (bgSrc == null) {
            bgSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
        }
        bgCanvas.drawRoundRect(bgRectF, heigth / 2, heigth / 2, srcPaint);
        srcPaint.setXfermode(mPorterDuffXfermode);
        bgCanvas.drawBitmap(bgSrc, null, bgRectF, srcPaint);
        canvas.drawBitmap(bgBitmap, 0, 0, srcPaint);
        srcPaint.setXfermode(null);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawFg(Canvas canvas) {
        if (scale == 0) {
            return;
        }
        Bitmap fgBitmap = null;
        if (fgBitmap == null) {
            Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
        }
        Canvas fgCanvas = new Canvas(fgBitmap);
        Bitmap fgSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.fg);
        fgCanvas.drawRoundRect(sideWidth, sideWidth, (width - sideWidth) * scale, heigth - sideWidth, width / 2, heigth / 2, srcPaint);
        srcPaint.setXfermode(mPorterDuffXfermode);
        fgCanvas.drawBitmap(fgSrc, null, bgRectF, srcPaint);
        canvas.drawBitmap(fgBitmap, 0, 0, null);
        srcPaint.setXfermode(null);
    }

    private void drawSide(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, heigth / 2.0f, heigth / 2.0f, sidePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = 200;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = 100;
        }
        setMeasuredDimension(widthSize, heightSize);

    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public void setTotalAndCurrentCount(int totalCount, int currentCount) {
        this.totalCount = totalCount;
        if (currentCount > totalCount) {
            currentCount = totalCount;
        }
        this.currentCount = currentCount;
        postInvalidate();
    }
}
