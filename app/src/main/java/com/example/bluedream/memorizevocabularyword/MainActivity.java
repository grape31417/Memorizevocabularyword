package com.example.bluedream.memorizevocabularyword;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button mbtnEnterInput,mbtnShowInput,mbtnTestSetInput;

    public static final String DB_FILE = "Word.db",
            DB_TABLE = "word";

    public SQLiteDatabase mWorddb;

    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.getInstance().addActivity(this);

        mbtnEnterInput=(Button)findViewById(R.id.btnEnterInputActivity);
        mbtnShowInput=(Button)findViewById(R.id.btnEnterShowActivity);
        mbtnTestSetInput=(Button)findViewById(R.id.btnEnterTestSetMode);
        mbtnEnterInput.setOnClickListener(btnEnterInput);
        mbtnShowInput.setOnClickListener(btnEnterShow);
        mbtnTestSetInput.setOnClickListener(btnEnterTestSet);


        //開啟資料庫
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
                        "Eng TEXT NOT NULL,"+
                        "Level INTEGER NOT NULL);");
            cursor.close();
        }


        // 設定側開式選單。
        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.Drawlayout);
        mActBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name);
        mActBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mActBarDrawerToggle);

        ListView listView = (ListView) findViewById(R.id.menu_list);
        ArrayAdapter<CharSequence> menulist =
                ArrayAdapter.createFromResource(this, R.array.Menu_List,
                        android.R.layout.simple_list_item_1);
        listView.setAdapter(menulist);
        listView.setOnItemClickListener(listViewOnItemClick);



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


    //側開選單功能

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 要先把選單的項目傳給 ActionBarDrawerToggle 處理。
        // 如果它回傳 true，表示處理完成，不需要再繼續往下處理。
        if (mActBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener listViewOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            switch(((TextView)view).getText().toString()) {
                case "輸入單字":
                    Intent it = new Intent();
                    it.setClass(MainActivity.this, inputWord.class);
                    startActivity(it);
                    break;
                case "測驗模式":
                    it =new Intent();
                    it.setClass(MainActivity.this,testWordSetMode.class);
                    startActivity(it);
                    break;
                case "列表模式":
                    it =new Intent();
                    it.setClass(MainActivity.this,showWord.class);
                    startActivity(it);
                    break;
                case "關閉程式":
                    mWorddb.close();
                    AppUtils.getInstance().exit();
                    android.os.Process.killProcess(android.os.Process.myPid());

            }

            mDrawerLayout.closeDrawers();
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorddb.close();
    }
}
