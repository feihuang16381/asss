package com.cqut.icode.asss_android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * 作者：hwl
 * 时间：2017/7/13:20:11
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class BigInfoDialog extends Dialog {
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tv_info)
    EditText tvInfo;

    private String info;
    private String title;
    private onCompleteOnclickListener completeOnclickListener = null;
    private onCancelOnclickListener cancelOnclickListener = null;


    public BigInfoDialog(Context context) {
        super(context,R.style.DialogTheme);
    }

    public BigInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public BigInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public String getInfo() {
        return tvInfo.getText().toString();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_info_dialog);
        ButterKnife.bind(this);
        initState();
    }

    //设置Dialog的大小及其样式
    private void initState() {
        //设置title
        tvTitle.setText(title);
        //定位
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public interface onCompleteOnclickListener {
        void finishClick();

    }

    public interface onCancelOnclickListener {
        void cancelClick();
    }

    @Optional
    @OnClick({R.id.tv_cancel, R.id.tv_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                cancelOnclickListener.cancelClick();
                break;
            case R.id.tv_finish:
                dismiss();
                completeOnclickListener.finishClick();
                break;
        }
    }

    public void setCompleteOnclickListener(onCompleteOnclickListener completeOnclickListener) {
        this.completeOnclickListener = completeOnclickListener;
    }

    public void setCancelOnclickListener(onCancelOnclickListener cancelOnclickListener) {
        this.cancelOnclickListener = cancelOnclickListener;
    }
}
