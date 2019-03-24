package com.cqut.icode.asss_android.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.activity.DangerReportActivity;
import com.cqut.icode.asss_android.common.LogUtil;
import com.cqut.icode.asss_android.util.TimeUtil;
import com.cqut.icode.asss_android.view.BigInfoDialog;
import com.cqut.icode.asss_android.view.MultipleChoiceListDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者：hwl
 * 时间：2017/7/12:15:27
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class StandardizationFragment extends Fragment {

    @BindView(R.id.sd_peoples)
    EditText sdPeoples;
    @BindView(R.id.before_stand)
    EditText beforeStand;
    @BindView(R.id.after_stand)
    EditText afterStand;
    @BindView(R.id.sd_recover)
    RadioButton sdRecover;
    @BindView(R.id.sd_no_recover)
    RadioButton sdNoRecover;
    @BindView(R.id.sd_finish_date)
    EditText sdFinishDate;
    @BindView(R.id.sd_finish_time)
    EditText sdFinishTime;
    @BindView(R.id.sd_remark)
    EditText sdRemark;
    private Unbinder unbinder;
    private List<Map<String, Object>> personList = new ArrayList<>();
    private MultipleChoiceListDialog multipleChoiceListDialog;
    private BigInfoDialog bigInfoDialog;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private boolean isCreateView;
    private boolean isCreate;
    private String remark;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private DangerReportActivity activity;
    private Map<String, Object> shareData;

    public StandardizationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (DangerReportActivity) getActivity();
        shareData = activity.getShareData();
        isCreate = true;
        LoadShareData();
        multipleChoiceListDialog = new MultipleChoiceListDialog(getContext(), personList);
        multipleChoiceListDialog.setTitle("人员选择");
        bigInfoDialog = new BigInfoDialog(getContext());
        bigInfoDialog.setTitle("备注信息");

        initListener();
        LogUtil.d("--------------","StandardizationFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCreate = false;
    }
    public boolean isCreate() {
        return isCreate;
    }

    public void initListener(){
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year,month,dayOfMonth);
                //修改共享区时间
                changeShareDate();
            }
        },year,month,dayOfMonth);
        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay,minute);
                //修改共享区时间
                changeShareTime();
            }
        }, hourOfDay, minute,true);

        multipleChoiceListDialog.setCancelOnclickListener(new MultipleChoiceListDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        multipleChoiceListDialog.setCompleteOnclickListener(new MultipleChoiceListDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                personList = multipleChoiceListDialog.getSelectedList();
                changePeopleText();
                changeSharePeopleData();
            }
        });
        bigInfoDialog.setCancelOnclickListener(new BigInfoDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        bigInfoDialog.setCompleteOnclickListener(new BigInfoDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                remark = bigInfoDialog.getInfo();
                sdRemark.setText(remark);
            }
        });
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
        sdPeoples.setText(content);
    }

    public void changeSharePeopleData(){
        List<Map<String,Object>> peoples = (List<Map<String,Object>>)shareData.get("peoples");
        peoples.clear();
        peoples.addAll(personList);//修改共享人员选择信息
        activity.synchronousData(DangerReportActivity.STANDARD);
    }


    public void setDate(int selectYear,int selectMonth,int selectDayOfMonth){
        year = selectYear;
        month = selectMonth;
        dayOfMonth = selectDayOfMonth;
        sdFinishDate.setText(TimeUtil.formatDate(selectYear,selectMonth,selectDayOfMonth));

    }

    public void changeShareDate(){
        shareData.put("year",year);
        shareData.put("month",month);
        shareData.put("dayOfMonth",dayOfMonth);
        activity.synchronousData(DangerReportActivity.STANDARD);
    }

    public void changeShareTime(){
        shareData.put("minute",minute);
        shareData.put("hourOfDay",hourOfDay);
        activity.synchronousData(DangerReportActivity.STANDARD);
    }

    public void setTime(int selectHourOfDay, int selectMinute){
        hourOfDay = selectHourOfDay;
        minute = selectMinute;
        sdFinishTime.setText(TimeUtil.formatTime(selectHourOfDay,selectMinute));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standardization, container, false);
        unbinder = ButterKnife.bind(this, view);
        LogUtil.d("---------createView","StandardizationFragment");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponent();
        initShareValue();
        isCreateView = true;
    }

    //初始化共享数据
    public void initShareValue(){
        changePeopleText();
        setDate(year,month,dayOfMonth);
        setTime(hourOfDay,minute);
    }

    public void initComponent() {
        sdPeoples.setInputType(InputType.TYPE_NULL);
        sdFinishTime.setInputType(InputType.TYPE_NULL);
        sdFinishDate.setInputType(InputType.TYPE_NULL);
        sdRemark.setInputType(InputType.TYPE_NULL);
    }



    public void LoadShareData(){
        personList.clear();
        personList.addAll((List<Map<String, Object>>) shareData.get("peoples"));
        year = Integer.parseInt(shareData.get("year").toString());
        month = Integer.parseInt(shareData.get("month").toString());
        dayOfMonth = Integer.parseInt(shareData.get("dayOfMonth").toString());
        hourOfDay = Integer.parseInt(shareData.get("hourOfDay").toString());
        minute = Integer.parseInt(shareData.get("minute").toString());
    }

    public void reLoadShareData(){
        LoadShareData();
        multipleChoiceListDialog.setDataList(personList);
    }

    public boolean isCreateView() {
        return isCreateView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        isCreateView = false;
        LogUtil.d("---------destroy","StandardizationFragment");
    }
    @OnClick({R.id.sd_peoples, R.id.sd_finish_date, R.id.sd_finish_time, R.id.sd_remark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sd_peoples:
                multipleChoiceListDialog.show();
                break;
            case R.id.sd_finish_date:
                datePickerDialog.show();
                break;
            case R.id.sd_finish_time:
                timePickerDialog.show();
                break;
            case R.id.sd_remark:
                bigInfoDialog.setInfo(remark);
                bigInfoDialog.show();
                break;
        }
    }
}
