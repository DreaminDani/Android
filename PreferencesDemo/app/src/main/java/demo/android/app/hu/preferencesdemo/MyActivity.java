package demo.android.app.hu.preferencesdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;


public class MyActivity extends Activity {

    public static final String SP_DATA = "SP_DATA";
    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_ET_DATA = "KEY_ET_DATA";


    private TextView tvLastRun;
    private EditText etData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        tvLastRun = (TextView) findViewById(R.id.tvLastRun);
        etData = (EditText) findViewById(R.id.etData);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sp = getSharedPreferences(SP_DATA,MODE_PRIVATE);

        tvLastRun.setText(sp.getString(KEY_DATE,"never"));
        etData.setText(sp.getString(KEY_ET_DATA,"no data"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sp = getSharedPreferences(SP_DATA,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_DATE,new Date(System.currentTimeMillis()).toString());
        editor.putString(KEY_ET_DATA,etData.getText().toString());

        editor.commit();
    }
}
