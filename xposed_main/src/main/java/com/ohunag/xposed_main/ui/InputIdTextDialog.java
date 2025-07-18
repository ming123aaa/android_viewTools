package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;

public class InputIdTextDialog {

    private Dialog dialog;
    private Activity activity;
    private OnInputTextListener listener;
    private ViewGroup dialog_input_text_xposed;

    public InputIdTextDialog(Activity activity, String title, String message, OnInputTextListener listener) {
        this.activity = activity;
        this.listener = listener;
        dialog_input_text_xposed = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.dialog_input_id_text_xposed), null, false);
        TextView tv_title = (TextView) dialog_input_text_xposed.findViewWithTag("tv_title");
        TextView et_input = (TextView) dialog_input_text_xposed.findViewWithTag("et_input");
        TextView tv_cancel = (TextView) dialog_input_text_xposed.findViewWithTag("tv_cancel");
        TextView tv_ok = (TextView) dialog_input_text_xposed.findViewWithTag("tv_ok");
        CheckBox check_view_visibility = (CheckBox) dialog_input_text_xposed.findViewWithTag("check_view_visibility");
        tv_title.setText(title);
        et_input.setHint( message);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = et_input.getText().toString();
                boolean checked = check_view_visibility.isChecked();
                if (listener!=null) {
                    listener.onInputText(string,checked);
                }
                hide();
            }
        });
        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Black_NoTitleBar)
                .setView(dialog_input_text_xposed)
                .create();
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

    public interface OnInputTextListener {
        void onInputText(String text,boolean isVisible);
    }
}
