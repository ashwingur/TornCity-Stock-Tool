package com.example.tornstocks.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tornstocks.Database.TriggerDao;
import com.example.tornstocks.Database.TriggerDatabase;
import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.Service.TriggerCheckerService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriggerRepository {
    private static final String TAG = "TriggerRepository";

    private TriggerDao triggerDao;
    private LiveData<List<Trigger>> triggers;
    private List<Trigger> nonLiveTriggers;

    public TriggerRepository(Application application){
        triggerDao = TriggerDatabase.getInstance(application).triggerDao();
        triggers = triggerDao.getAllTriggers();
    }

    public void insert(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new InsertTriggerRunnable(trigger));
        TriggerCheckerService.triggersChanged = true;
    }

    public void update(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new UpdateTriggerRunnable(trigger));
        TriggerCheckerService.triggersChanged = true;
    }

    public void delete(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new DeleteTriggerRunnable(trigger));
        TriggerCheckerService.triggersChanged = true;
    }

    public LiveData<List<Trigger>> getAllTriggers(){ return triggers; }

    public List<Trigger> getAllTriggersNonLive(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new GetNonLiveTriggersRunnable());
        return nonLiveTriggers;
    }

    private class InsertTriggerRunnable implements Runnable{
        private Trigger trigger;

        public InsertTriggerRunnable(Trigger trigger) {
            this.trigger = trigger;
        }

        @Override
        public void run() { triggerDao.insert(trigger); }
    }

    private class UpdateTriggerRunnable implements Runnable{
        private Trigger trigger;

        public UpdateTriggerRunnable(Trigger trigger) {
            this.trigger = trigger;
        }

        @Override
        public void run() { triggerDao.update(trigger); }
    }

    private class DeleteTriggerRunnable implements Runnable{
        private Trigger trigger;

        public DeleteTriggerRunnable(Trigger trigger) {
            this.trigger = trigger;
        }

        @Override
        public void run() { triggerDao.delete(trigger); }
    }

    private class GetNonLiveTriggersRunnable implements Runnable{
        @Override
        public void run() { nonLiveTriggers = triggerDao.getAllTriggersNonLive(); }
    }
}
