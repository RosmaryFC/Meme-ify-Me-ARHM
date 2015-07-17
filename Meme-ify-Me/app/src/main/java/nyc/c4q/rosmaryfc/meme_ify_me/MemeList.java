package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

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
    private List<Meme> mList;
    private Uri uri;

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
            mListView = (ListView) findViewById(R.id.listView);


            return null;
        }
    }


}
