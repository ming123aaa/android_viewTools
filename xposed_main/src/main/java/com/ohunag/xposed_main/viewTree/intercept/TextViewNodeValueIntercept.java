package com.ohunag.xposed_main.viewTree.intercept;

import android.view.View;
import android.widget.TextView;

import com.ohunag.xposed_main.util.StringUtil;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.Map;

public class TextViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view) {
        if (view instanceof TextView){
            TextView textView=((TextView) view);
            CharSequence text =textView.getText();
            if (text!=null) {
                map.put("text", new NodeValue(NodeValue.Type.str, StringUtil.getString(text.toString(),100)));
            }
        }
        return false;
    }
}
