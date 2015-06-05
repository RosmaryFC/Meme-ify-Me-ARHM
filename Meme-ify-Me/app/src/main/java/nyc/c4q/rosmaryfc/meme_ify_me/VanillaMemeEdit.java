package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;







public class VanillaMemeEdit extends ActionBarActivity {
    private Uri imageUri;
    private TextView topTextView;
    private TextView midTextView;
    private TextView btmTextView;
    private EditText topEditText;
    private EditText midEditText;
    private EditText btmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme_edit);

        //Drawable myIcon = getResources().getDrawable( R.drawable.);

        Button topEditTxtPreviewBtn = (Button) findViewById(R.id.top_editText_preview_btn);
        topEditTxtPreviewBtn.setOnClickListener(topPreviewBtnListener);

        Button midEditTxtPreviewBtn = (Button) findViewById(R.id.mid_editText_preview_btn);
        midEditTxtPreviewBtn.setOnClickListener(midPreviewBtnListener);

        Button btmEditTxtPreviewBtn = (Button) findViewById(R.id.btm_editText_preview_btn);
        btmEditTxtPreviewBtn.setOnClickListener(btmPreviewBtnListener);

    }

    private View.OnClickListener topPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            topEditText = (EditText) findViewById(R.id.top_editText);
            topTextView = (TextView) findViewById(R.id.top_textView);
            topTextView.setText(topEditText.getText().toString());
        }
    };

    private View.OnClickListener midPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            midEditText = (EditText) findViewById(R.id.mid_editText);
            midTextView = (TextView) findViewById(R.id.mid_textView);
            midTextView.setText(midEditText.getText().toString());
        }
    };

    private View.OnClickListener btmPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btmEditText = (EditText) findViewById(R.id.btm_editText);
            btmTextView = (TextView) findViewById(R.id.btm_textView);
            btmTextView.setText(btmEditText.getText().toString());
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vanilla_meme_edit, menu);
        return true;
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
                intent.putExtra(Intent.EXTRA_STREAM, imageUri); //need to update this so that we are sending the final meme, not the image.
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
}
