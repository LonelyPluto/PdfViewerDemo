// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.content.Context;
import android.app.ProgressDialog;

class ProgressDialogX extends ProgressDialog
{
    private boolean mCancelled;
    
    ProgressDialogX(final Context context) {
        super(context);
        this.mCancelled = false;
    }
    
    boolean isCancelled() {
        return !this.mCancelled;
    }
    
    public void cancel() {
        this.mCancelled = true;
        super.cancel();
    }
}
