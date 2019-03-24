package com.cqut.icode.asss_android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by 10713 on 2017/7/13.
 * 活动描述：站点详情
 */
public class TerminalInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.terminal_type_ic)
    ImageView im_terminal_type_ic;
    @BindView(R.id.terminal_name)
    TextView tv_terminal_name;
    @BindView(R.id.terminal_code)
    TextView tv_terminal_code;
    @BindView(R.id.terminal_type)
    TextView tv_terminal_type;
    @BindView(R.id.terminal_ll)
    TextView tv_terminal_ll;
    @BindView(R.id.info_change)
    LinearLayout ll_info_change;
    @BindView(R.id.danger_report)
    LinearLayout ll_danger_report;
    @BindView(R.id.other)
    LinearLayout ll_other;
    @BindView(R.id.bmapView)
    MapView mapView;

    private BaiduMap baiduMap;
    private int type;
    private boolean isFirstLocate = true;
    public LocationClient mLocationClient;
    private Map<String, Object> terminal_info_data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.terminal_info);
        Bundle bundle = getIntent().getExtras();
        type = Integer.parseInt(bundle.get("type").toString());
        getData();
        initialize();
        fillTitleData();
        requestPermissions();
    }

    //  检查当前网络是否可用
    public boolean isNetWorkAvailable(Activity activity){
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
//判断GPS是否打开
    public static boolean isGpsEnabled(Activity activity) {
        Context context = activity.getApplicationContext();
        LocationManager location = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = location.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    public void requestPermissions(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(TerminalInfoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(TerminalInfoActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(TerminalInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(TerminalInfoActivity.this, permissions, 1);
        } else {
            requestLocation();
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
                    requestLocation();
                } else {
                    Toast.makeText(this, "手机硬件不支持", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void navigateTo(BDLocation location) {
        if(isFirstLocate){
            LatLng ll = new LatLng(Double.parseDouble(terminal_info_data.get("latitude").toString().trim()),Double.parseDouble(terminal_info_data.get("longitude").toString().trim()));

            MapStatus mapStatus = new MapStatus.Builder().target(ll).zoom(16.0f).build();
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;

            BitmapDescriptor mCurrentMark = BitmapDescriptorFactory.fromResource(R.drawable.icon_dinwei);
            OverlayOptions options = new MarkerOptions().position(ll).icon(mCurrentMark).draggable(false);
            baiduMap.getUiSettings().setScrollGesturesEnabled(false);
            baiduMap.addOverlay(options);
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location.getLocType() == BDLocation.TypeGpsLocation||location.getLocType()==BDLocation.TypeNetWorkLocation)
                navigateTo(location);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void fillTitleData() {
        tv_terminal_code.setText("站点编号:"+terminal_info_data.get("terminal_id").toString().trim());

        tv_terminal_name.setText(terminal_info_data.get("terminal_name").toString().trim());

        if(terminal_info_data.get("type_id").equals("1")) {
            tv_terminal_type.setText("站点类型:危险源设备");
            im_terminal_type_ic.setImageResource(R.drawable.zhandian_ico_red);
            ll_danger_report.setVisibility(View.VISIBLE);
            ll_other.setVisibility(View.GONE);
        }
        else if(terminal_info_data.get("type_id").equals("2")) {
            tv_terminal_type.setText("站点类型:其他");
            im_terminal_type_ic.setImageResource(R.drawable.zhandian_ico_lemon);
            ll_other.setVisibility(View.VISIBLE);
            ll_danger_report.setVisibility(View.GONE);
        }

        tv_terminal_ll.setText("站点经纬度:"+terminal_info_data.get("longitude").toString() + " " + terminal_info_data.get("latitude").toString());
    }

    private void initialize() {
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.icon_return);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);

        //初始化位置监听器
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
     //   baiduMap.setMyLocationEnabled(true);

        if(isNetWorkAvailable(TerminalInfoActivity.this) && isGpsEnabled(TerminalInfoActivity.this)){
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("             无网络或GPS未打开\n" +
                                "      请确认网络状况及GPS是否打开")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_info_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });
        ll_danger_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyApplication.getContext(),DangerReportActivity.class);
                startActivity(intent);


            }
        });
        ll_other.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(),CommonMaintainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

}

    private void getData() {
        String response = "{ \"terminal_id\": \"2691\", \"terminal_name\": \"重庆市合川区\",\"type_id\":\"1\",\"longitude\":\"106.28\",\"latitude\":\"29.97\"}";
        String response1 = "{ \"terminal_id\": \"2601\", \"terminal_name\": \"重庆理工大学\",\"type_id\":\"2\",\"longitude\":\"106.53\",\"latitude\":\"29.46\"}";
        if(type == 2)
        {
            response = response1;
        }
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>(){}.getType());
        terminal_info_data.clear();
        terminal_info_data.putAll(map);
    }

}
