package com.sportcoachhelper.model;

import android.graphics.Paint;

public class Team {

	
	int number;
	
	Paint paint;

	public Team(int i) {
		number=i;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	
	
}
