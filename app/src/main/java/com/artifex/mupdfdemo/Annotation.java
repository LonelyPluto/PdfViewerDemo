// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;

public class Annotation extends RectF
{
    public final Type type;
    
    public Annotation(final float x0, final float y0, final float x1, final float y1, final int _type) {
        super(x0, y0, x1, y1);
        this.type = ((_type == -1) ? Type.UNKNOWN : Type.values()[_type]);
    }
    
    public enum Type
    {
        TEXT, 
        LINK, 
        FREETEXT, 
        LINE, 
        SQUARE, 
        CIRCLE, 
        POLYGON, 
        POLYLINE, 
        HIGHLIGHT, 
        UNDERLINE, 
        SQUIGGLY, 
        STRIKEOUT, 
        STAMP, 
        CARET, 
        INK, 
        POPUP, 
        FILEATTACHMENT, 
        SOUND, 
        MOVIE, 
        WIDGET, 
        SCREEN, 
        PRINTERMARK, 
        TRAPNET, 
        WATERMARK, 
        A3D, 
        UNKNOWN;
    }
}
