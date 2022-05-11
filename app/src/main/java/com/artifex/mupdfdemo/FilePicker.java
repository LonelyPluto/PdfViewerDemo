// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.net.Uri;

public abstract class FilePicker
{
    private final FilePickerSupport support;
    
    FilePicker(final FilePickerSupport _support) {
        this.support = _support;
    }
    
    void pick() {
        this.support.performPickFor(this);
    }
    
    abstract void onPick(final Uri p0);
    
    public interface FilePickerSupport
    {
        void performPickFor(final FilePicker p0);
    }
}
