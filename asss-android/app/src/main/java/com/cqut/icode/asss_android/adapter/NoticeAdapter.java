package com.cqut.icode.asss_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 10713 on 2017/7/13.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private List<Map<String,Object>> noticeList;
    View view;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageId;
        ImageView redDot;
        TextView title;
        TextView createTime;
        TextView founder;
        TextView createInfo;

        View noticeView;

        public  ViewHolder(View view){
            super(view);
            noticeView = view;
            imageId = (ImageView)view.findViewById(R.id.is_new_info);
            redDot = (ImageView)view.findViewById(R.id.red_dot);
            title = (TextView)view.findViewById(R.id.title);
            createTime = (TextView)view.findViewById(R.id.create_time);
            founder = (TextView)view.findViewById(R.id.founder);
            createInfo = (TextView)view.findViewById(R.id.create_info);
        }
    }

    public NoticeAdapter(List<Map<String,Object>> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list_child,parent,false);//通知页面list的子选项的布局文件

        ViewHolder holder = new ViewHolder(view);

//        holder.noticeView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
////                Map<String,Object> noticelist = noticeList.get(position);
////
////                String string = "状态："+noticelist.get("state")+"\n时间："+noticelist.get("time")+"\n创建者:"+noticelist.get("founder")+"\n创建信息:"+noticelist.get("info");
////                Toast.makeText(MyApplication.getContext(),string,Toast.LENGTH_SHORT).show();
//              //  view.setOnClickListener(this);
//            }
//        });
        holder.noticeView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Map<String,Object> noticelist = noticeList.get(position);
            if (noticelist.get("state").toString().equals("1.0")) {
                holder.redDot.setVisibility(View.GONE);//判断status来显示是否为未读
            } else {
                holder.redDot.setVisibility(View.VISIBLE);
            }
            if (noticelist.get("founder").equals("我")) {
                holder.imageId.setImageResource(R.drawable.icon_inform_green);
            } else {
                holder.imageId.setImageResource(R.drawable.icon_inform_blue);
            }
            holder.title.setText(noticelist.get("title").toString());
            holder.createTime.setText(noticelist.get("create_time").toString());
            holder.founder.setText(noticelist.get("founder").toString());
            String string = noticelist.get("content").toString();
            if (string.length() > 20) {
                String string_info = string.substring(0, 17) + "...";
                holder.createInfo.setText(string_info);
            } else
                holder.createInfo.setText(string);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                    onItemClickListener.onClick(position);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickListener != null)
                    onItemLongClickListener.onLongClick(position);

                return true;//返回false会在长按结束后继续点击
            }
        });
    }

//    public interface onSwipeListener {
//        void onDel(int pos);
//    }
//
//    private onSwipeListener mOnSwipeListener;
//
//    public onSwipeListener getOnDelListener() {
//        return mOnSwipeListener;
//    }
//
//    public void setOnDelListener(onSwipeListener mOnDelListener) {
//        this.mOnSwipeListener = mOnDelListener;
//    }


    //点击
    public interface OnItemClickListener{
        void onClick(int position);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener{
        void onLongClick(int position);
    }
    OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
