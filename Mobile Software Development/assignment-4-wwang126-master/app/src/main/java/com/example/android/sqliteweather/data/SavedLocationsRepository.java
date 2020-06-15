package com.example.android.sqliteweather.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class SavedLocationsRepository {
    private SavedLocsDao mDAO;

    public SavedLocationsRepository(Application application) {
        LocationDatabase db = LocationDatabase.getDatabase(application);
        mDAO = db.savedLocsDao();
    }

    public void insertSavedRepo(ForecastLocation location) {
        new InsertAsyncTask(mDAO).execute(location);
    }

    public void deleteSavedRepo(ForecastLocation location) {
        new DeleteAsyncTask(mDAO).execute(location);
    }

    public LiveData<List<ForecastLocation>> getAllLocations() {
        return mDAO.getAllLocations();
    }

    public LiveData<ForecastLocation> getLocationByLocation(String location) {
        return mDAO.getLocationByLocation(location);
    }

    private static class InsertAsyncTask extends AsyncTask<ForecastLocation, Void, Void> {
        private SavedLocsDao mAsyncTaskDAO;
        InsertAsyncTask(SavedLocsDao dao) {
            mAsyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(ForecastLocation... locations) {
            mAsyncTaskDAO.insert(locations[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<ForecastLocation, Void, Void> {
        private SavedLocsDao mAsyncTaskDAO;
        DeleteAsyncTask(SavedLocsDao dao) {
            mAsyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(ForecastLocation... locations) {
            mAsyncTaskDAO.delete(locations[0]);
            return null;
        }
    }
}
