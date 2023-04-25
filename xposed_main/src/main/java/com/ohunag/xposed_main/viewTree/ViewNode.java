package com.ohunag.xposed_main.viewTree;

import android.view.View;
import android.view.ViewGroup;

import com.ohunag.xposed_main.bean.ViewRootMsg;
import com.ohunag.xposed_main.view.HookRootFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ViewNode {

    private ViewNode parent;

    private final List<ViewNode> childNode = new ArrayList<>();

    private final Map<String, NodeValue> valueMap = new TreeMap<>();

    private View view;

    private String viewClassName;

    private List<NodeValueIntercept> nodeValueIntercepts = new ArrayList<>();

    public ViewNode() {
    }

    public ViewNode(View view ) {
        this.view = view;
        viewClassName = view.getClass().getName();
        initChild();
    }

    public void init(List<NodeValueIntercept> nodeValueIntercepts){
        this.nodeValueIntercepts = nodeValueIntercepts;
        initValueMap();
        for (ViewNode viewNode : childNode) {
            viewNode.init(nodeValueIntercepts);
        }
    }

    private void initValueMap() {
        for (int i = 0; i < nodeValueIntercepts.size(); i++) {
            nodeValueIntercepts.get(i).onIntercept(valueMap, view,this);
        }
    }

    private void initChild() {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt != null && !(childAt instanceof HookRootFrameLayout)) {
                    ViewNode viewNode = new ViewNode(childAt);
                    addChildNode(viewNode);
                }
            }
        }
    }

    public ViewNode getParent() {
        return parent;
    }

    public void setParent(ViewNode parent) {
        this.parent = parent;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public List<ViewNode> getChildNode() {
        return childNode;
    }

    public Map<String, NodeValue> getValueMap() {
        return valueMap;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public boolean addChildNode(ViewNode viewNode) {
        if (viewNode == null) {
            return false;
        }
        for (int i = 0; i < childNode.size(); i++) {
            if (viewNode == childNode.get(i)) {
                return false;
            }
            if (childNode.get(i) != null && childNode.get(i).getView() == viewNode.getView()) {
                return false;
            }
        }
        viewNode.setParent(this);
        childNode.add(viewNode);
        return true;
    }

    public int getViewNodeCount() {
        return childNode.size();
    }

    public ViewNode getViewNodeForIndex(int index) {
        if (index >= 0 && index < childNode.size()) {
            return childNode.get(index);
        }
        return null;
    }

    public int getViewNodeIndex(ViewNode viewNode) {
        for (int i = 0; i < childNode.size(); i++) {
            if (viewNode == childNode.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public int inParentIndex() {
        if (parent != null) {
            return parent.getViewNodeIndex(this);
        }
        return -1;
    }

    public String getViewNodePath() {
        int i = inParentIndex();
        if (i == -1) {
            return viewClassName;
        }
        String viewNodePath = parent.getViewNodePath();

        return viewNodePath + " > [" + i + "]" + viewClassName;
    }


    /**
     * 从前面开始遍历
     *
     * @param foreachCallBack
     * @return
     */
    public boolean frontTraversal(ForeachCallBack foreachCallBack) {
        for (int i = 0; i < childNode.size(); i++) {
            if (childNode.get(i).frontTraversal(foreachCallBack)) {
                return true;
            }
        }
        return foreachCallBack.onIntercept(this);
    }

    /**
     * 从后面开始遍历  想遍历先遍历上层UI用这个
     *
     * @return
     */
    public boolean afterTraversal(ForeachCallBack foreachCallBack) {
        for (int i = childNode.size() - 1; i >= 0; i--) {
            if (childNode.get(i).afterTraversal(foreachCallBack)) {
                return true;
            }
        }
        return foreachCallBack.onIntercept(this);
    }

    /**
     * 遍历可见view 想遍历先遍历上层UI用这个
     *
     * @return
     */
    public boolean afterTraversalVisibleView(ForeachCallBack foreachCallBack) {
        if (view==null||view.getAlpha()==0||view.getVisibility()!=View.VISIBLE){
            return false;
        }
        for (int i = childNode.size() - 1; i >= 0; i--) {
            if (childNode.get(i).afterTraversalVisibleView(foreachCallBack)) {
                return true;
            }
        }

        return foreachCallBack.onIntercept(this);
    }

    public ViewRootMsg hasViewRootMsg(List<ViewRootMsg> viewRootMsgs){
        if (viewRootMsgs==null){
            return null;
        }
        for (ViewRootMsg viewRootMsg : viewRootMsgs) {
            if (viewRootMsg!=null&&viewRootMsg.getView()!=null){
                if (viewRootMsg.getView()==view){
                    return viewRootMsg;
                }
            }
        }
        if (parent!=null){
            return parent.hasViewRootMsg(viewRootMsgs);
        }
        return null;
    }

    public interface ForeachCallBack {
        /**
         * @param viewNode
         * @return ture  拦截不继续遍历
         */
        boolean onIntercept(ViewNode viewNode);
    }

    public interface NodeValueIntercept {

        /**
         * @param
         * @return ture  拦截不继续遍历
         */
        boolean onIntercept(Map<String, NodeValue> map, View view,ViewNode viewNode);
    }
}
