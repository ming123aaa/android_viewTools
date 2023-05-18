package com.ohunag.xposed_main.ui;

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
import com.ohunag.xposed_main.config.MainConfig;
import com.ohunag.xposed_main.util.FileUtils;
import com.ohunag.xposed_main.util.GlideUtil;
import com.ohunag.xposed_main.util.GsonUtil;
import com.ohunag.xposed_main.util.InputManagerUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectToJsonAdapter extends BaseAdapter {

    private List<Object> data = new ArrayList<>();
    private Activity activity;

    public ObjectToJsonAdapter(List<Object> objects, Activity activity) {
        this.activity = activity;
        data.addAll(objects);


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
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_edit_xposed), parent, false);
        }
        TextView tv_type_view_edit_xposed = view.findViewWithTag("tv_type_view_edit_xposed");
        EditText tv_value_view_edit_xposed = view.findViewWithTag("tv_value_view_edit_xposed");
        TextView tv_save_view_edit_xposed = view.findViewWithTag("tv_save_view_edit_xposed");
        tv_type_view_edit_xposed.setText(data.get(position).getClass().getSimpleName());
        tv_value_view_edit_xposed.setText(data.get(position).toString());

        tv_value_view_edit_xposed.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        InputManagerUtil.showInputMethod(v.getContext(), tv_value_view_edit_xposed);
                        break;
                }
                return false;
            }
        });

        tv_save_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = GsonUtil.toJson(UiHook.classLoader, data.get(position));
                String path=MainConfig.saveFilePath+"/"+System.currentTimeMillis()+".txt";
                try {
                    FileUtils.writeText(path,s);
                    Toast.makeText(activity,"保存到"+path,Toast.LENGTH_LONG).show();
                } catch (IOException e) {

                }

            }
        });

        return view;
    }
}