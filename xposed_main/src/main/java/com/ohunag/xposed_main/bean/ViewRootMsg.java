package com.ohunag.xposed_main.bean;

import android.view.View;

import java.lang.ref.WeakReference;

public class ViewRootMsg {
    private String viewName;
    private WeakReference<View> view;

    public ViewRootMsg(String viewName, View view) {
        this.viewName = viewName;
        this.view = new WeakReference<>(view);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public View getView() {
        return view.get();
    }




}
