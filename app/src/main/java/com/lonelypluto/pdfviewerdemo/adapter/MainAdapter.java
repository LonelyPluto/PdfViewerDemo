package com.lonelypluto.pdfviewerdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.artifex.mupdfdemo.OutlineItem;
import com.lonelypluto.pdfviewerdemo.R;
import com.lonelypluto.pdfviewerdemo.entry.MainBean;

import java.util.List;

/**
 * @Description: 文件目录adapter
 * @author: ZhangYW
 * @time: 2019/1/22 11:17
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MainBean> mList;

    public MainAdapter(Context context, List<MainBean> list){
        this.mContext = context;
        this.mList = list;

    }
    /**
     * 渲染具体的ViewHolder
     * @param parent ViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.main_item,parent,false);
        return new MainViewHolder(itemView);
    }
    /**
     * 绑定ViewHolder的数据。
     * @param holder
     * @param position 数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MainBean bean = mList.get(position);
        if (null == bean)
            return;
        MainViewHolder viewHolder = (MainViewHolder) holder;

        viewHolder.tv_title.setText(bean.getTitle());
        viewHolder.tv_describe.setText(bean.getDescribe());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MainViewHolder extends  RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_describe;

        public MainViewHolder(View itemView){
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.main_item_tv_title);
            tv_describe = (TextView) itemView.findViewById(R.id.main_item_tv_describe);
        }
    }

}
