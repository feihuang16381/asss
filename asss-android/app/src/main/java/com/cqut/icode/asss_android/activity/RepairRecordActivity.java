package com.cqut.icode.asss_android.activity;

import android.content.Intent;
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
import com.cqut.icode.asss_android.adapter.RepairRecordAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动描述：个人中心-->维修记录-->维修记录
 */
public class RepairRecordActivity extends BaseActivity implements SHSwipeRefreshLayout.SHSOnRefreshListener {

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
    @BindView(R.id.screen_ly)
    LinearLayout screenLy;
    @BindView(R.id.list_item)
    RecyclerView listItem;

    @BindView(R.id.refresh_item)
    SHSwipeRefreshLayout refreshItem;

    private TextView selectedClassifyTV = null;
    private ImageView selectedClassifyIMG = null;
    private TextView tvClassifyAllSite = null;
    private ImageView imgClassifyAllSite = null;
    private TextView tvClassifDgSource = null;
    private ImageView imgClassifyDgSource = null;
    private TextView tvClassifOther = null;
    private ImageView imgClassifyOther = null;

    public final static int DANGER_SOURCE = 1;
    public final static int OTHER = 2;
    public final static int ALL_CLASSIFY = 0;

    private int currentClassify = ALL_CLASSIFY;
    private View classifyPopupView = null;

