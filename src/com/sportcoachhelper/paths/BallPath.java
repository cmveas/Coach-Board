package com.sportcoachhelper.paths;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.util.Utility;

public class BallPath extends ColorPath {


	private int resource;
	private transient Bitmap bitmap;

	public BallPath(Paint paint, int resource, Resources resources) {
		super(paint);
		this.resource = resource;
		init();
	}

	private void init() {
		bitmap = BitmapFactory.decodeResource(CoachApp.getInstance().getApplicationContext().getResources(), resource);
		bitmap = Utility.getResizedBitmap(bitmap, SIZE, SIZE);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getResource(){
		return resource;
	}


	@Override
	public void draw(Canvas canvas) {
		if(bitmap==null) {
			init();
		}
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
	
	@Override
	public boolean canBeMoved() {
		return true;
	}
	
}
