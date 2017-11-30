package com.example.bluedream.memorizevocabularyword;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by USER on 2017/11/23.
 */

public class wordAdapter extends RecyclerView.Adapter<wordAdapter.ViewHolder> {

    // 儲存要顯示的資料。
    private List<String> mListStringEng;
    private List<String> mListStringCht;

    // ViewHolder 是把項目中所有的 View 物件包起來。
    // 它在 onCreateViewHolder() 中使用。
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView mImgView;
        public TextView mTxt;
        public LinearLayout mViewCard;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgView = (ImageView) itemView.findViewById(R.id.imgView);
            mTxt = (TextView) itemView.findViewById(R.id.txt);
            mViewCard=(LinearLayout)itemView.findViewById(R.id.viewcard);


            // 處理按下的事件。
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            testWord.iRan=getPosition();
            testWord.ChoseWord = mListStringEng.get(testWord.iRan).toString();
            testWord.Question.setText(mListStringCht.get(testWord.iRan).toString());

        }
    }


    // 建構式，用來接收外部程式傳入的項目資料。
    public wordAdapter(List<String> ChtlistString ,List<String> EnglistString) {
        mListStringCht = ChtlistString;
        mListStringEng = EnglistString;
    }

    @Override
    public wordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 建立一個 view。
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_view_item, parent, false);

        // 建立這個 view 的 ViewHolder。
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(wordAdapter.ViewHolder holder, int position) {
        // 把資料設定給 ViewHolder。
        holder.mImgView.setImageResource(android.R.drawable.star_on);
        holder.mTxt.setText(mListStringCht.get(position)+"  ");
    }

    @Override
    public int getItemCount() {
        return mListStringEng.size();
    }

}
