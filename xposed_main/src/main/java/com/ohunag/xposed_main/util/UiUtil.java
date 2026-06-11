package com.ohunag.xposed_main.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public class UiUtil {


    public static String getViewId(View view) {
        StringBuilder out = new StringBuilder();
        final int id = view.getId();
        if (id != 0) {
            out.append(" 0x");
            out.append(Integer.toHexString(id));
            final Resources r = view.getResources();
            if (id > 0 && r != null) {
                try {
                    String pkgname;
                    switch (id & 0xff000000) {
                        case 0x7f000000:
                            pkgname = "app";
                            break;
                        case 0x01000000:
                            pkgname = "android";
                            break;
                        default:
                            pkgname = r.getResourcePackageName(id);
                            break;
                    }
                    String typename = r.getResourceTypeName(id);
                    String entryname = r.getResourceEntryName(id);
                    out.append(" ");
                    out.append(pkgname);
                    out.append(":");
                    out.append(typename);
                    out.append("/");
                    out.append(entryname);
                } catch (Resources.NotFoundException e) {
                }
            }
        }
        return out.toString();
    }

    /**
     *
     * @param resources
     * @param id
     * @return
     */
    public static String getResIdName(Resources resources, int id){
        if (id==0){
            return "";
        }
        Resources res = resources;
        try {
//                        // 获取资源类型：返回 "id"
//                        String resourceType = res.getResourceTypeName(id);
//
//                        // 获取资源名称：返回 "btn_login"
//                        String resourceName = res.getResourceEntryName(id);
//
//                        // 获取包名：返回 "com.example.app"
//                        String packageName = res.getResourcePackageName(id);

            // 获取完整资源名：返回 "com.example.app:id/btn_login"
            return res.getResourceName(id);

        } catch (Resources.NotFoundException e) {
            // id 对应的资源不存在
         return getIdString(id);
        }
    }
    public static String getIdString(int id) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(id));
        return stringBuilder.toString();
    }

    /**
     * 从整个Activity开始截取
     *
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
     *
     * @param saveView
     * @return
     */
    public static Bitmap viewToBitmap(View saveView) {
        Bitmap bitmap;

        saveView.setDrawingCacheEnabled(true);
        saveView.buildDrawingCache();
        bitmap = saveView.getDrawingCache();


        return bitmap;
    }

    public static Bitmap getDownscaledBitmapForView(View view) {
        try {
            View screenView = view;
            Bitmap bitmap = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(), Bitmap.Config.ARGB_8888);//准备图片
            Canvas canvas = new Canvas(bitmap);//将bitmap作为绘制画布
            screenView.draw(canvas);//讲View特定的区域绘制到这个canvas（bitmap）上去，
            return bitmap;
        } catch (Exception e) {

        }

        return null;//得到最新的画布
    }

    public static Bitmap getDownscaledBitmapForView(View view, Rect crop, float downscaleFactor) {

        View screenView = view;

        int width = (int) (crop.width() * downscaleFactor);
        int height = (int) (crop.height() * downscaleFactor);
        float dx = -crop.left * downscaleFactor;
        float dy = -crop.top * downscaleFactor;

        if (width * height <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//准备图片
        Canvas canvas = new Canvas(bitmap);//将bitmap作为绘制画布
        Matrix matrix = new Matrix();
        matrix.preScale(downscaleFactor, downscaleFactor);
        matrix.postTranslate(dx, dy);
        canvas.setMatrix(matrix);//设置matrix
        screenView.draw(canvas);//讲View特定的区域绘制到这个canvas（bitmap）上去，
        return bitmap;//得到最新的画布
    }

    public static Bitmap drawableToBitamp(Drawable drawable) {
        //声明将要创建的bitmap
        Bitmap bitmap = null;
        //获取图片宽度
        int width = drawable.getIntrinsicWidth();
        //获取图片高度
        int height = drawable.getIntrinsicHeight();
        //图片位深，PixelFormat.OPAQUE代表没有透明度，RGB_565就是没有透明度的位深，否则就用ARGB_8888。详细见下面图片编码知识。
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        //创建一个空的Bitmap
        bitmap = Bitmap.createBitmap(width, height, config);
        //在bitmap上创建一个画布
        Canvas canvas = new Canvas(bitmap);
        //设置画布的范围
        drawable.setBounds(0, 0, width, height);
        //将drawable绘制在canvas上
        drawable.draw(canvas);
        return bitmap;
    }

}
