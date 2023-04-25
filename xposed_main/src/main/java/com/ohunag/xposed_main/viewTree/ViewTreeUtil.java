package com.ohunag.xposed_main.viewTree;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.ViewRootMsg;
import com.ohunag.xposed_main.viewTree.edit.ImageViewEditGroup;
import com.ohunag.xposed_main.viewTree.edit.TextViewEditGroup;
import com.ohunag.xposed_main.viewTree.edit.ViewEditGroup;
import com.ohunag.xposed_main.viewTree.edit.WebViewEditGroup;
import com.ohunag.xposed_main.viewTree.intercept.TextViewNodeValueIntercept;
import com.ohunag.xposed_main.viewTree.intercept.ViewNodeValueIntercept;
import com.ohunag.xposed_main.viewTree.intercept.WebViewNodeValueIntercept;
import com.ohunag.xposed_main.util.RefInvoke;

import java.util.ArrayList;
import java.util.List;

public class ViewTreeUtil {

    public static ViewNode getViewNode(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        return getViewNode(decorView);
    }

    public static ViewNode getViewNode(View view){
        List<ViewNode.NodeValueIntercept> data=new ArrayList<>();
        data.add(new ViewNodeValueIntercept());
        data.add(new TextViewNodeValueIntercept());
        data.add(new WebViewNodeValueIntercept());
        return new ViewNode(view,data);
    }

    public static List<IViewEdit> getIViewEdit(View view){
        List<IViewEditGroup> iViewEditGroups=new ArrayList<>();
        iViewEditGroups.add(new ImageViewEditGroup());
        iViewEditGroups.add(new WebViewEditGroup());
        iViewEditGroups.add(new TextViewEditGroup());
        iViewEditGroups.add(new ViewEditGroup());
        List<IViewEdit> data=new ArrayList<>();
        for (IViewEditGroup iViewEditGroup : iViewEditGroups) {
            iViewEditGroup.addToList(data,view);
        }
        return data;
    }

    public static String getViewType(View view) {
        if (view == null) {
            return "null";
        }
        if(view instanceof EditText){
            return "EditText";
        }
        if (view instanceof Button) {
            return "Button";
        }
        if (view instanceof TextView) {
            return "TextView";
        }

        if (view instanceof ImageView) {
            return "ImageView";
        }
        if (view instanceof ListView) {
            return "ListView";
        }
        if (view instanceof WebView) {
            return "WebView";
        }
        if (view instanceof TableLayout) {
            return "TableLayout";
        }
        if (view instanceof LinearLayout) {
            return "LinearLayout";
        }
        if (view instanceof FrameLayout) {
            return "FrameLayout";
        }
        if (view instanceof RelativeLayout) {
            return "RelativeLayout";
        }
        if (view instanceof GridLayout) {
            return "GridLayout";
        }
        if (view instanceof ViewGroup) {
            return "ViewGroup";
        }

        return "View";
    }

    public static void getFragmentViewRootMsg(Activity activity,List<ViewRootMsg> viewRootMsgs){
        if (viewRootMsgs==null||activity==null){
            return;
        }
        if (UiHook.type== UiHook.Type.APP){
            if (activity instanceof FragmentActivity){
                  traversalFragment(((FragmentActivity) activity).getSupportFragmentManager(),viewRootMsgs);
            }
        } else if (UiHook.type== UiHook.Type.XPOSED) {
            if (RefInvoke.matchClass(activity.getClass(),"androidx.fragment.app.FragmentActivity",UiHook.classLoader)) {
                xposedTraversalFragment(RefInvoke.invokeMethod(UiHook.classLoader,"androidx.fragment.app.FragmentActivity"
                        ,"getSupportFragmentManager",activity,new Class[]{},new Object[]{}),viewRootMsgs);
            }
        }
    }

    private static void xposedTraversalFragment(Object fragmentManager,List<ViewRootMsg> viewRootMsgs){
        if (fragmentManager==null||viewRootMsgs==null){
            return;
        }
        List<Object> fragments = (List<Object>) RefInvoke.invokeMethod(UiHook.classLoader,"androidx.fragment.app.FragmentManager"
                ,"getFragments",fragmentManager,new Class[]{},new Object[]{});
        for (Object fragment : fragments) {

            if (fragment!=null){
                View view= (View) RefInvoke.invokeMethod(UiHook.classLoader,"androidx.fragment.app.Fragment"
                        ,"getView",fragment,new Class[]{},new Object[]{});
                if (view!=null) {
                    viewRootMsgs.add(new ViewRootMsg(fragment.getClass().toString(),view));
                    xposedTraversalFragment( RefInvoke.invokeMethod(UiHook.classLoader,"androidx.fragment.app.Fragment"
                            ,"getChildFragmentManager",fragmentManager,new Class[]{},new Object[]{}), viewRootMsgs);
                }
            }
        }
    }

    private static void traversalFragment(FragmentManager fragmentManager,List<ViewRootMsg> viewRootMsgs){
        if (fragmentManager==null||viewRootMsgs==null){
            return;
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment!=null&&fragment.getView()!=null){
                viewRootMsgs.add(new ViewRootMsg(fragment.getClass().toString(),fragment.getView()));
                traversalFragment(fragment.getChildFragmentManager(),viewRootMsgs);
            }
        }
    }
}
