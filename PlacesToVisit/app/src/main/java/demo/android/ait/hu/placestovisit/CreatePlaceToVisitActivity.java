package demo.android.ait.hu.placestovisit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import demo.android.ait.hu.placestovisit.data.Place;


public class CreatePlaceToVisitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place_to_visit);

        final Spinner spinnerPlaceType = (Spinner) findViewById(R.id.spinnerPlaceType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.placetypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceType.setAdapter(adapter);

        final EditText etPlace = (EditText) findViewById(R.id.etPlaceName);
        final EditText etPlaceDesc = (EditText) findViewById(R.id.etPlaceDesc);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentResult = new Intent();
                intentResult.putExtra("KEY_PLACE",
                        new Place(Place.PlaceType.fromInt(spinnerPlaceType.getSelectedItemPosition())
                                ,etPlace.getText().toString(),etPlaceDesc.getText().toString(),
                                new Date(System.currentTimeMillis())));
                setResult(RESULT_OK,intentResult);
                finish();
            }
        });
    }
}
