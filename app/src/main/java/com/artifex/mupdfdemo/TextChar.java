// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;

public class TextChar extends RectF
{
    public char c;
    
    public TextChar(final float x0, final float y0, final float x1, final float y1, final char _c) {
        super(x0, y0, x1, y1);
        this.c = _c;
    }
}
