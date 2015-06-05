package nyc.c4q.rosmaryfc.meme_ify_me;


import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements IAdobeAuthClientCredentials {

    private static final int EDIT_PICTURE = 3;
    private static final int USE_EDITED_PICTURE = 4;

    private static String logtag = "CameraApp";
    private static int TAKE_PICTURE = 1;
    private static final int PICK_PICTURE = 2;
    private Uri imageUri;
    ImageView imageview;
    private String selectedImagePath;
    private Button editMemeButton;
    private RadioButton vanillaRadioButton;
    private RadioButton demotivationalRadBtn;
    private Intent vanillaMemeIntent;
    private Intent demotivationalMemeIntent;


//    private static int DEFAULT_SIGN_IN_REQUEST_CODE = 2002;

//    private final AdobeUXAuthManager _uxAuthManager = AdobeUXAuthManager.getSharedAuthManager();
//    private AdobeAuthSessionHelper _authSessionHelper = null;

//
//    private AdobeAuthSessionHelper.IAdobeAuthStatusCallback _statusCallback = new AdobeAuthSessionHelper.IAdobeAuthStatusCallback() {
//
//        @Override
//        public void call(AdobeAuthSessionHelper.AdobeAuthStatus status, AdobeAuthException exception) {
//            if ( AdobeAuthSessionHelper.AdobeAuthStatus.AdobeAuthLoggedIn == status ) {
//                //Show Logged In UI
//            }
//            else {
//                showNotLoggedInUI();
//            }
//        }
//    };
//
//    public void showNotLoggedInUI(){
//        _uxAuthManager.login(new AdobeAuthSessionLauncher.Builder().withActivity(this).withRequestCode(DEFAULT_SIGN_IN_REQUEST_CODE).build());
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Perform initialization operations for activity
        //Show the default UI for the Activity.
        //At this point the status of Creative Cloud Authentication is still not available.
        //_authSessionHelper = new AdobeAuthSessionHelper(_statusCallback);
        //_authSessionHelper.onCreate(savedInstanceState);


        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        Intent intent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(intent);


        imageview = (ImageView) findViewById(R.id.image);
        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);
        ImageButton fromGalleryButton = (ImageButton) findViewById(R.id.pic_from_gallery_button);
        fromGalleryButton.setOnClickListener(GalleryListener);

        vanillaRadioButton = (RadioButton) findViewById(R.id.vanilla_memes_radBtn);
        demotivationalRadBtn = (RadioButton) findViewById(R.id.demotivational_posters_radBtn);

        vanillaMemeIntent = new Intent(this, VanillaMemeEdit.class);
        demotivationalMemeIntent = new Intent(this, DemotivationalMemeEdit.class);

        editMemeButton = (Button)findViewById(R.id.edit_meme_button);
        editMemeButton.setOnClickListener(editMemeListener);

        vanillaRadioButton = (RadioButton) findViewById(R.id.vanilla_memes_radBtn);
        demotivationalRadBtn = (RadioButton) findViewById(R.id.demotivational_posters_radBtn);

        vanillaMemeIntent = new Intent(this, VanillaMemeEdit.class);
        demotivationalMemeIntent = new Intent(this, DemotivationalMemeEdit.class);



    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };


    private View.OnClickListener GalleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickPhoto(v);
        }
    };


    //method for requesting image from gallery/camera roll
    public void pickPhoto(View v) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, PICK_PICTURE);

}



    //method for requesting camera to capture image and save it under a new file
    public void takePhoto (View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }


    private void editPhoto(View v){
        Intent aviaryIntent = new AviaryIntent
                .Builder(this)
                .setData(imageUri)
                .withOutputSize(MegaPixels.Mp5)
                .build();

        aviaryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(aviaryIntent, EDIT_PICTURE);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        //_authSessionHelper.onActivityResult(requestCode, resultCode, intent);
//
//        if (resultCode == Activity.RESULT_OK) {
//
//
//            if(requestCode == TAKE_PICTURE) {
//                Uri selectedImage = imageUri;
//                getContentResolver().notifyChange(selectedImage, null);
//
//                ImageView imageview = (ImageView) findViewById(R.id.image);
//                ContentResolver cr = getContentResolver();
//                Bitmap bitmap;
//
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                    imageview.setImageBitmap(bitmap);
//                    Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
//
//                } catch (Exception e) {
//                    Log.e(logtag, e.toString());
//                }
//
//                Intent aviaryIntent = new AviaryIntent
//                        .Builder(this)
//                        .setData(imageUri)
//                        .withOutputSize(MegaPixels.Mp5)
//                        .build();
//                startActivityForResult(aviaryIntent, EDIT_PICTURE);
//
//                Uri mImageUri = aviaryIntent.getData(); // generated output file
//                Bundle extra = aviaryIntent.getExtras();
//                if( null != extra ) {
//                    // image has been changed?
//                    boolean changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
//                }
//
//            }
//        } else if(resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "User cancelled for requestCode: " + requestCode, Toast.LENGTH_SHORT).show();


    private View.OnClickListener editMemeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(vanillaRadioButton.isChecked()){
                startActivity(vanillaMemeIntent);
            }else if(demotivationalRadBtn.isChecked()) {
                startActivity(demotivationalMemeIntent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Select a Meme Type", Toast.LENGTH_SHORT).show();
            }
        }
    };

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
//
//            try {
//
//                Uri selectedImage = imageUri;
//
//
//                getContentResolver().notifyChange(selectedImage, null);
//
//                ImageView imageview = (ImageView) findViewById(R.id.image);
//                ContentResolver cr = getContentResolver();
//                Bitmap bitmap;
//
//
//                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage); //don't store in memor card by default
//                imageview.setImageBitmap(bitmap);
//                Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
//            }


            //method for gathering intent information from takePhoto and pickPhoto methods
            // and setting the imageview with correct bitmap
            @Override
            protected void onActivityResult ( int requestCode, int resultCode, Intent data){
                if (resultCode == RESULT_OK) {
                    if (requestCode == PICK_PICTURE) {
                        selectedImagePath = String.valueOf(data.getData());
                        imageview.setImageBitmap(decodePhoto(selectedImagePath));
                    } else if (requestCode == TAKE_PICTURE) {
                        selectedImagePath = imageUri.toString();
                        imageview.setImageBitmap(decodePhoto(selectedImagePath));

                        Intent aviaryIntent = new AviaryIntent
//                        .Builder(this)
//                        .setData(imageUri)
//                        .withOutputSize(MegaPixels.Mp5)
//                        .build();
//                startActivityForResult(aviaryIntent, EDIT_PICTURE);
                    } else if (requestCode == EDIT_PICTURE) {
                        selectedImagePath = imageUri.toString();
                        imageview.setImageBitmap(decodePhoto(selectedImagePath));
                    }

                    else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }

            //requesting image's file path and converting into url and calling the
            // ContentResolver to retrieve image and set it inside a bitmap
        public Bitmap decodePhoto (String path){
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



        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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


    @Override
    protected void onResume() {
        super.onResume();
        //_authSessionHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //_authSessionHelper.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //_authSessionHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //_authSessionHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //_authSessionHelper.onDestroy();
    }


    @Override
    public String getClientID() {
        return CreativeCloud.YOUR_API_KEY;
    }

    @Override
    public String getClientSecret() {
        return CreativeCloud.YOUR_API_SECRET;
    }



        public void onRadioButtonClicked (View view){
            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch (view.getId()) {
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


        public void saveMeme (View v){
//        Intent intent = new Intent(MainActivity.this, MemeHandler.class);
//        startActivity(intent);
        }


        public void exportMeme (View v){

        }


        }

