package net.desandoval.apps.imhere.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import net.desandoval.apps.imhere.R;
import net.desandoval.apps.imhere.contacts.Contact;
import net.desandoval.apps.imhere.contacts.CustomContactsAdapter;
import net.desandoval.apps.imhere.locations.EditLocations;
import net.desandoval.apps.imhere.locations.MovementLocationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Daniel on 9/12/2014.
 */
public class SendLocation extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    final String TAG = CustomContactsAdapter.class.getSimpleName();

    private List<Contact> mContacts;
    private List<Contact> storedContacts;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private String mMessage;
    private LocationClient mLocationClient;
    private EditText etSendLocationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_location);

        // initialize recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.SendLocationRecyclerList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        populateList();

        etSendLocationMessage = (EditText) findViewById(R.id.etSendLocationMessage);

        // fill EditText with message
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();

        RelativeLayout sendViaSMS = (RelativeLayout) findViewById(R.id.sendViaSMS);
        sendViaSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(mMessage);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();

        super.onStop();
    }

    private void generateMessage(Location lastLocation) {
        mMessage = "Just wanted to let you know that ImHere:\n";
        LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        String address = EditLocations.getMyLocationAddress(location, this);
        if (address != null) {
            mMessage += address;
        } else {
            mMessage += "ENTER YOUR CUSTOM LOCATION NAME HERE";
        }
    }

    private void populateList() {
        // get contacts from Parse
        storedContacts = new ArrayList<>();
        getContactsFromParse();
        mContacts = storedContacts;
        if (mContacts == null) {
            mContacts = new ArrayList<>();
        }
    }

    private void getContactsFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CustomContacts");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseObject obj = parseObjects.get(i);
                        Contact storedContact = new Contact(obj.getString("name"), obj.getString("number"), obj.getString("id"));
                        storedContacts.add(storedContact);
                    }
                } else {
                    Log.d("Parse Error", "Could not retrieve stored markers for deletion - markers not delete from parse");
                }
                CustomContactsAdapter adapter = new CustomContactsAdapter(mContacts, SendLocation.this);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }

//    private LatLng retrievelocationFromPreferences() {
//        SharedPreferences pref = getSharedPreferences("ImHere", Context.MODE_PRIVATE);
//        float lat = pref.getFloat(MovementLocationListener.POINT_LATITUDE_KEY, 0);
//        float lon = pref.getFloat(MovementLocationListener.POINT_LONGITUDE_KEY, 0);
//        Log.d("Retrieved Location", lat + " | " + lon);
//        if (lat == 0 && lon == 0) {
//            return null;
//        } else {
//            return new LatLng(lat, lon);
//        }
//    }

    private void sendSMS(String message) {
        String separator = "; ";
        if(android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")){
            separator = ", ";
        }
        StringBuilder phoneNumbers = new StringBuilder();
        for (int i = 0; i < mContacts.size(); i++) {
            Contact thisContact = mContacts.get(i);
            phoneNumbers.append(thisContact.getNumber());
            phoneNumbers.append(separator);
        }
        try {

            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("address", phoneNumbers.toString());
            sendIntent.putExtra("sms_body", message);
            sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        generateMessage(mLocationClient.getLastLocation());
        etSendLocationMessage.setText(mMessage);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
