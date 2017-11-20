package com.example.bluedream.memorizevocabularyword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class testWord extends AppCompatActivity {
    private Button mEnter,mExit,mTip;
    private ArrayList<String> srrCht= new ArrayList();
    private ArrayList<String> srrEng= new ArrayList();
    private ArrayList<Integer> id= new ArrayList();
    TextView Question,Result;
    EditText Answer;
    String ChoseWord;
    int iRan,WordsSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);
        Intent it =getIntent();
        mEnter=(Button)findViewById(R.id.btnEnter);
        mExit=(Button)findViewById(R.id.btnExit);
        mTip=(Button)findViewById(R.id.btnTip);
        Question=(TextView)findViewById(R.id.ChtName) ;
        Result=(TextView)findViewById(R.id.Result);
        Answer=(EditText) findViewById(R.id.edtEng);
        Bundle bundle =it.getExtras();
        srrCht =bundle.getStringArrayList("srrCht");
        srrEng =bundle.getStringArrayList("srrEng");
        id=bundle.getIntegerArrayList("id");
        int mode =bundle.getInt("mode");
        WordsSize =srrCht.size();


        iRan=(int)(Math.random()*WordsSize);
        ChoseWord=srrEng.get(iRan).toString();
        Question.setText(srrCht.get(iRan).toString());

       // mTip.setOnClickListener(Tip);
        mExit.setOnClickListener(Exit);
       mEnter.setOnClickListener(Enter);
    }

    private View.OnClickListener Exit= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener Enter= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String InputAnswer= Answer.getText().toString();
            Answer.setText("");
            if(srrEng.get(iRan).equals(InputAnswer))
            {
                Result.setText("答對了");
            }
            else
            {
                Result.setText("答錯了 正解:"+srrEng.get(iRan));
            }
            iRan=(int)(Math.random()*WordsSize);
            ChoseWord=srrEng.get(iRan).toString();
            Question.setText(srrCht.get(iRan).toString());
        }
    };


}
