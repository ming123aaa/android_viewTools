package com.ohunag.xposed_main.viewTree;

import android.view.View;

import java.util.List;

public interface IViewEditGroup {

    void addToList(List<IViewEdit> data, View view);
}
