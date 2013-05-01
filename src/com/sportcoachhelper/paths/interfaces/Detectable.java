package com.sportcoachhelper.paths.interfaces;

public interface Detectable {

	boolean isItIn(float x, float y);
	
	void setX(int x);

	void setY(int y);
	
	boolean canBeMoved();
	
}
