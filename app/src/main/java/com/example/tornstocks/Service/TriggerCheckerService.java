package com.example.tornstocks.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tornstocks.Utils.Credentials;

import java.util.Timer;
import java.util.TimerTask;

public class TriggerCheckerService extends Service {
    public int counter=0;
    public static final int delay = 1000 * 60;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i("Count", " " + counter++);
            player.start();
            handler.postDelayed(this, delay);
        }
    };
    private MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("START COMMAND", " started");
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Count", "ON DESTROY");
        stopTimerTask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }



    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        Log.i("Count", "START TIMER");
        handler.removeCallbacks(runnable);
        player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        handler.postDelayed(runnable , delay);
//        if (timer == null){
//            timer = new Timer();
//        }
//        final MediaPlayer player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
//        timerTask = new TimerTask() {
//            public void run() {
//                player.start();
//
//                Log.i("Counter", "=========  "+ (counter++) + " Timer: " + timer);
//            }
//        };
//        timer.schedule(timerTask, 1000, 4000); //
    }

    public void stopTimerTask() {
        Log.i("Count", "STOPPING TIMER");
        handler.removeCallbacks(runnable);
        handler = null;
//        if (timer != null) {
//            Log.i("Count", "TIMER NULL");
//            timer.cancel();
//            timer = null;
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
