// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;

public class SearchTaskResult
{
    public final String txt;
    public final int pageNumber;
    public final RectF[] searchBoxes;
    private static SearchTaskResult singleton;
    
    SearchTaskResult(final String _txt, final int _pageNumber, final RectF[] _searchBoxes) {
        this.txt = _txt;
        this.pageNumber = _pageNumber;
        this.searchBoxes = _searchBoxes;
    }
    
    public static SearchTaskResult get() {
        return SearchTaskResult.singleton;
    }
    
    public static void set(final SearchTaskResult r) {
        SearchTaskResult.singleton = r;
    }
}
