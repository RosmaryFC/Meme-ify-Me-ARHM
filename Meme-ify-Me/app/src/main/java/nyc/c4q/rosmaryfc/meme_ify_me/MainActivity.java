package nyc.c4q.rosmaryfc.meme_ify_me;

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

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private static String logtag ="CameraApp8";
    private static int TAKE_PICTURE =1;
    private static final int PICK_PICTURE = 2;
    private Uri imageUri;
    ImageView imageview;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = (ImageView)findViewById(R.id.image);
        Button cameraButton = (Button)findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);
        Button fromGalleryButton = (Button) findViewById(R.id.pic_from_gallery_button);
        fromGalleryButton.setOnClickListener(galleryListener);
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

    private void pickPhoto(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PICTURE);

    }

    private void takePhoto (View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }



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

    public Bitmap decodePhoto (String path){
        Uri selectedImageUri=Uri.parse(selectedImagePath);
        getContentResolver().notifyChange(selectedImageUri, null);
        ContentResolver cr = getContentResolver();
        Bitmap bitmap=null;
        try{
                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImageUri);
               //imageview.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, selectedImageUri.toString(), Toast.LENGTH_LONG).show();

            }catch(Exception e){
                Log.e(logtag, e.toString());
            }
      return bitmap;
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
        switch(view.getId()) {
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
}
