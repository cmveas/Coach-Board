package com.sportcoachhelper.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Template implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6927868019625017972L;
	public ArrayList<TemplateItem> templateItems = new ArrayList<TemplateItem>();
	private String field;
	private String name;

	public void add(TemplateItem item) {
		templateItems.add(item);

	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field=field;
		
	}

	public void setName(String name) {
		this.name=name;
		
	}

	public String getName() {
		return name;
	}

	public  ArrayList<TemplateItem> getPlayers() {
		// TODO Auto-generated method stub
		return templateItems;
	}

}
