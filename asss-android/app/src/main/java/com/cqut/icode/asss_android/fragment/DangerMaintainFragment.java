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
import android.widget.TimePicker;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.activity.DangerReportActivity;
import com.cqut.icode.asss_android.common.LogUtil;
import com.cqut.icode.asss_android.util.TimeUtil;
import com.cqut.icode.asss_android.view.BigInfoDialog;
import com.cqut.icode.asss_android.view.MultipleChoiceListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DangerMaintainFragment extends Fragment {


    @BindView(R.id.mt_peoples)
    EditText mtPeoples;
    @BindView(R.id.mt_description)
    EditText mtDescription;
    @BindView(R.id.replace_facility)
    EditText replaceFacility;
    @BindView(R.id.mt_finish_date)
    EditText mtFinishDate;
    @BindView(R.id.mt_finish_time)
    EditText mtFinishTime;
    @BindView(R.id.dg_mt_remark)
    EditText dgMtRemark;
    private Unbinder unbinder;

    private List<Map<String, Object>> personList = new ArrayList<>();
    private List<Map<String, Object>> facilityList = new ArrayList<>();
    private MultipleChoiceListDialog peopleListDialog;
    private MultipleChoiceListDialog facilityListDialog;
    private BigInfoDialog remarkDialog;
    private BigInfoDialog faultDescriptionDialog;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private boolean isCreateView;
    private boolean isCreate;
    private String remark;
    private String faultDescription;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private  DangerReportActivity activity;
    private Map<String, Object> shareData;

    public DangerMaintainFragment() {

    }

    public boolean isCreate() {
        return isCreate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (DangerReportActivity) getActivity();
        shareData = activity.getShareData();
        isCreate = true;
        LoadShareData();
        loadPrivateData();
        peopleListDialog = new MultipleChoiceListDialog(getContext(), personList);
        peopleListDialog.setTitle("选择人员");
        facilityListDialog = new MultipleChoiceListDialog(getContext(), facilityList);
        facilityListDialog.setTitle("更换备件");
        remarkDialog = new BigInfoDialog(getContext());
        remarkDialog.setTitle("备注信息");
        faultDescriptionDialog = new BigInfoDialog(getContext());
        faultDescriptionDialog.setTitle("故障描述");


        initListener();
        LogUtil.d("--------------","DangerMaintainFragment");
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
                changeSharePeopleData();
            }
        });
        facilityListDialog.setCancelOnclickListener(new MultipleChoiceListDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        facilityListDialog.setCompleteOnclickListener(new MultipleChoiceListDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                facilityList = facilityListDialog.getSelectedList();
                changeFacilityText();
            }
        });
        remarkDialog.setCancelOnclickListener(new BigInfoDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        remarkDialog.setCompleteOnclickListener(new BigInfoDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                remark = remarkDialog.getInfo();
                dgMtRemark.setText(remark);
            }
        });
        faultDescriptionDialog.setCancelOnclickListener(new BigInfoDialog.onCancelOnclickListener(){
            @Override
            public void cancelClick() {

            }
        });
        faultDescriptionDialog.setCompleteOnclickListener(new BigInfoDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                faultDescription = faultDescriptionDialog.getInfo();
                mtDescription.setText(faultDescription);
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
        mtPeoples.setText(content);
        LogUtil.d("----mtPeoples",mtPeoples.getText().toString());
    }

    //修改替换备件选择情况
    private void changeFacilityText() {
        StringBuffer stringBuffer = new StringBuffer();
        String content = "";
        for (int i = 0; i < facilityList.size(); i++) {
            if (Boolean.parseBoolean(facilityList.get(i).get("selected").toString()))
                stringBuffer.append("," + facilityList.get(i).get("name"));
        }
        if (stringBuffer.length() > 0)
            content = stringBuffer.substring(1);
        replaceFacility.setText(content);
    }

    public void changeSharePeopleData(){
        List<Map<String,Object>> peoples = (List<Map<String,Object>>)shareData.get("peoples");
        peoples.clear();
        peoples.addAll(personList);//修改共享人员选择信息
        activity.synchronousData(DangerReportActivity.MAINTAIN);
    }


    public void setDate(int selectYear,int selectMonth,int selectDayOfMonth){
        year = selectYear;
        month = selectMonth;
        dayOfMonth = selectDayOfMonth;
        mtFinishDate.setText(TimeUtil.formatDate(selectYear,selectMonth,selectDayOfMonth));

    }

    public void changeShareDate(){
        shareData.put("year",year);
        shareData.put("month",month);
        shareData.put("dayOfMonth",dayOfMonth);
        activity.synchronousData(DangerReportActivity.MAINTAIN);
    }

    public void changeShareTime(){
        shareData.put("minute",minute);
        shareData.put("hourOfDay",hourOfDay);
        activity.synchronousData(DangerReportActivity.MAINTAIN);
    }

    public void setTime(int selectHourOfDay, int selectMinute){
        hourOfDay = selectHourOfDay;
        minute = selectMinute;
        mtFinishTime.setText(TimeUtil.formatTime(selectHourOfDay,selectMinute));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danger_maintain, container, false);
        unbinder = ButterKnife.bind(this, view);
        LogUtil.d("---------createView","DangerMaintainFragment");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponent();
        initShareValue();
        initPrivateValue();
        isCreateView = true;
    }

    public void initPrivateValue(){
        changeFacilityText();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCreate = false;
    }

    public boolean isCreateView() {
        return isCreateView;
    }

    //初始化共享数据
    public void initShareValue(){
        changePeopleText();
        setDate(year,month,dayOfMonth);
        setTime(hourOfDay,minute);
    }

    public void initComponent() {
        mtPeoples.setInputType(InputType.TYPE_NULL);
        mtDescription.setInputType(InputType.TYPE_NULL);
        replaceFacility.setInputType(InputType.TYPE_NULL);
        mtFinishTime.setInputType(InputType.TYPE_NULL);
        mtFinishDate.setInputType(InputType.TYPE_NULL);
        dgMtRemark.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        isCreateView = false;
        LogUtil.d("---------destroy","DangerMaintainFragment");
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
        peopleListDialog.setDataList(personList);
    }

    @OnClick({R.id.mt_peoples, R.id.mt_description, R.id.replace_facility,
            R.id.mt_finish_date, R.id.mt_finish_time,R.id.dg_mt_remark})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mt_peoples:
                peopleListDialog.show();
                break;
            case R.id.mt_description:
                faultDescriptionDialog.setInfo(faultDescription);
                faultDescriptionDialog.show();
                break;
            case R.id.replace_facility:
                facilityListDialog.show();
                break;
            case R.id.mt_finish_date:
                datePickerDialog.show();
                break;
            case R.id.mt_finish_time:
                timePickerDialog.show();
                break;
            case R.id.dg_mt_remark:
                remarkDialog.setInfo(remark);
                remarkDialog.show();
                break;
        }
    }
    
    public void loadPrivateData(){
        String response = "[{ \"id\": \"1\", \"name\": \"CUP\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"2\", \"name\": \"GPU\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"3\", \"name\": \"显示屏\", \"selected\": \"true\" },\n" +
                "    { \"id\": \"4\", \"name\": \"扇热器\", \"selected\": false },\n" +
                "    { \"id\": \"5\", \"name\": \"主机\", \"selected\": true },\n" +
                "    { \"id\": \"6\", \"name\": \"键盘\", \"selected\": false },\n" +
                "    { \"id\": \"7\", \"name\": \"投影仪\", \"selected\": false },\n" +
                "    { \"id\": \"8\", \"name\": \"操作员\", \"selected\": false },\n" +
                "    { \"id\": \"9\", \"name\": \"验钞机\", \"selected\": false }\n" +
                "]";
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken< List<Map<String, Object>>>(){}.getType());
        facilityList.clear();
        facilityList.addAll(list);
    }

}
