package com.sportcoachhelper.database;

import com.sportcoachhelper.CoachApp;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Plays Data
     */
	public static final String PLAYS_TABLE_NAME = "plays";
	public static final String PLAYS_ID = "_id";
	public static final String PLAYS_NAME = "name";
	public static final String PLAYS_DATE = "date";
	public static final String PLAYS_FIELD = "field";
	public static final String PLAYS_DATA = "data";
    public static final String PLAYS_VERSION = "data";
	
	private static final String CREATE_TABLE_PLAYS = "create table " + PLAYS_TABLE_NAME + " (" +
													 PLAYS_ID + " integer primary key," +
													 PLAYS_NAME + " text not null," +
													 PLAYS_DATA + " text not null," + 
													 PLAYS_FIELD + " text not null," +
                                                     PLAYS_VERSION + " integer not null," +
													 PLAYS_DATE + " integer);";


    /**
     * Play´s Components Data
     */
    public static final String PLAYS_COMPONENT_TABLE_NAME = "plays_components";
    public static final String PLAYS_COMPONENT_ID = "_id";
    public static final String PLAYS_COMPONENT_SHAPE = "shape";
    public static final String PLAYS_COMPONENT_PLAY_ID = "play_id";
    public static final String PLAYS_COMPONENT_ORDER = "order";
    public static final String PLAYS_COMPONENT_DATA = "data";

    private static final String CREATE_TABLE_PLAYS_COMPONENT = "create table " + PLAYS_COMPONENT_TABLE_NAME + " (" +
                                                        PLAYS_COMPONENT_ID + " integer primary key," +
                                                        PLAYS_COMPONENT_PLAY_ID + " integer not null," +
                                                        PLAYS_COMPONENT_ORDER + " integer not null," +
                                                        PLAYS_COMPONENT_SHAPE + " text not null," +
                                                        PLAYS_COMPONENT_DATA + " text not null);";
			                                         

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	
	}

	private static DatabaseHelper instance;
	
	public static DatabaseHelper getInstance(){
		if(instance==null) {
			PackageInfo pInfo;
			try {
				pInfo = CoachApp.getInstance().getPackageManager().getPackageInfo(CoachApp.getInstance().getApplicationContext().getPackageName(), 0);
			
	 
				instance = new DatabaseHelper(CoachApp.getInstance(), "plays", null, pInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PLAYS);
        db.execSQL(CREATE_TABLE_PLAYS_COMPONENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public long insertPlay(String name, String field, String play, long date){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(PLAYS_NAME, name);
		cv.put(PLAYS_DATE, date);
		cv.put(PLAYS_FIELD, field);
		cv.put(PLAYS_DATA, play);
	
		long id =  db.insert(PLAYS_TABLE_NAME, null, cv);
        return id;
	}


    public void insertPlayComponent(String shape, String data, long playId, int order){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PLAYS_COMPONENT_SHAPE, shape);
        cv.put(PLAYS_COMPONENT_DATA, data);
        cv.put(PLAYS_COMPONENT_ORDER, order);
        cv.put(PLAYS_COMPONENT_PLAY_ID,playId);

        db.insert(PLAYS_TABLE_NAME, null, cv);
    }
	
	public Cursor getPlays(String court){
		SQLiteDatabase db = getReadableDatabase();
		
		return db.query(PLAYS_TABLE_NAME, null, PLAYS_FIELD+"=?", new String[]{court}, null, null, PLAYS_DATE);
		
	}

}
