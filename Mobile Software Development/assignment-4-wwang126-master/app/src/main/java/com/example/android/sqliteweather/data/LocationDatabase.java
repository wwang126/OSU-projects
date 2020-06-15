package com.example.android.sqliteweather.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;
import android.location.Location;

@Database(entities = {ForecastLocation.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract SavedLocsDao savedLocsDao();
    private static volatile LocationDatabase INSTANCE;

    static LocationDatabase getDatabase (final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LocationDatabase.class,
                            "github_repos_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
