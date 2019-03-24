package com.cqut.icode.asss_android.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.UploadListAdapter;
import com.cqut.icode.asss_android.view.MyDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadStateActivity extends BaseActivity {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.list_item)
    RecyclerView listItem;

    private UploadListAdapter uploadListAdapter;
    private  ArrayList<Map<String, Object>> uploadDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_state);
        ButterKnife.bind(this);
        getData();
        initComponent();
        initListener();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_more);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        item.setVisible(false);
        return true;
    }

    public void initComponent() {
        activityTitle.setText("维修列表");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //adapter初始化
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(LayoutManager);
        uploadListAdapter = new UploadListAdapter(uploadDataList);
        listItem.setAdapter(uploadListAdapter);
        listItem.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
    }

    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getData(){
        String response ="[{\n" +
                "        \"terminal_name\": \"梅轩\",\n" +
                "        \"id\": \"123\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"竹轩\",\n" +
                "        \"id\": \"234\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 2\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"菊轩\",\n" +
                "        \"id\": \"564\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 3\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"松轩\",\n" +
                "        \"id\": \"741\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 4\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"桂轩\",\n" +
                "        \"id\": \"852\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"一实验楼\",\n" +
                "        \"id\": \"987\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 2\n" +
                "    },\n" +
                "    {\n" +
                "        \"terminal_name\": \"二实验楼\",\n" +
                "        \"id\": \"438\",\n" +
                "        \"pictures\": [\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" },\n" +
                "            { \"pictureId\": \"\", \"picturePath\": \"\" }\n" +
                "        ],\n" +
                "        \"upload_state\": 3\n" +
                "    }\n" +
                "]";
        Gson gson = new Gson();
        List<Map<String, Object>> listMap = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        uploadDataList.addAll(listMap);
    }

}
