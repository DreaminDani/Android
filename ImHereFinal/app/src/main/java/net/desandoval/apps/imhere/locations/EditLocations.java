package net.desandoval.apps.imhere.locations;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.desandoval.apps.imhere.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * EditLocations activity for editing the user's custom locations (notified when nearby)
 */
public class EditLocations extends ActionBarActivity {

    private GoogleMap map;
    private List<Marker> markerList;
    private List<Marker> storedMarkers;

    private static final long POINT_RADIUS = 10000; // in Meters (calculated average American city radius)
    private static final long PROX_ALERT_EXPIRATION = -1;

    private static final String PROX_ALERT_INTENT = "net.desandoval.apps.imhere.main.ProximityAlert";

    private static final NumberFormat nf = new DecimalFormat("##.########");

    private LocationManager locationManager;

    public EditLocations(){
        if(markerList == null){
            markerList = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = ((MapFragment)getFragmentManager() . findFragmentById(R.id.map)).getMap();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (!pref.getBoolean("hasMovementListener",false)){
            new MovementLocationListener(getApplicationContext());
        }

        getMarkersFromParse();

        markerList = storedMarkers;

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                String label = getMyLocationAddress(point, EditLocations.this);
                MarkerOptions mo = new MarkerOptions().position(point).title(label).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mo.snippet("Tap here to remove this marker");
                markerList.add(map.addMarker(mo));
                ParseObject marker = new ParseObject("CustomLocation");
                marker.put("user", ParseUser.getCurrentUser());
                marker.put("latitude", point.latitude);
                marker.put("longitude", point.longitude);
                marker.put("location", label);
                marker.saveInBackground();
                saveProximityAlertPoint(point);
            }
        });

        // Setting click event handler for InfoWindow
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng currentPoint = marker.getPosition();

                removeProximityAlert(getPointId(currentPoint));

                ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomLocation");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.whereEqualTo("latitude", currentPoint.latitude);
                query.whereEqualTo("longitude", currentPoint.longitude);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            ParseObject.deleteAllInBackground(parseObjects);
                        } else {
                            Log.d("Parse Error", "Could not retrieve stored markers for deletion - markers not delete from parse");
                        }
                    }
                });
                marker.remove();
            }
        });
    }

    /*
     * Retrieves address for given location
     */
    public static String getMyLocationAddress(LatLng point, Context context) {
        Geocoder geocoder= new Geocoder(context, Locale.ENGLISH);
        String result = "Unknown Location";

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(point.latitude,point.longitude, 1);

            if(addresses != null) {
                result = "";
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                result = strAddress.toString();

            }
                return result;

        }
        catch (IOException e) {
            e.printStackTrace();
            return "Unknown Location";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id   == R.id.action_help) {
            showDialog();
        }
        if (id == R.id.action_clear) {
            map.clear();
            markerList.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomLocation");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        ParseObject.deleteAllInBackground(parseObjects);
                    } else {
                        Log.d("Parse Error", "Could not retrieve stored markers for deletion - markers not delete from parse");
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Retrieves previously stored markers from Parse
     */
    public void getMarkersFromParse() {
        storedMarkers = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomLocation");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject markerObject = parseObjects.get(i);
                        LatLng point = new LatLng(markerObject.getDouble("latitude"), markerObject.getDouble("longitude"));
                        MarkerOptions mo = new MarkerOptions().position(point).title(markerObject.getString("location")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mo.snippet("Tap here to remove this marker");
                        storedMarkers.add(map.addMarker(mo));
                    }
                } else {
                    Log.d("Parse Error", "Could not retrieve stored markers");
                }
            }
        });
    }

    /*
     * Generates hashKey based on given object
     */
    private String generatePointKey(Object toGen) {
        int key = String.valueOf(toGen).hashCode();
        return String.valueOf(key);
    }

    /*
     * Saves a new ProximityAlert for the given LatLng point
     */
    private void saveProximityAlertPoint(LatLng point) {
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location==null) {
//            Toast.makeText(this, "No last known location. Aborting...", Toast.LENGTH_LONG).show();
//            return;
//        }
        saveCoordinatesInPreferences((float)point.latitude, (float)point.longitude, generatePointKey(point.latitude), generatePointKey(point.longitude));
        addProximityAlert(point.latitude, point.longitude, getPointId(point));
    }

    /*
     * Generates hashCode for given point
     */
    private int getPointId(LatLng point) {
        String id = String.valueOf(point.latitude) + String.valueOf(point.longitude);
        return id.hashCode();
    }

    /*
     * Adds a new ProximityAlert for the given LatLng point
     */
    private void addProximityAlert(double latitude, double longitude, int id) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(), filter);

    }

    /*
     * Removes a given ProximityAlert from Android OS
     */
    private void removeProximityAlert(int id) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        locationManager.removeProximityAlert(proximityIntent);
    }

    /*
     * Stores given coordinate in sharedPreferences
     */
    private void saveCoordinatesInPreferences(float latitude, float longitude, String latKey, String lonKey) {
        SharedPreferences prefs = this.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putFloat(latKey, latitude);
        prefsEditor.putFloat(lonKey, longitude);
        prefsEditor.apply();
    }
    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                0);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    /*
     * Retrieves given coordinate in sharedPreferences (depreciated)
     */
//    private Location retrievelocationFromPreferences(String latKey, String lonKey) {
//        SharedPreferences prefs = this.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
//        Location location = new Location("POINT_LOCATION");
//        location.setLatitude(prefs.getFloat(latKey, 0));
//        location.setLongitude(prefs.getFloat(lonKey, 0));
//        return location;
//    }
}