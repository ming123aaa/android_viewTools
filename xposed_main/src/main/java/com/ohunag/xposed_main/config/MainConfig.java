package com.ohunag.xposed_main.config;

import android.os.Environment;

import java.io.File;

public class MainConfig {

    public static String packageName = "com.ohunag.activityuihook";
    public static final String dirFile = "activityuihook";


    public static String getSavePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + dirFile + File.separator + MainConfig.packageName;
    }
}
