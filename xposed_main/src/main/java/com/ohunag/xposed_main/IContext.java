package com.ohunag.xposed_main;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class IContext extends ContextWrapper {
    public IContext(Context base) {
        super(base);
    }

    @Override
    public AssetManager getAssets() {
        return UiHook.xpRes.getAssets();
    }

    @Override
    public Resources getResources() {
        return UiHook.xpRes.newTheme().getResources();
    }


}
