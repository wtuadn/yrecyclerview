package com.wtuadn.demo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "goods_db";
    public final static int version = 1;
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("create table if not exists goods(" +
                    "id integer primary key," +
                    "name varchar)");

            for (int i = 0; i < 30000; i++) {
                db.execSQL("INSERT INTO goods VALUES (" + i + ", '商品 " + i + "')");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}