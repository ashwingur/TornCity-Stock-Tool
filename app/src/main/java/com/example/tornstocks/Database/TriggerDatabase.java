package com.example.tornstocks.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tornstocks.Models.Trigger;

@Database(entities = {Trigger.class}, version = 1)
public abstract class TriggerDatabase extends RoomDatabase {

    private static TriggerDatabase instance;

    public abstract TriggerDao triggerDao();

    public static synchronized TriggerDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TriggerDatabase.class, "trigger_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
