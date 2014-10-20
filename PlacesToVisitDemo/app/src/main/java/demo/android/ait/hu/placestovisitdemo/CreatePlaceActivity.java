package demo.android.ait.hu.placestovisitdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import demo.android.ait.hu.placestovisitdemo.data.Place;


public class CreatePlaceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        final Spinner spinnerPlacesType = (Spinner) findViewById(
                R.id.spinnerPlaceType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.placetypes_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlacesType.setAdapter(adapter);

        final EditText etPlace = (EditText) findViewById(R.id.etPlaceName);
        final EditText etPlaceDesc = (EditText) findViewById(R.id.etPlaceDesc);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: create new place
                Place.PlaceCategory category = Place.PlaceCategory.LANDSCAPE;
                switch (spinnerPlacesType.getSelectedItemPosition()) {
                    case 0:
                        category = Place.PlaceCategory.LANDSCAPE;
                        break;
                    case 1:
                        category = Place.PlaceCategory.CITY;
                        break;
                    case 2:
                        category = Place.PlaceCategory.BUILDING;
                        break;
                }

                Place p = new Place(category,
                        etPlace.getText().toString(),
                        etPlaceDesc.getText().toString(),
                        new Date(System.currentTimeMillis()));

                Intent iResult = new Intent();
                iResult.putExtra("KEY_PLACE",p);
                setResult(RESULT_OK,iResult);

                finish();
            }
        });
    }
}
