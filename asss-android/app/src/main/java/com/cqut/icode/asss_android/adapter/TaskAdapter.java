package com.cqut.icode.asss_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：hwl
 * 时间：2017/7/21:11:08
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements View.OnClickListener{
    private List<Map<String,Object>> taskList;

    public TaskAdapter(List<Map<String, Object>> taskList) {
        this.taskList = taskList;
    }
    private TaskAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    public interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
}

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        Map<String,Object> task = taskList.get(position);
        holder.itemTitle.setText(task.get("id").toString() + " " + task.get("task_name").toString());
        holder.creatorName.setText(task.get("user_name").toString());
        holder.itemType.setText("类别: " + getTaskType(task.get("type").toString()));
        holder.createTime.setText(task.get("time").toString());

        if(task.get("is_receive").toString().equals("1.0")){
            holder.taskGetState.setImageResource(R.drawable.ic_get);
        }else{
            holder.taskGetState.setImageResource(R.drawable.ic_not_get);
        }
        holder.itemView.setTag(position);//设置可传输的tag
    }

    public String getTaskType(String type){
        switch (type){
            case "危险源巡检": return "危险源巡检";
            case "危险源标定": return "危险源标定";
            case "危险源维护": return "危险源维护";
            case "通用维护": return "通用维护";
            default:
                return "通用维护";
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
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
        ImageView taskGetState;
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

    public void setOnItemClickListener(TaskAdapter.OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
