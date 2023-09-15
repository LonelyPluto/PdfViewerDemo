// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

public class Stepper
{
    protected final View mPoster;
    protected final Runnable mTask;
    protected boolean mPending;
    
    public Stepper(final View v, final Runnable r) {
        this.mPoster = v;
        this.mTask = r;
        this.mPending = false;
    }
    
    @SuppressLint({ "NewApi" })
    public void prod() {
        if (!this.mPending) {
            this.mPending = true;
            if (Build.VERSION.SDK_INT >= 16) {
                this.mPoster.postOnAnimation((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Stepper.this.mPending = false;
                        Stepper.this.mTask.run();
                    }
                });
            }
            else {
                this.mPoster.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Stepper.this.mPending = false;
                        Stepper.this.mTask.run();
                    }
                });
            }
        }
    }
}
