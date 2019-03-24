package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 活动描述：任务-->任务领取-->任务领取
 */
public class TaskDetailActivity extends BaseActivity {

    @BindView(R.id.task_info_ly)
    LinearLayout taskInfoLy;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.task_name)
    TextView taskName;
    @BindView(R.id.task_type)
    TextView taskType;
    @BindView(R.id.task_terminal)
    TextView taskTerminal;
    @BindView(R.id.peoples)
    TextView peoples;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.task_remark)
    TextView taskRemark;
    @BindView(R.id.get_it)
    LinearLayout getIt;
    @BindView(R.id.img_task_get_status)
    ImageView imgTaskGetStatus;

    private Map<String, Object> taskInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
      /*  getTaskInfoData();*/
        ReceiveTask();

        initComponent();
   /*     initValue();*/
        initListener();
        showInfo();
    }

    public void initComponent() {
//        //展示虚线，取消展示虚线部分的硬件加速
//        if (Build.VERSION.SDK_INT > 22)
//            taskInfoLy.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //toolbar
        activityTitle.setText("任务领取");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_return);
    }
    private void showInfo(){
        Intent intent = getIntent();
       /* tv_head.setText(intent.getStringExtra("title").toString().trim());
        tv_creator.setText(intent.getStringExtra("founder").toString().trim() + "： " + intent.getStringExtra("content").toString().trim());
*/
        if (Double.parseDouble(intent.getStringExtra("is_receive").toString()) == 1.0) {
            imgTaskGetStatus.setImageResource(R.drawable.task_letter_get_label);
        } else {
            imgTaskGetStatus.setImageResource(R.drawable.task_letter_label);
        }
        taskType.setText(intent.getStringExtra("task_type"));
        taskRemark.setText(intent.getStringExtra("remark"));
        taskName.setText(intent.getStringExtra("task_name"));
        taskTerminal.setText(intent.getStringExtra("terminal_name"));
        peoples.setText(intent.getStringExtra("user_name"));
        time.setText(intent.getStringExtra("time"));

    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
   /* public void initValue() {
        if (Double.parseDouble(taskInfo.get("is_receive").toString()) == 1.0) {
            imgTaskGetStatus.setImageResource(R.drawable.task_letter_get_label);
        } else {
            imgTaskGetStatus.setImageResource(R.drawable.task_letter_label);
        }
        taskName.setText(taskInfo.get("task_name").toString());
        taskTerminal.setText(taskInfo.get("terminal_name").toString());
        changePeopleText();
        time.setText(taskInfo.get("time").toString());
        taskRemark.setText(taskInfo.get("remark").toString());
    }*/

    //修改人员选择情况
    private void changePeopleText() {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<Map<String, Object>> peopleList = (ArrayList<Map<String, Object>>) taskInfo.get("peoples");
        String content = "";
        for (int i = 0; i < peopleList.size(); i++) {
            stringBuffer.append("," + peopleList.get(i).get("userName"));
        }
        if (stringBuffer.length() > 0)
            content = stringBuffer.substring(1);
        peoples.setText(content);
    }

    @OnClick(R.id.get_it)
    public void onClick()
    {
        showToast("领取成功");

    }
 /*addParams("taskID",intent.getStringExtra("task_id"))
            .addParams("IS_Receive",intent.getStringExtra("is_receive"))*/
    public void ReceiveTask(){
        Intent intent = getIntent();
        String taskID = intent.getStringExtra("task_id");
        String isReceive = intent.getStringExtra("is_receive");
        SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);

        String sessionid= share.getString("sessionid","null");
        RequestBody requestBody = new FormBody.Builder()
        .add("taskID",taskID)
        .add("isReceive",isReceive)
        .build();
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        try {
            Request request = new Request.Builder()
                    /*192.168.43.92*/
                     .url("http://10.0.2.2:8080/asss-webbg/message/ReceiveTask.do")//请求接口。如果需要传参拼接到接口后面。
                     .addHeader("cookie",sessionid)
                     .header("taskID",intent.getStringExtra("task_id"))
                     .header("isReceive",intent.getStringExtra("is_receive"))
                    .post(requestBody)
                     .build();//创建Request 对象

            Response response = null;
            response = client.newCall(request).execute();//得到Response 对象
            Log.d("LoginActivity", response.toString());

            if (response.isSuccessful()) {
                //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
               String BackStageString = response.body().string();
                Log.d("LoginActivity", "doLogin: "+BackStageString);
            }
        }  catch (SocketTimeoutException e) {
            client.dispatcher().cancelAll();
            client.connectionPool().evictAll();
            //TODO: 重新请求
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       }
public void ReceiveTask2(){
        Intent intent = getIntent();
    String url = "http://10.0.2.2:8080/asss-webbg/message/ReceiveTask.do";
    OkHttpUtils
            .post()
            .url(url)
            .addParams("taskID",intent.getStringExtra("task_id"))
            .addParams("isReceive",intent.getStringExtra("is_receive"))
            .build()
            .execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e("TaskDetailActivity", e.toString() );
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.e("TaskDetailActivity", response );
                }


            });
}

}
