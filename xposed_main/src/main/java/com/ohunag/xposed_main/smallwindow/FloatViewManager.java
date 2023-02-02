package com.ohunag.xposed_main.smallwindow;

import android.app.Activity;
import android.view.View;

import com.ohunag.xposed_main.ui.MainWindowUI;

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
        FloatViewHelper floatViewHelper = null;
        if (map.containsKey(activity)) {
            floatViewHelper = map.get(activity);
        } else {
            floatViewHelper = new FloatViewHelper(activity);
        }
        map.put(activity, floatViewHelper);
        floatViewHelper.show();
        return floatViewHelper;
    }

    public FloatViewHelper hide(Activity activity) {
        FloatViewHelper floatViewHelper = null;
        if (map.containsKey(activity)) {
            floatViewHelper = map.get(activity);
        } else {
            floatViewHelper = new FloatViewHelper(activity);
        }
        map.put(activity, floatViewHelper);
        floatViewHelper.hide();
        return floatViewHelper;
    }

    public void onDestroy(Activity activity) {
        if (mainWindowUIMap.containsKey(activity)) {
            mainWindowUIMap.remove(activity).hide();
        }
        if (map.containsKey(activity)) {
            map.remove(activity).hide();
        }


    }

}
