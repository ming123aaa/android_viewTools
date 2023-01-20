package com.ohunag.xposed_main.smallwindow;

import android.view.MotionEvent;
import android.view.View;

public class TouchClickListener implements View.OnTouchListener {
    private View.OnClickListener listener;
    private long lastTime;
    private long delay = 2000L;
    float lastX;
    float lastY;
    boolean canClick = true;

    public TouchClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        boolean overstep = (rawX - lastX) * (rawX - lastX) + (rawY - lastY) * (rawY - lastY) < 400;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (canClick) {
                    canClick = overstep;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (canClick) {
                    canClick = overstep;
                    if (canClick) {
                        listener.onClick(v);
                    }
                }
                canClick = true;
                break;

        }
        return true;
    }
}
