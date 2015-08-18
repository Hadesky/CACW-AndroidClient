package com.hadesky.app.cacw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by 45517 on 2015/8/11.
 */
public class CircularImage extends MaskedImage {
    private Paint mPaint;
    private int mCircleColor;
    private int mCircleWidth;

    public CircularImage(Context paramContext) {
        this(paramContext, null);
    }

    public CircularImage(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public CircularImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        TypedArray typedArray = getResources().obtainAttributes(paramAttributeSet, R.styleable.CircularImage);
        final int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CircularImage_circle_width:
                    mCircleWidth = (int) typedArray.getDimension(attr, 0);
                    break;
                case R.styleable.CircularImage_circle_color:
                    mCircleColor = typedArray.getColor(attr, getResources().getColor(android.R.color.white));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        paramCanvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2 - mCircleWidth / 2, mPaint);
    }

    public Bitmap createMask() {
        int i = getWidth();
        int j = getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float f1 = getWidth();
        float f2 = getHeight();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    }
}