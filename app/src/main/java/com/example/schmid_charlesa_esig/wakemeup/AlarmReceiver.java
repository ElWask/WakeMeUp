package com.example.schmid_charlesa_esig.wakemeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SCHMID_CHARLESA-ESIG on 30.01.2017.
 */
// Anna xu
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "GG dude");
//      fetch the extra string from the intent
        String getString = intent.getExtras().getString("extra");
        String getName = intent.getExtras().getString("taskNameTrans");
        Log.e("What is the key ?", getString);

            Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
//      pass the extra string from alarmAct to the RingtonePlayingService
            serviceIntent.putExtra("extra", getString);
            serviceIntent.putExtra("nameReTrans", getName);
            serviceIntent.putExtra("titleNotif", getName);
            context.startService(serviceIntent);
    }
}
