package com.lonelypluto.pdfviewerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.artifex.mupdfdemo.OutlineItem;
import com.lonelypluto.pdfviewerdemo.R;

/**
 * @Description: 文件目录adapter
 * @author: ZhangYW
 * @time: 2019/1/22 11:17
 */
public class OutlineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OutlineItem mList[];

    public OutlineAdapter(Context context, OutlineItem mList[]){
        this.mContext = context;
        this.mList = mList;

    }
    /**
     * 渲染具体的ViewHolder
     * @param parent ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.outline_item,parent,false);
        return new OutlineViewHolder(itemView);
    }
    /**
     * 绑定ViewHolder的数据。
     * @param holder
     * @param position 数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OutlineItem bean = mList[position];
        if (null == bean)
            return;
        OutlineViewHolder viewHolder = (OutlineViewHolder) holder;

        int level = bean.level;
        if (level > 8) level = 8;
        String space = "";
        for (int i=0; i<level;i++)
            space += "   ";
        String text = space + bean.title;

        viewHolder.tv_title.setText(text);
        viewHolder.tv_page.setText(String.valueOf(bean.page+1));
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }
    public static class OutlineViewHolder extends  RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_page;

        public OutlineViewHolder(View itemView){
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.outline_item_title);
            tv_page = (TextView) itemView.findViewById(R.id.outline_item_page);
        }
    }

}
