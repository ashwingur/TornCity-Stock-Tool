package com.example.tornstocks.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("Count", "STARTING SERVICE: ");
            context.startForegroundService(new Intent(context, TriggerCheckerService.class));
        } else {
            context.startService(new Intent(context, TriggerCheckerService.class));
        }
    }
}
