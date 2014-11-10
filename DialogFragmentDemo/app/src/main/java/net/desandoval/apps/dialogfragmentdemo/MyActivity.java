package net.desandoval.apps.dialogfragmentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MyActivity extends Activity
        implements OptionsFragment.FruitSelectionInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button btnMessage = (Button) findViewById(R.id.btnMessageFragment);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("This is my message!");
            }
        });

        Button btnOptions = (Button) findViewById(R.id.btnOptionsFragment);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OptionsFragment().show(
                        getFragmentManager(),OptionsFragment.TAG);
            }
        });
    }

    private void showMessage(String msg) {
        // display dialog fragment with the message
        MessageFragment dialog = new MessageFragment();
        Bundle b = new Bundle();
        b.putString(MessageFragment.KEY_MSG, msg);
        dialog.setArguments(b);
        dialog.show(getFragmentManager(), MessageFragment.TAG);
    }

    @Override
    public void fruitSelected(String fruit) {
        Toast.makeText(this,"Selected fruit: "+fruit, Toast.LENGTH_LONG).show();
    }
}