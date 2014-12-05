package android.aut.hu.parseforum;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Peter on 2014.12.01..
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this,
                "prsoJU0IqXhp7Qm6sWetAT6li7NIXhYpTR1rE8Zj",
                "C8yNreU7SxBrWm3UPt8BXeCGmZ7Z2tzbluK6STYg");

        PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
