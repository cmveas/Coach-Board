package com.sportcoachhelper.interfaces;

import com.sportcoachhelper.paths.ColorPath;

public interface OnComponentSelectedListener {

	void onComponentSelected(ColorPath path);

    void onPlayerAdded();
	
	void onComponentRelease();
	
}
