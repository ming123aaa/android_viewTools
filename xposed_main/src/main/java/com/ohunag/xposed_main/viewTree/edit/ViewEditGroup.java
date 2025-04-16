package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.ui.ObjectMsgDailog;
import com.ohunag.xposed_main.ui.ViewClassTreeDialog;
import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.util.TryCatch;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.view.ViewListenerUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        data.add(new ClickableEdit());
        data.add(new EnableEdit());
        data.add(new VisibilityEdit());
        data.add(new WidthEdit());
        data.add(new HeightEdit());
        data.add(new SaveViewImgEdit());
        data.add(new GetForegroundDrawableEdit());
        data.add(new GetBackgroundDrawableEdit());
        data.add(new AlphaEdit());
        data.add(new showClassTree());
        data.add(new ClickViewEdit());
        data.add(new ViewMsgEdit());
        data.add(new ClickListenerEdit());
        data.add(new OnTouchListenerEdit());
        data.add(new OnLongClickListenerEdit());
        data.add(new OnKeyListenerEdit());
        data.add(new OnContextClickListenerEdit());
        data.add(new OnCreateContextMenuListenerEdit());
        data.add(new AdpaterEdit());
    }

    public static class AdpaterEdit implements IViewEdit {
        @Override
        public String getValueName() {
            return "Adapter";
        }

        @Override
        public String getHint() {
            return "getAdapter()";
        }

        private Object getAdapter(View view) {
            try {
               return view.getClass().getMethod("getAdapter").invoke(view);
            }catch (Throwable e){

            }
            return null;
        }

        @Override
        public String getValue(View view) {
            Object adapter=getAdapter(view);
            if (adapter!=null){
                return adapter.toString();
            }
            return null;
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object adapter=getAdapter(view);
            if (adapter!=null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(adapter);
                objectMsgDailog.show();
            }else {
                ToastUtil.show(activity,"adapter is null");
            }
        }
    }

    public static class WidthEdit implements IViewEdit {
        @Override
        public String getValueName() {
            return "设置宽度";
        }

        @Override
        public String getHint() {
            return "-1 MATCH -2 WRAP";
        }

        @Override
        public String getValue(View view) {
            return view.getWidth() + "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            try {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams!=null) {
                    Integer integer = Integer.valueOf(s);
                    if (integer==-1){
                        layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT;
                    }else if (integer==-2){
                        layoutParams.width=ViewGroup.LayoutParams.WRAP_CONTENT;
                    }else  if (integer<0){
                        layoutParams.width=0;
                    }else {
                        layoutParams.width=integer;
                    }
                    view.setLayoutParams(layoutParams);
                }
            } catch (Exception e) {
               ToastUtil.show(activity,e.toString());
            }
        }
    }

    public static class HeightEdit implements   IViewEdit{
        @Override
        public String getValueName() {
            return "设置高度";
        }

        @Override
        public String getHint() {
            return "-1 MATCH -2 WRAP";
        }

        @Override
        public String getValue(View view) {
            return view.getHeight() + "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            try {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams!=null) {
                    Integer integer = Integer.valueOf(s);
                    if (integer==-1){
                        layoutParams.height=ViewGroup.LayoutParams.MATCH_PARENT;
                    }else if (integer==-2){
                        layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                    }else  if (integer<0){
                        layoutParams.height=0;
                    }else {
                        layoutParams.height=integer;
                    }
                    view.setLayoutParams(layoutParams);
                }
            } catch (Exception e) {
                ToastUtil.show(activity,e.toString());
            }
        }

    }

    public static class ClickListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnClickListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnClickListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnClickListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnClickListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }

    public static class OnTouchListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnTouchListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnTouchListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnTouchListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnTouchListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }

    public static class OnLongClickListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnLongClickListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnLongClickListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnLongClickListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnLongClickListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }


    public static class OnKeyListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnKeyListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnKeyListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnKeyListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnKeyListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }

    public static class OnContextClickListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnContextClickListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnContextClickListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnContextClickListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnContextClickListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }


    public static class OnCreateContextMenuListenerEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public boolean isEnable(View view) {
            Object listener = ViewListenerUtil.getOnCreateContextMenuListener(view);
            return listener != null;
        }

        @Override
        public String getValueName() {
            return "OnCreateContextMenuListener";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            Object listener = ViewListenerUtil.getOnCreateContextMenuListener(view);
            if (listener != null) {
                return listener.getClass().getName();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Object listener = ViewListenerUtil.getOnCreateContextMenuListener(view);
            if (listener != null) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(listener);
                objectMsgDailog.show();
            } else {
                ToastUtil.show(activity, "没有设置listener");
            }
        }
    }

    public static class ViewMsgEdit implements IViewEdit {

        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public String getValueName() {
            return "ViewMsg";
        }

        @Override
        public String getHint() {
            return "无需输入";
        }

        @Override
        public String getValue(View view) {
            return view.getClass().getName();
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
            objectMsgDailog.setObject(view);
            objectMsgDailog.show();
        }
    }


    public static class ClickViewEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "点击";
        }

        @Override
        public String getValueName() {
            return "模拟点击";
        }

        @Override
        public String getHint() {
            return "performClick";
        }

        @Override
        public String getValue(View view) {
            return "view.performClick()";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            view.performClick();
        }
    }

    public static class GetBackgroundDrawableEdit implements IViewEdit {

        @Override
        public String editButtonName() {
            return "保存";
        }

        @Override
        public String getValueName() {
            return "获取背景图片";
        }

        @Override
        public String getHint() {
            return "输入图片路径";
        }

        @Override
        public boolean isEnable(View view) {
            return view.getBackground() != null;
        }

        @Override
        public String getValue(View view) {
            if (view.getBackground() != null) {
                return "";
            } else {
                return "不可用";
            }
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Drawable drawable = view.getBackground();
            if (drawable != null) {
                TryCatch.run(() -> {
                    Bitmap bitmap = UiUtil.drawableToBitamp(drawable);
                    String fileName = s;
                    if (TextUtils.isEmpty(fileName)) {
                        fileName = System.currentTimeMillis() + "";
                    }
                    fileName = fileName + ".png";
                    try {
                        saveImage(activity, bitmap, fileName);
                        String storePath = MainConfig.getSavePath();
                        Toast.makeText(view.getContext(), "路径:" + storePath, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }, e -> {
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                });
            } else {
                Toast.makeText(view.getContext(), "drawable为空", Toast.LENGTH_LONG).show();
            }

        }

        private void saveImage(Activity activity, Bitmap bitmap, String fileName) throws IOException {
            String storePath = MainConfig.getSavePath();
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File file = new File(appDir, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }

    public static class GetForegroundDrawableEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "保存";
        }

        @Override
        public String getValueName() {
            return "获取前景图片";
        }

        @Override
        public String getHint() {
            return "输入图片路径";
        }

        @Override
        public boolean isEnable(View view) {
            Drawable drawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                drawable = view.getForeground();
            }
            return drawable != null;
        }

        @Override
        public String getValue(View view) {
            Drawable drawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                drawable = view.getForeground();
            }
            if (drawable != null) {
                return "";
            } else {
                return "不可用";
            }
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            Drawable drawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                drawable = view.getForeground();
            }
            if (drawable != null) {
                Drawable finalDrawable = drawable;
                TryCatch.run(() -> {
                    Bitmap bitmap = UiUtil.drawableToBitamp(finalDrawable);
                    String fileName = s;
                    if (TextUtils.isEmpty(fileName)) {
                        fileName = System.currentTimeMillis() + "";
                    }
                    fileName = fileName + ".png";
                    try {
                        saveImage(activity, bitmap, fileName);
                        String storePath = MainConfig.getSavePath();
                        Toast.makeText(view.getContext(), "路径:" + storePath, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }, e -> {
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                });
            } else {
                Toast.makeText(view.getContext(), "drawable为空", Toast.LENGTH_LONG).show();
            }

        }

        private void saveImage(Activity activity, Bitmap bitmap, String fileName) throws IOException {
            String storePath = MainConfig.getSavePath();
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File file = new File(appDir, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }

    public static class showClassTree implements IViewEdit {
        @Override
        public String editButtonName() {
            return "查看";
        }

        @Override
        public String getValueName() {
            return "class";
        }

        @Override
        public String getHint() {
            return "查看class继承关系";
        }

        @Override
        public String getValue(View view) {
            return view.getClass().getName();
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            ViewClassTreeDialog viewClassTreeDialog = new ViewClassTreeDialog(activity);
            viewClassTreeDialog.setClass(view.getClass());
            viewClassTreeDialog.show();
            ToastUtil.show(activity, "修改成功");
        }
    }

    public static class AlphaEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "Alpha";
        }

        @Override
        public String getHint() {
            return "num 0~1";
        }

        @Override
        public String getValue(View view) {
            return "" + view.getAlpha();
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            try {
                float aFloat = Float.valueOf(s);
                view.setAlpha(aFloat);
                ToastUtil.show(activity, "修改成功");
            } catch (Exception e) {
                ToastUtil.show(activity, e.toString());
            }

        }
    }


    public static class SaveViewImgEdit implements IViewEdit {

        @Override
        public String editButtonName() {
            return "保存";
        }

        @Override
        public String getValueName() {
            return "将View保存为图片";
        }

        @Override
        public String getHint() {
            return "请输入保存的图片的文件名";
        }

        @Override
        public String getValue(View view) {
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            TryCatch.run(() -> {
                String fileName = s;
                if (TextUtils.isEmpty(fileName)) {
                    fileName = System.currentTimeMillis() + "";
                }
                fileName = fileName + ".png";
                try {
                    saveImage(activity, view, fileName);
                    String storePath = MainConfig.getSavePath();
                    Toast.makeText(view.getContext(), "路径:" + storePath, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }, e -> {
                Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();
            });
        }


        private void saveImage(Activity activity, View saveView, String fileName) throws IOException {
            String storePath = MainConfig.getSavePath();
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            File file = new File(appDir, fileName);
            Bitmap bitmap = UiUtil.getDownscaledBitmapForView(saveView);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Uri uri = Uri.fromFile(file);
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }
    }

    public static class ClickableEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "isClickable";
        }

        @Override
        public String getHint() {
            return "true 或者 false";
        }

        @Override
        public String getValue(View view) {
            return String.valueOf(view.isClickable());
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            if ("flase".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                view.setClickable(false);
                ToastUtil.show(activity, "修改成功");
            }
            if ("ture".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
                view.setClickable(true);
                ToastUtil.show(activity, "修改成功");
            }
        }
    }

    public static class EnableEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "isEnabled";
        }

        @Override
        public String getHint() {
            return "true 或者 false";
        }

        @Override
        public String getValue(View view) {
            return String.valueOf(view.isEnabled());
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            if ("flase".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                view.setEnabled(false);
                ToastUtil.show(activity, "修改成功");
            }
            if ("ture".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
                view.setEnabled(true);
                ToastUtil.show(activity, "修改成功");
            }
        }
    }

    public static class VisibilityEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "Visibility";
        }

        @Override
        public String getHint() {
            return "0=VISIBLE  4=INVISIBLE 8=GONE";
        }

        @Override
        public String getValue(View view) {
            return String.valueOf(view.getVisibility());
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            if ("0".equals(s)) {
                view.setVisibility(View.VISIBLE);
            }
            if ("4".equals(s) || "1".equals(s)) {
                view.setVisibility(View.INVISIBLE);
            }
            if ("8".equals(s) || "2".equals(s)) {
                view.setVisibility(View.GONE);
            }
            ToastUtil.show(activity, "修改成功");
        }
    }
}
