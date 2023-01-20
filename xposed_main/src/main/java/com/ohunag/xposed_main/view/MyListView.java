package com.ohunag.xposed_main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyListView extends LinearLayout {
    public MyListView(Context context) {
        super(context);

    }

    public MyListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private BaseAdapter adapter;

    public void setAdapter(BaseAdapter adapter) {
        removeAllViews();
        if (adapter!=null) {
            int count = adapter.getCount();
            setOrientation(VERTICAL);
            for (int i = 0; i < count; i++) {
                View view = adapter.getView(i, null, this);
                if (view != null) {
                    addView(view);
                }

            }
        }
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }
}
