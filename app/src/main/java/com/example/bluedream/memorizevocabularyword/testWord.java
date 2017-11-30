package com.example.bluedream.memorizevocabularyword;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shinelw.library.ColorArcProgressBar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class testWord extends AppCompatActivity  {
    private Button mEnter, mExit, mSpeak,mtip;
    private ArrayList<String> srrCht = new ArrayList();
    private ArrayList<String> srrEng = new ArrayList();
    private ArrayList<Integer> id = new ArrayList();
    private ArrayList<Integer> level = new ArrayList();
    private ArrayList<Integer> CompleteWordid = new ArrayList();
    static TextView Question;
    TextView Result;
    EditText Answer;
     static String ChoseWord;
    int mode,tipflag=0;
    static int iRan;
    int WordsSize,right,error;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActBarDrawerToggle;
    private TextToSpeech spek;
    private SQLiteDatabase mWorddb;
    private ColorArcProgressBar crpv;
    int learn=0;
    private SoundPool soundPool ;



    private void iniz()
    {
        AppUtils.getInstance().addActivity(this);
        DatabaseControler Database = new DatabaseControler(this.getApplicationContext());
        mWorddb = Database.OpenDatabase();
        Intent it = getIntent();
        mEnter = (Button) findViewById(R.id.btnEnter);
        mExit = (Button) findViewById(R.id.btnExit);
        mSpeak = (Button) findViewById(R.id.btnSay);
        mtip =(Button) findViewById(R.id.btnTip);
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


        mSpeak.setOnClickListener(Speak);
        mExit.setOnClickListener(Exit);
        mEnter.setOnClickListener(Enter);

        iRan = (int) (Math.random() * WordsSize);
        ChoseWord = srrEng.get(iRan).toString();
        Question.setText(srrCht.get(iRan).toString());

        mSpeak.setOnClickListener(Speak);
        mExit.setOnClickListener(Exit);
        mEnter.setOnClickListener(Enter);
        mtip.setOnClickListener(tip);

        InputFilter Engtype = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("[a-zA-Z]+");
                Matcher m = p.matcher(source.toString());
                if (!m.matches()) {
                    //Toast.makeText(testWord.this,"請輸入英文",Toast.LENGTH_SHORT).show();
                    return "";
                }
                return null;
            }
        };

        Answer.setFilters(new InputFilter[]{Engtype});

        soundPool= new SoundPool(1, AudioManager.STREAM_MUSIC, 5);

         right =soundPool.load(this, R.raw.right, 1);
         error =soundPool.load(this,R.raw.erroe, 1);





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

    private void setMyChoice()
    {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        wordAdapter wordAdapter = new wordAdapter(srrCht,srrEng);
        recyclerView.setAdapter(wordAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);
        setActionBar();
        iniz();
        setMyChoice();

    }




    private View.OnClickListener Exit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveLevelData();
        }
    };
    private void saveLevelData()
    {
        int counter = CompleteWordid.size();
        if(counter!=0) {
            DatabaseControler Database = new DatabaseControler(this.getApplicationContext());
            mWorddb = Database.OpenDatabase();
            for (int i = 0; i < counter; i++) {
                String id = CompleteWordid.get(i).toString();

                mWorddb.execSQL("UPDATE "+DB_TABLE +" SET Level = 3 WHERE _ID = "+id+";");
            }
        }
        finish();
    }

    private View.OnClickListener Enter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String InputAnswer;
            switch (mode) {
                case 1:
                  LearnMode();
                    break;
                case 2:
                    RandomMode();
                    break;

            }
        }
    };
    private void RandomMode()
    {
       String InputAnswer = Answer.getText().toString();
        Answer.setText("");
        if (srrEng.get(iRan).equals(InputAnswer)) {
            Result.setText("答對了");
            soundPool.play(right,1, 1, 0, 0, 1);
            if (level.get(iRan) < 3) {
                level.set(iRan, level.get(iRan) + 1);
            } else if (level.get(iRan) == 3) {
                Result.setText("答對了" +srrCht.get(iRan)+" 已經很熟了");
                CompleteWordid.add(id.get(iRan));
                level.set(iRan, level.get(iRan) + 1);
            }
        } else {
            Result.setText("答錯了 正解:" + srrEng.get(iRan));
            soundPool.play(error,1, 1, 0, 0, 1);
        }
        iRan = (int) (Math.random() * WordsSize);
        ChoseWord = srrEng.get(iRan).toString();
        Question.setText(srrCht.get(iRan).toString());
    }
    private void LearnMode()
    {
        if (srrCht.isEmpty() == false) {
            String InputAnswer = Answer.getText().toString();
            Answer.setText("");
            if (srrEng.get(iRan).equals(InputAnswer)) {
                soundPool.play(right,1, 1, 0, 0, 1);
                Result.setText("答對了");
                if (level.get(iRan) < 3) {
                    level.set(iRan, level.get(iRan) + 1);
                }
                if (level.get(iRan) == 3) {
                    CompleteWordid.add(id.get(iRan));
                    Result.setText("答對了" +srrCht.get(iRan)+" 已經很熟了");
                    level.remove(iRan);
                    srrEng.remove(iRan);
                    srrCht.remove(iRan);
                    id.remove(iRan);
                    WordsSize = WordsSize - 1;
                    setMyChoice();
                }
            } else {
                Result.setText("答錯了 正解:" + srrEng.get(iRan));
                soundPool.play(error,1, 1, 0, 0, 1);
            }
            if(WordsSize!=0) {
                iRan = (int) (Math.random() * WordsSize);
                ChoseWord = srrEng.get(iRan).toString();
                Question.setText(srrCht.get(iRan).toString());
            }
        }
        if(srrCht.isEmpty())  {
            MyAlertDialog check = new MyAlertDialog(testWord.this);
            check.setTitle("恭喜");
            check.setMessage("你已背完所有單字");
            check.setIcon(android.R.drawable.ic_dialog_alert);
            check.setCancelable(false);
            check.setButton(DialogInterface.BUTTON_POSITIVE, "確定", checkyes);
            check.show();
        }
    }




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
            SlectMenuList (((TextView) view).getText().toString());
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

    private DialogInterface.OnClickListener checkyes = new DialogInterface.OnClickListener() {


        @Override
        public void onClick(DialogInterface dialog, int which) {
            saveLevelData();
        }
    };

    private View.OnClickListener Speak =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            spek.speak(srrEng.get(iRan).toString(),TextToSpeech.QUEUE_FLUSH,null);
        }
    };


    private View.OnClickListener tip =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tip();
        }
    };

    private void tip()
    {
        String answer =Answer.getText().toString();
        char tipword = 0;
        int inputwordsize =answer.length();
        int tipWordSize =srrEng.get(iRan).length();
        String tipanswer ="" ;


        if (tipflag == 3&&inputwordsize<tipWordSize)//修正錯誤
        {
            for (int i = 0; i < inputwordsize; i++) {
                tipanswer = tipanswer + srrEng.get(iRan).charAt(i);
            }
        }

        if (inputwordsize>tipWordSize)tipflag=5;//輸入字數 超過答案字數
        if(tipflag!=5&&tipflag!=3) {
            if (inputwordsize == 0) {
                tipword = srrEng.get(iRan).charAt(0);
                tipflag = 0;//無輸入
            }


            for (int i = 0; i < inputwordsize; i++) {//確認輸入有無錯誤
                if (srrEng.get(iRan).charAt(i) != answer.charAt(i)) {
                    tipflag = 2;
                    break;
                }
                tipflag = 1;
            }

            if (tipflag == 1 && inputwordsize == srrEng.get(iRan).length()) tipflag = 4;//全對
        }


        switch (tipflag) {
            case 0:
                Result.setText("下一個字為 "+tipword);
                tipflag=0;
                break;
            case 1:
                Result.setText("你目前輸入的都對 提示下一個字為"+srrEng.get(iRan).charAt(inputwordsize) );
                tipflag=0;
                break;
            case 2:
                tipflag=3;
                Result.setText("你目前輸入有誤 想不出來的話 再按提示紐");
                break;
            case 3:
                Answer.setText(tipanswer);
                Answer.setSelection(tipanswer.length());
                Result.setText("已幫你修正錯誤的地方");
                tipflag=0;
                break;
            case 4:
                Result.setText("不需提示 有信心點");
                tipflag=0;
                break;
            case 5:
                Result.setText("字數不同 這個單字只有  "+tipWordSize+"個字");
                tipflag=0;
                break;

        }





    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkexit();
        }
        return false;
    }

    private void checkexit ()
    {
        if (srrCht.isEmpty()==false) {
            MyAlertDialog isExit = new MyAlertDialog(testWord.this);
            isExit.setTitle("退出確認");
            isExit.setMessage(" 即將結束測驗 將會保存進度");
            isExit.setIcon(android.R.drawable.ic_dialog_alert);
            isExit.setCancelable(false);
            isExit.setButton(DialogInterface.BUTTON_POSITIVE, "是", checkyes);
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "否", checkNO);
            isExit.show();
        }
        else
            finish();

    }

    private DialogInterface.OnClickListener backcheckyes =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            saveLevelData();
            finish();
        }
    };
    private DialogInterface.OnClickListener checkNO =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
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