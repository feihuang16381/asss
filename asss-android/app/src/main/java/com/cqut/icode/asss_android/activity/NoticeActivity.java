package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.NoticeAdapter;
import com.cqut.icode.asss_android.common.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;


/**
 * Created by 10713 on 2017/7/13.
 * 活动描述：个人中心-->通知
 */
public class NoticeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    private NoticeAdapter adapter;
    private List<Map<String, Object>> noticelist =  new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list);
        swipeRefresh.setColorSchemeResources(R.color.sta_font);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        getNoticeList();
    /*    initialize();*/
        initData();
    }


public void  initData(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1600);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initialize();
                    getNoticeList();
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                }
            });
        }
    }).start();
}
    private void refreshData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1600);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                        getNoticeList();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(MyApplication.getContext(),"刷新完成",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void initialize() {
        mToolbar.setNavigationIcon(R.drawable.icon_return);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);//取代原本的actionbar

        //搜索初始设置
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);//搜索框的ListView中的Item条目是否是单显示
        searchView.setSubmitOnClick(true);//点击Item是否要回调搜索方法，设置为true后，单击ListView的条目，searchView隐藏
        searchView.setSuggestions(getResources().getStringArray(R.array.notice_search));

        searchView.setCursorDrawable(R.drawable.cutom_cursor);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeAdapter(noticelist);
        recyclerView.setAdapter(adapter);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索框中EditText数据的监听
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            //数据提交时
            //1.点击ListView的Item条目会回调这个方法
            //2.点击系统键盘的搜索/按回车建后回调这个方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Toast.makeText(MyApplication.getContext(), "你要搜索的是：" + query, Toast.LENGTH_SHORT).show();
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

              adapter.setOnItemClickListener(new NoticeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Map<String,Object> noticeList = noticelist.get(position);

                String string = "状态："+noticeList.get("state")+"\n时间："+noticeList.get("create_time")+"\n创建者:"+noticeList.get("founder")+"\n创建信息:"+noticeList.get("info");
                Toast.makeText(MyApplication.getContext(),string,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MyApplication.getContext(),NoticeDetailsActivity.class);
                intent.putExtra("state",noticeList.get("state").toString());
                intent.putExtra("title",noticeList.get("title").toString());
                intent.putExtra("founder",noticeList.get("founder").toString());
                intent.putExtra("content",noticeList.get("content").toString());
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new NoticeAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(final int position) {
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                noticelist.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.notice_head, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_more) {
            Intent intent = new Intent(MyApplication.getContext(),NoticeIssueActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getNoticeList(){
        SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);

        String sessionid= share.getString("sessionid","null");
//    192.168.43.92
        String url = "http://10.0.2.2:8080/asss-webbg/message/getnoticelist2.do";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("bookName", "hyman")
                .addParams("password", "123")
                .addHeader("cookie",sessionid)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(NoticeActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }

                    @Override
                    public void onResponse(String response, int id) {

                        noticelist.clear();
                        Gson gson = new Gson();
                        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>(){}.getType());
                        noticelist.addAll(list);
                    }


                });
    }

    @Override
    public void onClick(View v) {

    }
}
