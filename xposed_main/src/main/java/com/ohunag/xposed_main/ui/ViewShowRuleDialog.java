package com.ohunag.xposed_main.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.viewTree.ViewNode;

public class ViewShowRuleDialog {

    private Dialog dialog;
    private Activity activity;
    private OnRuleListener listener;
    private ViewGroup dialog_view_show_rule_xposed;

    public ViewShowRuleDialog(Activity activity, ViewShowRule viewShowRule, OnRuleListener listener) {
        this.activity = activity;
        this.listener = listener;
        dialog_view_show_rule_xposed = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.dialog_view_show_rule_xposed), null, false);
        TextView tv_title = (TextView) dialog_view_show_rule_xposed.findViewWithTag("tv_title");
        TextView et_input = (TextView) dialog_view_show_rule_xposed.findViewWithTag("et_input");
        TextView tv_cancel = (TextView) dialog_view_show_rule_xposed.findViewWithTag("tv_cancel");
        TextView tv_ok = (TextView) dialog_view_show_rule_xposed.findViewWithTag("tv_ok");
        CheckBox check_view_visibility = (CheckBox) dialog_view_show_rule_xposed.findViewWithTag("check_view_visibility");

        TextView btn_textView = (TextView) dialog_view_show_rule_xposed.findViewWithTag("btn_textView");
        TextView btn_ImageView = (TextView) dialog_view_show_rule_xposed.findViewWithTag("btn_ImageView");
        TextView btn_WebView = (TextView) dialog_view_show_rule_xposed.findViewWithTag("btn_WebView");
        TextView btn_noClass = (TextView) dialog_view_show_rule_xposed.findViewWithTag("btn_noClass");

        RadioButton rb_all = (RadioButton) dialog_view_show_rule_xposed.findViewWithTag("rb_all");
        RadioButton rb_view = (RadioButton) dialog_view_show_rule_xposed.findViewWithTag("rb_view");
        RadioButton rb_viewGroup = (RadioButton) dialog_view_show_rule_xposed.findViewWithTag("rb_viewGroup");

        check_view_visibility.setChecked(viewShowRule.onlyVisibility);
        switch (viewShowRule.ruleType) {
            case ALL:
                rb_all.setChecked(true);
                break;

            case OnlyView:
                rb_view.setChecked(true);
                break;

            case OnlyViewGroup:
                rb_viewGroup.setChecked(true);
                break;
        }
        if (viewShowRule.viewClass != null) {
            et_input.setText(viewShowRule.viewClass.getName());
        } else {
            et_input.setText("");
        }
        btn_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText(TextView.class.getName());
            }
        });
        btn_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText(ImageView.class.getName());
            }
        });
        btn_WebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText(WebView.class.getName());
            }
        });
        btn_noClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
            }
        });

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
                try {
                    if (TextUtils.isEmpty(string)){
                        viewShowRule.viewClass = null;
                    }else {
                        viewShowRule.viewClass = UiHook.classLoader.loadClass(string);
                    }
                } catch (Throwable e) {
                    viewShowRule.viewClass = null;
                    ToastUtil.show(activity, "noFind class="+string);
                }
                viewShowRule.onlyVisibility = check_view_visibility.isChecked();
                if (rb_all.isChecked()) {
                    viewShowRule.ruleType = RuleType.ALL;
                } else if (rb_view.isChecked()) {
                    viewShowRule.ruleType = RuleType.OnlyView;
                } else if (rb_viewGroup.isChecked()) {
                    viewShowRule.ruleType = RuleType.OnlyViewGroup;
                }
                listener.onRule(viewShowRule);
                hide();
            }
        });
        dialog = new AlertDialog.Builder(activity, android.R.style.Theme_Black_NoTitleBar)
                .setView(dialog_view_show_rule_xposed)
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

    public interface OnRuleListener {
        void onRule(ViewShowRule viewShowRule);
    }

    public  enum RuleType {
        ALL,
        OnlyView,
        OnlyViewGroup
    }

    public static class ViewShowRule {
        public RuleType ruleType = RuleType.ALL;

        public boolean onlyVisibility = true;
        public Class<?> viewClass;

        public boolean isAddViewForRule(ViewNode node) {
            View view = node.getView();
            if (view == null) {
                return false;
            }
            if (ruleType != RuleType.ALL) {
                if (view instanceof ViewGroup) {
                    if (ruleType == RuleType.OnlyView) {
                        return false;
                    }
                } else {
                    if (ruleType == RuleType.OnlyViewGroup) {
                        return false;
                    }
                }
            }
            if (viewClass != null) {
                if (!isSuperClass(view.getClass(), viewClass)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isSuperClass(Class<?> clazz, Class<?> superClass) {
            if (clazz == superClass) {
                return true;
            }
            if (clazz.getSuperclass() == null || clazz.getSuperclass() == Object.class) {
                return false;
            }
            return isSuperClass(clazz.getSuperclass(), superClass);
        }
    }


}
