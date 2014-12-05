package net.desandoval.apps.imhere.TripIt;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.desandoval.apps.imhere.R;

import org.springframework.social.tripit.api.impl.TripItTemplate;

/**
 * Created by Daniel on 5/12/2014.
 */
public class Login extends Activity{

    private TripItTemplate template;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button signIn = (Button) findViewById(R.id.btnSignIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: here's where I'm having trouble
                template = new TripItTemplate("73553f9278b2da745d12a865d7069c38d93ade66", "0aa95998fb503190ea61354cd857d1e7b2c889a3", "How Do I get This AccessToken?", "How do I get this accessTokenSecret");
                user = new User(template.getUserProfile(), template.getUpcomingTrips());

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
