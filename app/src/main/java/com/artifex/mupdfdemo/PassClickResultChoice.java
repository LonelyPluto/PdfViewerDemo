// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

class PassClickResultChoice extends PassClickResult
{
    public final String[] options;
    public final String[] selected;
    
    public PassClickResultChoice(final boolean _changed, final String[] _options, final String[] _selected) {
        super(_changed);
        this.options = _options;
        this.selected = _selected;
    }
    
    @Override
    public void acceptVisitor(final PassClickResultVisitor visitor) {
        visitor.visitChoice(this);
    }
}
