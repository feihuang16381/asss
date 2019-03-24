package com.cqut.icode.asss_android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.MyApplication;

import butterknife.BindView;

/**
 * Created by 10713 on 2017/7/15.
 */

public class SetWifiLoadActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wifiload)
    Switch wifistatus;
    SharedPreferences wifi_status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_wifi_load);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_return);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        wifi_status = getSharedPreferences("wifistatus", Activity.MODE_PRIVATE);
       wifistatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   wifi_status.edit().putString("switch_state","1").commit();
                   if(isWifi(SetWifiLoadActivity.this)){
                       Toast.makeText(MyApplication.getContext(),"调用传输服务",Toast.LENGTH_SHORT).show();
                   }
               }
               else{
                   wifi_status.edit().putString("switch_state","0").commit();
               }
           }
       });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static boolean isWifi(Activity activity) {
        Context context = activity.getApplicationContext();
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isAvailable() && networkInfo.getState()==NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
