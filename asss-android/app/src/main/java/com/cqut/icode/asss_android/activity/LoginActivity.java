package com.cqut.icode.asss_android.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zhy.http.okhttp.OkHttpUtils.post;

public  class  LoginActivity extends BaseActivity {


    @BindView(R.id.loginUser)
    EditText loginUser;
    @BindView(R.id.loginPwd)
    EditText loginPwd;
    @BindView(R.id.login_go)
    LinearLayout login_go;
    private static Map<String,String> userMap = new HashMap<>();
   private static String BackStageString;
   private static String sessionid;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.login_go)
    public void onClick() {
        String name = loginUser.getText().toString().trim();
        String pwd = loginPwd.getText().toString().trim();
        userMap.put("userName",name);
        userMap.put("password",pwd);
     doLogin();


try {
    if (Integer.parseInt(BackStageString) == 0) {
        Toast.makeText(LoginActivity.this, "账户不存在", Toast.LENGTH_SHORT).show();
        loginUser.getText().clear();
        loginPwd.getText().clear();

    } else if (Integer.parseInt(BackStageString) == -2)
        Toast.makeText(LoginActivity.this, "账户被禁用", Toast.LENGTH_SHORT).show();
    else if (Integer.parseInt(BackStageString) == -1) {
        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
    }
    else if(Integer.parseInt(BackStageString)==1)
        doLoginRequest(name, pwd);
}
catch (NumberFormatException e){
    doLoginRequest(name, pwd);
}
    }

    private void doLoginRequest(final String name, String pwd) {
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("pwd",pwd);
        //从服务器上做登陆验证
        openActivity(MainActivity.class,bundle);
        finish();
    }

public   void  doLogin(){
    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
    try {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(userMap));
        Request request = new Request.Builder()
                /*192.168.43.92*/
                .url("http://10.0.2.2:8080/asss-webbg/access/token")//请求接口。如果需要传参拼接到接口后面。
                .post(body)

                .build();//创建Request 对象

            Response response = null;
        response = client.newCall(request).execute();//得到Response 对象
        Log.d("LoginActivity", response.toString());

        if (response.isSuccessful()) {
            Headers headers =response.headers();//response为okhttp请求后的响应
            List cookies = headers.values("Set-Cookie");
            if(cookies.size()!=0) {
                String session = (String) cookies.get(0);
                String sessionid = session.substring(0, session.indexOf(";"));
                SharedPreferences share = LoginActivity.this.getSharedPreferences("Session", MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();//编辑文件
                edit.putString("sessionid", sessionid);
                edit.commit();
            }
            //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
             BackStageString = response.body().string();
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

}
