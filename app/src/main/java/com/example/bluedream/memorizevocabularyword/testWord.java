package com.example.bluedream.memorizevocabularyword;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.Locale;


import static android.R.attr.mode;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class testWord extends AppCompatActivity  {
    private Button mEnter, mExit, mSpeak;
    private ArrayList<String> srrCht = new ArrayList();
    private ArrayList<String> srrEng = new ArrayList();
    private ArrayList<Integer> id = new ArrayList();
    private ArrayList<Integer> level = new ArrayList();
    private ArrayList<Integer> CompleteWordid = new ArrayList();
    TextView Question, Result;
    EditText Answer;
    String ChoseWord;
    int mode;
    int iRan, WordsSize;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActBarDrawerToggle;
    TextToSpeech spek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);
        AppUtils.getInstance().addActivity(this);


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

        Intent it = getIntent();
        mEnter = (Button) findViewById(R.id.btnEnter);
        mExit = (Button) findViewById(R.id.btnExit);
        mSpeak = (Button) findViewById(R.id.btnTip);
        Question = (TextView) findViewById(R.id.ChtName);
        Result = (TextView) findViewById(R.id.Result);
        Answer = (EditText) findViewById(R.id.edtEng);
        Bundle bundle = it.getExtras();
        srrCht = bundle.getStringArrayList("srrCht");
        srrEng = bundle.getStringArrayList("srrEng");
        level = bundle.getIntegerArrayList("level");
        id = bundle.getIntegerArrayList("id");
        mode = bundle.getInt("mode");
        WordsSize = srrCht.size();


        spek = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                spek.setLanguage(Locale.US);

            }
        });



        iRan = (int) (Math.random() * WordsSize);
        ChoseWord = srrEng.get(iRan).toString();
        Question.setText(srrCht.get(iRan).toString());

        mSpeak.setOnClickListener(Speak);
        mExit.setOnClickListener(Exit);
        mEnter.setOnClickListener(Enter);
    }

    private View.OnClickListener Exit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int counter = CompleteWordid.size();
            if(counter!=0) {

                SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                        new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
                SQLiteDatabase mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

                for (int i = 0; i < counter; i++) {
                    String id = CompleteWordid.get(i).toString();

                    mWorddb.execSQL("UPDATE "+DB_TABLE +" SET Level = 3 WHERE _ID = "+id+";");

                }
            }

            finish();
        }
    };

    private View.OnClickListener Enter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String InputAnswer;
            switch (mode) {
                case 1:
                    if (srrCht.isEmpty() == false) {
                        InputAnswer = Answer.getText().toString();
                        Answer.setText("");
                        if (srrEng.get(iRan).equals(InputAnswer)) {
                            Result.setText("答對了");
                            if (level.get(iRan) < 3) {
                                level.set(iRan, level.get(iRan) + 1);
                            }
                            if (level.get(iRan) == 3) {
                                CompleteWordid.add(id.get(iRan));
                                level.remove(iRan);
                                srrEng.remove(iRan);
                                srrCht.remove(iRan);
                                id.remove(iRan);
                                WordsSize = WordsSize - 1;
                            }
                        } else {
                            Result.setText("答錯了 正解:" + srrEng.get(iRan));
                        }
                        if(WordsSize!=0) {
                            iRan = (int) (Math.random() * WordsSize);
                            ChoseWord = srrEng.get(iRan).toString();
                            Question.setText(srrCht.get(iRan).toString());
                        }
                        break;
                    }
                    if(srrCht.isEmpty())  {
                        MyAlertDialog check = new MyAlertDialog(testWord.this);
                        check.setTitle("恭喜");
                        check.setMessage("你已背完所有單字");
                        check.setIcon(android.R.drawable.ic_dialog_alert);
                        check.setCancelable(false);
                        check.setButton(DialogInterface.BUTTON_POSITIVE, "確定", checkyes);
                        check.show();
                        break;
                    }

                case 2:
                    InputAnswer = Answer.getText().toString();
                    Answer.setText("");
                    if (srrEng.get(iRan).equals(InputAnswer)) {
                        Result.setText("答對了");
                        if (level.get(iRan) < 3) {
                            level.set(iRan, level.get(iRan) + 1);
                        } else if (level.get(iRan) == 3) {
                            CompleteWordid.add(id.get(iRan));
                            level.set(iRan, level.get(iRan) + 1);
                        }
                    } else {
                        Result.setText("答錯了 正解:" + srrEng.get(iRan));
                    }
                    iRan = (int) (Math.random() * WordsSize);
                    ChoseWord = srrEng.get(iRan).toString();
                    Question.setText(srrCht.get(iRan).toString());

            }
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
            switch (((TextView) view).getText().toString()) {
                case "輸入單字":
                    Intent it = new Intent();
                    it.setClass(testWord.this, inputWord.class);
                    startActivity(it);
                    finish();
                    break;
                case "測驗模式":
                    it = new Intent();
                    it.setClass(testWord.this, testWordSetMode.class);
                    startActivity(it);
                    finish();
                    break;
                case "列表模式":
                    it = new Intent();
                    it.setClass(testWord.this, showWord.class);
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
    private DialogInterface.OnClickListener checkyes = new DialogInterface.OnClickListener() {


        @Override
        public void onClick(DialogInterface dialog, int which) {
            int counter = CompleteWordid.size();

            SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                    new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
            SQLiteDatabase mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

            for (int i = 0; i < counter; i++) {
                String id = CompleteWordid.get(i).toString();

                mWorddb.execSQL("UPDATE "+DB_TABLE +" SET Level = 3 WHERE _ID = "+id+";");

            }
            finish();

        }
    };

    private View.OnClickListener Speak =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            spek.speak(srrEng.get(iRan).toString(),TextToSpeech.QUEUE_FLUSH,null);
        }
    };

    @Override
    public void onDestroy()
    {
        if (spek != null)
        {
            spek.stop();
            spek.shutdown();
        }
        super.onDestroy();
    }

}