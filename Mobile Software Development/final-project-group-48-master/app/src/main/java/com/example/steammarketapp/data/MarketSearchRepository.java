package com.example.steammarketapp.data;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.steammarketapp.utils.SteamMarketUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MarketSearchRepository implements MarketSearchAsyncTask.Callback {
    private static final String TAG = MarketSearchRepository.class.getSimpleName();
    private MutableLiveData<List<MarketItem>> mSearchResults;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentQuery;
    private String mCurrentType;
    private String mCurrentRarity;

    public MarketSearchRepository() {
        mSearchResults = new MutableLiveData<>();
        mSearchResults.setValue(null);
        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<MarketItem>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }


    @Override
    public void onSearchFinished(List<MarketItem> searchResults) {
        mSearchResults.setValue(searchResults);
        if (searchResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String query, String type, String rarity) {
        return !TextUtils.equals(query, mCurrentQuery)
                || !TextUtils.equals(type, mCurrentType)
                || !TextUtils.equals(rarity, mCurrentRarity);
    }

    public void loadSearchResults(String query, String type, String rarity) throws UnsupportedEncodingException {
        if (shouldExecuteSearch(query, type, rarity)) {
            mCurrentQuery = query;
            mCurrentType = type;
            mCurrentRarity = rarity;

            String url = SteamMarketUtils.buildSearchURL(query, type, rarity);
            mSearchResults.setValue(null);
            Log.d(TAG, "executing search with url: " + url);
            mLoadingStatus.setValue(Status.LOADING);
            new MarketSearchAsyncTask(this).execute(url);
        } else {
            Log.d(TAG, "using cached search results");
        }
    }


}
