package nyc.c4q.rosmaryfc.meme_ify_me;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements IAdobeAuthClientCredentials {

    private static final int EDIT_PICTURE = 3;
    private static String logtag = "CameraApp";
    private static int TAKE_PICTURE = 1;
    private static final int PICK_PICTURE = 2;
    private Uri imageUri;
    protected ImageView imageview;
    private String selectedImagePath;
    private RadioButton vanillaRadioButton;
    private RadioButton demotivationalRadBtn;
    private Intent vanillaMemeIntent;
    private Intent demotivationalMemeIntent;
    Button editButton;
    private File photo = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SelectedImagePath", selectedImagePath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
        Intent intent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(intent);

        if ((savedInstanceState != null)) {
            selectedImagePath = savedInstanceState.getString("SelectedImagePath");
        }

        initializeViews();

    }

    private void initializeViews(){

        imageview = (ImageView) findViewById(R.id.image);

        vanillaRadioButton = (RadioButton) findViewById(R.id.vanilla_memes_radBtn);
        demotivationalRadBtn = (RadioButton) findViewById(R.id.demotivational_posters_radBtn);

        editButton = (Button) findViewById(R.id.editButton);
        //button needs to show only when picture was taken.
        editButton.setVisibility(View.INVISIBLE);
    }


    public void editMeme(View v){
            if (selectedImagePath == null) {
                Toast.makeText(getApplicationContext(), "Select an image or take a picture", Toast.LENGTH_SHORT).show();
            } else {
                if (vanillaRadioButton.isChecked()) {
                    vanillaMemeIntent = new Intent(MainActivity.this, VanillaMemeEdit.class);
                    vanillaMemeIntent.putExtra("SelectedImagePath", selectedImagePath);
                    startActivity(vanillaMemeIntent);
                } else if (demotivationalRadBtn.isChecked()) {
                    demotivationalMemeIntent = new Intent(MainActivity.this, DemotivationalMemeEdit.class);
                    demotivationalMemeIntent.putExtra("SelectedImagePath", selectedImagePath);
                    startActivity(demotivationalMemeIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Select a Meme Type", Toast.LENGTH_SHORT).show();
                }
            }
    }

    //method for requesting image from gallery/camera roll
    public void pickPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PICTURE);
    }

    //method for requesting camera to capture image and save it under a new file
    public void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), createImageFileName());
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    //open up the editor to edit the picture loaded in imageView
    public void editPhoto(View v) {
        photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), createImageFileName());

        Intent aviaryIntent = new AviaryIntent
                .Builder(this)
                .setData(imageUri)
                .withOutput(photo)
                .withOutputSize(MegaPixels.Mp5)
                .build();

        imageUri = Uri.fromFile(photo);

        Bundle extra = aviaryIntent.getExtras();
        if (null != extra) {
            // image has been changed?
            boolean changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
            if (changed) {
                aviaryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            }
        }
        startActivityForResult(aviaryIntent, EDIT_PICTURE);

    }

    /**
     * Creates a temporary file that the camera can use to save
     *
     * @return File
     * @throws IOException
     */
    private String createImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        // Save a file: path for use with ACTION_VIEW intents
        return imageFileName;
    }

    //method for gathering intent information from takePhoto and pickPhoto methods
    // and setting the imageview with correct bitmap, and saving
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PICTURE) {
                selectedImagePath = String.valueOf(data.getData());
                imageview.setImageBitmap(decodePhoto(MainActivity.this, selectedImagePath));

                //make Edit Picture button invisible
                editButton.setVisibility(View.INVISIBLE);


            } else if (requestCode == TAKE_PICTURE) {

                selectedImagePath = imageUri.toString();
                imageview.setImageBitmap(decodePhoto(MainActivity.this, selectedImagePath));

                //make Edit Picture button visible
                editButton.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"You can edit the picture you just took by pressing the edit picture button", Toast.LENGTH_LONG).show();

                Context context = getApplicationContext();
                LayoutInflater inflater = getLayoutInflater();

                View customToastroot = inflater.inflate(R.layout.mycustom_toast, null);
                Toast customtoast = new Toast(context);

                customtoast.setView(customToastroot);
                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                customtoast.setDuration(Toast.LENGTH_LONG);
                customtoast.show();


            } else if (requestCode == EDIT_PICTURE) {

                selectedImagePath = imageUri.toString();
                imageview.setImageBitmap(decodePhoto(MainActivity.this, selectedImagePath));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        boolean imageSelected = (selectedImagePath != null);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.vanilla_memes_radBtn:

                if (checked && !imageSelected) {
                    // load vanilla_memes layout
                    //todo: this is where code will go to change sample image to sample vanilla meme image
                    //imageview.setImageResource(R.drawable.vanillapreview);
                }
                //else {
                    if (checked) {
                        // load vanilla_memes layout toast

                        Context contextMeme = getApplicationContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastroot = inflater.inflate(R.layout.meme1toast, null);
                        Toast contextMeme1 = new Toast(contextMeme);
                        contextMeme1.setView(customToastroot);
                        contextMeme1.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        contextMeme1.setGravity(Gravity.AXIS_X_SHIFT, 0, 33);
                        contextMeme1.setDuration(Toast.LENGTH_SHORT);
                        contextMeme1.show();


                    }
                    break;
                //}

            case R.id.demotivational_posters_radBtn:

                if (checked && !imageSelected) {
                    // load demotivational_posters layout
                    //todo: this is where code will go to change sample image to sample demotivational poster image
                    //imageview.setImageResource(R.drawable.demotpreview);
                }
                //else {
                    //set background image to demotivational looking
                    if (checked) {
                        // load demotivational_posters layout toast


                        Context contextMemeTwo = getApplicationContext();
                        LayoutInflater inflaterTwo = getLayoutInflater();
                        View customToastrootTwo = inflaterTwo.inflate(R.layout.meme2toast, null);
                        Toast contextMemetoastTwo = new Toast(contextMemeTwo);
                        contextMemetoastTwo.setView(customToastrootTwo);
                        contextMemetoastTwo.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        contextMemetoastTwo.setGravity(Gravity.AXIS_X_SHIFT, 0, 33);
                        contextMemetoastTwo.setDuration(Toast.LENGTH_SHORT);
                        contextMemetoastTwo.show();

                    }
                    break;
                //}
        }
    }


        @Override
        public String getClientID () {
            return CreativeCloud.YOUR_API_KEY;
        }

        @Override
        public String getClientSecret () {
            return CreativeCloud.YOUR_API_SECRET;
        }



}



