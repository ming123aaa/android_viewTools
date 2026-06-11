package com.ohunag.xposed_main;


import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import com.ohunag.xposed_main.bean.ViewRootMsg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.weishu.reflection.Reflection;

public class UiHook {
    public static final String TAG = "UiHook";
    public static Resources xpRes;
    public static Application application;
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


    public static void init(Application application,Resources xpRes, ViewListManager viewListManager, Type type, ClassLoader classLoader,boolean isUnseal) {
        UiHook.application=application;
        UiHook.xpRes = xpRes;
        UiHook.viewListManager = viewListManager;
        UiHook.type = type;
        UiHook.classLoader = classLoader;
        if (isUnseal&&application!=null) {
            try {
                Reflection.unseal(application);//反射隐藏api
            } catch (Throwable e) {

            }
        }
    }

    public static void setApplication(Application application){
        if (UiHook.application==null){
            UiHook.application=application;
            try {
                Reflection.unseal(application);//反射隐藏api
            } catch (Throwable e) {

            }
        }
    }

    public interface ViewListManager {
        List<View> getViews();

        List<ViewRootMsg> getDialog();
    }

    public enum Type {
        XPOSED, APP
    }

}
