package demo.android.app.hu.qrcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MyActivity extends Activity {

    private EditText etData;
    private Button btnReadQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        etData = (EditText) findViewById(R.id.etData);
        btnReadQr = (Button) findViewById(R.id.btnReadQr);

        btnReadQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Handle successful scan
                etData.setText(contents);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(this, "QR cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
