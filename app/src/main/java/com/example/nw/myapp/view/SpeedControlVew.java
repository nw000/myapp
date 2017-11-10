package com.example.nw.myapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.type;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by nw on 2017/10/13.
 */

public class SpeedControlVew extends View implements Runnable {

    //速度范围的2个扇形外切矩形
    private RectF speedRectF, speedRectFInner;
    //圆心
    private int pointX, pointY;
    //画笔
    private Paint mPaint, textPaint, speedAreaPaint;

    private int radius;
    //速度
    private int speed;

    private int sRaduis;

    //文字的偏移量
    private float textScale;
    // 速度文字 绘制的XY坐标
    private int baseX, baseY;

    //开始重绘
    private boolean start = true;

    public void setStart(boolean start) {
        this.start = start;
    }

    //设置速度 并从新绘图
    public void setSpeed(int speed) {
        this.speed = speed;
        postInvalidate();
    }

    public SpeedControlVew(Context context) {
        super(context);
    }

    public SpeedControlVew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPants();
    }

    private void initPants() {
        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF343434);

        //文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);

        //速度画笔
        speedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedAreaPaint.setAntiAlias(true);
        speedAreaPaint.setStyle(Paint.Style.FILL);
        speedAreaPaint.setColor(0x7E3F51B5);

        // 设置速度范围扇形的渐变颜色
        Shader mShader = new LinearGradient(pointX - radius, pointY, pointX + radius, pointY,
                new int[]{0xFF445EED, 0xFF072AE9, 0xFF0625CE}, null, Shader.TileMode.CLAMP);
        speedAreaPaint.setShader(mShader);

        // 初始化速度范围的2个扇形外切矩形
        speedRectF = new RectF(pointX - radius + 10, pointY - radius + 10,
                pointX + radius - 10, pointY + radius - 10);
        speedRectFInner = new RectF(pointX - radius / 2, pointY - radius / 2,
                pointX + radius / 2, pointY + radius / 2);


    }

    private void drawText(Canvas canvas, int i) {
        String TEXT = String.valueOf(i);
        switch (i) {
            case 0:
                // 计算Baseline绘制的起点X轴坐标
                baseX = (int) (pointX - sRaduis * Math.cos(Math.PI / 5) + textPaint.measureText(TEXT) / 2 + textScale / 2);
                // 计算Baseline绘制的Y坐标
                baseY = (int) (pointY + sRaduis * Math.sin(Math.PI / 5) + textScale / 2);
                break;
            case 30:
                baseX = (int) (pointX - sRaduis + textPaint.measureText(TEXT) / 2);
                baseY = (int) (pointY + textScale);
                break;
            case 60:
                baseX = (int) (pointX - sRaduis * Math.cos(Math.PI / 5) + textScale);
                baseY = (int) (pointY - sRaduis * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 90:
                baseX = (int) (pointX - sRaduis * Math.cos(2 * Math.PI / 5) - textScale / 2);
                baseY = (int) (pointY - sRaduis * Math.sin(2 * Math.PI / 5) + 2 * textScale);
                break;
            case 120:
                baseX = (int) (pointX + sRaduis * Math.sin(Math.PI / 10) - textPaint.measureText(TEXT) / 2);
                baseY = (int) (pointY - sRaduis * Math.cos(Math.PI / 10) + 2 * textScale);
                break;
            case 150:
                baseX = (int) (pointX + sRaduis * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY - sRaduis * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 180:
                baseX = (int) (pointX + sRaduis - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY + textScale);
                break;
            case 210:
                baseX = (int) (pointX + sRaduis * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY + sRaduis * Math.sin(Math.PI / 5) - textScale / 2);
                break;
        }
        canvas.drawText(TEXT, baseX, baseY, textPaint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制内外圆
        drawCicle(canvas);
        //绘制速度
        drawSpeedArea(canvas);
        //绘制刻度
        drawScale(canvas);

        sRaduis = radius - 50;
        textScale = Math.abs(textPaint.descent() + textPaint.ascent()) / 2;
        for (int i = 0; i < 8; i++) {
            drawText(canvas, i * 30);
        }
        //绘制中间文字内容
        drawCenter(canvas);
    }

    /**
     * 绘制中间文字内容
     */
    private void drawCenter(Canvas canvas) {
        //速度
        textPaint.setTextSize(60);
        float tw = textPaint.measureText(String.valueOf(speed));
        baseX = (int) (pointX - tw / 2);
        baseY = (int) (pointY + Math.abs(textPaint.descent() + textPaint.ascent()) / 4);
        canvas.drawText(String.valueOf(speed), baseX, baseY, textPaint);

        //单位
        textPaint.setTextSize(20);
        tw = textPaint.measureText("km/h");
        baseX = (int) (pointX - tw / 2);
        baseY = (int) (pointY + radius / 4 + Math.abs(textPaint.descent() + textPaint.ascent()) / 4);
        canvas.drawText("km/h", baseX, baseY, textPaint);
    }

    private void drawScale(Canvas canvas) {
        mPaint.setColor(0xBF3F6AB5);
        for (int i = 0; i < 60; i++) {
            if (i % 6 == 0) {
                canvas.drawLine(pointX - radius + 10, pointY, pointX - radius + 50, pointY, mPaint);
            } else {
                canvas.drawLine(pointX - radius + 10, pointY, pointX - radius + 30, pointY, mPaint);
            }
            canvas.rotate(6, pointX, pointY);
        }
    }

    private void drawSpeedArea(Canvas canvas) {
        int degree;
        if (speed < 210) {
            degree = speed * 36 / 30;
        } else {
            degree = 210 * 36 / 30;
        }
        canvas.drawArc(speedRectF, 144, degree, true, speedAreaPaint);
        // TODO: 2016/5/12
        //不显示中间的内圈的扇形区域
        mPaint.setColor(0xFF343434);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(speedRectFInner, 0, degree, true, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }


    private void drawCicle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF343434);
        canvas.drawCircle(pointX, pointY, radius, mPaint);
        //外圈的两个圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xBF3F6AB5);
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(pointX, pointY, radius, mPaint);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(pointX, pointY, radius - 10, mPaint);

        //内圈2个圆
        mPaint.setStrokeWidth(5);
        mPaint.setColor(0xE73F51B5);
        canvas.drawCircle(pointX, pointY, radius / 2, mPaint);
        mPaint.setColor(0x7E3F51B5);
        canvas.drawCircle(pointX, pointY, radius / 2 + 5, mPaint);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = 200;
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = 200;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getWidth();
        int height = getHeight();
        //初始化半径,以及坐标
        radius = Math.min(width / 3, height / 3);
        pointX = pointY = Math.min(width, height) / 2;

    }

    @Override
    public void run() {
        int speedChange;
        while (start) {
            switch (type) {
                case 1://油门
                    speedChange = 3;
                    break;
                case 2://刹车
                    speedChange = -5;
                    break;
                case 3://手刹
                    speed = 0;
                default:
                    speedChange = -1;
                    break;
            }
            speed += speedChange;
            if (speed < 1) {
                speed = 0;
            }
            try {
                Thread.sleep(50);
                setSpeed(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}






























