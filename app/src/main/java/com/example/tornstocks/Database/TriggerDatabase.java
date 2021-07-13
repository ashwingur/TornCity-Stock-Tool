package com.example.tornstocks.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tornstocks.Models.Trigger;

@Database(entities = {Trigger.class}, version = 1)
public abstract class TriggerDatabase extends RoomDatabase {

    private static TriggerDatabase instance;
}
