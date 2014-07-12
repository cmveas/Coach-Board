package com.sportcoachhelper.managers;

import android.database.Cursor;

import com.sportcoachhelper.database.DatabaseHelper;

/**
 * Created by claudioveas on 12/07/14.
 */
public class StorageManager {

    private static StorageManager sInstance = new StorageManager();
    private DatabaseHelper mDataBaseHelper = DatabaseHelper.getInstance();

    private StorageManager(){

    }

    public static StorageManager getInstance() {
        return sInstance;
    }

    public void insertPlayComponent(String componentType, String data, long playId, int index) {
        mDataBaseHelper.insertPlayComponent(componentType, data.toString(), playId, index);
    }

    public void updatePlay(long id, String name, String field, String fieldType, String s, long time) {
        mDataBaseHelper.updatePlay(id, name, field, fieldType, s, time);
        mDataBaseHelper.deletePlayComponents(id);
    }

    public Cursor getPlayComponents(long id) {
        return mDataBaseHelper.getPlayComponents(id);
    }

    public Cursor getPlays(String court) {
       return mDataBaseHelper.getPlays(court);
    }

    public long insertPlay(String name, String field, String fieldType, String s, long time) {
        return mDataBaseHelper.insertPlay(name, field,fieldType , s, time);
    }
}
