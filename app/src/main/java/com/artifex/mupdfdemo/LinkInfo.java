// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;

public class LinkInfo
{
    public final RectF rect;
    
    public LinkInfo(final float l, final float t, final float r, final float b) {
        this.rect = new RectF(l, t, r, b);
    }
    
    public void acceptVisitor(final LinkInfoVisitor visitor) {
    }
}
