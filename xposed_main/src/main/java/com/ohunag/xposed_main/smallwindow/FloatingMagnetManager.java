package com.ohunag.xposed_main.smallwindow;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class FloatingMagnetManager {

    int[] xy = {100, 100};//初始位置

    private FloatingMagnetManager() {
    }

    private static final class InstanceHolder {
        static final FloatingMagnetManager instance = new FloatingMagnetManager();
    }

    public static FloatingMagnetManager getInstance() {
        return InstanceHolder.instance;
    }


    private final Map<Activity, FloatingMagnetHepler> map = new HashMap<>();

    private boolean isShowEnable =true;

    public boolean isShowEnable() {
        return isShowEnable;
    }

    public void setShowEnable(boolean showEnable) {
        isShowEnable = showEnable;
        if (!isShowEnable){
            for (Activity activity : map.keySet()) {
                hide(activity);
            }
        }
    }

    public FloatingMagnetHepler attach(Activity activity) {

        FloatingMagnetHepler floatingMagnetHepler = null;
        if (map.containsKey(activity)) {
            floatingMagnetHepler = map.get(activity);
        } else {
            floatingMagnetHepler = new FloatingMagnetHepler(activity);
        }
        map.put(activity, floatingMagnetHepler);
        return floatingMagnetHepler;
    }

    public FloatingMagnetHepler show(Activity activity) {

        FloatingMagnetHepler floatingMagnetHepler = null;
        if (map.containsKey(activity)) {
            floatingMagnetHepler = map.get(activity);
        } else {

            floatingMagnetHepler = new FloatingMagnetHepler(activity);
        }
        map.put(activity, floatingMagnetHepler);
        if (floatingMagnetHepler!=null&& isShowEnable) {
            floatingMagnetHepler.show();
            floatingMagnetHepler.setXY(xy);
        }
        return floatingMagnetHepler;
    }

    public FloatingMagnetHepler hide(Activity activity) {
        FloatingMagnetHepler floatingMagnetHepler = null;
        if (map.containsKey(activity)) {
            floatingMagnetHepler = map.get(activity);
            if (floatingMagnetHepler!=null) {
                floatingMagnetHepler.hide();
                if (isShowEnable) {
                    xy = floatingMagnetHepler.getXY();
                }
            }
            return floatingMagnetHepler;
        }
        return null;
    }

    public void onDestroy(Activity activity) {
        if (map.containsKey(activity)) {
            FloatingMagnetHepler remove = map.remove(activity);
            if (remove!=null){
                remove.hide();
                if (isShowEnable) {
                    xy = remove.getXY();
                }
            }
        }

    }


}
