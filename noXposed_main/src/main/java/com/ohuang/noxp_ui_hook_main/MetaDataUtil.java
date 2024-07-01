package com.ohuang.noxp_ui_hook_main;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

class MetaDataUtil {

    public static Boolean getMetaDataForApplication(Context context, String name) {
        boolean data = true;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                // 注意此处为ApplicationInfo 而不是 ActivityInfo,
                // 因为设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        context.getPackageName(),
                        PackageManager.GET_META_DATA
                );
                if (applicationInfo.metaData != null) {
                    data = applicationInfo.metaData.getBoolean(name,true);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }
}
