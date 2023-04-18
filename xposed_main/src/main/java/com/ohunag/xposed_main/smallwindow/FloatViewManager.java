package com.ohunag.xposed_main.smallwindow;

import android.app.Activity;
import android.view.View;

import com.ohunag.xposed_main.ui.MainWindowUI;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class FloatViewManager {

    private FloatViewManager() {
    }

    private static final class InstanceHolder {
        static final FloatViewManager instance = new FloatViewManager();
    }

    public static FloatViewManager getInstance() {
        return InstanceHolder.instance;
    }

    Map<Activity, FloatViewHelper> map = new HashMap<>();
    Map<Activity, MainWindowUI> mainWindowUIMap = new HashMap<>();
    private WeakReference<Activity> resumeActivity=null;

    int[] xy=new int[2];

    public FloatViewHelper attach(Activity activity) {
        FloatViewHelper floatViewHelper = null;
        if (map.containsKey(activity)) {
            floatViewHelper = map.get(activity);
        } else {
            floatViewHelper = new FloatViewHelper(activity);
            floatViewHelper.setListener(v -> {
                MainWindowUI mainWindowUI = null;
                if (mainWindowUIMap.containsKey(activity)) {
                    mainWindowUI = mainWindowUIMap.get(activity);
                } else {
                    mainWindowUI = new MainWindowUI(activity);
                }
                mainWindowUIMap.put(activity, mainWindowUI);
                if (mainWindowUI != null) {
                    if (mainWindowUI.isShow) {
                        mainWindowUI.hide();
                    }
                    mainWindowUI.show();
                }
            });
        }
        map.put(activity, floatViewHelper);
        return floatViewHelper;
    }

    public FloatViewHelper show(Activity activity) {
        resumeActivity=new WeakReference<>(activity);
        FloatViewHelper floatViewHelper = null;
        if (map.containsKey(activity)) {
            floatViewHelper = map.get(activity);
        } else {
            floatViewHelper = new FloatViewHelper(activity);
        }
        map.put(activity, floatViewHelper);
        floatViewHelper.show();
        floatViewHelper.setXY(xy);
        return floatViewHelper;
    }

    public FloatViewHelper hide(Activity activity) {
        resumeActivity=null;
        FloatViewHelper floatViewHelper = null;
        if (map.containsKey(activity)) {
            floatViewHelper = map.get(activity);
        } else {
            floatViewHelper = new FloatViewHelper(activity);
        }
        map.put(activity, floatViewHelper);
        xy=floatViewHelper.getXY();
        floatViewHelper.hide();

        return floatViewHelper;
    }

    public void onDestroy(Activity activity) {
        resumeActivity=null;
        if (mainWindowUIMap.containsKey(activity)) {
            mainWindowUIMap.remove(activity).hide();
        }
        if (map.containsKey(activity)) {
            map.remove(activity).hide();
        }
    }

    /**
     * 弹出dialog 会导致floatView不在顶部  需要调用此方法
     */
    public void floatViewToTop(){
        if (resumeActivity!=null&&resumeActivity.get()!=null){
            if (map.containsKey(resumeActivity.get())) {
                FloatViewHelper floatViewHelper = map.get(resumeActivity.get());
                if (floatViewHelper.isShow) {
                    hide(resumeActivity.get());
                    show(resumeActivity.get());
                }
            }
        }
    }

}
