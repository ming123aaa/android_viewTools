package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import com.ohunag.xposed_main.util.StringUtil;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.Map;

public class TextViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view,ViewNode viewNode) {
        if (view instanceof TextView){
            TextView textView=((TextView) view);
            CharSequence text =textView.getText();
            if (text!=null) {
                map.put("text",  NodeValue.createNode(StringUtil.getString(text.toString(),100)));
            }
            text=textView.getHint();
            if (text!=null) {
                map.put("hint",  NodeValue.createNode(StringUtil.getString(text.toString(),100)));
            }
            map.put("textSize", NodeValue.createNode(textView.getTextSize()));
            map.put("textColor",NodeValue.createNode(getTextColor((TextView) view)));
            map.put("textColorHint",NodeValue.createNode(getTextHintColor((TextView) view)));
        }
        return false;
    }

    public String getTextColor(TextView textView){
        ColorStateList textColors = textView.getTextColors();
        if (textColors==null){
            return "null";
        }
        return colorIntToString(textColors.getDefaultColor());
    }
    public String getTextHintColor(TextView textView){
        ColorStateList hintTextColors = textView.getHintTextColors();
        if (hintTextColors==null){
            return "null";
        }
        return colorIntToString(hintTextColors.getDefaultColor());
    }

    public String colorIntToString(int colorInt){

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(Integer.toHexString(colorInt));
        return stringBuilder.toString();
    }
}
