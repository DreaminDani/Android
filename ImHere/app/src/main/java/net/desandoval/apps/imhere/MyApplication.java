package net.desandoval.apps.imhere;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Peter on 2014.12.01..
 */
public class MyApplication extends Application {

    public static final String OAUTH_PUBLIC_KEY = "wi0_puckBkhSaV9sk3vAr6aPpis";
    private final String APP_ID = "gP5nq9UDGiLxe7SC0BTwtoTCgFA4JTLLDuc6dd94";
    private final String CLIENT_KEY = "cQ446O62YQi3oqH3TLoN0od6jbwlVxEkzQrqlqeJ";


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APP_ID, CLIENT_KEY);

        PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
