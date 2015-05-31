package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating an intent to take a picture and return a control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // creating a file to save image
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri); //set the image filename

        //start image capture intent
        startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    //receiving camera intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (requestCode == RESULT_OK){
                // Image is captured and saved to fileUri
                Toast.makeText(this, "Image saved to:\n"
                        + data.getData(), Toast.LENGTH_LONG).show();
            }else if (resultCode == RESULT_CANCELED){
                //User cancels th image captured, display message to user
                Toast.makeText(this, "Image Cancelled"
                        + data.getData(), Toast.LENGTH_LONG).show();
            }else {
                //image capture failed, display message to user
                Toast.makeText(this, "Failed to capture image,Try again."
                        + data.getData(), Toast.LENGTH_LONG).show();
            }
        }
    }
    
    
   /** Create a file Uri for saving an image */
   private static Uri getOutputMediaFileUri(int type){
       return Uri.fromFile(getOutputMediaFile(type));
   }
    
 //todo: http://developer.android.com/guide/topics/media/camera.html#saving-media
    private static File getOutputMediaFile(int type) {
        return null;
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
}
