package demo.android.app.hu.bindserivcegetfreespace;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;


public class MyActivity extends Activity {

    private FreeSpaceSerivce myService;
    private boolean serviceBounded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Button btnFreeSpace = (Button) findViewById(R.id.btnFreeSpace);
        btnFreeSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceBounded && myService != null) {
                    long freeMB = myService.getFreeSpace();
                    long freeGB = freeMB / 1024;
                    freeMB = freeMB % 1024;
                    btnFreeSpace.setText(freeGB+" GB and "+freeMB+" MB");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, FreeSpaceSerivce.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(myConnection);
    }

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            FreeSpaceSerivce.MyFreeSpaceBinder binder =
                    (FreeSpaceSerivce.MyFreeSpaceBinder) service;
            myService = binder.getService();
            serviceBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBounded = false;
        }

    };

}
