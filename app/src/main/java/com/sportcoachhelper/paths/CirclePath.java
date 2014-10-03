package com.sportcoachhelper.paths;

import android.graphics.Paint;
import android.graphics.Path;

public class CirclePath extends ShapePath {

    public static final String CIRCLE = "circle";

    public CirclePath(Paint paint, int x, int y) {
		super(paint,x,y);
	}
	
	



	@Override
	public void reinitialize() {
		super.reinitialize();
		addCircle(x, y, ColorPath.HALF_SIZE, Path.Direction.CCW);
		addShapePath(new float[]{x, y});
	}

    @Override
    public String getComponentType() {
        return CIRCLE;
    }


}