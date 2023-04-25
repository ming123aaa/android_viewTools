package com.ohunag.xposed_main.viewTree.intercept;

import android.view.View;

import com.ohunag.xposed_main.bean.ViewRootMsg;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.List;
import java.util.Map;

public class FragmentNodeValueIntercept implements ViewNode.NodeValueIntercept{
    List<ViewRootMsg> viewRootMsgs;
    public FragmentNodeValueIntercept(List<ViewRootMsg> viewRootMsgs) {
        this.viewRootMsgs=viewRootMsgs;
    }

    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view,ViewNode viewNode) {
        if (view!=null&&viewNode!=null){
            ViewRootMsg viewRootMsg = viewNode.hasViewRootMsg(viewRootMsgs);
            if (viewRootMsg!=null){
                map.put("fragment", NodeValue.createNode(viewRootMsg.getViewName()));
            }
        }

        return false;
    }
}
