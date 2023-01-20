package com.ohunag.xposed_main.viewTree.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ohunag.xposed_main.util.GlideUtil;
import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.util.TryCatch;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class ImageViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof ImageView) {
            data.add(new ImageViewEditGroup.SetImgEdit());
        }
    }


    public static class SetImgEdit implements IViewEdit {


        @Override
        public String getValueName() {
            return "设置图片";
        }

        @Override
        public String getHint() {
            return "图片url";
        }

        @Override
        public String getValue(View view) {
            return "";
        }

        @SuppressLint("CheckResult")
        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            if (view instanceof ImageView) {
                TryCatch.run(() -> {
                    GlideUtil.getInstance().loadImage((ImageView) view, s);
                    ToastUtil.showLong(view.getContext(), "修改成功");
                }, e -> {
                    ToastUtil.showLong(view.getContext(), e.toString());
                });
            }

        }
    }

}
