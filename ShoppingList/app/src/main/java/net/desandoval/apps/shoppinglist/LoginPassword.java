package net.desandoval.apps.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Daniel on 19/10/2014.
 * Skeleton Code from http://www.tutorialspoint.com/android/android_login_screen.htm
 */
public class LoginPassword extends Activity {

    public static final String PREFERENCES = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    private EditText password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        prefEditor = settings.edit();

        final Boolean isFirstRun = settings.getBoolean("firstRun", true);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.sign_in_button);

        if (isFirstRun) {
            password.setHint("Please Create a Password");
        }



        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isFirstRun) {
                    if (password.getText().toString().length() > 3) {
                        prefEditor.putString("password", password.getText().toString()).apply();
                        prefEditor.putBoolean("firstRun", false).apply();

                        Toast.makeText(getApplicationContext(), "You have created a password for your phone's shopping list\n" +
                                "If you forget it, you will not be able to retrieve it",Toast.LENGTH_LONG).show();


                        Intent i = new Intent(LoginPassword.this, ShoppingList.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    } else {
                        password.setError("Please enter a password that is at least 4 digits");
                    }
                } else {
                    String storedPass = settings.getString("password", "");
                    Log.d("Alert", "Password in storage: " + storedPass);
                    Log.d("Alert", "User password: " + password.getText().toString());
                    if (password.getText().toString().equals(storedPass)) {
                        Intent i = new Intent(LoginPassword.this, ShoppingList.class);
                        startActivity(i);

                        // close this activity
                        finish();
                    } else {
                        password.setText("");
                        password.setError("That password is invalid, please try again");
                    }
                }
            }
        });

    }

}
