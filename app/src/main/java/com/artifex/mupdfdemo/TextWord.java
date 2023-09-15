// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;

public class TextWord extends RectF
{
    public String w;
    
    public TextWord() {
        this.w = new String();
    }
    
    public void Add(final TextChar tc) {
        super.union((RectF)tc);
        this.w = this.w.concat(new String(new char[] { tc.c }));
    }
}
