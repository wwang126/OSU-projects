package com.example.steammarketapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SavedMarketItemsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MarketItem item);

    @Delete
    void delete(MarketItem item);

    @Query("SELECT * FROM marketItems")
    LiveData<List<MarketItem>> getAllItems();

    @Query("SELECT * FROM marketItems WHERE hash_name = :hashName LIMIT 1")
    LiveData<MarketItem> getItemByHashName(String hashName);
}
