package com.example.steammarketapp.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OwnedItemRepository {
    private SavedOwnedItemsDao mDAO;

    public OwnedItemRepository(Application application) {
        OwnedDatabase db = OwnedDatabase.getDatabase(application);
        mDAO = db.savedOwnedItemsDao();
    }

    public void insertItemRepo(OwnedItem item){
        new InsertAsyncTask(mDAO).execute(item);
    }

    public  void deleteSavedRepo(OwnedItem item){
        new DeleteAsyncTask(mDAO).execute(item);
    }

    public LiveData<List<OwnedItem>> getAllItems() {
        return mDAO.getAllItems();
    }

    /**
     * This gets the Market Item based off the hash_name, NOT the name
     */
    public LiveData<List<OwnedItem>> getItemsByHashName(String hashName) {
        return mDAO.getItemsByHashName(hashName);
    }
    /**
     * This gets the Owned Item based off the hash_name, NOT the name
     */
    public LiveData<List<OwnedItem>> getItemByOwnerID(String owner) {
        return mDAO.getItemsByOwnerID(owner);
    }
    /**
     * This gets the Owned Item based off the owner name and hash name
     */
    public LiveData<List<OwnedItem>> getItemsByOwnerIDHashName(String owner_ID, String hashName){
        return mDAO.getItemsByOwnerIDHashName(owner_ID,hashName);
    }

    private static class InsertAsyncTask extends AsyncTask<OwnedItem, Void, Void>{
        private SavedOwnedItemsDao mAsyncTaskDao;
        InsertAsyncTask(SavedOwnedItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected  Void doInBackground(OwnedItem... items){
            mAsyncTaskDao.insert(items[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<OwnedItem, Void, Void>{
        private SavedOwnedItemsDao mAsyncTaskDao;
        DeleteAsyncTask(SavedOwnedItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected  Void doInBackground(OwnedItem... items){
            mAsyncTaskDao.delete(items[0]);
            return null;
        }
    }
}