    private static int screen_width = 0;
    private PopupWindow mPopupWindowClassify = null; //分区选择弹出窗
    private ArrayList<Map<String, Object>> repairRecordData = new ArrayList<>();//站点列表数据
    private RepairRecordAdapter repairRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_record);
        ButterKnife.bind(this);
        getRepairRecordData("123", 1);
        initComponent();
    }

    public void initComponent() {

        activityTitle.setText("维修记录");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        screen_width = dm.widthPixels / 3;
        LinearLayoutManager LayoutManager = new LinearLayoutManager(RepairRecordActivity.this);
        listItem.setLayoutManager(LayoutManager);
        repairRecordAdapter = new RepairRecordAdapter(repairRecordData);
        listItem.setAdapter(repairRecordAdapter);

        screenLy.setVisibility(View.INVISIBLE);

        initPopupWindow();
        initListener();
    }

    private void initListener() {
        refreshItem.setOnRefreshListener(this);
        repairRecordAdapter.setOnItemClickListener(new RepairRecordAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               Map<String,Object> currentSelect =  repairRecordData.get(position);
                skipActivity(currentSelect.get("type").toString());
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void skipActivity(String type){
        switch (type){
            case "1.0":
                Intent dangerIntent = new Intent(RepairRecordActivity.this,DangerReportActivity.class);
                startActivity(dangerIntent);
                break;
            case "2.0":
                Intent commonTntent = new Intent(RepairRecordActivity.this,CommonMaintainActivity.class);
                startActivity(commonTntent);
                break;
        }

    }

    public void initPopupWindow() {
        classifyPopupView = setPopupWindowView(R.layout.terminal_classify_list);
        mPopupWindowClassify = setPopupWindowProperty(classifyPopupView);
        setClassifyItemListener(classifyPopupView);//给选项添加点击事件
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
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

    //获取站点分类的数据
    private void setClassifyItemListener(View contentView) {
        LinearLayout allSite = (LinearLayout) contentView.findViewById(R.id.allSite);
        LinearLayout dangerSource = (LinearLayout) contentView.findViewById(R.id.dangerSource);
        LinearLayout other = (LinearLayout) contentView.findViewById(R.id.other);

        tvClassifyAllSite = (TextView) contentView.findViewById(R.id.tv_allSite);
        imgClassifyAllSite = (ImageView) contentView.findViewById(R.id.img_allSite);
        tvClassifDgSource = (TextView) contentView.findViewById(R.id.tv_dg_source);
        imgClassifyDgSource = (ImageView) contentView.findViewById(R.id.img_dg_source);
        tvClassifOther = (TextView) contentView.findViewById(R.id.tv_other);
        imgClassifyOther = (ImageView) contentView.findViewById(R.id.img_other);

        selectedClassifyTV = tvClassifyAllSite;
        selectedClassifyIMG = imgClassifyAllSite;
        changeClassifySelectItem(tvClassifyAllSite, imgClassifyAllSite);

        allSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = ALL_CLASSIFY;
                changeClassifySelectItem(tvClassifyAllSite, imgClassifyAllSite);
                mPopupWindowClassify.dismiss();
                Toast.makeText(RepairRecordActivity.this, "you click the allSite", Toast.LENGTH_SHORT).show();

            }
        });
        dangerSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = DANGER_SOURCE;
                changeClassifySelectItem(tvClassifDgSource, imgClassifyDgSource);
                mPopupWindowClassify.dismiss();
                Toast.makeText(RepairRecordActivity.this, "you click the dangerSource", Toast.LENGTH_SHORT).show();

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentClassify = OTHER;
                changeClassifySelectItem(tvClassifOther, imgClassifyOther);
                mPopupWindowClassify.dismiss();
                Toast.makeText(RepairRecordActivity.this, "you click the other", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPopupWindowClassify() {
        backgroundAlpha(0.7f);
        mPopupWindowClassify.showAsDropDown(classify, 60, -30);
    }


    private void changeClassifySelectItem(TextView tv, ImageView img) {
        selectedClassifyTV.setTextColor(getResources().getColor(R.color.theme_secondary_text_inverted));
        selectedClassifyIMG.setVisibility(View.INVISIBLE);
        tv.setTextColor(getResources().getColor(R.color.yellow));
        img.setVisibility(View.VISIBLE);
        selectedClassifyTV = tv;
        selectedClassifyIMG = img;
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @OnClick(R.id.classify)
    public void onClick() {
        showPopupWindowClassify();//点击分类
    }

    @Override
    public void onRefresh() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishRefresh();
                Toast.makeText(RepairRecordActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
            }
        }, 1600);
    }

    @Override
    public void onLoading() {
        refreshItem.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshItem.finishLoadmore();
                Toast.makeText(RepairRecordActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
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

    private void getRepairRecordData(String searchValue, int type) {
        String response = "[\n" +
                "    {\n" +
                "        \"id\": \"123\",\n" +
                "        \"terminal_id\": \"1234\",\n" +
                "        \"terminal_name\": \"重庆理工1\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-2 14:00\",\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"145\",\n" +
                "        \"terminal_id\": \"12345\",\n" +
                "        \"terminal_name\": \"重庆理工10\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-2 14:00\",\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "\n" +
                "    {\n" +
                "        \"id\": \"582\",\n" +
                "        \"terminal_id\": \"1268\",\n" +
                "        \"terminal_name\": \"重庆理工9\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-3 14:00\",\n" +
                "        \"type\": 2\n" +
                "    },\n" +
                "\n" +
                "    {\n" +
                "        \"id\": \"1893\",\n" +
                "        \"terminal_id\": \"11434\",\n" +
                "        \"terminal_name\": \"重庆理工8\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-6 14:00\",\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"2685\",\n" +
                "        \"terminal_id\": \"3669\",\n" +
                "        \"terminal_name\": \"重庆理工7\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-9 14:00\",\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"361\",\n" +
                "        \"terminal_id\": \"2550\",\n" +
                "        \"terminal_name\": \"重庆理工6\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-7 14:00\",\n" +
                "        \"type\": 2\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"114\",\n" +
                "        \"terminal_id\": \"110\",\n" +
                "        \"terminal_name\": \"重庆理工4\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-2 14:00\",\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"361\",\n" +
                "        \"terminal_id\": \"2012\",\n" +
                "        \"terminal_name\": \"重庆理工3\",\n" +
                "        \"serviceman\": \"张三、李四、王五\",\n" +
                "        \"time\": \"2017-12-1 14:00\",\n" +
                "        \"type\": 2\n" +
                "    }\n" +
                "]";
        repairRecordData.clear();
        Gson gson = new Gson();
        List<Map<String, Object>> list = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        repairRecordData.addAll(list);
        list = null;
    }

}
