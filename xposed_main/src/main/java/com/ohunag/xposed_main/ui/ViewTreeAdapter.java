package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.viewTree.ViewNode;

public class ViewTreeAdapter extends BaseAdapter {
    private ViewNode viewNode;
    private int selectId=0;
    private View.OnClickListener listener;

    public ViewTreeAdapter(ViewNode viewNode,int selectId) {
        this.viewNode = viewNode;

        if (selectId<0||selectId>=viewNode.getViewNodeCount()+1){
            selectId=0;
        }
        this.selectId=selectId;
    }


    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return viewNode.getViewNodeCount()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==0){
            return viewNode;
        }
        return viewNode.getViewNodeForIndex(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    public ViewNode getSelectNode(){
        Object item = getItem(selectId);
        return (ViewNode) item;
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view==null){
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_tree_xposed), parent, false);
        }

        TextView textView=view.findViewWithTag("tv_name_item_viewTree_xposed");
        ViewNode item = (ViewNode) getItem(position);
        if (position==0){
            textView.setText(item.getViewClassName());
        }else {
            textView.setText("[" + (position - 1) + "]" + item.getViewClassName()+"("+item.getViewNodeCount()+")");
        }
        if (position==selectId){
            textView.setTextColor(0xffff0000);
            textView.setBackground(UiHook.xpRes.getDrawable(R.drawable.btn_backgroud_wight_aaa));
        }else {
            textView.setTextColor(0xffffffff);
            textView.setBackgroundColor(0x00000000);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectId!=position) {
                    selectId = position;
                    if (listener!=null){
                        listener.onClick(v);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }
}
