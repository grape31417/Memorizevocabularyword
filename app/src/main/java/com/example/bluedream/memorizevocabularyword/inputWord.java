package com.example.bluedream.memorizevocabularyword;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_word);
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
                    mWorddb.insert(DB_TABLE, null, newRow);
                }
                mWorddb.close();
                finish();


        }
    };
}
