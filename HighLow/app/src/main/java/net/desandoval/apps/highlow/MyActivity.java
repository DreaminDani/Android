package net.desandoval.apps.highlow;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.SystemClock;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.Random;


public class MyActivity extends Activity {

    private int generated = 0;
    private int guesses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        generateNewNumber();

        final EditText etGuess = (EditText) findViewById(R.id.etGuess);
        final TextView tvGuessNr = (TextView) findViewById(R.id.tvGuessNr);
        Button btnGuess = (Button) findViewById(R.id.btnGuess);
        tvGuessNr.setText(getString(R.string.text_nr_guess) + " 0");
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etGuess.getText().toString().trim().length() > 0){
                    guesses++;
                    tvGuessNr.setText(getString(R.string.text_nr_guess) + " " + guesses);

                    int guessNumber = Integer.parseInt(etGuess.getText().toString());
                    etGuess.setText("", TextView.BufferType.EDITABLE);

                    if (guessNumber < generated) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.txt_larger), Toast.LENGTH_LONG).show();
                    } else if (guessNumber > generated) {
                        Toast.makeText(getApplicationContext(),
                                "The number is smaller", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "You have won!", Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(),
                                "You guessed it in " + guesses + " tries!", Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(MyActivity.this, Splash.class);
                        startActivity(myIntent);

                    }

                } else {

                    etGuess.setError("You did not enter a number");
                }
            }
        });
    }

    private void generateNewNumber() {
        Random r = new Random(System.currentTimeMillis());
        generated = r.nextInt(10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}