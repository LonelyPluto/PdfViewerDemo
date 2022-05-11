// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

class PassClickResultSignature extends PassClickResult
{
    public final SignatureState state;
    
    public PassClickResultSignature(final boolean _changed, final int _state) {
        super(_changed);
        this.state = SignatureState.values()[_state];
    }
    
    @Override
    public void acceptVisitor(final PassClickResultVisitor visitor) {
        visitor.visitSignature(this);
    }
}
