package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DemotivationalMemeEdit extends ActionBarActivity {



    private Uri imageUri;
    private TextView titleTextView;
    private TextView phraseTextView;
    private EditText titleEditText;
    private EditText phraseEditText;


    private RelativeLayout layout;
    private Bitmap viewbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme_edit);

        Button titleEditTxtPreviewBtn = (Button) findViewById(R.id.title_editText_preview_btn);
        titleEditTxtPreviewBtn.setOnClickListener(titlePreviewBtnListener);

        Button phraseEditTxtPreviewBtn = (Button) findViewById(R.id.phrase_editText_preview_btn);
        phraseEditTxtPreviewBtn.setOnClickListener(phrasePreviewBtnListener);

        //Todo: some code for the save button that might be useful, is not working yet so left commented out
//        Button saveButton = (Button) findViewById(R.id.save_meme);
//        saveButton.setOnClickListener(saveBtnListener);

    }

//    private View.OnClickListener saveBtnListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            layout = (RelativeLayout) findViewById(R.id.meme_preview_relative_layout);
//            layout.setDrawingCacheEnabled(true);
//            layout.buildDrawingCache();
//            viewbitmap = layout.getDrawingCache(true);
//
//            File f = new File(getApplicationContext().getFilesDir(), "memeEdit"+"1"+".jpg");
//
//            try {
//                viewbitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f));
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    };

    private View.OnClickListener titlePreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titleEditText = (EditText) findViewById(R.id.title_editText);
            titleTextView = (TextView) findViewById(R.id.title_textView);
            titleTextView.setText(titleEditText.getText().toString());
        }
    };

    private View.OnClickListener phrasePreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            phraseEditText = (EditText) findViewById(R.id.phrase_editText);
            phraseTextView = (TextView) findViewById(R.id.phrase_textView);
            phraseTextView.setText(phraseEditText.getText().toString());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demotivational_meme_edit, menu);
        return true;
    }

    public void onShareClick(View v){
        //returnedBitmap = drawMeme(v);
        List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
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

                //intent.putExtra(Intent.EXTRA_SUBJECT, "Made with Meme-ify Me");
                //intent.putExtra(Intent.EXTRA_TEXT, "Check out my new meme!");

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

    public Bitmap drawMeme(View v){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.meme_preview_relative_layout);
        layout.setDrawingCacheEnabled(true);
        Bitmap memeBitMap = layout.getDrawingCache();
        Bitmap meme = memeBitMap.copy(Bitmap.Config.ARGB_8888, false);
        layout.buildDrawingCache();
        layout.destroyDrawingCache();
        return meme;
    }

    public void saveDemotivationalMeme (View v) {
        Bitmap meme = drawMeme(v);
        try {
            meme.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));



        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        String filename;
       if (Environment.getExternalStorageDirectory() != null) {

            File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeFile.jpg");
            imageUri = Uri.fromFile(photo);
            //Toast.makeText(DemotivationalMemeEdit.this, "File saved to :" + imageUri.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "File saved to: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        } else {
            File photo = new File(Environment.getRootDirectory(), "memeFile.jpg");
            imageUri = Uri.fromFile(photo);
        }

        //File f = new File("memeFile");
        MediaStore.Images.Media.insertImage(getContentResolver(), meme, "Meme _", "New meme");
        //return returnedBitmap;
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
