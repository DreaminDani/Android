package demo.android.ait.hu.startedservicedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Peter on 2014.11.10..
 */
public class MyTimeService extends Service {

    public static final int NOTIFICATION_ID = 101;

    private boolean enabled = false;

    private class MyTimeThread extends Thread {
        public void run() {
            Handler hMain = new Handler(MyTimeService.this.getMainLooper());

            //int counter = 0;

            while(enabled) {
                //counter++;

                hMain.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyTimeService.this,
                                new Date(System.currentTimeMillis()).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                updateNotification(
                        new Date(System.currentTimeMillis()).toString());

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*if (counter>3) {
                    stopSelf();
                }*/
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!enabled) {
            startForeground(NOTIFICATION_ID, getMyNotification("Loading..."));

            enabled = true;
            new MyTimeThread().start();
        }

        return START_STICKY;
    }

    private Notification getMyNotification(String text) {

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                i,
                PendingIntent.FLAG_CANCEL_CURRENT
                );

        Notification notif = new Notification.Builder(this)
                .setContentTitle("Title")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pi)
                .build();

        return notif;
    }

    private void updateNotification(String text) {
        Notification notif = getMyNotification(text);
        NotificationManager nfm = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        nfm.notify(NOTIFICATION_ID, notif);
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
