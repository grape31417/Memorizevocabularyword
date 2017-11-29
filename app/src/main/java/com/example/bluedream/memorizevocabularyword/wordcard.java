package com.example.bluedream.memorizevocabularyword;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_FILE;
import static com.example.bluedream.memorizevocabularyword.MainActivity.DB_TABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class wordcard extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    SwipeFlingAdapterView flingContainer;
    private int i;





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

        flingContainer=(SwipeFlingAdapterView)getActivity().findViewById(R.id.SwipeFlingAdapterView);

        SQLiteDatabaseOpenHelper SQLiteDatabaseOpenHelper =
                new SQLiteDatabaseOpenHelper(getActivity().getApplicationContext(), DB_FILE, null, 1);
        SQLiteDatabase mWorddb = SQLiteDatabaseOpenHelper.getWritableDatabase();

        Cursor c = mWorddb.query(true, DB_TABLE, new String[]{"Cht","Eng","_id","Level"}, 	null, null, null, null, null, null);
        int rowCount =c.getCount();
         List<String> inputlist = new ArrayList<>();


        c.moveToFirst();
        for (int i = 1; i <=rowCount ; i++) {
                inputlist.add(c.getString(0)+" "+c.getString(1));
                c.moveToNext();
            }
        final  ArrayList arraylist = new ArrayList(inputlist);;
         final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.wordcardlayout, R.id.helloText,arraylist);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                arraylist.add(arraylist.get(0));
                arraylist.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                flingContainer.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float v) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
            }
        });


        c.close();
        mWorddb.close();
    }


}
