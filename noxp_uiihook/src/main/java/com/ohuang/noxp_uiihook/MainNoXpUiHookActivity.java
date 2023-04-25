package com.ohuang.noxp_uiihook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

public class MainNoXpUiHookActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_no_xp_ui_hook);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main_no_xp_ui_hook, new BlankNoXpUiHookFragment()).commit();
        findViewById(R.id.tv_main_no_xp_ui_hook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNoXpUiHookActivity.this, TestNoXpUiHookActivity.class));
            }
        });
    }
}