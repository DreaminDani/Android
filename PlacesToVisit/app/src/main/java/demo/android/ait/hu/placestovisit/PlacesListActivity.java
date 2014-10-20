package demo.android.ait.hu.placestovisit;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import demo.android.ait.hu.placestovisit.adapter.PlacesToVisitAdapter;
import demo.android.ait.hu.placestovisit.data.Place;


public class PlacesListActivity extends ListActivity {

    public static final int REQUEST_NEW_PLACE_CODE = 100;
    public static final int CONTEXT_ACTION_DELETE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        List<Place> placesToVisit = new ArrayList<Place>();
        placesToVisit.add(new Place(Place.PlaceType.LANDSCAPE, "Place1", "Desc1", new Date(System.currentTimeMillis())));
        placesToVisit.add(new Place(Place.PlaceType.CITY, "Place2", "Desc2", new Date(System.currentTimeMillis())));
        placesToVisit.add(new Place(Place.PlaceType.BUILDING, "Place3", "Desc3", new Date(System.currentTimeMillis())));

        PlacesToVisitAdapter adapter = new PlacesToVisitAdapter(getApplicationContext(), placesToVisit);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                ((PlacesToVisitAdapter) getListAdapter()).addPlace((Place) data.getSerializableExtra("KEY_PLACE"));
                ((PlacesToVisitAdapter) getListAdapter()).notifyDataSetChanged();
                Toast.makeText(this, "Place added to the list!", Toast.LENGTH_LONG).show();
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.places_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_place) {
            Intent i = new Intent();
            i.setClass(this, CreatePlaceToVisitActivity.class);
            startActivityForResult(i, REQUEST_NEW_PLACE_CODE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Menu");
        menu.add(0, CONTEXT_ACTION_DELETE, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CONTEXT_ACTION_DELETE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ((PlacesToVisitAdapter) getListAdapter()).removeItem(info.position);
            ((PlacesToVisitAdapter) getListAdapter()).notifyDataSetChanged();
        } else {
            return false;
        }
        return true;
    }
}
