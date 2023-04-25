package com.ohunag.xposed_main.viewTree.intercept;

import android.view.View;
import android.webkit.WebView;

import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.Map;

public class WebViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view,ViewNode viewNode) {
        if (view instanceof WebView){
            String url = ((WebView) view).getUrl();
            if (url!=null){
                map.put("url",NodeValue.createNode(url));
            }
        }
        return false;
    }
}
