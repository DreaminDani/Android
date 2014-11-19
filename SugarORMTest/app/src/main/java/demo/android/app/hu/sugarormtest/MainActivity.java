package demo.android.app.hu.sugarormtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import demo.android.app.hu.sugarormtest.data.DayItem;


public class MainActivity extends Activity {

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDayObject("12title", new Date(System.currentTimeMillis()));
            }
        });

        Button btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDayItems();
            }
        });

        Button btnDeleteAll = (Button) findViewById(R.id.btnDeleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });
    }

    private void insertDayObject(String title, Date date) {
        DayItem dayItem = new DayItem(title, date);
        dayItem.save();
    }

    private void showDayItems() {
        //List<DayItem> dayItems = DayItem.listAll(DayItem.class);

        List<DayItem> dayItems = DayItem.find(DayItem.class, "title = '12title'");

        tvStatus.setText("");
        for (DayItem di : dayItems) {
            tvStatus.append(di.getTitle()+"\n");
        }
    }

    private void deleteAll() {
        DayItem.deleteAll(DayItem.class);
    }

}
