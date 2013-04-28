package com.sportcoachhelper.paths;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import com.sportcoachhelper.util.Utility;

public class BallPath extends ColorPath {

	private int resource;
	private int x;
	private int y;
	private Bitmap bitmap;

	public BallPath(Paint paint, int resource, Resources resources) {
		super(paint);
		this.resource = resource;
		bitmap = BitmapFactory.decodeResource(resources, resource);
		bitmap = Utility.getResizedBitmap(bitmap, 35, 35);
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
	
	
}
