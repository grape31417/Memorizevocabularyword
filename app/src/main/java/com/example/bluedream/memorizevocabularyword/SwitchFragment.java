package com.example.bluedream.memorizevocabularyword;

/**
 * Created by USER on 2017/11/24.
 */

public class SwitchFragment {

    private android.support.v4.app.FragmentTransaction transaction;
    public SwitchFragment(int rowCount,android.support.v4.app.FragmentManager manager) {
        transaction = manager.beginTransaction();
        noword noword =new noword();
        allwordcard allword =new allwordcard();
        wordcard wordcard = new wordcard();

        if(rowCount==0)
        {
            transaction.replace(R.id.frameLay, noword, "noword");
            //transaction.addToBackStack("noword");
        }

        if(rowCount==-1)
            transaction.replace(R.id.frameLay, allword, "allword");


        if (rowCount!=0)
        {
            transaction.replace(R.id.frameLay, wordcard, "wordcard");
            //transaction.addToBackStack("allword");
        }
        transaction.commit();
    }
}
