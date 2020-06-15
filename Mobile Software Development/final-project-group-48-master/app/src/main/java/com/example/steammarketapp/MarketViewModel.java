package com.example.steammarketapp;

import android.app.Application;
import android.app.ListActivity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.data.MarketItemRepository;

import java.util.List;

public class MarketViewModel extends AndroidViewModel{
    private MarketItemRepository mRepository;

    public MarketViewModel(Application application){
        super(application);
        mRepository = new MarketItemRepository(application);
    }
    public void insertItem(MarketItem item){
        mRepository.insertItemRepo(item);
    }

    public void deleteItem(MarketItem item){
        mRepository.deleteSavedRepo(item);
    }

    public LiveData<List<MarketItem>> getAllItems() {
        return mRepository.getAllItems();
    }

    public LiveData<MarketItem> getItemByHashName(String hashName){
        return mRepository.getItemByHashName(hashName);
    }
}