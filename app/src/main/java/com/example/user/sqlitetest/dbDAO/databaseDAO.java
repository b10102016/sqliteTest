package com.example.user.sqlitetest.dbDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by user on 2017/4/19.
 */

public class databaseDAO {
    // 表格名稱
    public static final String TABLE_NAME = "PAPER";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
//    public static final String DATETIME_COLUMN = "datetime";
//    public static final String COLOR_COLUMN = "color";
//    public static final String TITLE_COLUMN = "title";
//    public static final String CONTENT_COLUMN = "content";
//    public static final String FILENAME_COLUMN = "filename";
//    public static final String LATITUDE_COLUMN = "latitude";
//    public static final String LONGITUDE_COLUMN = "longitude";
//    public static final String LASTMODIFY_COLUMN = "lastmodify";

    // 使用上面宣告的變數建立表格的SQL指令
//    public static final String CREATE_TABLE =
//            "CREATE TABLE " + TABLE_NAME + " (" +
//                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    DATETIME_COLUMN + " INTEGER NOT NULL, " +
//                    COLOR_COLUMN + " INTEGER NOT NULL, " +
//                    TITLE_COLUMN + " TEXT NOT NULL, " +
//                    CONTENT_COLUMN + " TEXT NOT NULL, " +
//                    FILENAME_COLUMN + " TEXT, " +
//                    LATITUDE_COLUMN + " REAL, " +
//                    LONGITUDE_COLUMN + " REAL, " +
//                    LASTMODIFY_COLUMN + " INTEGER)";

    // 資料庫物件
    private SQLiteDatabase db;
    private final String TAG="dbDAO";
    // 建構子，一般的應用都不需要修改
    public databaseDAO(Context context) {
        db = SQLiteHelper.getDatabase(context);
        Log.d(TAG,"DAO created!");
    }

    public databaseDAO(SQLiteDatabase sqldb) {
        db = sqldb;
        Log.d(TAG,"DAO created from existed SQLite!");
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }
    public void clearTable()
    {
        db.execSQL("delete from "+ TABLE_NAME);
    }
    // 新增參數指定的物件
    public void insert(ContentValues input) {

        long id = db.insert(TABLE_NAME, null, input);

    }

    // 修改參數指定的物件
    public boolean update() {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料


        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "= TESTING" ;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 讀取所有記事資料
    public String getAll() {
        StringBuilder result = new StringBuilder();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Log.d(TAG,"Getting Record : "+cursor.getString(1));
            result.append(getRecord(cursor)).append('\n');
        }


        cursor.close();
        Log.d(TAG,"Finish");
        return result.toString();

    }
    public String getAll_f2() {
        StringBuilder result = new StringBuilder();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Log.d(TAG,"Getting Record : "+cursor.getString(1));
            result.append(getRecord(cursor)).append(';');
        }


        cursor.close();
        Log.d(TAG,"Finish");
        return result.toString();

    }

    // 取得指定編號的資料物件
    public String get(long id) {
        // 準備回傳結果用的物件

        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);
        String resultStr = "";
        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            resultStr = resultStr+getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return resultStr;
    }

    // 把Cursor目前的資料包裝為物件
    public String getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        StringBuilder record=new StringBuilder();
        for(int i=1;i<cursor.getColumnCount();i++)
        {
            //i=1 to ignore "_id" column
            // if column is a number parse 0 else parse |
            if(cursor.isNull(i))
                if(i==5) record.append("0").append("|");
                else if(i<8) record.append("|");
                else if(i<37) record.append("0").append("|");
                else record.append("|");
            else
                record.append(cursor.getString(i)).append("|");

        }

        // 回傳結果
        String result = record.toString();
        return result.substring(0,result.length()-1);
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }


}
