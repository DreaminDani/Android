package examples.android.ait.hu.animationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Animation anim = AnimationUtils.loadAnimation(this,R.anim.push_anim);

        final RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.layoutContainer);

        final Button btnStartAnim = (Button) findViewById(R.id.btnStartAnim);
        btnStartAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartAnim.startAnimation(anim);
                layoutContainer.startAnimation(anim);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
