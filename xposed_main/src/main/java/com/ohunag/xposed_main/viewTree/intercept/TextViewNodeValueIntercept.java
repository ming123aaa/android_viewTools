package com.ohunag.xposed_main.viewTree.intercept;

import static android.graphics.Typeface.BOLD_ITALIC;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.util.StringUtil;
import com.ohunag.xposed_main.util.UiUtil;
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
                map.put("text",  NodeValue.createNodeBold(StringUtil.getString(text.toString(),100)));
            }
            text=textView.getHint();
            if (text!=null) {
                map.put("hint",  NodeValue.createNodeBold(StringUtil.getString(text.toString(),100)));
            }
            textId(map,textView);
            map.put("textSize", NodeValue.createNodeBold(textView.getTextSize()));
            map.put("textStyle", NodeValue.createNodeBold(getTextStyle((TextView) view)));
            map.put("textColor",NodeValue.createNodeBold(getTextColor((TextView) view)));
            map.put("textColorHint",NodeValue.createNodeBold(getTextHintColor((TextView) view)));
            textDrawable(map,textView);
        }
        return false;
    }


    public void textDrawable(Map<String, NodeValue> map, TextView textView) {
        // compoundDrawable (left, top, right, bottom)
        Drawable[] drawables = textView.getCompoundDrawables();
        String[] names = {"DrawableLeft", "DrawableTop", "DrawableRight", "DrawableBottom"};
        for (int i = 0; i < 4; i++) {
            if (drawables[i] != null) {
                DrawableInfoUtil.drawableInfo(map, drawables[i], "text" + names[i], NodeValue.Type.bold);
            }
        }
        // compoundDrawablePadding
        try {
            int padding = textView.getCompoundDrawablePadding();
            if (padding > 0) {
                map.put("CompoundDrawablePadding", NodeValue.createNodeBold(String.valueOf(padding)));
            }
        } catch (Exception e) {

        }

    }


    public void textId(Map<String, NodeValue> map,TextView textView){
        try {
            Integer mTextId= (Integer) RefInvoke.getFieldOjbect(TextView.class.getClassLoader(),TextView.class.getName(),textView,"mTextId");
            if (mTextId!=null&&mTextId!=0){
                map.put("mTextId", NodeValue.createNodeBold(UiUtil.getResIdName(textView.getResources(),mTextId)));
            }
        } catch (Exception e) {

        }

        try {
            Integer mHintId= (Integer) RefInvoke.getFieldOjbect(TextView.class.getClassLoader(),TextView.class.getName(),textView,"mHintId");
            if (mHintId!=null&&mHintId!=0){
                map.put("mHintId", NodeValue.createNodeBold(UiUtil.getResIdName(textView.getResources(),mHintId)));
            }
        } catch (Exception e) {

        }


    }


    public String getTextStyle(TextView textView){
        if (textView.getTypeface()==null){
            return "正常字体/Normal";
        }
        if (textView.getTypeface().isBold()){
            return "加粗字体/Bold";
        }else if (textView.getTypeface().isItalic()){
            return "斜体/Italic";
        }else if (textView.getTypeface().getStyle()==BOLD_ITALIC){
            return "加粗斜体/Bold_Italic";
        }
        return "正常字体/Normal";
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
