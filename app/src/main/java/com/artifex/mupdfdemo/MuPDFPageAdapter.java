// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.os.AsyncTask;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.SparseArray;
import android.content.Context;
import android.widget.BaseAdapter;

public class MuPDFPageAdapter extends BaseAdapter
{
    private final Context mContext;
    private final MuPDFCore mCore;
    private final SparseArray<PointF> mPageSizes;
    private Bitmap mSharedHqBm;
    
    public MuPDFPageAdapter(final Context c, final MuPDFCore core) {
        this.mPageSizes = (SparseArray<PointF>)new SparseArray();
        this.mContext = c;
        this.mCore = core;
    }
    
    public int getCount() {
        return this.mCore.countPages();
    }
    
    public Object getItem(final int position) {
        return null;
    }
    
    public long getItemId(final int position) {
        return 0L;
    }
    
    public void releaseBitmaps() {
        if (this.mSharedHqBm != null) {
            this.mSharedHqBm.recycle();
        }
        this.mSharedHqBm = null;
    }
    
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        MuPDFPageView pageView;
        if (convertView == null) {
            if (this.mSharedHqBm == null || this.mSharedHqBm.getWidth() != parent.getWidth() || this.mSharedHqBm.getHeight() != parent.getHeight()) {
                this.mSharedHqBm = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
            }
            pageView = new MuPDFPageView(this.mContext, this.mCore, new Point(parent.getWidth(), parent.getHeight()), this.mSharedHqBm);
        }
        else {
            pageView = (MuPDFPageView)convertView;
        }
        final PointF pageSize = (PointF)this.mPageSizes.get(position);
        if (pageSize != null) {
            pageView.setPage(position, pageSize);
        }
        else {
            pageView.blank(position);
            final AsyncTask<Void, Void, PointF> sizingTask = new AsyncTask<Void, Void, PointF>() {
                protected PointF doInBackground(final Void... arg0) {
                    return MuPDFPageAdapter.this.mCore.getPageSize(position);
                }
                
                protected void onPostExecute(final PointF result) {
                    super.onPostExecute(result);
                    MuPDFPageAdapter.this.mPageSizes.put(position, result);
                    if (pageView.getPage() == position) {
                        pageView.setPage(position, result);
                    }
                }
            };
            sizingTask.execute(new Void[] { null });
        }
        return (View)pageView;
    }
}
