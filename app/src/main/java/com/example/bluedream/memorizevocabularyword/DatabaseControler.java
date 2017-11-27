package com.example.bluedream.memorizevocabularyword;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase;




/**
 * Created by USER on 2017/11/24.
 */

public class DatabaseControler {
    private SQLiteDatabase mWorddb;
    private Context context;
    public static final String DB_FILE = "Word.db",
            DB_TABLE = "word";
    final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE;

    public DatabaseControler(Context applicationContext) {
        context= applicationContext;
    }

    public SQLiteDatabase  OpenDatabase ()
    {
        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(context, DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

        Cursor cursor = mWorddb.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        DB_TABLE + "'", null);

        if(cursor != null) {
            if (cursor.getCount() == 0)
                mWorddb.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY," +
                        "Cht TEXT NOT NULL," +
                        "Eng TEXT NOT NULL," +
                        "Level INTEGER NOT NULL);");
            cursor.close();
        }

        return mWorddb;
    }

    public void CloseDatabase()
    {
        mWorddb.close();
    }

    public Cursor query (boolean _id,boolean Cht,boolean Eng,boolean Level)
    {
        String id="",cht="",eng="",level="";
        if(_id==true)id="_id";
        if (Cht==true)cht="Cht";
        if(Eng==true)eng="Eng";
        if (Level==true)level="Level";

        Cursor c = mWorddb.query(true, DB_TABLE, new String[]{id,cht,eng,level}, null, null, null, null, null, null);
        return c;
    }
    public int DatabaseCount()
    {
        Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"_id"}, null, null, null, null, null, null);
        int count=c.getCount();
        c.close();
        return count;
    }

    public boolean dleteDatabase()
    {
        mWorddb.execSQL(DROP_TABLE);
        return true;
    }
}
