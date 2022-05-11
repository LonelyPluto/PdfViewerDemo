// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.RectF;

class TextSelector
{
    private final TextWord[][] mText;
    private final RectF mSelectBox;
    
    public TextSelector(final TextWord[][] text, final RectF selectBox) {
        this.mText = text;
        this.mSelectBox = selectBox;
    }
    
    public void select(final TextProcessor tp) {
        if (this.mText == null || this.mSelectBox == null) {
            return;
        }
        final ArrayList<TextWord[]> lines = new ArrayList<TextWord[]>();
        for (final TextWord[] line : this.mText) {
            if (line[0].bottom > this.mSelectBox.top && line[0].top < this.mSelectBox.bottom) {
                lines.add(line);
            }
        }
        for (final TextWord[] line2 : lines) {
            final boolean firstLine = line2[0].top < this.mSelectBox.top;
            final boolean lastLine = line2[0].bottom > this.mSelectBox.bottom;
            float start = Float.NEGATIVE_INFINITY;
            float end = Float.POSITIVE_INFINITY;
            if (firstLine && lastLine) {
                start = Math.min(this.mSelectBox.left, this.mSelectBox.right);
                end = Math.max(this.mSelectBox.left, this.mSelectBox.right);
            }
            else if (firstLine) {
                start = this.mSelectBox.left;
            }
            else if (lastLine) {
                end = this.mSelectBox.right;
            }
            tp.onStartLine();
            for (final TextWord word : line2) {
                if (word.right > start && word.left < end) {
                    tp.onWord(word);
                }
            }
            tp.onEndLine();
        }
    }
}
