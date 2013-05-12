package com.sportcoachhelper.paths;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LinePath extends ColorPath {

	private String lineMode;

	public String getLineMode() {
		return lineMode;
	}

	public LinePath(Paint paint) {
		super(paint);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(this, getPaint());
		
	}

	@Override
	public void reinitialize() {
		// TODO Auto-generated method stub
		
	}

	public void setLineMode(String lineMode) {
		this.lineMode=lineMode;
		
	}

}
