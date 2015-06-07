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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DemotivationalMemeEdit extends ActionBarActivity {
    private static String logtag = "CameraApp";
    private Uri imageUri;
    private TextView titleTextView;
    private TextView phraseTextView;
    private EditText titleEditText;
    private EditText phraseEditText;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme_edit);

        //setting Image from filepath to ImageView source
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("SelectedImagePath");
        if(imagePath == null){
            Log.d("Error","imagePath is null" );
        }
        bmp = decodePhoto(this, imagePath);

        ImageView posterImageView = (ImageView) findViewById(R.id.imageView_for_poster);
        posterImageView.setImageBitmap(bmp);

        Button titleEditTxtPreviewBtn = (Button) findViewById(R.id.title_editText_preview_btn);
        titleEditTxtPreviewBtn.setOnClickListener(titlePreviewBtnListener);

        Button phraseEditTxtPreviewBtn = (Button) findViewById(R.id.phrase_editText_preview_btn);
        phraseEditTxtPreviewBtn.setOnClickListener(phrasePreviewBtnListener);

    }

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

    public void onShareClick(View v) {
        imageUri = saveDemotivationalMeme(v);
        Toast.makeText(getApplicationContext(), "Preparing to share :" + imageUri.toString(), Toast.LENGTH_LONG).show();
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);

        boolean intentSafe = resInfos.size() > 0;
        if (intentSafe) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Made with Meme-ify Me");
                intent.putExtra(Intent.EXTRA_TEXT, "Check out my new meme!");
                intent.setPackage(packageName);
                targetShareIntents.add(intent);
            }
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
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

    public Uri saveDemotivationalMeme (View v) {
        Bitmap meme = drawMeme(v);
        try {
            meme.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       if (Environment.getExternalStorageDirectory() != null) {
            File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "memeFile.jpg");
            imageUri = Uri.fromFile(photo);
            Toast.makeText(getApplicationContext(), "File saved to: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        } else {
            File photo = new File(Environment.getRootDirectory(), "memeFile.jpg");
            imageUri = Uri.fromFile(photo);
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        meme.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File photo = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            photo.createNewFile();
            FileOutputStream fo = new FileOutputStream(photo);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUri = Uri.fromFile(photo);
        MediaStore.Images.Media.insertImage(getContentResolver(), meme, "Meme _", "New meme");
        return imageUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demotivational_meme_edit, menu);
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
}
