package com.ohunag.activityuihook;


import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.res.XModuleResources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.ViewRootMsg;
import com.ohunag.xposed_main.smallwindow.FloatViewManager;
import com.ohunag.xposed_main.smallwindow.SmallWindowView;
import com.ohunag.xposed_main.view.HookRootFrameLayout;
import com.ohunag.xposedutil.Hook;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ActivityUiHook implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {
    public static final String TAG = "UiHook";
    public static XModuleResources xpRes;
    private static String modulePath;
    public static final List<View> rootViews = new LinkedList<>();

    public static final List<Dialog> dialogs = new ArrayList<>();

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        Log.d(TAG, "handleInitPackageResources: modulePath=" + modulePath);
    }

    public static String packageName = "null";
    public static String processName = "null";
    public static boolean isHook = false;
    private static final String[] whiteView = {SmallWindowView.class.getName(), HookRootFrameLayout.class.getName()};//白名单View

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        packageName = lpparam.packageName;
        processName = lpparam.processName;
        Log.e(TAG, "handleLoadPackage: pkg=" + packageName + " processName=" + processName);
        if (!isHook) {
            isHook = true;
            UiHook.classLoader = lpparam.classLoader;
            new Hook(Instrumentation.class.getName(), lpparam.classLoader) {

                @Override
                protected boolean beforeMethod(MethodHookParam param) {
                    Activity activity = (Activity) param.args[0];
                    Log.d(TAG, "Instrumentation beforeMethod: " + param.method.getName());
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

            new Hook("android.view.WindowManagerImpl", lpparam.classLoader) {

                @Override
                protected boolean beforeMethod(MethodHookParam param) {
                    View view = (View) param.args[0];
                    switch (param.method.getName()) {
                        case "removeView":
                        case "removeViewImmediate":
                            if (shouldHookView(view)) {
                                rootViews.remove(view);
                            }
                            break;
                    }

                    return super.beforeMethod(param);
                }

                @Override
                protected boolean afterMethod(MethodHookParam param) {
                    View view = (View) param.args[0];
                    switch (param.method.getName()) {
                        case "addView":
                            if (shouldHookView(view)) {
                                if (!rootViews.contains(view)) {
                                    rootViews.add(0, view);
                                }
//                                FloatViewManager.getInstance().floatViewToTop();
                            }
                            break;

                    }

                    logViewRoots();
                    return super.afterMethod(param);
                }

                @Override
                public void hook() {
                    hookAllMethod("addView");
                    hookAllMethod("removeView");
                    hookAllMethod("removeViewImmediate");
                }
            }.hook();

            new Hook(Dialog.class.getName(), lpparam.classLoader) {


                @Override
                public void hook() {
                    hookAllMethod("show");
                    hookAllMethod("hide");
                    hookAllMethod("dismiss");
                }


                @Override
                protected boolean beforeMethod(MethodHookParam param) {
                    switch (param.method.getName()) {
                        case "show":
                            if (!dialogs.contains(param.thisObject)) {
                                dialogs.add((Dialog) param.thisObject);
                            }
                            break;
                        case "hide":
                        case "dismiss":
                            dialogs.remove(param.thisObject);
                            break;

                    }
                    return super.beforeMethod(param);
                }
            }.hook();


        }
    }

    public void logViewRoots() {
        StringBuilder stringBuildr = new StringBuilder();
        for (int i = 0; i < rootViews.size(); i++) {
            stringBuildr.append("[" + i + "]").append(rootViews.get(i).toString()).append(",");
        }

    }

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

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        modulePath = startupParam.modulePath;
        xpRes = XModuleResources.createInstance(modulePath, null);
        UiHook.init(xpRes, new UiHook.ViewListManager() {
            @Override
            public List<View> getViews() {
                return rootViews;
            }

            @Override
            public List<ViewRootMsg> getDialog() {
                List<ViewRootMsg> data=new ArrayList<>();
                for (int i = 0; i < dialogs.size(); i++) {
                    Dialog dialog = dialogs.get(i);
                    if (dialog!=null) {
                        data.add(new ViewRootMsg(dialog.getClass().getName()+"[dialog]",dialog.getWindow().getDecorView(),dialog));
                    }
                }
                return data;
            }
        }, UiHook.Type.XPOSED);
        Log.d(TAG, "initZygote: " + modulePath);
    }
}
