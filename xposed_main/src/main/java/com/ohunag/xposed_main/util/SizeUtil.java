package com.ohunag.xposed_main.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * dp、sp、px 单位转换工具类
 */
public final class SizeUtil {

    private SizeUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String px2dpString(Context context, float px){
        return px2dp(context, px)+"dp";
    }
    public static String px2spString(Context context, float px){
        return px2sp(context, px)+"sp";
    }

    public static String px2dpString(Resources res, float px){
        return px2dp(res, px)+"dp";
    }
    public static String px2spString(Resources res, float px){
        return px2sp(res, px)+"sp";
    }

    /**
     * dp 转 px
     */
    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * sp 转 px
     */
    public static int sp2px(Context context, float sp) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * px 转 sp
     */
    public static int px2sp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * dp 转 px (基于 Resources，不需要 Context)
     */
    public static int dp2px(Resources resources, float dp) {
        return (int) (dp * resources.getDisplayMetrics().density + 0.5f);
    }

    /**
     * px 转 dp (基于 Resources，不需要 Context)
     */
    public static int px2dp(Resources resources, float px) {
        return (int) (px / resources.getDisplayMetrics().density + 0.5f);
    }

    /**
     * sp 转 px (基于 Resources，不需要 Context)
     */
    public static int sp2px(Resources resources, float sp) {
        return (int) (sp * resources.getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * px 转 sp (基于 Resources，不需要 Context)
     */
    public static int px2sp(Resources resources, float px) {
        return (int) (px / resources.getDisplayMetrics().scaledDensity + 0.5f);
    }
}
