package com.sportcoachhelper.paths;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.paths.interfaces.Movable;
import com.sportcoachhelper.util.Utility;

public class BallPath extends ColorPath implements Movable {

	private static final int SIZE = 35;
	private int resource;
	private int x;
	private int y;
	private Bitmap bitmap;

	public BallPath(Paint paint, int resource, Resources resources) {
		super(paint);
		this.resource = resource;
		bitmap = BitmapFactory.decodeResource(resources, resource);
		bitmap = Utility.getResizedBitmap(bitmap, SIZE, SIZE);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getResource(){
		return resource;
	}

	public void setX(int x) {
		this.x=x;
		
	}

	public void setY(int y) {
		this.y=y;
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap,x,y,getPaint());
	}

	@Override
	public boolean isItIn(float x, float y) {
		boolean result = false;
				
		if(x>this.x && x<this.x+SIZE) {
			result = true;
		}
		
		if(y>this.y && y<this.y+SIZE) {
			result =  result && true;
		}
		return result;
	}
}
