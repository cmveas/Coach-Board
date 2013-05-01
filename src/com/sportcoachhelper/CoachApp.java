package com.sportcoachhelper;

import android.app.Application;

public class CoachApp extends Application {

	private static CoachApp instance;
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
	}
	
	public static CoachApp getInstance(){
		return instance;
	}
	
}
