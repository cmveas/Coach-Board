package com.sportcoachhelper.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.sportcoachhelper.paths.ColorPath;
import com.sportcoachhelper.paths.interfaces.Detectable;
import com.sportcoachhelper.paths.interfaces.Dibujables;

public class Play implements Serializable {

    private long id=-1;
    private String field_type;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    private long lastSaved;
	private String name;
	private String field;
	private ArrayList<Dibujables> undoablePaths = new ArrayList<Dibujables>();
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Dibujables> getUndoablePaths() {
		return undoablePaths;
	}

	public void setUndoablePaths(ArrayList<Dibujables> undoablePaths) {
		this.undoablePaths = undoablePaths;
	}


	public void addPath(ColorPath mPath) {
		undoablePaths.add(mPath);		
	}

	public boolean removeLast() {
		boolean result = false;
		if (undoablePaths.size() > 0) {
			Dibujables removedPath = undoablePaths.remove(undoablePaths.size() - 1);
			removedPath.resetPath();		
			result=true;
		}
		return result;
	}

	public void removeAllMoves() {
		undoablePaths.removeAll(undoablePaths);
		
	}

	public boolean erasedSelected(Detectable movable) {
		boolean result = false;
		if(movable!=null && undoablePaths.contains(movable)) {
			undoablePaths.remove(movable);
			result = true;
		}
		return result;
	}

	public long getLastSaved() {
		return lastSaved;
	}

	public void setLastSaved(long lastSaved) {
		this.lastSaved = lastSaved;
	}

    public void loadFully(){

    }


    public void setFieldType(String field_type) {
        this.field_type=field_type;

    }

    public String getFieldType() {
        return field_type;
    }
}
