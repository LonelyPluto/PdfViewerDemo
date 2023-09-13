// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.view.View;
import android.view.ScaleGestureDetector;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.util.AttributeSet;
import androidx.core.content.ContextCompat;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import com.lonelypluto.pdflibrary.utils.SharedPreferencesUtil;
import com.lonelypluto.pdfviewerdemo.R;

import android.content.Context;

public class MuPDFReaderView extends ReaderView
{
    private MuPDFReaderViewListener listener;
    private final Context mContext;
    private boolean mLinksEnabled;
    private boolean isLinkHighlightColor;
    private Mode mMode;
    private boolean tapDisabled;
    private int tapPageMargin;
    private int mLinkHighlightColor;
    private float mX;
    private float mY;
    private static final float TOUCH_TOLERANCE = 2.0f;
    
    protected void onTapMainDocArea() {
        this.checkMuPDFReaderViewListener();
        this.listener.onTapMainDocArea();
    }
    
    protected void onDocMotion() {
        this.checkMuPDFReaderViewListener();
        this.listener.onDocMotion();
    }
    
    protected void onHit(final Hit item) {
        this.checkMuPDFReaderViewListener();
        this.listener.onHit(item);
    }
    
    public void setLinksEnabled(final boolean b) {
        this.mLinksEnabled = b;
        this.resetupChildren();
    }
    
    public void setLinkHighlightColor(final int color) {
        this.isLinkHighlightColor = true;
        this.mLinkHighlightColor = color;
        this.resetupChildren();
    }
    
    public void setSearchTextColor(final int color) {
        SharedPreferencesUtil.put("sp_color_search_text", color);
        this.resetupChildren();
    }
    
    public void setInkColor(final int color) {
        ((MuPDFView)this.getCurrentView()).setInkColor(color);
    }
    
    public void setPaintStrockWidth(final float inkThickness) {
        ((MuPDFView)this.getCurrentView()).setPaintStrockWidth(inkThickness);
    }
    
    public float getCurrentScale() {
        return ((MuPDFView)this.getCurrentView()).getCurrentScale();
    }
    
    public void setMode(final Mode m) {
        this.mMode = m;
    }

    public Mode getMode() {
        return mMode;
    }

