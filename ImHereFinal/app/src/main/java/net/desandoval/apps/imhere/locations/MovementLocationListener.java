package net.desandoval.apps.imhere.locations;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import net.desandoval.apps.imhere.R;
import net.desandoval.apps.imhere.main.SendLocation;

/**
 * Created by Daniel on 12/12/2014.
 */
public class MovementLocationListener implements LocationListener {

    public static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    public static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";

    public static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    public static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    private Context ctx;
    private LocationManager locationManager;
    private SharedPreferences pref;

    public MovementLocationListener (Context context) {
        ctx = context;
        pref = ctx.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("hasMovementListener", true);
        editor.apply();

        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATE,
                MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                this
        );
    }

    // send push notification if user has travelled more than 2657027m since last location change
    // distance determined by length of Puerto Rico (100 miles)
    public void onLocationChanged(Location location) {
        Location pointLocation = retrievelocationFromPreferences();
        if (pointLocation != null) {
            float distance = location.distanceTo(pointLocation);
            int pushId = String.valueOf(distance).hashCode();
            if (distance > 160934) {
                createNotication(ctx, "You've traveled quite far!","Click here to notify your \"ImHere\" group", String.valueOf(pushId));
                Log.d("Create Push", "From Location: " + pointLocation.toString() + " | To Location: " + location.toString());
                Log.d("Create Push", "Distance Traveled: " + distance);
            }
        }else {
            createNotication(ctx, "Welcome to ImHere", "Clicking this notification will send a message to selected contacts", String.valueOf(1));
        }

        saveCoordinatesInPreferences((float)location.getLatitude(), (float)location.getLongitude());
    }

    private void saveCoordinatesInPreferences(float latitude, float longitude) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        Log.d("Saved Location", "lat: " + latitude + "| lon: " + longitude);
        prefsEditor.apply();
    }

    private Location retrievelocationFromPreferences() {
        Location location = new Location("POINT_LOCATION");
        float lat = pref.getFloat(POINT_LATITUDE_KEY, 0);
        float lon = pref.getFloat(POINT_LONGITUDE_KEY, 0);
        Log.d("Retrieved Location", location.toString());
        if (lat == 0 && lon == 0) {
            return null;
        } else {
            location.setLatitude(lat);
            location.setLongitude(lon);

            return location;
        }
    }

    public void onStatusChanged(String s, int i, Bundle b) {
    }
    public void onProviderDisabled(String s) {
    }
    public void onProviderEnabled(String s) {
    }

    public static void createNotication(Context context, String title, String content, String pushId) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, SendLocation.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(pushId, 1, mBuilder.build());
    }
}
