package com.ohunag.xposed_main.ui;

import static com.ohunag.xposed_main.util.RefInvoke.isContext;
import static com.ohunag.xposed_main.util.RefInvoke.isStringNumberOrBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.FiedMsg;
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.FileUtils;
import com.ohunag.xposed_main.util.GsonUtil;
import com.ohunag.xposed_main.util.InputManagerUtil;
import com.ohunag.xposed_main.util.RefInvoke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectMsgAdapter extends BaseAdapter {
    private List<FiedMsg> data = new ArrayList<>();
    private Activity activity;
    private Listener listener;

    public ObjectMsgAdapter(List<FiedMsg> objects, Activity activity, Listener listener) {
        this.activity = activity;
        data.addAll(objects);
        this.listener = listener;

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
        FiedMsg fiedMsg = data.get(position);
        boolean isAdd = true;

        TextView tv_type_view_edit_xposed = view.findViewWithTag("tv_type_view_edit_xposed");
        TextView tv_value_view_edit_xposed = view.findViewWithTag("tv_value_view_edit_xposed");
        TextView tv_save_view_edit_xposed = view.findViewWithTag("tv_save_view_edit_xposed");
        TextView tv_changeValue_xposed = view.findViewWithTag("tv_changeValue_xposed");
        tv_type_view_edit_xposed.setText(fiedMsg.field.getType().getName());
        tv_value_view_edit_xposed.setText("name=" + fiedMsg.type + "\n value=" + fiedMsg.object.toString());

        Class<?> type = fiedMsg.field.getType();
        if (isStringNumberOrBoolean(fiedMsg.field.getType()) || isStringNumberOrBoolean(fiedMsg.object.getClass())) {
            tv_changeValue_xposed.setVisibility(View.VISIBLE);
        } else {
            tv_changeValue_xposed.setVisibility(View.GONE);
        }

        tv_changeValue_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputTextDialog((Activity) v.getContext(), "修改参数值", "请输入" + type.getSimpleName() + "类型", fiedMsg.object.toString(), new InputTextDialog.OnInputTextListener() {
                    @Override
                    public void onInputText(String text) {
                        Class<?> clazz = type;
                        if (type.isInterface()) {
                            clazz = fiedMsg.object.getClass();
                        }
                        Object o = RefInvoke.as(text, clazz);
                        try {
                            fiedMsg.field.setAccessible(true);
                            fiedMsg.field.set(fiedMsg.parent, o);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        listener.refresh();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
            }
        });

        tv_type_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewClassTreeDialog viewClassTreeDialog = new ViewClassTreeDialog(activity);
                viewClassTreeDialog.setClass(fiedMsg.object.getClass());
                viewClassTreeDialog.show();
            }
        });

        tv_value_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = GsonUtil.toJson(UiHook.classLoader, fiedMsg.object);
                String path = MainConfig.getSavePath() + "/" + System.currentTimeMillis() + ".txt";
                try {
                    FileUtils.writeText(path, s);
                    Toast.makeText(activity, "保存到" + path, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_save_view_edit_xposed.setText("查看");
        tv_save_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                objectMsgDailog.setObject(fiedMsg.object);
                objectMsgDailog.show();
            }
        });

        return view;
    }

    interface Listener {

        void refresh();

    }
}
