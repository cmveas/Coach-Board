package com.sportcoachhelper.paths.interfaces;

import android.graphics.Canvas;
import org.json.JSONArray;

public interface Dibujables {

	void draw(Canvas canvas);
	
	void resetPath();

     String getComponentType();

    public JSONArray toJsonData();
}
