package com.sportcoachhelper.paths;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.paths.interfaces.Dibujables;

public abstract class ColorPath extends SerializablePath{

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
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(this,paint);
	} 
	
	
}
