package net.desandoval.apps.imhere.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Daniel on 20/11/2014.
 */
public class StartAtBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent i = new Intent(context, MainActivity.class); // run gps reciever
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);

        // TODO: Use proximity alert for LEAVING a certain distance

    }
}
