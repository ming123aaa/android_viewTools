package com.ohuang.noxp_uiihook;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainNoXpUiHookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_no_xp_ui_hook);
        findViewById(R.id.tv_main_no_xp_ui_hook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNoXpUiHookActivity.this,TestNoXpUiHookActivity.class));
            }
        });
    }
}