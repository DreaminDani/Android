package net.desandoval.apps.imhere.recievers;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Daniel on 2014.12.01..
 */
public class MyPushBroadcastReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // no longer using this class as all location processing is done on the device
        // feel free to delete
    }
}
