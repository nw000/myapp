package com.example.nw.myapp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nw on 2017/10/11.
 */

public class DeleteableEditText extends android.support.v7.widget.AppCompatEditText {


    private Drawable rightDrawable;

    public DeleteableEditText(Context context) {
        super(context);
        init();
    }

    public DeleteableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeleteableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //取得rigth位置的drawable
        Drawable[] drawables = getCompoundDrawables();
        rightDrawable = drawables[2];

        addTextChangedListener(new TextWatcherImpl());

        //设置右侧图片不可见
        setClearDrawableVisible(false);

    }

    private void setClearDrawableVisible(boolean visible) {
        Drawable drawable;
        if (visible) {
            drawable = rightDrawable;
        } else {
            drawable = null;
        }
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (isClean) {
                    setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public class TextWatcherImpl implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isVisible = getText().toString().length() > 0;
            setClearDrawableVisible(isVisible);
        }
    }

    public class FocusChangeListenerImpl implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                boolean isVisible = getText().toString().length() > 0;
                setClearDrawableVisible(isVisible);
            } else {
                setClearDrawableVisible(false);
            }

        }
    }


}
