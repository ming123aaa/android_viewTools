package com.ohunag.xposed_main.viewTree.edit;

import android.app.Activity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ohunag.xposed_main.ui.ObjectMsgDailog;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.IViewEditGroup;

import java.util.List;

public class ListViewEditGroup implements IViewEditGroup {
    @Override
    public void addToList(List<IViewEdit> data, View view) {
        if (view instanceof ListView) {
            data.add(new AdapterEdit());
        }
    }


    public static class AdapterEdit implements IViewEdit {
        @Override
        public String editButtonName() {
            return "获取数据";
        }

        @Override
        public String getValueName() {
            return "adapter";
        }

        @Override
        public String getHint() {
            return "请输入文本";
        }

        @Override
        public String getValue(View view) {
            if (view instanceof ListView) {
                if (((ListView) view).getAdapter() != null) {
                    return ((ListView) view).getAdapter().getClass().getName();
                }
            }
            return "";
        }

        @Override
        public void setValue(Activity activity, View view, String s) {
            if (view instanceof ListView) {
                ListAdapter adapter = ((ListView) view).getAdapter();
                if (adapter!=null) {
                    ObjectMsgDailog objectToJsonDialog = new ObjectMsgDailog(activity);
                    objectToJsonDialog.setObject(adapter, ListAdapter.class.getName());
                    objectToJsonDialog.show();
                    Toast.makeText(activity, "展示", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "adapter为空", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
