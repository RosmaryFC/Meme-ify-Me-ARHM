package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by c4q-ac35 on 7/15/15.
 */
public class MemeList extends Activity {

    @Bind(R.id.listView) ListView mListView;
    private DatabaseHelper mHelper;
    private MemeAdapter mAdapter;
    public List<Meme> mList;
    public Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_list);

        ButterKnife.bind(this);

        mAdapter = new MemeAdapter(getApplicationContext(), mList);

        mListView.setAdapter(mAdapter);

    }

    private class DatabaseTask extends AsyncTask<Void,Void,List<Meme>>{
        @Override
        protected List<Meme> doInBackground(Void... params) {

            mHelper = DatabaseHelper.getInstance(getApplicationContext());

            try {
                if(mHelper.loadData().size()==0){
                    mHelper.insertRow(R.drawable.but_thats_none_of_my_business,"But that's none of my business");
                    mHelper.insertRow(R.drawable.actual_advice_mallard,"Actual advice Mallard");
                    mHelper.insertRow(R.drawable.creepy_condescending_wonka,"Condescending Wonka");
                    mHelper.insertRow(R.drawable.futurama_fry,"Futurama Fry");
                    mHelper.insertRow(R.drawable.good_guy_greg,"Good guy Greg");
                    mHelper.insertRow(R.drawable.liam_neeson_taken,"Taken");
                    mHelper.insertRow(R.drawable.one_does_not_simply,"One does not simply");
                    mHelper.insertRow(R.drawable.scumbag_steve,"Scumbag Steve");
                    mHelper.insertRow(R.drawable.shut_up_and_take_my_money_fry,"Shut up and take my money");
                    mHelper.insertRow(R.drawable.ten_guy,"Ten Guy");
                    mHelper.insertRow(R.drawable.the_most_interesting_man_in_the_world,"Most Interesting man");
                    mHelper.insertRow(R.drawable.third_world_skeptical_kid,"Skeptical kid");
                    mHelper.insertRow(R.drawable.unhelpful_high_school_teacher,"Unhelpful teaher");
                    mHelper.insertRow(R.drawable.yao_ming,"Yao Ming");
                    mHelper.insertRow(R.drawable.you_the_real_mvp,"Real MVP");
                }
                return mHelper.loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Meme> memes) {
            mAdapter = new MemeAdapter(MemeList.this,memes);
            mListView.setAdapter(mAdapter);
        }
    }


}
