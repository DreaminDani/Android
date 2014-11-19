package demo.android.app.hu.intentdemo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIntentDemo = (Button) findViewById(R.id.btnIntent);
        btnIntentDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(Intent.ACTION_CALL,
                 //       Uri.parse("tel:+36304892111"));

                Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
                i.putExtra(SearchManager.QUERY,
                    "Which is the newest Android version?");

                startActivity(i);
            }
        });

        if (getIntent().getData() != null &&
                getIntent().getData().getPath() != null) {
            String file = getIntent().getData().getPath();
            Toast.makeText(this,"TXT opened: "+file, Toast.LENGTH_LONG).show();
        }
    }

}
