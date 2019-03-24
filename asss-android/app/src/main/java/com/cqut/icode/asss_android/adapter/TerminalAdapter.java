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
 * 时间：2017/7/3:22:09
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class TerminalAdapter extends RecyclerView.Adapter<TerminalAdapter.ViewHolder>  implements View.OnClickListener {
    private List<Map<String,Object>> terminalList;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;


    @Override
    public void onClick(View v) {
        if(onRecyclerViewItemClickListener != null){
            onRecyclerViewItemClickListener.onItemClick(v, Integer.parseInt(v.getTag().toString()));
        }
    }


    public interface  OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.termial_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,Object> terminal = terminalList.get(position);
        holder.terminal_identify_text.setText(terminal.get("terminal_name").toString());
        holder.terminal_code_text.setText("站点 : " + terminal.get("type").toString());
        if (terminal.get("type").toString().equals("1")) {//默认1位危险源2为其他
            holder.terminal_type_text.setText("分类 : 危险源");
            holder.type_ic.setImageResource(R.drawable.zhandian_ico_red);
        }
        else if(terminal.get("type").toString().equals("2")){
            holder.terminal_type_text.setText("分类 : 其他");
            holder.type_ic.setImageResource(R.drawable.zhandian_ico_lemon);

        }
        holder.itemView.setTag(position);//设置可传输的tag
    }

    @Override
    public int getItemCount() {
        return terminalList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.terminal_type_text)
        TextView terminal_type_text;
        @BindView(R.id.terminal_code_text)
        TextView terminal_code_text;
        @BindView(R.id.terminal_identify_text)
        TextView terminal_identify_text;
        @BindView(R.id.type_ic)
        ImageView type_ic;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TerminalAdapter(List<Map<String,Object>> terminalList) {
        this.terminalList = terminalList;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
