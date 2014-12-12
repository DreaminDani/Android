package demo.android.app.hu.servicedemo.location;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by Peter on 2014.10.26..
 */
public class LDLocationManager  {

    private Context context;
    private LocationListener listener;
    private LocationManager locMan;

    public LDLocationManager(Context aContext, LocationListener listener) {
        context = aContext;
        this.listener = listener;
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationMonitoring() {
        locMan.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, 100, listener);
        // NETWORK PROVIDER IS NOT WORKING ON EMULATOR - THROWS AN EXCEPTION!!!
        /*locMan.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0, 0, listener);*/
    }

    public void stopLocationMonitoring() {
        if (locMan != null) {
            locMan.removeUpdates(listener);
        }
    }
}