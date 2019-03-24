package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.adapter.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 活动描述：任务-->任务领取
 */
public class TaskListActivity extends BaseActivity implements SHSwipeRefreshLayout.SHSOnRefreshListener {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.classify_textView)
    TextView classifyTextView;
    @BindView(R.id.classify)
    LinearLayout classify;
    @BindView(R.id.screen_textView)
    TextView screenTextView;
    @BindView(R.id.screen_ly)
    LinearLayout screenLy;
    @BindView(R.id.list_item)
    RecyclerView listItem;
    @BindView(R.id.refresh_item)
    SHSwipeRefreshLayout refreshItem;

    private TextView selectedClassifyTV = null;
    private ImageView selectedClassifyIMG = null;
    private TextView selectedScreenTV = null;
    private ImageView selectedScreenIMG = null;
    private TextView clsfyAllSiteTV = null;
    private ImageView clsfyAllSiteIMG = null;
    private TextView clsfyDgInspectTV = null;
    private ImageView clsfyDgInspectIMG = null;
    private TextView clsfyDgMaintainTV = null;
    private ImageView clsfyDgMaintainIMG = null;
    private TextView clsfyDgStandTV = null;
    private ImageView clsfyDgStandIMG = null;
    private TextView clsfyCmMaintainTV = null;
    private ImageView clsfyCmMaintainIMG = null;

    private TextView screenAllStateTV = null;
    private ImageView screenAllStateIMG = null;
    private TextView screenICTV = null;
    private ImageView screenICIMG = null;
    private TextView screenUnGetTV = null;
    private ImageView screenUnGetIMG = null;
    private TextView screenGetTV = null;
    private ImageView screenGetIMG = null;

    public final static int ALL_CLASSIFY = 0;
    public final static int DANGER_INSPECT = 1;
    public final static int DANGER_MAINTAIN = 2;
    public final static int DANGER_STAND = 3;
    public final static int COMMON_MAINTAIN = 4;
    public final static int ALL_SCREEN = 0;
    public final static int I_CREATE = 1;
    public final static int GET = 2;
    public final static int UN_GET = 3;

    private int currentClassify = ALL_CLASSIFY;
    private int currentScreen = ALL_SCREEN;
    private View classifyPopupView = null;
    private View screenPopupView = null;


    private List<Map<String, Object>> taskList = new ArrayList<>();//站点列表数据
    private TaskAdapter taskAdapter;
    private static int screen_width = 0;
    private PopupWindow mPopupWindowClassify = null; //分区选择弹出窗
    private PopupWindow mPopupWindowScreen = null; //分区选择弹出窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);
     /*   getTaskData(null, null, 0);*/
        getNoticeList();
        initComponent();
        initListener();
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
               /*         initialize();*/
                        getNoticeList();
                        taskAdapter.notifyDataSetChanged();
                        refreshItem.setRefreshEnable(false);

                    }
                });
            }
        }).start();
    }
    public void initComponent() {
        //toolBar初始设置
        activityTitle.setText("任务领取");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        //初始化recycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(TaskListActivity.this);
        listItem.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(taskList);
        listItem.setAdapter(taskAdapter);

        //搜索初始设置
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setCursorDrawable(R.drawable.cutom_cursor);

        //初始化popup  //获取系统高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels / 3;
        initPopupWindow();
    }

    public void initPopupWindow() {
        classifyPopupView = setPopupWindowView(R.layout.task_classify_list);
        screenPopupView = setPopupWindowView(R.layout.task_screen_list);
        mPopupWindowClassify = setPopupWindowProperty(classifyPopupView);
        mPopupWindowScreen = setPopupWindowProperty(screenPopupView);
        setScreenItemListener(screenPopupView);//给选项添加点击事件
        setClassifyItemListener(classifyPopupView);//给选项添加点击事件
//        setScreenItemColor(screenPopupView);
////        setClassifyItemColor(classifyPopupView);
    }


    public View setPopupWindowView(int layout) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    public void showPopupWindowClassify() {
        backgroundAlpha(0.7f);
        mPopupWindowClassify.showAsDropDown(classify, 60, -30);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_more:
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //获取站点分类的数据
    private void setClassifyItemListener(View contentView) {
        LinearLayout allSite = (LinearLayout) contentView.findViewById(R.id.allSite);
        LinearLayout dgInspect = (LinearLayout) contentView.findViewById(R.id.dg_inspect);
        LinearLayout dgMaintain = (LinearLayout) contentView.findViewById(R.id.dg_maintain);
        LinearLayout dgStand = (LinearLayout) contentView.findViewById(R.id.dg_stand);
        LinearLayout cmMaintain = (LinearLayout) contentView.findViewById(R.id.cm_maintain);
        clsfyAllSiteTV = (TextView) contentView.findViewById(R.id.tv_task_clsfy_allSite);
        clsfyAllSiteIMG = (ImageView) contentView.findViewById(R.id.img_task_clsfy_allSite);
        clsfyDgInspectTV = (TextView) contentView.findViewById(R.id.tv_task_clsfy_dg_inspect);
        clsfyDgInspectIMG = (ImageView) contentView.findViewById(R.id.img_task_clsfy_dg_inspect);
        clsfyDgMaintainTV = (TextView) contentView.findViewById(R.id.tv_task_clsfy_dg_maintain);
        clsfyDgMaintainIMG = (ImageView) contentView.findViewById(R.id.img_task_clsfy_dg_maintain);
        clsfyDgStandTV = (TextView) contentView.findViewById(R.id.tv_task_clsfy_dg_stand);
        clsfyDgStandIMG = (ImageView) contentView.findViewById(R.id.img_task_clsfy_dg_stand);
        clsfyCmMaintainTV = (TextView) contentView.findViewById(R.id.tv_task_clsfy_cm_maintain);
        clsfyCmMaintainIMG = (ImageView) contentView.findViewById(R.id.img_task_clsfy_cm_maintain);
        selectedClassifyIMG = clsfyAllSiteIMG;
        selectedClassifyTV = clsfyAllSiteTV;
        changeClassifySelectItem(clsfyAllSiteTV, clsfyAllSiteIMG);
        allSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = ALL_CLASSIFY;
                changeClassifySelectItem(clsfyAllSiteTV, clsfyAllSiteIMG);
                mPopupWindowClassify.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the allSite", Toast.LENGTH_SHORT).show();

            }
        });
        dgInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = DANGER_INSPECT;
                changeClassifySelectItem(clsfyDgInspectTV, clsfyDgInspectIMG);
                mPopupWindowClassify.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the danger inspect", Toast.LENGTH_SHORT).show();

            }
        });
        dgMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = DANGER_MAINTAIN;
                changeClassifySelectItem(clsfyDgMaintainTV, clsfyDgMaintainIMG);
                mPopupWindowClassify.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the danger maintain", Toast.LENGTH_SHORT).show();
            }
        });
        dgStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = DANGER_STAND;
                changeClassifySelectItem(clsfyDgStandTV, clsfyDgStandIMG);
                mPopupWindowClassify.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the danger stand", Toast.LENGTH_SHORT).show();
            }
        });
        cmMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = COMMON_MAINTAIN;
                changeClassifySelectItem(clsfyCmMaintainTV, clsfyCmMaintainIMG);
                mPopupWindowClassify.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the common maintain", Toast.LENGTH_SHORT).show();

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


    //获取站点分类的数据
    private void setScreenItemListener(View contentView) {
        LinearLayout allSite = (LinearLayout) contentView.findViewById(R.id.allSite);
        LinearLayout lyICreate = (LinearLayout) contentView.findViewById(R.id.ly_i_create);
        LinearLayout lyNotGet = (LinearLayout) contentView.findViewById(R.id.ly_not_get);
        LinearLayout lyGet = (LinearLayout) contentView.findViewById(R.id.ly_get);
        screenAllStateTV = (TextView) contentView.findViewById(R.id.tv_task_screen_allState);
        screenAllStateIMG = (ImageView) contentView.findViewById(R.id.img_task_screen_allState);
        screenICTV = (TextView) contentView.findViewById(R.id.tv_task_screen_IC);
        screenICIMG = (ImageView) contentView.findViewById(R.id.img_task_screen_IC);
        screenUnGetTV = (TextView) contentView.findViewById(R.id.tv_task_screen_un_get);
        screenUnGetIMG = (ImageView) contentView.findViewById(R.id.img_task_screen_un_get);
        screenGetTV = (TextView) contentView.findViewById(R.id.tv_task_screen_get);
        screenGetIMG = (ImageView) contentView.findViewById(R.id.img_task_screen_get);
        selectedScreenTV = screenAllStateTV;
        selectedScreenIMG = screenAllStateIMG;
        changeScreenSelectItem(screenAllStateTV, screenAllStateIMG);
        allSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = ALL_SCREEN;
                changeScreenSelectItem(screenAllStateTV, screenAllStateIMG);
                mPopupWindowScreen.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the allSite", Toast.LENGTH_SHORT).show();

            }
        });
        lyICreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = I_CREATE;
                changeScreenSelectItem(screenICTV, screenICIMG);
                mPopupWindowScreen.dismiss();

                Toast.makeText(TaskListActivity.this, "you click the I create", Toast.LENGTH_SHORT).show();

            }
        });
        lyNotGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = UN_GET;
                changeScreenSelectItem(screenUnGetTV, screenUnGetIMG);
                mPopupWindowScreen.dismiss();

                Toast.makeText(TaskListActivity.this, "you click the not get task", Toast.LENGTH_SHORT).show();

            }
        });
        lyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentScreen = GET;
                changeScreenSelectItem(screenGetTV, screenGetIMG);
                mPopupWindowScreen.dismiss();
                Toast.makeText(TaskListActivity.this, "you click the get task", Toast.LENGTH_SHORT).show();

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

    private void initListener() {
        refreshItem.setOnRefreshListener(this);
        //初始化监听器

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

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnRecyclerViewItemClickListener() {
         @Override
         public void onItemClick(View view, int position) {
             Map<String,Object> tasklist = taskList.get(position);
          Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
          Bundle bundle = new Bundle();
//          bundle.putString("task_id", taskList.get(position).get("id").toString());
             intent.putExtras(bundle);
             if(tasklist.containsKey("task_id"))
             intent.putExtra("task_id",tasklist.get("task_id").toString());
             if(tasklist.containsKey("task_type"))
             intent.putExtra("task_type",tasklist.get("type").toString());
             if(tasklist.containsKey("user_name"))
             intent.putExtra("user_name",tasklist.get("user_name").toString());
             if(tasklist.containsKey("is_receive"))
             intent.putExtra("is_receive",tasklist.get("is_receive").toString());
             if(tasklist.containsKey("remark")) {
                 intent.putExtra("remark", tasklist.get("remark").toString());
             }
             if(tasklist.containsKey("task_name"))
             intent.putExtra("task_name",tasklist.get("task_name").toString());
         if(tasklist.containsKey("terminal_name"))
 intent.putExtra("terminal_name",tasklist.get("terminal_name").toString());
             if(tasklist.containsKey("time"))
             intent.putExtra("time",tasklist.get("time").toString());
           startActivity(intent);
             }
           }

        );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishRefresh();
                Toast.makeText(TaskListActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
            }
        }, 1600);
    }


    @Override
    public void onLoading() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishLoadmore();
                Toast.makeText(TaskListActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
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


    public void getNoticeList(){
        SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
        String sessionid= share.getString("sessionid","null");
//       192.168.43.92
        String url = "http://10.0.2.2:8080/asss-webbg/message/DealData.do";
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("cookie",sessionid)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(TaskListActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        taskList.clear();

                        Gson gson = new Gson();
                        /*          Map<String, Object> map1 = gson.fromJson(response, new TypeToken<Map<String, Object>>(){}.getType());*/
                        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>(){}.getType());
                        /*      List<Map<String, Object>> list = (List<Map<String, Object>>) map1;*/
                        taskList.addAll(list);
//                        Toast.makeText(TaskListActivity.this,taskList.toString(),Toast.LENGTH_SHORT).show();
                        /*    noticelist.add(map1);*/
                    }


                });
    }



    @OnClick({R.id.classify, R.id.screen_ly})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.classify:
                showPopupWindowClassify();//点击分类
                break;
            case R.id.screen_ly:
                showPopupWindowScreen();//点击筛选站点
                break;
        }
    }
}
