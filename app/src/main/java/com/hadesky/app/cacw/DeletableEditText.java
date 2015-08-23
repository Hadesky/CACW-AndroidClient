package com.hadesky.app.cacw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
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

    private int mUnderlineColor;
    private Paint mPaint;
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
                case R.styleable.DeletableEditText_underline_color:
                    mUnderlineColor = typedArray.getColor(attr, getResources().getColor(android.R.color.darker_gray));
                default:
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setStrokeWidth(3.0f);
        mPaint.setColor(mUnderlineColor);

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算icon绘制的位置
        mIconRightX = getMeasuredWidth() - getCompoundDrawablePadding() - getPaddingRight();
        mIconLeftX = getMeasuredWidth() - mClearIconSize - getCompoundDrawablePadding() - getPaddingRight();
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
            super.onDraw(canvas);
            int x=this.getScrollX();
            int w=this.getMeasuredWidth();
            canvas.drawLine(0, this.getHeight() - 1, w+x,
                    this.getHeight() - 1, mPaint);
    }

    public void setIsClearIconVisible(boolean isClearIconVisible) {
        this.isClearIconVisible = isClearIconVisible;
        if (isClearIconVisible) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, mClearIcon, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
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
