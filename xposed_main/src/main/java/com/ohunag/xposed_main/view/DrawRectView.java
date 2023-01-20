package com.ohunag.xposed_main.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawRectView extends FrameLayout {
    public DrawRectView(@NonNull Context context) {
        super(context);
    }

    public DrawRectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float mTop = 0f;
    private float mLeft = 0f;
    private float mBottom = 0f;
    private float mRight = 0f;
    private boolean isShowAble;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowAble) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            int[] ints = new int[2];
            getLocationOnScreen(ints);
            canvas.drawRect(mLeft-ints[0], mTop-ints[1], mRight-ints[0], mBottom-ints[1], paint);
        }
    }


    public void showEnable(boolean isShowAble) {
        this.isShowAble = isShowAble;
    }

    public void clear(){
        mTop = 0;
        mLeft = 0;
        mBottom = 0;
        mRight = 0;
        isShowAble = false;
        invalidate();
    }

    public void setRect(float left, float top, float right, float bottom) {
        mTop = top;
        mLeft = left;
        mBottom = bottom;
        mRight = right;
        isShowAble = true;
        invalidate();
    }

}
