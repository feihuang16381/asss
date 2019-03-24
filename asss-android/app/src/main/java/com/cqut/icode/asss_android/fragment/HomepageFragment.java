package com.cqut.icode.asss_android.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.activity.TerminalInfoActivity;
import com.cqut.icode.asss_android.adapter.TerminalAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class HomepageFragment extends Fragment implements SHSwipeRefreshLayout.SHSOnRefreshListener {

    @BindView(R.id.classify)
    LinearLayout classify;
    @BindView(R.id.screen_ly)
    LinearLayout screenLy;
    @BindView(R.id.list_item)
    RecyclerView listItem;
    @BindView(R.id.refresh_item)
    SHSwipeRefreshLayout refreshItem;
    @BindView(R.id.location_text)
    TextView location_text;

    private TextView selectedClassifyTV = null;
    private ImageView selectedClassifyIMG = null;
    private TextView tvClassifyAllSite = null;
    private ImageView imgClassifyAllSite = null;
    private TextView tvClassifDgSource = null;
    private ImageView imgClassifyDgSource = null;
    private TextView tvClassifOther = null;
    private ImageView imgClassifyOther = null;

    private TextView selectedScreenTV = null;
    private ImageView selectedScreenIMG = null;
    private TextView tvAllSiteScreen = null;
    private ImageView imgAllSiteScreen = null;
    private TextView tvNearbyScreen = null;
    private ImageView imgNearbyScreen = null;

    public final static int ALL_CLASSIFY = 0;
    public final static int ALL_SCREEN = 0;
    public final static int DANGER_SOURCE = 1;
    public final static int OTHER = 2;
    public final static int NEARLY = 1;
    private int currentClassify = ALL_CLASSIFY;
    private int currentScreen = ALL_SCREEN;

    private Unbinder unbinder;
    private View classifyPopupView = null;
    private View screenPopupView = null;
    private static int screen_width = 0;
    public LocationClient mLocationClient;
    private PopupWindow mPopupWindowClassify = null; //分区选择弹出窗
    private PopupWindow mPopupWindowScreen = null; //分区选择弹出窗
    private List<Map<String, Object>> terminalArr = new ArrayList<>();//站点列表数据
    private TerminalAdapter terminalAdapter;


    public HomepageFragment() {
    }

    public void initComponent() {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels / 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        listItem.setLayoutManager(gridLayoutManager);
        terminalAdapter = new TerminalAdapter(terminalArr);
        listItem.setAdapter(terminalAdapter);
        //初始化位置监听器
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        initPopupWindow();
    }

    private void initListener() {
        refreshItem.setOnRefreshListener(this);
        terminalAdapter.setOnItemClickListener(new TerminalAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), TerminalInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("terminalId", terminalArr.get(position).get("id").toString());
                bundle.putString("type", terminalArr.get(position).get("type").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this, view);
        getTerminalData("124", "重庆理工");
        initComponent();
        requestPermissions();
        initListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishRefresh();
                Toast.makeText(getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
            }
        }, 1600);
    }


    public void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
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
                            Toast.makeText(getContext(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                showLocation("纬度：" + location.getLatitude() + " 经线：" + location.getLongitude());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                showLocation(location.getCity() + location.getDistrict() + location.getStreet());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Optional
    @OnClick({R.id.classify, R.id.screen_ly})
    public void perScreen(LinearLayout view) {
        if (view.getId() == R.id.classify) {
            showPopupWindowClassify();//点击分类
        }
        if (view.getId() == R.id.screen_ly) {
            showPopupWindowScreen();//点击筛选站点
        }
    }

    public void initPopupWindow() {
        classifyPopupView = setPopupWindowView(R.layout.terminal_classify_list);
        screenPopupView = setPopupWindowView(R.layout.terminal_screen_list);
        mPopupWindowClassify = setPopupWindowProperty(classifyPopupView);
        mPopupWindowScreen = setPopupWindowProperty(screenPopupView);
        setScreenItemListener(screenPopupView);//给选项添加点击事件
        setClassifyItemListener(classifyPopupView);//给选项添加点击事件
    }

    public View setPopupWindowView(int layout) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null);
        contentView.setFocusable(true); // 这个很重要
        contentView.setFocusableInTouchMode(true);
        return contentView;
    }

    public PopupWindow setPopupWindowProperty(View contentView) {
         /* 第一个参数弹出显示view 后两个是窗口大小 */
        PopupWindow myPopup = new PopupWindow(contentView, screen_width, WindowManager.LayoutParams.WRAP_CONTENT);
        myPopup.setBackgroundDrawable((new ColorDrawable(0x00000000)));
         /* 设置触摸外面时消失 */
        myPopup.setFocusable(true);
        myPopup.setOutsideTouchable(true);
        myPopup.setAnimationStyle(R.style.anim_menu_bar);
        myPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });
        return myPopup;
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    //获取站点分类的数据
    private void setClassifyItemListener(View contentView) {
        LinearLayout allSite = (LinearLayout) contentView.findViewById(R.id.allSite);
        LinearLayout dangerSource = (LinearLayout) contentView.findViewById(R.id.dangerSource);
        LinearLayout other = (LinearLayout) contentView.findViewById(R.id.other);

        tvClassifyAllSite = (TextView) contentView.findViewById(R.id.tv_allSite);
        imgClassifyAllSite =  (ImageView) contentView.findViewById(R.id.img_allSite);
        tvClassifDgSource = (TextView) contentView.findViewById(R.id.tv_dg_source);
        imgClassifyDgSource = (ImageView) contentView.findViewById(R.id.img_dg_source);
        tvClassifOther = (TextView) contentView.findViewById(R.id.tv_other);
        imgClassifyOther = (ImageView) contentView.findViewById(R.id.img_other);

        selectedClassifyTV = tvClassifyAllSite;
        selectedClassifyIMG = imgClassifyAllSite;
        changeClassifySelectItem(tvClassifyAllSite,imgClassifyAllSite);

        allSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = ALL_CLASSIFY;
                changeClassifySelectItem(tvClassifyAllSite,imgClassifyAllSite);
                mPopupWindowClassify.dismiss();
                Toast.makeText(getContext(), "you click the allSite", Toast.LENGTH_SHORT).show();

            }
        });
        dangerSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = DANGER_SOURCE;
                changeClassifySelectItem(tvClassifDgSource,imgClassifyDgSource);
                mPopupWindowClassify.dismiss();
                Toast.makeText(getContext(), "you click the dangerSource", Toast.LENGTH_SHORT).show();

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = OTHER;
                changeClassifySelectItem(tvClassifOther,imgClassifyOther);
                mPopupWindowClassify.dismiss();
                Toast.makeText(getContext(), "you click the other", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void changeClassifySelectItem(TextView tv, ImageView img) {
        selectedClassifyTV.setTextColor(getResources().getColor(R.color.theme_secondary_text_inverted));
        selectedClassifyIMG.setVisibility(View.INVISIBLE);
        tv.setTextColor(getResources().getColor(R.color.yellow));
        img.setVisibility(View.VISIBLE);
        selectedClassifyTV = tv;
        selectedClassifyIMG = img;
    }


    public void showPopupWindowScreen() {
        backgroundAlpha(0.7f);
        mPopupWindowScreen.showAsDropDown(screenLy, 50, -30);
    }

    public void showPopupWindowClassify() {
        backgroundAlpha(0.7f);
        mPopupWindowClassify.showAsDropDown(classify, 60, -30);
    }

    //获取站点分类的数据
    private void setScreenItemListener(View contentView) {
        LinearLayout allSite = (LinearLayout) contentView.findViewById(R.id.allSite);
        LinearLayout nearby = (LinearLayout) contentView.findViewById(R.id.nearby);

        tvAllSiteScreen = (TextView) contentView.findViewById(R.id.tv_allSite);
        imgAllSiteScreen = (ImageView) contentView.findViewById(R.id.img_allSite);
        tvNearbyScreen = (TextView) contentView.findViewById(R.id.tv_nearby_site);
        imgNearbyScreen = (ImageView) contentView.findViewById(R.id.img_nearby_site);

        selectedScreenTV = tvAllSiteScreen;
        selectedScreenIMG = imgAllSiteScreen;
        changeScreenSelectItem(tvAllSiteScreen,imgAllSiteScreen);

        allSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = ALL_SCREEN;
                changeScreenSelectItem(tvAllSiteScreen,imgAllSiteScreen);
                mPopupWindowScreen.dismiss();
                Toast.makeText(getContext(), "you click the allSite", Toast.LENGTH_SHORT).show();

            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = NEARLY;
                changeScreenSelectItem(tvNearbyScreen,imgNearbyScreen);
                mPopupWindowScreen.dismiss();
                Toast.makeText(getContext(), "you click the dangerSource", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void changeScreenSelectItem(TextView tv, ImageView img) {
        selectedScreenTV.setTextColor(getResources().getColor(R.color.theme_secondary_text_inverted));
        selectedScreenIMG.setVisibility(View.INVISIBLE);
        tv.setTextColor(getResources().getColor(R.color.yellow));
        img.setVisibility(View.VISIBLE);
        selectedScreenTV = tv;
        selectedScreenIMG = img;
    }

    private void showLocation(final String location) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                location_text.setText(location);
            }
        });
    }

    @Override
    public void onLoading() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishLoadmore();
                Toast.makeText(getContext(), "加载完成", Toast.LENGTH_SHORT).show();
            }
        }, 1600);
    }

    @Override
    public void onRefreshPulStateChange(float percent, int state) {
        switch (state) {
            case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("下拉刷新");
                break;
            case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("松开刷新");
                //
                break;
            case SHSwipeRefreshLayout.START:
                refreshItem.setLoaderViewText("正在刷新");
                break;
        }
    }

    @Override
    public void onLoadmorePullStateChange(float percent, int state) {
        switch (state) {
            case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("上拉加载");
                break;
            case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("松开加载");
                break;
            case SHSwipeRefreshLayout.START:
                refreshItem.setLoaderViewText("正在加载...");
                break;
        }
    }

    private void getTerminalData(String subareaId, String terminalName) {
        //从服务器获取数据代替此处代码
        String response = "[{ \"station_code\": \"1234\", \"terminal_name\": \"重庆理工\",\"id\":\"111\",\"type\":\"1\"}," +
                "    {\"station_code\": \"1235\", \"terminal_name\": \"重庆理工1\",\"id\":\"112\",\"type\":\"2\"}," +
                "    {\"station_code\": \"1236\", \"terminal_name\": \"重庆理工2\" ,\"id\":\"113\",\"type\":\"1\"}," +
                "    {\"station_code\": \"1237\", \"terminal_name\": \"重庆理工3\",\"id\":\"114\",\"type\":\"2\" }," +
                "    {\"station_code\":\"1238\",\"terminal_name\":\"重庆理工4\",\"id\":\"115\",\"type\":\"1\"}," +
                "    {\"station_code\":\"1239\",\"terminal_name\":\"重庆理工5\",\"id\":\"116\",\"type\":\"1\"}," +
                "    {\"station_code\":\"1212\",\"terminal_name\":\"重庆理工6\",\"id\":\"117\",\"type\":\"2\"}," +
                "    {\"station_code\":\"1214\",\"terminal_name\":\"重庆理工7\",\"id\":\"118\",\"type\":\"2\"}," +
                "    {\"station_code\":\"1235\",\"terminal_name\":\"重庆理工8\",\"id\":\"119\",\"type\":\"1\"}" +
                "]";
        terminalArr.clear();
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        terminalArr.addAll(list);
//        terminalAdapter.notifyDataSetChanged();
    }
}
