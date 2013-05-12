package com.sportcoachhelper.paths;

import android.graphics.Paint;


public class TrianglePath extends ShapePath {

	public TrianglePath(Paint paint, int x, int y) {
		super(paint,x,y);
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		this.moveTo(x, y);
		this.lineTo(x-ColorPath.HALF_SIZE, y+ColorPath.SIZE);
		this.lineTo(x+ColorPath.HALF_SIZE, y+ColorPath.SIZE);
		this.lineTo(x, y);	
		this.addCirclePath(new float[]{x,y});
		
	}
	
	

}
