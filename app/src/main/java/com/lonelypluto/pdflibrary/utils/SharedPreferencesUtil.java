// 
// Decompiled by Procyon v0.5.36
// 

package com.lonelypluto.pdflibrary.utils;

import android.content.Context;
import android.app.Application;
import android.content.SharedPreferences;

public class SharedPreferencesUtil
{
    private static SharedPreferences sharedPreferences;
    
    public static void init(final Application application) {
        SharedPreferencesUtil.sharedPreferences = application.getSharedPreferences("lonelypluto_mupdf", 0);
    }
    
    public static int getSearchTextColor() {
        final int color = SharedPreferencesUtil.sharedPreferences.getInt("sp_color_search_text", -2130749662);
        return color;
    }
    
    public static void put(final String key, final Object value) {
        final SharedPreferences.Editor editor = SharedPreferencesUtil.sharedPreferences.edit();
        if (value.getClass() == Boolean.class) {
            editor.putBoolean(key, (boolean)value);
        }
        if (value.getClass() == String.class) {
            editor.putString(key, (String)value);
        }
        if (value.getClass() == Integer.class) {
            editor.putInt(key, (int)value);
        }
        editor.commit();
    }
    
    public static void cleanStringValue(final Context context, final String... keys) {
        for (final String key : keys) {
            final SharedPreferences settings = context.getSharedPreferences("lonelypluto_mupdf", 0);
            final SharedPreferences.Editor editor = settings.edit();
            if (settings.contains(key)) {
                editor.remove(key).commit();
            }
        }
    }
    
    public static void clear() {
        final SharedPreferences.Editor editor = SharedPreferencesUtil.sharedPreferences.edit();
        editor.remove("sp_color_search_text");
        editor.commit();
    }
}
