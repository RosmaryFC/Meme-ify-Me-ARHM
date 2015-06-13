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
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "VanillaMemeEdit.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme_edit);

        imageUri = getIntent().getData();

        //setting Image from filepath to ImageView source
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("SelectedImagePath");
        if(imagePath == null){
            Log.d(MainActivity.TAG,"imagePath is null" );

        }
        Bitmap bmp = decodePhoto(this, imagePath);
        ImageView imageForMeme = (ImageView) findViewById(R.id.image_for_meme);
        imageForMeme.setImageBitmap(bmp);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        topEditText = (EditText) findViewById(R.id.top_editText);
        topTextView = (TextView) findViewById(R.id.top_textView);
        topTextView.setText(topEditText.getText().toString());

        midEditText = (EditText) findViewById(R.id.mid_editText);
        midTextView = (TextView) findViewById(R.id.mid_textView);
        midTextView.setText(midEditText.getText().toString());

        btmEditText = (EditText) findViewById(R.id.btm_editText);
        btmTextView = (TextView) findViewById(R.id.btm_textView);
        btmTextView.setText(btmEditText.getText().toString());

//TODO: what's up with the underlining of text?? low priority

        //Just like I did in DemotivationalMemeEdit.java, I added these next 3 methods so I could get rid of
        // the preview buttons--letting user see their changes immediately and also decluttering the UI a bit.
        topEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                topTextView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        midEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                midTextView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        btmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btmTextView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private View.OnClickListener topPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imm.hideSoftInputFromWindow(topEditText.getWindowToken(), 0);

        }
    };

    private View.OnClickListener midPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imm.hideSoftInputFromWindow(midEditText.getWindowToken(), 0);

        }
    };

    private View.OnClickListener btmPreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
        Log.d(MainActivity.TAG, "VanillaMemeEdit.decodePhoto()");
        Uri selectedImageUri = Uri.parse(path);
        getContentResolver().notifyChange(selectedImageUri, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmapImage = null;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(cr, selectedImageUri);
            //show image file path to user
            Toast.makeText(context, selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(MainActivity.TAG, e.toString());
        }
        return bitmapImage;
    }

    public void onShareClick(View v) {
        Log.d(MainActivity.TAG, "VanillaMemeEdit.onShareClick()");
        imageUri = saveVanillaMeme(v);
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
                Log.i(MainActivity.TAG, packageName);
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
        Log.d(MainActivity.TAG, "VanillaMemeEdit.drawMeme()");
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.meme_preview_relativeLayout);
        layout.setDrawingCacheEnabled(true);
        Bitmap memeBitMap = layout.getDrawingCache();
        Bitmap meme = memeBitMap.copy(Bitmap.Config.ARGB_8888, false);
        layout.buildDrawingCache();
        layout.destroyDrawingCache();
        return meme;
    }

    public Uri saveVanillaMeme (View v) {
        Log.d(MainActivity.TAG, "VanillaMemeEdit.saveVanillaMeme()");
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
