package com.example.a_tsu.refrigerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    //データベース名
    private static final String DB_NAME = "App.db";
    //データベースバージョン
    private static final int DB_VERSION = 1;
    private int m_writableDatabaseCount = 0;
    private static DBOpenHelper m_instance = null;

    synchronized static public DBOpenHelper getInstance(Context context) {
        if (m_instance == null) {
            m_instance = new DBOpenHelper(context.getApplicationContext());
        }
        return m_instance;
    }

    public DBOpenHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    synchronized public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        if(db != null)
            ++m_writableDatabaseCount;
        return db;
    }

    synchronized public void closeWritableDatabase( SQLiteDatabase database ) {
        if ( m_writableDatabaseCount > 0 && database != null ) {
            --m_writableDatabaseCount;
            if ( m_writableDatabaseCount == 0 )
                database.close();
        }
    }

    @Override
    //データベースを作成する
    public void onCreate(SQLiteDatabase db) {
        //必要データベース数の分だけ行数を書く
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Stock ( item_name TEXT PRIMARY KEY NOT NULL , number INTEGER , category TEXT , price INTEGER , deadline TEXT)"
        );
        //"CREATE TABLE IF NOT EXISTS Stock ( item_name TEXT PRIMARY KEY NOT NULL , number INTEGER , category TEXT , price INTEGER , deadline TEXT PRIMARY KEY NOT NULL)"
        //のほうがいい
    }

    @Override
    //バージョンを変更したときに実行する
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
