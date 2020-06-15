package com.example.steammarketapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SavedOwnedItemsDao {
    @Insert
    void insert(OwnedItem repo);

    @Delete
    void delete(OwnedItem repo);

    @Query("SELECT * FROM ownedItems")
    LiveData<List<OwnedItem>> getAllItems();

    @Query("SELECT * FROM ownedItems WHERE hash_name = :hashName")
    LiveData<List<OwnedItem>> getItemsByHashName(String hashName);

    @Query("SELECT * FROM ownedItems WHERE ownerID = :owner_ID")
    LiveData<List<OwnedItem>> getItemsByOwnerID(String owner_ID);

    @Query("SELECT * FROM ownedItems WHERE ownerID = :owner_ID AND hash_name = :hashName")
    LiveData<List<OwnedItem>> getItemsByOwnerIDHashName(String owner_ID, String hashName);
}
