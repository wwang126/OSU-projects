package com.example.steammarketapp;

import android.app.Application;
import android.app.ListActivity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.steammarketapp.data.OwnedItem;
import com.example.steammarketapp.data.OwnedItemRepository;

import java.util.List;

public class OwnedViewModel extends AndroidViewModel{
    private OwnedItemRepository mRepository;

    public OwnedViewModel(Application application){
        super(application);
        mRepository = new OwnedItemRepository(application);
    }
    public void insertItem(OwnedItem item){
        mRepository.insertItemRepo(item);
    }

    public void deleteItem(OwnedItem item){
        mRepository.deleteSavedRepo(item);
    }

    public LiveData<List<OwnedItem>> getAllItems() {
        return mRepository.getAllItems();
    }

    public LiveData<List<OwnedItem>> getItemsByHashName(String hashName){
        return mRepository.getItemsByHashName(hashName);
    }
    public LiveData<List<OwnedItem>> getItemsByOwnerID(String ownerID){
        return mRepository.getItemByOwnerID(ownerID);
    }

    public LiveData<List<OwnedItem>> getItemsByOwnerIDHashName(String ownerID, String hashName){
        return mRepository.getItemsByOwnerIDHashName(ownerID, hashName);
    }
}