package com.sportcoachhelper.paths;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LinePath extends ColorPath {

	private static final String TAG = "LinePath";
	private static final boolean DEBUG = false ;
	private String lineMode;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;

	public String getLineMode() {
		return lineMode;
	}

	private float highestY=-1;
	private float lowestY=-1;
	private float highestX=-1;
	private float lowestX=-1;
	
	public LinePath(Paint paint) {
		super(paint);
		// TODO Auto-generated constructor stub
	}
	
	public boolean areBoundsInitialize(){
		return highestX!=-1&&highestY!=-1&&lowestX!=-1&&lowestY!=-1;
	}

	@Override
	public void addPathPoints(float[] points) {
		if(points.length==4) {
			float x1=points[0];
			float y1=points[1];
			float x2=points[2];
			float y2=points[3];
			float lowerX = (x1<x2?x1:x2);
			float lowerY = (y1<y2?y1:y2);
			float higherX = (x1>x2?x1:x2);
			float higherY = (y1>y2?y1:y2);
			
			if(lowestX==-1 ||lowerX<lowestX) {
				lowestX = lowerX;
			}
			
			if(lowestY==-1 ||lowerY<lowestY) {
				lowestY = lowerY;
			}
			
			if(higherY>highestY) {
				highestY = higherY;
			}
			
			if(higherX>highestX) {
				highestX = higherX;
			}
					
		}
		super.addPathPoints(points);
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(DEBUG && areBoundsInitialize()) {
			canvas.drawRect(lowestX, lowestY, highestX, highestY, selectedPaint);
		}
	}

	@Override
	public void reinitialize() {
		// TODO Auto-generated method stub
		
	}

	public void setLineMode(String lineMode) {
		this.lineMode=lineMode;
		
	}
	
	@Override
	public boolean isItIn(float x, float y) {
		boolean result = false;		
		
		if(!areBoundsInitialize()) {
			return false;
		}
		
		if(x>(lowestX) && x<highestX) {
			result = true;
		}
		
		
		if((y>this.lowestY) && (y<highestY)) {
			result =  result && true;
		} else {
			result =  false;
		}
		
		android.util.Log.d(TAG,"x=" +x + " Xinit = " + lowestX + " XFinal:" + highestX + " result:" + result);
		android.util.Log.d(TAG,"y=" +y + " Yinit = " + lowestY + " YFinal:" + highestY + " result:" + result);
		
		return result;
	}

    @Override
    public JSONArray toJsonData() {

        JSONArray array = new JSONArray();
        try {
       for(float [] points : pathPoints) {
            JSONObject item = new JSONObject();
            item.put("X",points[0]);
            item.put("Y",points[1]);
            item.put("XF",points[2]);
            item.put("YF",points[3]);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    @Override
	public boolean canBeMoved() {
		return false;
	}

    @Override
    public String getComponentType() {
        return "line";
    }

}
