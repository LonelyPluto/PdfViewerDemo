// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class LinkInfoExternal extends LinkInfo
{
    public final String url;
    
    public LinkInfoExternal(final float l, final float t, final float r, final float b, final String u) {
        super(l, t, r, b);
        this.url = u;
    }
    
    @Override
    public void acceptVisitor(final LinkInfoVisitor visitor) {
        visitor.visitExternal(this);
    }
}
