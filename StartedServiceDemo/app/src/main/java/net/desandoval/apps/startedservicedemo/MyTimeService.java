package net.desandoval.apps.startedservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Daniel on 10/11/2014.
 */
public class MyTimeService extends Service {

    private boolean enabled = false;

    private class MyTimeThread extends Thread {
        public void run() {
            while(enabled) {
                Toast.makeText(MyTimeService.this,
                        new Date(System.currentTimeMillis()).toString(),
                        Toast.LENGTH_SHORT).show();

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}