package com.sportcoachhelper.paths;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LinePath extends ColorPath {

	public LinePath(Paint paint) {
		super(paint);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(this, getPaint());
		
	}

}
