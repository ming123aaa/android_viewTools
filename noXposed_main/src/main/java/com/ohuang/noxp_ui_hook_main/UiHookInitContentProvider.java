package com.ohuang.noxp_ui_hook_main;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * 暂不使用
 */
public class UiHookInitContentProvider extends ContentProvider {

    static final String key = "activityHook_enable";

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            Boolean metaDataForApplication = MetaDataUtil.getMetaDataForApplication(getContext(), key);
            if (metaDataForApplication) {
                Context applicationContext = getContext().getApplicationContext();
                if (applicationContext instanceof Application) {
                    UiHookManager.getInstance().init((Application) applicationContext);
                }
            }

        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
