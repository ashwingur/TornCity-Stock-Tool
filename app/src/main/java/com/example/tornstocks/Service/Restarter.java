package com.example.tornstocks.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Restarter extends BroadcastReceiver {
    // Received broadcast to restart the TriggerCheckerService because it has been closed
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, TriggerCheckerService.class));
        } else {
            context.startService(new Intent(context, TriggerCheckerService.class));
        }
    }
}
