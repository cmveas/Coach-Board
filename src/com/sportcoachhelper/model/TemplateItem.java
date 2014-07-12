package com.sportcoachhelper.model;

import java.io.Serializable;

public class TemplateItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7079729288279589889L;
	public static final int SHAPE_CIRCLE = 0;
	public static final int SHAPE_SQUARE = 1;
	public static final int SHAPE_TRIANGLE = 2;
	
	
	private int xPercentage;
	
	private int yPercentage;
	
	private int shape;
	
	public int getxPercentage() {
		return xPercentage;
	}
	public void setxPercentage(int xPercentage) {
		this.xPercentage = xPercentage;
	}
	public int getyPercentage() {
		return yPercentage;
	}
	public void setyPercentage(int yPercentage) {
		this.yPercentage = yPercentage;
	}
	public int getShape() {
		return shape;
	}
	public void setShape(int shape) {
		this.shape = shape;
	}
	
}
