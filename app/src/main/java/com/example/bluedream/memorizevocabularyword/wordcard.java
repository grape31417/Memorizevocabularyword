package com.example.bluedream.memorizevocabularyword;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class wordcard extends Fragment {


    public wordcard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_wordcard, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getActivity().getApplicationContext(), DB_FILE, null, 1);
        SQLiteDatabase mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

        Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","_id","Level"}, 	null, null, null, null, null, null);
        int rowCount =c.getCount();

        List<String> listStrEng = new ArrayList<>();
        List<String> listStrCht = new ArrayList<>();

        c.moveToFirst();
        for (int i = 1; i <= rowCount; i++) {
            if(c.getInt(3)!=3) {
                listStrCht.add(c.getString(0));
                listStrEng.add(c.getString(1));
                c.moveToNext();
            }
            else c.moveToNext();
        }
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        wordAdapter itemAdapter = new wordAdapter(listStrCht,listStrCht);
        recyclerView.setAdapter(itemAdapter);

        c.close();
        mWorddb.close();
    }


}
