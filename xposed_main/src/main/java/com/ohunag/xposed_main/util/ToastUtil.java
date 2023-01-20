package com.ohunag.xposed_main.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context,String s){
        if (!TextUtils.isEmpty(s)) {
            Toast.makeText(context, s,Toast.LENGTH_SHORT ).show();
        }
    }
    public static void showLong(Context context,String s){
        if (!TextUtils.isEmpty(s)) {
            Toast.makeText(context, s,Toast.LENGTH_LONG).show();
        }
    }
}
