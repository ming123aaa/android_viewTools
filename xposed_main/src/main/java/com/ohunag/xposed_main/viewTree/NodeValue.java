package com.ohunag.xposed_main.viewTree;

public class NodeValue {
    private NodeValue(Type type, Object object) {
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

    public static NodeValue createNode(Object value) {
        return new NodeValue(Type.normal, value);
    }

    public static NodeValue createNode(Type type,Object value) {
        return new NodeValue(type, value);
    }


    public static NodeValue createNodeBold(Object value) {
        return new NodeValue(Type.bold, value);
    }

    public static NodeValue createNodeHighlight(Object value) {
        return new NodeValue(Type.highlight, value);
    }


    @Override
    public String toString() {
        return "NodeValue{" +
                "type=" + type +
                ", object=" + object +
                '}';
    }

    public enum Type {
        normal, bold, highlight
    }
}
