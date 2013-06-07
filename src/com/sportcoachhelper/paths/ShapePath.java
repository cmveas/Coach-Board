package com.sportcoachhelper.paths;

import com.sportcoachhelper.model.TemplateItem;
import com.sportcoachhelper.managers.TeamManager;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ShapePath extends ColorPath {

	
	private int team = 1;
	private TemplateItem template;
	
	public ShapePath(Paint paint, int x, int y) {
		super(paint);
		setX(x);
		setY(y);
		template = new TemplateItem();
		reinitialize();
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		checkPaint();
		super.draw(canvas);
	}
	
	
	private void checkPaint() {		
		if(paint==null || textPaint==null) {
			setPaint(TeamManager.getInstance().getPaintForTeam(team));
		}
		
	}

	@Override
	public void reinitialize() {
		reset();
		
	}

    @Override
    public JSONArray toJsonData() {

        JSONArray array = new JSONArray();
        try {
            for(float [] points : pathPoints) {
                JSONObject item = new JSONObject();
                item.put("X",points[0]);
                item.put("Y",points[1]);
                item.put("Team",getTeam());
                item.put("SIZE",SIZE);
                item.put("HALF_SIZE",HALF_SIZE);
                array.put(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

}
