package com.sportcoachhelper.paths;

import android.graphics.Paint;

public abstract class ColorPath extends SerializablePath {

	private Paint paint;

	public ColorPath(Paint paint){
		super();
		this.setPaint(new Paint(paint));
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	
}
