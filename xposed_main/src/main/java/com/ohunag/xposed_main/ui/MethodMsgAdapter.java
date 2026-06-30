package com.ohunag.xposed_main.ui;

import static com.ohunag.xposed_main.util.RefInvoke.isContext;
import static com.ohunag.xposed_main.util.RefInvoke.isStringNumberOrBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.FiedMsg;
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.FileUtils;
import com.ohunag.xposed_main.util.GsonUtil;
import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodMsgAdapter extends BaseAdapter {
    private List<Method> data = new ArrayList<>();
    private Activity activity;
    private Object object;

    public MethodMsgAdapter(List<Method> objects, Activity activity, Object object) {
        this.activity = activity;
        data.addAll(objects);
        this.object = object;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_show_msg_xposed), parent, false);
        }


        Method fiedMsg = data.get(position);
        Class<?>[] parameterTypes = fiedMsg.getParameterTypes();
        StringBuilder string = getStrParamterTypes(parameterTypes);
        boolean isRunEnable=true;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (isContext(parameterTypes[i])){
                continue;
            }
            if (!isStringNumberOrBoolean(parameterTypes[i])) {
                isRunEnable = false;
                break;
            }
        }


        TextView tv_type_view_edit_xposed = view.findViewWithTag("tv_type_view_edit_xposed");
        TextView tv_value_view_edit_xposed = view.findViewWithTag("tv_value_view_edit_xposed");
        TextView tv_save_view_edit_xposed = view.findViewWithTag("tv_save_view_edit_xposed");
        tv_type_view_edit_xposed.setText(fiedMsg.getReturnType().getName());


        tv_value_view_edit_xposed.setText(fiedMsg.getName()+"("+string+")"+(isRunEnable?"保存结果":""));
        tv_value_view_edit_xposed.setEnabled(isRunEnable);
        tv_type_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewClassTreeDialog viewClassTreeDialog = new ViewClassTreeDialog(activity);
                viewClassTreeDialog.setClass(fiedMsg.getReturnType());
                viewClassTreeDialog.show();
            }
        });

        tv_save_view_edit_xposed.setEnabled(isRunEnable);
        tv_save_view_edit_xposed.setText(isRunEnable?"运行查看":"");




        tv_value_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fiedMsg.getParameterTypes().length == 0) {
                        Object invoke = fiedMsg.invoke(object);
                        saveObject(invoke);
                    } else {
                        Class<?>[] parameterTypes = fiedMsg.getParameterTypes();
                        StringBuilder string = getStrParamterTypes(parameterTypes);
                        new InputTextDialog((Activity) v.getContext(), "修改参数值", "请输入参数 参数直接用,隔开 转义符为\\", string.toString(), new InputTextDialog.OnInputTextListener() {
                            @Override
                            public void onInputText(String text) {
                                try {
                                    String[] split = splitWithEscape(text, ',', '\\');
                                    Object[] args = new Object[parameterTypes.length];
                                    for (int i = 0; i < parameterTypes.length; i++) {
                                        if (i < split.length) {
                                            if (RefInvoke.isContext(parameterTypes[i])){
                                                args[i]=activity;
                                            }else {
                                                args[i] = RefInvoke.as(split[i].trim(), parameterTypes[i]);
                                            }
                                        } else {
                                            args[i] = getDefaultValue(parameterTypes[i],activity);
                                        }
                                    }
                                    Object invoke = fiedMsg.invoke(object, args);
                                    saveObject(invoke);
                                } catch (Exception e) {
                                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).show();
                    }
                } catch (Exception e) {
                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                }
            }
        });

        tv_save_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (fiedMsg.getParameterTypes().length==0){
                        Object invoke=fiedMsg.invoke(object);
                        showObject(invoke);
                    }else {
                        Class<?>[] parameterTypes = fiedMsg.getParameterTypes();

                        StringBuilder string = getStrParamterTypes(parameterTypes);
                        new InputTextDialog((Activity) v.getContext(), "修改参数值", "请输入参数 参数直接用,隔开 转义符为\\", string.toString(), new InputTextDialog.OnInputTextListener() {
                            @Override
                            public void onInputText(String text) {
                                try {
                                    String[] split = splitWithEscape(text, ',', '\\');
                                    Object[] args = new Object[parameterTypes.length];
                                    for (int i = 0; i < parameterTypes.length; i++) {
                                        if (i < split.length) {
                                            if (RefInvoke.isContext(parameterTypes[i])){
                                                args[i]=activity;
                                            }else {
                                                args[i] = RefInvoke.as(split[i].trim(), parameterTypes[i]);
                                            }
                                        } else {
                                            args[i] = getDefaultValue(parameterTypes[i],activity);
                                        }
                                    }
                                    Object invoke = fiedMsg.invoke(object, args);
                                    showObject(invoke);
                                } catch (Exception e) {
                                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).show();
                    }


                } catch (Exception e) {
                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                }
            }
        });

        return view;
    }

    @NonNull
    private static StringBuilder getStrParamterTypes(Class<?>[] parameterTypes) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];

            if (i==0){
                string = new StringBuilder(parameterType.getSimpleName());
            }else {
                string.append(",").append(parameterType.getSimpleName());
            }
        }
        return string;
    }

    private void showObject(Object invoke){
        if (invoke != null) {
            ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
            objectMsgDailog.setObject(invoke);
            objectMsgDailog.show();
            ToastUtil.show(activity, "运行成功");
        } else {
            ToastUtil.show(activity, "运行成功,无返回值");
        }
    }

    private void saveObject(Object invoke) throws IOException {
        if (invoke != null) {
            String s = GsonUtil.toJson(UiHook.classLoader, invoke);
            String path = MainConfig.getSavePath() + "/" + System.currentTimeMillis() + ".txt";
            FileUtils.writeText(path, s);
            Toast.makeText(activity, "保存到" + path, Toast.LENGTH_LONG).show();
        } else {
            ToastUtil.show(activity, "运行成功,无返回值");
        }
    }

    private String[] splitWithEscape(String str, char delimiter, char escape) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inEscape = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (inEscape) {
                current.append(c);
                inEscape = false;
            } else if (c == escape) {
                inEscape = true;
            } else if (c == delimiter) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    private Object getDefaultValue(Class<?> clazz,Activity activity) {
        if (clazz.isPrimitive()) {
            if (clazz == boolean.class) return false;
            if (clazz == char.class) return '\0';
            if (clazz == byte.class) return (byte) 0;
            if (clazz == short.class) return (short) 0;
            if (clazz == int.class) return 0;
            if (clazz == long.class) return 0L;
            if (clazz == float.class) return 0.0f;
            if (clazz == double.class) return 0.0d;
            if (RefInvoke.isContext(clazz)) return activity;
        }
        return null;
    }
}
