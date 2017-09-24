package com.example.bob.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bob on 2017/8/20.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String dbname = "Note";
    private static final String tbname = "note";
    private static final String pictbname = "pic";
    private static final int version = 2;
    public static int id;
    private static DatabaseHelper dbHelper;

    public DatabaseHelper(Context context) {
        super(context, dbname, null, version);
    }

    public static SQLiteDatabase getInstance(Context context) {
        if (dbHelper == null) {
            // 指定数据库名为student，需修改时在此修改；此处使用默认工厂；指定版本为1
            dbHelper = new DatabaseHelper(context);
            id = 0;
        }
        return dbHelper.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user(account TEXT primary key,password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS note(noteid integer primary key autoincrement,note TEXT,time TEXT,user TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS pic(picid integer primary key,pos integer,picture String)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

