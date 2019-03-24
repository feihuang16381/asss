package com.cqut.icode.asss_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.MultipleChoiceListAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 10713 on 2017/7/28.
 */

public class SingleChoiceListDialog extends Dialog {
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.dg_recycler)
    RecyclerView dgRecycler;

    private List<Map<String, Object>> dataList = null;
    private MultipleChoiceListAdapter adapter;
    private int oldPosition;
    private SingleChoiceListDialog.onCompleteOnclickListener completeOnclickListener = null;
    private SingleChoiceListDialog.onCancelOnclickListener cancelOnclickListener = null;
    private Context context;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    //获取所选择的序列
    public List<Map<String, Object>> getSelectedList() {
        return dataList;
    }

    public interface onCompleteOnclickListener {
        void finishClick();

    }

    public interface onCancelOnclickListener {
        void cancelClick();
    }

    public void setCompleteOnclickListener(SingleChoiceListDialog.onCompleteOnclickListener completeOnclickListener) {
        this.completeOnclickListener = completeOnclickListener;
    }

    public void setCancelOnclickListener(SingleChoiceListDialog.onCancelOnclickListener cancelOnclickListener) {
        this.cancelOnclickListener = cancelOnclickListener;
    }

    private void initListener() {
        adapter.setOnItemClickListener(new MultipleChoiceListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < dataList.size(); i++) {
                    if(Boolean.parseBoolean(dataList.get(i).get("selected").toString())) {
                        oldPosition = i;
                        dataList.get(oldPosition).put("selected",false);
                    }
                }

                if(Boolean.parseBoolean(dataList.get(position).get("selected").toString())) {
                    dataList.get(position).put("selected",false);
                }
                else {
                    dataList.get(position).put("selected",true);
                }
                oldPosition = position;
                adapter.notifyDataSetChanged();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                cancelOnclickListener.cancelClick();
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completeOnclickListener != null)
                    dismiss();
                completeOnclickListener.finishClick();
            }
        });
    }

    private void initComponent() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getOwnerActivity());
        dgRecycler.setLayoutManager(layoutManager);
        adapter = new MultipleChoiceListAdapter(dataList,context);
        dgRecycler.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_single_dialog);
        ButterKnife.bind(this);
        initState();
        initComponent();
        initListener();

    }

    //设置Dialog的大小及其样式
    private void initState() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    //暴露设置title的接口
    public void setTitle(String content) {
        this.title.setText(content);
    }

    public SingleChoiceListDialog(Context context, List<Map<String, Object>> dataList) {
        super(context, R.style.DialogTheme);
        this.dataList = dataList;
        this.context = context;
    }

    public SingleChoiceListDialog(Context context, int themeResId, List<Map<String, Object>> dataList) {
        super(context, themeResId);
        this.dataList = dataList;
        this.context = context;
    }

    public SingleChoiceListDialog(Context context, boolean cancelable, OnCancelListener cancelListener, List<Map<String, Object>> dataList) {
        super(context, cancelable, cancelListener);
        this.dataList = dataList;
        this.context = context;
    }

}
