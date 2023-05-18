package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;

import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.IOException;
import java.util.List;

public class WebViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof WebView) {
            data.add(new UrlEdit());
            data.add(new DebugEnableEdit());
        }
    }

    public static class UrlEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "url";
        }

        @Override
        public String getHint() {
            return "输入url";
        }

        @Override
        public String getValue(View view) {
            if (view instanceof WebView) {
                return ((WebView) view).getUrl();
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            try {
                if (view instanceof WebView) {
                    ((WebView) view).loadUrl(s);
                }
                ToastUtil.show(activity, "修改成功");
            } catch (Exception e) {
                ToastUtil.show(activity, e.toString());
            }

        }
    }

    public static class DebugEnableEdit implements IViewEdit {


        @Override
        public String getValueName() {
            return "webViewDebug";
        }

        @Override
        public String getHint() {
            return "1为debug模式 ";
        }

        @Override
        public String getValue(View view) {
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {
            if ("1".equals(s)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }else {
                WebView.setWebContentsDebuggingEnabled(false);
            }
        }
    }
}
