package com.example.tornstocks.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tornstocks.Database.TriggerDao;
import com.example.tornstocks.Database.TriggerDatabase;
import com.example.tornstocks.Models.Trigger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriggerRepository {

    private TriggerDao triggerDao;
    private LiveData<List<Trigger>> triggers;

    public TriggerRepository(Application application){
        triggerDao = TriggerDatabase.getInstance(application).triggerDao();
        triggers = triggerDao.getAllTriggers();
    }

    public void insert(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new InsertTriggerRunnable(trigger));
    }

    public void update(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new UpdateTriggerRunnable(trigger));
    }

    public void delete(Trigger trigger){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new DeleteTriggerRunnable(trigger));
    }

    public LiveData<List<Trigger>> getAllTriggers(){
        return triggers;
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
}
