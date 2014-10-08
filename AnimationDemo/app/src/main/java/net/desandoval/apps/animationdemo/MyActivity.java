package net.desandoval.apps.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Animation anim = AnimationUtils.loadAnimation(
                getApplicationContext(),R.anim.anim);

        final LinearLayout layoutContainer = (LinearLayout) findViewById(
                R.id.layoutContainer);
        final EditText etData = (EditText) findViewById(R.id.etData);
        final Button btnPress = (Button) findViewById(R.id.btnPress);
        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // play animation
//
//                btnPress.startAnimation(anim);
//                etData.startAnimation(anim);
                layoutContainer.startAnimation(anim);
            }
        });

    }

}