// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public interface CancellableTaskDefinition<Params, Result>
{
    Result doInBackground(final Params... p0);
    
    void doCancel();
    
    void doCleanup();
}
