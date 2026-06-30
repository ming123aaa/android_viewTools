package com.ohunag.xposed_main.util;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefInvoke {


    public static boolean isLog = false;

    public static Object invokeStaticMethod(String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {
        return invokeStaticMethod(ClassLoader.getSystemClassLoader(), class_name, method_name, pareTyple, pareVaules);
    }

    public static Object invokeStaticMethod(ClassLoader classLoader, String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {

        try {
            Class obj_class = classLoader.loadClass(class_name);
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(null, pareVaules);
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException |
                 IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {

            e.printStackTrace();
        }
        return null;

    }

    public static Object invokeMethod(String class_name, String method_name, Object obj, Class[] pareTyple, Object[] pareVaules) {

        return invokeMethod(ClassLoader.getSystemClassLoader(), class_name, method_name, obj, pareTyple, pareVaules);

    }

    public static Object invokeMethod(ClassLoader classLoader, String class_name, String method_name, Object obj, Class[] pareTyple, Object[] pareVaules) {

        try {
            Class obj_class = classLoader.loadClass(class_name);
            //获取类中的所有方法，但不包括继承父类的方法
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(obj, pareVaules);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            if (isLog) {
                e.printStackTrace();
            }
        }

        return null;

    }

    public static Object getFieldOjbect(String class_name, Object obj, String filedName) {
        return getFieldOjbect(ClassLoader.getSystemClassLoader(), class_name, obj, filedName);
    }

    public static Object getFieldOjbect(ClassLoader classLoader, String class_name, Object obj, String filedName) {
        try {
            Class obj_class = classLoader.loadClass(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException |
                 IllegalAccessException | ClassNotFoundException e) {
            if (isLog) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static Object getStaticFieldOjbect(String class_name, String filedName) {
        return getStaticFieldOjbect(ClassLoader.getSystemClassLoader(), class_name, filedName);
    }

    public static Object getStaticFieldOjbect(ClassLoader classLoader, String class_name, String filedName) {

        try {
            Class obj_class = classLoader.loadClass(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(null);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException |
                 IllegalAccessException | ClassNotFoundException e) {
            if (isLog) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static void setFieldOjbect(String classname, String filedName, Object obj, Object filedVaule) {
        setFieldOjbect(ClassLoader.getSystemClassLoader(), classname, filedName, obj, filedVaule);
    }

    public static void setFieldOjbect(ClassLoader classLoader, String classname, String filedName, Object obj, Object filedVaule) {
        try {
            Class obj_class = classLoader.loadClass(classname);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (SecurityException | ClassNotFoundException | IllegalAccessException |
                 IllegalArgumentException | NoSuchFieldException e) {
            if (isLog) {
                e.printStackTrace();
            }
        }
    }

    public static void setStaticOjbect(String class_name, String filedName, Object filedVaule) {
        setStaticOjbect(ClassLoader.getSystemClassLoader(), class_name, filedName, filedVaule);
    }

    public static void setStaticOjbect(ClassLoader classLoader, String class_name, String filedName, Object filedVaule) {
        try {
            Class obj_class = classLoader.loadClass(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(null, filedVaule);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException |
                 IllegalAccessException | ClassNotFoundException e) {
            if (isLog) {
                e.printStackTrace();
            }
        }
    }

    public static boolean matchClass(Class<?> aclass, String className, ClassLoader classLoader) {
        Class<?> iClass = null;
        try {
            iClass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (iClass == null) {
            return false;
        }
        if (!iClass.isInterface()) {

            Class<?> thisClass = aclass;
            while (thisClass != null) {
                if (thisClass.getName().equals(className)) {
                    return true;
                }
                thisClass = thisClass.getSuperclass();
            }
        } else {

            Class<?> thisClass = aclass;
            while (thisClass != null) {
                for (Class<?> anInterface : thisClass.getInterfaces()) {
                    if (anInterface.getName().equals(className)) {
                        return true;
                    }
                }
                thisClass = thisClass.getSuperclass();
            }

        }
        return false;
    }


    /**
     * 判断指定对象是否为字符串、数字类型或布尔类型
     *
     * @param obj 要判断的对象
     * @return true 如果是字符串、数字类型或布尔类型，否则返回 false
     */
    public static boolean isStringNumberOrBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        return isStringNumberOrBoolean(obj.getClass());
    }

    public static Object as(String object, Class<?> clazz) {
        if (object == null) {
            return null;
        }
        if (object.equals("null")){
            return null;
        }
        if (clazz == null) {
            return object;
        }

        if (clazz == String.class||(clazz.isInterface()&&clazz==CharSequence.class)) {
            return object;
        }

        // 布尔类型（基本 + 包装）
        if (clazz == boolean.class || clazz == Boolean.class) {
            return (object.equals("1") || object.equalsIgnoreCase("true"));
        }

        // 字符类型（基本 + 包装）- 虽然不算数字，但常见于基础类型判断
        if (clazz == char.class || clazz == Character.class) {
            if (!object.isEmpty()){
                return object.charAt(0);
            }
            return null;
        }

        // 基本数字类型
        if (clazz == byte.class||clazz== Byte.class) {
            return Byte.parseByte(object);
        }

        if (clazz == short.class||clazz== Short.class) {
            return Short.parseShort(object);
        }
        if (clazz == int.class||clazz== Integer.class) {
            return Integer.parseInt(object);
        }
        if (clazz==long.class||clazz==Long.class){
              return Long.parseLong(object);
        }

        if (clazz == float.class||clazz == Float.class){
            return Float.parseFloat(object);
        }

        if (clazz==double.class||clazz== Double.class){
            return Double.parseDouble(object);
        }

       return GsonUtil.toObject(object, clazz);
    }

    public static boolean isContext(Class<?> clazz){
        return Context.class.isAssignableFrom(clazz);
    }

    /**
     * 判断指定的 Class 类型是否为字符串、数字类型或布尔类型
     *
     * @param clazz 要判断的 Class 对象
     * @return true 如果是字符串、数字类型或布尔类型，否则返回 false
     */
    public static boolean isStringNumberOrBoolean(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        // String 类型
        if (clazz == String.class||(clazz.isInterface()&&clazz==CharSequence.class)) {
            return true;
        }

        // 布尔类型（基本 + 包装）
        if (clazz == boolean.class || clazz == Boolean.class) {
            return true;
        }

        // 字符类型（基本 + 包装）- 虽然不算数字，但常见于基础类型判断
        if (clazz == char.class || clazz == Character.class) {
            return true;
        }

        // 数字类型（基本 + 包装）
        return isNumberType(clazz);
    }


    /**
     * 判断是否为数字类型
     */
    private static boolean isNumberType(Class<?> clazz) {
        // 基本数字类型
        if (clazz == byte.class || clazz == short.class ||
                clazz == int.class || clazz == long.class ||
                clazz == float.class || clazz == double.class) {
            return true;
        }

        // 数字包装类型（Number 的所有子类）
        return Number.class.isAssignableFrom(clazz);
    }

}