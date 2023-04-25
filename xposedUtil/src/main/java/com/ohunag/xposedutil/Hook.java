package com.ohunag.xposedutil;

import android.util.Log;

import com.ohunag.xposedutil.IHook;

import java.lang.reflect.Member;
import java.util.Arrays;

public abstract class Hook extends IHook {

    public String className;

    public Hook(String className) {
        this.className = className;
    }

    public Hook(String className,ClassLoader classLoader){
        super(classLoader);
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    protected boolean beforeMethod(MethodHookParam param) {
        Member method = param.method;
        Object thisObject = param.thisObject;

        return false;
    }

    public void log(String s){
        Log.e(className + "Hook", s);

    }

    @Override
    protected boolean afterMethod(MethodHookParam param) {
        return false;
    }
}
