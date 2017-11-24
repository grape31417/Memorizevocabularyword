package com.example.bluedream.memorizevocabularyword;

import android.app.Activity;
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

import static com.example.bluedream.memorizevocabularyword.DatabaseControler.DB_TABLE;

public class MainActivity extends AppCompatActivity {
    private Button mbtnEnterInput,mbtnShowInput,mbtnTestSetInput;
    public SQLiteDatabase mWorddb;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActBarDrawerToggle;

    public static final String DB_FILE = "Word.db",
            DB_TABLE = "word";
    private android.support.v4.app.FragmentManager manager= getSupportFragmentManager();
    private android.support.v4.app.FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.getInstance().addActivity(this);
        DatabaseControler Database = new DatabaseControler(this.getApplicationContext());
        mWorddb=Database.OpenDatabase();
        int rowCount =Database.DatabaseCount();
        SwitchFragment switchFragment =new SwitchFragment(rowCount,manager);
        setActionBar();

    }


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

    private void setActionBar()
    {
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

    private AdapterView.OnItemClickListener listViewOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SlectMenuList(((TextView)view).getText().toString());
            mDrawerLayout.closeDrawers();
        }
    };

    public void SlectMenuList(String string)
    {
        Intent it =new Intent();
        switch(string) {
            case "輸入單字":
                it = new Intent();
                it.setClass(this, inputWord.class);
                startActivity(it);
                break;
            case "測驗模式":
                it =new Intent();
                it.setClass(this,testWordSetMode.class);
                startActivity(it);
                break;
            case "列表模式":
                it =new Intent();
                it.setClass(this,showWord.class);
                startActivity(it);
                break;
            case "關閉程式":
                AppUtils.getInstance().exit();
                finish();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorddb.close();
    }

}
