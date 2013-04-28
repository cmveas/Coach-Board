package com.sportcoachhelper.paths;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Path;

import com.sportcoachhelper.paths.interfaces.Dibujables;

public abstract class SerializablePath extends Path implements Serializable,Dibujables {

    private ArrayList<float[]> pathPoints;

    public SerializablePath() {
        super();
        pathPoints = new ArrayList<float[]>();
    }

    public SerializablePath(SerializablePath p) {
        super(p);
        pathPoints = p.pathPoints;
    }

    public void addPathPoints(float[] points) {
        this.pathPoints.add(points);
    }

    public void loadPathPointsAsQuadTo() {
        float[] initPoints = pathPoints.remove(0);
        this.moveTo(initPoints[0], initPoints[1]);
        for (float[] pointSet : pathPoints) {
            this.quadTo(pointSet[0], pointSet[1], pointSet[2], pointSet[3]);
        } 
    }
    
    public void resetPath(){
    	reset();
    }
}
