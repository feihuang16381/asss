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
 * 时间：2017/7/27:11:06
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class ApplyForPartsAdapter extends RecyclerView.Adapter<ApplyForPartsAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Map<String,Object>> applyForPartsList;
    private ApplyForPartsAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    public ApplyForPartsAdapter(ArrayList<Map<String, Object>> applyForPartsList) {
        this.applyForPartsList = applyForPartsList;
    }

    public interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }

    @Override
    public int getItemCount() {
        return applyForPartsList.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,Object> applyInfo = applyForPartsList.get(position);
        holder.itemTitle.setText(applyInfo.get("apply_name").toString());
        holder.creatorName.setText(applyInfo.get("creator_name").toString());
        holder.itemType.setText("关联任务: " + applyInfo.get("task_name").toString());
        holder.createTime.setText(applyInfo.get("apply_time").toString());
        holder.stateImgLabel.setVisibility(View.GONE);
        holder.itemView.setTag(position);//设置可传输的tag
    }

    @Override
    public ApplyForPartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title)
        TextView itemTitle;
        @BindView(R.id.creator_name)
        TextView creatorName;
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

    public void setOnItemClickListener(ApplyForPartsAdapter.OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
