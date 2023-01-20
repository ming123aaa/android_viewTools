package com.ohunag.xposed_main.viewTree;

public class NodeValue {
    public NodeValue(Type type, Object object) {
        this.type = type;
        this.object = object;
    }

    private Type type;
    private Object object;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public static NodeValue createNode(String value) {
        return new NodeValue(Type.str, value);
    }
    public static NodeValue createNode(boolean value) {
        return new NodeValue(Type.bool, value);
    }

    public static NodeValue createNode(int value) {
        return new NodeValue(Type.number_int, value);
    }

    public static NodeValue createNode(float value) {
        return new NodeValue(Type.number_float, value);
    }

    public static NodeValue createNode(double value) {
        return new NodeValue(Type.number_double, value);
    }

    public static NodeValue createNode(long value) {
        return new NodeValue(Type.number_long, value);
    }

    @Override
    public String toString() {
        return "NodeValue{" +
                "type=" + type +
                ", object=" + object +
                '}';
    }

    public enum Type {
        str, bool, number_int, number_long, number_double, number_float
    }
}
