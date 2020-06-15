package com.example.steammarketapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OwnedItem.class}, version = 1)
public abstract class OwnedDatabase extends RoomDatabase {
    public abstract SavedOwnedItemsDao savedOwnedItemsDao();
    private static volatile OwnedDatabase INSTANCE;

    static OwnedDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OwnedDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            OwnedDatabase.class,
                            "owned_items_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
