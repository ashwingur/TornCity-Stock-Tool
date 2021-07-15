package com.example.tornstocks.Database;

// Database access object

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tornstocks.Models.Trigger;

import java.util.List;

@Dao
public interface TriggerDao {

    @Insert
    void insert(Trigger trigger);

    @Update
    void update(Trigger trigger);

    @Delete
    void delete(Trigger trigger);

    @Query("SELECT * FROM trigger_table ORDER BY acronym ASC")
    LiveData<List<Trigger>> getAllTriggers();

    @Query("SELECT * FROM trigger_table")
    List<Trigger> getAllTriggersNonLive();
}
