package android.ait.hu.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends Activity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = ((MapFragment)getFragmentManager() . findFragmentById(R.id.map)).getMap();

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
        if (id   == R.id.action_add) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(map.getCameraPosition().target);
            marker.title("My new marker");
            marker.snippet("My marker info text");

            marker.icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
            marker.draggable(true);

            map.addMarker(marker);
        }

        return super.onOptionsItemSelected(item);
    }
}
