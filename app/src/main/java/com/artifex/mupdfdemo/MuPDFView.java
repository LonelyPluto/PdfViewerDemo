// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;
import android.graphics.PointF;

public interface MuPDFView
{
    void setPage(final int p0, final PointF p1);
    
    void setScale(final float p0);
    
    int getPage();
    
    void blank(final int p0);
    
    Hit passClickEvent(final float p0, final float p1);
    
    LinkInfo hitLink(final float p0, final float p1);
    
    void selectText(final float p0, final float p1, final float p2, final float p3);
    
    void deselectText();
    
    boolean copySelection();
    
    boolean markupSelection(final Annotation.Type p0);
    
    void deleteSelectedAnnotation();
    
    void setSearchBoxes(final RectF[] p0);
    
    void setLinkHighlighting(final boolean p0);
    
    void deselectAnnotation();
    
    void startDraw(final float p0, final float p1);
    
    void continueDraw(final float p0, final float p1);
    
    void cancelDraw();
    
    boolean saveDraw();
    
    void setChangeReporter(final Runnable p0);
    
    void update();
    
    void updateHq(final boolean p0);
    
    void removeHq();
    
    void releaseResources();
    
    void releaseBitmaps();
    
    void setLinkHighlightColor(final int p0);
    
    void setInkColor(final int p0);
    
    void setPaintStrockWidth(final float p0);
    
    float getCurrentScale();
}
