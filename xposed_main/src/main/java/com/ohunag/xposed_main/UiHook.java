package com.ohunag.xposed_main;


import android.content.res.Resources;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class UiHook  {
    public static final String TAG = "UiHook";
    public static Resources xpRes;

    public static  List<View> rootViews = new LinkedList<>();
   private static ViewListManager viewListManager;
    public static List<View> getRootViews() {
        if (viewListManager!=null){
            List<View> views = viewListManager.getViews();
            if (views!=null){
                return views;
            }
        }
        return rootViews;
    }



    public static void init(Resources xpRes, ViewListManager viewListManager){
        UiHook.xpRes=xpRes;
        UiHook.viewListManager=viewListManager;
    }

    public interface ViewListManager{
        List<View> getViews();
    }

}
