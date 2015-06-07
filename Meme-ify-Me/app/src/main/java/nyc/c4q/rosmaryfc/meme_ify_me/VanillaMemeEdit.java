package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VanillaMemeEdit extends ActionBarActivity {
    private Uri imageUri;
    private static String logtag = "CameraApp";
    private TextView topTextView;
    private TextView midTextView;
    private TextView btmTextView;
    private EditText topEditText;
    private EditText midEditText;
    private EditText btmEditText;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme_edit);
        imageUri = getIntent().getData();

        //setting Image from filepath to ImageView source
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("SelectedImagePath");
        if(imagePath == null){
            Log.d("Error","imagePath is null" );
        }
        Bitmap bmp = decodePhoto(this, imagePath);

        ImageView imageForMeme = (ImageView) findViewById(R.id.image_for_meme);
        imageForMeme.setImageBitmap(bmp);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

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

            imm.hideSoftInputFromWindow(topEditText.getWindowToken(), 0);

        }
    };

    private View.OnClickListener midPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            midEditText = (EditText) findViewById(R.id.mid_editText);
            midTextView = (TextView) findViewById(R.id.mid_textView);
            midTextView.setText(midEditText.getText().toString());

            imm.hideSoftInputFromWindow(midEditText.getWindowToken(), 0);

        }
    };

    private View.OnClickListener btmPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btmEditText = (EditText) findViewById(R.id.btm_editText);
            btmTextView = (TextView) findViewById(R.id.btm_textView);
            btmTextView.setText(btmEditText.getText().toString());

            imm.hideSoftInputFromWindow(btmEditText.getWindowToken(), 0);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vanilla_meme_edit, menu);
        return true;
    }

    //requesting image's file path and converting into url and calling the
    // ContentResolver to retrieve image and set it inside a bitmap
    public Bitmap decodePhoto(Context context, String path) {
        Uri selectedImageUri = Uri.parse(path);
        getContentResolver().notifyChange(selectedImageUri, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmapImage = null;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(cr, selectedImageUri);
            //show image file path to user
            Toast.makeText(context, selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(logtag, e.toString());
        }
        return bitmapImage;
    }

    public void onShareClick(View v){
        Bitmap meme = drawMeme(v);
        try {
            meme.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeFile.jpg");
        imageUri = Uri.fromFile(photo);
        Toast.makeText(getApplicationContext(), "Preparing to share :" + imageUri.toString(), Toast.LENGTH_LONG).show();
        List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);

        boolean intentSafe = resInfos.size() > 0;
        if(intentSafe){

            for(ResolveInfo resInfo : resInfos){
                String packageName=resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);

                Intent intent=new Intent();
                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
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

    public Bitmap drawMeme(View v){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.meme_preview_relativeLayout);
        layout.setDrawingCacheEnabled(true);
        Bitmap memeBitMap = layout.getDrawingCache();
        Bitmap meme = memeBitMap.copy(Bitmap.Config.ARGB_8888, false);
        layout.buildDrawingCache();
        layout.destroyDrawingCache();
        return meme;
    }

    public File saveVanillaMeme (View v) {
        Bitmap returnedBitmap = drawMeme(v);
        //File photo;
        try {
            returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream("memeFile.jpg"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeFile.jpg");
        imageUri = Uri.fromFile(photo);

        Toast.makeText(getApplicationContext(), "File saved to: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        MediaStore.Images.Media.insertImage(getContentResolver(), returnedBitmap, "Meme _", "New meme");
        return photo;
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
