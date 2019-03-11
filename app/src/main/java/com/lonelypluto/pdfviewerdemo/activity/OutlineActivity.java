package com.lonelypluto.pdfviewerdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.artifex.mupdfdemo.OutlineActivityData;
import com.artifex.mupdfdemo.OutlineItem;
import com.lonelypluto.pdfviewerdemo.R;
import com.lonelypluto.pdfviewerdemo.adapter.OutlineAdapter;
import com.lonelypluto.pdfviewerdemo.widget.OnRecyclerItemClickListener;

/**
 * @Description: 目录
 * @author: ZhangYW
 * @time: 2019/3/8 10:29
 */
public class OutlineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OutlineAdapter adapter;
    private OutlineItem mItems[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline);

        initData();
        initView();
        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        mItems = OutlineActivityData.get().items;
    }

    /**
     * 初始化布局
     */
    private void initView(){
        adapter = new OutlineAdapter(this,mItems);
        recyclerView = (RecyclerView)findViewById(R.id.outline_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//HORIZONTAL 水平
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置点击事件
     */
    private void setListener(){
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                OutlineActivityData.get().position = vh.getLayoutPosition();
                setResult(mItems[vh.getLayoutPosition()].page);
                finish();
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
    }
}
