package demo.android.app.hu.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.layoutContainer, new LocationDashboardFragment())
                    .commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(MainActivity.this,
                        SettingsActivity.class);
                intentSettings.putExtra(SettingsActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.FragmentSettingsBasic.class.getName());
                intentSettings.putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
                startActivity(intentSettings);
                break;
        }

        return true;
    }
}
