package com.sportcoachhelper.paths;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Path;

import com.sportcoachhelper.paths.interfaces.Dibujables;

public abstract class SerializablePath extends Path implements Serializable,
		Dibujables {

	private ArrayList<float[]> pathPoints;

	public SerializablePath() {
		super();
		pathPoints = new ArrayList<float[]>();
	}
	
	@Override
	public void lineTo(float x, float y) {
		super.lineTo(x, y);
	}
	
	@Override
	public void quadTo(float x1, float y1, float x2, float y2) {
		super.quadTo(x1, y1, x2, y2);
	}

	public SerializablePath(SerializablePath p) {
		super(p);
		pathPoints = p.pathPoints;
	}

	public void addPathPoints(float[] points) {
		this.pathPoints.add(points);
	}
	
	public void addCirclePath(float[] points) {
		this.pathPoints.add(points);
	}

	public void loadPathPointsAsQuadTo() {
		if (pathPoints.size() > 0) {
			float[] initPoints = pathPoints.remove(0);
			this.moveTo(initPoints[0], initPoints[1]);
			for (int i=0;i<pathPoints.size();i++) {
				float[] pointSet = pathPoints.get(i);
				this.quadTo(pointSet[0], pointSet[1], pointSet[2], pointSet[3]);
			}
		}
		
		
	}
	
	public void loadCirclePathPointsAsQuadTo(){
		if(pathPoints.size()>0) {
			float[] initPoints = pathPoints.get(0);
			addCircle(initPoints[0], initPoints[1], ShapePath.HALF_SIZE, Path.Direction.CCW);
		}
	}
	
	public void loadTrianglePathPointsAsQuadTo(){
		if(pathPoints.size()>0) {
			
			float[] initPoints = pathPoints.get(0);
			float x = initPoints[0];
			float y = initPoints[1];
			moveTo(x, y);
			lineTo(x-ColorPath.HALF_SIZE, y+ColorPath.SIZE);
			lineTo(x+ColorPath.HALF_SIZE, y+ColorPath.SIZE);
			lineTo(x, y);	
			addCirclePath(new float[]{x,y});
		}
	}
	
	
	
	public void loadSquarePathPointsAsQuadTo(){
		if(pathPoints.size()>0) {
			float[] initPoints = pathPoints.get(0);
			addRect(initPoints[0], initPoints[1], initPoints[2],initPoints[3], Path.Direction.CCW);
		}
	}
	
	

	public void resetPath() {
		reset();
	}
}


