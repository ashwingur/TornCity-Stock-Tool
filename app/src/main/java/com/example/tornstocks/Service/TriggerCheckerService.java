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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.R;
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
    public int counter=0;
    public static final int delay = 1000 * 10;
    private Handler handler = new Handler();
    private TriggerRepository triggerRepository;
    private List<Trigger> currentTriggers = new ArrayList<>();
    private List<Stock> currentStocks = new ArrayList<>();
    public static boolean triggersChanged = true;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currentTriggers == null || triggersChanged){
                Log.d(TAG, "run: CHANGED");
                currentTriggers = triggerRepository.getAllTriggersNonLive();
                triggersChanged = false;
            }
            StockRepository.getInstance().queryStocks(Credentials.API_KEY);
            currentStocks = StockRepository.getInstance().getStocks().getValue();
            Log.d(TAG, "TRIGGER: " + currentTriggers + " STOCKS " + currentStocks);
            if (currentTriggers != null && currentStocks != null){
                for (Trigger t : currentTriggers){
                    for (Stock s : currentStocks){
                        if (t.getStock_id() == s.getStock_id()) {
                            if (t.isIs_above() && (s.getCurrent_price() > t.getTrigger_price())){
                                Log.d(TAG, t + "ABOVE TRIGGER HIT");
                                player.start();
                                triggerRepository.delete(t);
                                currentTriggers = null;
                            } else if (!t.isIs_above() && (s.getCurrent_price() < t.getTrigger_price())){
                                Log.d(TAG, t + "BELOW TRIGGER HIT");
                                player.start();
                                triggerRepository.delete(t);
                                currentTriggers = null;
                            }
                            break;
                        }
                    }
                }
            }
            showNotification(TriggerCheckerService.this, "Title", "This is a message", new Intent(getApplicationContext(), StockListActivity.class),1);
            //Log.d(TAG, "run: " + triggerRepository.getAllTriggersNonLive());
            handler.postDelayed(this, delay);
        }
    };
    private MediaPlayer player;

    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
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
        triggerRepository = new TriggerRepository(getApplication());
        triggersChanged = true;
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
    }

    public void stopTimerTask() {
        Log.i("Count", "STOPPING TIMER");
        handler.removeCallbacks(runnable);
        handler = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
