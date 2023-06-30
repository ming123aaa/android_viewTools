package com.ohunag.xposed_main.view;

import android.view.View;

import com.ohunag.xposed_main.util.RefInvoke;

public class ViewListenerUtil {

    public static Object getOnTouchListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnTouchListener");
            return onTouchListener;
        }

        return null;
    }

    public static Object getOnClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            return RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnClickListener");
        }

        return null;
    }

    public static Object getOnLongClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnLongClickListener");
            return onTouchListener;
        }

        return null;
    }

    public static Object getOnKeyListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnKeyListener");
            return onTouchListener;
        }

        return null;
    }

    public static Object getOnContextClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnContextClickListener");
            return onTouchListener;
        }

        return null;
    }

    public static Object getOnCreateContextMenuListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnCreateContextMenuListener");
            return onTouchListener;
        }
        return null;
    }
}
