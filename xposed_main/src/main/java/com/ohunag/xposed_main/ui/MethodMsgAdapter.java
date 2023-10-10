package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.FiedMsg;
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.FileUtils;
import com.ohunag.xposed_main.util.GsonUtil;
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
        TextView tv_type_view_edit_xposed = view.findViewWithTag("tv_type_view_edit_xposed");
        TextView tv_value_view_edit_xposed = view.findViewWithTag("tv_value_view_edit_xposed");
        TextView tv_save_view_edit_xposed = view.findViewWithTag("tv_save_view_edit_xposed");
        tv_type_view_edit_xposed.setText(fiedMsg.getReturnType().getName());
        tv_value_view_edit_xposed.setText(fiedMsg.getName()+"()运行保存");

        tv_type_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewClassTreeDialog viewClassTreeDialog = new ViewClassTreeDialog(activity);
                viewClassTreeDialog.setClass(fiedMsg.getReturnType());
                viewClassTreeDialog.show();
            }
        });

        tv_value_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Object invoke = fiedMsg.invoke(object);
                    if (invoke != null) {
                        String s = GsonUtil.toJson(UiHook.classLoader, invoke);
                        String path = MainConfig.saveFilePath + "/" + System.currentTimeMillis() + ".txt";
                        FileUtils.writeText(path, s);
                        Toast.makeText(activity, "保存到" + path, Toast.LENGTH_LONG).show();
                    } else {
                        ToastUtil.show(activity, "运行成功,无返回值");
                    }
                } catch (Exception e) {
                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                }
            }
        });
        tv_save_view_edit_xposed.setText("运行查看");
        tv_save_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Object invoke = fiedMsg.invoke(object);
                    if (invoke != null) {
                        ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                        objectMsgDailog.setObject(invoke);
                        objectMsgDailog.show();
                        ToastUtil.show(activity, "运行成功");
                    } else {
                        ToastUtil.show(activity, "运行成功,无返回值");
                    }
                } catch (Exception e) {
                    ToastUtil.show(activity, "运行失败" + e.getMessage());
                }
            }
        });

        return view;
    }
}
