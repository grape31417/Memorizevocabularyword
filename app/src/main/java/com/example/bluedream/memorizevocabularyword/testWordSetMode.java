package com.example.bluedream.memorizevocabularyword;

import android.content.DialogInterface;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shinelw.library.ColorArcProgressBar;

import java.util.ArrayList;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class testWordSetMode extends AppCompatActivity {
    Button mBtnSetModeBack,mbtnEnterTestMode;
    int maxtestCount,mode,i;
    RadioGroup testMode;
    public SQLiteDatabase mWorddb;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActBarDrawerToggle;

    private ArrayList<String> srrCht= new ArrayList();
    private ArrayList<String> srrEng= new ArrayList();
    private ArrayList<Integer> id= new ArrayList();
    private ArrayList<Integer> level= new ArrayList();
    private ColorArcProgressBar crpv;
    private TextView judge;




    private void iniz()
    {
        AppUtils.getInstance().addActivity(this);

        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

        mbtnEnterTestMode=(Button)findViewById(R.id.btnEnterTestMode);
        mBtnSetModeBack=(Button)findViewById(R.id.btnSetModeBack);
        testMode=(RadioGroup)findViewById(R.id.rgTestmode);

        mBtnSetModeBack.setOnClickListener(returntitle);
        mbtnEnterTestMode.setOnClickListener(GOTest);
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

    private void setSectorProgressView ()
    {
        Cursor cursor = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","_id","Level"}, 	null, null, null, null, null, null);
        int maxCount =cursor.getCount();
        int learn=0;
        float result =0;


        cursor.moveToFirst();
        for (int i = 1; i <= maxCount; i++) {
            if (cursor.getInt(3) == 3) {
                learn = learn + 1;
                cursor.moveToNext();
            } else cursor.moveToNext();
        }

        int allword =maxCount;
        int learnword =learn;
        if(allword!=0)
        result =(float)learnword/(float) allword*100;
        cursor.close();


        String a="字庫裡共有"+allword+"個 已背完 "+learn +"個 請持續努力";
        String b="已背完全部單字了 新增單字或 試試隨機模式! ";
        String c="字庫裡沒單字囉  ";
        judge=(TextView) findViewById(R.id.judge);
        if(allword!=0&&result<100)judge.setText(a);
        if (allword!=0&&result==100)judge.setText(b);
        if (allword==0)judge.setText(c);


        crpv = (ColorArcProgressBar) findViewById(R.id.crpv);
        crpv.setCurrentValues(result);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word_set_mode);
        iniz();
        setActionBar();
        setSectorProgressView();

    }

    private View.OnClickListener returntitle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mWorddb.close();
            finish();
        }
    };

    private View.OnClickListener GOTest =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","_id","Level"}, 	null, null, null, null, null, null);
            int rowCount =c.getCount();
            switch (testMode.getCheckedRadioButtonId())
            {
                case R.id.rbbNotLearn:
                    learnModeInput(rowCount,c);
                    break;
                case  R.id.rbRandom:
                    RandModeInput(rowCount,c);
                    break;
            }
            c.close();
            CheckWordEmpty(srrEng);




        }
    };
    private void learnModeInput(int rowCount ,Cursor c)
    {
        mode=1;
        c.moveToFirst();
        for (int i = 1; i <= rowCount; i++) {
            if(c.getInt(3)!=3) {
                srrCht.add(c.getString(0));
                srrEng.add(c.getString(1));
                id.add(c.getInt(2));
                level.add(c.getInt(3));
                c.moveToNext();
            }
            else c.moveToNext();
        }
    }

    private void RandModeInput(int rowCount ,Cursor c)
    {
        mode=2;
        c.moveToFirst();
        for (int i=1;i<=rowCount;i++) {
            srrCht.add(c.getString(0));
            srrEng.add(c.getString(1));
            id.add(c.getInt(2));
            level.add(c.getInt(3));
            c.moveToNext();
        }
    }

    private void CheckWordEmpty(ArrayList srrEng)
    {
        if (srrEng.isEmpty()) {
            MyAlertDialog check = new MyAlertDialog(testWordSetMode.this);
            check.setTitle("哈囉");
            check.setMessage("單字庫已沒單字可背  請至輸入模式輸入");
            check.setIcon(android.R.drawable.ic_dialog_alert);
            check.setCancelable(false);
            check.setButton(DialogInterface.BUTTON_POSITIVE, "確定", checkyes);
            check.show();

        }
        if (srrEng.isEmpty()==false) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("srrCht", srrCht);
            bundle.putStringArrayList("srrEng", srrEng);
            bundle.putIntegerArrayList("id", id);
            bundle.putIntegerArrayList("level", level);
            bundle.putInt("mode", mode);
            Intent it = new Intent();
            it.setClass(testWordSetMode.this, testWord.class);
            it.putExtras(bundle);
            startActivity(it);
            mWorddb.close();
            finish();
        }
    }
    private DialogInterface.OnClickListener checkyes = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent it = new Intent();
            it.setClass(testWordSetMode.this, inputWord.class);
            startActivity(it);
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
            SlectMenuList(((TextView)view).getText().toString());
            finish();
            mDrawerLayout.closeDrawers();
        }
    };
    public  void SlectMenuList(String string)
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





}
