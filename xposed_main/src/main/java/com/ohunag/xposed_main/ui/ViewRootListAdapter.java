package com.ohunag.xposed_main.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.ViewRootMsg;

import java.lang.ref.WeakReference;
import java.util.List;

public class ViewRootListAdapter extends BaseAdapter {
    private List<ViewRootMsg> mData;
    private Listener listener;
    public ViewRootListAdapter(List<ViewRootMsg> data) {
        mData = data;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view==null){
            view = LayoutInflater.from(parent.getContext()).inflate(UiHook.xpRes.getLayout(R.layout.item_view_root_list_xposed), parent, false);
        }
        TextView tv_viewName_item_RootList_xposed=view.findViewWithTag("tv_viewName_item_RootList_xposed");
        TextView tv_show_item_RootList_xposed=view.findViewWithTag("tv_show_item_RootList_xposed");
        TextView tv_select_item_RootList_xposed=view.findViewWithTag("tv_select_item_RootList_xposed");
        WeakReference<View> weakReference=new WeakReference<>(mData.get(position).getView());
        if (weakReference.get()!=null){
            tv_viewName_item_RootList_xposed.setText(mData.get(position).getViewName());
        }
        tv_show_item_RootList_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onShow(weakReference);
                }

            }
        });
        tv_select_item_RootList_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onSelect(weakReference);
                }
            }
        });

        return view;
    }


    public interface Listener{
        void onShow(WeakReference<View> weakReference);
        void onSelect(WeakReference<View> weakReference);

    }
}
