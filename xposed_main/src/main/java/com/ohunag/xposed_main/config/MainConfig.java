package com.ohunag.xposed_main.config;

import android.os.Environment;

import java.io.File;

public class MainConfig {

    public static String packageName="com.ohunag.activityuihook";

    public static String saveFilePath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + MainConfig.packageName;
}
