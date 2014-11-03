package net.desandoval.apps.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.desandoval.apps.shoppinglist.data.DatabaseHelper;
import net.desandoval.apps.shoppinglist.data.Item;

/**
 * Created by Daniel Sandoval on 2014.10.21
 * Modified from Peter's PlaceToVisit Example in class on 2014.10.08..
 */
public class CreateShoppingListActivity extends Activity {

    private DatabaseHelper databaseHelper = null;
    private Dao<Item, Integer> ItemDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        databaseHelper = new DatabaseHelper(this);

        final Spinner spinnerPlaceType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceType.setAdapter(adapter);

        final EditText etItem = (EditText) findViewById(R.id.etItemName);
        final EditText etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        final EditText etStore = (EditText) findViewById(R.id.etStoreName);
        final EditText etPrice = (EditText) findViewById(R.id.etPrice);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new Item
                Intent intentResult = new Intent();
                intentResult = createItem(
                        new Item(Item.ItemType.fromInt(spinnerPlaceType.getSelectedItemPosition())
                                ,etItem.getText().toString(),etItemDesc.getText().toString(),
                                new Date(System.currentTimeMillis()),etStore.getText().toString(),
                                etPrice.getText().toString(),-1,false), intentResult);
                setResult(RESULT_OK,intentResult);
                finish();
            }
        });
    }

    /*
     * Adds passed Item to ORM database
     */
    public Intent createItem(Item p, Intent intent) {
        intent.putExtra("KEY_ITEM",p);
        try {
            getItemDAO().create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return intent;
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

}
