// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public abstract class LinkInfoVisitor
{
    public abstract void visitInternal(final LinkInfoInternal p0);
    
    public abstract void visitExternal(final LinkInfoExternal p0);
    
    public abstract void visitRemote(final LinkInfoRemote p0);
}
