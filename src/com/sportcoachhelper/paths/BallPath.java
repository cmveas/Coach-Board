package com.sportcoachhelper.paths;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.R;
import com.sportcoachhelper.util.Utility;

public class BallPath extends ColorPath {


	public static final int SOCCER_BALL = 0;
	public static final int VOLLEYBALL = 1;
	public static final int BASKETBALL = 2;
	

	private int ballType;
	private transient Bitmap bitmap;

	public BallPath(Paint paint, int resource, Resources resources) {
		super(paint);
		this.ballType = resource;
		init();
	}

	private void init() {
		bitmap = BitmapFactory.decodeResource(CoachApp.getInstance().getApplicationContext().getResources(), getResourceFromType());
		bitmap = Utility.getResizedBitmap(bitmap, SIZE, SIZE);
	}

	private int getResourceFromType() {
		int result = 0;
		switch(ballType){
		case SOCCER_BALL:
			result = R.drawable.soccerball;
			break;
		case VOLLEYBALL:
			result = R.drawable.volleyball;
			break;
		case BASKETBALL:
			result = R.drawable.basketball;
			break;
		}
		return result;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getBallType(){
		return ballType;
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

	@Override
	public void reinitialize() {
		// TODO Auto-generated method stub
		
	}
	
}
