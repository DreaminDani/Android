package net.desandoval.apps.broadcastrecieverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Daniel on 10/11/2014.
 */
public class BRAirPlaneMode extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean state = intent.getExtras().getBoolean("state");

        Toast.makeText(context, "AIRPLANE changed to " +state,Toast.LENGTH_LONG).show();
    }
}
