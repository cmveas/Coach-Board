package com.sportcoachhelper.managers;

import android.database.Cursor;
import android.view.LayoutInflater;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.R;
import com.sportcoachhelper.database.DatabaseHelper;
import com.sportcoachhelper.model.Play;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Claudio on 6/1/13.
 */
public class PlaysManager {

    private static final PlaysManager instance = new PlaysManager();
    private final String[] fields;
    private final StorageManager mStorageManager;

    public static PlaysManager getInstance() {
        return instance;
    }

    Hashtable<String,ArrayList<Play>> allPlays = new Hashtable<String, ArrayList<Play>>();


    public PlaysManager(){
        fields = CoachApp.getInstance().getResources().getStringArray(R.array.fields);
        mStorageManager = CoachApp.getInstance().getStorageManager();
        for(String field : fields){
            reloadList(field);
        }

    }


    private void reloadList(String label) {
        allPlays.remove(label);
        ArrayList<Play> plays = new ArrayList<Play>();
        Cursor cursor = mStorageManager.getPlays(label);
        if(cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_NAME);
            int dateIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_DATE);
            int fieldIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_FIELD);
            int fieldTypeIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_FIELD_TYPE);
            int linesArrayIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_LINES_DATA);
            int idIndex = cursor.getColumnIndex(DatabaseHelper.PLAYS_ID);
            while(!cursor.isAfterLast()){
                String name = cursor.getString(nameIndex);
                long date = cursor.getLong(dateIndex);
                long id = cursor.getLong(idIndex);
                String field = cursor.getString(fieldIndex);
                String fieldType = cursor.getString(fieldTypeIndex);
                byte[] linesArray = cursor.getBlob(linesArrayIndex);
                Play play = new Play();
                play.setLinesByteArray(linesArray);
                play.setField(field);
                play.setName(name);
                play.setLastSaved(date);
                play.setFieldType(fieldType);
                play.setId(id);
                plays.add(play);
                cursor.moveToNext();
            }
        }
        if(cursor!=null && !cursor.isClosed()) {
            cursor.close();
        }
        allPlays.put(label,plays);

    }

    public void invalidate(String field) {
        reloadList(field);
    }

    public ArrayList<Play> getPlaysFromField(String field){
       return allPlays.get(field);
    }

    public Play getPlay(String field, long playId) {
        Play result = null;
        ArrayList<Play> plays = getPlaysFromField(field);
        if(plays!=null) {
            for(Play play : plays){
                if(play.getId()==playId) {
                    result = play;
                    break;
                }
            }
        }
        return result;
    }
}
