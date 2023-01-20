package com.ohunag.xposed_main.smallwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.view.HookRootFrameLayout;


public class FloatingMagnetHepler {

    Activity activity;
    HookRootFrameLayout frameLayout;
    FloatingMagnetView floatingMagnetView;
    ImageView imageView;

    boolean isShow = false;
    boolean isInit = false;
    View.OnClickListener listener;

    public FloatingMagnetHepler(Activity activity) {
        this.activity = activity;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
//    private int getDrawableRes(Context context, String type,String name) {
//        String packageName = "com.ohunag.activityuihook";
//        return context.getResources().getIdentifier(name, type, packageName);
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void init() {
        frameLayout = new HookRootFrameLayout(activity);
        floatingMagnetView = new FloatingMagnetView(activity);
        imageView = new ImageView(activity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100, 100);
        floatingMagnetView.addView(imageView, layoutParams);
        frameLayout.addView(floatingMagnetView, layoutParams);
        if (UiHook.xpRes!=null) {
            imageView.setBackground(UiHook.xpRes.getDrawable(R.mipmap.icon_activity_ui_j_and_xj));
        }else {
            imageView.setBackground(activity.getDrawable(R.mipmap.icon_activity_ui_j_and_xj));
        }
        floatingMagnetView.setMagnetViewListener(new FloatingMagnetView.MagnetViewListener() {
            @Override
            public void onUp(FloatingMagnetView floatingMagnetView) {

            }

            @Override
            public void onClick(FloatingMagnetView floatingMagnetView) {
                if (listener != null) {
                    listener.onClick(imageView);
                }
            }

            @Override
            public void onDown(FloatingMagnetView floatingMagnetView) {

            }

            @Override
            public void onRemove(FloatingMagnetView floatingMagnetView) {

            }
        });
        isInit = true;
    }

    public void show() {
        if (!isInit) {
            init();
            isInit = true;
        }
        if (!isShow) {
            isShow = true;
            View decorView = activity.getWindow().getDecorView();
            if (decorView instanceof ViewGroup) {
                ((ViewGroup) decorView).addView(frameLayout, new ViewGroup.LayoutParams(-1, -1));
            }
        }
    }

    public void hide() {
        if (!isInit) {
            return;
        }
        if (isShow) {
            isShow = false;
            View decorView = activity.getWindow().getDecorView();
            if (decorView instanceof ViewGroup) {
                ((ViewGroup) decorView).removeView(frameLayout);
            }
        }
    }

    public  int[] getXY(){
        int[] xy=new int[2];
        xy[0]= (int) floatingMagnetView.getX();
        xy[1]= (int) floatingMagnetView.getY();
        return xy;
    }
    public  void setXY(int[] xy){
       floatingMagnetView.setX(xy[0]);
       floatingMagnetView.setY(xy[1]);
    }
}
