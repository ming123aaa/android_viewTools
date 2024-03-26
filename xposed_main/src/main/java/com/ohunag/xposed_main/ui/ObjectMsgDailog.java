package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.FiedMsg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectMsgDailog {
    private Dialog dialog;
    private Activity activity;
    private ViewGroup dialog_object_msg_xposed;
    private TextView tv_close_xposed_class;
    private TextView tv_title;
    private TextView tv_tostring;
    private ListView list_close_xposed_class;
    private CheckBox cb_listState;

    private Object object;
    private String superClass;
    private EditText edit_search;
    private TextView btn_search;

    public ObjectMsgDailog(Activity activity) {
        this.activity = activity;
        dialog_object_msg_xposed = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.dialog_object_msg_xposed), null, false);
        tv_close_xposed_class = dialog_object_msg_xposed.findViewWithTag("tv_close_xposed_class");
        tv_title = dialog_object_msg_xposed.findViewWithTag("tv_title");
        tv_tostring = dialog_object_msg_xposed.findViewWithTag("tv_tostring");
        edit_search = dialog_object_msg_xposed.findViewWithTag("edit_search");
        btn_search = dialog_object_msg_xposed.findViewWithTag("btn_search");
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edit_search.getText().toString();
                boolean checked = cb_listState.isChecked();
                if (TextUtils.isEmpty(s)){
                   if (checked){
                       showMethod(null);
                   }else {
                       showFiedMsg(null);
                   }
                }else {
                    String[] split = s.split("\\+");
                    ArrayList<String> strings = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        String s1 = split[i];
                        if (!TextUtils.isEmpty(s1)){
                            strings.add(s1);
                        }
                    }
                    if (checked){
                        showMethod(strings);
                    }else {
                        showFiedMsg(strings);
                    }
                }
            }
        });
        tv_tostring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToString();
            }
        });
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (object != null) {
                    ViewClassTreeDialog viewClassTreeDialog = new ViewClassTreeDialog(activity);
                    viewClassTreeDialog.setClass(object.getClass());
                    viewClassTreeDialog.show();
                }
            }
        });
        cb_listState = dialog_object_msg_xposed.findViewWithTag("cb_listState");
        cb_listState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_listState.setText("查看参数");
                    showMethod();
                } else {
                    cb_listState.setText("查看方法");
                    showFiedMsg();
                }
            }
        });
        cb_listState.setText("查看方法");
        tv_close_xposed_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        list_close_xposed_class = dialog_object_msg_xposed.findViewWithTag("list_close_xposed_class");

        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar)
                .setView(dialog_object_msg_xposed)
                .setCancelable(false)
                .create();

    }

    public void setObject(Object object) {
        setObject(object, Object.class.getName());
    }

    public void setObject(Object object, String superClass) {
        if (object != null && superClass != null) {
            this.object = object;
            this.superClass = superClass;
            declared = null;
            methodList = null;
            showFiedMsg();
        }
    }

    private List<FiedMsg> declared;

    private void showFiedMsg() {
        if (object != null && superClass != null) {
            if (declared == null) {
                declared = getDeclared(object, superClass);
            }
            list_close_xposed_class.setAdapter(new ObjectMsgAdapter(declared, activity));
            tv_title.setText(object.getClass().getName());
        }
    }
    private void showFiedMsg(List<String> data) {
        if (object != null && superClass != null) {
            if (declared == null) {
                declared = getDeclared(object, superClass);
            }
            ArrayList<FiedMsg> fiedMsgs = new ArrayList<>();
            for (int i = 0; i < declared.size(); i++) {
                FiedMsg fiedMsg = declared.get(i);
                String s=fiedMsg.type+"  "+ fiedMsg.object.getClass().getName();
                String s1 = s.toLowerCase();
                if (isMatchString(data,s1)){
                    fiedMsgs.add(fiedMsg);
                }
            }
            list_close_xposed_class.setAdapter(new ObjectMsgAdapter(fiedMsgs, activity));
            tv_title.setText(object.getClass().getName());
        }
    }

    private boolean isMatchString(List<String> data,String s){
        if (s==null){
            return false;
        }
        if (data==null){
            return true;
        }
        for (int i = 0; i < data.size(); i++) {
            String s1 = data.get(i).toLowerCase();
            boolean contains = s.contains(s1);
            if (!contains){
                return false;
            }
        }
        return true;
    }



    private List<Method> methodList;

    private void showMethod(List<String> data) {
        if (object != null && superClass != null) {
            if (methodList == null) {
                methodList = getNoParameterMethod(object,superClass);
            }
            ArrayList<Method> methods = new ArrayList<>();
            for (int i = 0; i < methodList.size(); i++) {
                Method method = methodList.get(i);
                String s=method.getReturnType().getName()+"  "+ method.getName();
                String s1 = s.toLowerCase();
                if (isMatchString(data,s1)){
                    methods.add(method);
                }
            }
            list_close_xposed_class.setAdapter(new MethodMsgAdapter(methods, activity, object));
            tv_title.setText(object.getClass().getName());
        }
    }
    private void showMethod() {
        if (object != null && superClass != null) {
            if (methodList == null) {
                methodList = getNoParameterMethod(object,superClass);
            }
            list_close_xposed_class.setAdapter(new MethodMsgAdapter(methodList, activity, object));
            tv_title.setText(object.getClass().getName());
        }
    }

    private void showToString() {
        if (object != null ) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(object.toString());
            list_close_xposed_class.setAdapter(new  ViewClassTreeAdapter(strings));
            tv_title.setText(object.getClass().getName());
        }
    }

    /**
     * 获取无参方法
     *
     * @return
     */
    public List<Method> getNoParameterMethod(Object object, String superClass) {
        List<Method> data = new ArrayList<>();
        Class<?> aClass = object.getClass();

        while (aClass != null && !aClass.getName().equals(superClass) && aClass != Object.class) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                Class<?> returnType = declaredMethod.getReturnType();
                int parameterCount = declaredMethod.getParameterTypes().length;
                if (parameterCount == 0) {
                    declaredMethod.setAccessible(true);
                    data.add(declaredMethod);
                }
            }
            aClass = aClass.getSuperclass();
        }



        return data;
    }

    public List<FiedMsg> getDeclared(Object object, String superClass) {
        List<FiedMsg> data = new ArrayList<>();
        Class<?> aClass = object.getClass();
        while (aClass != null && !aClass.getName().equals(superClass) && aClass != Object.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                field.setAccessible(true);
                try {
                    Object o = field.get(object);
                    if (o != null) {
                        FiedMsg fiedMsg = new FiedMsg(o, field.getName());
                        data.add(fiedMsg);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            aClass = aClass.getSuperclass();
        }
        return data;
    }

    public List<String> getClassTree(Class clazz) {
        List<String> data = new ArrayList<>();
        Class thisClass = clazz;
        while (thisClass != null) {
            data.add(thisClass.getName());
            thisClass = thisClass.getSuperclass();
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
