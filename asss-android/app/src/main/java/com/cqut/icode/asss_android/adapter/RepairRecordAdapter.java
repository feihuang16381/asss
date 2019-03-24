package com.cqut.icode.asss_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：hwl
 * 时间：2017/7/31:8:58
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class RepairRecordAdapter extends RecyclerView.Adapter<RepairRecordAdapter.ViewHolder>
   implements  View.OnClickListener{

    private ArrayList<Map<String,Object>> repairRecordList;
    private RepairRecordAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    public RepairRecordAdapter(ArrayList<Map<String, Object>> repairRecordList) {
        this.repairRecordList = repairRecordList;
    }

    public interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return repairRecordList.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,Object> repairInfo = repairRecordList.get(position);
        holder.itemTitle.setText(repairInfo.get("terminal_id").toString() +
                repairInfo.get("terminal_name").toString());
        holder.serviceman.setText(repairInfo.get("serviceman").toString());
        holder.itemType.setText("类型: " + getTaskType(repairInfo.get("type").toString()));
        holder.createTime.setText(repairInfo.get("time").toString());
        holder.stateImgLabel.setVisibility(View.GONE);
        holder.itemView.setTag(position);//设置可传输的tag
    }

    public String getTaskType(String type){
        switch (type){
            case "1.0": return "危险源";
            case "2.0": return "其他";
            default:
                return "通用维护";
        }
    }

    @Override
    public RepairRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.creator_name)
        TextView serviceman;
        @BindView(R.id.create_time)
        TextView createTime;
        @BindView(R.id.item_type)
        TextView itemType;
        @BindView(R.id.state_img_label)
        ImageView stateImgLabel;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onClick(View v) {
        if(onRecyclerViewItemClickListener != null){
            onRecyclerViewItemClickListener.onItemClick(v, Integer.parseInt(v.getTag().toString()));
        }
    }

    public void setOnItemClickListener(RepairRecordAdapter.OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
