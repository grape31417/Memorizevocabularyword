package com.example.bluedream.memorizevocabularyword;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class inputWord extends AppCompatActivity  {

    private ArrayList<String> srrCht= new ArrayList();
    private ArrayList<String> srrEng= new ArrayList();
    private int index =0;
    private Button mBtnAddWord,mbtnRemoveLastWord,mbtnInputExit;
    private EditText medtCht,medtEng;
    private TextView mResult;
    public SQLiteDatabase mWorddb;


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActBarDrawerToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_word);
        AppUtils.getInstance().addActivity(this);

        mBtnAddWord =(Button)findViewById(R.id.btnAddWord);
        mbtnRemoveLastWord=(Button)findViewById(R.id.btnRemoveLasrWord);
        mbtnInputExit=(Button)findViewById(R.id.btnInputExit);
        mResult =(TextView)findViewById(R.id.txtInputResult);
        medtCht=(EditText)findViewById(R.id.edtCht);
        medtEng=(EditText)findViewById(R.id.edtEng);

        mBtnAddWord.setOnClickListener(addWord);
        mbtnRemoveLastWord.setOnClickListener(removeLastWord);
        mbtnInputExit.setOnClickListener(exit);

        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();


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

    private View.OnClickListener addWord =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(medtEng.getText().toString().equals("")||medtCht.getText().toString().equals(""))
            {
                mResult.setText("有欄位為空，請重新輸入");
            }
            else
            {
                srrCht.add(medtCht.getText().toString());
                srrEng.add(medtEng.getText().toString());
                index++;
               String s="你已輸入第"+index+"個單字  "+srrCht.get(index-1).toString()+"   "+srrEng.get(index-1).toString();
                mResult.setText(s);
                medtEng.setText("");
                medtCht.setText("");
            }

        }
    };

    private View.OnClickListener removeLastWord =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(srrEng.isEmpty()==true||srrCht.isEmpty()==true)
            {
                mResult.setText("無輸入單字，無法刪除");
            }
            else {
                String s="你已刪除第"+index+"個單字  "+srrCht.get(index-1).toString()+"   "+srrEng.get(index-1).toString();
                mResult.setText(s);
                srrCht.remove(index - 1);
                srrEng.remove(index - 1);
                index--;
            }
        }
    };

    private View.OnClickListener exit =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                for (int i = 1; i <= index; i++) {
                    ContentValues newRow = new ContentValues();
                    newRow.put("Cht", srrCht.get(i-1).toString());
                    newRow.put("Eng", srrEng.get(i-1).toString());
                    newRow.put("Level",0);
                    mWorddb.insert(DB_TABLE, null, newRow);
                }
                mWorddb.close();
                finish();
        }
    };

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
                    it.setClass(inputWord.this, inputWord.class);
                    startActivity(it);
                    finish();
                    break;
                case "測驗模式":
                    it =new Intent();
                    it.setClass(inputWord.this,testWordSetMode.class);
                    startActivity(it);
                    finish();
                    break;
                case "列表模式":
                    it =new Intent();
                    it.setClass(inputWord.this,showWord.class);
                    startActivity(it);
                    finish();
                    break;
                case "關閉程式":
                    AppUtils.getInstance().exit();
                    finish();

            }
            mDrawerLayout.closeDrawers();
        }
    };

}
