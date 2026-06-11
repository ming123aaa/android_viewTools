package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.Map;

public class ImageViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view, ViewNode viewNode) {
        if (view instanceof ImageView) {

            map.put("imageScaleType", NodeValue.createNodeBold(getScaleType((ImageView) view)));
            map.put("adjustViewBounds", NodeValue.createNodeBold(((ImageView) view).getAdjustViewBounds()));
            getImageTint(map, (ImageView) view);
            imageResourceId(map, (ImageView) view);
        }
        return false;
    }

    public void imageResourceId(Map<String, NodeValue> map, ImageView imageView) {

        try {


            int id = (int) RefInvoke.getFieldOjbect(UiHook.classLoader,ImageView.class.getName(),imageView,"mResource");
            if (id != 0) {
                String fullName = UiUtil.getResIdName(imageView.getResources(),id);
                map.put("imageId", NodeValue.createNodeBold(fullName));
            }
        } catch (Throwable e) {
           e.printStackTrace();
        }

        try {

            Uri uri = (Uri) RefInvoke.getFieldOjbect(UiHook.classLoader,ImageView.class.getName(),imageView,"mUri");;
            if (uri!=null) {
                map.put("imageUri", NodeValue.createNodeBold(uri.toString()));
            }
        } catch (Throwable e) {
            e.printStackTrace();

        }
    }

    public String colorIntToString(int colorInt) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(Integer.toHexString(colorInt));
        return stringBuilder.toString();
    }

    private void getImageTint(Map<String, NodeValue> map, ImageView view) {
        ColorStateList imageTintList = view.getImageTintList();
        if (imageTintList != null) {
            map.put("imageTintColor", NodeValue.createNodeBold(colorIntToString(imageTintList.getDefaultColor())));
        }
    }

    private String getScaleType(ImageView view) {

        ImageView.ScaleType scaleType = view.getScaleType();
        if (scaleType != null) {
            return scaleType.name();
        } else {
            return "null";
        }
    }
}
