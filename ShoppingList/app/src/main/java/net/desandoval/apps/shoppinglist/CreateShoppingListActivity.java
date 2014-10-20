package net.desandoval.apps.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import net.desandoval.apps.shoppinglist.data.Item;


public class CreateShoppingListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

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
                Intent intentResult = new Intent();
                intentResult.putExtra("KEY_ITEM",
                        new Item(Item.ItemType.fromInt(spinnerPlaceType.getSelectedItemPosition())
                                ,etItem.getText().toString(),etItemDesc.getText().toString(),
                                new Date(System.currentTimeMillis()),etStore.getText().toString(),
                                etPrice.getText().toString());
                setResult(RESULT_OK,intentResult);
                finish();
            }
        });
    }
}
