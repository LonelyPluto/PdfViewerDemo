// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class LinkInfoRemote extends LinkInfo
{
    public final String fileSpec;
    public final int pageNumber;
    public final boolean newWindow;
    
    public LinkInfoRemote(final float l, final float t, final float r, final float b, final String f, final int p, final boolean n) {
        super(l, t, r, b);
        this.fileSpec = f;
        this.pageNumber = p;
        this.newWindow = n;
    }
    
    @Override
    public void acceptVisitor(final LinkInfoVisitor visitor) {
        visitor.visitRemote(this);
    }
}
