package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewMsgAdapter extends BaseAdapter {
    private ViewNode viewNode;
    private  Map<String, NodeValue> valueMap;
    private List<String> keys=new ArrayList<>();
    public ViewMsgAdapter(ViewNode viewNode) {
        this.viewNode = viewNode;
        valueMap = viewNode.getValueMap();
        keys.addAll(valueMap.keySet());
    }

    @Override
    public int getCount() {
        return valueMap.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==0){
          return   "class";
        }
        if (position>0) {
            return keys.get(position -1);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view==null){
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_msg_xposed), parent, false);
        }

        TextView tv_type_view_msg_xposed=view.findViewWithTag("tv_type_view_msg_xposed");
        TextView tv_value_view_msg_xposed=view.findViewWithTag("tv_value_view_msg_xposed");
        if (position==0){
            tv_type_view_msg_xposed.setText("class");
            tv_value_view_msg_xposed.setText(viewNode.getViewClassName());
        }else {
            tv_type_view_msg_xposed.setText(String.valueOf(getItem(position)));
            Object object = valueMap.get(getItem(position)).getObject();
            tv_value_view_msg_xposed.setText(String.valueOf(object));
        }
        return view;
    }
}
