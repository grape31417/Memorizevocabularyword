package com.example.bluedream.memorizevocabularyword;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import java.util.ArrayList;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class testWordSetMode extends AppCompatActivity {
    Button mBtnSetModeBack,mbtnEnterTestMode;
    int maxtestCount,mode,i;
    RadioGroup testMode;
    public SQLiteDatabase mWorddb;


    private ArrayList<String> srrCht= new ArrayList();
    private ArrayList<String> srrEng= new ArrayList();
    private ArrayList<Integer> id= new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word_set_mode);

        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();



        mbtnEnterTestMode=(Button)findViewById(R.id.btnEnterTestMode);
        mBtnSetModeBack=(Button)findViewById(R.id.btnSetModeBack);
        testMode=(RadioGroup)findViewById(R.id.rgTestmode);


        mBtnSetModeBack.setOnClickListener(returntitle);
        mbtnEnterTestMode.setOnClickListener(GOTest);
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
            Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","_id",}, 	null, null, null, null, null, null);
            int rowCount =c.getCount();
            switch (testMode.getCheckedRadioButtonId())
            {
                case R.id.rbbNotLearn:
                    mode=1;
                    c.moveToFirst();
                    for (int i=1;i<=rowCount;i++) {
                        srrCht.add(c.getString(0));
                        srrEng.add(c.getString(1));
                        id.add(c.getInt(2));
                        c.moveToNext();
                    }
                    break;
                case  R.id.rbRandom :
                    mode=2;
                    c.moveToFirst();
                    for (int i=1;i<=rowCount;i++) {
                        //int level =c.getInt(2);
                        //if(level==0||level==1) {
                        srrCht.add(c.getString(0));
                        srrEng.add(c.getString(1));
                        id.add(c.getInt(2));
                        // }
                        c.moveToNext();
                    }
                    break;
            }
            c.close();

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("srrCht",srrCht);
            bundle.putStringArrayList("srrEng",srrEng);
            bundle.putIntegerArrayList("id",id);
            bundle.putInt("mode",mode);
            Intent it =new Intent();
            it.setClass(testWordSetMode.this,testWord.class);
            it.putExtras(bundle);
            startActivity(it);
            mWorddb.close();


            finish();
        }
    };




}
