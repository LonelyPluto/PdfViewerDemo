// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class LinkInfoInternal extends LinkInfo
{
    public final int pageNumber;
    
    public LinkInfoInternal(final float l, final float t, final float r, final float b, final int p) {
        super(l, t, r, b);
        this.pageNumber = p;
    }
    
    @Override
    public void acceptVisitor(final LinkInfoVisitor visitor) {
        visitor.visitInternal(this);
    }
}
