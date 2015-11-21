package fr.esilv.sport_together;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by anirb on 20/11/2015.
 */
public class app_init extends Application {
    static final String TAG = "myApp";

    @Override
    public void onCreate(){
        super.onCreate();
        FacebookSdk.sdkInitialize((getApplicationContext()));
        Parse.initialize(this, "piScUisAIhcCWqczLGBRTqXQLbG6o9e8Z5Y6X7h1", "GocWv1Mfo90AvnLBvD2Fkppps58ZIadkDlyZhNMy");
        ParseFacebookUtils.initialize(this);

    }
}
