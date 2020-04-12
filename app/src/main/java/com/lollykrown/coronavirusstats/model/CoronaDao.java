package com.lollykrown.coronavirusstats.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CoronaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<Corona> coronas);

    @Query("SELECT * FROM Corona ORDER BY cases DESC")
    LiveData<List<Corona>> getAllDataLiveData();

    @Query("SELECT * FROM Corona ORDER BY cases DESC")
    List<Corona> getAllData();

    @Query("DELETE FROM Corona")
    void deleteAll();
}
