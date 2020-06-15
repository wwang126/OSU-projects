package com.example.android.lifecycleweather.data;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.lifecycleweather.utils.OpenWeatherMapUtils;
import com.example.android.lifecycleweather.utils.NetworkUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

public class ForecastData implements ForecastAsyncTask.Callback {
    private static final String TAG = ForecastData.class.getSimpleName();
    private MutableLiveData<List<ForecastItem>> mForecasts;
    private MutableLiveData<Status> mLoadingStatus;

    public ForecastData() {
        mForecasts = new MutableLiveData<>();
        mForecasts.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<ForecastItem>> getForecasts() {
        return mForecasts;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onForecastsFinished(List<ForecastItem> forecasts) {
        mForecasts.setValue(forecasts);
        if (forecasts != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    public void loadForecasts(String location, String units) {
        String url = OpenWeatherMapUtils.buildForecastURL(location, units);
        mForecasts.setValue(null);
        Log.d(TAG, "executing search with url: " + url);
        mLoadingStatus.setValue(Status.LOADING);
        new ForecastAsyncTask(this).execute(url);
    }
}
