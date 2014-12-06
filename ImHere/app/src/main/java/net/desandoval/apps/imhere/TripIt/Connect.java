package net.desandoval.apps.imhere.TripIt;

import android.content.Context;

import net.desandoval.apps.imhere.Login;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;

/**
 * Created by Daniel on 5/12/2014.
 */
public class Connect {
    private final String API_KEY = "73553f9278b2da745d12a865d7069c38d93ade66";
    private final String API_SECRET = "0aa95998fb503190ea61354cd857d1e7b2c889a3";
    private final String OAUTH_PUBLIC_KEY = "wi0_puckBkhSaV9sk3vAr6aPpis";

    private Context context;
    public Connect(Context context) {
        this.context = context;
    }

    public OAuth connectToTripIt() {
        final OAuth o = new OAuth(context);
        o.initialize(OAUTH_PUBLIC_KEY); // Initialize the oauth key
        return o;
    }
}
