// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import android.graphics.RectF;
import android.content.DialogInterface;
import com.lonelypluto.pdfviewerdemo.R;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.os.Handler;
import android.content.Context;

public abstract class SearchTask
{
    private static final int SEARCH_PROGRESS_DELAY = 200;
    private final Context mContext;
    private final MuPDFCore mCore;
    private final Handler mHandler;
    private final AlertDialog.Builder mAlertBuilder;
    private AsyncTask<Void, Integer, SearchTaskResult> mSearchTask;
    
    public SearchTask(final Context context, final MuPDFCore core) {
        this.mContext = context;
        this.mCore = core;
        this.mHandler = new Handler();
        this.mAlertBuilder = new AlertDialog.Builder(context);
    }
    
    protected abstract void onTextFound(final SearchTaskResult p0);
    
    public void stop() {
        if (this.mSearchTask != null) {
            this.mSearchTask.cancel(true);
            this.mSearchTask = null;
        }
    }
    
    public void go(final String text, final int direction, final int displayPage, final int searchPage) {
        if (this.mCore == null) {
            return;
        }
        this.stop();
        final int increment = direction;
        final int startIndex = (searchPage == -1) ? displayPage : (searchPage + increment);
        final ProgressDialogX progressDialog = new ProgressDialogX(this.mContext);
        progressDialog.setProgressStyle(1);
        progressDialog.setTitle((CharSequence)this.mContext.getString(R.string.searching));
        progressDialog.setOnCancelListener((DialogInterface.OnCancelListener)new DialogInterface.OnCancelListener() {
            public void onCancel(final DialogInterface dialog) {
                SearchTask.this.stop();
            }
        });
        progressDialog.setMax(this.mCore.countPages());
        (this.mSearchTask = new AsyncTask<Void, Integer, SearchTaskResult>() {
            protected SearchTaskResult doInBackground(final Void... params) {
                for (int index = startIndex; 0 <= index && index < SearchTask.this.mCore.countPages() && !this.isCancelled(); index += increment) {
                    this.publishProgress(new Integer[] { index });
                    final RectF[] searchHits = SearchTask.this.mCore.searchPage(index, text);
                    if (searchHits != null && searchHits.length > 0) {
                        return new SearchTaskResult(text, index, searchHits);
                    }
                }
                return null;
            }
            
            protected void onPostExecute(final SearchTaskResult result) {
                progressDialog.cancel();
                if (result != null) {
                    SearchTask.this.onTextFound(result);
                }
                else {
                    SearchTask.this.mAlertBuilder.setTitle((SearchTaskResult.get() == null) ? R.string.text_not_found : R.string.no_further_occurrences_found);
                    final AlertDialog alert = SearchTask.this.mAlertBuilder.create();
                    alert.setButton(-1, (CharSequence)SearchTask.this.mContext.getString(R.string.dismiss), (DialogInterface.OnClickListener)null);
                    alert.show();
                }
            }
            
            protected void onCancelled() {
                progressDialog.cancel();
            }
            
            protected void onProgressUpdate(final Integer... values) {
                progressDialog.setProgress((int)values[0]);
            }
            
            protected void onPreExecute() {
                super.onPreExecute();
                SearchTask.this.mHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isCancelled()) {
                            progressDialog.show();
                            progressDialog.setProgress(startIndex);
                        }
                    }
                }, 200L);
            }
        }).execute(new Void[0]);
    }
}
