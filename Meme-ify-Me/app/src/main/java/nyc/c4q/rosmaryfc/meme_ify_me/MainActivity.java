package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ComponentName;
import android.content.ContentResolver;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static String logtag ="CameraApp8";
    private static int TAKE_PICTURE =1;
    private static final int PICK_PICTURE = 2;
    private Uri imageUri;
    ImageView imageview;
    private String selectedImagePath;
    private Button editMemeButton;
    private RadioButton vanillaRadioButton;
    private RadioButton demotivationalRadBtn;
    private Intent vanillaMemeIntent;
    private Intent demotivationalMemeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageview = (ImageView)findViewById(R.id.image);
        Button cameraButton = (Button)findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);
        Button fromGalleryButton = (Button) findViewById(R.id.pic_from_gallery_button);
        fromGalleryButton.setOnClickListener(galleryListener);


        vanillaRadioButton = (RadioButton) findViewById(R.id.vanilla_memes_radBtn);
        demotivationalRadBtn = (RadioButton) findViewById(R.id.demotivational_posters_radBtn);

        vanillaMemeIntent = new Intent(this, VanillaMemeEdit.class);
        demotivationalMemeIntent = new Intent(this, DemotivationalMemeEdit.class);

        editMemeButton = (Button)findViewById(R.id.edit_meme_button);
        editMemeButton.setOnClickListener(editMemeListener);

    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private View.OnClickListener galleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickPhoto(v);
        }
    };

    //method for requesting image from gallery/camera roll
    private void pickPhoto(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PICTURE);

    }
    //method for requesting camera to capture image and save it under a new file
    private void takePhoto (View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    //method for gathering intent information from takePhoto and pickPhoto methods
    // and setting the imageview with correct bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK ){
                if (requestCode == PICK_PICTURE){
                    selectedImagePath = String.valueOf(data.getData());
                    imageview.setImageBitmap(decodePhoto(selectedImagePath));
                } else if (requestCode == TAKE_PICTURE){
                    selectedImagePath = imageUri.toString();
                    imageview.setImageBitmap(decodePhoto(selectedImagePath));
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
        }
    }

    //requesting image's file path and converting into url and calling the
    // ContentResolver to retrieve image and set it inside a bitmap
    public Bitmap decodePhoto (String path) {
        Uri selectedImageUri = Uri.parse(selectedImagePath);
        getContentResolver().notifyChange(selectedImageUri, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImageUri);
            //show image file path to user
            Toast.makeText(MainActivity.this, selectedImageUri.toString(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e(logtag, e.toString());
        }
        return bitmap;
    }

    private View.OnClickListener editMemeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(vanillaRadioButton.isChecked()){
                startActivity(vanillaMemeIntent);
            }else if(demotivationalRadBtn.isChecked()) {
                startActivity(demotivationalMemeIntent);
            } else {
                Toast.makeText(getApplicationContext(),"Select a Meme Type", Toast.LENGTH_SHORT).show();
            }
        }
    };

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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.vanilla_memes_radBtn:
                if (checked)
                    // load vanilla_memes layout
                //todo: this is where code will go to change sample image to sample vanilla meme image
                    break;
            case R.id.demotivational_posters_radBtn:
                if (checked)
                    // load demotivational_posters layout
                    //todo: this is where code will go to change sample image to sample demotivational poster image
                    break;
        }
    }


    public void saveMeme(View v){
//        Intent intent = new Intent(MainActivity.this, MemeHandler.class);
//        startActivity(intent);
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

    public  void exportMeme(View v){

    }


}
