package com.cqut.icode.asss_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * 作者：hwl
 * 时间：2017/7/12:15:27
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class MultipleChoiceListDialog extends Dialog {
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_all_select)
    TextView tvAllSelect;
    @BindView(R.id.dg_recycler)
    RecyclerView dgRecycler;

    private boolean isAllSelected;
    private boolean isCreate;
    private int selectedSize;
    private String title;
    private List<Map<String, Object>> dataList = null;
    private MultipleChoiceListAdapter adapter;
    private onCompleteOnclickListener completeOnclickListener = null;
    private onCancelOnclickListener cancelOnclickListener = null;
    private Context context;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
        selectedSize = 0;
        if(isCreate) {
            setSelectState();
            adapter.notifyDataSetChanged();
        }
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

    public void setCompleteOnclickListener(onCompleteOnclickListener completeOnclickListener) {
        this.completeOnclickListener = completeOnclickListener;
    }

    public void setCancelOnclickListener(onCancelOnclickListener cancelOnclickListener) {
        this.cancelOnclickListener = cancelOnclickListener;
    }

    //初始化所选个数
    private void setSelectState() {
        for (int i = 0; i < dataList.size(); i++) {
            if (Boolean.parseBoolean(dataList.get(i).get("selected").toString()))
                selectedSize++;
        }
        Log.i("selectedSize",selectedSize  + "");
        if(selectedSize >= dataList.size())//如果已经全选
            changeSelectAllState(true);
        else
            changeSelectAllState(false);
    }

    private void initListener() {
        adapter.setOnItemClickListener(new MultipleChoiceListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int p) {
                if (Boolean.parseBoolean(dataList.get(p).get("selected").toString())) {
                    selectedSize--;
                    dataList.get(p).put("selected", false);
                } else {
                    selectedSize++;
                    dataList.get(p).put("selected", true);
                }
                if (selectedSize >= dataList.size())
                    changeSelectAllState(true);
                if (selectedSize < dataList.size())
                    changeSelectAllState(false);
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
                dismiss();
                if (completeOnclickListener != null) {
                    completeOnclickListener.finishClick();
                }
            }
        });
        tvAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllSelect(!isAllSelected);
            }
        });
    }

    //全选
    private void AllSelect(boolean state) {
        isAllSelected = state;
        changeSelectAllState(state);
        if (!state)
            selectedSize = 0;
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).put("selected", state);
        }
        adapter.notifyDataSetChanged();
    }

    //改变全选的状态
    private void changeSelectAllState(boolean state) {
        if (state) {
            tvAllSelect.setTextColor(context.getResources().getColor(R.color.green));
            isAllSelected = state;
            selectedSize = dataList.size();
        } else {
            tvAllSelect.setTextColor(context.getResources().getColor(R.color.theme_secondary_text_inverted));
            isAllSelected = state;
        }
    }

    private void initComponent() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getOwnerActivity());
        dgRecycler.setLayoutManager(layoutManager);
        adapter = new MultipleChoiceListAdapter(dataList, context);
        dgRecycler.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_dialog);
        ButterKnife.bind(this);
        setSelectState();
        initState();
        initComponent();
        initListener();
        isCreate = true;
    }



    //设置Dialog的大小及其样式
    private void initState() {
        tvTitle.setText(title);
        //这是定位
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置大小
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    //暴露设置tvTitle的接口
    public void setTitle(String content) {
        this.title = content;
    }

    public MultipleChoiceListDialog(Context context, List<Map<String, Object>> dataList) {
        super(context, R.style.DialogTheme);
        this.dataList = dataList;
        this.context = context;
    }

    public MultipleChoiceListDialog(Context context, int themeResId, List<Map<String, Object>> dataList) {
        super(context, themeResId);
        this.dataList = dataList;
        this.context = context;

    }

    public MultipleChoiceListDialog(Context context, boolean cancelable, OnCancelListener cancelListener, List<Map<String, Object>> dataList) {
        super(context, cancelable, cancelListener);
        this.dataList = dataList;
        this.context = context;
    }
}
