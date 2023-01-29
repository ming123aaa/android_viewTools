package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.util.List;

public class TextViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof TextView){
            data.add(new TextViewEdit());

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
            return ((TextView) view).getText().toString();
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            ((TextView) view).setText(s);
            ToastUtil.show(activity,"修改成功");
        }




    }

}
