package com.cqut.icode.asss_android.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.activity.ApplyForPartsListActivity;
import com.cqut.icode.asss_android.activity.MainActivity;
import com.cqut.icode.asss_android.activity.NoticeActivity;
import com.cqut.icode.asss_android.activity.RepairRecordActivity;
import com.cqut.icode.asss_android.activity.TaskListActivity;
import com.cqut.icode.asss_android.common.MyApplication;
import com.cqut.icode.asss_android.util.BadgeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Uo on 2017/7/4.
 */

public class PersonalCenterFragment extends Fragment {

    @BindView(R.id.mapView)
    TextureMapView mapView;
    @BindView(R.id.location_name)
    TextView positionText;
    @BindView(R.id.red_digit1)
    TextView red_digit;
    @BindView(R.id.red_digit2)
    TextView red_digit2;

    private Unbinder unbinder;
    public LocationClient locationClient;
    public View markView;
    public  TextView windowText;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    public int count =0;
    public int countUnreadingsForNotice=0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        SDKInitializer.initialize(MyApplication.getContext());
        View view = inflater.inflate(R.layout.fragment_person_center, container, false);
        unbinder = ButterKnife.bind(this, view);
        initBaiDuMap();
        getPermissions();



        red_digit();


        //解决location_name处图标过大问题
        Drawable drawable1 = getResources().getDrawable(R.drawable.icon_mappoint);
        drawable1.setBounds(0, 0, 45, 57);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        positionText.setCompoundDrawables(drawable1, null, null, null);//只放左边
        return view;

    }
   public void red_digit(){
       MainActivity activity = (MainActivity)getActivity();

       List<Map<String,Object>> taskList=activity.taskList;
       List<Map<String,Object>> noticeList=activity.noticelist;
       count = countNonReadings(taskList);

       countUnreadingsForNotice = countNonReadingsForNotice(noticeList);

       TextView TextView =red_digit;
       QBadgeView qBadgeView1 =  new QBadgeView(TextView.getContext());
       qBadgeView1.bindTarget(TextView);



       qBadgeView1.setBadgeNumber(count);
       /*   qBadgeView.setGravityOffset(30,-2,true);*/
       qBadgeView1.setBadgeGravity((Gravity.END | Gravity.TOP)) ;
       qBadgeView1.setGravityOffset(2,3,true);

       qBadgeView1.setBadgePadding(5  ,true);
       /*qBadgeView1.setBadgeText("1231");*/
       qBadgeView1.setBadgeTextSize(9,true);
       qBadgeView1.isDraggable();

       TextView TextView2 =red_digit2;
       QBadgeView qBadgeView2 =  new QBadgeView(TextView2.getContext());
       qBadgeView2.bindTarget(TextView2);
       qBadgeView2.setBadgeNumber(countUnreadingsForNotice);
       /*   qBadgeView.setGravityOffset(30,-2,true);*/
       qBadgeView2.setBadgeGravity((Gravity.END | Gravity.TOP)) ;
       qBadgeView2.setGravityOffset(2,3,true);

       qBadgeView2.setBadgePadding(5  ,true);
       /*qBadgeView1.setBadgeText("1231");*/
       qBadgeView2.setBadgeTextSize(9,true);
       qBadgeView2.isDraggable();

    }

   public  int countNonReadings(List<Map<String,Object>> taskList ){
        int count =0;
for(int i = 0;i<taskList.size();i++){
    if(taskList.get(i).get("is_receive").toString().equals("0.0"))
        count++;


}
        return count;

    }

    public  int countNonReadingsForNotice(List<Map<String,Object>> List ){
        int count =0;
        for(int i = 0;i<List.size();i++){
            if(List.get(i).get("state").toString().equals("0.0"))
                count++;


        }
        return count;

    }


    public void initBaiDuMap(){
        locationClient = new LocationClient(MyApplication.getContext());
        markView = LayoutInflater.from(getActivity()).inflate(R.layout.terminal_info_window, null);
        windowText = (TextView)markView.findViewById(R.id.terminal_infoTv);
        locationClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
    }

    public void getPermissions(){
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }else{
            requestLocation();
        }
    }

    //按钮点击事件
    @OnClick({R.id.sing_in, R.id.task_list, R.id.notification, R.id.request, R.id.maintenance})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sing_in:
                Toast.makeText(getActivity(), "点击了签到", Toast.LENGTH_SHORT).show();
                break;
            case R.id.task_list:
                Intent taskIntent = new Intent(getActivity(), TaskListActivity.class);
                startActivity(taskIntent);
                break;
            case R.id.notification:
                Intent noticeIntent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(noticeIntent);//启动通知活动
                break;

            case R.id.request:
                Intent applyForPartsIntent = new Intent(getActivity(), ApplyForPartsListActivity.class);
                startActivity(applyForPartsIntent);
                break;
            case R.id.maintenance:
                Intent repairRecordIntent = new Intent(getActivity(), RepairRecordActivity.class);
                startActivity(repairRecordIntent);
                break;
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                            Toast.makeText(getContext(), "必须同意所有权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(getContext(), "手机硬件不支持", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        public MyLocationListener(){

        }
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            final StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(bdLocation.getCity() + bdLocation.getDistrict() + bdLocation.getStreet());
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                windowText.setText(currentPosition);
                navigateTo(bdLocation, markView);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    positionText.setText(currentPosition);
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
