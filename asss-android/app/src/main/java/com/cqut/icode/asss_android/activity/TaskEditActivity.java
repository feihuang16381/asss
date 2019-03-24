package com.cqut.icode.asss_android.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.view.MultipleChoiceListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动描述：任务-->任务领取右侧加号-->任务创建
 */
public class TaskEditActivity extends BaseActivity {
    @BindView(R.id.task_name)
    EditText taskName;
    @BindView(R.id.peoples)
    EditText peoples;
    @BindView(R.id.task_terminal)
    EditText taskTerminal;
    @BindView(R.id.task_type)
    Spinner taskType;
    @BindView(R.id.task_description)
    EditText taskDescription;
    @BindView(R.id.activity_task_edit)
    LinearLayout activityTaskEdit;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_affirm)
    TextView tvAffirm;

    private List<Map<String, Object>> personList = new ArrayList<>();

    private MultipleChoiceListDialog peopleListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        ButterKnife.bind(this);
        initDialog();
        initComponent();
        initListener();
        getPersonData();
        changePeopleText();
    }

    public void initDialog() {
        peopleListDialog = new MultipleChoiceListDialog(this, personList);
        peopleListDialog.setTitle("选择人员");

    }

    public void initComponent() {
        //toolbar
        activityTitle.setText("任务创建");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        peoples.setInputType(InputType.TYPE_NULL);
    }

    public void initListener() {
        peopleListDialog.setCancelOnclickListener(new MultipleChoiceListDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        peopleListDialog.setCompleteOnclickListener(new MultipleChoiceListDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                personList = peopleListDialog.getSelectedList();
                changePeopleText();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inquiryOut();
            }
        });
    }

    private void inquiryOut() {
        final AlertDialog.Builder queryDialog =
                new AlertDialog.Builder(this);
        queryDialog.setTitle("询问");
        queryDialog.setMessage("放弃保存?");
        queryDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        queryDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        queryDialog.show();
    }


    //修改人员选择情况
    private void changePeopleText() {
        StringBuffer stringBuffer = new StringBuffer();
        String content = "";
        for (int i = 0; i < personList.size(); i++) {
            if (Boolean.parseBoolean(personList.get(i).get("selected").toString()))
                stringBuffer.append("," + personList.get(i).get("name"));
        }
        if (stringBuffer.length() > 0)
            content = stringBuffer.substring(1);
        peoples.setText(content);
    }

    private void getPersonData() {
        String response = "[{ \"id\": \"1\", \"name\": \"张三\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"2\", \"name\": \"李四\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"3\", \"name\": \"王五\", \"selected\": \"true\" }]";
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        personList.addAll(list);
    }


    @OnClick({R.id.tv_affirm, R.id.peoples})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_affirm:
                this.finish();
                break;
            case R.id.peoples:
                peopleListDialog.show();
                break;
        }
    }
}
