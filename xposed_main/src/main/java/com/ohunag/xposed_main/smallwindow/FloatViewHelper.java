package com.ohunag.xposed_main.smallwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;


public class FloatViewHelper {
    Context activity;
    SmallWindowView smallWindowView;
    ImageView imageView;
    WindowManager.LayoutParams layoutParams;
    boolean isShow = false;
    boolean isInit = false;
    View.OnClickListener listener;

    public FloatViewHelper(Context activity) {
        this.activity = activity;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    public void init() {
        smallWindowView = new SmallWindowView(activity);

        imageView = new ImageView(activity);
        if (UiHook.xpRes != null) {
            imageView.setImageDrawable(UiHook.xpRes.getDrawable(R.mipmap.icon_activity_ui_j_and_xj));
        }
        imageView.setOnTouchListener(new TouchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        }));
        smallWindowView.addView(imageView);


        layoutParams = new WindowManager.LayoutParams(150, 150,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        smallWindowView.init(getWindowManager(activity), layoutParams);

    }

    public int[] getXY() {
        int[] xy = new int[2];
        xy[0] = layoutParams.x;
        xy[1] = layoutParams.y;
        return xy;
    }

    public void setXY(int[] xy) {
        layoutParams.x = xy[0];
        layoutParams.y = xy[1];
        if (isShow) {
            getWindowManager(activity).updateViewLayout(smallWindowView, layoutParams);
        }
    }

    public void show() {
        if (!isInit) {
            init();
            isInit = true;
        }
        if (!isShow) {
            isShow = true;
            getWindowManager(activity).addView(smallWindowView, layoutParams);
        }
    }

    public WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void hide() {
        if (!isInit) {
            return;
        }
        if (isShow) {
            isShow = false;
            getWindowManager(activity).removeView(smallWindowView);
        }
    }

}
