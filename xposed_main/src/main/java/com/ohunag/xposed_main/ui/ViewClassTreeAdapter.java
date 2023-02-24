package com.ohunag.xposed_main.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;

import java.util.ArrayList;
import java.util.List;

public class ViewClassTreeAdapter extends BaseAdapter {

    private List<String> data;

    public ViewClassTreeAdapter(List<String> data) {
        this.data = data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view==null){
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_class_tree_xposed), parent, false);
        }

        TextView textView=view.findViewWithTag("tv_name_item_class_xposed");
        textView.setText(data.get(position));
        return view;
    }
}
