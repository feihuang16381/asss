package com.cqut.icode.asss_android.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.LogUtil;
import com.cqut.icode.asss_android.common.StaticParameters;
import com.cqut.icode.asss_android.util.ImageUtil;
import com.cqut.icode.asss_android.util.TimeUtil;
import com.cqut.icode.asss_android.view.BigInfoDialog;
import com.cqut.icode.asss_android.view.MultipleChoiceListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动描述：个人中心-->维修记录-->类型：其他-->通用维护
 * 活动描述：首页-->选择分类为其他的点击-->通用维护
 */
public class CommonMaintainActivity extends BaseActivity {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.terminal_type_ic)
    ImageView terminalTypeIc;
    @BindView(R.id.terminal_name)
    TextView terminalName;
    @BindView(R.id.terminal_code)
    TextView terminalCode;
    @BindView(R.id.terminal_type)
    TextView terminalType;
    @BindView(R.id.terminal_ll)
    TextView terminalLl;
    @BindView(R.id.peoples)
    EditText peoples;
    @BindView(R.id.fault)
    EditText fault;
    @BindView(R.id.change_parts)
    EditText changeParts;

    @BindView(R.id.remark_info)
    EditText remarkInfo;
    @BindView(R.id.cm_finish_date)
    EditText cmFinishDate;
    @BindView(R.id.cm_finish_time)
    EditText cmFinishTime;
    @BindView(R.id.tv_affirm)
    TextView tvAffirm;
    @BindView(R.id.ly_before)
    LinearLayout lyBefore;
    @BindView(R.id.ly_ing)
    LinearLayout lyIng;
    @BindView(R.id.ly_end)
    LinearLayout lyEnd;

    public static final int TAKE_PHOTO = 1;
    public static final int VIEW_PHOTO = 2;
    private String imagePath;
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.img_before_del)
    ImageView imgBeforeDel;
    @BindView(R.id.img_ing_del)
    ImageView imgIngDel;
    @BindView(R.id.img_end_del)
    ImageView imgEndDel;
    @BindView(R.id.img_before)
    ImageView imgBefore;
    @BindView(R.id.img_ing)
    ImageView imgIng;
    @BindView(R.id.img_end)
    ImageView imgEnd;
    private List<Map<String, Object>> personList = new ArrayList<>();
    private List<Map<String, Object>> facilityList = new ArrayList<>();
    private MultipleChoiceListDialog peopleListDialog;
    private MultipleChoiceListDialog facilityListDialog;
    private BigInfoDialog remarkDialog;
    private BigInfoDialog faultDescriptionDialog;
    private Uri imageUri;
    private String screenshotPath;
    private Map<String, Object> maintainData = new HashMap<>();
    private int year;
    private int month;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private String remark;
    private String terminalID = "123";
    private String faultDescription;
    private int selectedImageId;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_maintain);
        ButterKnife.bind(this);
        initDialog();
        initComponent();
        initListener();
        getPersonData();
        getFacilityData();
        changePeopleText();
        changeFacilityText();
        initDateTime();
        setTime(hourOfDay, minute);
        setDate(year, month, dayOfMonth);
    }

    public void initComponent() {
        //toolbar
        activityTitle.setText("通用维护");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        peoples.setInputType(InputType.TYPE_NULL);
        fault.setInputType(InputType.TYPE_NULL);
        changeParts.setInputType(InputType.TYPE_NULL);
        cmFinishDate.setInputType(InputType.TYPE_NULL);
        cmFinishTime.setInputType(InputType.TYPE_NULL);
        remarkInfo.setInputType(InputType.TYPE_NULL);
    }


    public void initDialog() {
        peopleListDialog = new MultipleChoiceListDialog(this, personList);
        peopleListDialog.setTitle("选择人员");
        facilityListDialog = new MultipleChoiceListDialog(this, facilityList);
        facilityListDialog.setTitle("更换备件");
        remarkDialog = new BigInfoDialog(this);
        remarkDialog.setTitle("备注信息");
        faultDescriptionDialog = new BigInfoDialog(this);
        faultDescriptionDialog.setTitle("故障描述");
    }

    public void initListener() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(hourOfDay, minute);
            }
        }, hourOfDay, minute, true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inquiryOut();
            }
        });
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
                remarkInfo.setText(remark);
            }
        });
        faultDescriptionDialog.setCancelOnclickListener(new BigInfoDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        faultDescriptionDialog.setCompleteOnclickListener(new BigInfoDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                faultDescription = faultDescriptionDialog.getInfo();
                fault.setText(faultDescription);
            }
        });
    }

    @Override
    public void onBackPressed() {
        inquiryOut();
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
        changeParts.setText(content);
    }

    public void setDate(int selectYear, int selectMonth, int selectDayOfMonth) {
        year = selectYear;
        month = selectMonth;
        dayOfMonth = selectDayOfMonth;
        cmFinishDate.setText(TimeUtil.formatDate(selectYear,selectMonth,selectDayOfMonth));
    }

    public void setTime(int selectHourOfDay, int selectMinute) {
        hourOfDay = selectHourOfDay;
        minute = selectMinute;
        cmFinishTime.setText(TimeUtil.formatTime(selectHourOfDay,selectMinute));
    }

    @OnClick({R.id.peoples, R.id.fault, R.id.change_parts,
            R.id.tv_affirm, R.id.ly_before, R.id.ly_ing, R.id.ly_end,
            R.id.cm_finish_date, R.id.cm_finish_time, R.id.remark_info,
            R.id.img_before_del, R.id.img_ing_del, R.id.img_end_del,
            R.id.img_before, R.id.img_ing, R.id.img_end})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.peoples:
                peopleListDialog.show();
                break;
            case R.id.fault:
                faultDescriptionDialog.setInfo(faultDescription);
                faultDescriptionDialog.show();
                break;
            case R.id.change_parts:
                facilityListDialog.show();
                break;
            case R.id.cm_finish_date:
                datePickerDialog.show();
                break;
            case R.id.cm_finish_time:
                timePickerDialog.show();
                break;
            case R.id.remark_info:
                remarkDialog.setInfo(remark);
                remarkDialog.show();
                break;
            case R.id.tv_affirm:
                break;
            case R.id.img_before_del:
                imgBefore.setVisibility(View.INVISIBLE);
                imgBeforeDel.setVisibility(View.GONE);
                lyBefore.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_BEFORE).toString());
                maintainData.put(StaticParameters.IM_BEFORE, null);
                break;
            case R.id.img_ing_del:
                imgIng.setVisibility(View.INVISIBLE);
                imgIngDel.setVisibility(View.GONE);
                lyIng.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_ING).toString());
                maintainData.put(StaticParameters.IM_ING, null);
                break;
            case R.id.img_end_del:
                imgEnd.setVisibility(View.INVISIBLE);
                imgEndDel.setVisibility(View.GONE);
                lyEnd.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_END).toString());
                maintainData.put(StaticParameters.IM_END, null);
                break;
            case R.id.ly_before:
                selectedImageId = R.id.img_before;
                checkPermission();
                break;
            case R.id.ly_ing:
                selectedImageId = R.id.img_ing;
                checkPermission();
                break;
            case R.id.ly_end:
                selectedImageId = R.id.img_end;
                checkPermission();
                break;
            case R.id.img_before:
                viewPhoto(maintainData.get(StaticParameters.IM_BEFORE).toString());
                break;
            case R.id.img_ing:
                viewPhoto(maintainData.get(StaticParameters.IM_ING).toString());
                break;
            case R.id.img_end:
                viewPhoto(maintainData.get(StaticParameters.IM_END).toString());
                break;
        }
    }


    //查看图片
    private void viewPhoto(String imagePath) {
        Intent intent = new Intent(CommonMaintainActivity.this, ScanPhotoActivity.class);
        intent.putExtra(StaticParameters.IMAGE_PATH, imagePath);
        startActivity(intent);
    }


    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CommonMaintainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(CommonMaintainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CommonMaintainActivity.this, permissions, 1);
        } else {
            takenPicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    takenPicture();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void setImage() {

        switch (selectedImageId) {
            case R.id.img_before:
                lyBefore.setVisibility(View.GONE);
                maintainData.put(StaticParameters.IM_BEFORE, screenshotPath);
                imgBefore.setVisibility(View.VISIBLE);
                LogUtil.d("imgBefore.getWidth()", imgBefore.getWidth() + "");
                LogUtil.d("imgBefore.getHeight()", imgBefore.getHeight() + "");
                imgBefore.setImageBitmap(ImageUtil.getFitBitMap(screenshotPath,
                        imgBefore.getWidth(), imgBefore.getHeight()));
                imgBeforeDel.setVisibility(View.VISIBLE);
                break;
            case R.id.img_ing:
                lyIng.setVisibility(View.GONE);

                maintainData.put(StaticParameters.IM_ING, screenshotPath);
                imgIng.setVisibility(View.VISIBLE);
                imgIng.setImageBitmap(ImageUtil.getFitBitMap(screenshotPath,
                        imgIng.getWidth(), imgIng.getHeight()));
                imgIngDel.setVisibility(View.VISIBLE);
                break;
            case R.id.img_end:
                lyEnd.setVisibility(View.GONE);
                maintainData.put(StaticParameters.IM_END, screenshotPath);
                imgEnd.setVisibility(View.VISIBLE);
                imgEnd.setImageBitmap(ImageUtil.getFitBitMap(screenshotPath,
                        imgEnd.getWidth(), imgEnd.getHeight()));
                imgEndDel.setVisibility(View.VISIBLE);
                break;
        }
    }

    public String mdImageName() {
        StringBuffer stringBuffer = new StringBuffer(terminalID);
        switch (selectedImageId) {
            case R.id.img_before:
                stringBuffer.append("bef.JPG");
                break;
            case R.id.img_ing:
                stringBuffer.append("ing.JPG");
                break;
            case R.id.img_end:
                stringBuffer.append("end.JPG");
                break;
        }
        return stringBuffer.toString();
    }


    private void takenPicture() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        imagePath = outputImage.getPath();
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(CommonMaintainActivity.this, "com.cqut.icode.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                Intent intent = new Intent(CommonMaintainActivity.this, ViewPhotoActivity.class);
                intent.putExtra(StaticParameters.IMAGE_PATH, imagePath);
                intent.putExtra(StaticParameters.SCREENSHOT_NAME, mdImageName());
                startActivityForResult(intent, VIEW_PHOTO);

            }
            if (requestCode == VIEW_PHOTO) {
                screenshotPath = data.getStringExtra(StaticParameters.SCREENSHOT_PATH);
                setImage();
            }
        }
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

    public void getFacilityData() {
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
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        facilityList.clear();
        facilityList.addAll(list);
    }

    private void initDateTime() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        minute = c.get(Calendar.MINUTE);
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    }

}
