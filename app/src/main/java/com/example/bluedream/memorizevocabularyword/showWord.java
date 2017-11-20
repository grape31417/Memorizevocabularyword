package com.example.bluedream.memorizevocabularyword;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class showWord extends AppCompatActivity {
    private SQLiteDatabase mWorddb;
    private EditText mEdtCht,
            mEdtEng,
            mEdtList;

    private Button
            mBtnQuery,
            mBtnList,
            mBtnExitShowWord,
            mBtnDeleteDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);

        mEdtCht = (EditText)findViewById(R.id.edtName);
        mEdtEng = (EditText)findViewById(R.id.edtSex);
        mEdtList = (EditText)findViewById(R.id.edtList);

        mBtnQuery = (Button)findViewById(R.id.btnQuery);
        mBtnList = (Button)findViewById(R.id.btnList);
        mBtnExitShowWord=(Button)findViewById(R.id.exit);
        mBtnDeleteDb =(Button)findViewById(R.id.btnDeleteAll) ;

        mBtnQuery.setOnClickListener(btnQueryOnClick);
        mBtnList.setOnClickListener(btnListOnClick);
        mBtnExitShowWord.setOnClickListener(btnInputExit);
        mBtnDeleteDb.setOnClickListener(btnClickDelete);

        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();
    }


    private View.OnClickListener btnQueryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor c = null;

            if (!mEdtCht.getText().toString().equals("")) {
                c = mWorddb.query(true, DB_TABLE, new String[]{"Cht", "Eng"}, "Cht=" + "\"" + mEdtCht.getText().toString()
                        + "\"", null, null, null, null, null);
            } else if (!mEdtEng.getText().toString().equals("")) {
                c = mWorddb.query(true, DB_TABLE, new String[]{"Cht", "Eng",}, "Eng=" + "\"" + mEdtEng.getText().toString()
                        + "\"", null, null, null, null, null);

            }

            if (c == null)
                return;

            if (c.getCount() == 0) {
                mEdtList.setText("");
                Toast.makeText(showWord.this, "沒有這筆資料", Toast.LENGTH_LONG)
                        .show();
            } else {
                c.moveToFirst();
                mEdtList.setText(c.getString(0) + c.getString(1));

                while (c.moveToNext())
                    mEdtList.append("\n" + c.getString(0) + c.getString(1) );
            }
        }
    };

    private View.OnClickListener btnListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht", "Eng",}, 	null, null, null, null, null, null);

            if (c == null)
                return;

            if (c.getCount() == 0) {
                mEdtList.setText("");
                Toast.makeText(showWord.this, "沒有資料", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                c.moveToFirst();
                mEdtList.setText(c.getString(0) + c.getString(1));

                while (c.moveToNext())
                    mEdtList.append("\n" + c.getString(0) + c.getString(1) );
            }
        }
    };

    private View.OnClickListener btnClickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyAlertDialog check = new MyAlertDialog(showWord.this);
            check.setTitle("刪除");
            check.setMessage("確定刪除所有單字數據嗎?");
            check.setIcon(android.R.drawable.ic_dialog_alert);
            check.setCancelable(false);
            check.setButton(DialogInterface.BUTTON_POSITIVE, "是",checkyes);
            check.setButton(DialogInterface.BUTTON_NEGATIVE,"否", checkno);
            check.show();

        }
    };

    private DialogInterface.OnClickListener checkyes =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mEdtList.setText("");

            final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE;
            mWorddb.execSQL(DROP_TABLE);

            Toast.makeText(showWord.this,"已刪除所有資料",Toast.LENGTH_LONG).show();

            Cursor cursor = mWorddb.rawQuery(
                    "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                            DB_TABLE + "'", null);

            if(cursor != null) {
                if (cursor.getCount() == 0)
                    mWorddb.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                            "_id INTEGER PRIMARY KEY," +
                            "Cht TEXT NOT NULL," +
                            "Eng TEXT);" );
                cursor.close();
            }

        }
    };

    private DialogInterface.OnClickListener checkno = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    };

    private  View.OnClickListener btnInputExit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mWorddb.close();
            finish();
        }
    };
}
