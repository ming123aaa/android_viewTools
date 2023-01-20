package com.ohunag.xposed_main;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.XModuleResources;
import android.os.Bundle;
import android.util.Log;


import com.ohunag.xposed_main.smallwindow.FloatViewManager;
import com.ohunag.xposed_main.smallwindow.FloatingMagnetManager;
import com.ohunag.xposedutil.Hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UiHook implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {
    public static final String TAG = "UiHook";
    public  static XModuleResources xpRes;
    private static String modulePath;
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        Log.d(TAG, "handleInitPackageResources: modulePath="+modulePath);



    }

    public static String packageName = "null";
    public static String processName = "null";
    public static boolean isHook=false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        packageName = lpparam.packageName;
        processName = lpparam.processName;
        Log.e(TAG, "handleLoadPackage: pkg=" + packageName + " processName=" + processName);
        if (!isHook) {
            isHook=true;
            new Hook(Instrumentation.class.getName(), lpparam.classLoader) {

                @Override
                protected boolean beforeMethod(MethodHookParam param) {
                    Activity activity = (Activity) param.args[0];
                    Log.d(TAG, "beforeMethod: " + param.method.getName());
                    switch (param.method.getName()) {
                        case "callActivityOnCreate":
                            FloatViewManager.getInstance().attach(activity);
                            break;
                        case "callActivityOnResume":
                            FloatViewManager.getInstance().show(activity);
                            break;
                        case "callActivityOnPause":
                            FloatViewManager.getInstance().hide(activity);
                            break;
                        case "callActivityOnDestroy":
                            FloatViewManager.getInstance().onDestroy(activity);
                            break;
                    }
                    return super.beforeMethod(param);
                }

                @Override
                public void hook() {
                    hookMethod("callActivityOnCreate", Activity.class, Bundle.class);
                    hookMethod("callActivityOnDestroy", Activity.class);
                    hookMethod("callActivityOnResume", Activity.class);
                    hookMethod("callActivityOnPause", Activity.class);
                }
            }.hook();


        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        modulePath = startupParam.modulePath;
        xpRes = XModuleResources.createInstance(modulePath, null);
        Log.d(TAG, "initZygote: "+modulePath);
    }
}
