package com.ohuang.noxp_uiihook;

import android.app.Application;

import com.ohuang.noxp_ui_hook_main.UiHookManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UiHookManager.getInstance().init(this);
    }
}
