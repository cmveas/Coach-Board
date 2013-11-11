package com.sportcoachhelper.paths;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.sportcoachhelper.paths.interfaces.Detectable;

public abstract class ColorPath extends SerializablePath implements Detectable {
	
	private static final boolean DEBUG = false ;
	public static final int SIZE = 30;
	public static final int HALF_SIZE = 15;
	private static final String TAG = "ColorPath";
	
	protected transient Paint paint;
	protected int x;
	protected int y;
	protected transient Paint textPaint;
	protected transient Paint selectedPaint;

	public ColorPath(Paint paint){
		super();
		this.setPaint(new Paint(paint));
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
		this.textPaint = new Paint(paint);
		this.selectedPaint = new Paint(paint);
		selectedPaint.setAlpha(0x7f);
		textPaint.setTextSize(36);
		textPaint.setColor(0xFFFFFFFF);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if(selected) {
		canvas.drawPath(this,selectedPaint);
		} else {
		canvas.drawPath(this,paint);	
		}
		if(label!=null && !label.trim().equals("")) {
			canvas.drawText(label, x-HALF_SIZE, y-HALF_SIZE, textPaint);
		}
		
		if(DEBUG) {
			canvas.drawRect(this.x-HALF_SIZE, this.y-HALF_SIZE, (this.x+SIZE), (this.y+SIZE), selectedPaint);
		}
	} 
	
	

	@Override
	public boolean canBeMoved() {
		return true;
	}
	
	@Override
	public void setX(int x) {
		this.x=x;	
	}
	
	@Override
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
	public boolean isItIn(float x, float y) {
		boolean result = false;
		
		
		
		if(x>(this.x-HALF_SIZE) && x<this.x+SIZE) {
			result = true;
		}
		
		
		if((y>this.y-HALF_SIZE) && (y<this.y+SIZE)) {
			result =  result && true;
		} else {
			result =  false;
		}
		
		android.util.Log.d(TAG,"x=" +x + " Xinit = " + (this.x-HALF_SIZE) + " XFinal:" + (this.x+SIZE) + " result:" + result);
		android.util.Log.d(TAG,"y=" +y + " Yinit = " + (this.y-HALF_SIZE) + " YFinal:" + (this.y+SIZE) + " result:" + result);
		
		return result;
	}
	

	private String label;
	private boolean selected;

	
	public void setLabel(String number) {
		label=number;
	}
	
	public String getLabel(){
		return label;
	}
	
	
	@Override
	public void setSelected(boolean b) {
		selected=b;
		
	}
	
	public boolean isSelected(){
		return selected;
    }

}
