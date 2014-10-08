package net.desandoval.apps.stopwatch;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.Date;


public class StopWatchActivity extends Activity {

    private Date startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        final LinearLayout container = (LinearLayout) findViewById(
                R.id.scroll);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        final Button btnPick = (Button) findViewById(R.id.btnPick);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = new Date(System.currentTimeMillis());
                btnPick.setEnabled(true);
                container.removeAllViews();
            }
        });

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date pickDate = new Date(System.currentTimeMillis());

                long diff = pickDate.getTime()-startTime.getTime();
                int diffInSec = (int)diff/1000;

                LayoutInflater inflater = (LayoutInflater) getSystemService(
                        LAYOUT_INFLATER_SERVICE);

                View lineTime = inflater.inflate(
                        R.layout.line_time, null, false);

                TextView tvTime = (TextView) lineTime.findViewById(R.id.tvTime);
                tvTime.setText(diffInSec+" sec");
                tvTime.setTextSize(25);

                container.addView(lineTime);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stop_watch, menu);
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