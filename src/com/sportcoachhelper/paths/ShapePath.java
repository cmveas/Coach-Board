package com.sportcoachhelper.paths;

import com.sportcoachhelper.util.TeamManager;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class ShapePath extends ColorPath {

	private int team = 1;
	
	public ShapePath(Paint paint, int x, int y) {
		super(paint);
		setX(x);
		setY(y);
		reinitialize();
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		checkPaint();
		super.draw(canvas);
	}
	
	
	private void checkPaint() {		
		if(paint==null || textPaint==null) {
			setPaint(TeamManager.getInstance().getPaintForTeam(team));
		}
		
	}

	@Override
	public void reinitialize() {
		reset();
		
	}
}
