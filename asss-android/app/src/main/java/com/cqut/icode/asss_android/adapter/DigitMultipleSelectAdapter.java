package com.cqut.icode.asss_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：hwl
 * 时间：2017/7/28:17:12
 * 邮箱：1097412672@qq.com
 * 说明：提供具有数量选择的，又返回结果的adapter
 */
public class DigitMultipleSelectAdapter extends RecyclerView.Adapter<DigitMultipleSelectAdapter.ViewHolder> {
    private List<Map<String, Object>> dataList = new ArrayList<>();

    //    private List<Map<String, Object>> changData;
    public DigitMultipleSelectAdapter(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.digit_multiple_select_item, parent, false);
        final DigitMultipleSelectAdapter.ViewHolder holder = new DigitMultipleSelectAdapter.ViewHolder(view);

        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int currentSize = (int) Double.parseDouble(dataList.get(position).get("size").toString());
                if (currentSize > 0) {
                    holder.size.setText(--currentSize + "");
                    dataList.get(position).put("size", currentSize);
                }
                if(currentSize <= 0){//不可再减
                    holder.reduce.setImageResource(R.drawable.ic_reduce);
                }
            }
        });

        holder.mount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int currentSize = (int) Double.parseDouble(dataList.get(position).get("size").toString());
                if (currentSize == 0)
                    holder.reduce.setImageResource(R.drawable.ic_reduceable);
                holder.size.setText(++currentSize + "");
                dataList.get(position).put("size", currentSize);

            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> objectMap = dataList.get(position);
        holder.tvName.setText(objectMap.get("spare_part_name").toString());
        int size = (int) Double.parseDouble(objectMap.get("size").toString());
        holder.size.setText(size + "");
        if (size > 0)
            holder.reduce.setImageResource(R.drawable.ic_reduceable);
        else
            holder.reduce.setImageResource(R.drawable.ic_reduce);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.size)
        TextView size;
        @BindView(R.id.name)
        TextView tvName;
        @BindView(R.id.mount)
        ImageView mount;
        @BindView(R.id.reduce)
        ImageView reduce;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
