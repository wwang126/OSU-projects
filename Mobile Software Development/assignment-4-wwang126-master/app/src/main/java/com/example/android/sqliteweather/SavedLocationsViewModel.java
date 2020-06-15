package com.example.android.sqliteweather;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.android.sqliteweather.data.ForecastLocation;
import com.example.android.sqliteweather.data.SavedLocationsRepository;
import com.example.android.sqliteweather.data.SavedLocsDao;

import java.util.List;

public class SavedLocationsViewModel extends AndroidViewModel {
    private SavedLocationsRepository mRepository;

    public  SavedLocationsViewModel(Application application){
        super(application);
        mRepository = new SavedLocationsRepository(application);
    }
    public void insertSavedLocation(ForecastLocation location) {
        mRepository.insertSavedRepo(location);
    }

    public void deleteSavedRepo(ForecastLocation location) {
        mRepository.deleteSavedRepo(location);
    }

    public LiveData<List<ForecastLocation>> getAllRepos() {
        return mRepository.getAllLocations();
    }

    public LiveData<ForecastLocation> getLocationByLocation(String location) {
        return mRepository.getLocationByLocation(location);
    }
}