    private void setup() {
        final DisplayMetrics dm = new DisplayMetrics();
        final WindowManager wm = (WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        this.tapPageMargin = (int)dm.xdpi;
        if (this.tapPageMargin < 100) {
            this.tapPageMargin = 100;
        }
        if (this.tapPageMargin > dm.widthPixels / 5) {
            this.tapPageMargin = dm.widthPixels / 5;
        }
        this.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.muPDFReaderView_bg));
    }
    
    public MuPDFReaderView(final Context context) {
        super(context);
        this.mLinksEnabled = false;
        this.isLinkHighlightColor = false;
        this.mMode = Mode.Viewing;
        this.tapDisabled = false;
        this.mContext = context;
        this.setup();
    }
    
    public MuPDFReaderView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mLinksEnabled = false;
        this.isLinkHighlightColor = false;
        this.mMode = Mode.Viewing;
        this.tapDisabled = false;
        this.mContext = context;
        this.setup();
    }
    
    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        if (this.mMode == Mode.Viewing && !this.tapDisabled) {
            final MuPDFView pageView = (MuPDFView)this.getDisplayedView();
            final Hit item = pageView.passClickEvent(e.getX(), e.getY());
            this.onHit(item);
            if (item == Hit.Nothing) {
                final LinkInfo link;
                if (this.mLinksEnabled && (link = pageView.hitLink(e.getX(), e.getY())) != null) {
                    link.acceptVisitor(new LinkInfoVisitor() {
                        @Override
                        public void visitInternal(final LinkInfoInternal li) {
                            MuPDFReaderView.this.setDisplayedViewIndex(li.pageNumber);
                        }
                        
                        @Override
                        public void visitExternal(final LinkInfoExternal li) {
                            final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(li.url));
                            MuPDFReaderView.this.mContext.startActivity(intent);
                        }
                        
                        @Override
                        public void visitRemote(final LinkInfoRemote li) {
                        }
                    });
                }
                else if (e.getX() < this.tapPageMargin) {
                    super.smartMoveBackwards();
                }
                else if (e.getX() > super.getWidth() - this.tapPageMargin) {
                    super.smartMoveForwards();
                }
                else if (e.getY() < this.tapPageMargin) {
                    super.smartMoveBackwards();
                }
                else if (e.getY() > super.getHeight() - this.tapPageMargin) {
                    super.smartMoveForwards();
                }
                else {
                    this.onTapMainDocArea();
                }
            }
        }
        return super.onSingleTapUp(e);
    }
    
    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        final MuPDFView pageView = (MuPDFView)this.getDisplayedView();
        switch (this.mMode) {
            case Viewing: {
                if (!this.tapDisabled) {
                    this.onDocMotion();
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
            case Selecting: {
                if (pageView != null) {
                    pageView.selectText(e1.getX(), e1.getY(), e2.getX(), e2.getY());
                }
                return true;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        switch (this.mMode) {
            case Viewing: {
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public boolean onScaleBegin(final ScaleGestureDetector d) {
        this.tapDisabled = true;
        return super.onScaleBegin(d);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (this.mMode == Mode.Drawing) {
            final float x = event.getX();
            final float y = event.getY();
            switch (event.getAction()) {
                case 0: {
                    this.touch_start(x, y);
                    break;
                }
                case 2: {
                    this.touch_move(x, y);
                    break;
                }
            }
        }
        if ((event.getAction() & event.getActionMasked()) == 0x0) {
            this.tapDisabled = false;
        }
        return super.onTouchEvent(event);
    }
    
    private void touch_start(final float x, final float y) {
        final MuPDFView pageView = (MuPDFView)this.getDisplayedView();
        if (pageView != null) {
            pageView.startDraw(x, y);
        }
        this.mX = x;
        this.mY = y;
    }
    
    private void touch_move(final float x, final float y) {
        final float dx = Math.abs(x - this.mX);
        final float dy = Math.abs(y - this.mY);
        if (dx >= 2.0f || dy >= 2.0f) {
            final MuPDFView pageView = (MuPDFView)this.getDisplayedView();
            if (pageView != null) {
                pageView.continueDraw(x, y);
            }
            this.mX = x;
            this.mY = y;
        }
    }
    
    @Override
    protected void onChildSetup(final int i, final View v) {
        if (SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber == i) {
            ((MuPDFView)v).setSearchBoxes(SearchTaskResult.get().searchBoxes);
        }
        else {
            ((MuPDFView)v).setSearchBoxes(null);
        }
        ((MuPDFView)v).setLinkHighlighting(this.mLinksEnabled);
        if (this.isLinkHighlightColor) {
            ((MuPDFView)v).setLinkHighlightColor(this.mLinkHighlightColor);
        }
        ((MuPDFView)v).setChangeReporter(new Runnable() {
            @Override
            public void run() {
                MuPDFReaderView.this.applyToChildren(new ViewMapper() {
                    @Override
                    public void applyToView(final View view) {
                        ((MuPDFView)view).update();
                    }
                });
            }
        });
    }
    
    @Override
    protected void onMoveToChild(final int i) {
        if (SearchTaskResult.get() != null && SearchTaskResult.get().pageNumber != i) {
            SearchTaskResult.set(null);
            this.resetupChildren();
        }
        this.checkMuPDFReaderViewListener();
        this.listener.onMoveToChild(i);
    }
    
    @Override
    protected void onMoveOffChild(final int i) {
        final View v = this.getView(i);
        if (v != null) {
            ((MuPDFView)v).deselectAnnotation();
        }
    }
    
    @Override
    protected void onSettle(final View v) {
        ((MuPDFView)v).updateHq(false);
    }
    
    @Override
    protected void onUnsettle(final View v) {
        ((MuPDFView)v).removeHq();
    }
    
    @Override
    protected void onNotInUse(final View v) {
        ((MuPDFView)v).releaseResources();
    }
    
    @Override
    protected void onScaleChild(final View v, final Float scale) {
        ((MuPDFView)v).setScale(scale);
    }
    
    public void setListener(final MuPDFReaderViewListener listener) {
        this.listener = listener;
    }
    
    private void checkMuPDFReaderViewListener() {
        if (this.listener == null) {
            this.listener = new MuPDFReaderViewListener() {
                @Override
                public void onMoveToChild(final int i) {
                }
                
                @Override
                public void onTapMainDocArea() {
                }
                
                @Override
                public void onDocMotion() {
                }
                
                @Override
                public void onHit(final Hit item) {
                }
            };
        }
    }
    
    public enum Mode
    {
        Viewing, 
        Selecting, 
        Drawing;
    }
}
