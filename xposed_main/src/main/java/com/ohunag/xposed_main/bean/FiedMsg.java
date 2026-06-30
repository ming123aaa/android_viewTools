package com.ohunag.xposed_main.bean;

import java.lang.reflect.Field;

public class FiedMsg {
    public Object object;
    public String type;
    public Field field;
    public Object parent;


    public FiedMsg(Object object, String type,Field field,Object parent) {
        this.object = object;
        this.type = type;
        this.field=field;
        this.parent=parent;
    }


}
