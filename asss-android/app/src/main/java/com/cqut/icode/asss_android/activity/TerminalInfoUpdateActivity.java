package com.cqut.icode.asss_android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.cqut.icode.asss_android.DataBase.MyDatabaseHelper;
import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;
import com.cqut.icode.asss_android.common.StaticParameters;
import com.cqut.icode.asss_android.util.ImageUtil;
import com.cqut.icode.asss_android.view.BigInfoDialog;
import com.cqut.icode.asss_android.view.SingleChoiceListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by 10713 on 2017/7/29.
 */

public class TerminalInfoUpdateActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbartitle;
    @BindView(R.id.affirm)
    TextView tv_affirm;

    @BindView(R.id.area)
    TextView tv_area;
    @BindView(R.id.terminal_name)
    EditText et_terminal_name;
    @BindView(R.id.terminal_code)
    EditText et_terminal_code;
    @BindView(R.id.equipment_id)
    EditText et_equipment_id;
    @BindView(R.id.equipment_type)
    LinearLayout ll_equipment_type;
    @BindView(R.id.equipment_type_show)
    TextView tv_equipment_type_show;
    @BindView(R.id.choose_type)
    ImageView chooseType;
    @BindView(R.id.item)
    EditText et_item;
    @BindView(R.id.card_num)
    EditText et_card_num;
    @BindView(R.id.radioGroup)
    RadioGroup rg_radioGroup;
    @BindView(R.id.yes)
    RadioButton radioButton_yes;
    @BindView(R.id.no)
    RadioButton radioButton_no;
    @BindView(R.id.remark_info)
    TextView et_remark_info;
    @BindView(R.id.longitude)
    TextView tv_longitude;
    @BindView(R.id.latitude)
    TextView tv_latitude;

    @BindView(R.id.mapView)
    TextureMapView mapView;

    @BindView(R.id.ly_before)
    LinearLayout lyBefore;
    @BindView(R.id.ly_ing)
    LinearLayout lyIng;
    @BindView(R.id.ly_end)
    LinearLayout lyEnd;

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

    public static final int TAKE_PHOTO = 1;
    public static final int VIEW_PHOTO = 2;

    private int selectedImageId;
    private Uri imageUri;
    private String screenshotPath;
    private String terminalID = "123";
    private Map<String, Object> maintainData = new HashMap<>();

    public LocationClient locationClient;
    public View markView;
    public  TextView windowText;
    public TextView mapLongitude;
    public TextView mapLatitude;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private SingleChoiceListDialog singleChoiceListDialog;
    private BigInfoDialog remarkDialog;
    private String remark;
    private String isOverInsured;
    private Boolean isAffirm = false;
    private PopupWindow popupWindow;

    private List<Map<String, Object>> areadate = new ArrayList<>();

    private MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.terminal_info_add);
        dbHelper = new MyDatabaseHelper(this,"notice.db",null,1);
        getAreaData();
        initComponent();
        initBaiDuMap();
        getPermissions();
    }

    private void initComponent() {
        toolbar.setTitle("");
        toolbartitle.setText("站点信息更新");
        toolbar.setNavigationIcon(R.drawable.icon_return);
        setSupportActionBar(toolbar);

        markView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.terminal_update_location, null);
        windowText = (TextView)markView.findViewById(R.id.terminal_name);
        mapLongitude = (TextView)markView.findViewById(R.id.map_longitude);
        mapLatitude = (TextView)markView.findViewById(R.id.map_latitude);

        singleChoiceListDialog = new SingleChoiceListDialog(TerminalInfoUpdateActivity.this,areadate);

        remarkDialog = new BigInfoDialog(this);
        remarkDialog.setTitle("备注信息");

        photoListener();

        initListener();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAffirm)
                    finish();
                else
                    inquiryOut();
            }
        });
        tv_affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataToSQL();
                isAffirm = true;
            }
        });
        tv_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChoiceListDialog.show();
            }
        });

        //设备类型
        ll_equipment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        //是否过保
        rg_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.yes) {
                    isOverInsured = "是";
                }
                else if (checkedId == R.id.no) {
                    isOverInsured = "否";
                }
            }
        });
        //备注信息
        et_remark_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkDialog.setInfo(remark);
                remarkDialog.show();
            }
        });

    }

    public void updateDataToSQL() {
        db = dbHelper.getWritableDatabase();

        values.clear();
        values.put("area",tv_area.getText().toString().trim());
        values.put("terminal_name",et_terminal_name.getText().toString().trim());
        values.put("equipment_id",et_equipment_id.getText().toString().trim());
        values.put("equipment_type_show",tv_equipment_type_show.getText().toString().trim());
        values.put("item",et_item.getText().toString().trim());
        values.put("card_num",et_card_num.getText().toString().trim());
        values.put("isover_insured",isOverInsured);
        values.put("remark_info",et_remark_info.getText().toString().trim());
        values.put("longitude",tv_longitude.getText().toString().trim());
        values.put("latitude",tv_latitude.getText().toString().trim());

        db.update("terminal_info",values,"terminal_code=?",new String[]{et_terminal_code.getText().toString().trim()});
        Toast.makeText(MyApplication.getContext(),"数据更新成功,保存",Toast.LENGTH_SHORT).show();

    }

    public void initListener(){
        singleChoiceListDialog.setCancelOnclickListener(new SingleChoiceListDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
        singleChoiceListDialog.setCompleteOnclickListener(new SingleChoiceListDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                areadate = singleChoiceListDialog.getSelectedList();
                String content = "";
                for(int i = 0; i < areadate.size(); i++) {
                    if(Boolean.parseBoolean(areadate.get(i).get("selected").toString())) {
                        content = areadate.get(i).get("name").toString();
                    }
                }
                tv_area.setText(content);
            }
        });
        remarkDialog.setCompleteOnclickListener(new BigInfoDialog.onCompleteOnclickListener() {
            @Override
            public void finishClick() {
                remark = remarkDialog.getInfo();
                et_remark_info.setText(remark);
            }
        });

        remarkDialog.setCancelOnclickListener(new BigInfoDialog.onCancelOnclickListener() {
            @Override
            public void cancelClick() {

            }
        });
    }

    private void getAreaData() {
        String response = "[{ \"id\": \"1\", \"name\": \"巴南区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"2\", \"name\": \"南岸区\", \"selected\": \"true\" },\n" +
                "    { \"id\": \"3\", \"name\": \"江北区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"4\", \"name\": \"渝北区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"5\", \"name\": \"两江新区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"6\", \"name\": \"九龙坡区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"7\", \"name\": \"合川区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"8\", \"name\": \"江津区\", \"selected\": \"false\" },\n" +
                "    { \"id\": \"9\", \"name\": \"沙坪坝区\", \"selected\": \"false\" }]";
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        areadate.clear();
        areadate.addAll(list);
    }

    public void showPopupWindow() {
        View contentView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.equipment_type_popup_menu,null);

        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout ll_other = (LinearLayout)contentView.findViewById(R.id.other);
        LinearLayout ll_danger_source = (LinearLayout)contentView.findViewById(R.id.danger_source);
        final TextView otherName = (TextView)contentView.findViewById(R.id.other_name);
        final TextView dangerName = (TextView)contentView.findViewById(R.id.danger_name);
        final ImageView otherDraw = (ImageView)contentView.findViewById(R.id.other_draw);
        final ImageView dangerDraw = (ImageView)contentView.findViewById(R.id.danger_draw);

        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        popupWindow.setTouchable(true);

        ll_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_equipment_type_show.setText(otherName.getText().toString());
                otherName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.equipment_type_choose));
                dangerName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.stationInfo));
                otherDraw.setVisibility(View.VISIBLE);
                dangerDraw.setVisibility(View.GONE);
            }
        });

        ll_danger_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_equipment_type_show.setText(dangerName.getText().toString());
                dangerName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.equipment_type_choose));
                otherName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.stationInfo));
                dangerDraw.setVisibility(View.VISIBLE);
                otherDraw.setVisibility(View.GONE);
            }
        });
             /* 设置触摸外面时消失 */
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable((new ColorDrawable(0x00000000)));
        popupWindow.update();
        popupWindow.showAsDropDown(chooseType,-130,15);
    }

    @Override
    public void onBackPressed() {
        if (isAffirm)
            finish();
        else
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

    //地图部分
    public void initBaiDuMap(){
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
    }

    public void getPermissions(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if(ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions((Activity) MyApplication.getContext(), permissions, 1);
        }else{
            requestLocation();
        }
    }

    private void navigateTo(BDLocation location, View view){
        if(isFirstLocate){
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16.0f).build();
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;

            //构建定位图标
            constructionLocationIcon(ll, view);

            // 对定位图标设定点击事件
            initMarkerClickEvent();
        }
    }

    private void initMarkerClickEvent() {
        // 对Marker的点击
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                tv_latitude.setText(mapLatitude.getText().toString().trim());
                tv_longitude.setText(mapLongitude.getText().toString().trim());
                return false;
            }
        });
    }

    /**
     * 构建定位图标
     * @param point 图标位置
     */
    private void constructionLocationIcon(LatLng point, View view){
        BitmapDescriptor iconMark = BitmapDescriptorFactory.fromView(view);
        OverlayOptions options = new MarkerOptions().position(point).icon(iconMark).draggable(false);
        baiduMap.getUiSettings().setScrollGesturesEnabled(false);
        baiduMap.addOverlay(options);
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result: grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MyApplication.getContext(), "必须同意所有权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(MyApplication.getContext(), "手机硬件不支持", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        public MyLocationListener(){

        }
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(bdLocation.getDistrict() + bdLocation.getStreet());
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                windowText.setText(currentPosition);
                mapLongitude.setText(""+bdLocation.getLongitude());
                mapLatitude.setText(""+bdLocation.getLatitude());
                navigateTo(bdLocation, markView);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //相机部分
    //查看图片
    private void viewPhoto(String imagePath) {
        Intent intent = new Intent(MyApplication.getContext(), ScanPhotoActivity.class);
        intent.putExtra(StaticParameters.IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    private void photoListener() {
        lyBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageId = R.id.img_before;
                checkPermission();
            }
        });
        imgBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhoto(maintainData.get(StaticParameters.IM_BEFORE).toString());
            }
        });
        imgBeforeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBefore.setVisibility(View.INVISIBLE);
                imgBeforeDel.setVisibility(View.GONE);
                lyBefore.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_BEFORE).toString());
                maintainData.put(StaticParameters.IM_BEFORE, null);
            }
        });
        lyIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageId = R.id.img_ing;
                checkPermission();
            }
        });
        imgIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhoto(maintainData.get(StaticParameters.IM_ING).toString());
            }
        });
        imgIngDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgIng.setVisibility(View.INVISIBLE);
                imgIngDel.setVisibility(View.GONE);
                lyIng.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_ING).toString());
                maintainData.put(StaticParameters.IM_ING, null);
            }
        });
        lyEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageId = R.id.img_end;
                checkPermission();
            }
        });
        imgEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPhoto(maintainData.get(StaticParameters.IM_END).toString());
            }
        });
        imgEndDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgEnd.setVisibility(View.INVISIBLE);
                imgEndDel.setVisibility(View.GONE);
                lyEnd.setVisibility(View.VISIBLE);
                ImageUtil.deleteImageFromSDCard(maintainData.get(StaticParameters.IM_END).toString());
                maintainData.put(StaticParameters.IM_END, null);
            }
        });
    }

    public void setImage() {
        switch (selectedImageId) {
            case R.id.img_before:
                lyBefore.setVisibility(View.GONE);
                maintainData.put(StaticParameters.IM_BEFORE, screenshotPath);
                imgBefore.setVisibility(View.VISIBLE);
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

    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions((Activity) MyApplication.getContext(), permissions, 1);
        } else {
            takenPicture();
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
            imageUri = FileProvider.getUriForFile(MyApplication.getContext(), "com.cqut.icode.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                Intent intent = new Intent(MyApplication.getContext(), ViewPhotoActivity.class);
                intent.putExtra(StaticParameters.IMAGE_PATH, imageUri.getPath());
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
