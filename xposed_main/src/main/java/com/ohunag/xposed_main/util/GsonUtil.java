package com.ohunag.xposed_main.util;

import com.google.gson.Gson;

import java.lang.reflect.Method;

public class GsonUtil {

    public static String toJson(ClassLoader classLoader, Object o) {
        String json = "";
//        try {
//            Class<?> aClass = classLoader.loadClass(Gson.class.getName());
//            Object o1 = aClass.newInstance();
//            Method toJson = aClass.getDeclaredMethod("toJson", Object.class);
//            json = (String) toJson.invoke(o1, o);
//        } catch (Exception e) {
//               json=e.toString();
//        }
        try {
            Gson gson = new Gson();
            json = gson.toJson(o);
        }catch (Exception e){
            json=e.toString();
        }

        return json;
    }
}
