package net.desandoval.apps.autocompletedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class MyActivity extends Activity {

    private final String[] cities = new String[] {"Budapest", "Berlin",
            "New York", "Las Vegas", "Bukares"};;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        AutoCompleteTextView atvCities = (AutoCompleteTextView) findViewById(R.id.atvCities);

        ArrayAdapter<String> listCities = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line,
                cities
        );

        atvCities.setAdapter(listCities);
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
