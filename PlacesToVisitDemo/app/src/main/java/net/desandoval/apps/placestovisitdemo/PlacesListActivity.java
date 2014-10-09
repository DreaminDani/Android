package net.desandoval.apps.placestovisitdemo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.desandoval.apps.placestovisitdemo.Adapter.PlacesAdapter;
import net.desandoval.apps.placestovisitdemo.data.Place;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PlacesListActivity extends ListActivity {

    public static final int REQUEST_NEW_PLACE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Place> placesList = new ArrayList<Place>();
        placesList.add(new Place(Place.PlaceCategory.LANDSCAPE,
                "New York","Des", new Date(System.currentTimeMillis())));
        placesList.add(new Place(Place.PlaceCategory.LANDSCAPE,
                "Bejing","Des2", new Date(System.currentTimeMillis())));

        setListAdapter(new PlacesAdapter(getApplicationContext(),placesList));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place p = (Place) data.getSerializableExtra("KEY_PLACE");

            ((PlacesAdapter)getListAdapter()).addPlace(p);
            ((PlacesAdapter)getListAdapter()).notifyDataSetChanged();

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this,"Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_new_place) {
            Intent i = new Intent();
            i.setClass(this,createPlace.class);
            startActivityForResult(i,REQUEST_NEW_PLACE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
