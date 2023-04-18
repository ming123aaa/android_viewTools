package com.ohunag.xposed_main.util;

public class StringUtil {

    public static String getString(String s,int maxLength){
        if (maxLength>0&&s!=null){
            if (s.length()<=maxLength){
                return s;
            }
            return s.substring(0,maxLength);
        }
        return "";

    }
}
