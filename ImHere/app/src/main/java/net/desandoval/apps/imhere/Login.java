package net.desandoval.apps.imhere;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import net.desandoval.apps.imhere.R;
import net.desandoval.apps.imhere.TripIt.Connect;
import net.desandoval.apps.imhere.TripIt.User;

import org.json.JSONObject;
import org.springframework.social.tripit.api.impl.TripItTemplate;
import org.springframework.social.tripit.connect.TripItConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import io.oauth.OAuthRequest;

/**
 * Created by Daniel on 5/12/2014.
 */
public class Login extends Activity implements OAuthCallback {

    private TripItTemplate template;
    private TextView subTitle;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button signIn = (Button) findViewById(R.id.btnSignIn);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {

            subTitle = (TextView) findViewById(R.id.subTitle);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //                Connect connection = new Connect(getApplicationContext());
                    //                OAuth o = connection.connectToTripIt();
                    OAuth o = new OAuth(Login.this);
                    o.initialize(MyApplication.OAUTH_PUBLIC_KEY); // Initialize the oauth key

                    o.popup("tripit", Login.this); // Launch the pop up with the right provider & callback

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                    // TODO: here's where I'm having trouble
                    //                template = new TripItTemplate("73553f9278b2da745d12a865d7069c38d93ade66", "0aa95998fb503190ea61354cd857d1e7b2c889a3", "How Do I get This AccessToken?", "How do I get this accessTokenSecret");
                    //                user = new User(template.getUserProfile(), template.getUpcomingTrips());

                    // TODO: Finish application
                    // go to another screen and do something with this information
                    // also store this user to shared preferences (to prevent re-logging in)
                    // start a service to do things in the background
                    // upload this all to Parse
                    // process info on Parse and send push notification when user is in right spot
                    // push notification opens up activity that makes a friends list
                    // figure out how to send stuff to these friends
                    // ???
                    // profit
                }
            });
        }
    }

    @Override
    public void onFinished(OAuthData data) {
        final TextView textview = subTitle;
        if ( ! data.status.equals("success")) {
            textview.setTextColor(Color.parseColor("#FF0000"));
            textview.setText("error, " + data.error);
        }

        // You can access the tokens through data.token and data.secret

        textview.setText("loading...");

        // Let's skip the NetworkOnMainThreadException for the purpose of this sample.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        User user = new User(data.token, data.secret);
        if (user.error != null) {
            Toast.makeText(Login.this, "Oops! Something went wrong. Please try logging in again", Toast.LENGTH_LONG).show();
        }

        Log.d("TEST", data.provider);
        Log.d("TEST", data.state);
        Log.d("TEST", data.token);
        Log.d("TEST", data.secret);
        Log.d("TEST", data.status);
        Log.d("TEST", data.expires_in);
        Log.d("TEST", data.error);
        Log.d("TEST", data.request.toString());

        // To make an authenticated request, you can implement OAuthRequest with your prefered way.
        // Here, we use an URLConnection (HttpURLConnection) but you can use any library.
        data.http("/v1/list/object/format/json", new OAuthRequest() {
            private URL url;
            private URLConnection con;

            @Override
            public void onSetURL(String _url) {
                try {
                    Log.d("URL", _url);
                    url = new URL(_url);
                    con = url.openConnection();
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onSetHeader(String header, String value) {
                con.addRequestProperty(header, value);
            }

            @Override
            public void onReady() {
                try {
                    BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }
                    Log.d("JSON", total.toString());
                    JSONObject result = new JSONObject(total.toString());
                    textview.setText("hello, " + result.getString("timestamp"));
                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onError(String message) {
                textview.setText("error: " + message);
            }
        });
    }
}
