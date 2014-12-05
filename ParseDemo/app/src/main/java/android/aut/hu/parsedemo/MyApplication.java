package android.aut.hu.parsedemo;

import android.app.Application;
import android.aut.hu.parsedemo.data.Item;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;

/**
 * Created by Peter on 2014.12.01..
 */
public class MyApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            parseInit();
        }

        public void parseInit() {
            ParseObject.registerSubclass(Item.class);

            Parse.initialize(this, "Fg3RvMsNGV15NF38CO0PqjDE9NRH82NA3IlVUte4",
                    "htIY5vhzQak9UxhAtiuzE0y8agMy4okOjlIAb1OZ");

            PushService.setDefaultPushCallback(this, MainActivity.class);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }

}
