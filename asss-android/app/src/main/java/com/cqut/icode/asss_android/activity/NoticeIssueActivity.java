package com.cqut.icode.asss_android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;

import butterknife.BindView;

/**
 * Created by 10713 on 2017/7/23.
 * 活动描述：个人中心-->通知-->通知详情右侧的加号-->编辑-->通知发布
 * 活动描述：个人中心-->通知-->通知右侧的加号-->通知发布
 */
public class NoticeIssueActivity extends BaseActivity {
    @BindView(R.id.head_return)
    ImageView im_return;
    @BindView(R.id.head_issue)
    TextView im_issue;
    @BindView(R.id.title)
    EditText et_title;
    @BindView(R.id.text_content)
    EditText et_text_content;
    @BindView(R.id.affix)
    ImageView im_affix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_issue);
        initialize();
    }

    private void initialize() {
        im_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        im_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });
    }

    public void getInfo() {
        String title = et_title.getText().toString().trim();
        String textinfo = et_text_content.getText().toString().trim();

        if((title.length() <= 40 && title.length() >= 4) && (textinfo.length() >= 15 && textinfo.length() <=1500)) {
            Toast.makeText(MyApplication.getContext(),"发布成功",Toast.LENGTH_SHORT).show();
        }
        else if(title.length() < 4)
            Toast.makeText(MyApplication.getContext(), "标题长度不能小于4个字", Toast.LENGTH_SHORT).show();
        else if(title.length() > 40)
            Toast.makeText(MyApplication.getContext(),"标题长度不能超过40个字",Toast.LENGTH_SHORT).show();
        else if(textinfo.length() < 15)
            Toast.makeText(MyApplication.getContext(), "正文内容长度不能小于15个字", Toast.LENGTH_SHORT).show();
        else if(textinfo.length() >1500)
            Toast.makeText(MyApplication.getContext(),"正文内容长度不能超过1500个字",Toast.LENGTH_SHORT).show();

    }
}
