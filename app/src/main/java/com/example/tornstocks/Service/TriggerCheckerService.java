package com.example.tornstocks.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.R;
import com.example.tornstocks.Repositories.ApiKeyRepository;
import com.example.tornstocks.Repositories.StockRepository;
import com.example.tornstocks.Repositories.TriggerRepository;
import com.example.tornstocks.StockListActivity;
import com.example.tornstocks.Utils.Credentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TriggerCheckerService extends Service {
    private static final String TAG = "TriggerCheckerService";
    private int reqCodeCounter = 3; // Start from 3 since foreground and myownforeground use channels 1,2
    public static final int delay = 1000 * 60; // 60 seconds between checks since that is how often the stock updates
    private Handler handler = new Handler();
    private TriggerRepository triggerRepository;
    private List<Trigger> currentTriggers = new ArrayList<>();
    private List<Stock> currentStocks = new ArrayList<>();
    public static boolean triggersChanged = true;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // If currentTriggers has not yet been fetched, or it has been updated
            if (currentTriggers == null || triggersChanged){
                Log.d(TAG, "run: CHANGED");
                currentTriggers = triggerRepository.getAllTriggersNonLive();
                triggersChanged = false;
            }
            // Query the stocks API to get the latest stock data
            StockRepository.getInstance().queryStocks(ApiKeyRepository.getApiKey(TriggerCheckerService.this), null);
            currentStocks = StockRepository.getInstance().getStocks().getValue();

            // Check if a trigger has been reached, if it has then delete it and send a notification.
            if (currentTriggers != null && currentStocks != null){
                for (Trigger t : currentTriggers){
                    for (Stock s : currentStocks){
                        if (t.getStock_id() == s.getStock_id()) {
                            if (t.isIs_above() && (s.getCurrent_price() >= t.getTrigger_price())){
                                showNotification(TriggerCheckerService.this, "Torn Stocks"
                                        , String.format("%s is now above %.2f", t.getAcronym(), t.getTrigger_price())
                                        , new Intent(getApplicationContext(), StockListActivity.class),reqCodeCounter++);
                                triggerRepository.delete(t);
                                triggersChanged = true;
                            } else if (!t.isIs_above() && (s.getCurrent_price() <= t.getTrigger_price())){
                                showNotification(TriggerCheckerService.this, "Torn Stocks"
                                        , String.format("%s is now below %.2f", t.getAcronym(), t.getTrigger_price())
                                        , new Intent(getApplicationContext(), StockListActivity.class),reqCodeCounter++);
                                triggerRepository.delete(t);
                                triggersChanged = true;
                            }
                            break;
                        }
                    }
                }
            }
            // Repeat again after the delay
            handler.postDelayed(this, delay);
        }
    };

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        // This method sends a notification to the user
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "Trigger Alert";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        notificationBuilder.setVibrate(new long[]{0, 500, 200, 500, 200, 1000}); // Vibrate pattern [delay, vibrate, delay, vibrate...]
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For build versions greater than oreo we must create a notification channel as well
            CharSequence name = "Stock Trigger Alert";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id
        Log.d("showNotification", "showNotification: " + reqCode);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O) // Build versions Oreo or higher
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // Available to see on lock screen

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Stocks are being monitored")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        triggerRepository = new TriggerRepository(getApplication());
        triggersChanged = true;
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    public void startTimer() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable , delay);
    }

    public void stopTimerTask() {
        handler.removeCallbacks(runnable);
        handler = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
