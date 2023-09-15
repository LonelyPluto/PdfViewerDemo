// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

class PassClickResultText extends PassClickResult
{
    public final String text;
    
    public PassClickResultText(final boolean _changed, final String _text) {
        super(_changed);
        this.text = _text;
    }
    
    @Override
    public void acceptVisitor(final PassClickResultVisitor visitor) {
        visitor.visitText(this);
    }
}
