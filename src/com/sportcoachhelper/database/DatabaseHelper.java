package com.sportcoachhelper.database;

import com.sportcoachhelper.CoachApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	private static final String PLAYS_TABLE_NAME = "plays";
	private static final String PLAYS_ID = "_id";
	private static final String PLAYS_NAME = "name";
	private static final String PLAYS_DATE = "date";
	private static final String PLAYS_DATA = "data";
	
	private static final String CREATE_TABLE_PLAYS = "create table " + PLAYS_TABLE_NAME + " (" +
													 PLAYS_ID + " integer primary key," +
													 PLAYS_NAME + " text not null," +
													 PLAYS_DATA + " blob not null," + 
													 PLAYS_DATE + " integer);";
			                                         

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

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
