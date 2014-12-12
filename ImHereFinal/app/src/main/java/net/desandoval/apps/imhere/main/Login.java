package net.desandoval.apps.imhere.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import net.desandoval.apps.imhere.R;
import net.desandoval.apps.imhere.contacts.EditContacts;
import net.desandoval.apps.imhere.locations.EditLocations;

import java.util.UUID;

/**
 * Created by Daniel on 5/12/2014.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(Login.this, "Loading old user", Toast.LENGTH_LONG).show();
        } else {
            final String deviceId = getTelephoneId();
            ParseUser.logInInBackground(deviceId, deviceId, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Toast.makeText(Login.this, "Logging into Parse", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "User not yet created", Toast.LENGTH_LONG).show();
                        createNewUser(deviceId);
                    }
                }
            });

        }

        ImageButton editLocations = (ImageButton) findViewById(R.id.editLocations);
        ImageButton editContacts = (ImageButton) findViewById(R.id.editContacts);
        editLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, EditLocations.class);
                startActivity(i);
            }
        });
        editContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, EditContacts.class);
                startActivity(i);
            }
        });
    }

    private void createNewUser(String deviceId) {
        ParseUser user = new ParseUser();
        user.setUsername(deviceId);
        user.setPassword(deviceId);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(Login.this, "Created New User", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        throw new Exception(e.getMessage());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public String getTelephoneId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }
}
