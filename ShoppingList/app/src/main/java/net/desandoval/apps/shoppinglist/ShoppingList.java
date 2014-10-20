package net.desandoval.apps.shoppinglist;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.desandoval.apps.shoppinglist.adapter.ShoppingListAdapter;
import net.desandoval.apps.shoppinglist.data.Item;
import net.desandoval.apps.shoppinglist.data.Item.ItemType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShoppingList extends ListActivity {

    public static final int REQUEST_NEW_PLACE_CODE = 100;
    public static final int CONTEXT_ACTION_DELETE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        List<Item> placesToVisit = new ArrayList<Item>();

        public static final String PREFERENCES = "ShoppingListPrefs";

        SharedPreferences settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();

        String storedJSON = settings.getString("savedList", "");
        JSONObject jObj = new JSONObject();
        JSONArray jsonArray = null;
        try {
            jsonArray = jObj.getJSONArray(storedJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int i = 0;
        while (i < (jsonArray != null ? jsonArray.length() : 0)) {

            JSONObject item = null;
            try {
                item = (JSONObject) JSONArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            keyNames = //list of all serialized keys?

            int myNum = 0;

            try {
                myNum = Integer.parseInt(retrieveFromJSON(item, keyNames).getText().toString());
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

            Item.ItemType itemType;
            String itemName;
            String description;
            Date pickUpDate;
            String store;
            String price;
            long id = i;


            placesToVisit.add(new Item(itemType, "Place1", "Desc1", new Date(System.currentTimeMillis())));
            i++;
        }

        ShoppingListAdapter adapter = new ShoppingListAdapter(getApplicationContext(), placesToVisit);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
    }

    private String retrieveFromJSON(JSONObject item, String q) {
        if (item != null) {
            try {
                return item.getString(q);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                ((ShoppingListAdapter) getListAdapter()).addPlace((Item) data.getSerializableExtra("KEY_ITEM"));
                ((ShoppingListAdapter) getListAdapter()).notifyDataSetChanged();
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
            i.setClass(this, CreateShoppingListActivity.class);
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
            ((ShoppingListAdapter) getListAdapter()).removeItem(info.position);
            ((ShoppingListAdapter) getListAdapter()).notifyDataSetChanged();
        } else {
            return false;
        }
        return true;
    }
}