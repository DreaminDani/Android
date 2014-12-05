package android.aut.hu.parsedemo;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParsePush;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParsePush.subscribeInBackground("AITPush");
    }
}
