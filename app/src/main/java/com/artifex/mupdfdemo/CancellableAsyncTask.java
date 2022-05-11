// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;

public class CancellableAsyncTask<Params, Result>
{
    private final AsyncTask<Params, Void, Result> asyncTask;
    private final CancellableTaskDefinition<Params, Result> ourTask;
    
    public void onPreExecute() {
    }
    
    public void onPostExecute(final Result result) {
    }
    
    public CancellableAsyncTask(final CancellableTaskDefinition<Params, Result> task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        this.ourTask = task;
        this.asyncTask = new AsyncTask<Params, Void, Result>() {
            protected Result doInBackground(final Params... params) {
                return task.doInBackground(params);
            }
            
            protected void onPreExecute() {
                CancellableAsyncTask.this.onPreExecute();
            }
            
            protected void onPostExecute(final Result result) {
                CancellableAsyncTask.this.onPostExecute(result);
                task.doCleanup();
            }
        };
    }
    
    public void cancelAndWait() {
        this.asyncTask.cancel(true);
        this.ourTask.doCancel();
        try {
            this.asyncTask.get();
        }
        catch (InterruptedException ex) {}
        catch (ExecutionException ex2) {}
        catch (CancellationException ex3) {}
        this.ourTask.doCleanup();
    }
    
    public void execute(final Params... params) {
        this.asyncTask.execute(params);
    }
}
