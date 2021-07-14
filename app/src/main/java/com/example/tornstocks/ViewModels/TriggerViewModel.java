package com.example.tornstocks.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.Repositories.TriggerRepository;

import java.util.List;

public class TriggerViewModel extends AndroidViewModel {
    private TriggerRepository triggerRepository;
    private LiveData<List<Trigger>> triggers;

    public TriggerViewModel(@NonNull Application application) {
        super(application);
        triggerRepository = new TriggerRepository(application);
        triggers = triggerRepository.getAllTriggers();
    }

    public void insert(Trigger trigger){ triggerRepository.insert(trigger); }
    public void delete(Trigger trigger){ triggerRepository.delete(trigger); }
    public void update(Trigger trigger){ triggerRepository.update(trigger); }

    public LiveData<List<Trigger>> getTriggers() { return triggers; }
}
