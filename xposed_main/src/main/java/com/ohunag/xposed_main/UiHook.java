package com.ohunag.xposed_main;


import android.content.res.Resources;
import android.view.View;

import com.ohunag.xposed_main.bean.ViewRootMsg;

import java.util.ArrayList;
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

    public static List<ViewRootMsg> getDialogs(){
        if (viewListManager != null) {
            List<ViewRootMsg> views = viewListManager.getDialog();
            if (views != null) {
                return views;
            }
        }
        return new ArrayList<>();
    }


    public static void init(Resources xpRes, ViewListManager viewListManager, Type type,ClassLoader classLoader) {
        UiHook.xpRes = xpRes;
        UiHook.viewListManager = viewListManager;
        UiHook.type = type;
        UiHook.classLoader = classLoader;
    }

    public interface ViewListManager {
        List<View> getViews();

        List<ViewRootMsg> getDialog();
    }

    public enum Type {
        XPOSED, APP
    }

}
