package com.cqut.icode.asss_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：hwl
 * 时间：2017/8/1:10:18
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    private List<Map<String,Object>> dataList;

    public UploadListAdapter(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(),"pause",Toast.LENGTH_SHORT).show();
            }
        });
        holder.cancelUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dataList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.reUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(),"reUpload",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,Object> data = dataList.get(position);
        holder.repairRecordName.setText(data.get("terminal_name").toString());
        //*寻找找一张存在的图片作为展示
//        List<Map<String,Object>> pictures = (List<Map<String,Object>>)data.get("pictures");
//        int index = 0;
//        while(index < pictures.size() ){
//            String picturePath = pictures.get(index++).get("picturePath").toString();
//            if(ImageUtil.imgIsExist(picturePath)){
//                holder.imgUpload.setImageBitmap(ImageUtil.getFitBitMap(picturePath,80,80));
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_upload)
        ImageView imgUpload;
        @BindView(R.id.repair_record_name)
        TextView repairRecordName;
        @BindView(R.id.upload_progress)
        ProgressBar uploadProgress;
        @BindView(R.id.upload_tv_state)
        TextView uploadTvState;
        @BindView(R.id.upload_percentage)
        TextView uploadPercentage;
        @BindView(R.id.pause)
        TextView pause;
        @BindView(R.id.cancel_upload)
        TextView cancelUpload;
        @BindView(R.id.re_upload)
        TextView reUpload;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
