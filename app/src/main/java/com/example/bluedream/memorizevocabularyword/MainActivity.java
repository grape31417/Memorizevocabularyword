package com.example.bluedream.memorizevocabularyword;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mbtnEnterInput,mbtnShowInput,mbtnTestSetInput;

    public static final String DB_FILE = "Word.db",
            DB_TABLE = "word";

    public SQLiteDatabase mWorddb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbtnEnterInput=(Button)findViewById(R.id.btnEnterInputActivity);
        mbtnShowInput=(Button)findViewById(R.id.btnEnterShowActivity);
        mbtnTestSetInput=(Button)findViewById(R.id.btnEnterTestSetMode);
        mbtnEnterInput.setOnClickListener(btnEnterInput);
        mbtnShowInput.setOnClickListener(btnEnterShow);
        mbtnTestSetInput.setOnClickListener(btnEnterTestSet);


        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();


        Cursor cursor = mWorddb.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        DB_TABLE + "'", null);

        if(cursor != null) {
            if (cursor.getCount() == 0)
                mWorddb.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY," +
                        "Cht TEXT NOT NULL," +
                        "Eng TEXT"+")" );
            cursor.close();
        }
    }
    private View.OnClickListener btnEnterInput =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it =new Intent();
            it.setClass(MainActivity.this,inputWord.class);
            startActivity(it);
        }
    };

    private View.OnClickListener btnEnterShow =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it =new Intent();
            it.setClass(MainActivity.this,showWord.class);
            startActivity(it);
        }
    };
    private View.OnClickListener btnEnterTestSet =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it =new Intent();
            it.setClass(MainActivity.this,testWordSetMode.class);
            startActivity(it);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorddb.close();
    }
}
