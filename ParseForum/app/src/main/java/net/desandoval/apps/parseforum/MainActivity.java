package net.desandoval.apps.parseforum;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;


public class MainActivity extends Activity {

    private String userName = "Daniel";
    private TextView tvMessages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etMessage = (EditText) findViewById(R.id.etMessage);
        Button btnSend = (Button) findViewById(R.id.btnSend);
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        tvMessages = (TextView) findViewById(R.id.tvMessages);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(etMessage.getText().toString());
                etMessage.setText("");
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMessages();
            }
        });

        ParsePush.subscribeInBackground("AITMessages");

        if (getIntent() != null && getIntent().hasExtra("KEY_NEW_MSG") && getIntent().getBooleanExtra("KEY_NEW_MSG", false)) {
            refreshMessages();
        }

    }

    private void sendMessage(String msg) {
        ParseObject po = new ParseObject("message");
        po.put("user_name", userName);
        po.put("message", msg);
        po.saveInBackground();

        ParsePush push = new ParsePush();
        push.setChannel("AITMessages");
        push.setMessage(userName+": "+msg);
        push.sendInBackground();
    }

    private void refreshMessages() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("message");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e != null) {
                    tvMessages.setText("ERROR: " + e.getMessage());
                }else {
                    tvMessages.setText("");

                    for (ParseObject po : parseObjects) {
                        tvMessages.append(po.getString("user_name") +
                        ": "+po.getString("message")+"\n");
                    }
                }
            }
        });
    }
}

