package net.desandoval.apps.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Created by Daniel on 1/10/2014.
 */
public class Splash extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // initialize number pickers
        final NumberPicker pickGridSize = (NumberPicker) findViewById(R.id.pickGridSize);
        pickGridSize.setMaxValue(10);
        pickGridSize.setMinValue(5);
        pickGridSize.setValue(5);

        final NumberPicker pickNumBombs = (NumberPicker) findViewById(R.id.pickNumBombs);
        pickNumBombs.setMaxValue(20);
        pickNumBombs.setMinValue(1);
        pickNumBombs.setValue(5);

        // start the game
        Button startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MinesweeperModel.getInstance().resetModel(pickGridSize.getValue(), pickNumBombs.getValue());
                Intent myIntent = new Intent(Splash.this, MinesweeperGame.class);
                startActivity(myIntent);
            }
        });

        // show "help" toast message
        Button helpBtn = (Button) findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Click a box to check if there's a mine beneath.\n-Flag boxes you think are bombs\n-The numbers that appear will tell you how many bombs are nearby",
                        Toast.LENGTH_LONG).show();
            }
        });

        // show "about" toast message
        Button aboutBtn = (Button) findViewById(R.id.about);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Copyright 2014 Daniel Sandoval\nAIT-Budapest\n(Skeleton stolen from TicTacToe example)", Toast.LENGTH_LONG).show();
            }
        });
    }
}
