package net.desandoval.apps.broadcastrecieverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Daniel on 10/11/2014.
 */
public class OutgoingCallReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Toast.makeText(context, "OUTGOING CALL TO " + outNumber, Toast.LENGTH_LONG).show();

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
