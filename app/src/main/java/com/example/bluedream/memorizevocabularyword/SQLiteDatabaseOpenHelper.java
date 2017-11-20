package com.example.bluedream.memorizevocabularyword;

import android.content.Context;
import android.database.DatabaseErrorHandler;

/**
 * Created by BlueDream on 2017/11/17.
 */

public class SQLiteDatabaseOpenHelper extends android.database.sqlite.SQLiteOpenHelper{

    public SQLiteDatabaseOpenHelper(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDatabaseOpenHelper(Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



