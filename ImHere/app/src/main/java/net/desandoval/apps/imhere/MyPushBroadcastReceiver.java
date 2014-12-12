package net.desandoval.apps.imhere;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Peter on 2014.12.01..
 */
public class MyPushBroadcastReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtras(intent.getExtras());
        i.putExtra("KEY_NEW_MSG",true);

        context.startActivity(i);
    }
}
