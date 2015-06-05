package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;

import java.io.File;


public class MainActivity extends ActionBarActivity implements IAdobeAuthClientCredentials {

    private static final int EDIT_PICTURE = 3;
    private static final int USE_EDITED_PICTURE = 4;

    private static String logtag = "CameraApp8";
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    private static int DEFAULT_SIGN_IN_REQUEST_CODE = 2002;
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

        Button cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);

        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        Intent intent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(intent);


    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

//    private void editPhoto(View v){
//        Intent aviaryIntent = new AviaryIntent
//                .Builder(this)
//                .setData(imageUri)
//                .withOutputSize(MegaPixels.Mp5)
//                .build();
//
//        aviaryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(aviaryIntent, EDIT_PICTURE);
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //_authSessionHelper.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {


            if(requestCode == TAKE_PICTURE) {
                Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);

                ImageView imageview = (ImageView) findViewById(R.id.image);
                ContentResolver cr = getContentResolver();
                Bitmap bitmap;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                    imageview.setImageBitmap(bitmap);
                    Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e(logtag, e.toString());
                }

                Intent aviaryIntent = new AviaryIntent
                        .Builder(this)
                        .setData(imageUri)
                        .withOutputSize(MegaPixels.Mp5)
                        .build();
                startActivityForResult(aviaryIntent, EDIT_PICTURE);

                Uri mImageUri = aviaryIntent.getData(); // generated output file
                Bundle extra = aviaryIntent.getExtras();
                if( null != extra ) {
                    // image has been changed?
                    boolean changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
                }

            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "User cancelled for requestCode: " + requestCode, Toast.LENGTH_SHORT).show();
        }
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


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.vanilla_memes:
                if (checked)
                    // load vanilla_memes layout
                    break;
            case R.id.demotivational_posters:
                if (checked)
                    // load demotivational_posters layout
                    break;
        }
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
}
