// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

public class OutlineActivityData
{
    public OutlineItem[] items;
    public int position;
    private static OutlineActivityData singleton;
    
    public static void set(final OutlineActivityData d) {
        OutlineActivityData.singleton = d;
    }
    
    public static OutlineActivityData get() {
        if (OutlineActivityData.singleton == null) {
            OutlineActivityData.singleton = new OutlineActivityData();
        }
        return OutlineActivityData.singleton;
    }
}
