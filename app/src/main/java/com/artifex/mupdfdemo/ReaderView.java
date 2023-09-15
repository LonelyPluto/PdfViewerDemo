// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import java.util.NoSuchElementException;
import android.view.ViewGroup;

import android.graphics.Point;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.Scroller;
import java.util.LinkedList;
import android.view.View;
import android.util.SparseArray;
import android.view.ScaleGestureDetector;
import android.view.GestureDetector;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.lonelypluto.pdfviewerdemo.R;

public class ReaderView extends AdapterView<Adapter> implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, Runnable
{
    private static final int MOVING_DIAGONALLY = 0;
    private static final int MOVING_LEFT = 1;
    private static final int MOVING_RIGHT = 2;
    private static final int MOVING_UP = 3;
    private static final int MOVING_DOWN = 4;
    private static final int FLING_MARGIN = 100;
    private static final int GAP = 20;
    private static final float MIN_SCALE = 1.0f;
    private static final float MAX_SCALE = 5.0f;
    private static final float REFLOW_SCALE_FACTOR = 0.5f;
    private boolean HORIZONTAL_SCROLLING;
    private Adapter mAdapter;
    private int mCurrent;
    private boolean mResetLayout;
    private final SparseArray<View> mChildViews;
    private final LinkedList<View> mViewCache;
    private boolean mUserInteracting;
    private boolean mScaling;
    private float mScale;
    private int mXScroll;
    private int mYScroll;
    private boolean mReflow;
    private boolean mReflowChanged;
    private final GestureDetector mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;
    private final Scroller mScroller;
    private final Stepper mStepper;
    private int mScrollerLastX;
    private int mScrollerLastY;
    private float mLastScaleFocusX;
    private float mLastScaleFocusY;
    private Context mContext;
    private boolean memAlert;
    
