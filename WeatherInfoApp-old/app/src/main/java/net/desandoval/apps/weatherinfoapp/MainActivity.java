package net.desandoval.apps.weatherinfoapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.desandoval.apps.weatherinfoapp.network.HttpAsyncTask;

import org.json.JSONObject;


public class MainActivity extends Activity {

    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView) findViewById(R.id.tvData);
        Button btnGet = (Button) findViewById(R.id.btnGetWeather);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url =
                        "http://api.openweathermap.org/data/2.5/weather?q=Budapest&units=imperial";
                new HttpAsyncTask(getApplicationContext()).execute(url);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                weatherReceiver, new IntentFilter(HttpAsyncTask.FILTER_WEATHER)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
    }

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String rawJson = intent.getStringExtra(HttpAsyncTask.KEY_WEATHER);

            try {
                JSONObject root = new JSONObject(rawJson);
                String des =
                        ((JSONObject)root.getJSONArray("weather").get(0)).getString(
                                "description");


                tvData.setText(des);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



}