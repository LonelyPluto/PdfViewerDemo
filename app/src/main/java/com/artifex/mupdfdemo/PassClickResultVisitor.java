// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

abstract class PassClickResultVisitor
{
    public abstract void visitText(final PassClickResultText p0);
    
    public abstract void visitChoice(final PassClickResultChoice p0);
    
    public abstract void visitSignature(final PassClickResultSignature p0);
}
