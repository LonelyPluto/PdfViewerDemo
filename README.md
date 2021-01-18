# PdfViewerDemo
pdf阅读器，基于MuPDF开发，可横竖切换浏览、多种批注功能、电子签章\
PdfViewerDemo is developed within the [mupdf](https://mupdf.com/downloads/) project.
# Screenshots
![.gif预览](screenshot/pdfview.gif)        ![预览图1](screenshot/s1.png)\
![预览图2](screenshot/s2.png)          ![预览图3](screenshot/s3.png)

# Features
* 显示pdf
* 支持横竖滑动
* 动态改变标注颜色和画笔颜色
* 支持插入图片
# How do I use?
pdf-viewer is available through JCenter. To be able to use this library in your project, add the following dependency to your build.gradle file:
~~~ 
dependencies{
	implementation 'com.lonelypluto:pdf-viewer:1.0.7'
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
# 特别说明
本来写个库可以方便使用，但是总有一些人跟我要.so文件甚至要.so文件的源码，这里就不多说了，最近一年太忙也没有太多时间和精力去扩展新的功能，我把.so文件放到项目的根目录libs文件夹中，希望对有需要的人提供一点帮助吧
