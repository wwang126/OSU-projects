package com.example.steammarketapp.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MarketItemRepository {
    private SavedMarketItemsDao mDAO;

    public MarketItemRepository(Application application) {
        MarketDatabase db = MarketDatabase.getDatabase(application);
        mDAO = db.savedMarketItemsDao();
    }

    public void insertItemRepo(MarketItem item){
        new InsertAsyncTask(mDAO).execute(item);
    }

    public  void deleteSavedRepo(MarketItem item){
        new DeleteAsyncTask(mDAO).execute(item);
    }

    public LiveData<List<MarketItem>> getAllItems() {
        return mDAO.getAllItems();
    }

    /**
     * This gets the Market Item based off the hash_name, NOT the name
     */
    public LiveData<MarketItem> getItemByHashName(String hashName) {
        return mDAO.getItemByHashName(hashName);
    }


    private static class InsertAsyncTask extends AsyncTask<MarketItem, Void, Void>{
        private SavedMarketItemsDao mAsyncTaskDao;
        InsertAsyncTask(SavedMarketItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected  Void doInBackground(MarketItem... items){
            mAsyncTaskDao.insert(items[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<MarketItem, Void, Void>{
        private SavedMarketItemsDao mAsyncTaskDao;
        DeleteAsyncTask(SavedMarketItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected  Void doInBackground(MarketItem... items){
            mAsyncTaskDao.delete(items[0]);
            return null;
        }
    }
}
