package demo.android.ait.hu.placestovisitdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import demo.android.ait.hu.placestovisitdemo.adapter.PlacesAdapter;
import demo.android.ait.hu.placestovisitdemo.data.Place;


public class PlacesListActivity extends ListActivity {

    public static final int REQUEST_NEW_PLACE = 100;
    public static final int CONTEXT_MENU_DELETE = 10;

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
        getMenuInflater().inflate(R.menu.places_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_place) {
            Intent i = new Intent();
            i.setClass(this,CreatePlaceActivity.class);
            startActivityForResult(i,REQUEST_NEW_PLACE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
