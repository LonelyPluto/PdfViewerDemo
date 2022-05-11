// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

class OpaqueImageView extends AppCompatImageView
{
    public OpaqueImageView(final Context context) {
        super(context);
    }
    
    public boolean isOpaque() {
        return true;
    }
}
