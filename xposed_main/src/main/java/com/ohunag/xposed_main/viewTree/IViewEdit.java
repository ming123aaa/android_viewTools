package com.ohunag.xposed_main.viewTree;

import android.app.Activity;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IViewEdit {

    String getValueName();

    String getHint();

    String getValue(View view);

    void setValue(Activity activity,View view, String s) throws IOException;

}
