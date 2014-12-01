package android.ait.hu.weatherinfoapp;

import android.ait.hu.weatherinfoapp.network.HttpAsyncTask;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by Daniel Sandoval
 * Modified from In-Class WeatherInfoApp example
 */
public class MainActivity extends Activity {

    private TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = this.getSharedPreferences(
                "android.ait.hu.weatherinfoapp", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        String storedCity = prefs.getString("city", "");
        final EditText cityName = (EditText) findViewById(R.id.etCity);

        if (!storedCity.isEmpty()) {
            request(storedCity);
            cityName.setText(storedCity);
            Toast.makeText(this, "Showing current data for your last searched City", Toast.LENGTH_LONG).show();
        }

        tvDesc = (TextView) findViewById(R.id.tvDesc);
        Button btnGet = (Button) findViewById(R.id.btnGetWeather);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityName.getText().toString().trim().length() > 0) {
                    request(cityName.getText().toString());
                    prefs.edit().putString("city", cityName.getText().toString()).apply();
                }else {
                    cityName.setError("Please enter the name of a city to see its weather");
                }
            }
        });
    }

    public void request(String city) {
        String url =
                "http://api.openweathermap.org/data/2.5/weather?q=";
        url += city;
        url += "&units=metric";
        new HttpAsyncTask(getApplicationContext()).execute(url);
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
            tvDesc.setText("Retrieving Data... Please Wait");
            ImageView icon = (ImageView) findViewById(R.id.ivIcon);
            Glide.with(getApplicationContext()).load("http://fitstop.pt/img/ajax-loader.gif").into(icon);

            String rawJson = intent.getStringExtra(HttpAsyncTask.KEY_WEATHER);

            try {
                //TODO: Parse JSON Object
                TextView tvTemp = (TextView) findViewById(R.id.tvTemp);
                TextView tvHum = (TextView) findViewById(R.id.tvHum);
                TextView tvSunrise = (TextView) findViewById(R.id.tvSunrise);
                TextView tvSunset = (TextView) findViewById(R.id.tvSunset);

                JSONObject root = new JSONObject(rawJson);
                JSONObject weather = (JSONObject) root.getJSONArray("weather").get(0);
                JSONObject main = (JSONObject)root.get("main");
                JSONObject sys = (JSONObject)root.get("sys");
                String iconUrl = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";

                Glide.with(getApplicationContext()).load(iconUrl).into(icon);
                tvDesc.setText(weather.getString("description"));
                tvTemp.setText(getResources().getString(R.string.temperature) + " " + main.getString("temp") + " deg. C");
                tvHum.setText(getResources().getString(R.string.humidity) + " " + main.getString("humidity") + "%");

                Format formatter = new SimpleDateFormat("HH:mm:ss");

                String sunrise = sys.getString("sunrise");
                java.util.Date time1 = new java.util.Date((long) Integer.parseInt(sunrise)*1000);

                tvSunrise.setText(getResources().getString(R.string.sunrise) + " " + formatter.format(time1) + " UTC");

                String sunset = sys.getString("sunset");
                java.util.Date time2 = new java.util.Date((long) Integer.parseInt(sunset)*1000);

                tvSunset.setText(getResources().getString(R.string.sunset) + " " + formatter.format(time2)+ " UTC");

//                tvData.setText(rawJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



}
