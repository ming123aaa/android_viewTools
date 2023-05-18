package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectToJsonDialog {
    public static final String TAG="ObjectToJsonDialog";
    private Dialog dialog;
    private Activity activity;
    private ViewGroup dialog_show_view_class_tree_xposed;
    private TextView tv_close_xposed_class;
    private ListView list_close_xposed_class;
    public ObjectToJsonDialog(Activity activity) {
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

        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar)
                .setView(dialog_show_view_class_tree_xposed)
                .setCancelable(false)
                .create();

    }

    public void setObject(Object object,String superClass){
        List<Object> declared = getDeclared(object, superClass);
        list_close_xposed_class.setAdapter(new ObjectToJsonAdapter(declared,activity));
    }

    public List<Object> getDeclared(Object object,String superClass){
        List<Object> data=new ArrayList<>();
        Class<?> aClass = object.getClass();
        while (aClass!=null&&!aClass.getName().equals(superClass)&&aClass!=Object.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field=declaredFields[i];
                field.setAccessible(true);

                try {
                    Object o = field.get(object);

                    if (o!=null){
                        data.add(o);
                    }
                } catch (IllegalAccessException e) {
                  e.printStackTrace();
                }
            }
            aClass=aClass.getSuperclass();
        }
        return data;
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
