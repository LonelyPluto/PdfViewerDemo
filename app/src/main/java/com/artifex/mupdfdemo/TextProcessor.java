// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

interface TextProcessor
{
    void onStartLine();
    
    void onWord(final TextWord p0);
    
    void onEndLine();
}
