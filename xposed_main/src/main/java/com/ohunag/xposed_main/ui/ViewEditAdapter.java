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
import com.ohunag.xposed_main.util.InputManagerUtil;
import com.ohunag.xposed_main.viewTree.IViewEdit;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewEditAdapter extends BaseAdapter {
    private ViewNode viewNode;
    private View mView;
    private List<IViewEdit> data=new ArrayList<>();
    private Activity activity;

    public ViewEditAdapter(ViewNode viewNode, Activity activity) {
        this.activity=activity;
        this.viewNode = viewNode;
        mView = viewNode.getView();
        if (mView != null) {
            List<IViewEdit> iViewEdit1 = ViewTreeUtil.getIViewEdit(viewNode.getView());
            for (IViewEdit iv : iViewEdit1) {
               if (iv.isEnable(viewNode.getView())){
                   data.add(iv);
               }
            }
        }
    }

    @Override
    public int getCount() {
        if (mView == null) {
            return 0;
        }
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
        tv_type_view_edit_xposed.setText(data.get(position).getValueName());
        tv_value_view_edit_xposed.setText(data.get(position).getValue(mView));
        tv_value_view_edit_xposed.setHint(data.get(position).getHint());
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
        tv_save_view_edit_xposed.setText(data.get(position).editButtonName());
        tv_save_view_edit_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = tv_value_view_edit_xposed.getText();
                if (text != null) {
                    try {
                        data.get(position).setValue(activity,mView, text.toString());
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return view;
    }
}
