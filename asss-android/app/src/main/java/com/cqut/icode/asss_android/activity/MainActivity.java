package com.cqut.icode.asss_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.ContentPagerAdapter;
import com.cqut.icode.asss_android.common.MyApplication;
import com.cqut.icode.asss_android.fragment.HomepageFragment;
import com.cqut.icode.asss_android.fragment.PersonalCenterFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vp_content)
    ViewPager viewPager;
    @BindView(R.id.tl_tab)
    TabLayout tabLayout;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.activity_title)
    TextView activityTitle;


    private ContentPagerAdapter contentPagerAdapter;
    private HomepageFragment homepageFragment;
    private Context context;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
   public  List<Map<String,Object>> taskList = new ArrayList<>();
    public  List<Map<String,Object>> noticelist  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     /*   getNoticeList();*/
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getTasklist();
        getNoticelist();
       /* getDatasync();*/
        setStatusBarLightMode(this, Color.TRANSPARENT);//设置status背景透明
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initComponent();
        initListener();
        this.context = this;
    }



    public void initComponent() {

        //toolBar初始设置
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //侧栏栏初始设置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
//        navView.setNavigationItemSelectedListener(this);
        //搜索初始设置
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setCursorDrawable(R.drawable.cutom_cursor);

        //初始化Tab
        initTabContent();
        //初始化tab
        initTab();
    }
//initData 获取数据

    private void initTabContent() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("首页");
        tabIndicators.add("个人中心");
        tabFragments = new ArrayList<>();

        tabFragments.add(new HomepageFragment());
        tabFragments.add(new PersonalCenterFragment());
        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), context,
                tabFragments, tabIndicators);
        viewPager.setAdapter(contentPagerAdapter);
    }

    public void getTasklist(){
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);

                    String sessionid= share.getString("sessionid","null");
                    Request request = new Request.Builder()
                            /*192.168.43.92*/
                            .url("http://10.0.2.2:8080/asss-webbg/message/DealData.do")
                            .addHeader("cookie",sessionid)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {

                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                        String responseData = response.body().string();
                      /*  Toast.makeText(MainActivity.this,responseData.toString(),Toast.LENGTH_SHORT).show();*/
                        Gson gson = new Gson();
                        List<Map<String, Object>> list = gson.fromJson(responseData, new TypeToken<List<Map<String, Object>>>(){}.getType());
                        taskList.addAll(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    public void getNoticelist(){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象

        SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);

        String sessionid= share.getString("sessionid","null");
        try {

            Request request = new Request.Builder()

                    /*192.168.43.92*/
                    .url("http://10.0.2.2:8080/asss-webbg/message/getnoticelist2.do")//请求接口。如果需要传参拼接到接口后面。
                    .addHeader("cookie",sessionid)//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            request.header("Set-Cookie");
            Response response = null;
            response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
                String session = response.headers().get("Set-Cookie");
                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                String responseData = response.body().string();
            /*    Toast.makeText(MainActivity.this,responseData.toString(),Toast.LENGTH_SHORT).show();*/
                Gson gson = new Gson();
                List<Map<String, Object>> list = gson.fromJson(responseData, new TypeToken<List<Map<String, Object>>>(){}.getType());
                noticelist.addAll(list);
            }
        }  catch (SocketTimeoutException e) {

            client.dispatcher().cancelAll();
            client.connectionPool().evictAll();

            //TODO: 重新请求
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }




    private void initTab() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorHeight(0);
        ViewCompat.setElevation(tabLayout, 10);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabLayout.Tab itemTab = tabLayout.getTabAt(i);
            if (itemTab != null) {
                itemTab.setCustomView(R.layout.item_tab_layout_custom);
                TextView itemTv = (TextView) itemTab.getCustomView().findViewById(R.id.tv_menu_item);
                itemTv.setText(tabIndicators.get(i));
            }
        }
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }


    //初始化监听器
    private void initListener() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_more:
                Intent intent = new Intent(MyApplication.getContext(), TerminalInfoAddActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.cg_pwd, R.id.upload_status, R.id.set, R.id.exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cg_pwd:
                Intent changePwdIntent = new Intent(MyApplication.getContext(), ChangePasswordActivity.class);
                startActivity(changePwdIntent);
                break;
            case R.id.upload_status:
                Intent uploadStatusIntent = new Intent(MyApplication.getContext(), UploadStateActivity.class);
                startActivity(uploadStatusIntent);
                break;
            case R.id.set:
                Intent setWifiIntent = new Intent(MyApplication.getContext(), SetWifiLoadActivity.class);
                startActivity(setWifiIntent);
                break;
            case R.id.exit:
                Intent existIntent = new Intent(MyApplication.getContext(), LoginActivity.class);
                startActivity(existIntent);
                break;
        }
    }
}
