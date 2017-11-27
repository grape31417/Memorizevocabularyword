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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;

public class showWord extends AppCompatActivity {
    private SQLiteDatabase mWorddb;
    private EditText mEdtCht, mEdtEng, mEdtList;
    private Button mBtnQuery, mBtnList, mBtnExitShowWord, mBtnDeleteDb,mBtnDeleteChoseWord;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActBarDrawerToggle;
    private Spinner mSpinerWordList;
    private ArrayList<String> spinerlist= new ArrayList();
    private ArrayList<Integer> srrid = new ArrayList();
    private final static String _ID = "_id";
    int choseid,deletemode;

    private void iniz()
    {
        AppUtils.getInstance().addActivity(this);

        mEdtCht = (EditText)findViewById(R.id.edtName);
        mEdtEng = (EditText)findViewById(R.id.edtSex);
        mEdtList = (EditText)findViewById(R.id.edtList);

        mBtnQuery = (Button)findViewById(R.id.btnQuery);
        mBtnList = (Button)findViewById(R.id.btnList);
        mBtnExitShowWord=(Button)findViewById(R.id.exit);
        mBtnDeleteDb =(Button)findViewById(R.id.btnDeleteAll) ;
        mBtnDeleteChoseWord=(Button)findViewById(R.id.btnDleteChoseWord);
        mSpinerWordList=(Spinner)findViewById(R.id.spinnerwordlist);

        mBtnQuery.setOnClickListener(btnQueryOnClick);
        mBtnList.setOnClickListener(btnListOnClick);
        mBtnExitShowWord.setOnClickListener(btnInputExit);
        mBtnDeleteDb.setOnClickListener(btnClickDelete);
        mBtnDeleteChoseWord.setOnClickListener(btnDleteChoseWord);
        mSpinerWordList.setOnItemSelectedListener(spinerlistChose);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        DatabaseControler Database = new DatabaseControler(this.getApplicationContext());
        mWorddb=Database.OpenDatabase();
        Cursor c = Database.query(true,true,true,true);
        c.close();
        iniz();

        setActionBar();
    }

    private void setSpinerlist ()
    {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, spinerlist); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinerWordList.setAdapter(spinnerArrayAdapter);
    }




    private View.OnClickListener btnQueryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Query();
        }
    };
    private void Query() {
        spinerlist.clear();
        srrid.clear();
        Cursor c = null;
        if (!mEdtCht.getText().toString().equals("")) {
            c = mWorddb.query(true, DB_TABLE, new String[]{"Cht", "Eng","_id"}, "Cht=" + "\"" + mEdtCht.getText().toString()
                    + "\"", null, null, null, null, null);
        } else if (!mEdtEng.getText().toString().equals("")) {
            c = mWorddb.query(true, DB_TABLE, new String[]{"Cht", "Eng","_id"}, "Eng=" + "\"" + mEdtEng.getText().toString()
                    + "\"", null, null, null, null, null);
        }
        if (c == null) return;

        if (c.getCount() == 0) {
            mEdtList.setText("");
            Toast.makeText(showWord.this, "沒有這筆資料", Toast.LENGTH_LONG).show();
        } else {
            c.moveToFirst();
            mEdtList.setText(c.getString(0) + c.getString(1));
            spinerlist.add(c.getString(0) + c.getString(1));
            srrid.add(c.getInt(2));
            while (c.moveToNext())
                mEdtList.append("\n" + c.getString(0) + c.getString(1));
        }
        c.close();
        deletemode=2;
        setSpinerlist();
    }

    private View.OnClickListener btnListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ListOn();
        }
    };
    private void ListOn ()
    {
        spinerlist.clear();
        srrid.clear();
        deletemode=1;

        Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","Level","_id"}, null, null, null, null, null, null);
        //Cursor c =Database.query(false,true,true,true);
        if (c == null)
            return;

        if (c.getCount() == 0) {
            mEdtList.setText("");
            Toast.makeText(showWord.this, "沒有資料", Toast.LENGTH_LONG).show();
        }
        else {
            String s="";
            c.moveToFirst();
            if(c.getInt(2)==3) s="  已背誦";
            if(c.getInt(2)!=3) s="  未背誦";
            spinerlist.add(c.getString(0) + c.getString(1) + s);
            srrid.add(c.getInt(3));
            mEdtList.setText(c.getString(0) + c.getString(1)+s);

            while (c.moveToNext()) {
                if (c.getInt(2) == 3) s = "  已背誦";
                if (c.getInt(2) != 3) s = "  未背誦";
                spinerlist.add(c.getString(0) + c.getString(1) + s);
                srrid.add(c.getInt(3));
                mEdtList.append("\n" + c.getString(0) + c.getString(1) + s);
            }
        }
        setSpinerlist();
        c.close();
    }

    private View.OnClickListener btnClickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setCheckdialog ();
        }
    };
    private void setCheckdialog ()
    {
        MyAlertDialog check = new MyAlertDialog(showWord.this);
        check.setTitle("刪除");
        check.setMessage("確定刪除所有單字數據嗎?");
        check.setIcon(android.R.drawable.ic_dialog_alert);
        check.setCancelable(false);
        check.setButton(DialogInterface.BUTTON_POSITIVE, "是",checkyes);
        check.setButton(DialogInterface.BUTTON_NEGATIVE,"否", checkno);
        check.show();
    }

    private DialogInterface.OnClickListener checkyes =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            deletedatabaseCheckYes ();
        }
    };
    private void deletedatabaseCheckYes ()
    {
        mEdtList.setText("");
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE;
        mWorddb.execSQL(DROP_TABLE);
        Toast.makeText(showWord.this,"已刪除所有資料",Toast.LENGTH_LONG).show();
    }

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

    private View.OnClickListener btnDleteChoseWord =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (deletemode) {
                case 0:
                    Toast.makeText(showWord.this,"無選取單字",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    mWorddb.delete(DB_TABLE, _ID + "=" + choseid, null);
                    Toast.makeText(showWord.this,"已刪除選取單字",Toast.LENGTH_LONG).show();
                    ListOn();
                    setSpinerlist();
                    break;
                case 2:
                    mWorddb.delete(DB_TABLE, _ID + "=" + choseid, null);
                    Toast.makeText(showWord.this,"已刪除選取單字",Toast.LENGTH_LONG).show();
                    mEdtList.setText("");
                    ListOn();
                    setSpinerlist();
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
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            SlectMenuList(((TextView)view).getText().toString());
            mDrawerLayout.closeDrawers();
                finish();
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

    private AdapterView.OnItemSelectedListener spinerlistChose =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            choseid=srrid.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            deletemode=0;
        }

    };

}
