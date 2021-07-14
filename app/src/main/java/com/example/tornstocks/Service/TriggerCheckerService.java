package com.example.tornstocks.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tornstocks.R;

public class TriggerCheckerService extends Service {
    private static final String TAG = "TriggerCheckerService";

    public static final int delay = 5000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Initiated");
        HandlerThread handlerThread = new HandlerThread("background-thread");
        handlerThread.start();
        final MediaPlayer mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        final Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                Log.d(TAG, "run: YES");
                mp.start();
                // call some methods here
                handler.postDelayed(this, delay);

            }
        }, delay);
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "onStartCommand: LOL");
//                handler.postDelayed(this, delay);
//            }
//        });
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
