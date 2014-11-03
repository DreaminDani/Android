package net.desandoval.apps.shoppinglist;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import net.desandoval.apps.shoppinglist.adapter.ShoppingListAdapter;
import net.desandoval.apps.shoppinglist.data.DatabaseHelper;
import net.desandoval.apps.shoppinglist.data.Item;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class ShoppingList extends ListActivity {
    private DatabaseHelper databaseHelper = null;
    private Dao<Item, Integer> ItemDAO = null;
    List<Item> itemsList = null;


    public static final int REQUEST_NEW_PLACE_CODE = 100;
    public static final int CONTEXT_ACTION_DELETE = 10;

    public boolean sortedByEntry = true;

    /*
     * Instantiates variables and handle onClick events
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        final Button btnSort = (Button) findViewById(R.id.btnSort);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Item i : ItemDAO) {
                    try {
                        ItemDAO.delete(i);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                onStart();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortedByEntry) {
                    btnSort.setText("Sort Items by:\nOrder Entered");
                    Collections.sort(itemsList);
                    sortedByEntry = false;
                    ((ShoppingListAdapter) getListAdapter()).notifyDataSetChanged();
                }else{
                    btnSort.setText("Sort Items by:\nStore (Location)");
                    Collections.sort(itemsList, new Comparator<Item>() {
                        public int compare(Item m1, Item m2) {
                            return m1.getPickUpDate().compareTo(m2.getPickUpDate());
                        }
                    });
                    sortedByEntry = true;
                    ((ShoppingListAdapter) getListAdapter()).notifyDataSetChanged();
                }
            }
        });
    }

    /*
     * Instantiates database, adapter, and itemsList. Builds list and generates ListView.
     */
    @Override
    protected void onStart(){
        super.onStart();

        itemsList = new ArrayList<Item>();

        databaseHelper = new DatabaseHelper(this);

        buildList();

        final ShoppingListAdapter adapter = new ShoppingListAdapter(getApplicationContext(), itemsList);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
    }

    /*
     * Closes Database instance, if necessary
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    /*
     * Saves local checkbox (variable value) to ORM database during onPause event
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            updateCheckBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Saves local checkbox (variable value) to ORM database. Used in onPause.
     */
    public void updateCheckBox() throws SQLException {
        ShoppingListAdapter adapter = (ShoppingListAdapter) getListAdapter();
        UpdateBuilder<Item, Integer> updateBuilder = getItemDAO().updateBuilder();

        for (int i = 0; i < adapter.getCount(); i++) {
            Item tempItem = (Item) adapter.getItem(i);
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id", tempItem.getId());
            // update the value of your field(s)
            updateBuilder.updateColumnValue("checked" /* column */, tempItem.getChecked() /* value */);
            updateBuilder.update();
        }
    }

    /*
     * Generates new itemsList from stored Items in the ORM database
     */
    public void buildList() {
        itemsList = new ArrayList<Item>();
        try {
            for (Item p : queryAllItem()) { //iterate through stored list and add to itemsList List
                itemsList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Returns ArrayList<Item> from ORM Database
     */
    public ArrayList<Item> queryAllItem() throws SQLException{
        return (ArrayList<Item>) getItemDAO().queryForAll();
    }

    /*
     * Returns local DAO from the current databaseHelper
     */
    public Dao<Item, Integer> getItemDAO() throws SQLException {
        if (ItemDAO == null) {
            ItemDAO = databaseHelper.getDao(Item.class);
        }

        return ItemDAO;
    }

    /*
     * Displays result of NEW ITEM activity to user in Toast message
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                ((ShoppingListAdapter) getListAdapter()).addItem((Item) data.getSerializableExtra("KEY_ITEM"));
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
            ShoppingListAdapter tempAdapter = (ShoppingListAdapter) getListAdapter();
            Item tempItem = (Item) (tempAdapter).getItem(info.position);

            try {
                ItemDAO.delete(tempItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            (tempAdapter).removeItem(info.position);
            (tempAdapter).notifyDataSetChanged();


        } else {
            return false;
        }
        return true;
    }
}