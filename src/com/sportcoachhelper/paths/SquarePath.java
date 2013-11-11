package com.sportcoachhelper.paths;

import android.graphics.Paint;
import android.graphics.Path;

public class SquarePath extends ShapePath {

    public static final String SQUARE = "square";

    public SquarePath(Paint paint, int x, int y) {
		super(paint,x,y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reinitialize() {
		super.reinitialize();
		addRect(x,y, x+ColorPath.SIZE, y+ColorPath.SIZE, Path.Direction.CCW);	
		addShapePath(new float[]{x, y, x + ColorPath.SIZE, y + ColorPath.SIZE});
		
	}

    @Override
    public String getComponentType() {
        return SQUARE;
    }

}
