package nyc.c4q.rosmaryfc.meme_ify_me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    /** Duration of wait **/
    ImageView img;
    AnimationDrawable frameAnimation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle pouch) {
        super.onCreate(pouch);
        setContentView(R.layout.activity_splash_screen);

        img = (ImageView) findViewById(R.id.image);
        img.setImageResource(R.drawable.splash_animation);
        frameAnimation = (AnimationDrawable) img.getDrawable();
        frameAnimation.start();


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                frameAnimation.stop();
                img.setImageResource(R.drawable.title);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    }
                }, 3000);

            }
        }, 1000);
    }
}