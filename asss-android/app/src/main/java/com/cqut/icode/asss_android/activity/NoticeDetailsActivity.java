package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;

import butterknife.BindView;

/**
 * Created by 10713 on 2017/7/21.
 * 活动描述：个人中心-->通知-->通知详情右侧的加号
 */
public class NoticeDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.head_return)
    ImageView im_return;
    @BindView(R.id.head_add)
    ImageView im_add;
    @BindView(R.id.notice_background)
    LinearLayout ll_background;
    @BindView(R.id.notice_head)
    TextView tv_head;
    @BindView(R.id.notice_creator)
    TextView tv_creator;

    LinearLayout edit;
    LinearLayout delete;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_datails);
        initialize();
        showInfo();
    }

    private void initialize() {
        tv_creator.setMovementMethod(ScrollingMovementMethod.getInstance());
        im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        im_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showInfo(){
        Intent intent = getIntent();
        tv_head.setText(intent.getStringExtra("title").toString().trim());
        tv_creator.setText(intent.getStringExtra("founder").toString().trim() + "： " + intent.getStringExtra("content").toString().trim());

    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(NoticeDetailsActivity.this).inflate(R.layout.lost_pop_menu,null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        edit = (LinearLayout)contentView.findViewById(R.id.ll_edit);
        delete = (LinearLayout)contentView.findViewById(R.id.ll_delete);

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);

        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        popupWindow.setTouchable(true);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAsDropDown(im_add,-210,10);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_edit:
                Intent intent = new Intent(MyApplication.getContext(),NoticeIssueNumActivity.class);
                intent.putExtra("title",tv_head.getText().toString());
                intent.putExtra("info",tv_creator.getText().toString());
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.ll_delete:
                Toast.makeText(MyApplication.getContext(),"删除",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
        }
    }

}