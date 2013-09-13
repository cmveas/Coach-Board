package com.sportcoachhelper.paths;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.R;
import com.sportcoachhelper.util.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BallPath extends ColorPath {
    @Override
    public String getComponentType() {
        return "ball";
    }

    public static final String BALL = "ball";

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
		bitmap = BitmapFactory.decodeResource(CoachApp.getInstance().getApplicationContext().getResources(), getResourceFromType(ballType));
		bitmap = Utility.getResizedBitmap(bitmap, SIZE, SIZE);
	}

	public static int getResourceFromType(int ballType) {
		int result = R.drawable.soccerball;
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


    public static int getLayoutFromType(int ballType) {
        int result = 0;
        switch(ballType){
            case SOCCER_BALL:
                result = R.layout.soccerbalcomponent;
                break;
            case VOLLEYBALL:
                result = R.layout.voleyballcomponent;
                break;
            case BASKETBALL:
                result = R.layout.basketballcomponent;
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

    @Override
    public JSONArray toJsonData() {
        JSONObject itemType = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            itemType.put("balltype",ballType);
            array.put(itemType);

                JSONObject item = new JSONObject();
                item.put("X",x);
                item.put("Y",y);
                item.put("SIZE",SIZE);
                item.put("HALF_SIZE",HALF_SIZE);
                array.put(item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
	
}
