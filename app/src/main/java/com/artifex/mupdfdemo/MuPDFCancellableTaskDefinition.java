// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public abstract class MuPDFCancellableTaskDefinition<Params, Result> implements CancellableTaskDefinition<Params, Result>
{
    private MuPDFCore.Cookie cookie;
    
    public MuPDFCancellableTaskDefinition(final MuPDFCore core) {
        this.cookie = core.new Cookie();
    }
    
    @Override
    public void doCancel() {
        if (this.cookie == null) {
            return;
        }
        this.cookie.abort();
    }
    
    @Override
    public void doCleanup() {
        if (this.cookie == null) {
            return;
        }
        this.cookie.destroy();
        this.cookie = null;
    }
    
    @Override
    public final Result doInBackground(final Params... params) {
        return this.doInBackground(this.cookie, params);
    }
    
    public abstract Result doInBackground(final MuPDFCore.Cookie p0, final Params... p1);
}
