package com.cqut.icode.asss_android.common;

import android.app.Application;
import android.content.Context;

/**
 * 作者：hwl
 * 时间：2017/7/22:20:32
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
