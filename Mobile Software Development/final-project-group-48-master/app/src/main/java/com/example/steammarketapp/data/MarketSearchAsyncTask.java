package com.example.steammarketapp.data;

import android.os.AsyncTask;
import com.example.steammarketapp.utils.NetworkUtils;
import com.example.steammarketapp.utils.SteamMarketUtils;

import java.io.IOException;
import java.util.List;


public class MarketSearchAsyncTask extends AsyncTask<String, Void, String> {
    private Callback mCallback;

    public interface Callback {
        void onSearchFinished(List<MarketItem> searchResults);
    }

    public MarketSearchAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String searchResults = null;
        try {
            searchResults = NetworkUtils.doHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        List<MarketItem> searchResults = null;
        if (s != null) {
            searchResults = SteamMarketUtils.parseItemJSON(s);
        }
        mCallback.onSearchFinished(searchResults);
    }
}
