// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo.widget;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.customview.widget.ViewDragHelper;

public class VDHDeepLayout extends RelativeLayout
{
    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private Point mAutoBackOriginPos;
    
    public VDHDeepLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mAutoBackOriginPos = new Point();
        (this.mViewDragHelper = ViewDragHelper.create((ViewGroup)this, 1.0f, (ViewDragHelper.Callback)new ViewDragHelper.Callback() {
            public boolean tryCaptureView(final View child, final int pointerId) {
                return child == VDHDeepLayout.this.mDragView;
            }
            
            public int clampViewPositionHorizontal(final View child, final int left, final int dx) {
                final int leftBound = VDHDeepLayout.this.getPaddingLeft();
                final int rightBound = VDHDeepLayout.this.getWidth() - VDHDeepLayout.this.mDragView.getWidth() - leftBound;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }
            
            public int clampViewPositionVertical(final View child, final int top, final int dy) {
                final int topBound = VDHDeepLayout.this.getPaddingTop();
                final int bottomBound = VDHDeepLayout.this.getHeight() - VDHDeepLayout.this.mDragView.getHeight() - topBound;
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }
            
            public void onViewReleased(final View releasedChild, final float xvel, final float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }
            
            public void onEdgeDragStarted(final int edgeFlags, final int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
            }
            
            public int getViewHorizontalDragRange(final View child) {
                return VDHDeepLayout.this.getMeasuredWidth() - child.getMeasuredWidth();
            }
            
            public int getViewVerticalDragRange(final View child) {
                return VDHDeepLayout.this.getMeasuredHeight() - child.getMeasuredHeight();
            }
        })).setEdgeTrackingEnabled(2);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        return this.mViewDragHelper.shouldInterceptTouchEvent(ev);
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        this.mViewDragHelper.processTouchEvent(event);
        return true;
    }
    
    public void computeScroll() {
        if (this.mViewDragHelper.continueSettling(true)) {
            this.invalidate();
        }
    }
    
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);
        this.mAutoBackOriginPos.x = this.mDragView.getLeft();
        this.mAutoBackOriginPos.y = this.mDragView.getTop();
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDragView = this.getChildAt(0);
    }
}
