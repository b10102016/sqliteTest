package com.example.user.sqlitetest.dbDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017/4/19.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BLOB";
    // 資料庫名稱
    public static final int DATABASE_VERSION = 1;
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    //public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;
    //use to create table
    private static final String ctDeviceValue ="CREATE TABLE IF NOT EXISTS PAPER(_id INTEGER PRIMARY KEY  NOT NULL," +
            "AuthorName NVARCHAR(100),AuthDataTime DATETIME,Title NVARCHAR(100));";
    //use to drop table
    private static final String dtDeviceVlue ="DROP TABLE IF EXISTS ECG;";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ctDeviceValue);


    }

    private void onDropTable(SQLiteDatabase db) {
        db.execSQL(dtDeviceVlue);

    }
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SQLiteHelper(context).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDropTable(db);
        onCreate(db);

    }
}
