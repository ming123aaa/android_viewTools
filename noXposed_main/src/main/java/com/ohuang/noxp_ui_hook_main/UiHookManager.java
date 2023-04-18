package com.ohuang.noxp_ui_hook_main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.smallwindow.FloatViewManager;
import com.ohunag.xposed_main.smallwindow.SmallWindowView;
import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.view.HookRootFrameLayout;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UiHookManager {
    public static final String TAG = "UiHookManager";

    private UiHookManager() {
    }

    private static final class InstanceHolder {
        static final UiHookManager instance = new UiHookManager();
    }

    public static UiHookManager getInstance() {
        return InstanceHolder.instance;
    }

    private Application mApplication;


    public void init(Application application) {
        if (this.mApplication == null && application != null) {
            mApplication = application;
            MainConfig.packageName=application.getPackageName();
            UiHook.init(mApplication.getResources(), new UiHook.ViewListManager() {
                @Override
                public List<View> getViews() {
                    return getHookAllRootView();
                }
            });
            registerActivityCallBack();

        }
    }

    private static final String[] whiteView = {SmallWindowView.class.getName(), HookRootFrameLayout.class.getName()};//白名单View
    public boolean shouldHookView(View view) {
        if (view == null) {
            return false;
        }
        for (String s : whiteView) {
            if (s.equals(view.getClass().getName())) {
                return false;
            }
        }
        return true;
    }


    private List<View> getHookAllRootView() {
        //android.view.WindowManagerGlobal  -> public static WindowManagerGlobal getInstance()

        Object getInstance = RefInvoke.invokeStaticMethod("android.view.WindowManagerGlobal", "getInstance", new Class[]{}, new Object[]{});
        Log.d(TAG, "hookGetAllRootView: WindowManagerGlobal="+getInstance);
        //   private final ArrayList<View> mViews = new ArrayList<View>();
        List<View> mViews = (List<View>) RefInvoke.getFieldOjbect("android.view.WindowManagerGlobal", getInstance, "mViews");

        List<View> data=new ArrayList<>();
        for (int i = 0; i < mViews.size(); i++) {
            if (shouldHookView(mViews.get(i))){
                data.add(mViews.get(i));
            }
        }
        return data;
    }

    private void registerActivityCallBack() {
        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                FloatViewManager.getInstance().attach(activity);

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                FloatViewManager.getInstance().show(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                FloatViewManager.getInstance().hide(activity);
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                FloatViewManager.getInstance().onDestroy(activity);
            }
        });

    }


}
