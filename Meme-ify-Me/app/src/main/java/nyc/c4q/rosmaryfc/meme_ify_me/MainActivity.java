package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraButton = (Button)findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);
    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private void takePhoto (View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        try{
        if(resultCode == Activity.RESULT_OK){
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);

            ImageView imageview = (ImageView)findViewById(R.id.image);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;


                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage); //don't store in memor card by default
                imageview.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();


        }
        }catch(Exception e){
            Log.e(logtag, e.toString());
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
