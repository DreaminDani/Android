package net.desandoval.apps.locationmonitor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends Activity implements LocationListener {

    public static final String KEY_WAKEUP ="key_wakeup"; // for reciever - not yet implemented

    private Button button;
    private Location prevLoc = null;
    private TextView tvLat;
    private TextView tvLng;
    private TextView tvDistance;
    private TextView tvProviderType;
    private TextView tvSpeed;
    private TextView tvAccuracy;
    private TextView tvAltitude;
    private TextView tvGPSTime;
    private TextView tvEstDistTravelled;

    private boolean emulator = true; // change this if you want network info
    private boolean tracking = false; // turn off to enable location updates automatically



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLng = (TextView) findViewById(R.id.tvLong);
        tvProviderType = (TextView) findViewById(R.id.tvProviderType);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvAccuracy = (TextView) findViewById(R.id.tvAccuracy);
        tvAltitude = (TextView) findViewById(R.id.tvAltitude);
        tvGPSTime = (TextView) findViewById(R.id.tvGPSTime);
        tvDistance = (TextView) findViewById(R.id.tvEstDistTravelled);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tracking) {
                    tracking = false;
                } else {
                    tracking = true;
                }
//
//                Intent intent = new Intent(MainActivity.this,
//                        GPSTrackingReceiver.class);
//                intent.putExtra("key","test data");
//                PendingIntent sender = PendingIntent.getBroadcast(
//                        MainActivity.this, 0, intent, 0
//                );
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.add(Calendar.SECOND, 10);
//
//                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//                am.setRepeating(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(),
//                        10000,
//                        sender);
            }
        });
//        if (getIntent().getExtras() != null) {
//            if (getIntent().getExtras().getInt(KEY_WAKEUP) == 1) {
//                playWakeUp();
//            }
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (!emulator) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (tracking) {

            tvLng.setText("Provider Type: " + location.getProvider());
            tvLat.setText("Lat : " + location.getLatitude());
            tvLng.setText("Long: " + location.getLongitude());
            tvSpeed.setText("Speed: " + location.getSpeed() + " m/s");
            tvAccuracy.setText("Accuracy: " + location.getAccuracy() + " m");
            tvAltitude.setText("Altitude: " + location.getAltitude() + " m");
            tvGPSTime.setText("GPS Time: " + location.getTime());
            if (prevLoc != null) {
                tvDistance.setText("Distance Travelled: " + prevLoc.distanceTo(location) + " m");
            }

            prevLoc = location;

//            Thread.sleep;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
