package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ohunag.xposed_main.BuildConfig;
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.TryCatch;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        data.add(new ClickableEdit());
        data.add(new EnableEdit());
        data.add(new VisibilityEdit());
        data.add(new SaveViewImgEdit());
    }


    public static class SaveViewImgEdit implements IViewEdit {


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
        public void setValue(Activity activity,View view, String s) {
            TryCatch.run(() -> {
                String fileName=s;
                if (TextUtils.isEmpty(fileName)){
                    fileName=System.currentTimeMillis()+"";
                }
                fileName=fileName+".png";
                try {
                    saveImage(activity, view, fileName);
                    String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + MainConfig.packageName;
                    Toast.makeText(view.getContext(),"路径:"+storePath,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }, e -> {
                Toast.makeText(view.getContext(),e.toString(),Toast.LENGTH_LONG).show();
            });
        }


        private boolean saveImage(Activity activity, View saveView, String fileName) throws IOException {
            Bitmap bitmap;
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + MainConfig.packageName;
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            File file = new File(appDir, fileName);
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int[] location = new int[2];
            saveView.getLocationOnScreen(location);

            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], saveView.getWidth(), saveView.getHeight());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            // 清理缓存
            view.destroyDrawingCache();

            return true;
        }
    }

    public static class ClickableEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "isClickable";
        }

        @Override
        public String getHint() {
            return "ture 或者 false";
        }

        @Override
        public String getValue(View view) {
            return String.valueOf(view.isClickable());
        }

        @Override
        public void setValue(Activity activity,View view, String s) {
            if ("flase".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                view.setClickable(false);
            }
            if ("ture".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
                view.setClickable(true);
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
            return "ture 或者 false";
        }

        @Override
        public String getValue(View view) {
            return String.valueOf(view.isEnabled());
        }

        @Override
        public void setValue(Activity activity,View view, String s) {
            if ("flase".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                view.setEnabled(false);
            }
            if ("ture".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
                view.setEnabled(true);
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
        public void setValue(Activity activity,View view, String s) {
            if ("0".equals(s)) {
                view.setVisibility(View.VISIBLE);
            }
            if ("4".equals(s) || "1".equals(s)) {
                view.setVisibility(View.INVISIBLE);
            }
            if ("8".equals(s) || "2".equals(s)) {
                view.setVisibility(View.GONE);
            }
        }
    }
}
