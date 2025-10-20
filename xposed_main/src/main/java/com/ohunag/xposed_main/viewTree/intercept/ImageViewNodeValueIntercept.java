package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;

import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.Map;

public class ImageViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view, ViewNode viewNode) {
        if (view instanceof ImageView) {
            map.put("imageScaleType", NodeValue.createNode(getScaleType((ImageView) view)));
            getImageTint(map, (ImageView) view);
            map.put("adjustViewBounds", NodeValue.createNode(((ImageView) view).getAdjustViewBounds()));
        }
        return false;
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
            map.put("imageTintColor", NodeValue.createNode(colorIntToString(imageTintList.getDefaultColor())));
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
