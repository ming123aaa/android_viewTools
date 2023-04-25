package com.ohunag.xposed_main;


import android.content.res.Resources;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class UiHook {
    public static final String TAG = "UiHook";
    public static Resources xpRes;
    public static Type type;

    public static List<View> rootViews = new LinkedList<>();

    public static ClassLoader classLoader;
    private static ViewListManager viewListManager;

    public static List<View> getRootViews() {
        if (viewListManager != null) {
            List<View> views = viewListManager.getViews();
            if (views != null) {
                return views;
            }
        }
        return rootViews;
    }


    public static void init(Resources xpRes, ViewListManager viewListManager, Type type) {
        UiHook.xpRes = xpRes;
        UiHook.viewListManager = viewListManager;
        UiHook.type = type;
    }

    public interface ViewListManager {
        List<View> getViews();
    }

    public enum Type {
        XPOSED, APP
    }

}
