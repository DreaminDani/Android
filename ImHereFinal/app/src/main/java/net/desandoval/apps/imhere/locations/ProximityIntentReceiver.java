package net.desandoval.apps.imhere.locations;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;

import net.desandoval.apps.imhere.R;

public class ProximityIntentReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_ID = 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;

		Boolean entering = intent.getBooleanExtra(key, false);
		
		if (entering) {
			Log.d(getClass().getSimpleName(), "entering");
            MovementLocationListener.createNotication(context, "You've arrived at a custom location", "Click here to notify your \"ImHere\" group", String.valueOf(NOTIFICATION_ID));
		}
		else {
			Log.d(getClass().getSimpleName(), "exiting");
		}
	}

}
