package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;

public class InputTextDialog {

    private Dialog dialog;
    private OnInputTextListener listener;
    private EditText et_input;

    public InputTextDialog(Activity activity, String title, String hint, OnInputTextListener listener) {
        this(activity, title, hint, "", listener);
    }

    public InputTextDialog(Activity activity, String title, String hint, String defaultValue, OnInputTextListener listener) {
        this.listener = listener;
        ViewGroup dialog_input_text = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.dialog_input_text), null, false);
        TextView tv_title = (TextView) dialog_input_text.findViewWithTag("tv_title");
        et_input = (EditText) dialog_input_text.findViewWithTag("et_input");
        TextView tv_cancel = (TextView) dialog_input_text.findViewWithTag("tv_cancel");
        TextView tv_ok = (TextView) dialog_input_text.findViewWithTag("tv_ok");
        tv_title.setText(title);
        et_input.setHint(hint);
        et_input.setText(defaultValue);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                }
                hide();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = et_input.getText().toString();
                if (listener != null) {
                    listener.onInputText(string);
                }
                hide();
            }
        });
        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Black_NoTitleBar)
                .setView(dialog_input_text)
                .create();
    }

    public void setText(String text) {
        if (et_input != null) {
            et_input.setText(text);
        }
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
        void onInputText(String text);

        void onCancel();
    }
}