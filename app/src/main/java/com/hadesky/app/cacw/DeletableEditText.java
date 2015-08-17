package com.hadesky.app.cacw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by 45517 on 2015/8/17.
 */
public class DeletableEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearIcon;
    private int mClearIconSize;
    private int mIconLeftX;
    private int mIconRightX;
    private boolean isClearIconVisible = true;

    public DeletableEditText(Context context) {
        this(context, null);
    }

    public DeletableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public DeletableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.DeletableEditText);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.DeletableEditText_clear_icon:
                    mClearIcon = typedArray.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        if (mClearIcon == null) {
            throw new RuntimeException("没有为删除图标设置资源");
        }
        //获取icon大小
        mClearIconSize = Math.max(mClearIcon.getIntrinsicWidth(), mClearIcon.getIntrinsicHeight());
        mClearIcon.setBounds(0, 0, mClearIconSize, mClearIconSize);

        //默认隐藏clear按钮
        setIsClearIconVisible(false);
        //设置焦点变化的监听器
        setOnFocusChangeListener(this);
        //设置内容变化监听器
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isClearIconVisible) {
                boolean isTouch = event.getX() > mIconLeftX && event.getX() < mIconRightX;
                if (isTouch) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null && isClearIconVisible) {
            super.onDraw(canvas);
            canvas.save();
            //计算icon绘制的位置
            int iconTopY;
            mIconRightX = getMeasuredWidth() - getPaddingRight();
            mIconLeftX = getMeasuredWidth() - mClearIconSize - getPaddingRight();
            iconTopY = getMeasuredHeight() / 2 - mClearIcon.getIntrinsicHeight() / 2;
            canvas.translate(mIconLeftX, iconTopY);
            mClearIcon.draw(canvas);
        }else if (canvas != null) {
            super.onDraw(canvas);
        }
    }

    public void setIsClearIconVisible(boolean isClearIconVisible) {
        this.isClearIconVisible = isClearIconVisible;
        invalidate();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setIsClearIconVisible(getText().length() > 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setIsClearIconVisible(getText().length() > 0);
        } else {
            setIsClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
