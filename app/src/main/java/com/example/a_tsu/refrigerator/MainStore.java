package com.example.a_tsu.refrigerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//実際にデータベースを操作するクラス
public class MainStore {
    private DBOpenHelper m_helper;
    private SQLiteDatabase	m_db;
    private static final String TBL_NAME = "Stock";

    public MainStore( Context context ) {
        m_helper = DBOpenHelper.getInstance( context );
        if( m_helper != null )
            m_db = m_helper.getWritableDatabase();
        else
            m_db = null;
    }

    public void close() {
        m_db.close();
    }

    //レコードの追加
    public void add( String item_name,String  number,String category,String price,String deadline ) {
        ContentValues val = new ContentValues();
        val.put( "item_name", item_name );
        val.put( "number", number );
        val.put( "category", category );
        val.put( "price", price );
        val.put( "deadline", deadline );
        m_db.insert( TBL_NAME, null, val );
    }

    //レコードの削除
    public void delete( String item_name ) {
        ContentValues val = new ContentValues();
        val.put("item_name", item_name);
        //テーブル TBL_NAME の item_name=(第二引数) item_name(第三引数) を満たす行を削除
        m_db.delete(TBL_NAME, "item_name=?", new String[] { item_name });
    }

    //レコード(数量)の更新
    public void number_update( String item_name, int number ) {
        ContentValues val = new ContentValues();
        val.put( "number", number );
        //テーブル TBL_NAME の item_name=(第三引数) item_name(第四引数) を満たす行を val(第二引数)に更新
        m_db.update( TBL_NAME, val, "item_name=?", new String[] { item_name });
    }

    //レコードの読み出し
    public ArrayList<Map<String, String>> loadAll() {
        //読みだしたデータを格納する変数 data
        ArrayList<Map<String, String>> data = new ArrayList<>();
        //なにも登録されていなければreturn
        if (m_db == null)   return null;
        //ビューを取り出す
        //GroupByはitem_nameでひとつにした方がいいかも
        Cursor cursor = m_db.query( TBL_NAME,new String[] { "item_name", "number", "category", "deadline" },null, null, null, null, null );
        //行数を格納する変数 numRows
        int numRows = cursor.getCount();
        //最初の行から
        cursor.moveToFirst();
        //一行づつ取り出して格納
        for(int i = 0; i < numRows; i++ ) {
            Map<String,String> item = new HashMap<>();
            item.put("item_name",cursor.getString(0));
            item.put("data","期限:"+cursor.getString(3)+" 品目:"+cursor.getString(2)+" 量:"+cursor.getString(1));
            data.add(item);
            //次の行へ
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    //食材名の検索 (部分一致検索 , 検索結果の行数を返す)
    public int name_seek( String name ) {
        //なにも登録されていなければreturn
        if ( m_db == null )  return 0;
        //検索文
        String query = "SELECT item_name FROM "+TBL_NAME+" WHERE item_name LIKE '%' || ? || '%'";
        //検索結果をcursorに格納 (基本的には1行1列の表ができるはず)
        Cursor cursor = m_db.rawQuery(query,new String[] { name });
        //行数の格納
        int numRows = cursor.getCount();
        cursor.close();
        //検索結果の行数を返す
        return numRows;
    }

    //ある食材名の品目についての検索 (部分一致検索 , 結果が一つであることが前提)
    public String category_seek( String name ) {
        String category = "";
        //なにも登録されていなければreturn
        if ( m_db == null )  return category;
        //検索文
        String query = "SELECT category FROM "+TBL_NAME+" WHERE item_name LIKE '%' || ? || '%'";
        //検索結果をcursorに格納 (基本的には1行1列の表ができるはず)
        Cursor cursor = m_db.rawQuery(query,new String[] { name });
        //検索結果がある場合
        if (cursor.getCount() != 0) {
            //品目を取り出し、categoryに格納
            cursor.moveToFirst();
            category = cursor.getString(0);
        }
        cursor.close();
        //品目を返す (ない場合は初期値""が返る)
        return category;
    }
}
