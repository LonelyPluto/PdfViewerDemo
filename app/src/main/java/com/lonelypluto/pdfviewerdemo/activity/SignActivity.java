package com.lonelypluto.pdfviewerdemo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.artifex.mupdfdemo.Hit;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.MuPDFReaderViewListener;
import com.artifex.mupdfdemo.MuPDFView;
import com.artifex.mupdfdemo.ReaderView;
import com.artifex.mupdfdemo.SavePdf;
import com.artifex.mupdfdemo.widget.VDHDeepLayout;
import com.lonelypluto.pdfviewerdemo.R;

/**
 * @Description: 电子签章 用itext提供的jar 网上我所能找到的demo插入图片后 再打开图片的位置都有偏差
 * 这里的方法也有缺陷，pdf放大后插入图片，坐标会有偏差，还没有解决，希望有大神可以提供方法
 * @author: ZhangYW
 * @time: 2019/3/11 15:56
 */
public class SignActivity extends AppCompatActivity {
    private static final String TAG = SignActivity.class.getSimpleName();
    private String filePath = Environment.getExternalStorageDirectory() + "/pdf_t1.pdf"; // 文件路径

    private MuPDFCore muPDFCore;// 加载mupdf.so文件
    private MuPDFReaderView muPDFReaderView;// 显示pdf的view

    private Button btn_sign;// 电子签章
    private Button btn_save;// 保存
    private VDHDeepLayout vdhDeepLayout;
    private ImageView iv_sign;

    private SavePdfTask savePdfTask;
    /*
     * 用于异步存储
     * */
    class SavePdfTask extends AsyncTask {

        SavePdf savePdf;
        public SavePdfTask(SavePdf savePdf) {
            this.savePdf = savePdf;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            savePdf.addText();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.e(TAG, "存储完成");
            try {
                Toast.makeText(SignActivity.this, "图片插入成功", Toast.LENGTH_SHORT).show();
//                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
    }

    private void initView() {

        // 电子签章
        btn_sign = (Button)findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "btn_sign");
                vdhDeepLayout.setVisibility(View.VISIBLE);
            }
        });

        // 保存
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里注意 in_path 和 out_path 不能一样 边读边写会出错
                String in_path = filePath;
                String out_path = in_path.substring(0, in_path.length() - 4) + "_t2.pdf";
                SavePdf savePdf = new SavePdf(in_path, out_path);
                savePdf.setScale(muPDFReaderView.getCurrentScale());
                savePdf.setPageNum(muPDFReaderView.getDisplayedViewIndex() + 1);

                savePdf.setWidthScale(1.0f * muPDFReaderView.getScaleX() / muPDFReaderView.getDisplayedView().getWidth());//计算宽偏移的百分比
                savePdf.setHeightScale(1.0f * muPDFReaderView.getScaleY() / muPDFReaderView.getDisplayedView().getHeight());//计算长偏移的百分比

                Log.e(TAG, "scaleX = " + muPDFReaderView.getScaleX() + "   " + muPDFReaderView.getDisplayedView().getWidth());
                savePdf.setWH(iv_sign.getX(), iv_sign.getY());
//                savePdf.setWidthScale(0);
//                savePdf.setHeightScale(0);

                //计算分辨率密度
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);

                float density = metric.density;
                savePdf.setDensity(density);

                Bitmap bitmap = getBitmap(SignActivity.this, com.lonelypluto.pdflibrary.R.mipmap.ic_launcher);
                savePdf.setBitmap(bitmap);

                savePdfTask = new SavePdfTask(savePdf);
                savePdfTask.execute();
            }
        });

        vdhDeepLayout = (VDHDeepLayout)findViewById(R.id.VDHDeepLayout);
        iv_sign = (ImageView)findViewById(R.id.iv_sign);

        muPDFReaderView = (MuPDFReaderView)findViewById(R.id.open_pdf_mupdfreaderview);
        // 通过MuPDFCore打开pdf文件
        muPDFCore = openFile(filePath);
        // 判断如果core为空，提示不能打开文件
        if (muPDFCore == null) {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(com.lonelypluto.pdflibrary.R.string.cannot_open_document);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(com.lonelypluto.pdflibrary.R.string.dismiss),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            alert.show();
            return;
        }
        // 显示
        muPDFReaderView.setAdapter(new MuPDFPageAdapter(this, muPDFCore));
    }

    private static Bitmap getBitmap(Context context, int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    /**
     * 打开文件
     * @param path 文件路径
     * @return
     */
    private MuPDFCore openFile(String path) {
        Log.e(TAG, "Trying to open " + path);
        try {
            muPDFCore = new MuPDFCore(this, path);
        } catch (Exception e) {
            Log.e(TAG, "openFile catch:" + e.toString());
            return null;
        } catch (OutOfMemoryError e) {
            //  out of memory is not an Exception, so we catch it separately.
            Log.e(TAG, "openFile catch: OutOfMemoryError " + e.toString());
            return null;
        }
        return muPDFCore;
    }

    @Override
    protected void onStart() {
        if (muPDFCore != null) {
            muPDFCore.startAlerts();
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (muPDFCore != null) {
            muPDFCore.stopAlerts();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (muPDFReaderView != null) {
            muPDFReaderView.applyToChildren(new ReaderView.ViewMapper() {
                public void applyToView(View view) {
                    ((MuPDFView) view).releaseBitmaps();
                }
            });
        }
        if (muPDFCore != null)
            muPDFCore.onDestroy();
        muPDFCore = null;
        super.onDestroy();
    }
}
