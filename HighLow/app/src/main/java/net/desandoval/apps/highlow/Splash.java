package net.desandoval.apps.highlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Daniel on 1/10/2014.
 */
public class Splash extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Button startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Splash.this, MyActivity.class);
                startActivity(myIntent);
            }
        });

        Button helpBtn = (Button) findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Guess a number between 0 and 10 and I'll give you a hint if you're close!", Toast.LENGTH_LONG).show();
            }
        });

        Button aboutBtn = (Button) findViewById(R.id.about);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Copyright 2014 Daniel Sandoval\nAIT-Budapest\n(Skeleton stolen from in-class example)", Toast.LENGTH_LONG).show();
            }
        });
    }
}
