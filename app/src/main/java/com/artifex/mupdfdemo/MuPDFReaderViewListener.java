// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public interface MuPDFReaderViewListener
{
    void onMoveToChild(final int p0);
    
    void onTapMainDocArea();
    
    void onDocMotion();
    
    void onHit(final Hit p0);
}
