package com.example.android.lifecycleweather;

import com.example.android.lifecycleweather.data.ForecastAsyncTask;
import com.example.android.lifecycleweather.data.ForecastData;
import com.example.android.lifecycleweather.data.ForecastItem;
import com.example.android.lifecycleweather.data.Status;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ForecastViewModel extends ViewModel {
    private ForecastData mForecastData;
    private LiveData<List<ForecastItem>> mForecasts;
    private LiveData<Status> mLoadingStatus;

    public ForecastViewModel() {
        mForecastData = new ForecastData();
        mForecasts = mForecastData.getForecasts();
        mLoadingStatus = mForecastData.getLoadingStatus();
    }

    public void loadForecasts(String location, String units) {
        mForecastData.loadForecasts(location, units);
    }

    public LiveData<List<ForecastItem>> getForecasts() {
        return mForecasts;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
