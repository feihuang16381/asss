package com.cqut.icode.asss_android.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 10713 on 2017/7/20.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String NOTICEINFO = "create table notice_info(" +
            "id text primary key," +
            "state text," +
            "createtime text," +
            "founder text," +
            "createinfo text)";

    public static final String NOTICEISSUE = "create table notice_issue(" +
            "title text," +
            "textinfo text)";

    public static final String TERMINALADD = "create table terminal_info(" +
                    "area text," +
                    "terminal_name text," +
                    "terminal_code text primary key," +
                    "equipment_id text," +
                    "equipment_type_show text," +
                    "item text," +
                    "card_num text," +
                    "isover_insured text," +
                    "remark_info text," +
                    "longitude text," +
                    "latitude text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTICEINFO);
        db.execSQL(NOTICEISSUE);
        db.execSQL(TERMINALADD);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists NOTICEINFO");
        db.execSQL("drop table if exists NOTICEISSUE");
        db.execSQL("drop table if exists TERMINALADD");
        onCreate(db);
    }
}
