package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class ViewClassTreeDialog {
    private Dialog dialog;
    private Activity activity;
    private ViewGroup dialog_show_view_class_tree_xposed;
    private TextView tv_close_xposed_class;
    private ListView list_close_xposed_class;
    public ViewClassTreeDialog(Activity activity) {
        this.activity=activity;
        dialog_show_view_class_tree_xposed = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.dialog_show_view_class_tree_xposed), null, false);
        tv_close_xposed_class = dialog_show_view_class_tree_xposed.findViewWithTag("tv_close_xposed_class");
        tv_close_xposed_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        list_close_xposed_class=dialog_show_view_class_tree_xposed.findViewWithTag("list_close_xposed_class");

        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Black_NoTitleBar)
                .setView(dialog_show_view_class_tree_xposed)
                .create();
    }

    public void setClass(Class clzz){
        list_close_xposed_class.setAdapter(new ViewClassTreeAdapter(getClassTree(clzz)));
    }

    public List<String>  getClassTree(Class clazz){
        List<String> data=new ArrayList<>();
        Class thisClass=clazz;
        while (thisClass!=null){
            data.add(thisClass.getName());
            thisClass=thisClass.getSuperclass();
        }
        return data;
    }

    public void show() {

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.show();
        }
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
