package com.sportcoachhelper;

import android.app.Application;

import com.sportcoachhelper.managers.StorageManager;

public class CoachApp extends Application {

	private static CoachApp instance;

    private StorageManager manager;
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();

        initialize();
	}

    private void initialize() {
        manager = StorageManager.getInstance();
    }

    public static CoachApp getInstance(){
		return instance;
	}

    public StorageManager getStorageManager() {
        return manager;
    }
	
}
