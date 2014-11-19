package demo.android.app.hu.simplelocationdemo;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener{

    private Location prevLoc = null;
    private TextView tvLat;
    private TextView tvLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLng = (TextView) findViewById(R.id.tvLng);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (prevLoc != null) {
            Toast.makeText(this,
              "Distance: "+prevLoc.distanceTo(location), Toast.LENGTH_LONG).show();
        }

        prevLoc = location;

        tvLat.setText(""+location.getLatitude());
        tvLng.setText(""+location.getLongitude());
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
