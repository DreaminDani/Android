package net.desandoval.apps.imhere.locations;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EditLocations extends ActionBarActivity {

    private GoogleMap map;
    private List<Marker> markerList;
    private List<Marker> storedMarkers;

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

        getMarkersFromParse();

        markerList = storedMarkers;

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                String label = getMyLocationAddress(point);
                MarkerOptions mo = new MarkerOptions().position(point).title(label).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerList.add(map.addMarker(mo));
                ParseObject marker = new ParseObject("CustomLocation");
                marker.put("user", ParseUser.getCurrentUser());
                marker.put("latitude", point.latitude);
                marker.put("longitude", point.longitude);
                marker.put("location", label);
                marker.saveInBackground();
            }
        });
    }

    public String getMyLocationAddress(LatLng point) {
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
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
            // display help model
        }
        if (id   == R.id.action_clear) {
            // delete marker array
            // update interface
        }

        return super.onOptionsItemSelected(item);
    }

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
                        storedMarkers.add(map.addMarker(mo));
                        // this isn't working...
                    }
                } else {
                    Log.d("Parse Error", "Could not retrieve stored markers");
                }
            }
        });
    }
}