package net.desandoval.apps.imhere.TripIt;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.springframework.social.tripit.api.Trip;
import org.springframework.social.tripit.api.TripItProfile;

import java.util.List;

import io.oauth.OAuthData;

/**
 * Created by Daniel on 5/12/2014.
 */
public class User {

    private String token;
    private String secret;

    private TripItProfile profile;
    private List<Trip> trips;

    public ParseException error;

    public OAuthData data;

    public User(String token, String secret) {
        this.token = token;
        this.secret = secret;
        ParseUser user = new ParseUser();
        user.setUsername(token);
        user.setPassword(secret);
        user.put("secret", secret);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                error = e;
            }
        });
    }
}
