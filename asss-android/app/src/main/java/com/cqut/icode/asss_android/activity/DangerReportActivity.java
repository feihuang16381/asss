package com.cqut.icode.asss_android.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.ContentPagerAdapter;
import com.cqut.icode.asss_android.common.LogUtil;
import com.cqut.icode.asss_android.common.StaticParameters;
import com.cqut.icode.asss_android.fragment.DangerMaintainFragment;
import com.cqut.icode.asss_android.fragment.InspectFragment;
import com.cqut.icode.asss_android.fragment.StandardizationFragment;
import com.cqut.icode.asss_android.util.ImageUtil;
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
import butterknife.Optional;

/**
 * 活动描述：个人中心-->维修记录-->类型：危险源-->危险源报告
 * 活动描述：首页-->选择分类为危险源的点击-->危险源报告
 */
public class DangerReportActivity extends BaseActivity {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.tv_affirm)
    TextView tvaffirm;
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
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    public static final int TAKE_PHOTO = 1;
    public static final int VIEW_PHOTO = 2;

    //刷新标识符
    public static final int INSPECT = 1;
    public static final int MAINTAIN = 2;
    public static final int STANDARD = 3;
    private String imagePath;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.ly_before)
    LinearLayout lyBefore;
    @BindView(R.id.img_before)
    ImageView imgBefore;
    @BindView(R.id.img_before_del)
    ImageView imgBeforeDel;
    @BindView(R.id.ly_ing)
    LinearLayout lyIng;
    @BindView(R.id.img_ing)
    ImageView imgIng;
    @BindView(R.id.img_ing_del)
    ImageView imgIngDel;
    @BindView(R.id.ly_end)
    LinearLayout lyEnd;
    @BindView(R.id.img_end)
    ImageView imgEnd;
    @BindView(R.id.img_end_del)
    ImageView imgEndDel;

    private Uri imageUri;
    private String terminalID = "456";
    private int selectedImageId;
    private String screenshotPath;
    private Map<String, Object> maintainData = new HashMap<>();
    private InspectFragment inspectFragment = new InspectFragment();
    private DangerMaintainFragment dangerMaintainFragment = new DangerMaintainFragment();
    private StandardizationFragment standardizationFragment = new StandardizationFragment();
    private Map<String, Object> terminalData = new HashMap<>();
    private ContentPagerAdapter contentPagerAdapter;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private Map<String, Object> shareData = new HashMap<String, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_report);
        ButterKnife.bind(this);
        getTerminalData();
        getPersonData();
        initDateTime();
        initComponent();
        initListener();
    }

    public Map<String, Object> getShareData() {
        return shareData;
    }

    public void setShareData(Map<String, Object> shareData) {
        this.shareData = shareData;
    }

    public void initComponent() {
        activityTitle.setText("危险源报告");
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_return);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        if (terminalData.get("type").toString().equals("1")) {
            terminalTypeIc.setImageResource(R.drawable.zhandian_ico_big_red);
            terminalType.setText("站点类型:危险源");
        } else {
            terminalTypeIc.setImageResource(R.drawable.zhandian_ico_big_yellow);
            terminalType.setText("站点类型:其他");
        }
        terminalCode.setText("站点编号:" + terminalData.get("station_code").toString());
        terminalName.setText(terminalData.get("terminal_name").toString());
        terminalLl.setText("站点经纬度:" + terminalData.get("longitude").toString() +
                " " + terminalData.get("latitude").toString());

        initTab();
        initContent();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inquiryOut();
            }
        });
    }

    @Optional
    @OnClick(R.id.tv_affirm)
    public void affirm() {
        showToast("保存");
        finish();
    }

    /**
     * uploadData(){
     * okhttp
     *     上传shareData
     * }
     */

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

    private void initTab() {
        tlTab.setTabMode(TabLayout.MODE_FIXED);
        tlTab.setTabTextColors(ContextCompat.getColor(this, R.color.theme_secondary_text_inverted),
                ContextCompat.getColor(this, R.color.colorAccent));
        tlTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
        ViewCompat.setElevation(tlTab, 10);
        tlTab.setupWithViewPager(vpContent);
    }

    private void initContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("巡检");
        tabIndicators.add("维护");
        tabIndicators.add("标定");
        tabFragments = new ArrayList<>();
        tabFragments.add(inspectFragment);
        tabFragments.add(dangerMaintainFragment);
        tabFragments.add(standardizationFragment);
        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(),
                getApplicationContext(), tabFragments, tabIndicators);
        vpContent.setAdapter(contentPagerAdapter);
    }


    //同步几个页面的数据
    public void synchronousData(int index) {
        LogUtil.d("---------ii", inspectFragment.isCreateView() + "");
        LogUtil.d("---------ii", dangerMaintainFragment.isCreateView() + "");
        LogUtil.d("---------ii", standardizationFragment.isCreateView() + "");
        if ((index != INSPECT) && inspectFragment.isCreate()) {
            inspectFragment.reLoadShareData();
            if (inspectFragment.isCreateView())
                inspectFragment.initShareValue();
        }
        if ((index != MAINTAIN) && dangerMaintainFragment.isCreate()) {
            dangerMaintainFragment.reLoadShareData();
            if (dangerMaintainFragment.isCreateView())
                dangerMaintainFragment.initShareValue();
        }
        if ((index != STANDARD) && standardizationFragment.isCreate()) {
            standardizationFragment.reLoadShareData();
            if (standardizationFragment.isCreateView())
                standardizationFragment.initShareValue();
        }
    }

    private void getPersonData() {
        //从服务器上获取
        String response = "[{ \"id\": \"1\", \"name\": \"张三\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"2\", \"name\": \"李四\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"3\", \"name\": \"王五\", \"selected\": \"true\" }]";
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        shareData.put("peoples", list);
    }

    private void initDateTime() {
        Calendar c = Calendar.getInstance();
        shareData.put("year", c.get(Calendar.YEAR));
        shareData.put("month", c.get(Calendar.MONTH));
        shareData.put("dayOfMonth", c.get(Calendar.DAY_OF_MONTH));
        shareData.put("minute", c.get(Calendar.MINUTE));
        shareData.put("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
    }

    private void getTerminalData() {
        String response = "{ \"station_code\": \"1234\", \"terminal_name\": \"重庆理工\", " +
                "\"id\": \"111\", \"type\": \"1\", \"longitude\": \"106.536192\", \"latitude\": \"29.45832\" }\n";
        terminalData.clear();
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());
        terminalData.putAll(data);
    }


    @OnClick({R.id.ly_before, R.id.img_before, R.id.img_before_del, R.id.ly_ing,
            R.id.img_ing, R.id.img_ing_del, R.id.ly_end, R.id.img_end, R.id.img_end_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_before:
                selectedImageId = R.id.img_before;
                checkPermission();
                break;
            case R.id.img_before:
                viewPhoto(maintainData.get(StaticParameters.IM_BEFORE).toString());
                break;
            case R.id.img_before_del:
                imgBefore.setVisibility(View.INVISIBLE);
                imgBeforeDel.setVisibility(View.GONE);
                lyBefore.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_BEFORE).toString());
                maintainData.put(StaticParameters.IM_BEFORE, null);
                break;
            case R.id.ly_ing:
                selectedImageId = R.id.img_ing;
                checkPermission();
                break;
            case R.id.img_ing:
                viewPhoto(maintainData.get(StaticParameters.IM_ING).toString());
                break;
            case R.id.img_ing_del:
                imgIng.setVisibility(View.INVISIBLE);
                imgIngDel.setVisibility(View.GONE);
                lyIng.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_ING).toString());
                maintainData.put(StaticParameters.IM_ING, null);
                break;
            case R.id.ly_end:
                selectedImageId = R.id.img_end;
                checkPermission();
                break;
            case R.id.img_end:
                viewPhoto(maintainData.get(StaticParameters.IM_END).toString());
                break;
            case R.id.img_end_del:
                imgEnd.setVisibility(View.INVISIBLE);
                imgEndDel.setVisibility(View.GONE);
                lyEnd.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_END).toString());
                maintainData.put(StaticParameters.IM_END, null);
                break;
        }
    }

    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(DangerReportActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(DangerReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(DangerReportActivity.this, permissions, 1);
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

    //查看图片
    private void viewPhoto(String imagePath) {
        Intent intent = new Intent(DangerReportActivity.this, ScanPhotoActivity.class);
        intent.putExtra(StaticParameters.IMAGE_PATH, imagePath);
        startActivity(intent);
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
            imageUri = FileProvider.getUriForFile(DangerReportActivity.this, "com.cqut.icode.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                Intent intent = new Intent(DangerReportActivity.this, ViewPhotoActivity.class);
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
}
