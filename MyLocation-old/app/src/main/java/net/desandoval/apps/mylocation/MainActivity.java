package net.desandoval.apps.mylocation;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements LocationListener {

    private TextView tvLat;
    private TextView tvLng;
    private TextView tvSpeed;
    private TextView tvAddress;

    private Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLng = (TextView) findViewById(R.id.tvLng);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        Button btnGeocode = (Button) findViewById(R.id.btnGeocode);
        btnGeocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    public void run() {

                        try {
                            Geocoder gc = new Geocoder(getApplicationContext());
                            double lat = currentLocation.getLatitude();
                            double lng = currentLocation.getLongitude();
                            List<Address> addresses =
                                    gc.getFromLocation(lat, lng, 1);

                            String country = addresses.get(0).getAddressLine(0);

                            tvAddress.setText(country);

                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        tvLat.setText(""+location.getLatitude());
        tvLng.setText(""+location.getLongitude());
        tvSpeed.setText(""+location.getSpeed());
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
