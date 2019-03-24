package com.cqut.icode.asss_android.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.util.CommonUtil;
import com.cqut.icode.asss_android.util.TimeUtil;
import com.cqut.icode.asss_android.view.DigitMultipleListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动描述：个人中心-->请备-->ty-->请求备件
 */
public class ApplySparePartsActivity extends BaseActivity {

    @BindView(R.id.spare_parts)
    EditText spareParts;

    @BindView(R.id.apply_name)
    EditText applyName;
    @BindView(R.id.connect_project)
    EditText connectProject;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.apply_date)
    EditText applyDate;
    @BindView(R.id.apply_time)
    EditText applyTime;

    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Map<String, Object> dataList = new HashMap<>();
    private List<Map<String, Object>> newSpare_parts;
    private DigitMultipleListDialog digitMultipleListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_spare_parts);
        ButterKnife.bind(this);
        getApplyData();
        initValue();
        initDialog();
        initComponent();
        initListener();
    }

    public void initDialog() {
        digitMultipleListDialog = new DigitMultipleListDialog(this,
                newSpare_parts, "选择备件名称");
    }

    public void initComponent() {
        //toolbar
        activityTitle.setText("请求备件");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        spareParts.setInputType(InputType.TYPE_NULL);
        applyDate.setInputType(InputType.TYPE_NULL);
        applyTime.setInputType(InputType.TYPE_NULL);
    }

    public void initValue() {
        newSpare_parts = (List<Map<String, Object>>) dataList.get("spare_part_record");
        changeSpareParts();
        String[] when = TimeUtil.spitTime(dataList.get("apply_time").toString());
        applyTime.setText(when[1]);
        applyDate.setText(when[0]);
        connectProject.setText(dataList.get("task_name").toString());
        remark.setText(dataList.get("remark").toString());
    }

    public void setDate(int selectYear,int selectMonth,int selectDayOfMonth){
        year = selectYear;
        month = selectMonth;
        dayOfMonth = selectDayOfMonth;
        applyDate.setText(TimeUtil.formatDate(selectYear,selectMonth,selectDayOfMonth));

    }

    public void setTime(int selectHourOfDay, int selectMinute){
        hourOfDay = selectHourOfDay;
        minute = selectMinute;
        applyTime.setText(TimeUtil.formatTime(selectHourOfDay,selectMinute));
    }


    public void initListener() {
        datePickerDialog = new DatePickerDialog(ApplySparePartsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year,month,dayOfMonth);
            }
        },year,month,dayOfMonth);
        timePickerDialog = new TimePickerDialog(ApplySparePartsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay,minute);
            }
        }, hourOfDay, minute,true);

        digitMultipleListDialog.setCancelOnclickListener(new DigitMultipleListDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {
                digitMultipleListDialog.changeDataList(newSpare_parts);
            }
        });

        digitMultipleListDialog.setCompleteOnclickListener(new DigitMultipleListDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                //spare_parts中的内容被adapter修改
                CommonUtil.cloneData(digitMultipleListDialog.getChangedData(),newSpare_parts);
                changeSpareParts();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void changeSpareParts() {
        StringBuffer stringBuffer = new StringBuffer();
        String content = "";
        for (int i = 0; i < newSpare_parts.size(); i++) {
            int size = (int) Double.parseDouble(newSpare_parts.get(i).get("size").toString());
            if (size != 0)
                stringBuffer.append("," + size + "个" + newSpare_parts.get(i).get("spare_part_name"));
        }
        if (stringBuffer.length() > 0)
            content = stringBuffer.substring(1);
        spareParts.setText(content);
    }


    private void getApplyData() {
        String response = "{\n" +
                "      \"id\": \"123547\",\n" +
                "      \"creator_id\": \"123\",\n" +
                "      \"apply_time\": \"2017-7-20 7:20\",\n" +
                "      \"apply_name\": \"粮仓告急\",\n" +
                "      \"task_id\": \"565\",\n" +
                "      \"task_name\": \"利剑行动\",\n" +
                "      \"spare_part_record\": [\n" +
                "      {\n" +
                "          \"spare_part_id\": \"123\",\n" +
                "          \"spare_part_name\": \"GPU\",\n" +
                "          \"size\": 12\n" +
                "      },\n" +
                "      {\n" +
                "          \"spare_part_id\": \"123\",\n" +
                "          \"spare_part_name\": \"扇热器\",\n" +
                "          \"size\": 12\n" +
                "      },\n" +
                "      {\n" +
                "          \"spare_part_id\": \"123\",\n" +
                "          \"spare_part_name\": \"继电器\",\n" +
                "          \"size\": 12\n" +
                "      }],\n" +
                "      \"remark\":\"快到碗里来\"\n" +
                "  }";
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());
        dataList.putAll(map);
    }

    @OnClick({R.id.spare_parts, R.id.tv_affirm, R.id.apply_time, R.id.apply_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spare_parts:
                digitMultipleListDialog.show();
                break;
            case R.id.tv_affirm:
                saveData();
                break;
            case R.id.apply_time:
                timePickerDialog.show();
                break;
            case R.id.apply_date:
                datePickerDialog.show();
                break;
        }
    }

    private void saveData(){
        String get = "apply_name" + applyName.getText() + "apply_time"
                + applyDate.getText() + " " + applyTime.getText() + "connect_project" + connectProject.getText()
                + "remark" + remark.getText() + "spare_parts" + spareParts.getText();
        showToast(get);
    }


}
