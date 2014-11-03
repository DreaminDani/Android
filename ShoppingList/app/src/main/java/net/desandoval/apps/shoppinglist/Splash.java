package net.desandoval.apps.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * Created by Daniel on 18/10/2014.
 */
public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.push_anim);

        final LinearLayout layoutContainer = (LinearLayout) findViewById(R.id.layoutContainer);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                layoutContainer.startAnimation(anim);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(Splash.this, LoginPassword.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);
    }
}
