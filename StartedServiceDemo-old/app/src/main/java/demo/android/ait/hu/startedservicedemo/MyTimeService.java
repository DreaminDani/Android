package demo.android.ait.hu.startedservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Peter on 2014.11.10..
 */
public class MyTimeService extends Service {

    private boolean enabled = false;

    private class MyTimeThread extends Thread {
        public void run() {
            Handler hMain = new Handler(MyTimeService.this.getMainLooper());

            while(enabled) {
                hMain.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyTimeService.this,
                                new Date(System.currentTimeMillis()).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

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
        enabled = true;
        new MyTimeThread().start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        enabled = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
