package com.ohunag.xposed_main.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;

import java.io.File;

public class UiUtil {

    /**
     * 从整个Activity开始截取
     * @param activity
     * @param saveView
     * @return
     */
    public static Bitmap viewToBitmap(Activity activity, View saveView) {
        Bitmap bitmap;

        View view = activity.getWindow().getDecorView();


        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        saveView.getLocationOnScreen(location);
        bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], saveView.getWidth(), saveView.getHeight());


        return bitmap;
    }

    /**
     * 单独截取某个View
     * @param saveView
     * @return
     */
    public static Bitmap viewToBitmap( View saveView) {
        Bitmap bitmap;

        saveView.setDrawingCacheEnabled(true);
        saveView.buildDrawingCache();
        bitmap = saveView.getDrawingCache();


        return bitmap;
    }
}
