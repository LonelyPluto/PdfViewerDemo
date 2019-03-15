# PdfViewerDemo
pdf阅读器，基于MuPDF开发，可横竖切换浏览、多种批注功能、电子签章\
PdfViewerDemo is developed within the [mupdf](https://mupdf.com/downloads/) project.
# Screenshots
![.gif预览](screenshot/contacts.gif)

# Features
* 显示pdf
* 支持横竖滑动
* 动态改变标注颜色和画笔颜色
* 支持插入图片
# How do I use?
pdf-viewer is available through JCenter. To be able to use this library in your project, add the following dependency to your build.gradle file:
~~~ 
dependencies{
	implementation 'com.lonelypluto:pdf-viewer:1.0.5'
 }
 ~~~
 Simple use cases will look something like this:
 
~~~xml
<com.artifex.mupdfdemo.MuPDFReaderView
        android:id="@+id/mupdfreaderview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />

MuPDFReaderView muPDFReaderView = (MuPDFReaderView)findViewById(R.id.mupdfreaderview);
MuPDFCore muPDFCore = new MuPDFCore(this, filePath);
muPDFReaderView.setAdapter(new MuPDFPageAdapter(this, muPDFCore));
~~~
