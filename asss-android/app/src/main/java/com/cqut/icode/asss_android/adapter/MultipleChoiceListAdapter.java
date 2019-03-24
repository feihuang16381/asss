package com.cqut.icode.asss_android.adapter;

import android.content.Context;
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
 * 时间：2017/7/12:10:39
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class MultipleChoiceListAdapter extends RecyclerView.Adapter<MultipleChoiceListAdapter.ViewHolder>  implements View.OnClickListener{
    private List<Map<String,Object>> dataList;

    private MultipleChoiceListAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;

    private Context context;
    public interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }


    public MultipleChoiceListAdapter(List<Map<String, Object>> dataList, Context context) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MultipleChoiceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choiceable_item, parent, false);
        MultipleChoiceListAdapter.ViewHolder holder = new MultipleChoiceListAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,Object> objectMap = dataList.get(position);
        holder.tvName.setText(objectMap.get("name").toString());
        if(Boolean.parseBoolean(objectMap.get("selected").toString())){
            //点亮选择图标
            holder.tvName.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.isSelected.setVisibility(View.VISIBLE);
        }else{
            //隐藏选择图标
            holder.tvName.setTextColor(context.getResources().getColor(R.color.theme_secondary_text_inverted));
            holder.isSelected.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);//设置可传输的tag
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.is_selected)
        ImageView isSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onClick(View v) {
        if(onRecyclerViewItemClickListener != null){
            onRecyclerViewItemClickListener.onItemClick(v,Integer.parseInt(v.getTag().toString()));
        }
    }

    public void setOnItemClickListener(MultipleChoiceListAdapter.OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
