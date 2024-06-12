package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.TryCatch;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof ImageView) {
            data.add(new ImageViewEditGroup.GetDrawableEdit());
        }
    }





    public static class GetDrawableEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "保存";
        }

        @Override
        public String getValueName() {
            return "获取图片(imageView)";
        }

        @Override
        public String getHint() {
            return "输入图片路径";
        }

        @Override
        public String getValue(View view) {
            if (view instanceof ImageView) {
                if (((ImageView) view).getDrawable() != null) {
                    return "";
                }
            }
            return "不可用";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            if (view instanceof ImageView) {
                Drawable drawable = ((ImageView) view).getDrawable();
                if (drawable != null) {
                    TryCatch.run(() -> {
                        Bitmap bitmap=UiUtil.drawableToBitamp(drawable);
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
                }else {
                    Toast.makeText(view.getContext(), "drawable为空", Toast.LENGTH_LONG).show();
                }
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

}
