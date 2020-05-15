package com.coolweather.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by yzz on 2020/5/12.
 */

public class MyData extends SQLiteOpenHelper {
    Context mContext;
    public final String CREATE_PROVINCE="create table Provite(" +
            "id integer primary key autoincrement," +
            "provinceName text unique," +
            "provinceCode integer unique)";
    public final String CREATE_CITY="create table City(" +
            "id integer primary key autoincrement," +
            "cityName text unique," +
            "citycode integer unique," +
            "provinceid integer)";
    public final String CREATE_COUNTY="create table County(" +
            "id integer primary key autoincrement," +
            "countyname text unique," +
            "weatherid integer unique," +
            "cityid integer)";

    public MyData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
