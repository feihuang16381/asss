package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.ApplyForPartsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动描述：个人中心-->请备-->请备列表
 */
public class ApplyForPartsListActivity extends BaseActivity implements SHSwipeRefreshLayout.SHSOnRefreshListener{

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.list_item)
    RecyclerView listItem;
    @BindView(R.id.refresh_item)
    SHSwipeRefreshLayout refreshItem;

    private ApplyForPartsAdapter applyForPartsAdapter;
    private  ArrayList<Map<String, Object>> applyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_parts_list);
        ButterKnife.bind(this);
        getApplyData();
        initComponent();
        initListener();

    }

    public void initComponent() {
        activityTitle.setText("请备列表");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
     //adapter初始化
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(LayoutManager);
         applyForPartsAdapter= new ApplyForPartsAdapter(applyData);
        listItem.setAdapter(applyForPartsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_more:
                Intent intent = new Intent(ApplyForPartsListActivity.this, ApplySparePartsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initListener() {
        refreshItem.setOnRefreshListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        applyForPartsAdapter.setOnItemClickListener(new ApplyForPartsAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ApplyForPartsListActivity.this,ApplySparePartsActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onRefresh() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishRefresh();
                Toast.makeText(ApplyForPartsListActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }


    @Override
    public void onLoading() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishLoadmore();
                Toast.makeText(ApplyForPartsListActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    @Override
    public void onRefreshPulStateChange(float percent, int state) {
        switch (state) {
            case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("下拉刷新");
                break;
            case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                refreshItem.setLoaderViewText("松开刷新");
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
    public void getApplyData(){
        String response = "[\n" +
                "{\n" +
                "    \"id\": \"123\",\n" +
                "    \"creator_id\": \"456\",\n" +
                "    \"creator_name\": \"李莫愁\",\n" +
                "    \"apply_time\": \"2017-12-14\",\n" +
                "    \"apply_name\": \"A计划\",\n" +
                "    \"task_id\": \"133\",\n" +
                "    \"task_name\": \"A任务\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": \"125\",\n" +
                "    \"creator_id\": \"789\",\n" +
                "    \"creator_name\": \"杨过\",\n" +
                "    \"apply_time\": \"2017-12-12\",\n" +
                "    \"apply_name\": \"B计划\",\n" +
                "    \"task_id\": \"134\",\n" +
                "    \"task_name\": \"B任务\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": \"124\",\n" +
                "    \"creator_id\": \"321\",\n" +
                "    \"creator_name\": \"小龙女\",\n" +
                "    \"apply_time\": \"2017-12-1\",\n" +
                "    \"apply_name\": \"C计划\",\n" +
                "    \"task_id\": \"135\",\n" +
                "    \"task_name\": \"C任务\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": \"159\",\n" +
                "    \"creator_id\": \"654\",\n" +
                "    \"creator_name\": \"黄蓉\",\n" +
                "    \"apply_time\": \"2017-12-2\",\n" +
                "    \"apply_name\": \"D计划\",\n" +
                "    \"task_id\": \"482\",\n" +
                "    \"task_name\": \"D任务\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": \"184\",\n" +
                "    \"creator_id\": \"789\",\n" +
                "    \"creator_name\": \"郭啸天\",\n" +
                "    \"apply_time\": \"2017-12-4\",\n" +
                "    \"apply_name\": \"E计划\",\n" +
                "    \"task_id\": \"682\",\n" +
                "    \"task_name\": \"W任务\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": \"987\",\n" +
                "    \"creator_id\": \"741\",\n" +
                "    \"creator_name\": \"洪七公\",\n" +
                "    \"apply_time\": \"2017-12-5\",\n" +
                "    \"apply_name\": \"F计划\",\n" +
                "    \"task_id\": \"962\",\n" +
                "    \"task_name\": \"F任务\"\n" +
                "}]";
        Gson gson = new Gson();
        List<Map<String, Object>> listMap = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        applyData.addAll(listMap);
    }

}
