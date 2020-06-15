package com.example.steammarketapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.regex.MatchResult;

@Database(entities = {MarketItem.class}, version = 1)
public abstract class MarketDatabase extends RoomDatabase {
    public abstract SavedMarketItemsDao savedMarketItemsDao();
    private static volatile MarketDatabase INSTANCE;

    static MarketDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MarketDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MarketDatabase.class,
                            "market_items_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}