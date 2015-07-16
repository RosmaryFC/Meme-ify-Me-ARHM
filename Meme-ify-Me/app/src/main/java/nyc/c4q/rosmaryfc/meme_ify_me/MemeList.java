package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by c4q-ac35 on 7/15/15.
 */
public class MemeList extends Activity {

    ListView listView;
    HashMap<Integer, String> memePairs;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_list);

        listView = (ListView) findViewById(R.id.listView);

        memePairs = new HashMap<>();
        memePairs.put(R.drawable.actual_advice_mallard, "Actual Advice Mallard");
        memePairs.put(R.drawable.but_thats_none_of_my_business, "But That's None Of My Business");
        memePairs.put(R.drawable.creepy_condescending_wonka, "Creepy Condescending Wonka");
        memePairs.put(R.drawable.futurama_fry, "Skeptical Fry");
        memePairs.put(R.drawable.good_guy_greg, "Good Guy Greg");
        memePairs.put(R.drawable.liam_neeson_taken, "Liam Neeson Taken");
        memePairs.put(R.drawable.one_does_not_simply, "One Does Not Simply");
        memePairs.put(R.drawable.scumbag_steve, "Scumbag Steve");
        memePairs.put(R.drawable.shut_up_and_take_my_money_fry, "Shut Up And Take My Money");
        memePairs.put(R.drawable.ten_guy, "Ten Guy");
        memePairs.put(R.drawable.the_most_interesting_man_in_the_world, "The Most Interesting Man In The World");
        memePairs.put(R.drawable.third_world_skeptical_kid, "Third World Skeptical Kid");
        memePairs.put(R.drawable.unhelpful_high_school_teacher, "Unhelpful High School Teacher");
        memePairs.put(R.drawable.yao_ming, "Yao Ming");
        memePairs.put(R.drawable.you_the_real_mvp, "You The Real MVP");


        final ArrayList<Integer> memeImages = new ArrayList<Integer>();
        ArrayList<String> memeNames = new ArrayList<String>();
        addItemsToArrays(memeImages, memeNames);

        CustomArrayAdapter memeAdapter = new CustomArrayAdapter(getApplicationContext(), memeNames, memeImages);

        listView.setAdapter(memeAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MemeList.this, EditPhoto.class);
//
//                uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(memeImages.get(position)) + '/' + getResources().getResourceTypeName(memeImages.get(position)) + '/' + getResources().getResourceEntryName(memeImages.get(position)));
//
//                intent.putExtra("image", uri);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Please select VANILLA or DEMO layout to begin", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });

    }

    public void addItemsToArrays(ArrayList<Integer> images, ArrayList<String> titles) {
        int position = 0;
        for (Integer image : memePairs.keySet()) {

            images.add(position, image);
            titles.add(position, memePairs.get(image));
            position++;
        }
    }

}
