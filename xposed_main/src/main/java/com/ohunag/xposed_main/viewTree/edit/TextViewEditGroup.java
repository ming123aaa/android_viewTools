package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ohunag.xposed_main.util.ClipboardUtils;
import com.ohunag.xposed_main.util.StringUtil;
import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.io.IOException;
import java.util.List;

public class TextViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof TextView){
            data.add(new TextViewEdit());
            data.add(new TextCopyViewEdit());
            data.add(new TextViewIsSelect());

        }
    }

    public static class TextViewIsSelect implements IViewEdit{

        @Override
        public String getValueName() {
            return "TextSelectable";
        }

        @Override
        public String getHint() {
            return "文本是否可选中 false ture";
        }

        @Override
        public String getValue(View view) {

            return ((TextView) view).isTextSelectable()+"";
        }

        @Override
        public void setValue(Activity activity, View view, String s) throws IOException {

            if ("flase".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                ((TextView) view).setTextIsSelectable(false);
                ToastUtil.show(activity, "修改成功");
            }
            if ("ture".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s)) {
                ((TextView) view).setTextIsSelectable(true);
                ToastUtil.show(activity, "修改成功");
            }
        }
    }

    public static class TextViewEdit implements IViewEdit {

        @Override
        public String getValueName() {
            return "text";
        }

        @Override
        public String getHint() {
            return "请输入文本";
        }

        @Override
        public String getValue(View view) {
            return StringUtil.getString(((TextView) view).getText().toString(),100);
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            ((TextView) view).setText(s);
            ToastUtil.show(activity,"修改成功");
        }

    }

    public static class TextCopyViewEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "复制";
        }

        @Override
        public String getValueName() {
            return "文本复制";
        }

        @Override
        public String getHint() {
            return "不需要输入内容";
        }

        @Override
        public String getValue(View view) {
            return StringUtil.getString(((TextView) view).getText().toString(),100);
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            ClipboardUtils.copyText(((TextView) view).getText().toString(),activity);
            ToastUtil.show(activity,"已复制到剪贴板");
        }

    }

}
