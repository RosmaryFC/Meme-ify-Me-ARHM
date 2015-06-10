package nyc.c4q.rosmaryfc.meme_ify_me;

/**
 * Created by Hoshiko on 5/30/15.
 */

import android.app.Application;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.aviary.android.feather.sdk.IAviaryClientCredentials;

/*
 * This connects our app to the Adobe Creative SDK.
 */
public class CreativeCloud extends Application implements IAviaryClientCredentials {
    public static final String YOUR_API_KEY = "958f6b23c0604850aae5bfc2f0c62628";
    public static final String YOUR_API_SECRET = "65f65622-35aa-4898-a901-cccff4562c15";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public String getBillingKey() {
        return ""; // leave it blank
    }

    @Override
    public String getClientID() {
        return YOUR_API_KEY;
    }

    @Override
    public String getClientSecret() {
        return YOUR_API_SECRET;
    }
}