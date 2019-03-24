package com.cqut.icode.asss_android.util;

import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * 作者：hwl
 * 时间：2017/7/29:9:56
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class TimeUtil {

    public static String formatDate(int selectYear, int selectMonth, int selectDayOfMonth) {
        return selectYear + "-" + selectMonth + "-" + selectDayOfMonth;
    }

    public static String formatTime(int selectHourOfDay, int selectMinute) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Time time = new Time(System.currentTimeMillis());
        time.setHours(selectHourOfDay);
        time.setMinutes(selectMinute);
        return format.format(time);
    }

    public static String[] spitTime(String time){
        String[] when = new String[2];
        when[0] = time.substring(0,10);
        when[1] = time.substring(10);
        return when;
    }
}
