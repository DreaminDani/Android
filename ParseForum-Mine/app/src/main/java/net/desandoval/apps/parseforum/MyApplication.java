package net.desandoval.apps.parseforum;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by Daniel on 1/12/2014.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this,
                "prsoJU0IqXhp7Qm6sWetAT6li7NIXhYpTR1rE8Zj",
                "C8yNreU7SxBrWm3UPt8BXeCGmZ7Z2tzbluK6STYg");

        PushService.setDefaultPushCallback(this, MainActivity.class); // if a notification arrives, launch this activity

//        Parse.initialize(this, "LXigISGuQgQAkBFsTMrETEGHCzDUADmAF6yyq004", "04lByXyhKDfNJg3WuQRZL7sZ0DATE10IhvDFx4F5");
    }
}
