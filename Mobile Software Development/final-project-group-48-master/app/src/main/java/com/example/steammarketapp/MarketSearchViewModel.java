package com.example.steammarketapp;

import android.view.View;

import com.example.steammarketapp.data.MarketItem;
import com.example.steammarketapp.data.MarketSearchRepository;
import com.example.steammarketapp.data.Status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MarketSearchViewModel extends ViewModel {
    private MarketSearchRepository mRepository;
    private LiveData<List<MarketItem>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public MarketSearchViewModel() {
        mRepository = new MarketSearchRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String query, String type, String rarity) throws UnsupportedEncodingException {
        mRepository.loadSearchResults(query, type, rarity);
    }

    public LiveData<List<MarketItem>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

}
