package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by c4q-Allison on 6/12/15.
 */
public class SaveMeme {

    public Bitmap loadBitmapFromView(FrameLayout view) {

        view.setDrawingCacheEnabled(true);

        view.buildDrawingCache();

        Bitmap bm = view.getDrawingCache();

        return bm;
    }

    public void saveMeme(Bitmap bm, String imgName, ContentResolver c) {

        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            // Compress image
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            // Update image to gallery
            MediaStore.Images.Media.insertImage(c,
                    f.getAbsolutePath(), f.getName(), f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
