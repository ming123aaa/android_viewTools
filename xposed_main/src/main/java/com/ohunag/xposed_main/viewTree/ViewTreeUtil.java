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

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.ohunag.xposed_main.viewTree.edit.ImageViewEditGroup;
import com.ohunag.xposed_main.viewTree.edit.TextViewEditGroup;
import com.ohunag.xposed_main.viewTree.edit.ViewEditGroup;
import com.ohunag.xposed_main.viewTree.intercept.TextViewNodeValueIntercept;
import com.ohunag.xposed_main.viewTree.intercept.ViewNodeValueIntercept;
import com.ohunag.xposed_main.viewTree.intercept.WebViewNodeValueIntercept;

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
        iViewEditGroups.add(new TextViewEditGroup());
        iViewEditGroups.add(new ViewEditGroup());
        iViewEditGroups.add(new ImageViewEditGroup());
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
}
