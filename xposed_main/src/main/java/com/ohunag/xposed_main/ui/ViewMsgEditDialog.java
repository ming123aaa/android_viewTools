package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.view.MyListView;
import com.ohunag.xposed_main.viewTree.ViewNode;

public class ViewMsgEditDialog {
    private Dialog dialog;
    private Activity activity;
    private ViewGroup ll_viewMsg_xposed;
    private MyListView listView_viewMsg;
    private TextView tv_close_xposed_viewMsg;
    private TextView tv_edit_viewMsg_xposed;

    public ViewMsgEditDialog(Activity activity) {
        this.activity = activity;
        ll_viewMsg_xposed = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_msg_xposed), null, false);
        ViewGroup srv_viewMsg = ll_viewMsg_xposed.findViewWithTag("scv_viewMsg_xposed");

        listView_viewMsg = new MyListView(activity);
        srv_viewMsg.addView(listView_viewMsg, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_close_xposed_viewMsg = ll_viewMsg_xposed.findViewWithTag("tv_close_xposed_viewMsg");
        tv_edit_viewMsg_xposed = ll_viewMsg_xposed.findViewWithTag("tv_edit_viewMsg_xposed");
        tv_close_xposed_viewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Translucent_NoTitleBar)
                .setView(ll_viewMsg_xposed)
                .setCancelable(false)
                .create();

    }


    public void set_ll_viewMsg_xposed_edit(ViewNode viewNode) {

        listView_viewMsg.setAdapter(new ViewEditAdapter(viewNode, activity));
        tv_edit_viewMsg_xposed.setVisibility(View.GONE);

    }

    public void show() {

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog.show();
        }
    }

    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
