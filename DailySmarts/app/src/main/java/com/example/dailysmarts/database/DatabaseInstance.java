package com.example.dailysmarts.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import java.util.List;

public class DatabaseInstance {

    private final AppDatabase db;
    private static DatabaseInstance instance;

    public static DatabaseInstance getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseInstance(context);
        }
        return instance;
    }

    private DatabaseInstance(Context context) {

        db = Room.databaseBuilder(context, AppDatabase.class, "db-smarty.db").build();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateTodaySmarty(String text, String author, String date) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                db.smartDao().updateTodaysSmarty(text, author, date);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertSingleAsync(final SmartEntity smartEntity) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                db.smartDao().insertSingle(smartEntity);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getAll(final DatabaseListener<List<SmartEntity>> callback) {
        new AsyncTask<Void, Void, List<SmartEntity>>() {

            @Override
            protected List<SmartEntity> doInBackground(Void... voids) {
                return db.smartDao().getAll();
            }

            @Override
            protected void onPostExecute(List<SmartEntity> listOfUsers) {
                super.onPostExecute(listOfUsers);
                callback.onDataReceived(listOfUsers);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getSmartiesWithText(final DatabaseListener<List<SmartEntity>> callback, String text) {
        new AsyncTask<Void, Void, List<SmartEntity>>() {

            @Override
            protected List<SmartEntity> doInBackground(Void... voids) {
                return db.smartDao().getSmartiesWithText(text);
            }

            @Override
            protected void onPostExecute(List<SmartEntity> listOfUsers) {
                super.onPostExecute(listOfUsers);
                callback.onDataReceived(listOfUsers);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getTheLastAdded(final DatabaseListener<List<SmartEntity>> callback) {
        new AsyncTask<Void, Void, List<SmartEntity>>() {

            @Override
            protected List<SmartEntity> doInBackground(Void... voids) {
                return db.smartDao().getTheLastAdded();
            }

            @Override
            protected void onPostExecute(List<SmartEntity> listOfUsers) {
                super.onPostExecute(listOfUsers);
                callback.onDataReceived(listOfUsers);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getEntityById(final DatabaseListener<SmartEntity> callback, int id) {
        new AsyncTask<Void, Void, SmartEntity>() {

            @Override
            protected SmartEntity doInBackground(Void... voids) {
                return db.smartDao().findEntityById(id);
            }

            @Override
            protected void onPostExecute(SmartEntity entity) {
                super.onPostExecute(entity);
                callback.onDataReceived(entity);
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteByEntityId(final DatabaseListener<List<SmartEntity>> callback, int id) {
        new AsyncTask<Void, Void, List<SmartEntity>>() {

            @Override
            protected List<SmartEntity> doInBackground(Void... voids) {
                db.smartDao().deleteByEntityId(id);
                return null;
            }

            @Override
            protected void onPostExecute(List<SmartEntity> listOfUsers) {
                super.onPostExecute(listOfUsers);
                callback.onDataReceived(listOfUsers);
            }
        }.execute();
    }

    public interface DatabaseListener<T> {
        void onDataReceived(T data);
    }

}
