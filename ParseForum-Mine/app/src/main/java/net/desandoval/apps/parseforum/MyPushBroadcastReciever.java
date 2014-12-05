package net.desandoval.apps.parseforum;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Daniel on 1/12/2014.
 */
public class MyPushBroadcastReciever extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // open the activity in a new task (run this activity after broadcast has finished)
        i.putExtras(intent.getExtras());
        i.putExtra("KEY_NEW_MSG", true);
        // to create your own notifications, use the notification from the StartedServiceDemo
        context.startActivity(i);
    }
}
