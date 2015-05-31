package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onShareClick(View v){
               List<Intent> targetShareIntents=new ArrayList<Intent>();
                Intent shareIntent=new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);
                boolean intentSafe = resInfos.size() > 0;
                if(intentSafe){

                                for(ResolveInfo resInfo : resInfos){
                                String packageName=resInfo.activityInfo.packageName;
                                Log.i("Package Name", packageName);

                                            Intent intent=new Intent();
                                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.setType("image/jpg");
                                  //reference hoshiko's code
                                    intent.putExtra(Intent.EXTRA_STREAM, " imageUri "); //need to update this so that we are using variable imageUri not the name (as per hoshiko's code)

                                   // maybe convert imageUri + userinputted text as a Bitmap.     bmp = Bitmap.createBitmap(imageUri);

                                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Made with Meme-ify Me");
                                    intent.putExtra(Intent.EXTRA_TEXT, "Check out my new meme!");

                                            intent.setPackage(packageName);
                                    targetShareIntents.add(intent);
                            }
                        Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                        startActivity(chooserIntent);
                    } else {
                        return;
                    }
           }
}
