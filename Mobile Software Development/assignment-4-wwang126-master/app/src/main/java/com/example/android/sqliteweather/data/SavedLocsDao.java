package com.example.android.sqliteweather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedLocsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (ForecastLocation location);

    @Delete
    void delete(ForecastLocation location);

    @Query("SELECT * FROM locations")
    LiveData<List<ForecastLocation>> getAllLocations();

    @Query("SELECT * FROM locations WHERE location = :location LIMIT 1")
    LiveData<ForecastLocation> getLocationByLocation(String location);
}
