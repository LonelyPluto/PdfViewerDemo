// 
// Decompiled by Procyon v0.5.36
// 

package com.artifex.mupdfdemo;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.DocumentException;
import com.lowagie.text.BadElementException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import android.util.Log;
import com.lowagie.text.Image;
import java.io.OutputStream;
import com.lowagie.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import com.lowagie.text.pdf.PdfReader;
import android.graphics.Bitmap;

public class SavePdf
{
    private float defaultScale;
    float widthScale;
    float heightScale;
    String inPath;
    String outPath;
    private int pageNum;
    private Bitmap bitmap;
    private float scale;
    private float density;
    private float width;
    private float height;
    
    public void setWidthScale(final float widthScale) {
        this.widthScale = widthScale;
    }
    
    public void setHeightScale(final float heightScale) {
        this.heightScale = heightScale;
    }
    
    public void setScale(final float scale) {
        this.scale = scale;
    }
    
    public void setWH(final float width, final float height) {
        this.width = width;
        this.height = height;
    }
    
    public void setDensity(final float density) {
        this.density = density;
    }
    
    public void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    
    public void setPageNum(final int pageNum) {
        this.pageNum = pageNum;
    }
    
    public SavePdf(final String inPath, final String outPath) {
        this.defaultScale = 0.90756303f;
        this.inPath = inPath;
        this.outPath = outPath;
    }
    
    public void addText() {
        try {
            final PdfReader reader = new PdfReader(this.inPath, "PDF".getBytes());
            final FileOutputStream outputStream = new FileOutputStream(this.outPath);
            final PdfStamper stamp = new PdfStamper(reader, (OutputStream)outputStream);
            final PdfContentByte over = stamp.getOverContent(this.pageNum);
            final byte[] bytes = this.Bitmap2Bytes(this.bitmap);
            final Image img = Image.getInstance(bytes);
            final Rectangle rectangle = reader.getPageSize(this.pageNum);
            img.setAlignment(1);
            Log.e("zyw", "position = " + rectangle.getWidth() * this.widthScale + "  " + rectangle.getHeight() * this.heightScale);
            Log.e("zyw", "density = " + this.density);
            Log.e("zyw", "img.getWidth() = " + img.getWidth() + "  img.getHeight() = " + img.getHeight());
            Log.e("zyw", "scale = " + this.scale);
            Log.e("zyw", "widthScale = " + this.widthScale + "  heightScale = " + this.heightScale);
            Log.e("zyw", "bitmap.w = " + this.bitmap.getWidth() + "  bitmap.h = " + this.bitmap.getHeight());
            Log.e("zyw", "rectangle.getLeft = " + rectangle.getLeft() + "  rectangle.getBottom() = " + rectangle.getBottom());
            Log.e("zyw", "rectangle.getWidth = " + rectangle.getWidth() + "  rectangle.getHeight = " + rectangle.getHeight());
            Log.e("zyw", "\u6bd4\u4f8b1 = " + rectangle.getWidth() / img.getWidth() * 100.0f);
            Log.e("zyw", "\u6bd4\u4f8b2 = " + rectangle.getWidth() * this.widthScale * 100.0f);
            Log.e("zyw", "\u5750\u6807AbsolutePosition = " + this.width * (rectangle.getWidth() * this.widthScale) + " " + (1964.0f - this.height - img.getHeight()) * (rectangle.getWidth() * this.widthScale));
            Log.e("zyw", "\u5dee\u503c = " + rectangle.getHeight() * (this.heightScale - this.widthScale));
            Log.e("zyw", "\u7f29\u653e\u6bd4\u4f8b = " + this.scale / this.defaultScale);
            img.scalePercent(rectangle.getWidth() * this.widthScale * 100.0f);
            img.setAbsolutePosition(this.width * (rectangle.getWidth() * this.widthScale) * this.scale, rectangle.getHeight() - this.height * (rectangle.getWidth() * this.widthScale) * (this.scale / this.defaultScale) + img.getHeight() / 2.0f * this.widthScale * 100.0f);
            over.addImage(img);
            stamp.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        catch (BadElementException e4) {
            e4.printStackTrace();
        }
        catch (DocumentException e5) {
            e5.printStackTrace();
        }
    }
    
    public byte[] Bitmap2Bytes(final Bitmap bm) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)baos);
        return baos.toByteArray();
    }
}
