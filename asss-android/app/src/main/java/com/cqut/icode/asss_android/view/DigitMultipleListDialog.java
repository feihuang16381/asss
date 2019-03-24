package com.cqut.icode.asss_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.DigitMultipleSelectAdapter;
import com.cqut.icode.asss_android.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：hwl
 * 时间：2017/7/28:17:33
 * 邮箱：1097412672@qq.com
 * 说明:提供存在数量选择的多选框
 */
public class DigitMultipleListDialog extends Dialog {

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.dg_recycler)
    RecyclerView dgRecycler;

    private List<Map<String, Object>> dataList = new ArrayList<>();
    private DigitMultipleSelectAdapter adapter;
    private String title;
    private onCompleteOnclickListener completeOnclickListener = null;
    private onCancelOnclickListener cancelOnclickListener = null;

    @OnClick({R.id.tv_cancel, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.cancelClick();
                }
                break;
            case R.id.tv_finish:
                dismiss();
                if (completeOnclickListener != null) {
                    completeOnclickListener.finishClick();
                }
                break;
        }
    }

    //获取所选择的序列
    public List<Map<String, Object>> getChangedData() {
        return this.dataList;
    }

    public interface onCompleteOnclickListener {
        void finishClick();

    }

    public interface onCancelOnclickListener {
        void cancelClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_digit_dialog);
        ButterKnife.bind(this);
        initState();
        initComponent();
    }


    //设置Dialog的大小及其样式
    private void initState() {
        tvTitle.setText(title);
        //这是定位
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置大小
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    //设置列表内容
    private void initComponent() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getOwnerActivity());
        dgRecycler.setLayoutManager(layoutManager);
        adapter = new DigitMultipleSelectAdapter(dataList);
        dgRecycler.setAdapter(adapter);
    }

    public void changeDataList(List<Map<String, Object>> dataList) {
        CommonUtil.cloneData(dataList,this.dataList);
        adapter.notifyDataSetChanged();
    }


    public void setCompleteOnclickListener(onCompleteOnclickListener completeOnclickListener) {
        this.completeOnclickListener = completeOnclickListener;
    }

    public void setCancelOnclickListener(onCancelOnclickListener cancelOnclickListener) {
        this.cancelOnclickListener = cancelOnclickListener;
    }

    public DigitMultipleListDialog(@NonNull Context context, List<Map<String, Object>> dataList, String title) {
        super(context, R.style.DialogTheme);
        CommonUtil.cloneData(dataList,this.dataList);
        this.title = title;
    }

    public DigitMultipleListDialog(@NonNull Context context, @StyleRes int themeResId, List<Map<String, Object>> dataList, String title) {
        super(context, themeResId);
        CommonUtil.cloneData(dataList, this.dataList);
        this.title = title;
    }

    public DigitMultipleListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, List<Map<String, Object>> dataList, String title) {
        super(context, cancelable, cancelListener);
        CommonUtil.cloneData(dataList, this.dataList);
        this.title = title;
    }
}