    public ReaderView(final Context context) {
        super(context);
        this.HORIZONTAL_SCROLLING = true;
        this.mChildViews = (SparseArray<View>)new SparseArray(3);
        this.mViewCache = new LinkedList<View>();
        this.mScale = 1.0f;
        this.mReflow = false;
        this.mReflowChanged = false;
        this.memAlert = false;
        this.mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, (ScaleGestureDetector.OnScaleGestureListener)this);
        this.mScroller = new Scroller(context);
        this.mStepper = new Stepper((View)this, this);
        this.mContext = context;
    }
    
    public ReaderView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.HORIZONTAL_SCROLLING = true;
        this.mChildViews = (SparseArray<View>)new SparseArray(3);
        this.mViewCache = new LinkedList<View>();
        this.mScale = 1.0f;
        this.mReflow = false;
        this.mReflowChanged = false;
        this.memAlert = false;
        if (this.isInEditMode()) {
            this.mGestureDetector = null;
            this.mScaleGestureDetector = null;
            this.mScroller = null;
            this.mStepper = null;
        }
        else {
            this.mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);
            this.mScaleGestureDetector = new ScaleGestureDetector(context, (ScaleGestureDetector.OnScaleGestureListener)this);
            this.mScroller = new Scroller(context);
            this.mStepper = new Stepper((View)this, this);
        }
        this.mContext = context;
    }
    
    public ReaderView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.HORIZONTAL_SCROLLING = true;
        this.mChildViews = (SparseArray<View>)new SparseArray(3);
        this.mViewCache = new LinkedList<View>();
        this.mScale = 1.0f;
        this.mReflow = false;
        this.mReflowChanged = false;
        this.memAlert = false;
        this.mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);
        this.mScaleGestureDetector = new ScaleGestureDetector(context, (ScaleGestureDetector.OnScaleGestureListener)this);
        this.mScroller = new Scroller(context);
        this.mStepper = new Stepper((View)this, this);
        this.mContext = context;
    }
    
    public int getDisplayedViewIndex() {
        return this.mCurrent;
    }
    
    public void setDisplayedViewIndex(final int i) {
        if (0 <= i && i < this.mAdapter.getCount()) {
            this.onMoveOffChild(this.mCurrent);
            this.onMoveToChild(this.mCurrent = i);
            this.mResetLayout = true;
            this.requestLayout();
        }
    }
    
    public void setHorizontalScrolling(final boolean HORIZONTAL_SCROLLING) {
        this.HORIZONTAL_SCROLLING = HORIZONTAL_SCROLLING;
    }
    
    public void moveToNext() {
        final View v = (View)this.mChildViews.get(this.mCurrent + 1);
        if (v != null) {
            this.slideViewOntoScreen(v);
        }
    }
    
    public void moveToPrevious() {
        final View v = (View)this.mChildViews.get(this.mCurrent - 1);
        if (v != null) {
            this.slideViewOntoScreen(v);
        }
    }
    
    private int smartAdvanceAmount(final int screenHeight, final int max) {
        int advance = (int)(screenHeight * 0.9 + 0.5);
        final int leftOver = max % advance;
        final int steps = max / advance;
        if (leftOver != 0) {
            if (leftOver / (float)steps <= screenHeight * 0.05) {
                advance += (int)(leftOver / (float)steps + 0.5);
            }
            else {
                final int overshoot = advance - leftOver;
                if (overshoot / (float)steps <= screenHeight * 0.1) {
                    advance -= (int)(overshoot / (float)steps + 0.5);
                }
            }
        }
        if (advance > max) {
            advance = max;
        }
        return advance;
    }
    
    public void smartMoveForwards() {
        final View v = (View)this.mChildViews.get(this.mCurrent);
        if (v == null) {
            return;
        }
        final int screenWidth = this.getWidth();
        final int screenHeight = this.getHeight();
        final int remainingX = this.mScroller.getFinalX() - this.mScroller.getCurrX();
        final int remainingY = this.mScroller.getFinalY() - this.mScroller.getCurrY();
        final int top = -(v.getTop() + this.mYScroll + remainingY);
        final int right = screenWidth - (v.getLeft() + this.mXScroll + remainingX);
        final int bottom = screenHeight + top;
        final int docWidth = v.getMeasuredWidth();
        final int docHeight = v.getMeasuredHeight();
        int yOffset;
        int xOffset;
        if (bottom >= docHeight) {
            if (right + screenWidth > docWidth) {
                final View nv = (View)this.mChildViews.get(this.mCurrent + 1);
                if (nv == null) {
                    return;
                }
                final int nextTop = -(nv.getTop() + this.mYScroll + remainingY);
                final int nextLeft = -(nv.getLeft() + this.mXScroll + remainingX);
                final int nextDocWidth = nv.getMeasuredWidth();
                final int nextDocHeight = nv.getMeasuredHeight();
                yOffset = ((nextDocHeight < screenHeight) ? (nextDocHeight - screenHeight >> 1) : 0);
                if (nextDocWidth < screenWidth) {
                    xOffset = nextDocWidth - screenWidth >> 1;
                }
                else {
                    xOffset = right % screenWidth;
                    if (xOffset + screenWidth > nextDocWidth) {
                        xOffset = nextDocWidth - screenWidth;
                    }
                }
                xOffset -= nextLeft;
                yOffset -= nextTop;
            }
            else {
                xOffset = screenWidth;
                yOffset = screenHeight - bottom;
            }
        }
        else {
            xOffset = 0;
            yOffset = this.smartAdvanceAmount(screenHeight, docHeight - bottom);
        }
        final int n = 0;
        this.mScrollerLastY = n;
        this.mScrollerLastX = n;
        this.mScroller.startScroll(0, 0, remainingX - xOffset, remainingY - yOffset, 400);
        this.mStepper.prod();
    }
    
    public void smartMoveBackwards() {
        final View v = (View)this.mChildViews.get(this.mCurrent);
        if (v == null) {
            return;
        }
        final int screenWidth = this.getWidth();
        final int screenHeight = this.getHeight();
        final int remainingX = this.mScroller.getFinalX() - this.mScroller.getCurrX();
        final int remainingY = this.mScroller.getFinalY() - this.mScroller.getCurrY();
        final int left = -(v.getLeft() + this.mXScroll + remainingX);
        final int top = -(v.getTop() + this.mYScroll + remainingY);
        final int docHeight = v.getMeasuredHeight();
        int yOffset;
        int xOffset;
        if (top <= 0) {
            if (left < screenWidth) {
                final View pv = (View)this.mChildViews.get(this.mCurrent - 1);
                if (pv == null) {
                    return;
                }
                final int prevDocWidth = pv.getMeasuredWidth();
                final int prevDocHeight = pv.getMeasuredHeight();
                yOffset = ((prevDocHeight < screenHeight) ? (prevDocHeight - screenHeight >> 1) : 0);
                final int prevLeft = -(pv.getLeft() + this.mXScroll);
                final int prevTop = -(pv.getTop() + this.mYScroll);
                if (prevDocWidth < screenWidth) {
                    xOffset = prevDocWidth - screenWidth >> 1;
                }
                else {
                    xOffset = ((left > 0) ? (left % screenWidth) : 0);
                    if (xOffset + screenWidth > prevDocWidth) {
                        xOffset = prevDocWidth - screenWidth;
                    }
                    while (xOffset + screenWidth * 2 < prevDocWidth) {
                        xOffset += screenWidth;
                    }
                }
                xOffset -= prevLeft;
                yOffset -= prevTop - prevDocHeight + screenHeight;
            }
            else {
                xOffset = -screenWidth;
                yOffset = docHeight - screenHeight + top;
            }
        }
        else {
            xOffset = 0;
            yOffset = -this.smartAdvanceAmount(screenHeight, top);
        }
        final int n = 0;
        this.mScrollerLastY = n;
        this.mScrollerLastX = n;
        this.mScroller.startScroll(0, 0, remainingX - xOffset, remainingY - yOffset, 400);
        this.mStepper.prod();
    }
    
    public void resetupChildren() {
        for (int i = 0; i < this.mChildViews.size(); ++i) {
            this.onChildSetup(this.mChildViews.keyAt(i), (View)this.mChildViews.valueAt(i));
        }
    }
    
    public View getCurrentView() {
        return (View)this.mChildViews.get(this.mCurrent);
    }
    
    public void applyToChildren(final ViewMapper mapper) {
        for (int i = 0; i < this.mChildViews.size(); ++i) {
            mapper.applyToView((View)this.mChildViews.valueAt(i));
        }
    }
    
    public void refresh(final boolean reflow) {
        this.mReflow = reflow;
        this.mReflowChanged = true;
        this.mResetLayout = true;
        this.mScale = 1.0f;
        final int n = 0;
        this.mYScroll = n;
        this.mXScroll = n;
        this.requestLayout();
    }
    
    protected void onChildSetup(final int i, final View v) {
    }
    
    protected void onMoveToChild(final int i) {
    }
    
    protected void onMoveOffChild(final int i) {
    }
    
    protected void onSettle(final View v) {
    }
    
    protected void onUnsettle(final View v) {
    }
    
    protected void onNotInUse(final View v) {
    }
    
    protected void onScaleChild(final View v, final Float scale) {
    }
    
    public View getView(final int i) {
        return (View)this.mChildViews.get(i);
    }
    
    public View getDisplayedView() {
        return (View)this.mChildViews.get(this.mCurrent);
    }
    
    public void run() {
        if (!this.mScroller.isFinished()) {
            this.mScroller.computeScrollOffset();
            final int x = this.mScroller.getCurrX();
            final int y = this.mScroller.getCurrY();
            this.mXScroll += x - this.mScrollerLastX;
            this.mYScroll += y - this.mScrollerLastY;
            this.mScrollerLastX = x;
            this.mScrollerLastY = y;
            this.requestLayout();
            this.mStepper.prod();
        }
        else if (!this.mUserInteracting) {
            final View v = (View)this.mChildViews.get(this.mCurrent);
            if (v != null) {
                this.postSettle(v);
            }
        }
    }
    
    public boolean onDown(final MotionEvent arg0) {
        this.mScroller.forceFinished(true);
        return true;
    }
    
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        if (this.mScaling) {
            return true;
        }
        final View v = (View)this.mChildViews.get(this.mCurrent);
        if (v != null) {
            final Rect bounds = this.getScrollBounds(v);
            switch (directionOfTravel(velocityX, velocityY)) {
                case 1: {
                    if (bounds.left < 0) {
                        break;
                    }
                    final View vl = (View)this.mChildViews.get(this.mCurrent + 1);
                    if (vl != null) {
                        this.slideViewOntoScreen(vl);
                        return true;
                    }
                    break;
                }
                case 3: {
                    if (this.HORIZONTAL_SCROLLING || bounds.top < 0) {
                        break;
                    }
                    final View vl = (View)this.mChildViews.get(this.mCurrent + 1);
                    if (vl != null) {
                        this.slideViewOntoScreen(vl);
                        return true;
                    }
                    break;
                }
                case 2: {
                    if (bounds.right > 0) {
                        break;
                    }
                    final View vr = (View)this.mChildViews.get(this.mCurrent - 1);
                    if (vr != null) {
                        this.slideViewOntoScreen(vr);
                        return true;
                    }
                    break;
                }
                case 4: {
                    if (this.HORIZONTAL_SCROLLING || bounds.bottom > 0) {
                        break;
                    }
                    final View vr = (View)this.mChildViews.get(this.mCurrent - 1);
                    if (vr != null) {
                        this.slideViewOntoScreen(vr);
                        return true;
                    }
                    break;
                }
            }
            final int n = 0;
            this.mScrollerLastY = n;
            this.mScrollerLastX = n;
            final Rect expandedBounds = new Rect(bounds);
            expandedBounds.inset(-100, -100);
            if (withinBoundsInDirectionOfTravel(bounds, velocityX, velocityY) && expandedBounds.contains(0, 0)) {
                this.mScroller.fling(0, 0, (int)velocityX, (int)velocityY, bounds.left, bounds.right, bounds.top, bounds.bottom);
                this.mStepper.prod();
            }
        }
        return true;
    }
    
    public void onLongPress(final MotionEvent e) {
    }
    
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        if (!this.mScaling) {
            this.mXScroll -= (int)distanceX;
            this.mYScroll -= (int)distanceY;
            this.requestLayout();
        }
        return true;
    }
    
    public void onShowPress(final MotionEvent e) {
    }
    
    public boolean onSingleTapUp(final MotionEvent e) {
        return false;
    }
    
    public boolean onScale(final ScaleGestureDetector detector) {
        final float previousScale = this.mScale;
        final float scale_factor = this.mReflow ? 0.5f : 1.0f;
        final float min_scale = 1.0f * scale_factor;
        final float max_scale = 5.0f * scale_factor;
        this.mScale = Math.min(Math.max(this.mScale * detector.getScaleFactor(), min_scale), max_scale);
        if (this.mReflow) {
            final View v = (View)this.mChildViews.get(this.mCurrent);
            if (v != null) {
                this.onScaleChild(v, this.mScale);
            }
        }
        else {
            final float factor = this.mScale / previousScale;
            final View v2 = (View)this.mChildViews.get(this.mCurrent);
            if (v2 != null) {
                final float currentFocusX = detector.getFocusX();
                final float currentFocusY = detector.getFocusY();
                final int viewFocusX = (int)currentFocusX - (v2.getLeft() + this.mXScroll);
                final int viewFocusY = (int)currentFocusY - (v2.getTop() + this.mYScroll);
                this.mXScroll += (int)(viewFocusX - viewFocusX * factor);
                this.mYScroll += (int)(viewFocusY - viewFocusY * factor);
                if (this.mLastScaleFocusX >= 0.0f) {
                    this.mXScroll += (int)(currentFocusX - this.mLastScaleFocusX);
                }
                if (this.mLastScaleFocusY >= 0.0f) {
                    this.mYScroll += (int)(currentFocusY - this.mLastScaleFocusY);
                }
                this.mLastScaleFocusX = currentFocusX;
                this.mLastScaleFocusY = currentFocusY;
                this.requestLayout();
            }
        }
        return true;
    }
    
    public boolean onScaleBegin(final ScaleGestureDetector detector) {
        this.mScaling = true;
        final int n = 0;
        this.mYScroll = n;
        this.mXScroll = n;
        final float n2 = -1.0f;
        this.mLastScaleFocusY = n2;
        this.mLastScaleFocusX = n2;
        return true;
    }
    
    public void onScaleEnd(final ScaleGestureDetector detector) {
        if (this.mReflow) {
            this.applyToChildren(new ViewMapper() {
                @Override
                public void applyToView(final View view) {
                    ReaderView.this.onScaleChild(view, ReaderView.this.mScale);
                }
            });
        }
        this.mScaling = false;
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        this.mScaleGestureDetector.onTouchEvent(event);
        this.mGestureDetector.onTouchEvent(event);
        if ((event.getAction() & 0xFF) == 0x0) {
            this.mUserInteracting = true;
        }
        if ((event.getAction() & 0xFF) == 0x1) {
            this.mUserInteracting = false;
            final View v = (View)this.mChildViews.get(this.mCurrent);
            if (v != null) {
                if (this.mScroller.isFinished()) {
                    this.slideViewOntoScreen(v);
                }
                if (this.mScroller.isFinished()) {
                    this.postSettle(v);
                }
            }
        }
        this.requestLayout();
        return true;
    }
    
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int n = this.getChildCount(), i = 0; i < n; ++i) {
            this.measureView(this.getChildAt(i));
        }
    }
    
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        try {
            this.onLayout2(changed, left, top, right, bottom);
        }
        catch (OutOfMemoryError e) {
            System.out.println("Out of memory during layout");
            if (!this.memAlert) {
                this.memAlert = true;
                final AlertDialog alertDialog = new AlertDialog.Builder(this.mContext).create();
                alertDialog.setMessage((CharSequence)"Out of memory during layout");
                alertDialog.setButton(-3, (CharSequence)"OK", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        ReaderView.this.memAlert = false;
                    }
                });
                alertDialog.show();
            }
        }
    }
    
    private void onLayout2(final boolean changed, final int left, final int top, final int right, final int bottom) {
        if (this.mAdapter != null) {
            if (this.isInEditMode()) {
                return;
            }
            View cv = (View)this.mChildViews.get(this.mCurrent);
            if (!this.mResetLayout) {
                if (cv != null) {
                    final Point cvOffset = this.subScreenSizeOffset(cv);
                    boolean move;
                    if (this.HORIZONTAL_SCROLLING) {
                        move = (cv.getLeft() + cv.getMeasuredWidth() + cvOffset.x + 10 + this.mXScroll < this.getWidth() / 2);
                    }
                    else {
                        move = (cv.getTop() + cv.getMeasuredHeight() + cvOffset.y + 10 + this.mYScroll < this.getHeight() / 2);
                    }
                    if (move && this.mCurrent + 1 < this.mAdapter.getCount()) {
                        this.postUnsettle(cv);
                        this.mStepper.prod();
                        this.onMoveOffChild(this.mCurrent);
                        this.onMoveToChild(++this.mCurrent);
                    }
                    if (this.HORIZONTAL_SCROLLING) {
                        move = (cv.getLeft() - cvOffset.x - 10 + this.mXScroll >= this.getWidth() / 2);
                    }
                    else {
                        move = (cv.getTop() - cvOffset.y - 10 + this.mYScroll >= this.getHeight() / 2);
                    }
                    if (move && this.mCurrent > 0) {
                        this.postUnsettle(cv);
                        this.mStepper.prod();
                        this.onMoveOffChild(this.mCurrent);
                        this.onMoveToChild(--this.mCurrent);
                    }
                }
                final int numChildren = this.mChildViews.size();
                final int[] childIndices = new int[numChildren];
                for (int i = 0; i < numChildren; ++i) {
                    childIndices[i] = this.mChildViews.keyAt(i);
                }
                for (final int ai : childIndices) {
                    if (ai < this.mCurrent - 1 || ai > this.mCurrent + 1) {
                        final View v = (View)this.mChildViews.get(ai);
                        this.onNotInUse(v);
                        this.mViewCache.add(v);
                        this.removeViewInLayout(v);
                        this.mChildViews.remove(ai);
                    }
                }
            }
            else {
                this.mResetLayout = false;
                final int n = 0;
                this.mYScroll = n;
                this.mXScroll = n;
                for (int numChildren = this.mChildViews.size(), j = 0; j < numChildren; ++j) {
                    final View v2 = (View)this.mChildViews.valueAt(j);
                    this.onNotInUse(v2);
                    this.mViewCache.add(v2);
                    this.removeViewInLayout(v2);
                }
                this.mChildViews.clear();
                if (this.mReflowChanged) {
                    this.mReflowChanged = false;
                    this.mViewCache.clear();
                }
                this.mStepper.prod();
            }
            final boolean notPresent = this.mChildViews.get(this.mCurrent) == null;
            cv = this.getOrCreateChild(this.mCurrent);
            final Point cvOffset = this.subScreenSizeOffset(cv);
            int cvLeft;
            int cvTop;
            if (notPresent) {
                cvLeft = cvOffset.x;
                cvTop = cvOffset.y;
            }
            else {
                cvLeft = cv.getLeft() + this.mXScroll;
                cvTop = cv.getTop() + this.mYScroll;
            }
            final int n2 = 0;
            this.mYScroll = n2;
            this.mXScroll = n2;
            int cvRight = cvLeft + cv.getMeasuredWidth();
            int cvBottom = cvTop + cv.getMeasuredHeight();
            if (!this.mUserInteracting && this.mScroller.isFinished()) {
                final Point corr = this.getCorrection(this.getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
                cvRight += corr.x;
                cvLeft += corr.x;
                cvTop += corr.y;
                cvBottom += corr.y;
            }
            else if (cv.getMeasuredHeight() <= this.getHeight()) {
                final Point corr = this.getCorrection(this.getScrollBounds(cvLeft, cvTop, cvRight, cvBottom));
                if (this.HORIZONTAL_SCROLLING) {
                    cvTop += corr.y;
                    cvBottom += corr.y;
                }
                else {
                    cvRight += corr.x;
                    cvLeft += corr.x;
                }
            }
            cv.layout(cvLeft, cvTop, cvRight, cvBottom);
            if (this.mCurrent > 0) {
                final View lv = this.getOrCreateChild(this.mCurrent - 1);
                final Point leftOffset = this.subScreenSizeOffset(lv);
                if (this.HORIZONTAL_SCROLLING) {
                    final int gap = leftOffset.x + 20 + cvOffset.x;
                    lv.layout(cvLeft - lv.getMeasuredWidth() - gap, (cvBottom + cvTop - lv.getMeasuredHeight()) / 2, cvLeft - gap, (cvBottom + cvTop + lv.getMeasuredHeight()) / 2);
                }
                else {
                    final int gap = leftOffset.y + 20 + cvOffset.y;
                    lv.layout((cvLeft + cvRight - lv.getMeasuredWidth()) / 2, cvTop - lv.getMeasuredHeight() - gap, (cvLeft + cvRight + lv.getMeasuredWidth()) / 2, cvTop - gap);
                }
            }
            if (this.mCurrent + 1 < this.mAdapter.getCount()) {
                final View rv = this.getOrCreateChild(this.mCurrent + 1);
                final Point rightOffset = this.subScreenSizeOffset(rv);
                if (this.HORIZONTAL_SCROLLING) {
                    final int gap = cvOffset.x + 20 + rightOffset.x;
                    rv.layout(cvRight + gap, (cvBottom + cvTop - rv.getMeasuredHeight()) / 2, cvRight + rv.getMeasuredWidth() + gap, (cvBottom + cvTop + rv.getMeasuredHeight()) / 2);
                }
                else {
                    final int gap = cvOffset.y + 20 + rightOffset.y;
                    rv.layout((cvLeft + cvRight - rv.getMeasuredWidth()) / 2, cvBottom + gap, (cvLeft + cvRight + rv.getMeasuredWidth()) / 2, cvBottom + gap + rv.getMeasuredHeight());
                }
            }
            this.invalidate();
        }
    }
    
    public Adapter getAdapter() {
        return this.mAdapter;
    }
    
    public View getSelectedView() {
        return null;
    }
    
    public void setAdapter(final Adapter adapter) {
        if (null != this.mAdapter && adapter != this.mAdapter && adapter instanceof MuPDFPageAdapter) {
            ((MuPDFPageAdapter)adapter).releaseBitmaps();
        }
        this.mAdapter = adapter;
        this.requestLayout();
    }
    
    public void setSelection(final int arg0) {
        throw new UnsupportedOperationException(this.getContext().getString(R.string.not_supported));
    }
    
    private View getCached() {
        if (this.mViewCache.size() == 0) {
            return null;
        }
        return this.mViewCache.removeFirst();
    }
    
    private View getOrCreateChild(final int i) {
        View v = (View)this.mChildViews.get(i);
        if (v == null) {
            v = this.mAdapter.getView(i, this.getCached(), (ViewGroup)this);
            this.addAndMeasureChild(i, v);
            this.onChildSetup(i, v);
            this.onScaleChild(v, this.mScale);
        }
        return v;
    }
    
    private void addAndMeasureChild(final int i, final View v) {
        LayoutParams params = v.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(-2, -2);
        }
        this.addViewInLayout(v, 0, params, true);
        this.mChildViews.append(i, v);
        this.measureView(v);
    }
    
    private void measureView(final View v) {
        v.measure(0, 0);
        if (!this.mReflow) {
            final float scale = Math.min(this.getWidth() / (float)v.getMeasuredWidth(), this.getHeight() / (float)v.getMeasuredHeight());
            v.measure(0x40000000 | (int)(v.getMeasuredWidth() * scale * this.mScale), 0x40000000 | (int)(v.getMeasuredHeight() * scale * this.mScale));
        }
        else {
            v.measure(0x40000000 | v.getMeasuredWidth(), 0x40000000 | v.getMeasuredHeight());
        }
    }
    
    private Rect getScrollBounds(final int left, final int top, final int right, final int bottom) {
        int xmin = this.getWidth() - right;
        int xmax = -left;
        int ymin = this.getHeight() - bottom;
        int ymax = -top;
        if (xmin > xmax) {
            xmax = (xmin = (xmin + xmax) / 2);
        }
        if (ymin > ymax) {
            ymax = (ymin = (ymin + ymax) / 2);
        }
        return new Rect(xmin, ymin, xmax, ymax);
    }
    
    private Rect getScrollBounds(final View v) {
        return this.getScrollBounds(v.getLeft() + this.mXScroll, v.getTop() + this.mYScroll, v.getLeft() + v.getMeasuredWidth() + this.mXScroll, v.getTop() + v.getMeasuredHeight() + this.mYScroll);
    }
    
    private Point getCorrection(final Rect bounds) {
        return new Point(Math.min(Math.max(0, bounds.left), bounds.right), Math.min(Math.max(0, bounds.top), bounds.bottom));
    }
    
    private void postSettle(final View v) {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                ReaderView.this.onSettle(v);
            }
        });
    }
    
    private void postUnsettle(final View v) {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                ReaderView.this.onUnsettle(v);
            }
        });
    }
    
    private void slideViewOntoScreen(final View v) {
        final Point corr = this.getCorrection(this.getScrollBounds(v));
        if (corr.x != 0 || corr.y != 0) {
            final int n = 0;
            this.mScrollerLastY = n;
            this.mScrollerLastX = n;
            this.mScroller.startScroll(0, 0, corr.x, corr.y, 400);
            this.mStepper.prod();
        }
    }
    
    private Point subScreenSizeOffset(final View v) {
        return new Point(Math.max((this.getWidth() - v.getMeasuredWidth()) / 2, 0), Math.max((this.getHeight() - v.getMeasuredHeight()) / 2, 0));
    }
    
    private static int directionOfTravel(final float vx, final float vy) {
        if (Math.abs(vx) > 2.0f * Math.abs(vy)) {
            return (vx > 0.0f) ? 2 : 1;
        }
        if (Math.abs(vy) > 2.0f * Math.abs(vx)) {
            return (vy > 0.0f) ? 4 : 3;
        }
        return 0;
    }
    
    private static boolean withinBoundsInDirectionOfTravel(final Rect bounds, final float vx, final float vy) {
        switch (directionOfTravel(vx, vy)) {
            case 0: {
                return bounds.contains(0, 0);
            }
            case 1: {
                return bounds.left <= 0;
            }
            case 2: {
                return bounds.right >= 0;
            }
            case 3: {
                return bounds.top <= 0;
            }
            case 4: {
                return bounds.bottom >= 0;
            }
            default: {
                throw new NoSuchElementException();
            }
        }
    }
    
    public abstract static class ViewMapper
    {
        public abstract void applyToView(final View p0);
    }
}
